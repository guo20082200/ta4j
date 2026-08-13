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
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.candles.EveningStarIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.BooleanIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.num.Num;

/**
 * Evening Star Trading Strategy (黄昏之星策略).
 *
 * This strategy combines the Evening Star candlestick pattern with RSI (Relative Strength Index)
 * to generate trading signals.
 *
 * Entry Conditions (Short/Sell):
 * - Evening Star pattern is detected
 * - RSI is in overbought territory (> 70)
 *
 * Exit Conditions:
 * - RSI drops below oversold level (< 30)
 *
 * The Evening Star pattern is a bearish reversal pattern consisting of:
 * 1. A long white candle (bullish)
 * 2. A small body candle that gaps up
 * 3. A long black candle that closes below the midpoint of the first candle
 *
 * Pattern Description:
 * - 黄昏之星（Evening Star）是一个看跌反转形态
 * - 由三根蜡烛线组成：
 *   1. 第一根：长阳线（看涨）
 *   2. 第二根：小实体，向上跳空
 *   3. 第三根：长阴线，收盘价在第一根蜡烛线中点以下
 * - 信号：市场可能从上升趋势反转为下降趋势
 */
public class EveningStarStrategy {

    /**
     * Builds and returns the Evening Star trading strategy.
     *
     * @param barSeries the bar series
     * @return the built strategy
     */
    public static Strategy buildStrategy(BarSeries barSeries) {
        if (barSeries == null) {
            throw new IllegalArgumentException("Bar series cannot be null");
        }

        // Create indicators
        EveningStarIndicator eveningStarIndicator = new EveningStarIndicator(barSeries);
        RSIIndicator rsiIndicator = new RSIIndicator(
                new ClosePriceIndicator(barSeries), 14);

        // ===== ENTRY RULES (Short Entry) =====
        // Rule 1: Evening Star pattern detected
        Rule entryRule1 = new BooleanIndicatorRule(eveningStarIndicator);

        // Rule 2: RSI is overbought (> 70)
        Rule entryRule2 = new CrossedUpIndicatorRule(
                rsiIndicator, Num.valueOf(70));

        // Combine entry rules: Evening Star AND RSI overbought
        Rule entryRule = entryRule1.and(entryRule2);

        // ===== EXIT RULES =====
        // Exit when RSI is oversold (< 30) - indicating potential bounce back
        Rule exitRule = new CrossedDownIndicatorRule(
                rsiIndicator, Num.valueOf(30));

        // Build and return strategy
        return new BaseStrategy(entryRule, exitRule);
    }

    /**
     * Alternative version with custom RSI thresholds.
     * This version allows fine-tuning of RSI thresholds.
     *
     * @param barSeries the bar series
     * @param rsiThresholdOversold RSI oversold threshold (default 30)
     * @param rsiThresholdOverbought RSI overbought threshold (default 70)
     * @return the built strategy with custom RSI thresholds
     */
    public static Strategy buildStrategyWithCustomRSI(BarSeries barSeries,
                                                       int rsiThresholdOversold,
                                                       int rsiThresholdOverbought) {
        if (barSeries == null) {
            throw new IllegalArgumentException("Bar series cannot be null");
        }
        if (rsiThresholdOversold < 0 || rsiThresholdOversold > 50) {
            throw new IllegalArgumentException("RSI oversold threshold should be between 0 and 50");
        }
        if (rsiThresholdOverbought < 50 || rsiThresholdOverbought > 100) {
            throw new IllegalArgumentException("RSI overbought threshold should be between 50 and 100");
        }

        // Create indicators
        EveningStarIndicator eveningStarIndicator = new EveningStarIndicator(barSeries);
        RSIIndicator rsiIndicator = new RSIIndicator(
                new ClosePriceIndicator(barSeries), 14);

        // Entry rules
        Rule entryRule1 = new BooleanIndicatorRule(eveningStarIndicator);
        Rule entryRule2 = new CrossedUpIndicatorRule(
                rsiIndicator, Num.valueOf(rsiThresholdOverbought));

        Rule entryRule = entryRule1.and(entryRule2);

        // Exit rules
        Rule exitRule = new CrossedDownIndicatorRule(
                rsiIndicator, Num.valueOf(rsiThresholdOversold));

        return new BaseStrategy(entryRule, exitRule);
    }
}
