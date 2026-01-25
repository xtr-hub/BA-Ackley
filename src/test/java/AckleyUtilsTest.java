import com.gitee.BA_Ackley.utils.AckleyUtils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AckleyUtils类的单元测试
 */
class AckleyUtilsTest {

    /**
     * 测试零向量：[0.0, 0.0, 0.0]
     * 预期结果：0.0
     */
    @Test
    void testAckley_ZeroVector() {
        ArrayRealVector vector = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        double result = AckleyUtils.Ackley(vector);

        assertEquals(0.0, result, 1e-10, "零向量的Ackley函数值应为0");
    }

    /**
     * 测试单元素向量：[1.0]
     * 计算过程：
     * - n = 1
     * - sum1 = 1^2 = 1
     * - sum2 = cos(2π×1) = cos(2π) = 1
     * - term1 = -20 × exp(-0.2 × √(1/1)) = -20 × exp(-0.2) ≈ -16.3746
     * - term2 = -exp(1/1) = -exp(1) ≈ -2.7183
     * - term3 = e + 20 ≈ 2.7183 + 20 = 22.7183
     * - 结果 ≈ -16.3746 - 2.7183 + 22.7183 = 3.6254
     */
    @Test
    void testAckley_SingleElement() {
        ArrayRealVector vector = new ArrayRealVector(new double[]{1.0});
        double result = AckleyUtils.Ackley(vector);

        double expected = -20 * Math.exp(-0.2 * Math.sqrt(1.0))
                - Math.exp(1.0)
                + Math.E + 20;
        assertEquals(expected, result, 1e-10, "单元素向量的Ackley函数值应正确");
    }

    /**
     * 测试二维向量：[1.0, 1.0]
     * 计算过程：
     * - n = 2
     * - sum1 = 1^2 + 1^2 = 2
     * - sum2 = cos(2π×1) + cos(2π×1) = 1 + 1 = 2
     * - term1 = -20 × exp(-0.2 × √(2/2)) = -20 × exp(-0.2) ≈ -16.3746
     * - term2 = -exp(2/2) = -exp(1) ≈ -2.7183
     * - term3 = e + 20 ≈ 22.7183
     * - 结果 ≈ -16.3746 - 2.7183 + 22.7183 = 3.6254
     */
    @Test
    void testAckley_TwoElements() {
        ArrayRealVector vector = new ArrayRealVector(new double[]{1.0, 1.0});
        double result = AckleyUtils.Ackley(vector);

        double expected = -20 * Math.exp(-0.2 * Math.sqrt(2.0 / 2.0))
                - Math.exp(2.0 / 2.0)
                + Math.E + 20;
        assertEquals(expected, result, 1e-10, "二维向量的Ackley函数值应正确");
    }

    /**
     * 测试三维向量：[1.0, 2.0, 3.0]
     * 计算过程：
     * - n = 3
     * - sum1 = 1^2 + 2^2 + 3^2 = 1 + 4 + 9 = 14
     * - sum2 = cos(2π×1) + cos(2π×2) + cos(2π×3) = 1 + 1 + 1 = 3
     * - term1 = -20 × exp(-0.2 × √(14/3)) ≈ -20 × exp(-0.2 × 2.1602) ≈ -20 × exp(-0.4320) ≈ -13.858
     * - term2 = -exp(3/3) = -exp(1) ≈ -2.7183
     * - term3 = e + 20 ≈ 22.7183
     * - 结果 ≈ -13.858 - 2.7183 + 22.7183 = 6.142
     */
    @Test
    void testAckley_ThreeElements() {
        ArrayRealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        double result = AckleyUtils.Ackley(vector);

        double expected = -20 * Math.exp(-0.2 * Math.sqrt(14.0 / 3.0))
                - Math.exp(3.0 / 3.0)
                + Math.E + 20;
        assertEquals(expected, result, 1e-10, "三维向量的Ackley函数值应正确");
    }

    /**
     * 测试包含负数的向量：[-1.0, -2.0, -3.0]
     * 由于cos函数是偶函数，cos(2π×(-x)) = cos(2π×x)，所以结果与正数相同
     */
    @Test
    void testAckley_NegativeValues() {
        ArrayRealVector vector = new ArrayRealVector(new double[]{-1.0, -2.0, -3.0});
        double result = AckleyUtils.Ackley(vector);

        // 应该与正数情况相同
        double expected = -20 * Math.exp(-0.2 * Math.sqrt(14.0 / 3.0))
                - Math.exp(3.0 / 3.0)
                + Math.E + 20;
        assertEquals(expected, result, 1e-10, "包含负数的向量的Ackley函数值应正确");
    }
}
