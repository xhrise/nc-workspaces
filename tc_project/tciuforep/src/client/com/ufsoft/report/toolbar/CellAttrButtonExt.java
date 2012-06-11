package com.ufsoft.report.toolbar;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.AbstractButton;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * Button�͵ĸ�ʽ�������ù�����
 * @author guogang
 *
 */
public class CellAttrButtonExt extends SetCellAttrExt {
    /** Ҫ���õĸ�ʽ����*/
	private int propertyName;
	/** Ҫ���õĸ�ʽ����ֵ*/
	private int propertyValue;
	/** �ø�ʽ���Ե�Ĭ��ֵ,�ɹ��췽����̬����*/
	private int propertyDefaultValue;
	/** �ù�������������ƣ��ɹ��췽����̬����*/
	private String extName;
	/** �����������ͼƬ,�ɹ��췽����̬����*/
	private String imageFile;
	/**
	 * �ڹ����µĲ����ʱ��ע��SetName(),setDefaultValue(),isFormatSetted()�Ƿ���ȷ
	 * @param propertyName PropertyType�ж���ĸ�ʽ����name�����������Ҫ���õĸ�ʽ����
	 * @param propertyValue TableConstant�ж���ĸ�ʽ����ֵ�����������Ҫ���õĸ�ʽ����ֵ
	 * @param rep
	 */
	public CellAttrButtonExt(int propertyName,int propertyValue,UfoReport rep){
		super(rep);
		this.propertyName=propertyName;
		this.propertyValue=propertyValue;
		setDefaultValue(propertyName);
		SetName(propertyName,propertyValue);
	}
	/**
	 * ��������Ϣ��ȡ������ƺ�ͼƬ����
	 * @param propertyName
	 * @param propertyValue
	 */
	public void SetName(int propertyName,int propertyValue){
		switch(propertyName){
		case PropertyType.FontStyle:
			  
			  switch(propertyValue){
			  case TableConstant.FS_BOLD:
				  extName=MultiLang.getString("bold");
				  imageFile="reportcore/bold.gif";
			  break;
			  case TableConstant.FS_SLOPE:
				   extName=MultiLang.getString("italic");
				   imageFile="reportcore/italic.gif";
			  break;
			  default :
				  extName=DefaultSetting.fontStyles[0];
			      imageFile=null;
			  break;
			  }	
	    break;
		case PropertyType.HorAlig:
			 switch(propertyValue){
			 case TableConstant.HOR_LEFT:
				  extName=MultiLang.getString("uiuforep0000757");
			      imageFile="reportcore/snap_to_left.gif";
			 break;
			 case TableConstant.HOR_CENTER:
				  extName=MultiLang.getString("uiuforep0000756");
			      imageFile="reportcore/mediacy.gif";
			 break;
			 case TableConstant.HOR_RIGHT:
				  extName=MultiLang.getString("uiuforep0000758");
			      imageFile="reportcore/snap_to_right.gif";
			 break;
			 default :
				  extName="";
			      imageFile=null;
			  break;
			 }
		break;
		case PropertyType.ChangeLine:
			 extName=MultiLang.getString("wordwrap");
			 imageFile="reportcore/wordwrap.gif";
		break;
		case PropertyType.ShrinkFit:
			 extName=MultiLang.getString("shrink");
			 imageFile="reportcore/shrink.gif";
		break;
		default :
			extName="";
	        imageFile=null;
		break;
		}
	}
	/**
	 * ��������Ϣ���ø����Ե�Ĭ��ֵ
	 * @param propertyName
	 */
	public void setDefaultValue(int propertyName){
		switch (propertyName) { 
		case PropertyType.FontStyle:
			propertyDefaultValue=TableConstant.FS_NORMAL;
			break;
		case PropertyType.HorAlig:
			propertyDefaultValue=TableConstant.UNDEFINED;
			break;
		case PropertyType.ChangeLine:
			 propertyDefaultValue=TableConstant.FALSE;
			 break;
		case PropertyType.ShrinkFit:
			 propertyDefaultValue=TableConstant.FALSE;
			 break;
		default: 
			propertyDefaultValue=TableConstant.UNDEFINED;
		    break;
		}
	}
	public int getPropertyDefaultValue() {
		return propertyDefaultValue;
	}
	public String getExtName() {
		return extName;
	}
	public String getImageFile() {
		return imageFile;
	}
	/**
	 * ��Ҫ�����������Ƿ������˸ø�ʽ����
	 * @param format ��Ҫ����������ĸ�ʽ��Ϣ
	 * @return
	 */
	private boolean isFormatSetted(Format format){
		boolean isSet=false;
		if(format!=null){
			switch (propertyName) { 
			case PropertyType.FontStyle:
				isSet=format.getFontstyle()==propertyValue;
				break;
			case PropertyType.HorAlig:
				isSet=format.getHalign()==propertyValue;
				break;
			case PropertyType.ChangeLine:
				isSet=format.isFold();
				break;
			case PropertyType.ShrinkFit:
				isSet=format.isShrink();
				break;
			default: 
				isSet=true;
			    break;
			}
		}
		return isSet;
	}
	/**
	 * override
	 * 
	 * @see AbsActionExt.getUIDesArr();
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setToolBar(true);
		uiDes1.setGroup(MultiLang.getString("uiuforep0000870"));
		uiDes1.setPaths(new String[] {});
		
		uiDes1.setName(getExtName());
		uiDes1.setTooltip(getExtName());
		uiDes1.setImageFile(getImageFile());
		
		return new ActionUIDes[] { uiDes1 };
	}
	/**
	 * override
	 * @see AbsActionExt.getParams();
	 */
	public Object[] getParams(UfoReport container) {
		Object[] params = new Object[8 + 1];
		params = getCellParams(params);
		Format format = computeFormat();
		Hashtable<Integer, Integer> m_propertyCache = new Hashtable<Integer, Integer>();
		//���û�����ø�ʽ,������ΪĬ��ֵ
		if(isFormatSetted(format)){
			m_propertyCache.put(new Integer(propertyName),
					new Integer(propertyDefaultValue));
		}else{
			m_propertyCache.put(new Integer(propertyName),
					new Integer(propertyValue));
		}
		params[0] = m_propertyCache;
		return params;
	}
	
	/**
	 * override
	 * �������cellspane�ϡ�reporttoolbar�϶�����ø÷���
	 * UfoReport.setFocusComp()������÷����ĵ���
	 * @see AbsActionExt.isEnabled()
	 */
	public boolean isEnabled(Component focusComp) {

		if (focusComp instanceof AbstractButton) {
			AbstractButton t = (AbstractButton) focusComp;
			if (getExtName().equals(
					focusComp.getName())) {
				CellPosition selectCell = getReport().getCellsModel()
						.getSelectModel().getAnchorCell();
				Format cellFormat = getReport().getCellsModel().getRealFormat(
						selectCell);
			
				if (t.isBorderPainted()) {
					t.setBorderPainted(false);
				} else {
					t.setBorderPainted(true);
				}
				return updateEnable(((AbstractButton) focusComp),cellFormat);
			}
		}
		return true;
	}
	
	/**
	 * override
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(final Component stateChangeComp) {
		getReport().getCellsModel().getSelectModel().addSelectModelListener(
				new SelectListener() {
					public void selectedChanged(SelectEvent e) {
						if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
							CellPosition selectCell = getReport()
									.getCellsModel().getSelectModel()
									.getAnchorCell();
							Format cellFormat = getReport().getCellsModel()
									.getRealFormat(selectCell);
							if (stateChangeComp instanceof AbstractButton) {

								if (getExtName().equals(
										stateChangeComp.getName())) {
									//����Ԫ���л���ʱ����¹�������״̬
									if (!isFormatSetted(cellFormat)) {
										((AbstractButton) stateChangeComp)
												.setBorderPainted(false);
									} else {
										((AbstractButton) stateChangeComp)
												.setBorderPainted(true);
									}
								}
							}
						}
					}

				});
	}
	/**
	 * ֻ�������ø����Ե�ʱ����ã���Ҫ�������⻥���ų����������
	 * @param button
	 * @param cellFormat
	 */
	private boolean updateEnable(AbstractButton button,Format cellFormat){
		boolean isEnable=true;
	    if(propertyName==PropertyType.ChangeLine&&cellFormat.isShrink()){
	    	isEnable=false;
	    }
	    if(propertyName==PropertyType.ShrinkFit&&cellFormat.isFold()){
	    	isEnable=false;
	    }
	    return isEnable;
	}
}
