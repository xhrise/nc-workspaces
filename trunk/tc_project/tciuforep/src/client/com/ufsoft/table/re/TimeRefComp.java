package com.ufsoft.table.re;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTextField;

import nc.ui.pub.beans.UITextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DateUtil;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.re.timeref.CalendarDialog;

/**
 * 
 * @author zzl 2005-6-22
 */
public class TimeRefComp extends nc.ui.pub.beans.UIPanel  implements IRefComp {

    private static final long serialVersionUID = 2427781691675774756L;
    
    private JTextField textField = new UITextField();
    //modify by 王宇光 2008-5-9 替换原来的日历参照
//    private DatePickerDialog dialog = null;
    private CalendarDialog dialog = null;
    /**
     * @param parent
     */
    public TimeRefComp(Date defaultDate,RefTextField ref) {
        super();
        //modify by 王宇光 2008-5-9 替换原来的日历参照
//        dialog = new DatePickerDialog(textField,defaultDate);
        dialog = new CalendarDialog(ref,defaultDate);
        dialog.setTimeRefComp(this);
        setLayout(new BorderLayout());
        add(dialog.getRootPane(),BorderLayout.CENTER);
        setPreferredSize(new Dimension(dialog.getWidth(), dialog.getHeight()));       
    }

    /*
     * @see com.ufsoft.table.re.IRefComp#getSelectIDName()
     */
    public Object getSelectValue() {
        return textField.getText();
    }

    /*
     * @see com.ufsoft.table.re.IRefComp#setDefaultIDName(com.ufsoft.table.re.IDName)
     */
    public void setDefaultValue(Object obj) {
    	try {
    		Date defaultDate = null;
    		if(obj instanceof String){
    			defaultDate = DateUtil.getDayFormat().parse((String)obj);//"2006-12-10")
    			dialog.initDate(defaultDate);
    		}       		
		} catch (ParseException e) {
			AppDebug.debug(e);
		}
    	
    }

    /*
     * @see com.ufsoft.table.re.IRefComp#isValidate(java.lang.String)
     */
    public Object getValidateValue(String text) {
        try {
            //只支持2005-02-02格式的字符串。不支持如02/02/05格式的。
            DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.SIMPLIFIED_CHINESE).parse(text.replace('/', '-'));
        } catch (ParseException e) {
            return null;
        }
        return text;
    }

    /*
     * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
     */
    public synchronized void addMouseListener(MouseListener l) {
                                dialog.setLocation(new Point(0, 0));
//                dialog.setSize(new Dimension(238, 304));
//                dialog.setBounds(new Rectangle(0, 0, 238, 324));
//                dialog.getCalendarView().addMouseListener(l);
    }

	public String getTitleValue() {
		return MultiLang.getString("uiuforep0001103");//"时间参照"
	}
    
}