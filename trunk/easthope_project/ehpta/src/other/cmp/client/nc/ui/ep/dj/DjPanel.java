package nc.ui.ep.dj;

/**
 * 此处插入类型说明。 创建日期：(2001-5-21 11:14:24)
 *
 * @author：陈飞
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.cmp.pub.cache.FiPubDataCache;
import nc.impl.arap.proxy.Proxy;
import nc.itf.cmp.prv.ICMPBillPrivate;
import nc.itf.cmp.settlement.ICMP4BusiMakeBill;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.KeyLock;
import nc.ui.arap.actions.EditAction;
import nc.ui.arap.actions.TempSaveAction;
import nc.ui.arap.actions.common.DjFilterHelper;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.arap.engine.ExtButtonObject;
import nc.ui.arap.global.DjTempletHelper;
import nc.ui.arap.global.IArapTempletController;
import nc.ui.arap.global.TempletController;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.ARAPDjUIFactory;
import nc.ui.arap.pubdj.AdjustVoAfterQuery;
import nc.ui.arap.selectedpay.Cache;
import nc.ui.arap.templetcache.DjlxTempletCacheNew;
import nc.ui.bd.def.ListDefShowUtil;
import nc.ui.cmp.IBusiInfoPanel;
import nc.ui.cmp.workflow.IworkflowLinkData;
import nc.ui.glpub.IParent;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billtype.BilltypeBO_Client;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.trade.bill.DefaultDefShowStrategyByBillItem;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapDjCalculator;
import nc.vo.arap.global.IRuntimeConstans;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.cmp.BusiInfo;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.security.AuthenticatorFactory;

/**
 * @function
 * @author maji
 * @version V6.0
 * @since V6.0
 */
@SuppressWarnings("restriction")
public abstract class DjPanel extends AbstractRuntime implements IBusiInfoPanel,
nc.ui.glpub.IUiPanel {
	public UFBoolean IsAloneNode=UFBoolean.FALSE;
	public UFBoolean getIsAloneNode(){
		return IsAloneNode;
	}
	class DjStateChange implements nc.ui.arap.pubdj.DjStateChangeListener {
		public void zyxStateChange(nc.ui.arap.pubdj.ArapDjPanel e) {
			ExceptionHandler.debug("setZyxButtonsState");
			refreshBtnStats();
		}

		public void tabIndexChange(nc.ui.arap.pubdj.ArapDjPanel e) {
			// 在单据和自由项中切换
			ExceptionHandler.debug("setButtonVisible");
			// refreshBtnStats();
			if(e.getM_TabIndex()==1)
				DjPanel.this.setCurrentpage(ArapBillWorkPageConst.ZYXPAGE);
			else if(e.getM_TabIndex()==2)
				DjPanel.this.setCurrentpage(ArapBillWorkPageConst.SETTLEPAGE);
			else if ( DjPanel.this  .getAttribute("CounterActAction_m_Bc")!=null)
				DjPanel.this.setCurrentpage(ArapBillWorkPageConst.MAKEUPPAGE);
			else
				DjPanel.this.setCurrentpage(ArapBillWorkPageConst.CARDPAGE);

			setButtonVisible(e.getM_TabIndex());
		}

		public void djStateChange(nc.ui.arap.pubdj.ArapDjPanel e) {
			ExceptionHandler.debug("setButtonsState");
			refreshBtnStats();
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {

		}

		public void deletedDj(nc.ui.arap.pubdj.ArapDjPanel e) {
			 DjPanel.this.editbillcache.remove(getDjDataBuffer().getCurrentDJZBVO());
			 DjPanel.this.addbillcache.remove(getDjDataBuffer().getCurrentDJZBVO());

		}

		public void saveNewDj(nc.ui.arap.pubdj.ArapDjPanel e) {
			try {
				// DjPanel.this.addDjlb(e.getM_Djzbvo());
				if (!m_isPayPanel)
					DjPanel.this.addDjlb(getDjDataBuffer().getCurrentDJZBVO());
			} catch (Throwable ee) {
				ExceptionHandler.debug("addDjlb error: " + ee);
			}

		}

		// 用户选择单据不同的表体行
		public void selectedBodyRow(nc.ui.arap.pubdj.ArapDjPanel e) {
			try {
				refreshBtnStats();
			} catch (Exception ee) {
				ExceptionHandler.debug("setVerifyVisible error: " + ee);
			}

		}

		public void saveEditDj(nc.ui.arap.pubdj.ArapDjPanel e) {

			try {
				// DjPanel.this.editDjlb(e.getM_Djzbvo());
				if (!m_isPayPanel)
					DjPanel.this.editDjlb(getDjDataBuffer().getCurrentDJZBVO());
			} catch (Throwable ee) {
				ExceptionHandler.debug("editDjlb error: " + ee);
			}
		}

	}

//	class windowListenerQuery implements java.awt.event.WindowListener {
//		public void windowOpened(java.awt.event.WindowEvent e) {
//		}
//
//		public void windowClosed(java.awt.event.WindowEvent e) {
//		}
//
//		public void windowDeiconified(java.awt.event.WindowEvent e) {
//		}
//
//		public void windowActivated(java.awt.event.WindowEvent e) {
//		}
//
//		public void windowDeactivated(java.awt.event.WindowEvent e) {
//		}
//
//		public void windowIconified(java.awt.event.WindowEvent e) {
//		}
//
//		public void windowClosing(java.awt.event.WindowEvent e) {
//			nc.bs.logging.Log.getInstance(this.getClass()).debug("---------windows closeing-----------");
//			ArapDjPanel arapDjPanel;
//			try {
//				arapDjPanel = (ArapDjPanel)((DjPanel)getParent()).invokeMethod("getArapDjPanel1");
//				if(3==arapDjPanel.getM_DjState()){
//					int flag = -1;
//					flag = (MessageDialog.showYesNoCancelDlg(getParent(), null,
//								nc.ui.ml.NCLangRes.getInstance().getStrByID(
//										"2006030102", "UPP2006030102-v53-000122")/*
//																				 * @res
//																				 * "是否保存未编辑完成的数据?"
//																				 */, MessageDialog.ID_YES));
//
//					switch (flag) {
//					case MessageDialog.ID_YES://保存编辑的数据
//						return "background";
//					default://关闭窗口，不做保存处理
//						return ;
//					}
//
//					(ArapDjPanel)((DjPanel)getParent()).invokeMethod("getArapDjPanel1");
//				}
//			} catch (BusinessException e1) {
//				nc.bs.logging.Log.getInstance(this.getClass()).error(e1.getMessage(), e1);
//			}
//
//
//			nc.bs.logging.Log.getInstance(this.getClass())
//					.debug("---------windows closeing-----------");
//
//
//		}
//
//	};
	/* 上一次的单据工作页状态 */
	private int m_lastWorkPage = -1;

	private boolean isInitied = false;

	private UIPanel ivjUIPanel2 = null;

	int m_TabIndex = 1; // 1列表,-1单据

	protected nc.ui.pub.bill.BillListPanel ivjBillListPanel1 = null;

	// refactoring:useless,deleted
	// private QueryCondDlg ivjQueryCondDlg1 = null;
	// private nc.ui.pub.beans.UIRefPane xmjdRef = null; //项目阶段参照

	private nc.ui.arap.pubdj.ArapDjPanel ivjArapDjPanel1 = null;

	private Cache voCache = new Cache();

	private nc.vo.ep.dj.DjCondVO cur_Djcondvo = null;

	public int[] m_BtnStates; //

	DjStateChange m_djStateChange = new DjStateChange();

	private javax.swing.JPanel ivjJPanel1 = null;

	private javax.swing.JPanel ivjVerifyPanel = null;

	public nc.vo.arap.global.BusiTransVO[] m_CurBusiTransVO;

	// private VerifyDjPanel verifyDjPanel=null;

	public int pzglh;

	protected boolean m_isPayPanel = false;

	// 0普通，1审批，2查询,3增加,4修改
	protected int PanelProp = 0;

	private boolean cancelCloseFlag = false;

	private boolean FROMCMP=false;



	public ExtButtonObject m_boAdd = new ExtButtonObject( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000002"),   nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000823") , 5,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","增加"));	/*-=notranslate=-*/
	public ExtButtonObject m_boCpy = new ExtButtonObject( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000043") ,   nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000824") , 5,nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","复制"));	/*-=notranslate=-*/
	public static final String Crypt_Code="ARAP_SIGN";
	//当列表界面没有被加载时，缓存新增单据，以便在切换到列表时显示
	private List<DJZBVO> addbillcache=new ArrayList<DJZBVO>();
	private List<DJZBVO> editbillcache=new ArrayList<DJZBVO>();
	//add by rocking 缓存VO
	private DJZBVO djzbvo4CmpQry=null;
	//与结算页切换临时保存业务单据按钮
	private ButtonObject[] tempbtns=null;
	/**
	 * DjPanel 构造子注解。
	 */
	public DjPanel() {
		super();
		// initialize();
		MyClientEnvironment.refresh();

	}

	public DjPanel(int SysCode) {
		super();
		this.pzglh = SysCode;
		setM_Syscode(SysCode);
	}

	public void postInit() {
		initialize();
	}
	protected void hideverifyFlag() {
		this.getBillListPanel1().getHeadItem("verifystatus").setShow(false);
	}
	/***************************************************************************
	 * 功能: 如果UiManager或者前面的功能模块要监听本模块 的某些事件，它可以通过该方法添加。
	 *
	 * 参数: Object objListener 监听器 Object objUserdata 表识前面是何种监听器
	 *
	 * 返回值: 无
	 *
	 * 注： 该方法其实没有固定的要求，只要调用者和被调用 者之间存在该调用的相关协议，它就可使用该功能
	 **************************************************************************/
	public void addListener(java.lang.Object objListener, java.lang.Object objUserdata) {
	}

	/***************************************************************************
	 * 功能: 如果UiManager或者前面的功能模块需要调用本模 块的某个方法以完成某个功能，它可以通过该方法 达到这一目标
	 *
	 * 参数: Object objData 所要传递的参数等信息 Object objUserData 所要传递的表示等信息
	 *
	 * 返回值: Object
	 *
	 * 注： 该方法其实没有固定的要求，只要调用者和被调用 者之间存在该调用的相关协议，它就可使用该功能
	 **************************************************************************/
	@SuppressWarnings("unchecked")
	public java.lang.Object invoke(java.lang.Object objData,
			java.lang.Object objUserData) {
		if(objData instanceof String&&"VoucherSaved".equalsIgnoreCase((String)objData)){
			String seqnum = (String) getAttribute(IRuntimeConstans.settleNo);
			Vector<String> selectedPK = (Vector<String>) getAttribute(IRuntimeConstans.selectedPK);
			List<DJZBVO> vAllSelectedDJ= getListAllSelectedVOs() ;
			List <String > selectpk=getAllSelectedDJPK();
			if(vAllSelectedDJ==null)
				vAllSelectedDJ=new ArrayList<DJZBVO>();
			if(vAllSelectedDJ.size()==0){
				vAllSelectedDJ.add(this.getDjDataBuffer().getCurrentDJZBVO());
				selectpk.add(this.getDjDataBuffer().getCurrentDJZBVOPK());
			}
			try {
				NCLocator.getInstance().lookup(ICMP4BusiMakeBill.class).notifyCMPAfterMakeBill(((DJZBHeaderVO) vAllSelectedDJ.get(0).getParentVO()).getDwbm(), selectpk,seqnum);
//				String seqnum=NCLocator.getInstance().lookup(ICMP4BusiMakeBill.class).makeBill(((DJZBHeaderVO) vAllSelectedDJ.get(0).getParentVO()).getDwbm(), selectpk);
			    boolean needseq=seqnum==null||seqnum.length()==0?true:false;
//			    if(needseq&&PubData.needSettleNum(((DJZBHeaderVO) vAllSelectedDJ.get(0).getParentVO()).getDwbm())){
//			    	seqnum=new nc.bs.arap.global.SeqNumCharger().getSettleNum(((DJZBHeaderVO) vAllSelectedDJ.get(0).getParentVO()).getDwbm(),1)[0];
//			    }
			    this.getProxy().getIArapBillPrivate().updateDJSettlenum(selectpk.toArray(new String[]{}), seqnum);
//			    ProxyBill.getIArapBillPrivate().updateDJSettlenum(selectpk.toArray(new String[]{}), seqnum);
			    for (DJZBVO vo:vAllSelectedDJ) {
		        	DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
					if(needseq)
		        		head.setSettlenum(seqnum);
		        	this.getBillListPanel1().getHeadBillModel()	.setValueAt(seqnum, getHeadRowInexByPK(head.getVouchid()), "settlenum");
			    }
			    if ( getM_TabIndex() != 1) {
			    	this.getArapDjPanel1().getBillCardPanelDj().setHeadItem("settlenum", seqnum);
			    }
//			    KeyLock.freeKeyArray(this.getAllSelectedDJPK().toArray(new String[]{}), this
//    					.getDjSettingParam().getPk_user(), null);

			    KeyLock.freeKeyArray(selectedPK.toArray(new String[]{}), this
    					.getDjSettingParam().getPk_user(), null);
			} catch ( Exception e) {
				ExceptionHandler.consume(e);
			}
		}
		return null;
	}

	/***************************************************************************
	 * 功能: 在A功能模块调用B功能模块后，如果B功能模块关闭时 A功能模块通过该方法得到通知
	 *
	 * 参数: 无 返回值: 无
	 **************************************************************************/
	public void nextClosed() {
			try {
				if(null != this.getAllSelectedDJPK() && this.getAllSelectedDJPK().size() > 0){
				    KeyLock.freeKeyArray(this.getAllSelectedDJPK().toArray(new String[]{}), this
	    					.getDjSettingParam().getPk_user(), null);
				}
				getArapDjPanel1().getSettlePanel().nextClosed();
			} catch ( Exception e) {
				ExceptionHandler.consume(e);
			}
	}

	/***************************************************************************
	 * 功能: 去除某个监听器
	 *
	 * 参数: Object objListener 监听器 Object objUserdata 标识前面是何种监听器
	 *
	 * 返回值: 无
	 *
	 * 注： 该方法其实没有固定的要求，只要调用者和被调用 者之间存在该调用的相关协议，它就可使用该功能
	 **************************************************************************/
	public void removeListener(java.lang.Object objListener, java.lang.Object objUserdata) {
	}

	/***************************************************************************
	 * 功能: 如果UiManager要显示某一个功能模块，它会调用 该模块的showMe方法以完成显示功能
	 *
	 * 参数: IParent parent 功能模块访问UiManager中的某些 数据的调用接口实现类 返回值: 无
	 **************************************************************************/
	public void showMe(nc.ui.glpub.IParent parent) {
		parent.getUiManager().add(this, this.getName());
		m_parent = parent;
		setFrame(parent.getFrame());
	}
	private void addDjlb(nc.vo.ep.dj.DJZBVO djzbvo ) throws Exception {
		addDjlb(djzbvo,true);
	}
	/**
	 * 功能:新增单据保存成功后在单据列表表尾加入一行 此处插入方法说明。 创建日期：(2001-8-30 11:39:44)
	 *
	 * @author：陈飞 算法： 备注：
	 * @param param
	 *            nc.vo.ep.dj.DJZBVO
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	private void addDjlb(nc.vo.ep.dj.DJZBVO djzbvo,boolean iscachebill) throws Exception {
		// //以下单据列表加一行
		if(iscachebill)
			addbillcache.add(djzbvo);
		if(getUIPanel2().getComponentCount()<1){
			return ;
		}
		DJZBHeaderVO newDjzbheadVo = (DJZBHeaderVO) djzbvo.getParentVO();
		AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(djzbvo);
		// refactoring
		ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();
		// 在全局变量中增加新行
		// m_Djzbvos_Hsb.put(newDjzbheadVo.getVouchid(), djzbvo);
		// add the djzbvo into data buffer
		dataBuffer.putDJZBVO(newDjzbheadVo.getVouchid(), djzbvo);

		getBillListPanel1().getHeadBillModel().addLine();

		int rowcount = getBillListPanel1().getHeadTable().getRowCount();
		getBillListPanel1().getHeadBillModel().setBodyRowVO(newDjzbheadVo, rowcount - 1);
		BillModel billModel =getBillListPanel1().getHeadBillModel();
		billModel.execLoadFormulaByRow(rowcount - 1);
//		getBillListPanel1().getBodyBillModel().execLoadFormula();
		// try {
		//
		// djlbHeadRowChange_dj(rowcount - 1);
		// //单据列表表头加载公式
		// //getBillListPanel1().getHeadBillModel().execLoadFormula();
		// } catch (Throwable e) {
		// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000453")/*@res
		// "刷新单据列表出错:"*/ + e);
		// }
		//
		// set the new row selected
		// added by wangyan in 2005-8-4
		getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(rowcount - 1, rowcount - 1);

	}

	/**
	 * 设置币种currtype的相关小数位数 此处插入方法说明。 创建日期：(2002-4-28 19:13:14)
	 */
	public void changeDigByCurr(DJZBItemVO[] items) throws Exception {
		// if (true)
		// return;
		getBillListPanel1().getBodyBillModel().clearBodyData();
		// for(int i=0,size=();i<size;i++)
		// getBillListPanel1().getBodyBillModel().setRowCount(0);
		String currtype;
		int digit = 2;
		// SetCurrency.setListbody(this.getBillListPanel1(),
		// items, getDjSettingParam().getCurrArith(),
		// ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),"")
		// ;
		for (int i = 0, size = items.length; i < size; i++) {
			if (getBillListPanel1().getBodyBillModel().getRowCount() < items.length)
				this.getBillListPanel1().getBodyBillModel().addLine();
			currtype = items[i].getBzbm();
			// CurrVO digitVO =
			// Currency.getCurrVO(this.getDjSettingParam().getPk_corp(),currtype,currtype);
			// if (digitVO.getCurrdigit() ==
			// null/*||digit>=digitVO.getCurrdigit().intValue()*/)
			// continue;
			if (null != currtype)
				digit = // digitVO.getCurrdigit().intValue();
				Currency.getCurrDigit(currtype);
			BillItem billItem = getBillListPanel1().getBodyItem("ybye");
			if (billItem != null)
				billItem.setDecimalDigits(digit);

			billItem = getBillListPanel1().getBodyItem("jfybje");
			if (billItem != null)
				billItem.setDecimalDigits(digit);

			billItem = getBillListPanel1().getBodyItem("jfybwsje");
			if (billItem != null)
				billItem.setDecimalDigits(digit);

			billItem = getBillListPanel1().getBodyItem("jfybje");
			if (billItem != null)
				billItem.setDecimalDigits(digit);

			billItem = getBillListPanel1().getBodyItem("dfybje");
			if (billItem != null)
				billItem.setDecimalDigits(digit);
			// items[i].setAttributeValue("dfybje", ((UFDouble) items[i]
			// .getAttributeValue("dfybje")).setScale(digit, 4));
			billItem = getBillListPanel1().getBodyItem("dfybwsje");
			if (billItem != null)
				billItem.setDecimalDigits(digit);

			billItem = getBillListPanel1().getBodyItem("dfybje");
			if (billItem != null)
				billItem.setDecimalDigits(digit);
			billItem = getBillListPanel1().getBodyItem("dfybsj");
			if (billItem != null)
				billItem.setDecimalDigits(digit);
			billItem = getBillListPanel1().getBodyItem("jfybsj");
			if (billItem != null)
				billItem.setDecimalDigits(digit);
			Integer intFBDigit = getDjSettingParam().getDigit_f();
			Integer intBBDigit = getDjSettingParam().getDigit_b();

			if (intFBDigit != null) {
				billItem = getBillListPanel1().getBodyItem("jffbje");
				if (billItem != null)
					billItem.setDecimalDigits(intFBDigit.intValue());

				billItem = getBillListPanel1().getBodyItem("dffbje");
				if (billItem != null)
					billItem.setDecimalDigits(intFBDigit.intValue());

				billItem = getBillListPanel1().getBodyItem("fbye");
				if (billItem != null)
					billItem.setDecimalDigits(intFBDigit.intValue());
			}

			if (intBBDigit != null) {
				billItem = getBillListPanel1().getBodyItem("jfbbje");
				if (billItem != null)
					billItem.setDecimalDigits(intBBDigit.intValue());

				billItem = getBillListPanel1().getBodyItem("dfbbje");
				if (billItem != null)
					billItem.setDecimalDigits(intBBDigit.intValue());

				billItem = getBillListPanel1().getBodyItem("bbye");
				if (billItem != null)
					billItem.setDecimalDigits(intBBDigit.intValue());
			}

			// get current djdl from data buffer,wangqiang refactoring
			String strDjdl = getDjDataBuffer().getCurrentDjdl();
			int rateDigit = 2;
			if (null != currtype){
				try{
				rateDigit = Currency.getRateDigit(getDjSettingParam().getPk_corp(), currtype,Currency.getLocalCurrPK(getDjSettingParam().getPk_corp()));// digitVO.getRatedigit().intValue();
				}catch(Exception e){
					rateDigit=2;
				}
			}
			BillItem btBbhl = getBillListPanel1().getBodyItem("bbhl");
			if (btBbhl != null)
				btBbhl.setDecimalDigits(rateDigit);

			// // 预设置单价,含税单价小数位数

			billItem = getBillListPanel1().getBodyItem("dj");
			if (billItem != null) {
				try {
					ResMessage res_num_dj = nc.ui.arap.global.PubData.getDjNum(getDjSettingParam().getPk_corp());
					if (res_num_dj.isSuccess)
						billItem.setDecimalDigits(res_num_dj.intValue);
				} catch (Exception e) {

				}
			}

			// 表体
			// //表体辅币，本币金额小数位数
			if (strDjdl.equals("ys") || strDjdl.equals("fk") || strDjdl.equals("fj") || strDjdl.equals("wf")) {
				try {
					if (getDjSettingParam().getFracCurrPK() != null) {
						BillItem jffbje = getBillListPanel1().getBodyItem("jfybje");
						jffbje.setDecimalDigits(digit);
					}

				} catch (Exception e) {
				ExceptionHandler.consume(e);
				}

			} else if (strDjdl.equals("yf") || strDjdl.equals("sk") || strDjdl.equals("sj") || strDjdl.equals("ws")) {
				try {
					if (getDjSettingParam().getFracCurrPK() != null) {
						BillItem jffbje = getBillListPanel1().getBodyItem("dfybje");
						if (jffbje != null)
							jffbje.setDecimalDigits(digit);
					}

				} catch (Exception e) {
				ExceptionHandler.consume(e);
				}

			}

			ResMessage res_num = nc.ui.arap.global.PubData.getSlNum(getDjSettingParam().getPk_corp());
			if (res_num.isSuccess == true) {

				BillItem shlItem = getBillListPanel1().getBodyItem("shl");
				if (shlItem != null)
					shlItem.setDecimalDigits(res_num.intValue);

				BillItem dfshl_billitem = getBillListPanel1().getBodyItem("dfshl");
				if (dfshl_billitem != null)
					dfshl_billitem.setDecimalDigits(res_num.intValue);

				BillItem jfshl_billitem = getBillListPanel1().getBodyItem("jfshl");
				if (jfshl_billitem != null)
					jfshl_billitem.setDecimalDigits(res_num.intValue);

				BillItem shlye_billitem = getBillListPanel1().getBodyItem("shlye");
				if (shlye_billitem != null)
					shlye_billitem.setDecimalDigits(res_num.intValue);
			}
			this.getBillListPanel1().getBodyBillModel().setBodyRowVO(items[i], i);
		}
	}

	/**
	 * 功能:翻页 创建日期：(2001-5-22 9:42:22)
	 *
	 * @return boolean
	 * @param pageIndex
	 *            int 单据列表指针
	 * @exception java.lang.Throwable
	 *                异常说明。 作者:阿飞
	 */
	public boolean changePage(int pageIndex) throws Exception {

		// 设置列表选中行
		getBillListPanel1().getHeadTable().setRowSelectionInterval(pageIndex, pageIndex);

		try {
			djlbHeadRowChange(pageIndex);
		} catch (Throwable e) {
			ExceptionHandler.consume(e);
		}
		// 联查,刷新单据
		Object djoid = getBillListPanel1().getHeadBillModel().getValueAt(pageIndex, "vouchid");
		if (djoid != null) {
			// if (getM_Syscode() == 3 || getM_Djdl().equals("ss"))
			// getArapDjPanel1().setM_Vouchid_SS(djoid.toString());
			// else
			// getArapDjPanel1().setM_Vouchid(djoid.toString());

			nc.vo.ep.dj.DJZBVO cur_djzbvo = null;
			// refactoring
			ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();

			cur_djzbvo = (nc.vo.ep.dj.DJZBVO) (dataBuffer.getDJZBVO((String) djoid));

			/* 添加加锁操作 add by SongTao 2004-08-03 */

			// if (head.getIslocked() != null &&
			// head.getIslocked().booleanValue())
			// //已经加锁
			// ;
			// else {
			// //加业务锁
			// boolean locked = false;
			// try {
			// locked = KeyLock
			// .lockKey(djoid.toString(), getDjSettingParam()
			// .getPk_user(), null);
			// head.setIslocked(new UFBoolean(locked));
			// } catch (Exception e2) {
			// Logger.error(e2.getMessage(), e2);
			// head.setIslocked(new UFBoolean(locked));
			//
			// }
			// }
			/* end */
			getArapDjPanel1().setDj(cur_djzbvo);
		}
		return true;

	}


	/**
	 * 功能:改变页签(单据和列表之间) 创建日期：(2001-5-21 15:07:39) 作者:阿飞 参数:tabIndex 页签码,isChange
	 * false不切换页签
	 */
	public void changeTab(int tabIndex, boolean isChange) {

		if (getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE) {
			//BillModel billModel  = this.getBillListPanel1().getHeadBillModel();
			int row = this.getBillListPanel1().getHeadTable().getSelectedRow();
			int rowcount_h = getBillListPanel1().getHeadBillModel().getRowCount();
			if (row >= rowcount_h)
				row = rowcount_h - 1;
			if(rowcount_h>0&&row<0)
				row=0;
			DJZBVO cur_djzbvo = null;
			DJZBHeaderVO head = null;
			DJZBItemVO[] items = null;
			if(row>=0){
				String djoid = getBillListPanel1().getHeadBillModel().getValueAt(row,"vouchid").toString();
				cur_djzbvo = (nc.vo.ep.dj.DJZBVO) (this.getDjDataBuffer().getDJZBVO(djoid));
				head = (nc.vo.ep.dj.DJZBHeaderVO) (cur_djzbvo.getParentVO());
				items = (nc.vo.ep.dj.DJZBItemVO[]) (cur_djzbvo.getChildrenVO());
			}
			if (head == null) {
				cur_djzbvo = getDjDataBuffer().getCurrentDJZBVO();
				if (cur_djzbvo != null)
					head = (nc.vo.ep.dj.DJZBHeaderVO) (cur_djzbvo.getParentVO());
			}
			((java.awt.CardLayout)this.getLayout()).show(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
							"UPT2006030102-000021")/* @res "单据" */);
			this.setCurrentpage(ArapBillWorkPageConst.CARDPAGE);
			if (getJPanel1().getComponentCount() < 1) { // 如果是初始化,第一次切换到单据
				this.initBillCardTemplet() ;
			}

			// 联查询单据,刷新单据
			if (rowcount_h > 0) {
				try {
					// 设置单据
					this.getArapDjPanel1().setDj(cur_djzbvo);

				} catch (Throwable e) {
					Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"2006030102", "UPP2006030102-000441") + e);
				}

			} else{
				if(null==getDjDataBuffer().getCurrentDjlxbm()){
					this.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2008",
					"UPP2008-000115")/* @res "未分配默认单据类型，请到集团单据类型设置节点进行分配" */);
					this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2008",
							"UPP2008-000115"));
					return ;
				}
				this.getArapDjPanel1().loadDjTemplet(null, getDjSettingParam().getPk_corp(),
						this.getDjDataBuffer().getCurrentDjdl(),getDjDataBuffer().getCurrentDjlxbm());
				this.getArapDjPanel1().getBillCardPanelDj() .getBillData().clearViewData();
			}
			getArapDjPanel1().getBillCardPanelDj().setEnabled(false);
			if (this  instanceof FiFlowPanel) {
				try {
					((FiFlowPanel) this ).cpyAndAddBoStatListener();
					((FiFlowPanel) this ).updateLocalButtons();
				} catch (Exception e) {
					ExceptionHandler.consume(e);
				}
			}
			if (getDjDataBuffer().getCurrentDJZBVO() != null) {
				if (head .getDjzt() == DJZBVOConsts.m_intDJStatus_UNSaved) {
					EditAction ea = new EditAction();
					ea.setActionRunntimeV0(this );
					try {
						ea.edit();
					} catch (BusinessException e) {
						ExceptionHandler.consume(e);
					}
				}
			}

		} else {
			try {
			((java.awt.CardLayout) this.getLayout()).show(((DjPanel) this ),
					nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
							"UPT2006030102-000049")/* @res "列表" */);
//			DJZBVO djzbvo=this.getDjDataBuffer().getCurrentDJZBVO();
			if (getUIPanel2().getComponentCount() < 1) {
				initBillListTemplet();
				if(addbillcache.size()>0){
					for(DJZBVO dj:addbillcache)
							this.addDjlb(dj,false);
				}
				if(this.editbillcache.size()>0){
					for(DJZBVO dj:this.editbillcache){
						this.editDjlb(dj,false);
					}
				}
			}
			this .setCurrentpage(ArapBillWorkPageConst.LISTPAGE);
//			DJZBItemVO[] items=djzbvo==null?null:(DJZBItemVO[])djzbvo.getChildrenVO();
			addbillcache.addAll(editbillcache);
			// 卡片切换到列表时将表体项带入表头
			int row = this.getBillListPanel1().getHeadTable().getSelectedRow();
			int rowcount_h = getBillListPanel1().getHeadBillModel().getRowCount();
			if (row >= rowcount_h)
				row = rowcount_h - 1;
			if(rowcount_h>0&&row<0)
				row=0;
			BillModel billModel =getBillListPanel1().getHeadBillModel();

			if(row==-1&&this.getDjDataBuffer().getCurrentDJZBVO()!=null){
				DJZBHeaderVO head=(DJZBHeaderVO)getDjDataBuffer().getCurrentDJZBVO().getParentVO();
				refDjLb(new DJZBHeaderVO[]{head});
				row=0;
//				billModel.execLoadFormulaByRow(row);
			}

			// 更新列表状态下的表头、表体显示
			String strCurrentPK = getDjDataBuffer().getCurrentDJZBVOPK();
			if (null != strCurrentPK) {
				if (row == -1) {
					row = ((DjPanel) this.getParent()).getHeadRowInexByPK(strCurrentPK);
				} else if (!strCurrentPK.equals(billModel.getValueAt(row,"vouchid"))) {
					for (int i = 0; i < billModel.getRowCount(); i++) {
						if (strCurrentPK.equals(billModel.getValueAt(row,"vouchid"))) {
							row = i;
							break;
						}
					}
				}
			}
			djlbHeadRowChange(row);
			if(null!=this.getDjDataBuffer().getCurrentDJZBVO())
			doSpecialFields(row,getDjDataBuffer().getCurrentDJZBVO());
			billModel.execLoadFormulaByRow(row);
//			getBillListPanel1().getBodyBillModel().execLoadFormula();
			} catch (Exception e) {
				ExceptionHandler.consume(e);
			}
			this.addbillcache.clear();
			this.editbillcache.clear();
		}
	}
	protected void doSpecialFields(int row,DJZBVO djzbvo){
		DJZBHeaderVO head=(DJZBHeaderVO)djzbvo.getParentVO();
//		DJZBItemVO[] items=(DJZBItemVO[])djzbvo.getChildrenVO();
		BillModel bm=this.getBillListPanel1().getHeadBillModel();
		bm.setValueAt(head.getBzbm(), row, "bzbm");
		bm.setValueAt(head.getPayman(), row, "payman");
		bm.setValueAt(head.getPaydate(), row, "paydate");
		bm.setValueAt(DJZBHeaderVO.getZzztMc(head.getZzzt(), head), row, "zzzt_mc");
		DjFilterHelper.fillVerifyStatus(djzbvo);
		bm.setValueAt(head.getVerifystatus(), row, "verifystatus");

	}
	/**
	 * 功能:用户选择单据列表头不同的行(刷新单据列表表体) 简要流程: 创建日期：(2001-6-8 10:33:47)
	 *
	 * @author：陈飞
	 * @param billlistRowIndex
	 *            int 单据列表头行序号
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public boolean djlbHeadRowChange(int billlistRowIndex) throws Exception {
		if (billlistRowIndex < 0)
			return true;
		String djzboid = getBillListPanel1().getHeadBillModel().getValueAt(billlistRowIndex, "vouchid").toString();
		String billtypecode = getBillListPanel1().getHeadBillModel().getValueAt(billlistRowIndex, "djlxbm").toString();
		String pk_corp = getBillListPanel1().getHeadBillModel().getValueAt(billlistRowIndex, "dwbm").toString();
		ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();
		DjLXVO cur_djlxvo = FiPubDataCache.getBillType(billtypecode,pk_corp);
		if (cur_djlxvo != null) {
			dataBuffer.setCurrentBillTypeInfo(cur_djlxvo);
			getArapDjPanel1().setTitleText(cur_djlxvo.getDjlxmc());
		}
		DJZBItemVO[] djzbitemvo = null;
		nc.vo.ep.dj.DJZBVO curdjzbvo = dataBuffer.getDJZBVO(djzboid);
		djzbitemvo = curdjzbvo.getChildrenVO() == null ? null : (DJZBItemVO[]) curdjzbvo.getChildrenVO();
		dataBuffer.setCurrentDJZBVO(curdjzbvo);
		Boolean isselected =(Boolean) getBillListPanel1().getHeadBillModel().getValueAt(billlistRowIndex, "isselected");
		for(DJZBItemVO vo: djzbitemvo){
			vo.setIsselected(new UFBoolean(isselected));
		}
		//
		try {
			changeDigByCurr(djzbitemvo);
		} catch (Exception e) {
			ExceptionHandler.consume(e);
		}
		getBillListPanel1().getBodyBillModel().execLoadFormula();
		try {
			String[] strArrayCDMBegin = ((String[]) MyClientEnvironment.getValue(this.getDjSettingParam().getPk_corp(),
					"CDMBegin", null));
			if (strArrayCDMBegin != null && strArrayCDMBegin.length != 0) {
				String[] formular = new String[] { "htmc->getColvalue(v_fi_fi_code,code,pk_id,htbh)" };
				getBillListPanel1().getBodyBillModel().execFormulas(formular);
			}

		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
 		String strLang = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000004", null,
				new String[] { String.valueOf(billlistRowIndex + 1) });
		showHintMessage(strLang);
		return true;

	}
	private void editDjlb(nc.vo.ep.dj.DJZBVO djzbvo) throws Exception {
		editDjlb(djzbvo,true);
	}
	/**
	 * 功能:修改单据保存成功后修改单据列表相应行 此处插入方法说明。 创建日期：(2001-8-30 11:39:44)
	 *
	 * @author：陈飞 算法： 备注：
	 * @param param
	 *            nc.vo.ep.dj.DJZBVO
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	private void editDjlb(nc.vo.ep.dj.DJZBVO djzbvo,boolean iscachebill) throws Exception { // //以下修改单据列表对应的行
		if(iscachebill)
			this.editbillcache.add(djzbvo);
		if(getUIPanel2().getComponentCount()<1)
			return ;
		DJZBHeaderVO head = (DJZBHeaderVO) (djzbvo.getParentVO());
		AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(djzbvo);
		int row = getBillListPanel1().getHeadTable().getSelectedRow();
		if (head.getVouchid().equals(getBillListPanel1().getHeadBillModel().getValueAt(row, "vouchid").toString()))
			;
		else {
			for (int i = 0; i < getBillListPanel1().getHeadBillModel().getRowCount(); i++) {
				if (head.getVouchid()
						.equals(getBillListPanel1().getHeadBillModel().getValueAt(i, "vouchid").toString())) {
					row = i;
					break;
				}
			}
		}
		BillModel billModel =getBillListPanel1().getHeadBillModel();
		billModel.execLoadFormulaByRow(row);
//		getBillListPanel1().getBodyBillModel().execLoadFormula();
		this.getDjDataBuffer().putDJZBVO(head.getVouchid(), djzbvo); // 设置单据集合中的一张单据
		try {
			getBillListPanel1().getHeadBillModel().setBodyRowVO(head, row);
			// getBillListPanel1().getHeadTable().getSelectionModel()
			// .setSelectionInterval(row, row);
			//
			// djlbHeadRowChange_dj(row);
			// 加载公式
			// getBillListPanel1().getHeadBillModel().execLoadFormula();
		} catch (Throwable e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453")/*
																											 * @res
																											 * "刷新单据列表出错:"
																											 */
					+ e);
		} // //////////////////////////////////////////////////////////////////////////////////
	}

	public Vector<String> getAllSelectedDJPK() {
		Vector<String> vSelectedPK = new Vector<String>();
		 for(DJZBVO vo:getListAllSelectedVOs()){
			 try {
				vSelectedPK.add(vo.getParentVO().getPrimaryKey());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				ExceptionHandler.consume(e);
			}
		 }
		return vSelectedPK;

	}

	public void reserveListSelected() {
		for (int row = 0; row < getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
			String djzboid = null; // 单据主表oid
			djzboid = getBillListPanel1().getHeadBillModel().getValueAt(row, "vouchid").toString();
			DJZBVO vo = this.getDjDataBuffer().getDJZBVO(djzboid);
			//按代码的写法分析：isselected 可以得到三个数据null、true 、false
			if (this.getDjDataBuffer().getListSelectedVOs().contains(vo))
				this.getDjDataBuffer().getListSelectedVOs().remove(vo);
			if (getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected") == null)
				continue;
			if (((Boolean) (getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected"))).booleanValue()) {

				// 签字确认数据库中的一张单据
				if (!this.getDjDataBuffer().getListSelectedVOs().contains(vo))
					vo.getParentVO().setAttributeValue("isselected",new UFBoolean(true));
					DJZBItemVO[] items=(DJZBItemVO[])vo.getChildrenVO();
					for(DJZBItemVO item:items){
						item.setAttributeValue("isselected",new UFBoolean(true));
					}
					this.getDjDataBuffer().getListSelectedVOs().add(vo);
			}
		}

	}

	public List<DJZBVO> getListAllSelectedVOs() {

		reserveListSelected();
		return this.getDjDataBuffer().getListSelectedVOs();
	}

	/**
	 * author:wangqiang create time:2004-11-3 function: return the index array
	 * of all the selected dj in the list panel
	 *
	 * @return
	 */
	public Vector<Object> getAllSelectedDJIndex() {
		Vector<Object> vSelectedPK = new Vector<Object>();
		for (int row = 0; row < getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
			if (getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected") == null)
				continue;
			if (((Boolean) (getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected"))).booleanValue()) {
				vSelectedPK.addElement(new Integer(row));
			}
		}
		return vSelectedPK;

	}

	public void setDjlxbm() {

	}

	/**
	 * author:wangqiang create time:2004-11-3 function: return the index array
	 * of all the selected dj in the list panel
	 *
	 * @return
	 */
	public Vector<Integer> getAllBodySelectedDJIndex() {
		Vector<Integer> vSelectedPK = new Vector<Integer>();
		for (int row = 0; row < getBillListPanel1().getBodyBillModel().getRowCount(); row++) {
			if (getBillListPanel1().getBodyBillModel().getValueAt(row, "isselected") == null)
				continue;
			if (((Boolean) (getBillListPanel1().getBodyBillModel().getValueAt(row, "isselected"))).booleanValue()) {
				vSelectedPK.addElement(new Integer(row));
			}
		}
		return vSelectedPK;

	}

	/**
	 * 返回 ArapDjPanel1 特性值。
	 *
	 * @return nc.ui.arap.pubdj.ArapDjPanel
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.arap.pubdj.ArapDjPanel getArapDjPanel1() {

		if (ivjArapDjPanel1 == null) {
			try {
				ivjArapDjPanel1 = new nc.ui.arap.pubdj.ArapDjPanel(this.getDjSettingParam(),this);
				// this.audit=AuditCache.getInstance(this.getClientEnvironment().getUser().getPrimaryKey(),this.getClientEnvironment().getCorporation().getPrimaryKey());
				// this.cache=new
				// DjlxTempletCache(this.audit,ivjArapDjPanel1,this.pzglh);
				if(!UFBoolean.TRUE.equals(this.getIsAloneNode())){
					this.setTempletCache(new DjlxTempletCacheNew(this.getDjSettingParam().getPk_user(), this.getDjSettingParam()
							.getPk_corp(), this.getNodeCode(), this.pzglh, ivjArapDjPanel1));

					ivjArapDjPanel1.setTempletCache(this.getTempletCache());
				}
				ivjArapDjPanel1.setName("ArapDjPanel1");
				ivjArapDjPanel1.setAutoscrolls(true);
				// user code begin {1}
				ivjArapDjPanel1.setM_Parent(this);
				// ivjArapDjPanel1.setDjOid("00019040394156800000");//测试用发版时这句去掉
				// refactoring:pass the pk corp through SettingParam
				// ivjArapDjPanel1.setM_Pk_corp(getClientEnvironment().getCorporation().getPk_corp());
				// ivjArapDjPanel1.setM_Date(getClientEnvironment().getDate());
				// ivjArapDjPanel1.setM_Kjnd(getClie.ntEnvironment().getAccountYear());
				// ivjArapDjPanel1.setM_Kjqj(getClientEnvironment().getAccountMonth());
				// ivjArapDjPanel1.setM_Lrr(getClientEnvironment().getUser().getPrimaryKey());
				// refactoring
				// ivjArapDjPanel1.setM_Syscode(getDjSettingParam().getSyscode());
				ivjArapDjPanel1.getBillCardPanelDj().setShowThMark(true);
				// ivjArapDjPanel1.setM_Qyrq(getClientEnvironment().getStartDateOfAccountPeriod());//账套启用日期
				// ivjArapDjPanel1.setM_Qyrq(getM_Qyrq());//账套启用日期
				// user code end
				// refactoring:set the data buffer to ARAPDjPanel
				ivjArapDjPanel1.setDjDataBuffer(this.getDjDataBuffer());
				// ivjArapDjPanel1.setDjSettingParam(this.getDjSettingParam());
				getArapDjPanel1().getBillCardPanelDj().addBodyMouseListener(
						new nc.ui.pub.bill.BillTableMouseListener() {
							public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
								try {
									refreshBtnStats();
								} catch (Throwable ee) {
								}
							}
						});
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		} else {
			// if (ivjArapDjPanel1.getM_Syscode() != getM_Syscode())
			// ivjArapDjPanel1.setM_Syscode(getM_Syscode());
		}
		return ivjArapDjPanel1;
	}

	/**
	 * 返回 BillListPanel1 特性值。
	 *
	 * @return nc.ui.pub.bill.BillListPanel
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.bill.BillListPanel getBillListPanel1() {
		//ivjBillListPanel1=null;
		return ivjBillListPanel1;
	}

	protected void addBillLinstener(nc.ui.pub.bill.BillListPanel ivjBillListPanel1) {
		ivjBillListPanel1.addMouseListener(new nc.ui.pub.bill.BillTableMouseListener() {
			public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
				if (e.getPos() == 0)
					changeTab(getCurrWorkPage(), true);
			}
		});
	}

	protected void addBillLinstener(nc.ui.pub.bill.BillCardPanel ivjBillCardPanel1) {
		ivjBillListPanel1.addMouseListener(new nc.ui.pub.bill.BillTableMouseListener() {
			public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
				if (e.getPos() == 0)
					changeTab(getCurrWorkPage(), true);
			}
		});
	}

	/**
	 * 费用结算节点模板的加载比较特殊，因此其入口节点要重写这个方法。 rocking 2005-9-10
	 */
	protected void loadBillListTemplate()
	{
//		ivjBillListPanel1.setNodeKey("DW");
		ivjBillListPanel1.loadTemplet(
		        getNodeCode()
		        //getModuleCode()
		        , null,
				getDjSettingParam().getPk_user(), getDjSettingParam()
						.getPk_corp(),"DW");
		String [] strItems=new String[] {((AbstractRuntime)this).getProxy().getDefBodyConst()};//"供应链/ARAP单据体"
		String [] strHeads=new String[] {((AbstractRuntime)this).getProxy().getDefHeadConst()};//"供应链/ARAP单据头"
        String [] strPrefix=new String[] {"zyx"};
        try {
			new ListDefShowUtil(this.getBillListPanel1(),new DefaultDefShowStrategyByBillItem()).showDefWhenRef(strItems, strPrefix,false);
			new ListDefShowUtil(this.getBillListPanel1() ,new DefaultDefShowStrategyByBillItem()).showDefWhenRef(strHeads, strPrefix,true);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			ExceptionHandler.consume(e);
		}
		try {
			//Logger.debug("单据大类:" + getM_Djdl());
			if(null!=ivjBillListPanel1.getBodyItem("wldx")){
				((nc.ui.pub.beans.UIComboBox) ivjBillListPanel1
						.getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemKH());
				((nc.ui.pub.beans.UIComboBox) ivjBillListPanel1
						.getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemGYS());

				((nc.ui.pub.beans.UIComboBox) ivjBillListPanel1
						.getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemBM());
				((nc.ui.pub.beans.UIComboBox) ivjBillListPanel1
						.getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemYWY());
				//ivjBillListPanel1.getBodyItem("wldx").setWithIndex(true);
			}
		} catch (Throwable e) {
			ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000850")/*@res "设置往来对象,这个错误也许可以勿略:"*/ + e);
		}
		try {
			nc.ui.pub.beans.UIRefPane zy_ref = (nc.ui.pub.beans.UIRefPane) getBillListPanel1()
					.getBodyItem("zy").getComponent();
			zy_ref.setRefNodeName("常用摘要");	/*-=notranslate=-*/
			//zy_ref.setReturnCode(false);
			zy_ref.setButtonVisible(true);
		} catch (Exception e) {
			ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000846")/*@res "常用摘要"*/ + e);
		}
		try{
			ivjBillListPanel1.getParentListPanel().setTotalRowShow(true);
			ivjBillListPanel1.getChildListPanel().setTotalRowShow(true);
			nc.vo.pub.bill.BillRendererVO voCell = new nc.vo.pub.bill.BillRendererVO();
			voCell.setShowThMark(true);
			voCell.setShowZeroLikeNull(true);
			ivjBillListPanel1.getChildListPanel().setShowFlags(voCell);
			ivjBillListPanel1.getParentListPanel().setShowFlags(voCell);
			ivjBillListPanel1.setEnabled(true);
			addBillLinstener(ivjBillListPanel1);
	        Integer intFBDigit = getDjSettingParam().getDigit_f();
	        Integer intBBDigit = getDjSettingParam().getDigit_b();
			BillItem bbje=ivjBillListPanel1.getHeadBillModel().getItemByKey("bbje");
			bbje.setDecimalDigits(intBBDigit);
			BillItem ybje=ivjBillListPanel1.getHeadBillModel().getItemByKey("ybje");
			ybje.setDecimalDigits(4);
			BillItem fbje=ivjBillListPanel1.getHeadBillModel().getItemByKey("fbje");
			if(null!=intFBDigit)
				fbje.setDecimalDigits(intFBDigit);
		}catch(Exception e){
			ExceptionHandler.consume(e);
		}

	}

	/**
	 * 此处插入方法描述。 创建日期：(2004-4-8 9:17:58)
	 *
	 * @author:陈飞
	 * @return nc.vo.arap.global.BusiTransVO[]
	 */
	public nc.vo.arap.global.BusiTransVO[] getCurBusiTransVO() {
		return m_CurBusiTransVO;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-5 16:05:45)
	 *
	 * @author：陈飞 功能:通过单据表头主健找数组中对应的index 算法： 备注：
	 * @return int
	 */
	public int getIndexByKey(DJZBHeaderVO[] vos, String vouchid) {
		int i = -1;
		for (i = 0; i < vos.length; i++) {
			if (vos[i].getPrimaryKey().equals(vouchid))
				break;
		}
		if (i < vos.length)
			return i;
		else
			return -1;
	}

	/**
	 * author:wangqiang create time:2004-12-10 function :given a djzbitemvo,this
	 * function return its index on the card panel
	 *
	 * @param strItemPK:
	 *            PK of the DJZBItemVO
	 * @return
	 */
	public int getBodyShowInexByPK(String strItemPK) {
		int intRowCount = getArapDjPanel1().getBillCardPanelDj().getBillTable().getRowCount();
		for (int index = 0; index < intRowCount; index++) {
			// get the item pk of the row on the panel
			String strRowItemPK = (String) getArapDjPanel1().getBillCardPanelDj().getBodyValueAt(index, "fb_oid");

			if (strItemPK.trim().equals(strRowItemPK.trim()))// the
																// corresponding
																// row is found
																// if it is true
																// here
				return index;
		}
		return -1;
	}

	/**
	 * 返回 JPanel1 特性值。
	 *
	 * @return javax.swing.JPanel
	 */
	// getJPanel1().add(getArapDjPanel1(), "Center");
	/* 警告：此方法将重新生成。 */
	public javax.swing.JPanel getJPanel1() {
		if (ivjJPanel1 == null) {
			try {
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.BorderLayout());
				// getJPanel1().add(getArapDjPanel1(), "Center");
				// user code begin {1}

				// getJPanel1().add(getArapDjPanel1(), "Center");
				ivjJPanel1
						.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000021")/*
																													 * @res
																													 * "单据"
																													 */);
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
	 * 此处插入方法说明。 创建日期：(2001-8-31 11:07:43)
	 *
	 * @author：陈飞 功能: 算法： 备注：
	 * @return int
	 */
	public int getM_TabIndex() {
		return m_TabIndex; // 此方法已废弃，Controller包删除后移除
	}

	// /**
	// * 功能：得到分配给当前用户的查询模板id 作者：宋涛 创建时间：(2001-8-16 16:16:03) 参数： <|>返回值： 算法：
	// 异常描述：
	// *
	// * @return java.lang.String
	// */
	// private String getQryTempletID() {
	// return "00018479175506409955";
	// }

	/**
	 * author : wangqiang create time : 2004-08-07 function : get the all the
	 * Item VOs of the rows selected by user in the bill body
	 */
	public DJZBItemVO[] getSelectedBodyRowVO(DJZBHeaderVO selectedHeader) {
		// get Header VO PK
		String strHeaderPK = selectedHeader.getVouchid();
		// get corresponding DJZBVO according to header PK
		nc.vo.ep.dj.DJZBVO djzbvo = this.getDjDataBuffer().getDJZBVO(strHeaderPK);
		// get item vos,note that once this method can be called,the item vos
		// array could not be null
		DJZBItemVO[] itemVOs = (DJZBItemVO[]) djzbvo.getChildrenVO();

		Vector<DJZBItemVO> vItemVOs = new Vector<DJZBItemVO>();
		for (int row = 0; row < getBillListPanel1().getBodyBillModel().getRowCount(); row++) {
			if (!Boolean.TRUE.equals(getBillListPanel1().getBodyBillModel().getValueAt(row, "isselected") ))
				continue;
			if (((Boolean) (getBillListPanel1().getBodyBillModel().getValueAt(row, "isselected"))).booleanValue()) {
				String strItemPK = getBillListPanel1().getBodyBillModel().getValueAt(row, "fb_oid").toString();
				// call assistant method to get the selected Item VO by Item PK
				DJZBItemVO selectedItemVO = getDjDataBuffer().getDJZBItemVOByItemPK(strItemPK, itemVOs);
				vItemVOs.addElement(selectedItemVO);
			}
		}
		DJZBItemVO[] selectedItemVOArray = new DJZBItemVO[vItemVOs.size()];
		vItemVOs.copyInto(selectedItemVOArray);
		return selectedItemVOArray;
	}

	/**
	 * author : wangqiang create time : 2004-08-07 function : get the all the
	 * header VO of the rows selected by user
	 */
	public Vector<DJZBHeaderVO> getSelectedHeadRowVO() {
		Vector<DJZBHeaderVO> vHeaderPK = new Vector<DJZBHeaderVO>();
		for (int row = 0; row < getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
			if (getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected") == null)
				continue;
			if (((Boolean) (getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected"))).booleanValue()) {
				String strHeadPK = getBillListPanel1().getHeadBillModel().getValueAt(row, "vouchid").toString();
				nc.vo.ep.dj.DJZBVO vo = this.getDjDataBuffer().getDJZBVO(strHeadPK);
				DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
				vHeaderPK.addElement(head);
			}
		}
		return vHeaderPK;
	}

	/**
	 * a功能: 作者：宋涛 创建时间：(2004-10-21 11:23:21) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
	 *
	 *
	 * @return nc.ui.ep.dj.DjPanel.DjStateChange
	 */
	public nc.ui.ep.dj.DjPanel.DjStateChange getStateChange() {
		return m_djStateChange;
	}

	/**
	 * 返回 UIPanel2 特性值。
	 *
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIPanel getUIPanel2() {
		if (ivjUIPanel2 == null) {
			try {
				ivjUIPanel2 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel2.setName("UIPanel2");
				ivjUIPanel2.setAutoscrolls(true);
				ivjUIPanel2.setLayout(new java.awt.BorderLayout());
				// user code begin {1}
				ivjUIPanel2
						.setName(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000049")/*
																													 * @res
																													 * "列表"
																													 */);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel2;
	}

	/**
	 * 返回 VerifyPanel 特性值。
	 *
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	public javax.swing.JPanel getVerifyPanel() {
		if (ivjVerifyPanel == null) {
			try {
				ivjVerifyPanel = new javax.swing.JPanel();
				ivjVerifyPanel.setName("VerifyPanel");
				ivjVerifyPanel.setLayout(new java.awt.BorderLayout());
				// user code begin {1}
				ivjVerifyPanel.setName(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("2006030102", "UPT2006030102-000053")/*
																			 * @res
																			 * "即时核销"
																			 */);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjVerifyPanel;
	}

	/**
	 * 每当部件抛出异常时被调用
	 *
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		Logger.warn(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000281")
				+ exception.getMessage(), exception);
		// nc.bs.logging.Log.getInstance(this.getClass()).warn(e.getMessage(),e);;
	}

	protected void initBillCardTemplet() {
		// user code begin {1}
		getJPanel1().add(this.getArapDjPanel1(), "Center");
		try {
			this.getArapDjPanel1().addDjStateChangeListener(getStateChange());
		} catch (Throwable e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000438") + e);
		}
		this.getArapDjPanel1().setTabTitle();
//		try {
//			this.getArapDjPanel().changeTab2(0,bInitializing);
//		} catch (Throwable e) {
//			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
//					"2006030102", "UPP2006030102-000439")  + e);
//		}
		try {
			this.getArapDjPanel1().setM_DjState(0);
		} catch (Throwable e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"2006030102", "UPP2006030102-000440") + e);
		}
	}

	protected void initBillListTemplet()  {
		// user code begin {1}
		if (ivjBillListPanel1 == null) {
			try {
				ivjBillListPanel1 = new nc.ui.pub.bill.BillListPanel(); //(false);
				ivjBillListPanel1.setName("BillListPanel1");
				loadBillListTemplate();
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
			getUIPanel2().add(this.getBillListPanel1(), "Center");
			this.getBillListPanel1().addEditListener(
					TempletController.getBillEditListener(this.getNodeCode(),
							IArapTempletController.ListHeadEvent, this)

			);
			this.getBillListPanel1().addBodyEditListener((nc.ui.pub.bill.BillEditListener)
					TempletController.getBillEditListener(this.getNodeCode(),
							IArapTempletController.ListBodyEvent, this));
	}

	/*
	 * 接口:从审批流转到单据管理时初始化数据
	 *
	 */
	public void initData(nc.vo.pub.msg.MessageVO msgvo) {
		DJZBHeaderVO djzbheadervo = null;
		// nc.bs.logging.Log.getInstance(this.getClass()).warn("stamp in
		// djPanel");
		try {
			if (getDjSettingParam().getSyscode() == 3)
				djzbheadervo = Proxy.getIArapItemPrivate().findSsHeaderByPK(msgvo.getBillPK());
			else
				djzbheadervo = getProxy().getIArapBillPrivate().findHeaderByPrimaryKey(msgvo.getBillPK());

		} catch (Exception e) {
			if (e instanceof nc.vo.ep.dj.ShenheException) {
				Logger.debug(((nc.vo.ep.dj.ShenheException) e).m_ResMessage.strMessage);
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000449")/*
																													 * @res
																													 * "单据加锁失败，查询失败"
																													 */);
				return;
			}
			if (e instanceof java.rmi.RemoteException) {
				if (((java.rmi.RemoteException) e).detail instanceof nc.vo.ep.dj.ShenheException) {
					java.rmi.RemoteException remE = (java.rmi.RemoteException) e;
					nc.vo.ep.dj.ShenheException shE = (nc.vo.ep.dj.ShenheException) (remE.detail);
					Logger.debug(shE.m_ResMessage.strMessage);
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000449")/*
																														 * @res
																														 * "单据加锁失败，查询失败"
																														 */);
					return;
				}
			}
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000450")/*
																											 * @res
																											 * "查询单据列表出错:"
																											 */
					+ e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000451")/*
																												 * @res
																												 * "查询失败"
																												 */);
			return;
		}

		// 以上查询单据列表表头

		if (djzbheadervo == null) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000424")/*
																												 * @res
																												 * "没有相关单据"
																												 */);
			// refactoring
			// m_Djzbvo = null;
			nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();
			// getArapDjPanel1().setM_Djzbvo(null);
			dataBuffer.setCurrentDJZBVO(null);
			return;
		}

		DJZBHeaderVO[] djzbheadervos = new DJZBHeaderVO[1];
		djzbheadervos[0] = djzbheadervo;

		// 刷新单据列表

		try {
			refDjLb(djzbheadervos);

			// 重新设置单据集合
			// m_Djzbvos_Hsb = new Hashtable();
			this.getDjDataBuffer().clearBuffer();
			nc.vo.ep.dj.DJZBVO temp_djzbvo = null;

			temp_djzbvo = new nc.vo.ep.dj.DJZBVO();
			temp_djzbvo.setParentVO(djzbheadervo);
			temp_djzbvo.setChildrenVO(null);
			this.getDjDataBuffer().putDJZBVO(djzbheadervo.getVouchid(), temp_djzbvo);

			getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
			djlbHeadRowChange(0);

		} catch (Throwable e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453")/*
																											 * @res
																											 * "刷新单据列表出错:"
																											 */
					+ e);
		}

		// 加载公式
		try {
			getBillListPanel1().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037")/*
																											 * @res
																											 * "加载公式出错:"
																											 */
					+ e);
		}
		try {
			// getBillListPanel1().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037")/*
																											 * @res
																											 * "加载公式出错:"
																											 */
					+ e);
		}

		changeTab(1, true);

		// m_boCloseOrNo.setVisible(false);
		// m_boNewByBudget.setVisible(false);
		// m_boNewByBudgetYT.setVisible(false);
		// m_boSplitSSItem.setVisible(false);
		// updateButton(m_boNewByBudget);
		// updateButton(m_boNewByBudgetYT);
		// updateButton(m_boSplitSSItem);
		// updateButton(m_boCloseOrNo);

	}
	protected void initialize( ) {
		initialize(true,true,false);
	}
	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	protected void initialize(boolean invoketempletcache,boolean islist,boolean iscard) {
		try {
			ButtonObject[] btArray = getDjButtons();
			setButtons(btArray);
			// user code end
			if (ILinkType.NONLINK_TYPE == this.getLinkedType()) {
				MyClientEnvironment.refresh();
			}
			setName("DjPanel");
			setAutoscrolls(true);
			setLayout(new java.awt.CardLayout());
			setSize(774, 419);
			add(getUIPanel2(), getUIPanel2().getName());
			add(getJPanel1(), getJPanel1().getName());
			add(getVerifyPanel(), getVerifyPanel().getName());
			this.setCurrentpage(islist?ArapBillWorkPageConst.LISTPAGE:ArapBillWorkPageConst.CARDPAGE);
			if(islist){
				this.initBillListTemplet();
				((java.awt.CardLayout) this.getLayout()).show(((DjPanel) this ),
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
								"UPT2006030102-000049")/* @res "列表" */);
			}else{
				this.initBillCardTemplet();
				((java.awt.CardLayout)this.getLayout()).show(this,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
								"UPT2006030102-000021")/* @res "单据" */);
				if(iscard){
					this.getArapDjPanel1().loadDjTemplet(null, getDjSettingParam().getPk_corp(),
							this.getDjDataBuffer().getCurrentDjdl(),getDjDataBuffer().getCurrentDjlxbm());
					this.getArapDjPanel1().getBillCardPanelDj() .getBillData().clearViewData();
					getArapDjPanel1().getBillCardPanelDj().setEnabled(false);
				}
				if (this  instanceof FiFlowPanel) {
					try {
						((FiFlowPanel) this ).cpyAndAddBoStatListener();
						((FiFlowPanel) this ).updateLocalButtons();
					} catch (Exception e) {
						ExceptionHandler.consume(e);
					}
				}
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
			this.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000084")/*@res "初始化节点出错"*/);
			return ;
		}
		// setM_TabIndex(-1);////已废弃，Controller包删除后移除


//		try {
//			changeTab(this.getCurrWorkPage(), false);
//		} catch (Throwable e) {
//			// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000912")/*@res
//			// "初始化时changeTab出错:"*/ + e);
//		}
		this.refreshBtnStats();
		if(invoketempletcache)
			this.getArapDjPanel1().invokeTempletCache();
	}


	public boolean checkID( ){
//		if((bo.getParent()!=null&&bo.getParent()==this.m_boAdd)
//				||(bo.getParent()!=null&&bo.getParent()==this.m_boCpy)
//				||"修改".equalsIgnoreCase(bo.getCode())
//				||"确认".equalsIgnoreCase(bo.getCode())
//						||"增加".equalsIgnoreCase(bo.getCode())
//			){
			boolean checked;
//			boolean must=false;
            try {
                checked = checkAuthen(  );
            } catch (Exception e) {
            	ExceptionHandler.consume(e);
                this.showErrorMessage(e.getMessage());
    	        return false;
            }
            if(!checked){
		        this.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2008","UPP2008-000102")/*@res "身份认证未通过，操作失败!"*/);
		        return false;
            }
//		}
	return true;
}
	/**
	 * 客户端身份认证
	 * @return
	 * @throws Exception
	 */
	 public boolean checkAuthen(  )throws Exception{

	    String djdl=this.getDjDataBuffer().getCurrentDjdl();
	    String pk_corp=this.getDjSettingParam().getPk_corp();
        String billtypecode=this.getDjDataBuffer().getCurrentDjlxbm();
        DjLXVO djlx= FiPubDataCache.getBillType(billtypecode,pk_corp);
        if (null==djlx) {
			throw new BusinessException("请先到集团分配交易类型！");
		}
        if (!("fj".equals(djdl) || "fk".equals(djdl) || "wf".equals(djdl) || "hj".equals(djdl))){
            return true;
        }
       
        if(null!=djlx.getIsidvalidated() &&djlx.getIsidvalidated().booleanValue()){
        	boolean need=nc.ui.sm.cmenu.ClientDesktopAssistant.useSignAuthen();
        	if( need)
        		return null!=AuthenticatorFactory.getInstance().getAuthenticator(this.getDjSettingParam().getPk_user()).sign(Crypt_Code);
	           return true;
        }
        return true;
    }
	/**
	 * 子类实现该方法，响应按钮事件。
	 *
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {

		this.beginPressBtn(bo);
		this.showProgressBar(true);
		// if(!this.checkID(bo)){
		// return ;
		// }
		if(this.getArapDjPanel1().isSelectSettleTab()){
			this.getArapDjPanel1().getSettlePanel().onButtonClicked(bo);
		}else{
			super.onButtonClicked(bo);
		}
		if(isSuccess()){
			this.endPressBtn(bo);
		}
		this.showProgressBar(false);
		// refactoring
		// whether some controller treate the event
		// nc.bs.logging.Log.getInstance(this.getClass()).warn("debugging000");
		// boolean bEventCaughtByContorllers = false;
		// ARAPDjUIController[] controllers = getUIControllers();
		// if (controllers != null) {
		// //nc.bs.logging.Log.getInstance(this.getClass()).warn("debugging in
		// if");
		// for (int i = 0; i < controllers.length; i++) {
		// //nc.bs.logging.Log.getInstance(this.getClass()).warn("debugging in
		// for:+" + i);
		// bEventCaughtByContorllers = controllers[i].treatEvent(bo);
		// //if the controller has treat the event,stop the loop then
		// if (bEventCaughtByContorllers == true)
		// break;
		// }
		// }
		//
		//
		// //nc.bs.logging.Log.getInstance(this.getClass()).warn("Null
		// controller array returned");
		// //nc.bs.logging.Log.getInstance(this.getClass()).warn("debugging111");
		//
		//
		// if (bEventCaughtByContorllers == false) {
		// if(getCurBusiTransVO()!=null)
		// for (int i = 0; i < getCurBusiTransVO().length; i++) {
		// if (bo == getCurBusiTransVO()[i].getBtnAssistant())
		// runExtAssistant(getCurBusiTransVO()[i]);
		// }
		// }
		// this.showProgressBar(false);
		// //if the button clicked is not "增行" and "删行",then hide the
		// // "serial_number" column
		// if (bo != m_boAddRow && bo != m_boDelRow && bo != m_boNewByBudget
		// && bo != m_boSplitSSItem && bo!=m_boBudgetImpl) {
		// //hide the "serial_number" column of the card panel body
		// //only effective for proceeding bills
		// getArapDjPanel1().hideGroupingSerialNumber();
		// }
		// //刷新按钮的方法，子类需要重写
		// //刷新按钮的方法，子类需要重写
		// if (bo !=m_boPrint&&bo!=m_boPrint_All) {
		// refreshBtnStats();
		// }
		
	}
	/*
	 * 设置是否取消关闭节点的标示信息
	 * **/
	public void setCancelCloseFlag(Boolean cancelCloseFlag){
		this.cancelCloseFlag=cancelCloseFlag.booleanValue();
	}
	/**
	 * 此处插入方法说明。 创建日期：(2001-12-19 15:06:45)
	 *
	 * @return boolean
	 */
	public boolean onClosing() {

		try {
			ExtButtonObject[] Buttons=getExtBtnProxy().getButtons();
          //本段代码用于扩展默认属性，便于增加新的功能,扩展时close设为常量
			for(ExtButtonObject ButtonObject:Buttons){
				if("windowClose".equalsIgnoreCase(ButtonObject.getBtninfo().getDefaultFlag())){
					getExtBtnProxy().doAction(ButtonObject);
					break;
				}
			}
			//判断是否取消关闭操作
			if(cancelCloseFlag){
				setCancelCloseFlag(false);
				return false;
			}

//			//临时实现代码
//			for(ExtButtonObject ButtonObject:Buttons){
//				if("windowClose".equalsIgnoreCase(ButtonObject.getBtninfo().getId())){
//					getExtBtnProxy().doAction(ButtonObject);
//					break;
//				}
//			}
			try{
				if(!UFBoolean.TRUE.equals(this.getIsAloneNode()))
					getTempletCache().audit.refreshDataBase();
			}catch(Exception ex){
				//可以忽略
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			this.showErrorMessage(e.getMessage());
			this.showHintMessage(e.getMessage());
			return false;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			this.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000085")/*@res "保存失败"*/);//日后换为多语言
			return true;
		}

		// //refactoring
		// //if (getVerifyInTimePanel() != null)
		// //getVerifyInTimePanel().onClosing();
		// //note:up to now, only the
		// // nc.ui.ep.dj.controller.ARAPDjCtlCounteract.onClosing() is
		// effective
		// ARAPDjUIController[] controllers = getUIControllers();
		// if (controllers != null) {
		// for (int i = 0; i < controllers.length; i++)
		// if(!controllers[i].onClosing())
		// return false;
		// }
		//
		// //boolean bflag = on_close_freeLock();
		// //非单据查询,才有必要解锁
		// if (getDjSettingParam().getSyscode() != -1) {
		// String[] strPKArray = getDjDataBuffer().getAllLockedDJZBVO_PK();
		// try {
		// if (strPKArray != null && strPKArray.length > 0)
		// KeyLock.freeKeyArray(strPKArray,
		// getDjSettingParam().getPk_user(), null);
		//
		// } catch (Exception e) {
		// nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(),e);
		// showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000014")/*@res
		// "单据解锁出错,本次操作自动取消"*/);
		// return true;
		// }
		// }
		//
		// this.getDjDataBuffer().clearBuffer();
		// this.cache.audit.refreshDataBase();
		// //nc.bs.logging.Log.getInstance(this.getClass()).warn("onClosing:true");
		// // this.audit.refreshDataBase();
		return true;
	}

	/**
	 * 功能:查询单据,刷新单据列表 创建日期：(2001-5-22 9:32:54)
	 *
	 * @return boolean
	 * @param pageIndex
	 *            int
	 * @exception java.lang.Throwable
	 *                异常说明。 作者:阿飞
	 */
	public boolean refDjLb(DJZBHeaderVO[] djzbheadervo) throws Exception {
		getBillListPanel1().getBodyBillModel().clearBodyData();
		getBillListPanel1().getHeadBillModel().clearBodyData();

		if (djzbheadervo == null || djzbheadervo.length < 1) {
			return true;
		}
		getBillListPanel1().setHeaderValueVO(djzbheadervo);

		// getBillListPanel1().getHeadTable().setRowSelectionInterval(0,0);
		// djlbHeadRowChange(0);

		return true;
	}

	public void resetDjlxRefBySyscode(UIRefPane ref) {
		String strDjdl = getDjDataBuffer().getCurrentDjdl();

		if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AR)
			ref.setWhereString(" where ( dr=0 and dwbm='" + getClientEnvironment().getCorporation().getPrimaryKey()
					+ "') and (djdl='ys' or djdl='sk'or djdl='yf' or djdl='fk') and djlxbm<>'DR'");// and
																									// fcbz='N'
		else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AP)
			ref.setWhereString(" where ( dr=0 and dwbm='" + getClientEnvironment().getCorporation().getPrimaryKey()
					+ "') and (djdl='ys' or djdl='sk'or djdl='yf' or djdl='fk') and djlxbm<>'DR'");// and
																									// fcbz='N'
		else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EP && !strDjdl.equals("ss"))
			ref.setWhereString(" where ( dr=0 and dwbm='" + getClientEnvironment().getCorporation().getPrimaryKey()
					+ "') and djlxbm<>'DR' and djdl<>'ss' and djdl<>'lb' and djdl<>'ts' and djdl<>'yt'");// and
																											// fcbz='N'
		else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm)
			ref.setWhereString(" where ( dr=0 and dwbm='" + getClientEnvironment().getCorporation().getPrimaryKey()
					+ "') and (isqr='Y') and djlxbm<>'DR'");// and fcbz='N'
		else if (strDjdl.equals("ss") || getDjSettingParam().getSyscode() == 3)
			ref.setWhereString(" where ( dr=0 and dwbm='" + getClientEnvironment().getCorporation().getPrimaryKey()
					+ "') and (djdl='ss') and djlxbm<>'DR' ");// and fcbz='N'
		else
			ref.setWhereString(" where ( dr=0 and dwbm='" + getClientEnvironment().getCorporation().getPrimaryKey()
					+ "') and (djdl<>'lb' and djdl<>'yt' and djdl<>'ss' and djdl<>'ts' ) ");// and
																							// fcbz='N'
	}

	/**
	 * 此处插入方法描述。 执行外接口动作 创建日期：(2002-6-19 14:43:50)
	 */
	public void runExtAssistant(nc.vo.arap.global.BusiTransVO bvo) {

		int row = 0;

		// nc.ui.bank.costContent.CostContentDlg cur_Cost = new
		// nc.ui.bank.costContent.CostContentDlg(
		// this);

		nc.vo.ep.dj.DJZBVO djzb = null;

		if (this.getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE) // 列表
		{
			if (getBillListPanel1().getHeadBillModel().getRowCount() < 1)
				return;

			int row_h = getBillListPanel1().getHeadTable().getSelectedRow();
			if (row_h < 0) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000423")/*
																													 * @res
																													 * "请选择单据"
																													 */);
				return;
			}

			row = getBillListPanel1().getBodyTable().getSelectedRow();
			if (row < 0) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000943")/*
																													 * @res
																													 * "请选择单据行"
																													 */);
				return;
			}

			if (getBillListPanel1().getHeadBillModel().getValueAt(row_h, "vouchid") == null)
				return;
			String vouchid = getBillListPanel1().getHeadBillModel().getValueAt(row_h, "vouchid").toString();
			djzb = this.getDjDataBuffer().getDJZBVO(vouchid);
		} else {
			// refactoring
			nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

			// 单据
			djzb = dataBuffer.getCurrentDJZBVO();
			row = getArapDjPanel1().getBillCardPanelDj().getBillTable().getSelectedRow();
			// 单据选择的行
			if (row < 0) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000943")/*
																													 * @res
																													 * "请选择单据行"
																													 */);
				return;
			}

		}
		// DJZBHeaderVO head = (DJZBHeaderVO)
		// (djzb.getParentVO());
		DJZBItemVO[] items = (DJZBItemVO[]) (djzb.getChildrenVO());

		// String vouchid = head.getVouchid(); //主表主键
		String fb_oid = items[row].getFb_oid(); // 辅表oid
		try {
			((nc.ui.arap.outer.ArapAssistantInf) (bvo.getInfClass())).runAssistant(djzb, fb_oid);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000944")/*
																												 * @res
																												 * "执行外系统动作出错\n"
																												 */
					+ bvo.getUsesystemname());
		}

	}

	/**
	 * 功能:全选 作者：陈飞 创建时间：(2001-7-11 10:01:47) 参数： <|>返回值： 算法：
	 */
	public void selectedAll() {
		getBillListPanel1().setEnabled(true);

		for (int row = 0; row < getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
			getBillListPanel1().getHeadBillModel().setValueAt(ArapConstant.UFBOOLEAN_TRUE, row, "isselected");
			// String
			// vouchid=(String)getBillListPanel1().getHeadBillModel().getValueAt(row,"vouchid"
			// );
			// DJZBVO djzbvo=this.getDjDataBuffer().getDJZBVO(vouchid);
			// if(!this.getDjDataBuffer().getListSelectedVOs().contains(djzbvo))
			// this.getDjDataBuffer().getListSelectedVOs().add(djzbvo);

			// DJZBHeaderVO tempHead = (DJZBHeaderVO) djzbvo.getParentVO();
			// tempHead.setIsselected(UFBoolean.TRUE);
		}
		// lockAll_bak();
	}

	/**
	 * 单据和自由项之间切换界面 作者：陈飞 创建时间：(2001-8-22 16:56:59) 参数： <|>返回值： 算法：
	 *
	 * @param tabIndex
	 *            int
	 */
	public void setButtonVisible(int tabIndex) {
		setM_Syscode(getDjSettingParam().getSyscode());
		refreshBtnStats();
	}

	// /**
	// * 此处插入方法描述。 初始化辅助外接口 创建日期：(2004-4-8 9:17:58)
	// *
	// * @author:陈飞
	// * @param newCurBusiTransVO
	// * nc.vo.arap.global.BusiTransVO[]
	// */
	// public void setCurBusiTransVO(
	// nc.vo.arap.global.BusiTransVO[] newCurBusiTransVO) {
	// try {
	// if (newCurBusiTransVO == null) {
	//
	// newCurBusiTransVO = Proxy.getIArapBillPrivate()
	// .initBusiTrans(getClientEnvironment().getCorporation()
	// .getPrimaryKey(), new Integer(
	// getDjSettingParam().getSyscode()));
	// }
	// m_CurBusiTransVO = newCurBusiTransVO;
	// for (int i = 0; m_CurBusiTransVO != null
	// && i < m_CurBusiTransVO.length; i++) {
	// m_boAssistant.addChildButton(m_CurBusiTransVO[i]
	// .getBtnAssistant());
	// }
	// } catch (Exception e) {
	// nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(),e);
	// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000947")/*@res
	// "初始化辅助外接口出错"*/);
	//
	// }
	//
	// }
	public void setDjlxbmIndPower(String djlxbm)throws BusinessException {
			UIRefPane accountRef = new UIRefPane();
			accountRef.setRefModel(new DjlxRefModel1()) ;

			accountRef.setPK(djlxbm);
			accountRef.setPk_corp(this.getDjSettingParam().getPk_corp());
			Object obj=accountRef.getRefValue("djlxbm");
			if(null==obj){
				throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000086")/*@res "没有当前交易类型的使用权限!"*/);
			}
			DjLXVO cur_djlxvo =FiPubDataCache.getBillType(djlxbm,this.getDjSettingParam().getPk_corp());
			if(null!=cur_djlxvo&&null!=cur_djlxvo.getUsesystem()){
				 Integer syscode=Integer.valueOf(cur_djlxvo.getUsesystem());
				 if (syscode == this.getDjSettingParam().getSyscode()
					|| syscode == 3
					|| this.getDjSettingParam().getSyscode() == -9
					|| this.getDjSettingParam().getSyscode() == -99
					|| this.getDjSettingParam().getSyscode() == 3
					|| this.getDjSettingParam().getSyscode() == 4
					|| this.getDjSettingParam().getSyscode() == -1) {
					 getDjDataBuffer().setCurrentBillTypeInfo(cur_djlxvo);
				 }else{
					 throw ExceptionHandler.createException(nc.ui.ml.NCLangRes.getInstance().getStrByID("2008",
						"UPP2008-000115")/* @res "未分配默认单据类型，请到集团单据类型设置节点进行分配" */);
				 }

			}else{
				 throw ExceptionHandler.createException(nc.ui.ml.NCLangRes.getInstance().getStrByID("2008",
					"UPP2008-000115")/* @res "未分配默认单据类型，请到集团单据类型设置节点进行分配" */);
			 }
	}
	/**
	 * 此处插入方法说明。 初始化时设置单据类型编码 创建日期：(2001-10-25 18:46:36)
	 *
	 * @author：陈飞
	 * @param djlxbm
	 *            java.lang.String
	 */
	public void setDjlxbm(String djlxbm) {

		// dataBuffer.setCurrentBillTypeInfo(cur_djlxvo, item);
		try {
			 this.setDjlxbmIndPower(djlxbm);

		} catch (Exception e) {
//			// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000841")/*@res
//			// "初始化时根据单据类型编码取单据类型VO出错: "*/ + e);
//			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
//					"UPP2006030102-000842")/* @res "致命错误" */,e.getMessage() );
		}

	}

	/**
	 * 功能: 简要流程: 算法: 创建日期：(2001-6-22 12:46:10)
	 *
	 * @author：陈飞
	 *
	 *
	 *
	 * @param newM_bQc
	 *            boolean
	 */
	protected void setM_bQc(boolean newM_bQc) {
		// refactored by wq
		// m_bQc = newM_bQc;
		getDjSettingParam().setIsQc(newM_bQc);
		refreshBtnStats();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-12-17 19:50:10)
	 *
	 * @param newM_Node
	 *            java.lang.String
	 */
	public void setM_Node(java.lang.String newM_Node) {
		// refactoring: (need further refactoring:remove this method)
		// m_Node = newM_Node;
		getDjSettingParam().setNodeID(newM_Node);
		// getBillListPanel1().loadTemplet(m_Node,null,getClientEnvironment().getUser().getPrimaryKey(),getM_Pk_corp(),"列表");
	}

	/**
	 * 功能: 简要流程: 算法: 创建日期：(2001-6-22 12:46:10)
	 *
	 * @author：陈飞
	 *
	 *
	 *
	 * @param newM_Syscode
	 *            int
	 */
	public void setM_Syscode(int newM_Syscode) {
		// m_Syscode = newM_Syscode;
		getDjSettingParam().setSyscode(newM_Syscode);
		// m_boQROrNo.setVisible(false);
		//
		// if (getDjSettingParam().getSyscode() != -2) {
		// m_boYhQROrNo.setVisible(false);
		//
		// }
		if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_DjQuery) // 单据查询
		{

			try {
				if (this.getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE)
					setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000411")/*
																													 * @res
																													 * "单据查询"
																													 */);
			} catch (Exception e) {
				nc.bs.logging.Logger.debug(e.getMessage(), this.getClass(), "setM_Syscode");
			}

		} else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm) // 签字确认
		{

			try {
				if (this.getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE)
					setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000412")/*
																													 * @res
																													 * "签字确认"
																													 */);
			} catch (Exception e) {
				nc.bs.logging.Logger.debug(e.getMessage(), this.getClass(), "setM_Syscode");
			}
		} else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_AR
				|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_AP
				|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_EP) // 确认
		{

			try {
				//if (this.getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE)
					setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000942")/*
																													 * @res
																													 * "单据确认"
																													 */);
			} catch (Exception e) {
				nc.bs.logging.Logger.debug(e.getMessage(), this.getClass(), "setM_Syscode");
			}
		}
		// 事项审批单
		else if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_SS) {

			try {
				if (this.getCurrWorkPage() == ArapBillWorkPageConst.LISTPAGE)
					setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000941")/*
																													 * @res
																													 * "事项审批单"
																													 */);
			} catch (Exception e) {
				nc.bs.logging.Logger.debug(e.getMessage(), this.getClass(), "setM_Syscode");
			}
		} else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_WSZZ) {
		} // 网上付款
		// refreshBtnStats();

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-31 11:07:43)
	 *
	 * @author：陈飞 功能: 算法： 备注：
	 * @param newM_TabIndex
	 *            int
	 */
	public void setM_TabIndex(int newM_TabIndex) {
		m_TabIndex = newM_TabIndex; // 此方法已废弃，Controller包删除后移除
	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-6-25 9:58:15)
	 */
	public void test() {
		/*
		 * nc.vo.ep.dj.BillConferVO billVO; billVO.setSourceBill("");
		 * billVO.setSourcecorp(""); billVO.setPk_cusmanid("");
		 * billVO.setPk_accid(""); DJZBHeaderVO head; nc.vo.ep.dj.DJZBVO djzbVO;
		 * djzbVO.getParentVO(); head.getDjlxbm(); head.getDwbm();
		 * head.getKsbm_cl(); head.getAccountid();
		 * billVO.getDocontrol().intValue(); nc.vo.ep.dj.BillConferVO
		 * temp_billVO ; head.setDjlxbm(temp_billVO.getTargetBill());
		 * head.setDwbm(temp_billVO.getTargetcorp()); head.setDjmboid("");
		 * head.setYwbm(""); head.setFktjbm(head.getVouchid());
		 * nc.vo.ep.dj.DjlxVO djlxvo; djlxvo.getDjmboid();
		 * djlxvo.getPrimaryKey(); djzbVO.setParentVO(head); head.getDjlxbm();
		 * Object[] a; a.length
		 */

	}

	/**
	 * 功能:全消 作者：陈飞 创建时间：(2001-7-11 10:01:47) 参数： <|>返回值： 算法：
	 */
	public void unSelectedAll() {
		for (int row = 0; row < getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
			getBillListPanel1().getHeadBillModel().setValueAt(ArapConstant.UFBOOLEAN_FALSE, row, "isselected");
			// String
			// vouchid=(String)getBillListPanel1().getHeadBillModel().getValueAt(row,"vouchid"
			// );
			// DJZBVO djzbvo=this.getDjDataBuffer().getDJZBVO(vouchid);
			// if(!this.getDjDataBuffer().getListSelectedVOs().contains(djzbvo))
			// this.getDjDataBuffer().getListSelectedVOs().remove(djzbvo);
		}
	}

	/**
	 * create time:2004-10-18 author:wangqiang function:provide a interface
	 * method for outside invocation
	 */
	public void updateButton(ButtonObject bo) {
		super.updateButton(bo);
	}

	/**
	 * 刷新按钮状态，在单据行变化，列表卡片切换，按钮点击等操作后调用，统一处理按钮的状态，
	 * 可以考虑扩展按钮的功能，由按钮监听器控制按钮的状态，这里只是发出消息 important notice:pls execute this
	 * method after the UI index(getM_TabIndex) have been setted or changed
	 */
	public void refreshBtnStats() {

		this.refreshBtnStatus();
	}

	/**
	 * 返回当前工作页为列表，卡片还是自由项 分别返回 0，1，2
	 *
	 * @author st
	 *
	 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
	 */
	// public int getCurrWorkPage(){
	// //暂时没有实现
	// if(getM_TabIndex()==1){
	// return ArapBillWorkPageConst.LISTPAGE;
	// }else if(getM_TabIndex()==-1){
	// if(getArapDjPanel1().getM_TabIndex()==0){
	// if(getArapDjPanel1().m_iVitStat==ArapBillWorkPageConst.VITPAGE){
	// return ArapBillWorkPageConst.VITPAGE;
	// }else if(getArapDjPanel1().m_iVitStat==ArapBillWorkPageConst.MAKEUPPAGE){
	// return ArapBillWorkPageConst.MAKEUPPAGE;
	// }else
	// return ArapBillWorkPageConst.CARDPAGE;
	// }else{
	// return ArapBillWorkPageConst.ZYXPAGE;
	// }
	//
	// }
	// return ArapBillWorkPageConst.LISTPAGE;
	// }
	/**
	 * @author st 得到上一工作页面
	 */
	protected int getLastWorkPage() {
		return m_lastWorkPage;
	}

	/**
	 * @author st 保存当前的工作页状态
	 */
	protected void setLastWorkPage(int newWorkpage) {
		m_lastWorkPage = newWorkpage;
	}

	/**
	 * 子类需要实现类，根据单据状态，来源等信息以及当前工作页信息得到 可用按钮的数组，此方法是每个子类都需要实现的方法
	 */
	protected ButtonObject[] getEnableButtonArry() {
		return new ButtonObject[0];
	}

	/**
	 * @return 返回 isInitied。
	 */
	public boolean isInitied() {
		return isInitied;
	}

	/**
	 * @param isInitied
	 *            要设置的 isInitied。
	 */
	public void setInitied(boolean isInitied) {
		this.isInitied = isInitied;
	}

	/**
	 * author:wangqiang create time:2004-12-10 function :given a djzbitemvo,this
	 * function return its index on the card panel
	 *
	 * @param strItemPK:
	 *            PK of the DJZBItemVO
	 * @return
	 */
	public int getHeadRowInexByPK(String strHeadPK) {
		int intRowCount = getBillListPanel1().getHeadTable().getRowCount();
		for (int index = 0; index < intRowCount; index++) {
			// get the item pk of the row on the panel
			String strRowPK = getBillListPanel1().getHeadBillModel().getValueAt(index, "vouchid").toString();
			if (strRowPK.trim().equals(strHeadPK.trim()))// the corresponding
															// row is found if
															// it is true here
				return index;
		}
		return -1;
	}

	// /**
	// * 此处插入方法描述。 创建日期：(2004-3-1 19:45:21)
	// */
	// public void freeLockCurDj() throws Exception {
	// //nc.vo.ep.dj.DJZBVO vo = getArapDjPanel1().getM_Djzbvo();
	// nc.vo.ep.dj.DJZBVO vo = getDjDataBuffer().getCurrentDJZBVO();
	// if (vo == null)
	// return;
	// DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
	//
	// if (head != null && head.getIslocked() != null &&
	// head.getIslocked().booleanValue()) { //已经加锁
	// //if (head != null) {
	//
	// //解锁
	// // boolean locked = false;
	// try {
	//
	// KeyLock.freeKey(head.getVouchid(), getDjSettingParam().getPk_user(),
	// null);
	// head.setIslocked(ArapConstant.UFBOOLEAN_FALSE);
	// } catch (Exception e2) {
	// Logger.error(e2.getMessage(), e2);
	//
	// }
	// }
	//
	// }
	/**
	 * @return 返回 voCache。
	 */
	public Cache getVoCache() {
		return voCache;
	}

	/**
	 * @param voCache
	 *            要设置的 voCache。
	 */
	public void setVoCache(Cache voCache) {
		this.voCache = voCache;
	}

	/**
	 * @return 返回 cur_Djcondvo。
	 */
	public nc.vo.ep.dj.DjCondVO getCur_Djcondvo() {
		return cur_Djcondvo;
	}

	/**
	 * @param cur_Djcondvo
	 *            要设置的 cur_Djcondvo。
	 */
	public void setCur_Djcondvo(nc.vo.ep.dj.DjCondVO cur_Djcondvo) {
		this.cur_Djcondvo = cur_Djcondvo;
	}

	public void showErrorMessage(String err) {
		// 显示错误对话框
		// MessageDialog.showErrorDlg(this, "错误", err);
		super.showErrorMessage(err);
		//super.showHintMessage(err);
	}

	public void listBodyAfterEdit(nc.ui.pub.bill.BillEditEvent e) {
	}

	public void listBodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		// jp.refreshBtnStats();
		this.getBillListPanel1().getBodyBillModel().setValueAt(Boolean.FALSE, e.getOldRow(), "isselected");
		this.getBillListPanel1().getBodyBillModel().setValueAt(Boolean.TRUE, e.getRow(), "isselected");

	}

	public void listHeadAfterEdit(nc.ui.pub.bill.BillEditEvent e) {
		// jp.refreshBtnStats();
		// 2004-11-23,bug caught,wangqiang
		try {

			Logger
					.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000910")/*
																											 * @res
																											 * "选择单据列表表头第:"
																											 */
							+ e.getRow()
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000652")/*
																												 * @res "
																												 * 行"
																												 */);
			this.djlbHeadRowChange(e.getRow()); // 选择单据列表表头

		} catch (Throwable ee) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000911")/*
																											 * @res
																											 * "选择单据列表表头出错:"
																											 */
					+ ee);
		}

	}

	public void listHeadRowChange(nc.ui.pub.bill.BillEditEvent e) {

		try {

			Logger
					.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000910")/*
																											 * @res
																											 * "选择单据列表表头第:"
																											 */
							+ e.getRow()
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000652")/*
																												 * @res "
																												 * 行"
																												 */);
			this.djlbHeadRowChange(e.getRow()); // 选择单据列表表头
			// jp.refreshBtnStats();
		} catch (Throwable ee) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000911")/*
																											 * @res
																											 * "选择单据列表表头出错:"
																											 */
					+ ee);
		}
	}

	public void doApproveAction(ILinkApproveData approvedata) {
		try {
			PanelProp = 1;
			DJZBVO djzbvo = null;
			DjLXVO djlxvo = FiPubDataCache.getBillType(approvedata.getBillType(), approvedata.getPkOrg());
//				(DjLXVO) nc.ui.arap.pub.MyClientEnvironment.getValue(approvedata.getPkOrg(), "DJLX",
//					approvedata.getBillType());
			try {
				if ("ss".equalsIgnoreCase(djlxvo.getDjdl())) {
					djzbvo =Proxy.getIArapItemPrivate().findSsByPrimaryKey(approvedata.getBillID());
				} else
					djzbvo = getProxy().getIArapBillPublic().findArapBillByPK(approvedata.getBillID());
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				Logger.debug(e.getMessage());
				nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			}
			if (null == djzbvo) {
				return;
			}
			DJZBHeaderVO head = ((DJZBHeaderVO) djzbvo.getParentVO());
			this.getDjSettingParam().setPk_corp(head.getDwbm());

			int pzglh = ((DJZBHeaderVO) djzbvo.getParentVO()).getPzglh().intValue();
			if(this.getNodeCode().startsWith("2006") && pzglh==1){
				setM_Syscode(1);
				setM_Node("2008030102");
				this.setIsAloneNode(UFBoolean.FALSE);
			}else if(this.getNodeCode().startsWith("2008") && pzglh==0){
				setM_Syscode(0);
				setM_Node("2006030102");
				this.setIsAloneNode(UFBoolean.FALSE);
			}else if ((this.getNodeCode().startsWith("2006") || this.getNodeCode().startsWith("2008")) && pzglh == 2) {
				setM_Syscode(2);
				setM_Node("20040302");
				this.setIsAloneNode(UFBoolean.FALSE);
//				setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(head.getDwbm(), 2));
			} else if ("ss".equalsIgnoreCase(djlxvo.getDjdl())) {
				// m_bQc = false;
				setM_Syscode(2);
				setM_Node("20040202");
				this.setIsAloneNode(UFBoolean.FALSE);
				nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
				dataBuffer.setCurrentDjdl("ss");
				// setDjlxbm("DZ"); //设置单据类型编码
//				setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation()
//						.getPrimaryKey(), 3));
			}
			ButtonObject[] btArray = getDjButtons();
			setButtons(btArray);
			setName("DjPanel");
			setAutoscrolls(true);
			setLayout(new java.awt.CardLayout());
			setSize(774, 419);
			add(getJPanel1(), getJPanel1().getName());
			add(getVerifyPanel(), getVerifyPanel().getName());
			this.getDjDataBuffer().putDJZBVO(head.getVouchid(), djzbvo);
			((java.awt.CardLayout) getLayout()).show(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPT2006030102-000021")/* @res "单据" */);

			getJPanel1().add(getArapDjPanel1(), "Center");
			getArapDjPanel1().addDjStateChangeListener(m_djStateChange);
			// getArapDjPanel1().changeTab2(0);
			getArapDjPanel1().setM_DjState(0);
			// this.setM_TabIndex(-1);
			this.setCurrentpage(ArapBillWorkPageConst.CARDPAGE);
			// boolean locked=KeyLock.lockKey(head.getVouchid(),
			// getDjSettingParam().getPk_user(), null);
			// head.setIslocked(new UFBoolean(locked));
			try{
			this.setDjlxbmIndPower(((DJZBHeaderVO)djzbvo.getParentVO()).getDjlxbm());
			}catch(Exception e){
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000842")/* @res "致命错误" */,e.getMessage() );
				return ;
			}
			getArapDjPanel1().setDj(djzbvo);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
		this.refreshBtnStats();
	}

	private transient String tempString;

	public void doMaintainAction(ILinkMaintainData maintaindata) {
		PanelProp = 4;
		this.setTempString(maintaindata.getBillID());
		doQryAction(new ILinkQueryData() {
			/**
			 * 返回单据ID
			 *
			 * @return
			 */
			public String getBillID() {
				return getTempString();
			}

			/**
			 * 返回单据类型
			 *
			 * @return
			 */
			public String getBillType() {
				return null;
			}

			/**
			 * 返回源公司PK
			 *
			 * @return
			 */
			public String getPkOrg() {
				return null;
			}

			/**
			 * 返回用户对象
			 *
			 * @return
			 */
			public Object getUserObject() {
				return null;
			}

		},false);
	}

	public void doQueryAction(ILinkQueryData querydata) {
		PanelProp = 2;
		doQryAction(querydata,true);
	}

	public DJZBVO[]  getQryVO(String pk_corp,String[] pks,boolean isPower){
		DJZBVO[] djzbvos=null;
		try {
			// djlxvo=Proxy.getIArapBillTypePublic().getDjlxvoByDjlxbm(querydata.getBillType(),
			// querydata.getPkOrg());

			if(isPower&&pk_corp!=null){
//				String dwbm=pk_corp== null?this.getDjSettingParam().getPk_corp():pk_corp;
				djzbvos = this.getProxy().getIArapBillPublic().findBillsByPkWithPower(pks,pk_corp,this.getDjSettingParam().getPk_user());

			}else
				djzbvos = this.getProxy().getIArapBillPublic().findArapBillByPKs(pks);
			if (null == djzbvos) {
				Vector<String> v = new Vector<String>();
				for (int i = 0, size = pks.length; i < size; i++) {
					v.add(pks[i]);
				}
				djzbvos=((ICMPBillPrivate) NCLocator.getInstance().lookup(
						ICMPBillPrivate.class.getName())).findByPrimaryKeys_SS(v);
			}
			if (null == djzbvos) {
				djzbvos = new DJZBVO[] { Proxy.getIArapTBBillPrivate().findTbByPrimaryKey(pks[0]) };
			}

		} catch (Exception e) {
			// TODO 自动生成 catch 块
			Logger.debug(e.getMessage());
		}
		return djzbvos;
	}
	public void doQryAction(ILinkQueryData querydata,boolean isPower) {
		ButtonObject[] btArray = getDjButtons();
		setButtons(btArray);
		String[] vouchids = null;
		try {
			vouchids = (String[]) ((Object[]) querydata.getUserObject())[0];
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
		if (null == vouchids && querydata.getBillID() != null)
			vouchids = new String[] { querydata.getBillID() };
		if (null == vouchids || vouchids.length == 0)
			return;
		DJZBVO[] djzbvos = getQryVO(querydata.getPkOrg(),vouchids,isPower);
//
//		try {
//			// djlxvo=Proxy.getIArapBillTypePublic().getDjlxvoByDjlxbm(querydata.getBillType(),
//			// querydata.getPkOrg());
//			if(isPower){
//				String dwbm=querydata.getPkOrg()== null?this.getDjSettingParam().getPk_corp():querydata.getPkOrg();
//				djzbvos = this.getProxy().getIArapBillPublic().findBillsByPkWithPower(vouchids,dwbm,this.getDjSettingParam().getPk_user());
//
//			}else
//				djzbvos = this.getProxy().getIArapBillPublic().findArapBillByPKs(vouchids);
//			if (null == djzbvos) {
//				Vector<String> v = new Vector<String>();
//				for (int i = 0, size = vouchids.length; i < size; i++) {
//					v.add(vouchids[i]);
//				}
//				djzbvos=((ICMPBillPrivate) NCLocator.getInstance().lookup(
//						ICMPBillPrivate.class.getName())).findByPrimaryKeys_SS(v);
//			}
//			if (null == djzbvos) {
//				djzbvos = new DJZBVO[] { Proxy.getIArapTBBillPrivate().findTbByPrimaryKey(vouchids[0]) };
//			}
//
//		} catch (Exception e) {
//			// TODO 自动生成 catch 块
//			Logger.debug(e.getMessage());
//		}
		if (null == djzbvos || djzbvos.length == 0 || null == djzbvos[0]) {
			this.showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000207")/*@res "查询不到对应的单据！"*/);
			return;
		}

		DJZBHeaderVO head = ((DJZBHeaderVO) djzbvos[0].getParentVO());
		this.getDjSettingParam().setPk_corp(head.getDwbm());
		int pzglh = head.getPzglh().intValue();
		if(DJZBVOConsts.NINE.equals(head.getLybz())&&head.getDjzt()==DJZBVOConsts.m_intDJStatus_Unconfirmed){
			//setM_Syscode(0);
			if(head.getPzglh()==0){
				setM_Node("2006030103");
				setM_Syscode(ArapBillWorkPageConst.SysCode_XT_AR);
			}else if(head.getPzglh()==1){
				setM_Syscode(ArapBillWorkPageConst.SysCode_XT_AP);
				setM_Node("2008030103");
			}
			this.setIsAloneNode(UFBoolean.FALSE);
//			loadBillListTemplate();
//			setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(head.getDwbm(), 0));

		}
		else if ("ss".equalsIgnoreCase(head.getDjdl())) {
			setM_Syscode(2);
			setM_Node("20040202");
			this.setIsAloneNode(UFBoolean.FALSE);
			nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
			dataBuffer.setCurrentDjdl("ss");
//			loadBillListTemplate();
			// setDjlxbm("DZ"); //设置单据类型编码
//			setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation()
//					.getPrimaryKey(), 3));
		} else if ("yt".equalsIgnoreCase(head.getDjdl())) {
			setM_Syscode(2);
			setM_Node("20040203");
			this.setIsAloneNode(UFBoolean.FALSE);
			nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
			dataBuffer.setCurrentDjdl("yt");
			setDjlxbm("DV");// 设置单据类型编码
//			loadBillListTemplate();
		} else if (( this.getNodeCode().startsWith("2008")) && pzglh == 0) {
			setM_Syscode(0);
			setM_Node("2006030102");
			this.setIsAloneNode(UFBoolean.FALSE);
//			loadBillListTemplate();
//			setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(head.getDwbm(), 0));
		} else if (( this.getNodeCode().startsWith("2006")) && pzglh == 1) {
			setM_Syscode(1);
			setM_Node("2008030102");
			this.setIsAloneNode(UFBoolean.FALSE);
//			loadBillListTemplate();
//			setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(head.getDwbm(), 1));
		}
		initialize(false,djzbvos.length>1,false);
//
//		if (this.getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE) {
//			try {
//				changeTab(this.getCurrWorkPage(), true);
//			} catch (Throwable e) {
//				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912")
//						+ e);
//			}
//		}
		if(djzbvos.length>1){
			DJZBHeaderVO[] djzbheadervo = new DJZBHeaderVO[djzbvos.length];
			try {
				for (int i = 0; i < djzbvos.length; i++) {
					AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(djzbvos[i]);
					djzbheadervo[i] = (DJZBHeaderVO) djzbvos[i].getParentVO();
					this.getDjDataBuffer().putDJZBVO(djzbheadervo[i].getVouchid(), djzbvos[i]);
				}
				this.refDjLb(djzbheadervo);
				this.getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
				this.djlbHeadRowChange(0);
			} catch (Throwable e) {
				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453") + e);
			}
			// 加载公式
			try {
				this.getBillListPanel1().getHeadBillModel().execLoadFormula();
			} catch (Exception e) {
				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037") + e);
			}
		}else if (djzbvos.length == 1){
			try {
				this.getDjDataBuffer().putDJZBVO(head.getVouchid(), djzbvos[0]);
				this.getArapDjPanel1().setDj(djzbvos[0]);
			} catch (Throwable e) {
				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912")
						+ e);
			}
		}

	}


//adddata中提供Dwbm（公司pk）----通过getSourcePkOrg（），pzglh通过(Object[]) adddata.getUserObject()[1]得到
	public void doAddAction(ILinkAddData adddata) {
		if(!checkPanelStatus()){
			return;
		}
		PanelProp=3;
		DJZBVO[] djzbvos=null;
		if(IworkflowLinkData.class.isAssignableFrom(adddata.getClass())){
			setWorkflowlinkData((IworkflowLinkData)adddata);
			djzbvos= new DJZBVO[]{(DJZBVO)getWorkflowlinkData().getDestBillvo()};
			for(DJZBVO dj:djzbvos){
				DJZBHeaderVO head=(DJZBHeaderVO)dj.getParentVO();
				head.setTransientFlag(DJZBVOConsts.ACT_FLOW_BILL);
			}
		}else{
			if(null==adddata||adddata.getUserObject()==null)
				return ;
			djzbvos=((DJZBVO[])((Object[])adddata.getUserObject())[0]);
		}

		if(null==djzbvos||djzbvos.length==0){
			return;
		}
		//0辅币 1本币
		DJZBHeaderVO head=((DJZBHeaderVO)djzbvos[0].getParentVO());
		for(DJZBItemVO item:(DJZBItemVO[])djzbvos[0].getChildrenVO() ){

			try {
				UFDouble[] hls = Currency.getRateBoth(item.getDwbm(), item.getBzbm(), ClientEnvironment.getInstance().getDate().toString());
				item.setBbhl(hls[1]);
				ArapDjCalculator.getInstance().calculateVO(item, "bbhl", ClientEnvironment.getInstance().getDate().toString(), head.getDjdl(), ArapDjCalculator.getInstance().getProior(head));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.showErrorMessage(e.getMessage());
				return ;
			}
		}

		setAttribute(IRuntimeConstans.SettleVO, djzbvos[0].getSettlementInfo());

		this.getDjSettingParam().setPk_corp(head.getDwbm());
		int pzglh=((DJZBHeaderVO)djzbvos[0].getParentVO()).getPzglh().intValue();
		if((this.getNodeCode().startsWith("2006")||this.getNodeCode().startsWith("2008"))&&pzglh==2){
			setM_Syscode(2);
			setM_Node("20040302");
			this.setIsAloneNode(UFBoolean.FALSE);

		}
		else if((this.getNodeCode().startsWith("2008"))&&pzglh==0){
			setM_Syscode(0);
			setM_Node("2006030102");
			this.setIsAloneNode(UFBoolean.FALSE);
//			setDjlxbm(
//			        nc.ui.arap.global.PubData.getDjlxbmByPkcorp(
//			        		head.getDwbm(),
//			            0));
		}else if((this.getNodeCode().startsWith("2006"))&&pzglh==1){
			setM_Syscode(1);
			setM_Node("2008030102");
			this.setIsAloneNode(UFBoolean.FALSE);
//			setDjlxbm(
//			        nc.ui.arap.global.PubData.getDjlxbmByPkcorp(
//			        		head.getDwbm(),
//			            1));
		}
		initialize(false,false,false);
		try{
			this.setDjlxbmIndPower(head.getDjlxbm());
			}catch(Exception e){
				nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000842")/* @res "致命错误" */,e.getMessage() );
				return ;
			}
//		if(this.getM_TabIndex()==-1){
//			try {
//			    changeTab(getM_TabIndex(), true);
//			} catch (Throwable e) {
//				ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000912")/*@res "初始化时changeTab出错:"*/ + e);
//			}
//        }
        DJZBHeaderVO[] djzbheadervo=new  DJZBHeaderVO[djzbvos.length];
        try {
	            for (int i = 0; i < djzbvos.length; i++) {
	            	ArapDjCalculator.getInstance().sumBtoH(djzbvos[i]);
	                djzbheadervo[i]=(DJZBHeaderVO)djzbvos[i].getParentVO();
	                this.getDjDataBuffer().setCurrentDJZBVO( djzbvos[i]);
            }
//            this.refDjLb(djzbheadervo);
//            this.getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
//            this.djlbHeadRowChange(0);
        } catch (Throwable e) {
            Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000453")/*@res "刷新单据列表出错:"*/ + e);
        }
//        //加载公式
//        try {
//            this.getBillListPanel1().getHeadBillModel().execLoadFormula();
//        } catch (Exception e) {
//        	Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000037")/*@res "加载公式出错:"*/ + e);
//        }
        if(djzbvos.length==1){
//            try {
////            	this.setDjlxbm(((DJZBHeaderVO)djzbvos[0].getParentVO()).getDjlxbm());
//            	changeTab (getM_TabIndex(), true );
//			} catch (Throwable e) {
//				ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000912")/*@res "初始化时changeTab出错:"*/ + e);
//			}
			this.getArapDjPanel1().setDj(djzbvos[0]);
			 getArapDjPanel1().setM_DjState(ArapBillWorkPageConst.WORKSTAT_NEW) ;
			  
			BillItem bi= this.getArapDjPanel1().getBillCardPanelDj().getHeadItem("isjszxzf");
			if(null!=bi)
				bi.setEnabled(false);
        }
//		this.refreshBtnStats();
//		this.m_boSave.setEnabled(false);
//		this.m_boEdit.setEnabled(false);
//		this.m_boCancel.setEnabled(false);
//		this.getArapDjPanel1().invokeTempletCache();
	}

	private boolean checkPanelStatus(){
		if (getM_TabIndex() == -1) {//卡片{
			if (getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT
                    || getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_NEW) {
                return this.onClosing();
            }
		}
			return true;
	}
	public void setLinkData(Object[] args) {
		String[] vouchids = (String[]) args[0];
		DJZBVO[] djzbvos = null;
		try {
			djzbvos = getProxy().getIArapBillPublic().findArapBillByPKs(vouchids);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			ExceptionHandler.consume(e);
		}
		if(this.getM_TabIndex()==-1){
			try {
			    changeTab(getM_TabIndex(), true);
			} catch (Throwable e) {
				ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000912") );
			}
		}
		DJZBHeaderVO[] djzbheadervo = new DJZBHeaderVO[djzbvos.length];
		try {
			for (int i = 0; i < djzbvos.length; i++) {
				djzbheadervo[i] = (DJZBHeaderVO) djzbvos[i].getParentVO();
				this.getDjDataBuffer().putDJZBVO(djzbheadervo[i].getVouchid(), djzbvos[i]);
			}
			this.refDjLb(djzbheadervo);
			this.getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
			this.djlbHeadRowChange(0);
		} catch (Throwable e) {
        	ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000453"));
		}
		// 加载公式
		try {
			this.getBillListPanel1().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
        	ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000037"));
		}
		if (djzbvos.length == 1)
			try {
			    changeTab(getM_TabIndex(), true);
			} catch (Throwable e) {
				ExceptionHandler.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000912"));
			}
		this.refreshBtnStats();
	}

	public String getTempString() {
		return tempString;
	}

	public void setTempString(String tempString) {
		this.tempString = tempString;
	}
	// /***************************************************************************
	// * 功能: 得到即时核销入口类（做为一种临时处理方法，日后优化）
	// *
	// * 参数: 无 返回值: VerifyDjPanel
	// **************************************************************************/
	// public VerifyDjPanel getVerifyDjPanel(){
	public int getPanelProp() {
		return PanelProp;
	// verifyDjPanel.setActionRunntimeV0((IActionRuntime)this);
	}
	// return verifyDjPanel;
	// }

	/**
	 * 获取展现业务单据的GUI组件
	 *
	 * @return
	 */
	public JComponent getComponent(){
		return this;
	}

	/**
	 * 获取需要的按钮
	 *
	 * @return
	 */
	public ButtonObject[] getButtons(){

		if(null!=this.getNodeCode()) {
//			System.out.println(this.getNodeCode() + "'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''");
			return this.getDjButtons();
		}else return  super.getButtons();
	}

//	/**
//	 * 处理按钮事件
//	 *
//	 * @param button
//	 */
//	public void onButtonClicked(ButtonObject button){
//
//	}

	/**
	 * 设置运行需要的ToftPanel，便于进行一些ToftPanel相关的操作
	 * 需要注意的是该类的调用时机：
	 *
	 * CMP首先会把该类展示业务用的Component加入到容器中，
	 * 在把Component加入到容器之前，会设置ToftPanel
	 *
	 * 然后提取按钮，当业务页签被选中的时候，ToftPanel上的按钮就变成了
	 * 业务页签的按钮，并且通过
	 *
	 * @param toftPanel
	 */
	public void setToftPanel(ToftPanel toftPanel){
		this.setFrame(toftPanel.getFrame());
		if(this.getFrame()==null){
			this.setFrame(toftPanel.getFrame());
		}
	}

	/**
	 * 获取页签标题，推荐用"业务信息"或"**单据"
	 * @return
	 */
	public String getTabTitle(){
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000015")/*@res "业务信息"*/;
	}

	/**
	 * 设置业务单据信息
	 *
	 */
	public void setBusiInfo(BusiInfo info){
		if(this.getParent() instanceof ToftPanel){
			   this.setFrame(((ToftPanel)this.getParent()).getFrame());

			  }
		this.setFROMCMP(true);
		if(null!=info&&null!=info.getBillid()){
			PanelProp = 2;
			this.setTempString(info.getBillid());
//			if( info.getBillid().equals(this.getDjDataBuffer().getCurrentDJZBVOPK())){
//				return;
//			}
			if(this.getDjDataBuffer().getCurrentDJZBVO()!=null){
				DJZBVO[] vo=getQryVO(info.getCorp(),new String[]{info.getBillid()},true);
				DjLXVO djlxvo=FiPubDataCache.getBillType(info.getBilltype(), info.getCorp());
				if(!info.getBilltype().equals(this.getDjDataBuffer().getCurrentDjlxbm())){
					this.getArapDjPanel1().loadDjTemplet(((DJZBHeaderVO)vo[0].getParentVO()).getPzglh(), info.getCorp(),
							djlxvo.getDjdl(), info.getBilltype());
				}
				this.getArapDjPanel1().setDj(vo[0]);
				return ;
			}
			doQryAction(new ILinkQueryData() {
				public String getBillID() {
					return getTempString();
				}
				public String getBillType() {
					return null;
				}
				public String getPkOrg() {
					return null;
				}
				public Object getUserObject() {
					return getDJZBVO4CMPQry();
				}
			},false);
		}
	}
	private DJZBVO getDJZBVO4CMPQry(){
		return djzbvo4CmpQry;
	}
	private void setDJZBVO4CMPQry(DJZBVO djzbvo){
		djzbvo4CmpQry=djzbvo;
	}
	/**
	 *	如果该视图和另外视图存在依赖关系,则会按照页签的方式进行展示,
	 *	当该视图转换成当前视图的时候,会调用方法.
	 *	当前视图指的是视图所关联的Tab被选中,处于现实状态
	 */
	public void selected(){
		this.setFROMCMP(true);
	}

	/**
	 *	如果该视图和另外视图存在依赖关系,则会按照页签的方式进行展示,
	 *	当该视图从当前视图转换成普通视图的时候,会调用方法.
	 */
	public void deSelected(){
		this.setFROMCMP(false);
	}

	public boolean isFROMCMP() {
		return  FROMCMP;
	}

	public void setFROMCMP(boolean fromcmp) {
		FROMCMP = fromcmp;
	}
	/**
	 * 获取业务单据的IParent
	 * @return
	 * @throws BusinessException
	 */
	public IParent getBusiPanelParent(IBusiInfoPanel panel) throws BusinessException{
		return this.m_parent;
	}
	public ButtonObject[] getDjButtons() {

		 ButtonObject[] ret=null;
		if(this.getArapDjPanel1().isSelectSettleTab()){
			ret=this.getArapDjPanel1().getSettlePanel().getButtons();
		}else{
			try {
	//			btns=getDjButton()==null?new ButtonObject[]{}:getDjButton();
				ret=getExtBtnProxy().getButtons()==null?new ButtonObject[]{}:getExtBtnProxy().getButtons();
	//			ret=new  ButtonObject[btns.length+extBtns.length];
	//			refreshBtnStatus();
//				 this.updateButtons();
			} catch (Exception e) {

				Log.getInstance(this.getClass()).error(e);
				// TODO Auto-generated catch block
				this.showErrorMessage(e.getMessage());

				this.showHintMessage(e.getMessage());

			}
		}
		return ret;
	}
	/**
	 * 查询业务单据的具体信息
	 * @param info
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject getBillVO(BusiInfo info) throws BusinessException{
		DJZBVO retvo=null;
		if(null==info.getBillid()){
			//是否缓存
			if(this.getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT
		            || getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_NEW){
				TempSaveAction edit=new TempSaveAction();
				edit.onTempSave();
			}
			retvo=(nc.vo.ep.dj.DJZBVO) this.getArapDjPanel1().getBillCardPanelDj().getBillValueVO  ("nc.vo.ep.dj.DJZBVO", "nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");
		}else{
			if(info.getRawBill()!=null)
				retvo=(DJZBVO)info.getRawBill();
			else if(this.getDjDataBuffer().getDJZBVO(info.getBillid())!=null){
				retvo=this.getDjDataBuffer().getDJZBVO(info.getBillid());
			}else{
				retvo=this.getProxy().getIArapBillPrivate().findBillByPK(info.getBillid());
			}
		}
		return retvo;
	}
	 public String getBillType() {
		 String djlxbm=null;
		 if("2006030121".equals(this.getNodeCode()))
			 djlxbm= "D0";
		 else if("2006030123".equals(this.getNodeCode()))
			 djlxbm="D2";
		 else if("2008030122".equals(this.getNodeCode()))
			 djlxbm="D1";
		 else if("2008030124".equals(this.getNodeCode()))
			 djlxbm="D3";
		 else if("20040204".equals(this.getNodeCode()))
			 djlxbm="DZ";
		 else if("20040320".equals(this.getNodeCode()))
			 djlxbm="D4";
		 else if("20040321".equals(this.getNodeCode()))
			 djlxbm="D5";
		 else if("20040322".equals(this.getNodeCode()))
			 djlxbm="D6";
		 else
			 djlxbm=BilltypeBO_Client.getBilltypeByNodecode(this.getNodeCode());
		 return djlxbm;
	 }
		/**
		 * 通知业务单据刷新
		 * @param idList
		 * @throws BusinessException
		 */
		public void notifyBusiRefresh(String id) throws BusinessException{
			DJZBVO vo=this.getProxy().getIArapBillPrivate().findBillByPK(id);
			AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(vo);
			this.getDjDataBuffer().refreshDJZBVO(vo.getParentVO().getPrimaryKey(), vo);
//			BillCardPanel arapDjBillCardPanel=this.getArapDjPanel1().getBillCardPanelDj();
//			arapDjBillCardPanel.setHeadItem("scomment", "aaaaaaa");
//			arapDjBillCardPanel.setTailItem("shr", head.getShr());
//			arapDjBillCardPanel.setHeadItem("shrq", head.getShrq());
//			arapDjBillCardPanel.setHeadItem("shkjnd", head.getShkjnd());
//			arapDjBillCardPanel.setHeadItem("shkjqj", head.getShkjqj());
//
//			arapDjBillCardPanel.setHeadItem("sxbzmc", head.getSxbz());// 修改界面状态,配如flow中
//			arapDjBillCardPanel.setHeadItem("sxr", head.getSxr());
//			arapDjBillCardPanel.setHeadItem("sxkjnd", head.getSxkjnd());
//			arapDjBillCardPanel.setHeadItem("sxkjqj", head.getSxkjqj());
//			arapDjBillCardPanel.setHeadItem("sxrq", head.getSxrq());
//
//			arapDjBillCardPanel.setHeadItem("yhqrr", head.getYhqrr());// 修改界面状态,配如flow中
//			arapDjBillCardPanel.setHeadItem("yhqrrq", head.getYhqrrq());
//			arapDjBillCardPanel.setHeadItem("yhqrkjnd", head.getYhqrkjnd());
//			arapDjBillCardPanel.setHeadItem("yhqrkjqj", head.getYhqrkjqj());
//
//			arapDjBillCardPanel.setHeadItem("ts", head.getts());
//			// getArapDjPanel().getBillCardPanelDj().setHeadItem("zyx20",
//			// head.getSpzt());
//			arapDjBillCardPanel.setHeadItem("spzt", DJZBVOUtility.getUIStringVerifyingStatus(head.getSpzt()));
//			String strCurrentPK = this.getDjDataBuffer().getCurrentDJZBVOPK();
//			BillModel billModel = this.getBillListPanel1().getHeadBillModel();
//			int row = this.getBillListPanel1().getHeadTable().getSelectedRow();
//			if (row == -1) {
//				row =  getHeadRowInexByPK(strCurrentPK);
//			} else if (!strCurrentPK.equals(billModel.getValueAt(row, "vouchid"))) {
//
//				for (int i = 0; i < billModel.getRowCount(); i++) {
//					if (strCurrentPK.equals(billModel.getValueAt(row, "vouchid"))) {
//						row = i;
//						break;
//					}
//				}
//			}
//			// 列表中的表头信息
//			billModel.setValueAt(head.getDjzt(), row, "djzt");
//			billModel.setValueAt(DJZBVOUtility.getDjztmc(head.getDjzt()), row, "djzt_mc");
//
//			billModel.setValueAt(head.getShr(), row, "shr");
//			billModel.setValueAt(head.getShrq(), row, "shrq");
//			billModel.setValueAt(head.getAttributeValue("spzt"), row, "spzt");
//			billModel.setValueAt(head.getts(), row, "ts");
			if(null==this.getArapDjPanel1().getBillCardPanelDj().getBillModel())return ;
			this.getArapDjPanel1().setDj(vo);

		}

		public void setIsAloneNode(UFBoolean isAloneNode) {
			IsAloneNode = isAloneNode;
		}

		/**
		 * 判断是否需要根据权限调节按钮
		 * @return
		 */
		public boolean isAdjustButtonByPower(){
			return true;
		}

		/***
		 * 如果需要根据按钮权限调节按钮，则必须返回该功能原生的功能节点
		 * @param pk_tradetype  需要展示的交易类型
		 * @return
		 */
		public String getRawFuncode(){
			return PfDataCache.getBillType(this.getDjDataBuffer().getCurrentDjlxbm()).getNodecode();
		}

		/**
		 * 设置需要处理的单据类型
		 * @param tradetype
		 */
		public void setTradeType(String tradetype){
			this.getDjDataBuffer().setCurrentDjlxbm(tradetype);
		}
		public void updateCmpButtonsPower(ButtonObject[] btns,String nodecode){
			if(!this.getDjSettingParam().getNodeID().equals(nodecode)&&tempbtns==null){
				tempbtns=this.getDjButtons();
			}
			if(null==btns)
				btns=tempbtns;
			this.setButtons(btns, nodecode);
		}
}