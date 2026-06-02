package com.gitee.BA_Ackley.server;

import com.gitee.BA_Ackley.entity.batis;
import com.gitee.BA_Ackley.utils.AckleyUtils;
import com.gitee.BA_Ackley.utils.RealVectorUtils;
import com.gitee.BA_Ackley.utils.XYChartUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class main {
    private static final Logger log = LoggerFactory.getLogger(main.class);
    private static final String CONFIG_FILE_PATH = "src/main/resources/config/BAConfig.properties";
    
    // BA算法参数
    private static double frequencyMin;
    private static double frequencyMax;
    private static double loudnessDecay;
    private static double pulseRateGamma;
    private static double initialLoudness;
    private static double initialPulseRate;
    private static long randomSeed;
    private static Random random;
    
    // 问题参数
    private static final int DIMENSION = 10; // 问题维度
    private static final int POP_SIZE = 40; // 种群数量
    private static final int MAX_ITERATIONS = 10000; // 最大迭代次数
    private static final double LOWER_BOUND = -32.768; // Ackley函数下界
    private static final double UPPER_BOUND = 32.768; // Ackley函数上界
    
    static {
        loadConfig();
    }
    
    /**
     * 加载配置文件
     */
    private static void loadConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            frequencyMin = Double.parseDouble(properties.getProperty("ba.frequency.min"));
            frequencyMax = Double.parseDouble(properties.getProperty("ba.frequency.max"));
            loudnessDecay = Double.parseDouble(properties.getProperty("ba.loudness.decay.a"));
            pulseRateGamma = Double.parseDouble(properties.getProperty("ba.pulse.rate.gamma"));
            initialLoudness = Double.parseDouble(properties.getProperty("ba.loudness.initial.a0"));
            initialPulseRate = Double.parseDouble(properties.getProperty("ba.pulse.rate.initial.r0"));
            randomSeed = Long.parseLong(properties.getProperty("ba.random.seed"));
            random = new Random(randomSeed);
            log.info("配置文件加载成功");
        } catch (IOException e) {
            log.error("配置文件加载失败", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        log.info("========== 蝙蝠算法优化Ackley函数开始 ==========");
        log.info("问题维度: {}, 种群大小: {}, 最大迭代次数: {}", DIMENSION, POP_SIZE, MAX_ITERATIONS);
        log.info("搜索空间: [{}, {}]", LOWER_BOUND, UPPER_BOUND);
        
        // 创建图表用于可视化优化过程
        XYChart chart = XYChartUtils.create(
            "蝙蝠算法优化Ackley函数", 
            "迭代次数", 
            "最优适应度值"
        );
        
        // 初始化蝙蝠种群
        batis[] population = initializePopulation();
        
        // 找到初始最优解
        //batis globalBest = findGlobalBest(population);
        batis globalBest = RealVectorUtils.best(population, (a, b) -> a.getAckleyValue() < b.getAckleyValue());
        log.info("初始最优适应度值: {}", globalBest.getAckleyValue());
        
        // 迭代优化
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            // 遍历每只蝙蝠
            for (int i = 0; i < POP_SIZE; i++) {
                batis bat = population[i];
                
                // 生成随机频率 [frequencyMin, frequencyMax]
                double frequency = frequencyMin + (frequencyMax - frequencyMin) * random.nextDouble();
                
                // 更新速度：v_i = v_i + (x_i - x_best) * frequency
                ArrayRealVector velocityUpdate = (ArrayRealVector) globalBest.getAnswer().subtract(bat.getAnswer())
                    .mapMultiply(frequency);
                ArrayRealVector newVelocity = (ArrayRealVector) bat.getSpeed().add(velocityUpdate);
                bat.setSpeed(newVelocity);
                
                // 更新位置：x_i = x_i + v_i
                ArrayRealVector newPosition = (ArrayRealVector) bat.getAnswer().add(newVelocity);
                
                // 局部搜索：如果随机数大于脉冲率，在最优解附近进行随机游走
                if (random.nextDouble() > bat.getPulseRate()) {
                    // 生成局部扰动 epsilon ∈ [-0.1, 0.1]
                    ArrayRealVector epsilon = RealVectorUtils.randomEpsilon(DIMENSION);
                    // 计算平均响度
                    double avgLoudness = calculateAverageLoudness(population);
                    // 在最优解附近随机游走：x_new = x_best + epsilon * avgLoudness
                    newPosition = (ArrayRealVector) globalBest.getAnswer().add(epsilon.mapMultiply(avgLoudness));
                }
                
                // 边界处理
                newPosition = boundPosition(newPosition);
                
                // 计算新位置的适应度值
                double newFitness = AckleyUtils.Ackley(newPosition);
                
                // 如果新解更优且随机数小于响度，接受新解
                if (newFitness < bat.getAckleyValue() && random.nextDouble() < bat.getLoudness()) {
                    bat.setAnswer(RealVectorUtils.copy(newPosition));
                    bat.setAckleyValue(newFitness);
                    
                    // 更新响度和脉冲率
                    bat.setLoudness(bat.getLoudness() * loudnessDecay);
                    bat.setPulseRate(initialPulseRate * (1 - Math.exp(-pulseRateGamma * iteration)));
                }
                
                // 更新全局最优解
                if (bat.getAckleyValue() < globalBest.getAckleyValue()) {
                    globalBest.setAnswer(RealVectorUtils.copy(bat.getAnswer()));
                    globalBest.setAckleyValue(bat.getAckleyValue());
                    globalBest.setSpeed(RealVectorUtils.copy(bat.getSpeed()));
                    globalBest.setLoudness(bat.getLoudness());
                    globalBest.setPulseRate(bat.getPulseRate());
                    log.info("第{}次迭代找到更优解，适应度值: {}", iteration + 1, globalBest.getAckleyValue());
                }
            }
            
            // 每10次迭代记录一次到图表
            if ((iteration + 1) % 10 == 0) {
                XYChartUtils.add(chart, iteration + 1, globalBest.getAckleyValue());
                log.info("第{}次迭代，当前最优适应度值: {}", iteration + 1, globalBest.getAckleyValue());
            }
        }
        
        // 输出最终结果
        log.info("========== 优化完成 ==========");
        log.info("最优适应度值: {}", globalBest.getAckleyValue());
        log.info("最优解向量: {}", globalBest.getAnswer());
        
        // 理论上Ackley函数的最小值在原点(0,0,...,0)处为0
        log.info("理论最优值: 0.0");
        log.info("优化精度: {}", Math.abs(globalBest.getAckleyValue() - 0.0));
    }
    
    /**
     * 初始化蝙蝠种群
     */
    private static batis[] initializePopulation() {
        log.info("正在初始化种群...");
        batis[] population = new batis[POP_SIZE];
        
        for (int i = 0; i < POP_SIZE; i++) {
            // 随机初始化位置
            ArrayRealVector position = RealVectorUtils.randomRealVector(
                DIMENSION, LOWER_BOUND, UPPER_BOUND
            );
            
            // 随机初始化速度（较小的初始速度）
            ArrayRealVector velocity = RealVectorUtils.randomRealVector(
                DIMENSION, -1.0, 1.0
            );
            
            // 计算适应度值
            double fitness = AckleyUtils.Ackley(position);
            
            // 创建蝙蝠个体
            population[i] = new batis(
                position,
                velocity,
                initialLoudness,
                initialPulseRate,
                fitness
            );
        }
        
        log.info("种群初始化完成");
        return population;
    }
    
    /**
     * 找到种群中的全局最优个体
     */
    private static batis findGlobalBest(batis[] population) {
        batis best = population[0];
        for (int i = 1; i < population.length; i++) {
            if (population[i].getAckleyValue() < best.getAckleyValue()) {
                best = population[i];
            }
        }
        // 返回副本，避免引用污染
        return new batis(
            RealVectorUtils.copy(best.getAnswer()),
            RealVectorUtils.copy(best.getSpeed()),
            best.getLoudness(),
            best.getPulseRate(),
            best.getAckleyValue()
        );
    }
    
    /**
     * 计算种群平均响度
     */
    private static double calculateAverageLoudness(batis[] population) {
        double sum = 0.0;
        for (batis bat : population) {
            sum += bat.getLoudness();
        }
        return sum / population.length;
    }
    
    /**
     * 边界处理：将超出边界的值限制在合法范围内
     */
    private static ArrayRealVector boundPosition(ArrayRealVector position) {
        ArrayRealVector bounded = RealVectorUtils.copy(position);
        for (int i = 0; i < position.getDimension(); i++) {
            double value = bounded.getEntry(i);
            if (value < LOWER_BOUND) {
                bounded.setEntry(i, LOWER_BOUND);
            } else if (value > UPPER_BOUND) {
                bounded.setEntry(i, UPPER_BOUND);
            }
        }
        return bounded;
    }
}
