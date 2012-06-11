package com.ufsoft.table.re;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Date;

import javax.swing.JComponent;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.format.UnifyDateFormat;

/**
 * Date ���͵Ļ�����
 * @author chxiaowei 2007-03-11
 */
public class DateRenderer extends DefaultSheetCellRenderer{
	private static final long serialVersionUID = -3985518389978670364L;

	/**
	 * �����������ͣ��洢��������Ϊ�����Ͷ���
	 * ���ݶ��������������ʾ��ʽ����ʽ�����ڶ���ת�����ַ�����ʽ��
	 */
	public Component getCellRendererComponent(CellsPane table, Object obj,  boolean isSelected, boolean hasFocus, int row, int column, Cell c) {
		JComponent editorComp = (JComponent) super.getCellRendererComponent(table,  obj, isSelected, hasFocus, row,column, c);
		
		if(c == null){
			return null;
		}
		
		if(c.getFormat()!=null && TableConstant.CELLTYPE_DATE == c.getFormat().getCellType()){
			Object value = c.getValue();
			IufoFormat format = (IufoFormat)c.getFormat();
			if(value != null && value instanceof Date){
				Method getText = null;
				try {
					getText = editorComp.getClass().getMethod("getText", null);
					String text = (String) getText.invoke(editorComp, null);
					if(text != null && !text.equals("")){
						Date date = (Date)value;
						UnifyDateFormat udf = null;

						int nDateType = format.getDateType();
						if(nDateType == TableConstant.DATETYPE_HOR){
							udf = UnifyDateFormat.getDateFormatInstance(7);
							text = udf.format(date);
						} else if(nDateType == TableConstant.DATETYPE_SLOPE){
							udf = UnifyDateFormat.getDateFormatInstance(8);
							text = udf.format(date);
						} else if(nDateType == TableConstant.DATETYPE_CHN){
							udf = UnifyDateFormat.getDateFormatInstance(5);
							text = udf.format(date);
						}
					}

					Method setText = editorComp.getClass().getMethod("setText", new Class[]{String.class});
					setText.invoke(editorComp, new Object[]{text});
				}catch(Exception e){
					throw new RuntimeException("");
				}
			}
		}
		return editorComp;
	}
	
	/**
     * @see com.ufsoft.table.re.DefaultSheetCellRenderer#decorateValue(java.lang.Object,
     *      com.ufsoft.table.format.Format)
     */
   protected String decorateValue(Component render,Object value, Format format) {
    	if (value == null) {
            return null;
        } else if (value instanceof Date) {
        	Date date = (Date) value;
            
        	//���������ַ����������ʽ
            format = format == null ? new IufoFormat() : format;
            if (format instanceof IufoFormat) {
                IufoFormat iufoFormat = (IufoFormat) format;
                return iufoFormat.getDateStr(date);             
            }
        }
    	
        return value.toString();
    }
}
