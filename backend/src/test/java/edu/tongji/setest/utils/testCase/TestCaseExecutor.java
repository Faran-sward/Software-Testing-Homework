package edu.tongji.setest.utils.testCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tongji.setest.utils.DataConverter;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 待测试的类
 * @date 2024/6/2 14:15:55
 */
@Getter
public class TestCaseExecutor {
    private final Class<?> clazz;
//    private Map<String, List<String>> methodParameterMap;
    private final TestCase testCase;
    public static final String clazzRootPath = "testObjects.";
    private final List<String> actualOutputs = new ArrayList<>();
    private final List<String> expectedOutputs = new ArrayList<>();


    public TestCaseExecutor (String caseFilePath, String className) throws IOException, ClassNotFoundException {
        testCase = new TestCase(caseFilePath);

        String clazzPath = clazzRootPath + className;
        clazz = findClassByName(clazzPath);
//        findMethodParameters(clazz);
    }

    public TestCaseExecutor (InputStream caseFile, String className) throws Exception, ClassNotFoundException {
        testCase = new TestCase(caseFile);

        String clazzPath = clazzRootPath + className;
        clazz = findClassByName(clazzPath);
    }

    public String execute(String methodName) throws JsonProcessingException {
        if (!hasMethod(methodName)){
            System.out.println("this class does not have this method.");
            return null;
        }
        // 获取方法名称和参数类型
//        String methodName = testCase.getMethodName();
        List<Class<?>> parameterTypes = List.of(testCase.getParameterTypes());

        // 获取待测试的数据列表
        List<DataConverter.MethodData> dataList = testCase.getDataList();

        // 存储比较结果
        List<Boolean> comparisonResults = new ArrayList<>();
        String jsonString = "";

        // 遍历测试数据
        for (DataConverter.MethodData testData : dataList) {
            try {
                // 获取测试数据的参数
                Object[] parameters = testData.getParameters();

                // 调用方法
                Object actualResult = clazz.getMethod(methodName, parameterTypes.toArray(new Class<?>[0]))
                        .invoke(null, parameters);
                actualOutputs.add(actualResult.toString());
                expectedOutputs.add(testData.getResult().toString());
                // 比较实际结果和期望结果
                boolean resultMatch = actualResult.equals(testData.getResult());
                comparisonResults.add(resultMatch);
            } catch (NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
                comparisonResults.add(false); // 如果出现异常，则将比较结果设置为 false
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("comparisonResults", comparisonResults);
            resultMap.put("actualOutputs", actualOutputs);
            resultMap.put("expectedOutputs", expectedOutputs);

            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(resultMap);
        }
        return jsonString;
    }

    public static List<String> getMethods(String className) throws ClassNotFoundException {
        String clazzPath = clazzRootPath + className;
        Class<?> tem_clazz = findClassByName(clazzPath);
        Map<String, List<String>> methodParameterMap = findMethodParameters(tem_clazz);
        // 打印结果
        for (Map.Entry<String, List<String>> entry : methodParameterMap.entrySet()) {
            System.out.println("Method: " + entry.getKey());
            for (String param : entry.getValue()) {
                System.out.println("    Parameter in this method: " + param);
            }
        }
        return methodParameterMap.keySet().stream().toList();
    }

    public static List<String> getClasses() {
        String packageName = clazzRootPath;
        ArrayList<String> classNames = new ArrayList<>();
        String relPath = packageName.replace('.', '/');
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        if (resource == null) {
            throw new RuntimeException("Unexpected problem: No resource for " + relPath);
        }
        File directory = new File(resource.getPath());
        for (File file : directory.listFiles()) {
            String fileName = file.getName();
            String className = null;
            if (fileName.endsWith(".class")) {
                className = fileName.substring(0, fileName.length() - 6);
            }
            if (className != null && !className.contains("$")) {
                classNames.add(className);
            }
        }
        return classNames;
    }

    public void print () {

        testCase.print();
    }

    public static void printResult(List<Boolean> comparisonResults) {
        // 打印比较结果
        System.out.println("Comparison results:");
        for (int i = 0; i < comparisonResults.size(); i++) {
            System.out.println("Test case " + (i + 1) + ": " + comparisonResults.get(i));
        }
    }

    private static Map<String, List<String>> findMethodParameters(Class<?> clazz) {
        // 创建一个字典来存储方法和参数列表的映射
        Map<String, List<String>> methodParameterMap = new HashMap<>();

        // 获取类中的所有方法
        Method[] methods = clazz.getDeclaredMethods();

        // 遍历所有方法
        for (Method method : methods) {
            // 获取方法的参数
            Parameter[] parameters = method.getParameters();

            // 创建一个列表来存储参数类型
            List<String> parameterList = new ArrayList<>();
            for (Parameter parameter : parameters) {
                parameterList.add(parameter.getType().getName() + " " + parameter.getName());
            }

            // 将方法名和参数列表存储到字典中
            methodParameterMap.put(method.getName(), parameterList);
        }
        return methodParameterMap;
    }

    private static Class<?> findClassByName(String clazzPath) throws ClassNotFoundException {
        // 使用 Class.forName 方法根据类名查找类
        return Class.forName(clazzPath);
    }

    public static boolean classIsValid(String className) {
        try {
            // 使用 Class.forName 方法根据类名查找类
            String clazzPath = clazzRootPath + className;
            System.out.println(clazzPath);
            Class.forName(clazzPath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    private boolean hasMethod(String methodName) {
        try {
            // 获取指定方法的Method对象
//            String methodName = testCase.getMethodName();
            List<Class<?>> parameterTypes = List.of(testCase.getParameterTypes());
            Method method = clazz.getMethod(methodName, parameterTypes.toArray(new Class<?>[0]));
            return method != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        String filePath = "/home/asahi/project/Java/Software-Testing-Homework/cases/ex1.xlsx";
//        TestCaseExecutor executor = new TestCaseExecutor(filePath, "Triangle");
//        System.out.println(executor.getMethods("Triangle"));
//        executor.print();
//        List<Boolean> result = executor.execute("getTriangleType");
//        TestCaseExecutor.printResult(result);
//    }
}
