/*
 * �������� 2005-7-1
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
 * ҵ�������ý���
 */
public class SetBusiFunctionPanel extends AbstractQueryDesignSetPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0050";//"ҵ����";

	//ҵ������ӿ�
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//�������
		String classname = getClassName();
		//����VO
		BusiFuncClassVO vo = new BusiFuncClassVO();
		vo.setClassname(classname);
		//
		qmd.getAdvModel().setBusifunc(vo);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		//�������
		BusiFuncClassVO vo = qmd.getAdvModel().getBusifunc();
		if (vo != null) {
			getRefPnClass().setPK(vo.getClassname());
			updateIBusiFunc();
		}
	}

	/*
	 * ���� Javadoc��
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 * @i18n miufo00155=--------- δ��׽�����쳣 ---------
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getRefPnClass().addValueChangedListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ú�������
	 */
	public String getClassName() {
		return getRefPnClass().getRefCode();
	}

	/**
	 * ����ֵ�ı��¼���Ӧ
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
	/* ���棺�˷������������ɡ� */
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
	 * ���� LabelClass ����ֵ��
	 * 
	 * @return UILabel
	 * @i18n miufo00327=��������
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� RefPnClass ����ֵ��
	 * 
	 * @return UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ú�����ӿ�
	 */
	public IBusiFunction getIBusiFunc() {
		return m_iBusiFunc;
	}

	/**
	 * ���º�����ӿ�
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
	 * ��ʼ������
	 */
	public void initUI() {
		//���ò���ģ��
		String dsNameForDef = getTabPn().getDefDsName();
		getRefPnClass().getRefModel().setDataSource(dsNameForDef);
	}
}  