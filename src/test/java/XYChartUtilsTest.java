
import com.gitee.BA_Ackley.utils.XYChartUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import static org.junit.jupiter.api.Assertions.*;

class XYChartUtilsTest {

    private XYChart chart;

    @Test
    void XYChartUtilsTest(){
        chart = XYChartUtils.create("测试图表", "X轴", "Y轴");
        XYChartUtils.add(chart, 1, 1);
    }
}