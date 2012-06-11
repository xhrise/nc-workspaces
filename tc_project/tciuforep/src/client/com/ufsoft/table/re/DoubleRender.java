package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.table.format.Format;

/**
 * Double类型的绘制器
 * @author zzl 2005-7-8
 */
public class DoubleRender extends DefaultSheetCellRenderer{
    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = -2794904869189487368L;
    /**
     * @see com.ufsoft.table.re.DefaultSheetCellRenderer#decorateValue(java.lang.Object,
     *      com.ufsoft.table.format.Format)
     */
    protected String decorateValue(Component render,Object value, Format format) {
    	if(value instanceof Integer){
    		value = new Double(value.toString());
    	}
    	
    	boolean bShowZero = ReportStyle.isShowZero();
        if (value == null) {
            return null;
        } else if (value instanceof Double) {
            Double val = (Double) value;
            if (!bShowZero && val.doubleValue() == 0) {
            	return "";
            }
            //修饰数字的输出格式
            format = format == null ? new IufoFormat() : format;
            if (format instanceof IufoFormat) {
                double dValue = 0;
                dValue = val.doubleValue();
                IufoFormat iufoFormat = (IufoFormat) format;
                if(iufoFormat.isMinusRed() && dValue < 0){
                    //因为后面还会根据单元属性的前景色设置，所以这里的操作要放到后面。
                    render.setForeground(Color.red);                  
                }
                return iufoFormat.getString(dValue);                
            }
        }
        return value.toString();
    }
}
