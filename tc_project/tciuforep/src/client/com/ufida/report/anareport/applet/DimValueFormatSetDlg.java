package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.report.crosstable.DimInfo;
import com.ufida.report.crosstable.DimValueSet;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.cellattr.CellPropertyDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 数据态下分析表值格式设计
 * @author guogang
 *
 */
public class DimValueFormatSetDlg extends UfoDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object[] dimKeys=null;
	private DimInfo dimInfo=null;
	private Format cellFmOriginal=null;
	
	/** 对话框的父容器*/
	private Container parent;
	private JComboBox ivjdimKeysComb;
	private UIButton ivjAddButton;
	private UIButton ivjDelButton;
	private FormatSetPanel pnFormatSetPanel;

	public DimValueFormatSetDlg(Container parent,DimValueSet[] dimValues,DimInfo dimInfo,Format cellFormat){
		super(parent);
		this.parent=parent;
		this.dimKeys=new Object[dimValues.length+1];
		this.dimKeys[0]=DimInfo.formatKey;
		for(int i=0;i<dimValues.length;i++){
			this.dimKeys[i+1]=dimValues[i];
		}
		this.dimInfo=dimInfo;
		this.cellFmOriginal=cellFormat;
		initialize();
	}
	
	/**
	 * @i18n miufo00180=值格式设置
	 */
	private void initialize() {

		setName("DimValueFormatDialog");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(StringResource.getStringResource("miufo00180"));
		
		GridBagLayout gridbaglayout = new GridBagLayout();
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.weightx = 1.0D;
		Container container = getContentPane();
		container.setLayout(gridbaglayout);

		gridbagconstraints.insets=new Insets(0,2,0,2);
		
		ivjdimKeysComb=new UIComboBox(dimKeys);
		ivjAddButton=createButton(MultiLang.getString("report00010"), this);
		ivjDelButton=createButton(MultiLang.getString("report00011"), this);
		ivjdimKeysComb.addItemListener(new ItemListener(){

			public void itemStateChanged(ItemEvent e) {
				if(ivjdimKeysComb.getSelectedIndex()==0){
					ivjDelButton.setEnabled(false);
				}else{
					ivjDelButton.setEnabled(true);
				}
				
			}
			
		});
		ivjDelButton.setEnabled(false);
		
		gridbaglayout.setConstraints(ivjdimKeysComb, gridbagconstraints);
		container.add(ivjdimKeysComb);
		
		gridbagconstraints.gridwidth=GridBagConstraints.RELATIVE;
		gridbaglayout.setConstraints(ivjAddButton, gridbagconstraints);
		container.add(ivjAddButton);
        
        gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
        gridbaglayout.setConstraints(ivjDelButton, gridbagconstraints);
        container.add(ivjDelButton);
		
        pnFormatSetPanel=new FormatSetPanel();
        gridbaglayout.setConstraints(pnFormatSetPanel, gridbagconstraints);
        container.add(pnFormatSetPanel);
        
		JPanel jpanel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
		jpanel.add(createOkButton());
		jpanel.add(createCancleButton());
		gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
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
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ivjAddButton){
			Object key=ivjdimKeysComb.getSelectedItem();
			pnFormatSetPanel.addContentPanel(key);
			pack();
		}
        if(e.getSource()==ivjDelButton){
        	Object key=ivjdimKeysComb.getSelectedItem();
			pnFormatSetPanel.removeContentPanel(key);
			pack();
		}
		
	}
    
	public Format getDimValueFormat(DimValueSet key){
		return pnFormatSetPanel.getFormatSet(key);
	}
	
	public Format getCellFormat(){
		return pnFormatSetPanel.getFormatSet(DimInfo.formatKey);
	}
	private class DimValueFormatPanel extends UIPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Object formateKey=null;
		private IufoFormat dimFmOriginal=null;
		/**nType-nValue;缓存新的格式设置值，当点击OK后，再真正保存,主要用户外部格式预览*/
		private Hashtable<Integer, Integer> fCacheCurent;
		
		private JPanel previewPanel=new UIPanel();
		private JLabel ivjpreviewLabel=new UILabel();
		private JButton formatButton;
		
		public DimValueFormatPanel(Object dimValue,IufoFormat valueFormat,Hashtable<Integer, Integer> fCache){
			super();	
			this.formateKey=dimValue;
			this.dimFmOriginal=valueFormat;
			this.fCacheCurent=fCache;
			this.setPreferredSize(new Dimension(330,50));
			
            setLayout(new BorderLayout());


			previewPanel.setName("PreviewPane" + dimValue);
			previewPanel.setBorder(new TitledBorder(new EtchedBorder(), dimValue.toString(), TitledBorder.LEFT, TitledBorder.TOP,// 预览
					new java.awt.Font("dialog", 0, 14)));
			ivjpreviewLabel.setName("PreviewLabel");
			ivjpreviewLabel.setText("AaBbCcXxYy");
			ivjpreviewLabel.setForeground(java.awt.Color.black);
			ivjpreviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        ivjpreviewLabel.setOpaque(true);
	        previewPanel.add(ivjpreviewLabel);
	        add(previewPanel,BorderLayout.CENTER);
	        
	        formatButton=new UIButton();
	        formatButton.setText(MultiLang.getString("format"));
	        JPanel panel1 = new JPanel();
			panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
			panel1.add(formatButton);
	        add(panel1,BorderLayout.EAST);
	        
	        formatButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					CellPropertyDialog dlg = new CellPropertyDialog(parent,dimFmOriginal,false,true,false);
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
	        showPreview(dimFmOriginal);
		}


		public Object getFormateKey() {
			return formateKey;
		}


		public Format getDimFormat() {
			if (fCacheCurent != null && !fCacheCurent.isEmpty()) {
				if(dimFmOriginal==null){
					dimFmOriginal=new IufoFormat();
				}
				Enumeration<Integer> enums = fCacheCurent.keys();
				while (enums.hasMoreElements()) {
					Integer tmpType = (Integer) enums.nextElement();
					int nType = tmpType.intValue();
					int nValue = ((Integer) fCacheCurent.get(tmpType))
							.intValue();
					PropertyType.setPropertyByType(dimFmOriginal, nType, nValue);
				}
			}
			return dimFmOriginal;
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
		 /**
		 * 由格式信息更新预览
		 * 
		 * @param format
		 */
        public void showPreview(IufoFormat format) {
    		
    		if(format!=null){
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
	}
	
	private class FormatSetPanel extends UIPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private GridBagLayout gridbaglayout = new GridBagLayout();
		private GridBagConstraints gridbagconstraints = new GridBagConstraints();
		private Hashtable<Object,DimValueFormatPanel> panelList=new Hashtable<Object,DimValueFormatPanel>();
		
		public FormatSetPanel(){
			super();
			
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
			setLayout(gridbaglayout);
			Format initFmt=null;
			
			if(dimKeys[0]!=null){
			  addContentPanel(dimKeys[0]);
			}	
			for(int i=1;i<dimKeys.length;i++){
				initFmt=dimInfo.getDimValueFormat((DimValueSet)dimKeys[i]);
				if(initFmt!=null){
					addContentPanel(dimKeys[i]);
				}
			}
		}
		/**
		 * @i18n miufo00181=格式设置已经存在,可进行修改
		 */
		public void addContentPanel(Object key){
			if(panelList.get(key)!=null){
				UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00181"), DimValueFormatSetDlg.this);
				return;
			}
			DimValueFormatPanel jpanel=null;
			gridbagconstraints.gridwidth=GridBagConstraints.REMAINDER;
			Hashtable<Integer, Integer> fCacheCurent=new Hashtable<Integer, Integer>();
			IufoFormat oldFormat=null;
			if(key instanceof String){
				 oldFormat=(IufoFormat)cellFmOriginal;
				 jpanel=new DimValueFormatPanel(key,oldFormat,fCacheCurent);
			}else{
				 oldFormat=(IufoFormat)dimInfo.getDimValueFormat((DimValueSet)key);
				 if (oldFormat == null) {
					 oldFormat=new IufoFormat();
				 }
					if (DataTypeConstant.isNumberType(dimInfo.getDataType())) {
						oldFormat.setCellType(TableConstant.CELLTYPE_NUMBER);
					} else {
						oldFormat.setCellType(TableConstant.CELLTYPE_SAMPLE);
					}
				
				 jpanel=new DimValueFormatPanel(key,oldFormat,fCacheCurent);
			}
			gridbaglayout.setConstraints(jpanel, gridbagconstraints);
			panelList.put(key, jpanel);
			add(jpanel);
			
		}
		public void removeContentPanel(Object key) {
			
			if (panelList.get(key) != null) {
				remove(panelList.get(key));
				panelList.remove(key);
			}

		}
		
		public Format getFormatSet(Object key){
			Format fmtSet=null;
			if(panelList.get(key)!=null){
				fmtSet=panelList.get(key).getDimFormat();
			}
			return fmtSet;
		}
 
	}
	
	public static void main(String[] args){
		DimValueFormatSetDlg dlg=new DimValueFormatSetDlg(null,new DimValueSet[0],null,null);
		dlg.show();
	}
}
 