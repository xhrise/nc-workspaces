/**
 * 
 */
package com.ufida.report.rep.applet.exarea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.dsmanager.FilterDescPanel;
import com.ufida.dataset.DataSet;
import com.ufida.dataset.descriptor.FilterDescriptor;
import com.ufida.dataset.descriptor.FilterItem;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufsoft.report.AbstractWizardTabPanel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author guogang
 *
 */
public class ExAreaFilterSetPanel extends AbstractWizardTabPanel implements ItemListener {
    
	private JPanel contentPanel;
	private AnaReportFilterDescPanel m_panel;
	private JRadioButton rbDefaultField;
	private JRadioButton rbAgainField;
	
	
	public ExAreaFilterSetPanel(){
		
	}
	/**
	 * 
	 */
	public ExAreaFilterSetPanel(DataSet dataSet,FilterDescriptor areafilter) {
		getFilterDescPanel().setDataSet(dataSet);
		getFilterDescPanel().setDescriptor(areafilter);
	}

	public void initData(DataSet dataSet,FilterDescriptor areafilter){
		getFilterDescPanel().setDataSet(dataSet);
		getFilterDescPanel().setDescriptor(areafilter);
		if(getFilterDescPanel().getDescriptor()!=null&&getFilterDescPanel().getDescriptor().isAgainFilter()){
			getRbAgain().setSelected(true);
		}else{
			getRbDefault().setSelected(true);
		}
		getFilterDescPanel().reload();
	}
   
	/* (non-Javadoc)
	 * @see com.ufsoft.report.AbstractWizardTabPanel#addListener()
	 */
	@Override
	public void addListener() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.AbstractWizardTabPanel#getContentPanel()
	 */
	/**
	 * @i18n iufobi00004=筛选类型
	 */
	@Override
	public JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new UIPanel();
			contentPanel.setLayout(new BorderLayout());
			JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlFilterType=new UILabel(StringResource.getStringResource("iufobi00004"));
			jlFilterType.setPreferredSize(new Dimension(100,20));
			toppanel.add(jlFilterType);
			ButtonGroup setgroup = new ButtonGroup();
			setgroup.add(getRbDefault());
			toppanel.add(getRbDefault());
			setgroup.add(getRbAgain());
			toppanel.add(getRbAgain());
			contentPanel.add(toppanel, BorderLayout.NORTH);
			contentPanel.add(getFilterDescPanel(), BorderLayout.CENTER);
		}
		return contentPanel;
	}
    
	/**
	 * @i18n iufobi00005=重新过滤
	 */
	private JRadioButton getRbAgain(){
		if(rbAgainField==null){
			rbAgainField = new UIRadioButton(StringResource.getStringResource("iufobi00005"));
			rbAgainField.addItemListener(this);
		}
		return rbAgainField;
	}
	/**
	 * @i18n iufobi00006=叠加过滤
	 */
	private JRadioButton getRbDefault(){
		if(rbDefaultField==null){
			rbDefaultField = new UIRadioButton(StringResource.getStringResource("iufobi00006"));
			rbDefaultField.addItemListener(this);
		}
		return rbDefaultField;
	}
    public AnaReportFilterDescPanel getFilterDescPanel(){
    	if(m_panel==null){
			m_panel=new AnaReportFilterDescPanel();
		}
		return m_panel;
    }
	/* (non-Javadoc)
	 * @see com.ufsoft.report.AbstractWizardTabPanel#getStepTitle()
	 */
	/**
	 * @i18n miufo00429=筛选条件设置
	 */
	@Override
	public String getStepTitle() {
		return StringResource.getStringResource("miufo00429");
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.AbstractWizardTabPanel#initInfo()
	 */
	@Override
	public void initInfo() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.AbstractWizardTabPanel#removeListener()
	 */
	@Override
	public void removeListener() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.AbstractWizardTabPanel#updateInfo()
	 */
	@Override
	public boolean updateInfo() {
		getFilterDescPanel().stopTableEdit();
		FilterDescriptor fd = getFilterDescPanel().getDescriptor();
		FilterItem[] fis = fd.getFilters();
		for(FilterItem item: fis){
			if(!item.validate()){
//				fd.removeFilter(item);
				return false;
			}
			// 字段为数值类型，而 值不是数值类型  时 提示 added by biancm 20091028
			if(item.getValueType() == FilterItem.TYPE_CONST){
				String value = item.getValue();
				int dataType = item.getDataType();
				if(DataTypeConstant.isNumberType(dataType)){
					try {
						Double.parseDouble(value);
					} catch (NumberFormatException e) {
						return false;
					}
				}
			}
		}
		if(rbAgainField.isSelected()){
			fd.setAgainFilter(true);
		}else{
			fd.setAgainFilter(false);
		}
		
		return true;
	}

	public class AnaReportFilterDescPanel extends FilterDescPanel{

		public AnaReportFilterDescPanel() {
			super(null,new FilterDescriptor());

		}

		@Override
		protected void initDescriptor(DataSet dataSet) {
			
		}

		@Override
		protected void setDescriptor(FilterDescriptor sd) {
			super.setDescriptor(sd);
		}
		
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == getRbAgain() || e.getSource() == getRbDefault()) {
			getFilterDescPanel().setDirty(true);
		}

	}

	
}
  