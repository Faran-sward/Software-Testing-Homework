package testObjects;

public class SalesCommissionSystem {

    public static void main(String[] args) {
        testSalesCommissionSystem();
    }

    public static void testSalesCommissionSystem() {
        // 测试用例
        // 测试用例
        // 测试用例
        // 测试用例
        double[][] testCases = {
                {-1, -1, -1},    // 非法输入
                {-1, -1, 50},    // 非法输入
                {-1, 10, 50},    // 非法输入
                {-1, 11, -1},    // 非法输入
                {-1, 11, 50},    // 非法输入
                {-1, 11, 2},     // 非法输入
                {2010000, -1, -1}, // 非法输入
                {2010000, -1, 50}, // 非法输入
                {2010000, -1, 50}, // 非法输入
                {2010000, 11, -1}, // 非法输入
                {2010000, 11, 50}, // 合法输入
                {2010000, 11, 50}, // 非法输入
                {2010000, 10, 80}, // 合法输入
                {2010000, 10, 30}, // 合法输入
                {2010000, 11, 90}  // 合法输入
        };

        double[] expectedResults = {
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                -1, // 非法输入
                2010000 / 6, // 合法输入
                -1, // 非法输入
                2010000 / 7, // 合法输入
                0, // 合法输入
                2010000 / 5 // 合法输入
        };

        for (int i = 0; i < testCases.length; i++) {
            double commission = calculateCommission(testCases[i][0], (int)testCases[i][1], testCases[i][2]);
            if (commission == -1) {
                System.out.printf("Test case %d: 非法输入 (预期输出: 非法输入)\n", i + 1);
            } else {
                System.out.printf("Test case %d: Sales Amount: %.2f, Days Absent: %d, Cash Arrival Rate: %.2f, Commission: %.2f (预期输出: %.2f)\n",
                        i + 1, testCases[i][0], (int)testCases[i][1], testCases[i][2], commission, expectedResults[i]);
            }
        }
    }

    public static double calculateCommission(double salesAmount, int daysAbsent, double cashArrivalRate) {
        // 校验输入是否在合法范围内
        if (salesAmount < 0 || daysAbsent < 0 || cashArrivalRate < 0 || cashArrivalRate > 100) {
            return -1;
        }

        double commission = 0;

        if (salesAmount > 2000000 && daysAbsent <= 10) {
            if (cashArrivalRate >= 60) {
                commission = salesAmount / 7;
            } else {
                commission = 0;
            }
        } else {
            if (cashArrivalRate <= 85) {
                commission = salesAmount / 6;
            } else if (cashArrivalRate > 85) {
                commission = salesAmount / 5;
            }
        }

        return commission;
    }
}

