package testObjects;

/**
 * @author asahi
 * @version 1.0
 * @project Software-Testing-Homework
 * @description 万年历的下一天
 * @date 2024/6/2 19:33:35
 */


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NextDayCalculator {

    public static void main(String[] args) {
        System.out.println(getNextDay(2024, 2, 28)); // 示例调用
        System.out.println(getNextDay(2024, 13, 28)); // 示例调用
    }

    public static String getNextDay(int year, int month, int day) {
        if (year < 1900 || year > 2100 || !isValidDate(year, month, day)) {
            return "输入日期无效";
        }

        LocalDate date = LocalDate.of(year, month, day);
        LocalDate nextDay = date.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return nextDay.format(formatter);
    }

    private static boolean isValidDate(int year, int month, int day) {
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > daysInMonth(year, month)) {
            return false;
        }
        return true;
    }

    private static int daysInMonth(int year, int month) {
        switch (month) {
            case 2:
                return isLeapYear(year) ? 29 : 28;
            case 4: case 6: case 9: case 11:
                return 30;
            default:
                return 31;
        }
    }

    private static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 100 != 0) {
            return true;
        } else if (year % 400 != 0) {
            return false;
        } else {
            return true;
        }
    }
}
