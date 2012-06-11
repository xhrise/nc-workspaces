package com.ufsoft.report.sysplugin.cellattr;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.FloatDocument;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
/**
 * 条件格式设置对话框
 * @author guogang
 *
 */
public class ConditionFormatDialog extends UfoDialog implements ActionListener {
	private Format c_format = null;//对话框要设置的原始条件格式
	private Container parent=null;
	private Hashtable<String,Object> c_conFormat=null;
	//nType-nValue;缓存设置的值，当点击OK后，再真正保存。
	private Hashtable<Integer, Integer> m_propertyCache = new Hashtable<Integer, Integer>();
	
	private JPanel ivjUfoDialogContentPane = null;
	private JPanel ivjConditionPane=null;
	private JComboBox ivjoperatorComb=null;
	private JPanel ivjconditionValue=null;
	private JTextField conditionText1=null;
	private JLabel jLabelAnd=null;
	private JTextField conditionText2=null;
	private JPanel ivjpreviewPane=null;
	private JLabel ivjpreviewLabel=null;
	
	private JButton ivjOkButton = null;
	
	private JButton ivjcancelButton = null;
	
	public ConditionFormatDialog(Container owner, Format format,Hashtable conFormat){
		super(owner);
		this.parent=owner;
		this.c_format=format;
		this.c_conFormat=conFormat;
		initialize();
	}
	public ConditionFormatDialog(){
		initialize();
	}
   
	/**
	 * 初始化类。
	 */
	private void initialize() {
		try {
			setResizable(false);
			setName("ConditionFormatDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(600, 200);
			setTitle(MultiLang.getString("uiuforep0001111"));//条件格式
			setContentPane(getUfoDialogContentPane());
			 
			
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	/**
	 * 返回 UfoDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getUfoDialogContentPane() {
		if (ivjUfoDialogContentPane == null) {
			try {
				ivjUfoDialogContentPane = new UIPanel();
				ivjUfoDialogContentPane.setName("UfoDialogContentPane");
				ivjUfoDialogContentPane.setLayout(null);
				getUfoDialogContentPane().add(getConditionPane(),
						getConditionPane().getName());
				getUfoDialogContentPane().add(getOkButton(),
						getOkButton().getName());
				getUfoDialogContentPane().add(getcancelButton(),
						getcancelButton().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUfoDialogContentPane;
	}
	/**
	 * 获取条件格式设置Pane
	 * @return
	 * @i18n report00004=条件
	 * @i18n report00005=单元格数值：
	 * @i18n format=格式
	 */
	private javax.swing.JPanel getConditionPane(){
		if (ivjConditionPane == null) {
			try {
				ivjConditionPane = new UIPanel();
				ivjConditionPane.setName("ConditionPane");
				ivjConditionPane.setLayout(null);
				ivjConditionPane.setBounds(9, 7, 582, 125);
				ivjConditionPane.setBorder(javax.swing.BorderFactory.createTitledBorder(MultiLang.getString("report00004")));
				JLabel jLabel1=new JLabel();
		        jLabel1.setBounds(10, 15, 100, 22);
		        jLabel1.setText(MultiLang.getString("report00005"));
		        ivjConditionPane.add(jLabel1);
		        ivjConditionPane.add(getOperatorComb(),getOperatorComb().getName());
		        ivjConditionPane.add(getConditionValuePane(),getConditionValuePane().getName());
		        ivjConditionPane.add(getConditionPreviewPane());
		        JButton formatButton=new nc.ui.pub.beans.UIButton();
		        formatButton.setText(MultiLang.getString("format"));
		        formatButton.setBounds(500, 60, 75, 22);
		        ivjConditionPane.add(formatButton);
		        formatButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						CellPropertyDialog dlg = new CellPropertyDialog(parent,c_format,false,true,true);
			            dlg.setVisible(true);
			            if (dlg.getResult() == UfoDialog.ID_OK) { 
			            	m_propertyCache=dlg.getPropertyCache();
			            	showPreview();
			            }
					}
		        });
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjConditionPane;
	}
	/**
	 * 获取条件设置下拉框
	 * @return
	 */
	private JComboBox getOperatorComb(){
		if(ivjoperatorComb==null){
		try{
		ivjoperatorComb=new UIComboBox();
		ivjoperatorComb.setName("ConditonOperator");
		ivjoperatorComb.setBounds(120, 15, 100, 22);
		String[] operatorList=DefaultSetting.operatorList;
		for(int i=0;i<operatorList.length;i++){
			ivjoperatorComb.addItem(operatorList[i]);
		}
		int n=0;
		if(c_conFormat!=null&&c_conFormat.get("operator")!=null){
			n = ((Integer)(c_conFormat.get("operator"))).intValue();
		}
		ivjoperatorComb.setSelectedIndex(n);
		ivjoperatorComb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int nSize = ivjoperatorComb.getSelectedIndex();
				if(nSize>5){
					jLabelAnd.setVisible(true);
			    	conditionText2.setVisible(true);
				}else{
					conditionText1.setText("");
					jLabelAnd.setVisible(false);
			    	conditionText2.setVisible(false);
			    	conditionText2.setText("");
				}
			}
		});
		}catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		}
		return ivjoperatorComb;
	}
	/**
	 * 获取条件设置数值Pane
	 * @return
	 * @i18n report00006= 和 
	 */
	private JPanel getConditionValuePane(){
		if(ivjconditionValue==null){
			try{
		 ivjconditionValue=new UIPanel();
		 ivjconditionValue.setName("ConditionValue");
		 ivjconditionValue.setBounds(230, 15, 300, 22);
		 ivjconditionValue.setLayout(null);
		 conditionText1=new UITextField();
	     conditionText1.setName("ConditionBefore");
	     conditionText1.setBounds(1, 1, 100, 20);
	     conditionText1.setDocument(new FloatDocument());
	     ivjconditionValue.add(conditionText1);
	     jLabelAnd=new UILabel();
	     jLabelAnd.setBounds(111, 1, 30, 22);
	     jLabelAnd.setText(MultiLang.getString("report00006"));
	     jLabelAnd.setVisible(false);
	     ivjconditionValue.add(jLabelAnd);
	     conditionText2=new UITextField();
	     conditionText2.setName("ConditionAfter");
	     conditionText2.setBounds(151, 1, 100, 20);
	     conditionText2.setDocument(new FloatDocument());
	     ivjconditionValue.add(conditionText2);
	     conditionText2.setVisible(false);
	     if(c_conFormat!=null&&c_conFormat.get("con1")!=null){
	    	 conditionText1.setText(String.valueOf(c_conFormat.get("con1")));
			}
	     if(c_conFormat!=null&&c_conFormat.get("con2")!=null&&!((String)(c_conFormat.get("con2"))).equals("")){
	    	 conditionText2.setText(String.valueOf(c_conFormat.get("con2")));
	    	 jLabelAnd.setVisible(true);
	    	 conditionText2.setVisible(true);
			}
			}catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
	    return ivjconditionValue;
		}
	/**
	 * 获取条件格式设置预览面版
	 * @return
	 * @i18n uiuforep0000787=预览
	 */
	private JPanel getConditionPreviewPane(){
		if(ivjpreviewPane==null){
			try{
		ivjpreviewPane=new UIPanel();
		ivjpreviewPane.setName("PreviewPane");
		ivjpreviewPane.setLayout(null);
		ivjpreviewPane.setBounds(300, 40, 200, 80);
		ivjpreviewPane.add(getPreviewLabel(),
				getPreviewLabel().getName());
		ivjpreviewPane.setBorder(new TitledBorder(new EtchedBorder(),
		        MultiLang.getString("uiuforep0000787"), TitledBorder.LEFT, TitledBorder.TOP,//预览
				new java.awt.Font("dialog", 0, 14)));
			}catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjpreviewPane;
	}
	/**
	 * 获取条件格式设置预览标签
	 * @return
	 * @i18n report00007=未设定格式
	 */
	private JLabel getPreviewLabel(){
		if(ivjpreviewLabel==null){
			try{
		ivjpreviewLabel=new UILabel();
//		ivjpreviewLabel.setDebugGraphicsOptions(DebugGraphics.LOG_OPTION);
		ivjpreviewLabel.setName("PreviewLabel");
		ivjpreviewLabel.setText(MultiLang.getString("report00007"));
		ivjpreviewLabel.setForeground(java.awt.Color.black);
		ivjpreviewLabel.setBounds(new Rectangle(8, 20, 184, 40));
		ivjpreviewLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ivjpreviewLabel.setOpaque(true);
		 if(c_conFormat!=null&&c_conFormat.get("format")!=null){
			 IufoFormat iufoFormat=(IufoFormat)c_conFormat.get("format");
			 showPreview(iufoFormat);
			}
		 
			}catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjpreviewLabel;
	}
	/**
	 * 由格式信息更新预览
	 * @param format
	 */
    public void showPreview(IufoFormat format) {
		
		if(ivjpreviewLabel!=null){
			ivjpreviewLabel.setText("AaBbCcXxYy");
			String fontName = DefaultFormatValue.FONTNAME;
			int fontSize =DefaultFormatValue.FONT_SIZE;
			int fontStyle =DefaultFormatValue.FONT_STYLE;
			Color b=format.getBackgroundColor();
			Color c=format.getForegroundColor();
                if(format.getFontname()!=null){
                	fontName=format.getFontname();
                	String[] names=DefaultSetting.fontNames;
                	for(int i=0;i<names.length;i++){
                	 if(fontName.equals(names[i]))
                	   m_propertyCache.put(PropertyType.FontIndex, i);
                	}
                }
                if(format.getFontsize()!=TableConstant.UNDEFINED){
                	fontSize=format.getFontsize();
                	m_propertyCache.put(PropertyType.FontSize, fontSize);
                }
                if(format.getFontstyle()!=TableConstant.UNDEFINED){
                	fontStyle=format.getFontstyle();
                	m_propertyCache.put(PropertyType.FontStyle, fontStyle);
                }
                if(b!=null){
	            	ivjpreviewLabel.setBackground(b);
	            	m_propertyCache.put(PropertyType.BackColor, b.getRGB());
                }
                if(c!=null){
	            	ivjpreviewLabel.setForeground(c);
	            	m_propertyCache.put(PropertyType.ForeColor, c.getRGB());
                }
	        Font f = new Font(fontName,fontStyle,fontSize);
            ivjpreviewLabel.setFont(f);
		}
	}
	/**
	 * 根据设置值,显示preview面板.
	 */
	public void showPreview() {
		if (ivjpreviewLabel == null) {
			return;
		}
		if(getPropertyCache()!=null&&!getPropertyCache().isEmpty()){
			ivjpreviewLabel.setText("AaBbCcXxYy");
			String fontName = DefaultFormatValue.FONTNAME;
			int fontSize = DefaultFormatValue.FONT_SIZE;
			int fontStyle = DefaultFormatValue.FONT_STYLE;
			Enumeration<Integer> enums = getPropertyCache().keys();
	        while (enums.hasMoreElements()) {
	        	Integer tmpType = (Integer) enums.nextElement();
	            int nType = tmpType.intValue();
	            int nValue = ((Integer) getPropertyCache().get(tmpType)).intValue();
	            
	            switch (nType) {
	            case PropertyType.FontSize :
	            	fontSize=nValue;
	            	break;
	            case PropertyType.FontIndex :
	            	fontName=DefaultSetting.fontNames[nValue];
	            	break;
	            case PropertyType.FontStyle :
	            	switch (nValue) {
	    			case TableConstant.FS_BOLD :
	    				fontStyle = Font.BOLD;
	    				break;
	    			case TableConstant.FS_SLOPE :
	    				fontStyle = Font.ITALIC;
	    				break;
	    			case TableConstant.FS_SLOPE | TableConstant.FS_BOLD :
	    				fontStyle = Font.BOLD | Font.ITALIC;
	    				break;
	    			default :
	    				fontStyle = Font.PLAIN;
	    				break;
	    		     }
	            	break;
	            case PropertyType.BackColor :
	            	Color b=new Color(nValue);
	            	ivjpreviewLabel.setBackground(b);
	            	break;
	            case PropertyType.ForeColor :
	            	Color c=new Color(nValue);
	            	ivjpreviewLabel.setForeground(c);
	            	break;
	            default:
	                break;
	            }
	        }
	        Font f = new Font(fontName,fontStyle,fontSize);
            ivjpreviewLabel.setFont(f);
		}
	}
	/**
	 * 返回 okButton 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getOkButton() {
		if (ivjOkButton == null) {
			try {
				ivjOkButton = new nc.ui.pub.beans.UIButton();
				ivjOkButton.setName("OkButton");
				ivjOkButton.setText(MultiLang.getString("uiuforep0000782"));//确定
				ivjOkButton.setBounds(400, 134, 75, 22);
				ivjOkButton.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOkButton;
	}
	/**
	 * 返回 cancelButton 特性值。
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getcancelButton() {
		if (ivjcancelButton == null) {
			try {
				ivjcancelButton = new nc.ui.pub.beans.UIButton();
				ivjcancelButton.setName("cancelButton");
				ivjcancelButton.setText(MultiLang.getString("uiuforep0000739"));
				ivjcancelButton.setBounds(500, 134, 75, 22);
				ivjcancelButton.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjcancelButton;
	}
	/**
	 * @i18n report00008=条件值输入有误，请重新输入
	 * @i18n report00009=请输入条件1的值
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JButton) {
			JButton source = (JButton) event.getSource();
			if(source == ivjOkButton){
				String text1=conditionText1.getText();
				String text2="";
				if(conditionText2!=null&&conditionText2.isVisible()){
					text2=conditionText2.getText();
					if("".equals(text1)||"".equals(text2)||Float.parseFloat(text1)>Float.parseFloat(text2)){
						JOptionPane.showMessageDialog(this,MultiLang.getString("report00008"));
					    return;
					}
				}
				else{
					if("".equals(text1)){
						JOptionPane.showMessageDialog(this,MultiLang.getString("report00009"));
					    return;
					}
				}
				setResult(UfoDialog.ID_OK);	
				this.close();
			}else if (source == ivjcancelButton) { //取消按钮的处理
				setResult(UfoDialog.ID_CANCEL);
				this.close();
			}
		}
		
	}
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println(MultiLang.getString("uiuforep0000805"));
		// 未捕捉到的异常
		exception.printStackTrace(System.out);
	}
	public Hashtable<String,Object> getConditionFmt(){
		if(c_conFormat==null){
			c_conFormat=new Hashtable<String,Object>();
		}
		if(ivjoperatorComb!=null){
			c_conFormat.put("operator", ivjoperatorComb.getSelectedIndex());
		}
		if(conditionText1!=null){
			c_conFormat.put("con1", conditionText1.getText());
		}
		if(conditionText2!=null){
			c_conFormat.put("con2", conditionText2.getText());
		}
		if(m_propertyCache!=null){
			Format format=new IufoFormat();
			Enumeration<Integer> enums = m_propertyCache.keys();
			while (enums.hasMoreElements()) {
	            Integer tmpType = (Integer) enums.nextElement();
	            int nType = tmpType.intValue();
	            int nValue = ((Integer) m_propertyCache.get(tmpType)).intValue();
			    PropertyType.setPropertyByType(format, nType, nValue);
			}
			c_conFormat.put("format", format);
		}
		return c_conFormat;
	}
	/**
	 * 获取设置的格式信息
	 * @return Hashtable
	 */
	public Hashtable<Integer, Integer> getPropertyCache() {
		return m_propertyCache;
	}
	public static void main(String[] args){
		ConditionFormatDialog t=new ConditionFormatDialog();
		t.setVisible(true);
		
	}
	
}
 