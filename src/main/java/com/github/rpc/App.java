package com.github.rpc;


import com.github.rpc.client.Test;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NoSuchMethodException {

        System.out.println(Test.class.getMethod("sayHello",new Class<?>[]{String.class})==Test.class.getMethod("sayHello",new Class<?>[]{String.class}));
    }
}
