package com.ufsoft.iuforeport.batchprint;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.DocumentName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;

public class IUFODoc implements Doc {
	/** ��ӡ�ĵ�������*/
	private DocFlavor flavor;
	/** �ĵ��Ĺ�����ӡ����*/
    private DocAttributeSet attributes;
    /** �ĵ�����*/
    private String documentName;
    /** ��ӡ����*/
    private UFOTable printData;
    /** ��ӡ�ĵ���ʽģ��*/
    private CellsModel formatModel;
    /** �ĵ�״̬ 0��׼�����ݣ�1�����ڴ�ӡ��2����ӡ���*/
    private boolean isPrinting;
    private static int units;

    static{
    	
        String country = Locale.getDefault().getCountry();
		if (country != null
				&& (country.equals("") || country.equals(Locale.US.getCountry()) || country
						.equals(Locale.CANADA.getCountry()))) {
			units = Size2DSyntax.INCH;
		}else{
			units = Size2DSyntax.MM;
		}
    }
    /**
     * ���췽���������µĴ�ӡ�ĵ�
     * @param name ��ӡ�ĵ�����
     * @param showModel ��ӡ�ĵ���ʾģ�ͣ�������ʽģ�ͺ�����ģ��
     */
    public IUFODoc(String name,CellsModel showModel){
    	this.flavor=DocFlavor.SERVICE_FORMATTED.PAGEABLE;  
    	this.documentName=name;
		this.attributes=new HashDocAttributeSet();
		this.attributes.add(new DocumentName(documentName,Locale.getDefault()));
		setFormatModel(showModel);
		setPrintData(createTableByModel(showModel));		
    }
    /**
     * ���췽����
     * @param name ��ӡ�ĵ�����
     * @param attributes �ĵ�����
     * @param formatModel ��ʽģ��
     */
	public IUFODoc(String name,DocAttributeSet attributes,CellsModel formatModel){
		this.flavor=DocFlavor.SERVICE_FORMATTED.PAGEABLE;    
		this.documentName=name;
		if(attributes!=null){
			this.attributes = AttributeSetUtilities.unmodifiableView(attributes);
		}else{
			this.attributes=new HashDocAttributeSet();
		}
		this.attributes.add(new DocumentName(documentName,Locale.getDefault()));
		setFormatModel(formatModel);
	}
    public void addDocAttributeFromPageFormat(){
    	if (formatModel != null) {
			PageFormat pgf = formatModel.getPrintSet().getPageformat();
			if (pgf.getOrientation() == PageFormat.LANDSCAPE) {
				attributes.add(OrientationRequested.LANDSCAPE);
			} else if (pgf.getOrientation() == PageFormat.PORTRAIT) {
				attributes.add(OrientationRequested.PORTRAIT);
			} else if (pgf.getOrientation() == PageFormat.REVERSE_LANDSCAPE) {
				attributes.add(OrientationRequested.REVERSE_LANDSCAPE);
			}
			double d = 72D;
			Paper pp = pgf.getPaper();
			MediaSize mediasize;
			MediaSizeName mediasizename = MediaSize.findMedia((float) (pp
					.getWidth() / d), (float) (pp.getHeight() / d),
					Size2DSyntax.INCH);
			attributes.remove(MediaSizeName.class);
			attributes.remove(MediaPrintableArea.class);

			mediasize = MediaSize.getMediaSizeForName(mediasizename);
			if (mediasize == null
					|| (units == Size2DSyntax.MM && mediasizename == MediaSizeName.NA_LETTER)) {
				mediasize = MediaSize.ISO.A4;
				attributes.add(MediaSizeName.ISO_A4);
				formatModel.getPrintSet().getPageformat().setPaper(
						getPaperFromMedia(MediaSizeName.ISO_A4));
				MediaPrintableArea mediaprintablearea1 = new MediaPrintableArea(
						1.0F, 1.0F, mediasize.getX(Size2DSyntax.INCH) - 2.0F,
						mediasize.getY(Size2DSyntax.INCH) - 2.0F,
						Size2DSyntax.INCH);
				attributes.add(mediaprintablearea1);
			} else {
				attributes.add(mediasizename);
				MediaPrintableArea mediaprintablearea2 = new MediaPrintableArea(
						(float) (pp.getImageableX() / d), (float) (pp
								.getImageableY() / d), (float) (pp
								.getImageableWidth() / d), (float) (pp
								.getImageableHeight() / d), Size2DSyntax.INCH);
				attributes.add(mediaprintablearea2);
			}
		}
    }
	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
		if(attributes!=null)
		 attributes.add(new DocumentName(documentName,Locale.getDefault()));
	}
	 private Paper getPaperFromMedia(Media media){
		    double d = 72D;
	    	Paper paper=new Paper();
	    	MediaSize mediasize;
	    	if(media instanceof MediaSizeName&& (mediasize = MediaSize.getMediaSizeForName((MediaSizeName)media)) != null){
	    		double d1 = (double)mediasize.getX(Size2DSyntax.INCH) * d;
	            double d3 = (double)mediasize.getY(Size2DSyntax.INCH) * d;
	            paper.setSize(d1, d3);
	            paper.setImageableArea(d, d, d1 - 2 * d, d3 - 2 * d);
	    	}
	    	return paper;
	    }
	public DocAttributeSet getAttributes() {
		return attributes;
	}
	
	public CellsModel getFormatModel() {
		return formatModel;
	}
	/**
	 * ���ø�ʽģ��
	 * @param printSet
	 */
	public void setFormatModel(CellsModel formatModel) {
		this.formatModel = formatModel;
		addDocAttributeFromPageFormat();
	}

	public DocFlavor getDocFlavor() {
		return flavor;
	}

	public UFOTable getPrintData() throws IOException {

		return printData;
	}
	private UFOTable createTableByModel(CellsModel showModel){
		UFOTable table=UFOTable.createTableByCellsModel(showModel);
		table.setOperationState(ReportContextKey.OPERATION_PRINT);
		return table;
	}
	public void setPrintData(UFOTable table){
       printData=table;
	}
    
	public Reader getReaderForText() throws IOException {
		
        return null;
	}

	public InputStream getStreamForBytes() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPrinting() {
		return isPrinting;
	}

	public void setPrinting(boolean isPrinting) {
		this.isPrinting = isPrinting;
	}	
    
}
