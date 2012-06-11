package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.ui.bi.query.manager.RptProvider;
import nc.ui.pub.beans.UIPopupMenu;
import nc.vo.bi.base.util.StringUtil;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import com.ufida.dataset.descriptor.FilterDescriptor;
import com.ufida.dataset.descriptor.FilterItem;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportCondition;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.iufo.resource.StringResource;

public class AnaExAreaFilterSetExt extends AbsActionExt {
	public static final int FILTER_TYPE_MANAGER=0;//筛选管理
    public static final int FILTER_TYPE_SHORTCUT=1;//单值筛选
    public static final int FILTER_TYPE_ENUM=2;//扩展区域筛选...
    public static final int DATASETFILTER_TYPE_ENUM=3;//报表数据集筛选
    
	private AnaReportPlugin m_plugin = null;
	private ExAreaModel exAreamodel=null;
	private int m_filterType=0;
	public AnaExAreaFilterSetExt(AnaReportPlugin plugin,int filterType){
		this.m_plugin=plugin;
		this.m_filterType=filterType;
	}
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		// 根据鼠标所在的位置和数据态和格式态的不同,所出的对话框也不同
		// 没有选中任何字段
		if (this.m_filterType == FILTER_TYPE_MANAGER) {
			AnaExAreaFilterSetDlg filterDlg = new AnaExAreaFilterSetDlg(
					container, m_plugin);
			filterDlg.show();
		} else if (this.m_filterType == FILTER_TYPE_SHORTCUT
				|| this.m_filterType == FILTER_TYPE_ENUM) {
			CellPosition[] selCells = m_plugin.getModel().getCellsModel()
					.getSelectModel().getSelectedCells();
			if (selCells != null && selCells.length > 0) {
				Cell dataCell = null;
				Cell fmtCell = m_plugin.getModel().getFormatCell(
						m_plugin.getModel().getCellsModel(), selCells[0]);
				if (!m_plugin.getModel().isFormatState()) {
					dataCell = m_plugin.getModel().getCellsModel().getCell(
							selCells[0]);
				}
				if (fmtCell != null
						&& fmtCell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null) {
					AnaRepField anaField = (AnaRepField) fmtCell
							.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
					if (!(anaField.getField() instanceof FieldCountDef)) {
						// 如果选中数据集的一个字段,格式态下出checkBoxList之类的,数据态下直接设置
						Field fld = anaField.getField();
						ExAreaCell selectedArea = getSelectedEx(fmtCell
								.getRow(), fmtCell.getCol());
						if (selectedArea != null
								&& selectedArea.getModel() instanceof AreaDataModel) {
							AreaDataModel areaData = (AreaDataModel) selectedArea
									.getModel();
							FilterDescriptor filter = null;
							if (areaData.getAreaFilter() == null) {
								filter = new FilterDescriptor();
							} else {
								filter = areaData.getAreaFilter();
							}
							if (dataCell != null
									&& this.m_filterType == FILTER_TYPE_SHORTCUT) {// 数据态
								FilterItem item = filter.getFilterItem(fld
										.getFldname());
								if (item!=null&&item.getValueType() == FilterItem.TYPE_CONST
										&& FilterItem.Filter_Operation[0]
												.equals(item.getOperation())) {
									filter.removeFilter(item);
								}else{
									item = new FilterItem();
									item.setOperation(FilterItem.Filter_Operation[0]);
									item.setFieldInfo(fld);
									item.setValue(dataCell.getValue() == null ? ""
													: dataCell.getValue()
															.toString());
									filter.addFilter(item);
								} 
								
								areaData.setAreaFilter(filter);
								m_plugin.setDirty(true);
								m_plugin.refreshDataModel(true);
							} else if (this.m_filterType == FILTER_TYPE_ENUM) {// 格式态
								AnaReportCondition condition = new AnaReportCondition(
										areaData.getDSTool(), fld);
								condition.setAreaData(areaData);
								FilterItem item = filter.getFilterItem(fld
										.getFldname());
								ArrayList<String> oldSelecteds = new ArrayList<String>();
								if (item != null) {
									if (item.getValue() != null
											&& item.getValue().length() > 0) {
										String[] temps = item.getValue().split(
												",");
										for (int i = 0; i < temps.length; i++) {
											oldSelecteds.add(temps[i]);
										}
									}
								}
								AnaExAreaFilterShortcutDlg shortcutDlg = new AnaExAreaFilterShortcutDlg(
										container, condition, oldSelecteds);
								shortcutDlg.show();
								if (shortcutDlg.getResult() == UfoDialog.ID_OK) {
									ArrayList<String> selecteds = shortcutDlg
											.getSelectValues();
									if (selecteds.size() > 0) {
										if (selecteds.size() > 1) {

											if (item == null) {
												item = new FilterItem();
											} else {
												filter.removeFilter(item);
											}
											item
													.setOperation(FilterItem.Filter_Operation[7]);
											item.setFieldInfo(fld);
											item.setValue(StringUtil
													.convert2String(selecteds));
											filter.addFilter(item);
										} else {
											if (item == null) {
												item = new FilterItem();
											} else {
												filter.removeFilter(item);
											}
											item
													.setOperation(FilterItem.Filter_Operation[0]);
											item.setFieldInfo(fld);
											item.setValue(StringUtil
													.convert2String(selecteds));
											filter.addFilter(item);
										}
										areaData.setAreaFilter(filter);
										m_plugin.setDirty(true);
										m_plugin.refreshDataModel(true);
									} else {
										if (item != null) {
											filter.removeFilter(item);
											areaData.setAreaFilter(filter);
											m_plugin.setDirty(true);
											m_plugin.refreshDataModel(true);
										}
									}
								}
							}
						}

						return null;
					}
				}
			}

		} else if (this.m_filterType == DATASETFILTER_TYPE_ENUM) {
			DefaultMutableTreeNode selNode = getSelectedNode();
			if (selNode != null && selNode.getUserObject() instanceof Field) {
				Field fld = (Field) selNode.getUserObject();
				DataSetDefVO dsVO = (DataSetDefVO) ((DefaultMutableTreeNode) selNode
						.getParent()).getUserObject();
				AnaDataSetTool dsTool = m_plugin.getModel().getDataSetTool(
						dsVO.getPk_datasetdef());
				FilterDescriptor filter = m_plugin.getModel().getDataSource()
						.getReportFilter(dsVO.getPk_datasetdef());
				if (filter == null) {
					filter = new FilterDescriptor();
				}
				AnaReportCondition condition = new AnaReportCondition(dsTool,
						fld);
				FilterItem item = filter.getFilterItem(fld.getFldname());
				ArrayList<String> oldSelecteds = new ArrayList<String>();
				if (item != null) {
					if (item.getValue() != null && item.getValue().length() > 0) {
						String[] temps = item.getValue().split(",");
						for (int i = 0; i < temps.length; i++) {
							oldSelecteds.add(temps[i]);
						}
					}
				}
				AnaExAreaFilterShortcutDlg shortcutDlg = new AnaExAreaFilterShortcutDlg(
						container, condition, oldSelecteds);
				shortcutDlg.show();
				if (shortcutDlg.getResult() == UfoDialog.ID_OK) {
					ArrayList<String> selecteds = shortcutDlg.getSelectValues();
					if (selecteds.size() > 0) {
						if (selecteds.size() > 1) {

							if (item == null) {
								item = new FilterItem();
							} else {
								filter.removeFilter(item);
							}
							item.setOperation(FilterItem.Filter_Operation[7]);
							item.setFieldInfo(fld);
							item.setValue(StringUtil.convert2String(selecteds));
							filter.addFilter(item);
						} else {
							if (item == null) {
								item = new FilterItem();
							} else {
								filter.removeFilter(item);
							}
							item.setOperation(FilterItem.Filter_Operation[0]);
							item.setFieldInfo(fld);
							item.setValue(StringUtil.convert2String(selecteds));
							filter.addFilter(item);
						}
						m_plugin.getModel().getDataSource().setReportFilter(
								dsVO.getPk_datasetdef(), filter);
						m_plugin.setDirty(true);
						m_plugin.refreshDataModel(true);
					} else {
						if (item != null) {
							filter.removeFilter(item);
							m_plugin.getModel().getDataSource()
									.setReportFilter(dsVO.getPk_datasetdef(),
											filter);
							m_plugin.setDirty(true);
							m_plugin.refreshDataModel(true);
						}
					}
				}

			}
			if (selNode != null && selNode.getUserObject() instanceof DataSetDefVO) {
				AnaDataSetFilterSetDlg dlg=new AnaDataSetFilterSetDlg(container,m_plugin,(DataSetDefVO)selNode.getUserObject());
				dlg.show();
			}
		}

		return null;
	}
	
	/**
	 * @i18n miufo00313=数据集筛选条件设置
	 * @i18n iufobi00015=单值筛选
	 * @i18n iufobi00016=筛选...
	 * @i18n miufo1001594=筛选
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes[] menues=null;
		if (this.m_filterType == FILTER_TYPE_MANAGER) {
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("miufo00313"));
			mainMenu.setPaths(new String[] { MultiLang.getString("data") });
			ActionUIDes uiDes1 = (ActionUIDes) mainMenu.clone();
			uiDes1.setPopup(true);
			uiDes1.setPaths(new String[] {});
			uiDes1.setGroup(StringResource.getStringResource("miufo00178"));
			ActionUIDes uiDes2 = new ActionUIDes();
			uiDes2.setToolBar(true);
			uiDes2.setGroup(StringResource.getStringResource("miufo00178"));
			uiDes2.setPaths(new String[] {});
			uiDes2.setName(StringResource.getStringResource("miufo00313"));
			uiDes2.setTooltip(StringResource.getStringResource("miufo00313"));
			uiDes2.setImageFile("reportcore/FilterManager.gif");
			menues = new ActionUIDes[] { mainMenu, uiDes1,uiDes2 };
		}else if(this.m_filterType==FILTER_TYPE_SHORTCUT){
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("iufobi00015"));
			mainMenu.setPaths(new String[] { MultiLang.getString("data")});
			mainMenu.setPopup(true);
			mainMenu.setPaths(new String[]{});
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setToolBar(true);
			uiDes1.setGroup(StringResource.getStringResource("miufo00178"));
			uiDes1.setPaths(new String[] {});
			uiDes1.setName(StringResource.getStringResource("iufobi00015"));
			uiDes1.setTooltip(StringResource.getStringResource("iufobi00015"));
			uiDes1.setImageFile("reportcore/FilterOne.gif");
			menues = new ActionUIDes[] { mainMenu, uiDes1 };
		}else if(this.m_filterType==FILTER_TYPE_ENUM){
			ActionUIDes mainMenu = new ActionUIDes();
			mainMenu.setName(StringResource.getStringResource("iufobi00016"));
			mainMenu.setPaths(new String[] { MultiLang.getString("data")});
			mainMenu.setPopup(true);
			mainMenu.setPaths(new String[]{});
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setToolBar(true);
			uiDes1.setGroup(StringResource.getStringResource("miufo00178"));
			uiDes1.setPaths(new String[] {});
			uiDes1.setName(StringResource.getStringResource("iufobi00016"));
			uiDes1.setTooltip(StringResource.getStringResource("iufobi00016"));
			uiDes1.setImageFile("reportcore/FilterEnum.gif");
			menues = new ActionUIDes[] { mainMenu, uiDes1 };
		}else if(this.m_filterType==DATASETFILTER_TYPE_ENUM){
			JPopupMenu datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(DataSetDefVO.class.getName());
			if(datasetPopMenu==null){
				datasetPopMenu=new UIPopupMenu();
			}
			JPopupMenu fieldPopMenu=m_plugin.getQueryPanel().getPopUpMenu(Field.class.getName());
			if(fieldPopMenu==null){
				fieldPopMenu=new UIPopupMenu();
			}
			AbstractAction filterMenu=new AbstractAction(StringResource.getStringResource("miufo1001594")){

				public void actionPerformed(ActionEvent e) {
					getParams(m_plugin.getReport());
				}
				
			};
			datasetPopMenu.add(filterMenu);
			fieldPopMenu.add(new AbstractAction(StringResource.getStringResource("iufobi00016")){

				public void actionPerformed(ActionEvent e) {
					getParams(m_plugin.getReport());
				}
				
			});
			m_plugin.getQueryPanel().initTreeRightMouseListener(DataSetDefVO.class.getName(), datasetPopMenu);
			m_plugin.getQueryPanel().initTreeRightMouseListener(Field.class.getName(), fieldPopMenu);
			datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(RptProvider.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
            datasetPopMenu.add(filterMenu);
            m_plugin.getQueryPanel().initTreeRightMouseListener(RptProvider.class.getName(),datasetPopMenu);
//			ActionUIDes mainMenu = new ActionUIDes();
//			mainMenu.setName("筛选...");
//			mainMenu.setPaths(new String[] { StringResource.getStringResource("miufo00241") });
//			menues = new ActionUIDes[] { mainMenu,};
		}
		
		return menues;
	}

	/**
	 * @i18n iufobi00017=取消筛选
	 * @i18n iufobi00015=单值筛选
	 */
	@Override
	public boolean isEnabled(Component focusComp) {
		boolean hasDataSet=false;
		if (this.m_filterType == FILTER_TYPE_MANAGER) {
			AreaPosition[] selCells = m_plugin.getFormatSelected();
			ExAreaCell[] exCells = getExAreaCells();
			if (exCells != null && exCells.length > 0) {
				AreaDataModel areaData = null;
				AreaPosition area = null;
				for (int i = 0; i < exCells.length; i++) {
					area = exCells[i].getArea();
					if (exCells[i].getModel() instanceof AreaDataModel) {
						areaData = (AreaDataModel) exCells[i].getModel();
						if (areaData.getDSTool() != null
								&& areaData.getDSTool().getDSDef() != null) {							
								hasDataSet = true;
						}
					}
				}
			}
		} else if (this.m_filterType == FILTER_TYPE_SHORTCUT
				|| this.m_filterType == FILTER_TYPE_ENUM) {
			CellPosition[] selCells = m_plugin.getModel().getCellsModel()
					.getSelectModel().getSelectedCells();
			if (selCells != null && selCells.length > 0) {
				Cell dataCell = null;
				Cell fmtCell = m_plugin.getModel().getFormatCell(
						m_plugin.getModel().getCellsModel(), selCells[0]);
				if (!m_plugin.getModel().isFormatState()) {
					dataCell = m_plugin.getModel().getCellsModel().getCell(
							selCells[0]);
				}
				if (fmtCell != null
						&& fmtCell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null) {
					ExAreaCell selectedArea = getSelectedEx(fmtCell.getRow(),
							fmtCell.getCol());
					if (selectedArea != null
							&& selectedArea.getModel() instanceof AreaDataModel) {
						AreaDataModel areaData = (AreaDataModel) selectedArea
								.getModel();
						FilterDescriptor filter = null;
						if (areaData.getAreaFilter() != null) {
							filter = areaData.getAreaFilter();
						}
						if (areaData.getDSTool() != null
								&& areaData.getDSTool().getDSDef() != null
//								&& areaData
//										.getDSTool()
//										.getDSDef()
//										.supportDescriptorFunc(
//												DescriptorType.FilterDescriptor)
												) {
							AnaRepField anaField = (AnaRepField) fmtCell
									.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
							if (!(anaField.getField() instanceof FieldCountDef)) {
								if (this.m_filterType == FILTER_TYPE_SHORTCUT
										&& dataCell != null) {
									hasDataSet = true;
									FilterItem item=null;
									if(filter!=null){
										item=filter.getFilterItem(anaField.getFieldID());
									}
									if (focusComp instanceof JButton) {
										if (item != null
												&& item.getValueType() == FilterItem.TYPE_CONST
												&& FilterItem.Filter_Operation[0]
														.equals(item
																.getOperation())) {
											((JButton) focusComp)
													.setToolTipText(StringResource
															.getStringResource("iufobi00017"));
										} else {
											((JButton) focusComp)
											.setToolTipText(StringResource
													.getStringResource("iufobi00015"));
										}
									}
								} else if (this.m_filterType == FILTER_TYPE_ENUM
										&& dataCell == null) {
									hasDataSet = true;
								}
							}
						}
					}
				}
			}
		} else if (this.m_filterType == DATASETFILTER_TYPE_ENUM) {
			DefaultMutableTreeNode selNode = getSelectedNode();
			if (selNode != null && selNode.getUserObject() instanceof Field) {
				DataSetDefVO dsVO = (DataSetDefVO) ((DefaultMutableTreeNode) selNode
						.getParent()).getUserObject();
				if (dsVO != null
				// &&dsVO.getDataSetDef().supportDescriptorFunc(DescriptorType.FilterDescriptor)
				) {
					if (dsVO.getDataSetDef().getProvider() instanceof RptProvider)
						hasDataSet = true;
				}

			}
		}
		return hasDataSet;
		
	}
	
	@Override
	public void initListenerByComp(final Component stateChangeComp) {
		if (this.m_filterType == FILTER_TYPE_SHORTCUT) {
			m_plugin.getModel().getFormatModel().getSelectModel().addSelectModelListener(
					new SelectListener() {
						/**
						 * @i18n iufobi00017=取消筛选
						 * @i18n iufobi00015=单值筛选
						 */
						public void selectedChanged(SelectEvent e) {
							if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
			CellPosition[] selCells = m_plugin.getModel().getCellsModel()
					.getSelectModel().getSelectedCells();
			if (selCells != null && selCells.length > 0) {
				Cell dataCell = null;
				Cell fmtCell = m_plugin.getModel().getFormatCell(
						m_plugin.getModel().getCellsModel(), selCells[0]);
				if (!m_plugin.getModel().isFormatState()) {
					dataCell = m_plugin.getModel().getCellsModel().getCell(
							selCells[0]);
				}
				if (fmtCell != null
						&& fmtCell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null) {
					ExAreaCell selectedArea = getSelectedEx(fmtCell.getRow(),
							fmtCell.getCol());
					if (selectedArea != null
							&& selectedArea.getModel() instanceof AreaDataModel) {
						AreaDataModel areaData = (AreaDataModel) selectedArea
								.getModel();
						FilterDescriptor filter = null;
						if (areaData.getAreaFilter() != null) {
							filter = areaData.getAreaFilter();
						}
						if (areaData.getDSTool() != null
								&& areaData.getDSTool().getDSDef() != null) {
							AnaRepField anaField = (AnaRepField) fmtCell
									.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
							if (!(anaField.getField() instanceof FieldCountDef)) {
								if (dataCell != null) {
									FilterItem item = null;
									if (filter != null) {
										item = filter.getFilterItem(anaField
												.getFieldID());
									}
									if (item != null
											&& item.getValueType() == FilterItem.TYPE_CONST
											&& FilterItem.Filter_Operation[0]
													.equals(item.getOperation())) {
										if(stateChangeComp  instanceof JButton){
											((JButton) stateChangeComp)
											.setToolTipText(StringResource
													.getStringResource("iufobi00017"));
										}else if(stateChangeComp instanceof JMenuItem){
											((JMenuItem)stateChangeComp).setText(StringResource
													.getStringResource("iufobi00017"));
										}
										
									} else {
										if(stateChangeComp  instanceof JButton){
											((JButton) stateChangeComp)
											.setToolTipText(StringResource
													.getStringResource("iufobi00015"));
										}else if(stateChangeComp instanceof JMenuItem){
											((JMenuItem)stateChangeComp).setText(StringResource
													.getStringResource("iufobi00015"));
										}
										
									}
								}
							}
						}
					}
				}
			}
							}
		}
					});
		
		}
	}
	
	private DefaultMutableTreeNode getSelectedNode() {
		TreePath selPath = m_plugin.getQueryPanel().getQueryModelTree().getSelectionPath();
		if (selPath == null)
			return null;

		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();

		return selNode;
	}
	
	public ExAreaModel getExAreaModel() {
		if (exAreamodel == null) {
			exAreamodel = ExAreaModel.getInstance(m_plugin.getModel().getFormatModel());
		}
		return exAreamodel;
	}
	private ExAreaCell[] getExAreaCells(){
		return getExAreaModel().getExAreaCells();
	}
	
	private ExAreaCell getSelectedEx(int row, int col) {
		ExAreaCell cell = null;
		AreaPosition selCell = AreaPosition.getInstance(row, col, 1, 1);
		ExAreaCell[] cells = getExAreaCells();
		for (int i = 0; i < cells.length; i++) {
			AreaPosition area = cells[i].getArea();
			if (selCell.intersection(area)) {
				cell = cells[i];
				break;
			}
		}

		return cell;
	}
	
    
	
}
  