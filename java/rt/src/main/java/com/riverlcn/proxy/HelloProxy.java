package com.riverlcn.proxy;

/**
 * 静态代理的例子.
 * 代理的目的，在调用具体方法前，可以预处理或者后处理一些信息，对处理方法进行条件过滤等操作.
 *
 * @author river
 */
public class HelloProxy implements HelloInterface {

    protected HelloInterface hello = new Hello();

    @Override
    public void sayHello() {
        System.out.println("Before say hello");
        hello.sayHello();
        System.out.println("After say hello");
    }

    public static void main(String[] args) {
        HelloProxy helloProxy = new HelloProxy();
        helloProxy.sayHello();
    }

}
