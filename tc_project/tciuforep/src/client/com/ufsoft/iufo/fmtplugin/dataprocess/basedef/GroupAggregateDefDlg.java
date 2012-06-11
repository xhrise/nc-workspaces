package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UISeparator;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.script.extfunc.GAggrFuncDriver;
/**
 @update 2003-11-12 16:52 liulp
  1)�½�ʱ��������ѡ״̬Ӧ�ø��ݱ�ѡ���һ����Ŀ����
  2)ѡ����Ŀ�仯֮��Ӧ�øı亯����ѡ�Ͳ���ѡ��״̬
 @end
 @update 2003-10-31 09:58 liulp
  ���Ӱ�������
 @end
 @update
   2003-10-31 09:39 liulp
    ʹ��MapName��Ϊ�������Ա�֤Ψһ
 @end
 * ˵��:���������öԻ���	
 * ���ߣ�������
 * �������ڣ�(2003-4-2 19:28:13)
 * �汾��
 */

public class GroupAggregateDefDlg
    extends com.ufsoft.report.dialog.UfoDialog
    implements java.awt.event.ActionListener, java.awt.event.ItemListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8046947744375269986L;
	/**
     * ���е����ݴ����ֶ�ʸ��,Ԫ��ΪDataProcessFld
     */
    private Vector m_vecAllDynAreaDPFld = null;
    /**
     * ����ͳ�ƺ�������
     */
    private GroupFormulaVO m_oGroupFormulaVO = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private javax.swing.JPanel ivjJPanel2 = null;
    private javax.swing.JSeparator ivjJSeparator1 = null;
    private javax.swing.JComboBox ivjJComboBoxProject = null;
    private javax.swing.JLabel ivjJLabel11 = null;
    private javax.swing.JPanel ivjJPanelItem = null;
    private javax.swing.JRadioButton ivjJRadioButtonAverage = null;
    private javax.swing.JRadioButton ivjJRadioButtonCount = null;
    private javax.swing.JRadioButton ivjJRadioButtonMax = null;
    private javax.swing.JRadioButton ivjJRadioButtonMin = null;
    private javax.swing.JRadioButton ivjJRadioButtonSum = null;
    private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOk = null;
    private javax.swing.JRadioButton ivjJRadioButtonNone = null;
    String[] funcnames =
        {
            GAggrFuncDriver.GCOUNT,
            GAggrFuncDriver.GSUM,
            GAggrFuncDriver.GMAX,
            GAggrFuncDriver.GMIN,
            GAggrFuncDriver.GAVG,
            FUNCNAME_NONE };
    public static final String FUNCNAME_NONE = "funcname_none";
    /**
     * ָ��PK��ϵhash<String, Integer>:(mapPK,new Integer(measType))
     */
    private Hashtable<String, Integer> m_hashMeasTypes = null;
/**
 * BQueryPropertyDlg ������ע�⡣
 */
public GroupAggregateDefDlg() {
	super();
	initialize();
}
/**
 * BQueryPropertyDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public GroupAggregateDefDlg(java.awt.Container parent) {
	super(parent);
	initialize();	
}
/**
 * �˴����뷽��������
 * 
 * �������ڣ�(2003-8-18 10:07:51)
 * @author������Ƽ
 * @param parent java.awt.Container
 * @param vecAllDynAreaMeasDPFld java.util.Vector - ��̬���������ָ�����͵����ݴ����ֶ�ʸ����Ԫ��ΪDataProcessFld
 * @param hashMeasTypes - ָ����������ͣ�measPK,new Integer(meastype)��
 * @param groupFormulaVO com.ufsoft.iuforeport.reporttool.process.basedef.GroupFormulaVO
 */
public GroupAggregateDefDlg(
    java.awt.Container parent,
    Vector vecAllDynAreaMeasDPFld,
    Hashtable<String, Integer> hashMeasTypes,
    GroupFormulaVO groupFormulaVO) {

    super(parent);

    m_vecAllDynAreaDPFld = vecAllDynAreaMeasDPFld;

    m_hashMeasTypes = hashMeasTypes;

    m_oGroupFormulaVO = groupFormulaVO;

    initialize();
}
/**
 * actionPerformed ����ע�⡣
 */
public void actionPerformed(ActionEvent e) {
    //ָ��ѡ�����仯��У����Щ����Radio����ʹ��
    if (e.getSource() == getJComboBoxProject()) {
        FieldMap fieldMap = (FieldMap) getJComboBoxProject().getSelectedItem();
        Integer nMeasType = (Integer) m_hashMeasTypes.get(fieldMap.getMapPK());
        if (nMeasType != null) {
            enableFuncRadioBtns(nMeasType.intValue());
        }

    }
    //ȷ��
    else
        if (e.getSource() == getJBtnOk()) {

            String funcname = null;

            if (getJRadioButtonCount().isSelected()) {
                funcname = funcnames[0];

            } else
                if (getJRadioButtonSum().isSelected()) {
                    funcname = funcnames[1];

                } else
                    if (getJRadioButtonMax().isSelected()) {
                        funcname = funcnames[2];

                    } else
                        if (getJRadioButtonMin().isSelected()) {
                            funcname = funcnames[3];

                        } else
                            if (getJRadioButtonAverage().isSelected()) {
                                funcname = funcnames[4];

                            } else
                                if (getJRadioButtonNone().isSelected()) {
                                    funcname = funcnames[5];
                                }

            m_oGroupFormulaVO.setFormulaContent(
                funcname
                    + "(\'"
                    + ((FieldMap) getJComboBoxProject().getSelectedItem()).getMapPK()
                    + "\')");

            setResult(ID_OK);
            close();
        }
    //ȡ��
    else
        if (e.getSource() == getJBtnCancel()) {
            setResult(ID_CANCEL);
            close();
        }
}
/**
 * ���Ӱ�����
 * 
 * �������ڣ�(2003-10-31 09:56:54)
 * �����ߣ�����Ƽ
 */
private void addHelp() {
    javax.help.HelpBroker hb =  ResConst.getHelpBroker();
    if (hb == null)
        return;
    hb.enableHelpKey(getContentPane(), "TM_Data_Process_GroupFun", null);

}
/**
 * ����ѡ��ָ����������ú����Ŀ�ѡ״̬��
 * �������ڣ�(2003-9-27 9:25:10)
 * @author������Ƽ
 * @param nMeasType int
 */
private void enableFuncRadioBtns(int nMeasType) {
    getJRadioButtonNone().setEnabled(true);
    getJRadioButtonCount().setEnabled(true);

    boolean bEnable = true;
    //��ֵ���͵�ָ�꣬ƽ���������С����ͺ�������ѡ
    if (nMeasType != nc.vo.iufo.measure.MeasureVO.TYPE_NUMBER) {
        bEnable = false;
        getJRadioButtonCount().setSelected(true);
    }
    getJRadioButtonAverage().setEnabled(bEnable);
    getJRadioButtonMax().setEnabled(bEnable);
    getJRadioButtonMin().setEnabled(bEnable);
    getJRadioButtonSum().setEnabled(bEnable);
}
/**
 * �˴����뷽��������
 * 
 * �������ڣ�(2003-8-15 11:52:06)
 * @author������Ƽ
 * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupFormulaVO
 */
public GroupFormulaVO getGroupFormulaVO() {
	return m_oGroupFormulaVO;
}
/**
 * ���� JButton3 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnCancel() {
    if (ivjJBtnCancel == null) {
        try {
            ivjJBtnCancel = new UIButton();
            ivjJBtnCancel.setName("JBtnCancel");
 //           ivjJBtnCancel.setFont(new java.awt.Font("dialog", 0, 12));
            ivjJBtnCancel.setText("Cancel");
            ivjJBtnCancel.setBounds(255, 10, 80, 23);
            // user code begin {1}
            String strCancel = StringResource.getStringResource(StringResource.CANCEL);//ȡ��
            ivjJBtnCancel.setText(strCancel);
            ivjJBtnCancel.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnCancel;
}
/**
 * ���� JButton2 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnOk() {
    if (ivjJBtnOk == null) {
        try {
            ivjJBtnOk = new UIButton();
            ivjJBtnOk.setName("JBtnOk");
 //           ivjJBtnOk.setFont(new java.awt.Font("dialog", 0, 12));
            ivjJBtnOk.setText("OK");
            ivjJBtnOk.setBounds(154, 10, 80, 23);
            ivjJBtnOk.setActionCommand("OK");
            // user code begin {1}
            ivjJBtnOk.addActionListener(this);
            //ȷ��
            String strOK = StringResource.getStringResource(StringResource.OK);
            ivjJBtnOk.setText(strOK);
            ivjJBtnOk.setActionCommand(strOK);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnOk;
}
/**
 * ���� JComboBoxProject ����ֵ��
 * @return javax.swing.JComboBox
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JComboBox getJComboBoxProject() {
    if (ivjJComboBoxProject == null) {
        try {
            ivjJComboBoxProject = new UIComboBox();
            ivjJComboBoxProject.setName("JComboBoxProject");
            ivjJComboBoxProject.setBounds(119, 22, 157, 25);
            // user code begin {1}
            ivjJComboBoxProject.addItemListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJComboBoxProject;
}
/**
 * ���� JLabel11 ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabel11() {
	if (ivjJLabel11 == null) {
		try {
			ivjJLabel11 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabel11.setName("JLabel11");
	//		ivjJLabel11.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabel11.setText("Item:");
			ivjJLabel11.setBounds(47, 28, 66, 16);
			ivjJLabel11.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strItem =StringResource.getStringResource("miufo1001282");  //"��Ŀ:"
			ivjJLabel11.setText(strItem);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel11;
}
/**
 * ���� JPanel1 ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getJPanel1() {
	if (ivjJPanel1 == null) {
		try {
			ivjJPanel1 = new UIPanel();
			ivjJPanel1.setName("JPanel1");
			ivjJPanel1.setLayout(null);
			ivjJPanel1.setBounds(4, 3, 387, 238);
			getJPanel1().add(getJLabel11(), getJLabel11().getName());
			getJPanel1().add(getJComboBoxProject(), getJComboBoxProject().getName());
			getJPanel1().add(getJPanelItem(), getJPanelItem().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanel1;
}
/**
 * ���� JPanel2 ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getJPanel2() {
	if (ivjJPanel2 == null) {
		try {
			ivjJPanel2 = new UIPanel();
			ivjJPanel2.setName("JPanel2");
			ivjJPanel2.setLayout(null);
			ivjJPanel2.setBounds(8, 259, 382, 44);
			getJPanel2().add(getJBtnOk(), getJBtnOk().getName());
			getJPanel2().add(getJBtnCancel(), getJBtnCancel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanel2;
}
/**
 * ���� JPanelItem ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getJPanelItem() {
	if (ivjJPanelItem == null) {
		try {
			ivjJPanelItem = new UIPanel();
			ivjJPanelItem.setName("JPanelItem");
			ivjJPanelItem.setLayout(null);
			ivjJPanelItem.setBounds(47, 68, 272, 137);
			getJPanelItem().add(getJRadioButtonCount(), getJRadioButtonCount().getName());
			getJPanelItem().add(getJRadioButtonSum(), getJRadioButtonSum().getName());
			getJPanelItem().add(getJRadioButtonMax(), getJRadioButtonMax().getName());
			getJPanelItem().add(getJRadioButtonMin(), getJRadioButtonMin().getName());
			getJPanelItem().add(getJRadioButtonAverage(), getJRadioButtonAverage().getName());
			getJPanelItem().add(getJRadioButtonNone(), getJRadioButtonNone().getName());
			// user code begin {1}
			String strCalType =StringResource.getStringResource("miufo1001283");   //"��������"
			getJPanelItem().setBorder(new TitledBorder(new EtchedBorder(),strCalType,TitledBorder.LEFT,TitledBorder.TOP,new java.awt.Font("dialog",0,12)));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanelItem;
}
/**
 * ���� JRadioButtonAverage ����ֵ��
 * @return javax.swing.JRadioButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JRadioButton getJRadioButtonAverage() {
	if (ivjJRadioButtonAverage == null) {
		try {
			ivjJRadioButtonAverage = new UIRadioButton();
			ivjJRadioButtonAverage.setName("JRadioButtonAverage");
	//		ivjJRadioButtonAverage.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJRadioButtonAverage.setText("Average");
			ivjJRadioButtonAverage.setBounds(150, 59, 95, 19);
			// user code begin {1}
			String strAverage = StringResource.getStringResource("miufo1001258");  //"ƽ��"
			ivjJRadioButtonAverage.setText(strAverage);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonAverage;
}
/**
 * ���� JRadioButtonCount ����ֵ��
 * @return javax.swing.JRadioButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JRadioButton getJRadioButtonCount() {
	if (ivjJRadioButtonCount == null) {
		try {
			ivjJRadioButtonCount = new UIRadioButton();
			ivjJRadioButtonCount.setName("JRadioButtonCount");
	//		ivjJRadioButtonCount.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJRadioButtonCount.setText("Count");
			ivjJRadioButtonCount.setBounds(38, 27, 95, 19);
			// user code begin {1}
			String strCount = StringResource.getStringResource("miufo1001255");  //"����"
			ivjJRadioButtonCount.setText(strCount);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonCount;
}
/**
 * ���� JRadioButtonMax ����ֵ��
 * @return javax.swing.JRadioButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JRadioButton getJRadioButtonMax() {
	if (ivjJRadioButtonMax == null) {
		try {
			ivjJRadioButtonMax = new UIRadioButton();
			ivjJRadioButtonMax.setName("JRadioButtonMax");
	//		ivjJRadioButtonMax.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJRadioButtonMax.setText("Max");
			ivjJRadioButtonMax.setBounds(38, 91, 95, 19);
			// user code begin {1}
			String strMax = StringResource.getStringResource("miufo1001256");  //"���"
			ivjJRadioButtonMax.setText(strMax);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMax;
}
/**
 * ���� JRadioButtonMin ����ֵ��
 * @return javax.swing.JRadioButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JRadioButton getJRadioButtonMin() {
	if (ivjJRadioButtonMin == null) {
		try {
			ivjJRadioButtonMin = new UIRadioButton();
			ivjJRadioButtonMin.setName("JRadioButtonMin");
	//		ivjJRadioButtonMin.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJRadioButtonMin.setText("Min");
			ivjJRadioButtonMin.setBounds(150, 29, 95, 19);
			// user code begin {1}
			String strMin = StringResource.getStringResource("miufo1001257");  //"��С"
			ivjJRadioButtonMin.setText(strMin);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMin;
}
/**
 * ���� JRadioButtonNone ����ֵ��
 * @return javax.swing.JRadioButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JRadioButton getJRadioButtonNone() {
	if (ivjJRadioButtonNone == null) {
		try {
			ivjJRadioButtonNone = new UIRadioButton();
			ivjJRadioButtonNone.setName("JRadioButtonNone");
	//		ivjJRadioButtonNone.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJRadioButtonNone.setText("None");
			ivjJRadioButtonNone.setBounds(150, 91, 95, 19);
			// user code begin {1}
			String strNone = StringResource.getStringResource("miufopublic358");  //"��"
			ivjJRadioButtonNone.setText(strNone);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonNone;
}
/**
 * ���� JRadioButtonSum ����ֵ��
 * @return javax.swing.JRadioButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JRadioButton getJRadioButtonSum() {
	if (ivjJRadioButtonSum == null) {
		try {
			ivjJRadioButtonSum = new UIRadioButton();
			ivjJRadioButtonSum.setName("JRadioButtonSum");
	//		ivjJRadioButtonSum.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJRadioButtonSum.setText("Sum");
			ivjJRadioButtonSum.setBounds(38, 59, 95, 19);
			// user code begin {1}
			String strSum = StringResource.getStringResource("miufopublic307");  //"�ϼ�"
			ivjJRadioButtonSum.setText(strSum);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonSum;
}
/**
 * ���� JSeparator1 ����ֵ��
 * @return javax.swing.JSeparator
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JSeparator getJSeparator1() {
	if (ivjJSeparator1 == null) {
		try {
			ivjJSeparator1 = new UISeparator();
			ivjJSeparator1.setName("JSeparator1");
			ivjJSeparator1.setBounds(6, 250, 387, 2);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJSeparator1;
}
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new UIPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
			ivjUfoDialogContentPane.setLayout(null);
			getUfoDialogContentPane().add(getJPanel1(), getJPanel1().getName());
			getUfoDialogContentPane().add(getJSeparator1(), getJSeparator1().getName());
			getUfoDialogContentPane().add(getJPanel2(), getJPanel2().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUfoDialogContentPane;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// System.out.println("--------- δ��׽�����쳣 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("GroupAggregateDefDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(401, 329);
        setTitle("");
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    //����ֻ�ܱ�ѡ��һ��
    javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
    group.add(getJRadioButtonCount());
    group.add(getJRadioButtonSum());
    group.add(getJRadioButtonMax());
    group.add(getJRadioButtonMin());
    group.add(getJRadioButtonAverage());
    group.add(getJRadioButtonNone());
    getJRadioButtonCount().setSelected(true);
    initUI();
    addHelp();
    // user code end
}
/**
 * ��ʼ�������ֵ��
 * 
 * �������ڣ�(2003-8-18 11:23:35)
 * @author������Ƽ
 */
private void initUI() {
    //�õ��ɷ���ͳ�ƹ�ʽ�Ĺ�ʽ���ƺ�ָ�����ơ�ָ��PK
    String strFormulaName = null;
    String strFormulaFldPK = null;
    boolean bGroup = false;
    if (m_oGroupFormulaVO != null
        && m_oGroupFormulaVO.getFormulaContent() != null) {
        String strFormulaContent = m_oGroupFormulaVO.getFormulaContent();
        int iPosPre = strFormulaContent.indexOf("(\'");
        int iPosAfter = strFormulaContent.indexOf("\')");
        if (iPosPre >= 0 && iPosAfter > iPosPre + 2) {
            strFormulaName = strFormulaContent.substring(0, iPosPre);
            strFormulaFldPK = strFormulaContent.substring(iPosPre + 2, iPosAfter);
            bGroup = true;
        }
    }
    //��ʼ��������ָ���ѡ�б� 
    if (m_vecAllDynAreaDPFld != null) {
        int iLen = m_vecAllDynAreaDPFld.size();
        for (int i = 0; i < iLen; i++) {
            FieldMap fieldMap = (FieldMap) m_vecAllDynAreaDPFld.get(i);
            String strMapPK = fieldMap.getMapPK();
            getJComboBoxProject().addItem(fieldMap);
            if (bGroup) {
                if (strFormulaFldPK != null && strFormulaFldPK.equals(strMapPK)) {
                    getJComboBoxProject().setSelectedItem(fieldMap);
                    Integer nMeasType = (Integer) m_hashMeasTypes.get(fieldMap.getMapName());
                    if (nMeasType != null) {
                        enableFuncRadioBtns(nMeasType.intValue());
                    }
                }
            }
        }
        if (!bGroup) {
            getJComboBoxProject().setSelectedIndex(0);
            if (iLen > 0) {
                FieldMap fieldMap = (FieldMap) m_vecAllDynAreaDPFld.get(0);
                Integer nMeasType = (Integer) m_hashMeasTypes.get(fieldMap.getMapName());
                enableFuncRadioBtns(nMeasType.intValue());
            }
        }

        if (bGroup && strFormulaName != null) { //����
            if (strFormulaName.equals(funcnames[0])) {
                getJRadioButtonCount().setSelected(true);
            } //���
            else
                if (strFormulaName.equals(funcnames[1])) {
                    getJRadioButtonSum().setSelected(true);
                } //���
            else
                if (strFormulaName.equals(funcnames[2])) {
                    getJRadioButtonMax().setSelected(true);
                } //��С
            else
                if (strFormulaName.equals(funcnames[3])) {
                    getJRadioButtonMin().setSelected(true);
                } //ƽ��
            else
                if (strFormulaName.equals(funcnames[4])) {
                    getJRadioButtonAverage().setSelected(true);
                }
        }
    }

    //��ʼ����Ϻ�����¼�������
    ivjJComboBoxProject.addActionListener(this);
}
/**
 * Invoked when an item has been selected or deselected.
 * The code written for this method performs the operations
 * that need to occur when an item is selected (or deselected).
 */
public void itemStateChanged(java.awt.event.ItemEvent e) {
    int nSelectIndex = getJComboBoxProject().getSelectedIndex();
    if (nSelectIndex >= 0) {
        FieldMap fieldMap = (FieldMap) m_vecAllDynAreaDPFld.get(nSelectIndex);
        Integer nMeasType = (Integer) m_hashMeasTypes.get(fieldMap.getMapName());
        enableFuncRadioBtns(nMeasType.intValue());
    }
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
    //try {

        //Vector vecAllDynAreaDPFld = new Vector();
        //DataProcessFld dpFld = new DataProcessFld();
        //dpFld.setMapName("����ͳ�ƺ���0");
        //dpFld.setMapType(FieldMap.FIELD_MAP_MEASURE);
        //dpFld.setMapPK("PK_0");

        //vecAllDynAreaDPFld.add(dpFld);

        //GroupFormulaVO groupFormulaVO = new GroupFormulaVO();
        ////groupFormulaVO.setFormulaContent(
        ////  "GSum(" + m_oAllDynAreaDPFlds[0].getMapName() + ")");

        ////���÷�������ݴ�����
        //GroupAggregateDefDlg aGroupAggregateDefDlg;
        //aGroupAggregateDefDlg =
            //new GroupAggregateDefDlg(null, vecAllDynAreaDPFld, groupFormulaVO);

        //aGroupAggregateDefDlg.setModal(true);
        //aGroupAggregateDefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
            //public void windowClosing(java.awt.event.WindowEvent e) {
                //System.exit(0);
            //};
        //});
        //aGroupAggregateDefDlg.show();
        //java.awt.Insets insets = aGroupAggregateDefDlg.getInsets();
        //aGroupAggregateDefDlg.setSize(
            //aGroupAggregateDefDlg.getWidth() + insets.left + insets.right,
            //aGroupAggregateDefDlg.getHeight() + insets.top + insets.bottom);
        //aGroupAggregateDefDlg.setVisible(true);

        //if (aGroupAggregateDefDlg.getResult() == ID_OK) {
            //System.out.println(
                //aGroupAggregateDefDlg.getGroupFormulaVO().getFormulaContent());
        //}
    //} catch (Throwable exception) {
        //System.err.println(
            //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog �� main() �з����쳣");
        //exception.printStackTrace(System.out);
    //}
}
}


