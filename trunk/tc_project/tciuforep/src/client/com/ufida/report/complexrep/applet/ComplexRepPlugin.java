package com.ufida.report.complexrep.applet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.complexrep.model.ComplexRepModel;
import com.ufida.report.complexrep.model.SplitUnitModel;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.applet.BIReportApplet;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author zzl 2005-5-9
 */
public class ComplexRepPlugin extends BIPlugIn{
	/** 添加位置 */
	public static final String LEFT = JSplitPane.LEFT;

	public static final String RIGHT = JSplitPane.RIGHT;

	public static final String TOP = JSplitPane.TOP;

	public static final String BOTTOM = JSplitPane.BOTTOM;

	private UfoReport focusSubReport = null;

	// private transient PropertyChangeSupport support = new
	// PropertyChangeSupport(this);

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
		int initState = getReport().getOperationState();

		getReport().getReportNavPanel().setMidComp(
				ComplexRepMainPane.getInstance());
		setModel(getModel());
		// addSubReport(report,null);
		
		getReport().resetGlobalPopMenuSupport();
		FocusManager.getCurrentManager().addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("focusOwner")) {
							Component focusComp = ((Component) evt
									.getNewValue());
							while (focusComp != null) {
								if (focusComp != getReport()
										&& focusComp instanceof UfoReport) {
									if (focusComp == focusSubReport) {
										break;
									}
//									UfoReport oldValue = focusSubReport;
									focusSubReport = (UfoReport) focusComp;
									// support.firePropertyChange(
									// "focusSubReportChanged", oldValue,
									// focusSubReport);
									break;
								}
								focusComp = focusComp.getParent();
							}
						}
					}
				});
		/** 模型状态修改通知报告组件 */
		getModel().addPropertyChangeListener(ReportResource.OPERATE_TYPE,
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						getReport().setOperationState(
								getModel().getOperationState());
					}

				});

		setOperationState(initState);
	}

	private void setModel(ComplexRepModel model) {
		Object subRepModel = model.getSubRepModel();
		ComplexRepMainPane.getInstance().removeAll();
		if (subRepModel != null) {
			ComplexRepMainPane.getInstance().add(getCompByModel(subRepModel));
		}
	}

	public ComplexRepModel getModel() {
		BIContextVO context = (BIContextVO) getReport().getContextVo();
		ComplexRepModel model = (ComplexRepModel) (context.getBaseReportModel());
		if (model == null) {
			model = new ComplexRepModel(null);
			((BIContextVO) getReport().getContextVo()).setBaseReportModel(model);
		}
		

		return model;
	}

	public BIContextVO getBIContextVO() {
		return (BIContextVO) getReport().getContextVo();
	}

	// public synchronized void addFocusSubReportChangeListener(
	// PropertyChangeListener listener) {
	// support.addPropertyChangeListener("focusSubReportChanged", listener);
	// }

	// public synchronized void removeFocusSubReportChangeListener(
	// PropertyChangeListener listener) {
	// support.removePropertyChangeListener("focusSubReportChanged", listener);
	// }

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#store()
	 */
	/**
	 * @i18n miufo00402=保存的复合报表不存在，无法保存
	 */
	public void store() {
		ReportSrv srv = new ReportSrv();
		ReportVO vo = null;
		String pk_report = getBIContextVO().getReportPK();
		vo = ((ReportVO) (srv.getByIDs(new String[] { pk_report }))[0]);

		if (vo != null) {
			vo.setDefinition(getModel());
			srv.update(new ReportVO[] { vo });
		} else {
			AppDebug.debug("保存的复合报表不存在，无法保存");// @devTools
												// System.out.println("保存的复合报表不存在，无法保存");
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		return new ComplexRepDesc(this);
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		return null;
	}

	/*
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
	}

	/*
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}

	/**
	 * 格式设计时,右键显示为报告的右键 数据浏览时,右键显示为各子报表的右键
	 * 
	 * @param operationState
	 *            void
	 */
	public void setOperationState(int operationState) {
		/** 模型改变通知组件状态改变,即ComplexRepModel通知UfoReport改变 */
		getModel().setOperationState(operationState,
				((BIContextVO) getReport().getContextVo()).getReportPK(),
				((BIContextVO) getReport().getContextVo()).getCurUserID());
		/**
		 * ComplexRepModel已经通知各个子报表模型改变, 这里只需修改组件改变
		 */
		UfoReport[] reports = getSubReports();
		if (operationState == UfoReport.OPERATION_FORMAT) {
			getReport().resetGlobalPopMenuSupport();
			if (reports != null)
				for (int i = 0; i < reports.length; i++) {
					reports[i].removeGlobalPopMenuSupport();
					reports[i].setOperationState(operationState);
				}
		} else {
			getReport().removeGlobalPopMenuSupport();
			if (reports != null)
				for (int i = 0; i < reports.length; i++) {
					reports[i].resetGlobalPopMenuSupport();
					reports[i].setOperationState(operationState);
				}
		}

		if (operationState == UfoReport.OPERATION_INPUT) {
			// 数据浏览状态，增加复合报表模型为子报表的维度成员变化属性的监听器.
			addsubReportLinkListener(getModel());
		} else {
			// 格式状态，删除复合报表模型为子报表的维度成员变化属性的监听器.
			getModel().removeSubRepPropListener(
					BaseReportModel.PROPERTY_DIMVALUE_CHANGE);
		}
	}

	/**
	 * 增加复合报表模型为子报表的维度成员变化属性的监听器.
	 * 
	 * @param model
	 */
	public static void addsubReportLinkListener(ComplexRepModel model) {
		model.addSubRepPropListener(BaseReportModel.PROPERTY_DIMVALUE_CHANGE);
	}

	public int getOperationState() {
		return getModel().getOperationState();
	}

	private UfoReport[] getSubReports() {
		Vector<Component> vec = new Vector<Component>();
		if (ComplexRepMainPane.getInstance().getComponents().length > 0) {
			Component comp = ComplexRepMainPane.getInstance().getComponent(0);
			getSubReportImpl(vec, comp);
		}
		return (UfoReport[]) vec.toArray(new UfoReport[0]);
	}

	private void getSubReportImpl(Vector<Component> vec, Component comp) {
		if (comp instanceof UfoReport) {
			vec.add(comp);
		} else if (comp instanceof ComplexRepSplitPane) {
			getSubReportImpl(vec, ((ComplexRepSplitPane) comp)
					.getLeftComponent());
			getSubReportImpl(vec, ((ComplexRepSplitPane) comp)
					.getRightComponent());
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 添加子报表 void
	 */
	public void addSubReport(UfoReport posReport,
			BaseReportModel subReportModel, String pos) {
		// subReport.removeGlobalPopMenuSupport();
		addSubReportImpl(posReport, subReportModel, pos);
		getReport().resetGlobalPopMenuSupport();
	}

	private void addSubReportImpl(UfoReport posReport,
			BaseReportModel subReportModel, String pos) {
		if (posReport == null) {
			if (ComplexRepMainPane.getInstance().getComponents().length == 0) {
				ComplexRepMainPane.getInstance().add(
						getReportByModel(subReportModel));
				getModel().setSubRepModel(subReportModel);
			} else {
				throw new IllegalArgumentException(StringResource
						.getStringResource("mbicomplex00004")/** 没有选中添加的子报表位置 */
				);
			}
		} else {
			if (!(posReport.getParent() instanceof ComplexRepSplitPane)
					&& !(posReport.getParent() instanceof ComplexRepMainPane)) {
				throw new IllegalArgumentException(StringResource
						.getStringResource("mbicomplex00005")/** 当前焦点UfoReport不在复杂报告中！ */
				);
			}

			SplitUnitModel splitModel = new SplitUnitModel();
			if (pos.equals(LEFT) || pos.equals(RIGHT)) {
				splitModel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			} else if (pos.equals(TOP) || pos.equals(BOTTOM)) {
				splitModel.setOrientation(JSplitPane.VERTICAL_SPLIT);
			} else {
				throw new IllegalArgumentException();
			}

			if (pos.equals(LEFT) || pos.equals(TOP)) {
				splitModel.setLeftModel(subReportModel);
				splitModel.setRightModel(((BIContextVO) posReport
						.getContextVo()).getBaseReportModel());
			} else if (pos.equals(RIGHT) || pos.equals(BOTTOM)) {
				splitModel.setRightModel(subReportModel);
				splitModel
						.setLeftModel(((BIContextVO) posReport.getContextVo())
								.getBaseReportModel());
			} else {
				throw new IllegalArgumentException();
			}
			ComplexRepSplitPane newSplitPane = new ComplexRepSplitPane(
					splitModel);
			replaceCompWith(posReport, newSplitPane);
			newSplitPane.initPane();
		}
		getReport().getReportNavPanel().revalidate();
		getReport().getReportNavPanel().repaint();
	}

	public BaseReportModel getFocusModel() {
		BaseReportModel model = null;
		if (focusSubReport != null) {
			Object objModel = getModelByComp(focusSubReport);
			if (objModel != null && objModel instanceof BaseReportModel) {
				model = (BaseReportModel) objModel;
			}
		}
		return model;
	}

	public void delSubReport() {
		if (focusSubReport == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("mbicomplex00006")/** 没有选中子报表！ */
			);
		}
		Container parent = focusSubReport.getParent();
		UfoReport toDelRep = focusSubReport;
		focusSubReport = null;
		if (parent instanceof ComplexRepSplitPane) {
			ComplexRepSplitPane splitPane = (ComplexRepSplitPane) parent;
			Component anotherComp = splitPane.getLeftComponent() == toDelRep ? splitPane
					.getRightComponent()
					: splitPane.getLeftComponent();
			replaceCompWith(splitPane, anotherComp);
		} else if (parent instanceof ComplexRepMainPane) {
			parent.remove(toDelRep);
			getModel().setSubRepModel(null);
		} else {
			throw new IllegalArgumentException(StringResource
					.getStringResource("mbicomplex00007")/** 当前报表不在复杂报告中 */
			);
		}
		htSubReport.remove(getModelByComp(toDelRep));

		getReport().getReportNavPanel().revalidate();
		getReport().getReportNavPanel().repaint();
	}

	private void replaceCompWith(Component oldComp, Component newComp) {
		if (oldComp.getParent() == ComplexRepMainPane.getInstance()) {
			getModel().setSubRepModel(getModelByComp(newComp));
			ComplexRepMainPane container = (ComplexRepMainPane) oldComp
					.getParent();
			container.remove(oldComp);
			container.add(newComp);
		} else if (oldComp.getParent() instanceof ComplexRepSplitPane) {
			ComplexRepSplitPane container = (ComplexRepSplitPane) oldComp
					.getParent();
			if (container.getLeftComponent() == oldComp) {
				container.setLeftComponent(newComp);
			} else if (container.getRightComponent() == oldComp) {
				container.setRightComponent(newComp);
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void resetFocusSubRepProp() {
		if (getFocusSubReport() == null)
			return;

		BaseReportModel baseModel = ((BIContextVO) ((UfoReport) getFocusSubReport())
				.getContextVo()).getBaseReportModel();
		boolean bShowPageDim = true;
		boolean bShowRowCol = false;
		if (baseModel != null && baseModel.getProperty() != null) {
			bShowPageDim = baseModel.getProperty().isDispalyPageDim();
			bShowRowCol = baseModel.getProperty().isDisplayRowCol();
		}
		getFocusSubReport().setTableShow(bShowPageDim, bShowRowCol, false);

	}

	public UfoReport getFocusSubReport() {
		return focusSubReport;
	}

	@SuppressWarnings("serial")
	class ComplexRepSplitPane extends nc.ui.pub.beans.UISplitPane {
		private SplitUnitModel m_model;

		private ComplexRepSplitPane(SplitUnitModel model) {
			setUI(new BasicSplitPaneUI() {
				protected void finishDraggingTo(int location) {
					super.finishDraggingTo(location);
					computeModelScale();
				}
			});
			m_model = model;
			setOrientation(m_model.getOrientation());
			setScale(m_model.getScale());
		}

		private void computeModelScale() {
			int leftSize = getDividerLocation()
					- (isHorSplit() ? getInsets().left : getInsets().top);
			int rightSize = (isHorSplit() ? getSize().width : getSize().height)
					- (isHorSplit() ? getInsets().right : getInsets().bottom)
					- getDividerSize() - getDividerLocation();
			setScale(1.0 * leftSize / (leftSize + rightSize));
		}

		private void initPane() {
			setLeftComponent(getCompByModel(m_model.getLeftModel()));
			setRightComponent(getCompByModel(m_model.getRightModel()));
		}

		private boolean isHorSplit() {
			return getOrientation() == HORIZONTAL_SPLIT;
		}

		public double getScale() {
			return m_model.getScale();
		}

		public void setScale(double newScale) {
			m_model.setScale(newScale);
			setResizeWeight(m_model.getScale());
			// setDividerLocation(m_model.getScale());
		}

		/*
		 * @see javax.swing.JSplitPane#setOrientation(int)
		 */
		public void setOrientation(int orientation) {
			m_model.setOrientation(orientation);
			super.setOrientation(orientation);
		}

		/*
		 * @see javax.swing.JSplitPane#setLeftComponent(java.awt.Component)
		 */
		public void setLeftComponent(Component comp) {
			// JSplitPane初始化时会生成JButton对象.
			if (!(comp instanceof JButton)) {
				getModel().setLeftModel(getModelByComp(comp));
			}
			super.setLeftComponent(comp);
		}

		/*
		 * @see javax.swing.JSplitPane#setRightComponent(java.awt.Component)
		 */
		public void setRightComponent(Component comp) {
			if (!(comp instanceof JButton)) {
				getModel().setRightModel(getModelByComp(comp));
			}
			super.setRightComponent(comp);
		}

		public SplitUnitModel getModel() {
			return m_model;
		}
	}

	@SuppressWarnings("serial")
	static class ComplexRepMainPane extends nc.ui.pub.beans.UIPanel {
		private static ComplexRepMainPane m_instance;

		private ComplexRepMainPane() {
			super(new BorderLayout());
			// add(comp,BorderLayout.CENTER);
		}

		public static ComplexRepMainPane getInstance() {
			if (m_instance == null) {
				m_instance = new ComplexRepMainPane();
			}
			return m_instance;
		}
	}

	/** ***新建或者得到UfoReport********* */
	private Hashtable<BaseReportModel, UfoReport> htSubReport = new Hashtable<BaseReportModel, UfoReport>();

	private UfoReport getReportByModel(BaseReportModel model) {
		if (htSubReport.keySet().contains(model)) {
			return (UfoReport) htSubReport.get(model);
		}
		BIContextVO vo = new BIContextVO();
		vo.setCurUserID(getBIContextVO().getCurUserID());
		vo.setBaseReportModel((BaseReportModel) model);
		UfoReport rep = new UfoReport(getOperationState(), vo);
		BIReportApplet.initPlugins(rep, ((BaseReportModel) model)
				.getReportType().intValue());

		boolean bShowPageDim = true;
		boolean bShowRowCol = false;
		if (model.getProperty() != null) {
			bShowPageDim = model.getProperty().isDispalyPageDim();
			bShowRowCol = model.getProperty().isDisplayRowCol();
		}

		rep.setTableShow(bShowPageDim, bShowRowCol, true);
		htSubReport.put(model, rep);
		return rep;
	}

	private Component getCompByModel(Object model) {
		Component comp;
		if (model instanceof BaseReportModel) {
			comp = getReportByModel((BaseReportModel) model);
		} else if (model instanceof SplitUnitModel) {
			comp = new ComplexRepSplitPane((SplitUnitModel) model);
			((ComplexRepSplitPane) comp).initPane();
		} else {
			throw new IllegalArgumentException();
		}
		return comp;
	}

	private Object getModelByComp(Component comp) {
		if (comp instanceof ComplexRepSplitPane) {
			return ((ComplexRepSplitPane) comp).getModel();
		} else if (comp instanceof UfoReport) {
			return ((BIContextVO) ((UfoReport) comp).getContextVo())
					.getBaseReportModel();
		} else {
			throw new IllegalArgumentException();
		}
	}
	// /**得到Applet运行时的参数.*/
	// public String getAppletParam(String name){
	// Component com = FocusManager.getCurrentManager().getFocusOwner();
	// while(com != null){
	// if(com instanceof JApplet){
	// break;
	// }else{
	// com = com.getParent();
	// }
	// }
	// if(com != null){
	// return ((JApplet) com).getParameter(name);
	// }else{
	// return "";
	// }
	// }
}  