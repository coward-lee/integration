package org.lee;

public class MaxProfit {
    public int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;
        for (int price : prices) {
            if (minPrice > price) {
                minPrice = price;
                continue;
            }
            int profit = price - minPrice;
            if (maxProfit < profit) {
                maxProfit = profit;
            }
        }
        return maxProfit;
    }

    public int maxProfit2(int[] prices) {
        int totalProfit = 0;
        int preBuyPrice = prices[0];
        for (int price : prices) {
            if (preBuyPrice > price) {
                preBuyPrice = price;
            }
            if (preBuyPrice < price){
                totalProfit += price-preBuyPrice;
                preBuyPrice = price;
            }
        }
        return totalProfit;
    }

}
