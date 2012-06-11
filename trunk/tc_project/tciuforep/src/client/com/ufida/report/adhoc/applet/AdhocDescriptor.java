package com.ufida.report.adhoc.applet;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.ui.bi.query.freequery.DataSetPreviewDlg;
import nc.ui.pub.beans.UIFileChooser;
import nc.us.bi.report.manager.BIReportSrv;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.freequery.BIMultiDataSet;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.measure.MeasureVO;

import com.ufida.bi.base.BIException;
import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.AdhocArea;
import com.ufida.report.adhoc.model.AdhocDataCenter;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufida.report.adhoc.model.FilterRowAnalyzer;
import com.ufida.report.adhoc.model.FilterValueAnalyzer;
import com.ufida.report.adhoc.model.IAdhocAnalyzer;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.adhoc.model.SortAnalyzer;
import com.ufida.report.anareport.model.ReportDrillParams;
import com.ufida.report.free.IMeasQueryCreator;
import com.ufida.report.free.applet.FreeQueryDesignePlugin;
import com.ufida.report.free.applet.FreeQueryUtil;
import com.ufida.report.rep.applet.BINavigationExt;
import com.ufida.report.rep.applet.BINavigationPanel;
import com.ufida.report.rep.applet.BIReportPreViewExt;
import com.ufida.report.rep.applet.BIReportSaveExt;
import com.ufida.report.rep.applet.FilterRowDlg;
import com.ufida.report.rep.applet.FilterValueDlg;
import com.ufida.report.rep.applet.PageDimNavigationDropTarget;
import com.ufida.report.rep.applet.PageDimNavigationPanel;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BIReportXMLUtil;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufida.report.rep.model.FilterRowDescriptor;
import com.ufida.report.rep.model.FilterValueDescriptor;
import com.ufida.report.rep.model.IBIField;
import com.ufida.report.rep.model.IColumnData;
import com.ufida.report.rep.model.SortDescriptor;
import com.ufida.report.rep.model.SortVO;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrCmd;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrExt;
import com.ufsoft.report.sysplugin.cellattr.SetConditionAttrExt;
import com.ufsoft.report.sysplugin.combinecell.CombineCellExt;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.report.sysplugin.insertdelete.InsertColumnsCmd;
import com.ufsoft.report.sysplugin.insertdelete.InsertColumnsExt;
import com.ufsoft.report.sysplugin.insertdelete.InsertRowsExt;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableDataModelException;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.header.Header;

/**
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * 
 * @author caijie
 */
public class AdhocDescriptor extends AbstractPlugDes implements IUfoContextKey {
	public class AdhocXMLImportExt extends AbsActionExt {

		/**
		 * @i18n report00112=导入XML
		 * @i18n data=数据
		 * @i18n import=导入
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes = new ActionUIDes();
			uiDes.setName(MultiLang.getString("report00112"));
			uiDes.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00004") });
			return new ActionUIDes[] { uiDes };
		}

		public UfoCommand getCommand() {
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			// return m_adhocPlugin.getOperationState() ==
			// UfoReport.OPERATION_FORMAT;
			return m_adhocPlugin.isFormat();

		}

		public Object[] getParams(UfoReport container) {
			JFileChooser chooser = new UIFileChooser();
			ExtNameFileFilter xf = new ExtNameFileFilter("xml");
			chooser.setFileFilter(xf);
			chooser.setMultiSelectionEnabled(false);
			int returnVal = chooser.showOpenDialog(m_adhocPlugin.getReport());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.exists()) {
					UfoPublic.showErrorDialog(m_adhocPlugin.getReport(), MultiLang.getString("file_not_exist"),
							MultiLang.getString("error"));
					return null;
				}

				AdhocModel model = null;
				try {
					ReportVO repVO = BIReportXMLUtil.parseAdhocReport(file.getAbsolutePath());
					model = (AdhocModel) repVO.getDefinition();
					model.setPK(repVO.getID());
				} catch (Exception e) {
					AppDebug.debug(e);
					JOptionPane.showMessageDialog(m_adhocPlugin.getReport(), e.getMessage(), "",
							JOptionPane.ERROR_MESSAGE);
				}
				AdhocPlugin.drillThrough(m_adhocPlugin.getReport(), model, UfoReport.OPERATION_FORMAT, null);
			}
			return null;
		}

	}

	public class AdhocXMLExportExt extends AbsActionExt {
		/**
		 * @i18n report00114=导出XML
		 * @i18n data=数据
		 * @i18n export=导出
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes = new ActionUIDes();
			uiDes.setName(MultiLang.getString("report00114"));
			uiDes.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00004") });
			return new ActionUIDes[] { uiDes };
		}

		public boolean isEnabled(Component focusComp) {
			return m_adhocPlugin.isFormat();
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
			JFileChooser chooser = new UIFileChooser();
			ExtNameFileFilter xf = new ExtNameFileFilter("xml");
			chooser.setFileFilter(xf);
			chooser.setMultiSelectionEnabled(false);
			int returnVal = chooser.showSaveDialog(m_adhocPlugin.getReport());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
				} catch (Exception e) {
					AppDebug.debug(e);
				}

				ReportVO reportVO = new ReportVO();
				reportVO.setType(ReportResource.INT_REPORT_ADHOC);
				reportVO.setDefinition(m_adhocPlugin.getModel());
				try {
					String strRepId = m_adhocPlugin.getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)m_adhocPlugin.getReport().getContextVo().getAttribute(REPORT_PK);
					
					ReportVO oriVO = BIReportSrv.getInstance().getByID(
							strRepId);
					reportVO.setID(oriVO.getID());
					reportVO.setReportname(oriVO.getReportname());
					reportVO.setReportcode(oriVO.getReportcode());
				} catch (Exception e1) {
					AppDebug.debug(e1);
				}

				File file = chooser.getSelectedFile();
				try {
					BIReportXMLUtil.exportReport(reportVO, file.getAbsolutePath());
				} catch (Exception e) {
					AppDebug.debug(e);
					JOptionPane.showMessageDialog(m_adhocPlugin.getReport(), e.getMessage(), "",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			return null;
		}
	}

	/**
	 * 联查报表
	 */
	public class TraceReportExt extends AbsActionExt {
		private UfoReport m_Report;

		public TraceReportExt(UfoReport rep) {
			super();
			m_Report = rep;
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
			CellPosition pos = m_adhocPlugin.getModel().getCellsModel().getSelectModel().getAnchorCell();
			ReportDrillParams params = (ReportDrillParams) m_adhocPlugin.getModel().getCellsModel().getBsFormat(pos,
					AdhocPublic.EXT_FMT_DRILL_PARAMS);
			if (params == null)
				return null;
			// JOptionPane.showMessageDialog(m_adhocPlugin.getReport(),
			// "当前单元格不能联查报表");

			AdhocQueryModel queryDef = m_adhocPlugin.getModel().getDataCenter().getCurrQuery();
			if (queryDef.getQueryModel() instanceof MeasureQueryModelDef) {
				// 穿透行的数据信息
				String[][] info = m_adhocPlugin.getModel().getDataCenter().getDrillThroughInfo(params);

				MeasureQueryModelDef query = (MeasureQueryModelDef) queryDef.getQueryModel();
				FreeQueryContextVO context = (FreeQueryContextVO) m_adhocPlugin.getReport().getContextVo();

				Object exObj = query.getExMetaInfo(params.getField());
				if (exObj == null)
					return null;
				if (context.getTaskID() == null) {
					JOptionPane.showMessageDialog(m_adhocPlugin.getReport(), StringResource.getStringResource("uiufotask00043"));//uiufotask00043=请选择当前任务
					return null;
				}
				String strAloneID = null;
				String[] keyValues = new String[20];
				int keycount = -1;
				for (int i = 0; i < info.length; i++) {// 过滤出需要传递的关键字值
					String fldKey = info[i][0];
					if (fldKey.equals("alone_id")) {
						strAloneID = info[i][1];
					} else if (fldKey.equals("unit_id")) {
						keyValues[0] = info[i][1];
						keycount = Math.max(0, keycount);
					} else if (fldKey.startsWith(IMeasQueryCreator.PERFIX_KEYALIAS)) {
						int keyIndex = Integer.parseInt(fldKey.substring(fldKey.lastIndexOf("_") + 1));
						if (keyIndex > 0 && keyIndex < 20) {
							keyValues[keyIndex] = info[i][1];
							keycount = Math.max(keyIndex, keycount);
						}
					}
					}
				String[] keyInfos = new String[keycount + 1];// 关键字值排序
				System.arraycopy(keyValues, 0, keyInfos, 0, keycount + 1);

				MeasureVO measVO = (MeasureVO) exObj;
				// if (strAloneID != null) {
				if (measVO.getSelReportPK() == null)
					measVO.setSelReportPK(measVO.getReportPK());

				// StringBuffer sbf = new StringBuffer();
				// sbf.append("穿透信息：\r\nrowIndex:");
				// sbf.append(params.getRowIndex());
				// for (String[] msg : info) {
				// sbf.append(msg[0] + ":" + msg[1] + "\r\n");
				// }
				// AppDebug.debug(sbf.toString());

				MeasTraceInfo measTraceInfo = new MeasTraceInfo(measVO.getCode(), keyInfos, measVO.getSelReportPK(),
						strAloneID, false);
				measTraceInfo.setStrOperUserPK(context.getCurUserID());
				measTraceInfo.setStrOrgPK(context.getOrgID());
				measTraceInfo.setStrLoginUnitID(context.getCurUnitID());
				measTraceInfo.setStrLangCode(context.getLang());
				measTraceInfo.setStrTaskId(context.getTaskID());
				measTraceInfo.setDataSource(((DataSourceInfo)context.getAttribute(FreeQueryContextVO.DATA_SOURCEINFO)));
				WindowNavUtil.traceMeasure(measTraceInfo);
				// }

			}

			return null;
		}

		public UfoReport getReport() {
			return m_Report;
		}

		public boolean isEnabled(Component focusComp) {
			if (m_adhocPlugin.getModel().isFormatState())
				return false;
			if(m_adhocPlugin.getModel().getDataCenter().isCross())
				return false;
			CellPosition[] cells = m_adhocPlugin.getModel().getCellsModel().getSelectModel().getSelectedCells();
			if (cells == null || cells.length == 0 || cells.length > 1)
				return false;
			ReportDrillParams params = (ReportDrillParams) m_adhocPlugin.getModel().getCellsModel().getBsFormat(cells[0],
					AdhocPublic.EXT_FMT_DRILL_PARAMS);
			if (params == null)
				return false;
			return true;
		}

		/**
		 * @i18n ubimultidim0052=穿透
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("uifreequery0006"));
			uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] {});
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}
	}

	/**
	 * 数据源，实现指标选择功能
	 */
	public class DataSourceDesignExt extends AbsActionExt {
		public DataSourceDesignExt() {
			super();
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
			doDataSourceDesign(container);
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			return true;
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufopublic128"));// 数据源
			uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] {});
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}
	}

	/** 设计向导* */
	private class DesignExt extends AbsActionExt {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00010"));
			uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] {});
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			return m_adhocPlugin.isFormat();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			FreeQueryContextVO context = (FreeQueryContextVO) container.getContextVo();
			String userID = context.getCurUserID();
			AdhocQueryModel selQuery = getSelectedQuery();
			if (selQuery == null)
				selQuery = m_adhocPlugin.getModel().getDataCenter().getCurrQuery();
			boolean bAddQuery = false;
			if (selQuery == null) {
				doDataSourceDesign(container);
				// // 弹出查询选择界面
				// int queryIndex = m_adhocPlugin.doSelectQueryModel(userID);
				// if (queryIndex >= 0) {// 新增查询
				// selQuery =
				// m_adhocPlugin.getModel().getDataCenter().getCurrQuery();
				// bAddQuery = true;
				// }
			}
			if (selQuery == null)
				return null;

			IBIField[] flds = selQuery.getMetaFields(true);
			if (bAddQuery) {
				biNavigationPanel.addFieldNode(selQuery, flds);
			}

			AdhocDesignDlg dlg = new AdhocDesignDlg(m_adhocPlugin.getReport(), StringResource
					.getStringResource("mbiadhoc00024"), m_adhocPlugin.getModel(), flds);
			dlg.setLocationRelativeTo(m_adhocPlugin.getReport());
			// dlg.setResizable(false);
			dlg.show();

			if (dlg.getResult() == UfoDialog.ID_OK) {
				boolean bOldFormat = m_adhocPlugin.getModel().isFormatState();
				m_adhocPlugin.getModel().changeState(true, false);
				boolean bNeesQuery = true;
				boolean isCross = dlg.isCrossTable();
				updateReportField(dlg.getSelectedFields(IBIField.BI_REPORT_FIELD), false);
				m_adhocPlugin.getModel().getDataCenter().setIsCross(isCross);
				if (isCross)
					m_adhocPlugin.getModel().getDataCenter().setCrossTableInfo(dlg.getCrossTableSet());

				updateGroup(dlg.getSelectedFields(IBIField.ADHOC_GROUP_FIELD));
				updateSortVO(dlg.getSortDescriptors());
				updatePageDims(dlg.getSelectedFields(IBIField.BI__PAGE_DIM_FIELD));
				m_adhocPlugin.getModel().changeState(bOldFormat, bNeesQuery);

			}
			return null;
		}
	}

	private boolean updatePageDims(IMetaData[] newfields) {
		IMetaData[] oldFlds = m_adhocPlugin.getModel().getFields(IBIField.BI__PAGE_DIM_FIELD);
		ArrayList<IMetaData> removeFldList = new ArrayList<IMetaData>();
		ArrayList<IMetaData> addFldList = new ArrayList<IMetaData>();
		parseFileds(oldFlds, newfields, addFldList, removeFldList, new ArrayList<IMetaData>());
		if (newfields != null) {
			for (int i = 0; i < newfields.length; i++)
				((PageDimField) newfields[i]).setPos(i);
		}

		// 删除多余的页唯度
		for (int i = 0; i < removeFldList.size(); i++) {
			m_adhocPlugin.getModel().removePageDim((PageDimField) removeFldList.get(i));
		}
		// 添加新的页唯度
		for (int i = 0; i < addFldList.size(); i++) {
			try {
				m_adhocPlugin.getModel().addPageDim((PageDimField) addFldList.get(i));
			} catch (BIException e1) {
				JOptionPane
						.showMessageDialog(m_adhocPlugin.getReport(), e1.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
			}
		}
		return true;
		// 没有增加删除维度的时候
		// m_adhocPlugin.getModel().firePageDimPosChanged();
	}

	private boolean updateSortVO(SortDescriptor[] fields) {
		SortAnalyzer sa = null;
		SortVO sortVO = null;
		AdhocDataCenter dataCenter = m_adhocPlugin.getModel().getDataCenter();
		IAdhocAnalyzer[] anas = dataCenter.getAnalyzerModel().getAnalyzerByType(IAdhocAnalyzer.TYPE_SORT);
		if (anas.length > 0) {
			sa = (SortAnalyzer) anas[0];
			sortVO = sa.getSortVO();
		} else {
			sa = new SortAnalyzer();
			sortVO = new SortVO();
			sa.setSortVO(sortVO);
		}
		sortVO.getSortDescriptorList().clear();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				sortVO.getSortDescriptorList().add(fields[i]);
			}
		}
		if (anas.length == 0) {
			dataCenter.getAnalyzerModel().addAnalyzer(null, sa);
		} else {
			dataCenter.setBProcessAnaEvent(false);
			dataCenter.getAnalyzerModel().removeAnalyzer(sa.getID());
			dataCenter.setBProcessAnaEvent(true);
			dataCenter.getAnalyzerModel().addAnalyzer(null, sa);
		}
		return true;
	}

	public boolean updateReportField(IMetaData[] fields, boolean removeOldFields) {
		Hashtable<?, ?> list = m_adhocPlugin.getCellsModel().getBsFormats(AdhocPublic.EXT_FMT_SELECTED_FIELD);
		IMetaData[] existFlds = (IMetaData[]) list.values().toArray(new IMetaData[0]);
		ArrayList<IMetaData> removedFldList = new ArrayList<IMetaData>();
		ArrayList<IMetaData> addFldList = new ArrayList<IMetaData>();
		parseFileds(existFlds, fields, addFldList, removedFldList, new ArrayList<IMetaData>());

		// 删除旧字段
		CellPosition pos = null;
		Enumeration eum = list.keys();
		while (eum.hasMoreElements()) {
			pos = (CellPosition) eum.nextElement();
			IMetaData fld = (IMetaData) list.get(pos);
			if (removeOldFields || removedFldList.contains(fld))
				m_adhocPlugin.getModel().removeQueryField(pos);
		}

		CellPosition[] existFldPoss = (CellPosition[]) list.keySet().toArray(new CellPosition[0]);
		CellPosition targetPos = CellPosition.getInstance(1, 0);
		for (int i = 0; i < existFldPoss.length; i++) {
			if (targetPos.getColumn() <= existFldPoss[i].getColumn()) {
				targetPos = CellPosition.getInstance(existFldPoss[i].getRow(), existFldPoss[i].getColumn() + 1);
			}
		}

		// 添加新字段
		for (int i = 0; i < addFldList.size(); i++) {
			try {
				DefaultReportField fld = null;
				if (addFldList.get(i) instanceof DefaultReportField)
					fld = (DefaultReportField) addFldList.get(i);
				else
					fld = new DefaultReportField(addFldList.get(i));
				m_adhocPlugin.getModel().addQueryField(fld, targetPos);
				targetPos = CellPosition.getInstance(targetPos.getRow(), targetPos.getColumn() + 1);
			} catch (BIException e1) {
				JOptionPane
						.showMessageDialog(m_adhocPlugin.getReport(), e1.getMessage(), "", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
			}
		}
		return true;
	}

	private boolean updateGroup(IMetaData[] fields) {
		IMetaData[] oldFlds = m_adhocPlugin.getModel().getFields(IBIField.ADHOC_GROUP_FIELD);
		ArrayList<IMetaData> removeFldList = new ArrayList<IMetaData>();
		ArrayList<IMetaData> addFldList = new ArrayList<IMetaData>();
		ArrayList<IMetaData> remainList = new ArrayList<IMetaData>();
		parseFileds(oldFlds, fields, addFldList, removeFldList, remainList);

		// 删除多余的分组
		for (int i = 0; i < removeFldList.size(); i++) {
			m_adhocPlugin.getModel().removeGroup((IMetaData) removeFldList.get(i));
		}

		for (int i = 0; i < fields.length; i++) {
			if (addFldList.contains(fields[i])) {
				try {
					DefaultReportField fld = (DefaultReportField) fields[i];
					m_adhocPlugin.getModel().insertGroup(fld, i, 1, 1);
				} catch (Exception e) {
					AppDebug.debug(e);// @devTools AppDebug.debug(e);
				}
			} else {
				try {
					m_adhocPlugin.getModel().moveGroup((IMetaData) fields[i], i);
				} catch (Exception e) {
					AppDebug.debug(e);// @devTools AppDebug.debug(e);
				}
			}
		}
		return true;
	}

	/**
	 * 比较两个数组,对其中的元素进行分类
	 * 
	 * @param oldElements
	 *            原对象组
	 * @param newElements
	 *            新对象组
	 * @param addElements
	 *            新对象
	 * @param removedElements
	 *            已经删除的对象
	 * @param remainElements
	 *            保持不变的对象
	 */
	private static void parseFileds(IMetaData[] oldElements, IMetaData[] newElements,
			ArrayList<IMetaData> addElementList, ArrayList<IMetaData> removedElementList,
			ArrayList<IMetaData> remainElementList) {
		for (int i = 0; i < newElements.length; i++) {
			boolean findInOldElements = false;
			for (int j = 0; j < oldElements.length; j++) {
				if (newElements[i].equals(oldElements[j])) {
					findInOldElements = true;
					break;
				}
			}
			if (findInOldElements) {
				remainElementList.add(newElements[i]);
			} else {
				addElementList.add(newElements[i]);
			}
		}
		for (int i = 0; i < oldElements.length; i++) {
			boolean findInNewElements = false;
			for (int j = 0; j < newElements.length; j++) {
				if (oldElements[i].equals(newElements[j])) {
					findInNewElements = true;
					break;
				}
			}
			if (findInNewElements) {
			} else {
				removedElementList.add(oldElements[i]);
			}
		}
	}

	/** 筛选行* */
	private class FilterRowExt extends AbsActionExt {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		/**
		 * @i18n uibiadhoc00012=限制返回行数
		 * @i18n miufo1001594=筛选
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("uibiadhoc00012"));
			uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005"),
					StringResource.getStringResource("miufo1001594") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] { StringResource.getStringResource("miufo1001594") });
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			IAdhocAnalyzer[] anas = m_adhocPlugin.getModel().getDataCenter().getAnalyzerModel().getAnalyzerByType(
					IAdhocAnalyzer.TYPE_FILTER_ROW);
			FilterRowDescriptor filterRowDescriptor = null;
			if (anas.length > 0) {
				filterRowDescriptor = ((FilterRowAnalyzer) anas[0]).getFilterRowDescriptor();
			}
			if (filterRowDescriptor != null) {
				try {
					filterRowDescriptor = (FilterRowDescriptor) filterRowDescriptor.clone();
				} catch (CloneNotSupportedException e) {
					filterRowDescriptor = new FilterRowDescriptor();
				}
			}

			IColumnData[] fields = m_adhocPlugin.getModel().getFilterValueFileds();
			if (fields != null) {
				FilterRowDlg dlg = new FilterRowDlg(m_adhocPlugin.getReport(), filterRowDescriptor, fields);
				dlg.setLocationRelativeTo(m_adhocPlugin.getReport());
				dlg.show();
				if (dlg.getResult() == UfoDialog.ID_OK) {
					m_adhocPlugin.getModel().fitlerRow(dlg.getFilterRowDescriptor());
				}
			}
			return null;
		}
	}

	/** 筛选字段值* */
	private class FilterValueExt extends AbsActionExt {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		/**
		 * @i18n miufo1001594=筛选
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00014"));
			uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005"),
					StringResource.getStringResource("miufo1001594") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] { StringResource.getStringResource("miufo1001594") });
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			IAdhocAnalyzer[] anas = m_adhocPlugin.getModel().getDataCenter().getAnalyzerModel().getAnalyzerByType(
					IAdhocAnalyzer.TYPE_FILTER_VALUE);
			FilterValueDescriptor filterValueDescriptor = null;
			if (anas.length > 0) {
				filterValueDescriptor = ((FilterValueAnalyzer) anas[0]).getFilterValueDescriptor();
			}
			if (filterValueDescriptor != null) {
				try {
					filterValueDescriptor = (FilterValueDescriptor) filterValueDescriptor.clone();
				} catch (CloneNotSupportedException e) {
					filterValueDescriptor = new FilterValueDescriptor();
				}
			}
			IColumnData[] fields = m_adhocPlugin.getModel().getFilterValueFileds();
			if (fields != null) {
				FilterValueDlg dlg = new FilterValueDlg(m_adhocPlugin.getReport(),m_adhocPlugin.getModel(),filterValueDescriptor,fields);
				dlg.setLocationRelativeTo(m_adhocPlugin.getReport());
				dlg.show();
				if (dlg.getResult() == UfoDialog.ID_OK) {
					FilterValueDescriptor result = dlg.getFilterValueDescriptor();
					if (anas.length > 0) {
						((FilterValueAnalyzer) anas[0]).setFilterValueDescriptor(result);
					} else {
						FilterValueAnalyzer filterAna = new FilterValueAnalyzer();
						filterAna.setFilterValueDescriptor(result);
						m_adhocPlugin.getModel().getDataCenter().getAnalyzerModel().addAnalyzer(null, filterAna);
					}
				}
			}
			return null;
		}
	}

	/** Adhoc设置当前列为升序* */
	private class SortAscendingExt extends AbsActionExt {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00020"));
			uiDes1.setImageFile("reportcore/up.gif");
			uiDes1.setPopup(true);

			ActionUIDes uiDes2 = new ActionUIDes();
			uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] {});
			uiDes2.setToolBar(true);
			uiDes2.setTooltip(StringResource.getStringResource("mbiadhoc00020"));
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}

		public boolean isEnabled(Component focusComp) {
			if (m_adhocPlugin.getModel().isFormatState())
				return false;
			if(m_adhocPlugin.getModel().getDataCenter().isCross())
				return false;
			int[] cols = m_adhocPlugin.getModel().getCellsModel().getSelectModel().getSelectedCol();
			if (cols != null)
				return true;
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			int[] cols = m_adhocPlugin.getModel().getCellsModel().getSelectModel().getSelectedCol();
			if ((cols != null) && (cols.length > 0)) {
				int col = cols[0];
				IBIField field = m_adhocPlugin.getModel().getColFieldByCol(col);
				m_adhocPlugin.getModel().sortColumnInInputState(field, SortVO.SORT_ASCENDING);
				//modify by guogang 2008-4-21 强制设置选中行
				m_adhocPlugin.getModel().getCellsModel().getColumnHeaderModel().setSelectAllHeader(m_adhocPlugin.getModel().getCellsModel().getRowHeaderModel());
			}
			return null;
		}
	}

	/** Adhoc设置当前列为降序* */
	private class SortDescendingExt extends AbsActionExt {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00019"));
			uiDes1.setImageFile("reportcore/down.gif");
			uiDes1.setPopup(true);

			ActionUIDes uiDes2 = new ActionUIDes();
			uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] {});
			uiDes2.setToolBar(true);
			uiDes2.setTooltip(StringResource.getStringResource("mbiadhoc00019"));
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}

		public boolean isEnabled(Component focusComp) {
			if (m_adhocPlugin.getModel().isFormatState())
				return false;
			if(m_adhocPlugin.getModel().getDataCenter().isCross())
				return false;
			int[] cols = m_adhocPlugin.getModel().getCellsModel().getSelectModel().getSelectedCol();
			if (cols != null)
				return true;
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			int[] cols = m_adhocPlugin.getModel().getCellsModel().getSelectModel().getSelectedCol();
			if ((cols != null) && (cols.length > 0)) {
				int col = cols[0];
				IBIField field = m_adhocPlugin.getModel().getColFieldByCol(col);
				m_adhocPlugin.getModel().sortColumnInInputState(field, SortVO.SORT_DESCENDING);
				//modify by guogang 2008-4-21 强制设置选中行
				m_adhocPlugin.getModel().getCellsModel().getColumnHeaderModel().setSelectAllHeader(m_adhocPlugin.getModel().getCellsModel().getRowHeaderModel());
			}
			return null;
		}
	}

	/** 删除* */
	private class DeleteExt extends AbsActionExt {
		ActionUIDes au1 = null;

		ActionUIDes au2 = null;

		ActionUIDes au3 = null;

		/**
		 * @i18n ubiquery0110=删除
		 * @i18n ubiquery0110=删除
		 */
		public ActionUIDes[] getUIDesArr() {
			au1 = new ActionUIDes();
			au1.setName(StringResource.getStringResource("ubiquery0110"));
			au1.setImageFile("reportcore/delete.gif");
			au1.setPaths(new String[] { MultiLang.getString("edit") });

			au2 = (ActionUIDes) au1.clone();
			au2.setPaths(new String[] {});
			au2.setToolBar(true);
			au2.setTooltip(StringResource.getStringResource("ubiquery0110"));

			au3 = (ActionUIDes) au1.clone();
			au3.setPaths(new String[] {});
			au3.setPopup(true);

			return new ActionUIDes[] { au1, au2, au3 };
		}

		public UfoCommand getCommand() {
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			// return m_adhocPlugin.getOperationState() ==
			// UfoReport.OPERATION_FORMAT;
			return m_adhocPlugin.isFormat();
		}

		public Object[] getParams(UfoReport container) {
			m_adhocPlugin.getModel().onDeleteAction(m_adhocPlugin.getCellsModel().getSelectModel().getSelectedArea());
			return null;
		}
	}

	public class HiddenRowsExt extends AbsActionExt {// implements
		// IMainMenuExt{
		private UfoReport m_Report;

		public HiddenRowsExt(UfoReport rep) {
			super();
			m_Report = rep;
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
			AreaPosition selectedArea = getReport().getCellsModel().getSelectModel().getSelectedArea();
			ArrayList<Header> headers = new ArrayList<Header>();
			for (int i = 0; i < selectedArea.getHeigth(); i++) {
				Header header = getReport().getCellsModel().getRowHeaderModel().getHeader(
						i + selectedArea.getStart().getRow());
				header.setVisible(false);
				headers.add(header);
			}

			//liuyy, size事件。
			PropertyChangeEvent evt = new PropertyChangeEvent(
					headers.toArray(new Header[0]), Header.HEADER_SIZE_PROPERTY, null,
					null);
			getReport().getCellsModel().getRowHeaderModel().fireHeaderPropertyChanged(evt);
//			getReport().getTable().invalidate();
//			getReport().getTable().repaint();
			return null;
		}

		public UfoReport getReport() {
			return m_Report;
		}

		public boolean isEnabled(Component focusComp) {
			return StateUtil.isCPane1THeader(null, focusComp)
					&& getReport().getCellsModel().getSelectModel().getSelectedCol() == null
					&& !getReport().getCellsModel().getSelectModel().isSelectAll();
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00016"));
			uiDes1.setPaths(new String[] { MultiLang.getString("edit"),
					StringResource.getStringResource("mbiadhoc00015") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00015") });
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}
	}

	public class HiddenColumnsExt extends AbsActionExt {// implements
		// IMainMenuExt{
		private UfoReport m_Report;

		public HiddenColumnsExt(UfoReport rep) {
			super();
			m_Report = rep;
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
			AreaPosition selectedArea = getReport().getCellsModel().getSelectModel().getSelectedArea();
			ArrayList<Header> headers = new ArrayList<Header>();
			
			
			for (int i = 0; i < selectedArea.getWidth(); i++) {
				Header header = getReport().getCellsModel().getColumnHeaderModel().getHeader(
						i + selectedArea.getStart().getColumn());
				header.setVisible(false);
				headers.add(header);
			}

			//liuyy, size事件。
			PropertyChangeEvent evt = new PropertyChangeEvent(
					headers.toArray(new Header[0]), Header.HEADER_SIZE_PROPERTY, null,
					null);
			getReport().getCellsModel().getRowHeaderModel().fireHeaderPropertyChanged(evt);
//			getReport().getTable().invalidate();
//			getReport().getTable().repaint();
			return null;
		}

		public UfoReport getReport() {
			return m_Report;
		}

		public boolean isEnabled(Component focusComp) {
			return StateUtil.isCPane1THeader(null, focusComp)
					&& getReport().getCellsModel().getSelectModel().getSelectedRow() == null
					&& !getReport().getCellsModel().getSelectModel().isSelectAll();
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00017"));
			uiDes1.setPaths(new String[] { MultiLang.getString("edit"),
					StringResource.getStringResource("mbiadhoc00015") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00015") });
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}
	}

	public class HiddenCancelExt extends AbsActionExt {// implements
		// IMainMenuExt{
		private UfoReport m_Report;

		public HiddenCancelExt(UfoReport rep) {
			super();
			m_Report = rep;
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
			AreaPosition selectedArea = getReport().getCellsModel().getSelectModel().getSelectedArea();
			int width = getReport().getCellsModel().getColNum();
			ArrayList<Header> headers = new ArrayList<Header>();
			for (int i = 0; i < Math.min(width, selectedArea.getWidth()); i++) {
				Header header = getReport().getCellsModel().getColumnHeaderModel().getHeader(
						i + selectedArea.getStart().getColumn());
				header.setVisible(true);
				headers.add(header);
			}
			
			for (int i = 0; i < selectedArea.getHeigth(); i++) {
				int rowIndex = i + selectedArea.getStart().getRow();
				// 数据浏览状态下继续隐藏保留的格式行
				if (isHiddedFormatRow(rowIndex))
					continue;
				Header header = getReport().getCellsModel().getRowHeaderModel().getHeader(rowIndex);
				header.setVisible(true);
				headers.add(header);
			}

			//liuyy, size事件。
			PropertyChangeEvent evt = new PropertyChangeEvent(
					headers.toArray(new Header[0]), Header.HEADER_SIZE_PROPERTY, null,
					null);
			getReport().getCellsModel().getRowHeaderModel().fireHeaderPropertyChanged(evt);
			
//			getReport().getTable().invalidate();
//			getReport().getTable().repaint();
			return null;
		}

		private boolean isHiddedFormatRow(int rowIndex) {
			ArrayList<AdhocArea> areaList = m_adhocPlugin.getModel().getDataCenter().getAreaList();
			for (int i = 0; i < areaList.size(); i++) {
				AdhocArea area = areaList.get(i);
				if (area.isHiddedFormatRow(rowIndex))
					return true;
			}
			return false;
		}

		public UfoReport getReport() {
			return m_Report;
		}

		public boolean isEnabled(Component focusComp) {
			return true;
		}

		/**
		 * @i18n mbiadhoc00018=取消隐藏
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("mbiadhoc00018"));
			uiDes1.setPaths(new String[] { MultiLang.getString("edit"),
					StringResource.getStringResource("mbiadhoc00015") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00015") });
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}
	}

	/**
	 * Ad-hoc报表插件
	 */
	private AdhocPlugin m_adhocPlugin = null;

	private PageDimNavigationPanel pageDimNavigationPanel = null;

	private BINavigationPanel biNavigationPanel = null;

	public AdhocDescriptor(AdhocPlugin plugin) {
		super(plugin);
		m_adhocPlugin = plugin;
	}

	/** 页维度字段导航 */
	private INavigationExt getPageDimNavigationExt() {
		INavigationExt ext = new BINavigationExt(ReportNavPanel.NORTH_NAV, getPageDimNavigationPanel());
		return ext;
	}

	/**
	 * 页纬度导航面板
	 * 
	 * @return
	 */
	public PageDimNavigationPanel getPageDimNavigationPanel() {
		if (pageDimNavigationPanel == null) {
			pageDimNavigationPanel = new PageDimNavigationPanel();
			PageDimNavigationDropTarget target = new PageDimNavigationDropTarget(pageDimNavigationPanel);
			//先初始化PageDimNavigationDropTarget.再PageDimNavigationPanel.addPageDim()
			IMetaData[] pageDims = m_adhocPlugin.getModel().getFields(IBIField.BI__PAGE_DIM_FIELD);
			if (pageDims != null) {
				for (int i = 0; i < pageDims.length; i++) {
					if (pageDims[i] instanceof PageDimField) {
						pageDimNavigationPanel.addPageDim((PageDimField) pageDims[i]);
					}
				}
			}
			

			// 当页纬度变化时更新面板
			m_adhocPlugin.getModel().addChangeListener(pageDimNavigationPanel);

			// 用户使用拖拽进行页纬度操作时通知模型
			target.addChangeListener(m_adhocPlugin.getModel());
		}
		return pageDimNavigationPanel;
	}

	/**
	 * 查询字段导航
	 */
	private INavigationExt getQueryModelNavigationExt() {
		if (biNavigationPanel == null) {
			biNavigationPanel = new BINavigationPanel();
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) biNavigationPanel.getQueryModelTree().getModel()
					.getRoot();
			rootNode.setUserObject(StringResource.getStringResource("miufopublic128"));// =数据源

			AdhocQueryModel[] allQuerys = m_adhocPlugin.getModel().getDataCenter().getAllQuerys();
			if (allQuerys != null)
				for (AdhocQueryModel query : allQuerys) {
					IBIField[] flds = query.getMetaFields(true);
					// TODO 应该将私有纬度字段过滤掉!
					biNavigationPanel.addFieldNode(query.getQueryModel(), flds);

				}
		}
		INavigationExt ext = new BINavigationExt(ReportNavPanel.WEST_NAV, biNavigationPanel);
		return ext;
	}

	/**
	 * 查询对象和常用信息面板
	 * 
	 * @return
	 */
	public BINavigationPanel getBINavigationPanel() {
		return biNavigationPanel;
	}

	public IExtension getCellAttrExt() {
		ICommandExt extSetCellAttr = new SetCellAttrExt(getReport()) {
			public boolean isVisiable(Component focusComp) {
				return StateUtil.isCPane1THeader(getReport(), focusComp);
			}

			public Object[] getCellParams(Object[] params) {
				Object[] cellParams = super.getCellParams(params);
				cellParams = changeCellParams(cellParams);
				return cellParams;
			}

			public UfoCommand getCommand() {

				return new SetCellAttrCmd(this.getReport()) {
					public void execute(Object[] params) {
						if ((params == null) || (params.length == 0))
							return;
						boolean shouldReLoadCells = false;// 记录是否需要重新设置浏览态的单元格式
						int paramIndex = 1;
						if (params[paramIndex] != null) {
							char scope = ((Character) params[paramIndex]).charValue();
							switch (scope) {
							case 't': // 全表
								shouldReLoadCells = true;
								break;
							case 'r':// 行
								// shouldReLoadCells = true;
								break;
							case 'c':// 列
								shouldReLoadCells = true;
								break;
							}
						}
						if (shouldReLoadCells && !m_adhocPlugin.getModel().isFormatState()) {
							m_adhocPlugin.getModel().changeState(true, false);
							super.execute(params);
							m_adhocPlugin.getModel().changeState(false, false);
						} else
							super.execute(params);

					}
				};

			}
		};
		return extSetCellAttr;
	}

	private Object[] changeCellParams(Object[] cellParams) {
		if (!m_adhocPlugin.isFormat() || m_adhocPlugin.getModel().isFormatState())// 报表浏览或者纯格式设计
			return cellParams;
		if (m_adhocPlugin.getModel().getDataCenter().isCross())// 交叉表样式不做范围扩大
			return cellParams;
		if (cellParams[1] == null || !(cellParams[1] instanceof Character))
			return cellParams;

		// 追加隐藏的格式单元，一起设置新的单元格式
		Character c = (Character) cellParams[1];
		if (c.equals('r')) {// 整行格式
			int[] rows = (int[]) cellParams[2];
			if (rows != null && rows.length > 0) {
				ArrayList<Integer> al_rows = new ArrayList<Integer>();
				for (int i : rows) {
					al_rows.add(i);
				}
				for (int i : rows) {
					CellPosition pos = CellPosition.getInstance(i, 0);
					IArea area = m_adhocPlugin.getModel().getFormatArea(pos, false, true);
					if (area != null) {
						int startRow = area.getStart().getRow();
						for (int j = startRow; j < startRow + area.getHeigth(); j++) {
							if (!al_rows.contains(j))
								al_rows.add(j);
						}
					}
				}
				if (al_rows.size() > rows.length) {
					rows = new int[al_rows.size()];
					for (int i = 0; i < al_rows.size(); i++) {
						rows[i] = al_rows.get(i);
					}
					cellParams[2] = rows;
				}
			}
		} else if (c.equals('a')) {// 单元格式
			CellPosition[] poses = (CellPosition[]) cellParams[2];
			if (poses != null && poses.length > 0) {
				ArrayList<CellPosition> al_rows = new ArrayList<CellPosition>();
				for (CellPosition pos : poses) {
					al_rows.add(pos);
				}
				for (CellPosition pos : poses) {
					IArea area = m_adhocPlugin.getModel().getFormatArea(pos, false, true);
					if (area != null) {
						int startRow = area.getStart().getRow();
						for (int j = startRow; j < startRow + area.getHeigth(); j++) {
							if (!al_rows.contains(CellPosition.getInstance(j, pos.getColumn())))
								al_rows.add(CellPosition.getInstance(j, pos.getColumn()));
						}
					}
				}
				if (al_rows.size() > poses.length) {
					poses = new CellPosition[al_rows.size()];
					for (int i = 0; i < al_rows.size(); i++) {
						poses[i] = al_rows.get(i);
					}
					cellParams[2] = poses;
				}
			}
		}
		return cellParams;

	}

	public IExtension getCellConditionAttrExt() {
		ICommandExt extConCellAttr = new SetConditionAttrExt(getReport()) {
			public boolean isVisiable(Component focusComp) {
				return StateUtil.isCPane1THeader(getReport(), focusComp);
			}

			public Object[] getCellParams(Object[] params) {
				Object[] cellParams = super.getCellParams(params);
				cellParams = changeCellParams(cellParams);
				return cellParams;
			}
		};
		return extConCellAttr;
	}

	public IExtension getCombineCellExt() {
		ICommandExt extCombineCell = new CombineCellExt(getReport()) {// 组合单元
			public boolean isVisiable(Component focusComp) {
				return StateUtil.isCellsPane(getReport(), focusComp);
			}
		};
		return extCombineCell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
	 */
	protected IExtension[] createExtensions() {
		ArrayList<IExtension> al_extensions = new ArrayList<IExtension>();

		if (m_adhocPlugin.isFormat()) {
			al_extensions.add(new DataSourceDesignExt());
			al_extensions.add(new DesignExt());
			al_extensions.add(new FilterValueExt());
			al_extensions.add(new FilterRowExt());
			al_extensions.add(new FunctionFieldExt(m_adhocPlugin));
			al_extensions.add(new CalcColumnExt(m_adhocPlugin));
			al_extensions.add(new TraceReportExt(getReport()));

			if (isAdhoc())
				al_extensions.add(new BIReportSaveExt(m_adhocPlugin));
			al_extensions.add(new BIReportPreViewExt(m_adhocPlugin));

			// new AdhocXMLImportExt(),
			// new AdhocXMLExportExt(),
			// 插入行
			al_extensions.add(new InsertRowsExt(getReport()) {
				protected boolean isFormatState() {
					return m_adhocPlugin.getModel().isFormatState();
				}
			});
			// 插入列
			al_extensions.add(new InsertColumnsExt(getReport()) {
				protected boolean isFormatState() {
					//modify by guogang 2008-4-21
//					return m_adhocPlugin.isFormat();
					return m_adhocPlugin.getModel().isFormatState();
				}

				public UfoCommand getCommand() {
					return new InsertColumnsCmd(getReport()) {
						public void execute(Object[] params) {
							if (getReport() == null)
								return;
							if (params == null)
								return;

							CellsModel model = getReport().getTable().getCells().getDataModel();
							AreaPosition[] areas = model.getSelectModel().getSelectedAreas();		
							if (areas == null || areas.length < 1) return;

							// //判断所有选中的单元格是否在一列
							int CellCol = areas[0].getStart().getColumn();
							IBIField fld = m_adhocPlugin.getModel().getColFieldByCol(CellCol);
							if (fld != null) {

							}

							// 当前焦点列处插入新列，当前列以及后面的列往后面偏移
							int InsertNum = 0;
							if (params != null) {
								Integer numObj = (Integer) params[0];
								if (numObj != null)
									InsertNum = numObj.intValue();
								if (InsertNum < 0)
									InsertNum = 0;
							}
							try {
								model.getColumnHeaderModel().addHeader(CellCol, InsertNum);
							} catch (TableDataModelException e) {
								JOptionPane.showMessageDialog(getReport(), MultiLang.getString("uiuforep0000857"),// 请选择插入列的位置
										MultiLang.getString("uiuforep0000858"), JOptionPane.ERROR_MESSAGE);// 插入列错误
								return;
							}
							// super.execute完成了，接下来清理新插入列中的扩展内容
							int[] cols = new int[InsertNum];
							for (int i = 0; i < InsertNum; i++) {
								cols[i] = CellCol + i;
							}
							m_adhocPlugin.getModel().removeColFieldByCol(cols);
						}
					};
				}

			});
			// 系统插件：删除
			al_extensions.add(new com.ufsoft.report.sysplugin.insertdelete.DeleteExt(getReport()) {
				protected boolean isFormatState() {
					//modify by guogang 2008-4-21
					return m_adhocPlugin.getModel().isFormatState();
//					return m_adhocPlugin.isFormat();
				}

				public UfoCommand getCommand() {
					return new DeleteCmd(this.getReport()) {
						public void execute(Object[] params) {
							if ((params == null) || (params.length == 0))
								return;

							boolean shouldChangeToFormat = false;// 是否需要切换到格式状态（HeadModel只在格式态允许删除）
							int deleteMethod = ((Integer) params[0]).intValue();
							if (deleteMethod == DeleteInsertDialog.DELETE_ROW || deleteMethod == DeleteInsertDialog.DELTE_COLUMN) {
								shouldChangeToFormat = true;
							}
							boolean isNowFormat = m_adhocPlugin.getModel().isFormatState();
							if (shouldChangeToFormat && !isNowFormat)
								m_adhocPlugin.getModel().changeState(true, false);
							if (shouldChangeToFormat) {
								int[] selectRow = (int[]) params[1];
								String msg = checkRemoveSupport(selectRow, deleteMethod == DeleteInsertDialog.DELETE_ROW);
								if (msg != null) {
									if (!isNowFormat)
										m_adhocPlugin.getModel().changeState(false, false);
									JOptionPane.showMessageDialog(m_adhocPlugin.getReport(), msg);
									return;
								}
							}
							super.execute(params);// 执行删除

							if (!isNowFormat)
								m_adhocPlugin.getModel().changeState(false, false);
						}
					};
				}
			});
			// al_extensions.add(new DeleteExt());// 删除

			al_extensions.add(getQueryModelNavigationExt()); // 数据源导航
			al_extensions.add(getCellAttrExt());// 单元格式属性设置
			al_extensions.add(getCellConditionAttrExt()); // 单元条件格式设置
			al_extensions.add(getCombineCellExt());// 合并单元格

			// new DataPreviewExt(),// 数据集预览，临时菜单

		} else {
			al_extensions.add(new FilterValueExt());
			al_extensions.add(new FilterRowExt());
			al_extensions.add(new TraceReportExt(getReport()));

			// new HeaderFooterExt() /* 頁眉頁腳 */
		}
		al_extensions.add(new SortAscendingExt());
		al_extensions.add(new SortDescendingExt());

		al_extensions.add(getPageDimNavigationExt()); // 页维度导航
		al_extensions.add(new HiddenRowsExt(getReport()));// 隐藏行
		al_extensions.add(new HiddenColumnsExt(getReport()));// 隐藏列
		al_extensions.add(new HiddenCancelExt(getReport()));// 取消隐藏行列

		return al_extensions.toArray(new IExtension[0]);
	}

	private boolean isAdhoc() {
		Context context = m_adhocPlugin.getReport().getContextVo();
		if (context instanceof BIContextVO
				&& ((BIContextVO) context).getReportType() == ReportResource.INT_REPORT_ADHOC)
			return true;
		return false;
	}

	private AdhocQueryModel getSelectedQuery() {
		TreePath selPath = getBINavigationPanel().getQueryModelTree().getSelectionPath();
		if (selPath == null) {
			return m_adhocPlugin.getModel().getDataCenter().getCurrQuery();
		}

		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
		if (selNode == null)
			return null;

		IFreeQueryModel selQuery = null;
		if (selNode.getUserObject() instanceof IMetaData) {
			DefaultMutableTreeNode queryNode = (DefaultMutableTreeNode) selNode.getParent();
			selQuery = (IFreeQueryModel) queryNode.getUserObject();
		} else if (selNode.getUserObject() instanceof IFreeQueryModel) {
			selQuery = (IFreeQueryModel) selNode.getUserObject();
		}
		if (selQuery != null)
			return m_adhocPlugin.getModel().getDataCenter().getQueryByID(selQuery.getID());
		return null;
	}

	/** 数据结果预览, 临时插件* */
	private class DataPreviewExt extends AbsActionExt {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
		 */
		/**
		 * @i18n miufo00242=数据结果预览
		 */
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo00242"));
			uiDes1.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00005") });
			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setPaths(new String[] {});
			uiDes2.setPopup(true);
			return new ActionUIDes[] { uiDes1, uiDes2 };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
		 */
		public UfoCommand getCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			// return m_adhocPlugin.getOperationState() ==
			// UfoReport.OPERATION_FORMAT;
			return m_adhocPlugin.isFormat();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
		 */
		/**
		 * @i18n miufo00420=请先选中要查看的数据源
		 */
		public Object[] getParams(UfoReport container) {
			AdhocQueryModel selQuery = getSelectedQuery();
			if (selQuery == null) {
				JOptionPane.showMessageDialog(getReport(), StringResource.getStringResource("miufo00420"));
				return null;
			}
			BIMultiDataSet ds = selQuery.getQueryResult(container.getContextVo(), true);
			DataSetPreviewDlg dlg = new DataSetPreviewDlg(container, FreeQueryUtil.getDesignerByModelType(selQuery
					.getQueryModel().getModelType()), container.getContextVo());
			dlg.setDatas(ds, selQuery.getQueryModel());
			dlg.showModal();
			return null;
		}
	}

	private String checkRemoveSupport(int[] selectRows, boolean isRow) {
		ArrayList<AdhocArea> al_areas = m_adhocPlugin.getModel().getDataCenter().getAreaList();
		try {
			for (int i = 0; i < selectRows.length; i++) {
				HeaderEvent checkEvent = new HeaderEvent(m_adhocPlugin.getModel().getCellsModel().getRowHeaderModel(),
						isRow, false, selectRows[i], 1);

				for (AdhocArea area : al_areas) {
					area.isSupport(0, checkEvent);
				}
			}
		} catch (ForbidedOprException ex) {
			return ex.getMessage();
		}
		return null;
	}

	/**
	 */
	private void doDataSourceDesign(UfoReport container) {
		// 1。根据当前数据源类型，调用相应的设计器
		AdhocQueryModel selQuery = m_adhocPlugin.getModel().getDataCenter().getCurrQuery();
		boolean bNewQuery = (selQuery == null || selQuery.getQueryModel() == null);
		String queryType = bNewQuery ? (isAdhoc() ? IFreeQueryModel.TYPE_BIQUERYMODEL
				: IFreeQueryModel.TYPE_MEASUREQUERY) : selQuery.getQueryModel().getModelType();
		IFreeQueryDesigner designer = FreeQueryUtil.getDesignerByModelType(queryType);
		if (designer.designQuery(getReport(), bNewQuery ? null : selQuery.getQueryModel(), getReport().getContextVo())) {
			// 2。设置设计结果。如果需要，自动生成报表内容
			IFreeQueryModel query = designer.getQueryDefResult();
			if (bNewQuery) {
				m_adhocPlugin.getModel().getDataCenter().addQuery(query);
			} else
				selQuery.setQueryModel(query);
			AppDebug.debug("设置新的查询模型");
			checkRemovedFld(m_adhocPlugin.getModel().getDataCenter().getCurrQuery().getMetaDatas());
			FreeQueryDesignePlugin.initReportDesigner(getReport(), query, (FreeQueryContextVO) getReport()
					.getContextVo(), bNewQuery, false);
		}

	}

	private void checkRemovedFld(IMetaData[] currMetaDatas) {
		Hashtable<?, ?> list = m_adhocPlugin.getCellsModel().getBsFormats(AdhocPublic.EXT_FMT_SELECTED_FIELD);

		// 删除旧字段
		CellPosition pos = null;
		Enumeration eum = list.keys();
		while (eum.hasMoreElements()) {
			pos = (CellPosition) eum.nextElement();
			IMetaData fld = (IMetaData) list.get(pos);
			if (!Arrays.asList(currMetaDatas).contains(fld))
				m_adhocPlugin.getModel().removeQueryField(pos);
		}
	}
}
  