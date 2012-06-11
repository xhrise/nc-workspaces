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
  增加报表查询修改时的校验（对查询引擎和报表指标/关键自的引用是否还有效）
 @end
 @update
  2003-11-04 15:29 liulp
   对whereCondVO的数据类型、空，单双引号进行了统一的校验处理
 @end
 @update 2003-10-31 09:58 liulp
  增加帮助链接
 @end
 @update 2003-10-23 13:21 liulp
  错误对话框应该是模态的
 @end
 @update 2003-10-21 20:42
   添加查询引擎所属数据源的属性
 @end
 @update 2003-10-15 16:29
   查询引擎的校验数据源的接口方法改变
 @end
 @update 2003-1014 17:01
   修改查询时，当前修改查询在右面板的属性没有展现的错误更正.
 @end
 * 说明:业务查询属性设置对话框	
 * 作者：王少松
 * 创建日期：(2003-4-2 19:28:13)
 * 版本：
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

    //创建或修改的报表查询
    private ReportQueryVO m_reportQueryVO = null;
    //映射的动态区域PK
    private String m_strDynAreaPK = null;
    
    private String STR_OK = StringResource.getStringResource(StringResource.OK);
    private String STR_NEXT_STEP = StringResource.getStringResource("miufopublic261");  //"下一步"
	private CellsModel _cellsModel;
	private Context _contextVO;
/**
 * BQueryPropertyDlg 构造子注解。
 */
public BQueryPropertyDlg() {
	super();
	initialize();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-15 14:02:32)
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
 * actionPerformed 方法注解。
 */
public void actionPerformed(ActionEvent e) {
    //下一步或确定
    if (e.getSource() == getBtnNext()) {
    	//确定
        if (getBtnNext().getText().equals(STR_OK)) {
            //#where条件界面检验
            StringBuffer sbWhereErr = new StringBuffer();
            WhereCondVO[] whereCondVOs = page3.getResultFromCond(sbWhereErr);
            if (sbWhereErr.length() > 0) {
                UfoPublic.sendWarningMessage(
                    sbWhereErr.toString(),this);
                return;

            }

            //#得到修改信息
            //所有选中字段信息
            ReportSelectFldVO[] repSelFldVOs = page2.getResultSelected();

            if (repSelFldVOs != null) {
                //设置选择的报表查询字段(排序字段在执行报表查询的时候统一进行)
                m_reportQueryVO.setSelectFlds(repSelFldVOs);

                //设置动态区域PK信息
                m_reportQueryVO.setDynAreaPK(m_strDynAreaPK);

                //设置条件信息
                m_reportQueryVO.setWhereCondVOs(whereCondVOs);

                //设置用户选择的数据源信息
                m_reportQueryVO.setDSName(page1.getDSName());

                //设置用户选择的数据源信息
                m_reportQueryVO.setQEDSName(page1.getQEDSName());
            }
            setResult(ID_OK);
            close();
            ////debug
            //return;
            ////end
        } else {
            //选择基础的查询引擎对象
            if (m_nStepNum == 1) {
                //校验查询定义在切换了所属数据源的时候是否仍有效
                boolean bQueryValid = doCheckReportQueryDefDS();
                if (!bQueryValid) {
                    UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001364"),this);  //"查询在指定数据源下校验不通过！"
                    return;
                }

                //#对于修改的查询,还需要做执行有效性和已定义的报表查询有效性的检验：
                if (!page1.isCreateReportQuery()) {
                    doCheckReportQueryDef();

                }

            }
            //报表查询字段映射
            else
                if (m_nStepNum == 2) {
                    //所有选中字段信息
                    ReportSelectFldVO[] repSelFldVOs = page2.getResultSelected();

                    //#映射个数检验
                    if (repSelFldVOs == null || repSelFldVOs.length <= 0) {
                        String s = StringResource.getStringResource("miufo1001365") +  //"请选择字段后,再进行“"
                        		"\"" +
                        		STR_NEXT_STEP +
                        		"\"";
                        UfoPublic.sendWarningMessage(s,this);
                        return;
                    }
                    //#校验同一查询的一个字段不能映射多个指标/关键字,且必须映射一个指标/关键字
                    //：即MappedName必须唯一
                    String strError = ReportQueryUtil.checkFldOneMapMore(repSelFldVOs);
                    if (strError.length() > 0) {
                        UfoPublic.sendWarningMessage(strError,this);
                        return;
                    }

                    //#映射有效性检验
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

    //上一步
    if (e.getSource() == getBtnPrevious()) {
        m_nStepNum = m_nStepNum - 1;
        if (m_nStepNum < 1)
            m_nStepNum = 1;
        enableButton();
        card.previous(ivjJPanel1);
    }

    //取消
    if (e.getSource() == getBtnCancel()) {
        setResult(ID_CANCEL);
        close();
    }
}
/**
 * 增加帮助。
 * 
 * 创建日期：(2003-10-31 09:56:54)
 * 创建者：刘良萍
 */
private void addHelp() {
    javax.help.HelpBroker hb = ResConst.getHelpBroker();
    if (hb == null)
        return;
    hb.enableHelpKey(getContentPane(), "TM_Data_Query_Add", null);

}
/**
 * 映射有效性检验。
 * 
 * 创建日期：(2003-11-5 12:12:44)
 * @author：刘良萍
 * @param sbError java.lang.StringBuffer
 * @param sbDynAreaPK java.lang.StringBuffer
 * @param repSelFldVOs com.ufsoft.iuforeport.reporttool.query.ReportSelectFldVO[]
 */
private int doCheckMapValid(
    StringBuffer sbError,
    StringBuffer sbDynAreaPK,
    ReportSelectFldVO[] repSelFldVOs) {

    int nCheck = -1;
    //得到报表对象，从而得到报表格式模型
  
    //#关键字组合之间是否冲突，关键字组合是否有效映射有效性
    //#并同时检查固定指标、动态指标的所属正确性
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
 * 对报表查询进行执行有效性和已定义的报表查询有效性的检验。
 * 
 * 创建日期：(2003-11-5 12:02:28)
 * @author：刘良萍
 */
private void doCheckReportQueryDef() {
    //得到报表对象，从而得到报表格式模型
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
 * 此处插入方法描述。
 * 创建日期：(2003-11-5 12:07:32)
 * @author：刘良萍
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
        //校验查询定义在切换了所属数据源的时候是否仍有效
        bQueryValid = nc.vo.pub.querymodel.ModelUtil.isQueryDefValid(strId, strDSName);
    } else {
        bQueryValid = true;
    }

    return bQueryValid;
}
/**
 * 在此处插入方法说明。
 * 创建日期：(2003-4-4 17:11:52)
 */
public void enableButton() {
	if (m_nStepNum == 1) {
		getBtnPrevious().setEnabled(false);
		getBtnNext().setEnabled(true);
		getBtnNext().setText(STR_NEXT_STEP);//下一步
		this.setTitle(StringResource.getStringResource("miufo1001366"));  //"插入表格-选择业务查询"
	}
	if (m_nStepNum == 3) {
		getBtnPrevious().setEnabled(true);
		getBtnNext().setText(STR_OK);//确定
		getBtnNext().setEnabled(true);
		this.setTitle(StringResource.getStringResource("miufo1001367"));  //"插入表格-筛选"
	}
	if (m_nStepNum == 2) {
		getBtnPrevious().setEnabled(true);
		getBtnNext().setEnabled(true);
		getBtnNext().setText(STR_NEXT_STEP);//下一步
		this.setTitle(StringResource.getStringResource("miufo1001368"));  //"插入表格-选择业务查询项目"
	}
}
/**
 * 返回 JButton3 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JButton2 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 BtnPrevious 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
			String strPreStep = StringResource.getStringResource("miufopublic260");  //"上一步"
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
 * 返回 JPanel1 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JPanel2 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JSeparator1 特性值。
 * @return javax.swing.JSeparator
 */
/* 警告：此方法将重新生成。 */
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
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-9 11:17:50)
 * 参数： @param：<|>
 * 返回值：@return：
 * 
 * @return ReportQueryVO
 */
public ReportQueryVO getReportQueryVO() {
	return m_reportQueryVO;
}
/**
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	 AppDebug.debug("--------- Not cateched Exception ---------");//@devTools System.out.println("--------- Not cateched Exception ---------");
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
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
    setTitle(StringResource.getStringResource("miufo1001369"));  //"插入报表查询-选择业务查询"
    addHelp();
    // user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-9 11:17:50)
 * 参数： @param：<|>
 * 返回值：@return：
 * 
 * @param newReportQueryVO com.ufsoft.iuforeport.reporttool.data.ReportQueryVO
 * @param bInitRightPanel boolean - 是否初始化第一页右边面扳的值
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


