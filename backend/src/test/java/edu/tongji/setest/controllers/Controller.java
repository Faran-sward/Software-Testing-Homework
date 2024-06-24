package edu.tongji.setest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.tongji.setest.services.Services;
import edu.tongji.setest.utils.testCase.TestCaseExecutor;
import edu.tongji.setest.utils.testRunner.TestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 测试脚本, 测试用例上传, 运行测试脚本
 * @date 2024/6/4 09:02:05
 */
@RestController
@RequestMapping("/api")
public class Controller {
    private static final String UPLOAD_DIR = String.valueOf(Paths.get(System.getProperty("user.dir") ,
            "src", "main", "java",
            TestCaseExecutor.clazzRootPath.replace('.', '/')));
    @Autowired
    private Services services;


//    @PostMapping("/files/uploadScript")
//    public ResponseEntity<String> uploadScript(@RequestParam("file") MultipartFile file) throws Exception {
//        if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
//        }
//
////        System.out.println(UPLOAD_DIR);
//        int index = TestCaseExecutor.clazzRootPath.lastIndexOf(".");
//        InputStream infile = ScriptPackageManager.packageCheck(file, TestCaseExecutor.clazzRootPath.substring(0, index));
//
//        try {
//            // Create the upload directory if it doesn't exist
//            File directory = new File(UPLOAD_DIR);
//            if (!directory.exists()) {
//                directory.mkdirs();
//                System.out.println("dir created.");
//            }
//
//            // Get the file's original name and save it to the upload directory
//            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
//            Files.copy(infile, filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            // Create a file download URI
//            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/api/files/download/")
//                    .path(file.getOriginalFilename())
//                    .toUriString();
//
//            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + fileDownloadUri);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
//        } finally {
//            // Close the InputStream
//            try {
//                infile.close();
//            } catch (IOException e) {
//                // Handle close exception if necessary
//            }
//        }
//
//    }

    @GetMapping("/files/download/{filename:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            byte[] fileData = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/methods")
    public ResponseEntity<List<String>> getMethods(String className) throws ClassNotFoundException {
        // 返回一个字符串列表
        return ResponseEntity.ok(services.getMethods(className));
    }

    @GetMapping("/classes")
    public ResponseEntity<List<String>> getClasses() {
        // 返回一个字符串列表
        return ResponseEntity.ok(services.getClasses());
    }

    @GetMapping("/uploadClassName")
    public ResponseEntity<Boolean> chooseClass(@RequestParam String input) {
        if (input == null || input.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean result = services.chooseClass(input);
        if (!result) {
            System.out.println("类" + input + "不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/uploadCase")
    public ResponseEntity<Boolean> uploadTestCase(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            services.uploadCase(inputStream);
            return ResponseEntity.ok(true); // 上传成功
        } catch (Exception e) {
            // 上传失败，返回错误消息和 500 状态码
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/execute")
    public ResponseEntity<String> executeCase(@RequestParam String methodName) throws JsonProcessingException {
        String executionResult = services.executeCase(methodName);

        return ResponseEntity.ok(executionResult);
    }

    @GetMapping("/default")
    public ResponseEntity<String> executeDefaultCase(@RequestParam String className, int method) throws Exception {
        String executionResult = "new ArrayList<>();";
        if (Objects.equals(className, "三角形")) {
            services.chooseClass("Triangle");
            System.out.println("class :" + className);
            services.uploadCase(String.format("../cases/Triangle/%d.xlsx", method));
            System.out.println("method :" + method);
            executionResult = services.executeCase("getTriangleType");
        }
        else if (Objects.equals(className, "万年历")) {
            services.chooseClass("NextDayCalculator");
            System.out.println("class :" + className);
            services.uploadCase(String.format("../cases/NextDayCalculator/%d.xlsx", method));
            System.out.println("method :" + method);
            executionResult = services.executeCase("getNextDay");
        }
        else if (Objects.equals(className, "电脑销售系统")) {
            services.chooseClass("SalesSystem");
            System.out.println("class :" + className);
            services.uploadCase(String.format("../cases/SalesSystem/%d.xlsx", method));
            System.out.println("method :" + method);
            executionResult = services.executeCase("calculateSales");
        }
        else if (Objects.equals(className, "电信收费")) {
            services.chooseClass("TelecomBillingSystem");
            System.out.println("class :" + className);
            services.uploadCase(String.format("../cases/TelecomBillingSystem/%d.xlsx", method));
            System.out.println("method :" + method);
            executionResult = services.executeCase("calculateMonthlyBill");
        }
        else if (Objects.equals(className, "销售系统")) {
            services.chooseClass("SalesCommissionSystem");
            System.out.println("class :" + className);
            services.uploadCase(String.format("../cases/SalesCommissionSystem/%d.xlsx", method));
            System.out.println("method :" + method);
            executionResult = services.executeCase("calculateCommission");
        }

        return ResponseEntity.ok(executionResult);
    }

    @GetMapping("/unit-test")
    public ResponseEntity<String> unitTest() throws JsonProcessingException {
        String executionResult = TestRunner.runAllTests("cn.tju.sse.spring_backend");
        return ResponseEntity.ok(executionResult);
    }
}

