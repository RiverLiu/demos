package com.riverlcn.proxy;

/**
 * @author river
 */
public class Hello implements HelloInterface {

    @Override
    public void sayHello() {
        System.out.println("Hello");
    }
}
