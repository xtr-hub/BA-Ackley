package com.gitee.BA_Ackley.utils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Math;



public class AckleyUtils {

    private static final Logger log = LoggerFactory.getLogger(AckleyUtils.class);
    private AckleyUtils(){};

    public static double Ackley(ArrayRealVector arrayRealVector){
        double sum1 = 0;//sqrt((1/n)*sum(xi*2))
        double sum2 = 0;//sum(cos(2*pi*xi))
        int n = 0;//向量维度
        n = arrayRealVector.getDimension();
        log.info("向量维度为：{}",n);

        for (int i = 0; i < n; i++) {
            double xi = arrayRealVector.getEntry(i);
            sum1 += xi * xi;//xi*2
            sum2 += Math.cos(2 * Math.PI * xi);//cos(2*pi*xi)
        }

        double term1 = -20 * Math.exp(-0.2 * Math.sqrt(sum1 / n));
        double term2 = -Math.exp(sum2 / n);
        double term3 = Math.E + 20;

        return term1 + term2 + term3;
    }
}
