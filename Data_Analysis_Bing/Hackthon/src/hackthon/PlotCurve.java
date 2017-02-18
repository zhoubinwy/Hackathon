package hackthon;

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class PlotCurve extends ApplicationFrame {
	final XYSeries series = new XYSeries("Acc_x");
	
	public PlotCurve (final String title, List<Float> nums) {
		super(title);

	    final XYSeriesCollection data = new XYSeriesCollection(series);
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        title,
	        "Time (s)", 
	        "Y", 
	        data,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );
	    
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);

		setXYData(nums);
		this.pack();
	}
	
	public void setXYData(List<Float> nums) {
		for (int i = 0; i < nums.size(); i++) {
			series.add(i / 50.0, nums.get(i));
		}		
	}
	
}
