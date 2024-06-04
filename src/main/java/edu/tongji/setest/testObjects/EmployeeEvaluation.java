package edu.tongji.setest.testObjects;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 第三题
 * @date 2024/6/2 20:08:26
 */
public class EmployeeEvaluation {

    public static double evaluateEmployee(int years, int leaveDays, int level, double sales) {
        if (years < 0 || years > 35) return -1; // Invalid years
        if (leaveDays < 0 || leaveDays > 20) return -1; // Invalid leave days
        if (level < 1 || level > 5) return -1; // Invalid level

        if (leaveDays >= 20) return 0; // Exceeds leave days, exempt from evaluation

        double score = 0;

        // Calculate score based on years
        if (years >= 30) {
            score += 5;
        } else if (years >= 20) {
            score += 4;
        } else if (years >= 10) {
            score += 3;
        } else if (years >= 5) {
            score += 2;
        } else {
            score += 1;
        }

        // Calculate score based on level
        score += level;

        // Calculate score based on sales
        if (sales >= 1000000) {
            score += 5;
        } else if (sales >= 500000) {
            score += 4;
        } else if (sales >= 100000) {
            score += 3;
        } else if (sales >= 50000) {
            score += 2;
        } else {
            score += 1;
        }

        return score > 5 ? 5 : score;
    }

    public static void main(String[] args) {
        // Testing with robust boundary values for years
        int[] testYears = {-1, 0, 1, 34, 35, 36};
        int[] testLeaveDays = {0, 1, 19, 20, 21};
        int[] testLevels = {0, 1, 2, 4, 5, 6};
        double[] testSales = {0, 100000, 1000000};

        for (int years : testYears) {
            for (int leaveDays : testLeaveDays) {
                for (int level : testLevels) {
                    for (double sales : testSales) {
                        double result = evaluateEmployee(years, leaveDays, level, sales);
                        System.out.printf("Years: %d, LeaveDays: %d, Level: %d, Sales: %.2f -> Evaluation Score: %.2f%n",
                                years, leaveDays, level, sales, result);
                    }
                }
            }
        }
    }
}

