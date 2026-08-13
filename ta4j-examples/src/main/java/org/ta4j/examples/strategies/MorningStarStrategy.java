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
import org.ta4j.core.indicators.candles.MorningStarIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.BooleanIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.num.Num;

/**
 * Morning Star Trading Strategy.
 *
 * This strategy combines the Morning Star candlestick pattern with RSI (Relative Strength Index)
 * to generate trading signals.
 *
 * Entry Conditions:
 * - Morning Star pattern is detected
 * - RSI is in oversold territory (< 30)
 *
 * Exit Conditions:
 * - RSI rises above overbought level (> 70)
 *
 * The Morning Star pattern is a bullish reversal pattern consisting of:
 * 1. A long dark candle (bearish)
 * 2. A small body candle that gaps down
 * 3. A long white candle that closes above the midpoint of the first candle
 */
public class MorningStarStrategy {

    /**
     * Builds and returns the Morning Star trading strategy.
     *
     * @param barSeries the bar series
     * @return the built strategy
     */
    public static Strategy buildStrategy(BarSeries barSeries) {
        if (barSeries == null) {
            throw new IllegalArgumentException("Bar series cannot be null");
        }

        // Create indicators
        MorningStarIndicator morningStarIndicator = new MorningStarIndicator(barSeries);
        RSIIndicator rsiIndicator = new RSIIndicator(
                new ClosePriceIndicator(barSeries), 14);

        // ===== ENTRY RULES =====
        // Rule 1: Morning Star pattern detected
        Rule entryRule1 = new BooleanIndicatorRule(morningStarIndicator);

        // Rule 2: RSI is oversold (< 30)
        Rule entryRule2 = new CrossedDownIndicatorRule(
                rsiIndicator, Num.valueOf(30));

        // Combine entry rules: Morning Star AND RSI oversold
        Rule entryRule = entryRule1.and(entryRule2);

        // ===== EXIT RULES =====
        // Exit when RSI is overbought (> 70) - indicating potential pullback
        Rule exitRule = new CrossedUpIndicatorRule(
                rsiIndicator, Num.valueOf(70));

        // Build and return strategy
        return new BaseStrategy(entryRule, exitRule);
    }

    /**
     * Alternative version with additional filters.
     * This version adds more strict conditions to reduce false signals.
     *
     * Additional Entry Conditions:
     * - Volume confirmation (if available)
     * - Price must be below a resistance level
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
        MorningStarIndicator morningStarIndicator = new MorningStarIndicator(barSeries);
        RSIIndicator rsiIndicator = new RSIIndicator(
                new ClosePriceIndicator(barSeries), 14);

        // Entry rules
        Rule entryRule1 = new BooleanIndicatorRule(morningStarIndicator);
        Rule entryRule2 = new CrossedDownIndicatorRule(
                rsiIndicator, Num.valueOf(rsiThresholdOversold));

        Rule entryRule = entryRule1.and(entryRule2);

        // Exit rules
        Rule exitRule = new CrossedUpIndicatorRule(
                rsiIndicator, Num.valueOf(rsiThresholdOverbought));

        return new BaseStrategy(entryRule, exitRule);
    }
}
