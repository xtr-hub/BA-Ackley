package com.gitee.BA_Ackley.core;

import com.gitee.BA_Ackley.entity.Batis;
import com.gitee.BA_Ackley.utils.AckleyUtils;
import com.gitee.BA_Ackley.utils.RealVectorUtils;
import com.gitee.BA_Ackley.utils.XYChartUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.knowm.xchart.XYChart;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class OriginMain {

    private static final String SERIES_NAME = "原始蝙蝠算法";
    private static final int DIMENSION = 10;
    private static final int POP_SIZE = 80;
    private static final int MAX_ITERATIONS = 500;
    private static final double LOWER_BOUND = -32.768;
    private static final double UPPER_BOUND = 32.768;

    private static double frequencyMin;
    private static double frequencyMax;
    private static double loudnessDecay;
    private static double pulseRateGamma;
    private static double initialLoudness;
    private static double initialPulseRate;
    private static long randomSeed;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config/BAConfig.properties")) {
            properties.load(fis);
            frequencyMin = Double.parseDouble(properties.getProperty("ba.frequency.min"));
            frequencyMax = Double.parseDouble(properties.getProperty("ba.frequency.max"));
            loudnessDecay = Double.parseDouble(properties.getProperty("ba.loudness.decay.a"));
            pulseRateGamma = Double.parseDouble(properties.getProperty("ba.pulse.rate.gamma"));
            initialLoudness = Double.parseDouble(properties.getProperty("ba.loudness.initial.a0"));
            initialPulseRate = Double.parseDouble(properties.getProperty("ba.pulse.rate.initial.r0"));
            randomSeed = Long.parseLong(properties.getProperty("ba.random.seed"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(XYChart chart) {
        // 重置随机种子，确保初始种群与优化版完全一致
        Random random = new Random(randomSeed);

        Batis[] population = initializePopulation(random);
        Batis globalBest = findGlobalBest(population);

        XYChartUtils.addSeries(chart, SERIES_NAME, 0, globalBest.getAckleyValue());

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            for (int i = 0; i < POP_SIZE; i++) {
                Batis bat = population[i];

                double frequency = frequencyMin + (frequencyMax - frequencyMin) * random.nextDouble();

                ArrayRealVector velocityUpdate = (ArrayRealVector) globalBest.getAnswer().subtract(bat.getAnswer())
                        .mapMultiply(frequency);
                ArrayRealVector newVelocity = (ArrayRealVector) bat.getSpeed().add(velocityUpdate);
                bat.setSpeed(newVelocity);

                // 原始位置更新：x = x + v，无自适应系数 G
                ArrayRealVector newPosition = (ArrayRealVector) bat.getAnswer().add(newVelocity);

                // 局部搜索
                if (random.nextDouble() > bat.getPulseRate()) {
                    ArrayRealVector epsilon = RealVectorUtils.randomEpsilon(DIMENSION);
                    double avgLoudness = calculateAverageLoudness(population);
                    newPosition = (ArrayRealVector) globalBest.getAnswer().add(epsilon.mapMultiply(avgLoudness));
                }

                newPosition = RealVectorUtils.boundPosition(newPosition, LOWER_BOUND, UPPER_BOUND);
                double newFitness = AckleyUtils.Ackley(newPosition);

                if (newFitness < bat.getAckleyValue() && random.nextDouble() < bat.getLoudness()) {
                    bat.setAnswer(RealVectorUtils.copy(newPosition));
                    bat.setAckleyValue(newFitness);
                    bat.setLoudness(bat.getLoudness() * loudnessDecay);
                    bat.setPulseRate(initialPulseRate * (1 - Math.exp(-pulseRateGamma * iteration)));
                }

                // 简单贪心更新全局最优
                if (bat.getAckleyValue() < globalBest.getAckleyValue()) {
                    globalBest.setAnswer(RealVectorUtils.copy(bat.getAnswer()));
                    globalBest.setAckleyValue(bat.getAckleyValue());
                    globalBest.setSpeed(RealVectorUtils.copy(bat.getSpeed()));
                    globalBest.setLoudness(bat.getLoudness());
                    globalBest.setPulseRate(bat.getPulseRate());
                }
            }

            if ((iteration + 1) % 10 == 0) {
                XYChartUtils.add(chart, SERIES_NAME, iteration + 1, globalBest.getAckleyValue());
            }
        }
    }

    private static Batis findGlobalBest(Batis[] population) {
        Batis best = population[0];
        for (int i = 1; i < population.length; i++) {
            if (population[i].getAckleyValue() < best.getAckleyValue()) {
                best = population[i];
            }
        }
        return new Batis(
                RealVectorUtils.copy(best.getAnswer()),
                RealVectorUtils.copy(best.getSpeed()),
                best.getLoudness(),
                best.getPulseRate(),
                best.getAckleyValue());
    }

    private static Batis[] initializePopulation(Random random) {
        Batis[] population = new Batis[POP_SIZE];
        for (int i = 0; i < POP_SIZE; i++) {
            ArrayRealVector position = RealVectorUtils.randomRealVector(random, DIMENSION, LOWER_BOUND, UPPER_BOUND);
            ArrayRealVector velocity = RealVectorUtils.randomRealVector(random, DIMENSION, -1.0, 1.0);
            population[i] = new Batis(position, velocity, initialLoudness, initialPulseRate, AckleyUtils.Ackley(position));
        }
        return population;
    }

    private static double calculateAverageLoudness(Batis[] population) {
        double sum = 0.0;
        for (Batis bat : population) {
            sum += bat.getLoudness();
        }
        return sum / population.length;
    }
}
