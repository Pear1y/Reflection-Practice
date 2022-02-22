package org.example.Reflection.Field;

import org.example.Reflection.Example;
import org.example.Reflection.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 反射修改 final 修饰的变量：
 *
 * 使用 Java 反射，通过 Field#setAccessible(true) 将 private 修饰的字段变为 accessible；再将 final 修饰符去掉；最后再设置新值即可
 *
 * Referer: https://github.dev/GoldArowana/K-Object
 *
 * */

public class Final_Test {

    private static void modify_int_field() throws Exception {
        // 目标: private final static Integer num = 100;
        // 修改前
        Example.printNum1();  // 100

        Class<?> clazz = Class.forName("org.example.Reflection.Example");
        Field numField = clazz.getDeclaredField("num1");
        numField.setAccessible(true);

        // 获取实例变量的 modifier, 去掉 final 修饰符
        Field modifers = numField.getClass().getDeclaredField("modifiers");
        modifers.setAccessible(true);
        modifers.set(numField, numField.getModifiers() & ~Modifier.FINAL);

        // 重新赋值
        numField.set(clazz, 200);

        // 修改后
        Example.printNum1();  // 200


        // 使用 springframework 的 ReflectionUtils, 仅依赖 springframework 核心模块, 较纯净，封装好的用着更方便;

        // 修改前
        Example.printNum2();  // 100

        Field numfield = ReflectionUtils.findField(Example.class, "num2");
        ReflectionUtils.makeAccessible(numfield);

        // 获取实例变量的 modifier, 去掉 final 修饰符
        Field modifiers = ReflectionUtils.findField(Field.class, "modifiers");
        ReflectionUtils.makeAccessible(modifiers);
        ReflectionUtils.setField(modifiers, numfield, numfield.getModifiers() & ~Modifier.FINAL);

        // 重新赋值
        ReflectionUtils.setField(numfield, null, 300);

        // 修改后
        Example.printNum2();  // 300
    }

    private static void modify_String_field_1() throws Exception{
        // 目标: private final static StringBuilder sbstring = new StringBuilder("sbstring");
        // 修改前
        Example.printsbstring();
        Example clazz = new Example();
        Field nameField = clazz.getClass().getDeclaredField("sbstring");
        nameField.setAccessible(true);

        // 获取实例变量的 modifier, 去掉 final 修饰符
        Field modifers = nameField.getClass().getDeclaredField("modifiers");
        modifers.setAccessible(true);
        modifers.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

//         重新赋值
        nameField.set(clazz, new StringBuilder("Tom"));
        modifers.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

        // 修改后
        Example.printsbstring();
    }

    private static void modify_String_field_2() throws Exception{
        // 目标: private final static String name = "Pear1y";
        // 修改前
        Example.printName();

        Example clazz = new Example();
        Field nameField = clazz.getClass().getDeclaredField("name");
        nameField.setAccessible(true);

        // 获取实例变量的 modifier, 去掉 final 修饰符
        Field modifers = nameField.getClass().getDeclaredField("modifiers");
        modifers.setAccessible(true);
        modifers.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

        // 重新赋值
        nameField.set(clazz, "Tom");

        // 修改后
        Example.printName();

        // 获取修改后的 name 值, 需要反射重新获取 name
        Object name = nameField.get(clazz);
        System.out.println(name);
    }

    public static void main(String[] args) throws Exception {
        modify_int_field();
        modify_String_field_1();
        modify_String_field_2();
    }
}

