# mockmvc

> 基于RESTful风格的SpringMVC的测试，可以测试完整的Spring MVC流程，即从URL请求到控制器处理，再到视图渲染都可以测试。


## MockMvcBuilder
   
> MockMvcBuilder是用来构造MockMvc的构造器，其主要有两个实现：StandaloneMockMvcBuilder和DefaultMockMvcBuilder，分别对应两种测试方式，
即独立安装和集成Web环境测试（此种方式并不会集成真正的web环境，而是通过相应的Mock API进行模拟测试，无须启动服务器）。对于我们来说直接使用静态工厂MockMvcBuilders创建即可。


1. 集成Web环境方式

MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc。

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/IncotermsRestServiceTest-context.xml")
@WebAppConfiguration
public class IncotermsRestServiceTest {

    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();   //构造MockMvc
    }
    
    ...
    
}

(1)@WebAppConfiguration：测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的value指定web应用的根；
(2)通过@Autowired WebApplicationContext wac：注入web环境的ApplicationContext容器；
(3)然后通过MockMvcBuilders.webAppContextSetup(wac).build()创建一个MockMvc进行测试；

```

2. 独立测试方式
   
MockMvcBuilders.standaloneSetup(Object... controllers)：通过参数指定一组控制器，这样就不需要从上下文获取了；
   
```
public class PricingExportResultsRestServiceTest {

    @InjectMocks
    private PricingExportResultsRestService pricingExportResultsRestService;
    @Mock
    private ExportRateScheduleService exportRateScheduleService;
    @Mock
    private PricingUrlProvider pricingUrlProvider;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pricingExportResultsRestService).build();  //构造MockMvc
    }
    
    ...
    
}

(1)首先自己创建相应的控制器，注入相应的依赖
(2)通过MockMvcBuilders.standaloneSetup模拟一个Mvc测试环境，通过build得到一个MockMvc

```


## MockMvc

```
整个测试过程非常有规律：
1、准备测试环境
2、通过MockMvc执行请求
3、添加验证断言
    3.1、添加结果处理器
    3.2、得到MvcResult进行自定义断言/进行下一步的异步请求
4、卸载测试环境
```


* 例子1

```
@Test
public void createIncotermSuccess() throws Exception {
    IncotermTo createdIncoterm = new IncotermTo();
    createdIncoterm.setId(new IncotermId(UUID.fromString("6305ff33-295e-11e5-ae37-54ee7534021a")));
    createdIncoterm.setCode("EXW");
    createdIncoterm.setDescription("code exw");
    createdIncoterm.setLocationQualifier(LocationQualifier.DEPARTURE);

    when(inventoryService.create(any(IncotermTo.class))).thenReturn(createdIncoterm);

    mockMvc.perform(post("/secured/resources/incoterms/create").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
            .content("{\"code\" : \"EXW\", \"description\" : \"code exw\", \"locationQualifier\" : \"DEPARTURE\"}".getBytes()))
            //.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id.value").exists())
            .andExpect(jsonPath("id.value").value("6305ff33-295e-11e5-ae37-54ee7534021a"))
            .andExpect(jsonPath("code").value("EXW"));
}

perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；
andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台；
andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；

```


* 例子2

```
@Test  
public void testView() throws Exception {  
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))  
            .andExpect(MockMvcResultMatchers.view().name("user/view"))  
            .andExpect(MockMvcResultMatchers.model().attributeExists("user"))  
            .andDo(MockMvcResultHandlers.print())  
            .andReturn();  
      
    Assert.assertNotNull(result.getModelAndView().getModel().get("user"));  
}
    
整个过程：
1、mockMvc.perform执行一个请求；
2、MockMvcRequestBuilders.get("/user/1")构造一个请求
3、ResultActions.andExpect添加执行完成后的断言
4、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
5、ResultActions.andReturn表示执行完成后返回相应的结果。
```


## Reference

* [https://www.jianshu.com/p/91045b0415f0](https://www.jianshu.com/p/91045b0415f0)

