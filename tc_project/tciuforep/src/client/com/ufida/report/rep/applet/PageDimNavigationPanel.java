/*
 * Created on 2005-6-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;


import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.anareport.model.AnaPropertyType;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportToolBarPanel;

/**
 * 页纬度导航面板 对托拽目标采用授权方式实现
 * 
 * @author caijie
 */
public class PageDimNavigationPanel extends ReportToolBarPanel implements
		PropertyChangeListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7747811211885671260L;

	/**
	 * @i18n mbiadhoc00039=页维度
	 */
	public static final String  PageDimNavigation = StringResource.getStringResource("mbiadhoc00039");

	//页维度字段列表，维护页维度字段的顺序
	private ArrayList<PageDimField> pageDimList = null;

	/**
	 * This is the default constructor
	 */
	public PageDimNavigationPanel() {
		super();
		this.setName(PageDimNavigation);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected void initialize() {
			
		pageDimList = new ArrayList<PageDimField>();
	}
    
	
	/**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	
	@SuppressWarnings("unchecked")
	public void addPageDim(PageDimField pageDimField) {
		if (pageDimField == null) return;	
		pageDimList.add(pageDimField);
		PageDimItem item = new PageDimItem(pageDimField);
		item.getPropertyChangeSupport().addPropertyChangeListener(this);
		pageDimField.addChangeListener(this);
		addPageDimItem(item);
		refresh();
	}
	
	/**
	 * 排序	 
	 */
	@SuppressWarnings("unchecked")
	private void sort(){
		pageDimList.trimToSize();
		if(pageDimList.size() < 2) return;
		
		ArrayList<PageDimField> list  = new ArrayList<PageDimField>();	
		list.add(pageDimList.get(0));		
		for(int i = 1; i < pageDimList.size(); i++){
			boolean added = false;
			int pos =  pageDimList.get(i).getPos();
			for(int j = 0; j < list.size(); j++){				
				if((list.get(j)).getPos() >= pos){
					list.add(j, pageDimList.get(i));
					added = true;
					break;
				}
			}
			if(added == false) list.add(pageDimList.get(i));
		}
		pageDimList = list;
		pageDimList.trimToSize();
	}
	private void refresh(){	
		this.validate();
		this.invalidate();
		this.repaint();
	}
	public void removePageDimItem(PageDimItem pageItem){
		if(pageItem==null)
			return;
		remove(pageItem);
		refresh();
		pageDimList.remove(pageItem.getPageDimField());
	}
	public void removePageDim(PageDimField pageDimField) {
		if (pageDimField == null)
			return;
		pageDimList.trimToSize();
		if (pageDimList.remove(pageDimField)) {
			int num = this.getComponentCount();
			PageDimItem item = null;
			for (int i = 0; i <num; i++) {
				if (this.getComponent(i) instanceof PageDimItem)
					item = (PageDimItem) this.getComponent(i);
				if (item!=null&&item.getPageDimField().equals(pageDimField)) {

					item.getPageDimField().removeChangeListener(item);
					item.getPropertyChangeSupport()
							.removePropertyChangeListener(this);
					remove(item);
					break;
				}
			}
			refresh();
		}

	}	

	private void addPageDimItem(PageDimItem item){
		add(item);		
	}

	/***************************************************************************
	 * 删除所有的页纬度字段
	 *  
	 */
	public void removeAllPageDim() {
		removeAll();	
		pageDimList.clear();
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt == null) {
			return;
		}
		if(evt.getPropertyName().equals(AnaPropertyType.REPCONDITION_CHANGED)){//分析报表整表条件变化
			Object oldObj=evt.getOldValue();
			Object newObj=evt.getNewValue();
			if(oldObj==null&&newObj!=null){
				try {
					PageDimField field = (PageDimField) evt.getNewValue();
					addPageDim(field);
					this.firePropertyChange(AdhocPublic.PAGE_DIM_CHANGED, oldObj, newObj);
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
			if(oldObj!=null&&newObj==null){
				try {
					PageDimField field = (PageDimField) evt.getOldValue();
					removePageDim(field);
					this.firePropertyChange(AdhocPublic.PAGE_DIM_CHANGED, oldObj, newObj);
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
		}
		if(evt.getPropertyName().equals(AdhocPublic.PAGE_DIM_CHANGED)){
			this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		}
		if (evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_POS_CHANGED)) {
			sort();
			refresh();
		}
		if(evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_LOCK_STATUS_CHANGED)){
			refresh();
		}
		if (evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_ADD)) {
			try {
				PageDimField field = (PageDimField) evt.getNewValue();
				addPageDim(field);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
		if (evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_REMOVE)) {
			try {
				PageDimField field = (PageDimField) evt.getOldValue();
				removePageDim(field);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		if (evt.getPropertyName().equals(AdhocPublic.PAGE_DIMENSION_REMOVE_ALL)) {
			try {
				removeAllPageDim();
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
		if (evt.getPropertyName().equals(IMultiDimConst.PROPERTY_SELDIM_CHANGED)) {
			try {
				removeAllPageDim();
				SelDimModel selMode = (SelDimModel) evt.getNewValue();
				PageDimField[] fields = selMode.getPageDimfield();
				if (fields != null && fields.length > 0) {
					for (int i = 0; i < fields.length; i++) {
						addPageDim(fields[i]);
					}
				}

			} catch (Exception e) {
				AppDebug.debug(e);
			}

		}
	}

	
} //  @jve:decl-index=0:visual-constraint="10,10"
 