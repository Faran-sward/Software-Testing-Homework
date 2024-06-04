package edu.tongji.setest.utils.testCase;

import lombok.Getter;
import edu.tongji.setest.utils.DataConverter;
import edu.tongji.setest.utils.ExcelData;
import edu.tongji.setest.utils.TypeConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static edu.tongji.setest.utils.DataConverter.convertToMethodData;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 存储从Excel表中转换来的测试用例信息
 * @date 2024/6/2 13:37:29
 */
@Getter
public class TestCase {
//    private final String methodName;
//    private final String className;
    private final List<String> paramsTypeName;
    private final String resultTypeName;
    private final Class<?>[] parameterTypes;
    private final Class<?> resultType;
    private final List<DataConverter.MethodData> DataList;

    public TestCase (String filePath) throws IOException {
        ExcelData excelData = new ExcelData(filePath);

//        // 读取待测试对象名称
//        List<String> titleRow = excelData.getTitleRow();
//        if (titleRow.size() > 1) {
//            methodName = titleRow.get(1);
//        }
//        else{
//            methodName = titleRow.get(0);
//        }
//        className = titleRow.get(0);

        // 读取类型
        List<List<String>> headers = excelData.getHeaders();
        List<String> lastRow = headers.get(headers.size() - 1);
        paramsTypeName = lastRow.subList(0, lastRow.size() - 2);
        resultTypeName = lastRow.get(lastRow.size() - 2);

        // 类型转换
        parameterTypes = TypeConverter.convertToClassList(paramsTypeName).toArray(new Class[0]);
        resultType = TypeConverter.convertToClass(resultTypeName);

        // 数据类型转换
        List<Map<String, Object>> data = excelData.getData();
        List<String> DataMap = excelData.getDataMap();
        DataList = convertToMethodData(data, DataMap, parameterTypes, resultType);
    }

    public TestCase (InputStream file) throws Exception {
        ExcelData excelData = new ExcelData(file);

        // 读取类型
        List<List<String>> headers = excelData.getHeaders();
        List<String> lastRow = headers.get(headers.size() - 1);
        paramsTypeName = lastRow.subList(0, lastRow.size() - 2);
        resultTypeName = lastRow.get(lastRow.size() - 2);

        // 类型转换
        parameterTypes = TypeConverter.convertToClassList(paramsTypeName).toArray(new Class[0]);
        resultType = TypeConverter.convertToClass(resultTypeName);

        // 数据类型转换
        List<Map<String, Object>> data = excelData.getData();
        List<String> DataMap = excelData.getDataMap();
        DataList = convertToMethodData(data, DataMap, parameterTypes, resultType);
    }

    public void print () {
//        System.out.println("Class Name:");
//        System.out.println(className);
//        System.out.println("Method Name:");
//        System.out.println(methodName);

        System.out.println("Parameter Types:");
        System.out.println(paramsTypeName);
        System.out.println("Result Type:");
        System.out.println(resultTypeName);

        // 打印转换后的数据
        for (DataConverter.MethodData methodData : DataList) {
            System.out.println("Parameters: " + Arrays.toString(methodData.getParameters()));
            System.out.println("Result: " + methodData.getResult());
            System.out.println();
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
    public static void main(String[] args) throws IOException {
        String filePath = "/home/asahi/project/Java/Software-Testing-Homework/cases/ex1.xlsx";

        TestCase testCase = new TestCase(filePath);

        testCase.print();
    }
}
