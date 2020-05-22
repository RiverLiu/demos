package com.riverlcn.spring.bean;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author river
 */
public class BeanFactoryTest {

    @Test
    public void test() {
        Resource rs = new ClassPathResource("spring-test.xml");
        BeanFactory bf = new XmlBeanFactory(rs);
        ExampleBean eb = (ExampleBean)bf.getBean("myTestBean");
        System.out.println(eb.foo());
    }

    @Test
    public void test2() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
		ExampleBean eb = (ExampleBean)context.getBean("myTestBean");
		System.out.println(eb.foo());
	}

}
