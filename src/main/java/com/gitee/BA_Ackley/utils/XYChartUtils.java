package com.gitee.BA_Ackley.utils;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.Method;

public class XYChartUtils {
    private static final Logger log = LoggerFactory.getLogger(XYChartUtils.class);
    private static final String DEFAULT_SERIES_NAME = "Default Series";

    // 缓存方法引用，避免每次反射开销
    private static Method addMethod = null;
    private static boolean methodChecked = false;

    /**
     * 创建并显示一个新的 XY 图表。
     */
    public static XYChart create(String title, String xAxisTitle, String yAxisTitle) {
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        log.info("创建二维图表: {}", title);
        
        // 添加空的数据系列，使用包含一个零值的数组以避免Y轴为空的错误
        chart.addSeries(DEFAULT_SERIES_NAME, new double[]{0.0}, new double[]{0.0});
        
        // 检查是否处于 headless 模式，如果是则不显示图表窗口
        if (!GraphicsEnvironment.isHeadless()) {
            new SwingWrapper<>(chart).displayChart();
        }
        
        return chart;
    }

    /**
     * 向默认数据系列追加一个数据点，兼容 XChart 2.x 和 3.x
     */
    public static void add(XYChart chart, double x, double y) {
        if (chart == null) {
            log.error("图表对象不能为null");
            return;
        }

        XYSeries series = chart.getSeriesMap().get(DEFAULT_SERIES_NAME);
        if (series == null) {
            log.error("未找到默认数据系列: {}", DEFAULT_SERIES_NAME);
            return;
        }

        try {
            if (!methodChecked) {
                try {
                    addMethod = XYSeries.class.getMethod("addData", double.class, double.class);
                    methodChecked = true;
                } catch (NoSuchMethodException e) {
                    log.warn("XYSeries.addData方法不存在，尝试使用updateXYSeries方法");
                    methodChecked = true;
                }
            }

            if (addMethod != null) {
                addMethod.invoke(series, x, y);
            } else {
                double[] xData = series.getXData();
                double[] yData = series.getYData();
                double[] newXData = new double[xData.length + 1];
                double[] newYData = new double[yData.length + 1];
                System.arraycopy(xData, 0, newXData, 0, xData.length);
                System.arraycopy(yData, 0, newYData, 0, yData.length);
                newXData[xData.length] = x;
                newYData[yData.length] = y;
                chart.updateXYSeries(DEFAULT_SERIES_NAME, newXData, newYData, null);
            }

            log.debug("添加数据点: ({}, {})", x, y);
        } catch (Exception e) {
            log.error("添加数据点失败: ({}, {})", x, y, e);
        }
    }
}