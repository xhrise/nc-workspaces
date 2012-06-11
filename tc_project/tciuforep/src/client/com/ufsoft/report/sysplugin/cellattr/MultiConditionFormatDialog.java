/**
 * 
 */
package com.ufsoft.report.sysplugin.cellattr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIButton;
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
import com.ufsoft.table.format.ConditionFormat;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
/**
 * 多条件格式设置对话框
 * @author guogang
 * @date 2007-10-16
 */
public class MultiConditionFormatDialog extends UfoDialog implements ActionListener {
	/**对话框要设置的单元格原始格式信息*/
	private Format fmOriginal;
	/** 对话框的父容器*/
	private Container parent;
	/** 单元格的原始条件格式信息，格式为：name-value,{["operator",value],["con1",value],["con2",value],["format",value],..}*/
//	private Hashtable<String,Object> conditionFmOriginal;
	private ArrayList<ConditionFormat> conditionFmOriginal;
	private ArrayList<ConditionFormat> conditionFmCurrent;
	
	/** 条件格式设置Panel*/
	private	ConditionFormatPanel pnConditionFormat;
	private UIButton ivjOkButton ;	
	private UIButton ivjcancelButton ;
	//支持多条件
	private int conditionNum=0;
	private UIButton ivjAddButton;
	private UIButton ivjDelButton;
	
	public MultiConditionFormatDialog(Container owner,Format format,ArrayList<ConditionFormat> conFormat){
		super(owner);
		this.parent=owner;
		this.fmOriginal=format;
		this.conditionFmOriginal=conFormat;
		initialize();
	}
	public MultiConditionFormatDialog(){
		super();
		initialize();
	}
   
	/**
	 * 初始化类。
	 * @i18n report00010=增加>>
	 * @i18n report00011=删除<<
	 */
	private void initialize() {
		GridBagLayout gridbaglayout = new GridBagLayout();
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
		
			setName("ConditionFormatDialog");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(MultiLang.getString("uiuforep0001111"));//条件格式
			Container container = getContentPane();
	        container.setLayout(gridbaglayout);
	       
	        pnConditionFormat=new ConditionFormatPanel();
	        gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
	        gridbaglayout.setConstraints(pnConditionFormat, gridbagconstraints);
	        container.add(pnConditionFormat);
	        JPanel jpanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
	        ivjAddButton=createButton(MultiLang.getString("report00010"), this);
	        jpanel.add(ivjAddButton);
	        ivjDelButton=createButton(MultiLang.getString("report00011"), this);
	        jpanel.add(ivjDelButton);
	        ivjOkButton=createButton(MultiLang.getString("uiuforep0000782"),this);
	        jpanel.add(ivjOkButton);
	        ivjcancelButton=createButton(MultiLang.getString("uiuforep0000739"),this);
	        jpanel.add(ivjcancelButton);
	        gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
	        gridbaglayout.setConstraints(jpanel, gridbagconstraints);
	        container.add(jpanel);
	        
	        setResizable(false); 
	        pack();
	}
	
	private UIButton createButton(String s, ActionListener actionlistener)
    {
        UIButton jbutton = new UIButton(s);
        jbutton.addActionListener(actionlistener);
        return jbutton;
    }
	/**
	 * 获取条件格式，供setConditionAttrExt调用
	 * @return
	 */
	public ArrayList<ConditionFormat> getConditionFmt(){
		
		if(pnConditionFormat!=null){
		int panelNum=pnConditionFormat.getComponentCount();
		ConditionContentPanel tmpPanel;
		for(int i=0;i<panelNum;i++){
			if(pnConditionFormat.getComponent(i) instanceof ConditionContentPanel){
				tmpPanel=((ConditionContentPanel)(pnConditionFormat.getComponent(i)));
				if(tmpPanel.getPanelCondition()!=null){
					if(conditionFmCurrent==null){
						conditionFmCurrent=new ArrayList<ConditionFormat>();
					}
					conditionFmCurrent.add(tmpPanel.getPanelCondition());
				}
			}
		}
		}	
		return conditionFmCurrent;
	}
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JButton) {
			JButton source = (JButton) event.getSource();
			if(source==ivjAddButton){
				pnConditionFormat.addContentPanel(null);
				pack();
			}
			if(source==ivjDelButton){
				pnConditionFormat.removeContentPanel();
				pack();
			}
			if(source==ivjOkButton){
				if(pnConditionFormat!=null&&pnConditionFormat.checkConditions()){
					setResult(UfoDialog.ID_OK);	
					this.close();
				}
			}
            if(source==ivjcancelButton){
            	setResult(UfoDialog.ID_CANCEL);
				this.close();
			}
		}
		
	}
	private class ConditionContentPanel extends UIPanel{
		private int posIndex;
		private ConditionFormat fConCurrent;
		/** 条件格式*/
		private Format cfmOriginal;
		/**nType-nValue;缓存新的格式设置值，当点击OK后，再真正保存*/
		private Hashtable<Integer, Integer> fCacheCurent = new Hashtable<Integer, Integer>();
		/** 条件操作符*/
		private JComboBox ivjoperatorComb;
		/** 条件值1*/
		private JTextField conditionText1=new UITextField(10);
		/** and面板，包括条件值2，可能不显示*/
		private JPanel andPanel=new JPanel();
		/** 条件值2*/
		private JTextField conditionText2=new UITextField(10);
		private JPanel previewPanel=new JPanel();
		private JLabel ivjpreviewLabel=new UILabel();
		private JButton formatButton;
		
		/**
		 * @i18n report00012=条件 
		 * @i18n report00005=单元格数值：
		 * @i18n report00006= 和 
		 * @i18n report00013=条件为真时，待用格式如右图所示：
		 * @i18n uiuforep0000787=预览
		 * @i18n report00007=未设定格式
		 * @i18n format=格式
		 */
		public ConditionContentPanel(int titleIndex){
			super();
			cfmOriginal=fmOriginal;
			setSize(600,150);
			posIndex=titleIndex;
			setBorder(BorderFactory.createTitledBorder(MultiLang.getString("report00012")+titleIndex));
			setName("ConditionPane"+titleIndex);
			GridBagLayout gridbaglayout = new GridBagLayout();
            GridBagConstraints gridbagconstraints = new GridBagConstraints();
            gridbagconstraints.insets=new Insets(0,2,0,2);
            
            setLayout(gridbaglayout);
            gridbagconstraints.weightx=0;
            gridbagconstraints.fill=GridBagConstraints.HORIZONTAL;
            JLabel jLabel1=new JLabel(MultiLang.getString("report00005"), SwingConstants.RIGHT);
            addToGB(jLabel1,this,gridbaglayout,gridbagconstraints);
            ivjoperatorComb=new UIComboBox();
            ivjoperatorComb.setName("ConditonOperator"+titleIndex);
            String[] operatorList=DefaultSetting.operatorList;
    		for(int i=0;i<operatorList.length;i++){
    			ivjoperatorComb.addItem(operatorList[i]);
    		}
    		ivjoperatorComb.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent event) {
    				int nSize = ivjoperatorComb.getSelectedIndex();
    				if(nSize>5){
    					andPanel.setVisible(true);
    				}else{
    					conditionText1.setText("");
    					andPanel.setVisible(false);
    			    	conditionText2.setText("");
    				}
    				pack();
    			}
    		});
    		
    		addToGB(ivjoperatorComb,this,gridbaglayout,gridbagconstraints);
    		
    		conditionText1.setName("ConditionBefore");
    		conditionText1.setDocument(new FloatDocument());
    		conditionText2.setName("ConditionAfter");
    		conditionText2.setDocument(new FloatDocument());
    		JLabel jLabelAnd=new UILabel();
   	        jLabelAnd.setText(MultiLang.getString("report00006"));
   	 	    addToGB(conditionText1, this, gridbaglayout, gridbagconstraints);
   	        addToGB(createAndPanel(jLabelAnd,conditionText2),this,gridbaglayout,gridbagconstraints);
   	     
   	        gridbagconstraints.gridy=1;
   	        gridbagconstraints.gridheight=2;
   	        gridbagconstraints.gridx=0;
   	        JTextArea jLabel2=new JTextArea();
            jLabel2.setText(MultiLang.getString("report00013"));
            jLabel2.setEditable(false);
            jLabel2.setLineWrap(true);
            addToGB(jLabel2,this,gridbaglayout,gridbagconstraints);
            previewPanel.setName("PreviewPane" + titleIndex);
			previewPanel.setBorder(new TitledBorder(new EtchedBorder(), MultiLang.getString("uiuforep0000787"), TitledBorder.LEFT, TitledBorder.TOP,// 预览
					new java.awt.Font("dialog", 0, 14)));
			ivjpreviewLabel.setName("PreviewLabel");
			ivjpreviewLabel.setForeground(java.awt.Color.black);
			ivjpreviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        ivjpreviewLabel.setOpaque(true);
	        
			ivjpreviewLabel.setText(MultiLang.getString("report00007"));
			previewPanel.add(ivjpreviewLabel);
			gridbagconstraints.gridwidth=2;
			gridbagconstraints.gridx=1;
			addToGB(previewPanel,this,gridbaglayout,gridbagconstraints);
			formatButton=new UIButton();
	        formatButton.setText(MultiLang.getString("format"));
			gridbagconstraints.gridwidth=1;
			gridbagconstraints.gridx=3;
			addToGB(formatButton,this,gridbaglayout,gridbagconstraints);
			formatButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					CellPropertyDialog dlg = new CellPropertyDialog(parent,cfmOriginal,false,true,true);
					//由于格式设计对话框只记录修改的格式属性，在修改之前需保存旧的格式属性
					dlg.setPropertyCache(fCacheCurent);
					dlg.setVisible(true);
		            if (dlg.getResult() == UfoDialog.ID_OK) { 
		            	fCacheCurent=dlg.getPropertyCache();
		            	showPreview();
		            	pack();
		            }
				}
	        });
		}
		public int getPosIndex() {
			return posIndex;
		}
		public void setPosIndex(int posIndex) {
			this.posIndex = posIndex;
		}
        public void updatePanel(ConditionFormat fConOriginal){
			if(fConOriginal!=null){
				int n=fConOriginal.getOperator();

				ivjoperatorComb.setSelectedIndex(n);
				conditionText1.setText(String.valueOf(fConOriginal.getCondition1()));
				//条件操作为‘介于’
				if(n>5){
			    	 conditionText2.setText(String.valueOf(fConOriginal.getCondition2()));
			    	 andPanel.setVisible(true);
					}
			     if(fConOriginal.getConditionFormat()!=null){
					 IufoFormat iufoFormat=(IufoFormat)fConOriginal.getConditionFormat();
					 cfmOriginal=iufoFormat;
					 showPreview(iufoFormat);
					}
			}
		}
        /**
		 * @i18n report00004=条件
		 * @i18n report00014=的条件值输入有误，请重新输入
		 */
        public boolean checkCondition(){
        	String text1=conditionText1.getText();
			String text2="";
			if(andPanel.isVisible()){
				text2=conditionText2.getText();
				if("".equals(text1)||"".equals(text2)||Float.parseFloat(text1)>Float.parseFloat(text2)){
					JOptionPane.showMessageDialog(this,MultiLang.getString("report00004")+getPosIndex()+MultiLang.getString("report00014"));
				    return false;
				}
			}
			
			return true;
        }
        public ConditionFormat getPanelCondition(){
        	if ("".equals(conditionText1.getText())) {
				return null;
			}
        	if (fConCurrent == null) {
				fConCurrent = new ConditionFormat();
			}
			fConCurrent.setOperator(ivjoperatorComb.getSelectedIndex());
			fConCurrent.setCondition1(conditionText1.getText());
			fConCurrent.setCondition2(conditionText2.getText());

			if (fCacheCurent != null && !fCacheCurent.isEmpty()) {
				Format format = new IufoFormat();
				Enumeration<Integer> enums = fCacheCurent.keys();
				while (enums.hasMoreElements()) {
					Integer tmpType = (Integer) enums.nextElement();
					int nType = tmpType.intValue();
					int nValue = ((Integer) fCacheCurent.get(tmpType))
							.intValue();
					PropertyType.setPropertyByType(format, nType, nValue);
				}
				fConCurrent.setConditionFormat(format);
			}
			return fConCurrent;
        }
        /**
		 * 由格式信息更新预览
		 * 
		 * @param format
		 */
        public void showPreview(IufoFormat format) {
    		
    		if(format!=null){
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
                    		 fCacheCurent.put(PropertyType.FontIndex, i);
                    	}
                    }
                    if(format.getFontsize()!=TableConstant.UNDEFINED){
                    	fontSize=format.getFontsize();
                    	fCacheCurent.put(PropertyType.FontSize, fontSize);
                    }
                    if(format.getFontstyle()!=TableConstant.UNDEFINED){
                    	fontStyle=format.getFontstyle();
                    	fCacheCurent.put(PropertyType.FontStyle, fontStyle);
                    }
                    if(b!=null){
    	            	ivjpreviewLabel.setBackground(b);
    	            	fCacheCurent.put(PropertyType.BackColor, b.getRGB());
                    }
                    if(c!=null){
    	            	ivjpreviewLabel.setForeground(c);
    	            	fCacheCurent.put(PropertyType.ForeColor, c.getRGB());
                    }
    	        Font f = new Font(fontName,fontStyle,fontSize);
                ivjpreviewLabel.setFont(f);
    		}
    	}
        /**
    	 * 根据设置值,显示preview面板.
    	 */
    	public void showPreview() {
    		if(fCacheCurent!=null&&!fCacheCurent.isEmpty()){
    			ivjpreviewLabel.setText("AaBbCcXxYy");
    			String fontName = DefaultFormatValue.FONTNAME;
    			int fontSize = DefaultFormatValue.FONT_SIZE;
    			int fontStyle = DefaultFormatValue.FONT_STYLE;
    			Enumeration<Integer> enums = fCacheCurent.keys();
    	        while (enums.hasMoreElements()) {
    	        	Integer tmpType = (Integer) enums.nextElement();
    	            int nType = tmpType.intValue();
    	            int nValue = ((Integer) fCacheCurent.get(tmpType)).intValue();
    	            
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
        private void addToGB(Component component, Container container, GridBagLayout gridbaglayout, GridBagConstraints gridbagconstraints)
        {
            gridbaglayout.setConstraints(component, gridbagconstraints);
            container.add(component);
        }
        private JPanel createAndPanel(JLabel jlabel,JTextField jtextfield){
        	GridBagLayout gridbaglayout = new GridBagLayout();
    		GridBagConstraints gridbagconstraints = new GridBagConstraints();
    		andPanel.setLayout(gridbaglayout);
    		gridbagconstraints.weightx = 0.0D;
    		addToGB(jlabel, andPanel, gridbaglayout, gridbagconstraints);
    		jlabel.setLabelFor(jtextfield);
    		addToGB(jtextfield, andPanel, gridbaglayout, gridbagconstraints);
    		andPanel.setVisible(false);
    		return andPanel;
        }
	}
	private class ConditionFormatPanel extends UIPanel{
		private GridBagLayout gridbaglayout = new GridBagLayout();
		private GridBagConstraints gridbagconstraints = new GridBagConstraints();
		
		public ConditionFormatPanel(){
			super();
			
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			setLayout(gridbaglayout);
			if(conditionFmOriginal==null){
				addContentPanel(null);
			}else{
				for(int i=0;i<conditionFmOriginal.size();i++){
					if(conditionFmOriginal.get(i)!=null){
						addContentPanel(conditionFmOriginal.get(i));
					}
				}
				
			}
			
		}
		public void addContentPanel(ConditionFormat contents){
			conditionNum++;
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			ConditionContentPanel jpanel=new ConditionContentPanel(conditionNum);
			jpanel.updatePanel(contents);
			gridbaglayout.setConstraints(jpanel, gridbagconstraints);
			add(jpanel);		
		}
		public void removeContentPanel(){
			if(conditionNum>0){
			 conditionNum--;
			 remove(conditionNum);
			 
			}
			if(conditionNum==0){
				 addContentPanel(null); 
			 }
		}
		public boolean checkConditions(){
			boolean isTrue=true;
			int panelNum=getComponentCount();
			ConditionContentPanel checkPanel;
			for(int j=0;j<panelNum;j++){
				if(getComponent(j) instanceof ConditionContentPanel){
					checkPanel=(ConditionContentPanel)getComponent(j);
					isTrue=checkPanel.checkCondition();
				}
				if(!isTrue) break;
			}
			return isTrue;
		}
	}
	public static void main(String[] args){
		MultiConditionFormatDialog t=new MultiConditionFormatDialog();
		t.setVisible(true);
		
	}
	
	
}
 