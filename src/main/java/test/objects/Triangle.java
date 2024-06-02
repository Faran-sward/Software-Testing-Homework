package test.objects;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 判断三角形类型
 * @date 2024/6/2 11:12:20
 */
public class Triangle {
    // 枚举类型定义三角形的类别
    public enum TriangleCategory {
        NOT_A_TRIANGLE,
        SCALENE,
        ISOSCELES,
        EQUILATERAL
    }

    // 判断三角形类型的函数
    public static int getTriangleType(int a, int b, int c) {
        // 判断边界
        if (a <= 0 || a > 100 || b <= 0 || b > 100 || c <= 0 || c > 100){
            return TriangleCategory.NOT_A_TRIANGLE.ordinal();
        }

        // 先判断是否能构成三角形
        if (a + b <= c || a + c <= b || b + c <= a) {
            return TriangleCategory.NOT_A_TRIANGLE.ordinal();
        }

        // 判断是否为等边三角形
        if (a == b && b == c) {
            return TriangleCategory.EQUILATERAL.ordinal();
        }

        // 判断是否为等腰三角形
        if (a == b || b == c || a == c) {
            return TriangleCategory.ISOSCELES.ordinal();
        }

        // 如果不符合上述条件，则为一般三角形
        return TriangleCategory.SCALENE.ordinal();
    }

    // 主函数用于测试
    public static void main(String[] args) {
        int a = 3;
        int b = 4;
        int c = 5;

        int type = getTriangleType(a, b, c);
        System.out.println("Triangle with sides " + a + ", " + b + ", " + c + " is: " + type);
    }
}
