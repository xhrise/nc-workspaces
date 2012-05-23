package com.ufsoft.report.sysplugin.xml;

import java.awt.print.PageFormat;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.print.PrintSet;

public class XMLParser2PrintSet extends AbsXmlParser {

	protected void objectToXmlImpl(Element element, Object obj) {
		PrintSet printSet = (PrintSet) obj;
		addToElement("printScale",""+printSet.getPrintScale(),element);
		addToElement("colPriorityPrinted",printSet.isColPriorityPrinted() ? "true": "false", element);
		if(printSet.getPrintArea() != null){
			int[] intPintArea = printSet.getPrintArea();
			try{
				AreaPosition printArea = AreaPosition.getInstance(intPintArea[0],intPintArea[1],
						intPintArea[3]-intPintArea[1],intPintArea[2]-intPintArea[0]);
				addToElement("printArea",printArea.toString(),element);
			}catch(Exception e){
				AppDebug.debug(e);
			}
		}
		if(printSet.getRowHeadRang()!=null){
			int[] rowHeadRang = printSet.getRowHeadRang();
			addToElement("rowHeadRang0",""+rowHeadRang[0],element);
			addToElement("rowHeadRang1",""+rowHeadRang[1],element);
		}
		if(printSet.getColHeadRang()!=null){
			int[] colHeadRang = printSet.getColHeadRang();
			addToElement("colHeadRang0",""+colHeadRang[0],element);
			addToElement("colHeadRang1",""+colHeadRang[1],element);
		}
		int[] pageBreakRows = printSet.getPageBreakRows();
		for (int i = 0; i < pageBreakRows.length; i++) {
			int pageBreakRow = pageBreakRows[i];
			addToElement("pageBreakRow",""+pageBreakRow,element);
		}
		int[] pageBreakCols = printSet.getPageBreakCols();
		for (int i = 0; i < pageBreakCols.length; i++) {
			int pageBreakCol = pageBreakCols[i];
			addToElement("pageBreakCol",""+pageBreakCol,element);
		}
		//×ª»»PageFormat
		PageFormat pf = printSet.getPageformat();
		addToElement("mOrientation",""+pf.getOrientation(),element);
		addToElement("paper_mHeight",""+pf.getPaper().getHeight(),element);
		addToElement("paper_mWidth",""+pf.getPaper().getWidth(),element);
		addToElement("paper_rect_x",""+pf.getPaper().getImageableX(),element);
		addToElement("paper_rect_y",""+pf.getPaper().getImageableY(),element);
		addToElement("paper_rect_h",""+pf.getPaper().getImageableHeight(),element);
		addToElement("paper_rect_w",""+pf.getPaper().getImageableWidth(),element);
	}


	protected Object xmlToObjectImpl(Element element) {
		PrintSet printSet = new PrintSet();
		float printScale = Float.parseFloat(getFromElementStr("printScale",element));
		printSet.setPrintScale(printScale);
		boolean colPriorityPrinted = "true".equals(getFromElementStr("colPriorityPrinted",element));
		printSet.setColPriorityPrinted(colPriorityPrinted);
		String printArea = getFromElementStr("printArea",element);
		if(printArea != null){
			AreaPosition areaPos = AreaPosition.getInstance(printArea);
			int[] intPintArea = new int[]{areaPos.getStart().getRow(),areaPos.getStart().getColumn(),
					areaPos.getEnd().getRow(),areaPos.getEnd().getColumn()};
			printSet.setPrintArea(intPintArea);
		}
		String rowHeadRang0 = getFromElementStr("rowHeadRang0",element);
		String rowHeadRang1 = getFromElementStr("rowHeadRang1",element);
		if(rowHeadRang0!=null && rowHeadRang1!=null){
			int[] rowHeadRang = new int[]{Integer.parseInt(rowHeadRang0),Integer.parseInt(rowHeadRang1)};
			printSet.setRowHeadRang(rowHeadRang);
		}
		String colHeadRang0 = getFromElementStr("colHeadRang0",element);
		String colHeadRang1 = getFromElementStr("colHeadRang1",element);
		if(colHeadRang0!=null && colHeadRang1!=null){
			int[] colHeadRang = new int[]{Integer.parseInt(colHeadRang0),Integer.parseInt(colHeadRang1)};
			printSet.setColHeadRang(colHeadRang);
		}
		Element[] pageBreakRowElements = getChildsByTagName(element,"pageBreakRow");
		int[] pageBreakRows = new int[pageBreakRowElements.length];
		for(int i=0;i<pageBreakRowElements.length;i++){
			Element aElement = pageBreakRowElements[i];
			Text text = (Text) aElement.getFirstChild();
			pageBreakRows[i] = Integer.parseInt(text.getData());
		}
		printSet.setPageBreakRows(pageBreakRows);
		Element[] pageBreakColElements = getChildsByTagName(element,"pageBreakCol");
		int[] pageBreakCols = new int[pageBreakColElements.length];
		for(int i=0;i<pageBreakColElements.length;i++){
			Element aElement = pageBreakColElements[i];
			Text text = (Text) aElement.getFirstChild();
			pageBreakCols[i] = Integer.parseInt(text.getData());
		}
		printSet.setPageBreakCols(pageBreakCols);
		//×ª»»PageFormat
		PageFormat pf = new PageFormat();
		printSet.setPageFormat(pf);
		pf.setOrientation(Integer.parseInt(getFromElementStr("mOrientation",element)));
		double paper_mHeight = Double.parseDouble(getFromElementStr("paper_mHeight",element));
		double paper_mWidth = Double.parseDouble(getFromElementStr("paper_mWidth",element));
		double paper_rect_x = Double.parseDouble(getFromElementStr("paper_rect_x",element));
		double paper_rect_y = Double.parseDouble(getFromElementStr("paper_rect_y",element));
		double paper_rect_h = Double.parseDouble(getFromElementStr("paper_rect_h",element));
		double paper_rect_w = Double.parseDouble(getFromElementStr("paper_rect_w",element));
		pf.getPaper().setSize(paper_mWidth,paper_mHeight);
		pf.getPaper().setImageableArea(paper_rect_x,paper_rect_y,paper_rect_w,paper_rect_h);
		
		return printSet;
	}
	
	public String getSupportClassName() {
		return PrintSet.class.getName();
	}

}
