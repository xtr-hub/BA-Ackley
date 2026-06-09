# BA-Ackley

BA-Ackley是一个基于Apache Commons Math实现Ackley函数和蝙蝠算法(Bat Algorithm)相关工具的Java库，用于优化算法测试和对比。

## 项目结构

```
├── server/
│   ├── main.java          # 优化版蝙蝠算法（含自适应系数G、离群度选择、bestSize动态调整）
│   └── OriginMain.java    # 原始蝙蝠算法（无优化，用于对比）
├── entity/
│   ── Batis.java         # 蝙蝠个体实体（含位置、速度、响度、脉冲率、适应度、离群度）
└── utils/
    ├── AckleyUtils.java       # Ackley函数计算
    ├── RealVectorUtils.java   # 向量操作工具（随机生成、距离计算、边界处理等）
    └── XYChartUtils.java      # 图表可视化工具
```

## 算法对比

运行 `main.main()` 会同时执行两个算法并在同一张图上对比：

| 特性 | 优化版 (main) | 原始版 (OriginMain) |
|------|--------------|-------------------|
| 位置更新 | `x = x + G·v`（自适应系数） | `x = x + v` |
| 自适应系数G | `G = sqrt(1 - gen/Gen_max)` | 无 |
| 精英选择 | `findBest()` 归一化评分（适应度 + 离群度） | `findGlobalBest()` 简单贪心 |
| 离群度计算 | 欧氏距离均值 | 无 |
| bestSize | 动态调整（随响度衰减，有上下限） | 无 |
| 图表系列名 | "Default Series" | "原始蝙蝠算法" |

## API文档

### AckleyUtils类

#### `public static double Ackley(ArrayRealVector arrayRealVector)`

计算向量的Ackley函数值。Ackley函数是一个常用的优化问题基准函数，具有许多局部最小值，全局最小值在原点处，用于评估优化算法的性能。

**参数:**
- `arrayRealVector` - ArrayRealVector类型，输入的实数向量

**返回值:**
- `double`类型，计算得到的Ackley函数值，值越小表示向量越接近原点

**计算公式:**
```
f(x) = -20 * exp(-0.2 * sqrt(1/n * Σxi²)) - exp(1/n * Σcos(2πxi)) + 20 + e
```

其中n为向量维度，xi为向量的第i个元素。

**示例:**
```java
ArrayRealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
double result = AckleyUtils.Ackley(vector);
```

### RealVectorUtils类

#### 随机向量生成

- `randomRealVector(int n)` — 生成n维随机向量，元素在[0,1)
- `randomRealVector(int n, double min, double max)` — 生成n维随机向量，元素在[min, max)
- `randomRealVector(Random random, int n, double min, double max)` — 使用指定Random生成随机向量（用于对比实验）
- `randomRealVector(int n, double[] min, double[] max)` — 每个维度独立范围
- `randomEpsilon(int n)` — 生成小扰动向量，元素在[-0.1, 0.1)

#### 向量操作

- `copy(ArrayRealVector v)` — 复制向量
- `bound(ArrayRealVector v, double[] lb, double[] ub)` — 边界约束处理
- `boundPosition(ArrayRealVector pos, double lower, double upper)` — 标量边界处理
- `encodingROV(ArrayRealVector v)` — ROV编码（实值向量转排列向量）

#### 距离计算

- `hammingDistance(T a, T b, Function<T, ArrayRealVector> convert)` — 汉明距离（适用于离散问题）
- `euclideanDistance(T a, T b, Function<T, ArrayRealVector> convert)` — 欧氏距离（适用于连续问题）

#### 最优个体查找

- `best(ArrayRealVector[] vectors)` — 从向量数组中找适应度最强的
- `best(T[] objects, Function<T, ArrayRealVector> convert)` — 函数式接口版本
- `best(T[] objects, BiFunction<T, T, Boolean> cmp)` — 自定义比较函数版本

### XYChartUtils类

- `create(String title, String xAxisTitle, String yAxisTitle, double firstX, double firstY)` — 创建图表并添加初始点
- `add(XYChart chart, double x, double y)` — 向默认系列追加数据点
- `addSeries(XYChart chart, String seriesName, double firstX, double firstY)` — 添加新数据系列
- `add(XYChart chart, String seriesName, double x, double y)` — 向指定系列追加数据点

### Batis实体类

| 字段 | 类型 | 说明 |
|------|------|------|
| answer | ArrayRealVector | 位置向量（蝙蝠当前坐标） |
| speed | ArrayRealVector | 速度向量 |
| loudness | double | 响度 |
| pulseRate | double | 脉冲率 |
| ackleyValue | double | 适应度值（Ackley函数值） |
| outlierDegree | double | 离群度（与其他精英的欧氏距离均值） |

## 配置参数

### BA算法参数 (src/main/resources/config/BAConfig.properties)
```properties
# 频率范围上下限
ba.frequency.min=0.0
ba.frequency.max=1.0

# 响度衰减系数 a (通常 0.9~0.99)
ba.loudness.decay.a=0.95

# 脉冲率增强系数 γ (通常 0.001~0.1)
ba.pulse.rate.gamma=0.01

# 初始响度 a0 (通常 0.5~2.0)
ba.loudness.initial.a0=1.0

# 初始脉冲率 r0 (通常 0.0~0.5)
ba.pulse.rate.initial.r0=0.1

# 随机数种子（便于复现实验）
ba.random.seed=1145141919810

# 精英集合初始大小与种群大小比例
ba.best.size=0.1
```

### 问题参数 (hardcoded in main.java)
```java
private static final int DIMENSION = 10;        // 问题维度
private static final int POP_SIZE = 80;         // 种群数量
private static final int MAX_ITERATIONS = 500;  // 最大迭代次数
private static final double LOWER_BOUND = -32.768;
private static final double UPPER_BOUND = 32.768;
```

## 核心优化原理

### 1. 自适应位置更新
引入自适应系数 `G = sqrt(1 - gen/Gen_max)`，使蝙蝠步长从"大步流星"的全局搜索逐渐过渡到"小步慢走"的精细搜索。正好解决Ackley函数"平坦外区域需要大步长、陡峭中心盆地需要小步长"的特点。

### 2. 离群度精英选择
不再简单贪心选适应度最小的个体，而是：
- 对前bestSize个候选精英计算离群度（与其他精英的欧氏距离均值）
- 归一化后评分：`score = ackley_norm - outlier_norm`
- 找score最小的个体作为全局最优

这样在适应度和多样性之间取得平衡，避免陷入局部最优。

### 3. bestSize动态调整
精英集合大小随种群平均响度动态变化：`bestSize = bestSize * (avgLoudness / initialLoudness)`，并限制在[1, POP_SIZE/2]范围内。

## 依赖

- Apache Commons Math 3.6.1
- XChart 3.x
- SLF4J 2.0.9
- JUnit 5.9.3 (测试)

## 构建

```bash
mvn clean install
```

## 使用示例

### 对比运行
```java
// 直接运行 main 类，会自动同时执行优化版和原始版并在同一张图上对比
com.gitee.BA_Ackley.server.main.main(null);
```

### 单独使用Ackley函数
```java
import com.gitee.BA_Ackley.utils.AckleyUtils;
import org.apache.commons.math3.linear.ArrayRealVector;

ArrayRealVector testVector = new ArrayRealVector(new double[]{0.5, -0.3, 0.8, -1.2});
double fitness = AckleyUtils.Ackley(testVector);
System.out.println("Ackley函数值: " + fitness);
```

### 使用RealVectorUtils
```java
import com.gitee.BA_Ackley.utils.RealVectorUtils;

// 生成5维随机向量
ArrayRealVector randomVector = RealVectorUtils.randomRealVector(5);

// 生成指定范围的随机向量
ArrayRealVector rangedVector = RealVectorUtils.randomRealVector(3, -10.0, 10.0);

// 计算两个向量的欧氏距离
double dist = RealVectorUtils.euclideanDistance(obj1, obj2, MyClass::getPosition);
```
