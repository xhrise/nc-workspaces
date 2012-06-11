package com.ufida.report.anareport.applet;

import java.awt.Color;
import java.awt.Component;

import com.ufida.report.crosstable.CrossTableCellElement;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.re.DefaultSheetCellRenderer;

public class CrossTableCellRender extends DefaultSheetCellRenderer{

	private static final long serialVersionUID = 2362014641726936883L;
	
	@Override
	protected String decorateValue(Component render, Object value, Format format) {

    	if(value instanceof CrossTableCellElement){
    		value =((CrossTableCellElement)value).getData();
    	}
    	
    	boolean bShowZero = ReportStyle.isShowZero();
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            Double val = (Double) value;
            if (!bShowZero && val.doubleValue() == 0) {
            	return "";
            }
            //�������ֵ������ʽ
            format = format == null ? new IufoFormat() : format;
            if (format instanceof IufoFormat) {
                double dValue = 0;
                dValue = val.doubleValue();
                IufoFormat iufoFormat = (IufoFormat) format;
                if(iufoFormat.isMinusRed() && dValue < 0){
                    //��Ϊ���滹����ݵ�Ԫ���Ե�ǰ��ɫ���ã���������Ĳ���Ҫ�ŵ����档
                    render.setForeground(Color.red);                  
                }
                return iufoFormat.getString(dValue);                
            }
        }
        return value.toString();
    
	}
    
}
