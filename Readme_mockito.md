# mockito_demo


## 思想

Mockito并不是创建一个真实的对象，而是模拟这个对象，用简单的when(mock.method(params)).thenReturn(result)语句设置mock对象的行为，如下语句：

```
// 设置mock对象的行为 － 当调用其get方法获取第0个元素时，返回"first"
Mockito.when(mockedList.get(0)).thenReturn("first");
```

在Mock对象的时候，创建一个proxy对象，保存被调用的方法名（get），以及调用时候传递的参数（0），然后在调用thenReturn方法时再把“first”保存起来，这样，就有了构建一个stub方法所需的所有信息，构建一个stub。当get方法被调用的时候，实际上调用的是之前保存的proxy对象的get方法，返回之前保存的数据。


## 环境

* `pom.xml`中加入下列包依赖

```
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-all</artifactId>
    <version>1.9.5</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
</dependency>
        
```


* 在`main/test`下创建测试类

* 其他正常即可


## Reference

