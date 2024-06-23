package testObjects;

public class TelecomBillingSystem {

    public static void main(String[] args) {
        testTelecomBillingSystem();
    }

    public static void testTelecomBillingSystem() {
        // 测试用例
        int[][] testCases = {
                {-1, -1}, {-1, 0}, {-1, 2}, {-1, 3}, {-1, 6}, {-1, 11}, {-1, 12},
                {60, -1}, {60, 0}, {60, 2}, {60, 3}, {60, 6}, {60, 11}, {60, 12},
                {120, -1}, {120, 0}, {120, 2}, {120, 3}, {120, 6}, {120, 11}, {120, 12},
                {180, -1}, {180, 0}, {180, 2}, {180, 3}, {180, 6}, {180, 11}, {180, 12},
                {300, -1}, {300, 0}, {300, 2}, {300, 3}, {300, 6}, {300, 11}, {300, 12},
                {44640, -1}, {44640, 0}, {44640, 2}, {44640, 3}, {44640, 6}, {44640, 11}, {44640, 12},
                {44641, -1}, {44641, 0}, {44641, 2}, {44641, 3}, {44641, 6}, {44641, 11}, {44641, 12}
        };

        for (int i = 0; i < testCases.length; i++) {
            double totalCost = calculateMonthlyBill(testCases[i][0], testCases[i][1]);
            if (totalCost == -1) {
                System.out.printf("Test case %d: 非法输入\n", i + 1);
            } else {
                System.out.printf("Test case %d: Minutes: %d, Late Payments: %d, Total Cost: %.2f\n",
                        i + 1, testCases[i][0], testCases[i][1], totalCost);
            }
        }
    }

    public static double calculateMonthlyBill(int minutes, int latePayments) {
        // 校验输入是否在合法范围内
        if (minutes < 0 || minutes > 44640 || latePayments < 0 || latePayments > 12) {
            return -1;
        }

        double baseRate = 25.0;
        double perMinuteRate = 0.15;
        double discountRate = 0.0;
        int maxLatePayments = 0;

        // 确定折扣率和最大容许未按时缴费次数
        if (minutes > 0 && minutes <= 60) {
            discountRate = 0.01;
            maxLatePayments = 1;
        } else if (minutes > 60 && minutes <= 120) {
            discountRate = 0.015;
            maxLatePayments = 2;
        } else if (minutes > 120 && minutes <= 180) {
            discountRate = 0.02;
            maxLatePayments = 3;
        } else if (minutes > 180 && minutes <= 300) {
            discountRate = 0.025;
            maxLatePayments = 3;
        } else if (minutes > 300) {
            discountRate = 0.03;
            maxLatePayments = 6;
        }

        // 计算通话费
        double callCost = minutes * perMinuteRate;

        // 确定是否有折扣
        if (latePayments > maxLatePayments) {
            discountRate = 0.0;
        }

        // 计算折扣后的通话费
        double discountedCallCost = callCost * (1 - discountRate);

        // 计算总费用
        double totalCost = baseRate + discountedCallCost;

        return totalCost;
    }
}
