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
 * Morning Star Indicator.
 *
 * The morning star is a bullish reversal pattern consisting of three candlesticks:
 * 1. First candle: A long dark (black) candle body
 * 2. Second candle: A smaller candle body that gaps below the first candle
 * 3. Third candle: A long white candle body that moves well into the first candle's body
 *
 * This pattern signals a potential reversal from downtrend to uptrend.
 *
 * @see <a href="https://www.investopedia.com/terms/m/morningstar.asp">Investopedia - Morning Star</a>
 */
public class MorningStarIndicator extends CachedIndicator<Boolean> {

    private final BarSeries barSeries;

    /**
     * Constructor.
     *
     * @param barSeries the bar series
     */
    public MorningStarIndicator(BarSeries barSeries) {
        super(barSeries);
        this.barSeries = barSeries;
    }

    @Override
    protected Boolean calculate(int index) {
        // Need at least 3 candles to detect morning star pattern
        if (index < 2) {
            return false;
        }

        Bar bar1 = barSeries.getBar(index - 2); // First candle: long black candle
        Bar bar2 = barSeries.getBar(index - 1); // Second candle: small body, gaps down
        Bar bar3 = barSeries.getBar(index);     // Third candle: long white candle

        // Check if first candle is bearish (close < open)
        boolean firstCandleBearish = bar1.getClosePrice().isLessThan(bar1.getOpenPrice());
        if (!firstCandleBearish) {
            return false;
        }

        // Check if first candle is relatively long (significant body)
        Num firstCandleBody = bar1.getOpenPrice().minus(bar1.getClosePrice());
        if (firstCandleBody.isLessThanOrEqual(Num.valueOf(0))) {
            return false;
        }

        // Check if second candle has small body (gap down)
        Num secondCandleBody = bar2.getOpenPrice().minus(bar2.getClosePrice());
        if (secondCandleBody.isNegative()) {
            secondCandleBody = bar2.getClosePrice().minus(bar2.getOpenPrice());
        }

        // Second candle body should be smaller than first candle
        if (secondCandleBody.isGreaterThan(firstCandleBody.multipliedBy(0.5))) {
            return false;
        }

        // Check if second candle gaps below first candle (low of second < close of first)
        boolean gapDown = bar2.getLowPrice().isLessThan(bar1.getClosePrice());
        if (!gapDown) {
            return false;
        }

        // Check if third candle is bullish (close > open)
        boolean thirdCandleBullish = bar3.getClosePrice().isGreaterThan(bar3.getOpenPrice());
        if (!thirdCandleBullish) {
            return false;
        }

        // Check if third candle is relatively long (significant body)
        Num thirdCandleBody = bar3.getClosePrice().minus(bar3.getOpenPrice());
        if (thirdCandleBody.isLessThanOrEqual(Num.valueOf(0))) {
            return false;
        }

        // Check if third candle closes above the midpoint of the first candle
        Num firstCandleMidpoint = bar1.getOpenPrice()
                .minus(bar1.getOpenPrice().minus(bar1.getClosePrice()).dividedBy(Num.valueOf(2)));
        boolean closesAboveMidpoint = bar3.getClosePrice().isGreaterThan(firstCandleMidpoint);

        return closesAboveMidpoint;
    }
}
