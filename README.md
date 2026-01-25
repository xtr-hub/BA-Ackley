# BA-Ackley

BA-Ackley是一个基于Apache Commons Math实现Ackley函数和蝙蝠算法(Bat Algorithm)相关工具的Java库，用于优化算法测试。

## API文档

### AckleyUtils类

#### `public static double Ackley(ArrayRealVector arrayRealVector)`

计算向量的Ackley函数值。

**参数:**
- `arrayRealVector` - [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，输入的实数向量

**返回值:**
- `double`类型，计算得到的Ackley函数值

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

#### `public static ArrayRealVector randomRealVector(int n)`

生成指定维度的随机向量，元素值在[0,1)范围内。

**参数:**
- `n` - 向量维度

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，生成的随机向量

**示例:**
```java
ArrayRealVector vector = RealVectorUtils.randomRealVector(5);
```

#### `public static ArrayRealVector randomRealVector(int n, double min, double max)`

生成指定维度的随机向量，元素值在[min,max)范围内。

**参数:**
- `n` - 向量维度
- `min` - 最小值
- `max` - 最大值

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，生成的随机向量

**示例:**
```java
ArrayRealVector vector = RealVectorUtils.randomRealVector(5, -5.0, 5.0);
```

#### `public static ArrayRealVector randomRealVector(int n, double[] min, double[] max)`

生成指定维度的随机向量，每个维度有独立的范围。

**参数:**
- `n` - 向量维度
- `min` - 各维度最小值数组
- `max` - 各维度最大值数组

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，生成的随机向量

**示例:**
```java
double[] min = {-5.0, -3.0};
double[] max = {5.0, 3.0};
ArrayRealVector vector = RealVectorUtils.randomRealVector(2, min, max);
```

#### `public static ArrayRealVector randomEpsilon(int n)`

生成指定维度的小随机向量，元素值在[-0.1, 0.1)范围内。

**参数:**
- `n` - 向量维度

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，生成的小随机向量

**示例:**
```java
ArrayRealVector vector = RealVectorUtils.randomEpsilon(3);
```

#### `public static ArrayRealVector bound(ArrayRealVector arrayRealVector, double[] lb, double[] ub)`

对向量进行边界约束处理。

**参数:**
- `arrayRealVector` - 待处理的向量
- `lb` - 下界数组
- `ub` - 上界数组

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，边界处理后的向量

**示例:**
```java
double[] lowerBounds = {-5.0, -5.0};
double[] upperBounds = {5.0, 5.0};
ArrayRealVector boundedVector = RealVectorUtils.bound(vector, lowerBounds, upperBounds);
```

#### `public static ArrayRealVector copy(ArrayRealVector arrayRealVector)`

复制向量。

**参数:**
- `arrayRealVector` - 待复制的向量

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，复制的新向量

**示例:**
```java
ArrayRealVector copiedVector = RealVectorUtils.copy(originalVector);
```

#### `public static ArrayRealVector best(ArrayRealVector[] arrayRealVectors)`

从向量数组中找出适应度最高的向量（即Ackley函数值最小的向量）。

**参数:**
- `arrayRealVectors` - 向量数组

**返回值:**
- [ArrayRealVector](file://C:\Users\37863\IdeaProjects\BA-Ackley/src/main/java/com/gitee/BA_Ackley/utils/AckleyUtils.java#L15-L33)类型，适应度最高的向量

**示例:**
```java
ArrayRealVector[] population = {vector1, vector2, vector3};
ArrayRealVector bestVector = RealVectorUtils.best(population);
```

## 配置参数

### BA算法参数 (src/main/resources/config/BAConfig.properties)
```properties
# BA算法核心参数配置
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

# 随机数种子
ba.random.seed=1145141919810
```

## 依赖

- Apache Commons Math 3.6.1
- SLF4J 2.0.9
- JUnit 5.9.3 (测试)

## 构建

使用Maven构建项目：
```bash
mvn clean install
```

## 使用示例

### 在优化算法中使用Ackley函数作为测试函数
```java
import com.gitee.BA_Ackley.utils.AckleyUtils;
import org.apache.commons.math3.linear.ArrayRealVector;

// 创建测试向量
ArrayRealVector testVector = new ArrayRealVector(new double[]{0.5, -0.3, 0.8, -1.2});

// 计算Ackley函数值
double fitness = AckleyUtils.Ackley(testVector);
System.out.println("Ackley函数值: " + fitness);
```

### 生成随机向量
```java
import com.gitee.BA_Ackley.utils.RealVectorUtils;

// 生成5维随机向量
ArrayRealVector randomVector = RealVectorUtils.randomRealVector(5);

// 生成指定范围的随机向量
ArrayRealVector rangedVector = RealVectorUtils.randomRealVector(3, -10.0, 10.0);

// 生成带边界的随机向量
double[] minVals = {-5.0, -3.0, -2.0};
double[] maxVals = {5.0, 3.0, 2.0};
ArrayRealVector boundedRandomVector = RealVectorUtils.randomRealVector(3, minVals, maxVals);
```