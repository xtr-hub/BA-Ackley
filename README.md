# BA-Ackley

BA-Ackley是一个基于Apache Commons Math实现Ackley函数的Java库。

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

## 依赖

- Apache Commons Math 3.6.1
- SLF4J 2.0.9
- JUnit 5.9.3 (测试)

## 构建

使用Maven构建项目：
```bash
mvn clean install
```