package com.sunjinwei;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


/**
 * instrument是jvm提供的一个可以修改已加载类的类库，专门为java语言编写的插桩服务提供支持。
 * jdk6之前：instrument只对jvm刚启动加载类时生效
 * jdk6之后：支持在运行时对类定义进行修改
 * 要实现instrument的类修改功能，需要实现它提供的ClassFileTransformer接口，定义一个类文件转换器，重写的transform方法会在类文件被加载时调用，
 * 而在transform方法内部 可以利用ASM或者Javassist 对传入的字节码进行改写和替换，生成新的字节码文件进行返回。
 *
 * 步骤：
 * 1。定义一个类文件转换器
 * 2。注入到正在运行的jvm【定义一个agent】
 * 3。如果jvm启动时开启了JPDA 那么类才是允许被重新加载的，在这种情况下 已经被加载的旧版本类信息可以被卸载，然后重新加载新版本的类
 *
 * https://segmentfault.com/a/1190000020345321?utm_source=tag-newest
 * https://www.cnblogs.com/zhenbianshu/p/10210597.html
 */
public class MyTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        System.out.println("Transforming:   " + className);
        ClassPool classPool = ClassPool.getDefault();
        try {

            CtClass ctClass = classPool.get("com.sunjinwei.Base");
            CtMethod method = ctClass.getDeclaredMethod("proceed");

            method.insertBefore("{System.out.println(\"insert before\");}");
            method.insertAfter("{System.out.println(\"insert after\");}");

            return ctClass.toBytecode();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
