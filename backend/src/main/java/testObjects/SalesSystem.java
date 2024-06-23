package testObjects;

public class SalesSystem {

    public static double calculateSales(int host, int displayer, int peripheral) {
        // 校验输入是否在合法范围内
        if (host < 0 || host > 70 || displayer < 0 || displayer > 80 || peripheral < 0 || peripheral > 90) {
            return -1;
        }

        int hostPrice = 25;
        int displayerPrice = 30;
        int peripheralPrice = 45;

        int totalSales = host * hostPrice + displayer * displayerPrice + peripheral * peripheralPrice;
        double commission;

        // 计算佣金
        if (totalSales <= 1000) {
            commission = totalSales * 0.1;
        } else if (totalSales <= 1800) {
            commission = totalSales * 0.15;
        } else {
            commission = totalSales * 0.2;
        }

        return commission;
    }
}
