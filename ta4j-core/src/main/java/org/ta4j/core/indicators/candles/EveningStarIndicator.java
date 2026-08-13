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
package org.ta4j.core.indicators.candles;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Evening Star Indicator.
 *
 * The evening star is a bearish reversal pattern consisting of three candlesticks:
 * 1. First candle: A long white (bullish) candle body
 * 2. Second candle: A smaller candle body that gaps above the first candle
 * 3. Third candle: A long black candle body that moves well into the first candle's body
 *
 * This pattern signals a potential reversal from uptrend to downtrend.
 *
 * @see <a href="https://www.investopedia.com/terms/e/eveningstar.asp">Investopedia - Evening Star</a>
 */
public class EveningStarIndicator extends CachedIndicator<Boolean> {

    private final BarSeries barSeries;

    /**
     * Constructor.
     *
     * @param barSeries the bar series
     */
    public EveningStarIndicator(BarSeries barSeries) {
        super(barSeries);
        this.barSeries = barSeries;
    }

    @Override
    protected Boolean calculate(int index) {
        // Need at least 3 candles to detect evening star pattern
        if (index < 2) {
            return false;
        }

        Bar bar1 = barSeries.getBar(index - 2); // First candle: long white candle
        Bar bar2 = barSeries.getBar(index - 1); // Second candle: small body, gaps up
        Bar bar3 = barSeries.getBar(index);     // Third candle: long black candle

        // Check if first candle is bullish (close > open)
        boolean firstCandleBullish = bar1.getClosePrice().isGreaterThan(bar1.getOpenPrice());
        if (!firstCandleBullish) {
            return false;
        }

        // Check if first candle is relatively long (significant body)
        Num firstCandleBody = bar1.getClosePrice().minus(bar1.getOpenPrice());
        if (firstCandleBody.isLessThanOrEqual(Num.valueOf(0))) {
            return false;
        }

        // Check if second candle has small body (gap up)
        Num secondCandleBody = bar2.getOpenPrice().minus(bar2.getClosePrice());
        if (secondCandleBody.isNegative()) {
            secondCandleBody = bar2.getClosePrice().minus(bar2.getOpenPrice());
        }

        // Second candle body should be smaller than first candle
        if (secondCandleBody.isGreaterThan(firstCandleBody.multipliedBy(0.5))) {
            return false;
        }

        // Check if second candle gaps above first candle (high of second > close of first)
        boolean gapUp = bar2.getHighPrice().isGreaterThan(bar1.getClosePrice());
        if (!gapUp) {
            return false;
        }

        // Check if third candle is bearish (close < open)
        boolean thirdCandleBearish = bar3.getClosePrice().isLessThan(bar3.getOpenPrice());
        if (!thirdCandleBearish) {
            return false;
        }

        // Check if third candle is relatively long (significant body)
        Num thirdCandleBody = bar3.getOpenPrice().minus(bar3.getClosePrice());
        if (thirdCandleBody.isLessThanOrEqual(Num.valueOf(0))) {
            return false;
        }

        // Check if third candle closes below the midpoint of the first candle
        Num firstCandleMidpoint = bar1.getOpenPrice()
                .plus(bar1.getClosePrice().minus(bar1.getOpenPrice()).dividedBy(Num.valueOf(2)));
        boolean closesBelowMidpoint = bar3.getClosePrice().isLessThan(firstCandleMidpoint);

        return closesBelowMidpoint;
    }
}
