/*
 * Created on 2005-6-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.vo.bi.base.util.IDMaker;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.calc.CalcLanguageRes;
import com.ufida.report.multidimension.calc.MultiDimCalcDlg;
import com.ufida.report.multidimension.calc.MultiDimCalcService;
import com.ufida.report.multidimension.calc.MultiDimFilterCondDlg;
import com.ufida.report.multidimension.calc.MultiDimFilterService;
import com.ufida.report.multidimension.model.AnalyzeUtil;
import com.ufida.report.multidimension.model.AnalyzerFilterVO;
import com.ufida.report.multidimension.model.AnalyzerHiddenVO;
import com.ufida.report.multidimension.model.AnalyzerSet;
import com.ufida.report.multidimension.model.AnalyzerSortVO;
import com.ufida.report.multidimension.model.CalcMemberVO;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.HiddenDescriptor;
import com.ufida.report.multidimension.model.IAnalyzerSet;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.LimitRowsSetVO;
import com.ufida.report.multidimension.model.MultiDimFilterCondItem;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufida.report.rep.applet.SortDlg;
import com.ufida.report.rep.model.FilterRowDescriptor;
import com.ufida.report.rep.model.SortVO;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.script.exception.ParseException;

/**
 * @author ll
 * 
 * 分析管理器设置对话框
 */
public class AnalyzerDialog extends nc.ui.pub.beans.UIDialog implements ActionListener, ListSelectionListener,
		PropertyChangeListener, TableModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ５种分析设置编辑器
	private IAnalyzerDialog[] m_editors = new IAnalyzerDialog[IAnalyzerSet.TYPE_COUNT];

	// 维度成员模型
	private SelDimModel m_seldimModel = null;

	// private MultiDimDataModel m_dataModel = null;

	// 分析设置数据模型
	private AnalyzerSet m_analyzerModel = null;

	// 当前所处的维度和行／列
	private String m_pk_dimdef = null;

	private int m_combine_pos = IMultiDimConst.COMBINE_COLUMN;

	/**
	 * 计算列服务
	 */
	MultiDimCalcService m_calcSrv = null;

	private MultiDimCalcService getCalcSrv() {
		return m_calcSrv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		int index = getJTable().getSelectedRow();// e.getFirstIndex()
		processSelectIndex(index);
	}

	private class FilterEditor implements IAnalyzerDialog {
		AnalyzerFilterVO m_filter = null;

		public IAnalyzerSet getAnalyzerSet() {
			return m_filter;
		}

		public void setAnalyzer(IAnalyzerSet analyzerSet) {
			if (analyzerSet != null && analyzerSet.getAnalyzerType() == IAnalyzerSet.TYPE_FILTER) {
				m_filter = (AnalyzerFilterVO) analyzerSet;
			} else {
				m_filter = null;
			}
		}

		public int showModel(Container parent) {
			// TODO AnalyzerFilterVO中操作数记录的DimMemberCombinationVO不是最新的。
			if (m_filter == null) {
				// 判断只能在一个方向添加一个过滤条件
				IAnalyzerSet[] filterConds = m_analyzerModel.getAnalyzer(IAnalyzerSet.TYPE_FILTER, null, m_combine_pos);
				if (filterConds != null && filterConds.length > 0) {
					JOptionPane.showMessageDialog(parent, StringResource
							.getStringResource(MultiDimFilterCondDlg.STR_FILTER_CONSTRAIN));
					return UIDialog.ID_CANCEL;
				}
			}
			ArrayList<MultiDimFilterCondItem> listCond = new ArrayList<MultiDimFilterCondItem>();
			if (m_filter != null && m_filter.getFilterCond() != null) {
				MultiDimFilterCondItem temp = null;
				for (int i = 0, size = m_filter.getFilterCond().size(); i < size; i++) {
					temp = (MultiDimFilterCondItem) m_filter.getFilterCond().get(i);
					listCond.add((MultiDimFilterCondItem) temp.clone());
				}
			}

			DimMemberCombinationVO[] fields = getFields(m_filter == null ? m_combine_pos : m_filter.getCombineType());
			if (fields != null) {
				int iCombinePos = m_combine_pos;
				if (m_filter != null)
					iCombinePos = m_filter.getCombineType();

				MultiDimFilterCondDlg dlg = new MultiDimFilterCondDlg(parent, listCond, iCombinePos, fields,
						m_seldimModel);
				dlg.setLocationRelativeTo(parent);
				dlg.show();
				if (dlg.getResult() == UIDialog.ID_OK) {
					if (m_filter == null)
						m_filter = new AnalyzerFilterVO(m_combine_pos);

					m_filter.setFilterCond(dlg.getFilterCond());
					return UIDialog.ID_OK;
				}
			}
			return UIDialog.ID_CANCEL;

		}

		public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer) {
			return true;
		}

		public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzers) {

		}

		public void processMemberChanged(SelDimModel oldSelDimModel, SelDimModel newSelDimModel) {
			IAnalyzerSet[] analyzers = m_analyzerModel.getAnalyzerByType(IAnalyzerSet.TYPE_FILTER);
			if (analyzers != null && analyzers.length > 0) {

				MultiDimFilterService filterSrv = new MultiDimFilterService(newSelDimModel, null);

				for (int i = 0, size = analyzers.length; i < size; i++) {
					if (analyzers[i] == null)
						continue;
					String strErr = filterSrv.checkFilterCond((AnalyzerFilterVO) analyzers[i]);
					if (strErr != null)
						analyzers[i].setValid(false);
					else
						analyzers[i].setValid(true);
				}
			}
		}
	}

	private class SortEditor implements IAnalyzerDialog {
		AnalyzerSortVO m_sort = null;

		public IAnalyzerSet getAnalyzerSet() {
			return m_sort;
		}

		public void setAnalyzer(IAnalyzerSet analyzerSet) {
			if (analyzerSet != null && analyzerSet.getAnalyzerType() == IAnalyzerSet.TYPE_SORT) {
				m_sort = (AnalyzerSortVO) analyzerSet;
			} else
				m_sort = null;
		}

		public int showModel(Container parent) {
			SortVO desc = (m_sort == null) ? null : m_sort.getSortVO();
			DimMemberCombinationVO[] fields = getFields(m_sort == null ? m_combine_pos : m_sort.getCombineType());
			if (fields != null) {

				SortDlg dlg = new SortDlg(parent, fields, desc);
				dlg.setLocationRelativeTo(parent);
				dlg.show();
				if (dlg.getResult() == UIDialog.ID_OK) {
					if (m_sort == null)
						m_sort = new AnalyzerSortVO(m_combine_pos);

					m_sort.setSortVO(dlg.getResultSort());
					return UIDialog.ID_OK;
				}
			}
			return UIDialog.ID_CANCEL;

		}

		public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer) {
			// TODO Auto-generated method stub
			return true;
		}

		public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzers) {
			// TODO Auto-generated method stub

		}

		public void processMemberChanged(SelDimModel oldSelDimModel, SelDimModel newSelDimModel) {
			AnalyzerCheckService checkService = new AnalyzerCheckService(newSelDimModel);
			IAnalyzerSet[] analyzers = m_analyzerModel.getAnalyzerByType(IAnalyzerSet.TYPE_SORT);
			if (analyzers != null && analyzers.length > 0) {
				for (int i = 0, size = analyzers.length; i < size; i++) {
					if (analyzers[i] == null)
						continue;
					SortVO sortDesc = ((AnalyzerSortVO) analyzers[i]).getSortVO();
					boolean isRow = (analyzers[i].getCombineType() == IMultiDimConst.COMBINE_ROW);
					ArrayList<IMetaData> al_sorts = sortDesc.getSortList();
					if (al_sorts != null) {
						boolean isValid = true;
						for (int j = 0; j < al_sorts.size(); j++) {
							isValid = checkService.isValidAnalyzer(isRow, true, al_sorts.get(j));
							if (!isValid)
								break;
						}
						analyzers[i].setValid(isValid);
					}
				}
			}
		}

	}

	private class LimitEditor implements IAnalyzerDialog {

		LimitRowsSetVO m_limit = null;

		public IAnalyzerSet getAnalyzerSet() {
			return m_limit;
		}

		public void setAnalyzer(IAnalyzerSet analyzerSet) {
			if (analyzerSet != null && analyzerSet.getAnalyzerType() == IAnalyzerSet.TYPE_LIMITROWS) {
				m_limit = (LimitRowsSetVO) analyzerSet;
			} else
				m_limit = null;
		}

		public int showModel(Container parent) {
			DimMemberCombinationVO[] fields = getFields(m_limit == null ? m_combine_pos : m_limit.getCombineType());
			if (fields != null) {

				RowLimitDlg dlg = new RowLimitDlg(parent);
				dlg.setCandidates(fields);
				if (m_limit != null) {
					dlg.setRowLimitSetVO(m_limit);
				} else {
					dlg.setRowLimitSetVO(new LimitRowsSetVO(m_combine_pos));
				}
				dlg.setLocationRelativeTo(parent);
				if (dlg.showModal() == UIDialog.ID_OK) {
					// if (m_limit == null)
					// m_limit = new LimitRowsSetVO(m_combine_pos);
					//
					// m_limit.setFilterRowDesc(dlg.getFilterRowDescriptor());
					m_limit = dlg.getRowLimitSetVO();
					return UIDialog.ID_OK;
				}
			}
			return UIDialog.ID_CANCEL;

		}

		public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer) {
			// TODO Auto-generated method stub
			return true;
		}

		public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzers) {
			// TODO Auto-generated method stub

		}

		public void processMemberChanged(SelDimModel oldSelDimModel, SelDimModel newSelDimModel) {
			AnalyzerCheckService checkService = new AnalyzerCheckService(newSelDimModel);
			IAnalyzerSet[] analyzers = m_analyzerModel.getAnalyzerByType(IAnalyzerSet.TYPE_LIMITROWS);
			if (analyzers != null && analyzers.length > 0) {
				for (int i = 0, size = analyzers.length; i < size; i++) {
					if (analyzers[i] == null)
						continue;
					FilterRowDescriptor limitDesc = ((LimitRowsSetVO) analyzers[i]).getFilterRowDesc();
					boolean isRow = (analyzers[i].getCombineType() == IMultiDimConst.COMBINE_ROW);
					boolean isValid = checkService.isValidAnalyzer(isRow, true, limitDesc.getField());
					analyzers[i].setValid(isValid);
				}
			}
		}

	}

	private class FormulaEditor implements IAnalyzerDialog {

		private CalcMemberVO m_calcMem = null;

		// /**
		// * 记录引用当前删除的计算列的计算列id
		// */
		// private String[] m_strReferringDels = null;

		/**
		 * 记录计算列旧顺序(计算列id按顺序排列)）
		 */
		private String[] m_oldOrderedIds = null;

		private Container m_parent = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufida.report.multidimension.applet.IAnalyzerDialog#getAnalyzerSet()
		 */
		public IAnalyzerSet getAnalyzerSet() {
			return m_calcMem;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufida.report.multidimension.applet.IAnalyzerDialog#setAnalyzer(com.ufida.report.multidimension.model.IAnalyzerSet)
		 */
		public void setAnalyzer(IAnalyzerSet analyzerSet) {
			if (analyzerSet != null && analyzerSet.getAnalyzerType() == IAnalyzerSet.TYPE_FORMULAR) {
				m_calcMem = (CalcMemberVO) analyzerSet;
			} else
				m_calcMem = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufida.report.multidimension.applet.IAnalyzerDialog#showModel()
		 */
		public int showModel(Container parent) {
			m_parent = parent;

			IAnalyzerSet[] analyzers = m_analyzerModel.getAnalyzerByType(IAnalyzerSet.TYPE_FORMULAR);

			MultiDimCalcDlg dlg = null;
			boolean bNew = false;
			if (m_calcMem == null) {
				dlg = new MultiDimCalcDlg(parent, m_seldimModel, getCalcSrv(), getFields(m_combine_pos), m_pk_dimdef,
						m_combine_pos);
				bNew = true;
			} else
				dlg = new MultiDimCalcDlg(parent, m_seldimModel, getCalcSrv(), getFields(m_calcMem.getCombineType()),
						m_calcMem);
			dlg.setLocationRelativeTo(parent);
			dlg.show();
			if (dlg.getResult() == UIDialog.ID_OK) {
				m_calcMem = dlg.getCalcMember();
				if (bNew) {
					// 记录相对位置
					int iOrder = analyzers == null ? 0 : analyzers.length;
					m_calcMem.setCalcOrder(iOrder);
					m_calcMem.setEnabled(true);

				}
				return UIDialog.ID_OK;
			}
			return UIDialog.ID_CANCEL;
		}

		public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer) {

			boolean bReturn = true;
			if (changeType == IAnalyzerDialog.ADD) {
				// new
				try {
					bReturn = getCalcSrv().addCalcCol(m_calcMem);
				} catch (ParseException e) {
					AppDebug.debug(e);// @devTools AppDebug.debug(e);
					JOptionPane.showMessageDialog(m_parent, e.getMessage(), StringResource
							.getStringResource(CalcLanguageRes.STR_MSG_ADD_ERR), JOptionPane.ERROR_MESSAGE);
					bReturn = false;
				}
			} else if (changeType == IAnalyzerDialog.EDIT) {
				// modify
				try {
					getCalcSrv().updateCalcForm(m_calcMem);
					getTableModel().fireTableDataChanged();
				} catch (ParseException e) {
					AppDebug.debug(e);// @devTools AppDebug.debug(e);
					JOptionPane.showMessageDialog(m_parent, e.getMessage(), StringResource
							.getStringResource(CalcLanguageRes.STR_MSG_EDIT_ERR), JOptionPane.ERROR_MESSAGE);
					bReturn = false;
				}
			} else if (changeType == IAnalyzerDialog.REMOVE) {
				getCalcSrv().removeCalcCol(m_calcMem.getID());
				getTableModel().fireTableDataChanged();
			} else if (changeType == IAnalyzerDialog.CHANGE_ORDER) {
				// 获得旧顺序

				m_oldOrderedIds = getOrderedCalcIds();
			}
			return bReturn;
		}

		private String[] getOrderedCalcIds() {
			String[] strReturns = null;
			IAnalyzerSet[] analyzers = m_analyzerModel.getAnalyzerByType(IAnalyzerSet.TYPE_FORMULAR);
			if (analyzers != null) {
				strReturns = new String[analyzers.length];
				for (int i = 0, size = analyzers.length; i < size; i++) {
					strReturns[i] = analyzers[i].getID();
				}
			}
			return strReturns;
		}

		public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzer) {

			if (analyzerType != IAnalyzerSet.TYPE_FORMULAR)
				return;

			if (changeType == IAnalyzerDialog.CHANGE_ORDER) {
				// 改变顺序
				String[] strNewOrderIds = getOrderedCalcIds();
				if (m_oldOrderedIds != null && strNewOrderIds == null) {
					boolean bChanged = false;
					if (m_oldOrderedIds.length == strNewOrderIds.length) {
						for (int i = 0, size = m_oldOrderedIds.length; i < size; i++) {
							if (m_oldOrderedIds[i] == null) {
								if (strNewOrderIds[i] != null) {
									bChanged = true;
									break;
								}
							} else if (m_oldOrderedIds[i].equals(strNewOrderIds[i]) == false) {
								bChanged = true;
								break;
							}
						}
					} else {
						bChanged = true;
					}
					if (bChanged == true)
						getCalcSrv().orderCalcMember(strNewOrderIds);
				}
			} else if (changeType == IAnalyzerDialog.ENABLE) {
				CalcMemberVO newObj = (CalcMemberVO) newAnalyzer;
				if (newAnalyzer.isEnabled()) {
					// 启用
					getCalcSrv().updateCalcEnable(newObj, false);
				} else {
					// 禁用
					getCalcSrv().updateCalcEnable(newObj, true);
				}
				getTableModel().fireTableDataChanged();

			}
		}

		public void processMemberChanged(SelDimModel oldSelDimModel, SelDimModel newSelDimModel) {
			// SelDimModelUtil util = new SelDimModelUtil(oldSelDimModel,
			// newSelDimModel);

			// // 增加维度
			// String[] strAddRowDims = util.getAddDims(true);
			// if (strAddRowDims!=null && strAddRowDims.length>0) {
			// m_calcSrv.updateCalcByAddDim(strAddRowDims,true);
			// }
			// String[] strAddColDims = util.getAddDims(false);
			// if (strAddColDims!=null && strAddColDims.length>0)
			// m_calcSrv.updateCalcByAddDim(strAddColDims,false);
			//			
			// //获得增加维度，将维度对应的无效计算列有效
			//			
			//
			// // 减少的维度,更新行列方向的计算列，且删除对应维度的计算列
			// String[] strDelRowDims = util.getDelDims(true);
			// m_calcSrv.updateCalcByDelDim(strDelRowDims,true);
			//			
			// String[] strDelColDims = util.getDelDims(false);
			// m_calcSrv.updateCalcByDelDim(strDelColDims,false);
			//			
			//
			// // 维度顺序改变
			// boolean bRowOrderChanged = util.isDimOrderChange(true);
			// if (bRowOrderChanged)
			// m_calcSrv.updateCalcByMoveDim(true,
			// SelDimModelUtil.getRowColDimIds(true, newSelDimModel));
			// boolean bColOrderChanged = util.isDimOrderChange(false);
			// if (bColOrderChanged)
			// m_calcSrv.updateCalcByMoveDim(false,
			// SelDimModelUtil.getRowColDimIds(false, newSelDimModel));
			//
			// // 维度成员减少
			// IMember[] delMembers = util.getDelMembers();
			// m_calcSrv.updateCalcByDelMem(delMembers);

			m_calcSrv.resetSelDimModel(newSelDimModel);

		}

	}

	private class HiddenEditor implements IAnalyzerDialog {
		private SelDimModel m_dimModel = null;

		private HiddenDialog m_dlg = null;

		private AnalyzerHiddenVO m_analyzer = null;

		public HiddenEditor(Container parent, SelDimModel model) {
			this.m_dimModel = model;
			init(parent);
		}

		public IAnalyzerSet getAnalyzerSet() {
			m_analyzer.setHiddenDesc(getDlg().getHiddenDesc());
			return m_analyzer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufida.report.multidimension.applet.IAnalyzerDialog#setAnalyzer(com.ufida.report.multidimension.model.IAnalyzerSet)
		 */
		public void setAnalyzer(IAnalyzerSet analyzerSet) {
			init(null);//debug
			m_analyzer = (AnalyzerHiddenVO) analyzerSet;
			if (m_analyzer == null) {// 构建一个新的隐藏行列
				m_analyzer = new AnalyzerHiddenVO(m_combine_pos);
				m_analyzer.setEnabled(true);
				m_analyzer.setID(IDMaker.makeID(10));
			}
			int pos = m_analyzer.getCombineType();
			getDlg().setFileds(getMembers(pos), getCombineMembers(pos));
			getDlg().setHiddenDesc(m_analyzer.getHiddenDesc());

		}

		private IMetaData[] getMembers(int pos) {
			SelDimensionVO[] dims = m_seldimModel.getSelDimVOs(pos);
			if (dims == null || dims.length == 0)
				return null;
			Vector<DimMemberCombinationVO> vec = new Vector<DimMemberCombinationVO>();
			for (int i = 0; i < dims.length; i++) {
				IMember[] mems = dims[i].getAllMembers();
				for (int j = 0; j < mems.length; j++) {
					DimMemberCombinationVO vo = new DimMemberCombinationVO();
					vo.setMembers(new IMember[] { mems[j] });
					vec.addElement(vo);
				}
			}
			IMetaData[] fields = new IMetaData[vec.size()];
			vec.copyInto(fields);
			return fields;
		}

		private IMetaData[] getCombineMembers(int pos) {
			SelDimensionVO[] dims = m_seldimModel.getSelDimVOs(pos);
			if (dims == null || dims.length == 0)
				return null;
			return MultiDimensionUtil.getAllCombination(dims);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufida.report.multidimension.applet.IAnalyzerDialog#showModel()
		 */
		public int showModel(Container parent) {
			getDlg().setLocationRelativeTo(parent);
			return getDlg().showModal();
		}

		private void init(Container parent) {
			m_dlg = new HiddenDialog(parent, m_dimModel);
		}

		private HiddenDialog getDlg() {
			return m_dlg;
		}

		public boolean beforeChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer) {
			// TODO Auto-generated method stub
			return true;
		}

		public void afterChanged(int changeType, int analyzerType, IAnalyzerSet editAnalyzer, IAnalyzerSet newAnalyzers) {
			// TODO Auto-generated method stub

		}

		public void processMemberChanged(SelDimModel oldSelDimModel, SelDimModel newSelDimModel) {
			AnalyzerCheckService checkService = new AnalyzerCheckService(newSelDimModel);
			IAnalyzerSet[] analyzers = m_analyzerModel.getAnalyzerByType(IAnalyzerSet.TYPE_HIDDEN);
			if (analyzers != null && analyzers.length > 0) {
				for (int i = 0, size = analyzers.length; i < size; i++) {
					if (analyzers[i] == null)
						continue;
					HiddenDescriptor hiddenDesc = ((AnalyzerHiddenVO) analyzers[i]).getHiddenDesc();
					boolean isRow = (analyzers[i].getCombineType() == IMultiDimConst.COMBINE_ROW);
					IMetaData[] conds = hiddenDesc.getSelHiddenMember();
					if (conds != null) {
						boolean isValid = true;
						for (int j = 0; j < conds.length; j++) {
							Object condKey = conds[j];
							if (!hiddenDesc.isCombine())// 隐藏行列中存储的都是组合对象，对于单维度的需要拆分出维度成员
								condKey = ((DimMemberCombinationVO) conds[j]).getMembers()[0];
							isValid = checkService.isValidAnalyzer(isRow, hiddenDesc.isCombine(), condKey);
							if (!isValid)
								break;
						}
						analyzers[i].setValid(isValid);
					}
				}
			}
		}
	}

	public AnalyzerDialog(Container con, MultiDimemsionModel multiDimModel) {
		super(con);
		this.m_seldimModel = multiDimModel.getSelDimModel();
		this.m_analyzerModel = multiDimModel.getAnalyzerSet();
		// this.m_dataModel = multiDimModel.getDataModel();
		this.m_calcSrv = multiDimModel.getCalcSrv();
		initialize();
	} /*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// 编辑
		if (o == getJButtonEdit()) {
			int index = getJTable().getSelectedRow();
			if (index >= 0 && index < m_analyzerModel.getAllAnalyzer().length) {
				IAnalyzerSet analyzer = m_analyzerModel.getAnalyzer(index);
				IAnalyzerSet newSet = editAnalyzer(analyzer.getAnalyzerType(), analyzer);
				if (newSet != null) {
					m_analyzerModel.setAnalyzer(index, newSet);

					getTableModel().fireTableDataChanged();
					getJTable().getSelectionModel().setSelectionInterval(index, index);

					afterEditAnalyzer(IAnalyzerDialog.EDIT, analyzer.getAnalyzerType(), analyzer);
				}
			}
			return;
		}
		// 删除
		if (o == getJButtonDelete()) {
			int index = getJTable().getSelectedRow();
			if (index < 0 || index > m_analyzerModel.getAllAnalyzer().length)
				return;
			IAnalyzerSet analyzer = m_analyzerModel.getAnalyzer(index);
			IAnalyzerDialog editDlg = getEditor(analyzer.getAnalyzerType());
			editDlg.setAnalyzer(analyzer);

			boolean canBeRemove = editDlg.beforeChanged(IAnalyzerDialog.REMOVE, analyzer.getAnalyzerType(), analyzer);
			if (!canBeRemove)
				return;
			m_analyzerModel.delAnalyzer(index);
			if (index >= m_analyzerModel.getRowCount())
				index--;
			getTableModel().fireTableDataChanged();
			getJTable().getSelectionModel().setSelectionInterval(index, index);

			editDlg.afterChanged(IAnalyzerDialog.REMOVE, analyzer.getAnalyzerType(), analyzer, null);
		}
		// 上移
		if (o == getJButtonUp()) {
			int index = getJTable().getSelectedRow();
			if (index < 0 || index > m_analyzerModel.getAllAnalyzer().length)
				return;
			IAnalyzerSet anaSet = m_analyzerModel.getAnalyzer(index);
			if (anaSet != null) {
				IAnalyzerDialog editDlg = getEditor(anaSet.getAnalyzerType());
				editDlg.setAnalyzer(anaSet);

				boolean canBeMove = editDlg.beforeChanged(IAnalyzerDialog.CHANGE_ORDER, anaSet.getAnalyzerType(),
						anaSet);

				if (canBeMove) {
					m_analyzerModel.delAnalyzer(index);
					m_analyzerModel.addAnalyzer(new Integer(index - 1), anaSet);
					getTableModel().fireTableDataChanged();
					getJTable().getSelectionModel().setSelectionInterval(index - 1, index - 1);

					editDlg.afterChanged(IAnalyzerDialog.CHANGE_ORDER, anaSet.getAnalyzerType(), anaSet, anaSet);
				}
			}
			return;
		}

		// 下移
		if (o == getJButtonDown()) {
			int index = getJTable().getSelectedRow();
			if (index < 0 || index > m_analyzerModel.getAllAnalyzer().length)
				return;
			IAnalyzerSet anaSet = m_analyzerModel.getAnalyzer(index);
			if (anaSet != null) {
				IAnalyzerDialog editDlg = getEditor(anaSet.getAnalyzerType());
				editDlg.setAnalyzer(anaSet);

				boolean canBeMove = editDlg.beforeChanged(IAnalyzerDialog.CHANGE_ORDER, anaSet.getAnalyzerType(),
						anaSet);
				if (canBeMove) {

					m_analyzerModel.delAnalyzer(index);
					m_analyzerModel.addAnalyzer(new Integer(index + 1), anaSet);
					getTableModel().fireTableDataChanged();
					getJTable().getSelectionModel().setSelectionInterval(index + 1, index + 1);
					editDlg.afterChanged(IAnalyzerDialog.CHANGE_ORDER, anaSet.getAnalyzerType(), anaSet, anaSet);
				}
			}
			return;
		}
		// 关闭，并根据计算项的依赖关系自动调整顺序
		if (o == getJButtonClose()) {
			boolean isRightOrder = checkCalcOrder();
			if (isRightOrder) {
				super.closeOK();
				return;
			} else {
				getTableModel().fireTableDataChanged();
				MessageDialog.showHintDlg(this, null, StringResource.getStringResource("mbimultidim0006"));// 
			}
		}
		// 新增
		int append_AnalyzerType = -1;
		if (o == getJButtonFilter()) {
			append_AnalyzerType = IAnalyzerSet.TYPE_FILTER;
		} else if (o == getJButtonSort()) {
			append_AnalyzerType = IAnalyzerSet.TYPE_SORT;
		} else if (o == getJButtonLimitRows()) {
			append_AnalyzerType = IAnalyzerSet.TYPE_LIMITROWS;
		} else if (o == getJButtonFormula()) {
			append_AnalyzerType = IAnalyzerSet.TYPE_FORMULAR;
		} else if (o == getJButtonHidden()) {
			append_AnalyzerType = IAnalyzerSet.TYPE_HIDDEN;
		}
		if (append_AnalyzerType >= 0) {
			// 对于有个数限制的分析类型，可能会处理成“编辑”
			int oldIndex = AnalyzeUtil.getDefinedAnalyzerSetIndex(m_analyzerModel, append_AnalyzerType, m_combine_pos);
			IAnalyzerSet oldSet = (oldIndex == -1) ? null : m_analyzerModel.getAnalyzer(oldIndex);
			IAnalyzerSet newSet = editAnalyzer(append_AnalyzerType, oldSet);
			if (newSet != null) {
				if (oldIndex >= 0) {
					m_analyzerModel.setAnalyzer(oldIndex, newSet);
					getTableModel().fireTableDataChanged();
					getJTable().getSelectionModel().setSelectionInterval(oldIndex, oldIndex);
					afterEditAnalyzer(IAnalyzerDialog.EDIT, append_AnalyzerType, oldSet);
				} else {
					m_analyzerModel.addAnalyzer(null, newSet);
					int newIndex = m_analyzerModel.getRowCount() + 1;
					getTableModel().fireTableRowsInserted(m_analyzerModel.getRowCount(),
							m_analyzerModel.getRowCount() + 1);
					getJTable().getSelectionModel().setSelectionInterval(newIndex, newIndex);
					afterEditAnalyzer(IAnalyzerDialog.ADD, append_AnalyzerType, newSet);
				}
			}
			return;
		}

	}

	private boolean checkCalcOrder() {
		IAnalyzerSet[] allItems = getAnalyzerSet().getAllAnalyzer();
		if (allItems == null || allItems.length == 0 || getCalcSrv() == null)
			return true;

		String[] calcIDs = getCalcSrv().getOrderedCalc();
		if (calcIDs == null || calcIDs.length == 0)
			return true;

		int[] oldPos = new int[calcIDs.length];
		int index = 0;
		boolean isRightOrder = true;
		for (int i = 0; i < allItems.length; i++) {
			IAnalyzerSet item = allItems[i];
			// 只检查启用并且有效的计算项
			if (item.getAnalyzerType() == IAnalyzerSet.TYPE_FORMULAR && item.isEnabled() && item.isValid()) {
				String itemID = item.getID();
				if (isRightOrder) {// 有顺序不同的就不用再比较啦
					if (!itemID.equals(calcIDs[index]))
						isRightOrder = false;
				}
				oldPos[index++] = i;
			}
		}
		if (!isRightOrder) {// 直接调整计算项的顺序，目前采取的策略是全部按照新的顺序放在最后
			//
			IAnalyzerSet[] orderedItems = new IAnalyzerSet[allItems.length];
			index = 0;
			for (int i = 0; i < allItems.length; i++) {// 先排列不参与计算项排序的内容
				if (i == oldPos[index]) {
					index++;
				} else {
					orderedItems[i - index] = allItems[i];
				}
			}
			for (int i = 0; i < calcIDs.length; i++) {
				int oldIndex = -1;
				for (int j = 0; j < oldPos.length; j++) {
					if (calcIDs[i].equals(allItems[oldPos[j]].getID())) {
						oldIndex = oldPos[j];
						break;
					}
				}
				if (oldIndex >= 0)
					orderedItems[allItems.length - oldPos.length + i] = allItems[oldIndex];
				else {// 排序后的计算项找不到原始位置，此种情况不应该出现！
					// ignore?
				}
			}
			for (int i = 0; i < orderedItems.length; i++) {
				getAnalyzerSet().setAnalyzer(i, orderedItems[i]);
			}
		}
		return isRightOrder;
	}

	private JPanel jPanel = null;

	private JTable jTable = null;

	private AnalyzerSetTableModel m_tableModel = null;

	private JScrollPane jScrollPane = null;

	private JButton m_btnEdit = null;

	private JButton m_btnDelete = null;

	private JButton m_btnUp = null;

	private JButton m_btnDown = null;

	private JButton m_btnClose = null;

	private JButton m_btnFilter = null;

	private JButton m_btnLimitRows = null;

	private JButton m_btnSort = null;

	private JButton m_btnHidden = null;

	private JButton m_btnFormula = null;

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(573, 368);

	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getJButtonDelete(), null);
			jPanel.add(getJButtonUp(), null);
			jPanel.add(getJButtonDown(), null);
			jPanel.add(getJButtonClose(), null);
			jPanel.add(getJButtonFilter(), null);
			jPanel.add(getJButtonLimitRows(), null);
			jPanel.add(getJButtonSort(), null);
			jPanel.add(getJButtonHidden(), null);
			jPanel.add(getJButtonFormula(), null);
			jPanel.add(getJButtonEdit(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new UITable();
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.getSelectionModel().addListSelectionListener(this);

			jTable.setModel(getTableModel());

			int[] preWidths = new int[] { 20, 50, 40, 200 };
			for (int i = 0; i < preWidths.length; i++) {
				jTable.getColumn(jTable.getColumnName(i)).setPreferredWidth(preWidths[i]);
			}
		}
		return jTable;
	}

	private AnalyzerSetTableModel getTableModel() {
		if (m_tableModel == null) {
			m_tableModel = new AnalyzerSetTableModel(m_analyzerModel);
			m_tableModel.addTableModelListener(this);

		}
		return m_tableModel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(15, 79, 440, 229);
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton8
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonEdit() {
		if (m_btnEdit == null) {
			m_btnEdit = new nc.ui.pub.beans.UIButton();
			m_btnEdit.setBounds(474, 100, 75, 22);
			m_btnEdit.setText(StringResource.getStringResource("miufopublic280"));
			m_btnEdit.addActionListener(this);
		}
		return m_btnEdit;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDelete() {
		if (m_btnDelete == null) {
			m_btnDelete = new nc.ui.pub.beans.UIButton();
			m_btnDelete.setBounds(474, 140, 75, 22);
			m_btnDelete.setText(StringResource.getStringResource("miufo1000930"));
			m_btnDelete.addActionListener(this);
		}
		return m_btnDelete;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonUp() {
		if (m_btnUp == null) {
			m_btnUp = new nc.ui.pub.beans.UIButton();
			m_btnUp.setBounds(474, 180, 75, 22);
			m_btnUp.setText(StringResource.getStringResource("miufo1001298"));
			m_btnUp.addActionListener(this);
		}
		return m_btnUp;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonDown() {
		if (m_btnDown == null) {
			m_btnDown = new nc.ui.pub.beans.UIButton();
			m_btnDown.setBounds(474, 220, 75, 22);
			m_btnDown.setText(StringResource.getStringResource("miufo1001290"));
			m_btnDown.addActionListener(this);
		}
		return m_btnDown;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonClose() {
		if (m_btnClose == null) {
			m_btnClose = new nc.ui.pub.beans.UIButton();
			m_btnClose.setBounds(474, 260, 75, 22);
			m_btnClose.setText(StringResource.getStringResource("miufo1000064"));
			m_btnClose.addActionListener(this);
		}
		return m_btnClose;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonFilter() {
		if (m_btnFilter == null) {
			m_btnFilter = new nc.ui.pub.beans.UIButton();
			m_btnFilter.setBounds(15, 25, 75, 22);
			m_btnFilter.setText(StringResource.getStringResource("ubimultidim0019"));
			m_btnFilter.addActionListener(this);
		}
		return m_btnFilter;
	}

	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonLimitRows() {
		if (m_btnLimitRows == null) {
			m_btnLimitRows = new nc.ui.pub.beans.UIButton();
			m_btnLimitRows.setBounds(122, 25, 75, 22);
			m_btnLimitRows.setText(StringResource.getStringResource("ubimultidim0021"));
			m_btnLimitRows.addActionListener(this);
		}
		return m_btnLimitRows;
	}

	/**
	 * This method initializes jButton5
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSort() {
		if (m_btnSort == null) {
			m_btnSort = new nc.ui.pub.beans.UIButton();
			m_btnSort.setBounds(229, 25, 75, 22);
			m_btnSort.setText(StringResource.getStringResource("ubimultidim0020"));
			m_btnSort.addActionListener(this);
		}
		return m_btnSort;
	}

	/**
	 * This method initializes jButton6
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonHidden() {
		if (m_btnHidden == null) {
			m_btnHidden = new nc.ui.pub.beans.UIButton();
			m_btnHidden.setBounds(443, 25, 75, 22);
			m_btnHidden.setText(StringResource.getStringResource("ubimultidim0022"));
			m_btnHidden.addActionListener(this);
		}
		return m_btnHidden;
	}

	/**
	 * This method initializes jButton7
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonFormula() {
		if (m_btnFormula == null) {
			m_btnFormula = new nc.ui.pub.beans.UIButton();
			m_btnFormula.setBounds(336, 25, 75, 22);
			m_btnFormula.setText(StringResource.getStringResource("ubimultidim0023"));
			m_btnFormula.addActionListener(this);
		}
		return m_btnFormula;
	}

	public AnalyzerSet getAnalyzerSet() {
		if (m_analyzerModel == null) {
			m_analyzerModel = new AnalyzerSet();
		}
		return m_analyzerModel;
	}

	/**
	 * 编辑一个分析设置，如果修改并确定，则返回新的设定；否则为空
	 * 
	 * @param analyzerType
	 * @param oldAnalyzer
	 * @return
	 */
	IAnalyzerSet editAnalyzer(int analyzerType, IAnalyzerSet oldAnalyzer) {

		IAnalyzerDialog editDlg = getEditor(analyzerType);
		editDlg.setAnalyzer(oldAnalyzer);
		if (editDlg.showModel(this) == UIDialog.ID_OK) {
			int changeType = (oldAnalyzer == null) ? IAnalyzerDialog.ADD : IAnalyzerDialog.EDIT;

			boolean canBeEdit = editDlg.beforeChanged(changeType, analyzerType, oldAnalyzer);
			if (canBeEdit)
				return editDlg.getAnalyzerSet();
		}
		return null;
	}

	void afterEditAnalyzer(int changeType, int analyzerType, IAnalyzerSet oldAnalyzer) {

		IAnalyzerDialog editDlg = getEditor(analyzerType);
		editDlg.setAnalyzer(oldAnalyzer);
		IAnalyzerSet newAnalyzer = m_analyzerModel.getAnalyzer(analyzerType, oldAnalyzer.getID());
		editDlg.afterChanged(changeType, analyzerType, oldAnalyzer, newAnalyzer);
	}

	private IAnalyzerDialog getEditor(int analyzerType) {
		if (m_editors[analyzerType] == null) {
			IAnalyzerDialog editor = null;
			switch (analyzerType) {
			case IAnalyzerSet.TYPE_FILTER:
				editor = new FilterEditor();
				break;
			case IAnalyzerSet.TYPE_SORT:
				editor = new SortEditor();
				break;
			case IAnalyzerSet.TYPE_LIMITROWS:
				editor = new LimitEditor();
				break;
			case IAnalyzerSet.TYPE_FORMULAR:
				editor = new FormulaEditor();
				break;
			case IAnalyzerSet.TYPE_HIDDEN:
				editor = new HiddenEditor(this, m_seldimModel);
				break;
			default:
				break;
			}
			m_editors[analyzerType] = editor;
		}
		return m_editors[analyzerType];

	}

	public void setCurrentInfo(String pk_dim, int combine_pos) {
		m_pk_dimdef = pk_dim;
		m_combine_pos = combine_pos;
		getTableModel().fireTableDataChanged();
		if (getTableModel().getRowCount() > 0)
			getJTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	/**
	 * 根据当前所处位置，返回行/列的维度成员组合 将来应该做成缓存
	 * 
	 * @return
	 * @i18n mbimulti00017=请先设置行/列维度和成员
	 */
	private DimMemberCombinationVO[] getFields(int iPos) {
		DimMemberCombinationVO[] fields = null;
		if (/* m_combine_pos */iPos == IMultiDimConst.COMBINE_COLUMN) {
			fields = MultiDimensionUtil.getAllCombination(m_seldimModel.getSelDimVOs(IMultiDimConst.POS_COLUMN));
		} else if (/* m_combine_pos */iPos == IMultiDimConst.COMBINE_ROW) {
			fields = MultiDimensionUtil.getAllCombination(m_seldimModel.getSelDimVOs(IMultiDimConst.POS_ROW));
		}
		if (fields == null) {
			MessageDialog.showHintDlg(this, null, StringResource.getStringResource("mbimulti00017"));
		}
		return fields;

	}

	public void propertyChange(PropertyChangeEvent evt) {
		// 处理行列维度的变换事件
		if (evt.getPropertyName().equals(IMultiDimConst.PROPERTY_SELDIM_CHANGED)) {
			m_seldimModel = (SelDimModel) evt.getNewValue();
			for (int i = 0; i < m_editors.length; i++) {
				getEditor(i).processMemberChanged((SelDimModel) evt.getOldValue(), m_seldimModel);
			}
		}
	}

	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0) {
			int index = e.getFirstRow();
			if (index >= 0 && index < m_analyzerModel.getAllAnalyzer().length) {
				IAnalyzerSet analyzer = m_analyzerModel.getAnalyzer(index);
				IAnalyzerDialog editDlg = getEditor(analyzer.getAnalyzerType());
				editDlg.afterChanged(IAnalyzerDialog.ENABLE, analyzer.getAnalyzerType(), null, analyzer);
			}
			processSelectIndex(index);
		}

	}

	private void processSelectIndex(int index) {
		getJButtonUp().setEnabled(index > 0);
		getJButtonDown().setEnabled(index >= 0 && (index < getJTable().getModel().getRowCount() - 1));

		getJButtonDelete().setEnabled(index >= 0);

		IAnalyzerSet analyzer = m_analyzerModel.getAnalyzer(index);
		if (analyzer != null && analyzer.isValid())
			getJButtonEdit().setEnabled(true);
		else
			getJButtonEdit().setEnabled(false);

	}
} // @jve:decl-index=0:visual-constraint="10,10"
