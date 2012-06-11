package com.ufsoft.iufo.fmtplugin.businessquery;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISeparator;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iuforeport.businessquery.QuerySimpleDef;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.pub.querymodel.ParamVO;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;
/**
 @update 2003-11-05 14:06 liulp
  ���ӱ����ѯ�޸�ʱ��У�飨�Բ�ѯ����ͱ���ָ��/�ؼ��Ե������Ƿ���Ч��
 @end
 @update
  2003-11-04 15:29 liulp
   ��whereCondVO���������͡��գ���˫���Ž�����ͳһ��У�鴦��
 @end
 @update 2003-10-31 09:58 liulp
  ���Ӱ�������
 @end
 @update 2003-10-23 13:21 liulp
  ����Ի���Ӧ����ģ̬��
 @end
 @update 2003-10-21 20:42
   ��Ӳ�ѯ������������Դ������
 @end
 @update 2003-10-15 16:29
   ��ѯ�����У������Դ�Ľӿڷ����ı�
 @end
 @update 2003-1014 17:01
   �޸Ĳ�ѯʱ����ǰ�޸Ĳ�ѯ������������û��չ�ֵĴ������.
 @end
 * ˵��:ҵ���ѯ�������öԻ���	
 * ���ߣ�������
 * �������ڣ�(2003-4-2 19:28:13)
 * �汾��
 */public class BQueryPropertyDlg extends UfoDialog implements ActionListener {
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private javax.swing.JPanel ivjJPanel2 = null;
    private javax.swing.JSeparator ivjJSeparator1 = null;
    private javax.swing.JButton ivjBtnPrevious = null;
    private javax.swing.JButton ivjBtnCancel = null;
    private javax.swing.JButton ivjBtnNext = null;
    private SelectBQueryPage page1 = null;
    private SelectFieldsPage page2 = null;
    private SetConditionPage page3 = null;
    CardLayout card = new CardLayout(10, 5);
    private int m_nStepNum = 1;

    //�������޸ĵı����ѯ
    private ReportQueryVO m_reportQueryVO = null;
    //ӳ��Ķ�̬����PK
    private String m_strDynAreaPK = null;
    
    private String STR_OK = StringResource.getStringResource(StringResource.OK);
    private String STR_NEXT_STEP = StringResource.getStringResource("miufopublic261");  //"��һ��"
	private CellsModel _cellsModel;
	private Context _contextVO;
/**
 * BQueryPropertyDlg ������ע�⡣
 */
public BQueryPropertyDlg() {
	super();
	initialize();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-15 14:02:32)
 * @param parent java.awt.Container
 * @param cellsModel 
 * @param reportQueryVO com.ufsoft.iuforeport.reporttool.query.ReportQueryVO
 */
public BQueryPropertyDlg(Container parent, CellsModel cellsModel,Context contextVO, ReportQueryVO reportQueryVO) {
    super(parent);
    _cellsModel = cellsModel;
    _contextVO = contextVO;
    this.m_reportQueryVO = reportQueryVO;

    initialize();
}
/**
 * actionPerformed ����ע�⡣
 */
public void actionPerformed(ActionEvent e) {
    //��һ����ȷ��
    if (e.getSource() == getBtnNext()) {
    	//ȷ��
        if (getBtnNext().getText().equals(STR_OK)) {
            //#where�����������
            StringBuffer sbWhereErr = new StringBuffer();
            WhereCondVO[] whereCondVOs = page3.getResultFromCond(sbWhereErr);
            if (sbWhereErr.length() > 0) {
                UfoPublic.sendWarningMessage(
                    sbWhereErr.toString(),this);
                return;

            }

            //#�õ��޸���Ϣ
            //����ѡ���ֶ���Ϣ
            ReportSelectFldVO[] repSelFldVOs = page2.getResultSelected();

            if (repSelFldVOs != null) {
                //����ѡ��ı����ѯ�ֶ�(�����ֶ���ִ�б����ѯ��ʱ��ͳһ����)
                m_reportQueryVO.setSelectFlds(repSelFldVOs);

                //���ö�̬����PK��Ϣ
                m_reportQueryVO.setDynAreaPK(m_strDynAreaPK);

                //����������Ϣ
                m_reportQueryVO.setWhereCondVOs(whereCondVOs);

                //�����û�ѡ�������Դ��Ϣ
                m_reportQueryVO.setDSName(page1.getDSName());

                //�����û�ѡ�������Դ��Ϣ
                m_reportQueryVO.setQEDSName(page1.getQEDSName());
            }
            setResult(ID_OK);
            close();
            ////debug
            //return;
            ////end
        } else {
            //ѡ������Ĳ�ѯ�������
            if (m_nStepNum == 1) {
                //У���ѯ�������л�����������Դ��ʱ���Ƿ�����Ч
                boolean bQueryValid = doCheckReportQueryDefDS();
                if (!bQueryValid) {
                    UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001364"),this);  //"��ѯ��ָ������Դ��У�鲻ͨ����"
                    return;
                }

                //#�����޸ĵĲ�ѯ,����Ҫ��ִ����Ч�Ժ��Ѷ���ı����ѯ��Ч�Եļ��飺
                if (!page1.isCreateReportQuery()) {
                    doCheckReportQueryDef();

                }

            }
            //�����ѯ�ֶ�ӳ��
            else
                if (m_nStepNum == 2) {
                    //����ѡ���ֶ���Ϣ
                    ReportSelectFldVO[] repSelFldVOs = page2.getResultSelected();

                    //#ӳ���������
                    if (repSelFldVOs == null || repSelFldVOs.length <= 0) {
                        String s = StringResource.getStringResource("miufo1001365") +  //"��ѡ���ֶκ�,�ٽ��С�"
                        		"\"" +
                        		STR_NEXT_STEP +
                        		"\"";
                        UfoPublic.sendWarningMessage(s,this);
                        return;
                    }
                    //#У��ͬһ��ѯ��һ���ֶβ���ӳ����ָ��/�ؼ���,�ұ���ӳ��һ��ָ��/�ؼ���
                    //����MappedName����Ψһ
                    String strError = ReportQueryUtil.checkFldOneMapMore(repSelFldVOs);
                    if (strError.length() > 0) {
                        UfoPublic.sendWarningMessage(strError,this);
                        return;
                    }

                    //#ӳ����Ч�Լ���
                    StringBuffer sbError = new StringBuffer();
                    StringBuffer sbDynAreaPK = new StringBuffer();
                    int nCheck = doCheckMapValid(sbError, sbDynAreaPK, repSelFldVOs);
                    if (nCheck < 0) {
                        UfoPublic.sendWarningMessage(sbError.toString(),this);
                        return;
                    } else {
                        m_strDynAreaPK = sbDynAreaPK.toString();
                    }
                }

            m_nStepNum = m_nStepNum + 1;
            if (m_nStepNum > 3) {
                m_nStepNum = 3;
            }

            enableButton();
            card.next(ivjJPanel1);
        }
    }

    //��һ��
    if (e.getSource() == getBtnPrevious()) {
        m_nStepNum = m_nStepNum - 1;
        if (m_nStepNum < 1)
            m_nStepNum = 1;
        enableButton();
        card.previous(ivjJPanel1);
    }

    //ȡ��
    if (e.getSource() == getBtnCancel()) {
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
    javax.help.HelpBroker hb = ResConst.getHelpBroker();
    if (hb == null)
        return;
    hb.enableHelpKey(getContentPane(), "TM_Data_Query_Add", null);

}
/**
 * ӳ����Ч�Լ��顣
 * 
 * �������ڣ�(2003-11-5 12:12:44)
 * @author������Ƽ
 * @param sbError java.lang.StringBuffer
 * @param sbDynAreaPK java.lang.StringBuffer
 * @param repSelFldVOs com.ufsoft.iuforeport.reporttool.query.ReportSelectFldVO[]
 */
private int doCheckMapValid(
    StringBuffer sbError,
    StringBuffer sbDynAreaPK,
    ReportSelectFldVO[] repSelFldVOs) {

    int nCheck = -1;
    //�õ�������󣬴Ӷ��õ������ʽģ��
  
    //#�ؼ������֮���Ƿ��ͻ���ؼ�������Ƿ���Чӳ����Ч��
    //#��ͬʱ���̶�ָ�ꡢ��ָ̬���������ȷ��
    Hashtable hashMeasFldVOs = new Hashtable();
    Hashtable hashKeyFldVOs = new Hashtable();
    ReportQueryUtil.setMeasKeyHashs(repSelFldVOs, hashMeasFldVOs, hashKeyFldVOs);
    nCheck =
        ReportQueryUtil.checkValidKeywordMapped(
            sbError,
            _cellsModel,
            hashKeyFldVOs,
            hashMeasFldVOs,
            sbDynAreaPK);

    return nCheck;
}
/**
 * �Ա����ѯ����ִ����Ч�Ժ��Ѷ���ı����ѯ��Ч�Եļ��顣
 * 
 * �������ڣ�(2003-11-5 12:02:28)
 * @author������Ƽ
 */
private void doCheckReportQueryDef() {
    //�õ�������󣬴Ӷ��õ������ʽģ��
	ParamVO[] params=null;
	Vector vecKeyVO = new Vector(KeywordModel.getInstance(_cellsModel).getMainKeyVOPos().keySet());
	if (vecKeyVO!=null){
		List<ParamVO> vParam=new ArrayList<ParamVO>();
		for (int i=0;i<vecKeyVO.size();i++){
			KeyVO key=(KeyVO)vecKeyVO.get(i);
			if (key.getCode()!=null && key.getCode().trim().length()>0){
				ParamVO param=new ParamVO();
				param.setParamCode("#"+key.getCode()+"#");
				param.setParamName("#"+key.getCode()+"#");
				param.setValue("111");
				vParam.add(param);
			}
		}
		params=vParam.toArray(new ParamVO[0]);
	}
	
    StringBuffer sbRepQueryError = new StringBuffer();
    ReportQueryUtil.checkReportQueryDef(
        sbRepQueryError,
        _cellsModel,
        m_reportQueryVO,
        page1.getQEDSName(),
        page1.getDSName(),
        params);
    if (sbRepQueryError.length() > 0) {
        UfoPublic.sendWarningMessage(
            sbRepQueryError.toString(),this);
        page2.setReportQueryVO(m_reportQueryVO);
        //return;
    }
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-11-5 12:07:32)
 * @author������Ƽ
 */
private boolean doCheckReportQueryDefDS() {
    boolean bQueryValid = false;

    QuerySimpleDef curSimpleDef = m_reportQueryVO.getQuerydef();
    String strDSName = page1.getDSName();
    String strOldDsName = curSimpleDef.getDsName();
    if (strOldDsName == null)
        strOldDsName = "";
    String strId = curSimpleDef.getID();
    if (!strOldDsName.equals(strDSName)) {
        //У���ѯ�������л�����������Դ��ʱ���Ƿ�����Ч
        bQueryValid = nc.vo.pub.querymodel.ModelUtil.isQueryDefValid(strId, strDSName);
    } else {
        bQueryValid = true;
    }

    return bQueryValid;
}
/**
 * �ڴ˴����뷽��˵����
 * �������ڣ�(2003-4-4 17:11:52)
 */
public void enableButton() {
	if (m_nStepNum == 1) {
		getBtnPrevious().setEnabled(false);
		getBtnNext().setEnabled(true);
		getBtnNext().setText(STR_NEXT_STEP);//��һ��
		this.setTitle(StringResource.getStringResource("miufo1001366"));  //"������-ѡ��ҵ���ѯ"
	}
	if (m_nStepNum == 3) {
		getBtnPrevious().setEnabled(true);
		getBtnNext().setText(STR_OK);//ȷ��
		getBtnNext().setEnabled(true);
		this.setTitle(StringResource.getStringResource("miufo1001367"));  //"������-ɸѡ"
	}
	if (m_nStepNum == 2) {
		getBtnPrevious().setEnabled(true);
		getBtnNext().setEnabled(true);
		getBtnNext().setText(STR_NEXT_STEP);//��һ��
		this.setTitle(StringResource.getStringResource("miufo1001368"));  //"������-ѡ��ҵ���ѯ��Ŀ"
	}
}
/**
 * ���� JButton3 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnCancel() {
	if (ivjBtnCancel == null) {
		try {
			ivjBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjBtnCancel.setName("BtnCancel");
	//		ivjBtnCancel.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnCancel.setText("Cancel");
			ivjBtnCancel.setBounds(523, 10, 75, 22);
			// user code begin {1}
			String strCancel = StringResource.getStringResource(StringResource.CANCEL);
			ivjBtnCancel.setText(strCancel);
			ivjBtnCancel.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnCancel;
}
/**
 * ���� JButton2 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnNext() {
	if (ivjBtnNext == null) {
		try {
			ivjBtnNext = new nc.ui.pub.beans.UIButton();
			ivjBtnNext.setName("BtnNext");
	//		ivjBtnNext.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnNext.setText("Next Step");
			ivjBtnNext.setBounds(422, 10, 75, 22);
			// user code begin {1}
			ivjBtnNext.setText(STR_NEXT_STEP);
			ivjBtnNext.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnNext;
}
/**
 * ���� BtnPrevious ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnPrevious() {
	if (ivjBtnPrevious == null) {
		try {
			ivjBtnPrevious = new nc.ui.pub.beans.UIButton();
			ivjBtnPrevious.setName("BtnPrevious");
			ivjBtnPrevious.setToolTipText("");
	//		ivjBtnPrevious.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnPrevious.setText("Pre Step");
			ivjBtnPrevious.setBounds(320, 10, 75, 22);
			ivjBtnPrevious.setEnabled(false);
			// user code begin {1}
			String strPreStep = StringResource.getStringResource("miufopublic260");  //"��һ��"
			ivjBtnPrevious.setText(strPreStep);
			ivjBtnPrevious.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnPrevious;
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
            ivjJPanel1.setBounds(4, 3, 621, 350);
            // user code begin {1}
            page1 = new SelectBQueryPage(this, m_reportQueryVO.getQEDSName());
            page1.setReportQueryVO(m_reportQueryVO, true);
            page2 = new SelectFieldsPage(this,_cellsModel,_contextVO);
            page2.setReportQueryVO(m_reportQueryVO);
            page3 = new SetConditionPage();
            page3.setUfoContext(_contextVO);
            page3.setReportQueryVO(m_reportQueryVO);

            ivjJPanel1.setLayout(card);
            ivjJPanel1.add("page1", page1);
            ivjJPanel1.add("page2", page2);
            ivjJPanel1.add("page3", page3);

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
			ivjJPanel2.setBounds(6, 372, 616, 44);
			getJPanel2().add(getBtnPrevious(), getBtnPrevious().getName());
			getJPanel2().add(getBtnNext(), getBtnNext().getName());
			getJPanel2().add(getBtnCancel(), getBtnCancel().getName());
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
 * ���� JSeparator1 ����ֵ��
 * @return javax.swing.JSeparator
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JSeparator getJSeparator1() {
	if (ivjJSeparator1 == null) {
		try {
			ivjJSeparator1 = new UISeparator();
			ivjJSeparator1.setName("JSeparator1");
			ivjJSeparator1.setBounds(1, 362, 621, 2);
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
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-9 11:17:50)
 * ������ @param��<|>
 * ����ֵ��@return��
 * 
 * @return ReportQueryVO
 */
public ReportQueryVO getReportQueryVO() {
	return m_reportQueryVO;
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
	 AppDebug.debug("--------- Not cateched Exception ---------");//@devTools System.out.println("--------- Not cateched Exception ---------");
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("BQueryPropertyDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(628, 444);
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setTitle(StringResource.getStringResource("miufo1001369"));  //"���뱨���ѯ-ѡ��ҵ���ѯ"
    addHelp();
    // user code end
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		BQueryPropertyDlg aBQueryPropertyDlg;
		aBQueryPropertyDlg = new BQueryPropertyDlg();
		aBQueryPropertyDlg.setModal(true);
		aBQueryPropertyDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aBQueryPropertyDlg.show();
		java.awt.Insets insets = aBQueryPropertyDlg.getInsets();
		aBQueryPropertyDlg.setSize(aBQueryPropertyDlg.getWidth() + insets.left + insets.right, aBQueryPropertyDlg.getHeight() + insets.top + insets.bottom);
		aBQueryPropertyDlg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main() :Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-9 11:17:50)
 * ������ @param��<|>
 * ����ֵ��@return��
 * 
 * @param newReportQueryVO com.ufsoft.iuforeport.reporttool.data.ReportQueryVO
 * @param bInitRightPanel boolean - �Ƿ��ʼ����һҳ�ұ�����ֵ
 */
protected void setReportQueryVO(
    ReportQueryVO newReportQueryVO,
    boolean bInitRightPanel) {
    m_reportQueryVO = newReportQueryVO;
    if (page1 != null && page2 != null && page3 != null) {
        page1.setReportQueryVO(m_reportQueryVO, bInitRightPanel);
        page2.setReportQueryVO(m_reportQueryVO);
        page3.setReportQueryVO(m_reportQueryVO);
    }
}
}


