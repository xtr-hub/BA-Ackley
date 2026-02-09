package com.gitee.BA_Ackley.entity;

import org.apache.commons.math3.linear.ArrayRealVector;

//蝙蝠类
public class batis {
    //每个蝙蝠对应的答案
    private ArrayRealVector answer;
    //速度
    private ArrayRealVector speed;
    //响度
    private double loudness;
    //脉冲率
    private double pulseRate;
    //适应度，也就是ackley函数的值
    private double ackleyValue;

    public batis() {
    }

    public batis(ArrayRealVector answer, ArrayRealVector speed, double loudness, double pulseRate, double ackleyValue) {
        this.answer = answer;
        this.speed = speed;
        this.loudness = loudness;
        this.pulseRate = pulseRate;
        this.ackleyValue = ackleyValue;
    }

    /**
     * 获取
     * @return answer
     */
    public ArrayRealVector getAnswer() {
        return answer;
    }

    /**
     * 设置
     * @param answer
     */
    public void setAnswer(ArrayRealVector answer) {
        this.answer = answer;
    }

    /**
     * 获取
     * @return speed
     */
    public ArrayRealVector getSpeed() {
        return speed;
    }

    /**
     * 设置
     * @param speed
     */
    public void setSpeed(ArrayRealVector speed) {
        this.speed = speed;
    }

    /**
     * 获取
     * @return loudness
     */
    public double getLoudness() {
        return loudness;
    }

    /**
     * 设置
     * @param loudness
     */
    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    /**
     * 获取
     * @return pulseRate
     */
    public double getPulseRate() {
        return pulseRate;
    }

    /**
     * 设置
     * @param pulseRate
     */
    public void setPulseRate(double pulseRate) {
        this.pulseRate = pulseRate;
    }

    /**
     * 获取
     * @return ackleyValue
     */
    public double getAckleyValue() {
        return ackleyValue;
    }

    /**
     * 设置
     * @param ackleyValue
     */
    public void setAckleyValue(double ackleyValue) {
        this.ackleyValue = ackleyValue;
    }

    public String toString() {
        return "batis{answer = " + answer + ", speed = " + speed + ", loudness = " + loudness + ", pulseRate = " + pulseRate + ", ackleyValue = " + ackleyValue + "}";
    }
}

