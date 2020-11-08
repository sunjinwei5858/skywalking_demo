package com.sunjinwei;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * 配置agent：借助agent注入到jvm中去
 * 用到另一个类：Instrumentation
 * 在Instrumentation中添加自定义的转换器，并指定要重新加载的类，这样当Agent被Attach到一个jvm时，就会执行类字节码替换并重新加载到jvm中
 */
public class MyAgent {

    public static void premain(String agentParam, Instrumentation instrumentation) {
        System.out.println("agent begin....");
        //指定自己定义的转换器 利用javaassit进行替换字节码
        instrumentation.addTransformer(new MyTransformer(), true);

        try {
            // 重新定义类 并加载
            instrumentation.retransformClasses(Base.class);
            System.out.println("agent load done");
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }


    }


}
