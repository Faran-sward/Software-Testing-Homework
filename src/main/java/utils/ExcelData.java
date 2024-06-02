package utils;

import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.ExcelReader.readData;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 存储从ExcelReader中得到的数据, 并按照类型转换
 * @date 2024/6/2 13:18:34
 */

@Getter
public class ExcelData {
    private final List<String> titleRow;

    private final List<List<String>> headers;

    private final List<Map<String, Object>> data;

    private final List<String> DataMap;

    public ExcelData(String filePath) throws IOException {
        int headRows = 2;

        titleRow = ExcelReader.readTitleRow(filePath);
        headers = ExcelReader.readHeaders(filePath, headRows);
        data = ExcelReader.readData(filePath, headRows);

        List<String> tempMap = new ArrayList<>();
        for (int i = 0; i < headers.get(0).size(); i++) {
            String map = headers.get(headers.size() - 2).get(i) + "_" + headers.get(headers.size() - 1).get(i);
            tempMap.add(map);
        }
        DataMap = tempMap;
    }

    public void print() {
        // 读取标题行内容
        System.out.println("Title Row:");
        System.out.println(titleRow);

        // 读取头行内容
        System.out.println("Headers:");
        for (List<String> headerRow : headers) {
            System.out.println(headerRow);
        }

        // 读取数据内容
        System.out.println("Data:");
        for (Map<String, Object> row : data) {
            System.out.println(row);
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
        String filePath = "/home/asahi/project/Java/Software-Testing-Homework/src/main/java/test/cases/ex1.xlsx";

        ExcelData excelData = new ExcelData(filePath);

        excelData.print();
    }
}
