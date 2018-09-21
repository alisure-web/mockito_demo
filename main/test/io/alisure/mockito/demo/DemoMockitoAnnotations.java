package io.alisure.mockito.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * 为了避免重复的mock，是测试类更具有可读性，可以使用下面的注解方式来快速模拟对象
 */

// 1.
/*
public class DemoMockitoAnnotations {
    @Mock
    private List mockList;

    public DemoMockitoAnnotations(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }
}
*/

// 2.
@RunWith(MockitoJUnitRunner.class)
public class DemoMockitoAnnotations {
    @Mock
    private List mockList;

    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }
}