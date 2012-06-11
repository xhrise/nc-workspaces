/**
 * 
 */
package com.ufida.report.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Stroke;

import nc.ui.reportquery.component.chart.ChartProperties;
import nc.ui.reportquery.component.chart.IChartConst;
import nc.ui.reportquery.component.chart.RQChartFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.Legend;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisConstants;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;

import com.thoughtworks.xstream.XStream;
import com.ufsoft.iufo.fmtplugin.chart.ChartConstants;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;

/**
 * @author wangyga
 * 图表属性值存贮类
 * @created at 2009-8-19,上午10:47:50
 *
 */
public class ChartPropertiesValues {

	public static final int DOMAIN = 0;
    public static final int RANGE = 1;
    public static final int COLORBAR = 2;
    
    public static final Paint DEFAULT_OUTLINE_PAINT = StandardLegend.DEFAULT_OUTLINE_PAINT;
    public static final Stroke DEFAULT_OUTLINE_STROKE = StandardLegend.DEFAULT_OUTLINE_STROKE;
    public static final Paint DEFAULT_LEGEND_BACKGROUND_PAINT = StandardLegend.DEFAULT_BACKGROUND_PAINT;
    public static final Font DEFAULT_LEGEND_ITEM_FONT = StandardLegend.DEFAULT_ITEM_FONT;
    public static final Paint DEFAULT_LEGEND_ITEM_PAINT = Color.BLACK;
    
    public static final Paint DEFAULT_PLOT_BACKGROUND_PAINT = Color.LIGHT_GRAY;
    public static final Insets DEFAULT_PLOT_INSETS = Plot.DEFAULT_INSETS;//new Insets(4, 8, 4, 8);
    
    public static final Font DEFAULT_AXIS_LABEL_FONT = RQChartFactory.DEFAULT_AXIS_LABEL_FONT;
    public static final Paint DEFAULT_AXIS_LABEL_PAINT = AxisConstants.DEFAULT_AXIS_LABEL_PAINT;
    public static final Font DEFAULT_TICK_LABEL_FONT = RQChartFactory.DEFAULT_TICK_LABEL_FONT;
    public static final Paint DEFAULT_TICK_LABEL_PAINT = AxisConstants.DEFAULT_TICK_LABEL_PAINT;
    public static final Insets DEFAULT_AXIS_LABEL_INSETS = AxisConstants.DEFAULT_AXIS_LABEL_INSETS;
    public static final Insets DEFAULT_TICK_LABEL_INSETS = AxisConstants.DEFAULT_TICK_LABEL_INSETS;
    
    public static final Paint DEFAULT_CA_BACKGROUND_PAINT = new GradientPaint(0, 0, Color.WHITE, 1000, 1000, Color.GREEN);
    public static final Paint DEFAULT_PIE_BACKGROUND_PAINT = new GradientPaint(0, 0, Color.WHITE, 1000, 0, Color.YELLOW);
    public static final Paint DEFAULT_METER_BACKGROUND_PAINT = new GradientPaint(0, 0, Color.WHITE, 0, 1000, Color.BLUE);
    private static XStream xStream;
        
    private int chartType = ChartConstants.CHART_BAR;
    
    //Legend Properties begin
    private Paint legendOutlinePaint = DEFAULT_OUTLINE_PAINT;
    private Stroke legendOutlineStroke = DEFAULT_OUTLINE_STROKE;
    private Paint legendBackgroundPaint = DEFAULT_LEGEND_BACKGROUND_PAINT;
    private Font legendItemFont = DEFAULT_LEGEND_ITEM_FONT;
    private Paint legendItemPaint = DEFAULT_LEGEND_ITEM_PAINT;
    //Legend Properties end
    
    //Plot Properties begin
    private Paint plotOutlinePaint = DEFAULT_OUTLINE_PAINT;
    private Stroke plotOutlineStroke = DEFAULT_OUTLINE_STROKE;
    private Paint plotBackgroundPaint = DEFAULT_PLOT_BACKGROUND_PAINT;
    private Insets plotInsets = DEFAULT_PLOT_INSETS;
    //Plot Properties end
    
    //Domain Axis Properties begin
    private String domainLabel;
	private Font domainLabelFont = DEFAULT_AXIS_LABEL_FONT;
	private Paint domainLabelPaint = DEFAULT_AXIS_LABEL_PAINT;
	private boolean domainTickMarksVisible = true;
	// private Stroke domainTickMarkStroke;
	private boolean domainTickLabelsVisible = true;
	private Font domainTickLabelFont = DEFAULT_TICK_LABEL_FONT;
	private Paint domainTickLabelPaint = DEFAULT_TICK_LABEL_PAINT;
	private Insets domainTickLabelInsets = DEFAULT_TICK_LABEL_INSETS;
	private Insets domainLabelInsets = DEFAULT_AXIS_LABEL_INSETS;
    //Domain Axis Properties end
    
    //Range Axis Properties begin
	private String rangeLabel;
	private Font rangeLabelFont = DEFAULT_AXIS_LABEL_FONT;
	private Paint rangeLabelPaint = DEFAULT_AXIS_LABEL_PAINT;
	private boolean rangeTickMarksVisible = true;
	// private Stroke rangeTickMarkStroke;
	private boolean rangeTickLabelsVisible = true;
	private Font rangeTickLabelFont = DEFAULT_TICK_LABEL_FONT;
	private Paint rangeTickLabelPaint = DEFAULT_TICK_LABEL_PAINT;
	private Insets rangeTickLabelInsets = DEFAULT_TICK_LABEL_INSETS;
	private Insets rangeLabelInsets = DEFAULT_AXIS_LABEL_INSETS;
		//Value Field Properties
	private  boolean autoRange=true;
	private   double lowerBound;
    private  double upperBound;
	//Range Axis Properties end
    
    //Common Properties begin
    private boolean antiAlias = true;
    private Paint backgroundPaint = DEFAULT_CA_BACKGROUND_PAINT;
    //Common Properties end
    
    public ChartPropertiesValues(){
		
	}

    public void updateChartProperties(ChartModel model) {
        JFreeChart chart = model.getChart();
        chartType = ChartConstants.getParentType(model.getType());
		if (chartType != ChartConstants.CHART_METER) {//只有仪表图没有图例
			setLegendProperties(chart.getLegend());
		}

		updatePlotProperties(chart.getPlot());
		
		chart.setAntiAlias(antiAlias);
		chart.setBackgroundPaint(backgroundPaint);
	}
    
    private void updatePlotProperties(Plot plot) {

		// set the plot properties...
		plot.setOutlinePaint(plotOutlinePaint);
		plot.setOutlineStroke(plotOutlineStroke);
		plot.setBackgroundPaint(plotBackgroundPaint);
		plot.setInsets(plotInsets);

		// then the axis properties...
		if (chartType == ChartConstants.CHART_BAR) {
			Axis domainAxis = null;
			if (plot instanceof CategoryPlot) {
				CategoryPlot p = (CategoryPlot) plot;
				domainAxis = p.getDomainAxis();
			}
			else if (plot instanceof XYPlot) {
				XYPlot p = (XYPlot) plot;
				domainAxis = p.getDomainAxis();
			}
			if (domainAxis != null) {
				setAxisProperties(domainAxis, DOMAIN);
			}
		}

		if (chartType == ChartConstants.CHART_BAR) {
			Axis rangeAxis = null;
			if (plot instanceof CategoryPlot) {
				CategoryPlot p = (CategoryPlot) plot;
				rangeAxis = p.getRangeAxis();
			}
			else if (plot instanceof XYPlot) {
				XYPlot p = (XYPlot) plot;
				rangeAxis = p.getRangeAxis();
			}
			if (rangeAxis != null) {
				setAxisProperties(rangeAxis, RANGE);
			}
		}
	}
    
    private void setLegendProperties(Legend legend) {
		if (legend instanceof StandardLegend) {
			// only supports StandardLegend at present
			StandardLegend standard = (StandardLegend) legend;
			standard.setOutlineStroke(legendOutlineStroke);
			standard.setOutlinePaint(legendOutlinePaint);
			standard.setBackgroundPaint(legendBackgroundPaint);
			standard.setItemFont(legendItemFont);
			standard.setItemPaint(legendItemPaint);
		}
		else {
			// raise exception - unrecognised legend
		}
	}
    
    private void setAxisProperties(Axis axis, int mode) {
		if (mode == DOMAIN) {
			axis.setLabel(domainLabel);
			axis.setLabelFont(domainLabelFont);
			axis.setLabelPaint(domainLabelPaint);
			axis.setTickMarksVisible(domainTickMarksVisible);
			// axis.setTickMarkStroke(domainTickMarkStroke);
			axis.setTickLabelsVisible(domainTickLabelsVisible);
			axis.setTickLabelFont(domainTickLabelFont);
			axis.setTickLabelPaint(domainTickLabelPaint);
			axis.setTickLabelInsets(domainTickLabelInsets);
			axis.setLabelInsets(domainLabelInsets);
		} else if (mode == RANGE) {
			axis.setLabel(rangeLabel);
			axis.setLabelFont(rangeLabelFont);
			axis.setLabelPaint(rangeLabelPaint);
			axis.setTickMarksVisible(rangeTickMarksVisible);
			// axis.setTickMarkStroke(rangeTickMarkStroke);
			axis.setTickLabelsVisible(rangeTickLabelsVisible);
			axis.setTickLabelFont(rangeTickLabelFont);
			axis.setTickLabelPaint(rangeTickLabelPaint);
			axis.setTickLabelInsets(rangeTickLabelInsets);
			axis.setLabelInsets(rangeLabelInsets);
			if(axis instanceof NumberAxis){
				NumberAxis nAxis=(NumberAxis)axis;
				nAxis.setAutoRange(autoRange);
				if (!autoRange) {
					nAxis.setLowerBound(lowerBound);
					nAxis.setUpperBound(upperBound);
				}
			}
		}
	}
    
    public void getProperties(ChartModel model){
        JFreeChart chart = model.getChart();
        chartType = ChartConstants.getParentType(model.getType());
        if (chartType != ChartConstants.CHART_METER) {//只有仪表图没有图例
			getLegendProperties(chart.getLegend());
		}

		getPlotProperties(chart.getPlot());
		antiAlias = chart.getAntiAlias();
		backgroundPaint = chart.getBackgroundPaint();
    }
    
    private void getLegendProperties(Legend legend) {
		if (legend instanceof StandardLegend) {
			// only supports StandardLegend at present
			StandardLegend standard = (StandardLegend) legend;
			legendOutlineStroke = standard.getOutlineStroke();
			legendOutlinePaint = standard.getOutlinePaint();
			legendBackgroundPaint = standard.getBackgroundPaint();
			legendItemFont = standard.getItemFont();
			legendItemPaint = standard.getItemPaint();
		}
		else {
			// raise exception - unrecognised legend
		}
	}
    
    private void getPlotProperties(Plot plot) {
		// set the plot properties...
        plotOutlinePaint = plot.getOutlinePaint();
        plotOutlineStroke = plot.getOutlineStroke();
        plotBackgroundPaint = plot.getBackgroundPaint();
        plotInsets = plot.getInsets();

		// then the axis properties...
		if (chartType == ChartConstants.CHART_BAR) {
			Axis domainAxis = null;
			if (plot instanceof CategoryPlot) {
				CategoryPlot p = (CategoryPlot) plot;
				domainAxis = p.getDomainAxis();
			}
			else if (plot instanceof XYPlot) {
				XYPlot p = (XYPlot) plot;
				domainAxis = p.getDomainAxis();
			}
			if (domainAxis != null) {
				getAxisProperties(domainAxis, DOMAIN);
			}
		}

		if (chartType == ChartConstants.CHART_BAR) {
			Axis rangeAxis = null;
			if (plot instanceof CategoryPlot) {
				CategoryPlot p = (CategoryPlot) plot;
				rangeAxis = p.getRangeAxis();
			}
			else if (plot instanceof XYPlot) {
				XYPlot p = (XYPlot) plot;
				rangeAxis = p.getRangeAxis();
			}
			if (rangeAxis != null) {
				getAxisProperties(rangeAxis, RANGE);
			}
		}
	}
    
    private void getAxisProperties(Axis axis, int mode) {
		if (mode == DOMAIN) {
			domainLabel = axis.getLabel();
			domainLabelFont = axis.getLabelFont();
			domainLabelPaint = axis.getLabelPaint();
			domainTickMarksVisible = axis.isTickMarksVisible();
			// domainTickMarkStroke = axis.getTickMarkStroke();
			domainTickLabelsVisible = axis.isTickLabelsVisible();
			domainTickLabelFont = axis.getTickLabelFont();
			domainTickLabelPaint = axis.getTickLabelPaint();
			domainTickLabelInsets = axis.getTickLabelInsets();
			domainLabelInsets = axis.getLabelInsets();
		} else if (mode == RANGE) {
			rangeLabel = axis.getLabel();
			rangeLabelFont = axis.getLabelFont();
			rangeLabelPaint = axis.getLabelPaint();
			rangeTickMarksVisible = axis.isTickMarksVisible();
			// rangeTickMarkStroke = axis.getTickMarkStroke();
			rangeTickLabelsVisible = axis.isTickLabelsVisible();
			rangeTickLabelFont = axis.getTickLabelFont();
			rangeTickLabelPaint = axis.getTickLabelPaint();
			rangeTickLabelInsets = axis.getTickLabelInsets();
			rangeLabelInsets = axis.getLabelInsets();
			if (axis instanceof NumberAxis) {
				NumberAxis nAxis = (NumberAxis) axis;
				autoRange = nAxis.isAutoRange();
				if (!autoRange) {
					lowerBound = nAxis.getLowerBound();
					upperBound = nAxis.getUpperBound();
				}

			}
		}
	}
    
    private synchronized static XStream getXStream(){
        if (xStream == null) {
            xStream = new XStream();
            xStream.alias("ChartProp", ChartProperties.class);
            //xStream.setSimpleFldOutputAsAttr(true);
        }
        return xStream;
    }
    
    public String toXmlString(){
        XStream xStream_i = new XStream();
        String xml = xStream_i.toXML(this);
        return xml;
    }
    
    public static ChartPropertiesValues parseXmlString(String xmlStr){
    	ChartPropertiesValues chartProp = (ChartPropertiesValues) getXStream().fromXML(xmlStr);
        
        if (chartProp.legendOutlinePaint == null)
        	chartProp.legendOutlinePaint = DEFAULT_OUTLINE_PAINT;
        if (chartProp.legendOutlineStroke == null)
        	chartProp.legendOutlineStroke = DEFAULT_OUTLINE_STROKE;
        if (chartProp.legendBackgroundPaint == null)
        	chartProp.legendBackgroundPaint = DEFAULT_LEGEND_BACKGROUND_PAINT;
        if (chartProp.legendItemFont == null)
        	chartProp.legendItemFont = DEFAULT_LEGEND_ITEM_FONT;
        if (chartProp.legendItemPaint == null)
        	chartProp.legendItemPaint = DEFAULT_LEGEND_ITEM_PAINT;
        if (chartProp.plotOutlinePaint == null)
        	chartProp.plotOutlinePaint = DEFAULT_OUTLINE_PAINT;
        if (chartProp.plotOutlineStroke == null)
        	chartProp.plotOutlineStroke = DEFAULT_OUTLINE_STROKE;
        if (chartProp.plotBackgroundPaint == null)
        	chartProp.plotBackgroundPaint = DEFAULT_PLOT_BACKGROUND_PAINT;
        if (chartProp.plotInsets == null)
        	chartProp.plotInsets = DEFAULT_PLOT_INSETS;
        if (chartProp.domainLabelFont == null)
        	chartProp.domainLabelFont = DEFAULT_AXIS_LABEL_FONT;
        if (chartProp.domainLabelPaint == null)
        	chartProp.domainLabelPaint = DEFAULT_AXIS_LABEL_PAINT;
        if (chartProp.domainTickLabelFont == null)
        	chartProp.domainTickLabelFont = DEFAULT_TICK_LABEL_FONT;
        if (chartProp.domainTickLabelPaint == null)
        	chartProp.domainTickLabelPaint = DEFAULT_TICK_LABEL_PAINT;
        if (chartProp.domainTickLabelInsets == null)
        	chartProp.domainTickLabelInsets = DEFAULT_TICK_LABEL_INSETS;
        if (chartProp.domainLabelInsets == null)
        	chartProp.domainLabelInsets = DEFAULT_AXIS_LABEL_INSETS;
        if (chartProp.rangeLabelFont == null)
        	chartProp.rangeLabelFont = DEFAULT_AXIS_LABEL_FONT;
        if (chartProp.rangeLabelPaint == null)
        	chartProp.rangeLabelPaint = DEFAULT_AXIS_LABEL_PAINT;
        if (chartProp.rangeTickLabelFont == null)
        	chartProp.rangeTickLabelFont = DEFAULT_TICK_LABEL_FONT;
        if (chartProp.rangeTickLabelPaint == null)
        	chartProp.rangeTickLabelPaint = DEFAULT_TICK_LABEL_PAINT;
        if (chartProp.rangeTickLabelInsets == null)
        	chartProp.rangeTickLabelInsets = DEFAULT_TICK_LABEL_INSETS;
        if (chartProp.rangeLabelInsets == null)
        	chartProp.rangeLabelInsets = DEFAULT_AXIS_LABEL_INSETS;
        if (chartProp.backgroundPaint == null)
        	chartProp.backgroundPaint = getDefaultBackgroundPaint(chartProp.chartType);
        
        return chartProp;
    }
    
    protected boolean equalsDefault(String fieldName){
        if ("legendOutlinePaint".equals(fieldName)) {
            return DEFAULT_OUTLINE_PAINT.equals(legendOutlinePaint);
        }
        else if ("legendOutlineStroke".equals(fieldName)){
            return DEFAULT_OUTLINE_STROKE.equals(legendOutlineStroke);
        }
        else if ("legendBackgroundPaint".equals(fieldName)){
            return DEFAULT_LEGEND_BACKGROUND_PAINT.equals(legendBackgroundPaint);
        }
        else if ("legendItemFont".equals(fieldName)){
            return DEFAULT_LEGEND_ITEM_FONT.equals(legendItemFont);
        }
        else if ("legendItemPaint".equals(fieldName)){
            return DEFAULT_LEGEND_ITEM_PAINT.equals(legendItemPaint);
        }
        else if ("plotOutlinePaint".equals(fieldName)){
            return DEFAULT_OUTLINE_PAINT.equals(plotOutlinePaint);
        }
        else if ("plotOutlineStroke".equals(fieldName)){
            return DEFAULT_OUTLINE_STROKE.equals(plotOutlineStroke);
        }
        else if ("plotBackgroundPaint".equals(fieldName)){
            return DEFAULT_PLOT_BACKGROUND_PAINT.equals(plotBackgroundPaint);
        }
        else if ("plotInsets".equals(fieldName)){
            return DEFAULT_PLOT_INSETS.equals(plotInsets);
        }
        else if ("domainLabelFont".equals(fieldName)){
            return DEFAULT_AXIS_LABEL_FONT.equals(domainLabelFont);
        }
        else if ("domainLabelPaint".equals(fieldName)){
            return DEFAULT_AXIS_LABEL_PAINT.equals(domainLabelPaint);
        }
        else if ("domainTickLabelFont".equals(fieldName)){
            return DEFAULT_TICK_LABEL_FONT.equals(domainTickLabelFont);
        }
        else if ("domainTickLabelPaint".equals(fieldName)){
            return DEFAULT_TICK_LABEL_PAINT.equals(domainTickLabelPaint);
        }
        else if ("domainTickLabelInsets".equals(fieldName)){
            return DEFAULT_TICK_LABEL_INSETS.equals(domainTickLabelInsets);
        }
        else if ("domainLabelInsets".equals(fieldName)){
            return DEFAULT_AXIS_LABEL_INSETS.equals(domainLabelInsets);
        }
        else if ("rangeLabelFont".equals(fieldName)){
            return DEFAULT_AXIS_LABEL_FONT.equals(rangeLabelFont);
        }
        else if ("rangeLabelPaint".equals(fieldName)){
            return DEFAULT_AXIS_LABEL_PAINT.equals(rangeLabelPaint);
        }
        else if ("rangeTickLabelFont".equals(fieldName)){
            return DEFAULT_TICK_LABEL_FONT.equals(rangeTickLabelFont);
        }
        else if ("rangeTickLabelPaint".equals(fieldName)){
            return DEFAULT_TICK_LABEL_PAINT.equals(rangeTickLabelPaint);
        }
        else if ("rangeTickLabelInsets".equals(fieldName)){
            return DEFAULT_TICK_LABEL_INSETS.equals(rangeTickLabelInsets);
        }
        else if ("rangeLabelInsets".equals(fieldName)){
            return DEFAULT_AXIS_LABEL_INSETS.equals(rangeLabelInsets);
        }
        else if ("backgroundPaint".equals(fieldName)){
            return getDefaultBackgroundPaint(chartType).equals(backgroundPaint);
        }
        else {
            return false;
        }        
    }
    
    private static Paint getDefaultBackgroundPaint(int chartType){
        switch (chartType) {
            case IChartConst.METER:
                return DEFAULT_METER_BACKGROUND_PAINT;
            case IChartConst.PIE:                
                return DEFAULT_PIE_BACKGROUND_PAINT;
            case IChartConst.CA:                
                return DEFAULT_CA_BACKGROUND_PAINT;
            default:
                return Color.GRAY;
        }
    }
}
