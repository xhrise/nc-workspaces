package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.TablePane;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.FontFactory;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.SheetCellRenderer;

public class KeyDefDisPlayRenderer implements SheetCellRenderer {
	public Component getCellRendererComponent(CellsPane cellsPane,   Object obj,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		
		// @edit by ll at 2009-5-14,����10:19:58
		if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT && cellsPane.getOperationState() != ReportContextKey.OPERATION_REF){
			return null;
		}
		
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsPane.getDataModel());
		KeyVO vo = dynAreaModel.getKeyVOAfterDataProcess(row,column);
		
		if(vo == null) return null;
		StringBuffer displayName = new StringBuffer();	
		//modify by wangyga 2008-7-4 ��ʽ̬�ؼ��ֵ���ʾ�ɣ��ؼ��֣���λ �޸ĳ� ��λ:�������Ƿ��Ҫ��
//		displayName.append(StringResource.getStringResource("miufopublic407"));//"�ؼ���:");
		displayName.append(vo.getName());
		displayName.append(":");
		com.ufsoft.table.beans.UFOLabel lblKeyComp = new com.ufsoft.table.beans.UFOLabel(displayName.toString());
		
		//Ϊ��������������
		Font font = null;
		String fontName = DefaultFormatValue.FONTNAME;
		int fontSize = DefaultFormatValue.FONT_SIZE;
		int fontStyle = DefaultFormatValue.FONT_STYLE;
		
		//Ϊ�������������壬�߿�
		Format format = cellsPane.getDataModel().getRealFormat(
				CellPosition.getInstance(row, column));
		if (format != null) {
			fontName = format.getFontname();
			if (fontName == null) {
				fontName = DefaultFormatValue.FONTNAME;
			}
			fontStyle = format.getFontstyle();
			if (fontStyle == TableConstant.UNDEFINED) {
				fontStyle = DefaultFormatValue.FONT_STYLE;
			}
			fontSize = format.getFontsize();
			if (fontSize == TableConstant.UNDEFINED) {
				fontSize = DefaultFormatValue.FONT_SIZE;
			}
		}
		
		//��Ҫ����Font�ĳߴ�.
		fontSize = (int) (fontSize * TablePane.getViewScale());
		font = FontFactory.createFont(fontName, fontStyle, fontSize);
		lblKeyComp.setFont(font);
		
		//����ǰ��ɫ
		Color foreGround = format == null
			|| format.getForegroundColor() == null ? cellsPane.getForeground() : format.getForegroundColor();
		if (isSelected) { //����ѡ���Ч��
			int foreColor = foreGround.getRGB();
			int sColor = TableStyle.SELECTION_BACKGROUND.getRGB();
			foreColor = foreColor != sColor ? foreColor ^ ~sColor : foreColor;
			foreGround = new Color(foreColor);
		}
		lblKeyComp.setForeground(foreGround);
		
		//���ö���:�����͡����ͺ���������ȱʡ������ʾ
		if (format == null || format.getHalign() == TableConstant.UNDEFINED) {
			lblKeyComp.setHorizontalAlignment(DefaultFormatValue.HALIGN);
			if (format != null
					&& format.getHalign() == TableConstant.UNDEFINED
					&& format.getCellType() == TableConstant.CELLTYPE_DATE) {
				lblKeyComp.setHorizontalAlignment(TableConstant.HOR_RIGHT);
			}
		} else {
			lblKeyComp.setHorizontalAlignment(format.getHalign());
		}
		
		lblKeyComp.setVerticalAlignment(format == null
				|| format.getValign() == TableConstant.UNDEFINED ? DefaultFormatValue.VALIGN
				: format.getValign());
		return lblKeyComp;
	}
}
