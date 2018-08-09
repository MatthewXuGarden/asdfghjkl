package com.carel.supervisor.plugin.energy;

import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;
import com.carel.supervisor.dataaccess.language.*;
import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;

import java.awt.Color;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class EnergyReportPDF {
	private static final int MARGIN_LEFT = 50;
	private static final int MARGIN_RIGHT = 40;
	private static final int MARGIN_TOP = 40;
	private static final int MARGIN_BOTTOM = 40;
	private static final int GRAPH_WIDTH = 750;
	private static final int GRAPH_HEIGHT = 250;
	private static final float TABLE_WIDTH = 100f; // width in percents
	private static final float TABLE_PADDING = 2f;
	private static final String TABLE_TRUNC = ".";
	
	private UserSession userSession;
	private UserTransaction userTransaction;
	private LangService lang;
	private String temp;	// temp folder for graph images
	private Document doc; 	// pdf document
	private PdfWriter pdf;	// pdf writer
	private Font font;		// pdf font
	private Font font_b;	// pdf font bold
	private java.awt.Font font_g = new java.awt.Font("Times Roman", 0, 9); // graph font
	private java.awt.Font font_gs = new java.awt.Font("Times Roman", 0, 8); // graph small font
	private ArrayList<String> files = new ArrayList<String>(); // temp graph images to be removed after pdf creation
	private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	
	
	public EnergyReportPDF(UserSession userSession, OutputStream out)
	{
		this.userSession = userSession;
		userTransaction = userSession.getCurrentUserTransaction();
		lang = LangMgr.getInstance().getLangService(userSession.getLanguage());
		temp = BaseConfig.getCarelPath() + "PvPro\\TempExports";
		font = new Font(Font.TIMES_ROMAN, 8);
		font_b = new Font(Font.TIMES_ROMAN, 8);
		font_b.setStyle(Font.BOLD);
		doc = new Document(PageSize.A4.rotate(), MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);
		try {
			//FileOutputStream out = new FileOutputStream(new File(temp + "\\energy" + (Long)userTransaction.getAttribute("timevalue") + ".pdf"));
			pdf = PdfWriter.getInstance(doc, out);
			
			// header
			addHeader();
			// footer
			addFooter();
			// content
			doc.open();
			addConsumptionPercentage();
			doc.newPage();
			addConsumptionKW();
			doc.newPage();
			addConsumptionKWh();
			doc.close();
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		clean();
	}
	
	
	private void addHeader()
	{
		String text = EnergyMgr.getInstance().getSiteConfiguration().getSiteProperty("header", "");
		EnergyReport report = (EnergyReport)userTransaction.getAttribute("report");
		// selection
		String root = (String)userTransaction.getAttribute("root");
		EnergyReportRecord record = null;
		if( root.equalsIgnoreCase("site") )
			record = report.getReportRecord("site");
		else if( root.equalsIgnoreCase("group") )
			record = report.getReportRecord("group" + (Integer)userTransaction.getAttribute("igroup"));
		else if( root.equalsIgnoreCase("cons") )
			record = report.getReportRecord("cons" + (Integer)userTransaction.getAttribute("igroup") + "." + (Integer)userTransaction.getAttribute("icons"));
		String selection = record.getName().equals("---") ? lang.getString("energy", "enersite") : record.getName();
		// period
		String type = "";
		String period = "";
		switch( report.getType() ) {
		case 1:
			type = lang.getString("energy","energrpstandardweeklyreport");
			period = sdfDate.format(report.getBegin()) + " - " + sdfDate.format(report.getEnd());
			break;
		case 2:
			type = lang.getString("energy","energrpstandardmonthlyreport");
			period = sdfDate.format(report.getBegin()) + " - " + sdfDate.format(report.getEnd());
			break;
		case 3:
			type = lang.getString("energy","energrpstandardyearlyreport");
			period = sdfYear.format(report.getBegin());
			break;
		}
		HeaderFooter header = new HeaderFooter(
			new Phrase((text.isEmpty()
				? userSession.getSiteName() + " - " + selection
				: text)
				+ " - " + type + ": " + period,
			font),
		false);
		header.setBorder(Rectangle.NO_BORDER);
		doc.setHeader(header);
	}
	
	
	private void addFooter()
	{
		String text = EnergyMgr.getInstance().getSiteConfiguration().getSiteProperty("footer", "");
		HeaderFooter footer = new HeaderFooter(new Phrase("", font),
			new Phrase(text.isEmpty() ? "" : " - " + text, font));
		footer.setBorder(Rectangle.NO_BORDER);
		doc.setFooter(footer);
	}
	
	
	private void addConsumptionPercentage()
		throws Exception
	{
		Paragraph paragraph = new Paragraph();
		//paragraph.setKeepTogether(true);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		// sub title
		paragraph.add(new Paragraph(lang.getString("energy", "energrpconsumptionperc")));
		
		// graph
		Long timevalue = (Long)userTransaction.getAttribute("timevalue");
		String graphName = "st2pie" + timevalue;
		DefaultPieDataset dataset = (DefaultPieDataset)userTransaction.getAttribute(graphName);
		JFreeChart jfreechart = ChartFactory.createPieChart(null, dataset, false, true, true);
		jfreechart.setBackgroundPaint(Color.WHITE);
		if( jfreechart.getTitle() != null )
			jfreechart.getTitle().setFont(font_g);
		if( jfreechart.getLegend() != null )
			jfreechart.getLegend().setItemFont(font_gs);
		PiePlot plot = (PiePlot)jfreechart.getPlot();
		((StandardPieSectionLabelGenerator)plot.getLabelGenerator()).getNumberFormat().setMaximumFractionDigits(1);
		String graphFileName = temp + "\\" + graphName + ".jpg";
		files.add(graphFileName);
		File graphFile = new File(graphFileName);
		FileOutputStream graphOutStream = new FileOutputStream(graphFile);
		ChartUtilities.writeChartAsJPEG(graphOutStream, jfreechart, GRAPH_WIDTH, GRAPH_HEIGHT);
		graphOutStream.close();
		paragraph.add(new Jpeg(graphFile.toURI().toURL()));
		
		// line break
		paragraph.add(new Paragraph(""));
		
		// table (header)
		EnergyReport report = (EnergyReport)userTransaction.getAttribute("report");
		String root = (String)userTransaction.getAttribute("root");
		Table table = new Table(6);
		table.setWidth(TABLE_WIDTH);
		table.setPadding(TABLE_PADDING);
		Cell cell = null;
		cell = new Cell(new Chunk(lang.getString("energy", "energroup"), font_b));
		cell.setShowTruncation(TABLE_TRUNC);
		cell.setMaxLines(1);
		cell.setHeader(true);
		table.addCell(cell);
		cell = new Cell(new Chunk("%", font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);	
		table.addCell(cell);
		cell = new Cell(new Chunk(lang.getString("energy", "enerkw"), font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);	
		table.addCell(cell);
		cell = new Cell(new Chunk(lang.getString("energy", "enerkwh"), font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);	
		table.addCell(cell);
		cell = new Cell(new Chunk(lang.getString("energy", "enerkgco2"), font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);	
		table.addCell(cell);
		cell = new Cell(new Chunk(lang.getString("energy", "enercost"), font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);	
		table.addCell(cell);
		// table (data)
		EnergyReportRecord record = null;
		if(root.equalsIgnoreCase("site"))
			record = report.getReportRecord("site");
		else if(root.equalsIgnoreCase("group") || root.equalsIgnoreCase("cons"))
			record = report.getReportRecord("group" + (Integer)userTransaction.getAttribute("igroup"));
		if( record != null && !root.equals("cons") ) {
			cell = new Cell(new Chunk(record.getName().equals("---") ? lang.getString("energy", "enersite") : record.getName(), font));
			cell.setShowTruncation(TABLE_TRUNC);
			table.addCell(cell);
			cell = new Cell(new Chunk("", font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***"	: EGUtils.formatkwh(record.getKwh()), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getKgco2().equals(Float.NaN) ? "***" : EGUtils.formatkgco2(record.getKgco2()), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			cell = new Cell(new Chunk(record.getCost().equals(Float.NaN) ? "***" : EGUtils.formatcost(record.getCost()).toString(), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
		}
		EnergyReportRecord rootrecord = record;
		String level1 = "";
		Integer nItems = 0;
		if( root.equalsIgnoreCase("site") ) {
			level1 = "group";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
		}
		else if( root.startsWith("group") ) {
			level1 = "cons";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
		}
		else if( root.startsWith("cons") ) {
			root = "group";
			level1="cons";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
		}
		for(int i = 1; i <= nItems; i++) {
			if( root.equalsIgnoreCase("site") ) {
				record = report.getReportRecord("group" + i);
			} else if( root.equalsIgnoreCase("group") ) {
				record = report.getReportRecord("cons" + (Integer)userTransaction.getAttribute("igroup") + "." + i);
			}
			if( record != null ) {
				cell = new Cell(new Chunk(record.getName(), font));
				cell.setShowTruncation(TABLE_TRUNC);
				table.addCell(cell);
				cell = new Cell(new Chunk(new Float(record.getKw()/rootrecord.getKw()).isNaN() ? "***" : EGUtils.formatperc(record.getKw()/rootrecord.getKw() * 100), font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
				cell = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
				cell = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***"	: EGUtils.formatkwh(record.getKwh()), font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
				cell = new Cell(new Chunk(record.getKgco2().equals(Float.NaN) ? "***" : EGUtils.formatkgco2(record.getKgco2()), font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
				cell = new Cell(new Chunk(record.getCost().equals(Float.NaN) ? "***" : EGUtils.formatcost(record.getCost()).toString(), font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
			}
		}
		record = report.getReportRecord("other");
		if( record != null ) {
			cell = new Cell(new Chunk(lang.getString("energy", "other"), font));
			cell.setShowTruncation(TABLE_TRUNC);
			cell.setMaxLines(1);
			table.addCell(cell);
			cell = new Cell(new Chunk(new Float(record.getKw()/rootrecord.getKw()).isNaN() ? "***" : EGUtils.formatperc(record.getKw()/rootrecord.getKw() * 100), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***"	: EGUtils.formatkwh(record.getKwh()), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getKgco2().equals(Float.NaN) ? "***" : EGUtils.formatkgco2(record.getKgco2()), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
			cell = new Cell(new Chunk(record.getCost().equals(Float.NaN) ? "***" : EGUtils.formatcost(record.getCost()).toString(), font));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			table.addCell(cell);
		}
		paragraph.add(table);
		
		doc.add(paragraph);
	}
	
	
	private void addConsumptionKW()
		throws Exception
	{
		Paragraph paragraph = new Paragraph();
		//paragraph.setKeepTogether(true);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		// sub title
		paragraph.add(new Paragraph(lang.getString("energy","energrpconsumptiondetailkw")));
		
		// graph
		Long timevalue = (Long)userTransaction.getAttribute("timevalue");
		String graphName = "st2barA" + timevalue;
		DefaultCategoryDataset dataset = (DefaultCategoryDataset)userTransaction.getAttribute(graphName);
		JFreeChart jfreechart = ChartFactory.createLineChart(null, null, "kW", dataset, PlotOrientation.VERTICAL, true, true, false);
		jfreechart.setBackgroundPaint(Color.WHITE);
		if( jfreechart.getTitle() != null )
			jfreechart.getTitle().setFont(font_g);
		if( jfreechart.getLegend() != null )
			jfreechart.getLegend().setItemFont(font_gs);
		((CategoryPlot)jfreechart.getPlot()).getDomainAxis().setTickLabelFont(font_gs);		
		((CategoryPlot)jfreechart.getPlot()).getRangeAxis().setTickLabelFont(font_gs);		
		String graphFileName = temp + "\\" + graphName + ".jpg";
		files.add(graphFileName);
		File graphFile = new File(graphFileName);
		FileOutputStream graphOutStream = new FileOutputStream(graphFile);
		ChartUtilities.writeChartAsJPEG(graphOutStream, jfreechart, GRAPH_WIDTH, GRAPH_HEIGHT);
		graphOutStream.close();
		paragraph.add(new Jpeg(graphFile.toURI().toURL()));
		
		// line break
		paragraph.add(new Paragraph(""));
		
		// table (header)
		EnergyReport report = (EnergyReport)userTransaction.getAttribute("report");
		String root = (String)userTransaction.getAttribute("root");
		Table table = new Table(1 + report.getIntervalsNumber() + 1);
		table.setWidth(TABLE_WIDTH);
		table.setPadding(TABLE_PADDING);
		Cell cell = null;
		cell = new Cell(new Chunk(lang.getString("energy", "energroup"), font_b));
		cell.setShowTruncation(TABLE_TRUNC);
		cell.setMaxLines(1);
		cell.setHeader(true);
		table.addCell(cell);
		for(Integer i = 1; i <= report.getIntervalsNumber(); i++) {
			cell = new Cell(new Chunk(i.toString(), font_b));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			cell.setHeader(true);
			table.addCell(cell);
		}
		cell = new Cell(new Chunk(lang.getString("energy", "avg"), font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);
		table.addCell(cell);
		// table (data)
		EnergyReportRecord record = null;
		if(root.equalsIgnoreCase("site"))
			record = report.getReportRecord("site");
		else if(root.equalsIgnoreCase("group") || root.equalsIgnoreCase("cons"))
			record = report.getReportRecord("group" + (Integer)userTransaction.getAttribute("igroup"));
		EnergyReportRecord rootrecord = record;
		String level1 = "";
		Integer nItems = 0;
		if( root.equalsIgnoreCase("site") ) {
			level1 = "group";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
		}
		else if( root.startsWith("group") ) {
			level1 = "cons";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
		}
		else if( root.startsWith("cons") ) {
			root = "group";
			level1="cons";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
		}
		for(int i = 1; i <= nItems; i++) {
			if( root.equalsIgnoreCase("site") ) {
				record = report.getReportRecord("group" + i);
			} else if( root.equalsIgnoreCase("group") ) {
				record = report.getReportRecord("cons" + (Integer)userTransaction.getAttribute("igroup") + "." + i);
			}
			if( record != null ) {
				cell = new Cell(new Chunk(record.getName(), font));
				cell.setShowTruncation(TABLE_TRUNC);
				cell.setMaxLines(1);
				table.addCell(cell);
				Cell cellLast = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
				cellLast.setHorizontalAlignment(Cell.ALIGN_CENTER);
				for(Integer iday = 1; iday <= report.getIntervalsNumber(); iday++) {
					String dayName = "";
					String level = "";
					if( root.equalsIgnoreCase("site") ) {
						level = "group";
					} else if(root.equalsIgnoreCase("group") ) {
						level = "cons" + (Integer)userTransaction.getAttribute("igroup") + ".";
					}
					dayName = "d" + iday +"." + level + i;
					record = report.getReportRecord(dayName);
					if( record != null )
						cell = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
					else
						cell = new Cell(new Chunk("***", font));
					cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
					table.addCell(cell);
				}
				table.addCell(cellLast);
			}
		}
		record = report.getReportRecord("other");
		if( record != null ) {
			cell = new Cell(new Chunk(lang.getString("energy", "other"), font));
			cell.setShowTruncation(TABLE_TRUNC);
			cell.setMaxLines(1);
			table.addCell(cell);
			Cell cellLast = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
			cellLast.setHorizontalAlignment(Cell.ALIGN_CENTER);
			for(Integer iday = 1; iday <= report.getIntervalsNumber(); iday++) {
				String dayName = "d" + iday + ".other";
				record = report.getReportRecord(dayName);
				if( record != null )
					cell = new Cell(new Chunk(record.getKw().equals(Float.NaN) ? "***" : EGUtils.formatkw(record.getKw()), font));
				else
					cell = new Cell(new Chunk("***", font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
			}
			table.addCell(cellLast);
		}
		paragraph.add(table);
		
		doc.add(paragraph);
	}
	
	
	private void addConsumptionKWh()
		throws Exception
	{
		Paragraph paragraph = new Paragraph();
		//paragraph.setKeepTogether(true);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		// sub title
		paragraph.add(new Paragraph(lang.getString("energy","energrpconsumptiodetailkwh")));
		
		// graph
		Long timevalue = (Long)userTransaction.getAttribute("timevalue");
		String graphName = "st2barB" + timevalue;
		DefaultCategoryDataset dataset = (DefaultCategoryDataset) userTransaction.getAttribute(graphName);
		JFreeChart jfreechart = ChartFactory.createStackedBarChart(null, null, "kWh", dataset, PlotOrientation.VERTICAL, true, true, false);
		jfreechart.setBackgroundPaint(Color.WHITE);
		if( jfreechart.getTitle() != null )
			jfreechart.getTitle().setFont(font_g);
		if( jfreechart.getLegend() != null )
			jfreechart.getLegend().setItemFont(font_gs);
		((CategoryPlot)jfreechart.getPlot()).getDomainAxis().setTickLabelFont(font_gs);
		((CategoryPlot)jfreechart.getPlot()).getRangeAxis().setTickLabelFont(font_gs);		
		String graphFileName = temp + "\\" + graphName + ".jpg";
		files.add(graphFileName);
		File graphFile = new File(graphFileName);
		FileOutputStream graphOutStream = new FileOutputStream(graphFile);
		ChartUtilities.writeChartAsJPEG(graphOutStream, jfreechart, GRAPH_WIDTH, GRAPH_HEIGHT);
		graphOutStream.close();
		paragraph.add(new Jpeg(graphFile.toURI().toURL()));
		
		// line break
		paragraph.add(new Paragraph(""));
		
		// table (header)
		EnergyReport report = (EnergyReport)userTransaction.getAttribute("report");
		String root = (String)userTransaction.getAttribute("root");
		Table table = new Table(1 + report.getIntervalsNumber() + 1);
		table.setWidth(TABLE_WIDTH);
		table.setPadding(TABLE_PADDING);
		Cell cell = null;
		cell = new Cell(new Chunk(lang.getString("energy", "energroup"), font_b));
		cell.setShowTruncation(TABLE_TRUNC);
		cell.setMaxLines(1);
		cell.setHeader(true);
		table.addCell(cell);
		for(Integer i = 1; i <= report.getIntervalsNumber(); i++) {
			cell = new Cell(new Chunk(i.toString(), font_b));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			cell.setHeader(true);
			table.addCell(cell);
		}
		cell = new Cell(new Chunk(lang.getString("energy", "enertot"), font_b));
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setHeader(true);
		table.addCell(cell);
		// table (data)
		EnergyReportRecord record = null;
		if(root.equalsIgnoreCase("site"))
			record = report.getReportRecord("site");
		else if(root.equalsIgnoreCase("group") || root.equalsIgnoreCase("cons"))
			record = report.getReportRecord("group" + (Integer)userTransaction.getAttribute("igroup"));
		EnergyReportRecord rootrecord = record;
		String level1 = "";
		Integer nItems = 0;
		if( root.equalsIgnoreCase("site") ) {
			level1 = "group";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
		}
		else if( root.startsWith("group") ) {
			level1 = "cons";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
		}
		else if( root.startsWith("cons") ) {
			root = "group";
			level1="cons";
			nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
		}
		for(int i = 1; i <= nItems; i++) {
			if( root.equalsIgnoreCase("site") ) {
				record = report.getReportRecord("group" + i);
			} else if( root.equalsIgnoreCase("group") ) {
				record = report.getReportRecord("cons" + (Integer)userTransaction.getAttribute("igroup") + "." + i);
			}
			if( record != null ) {
				cell = new Cell(new Chunk(record.getName(), font));
				cell.setShowTruncation(TABLE_TRUNC);
				cell.setMaxLines(1);
				table.addCell(cell);
				Cell cellLast = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh()), font));
				cellLast.setHorizontalAlignment(Cell.ALIGN_CENTER);
				for(Integer iday = 1; iday <= report.getIntervalsNumber(); iday++) {
					String dayName = "";
					String level = "";
					if( root.equalsIgnoreCase("site") ) {
						level = "group";
					} else if(root.equalsIgnoreCase("group") ) {
						level = "cons" + (Integer)userTransaction.getAttribute("igroup") + ".";
					}
					dayName = "d" + iday +"." + level + i;
					record = report.getReportRecord(dayName);
					if( record != null )
						cell = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh()), font));
					else
						cell = new Cell(new Chunk("***", font));
					cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
					table.addCell(cell);
					
				}
				table.addCell(cellLast);
			}
		}
		record = report.getReportRecord("other");
		if( record != null ) {
			cell = new Cell(new Chunk(lang.getString("energy", "other"), font));
			cell.setShowTruncation(TABLE_TRUNC);
			cell.setMaxLines(1);
			table.addCell(cell);
			Cell cellLast = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh()), font));
			cellLast.setHorizontalAlignment(Cell.ALIGN_CENTER);
			for(Integer iday = 1; iday <= report.getIntervalsNumber(); iday++) {
				String dayName = "d" + iday + ".other";
				record = report.getReportRecord(dayName);
				if( record != null )
					cell = new Cell(new Chunk(record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh()), font));
				else
					cell = new Cell(new Chunk("***", font));
				cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
				table.addCell(cell);
			}
			table.addCell(cellLast);
		}
		paragraph.add(table);
		
		doc.add(paragraph);
	}
	
	
	private void clean()
	{
		for(int i = 0; i < files.size(); i++) {
			File file = new File(files.get(i));
			file.delete();
		}
	}
}
