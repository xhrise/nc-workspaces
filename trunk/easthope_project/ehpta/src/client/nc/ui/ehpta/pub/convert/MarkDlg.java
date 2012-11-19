package nc.ui.ehpta.pub.convert;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.Border;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.ref.TempletItemRefPane;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.textfield.UITextType;

/**
 * 根据单据模板中可编辑的字段显示参照内容，再工具字段的类型修改UITextFiled的类型。
 * 
 * @author river
 */
public class MarkDlg extends UIDialog implements ActionListener {

	private UIButton btnCancel = null;

	private UIButton btnOK = null;

	private UIPanel btnPanel = null;

	private UIPanel contentPanel = null;

	private UIPanel filterPanel = null;

	private String title = null;

	private UIRefPane fieldRef = null;
	
	private Component txtValue = null;
	
	private static String pk_billtype = null;

	protected MarkDlg mark = null;
	
	private MarkDlg() {
		
	}
	
	private MarkDlg(Container parent ) throws Exception {
		super(parent);

		init();

		mark = this;
	}
	
	public static final MarkDlg getInstance(Container parent , String pk_billtype ) throws Exception {

		MarkDlg.pk_billtype = pk_billtype;
		
		return new MarkDlg(parent);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getBtnCancel()) {
			this.closeCancel();
		} else if (e.getSource() == getBtnOK()) {
				this.closeOK();
		}
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton();
			btnCancel.setName("m_btnCancel");
			btnCancel.setText("取消");
		}
		return btnCancel;
	}

	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setName("m_btnOK");
			btnOK.setText("确定");
		}
		return btnOK;
	}

	private Border getBorderByTitle(String title) {
		Border etched = BorderFactory.createEtchedBorder();
		return BorderFactory.createTitledBorder(etched, title);
	}

	private UIPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
			btnPanel.add(Box.createHorizontalGlue());
			btnPanel.add(getBtnOK());
			btnPanel.add(getBtnCancel());
			btnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		}
		return btnPanel;
	}

	private UIPanel getFilterPanel() throws Exception {
		if (filterPanel == null) {
			filterPanel = new UIPanel();

			Box hbox1 = Box.createHorizontalBox();
			hbox1.add(Box.createVerticalStrut(5));
			hbox1.add(new UILabel("属性 ： "));
			hbox1.add(Box.createHorizontalStrut(5));
			hbox1.add(getFieldRef());
			
			Box hbox2 = Box.createHorizontalBox();
			hbox2.add(Box.createVerticalStrut(5));
			hbox2.add(new UILabel("数值 ： "));
			hbox2.add(Box.createHorizontalStrut(5));
			hbox2.add(getTxtValue());

			Box vbox = Box.createVerticalBox();
			vbox.add(Box.createVerticalStrut(10));
			vbox.add(hbox1);
			vbox.add(Box.createVerticalStrut(10));
			vbox.add(hbox2);

			filterPanel.add(vbox);

		}
		return filterPanel;
	}

	private boolean isLegal(String string) {
		if (string != null && string.trim().length() > 0)
			return true;
		return false;
	}

	private UIPanel getUIDialogContentPane() throws Exception {
		if (contentPanel == null) {
			contentPanel = new UIPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getFilterPanel(), BorderLayout.WEST);
			contentPanel.add(getBtnPanel(), BorderLayout.SOUTH);
		}
		return contentPanel;
	}

	private void init()  throws Exception {
		initUI();
		initListener();

	}

	private void initListener() throws Exception {
		getBtnOK().addActionListener(this);
		getBtnCancel().addActionListener(this);
	}

	/**
	 * 初始化状态
	 */
	public void initStates() {

	}

	private void setFieldStatus() {

	}

	private void initUI()  throws Exception {
		setName("Message filter config");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(350, 155);
		setResizable(true);
		setTitle("批改");
		setContentPane(getUIDialogContentPane());
		initStates();
	}

	/**
	 * 恢复默认值
	 * 
	 * @return
	 */
	public void restoreDefaultValue() {

		setFieldStatus();
	}

	private boolean validateInput() {
			
		return true;
	}

	public UIPanel getContentPanel()  throws Exception {
		if(contentPanel == null) {
			contentPanel = new UIPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getFilterPanel(), BorderLayout.CENTER);
			contentPanel.add(getBtnPanel(), BorderLayout.SOUTH);
			
		}
		return contentPanel;
	}

	public void setContentPanel(UIPanel contentPanel) {
		this.contentPanel = contentPanel;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public UIRefPane getFieldRef() throws Exception {
		if(fieldRef == null) {
			fieldRef = new UIRefPane();
			fieldRef.setName("filed");
			fieldRef.setRefModel(new TempletItemRefPane()); 
			fieldRef.setWhereString(" 1 = 1 and pk_billtypecode = '"+pk_billtype+"' and showflag = 1 and pos = 1 and editflag = 1 ");
			fieldRef.setPreferredSize(new Dimension(249 , 22));
			fieldRef.addValueChangedListener(new ValueChangedListener() {
				
				public void valueChanged(ValueChangedEvent e) {
					try {
						UIRefPane source = (UIRefPane) e.getSource();
						Map retMap = (HashMap) UAPQueryBS.getInstance().executeQuery("select datatype , reftype from pub_billtemplet_b where pk_billtemplet_b = '"+source.getRefPK()+"'", new MapProcessor());
						
						// datatype数据类型引用自 IRefPane 接口
						switch(Integer.valueOf(retMap.get("datatype").toString())) {
						
							case 0 :
								txtValue = new UITextField();
								((UITextField)txtValue).setTextType(UITextType.TextStr);
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								break;
							
							case 1 : 
								
								txtValue = new UITextField();
								((UITextField)txtValue).setTextType(UITextType.TextInt);
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								
								break;
								
							case 2 : 
								txtValue = new UITextField();
								((UITextField)txtValue).setTextType(UITextType.TextDbl);
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								
								break;
								
							case 3 : 
								txtValue = new UITextField();
								((UITextField)txtValue).setTextType(UITextType.TextDate);
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								
								break;
								
							case 4 : 
								txtValue = new UICheckBox();
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								break;
								
							case 5 : 
								txtValue = new UIRefPane();
								if(retMap.get("reftype") != null) {
									if("<".equals(retMap.get("reftype").toString().charAt(0)) && ">".equals(retMap.get("reftype").toString().charAt(retMap.get("reftype").toString().length() - 1))) {
										String refModeName = retMap.get("reftype").toString().substring(1 , retMap.get("reftype").toString().length() - 1);	
										((UIRefPane)txtValue).setRefModel((AbstractRefModel) Class.forName(refModeName).newInstance());
									} else 
										((UIRefPane)txtValue).setRefNodeName(retMap.get("reftype").toString().split(",")[0]);
								
								}
								
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								break;
								
							default :
								txtValue = new UITextField();
								txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
								break;
									
						}
						
						btnCancel = null;

						btnOK = null;

						btnPanel = null;

						contentPanel = null;

						filterPanel = null;

						title = null;

						mark.init();
						mark.setVisible(false);
						mark.repaint();
						mark.setVisible(true);
						
					} catch(Exception ex) {
						Logger.error(ex.getMessage());
					}
				}
			});
		}
		
		return fieldRef;
	}
	
	public Component getTxtValue() {
		if(txtValue == null) {
			txtValue = new UITextField();
			txtValue.setPreferredSize(new java.awt.Dimension(249, 22));
		}
		
		return txtValue;
	}

	public void setBtnCancel(UIButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	public void setBtnOK(UIButton btnOK) {
		this.btnOK = btnOK;
	}

	public void setBtnPanel(UIPanel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public void setFilterPanel(UIPanel filterPanel) {
		this.filterPanel = filterPanel;
	}
}
