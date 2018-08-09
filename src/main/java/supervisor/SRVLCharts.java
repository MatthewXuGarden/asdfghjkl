package supervisor;
import java.awt.Color;
import java.io.*;
import java.text.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.plugin.energy.EnergyMgr;
import com.carel.supervisor.plugin.energy.EnergyProfile;
import com.carel.supervisor.plugin.optimum.StartStopLog;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;

public class SRVLCharts extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static final String separator = "          ";

	public SRVLCharts() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
		UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		UserTransaction userTransaction = userSession.getCurrentUserTransaction();
		OutputStream outChart = response.getOutputStream();
		try 
		{
			response.setContentType("image/png");
			int width = new Integer(request.getParameter("width")).intValue();
			int height = new Integer(request.getParameter("height")).intValue();
			String id = request.getParameter("imgid");
			String charttype = request.getParameter("charttype");
			if("piechart".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				DefaultPieDataset dataset = (DefaultPieDataset) userTransaction.getAttribute(id);
//				LoggerMgr.getLogger(this.getClass()).info("dataset-elements: "+(dataset!=null?dataset.getItemCount():"null"));
				JFreeChart jfreechart = ChartFactory.createPieChart(um, dataset, false, true, true);
				PiePlot plot = (PiePlot)jfreechart.getPlot();
				// single fraction digit
				((StandardPieSectionLabelGenerator)plot.getLabelGenerator()).getNumberFormat().setMaximumFractionDigits(1);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			else if("piechart_timeslot_color".equalsIgnoreCase(charttype))
			{
				DefaultPieDataset dataset = (DefaultPieDataset) userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createPieChart(null, dataset, false, true, false);
				PiePlot plot = (PiePlot)jfreechart.getPlot();
				EnergyProfile ep = EnergyMgr.getInstance().getEnergyProfile();
				for(int i = 0; i < EnergyProfile.TIMESLOT_NO; i++)
					plot.setSectionPaint(i, Color.decode(ep.getTimeSlot(i).getColor()));
				// single fraction digit
				((StandardPieSectionLabelGenerator)plot.getLabelGenerator()).getNumberFormat().setMaximumFractionDigits(1);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			} 
			else if("barchart".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = "kWh"; 
				String dtf = request.getParameter("dtf");
				String dtt = request.getParameter("dtt");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
//				LoggerMgr.getLogger(this.getClass()).info("dataset-elements: "+(dataset!=null?dataset.getColumnCount():"null"));
				JFreeChart jfreechart = ChartFactory.createStackedBarChart(null,dtf+separator+dtt,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			// used on meter device page
			else if("smallbarchart".equalsIgnoreCase(charttype))
			{
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
				JFreeChart chart = ChartFactory.createStackedBarChart(null,null,null,dataset,PlotOrientation.VERTICAL,false,false,false);
				chart.setBackgroundPaint(Color.BLACK);
				CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
				CategoryPlot plot = renderer.getPlot();
				plot.setBackgroundPaint(Color.BLACK);
				plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
				plot.getRangeAxis().setTickLabelPaint(Color.WHITE);
		        ChartUtilities.writeChartAsPNG(outChart, chart, width, height);
			} 
			else if("barchart_timeslot_color".equalsIgnoreCase(charttype))
			{
				String dtf = request.getParameter("dtf");
				String dtt = request.getParameter("dtt");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createStackedBarChart(null,dtf+separator+dtt,"kWh",dataset,PlotOrientation.VERTICAL,true,true,false);
				CategoryItemRenderer renderer = jfreechart.getCategoryPlot().getRenderer();
				EnergyProfile ep = EnergyMgr.getInstance().getEnergyProfile();
				for(int i = 0; i < EnergyProfile.TIMESLOT_NO; i++)
					renderer.setSeriesPaint(i, Color.decode(ep.getTimeSlot(i).getColor())); 
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			else if("line".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = "kW";	
				String dtf = request.getParameter("dtf");
				String dtt = request.getParameter("dtt");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
//				LoggerMgr.getLogger(this.getClass()).info("dataset-elements: "+(dataset!=null?dataset.getColumnCount():"null"));
				JFreeChart jfreechart = ChartFactory.createLineChart(null,dtf+separator+dtt,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			else if("linechart".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = "";
				String dtf = request.getParameter("dtf");
				String dtt = request.getParameter("dtt");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createLineChart(null,dtf+separator+dtt,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
				LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
				renderer.setShapesVisible(true);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			// custom chart used by Optimum Start&Stop and NightFreeCooling
			else if("linechart_optimum".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = "";
				String xlabel = request.getParameter("xlabel");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createLineChart(null,xlabel,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
				plot.getDomainAxis().setCategoryMargin(0.4);
				plot.getDomainAxis().setTickLabelFont(new java.awt.Font("Arial", 0, 9));
				plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(2.0f);
				plot.getDomainAxis().setMaximumCategoryLabelLines(1);
				
				DecimalFormat format = new DecimalFormat("0.0");
				((NumberAxis)plot.getRangeAxis()).setNumberFormatOverride(format);
				plot.getRangeAxis().setLabelInsets(new RectangleInsets(7.5, 10.0, 10.0, 7.5));
				plot.getRangeAxis().setAutoRangeMinimumSize(1);
				
				LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
				renderer.setSeriesPaint(0, Color.decode(StartStopLog.COL_InternalTemperature));
				renderer.setSeriesPaint(1, Color.decode(StartStopLog.COL_ExternalTemperature));
				renderer.setShapesVisible(true);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			// custom chart used by Optimum NightFreeCooling
			/*else if("linechart_nfc".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = "";
				String xlabel = request.getParameter("xlabel");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createLineChart(null,xlabel,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
				LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
				renderer.setShapesVisible(true);
				BasicStroke stroke = new BasicStroke(
					1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
					1.0f, new float[] {3.0f, 3.0f}, 0.0f
				);
				renderer.setSeriesPaint(1, renderer.getSeriesPaint(0));
				renderer.setSeriesStroke(1, stroke);
				renderer.setSeriesPaint(3, renderer.getSeriesPaint(2));
				renderer.setSeriesStroke(3, stroke);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}*/
			// custom chart used by Optimum Lights
			else if("linechart_l".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = "";
				String xlabel = request.getParameter("xlabel");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createLineChart(null,xlabel,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
				NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
				rangeAxis.setRange(0.0, 24.0);
				rangeAxis.setTickUnit(new NumberTickUnit(1.0));
				LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
				renderer.setShapesVisible(false);
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			}
			// custom chart used by Optimum Start&Stop
			else if("simplebarchart".equalsIgnoreCase(charttype))
			{
				String um = request.getParameter("um");
				if( um == null )
					um = ""; 
				String xlabel = request.getParameter("xlabel");
				DefaultCategoryDataset dataset = (DefaultCategoryDataset)userTransaction.getAttribute(id);
				JFreeChart jfreechart = ChartFactory.createBarChart(null,xlabel,um,dataset,PlotOrientation.VERTICAL,true,true,false);
				CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();
				// less space between bars
				//plot.getDomainAxis().setCategoryMargin(0.4);
				plot.getDomainAxis().setTickLabelFont(new java.awt.Font("Arial", 0, 9));
				
				DecimalFormat format = new DecimalFormat("0");
				plot.getRangeAxis().setLabelInsets(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
				((NumberAxis)plot.getRangeAxis()).setNumberFormatOverride(format);
				plot.getRangeAxis().setAutoRangeMinimumSize(20);
				plot.getRangeAxis().setLowerBound(0);
				
				String series0Color = request.getParameter("series0color");
				String series1Color = request.getParameter("series1color");
				
				CategoryItemRenderer renderer = plot.getRenderer();
				if(series0Color!=null)
					renderer.setSeriesPaint(0, Color.decode("#"+series0Color));
				else
					renderer.setSeriesPaint(0, Color.decode(StartStopLog.COL_AnticipatedTimeStart));
				if(series1Color!=null)
					renderer.setSeriesPaint(1, Color.decode(series1Color));
				else
					renderer.setSeriesPaint(1, Color.decode(StartStopLog.COL_AnticipatedTimeStop));
				
				ChartUtilities.writeChartAsPNG(outChart, jfreechart, width, height);
			} 
			else 
			{
				LoggerMgr.getLogger(this.getClass()).warn("Chart not supported");
			}
		} 
		catch (Exception e) 
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		} 
		finally 
		{
			outChart.flush();
			outChart.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}
}
