import utils.DataConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
public class TestCaseExecutor {
    private final Class<?> clazz;
    private Map<String, List<String>> methodParameterMap;
    private final TestCase testCase;

    private List<Boolean> comparisonResults;

    public TestCaseExecutor (String filePath) throws IOException, ClassNotFoundException {
        testCase = new TestCase(filePath);
        clazz = findClassByName(testCase.getClassName());
        findMethodParameters(clazz);
    }

    public void execute() {
        if (!hasMethod()){
            System.out.println("this class does not have this method.");
            return;
        }
        // 获取方法名称和参数类型
        String methodName = testCase.getMethodName();
        List<Class<?>> parameterTypes = List.of(testCase.getParameterTypes());

        // 获取待测试的数据列表
        List<DataConverter.MethodData> dataList = testCase.getDataList();

        // 存储比较结果
        comparisonResults = new ArrayList<>();

        // 遍历测试数据
        for (DataConverter.MethodData testData : dataList) {
            try {
                // 获取测试数据的参数
                Object[] parameters = testData.getParameters();

                // 调用方法
                Object actualResult = clazz.getMethod(methodName, parameterTypes.toArray(new Class<?>[0]))
                        .invoke(null, parameters);

                // 比较实际结果和期望结果
                boolean resultMatch = actualResult.equals(testData.getResult());
                comparisonResults.add(resultMatch);
            } catch (NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
                comparisonResults.add(false); // 如果出现异常，则将比较结果设置为 false
            }
        }
    }

    public void print () {
        // 打印结果
        for (Map.Entry<String, List<String>> entry : methodParameterMap.entrySet()) {
            System.out.println("Method: " + entry.getKey());
            for (String param : entry.getValue()) {
                System.out.println("    Parameter in this method: " + param);
            }
        }
        testCase.print();

        // 打印比较结果
        System.out.println("Comparison results:");
        for (int i = 0; i < comparisonResults.size(); i++) {
            System.out.println("Test case " + (i + 1) + ": " + comparisonResults.get(i));
        }
    }

    // 暂时没用
    private void findMethodParameters(Class<?> clazz) {
        // 创建一个字典来存储方法和参数列表的映射
        methodParameterMap = new HashMap<>();

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
    }

    public static Class<?> findClassByName(String className) throws ClassNotFoundException {
        // 使用 Class.forName 方法根据类名查找类
        return Class.forName(className);
    }

    private boolean hasMethod() {
        try {
            // 获取指定方法的Method对象
            String methodName = testCase.getMethodName();
            List<Class<?>> parameterTypes = List.of(testCase.getParameterTypes());
            Method method = clazz.getMethod(methodName, parameterTypes.toArray(new Class<?>[0]));
            return method != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String filePath = "/home/asahi/project/Java/Software-Testing-Homework/src/main/java/test/cases/ex1.xlsx";
        TestCaseExecutor executor = new TestCaseExecutor(filePath);
        executor.execute();
        executor.print();

    }
}
