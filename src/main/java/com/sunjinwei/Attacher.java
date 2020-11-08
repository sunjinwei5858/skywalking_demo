package com.sunjinwei;

/**
 * 使用 tools.jar 里方法将 agent 动态加载到目标 JVM 的类。
 */
public class Attacher {
    public static void main(String[] args) throws Exception {


        Base base = new Base();
        base.proceed();
    }
}
