package com.gitee.BA_Ackley.utils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class RealVectorUtils {
    private static final Logger log = LoggerFactory.getLogger(RealVectorUtils.class);
    private static final String filePath = "src/main/resources/config/BAConfig.properties";
    private static long randomSeed;//随机数种子，便于复现结果
    private static Random random;//随机数生成器
    static {
        //读取配置文件
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            properties.load(fis);
            randomSeed = Long.parseLong(properties.getProperty("ba.random.seed"));
            random = new Random(randomSeed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            log.info("配置文件读取完毕");
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private RealVectorUtils(){}

    public static ArrayRealVector randomRealVector(int n){
        ArrayRealVector arrayRealVector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            arrayRealVector.setEntry(i, random.nextDouble());
        }
        log.info("生成向量：{}",arrayRealVector);
        return arrayRealVector;
    }

    public static ArrayRealVector randomRealVector(int n,double min,double max){
        ArrayRealVector arrayRealVector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            arrayRealVector.setEntry(i, random.nextDouble() * (max - min) + min);
        }
        log.info("生成向量：{}",arrayRealVector);
        return arrayRealVector;
    }

    public static ArrayRealVector randomRealVector(int n,double[] min,double[] max){
        ArrayRealVector arrayRealVector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            arrayRealVector.setEntry(i, random.nextDouble() * (max[i] - min[i]) + min[i]);
        }
        log.info("生成向量：{}",arrayRealVector);
        return arrayRealVector;
    }

    public static ArrayRealVector randomEpsilon(int n){
        ArrayRealVector arrayRealVector = new ArrayRealVector(n);
        for (int i = 0; i < n; i++) {
            arrayRealVector.setEntry(i, random.nextDouble() * 0.2 - 0.1);
        }
        log.info("生成向量：{}",arrayRealVector);
        return arrayRealVector;
    }
    public static ArrayRealVector bound(ArrayRealVector arrayRealVector, double[] lb, double[] ub){
        int n = arrayRealVector.getDimension();
        for (int i = 0; i < n; i++) {
            arrayRealVector.setEntry(i, Math.min(Math.max(arrayRealVector.getEntry(i), lb[i]), ub[i]));
        }
        log.info("边界处理后的向量：{}",arrayRealVector);
        return arrayRealVector;
    }

    public static ArrayRealVector copy(ArrayRealVector arrayRealVector){
        return new ArrayRealVector(arrayRealVector);
    }

    //找出适应度最强的个体
    public static ArrayRealVector best(ArrayRealVector[] arrayRealVectors){
        int n = arrayRealVectors.length;
        ArrayRealVector best = arrayRealVectors[0];
        for (int i = 1; i < n; i++) {
            //计算Ackley
            if (AckleyUtils.Ackley(best) > AckleyUtils.Ackley(arrayRealVectors[i])){
                best = arrayRealVectors[i];
            }
        }
        log.info("适应度最强的向量：{}",best);
        return best;
    }

}
