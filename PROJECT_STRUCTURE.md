# ta4j 项目结构概览

## 项目说明

Ta4j 是一个纯 Java 的技术分析库，提供了 130+ 个技术指标、策略构建引擎以及回测工具，用于创建、评估和执行交易策略。这是一个 fork 自官方 ta4j/ta4j 仓库的项目，采用 MIT 许可证。

### 技术栈

- **语言:** Java (100%)
- **框架/运行时:** Java 21+ (Maven 构建)
- **核心依赖:**
  - SLF4J (日志门面)
  - Apache Commons Math 3 (数学计算)
  - JUnit 5 (单元测试)
  - AssertJ (测试断言)
  - OpenCSV (CSV 数据处理)

## 项目组织结构

```
ta4j/
├── ta4j-core/                          # 核心库模块
│   ├── src/main/java/org/ta4j/core/
│   │   ├── Bar.java                    # 单根蜡烛线数据结构
│   │   ├── BarSeries.java              # 蜡烛线序列容器
│   │   ├── BarBuilder.java             # Bar 构造器
│   │   ├── BarSeriesBuilder.java       # BarSeries 构造器
│   │   ├── BaseBar.java                # Bar 基础实现
│   │   ├── BaseBarSeries.java          # BarSeries 基础实现
│   │   ├── Indicator.java              # 指标接口定义
│   │   ├── Strategy.java               # 交易策略接口
│   │   ├── BaseStrategy.java           # 策略基础实现
│   │   ├── Rule.java                   # 交易规则接口
│   │   ├── Position.java               # 单个头寸（买入/卖出）
│   │   ├── Trade.java                  # 单笔交易
│   │   ├── TradingRecord.java          # 交易记录接口
│   │   ├── BaseTradingRecord.java      # 交易记录实现
│   │   ├── AnalysisCriterion.java      # 策略评估标准接口
│   │   │
│   │   ├── indicators/                 # 130+ 技术指标实现
│   │   │   ├── AbstractIndicator.java  # 指标基类
│   │   │   ├── CachedIndicator.java    # 缓存指标基类
│   │   │   ├── RecursiveCachedIndicator.java # 递归缓存指标
│   │   │   │
│   │   │   ├── 动量指标:
│   │   │   │   ├── MACDIndicator.java           # MACD 指标
│   │   │   │   ├── RSIIndicator.java           # RSI 相对强度指数
│   │   │   │   ├── StochasticOscillatorKIndicator.java  # K%D
│   │   │   │   ├── StochasticOscillatorDIndicator.java  # %D
│   │   │   │   ├── StochasticRSIIndicator.java # Stochastic RSI
│   │   │   │   ├── ROCIndicator.java           # 变化率
│   │   │   │   ├── CMOIndicator.java           # Chande 动量
│   │   │   │   ├── KSTIndicator.java           # Know Sure Thing
│   │   │   │   └── PPOIndicator.java           # 百分比价格振荡
│   │   │   │
│   │   │   ├── 波动率指标:
│   │   │   │   ├── ATRIndicator.java           # 平均真实波幅
│   │   │   │   ├── bollinger/                  # 布林带相关
│   │   │   │   │   ├── BollingerBandsMiddleIndicator.java
│   │   │   │   │   ├── BollingerBandsUpperIndicator.java
│   │   │   │   │   └── BollingerBandsLowerIndicator.java
│   │   │   │   ├── keltner/                    # Keltner 通道
│   │   │   │   │   ├── KeltnerChannelMiddleIndicator.java
│   │   │   │   │   ├── KeltnerChannelUpperIndicator.java
│   │   │   │   │   └── KeltnerChannelLowerIndicator.java
│   │   │   │   └── ChandelierExitLongIndicator.java
│   │   │   │
│   │   │   ├── 趋势指标:
│   │   │   │   ├── aroon/                      # Aroon 指标
│   │   │   │   │   ├── AroonUpIndicator.java
│   │   │   │   │   ├── AroonDownIndicator.java
│   │   │   │   │   └── AroonOscillatorIndicator.java
│   │   │   │   ├── adx/                        # ADX 趋势强度
│   │   │   │   │   ├── ADXIndicator.java
│   │   │   │   │   ├── ADXRIndicator.java
│   │   │   │   │   ├── MinusDIIndicator.java
│   │   │   │   │   └── PlusDIIndicator.java
│   │   │   │   ├── trend/                      # 趋势识别
│   │   │   │   │   └── GainLossIndicator.java
│   │   │   │   └── ParabolicSarIndicator.java  # SAR 抛物线
│   │   │   │
│   │   │   ├── 移动平均线族:
│   │   │   │   └── averages/
│   │   │   │       ├── SimpleMovingAverageIndicator.java (SMA)
│   │   │   │       ├── ExponentialMovingAverageIndicator.java (EMA)
│   │   │   │       ├── WilderMovingAverageIndicator.java
│   │   │   │       ├── WeightedMovingAverageIndicator.java (WMA)
│   │   │   │       ├── HullMovingAverageIndicator.java (HMA)
│   │   │   │       ├── TripleExponentialMovingAverageIndicator.java
│   │   │   │       ├── SMATrailIndicator.java
│   │   │   │       └── KaufmanAdaptiveMovingAverageIndicator.java (KAMA)
│   │   │   │
│   │   │   ├── 成交量指标:
│   │   │   │   └── volume/
│   │   │   │       ├── OnBalanceVolumeIndicator.java (OBV)
│   │   │   │       ├── KamaIndicator.java
│   │   │   │       ├── AccumulationDistributionIndicator.java (A/D)
│   │   │   │       ├── ChaikinADLineIndicator.java
│   │   │   │       ├── ChaikinMoneyFlowIndicator.java (CMF)
│   │   │   │       └── MoneyFlowIndexIndicator.java (MFI)
│   │   │   │
│   │   │   ├── K线形态:
│   │   │   │   └── candles/
│   │   │   │       ├── DojiIndicator.java
│   │   │   │       ├── HammerIndicator.java
│   │   │   │       ├── DragonflyDojiIndicator.java
│   │   │   │       ├── GravestoneDojiIndicator.java
│   │   │   │       └── TakuriIndicator.java
│   │   │   │
│   │   │   ├── 一目均衡表:
│   │   │   │   └── ichimoku/
│   │   │   │       ├── IchimokuIndicator.java
│   │   │   │       ├── IchimokuTenkanSenIndicator.java
│   │   │   │       ├── IchimokuKijunSenIndicator.java
│   │   │   │       ├── IchimokuChikouSpanIndicator.java
│   │   │   │       ├── IchimokuSenkouSpanAIndicator.java
│   │   │   │       └── IchimokuSenkouSpanBIndicator.java
│   │   │   │
│   │   │   ├── 其他指标:
│   │   │   │   ├── Williams %R (WilliamsRIndicator.java)
│   │   │   │   ├── CCI (CCIIndicator.java) - 商品通道指数
│   │   │   │   ├── Fisher Transform (FisherIndicator.java)
│   │   │   │   ├── Ultimate Oscillator (UltimateOscillatorIndicator.java)
│   │   │   │   ├── Awesome Oscillator (AwesomeOscillatorIndicator.java)
│   │   │   │   ├── Supertrend (supertrend/)
│   │   │   │   ├── Donchian 通道 (donchian/)
│   │   │   │   ├── Pivot Points (pivotpoints/)
│   │   │   │   ├── Random Walk Index (RWIHighIndicator, RWILowIndicator)
│   │   │   │   ├── Detrended Price Oscillator (DPOIndicator.java)
│   │   │   │   └── 其他 50+ 指标
│   │   │   │
│   │   │   ├── numeric/                        # 数值辅助指标
│   │   │   ├── helpers/                        # 指标辅助工具
│   │   │   ├── statistics/                     # 统计指标
│   │   │   └── package-info.java
│   │   │
│   │   ├── analysis/                   # 分析工具
│   │   │   └── 策略评估和分析模块
│   │   │
│   │   ├── backtest/                   # 回测引擎
│   │   │   └── 历史数据回测和性能评估
│   │   │
│   │   ├── criteria/                   # 交易评估标准
│   │   │   ├── 利润率
│   │   │   ├── 夏普比率
│   │   │   ├── 最大回撤
│   │   │   └── 其他性能指标
│   │   │
│   │   ├── bars/                       # 蜡烛线管理工具
│   │   │   ├── 数据转换
│   │   │   └── 时间序列操作
│   │   │
│   │   ├── rules/                      # 交易规则实现
│   │   │   ├── 买入规则
│   │   │   ├── 卖出规则
│   │   │   └── 组合规则逻辑
│   │   │
│   │   ├── reports/                    # 交易报告生成
│   │   │   └── 回测结果报告
│   │   │
│   │   ├── num/                        # 数值计算
│   │   │   └── 数字表示和计算
│   │   │
│   │   ├── utils/                      # 工具类
│   │   │   └── 通用工具函数
│   │   │
│   │   ├── aggregator/                 # 数据聚合
│   │   │   └── 时间序列数据聚合
│   │   │
│   │   └── package-info.java           # 包文档
│   │
│   └── src/test/java/org/ta4j/core/   # 单元测试
│       ├── 各模块对应的测试类
│       └── 回归测试和集成测试
│
├── ta4j-examples/                      # 示例代码模块
│   ├── src/main/java/org/ta4j/examples/
│   │   ├── 策略示例
│   │   ├── 指标使用示例
│   │   ├── 回测示例
│   │   └── 数据加载示例
│   │
│   └── src/test/                       # 示例测试
│
├── .github/                            # GitHub 配置
│   ├── workflows/                      # CI/CD 工作流
│   └── CONTRIBUTING.md                 # 贡献指南
│
├── pom.xml                             # 父 POM 配置 (Maven)
│   ├── 版本: 0.19-SNAPSHOT
│   ├── Java 版本: 21
│   ├── 依赖管理
│   └── 编译和发布配置
│
├── ta4j-core/pom.xml                   # 核心模块 POM
├── ta4j-examples/pom.xml               # 示例模块 POM
│
├── README.md                           # 项目说明
├── CHANGELOG.md                        # 版本历史
├── LICENSE                             # MIT 许可证
├── AUTHORS                             # 作者信息
├── CODE_OF_CONDUCT.md                  # 行为准则
├── Doxyfile                            # Doxygen 文档配置
└── code-formatter.xml                  # 代码格式化配置
```

## 关键模块说明

### ta4j-core（核心库）

核心库是主要功能所在，分为以下几层：

#### 1. **数据结构层**
- `Bar`: 单根蜡烛线，包含 OHLCV（开盘价、最高价、最低价、收盘价、成交量）数据
- `BarSeries`: 蜡烛线序列容器，管理多根 Bar 数据
- `BarBuilder` 和 `BarSeriesBuilder`: 使用 Builder 模式构建数据对象

#### 2. **指标层**
- `Indicator` 接口: 所有指标的基础接口
- `AbstractIndicator`: 指标抽象基类
- `CachedIndicator`: 带缓存的指标基类，提高性能
- **130+ 指标实现**:
  - **动量类**: RSI、MACD、Stochastic、CCI、ROC 等
  - **波动率**: ATR、布林带、Keltner 通道等
  - **趋势**: ADX、Aroon、SAR 等
  - **移动平均**: SMA、EMA、WMA、HMA、KAMA 等
  - **成交量**: OBV、A/D、CMF、MFI 等
  - **K线形态**: Doji、Hammer 等
  - **一目均衡表**: Ichimoku 系列指标

#### 3. **策略层**
- `Strategy` 接口: 定义交易策略
- `BaseStrategy`: 策略基础实现
- `Rule` 接口: 交易规则
- 结合指标和规则定义买入/卖出条件

#### 4. **交易记录层**
- `Position`: 单个头寸（买入或卖出）
- `Trade`: 单笔完整交易（买卖对）
- `TradingRecord`: 记录所有交易
- 用于回测和性能评估

#### 5. **评估层**
- `AnalysisCriterion`: 策略评估标准接口
- `criteria/` 包: 实现各种评估指标
  - 利润率
  - 夏普比率
  - 最大回撤
  - 盈亏因子等

#### 6. **支撑层**
- `analysis/`: 分析工具
- `backtest/`: 回测引擎
- `bars/`: 蜡烛线管理工具
- `rules/`: 规则实现
- `reports/`: 报告生成
- `num/`: 数值计算
- `utils/`: 通用工具

### ta4j-examples（示例代码）

提供实际使用案例，展示如何集成核心库：
- 如何加载市场数据
- 如何构建和运行策略
- 如何执行回测
- 如何分析结果

## 快速开始

### 环境要求

- Java 21+
- Maven 3.6+

### 编译构建

```bash
# 克隆仓库
git clone https://github.com/guo20082200/ta4j.git
cd ta4j

# 编译整个项目（包含 core 和 examples）
mvn clean install

# 只编译核心库
mvn clean install -pl ta4j-core

# 只编译示例
mvn clean install -pl ta4j-examples

# 运行所有测试
mvn test

# 只运行核心库测试
mvn test -pl ta4j-core

# 跳过测试进行快速编译
mvn clean install -DskipTests
```

### Maven 依赖

在你的项目的 `pom.xml` 中添加：

```xml
<dependency>
  <groupId>org.ta4j</groupId>
  <artifactId>ta4j-core</artifactId>
  <version>0.18</version>
</dependency>
```

或使用最新的快照版本：

```xml
<repository>
    <id>sonatype-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>

<dependency>
  <groupId>org.ta4j</groupId>
  <artifactId>ta4j-core</artifactId>
  <version>0.19-SNAPSHOT</version>
</dependency>
```

## 代码示例

### 基础使用

```java
// 1. 创建蜡烛线序列
BarSeries barSeries = new BaseBarSeries("My Series");

// 2. 添加蜡烛线数据
Bar bar = new BaseBar(
    Duration.ofMinutes(1),
    ZonedDateTime.now(),
    100,  // 开盘价
    110,  // 最高价
    90,   // 最低价
    100,  // 收盘价
    1000  // 成交量
);
barSeries.addBar(bar);

// 3. 使用指标
RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(barSeries), 14);
Num rsiValue = rsi.getValue(barSeries.getEndIndex());

// 4. 构建策略
Rule entryRule = new CrossedUpIndicatorRule(rsi, Num.valueOf(30));
Rule exitRule = new CrossedDownIndicatorRule(rsi, Num.valueOf(70));
Strategy strategy = new BaseStrategy(entryRule, exitRule);

// 5. 执行回测
BarSeriesManager manager = new BarSeriesManager(barSeries);
TradingRecord tradingRecord = manager.run(strategy);

// 6. 评估结果
AnalysisCriterion totalProfitCriterion = new TotalProfitCriterion();
double totalProfit = totalProfitCriterion.calculate(barSeries, tradingRecord);
```

## 架构特点

1. **灵活的指标体系**: 130+ 指标，可自由组合
2. **易用的策略框架**: 通过 Rule 组合实现复杂策略
3. **完整的回测支持**: 精确记录交易，详细评估性能
4. **高效的计算**: 缓存机制提升性能，支持大数据集
5. **纯 Java 实现**: 跨平台，易于集成
6. **最小依赖**: 仅依赖必要的第三方库

## 学习资源

- [官方 Wiki](https://ta4j.github.io/ta4j-wiki/) - 详细文档和教程
- [Issue Tracker](https://github.com/ta4j/ta4j/issues) - 问题追踪和讨论
- [Roadmap](https://ta4j.github.io/ta4j-wiki/Roadmap-and-Tasks.html) - 项目规划
- [如何贡献](https://ta4j.github.io/ta4j-wiki/How-to-contribute) - 贡献指南

## 常见问题

### 如何添加自定义指标？

继承 `AbstractIndicator` 或 `CachedIndicator`，实现 `getValue()` 方法：

```java
public class MyCustomIndicator extends CachedIndicator<Num> {
    public MyCustomIndicator(Indicator<Num> indicator) {
        super(indicator);
    }
    
    @Override
    protected Num calculate(int index) {
        // 实现计算逻辑
        return ...;
    }
}
```

### 如何优化回测性能？

1. 使用 `CachedIndicator` 缓存结果
2. 减少不必要的指标计算
3. 使用较少的历史数据进行初始测试
4. 并行处理多个策略

### 支持哪些时间周期？

支持任意 Duration，常见的有：
- 分钟级: 1m, 5m, 15m, 30m
- 小时级: 1h, 4h
- 日线: 1d
- 周线: 1w
- 月线: 1M

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件

## 贡献

欢迎提交 Issue 和 Pull Request！请查看 [CONTRIBUTING.md](.github/CONTRIBUTING.md) 了解详情。

---

**最后更新**: 2026-08-13
**当前版本**: 0.19-SNAPSHOT
**Java 版本**: 21+
