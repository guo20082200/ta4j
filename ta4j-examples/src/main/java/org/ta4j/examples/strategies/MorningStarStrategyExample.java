/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.examples.strategies;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.num.DecimalNum;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Morning Star Strategy Example.
 *
 * This example demonstrates how to use the MorningStarStrategy for backtesting
 * on historical price data.
 *
 * 曙光初现策略示例：
 * 这个例子展示了如何使用 MorningStarStrategy 在历史价格数据上进行回测。
 */
public class MorningStarStrategyExample {

    public static void main(String[] args) {
        // Create a sample bar series
        BarSeries barSeries = createSampleBarSeries();

        // Build the strategy
        Strategy strategy = MorningStarStrategy.buildStrategy(barSeries);

        // Run the strategy
        BarSeriesManager manager = new BarSeriesManager(barSeries);
        TradingRecord tradingRecord = manager.run(strategy);

        // Print results
        System.out.println("=== Morning Star Strategy Backtest Results ===");
        System.out.println("Total trades: " + tradingRecord.getTradeCount());
        System.out.println("Profitable trades: " + tradingRecord.getTradeCount());

        // Calculate total profit
        TotalProfitCriterion totalProfitCriterion = new TotalProfitCriterion();
        double totalProfit = totalProfitCriterion.calculate(barSeries, tradingRecord)
                .doubleValue();
        System.out.println("Total profit: " + String.format("%.2f%%", totalProfit * 100));

        // Print individual trades
        System.out.println("\n=== Trades ===");
        tradingRecord.getTrades().forEach(trade -> {
            System.out.println("Entry: " + trade.getEntry().getIndex() +
                    " @ " + trade.getEntry().getPrice() +
                    " Exit: " + trade.getExit().getIndex() +
                    " @ " + trade.getExit().getPrice());
        });
    }

    /**
     * Creates a sample bar series with OHLCV data.
     * In real usage, you would load this from a data source (CSV, API, etc.).
     *
     * @return a sample bar series
     */
    private static BarSeries createSampleBarSeries() {
        BarSeries barSeries = new BaseBarSeries("Sample Data");

        // Sample data: prices going down, then up (simulating morning star pattern)
        // Day 1: Long dark candle (bearish)
        barSeries.addBar(new BaseBar(Duration.ofMinutes(1),
                ZonedDateTime.now().minusDays(10),
                DecimalNum.valueOf(100),  // open
                DecimalNum.valueOf(100),  // high
                DecimalNum.valueOf(80),   // low
                DecimalNum.valueOf(85),   // close
                DecimalNum.valueOf(1000))); // volume

        // Day 2: Small body candle (gap down)
        barSeries.addBar(new BaseBar(Duration.ofMinutes(1),
                ZonedDateTime.now().minusDays(9),
                DecimalNum.valueOf(82),
                DecimalNum.valueOf(85),
                DecimalNum.valueOf(80),
                DecimalNum.valueOf(83),
                DecimalNum.valueOf(800)));

        // Day 3: Long white candle (bullish, closes above midpoint)
        barSeries.addBar(new BaseBar(Duration.ofMinutes(1),
                ZonedDateTime.now().minusDays(8),
                DecimalNum.valueOf(84),
                DecimalNum.valueOf(105),
                DecimalNum.valueOf(82),
                DecimalNum.valueOf(100),
                DecimalNum.valueOf(1200)));

        // Continue with more data...
        for (int i = 7; i >= 0; i--) {
            double close = 100 + (7 - i) * 2;
            barSeries.addBar(new BaseBar(Duration.ofMinutes(1),
                    ZonedDateTime.now().minusDays(i),
                    DecimalNum.valueOf(close - 1),
                    DecimalNum.valueOf(close + 2),
                    DecimalNum.valueOf(close - 2),
                    DecimalNum.valueOf(close),
                    DecimalNum.valueOf(1000)));
        }

        return barSeries;
    }
}
