import java.io.IOException;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 主程序
 * @date 2024/6/2 12:11:57
 */
public class Application {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 检查是否提供了文件路径作为命令行参数
        if (args.length != 1) {
            System.err.println("Usage: java Application <file_path>");
            System.exit(1);
        }

        // 获取文件路径
        String filePath = args[0];

        // 创建 TestCaseExecutor 对象并执行
        TestCaseExecutor executor = new TestCaseExecutor(filePath);
        executor.execute();
        executor.print();
    }
}