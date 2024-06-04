package edu.tongji.setest.utils;
import java.util.ArrayList;
import java.util.List;
/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 将读取的类型字符串, 转化为类型本身
 * @date 2024/6/2 12:38:01
 */


public class TypeConverter {
    public static List<Class<?>> convertToClassList(List<String> typeList) {
        List<Class<?>> classList = new ArrayList<>();

        for (String typeName : typeList) {
            Class<?> clazz = getClassForTypeName(typeName);
            if (clazz != null) {
                classList.add(clazz);
            }
        }

        return classList;
    }

    public static Class<?> convertToClass(String typeName) {
        return getClassForTypeName(typeName);
    }

    private static Class<?> getClassForTypeName(String typeName) {
        switch (typeName) {
            case "int":
                return int.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "char":
                return char.class;
            case "boolean":
                return boolean.class;
            case "String":
                return String.class;
            case "Integer":
                return Integer.class;
            default:
                try {
                    // 尝试获取指定名称的类
                    return Class.forName(typeName);
                } catch (ClassNotFoundException e) {
                    // 如果找不到对应的类，则返回null
                    System.err.println("Class not found for type name: " + typeName);
                    return null;
                }
        }
    }

    /**
     * @Author asahi
     * @Description 测试该部分代码用, 无实际意义
     * @Date 下午1:21 2024/6/2
     * @Param
     * @param args
     * @return
     **/
    public static void main(String[] args) {
        List<String> typeList = new ArrayList<>();
        typeList.add("int");
        typeList.add("String");
        typeList.add("int");

        List<Class<?>> classList = convertToClassList(typeList);
        System.out.println("Converted class list: " + classList);
    }
}

