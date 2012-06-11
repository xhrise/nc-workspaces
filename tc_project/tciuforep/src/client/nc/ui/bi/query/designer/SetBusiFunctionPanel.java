/*
 * 创建日期 2005-7-1
 *
 */
package nc.ui.bi.query.designer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BusiFuncClassVO;
import nc.vo.bi.query.manager.IBusiFunction;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 业务函数设置界面
 */
public class SetBusiFunctionPanel extends AbstractQueryDesignSetPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0050";//"业务函数";

	//业务函数类接口
	private IBusiFunction m_iBusiFunc = null;

	private UILabel ivjLabelClass = null;

	private UIRefPane ivjRefPnClass = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	/**
	 *  
	 */
	public SetBusiFunctionPanel() {
		super();
		initialize();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//获得类名
		String classname = getClassName();
		//构造VO
		BusiFuncClassVO vo = new BusiFuncClassVO();
		vo.setClassname(classname);
		//
		qmd.getAdvModel().setBusifunc(vo);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		//获得类名
		BusiFuncClassVO vo = qmd.getAdvModel().getBusifunc();
		if (vo != null) {
			getRefPnClass().setPK(vo.getClassname());
			updateIBusiFunc();
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#check()
	 */
	public String check() {
		return m_iBusiFunc.check(null);
	}

	class IvjEventHandler implements ValueChangedListener {
		public void valueChanged(ValueChangedEvent event) {
			if (event.getSource() == SetBusiFunctionPanel.this.getRefPnClass())
				connEtoC1(event);
		};
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 * @i18n miufo00155=--------- 未捕捉到的异常 ---------
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getRefPnClass().addValueChangedListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetBusiFunctionPanel");
			setLayout(new GridBagLayout());
			setSize(400, 240);

			GridBagConstraints constraintsRefPnClass = new GridBagConstraints();
			constraintsRefPnClass.gridx = 2;
			constraintsRefPnClass.gridy = 1;
			constraintsRefPnClass.fill = GridBagConstraints.BOTH;
			constraintsRefPnClass.weightx = 1.0;
			constraintsRefPnClass.weighty = 1.0;
			constraintsRefPnClass.insets = new Insets(109, 3, 109, 68);
			add(getRefPnClass(), constraintsRefPnClass);

			GridBagConstraints constraintsLabelClass = new GridBagConstraints();
			constraintsLabelClass.gridx = 1;
			constraintsLabelClass.gridy = 1;
			constraintsLabelClass.ipadx = 8;
			constraintsLabelClass.insets = new Insets(109, 66, 109, 3);
			add(getLabelClass(), constraintsLabelClass);
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		BusiFunctionModelRef refModel = new BusiFunctionModelRef();
		getRefPnClass().setRefModel(refModel);
		getRefPnClass().setButtonFireEvent(true);
		// user code end
	}

	/**
	 * 获得函数类名
	 */
	public String getClassName() {
		return getRefPnClass().getRefCode();
	}

	/**
	 * 参照值改变事件响应
	 */
	public void refPnClass_ValueChanged(ValueChangedEvent event) {
		String classname = getClassName();
		if (classname != null && !classname.trim().equals("")) {
			try {
				m_iBusiFunc = (IBusiFunction) Class.forName(classname)
						.newInstance();
			} catch (Exception e) {
				AppDebug.debug(e);
				MessageDialog.showWarningDlg(this, "UFBI", "Class Error");
			}
		}
	}

	/**
	 * connEtoC1: (RefPnClass.valueChanged.valueChanged(ValueChangedEvent) -->
	 * SetBusiFunctionPanel.refPnClass_ValueChanged(LValueChangedEvent;)V)
	 * 
	 * @param arg1
	 *            ValueChangedEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ValueChangedEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.refPnClass_ValueChanged(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 返回 LabelClass 特性值。
	 * 
	 * @return UILabel
	 * @i18n miufo00327=函数类名
	 */
	/* 警告：此方法将重新生成。 */
	private UILabel getLabelClass() {
		if (ivjLabelClass == null) {
			try {
				ivjLabelClass = new UILabel();
				ivjLabelClass.setName("LabelClass");
				ivjLabelClass.setText(StringResource.getStringResource("miufo00327"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelClass;
	}

	/**
	 * 返回 RefPnClass 特性值。
	 * 
	 * @return UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIRefPane getRefPnClass() {
		if (ivjRefPnClass == null) {
			try {
				ivjRefPnClass = new UIRefPane();
				ivjRefPnClass.setName("RefPnClass");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRefPnClass;
	}

	/**
	 * 获得函数类接口
	 */
	public IBusiFunction getIBusiFunc() {
		return m_iBusiFunc;
	}

	/**
	 * 更新函数类接口
	 */
	public void updateIBusiFunc() {
		String classname = getClassName();
		if (classname != null && !classname.trim().equals("")) {
			try {
				m_iBusiFunc = (IBusiFunction) Class.forName(classname)
						.newInstance();
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
	}

	/**
	 * 初始化参照
	 */
	public void initUI() {
		//设置参照模型
		String dsNameForDef = getTabPn().getDefDsName();
		getRefPnClass().getRefModel().setDataSource(dsNameForDef);
	}
}  