package com.ufsoft.table.re.timeref;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * 功能:日期选择控件
 *      
 * @author:   dulj
 * @version:  1.0
 * create date:2005-7-7 16:24:15 
 * @deprecated by 2008-5-15 王宇光 关闭此日历控件，被CalendarDialog取代
 */
public class DatePickerDialog extends UfoDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	/** 下个月按钮*/
    private JButton m_nextMonthButton;
    /** 上个月按钮*/
    private JButton m_previousMonthButton;
    /** 下一年按钮*/
    private JButton m_nextYearButton;
    /** 上一年按钮*/
    private JButton m_previousYearButton;
    /** 今日按钮*/
    private JButton m_todayButton;
    /** 日历控件*/
    private CalendarView m_calendarView;
    
    /** 选择日期后，日期存入的JTextField*/
    private JTextField m_returnTextField;
    
    private String m_imagePath = "/images/reportcore";
    public DatePickerDialog(JTextField returnTextField,JDialog parent) {
    	super(parent);
    	init(returnTextField,null);
    }
    private void init(JTextField returnTextField,Date defaultDate){
        //1.设置属性
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        m_returnTextField = returnTextField;
        //1.1设置日历控件事件
        m_calendarView = new CalendarView();
        m_calendarView.setActionCommand("MONTH_VIEW");
        m_calendarView.addActionListener(this);
        
        //1.2设置上个月按钮及事件
        Icon icon = UIManager.getIcon("CalendarView.monthUp.image");
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		m_imagePath + "/MonthUp.gif"));
        }
        m_previousMonthButton = new nc.ui.pub.beans.UIButton(icon);
        m_previousMonthButton.setActionCommand("PREVIOUS_MONTH");
        m_previousMonthButton.setPreferredSize(new Dimension(35, 20));
        m_previousMonthButton.setMnemonic(KeyEvent.VK_UNDEFINED);
        m_previousMonthButton.setSize(35,22);
        m_previousMonthButton.addActionListener(this);
        m_previousMonthButton.setToolTipText(MultiLang.getString("uiuforep0001104"));//"上月"
        
        //1.3设置上一年按钮及事件
        icon = UIManager.getIcon("CalendarView.yearUp.image");
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		m_imagePath + "/YearUp.gif"));
        }
        m_previousYearButton = new nc.ui.pub.beans.UIButton(icon);
        m_previousYearButton.setSize(35,22);
        m_previousYearButton.setPreferredSize(new Dimension(35, 20));
        m_previousYearButton.setActionCommand("PREVIOUS_YEAR");
        m_previousYearButton.addActionListener(this);
        m_previousYearButton.setToolTipText(MultiLang.getString("uiuforep0001105"));//"上年"
         
        //1.4设置下一年按钮及事件
        icon = UIManager.getIcon("CalendarView.yearDown.image");
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		m_imagePath + "/YearDown.gif"));
        }
        m_nextYearButton = new nc.ui.pub.beans.UIButton(icon);
        m_nextYearButton.setActionCommand("NEXT_YEAR");
        m_nextYearButton.setPreferredSize(new Dimension(35, 20));
        m_nextYearButton.setMnemonic(KeyEvent.VK_UNDEFINED);
        m_nextYearButton.setSize(35,22);
        m_nextYearButton.addActionListener(this);
        m_nextYearButton.setToolTipText(MultiLang.getString("uiuforep0001106"));//"下年"
        
        //1.5设置下个月按钮及事件
        icon = UIManager.getIcon("CalendarView.monthDown.image");
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		m_imagePath + "/MonthDown.gif"));
        }
        m_nextMonthButton = new nc.ui.pub.beans.UIButton(icon);
        m_nextMonthButton.setActionCommand("NEXT_MONTH");
        m_nextMonthButton.setPreferredSize(new Dimension(35, 20));
        m_nextMonthButton.setMnemonic(KeyEvent.VK_UNDEFINED);
        m_nextMonthButton.setSize(35,22);
        m_nextMonthButton.addActionListener(this);
        m_nextMonthButton.setToolTipText(MultiLang.getString("uiuforep0001107"));//"下月"
        
        //1.6设置当天按钮及事件
        icon = UIManager.getIcon("CalendarView.monthCurrent.image");
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		m_imagePath + "/Today.gif"));
        }
        m_todayButton = new nc.ui.pub.beans.UIButton(icon);
        m_todayButton.setActionCommand("TODAY");
        m_todayButton.setPreferredSize(new Dimension(35, 20));
        m_todayButton.setMnemonic(KeyEvent.VK_UNDEFINED);
        m_todayButton.setSize(35,22);
        m_todayButton.addActionListener(this);
        m_todayButton.setToolTipText(MultiLang.getString("uiuforep0001108"));//"当日"
        
        //2.加载控件
        //2.1加载日期控件
        contentPane.add(m_calendarView, BorderLayout.CENTER);
        //增加缺省值为指定初始值功能,2006-01-05,liulp
//        DateSpan span =
//            new DateSpan(new Date(),
//                    new Date());
		Date startDate = null;
		Date endDate = null;
		Date visibleDate = null;
		if(defaultDate !=  null){
			startDate = defaultDate;
			endDate = defaultDate;
			visibleDate = defaultDate;
		}else{
			startDate = new Date();
			endDate = new Date();
			visibleDate = new Date();
		}
        DateSpan span =
            new DateSpan(startDate,endDate);
        m_calendarView.setSelectedDateSpan(span);
	    m_calendarView.ensureDateVisible(visibleDate.getTime());
		
        
        
        
        JPanel panel = new UIPanel(new FlowLayout());
        //2.2加载上一年按钮
        panel.setPreferredSize(new Dimension(300, 30));
        panel.setPreferredSize(new Dimension(200, 30));
        panel.add(m_previousYearButton);
        //2.3加载上个月按钮
        panel.add(m_previousMonthButton);
        
        //2.4加载今日按钮
        panel.add(m_todayButton);
        
        //2.5加载下个月按钮
        panel.add(m_nextMonthButton);
        //2.6加载下一年按钮
        panel.add(m_nextYearButton);
        contentPane.add(panel, BorderLayout.NORTH);
        
        //3.设置关闭
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
    }
    /**
     * 构造函数:构造日期控件内容,增加控件的事件
     *      
     */
    public DatePickerDialog(JTextField returnTextField) {
       super();
       init(returnTextField,null);
    }
    /**
     * 构造函数:构造日期控件内容,增加控件的事件
     *      
     */
    public DatePickerDialog(JTextField returnTextField,Date defaultDate) {
       super();
       init(returnTextField,defaultDate);
    } 
    /**
     * 事件响应
     */
    public void actionPerformed(ActionEvent ev) {
        String command = ev.getActionCommand();
        if ("MONTH_VIEW" == command) {
            DateSpan span = m_calendarView.getSelectedDateSpan();
            if (m_returnTextField != null)
                m_returnTextField.setText(DateUtils.getDate(span.getStartAsDate()));
            this.dispose();
            
        } else if ("PREVIOUS_MONTH" == command) {
            m_calendarView.setFirstDisplayedDate(DateUtils.getPreviousMonth(
                          m_calendarView.getFirstDisplayedDate()));
        } else if ("PREVIOUS_YEAR" == command) {
            m_calendarView.setFirstDisplayedDate(DateUtils.getPreviousYear(
                          m_calendarView.getFirstDisplayedDate()));    
        } else if ("NEXT_MONTH" == command) {
            m_calendarView.setFirstDisplayedDate(DateUtils.getNextMonth(
                               m_calendarView.getFirstDisplayedDate()));
        } else if ("NEXT_YEAR" == command) {
            m_calendarView.setFirstDisplayedDate(DateUtils.getNextYear(
                               m_calendarView.getFirstDisplayedDate()));
        } else if ("TODAY" == command) {
            DateSpan span = new DateSpan(System.currentTimeMillis(),
                System.currentTimeMillis());
            m_calendarView.setSelectedDateSpan(span);
            m_calendarView.ensureDateVisible(span.getStart());
        }
    }
    /**
     * 提供给外部，用于添加监听器。
     * @return CalendarView
     */
    public CalendarView getCalendarView(){
        return m_calendarView;
    }
}
