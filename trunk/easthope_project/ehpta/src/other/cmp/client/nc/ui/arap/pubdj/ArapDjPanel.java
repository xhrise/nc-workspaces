package nc.ui.arap.pubdj;

/**
 * 此处插入类型说明。其它模块联查单据类 创建日期：(2001-6-19 9:45:58) setm_parent(tofpanel) 必然
 *
 * @author：陈飞 备注:类加载时以下几个方法必须设置 setM_parent(toftpanel); setM_djoid(vouchid)
 *              联查单据必须设置 setM_DjState(0); changeTab2(0);//显示单据界面
 *              setM_pk_corp(pk_corp);//单位 setM_kjnd(kjnd);//会计年度
 *              setM_kjqj(kjqj);//会计期间 setM_Lrr(lrr);//录入人
 *
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.arap.global.ArapSettlementAid;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.cmp.pub.cache.FiPubDataCache;
import nc.cmp.utils.BusiBillStatus;
import nc.itf.fi.pub.Currency;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.cust.ICustBankQry;
import nc.itf.uap.bd.psndoc.IPsnDocQueryService;
import nc.ui.arap.djlx.DjlxrefModel;
import nc.ui.arap.engine.AbstractRuntime;
import nc.ui.arap.global.ArapUFDoubleConverter;
import nc.ui.arap.templetcache.DjlxTempletCacheNew;
import nc.ui.cmp.ISettlementPanel;
import nc.ui.cmp.SettlementPanelAgent;
import nc.ui.cmp.pubbill.CmpBBHLUFDoubleConverter;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.IArapRefModel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapCommonTool;
import nc.vo.arap.global.DjCheckParamVO;
import nc.vo.arap.global.DjVOTreater;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.global.DjVOTreaterHj;
import nc.vo.arap.global.DjVOTreaterSs;
import nc.vo.arap.global.DjVOTreaterYt;
import nc.vo.arap.global.DjVoTreaterYs;
import nc.vo.arap.global.IRuntimeConstans;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.bd.b39.JobmngfilVO;
import nc.vo.bd.psndoc.PsnBasManUnionVO;
import nc.vo.cmp.global.LogTools;
import nc.vo.cmp.settlement.CmpMsg;
import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class ArapDjPanel extends nc.ui.pub.beans.UIPanel   {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3155012705507415119L;
 
	// 即时核销状态：初始 0 ,即时核销 3 ,补差 4
	public int m_iVitStat = ArapBillWorkPageConst.VITINIT;

	public ArapDjBillCardPanel ivjBillCardPanelDj = null;

	private DjZYXBillCardPanel ivjBillCardPanelZyx = null;

	private nc.ui.pub.beans.UITabbedPane ivjUITabbedPane1 = null;
	
	private ISettlementPanel settlePanel=null;

	protected javax.swing.event.EventListenerList DjStateChangeListenerList = new javax.swing.event.EventListenerList();
	//maji add 
//	private DJZBVO tempdjzbvo=null;

	public int m_DjState = 0;

	// 自由项状态,0 增加,1删除,2修改
	public int m_ZyxState = 0;

	public int m_DjzbitemsIndex = 0;


	public String m_Zyxmboid = "System9916205436690";

	
	public nc.ui.pub.ToftPanel m_Parent = null;


	public int m_TabIndex = 0;

	public IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private nc.ui.pub.beans.UIRefPane ivjdjlxRef = null;

	private javax.swing.JPanel ivjJPanel1 = null;

	public int m_SelectedRow = -1;

	public String m_Title = null;
	
	private nc.ui.ep.dj.ARAPDjDataBuffer m_djDataBuffer = null;

	public nc.ui.ep.dj.ARAPDjSettingParam m_djSettingParam = null;

	public DjlxTempletCacheNew cache = null;

	private DjVOTreater m_treater = null;

	private boolean tabChanged = true;
	
	private boolean SelectSettleTab=false;
	//仅用于临时存储表体信息，用于和结算切换用。 NND 用DefaultCurrTypeDecimalAdapter不行，明显又被改过了一次，代码TMD的乱！
	private DJZBVO tempDJZBItemVOs=null;
	private String curdjlxbm=null;
	public String getCurdjlxbm() {
		return curdjlxbm;
	}

	public void setCurdjlxbm(String curdjlxbm) {
		this.curdjlxbm = curdjlxbm;
	}
	
	
	public void setTempletCache(DjlxTempletCacheNew cache) {
		this.cache = cache;
	}

	class IvjEventHandler implements javax.swing.event.ChangeListener {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (e.getSource() == ArapDjPanel.this.getUITabbedPane1())
				if (isTabChanged())
					connEtoC1(e);
		};
	};
	public DjlxTempletCacheNew getCache() {
		return cache;
	}
	public void invokeTempletCache(){
		if(null!=cache)
			cache.preloadTempletBatch();
	}
	/**
	 * ArapDjPanel 构造子注解。
	 */
	public ArapDjPanel(ARAPDjSettingParam settingParam,ToftPanel parent ) {
		super();
		this.m_djSettingParam = settingParam;
		this.setM_Parent(parent);
		if (this.m_djSettingParam == null) {
			nc.bs.logging.Log.getInstance(this.getClass()).warn("setting is null in ArapDjPanel 0");
		} else
			nc.bs.logging.Log.getInstance(this.getClass()).warn("setting is not null in ArapDjPanel 0");

		initialize();
	}

	/**
	 * ArapDjPanel 构造子注解。
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 */
	public ArapDjPanel(java.awt.LayoutManager p0) {
		super(p0);
	}

	/**
	 * ArapDjPanel 构造子注解。
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 * @param p1
	 *            boolean
	 */
	public ArapDjPanel(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
	}

	/**
	 * ArapDjPanel 构造子注解。
	 * 
	 * @param p0
	 *            boolean
	 */
	public ArapDjPanel(boolean p0) {
		super(p0);
	}

	/**
	 * 功能: 作者：陈飞 创建时间：(2001-8-22 14:53:30) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.DjStateChangeListener
	 */
	public void addDjStateChangeListener(DjStateChangeListener listener) {
		DjStateChangeListenerList.add(DjStateChangeListener.class, listener);
	}

	private void saveTempDJZBItemVOs(){
		nc.ui.pub.bill.BillItem Btbbhl_B = this.getBillCardPanelDj().getBodyItem("bbhl");
		Btbbhl_B.setConverter(new ArapUFDoubleConverter());
		this.tempDJZBItemVOs=(DJZBVO)this.getBillCardPanelDj().getBillValueVO("nc.vo.ep.dj.DJZBVO",
				"nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");
	}
	public void changeTab2(int tabIndex) throws Exception {
		changeTab2(tabIndex,false);
	}
	/**
	 * 功能:改变页签(单据和自由项之间)
	 * 
	 * 当改变到自由项时: A.动态改变自由项单据模板: 动态改变单据模板 1.可在单据模板加载时备份BillTempletVO(改变时使用) ????
	 * 2.改变BillTempletVO数据 3.重新new BillData(BillTempletVO)
	 * 4.调用BillCardPanel.setBillData(BillData)
	 * B.设置自由项表体:1.当单据傅表自由项vo数组相应项有数据时显示当前数据,2否则当单据处于非增加状态时联查自由项 B
	 * 改变为===>>>>>>>> 阿飞：任何情况联查询自由项,且设置自由项状态为初始状态
	 * C.设置自由项buttons:只显示自由项的增加,修改,删除,增行,删行,保存,取消 D.设置自由项表头:单据表体相应行的某些项设置为自由项表头
	 * 
	 * 当改变到单据时: 设置单据的buttons:只显示单据相应的button
	 * 
	 * 创建日期：(2001-5-21 15:07:39) 作者:阿飞 参数:tabIndex 页签码
	 */
	public void changeTab2(int tabIndex,boolean bInitializing) throws Exception {
		// cf test begin
		if(tabIndex==2){
			((DjPanel)((DjPanel)this.m_Parent)).updateCmpButtonsPower(getSettlePanel().getButtons(),"20040401");
//				saveTempDJZBItemVOs();
				invokeSettlePamel();
				((DjPanel)this.m_Parent).refreshBtnStats();
				setM_TabIndex(tabIndex);
		} else{
			setSelectSettleTab(false);
			if(this.getM_TabIndex()==2){
				getSettlePanel().deSelected();
				((AbstractRuntime)this.m_Parent).setAttribute(IRuntimeConstans.SettleVO, getSettlePanel().getSettlementInfo());
				((DjPanel)((DjPanel)this.m_Parent)).updateCmpButtonsPower(null,this.getDjSettingParam().getNodeID());
				
			}
			if (tabIndex == 1)// 切换到自由项
			{
				if (getJPanel1().getComponentCount() < 1) {
					getJPanel1().add(getBillCardPanelZyx(), "Center");
				}
			}
			// cf test end
			if (m_Parent != null)
				// m_Parent.showHintMessage("正初始化数据，请稍候...");
				m_Parent.showHintMessage(" ");
			// refactoring:useless member var
			setM_TabIndex(tabIndex);
			// m_TabIndex=tabIndex;
	
			// 0为单据,1为自由项
	
			if (tabIndex == 0) {			
//				if (tempDJZBItemVOs != null&& (this.getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT
//						|| this.getM_DjState() == ArapBillWorkPageConst.WORKSTAT_NEW 
//						|| this.getM_DjState() == ArapBillWorkPageConst.WORKSTAT_ADJUST)
//						|| this.getM_DjState() == ArapBillWorkPageConst.is_BuCha) {
//					AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(tempDJZBItemVOs);
//					getBillCardPanelDj().setBillValueVO(tempDJZBItemVOs);
//					tempDJZBItemVOs=null;
//					getBillCardPanelDj().getBillModel().execLoadFormula();
//					getBillCardPanelDj().execHeadLoadFormulas();
//				}else {
//					if (this.getDjDataBuffer().getCurrentDJZBVO()!=null) {
//						AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(this.getDjDataBuffer().getCurrentDJZBVO());
//						getBillCardPanelDj().setBillValueVO(this.getDjDataBuffer().getCurrentDJZBVO());
//						getBillCardPanelDj().getBillModel().execLoadFormula();
//						getBillCardPanelDj().execHeadLoadFormulas();
//					}
//				}				
			} else {
				int intSelectedRow = this.getBillCardPanelDj().getBillTable().getSelectedRow();
				this.ivjBillCardPanelZyx.setItem(this.getSelectBodyVO(intSelectedRow));
				try {
					this.ivjBillCardPanelZyx.refZyx();
	
				} catch (Throwable e) {
					Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000757")/*
																													 * @res
																													 * "联查自由项出错:"
																													 */
							+ e);
					this.m_Parent.showErrorMessage(e.getMessage());
				}
	
				// 以上处理B
	
				setM_ZyxState(0);
			}
		}
	}

	private void invokeSettlePamel() throws BusinessException{
		setSelectSettleTab(true);
		getSettlePanel().setToftPanel(this.m_Parent);
		
		
    	String fb_oid_temp=ArapCommonTool.fb_temp_pk;
    	List<String> fb_oids=new ArrayList<String>();
		for(int i=0,size=this.getBillCardPanelDj().getBillModel().getRowCount();i<size;i++){
			Object fb_oid=this.getBillCardPanelDj().getBodyValueAt(i, "fb_oid");
			if(null!=fb_oid)
				fb_oids.add((String)fb_oid);
			 
		}
		int count =0;
		for(int i=0,size=this.getBillCardPanelDj().getBillModel().getRowCount();i<size;i++){
			String fb_oid=(String)this.getBillCardPanelDj().getBodyValueAt(i, "fb_oid");
			while(null==fb_oid){
				fb_oid=fb_oid_temp+count;
				if(!fb_oids.contains(fb_oid)){
					fb_oids.add(fb_oid);
					this.getBillCardPanelDj().setBodyValueAt(fb_oid, i, "fb_oid");
				}
				count++;
			}
		}
		 getBillCardPanelDj().getBodyItem("bbhl").setConverter(new ArapUFDoubleConverter());
		nc.vo.ep.dj.DJZBVO djzbvo = (nc.vo.ep.dj.DJZBVO) getBillCardPanelDj()
		.getBillValueVO("nc.vo.ep.dj.DJZBVO",
				"nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");
		if (null!=this.getDjDataBuffer().getCurrentDJZBVO()&&null!=djzbvo) {
			((DJZBHeaderVO)djzbvo.getParentVO()).setTransientFlag(((DJZBHeaderVO)this.getDjDataBuffer().getCurrentDJZBVO().getParentVO()).getTransientFlag());
			this.getBillCardPanelDj().getHeadItem("transientFlag").setValue(((DJZBHeaderVO)djzbvo.getParentVO()).getTransientFlag());
		}
//		this.getBillCardPanelDj().setBillValueVO(djzbvo);
		CmpMsg msg=ArapSettlementAid.getCmpMsg(djzbvo,this.getDjSettingParam().getLoginDate());
		msg.setMsgType( m_DjState==0?CmpMsg.ProcShow:CmpMsg.ProcEdit);
		msg.setNewBill(m_DjState==1);
		String node=this.getDjSettingParam().getNodeID();
		msg.setBusiBillStatus(getBusiBillStatusByNode(node));
		Logger.error("开始调用结算ISettlementPanel接口");
    	LogTools.outputArgs(msg, djzbvo,(SettlementAggVO)((AbstractRuntime)this.m_Parent).getAttribute(IRuntimeConstans.SettleVO));
    	getSettlePanel().procMsg(msg, djzbvo,(SettlementAggVO)((AbstractRuntime)this.m_Parent).getAttribute(IRuntimeConstans.SettleVO));
    	getSettlePanel().selected();

		
	}
	
	public BusiBillStatus getBusiBillStatusByNode(String node){
		if(((DjPanel)this.m_Parent).getPanelProp()==2){
			return BusiBillStatus.QUERY;
		}else{
			if("2006030102".equals(node)||"2008030102".equals(node)||"20040302".equals(node)){
				return BusiBillStatus.MANAGER;
			}else if("2006030101".equals(node)||"2008030101".equals(node)||"20040301".equals(node)){
				return BusiBillStatus.RECORD;
			}else if("2006030103".equals(node)||"2008030103".equals(node)||"20040303".equals(node)){
				return BusiBillStatus.CONFIRM;
			}else if("20060501".equals(node)||"20080501".equals(node)||"20040305".equals(node)){
				return BusiBillStatus.QUERY;
			}else{
				return BusiBillStatus.RECORD;
			}
		}

	}
	private nc.vo.ep.dj.DJZBItemVO getSelectBodyVO(int intSelectedRow) {
		return (nc.vo.ep.dj.DJZBItemVO) this.getBillCardPanelDj().getBillModel().getBodyValueRowVO(intSelectedRow,
				"nc.vo.ep.dj.DJZBItemVO");

	}

	/**
	 * 功能:当用户改变项目名称时动态设置项目阶段参照(针对应收,和应付单) 算法: 创建日期：(2001-6-26 10:05:52)
	 * 
	 * @author：陈飞
	 * 
	 * 
	 * 
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void changeXmmc1(nc.ui.pub.bill.BillEditEvent e) throws Exception {

		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();
		// get current djdl
		String strDjdl = dataBuffer.getCurrentDjdl();

		Logger.debug("key= " + e.getKey());
		if (e.getKey().equals("xmmc1")
				&& (strDjdl.equalsIgnoreCase("fk") || strDjdl.equalsIgnoreCase("sk") || strDjdl.equalsIgnoreCase("ys") || strDjdl
						.equalsIgnoreCase("yf"))) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000758")/*
																											 * @res
																											 * "设置项目阶段参照"
																											 */);
			try {
			
				nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanelDj().getBodyItem("xmjdmc")
						.getComponent();
				// String
				// pk_jobmngfil=getBillCardPanelDj().getBodyValueAt(e.getRow(),"pk_jobmngfil").toString();//项目管理档案主键
				String pk_jobmngfil = getBillCardPanelDj().getBodyValueAt(e.getRow(), "jobid").toString();
				// 项目管理档案主键
				nc.ui.bd.b39.PhaseRefModel model = (nc.ui.bd.b39.PhaseRefModel) ref.getRefModel();
				String dwbm=(String)this.getBillCardPanelDj().getHeadItem("dwbm").getValueObject();
				JobmngfilVO vo=(JobmngfilVO) NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(JobmngfilVO.class, " pk_jobbasfil='"+pk_jobmngfil+"' and pk_corp='"+dwbm+"'");

				model.setJobID(vo.getPk_jobmngfil());

				getBillCardPanelDj().setBodyValueAt(null, e.getRow(), "xmjdmc");// 项目阶段清零
				getBillCardPanelDj().setBodyValueAt(null, e.getRow(), "pk_jobobjpha");
				getBillCardPanelDj().setBodyValueAt(null, e.getRow(), "jobphaseid");

			} catch (Throwable ee) {
				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000759")/*
																												 * @res
																												 * "设置项目阶段参照,这个错误也许可以勿略:"
																												 */
						+ ee);
			}

		}

	}

	/**
	 * 功能:当用户改变收支项目时删除相应公开要素同时查询收支项目定义 简要流程: 创建日期：(2001-6-7 9:40:44)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void changeSzxm(int SelectedRow) throws java.lang.Exception {

		// refactoring
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

		nc.vo.ep.dj.DJZBItemVO[] djzbitems = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();

		int j = getdjzbitemIndex(SelectedRow); // 根据单据选择的行号取curdjzbvo中对应的序号
		if (j < 0)
			return;
		// m_DjzbIndex = j; //设置curdjzbvo当前位置
		djzbitems[j].setChildrenVO(null); // 删除自由项数据
		djzbitems[j].freepropertys = null; // 删除自由项定义

		if (getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid") == null
				|| getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString().trim().length() < 1)
		// 如果当前单据主表的表体对应收支项目为空,则自由项不可用
		{
			getUITabbedPane1().setEnabledAt(1, false);
			return;
		}

		getUITabbedPane1().setEnabledAt(1, true);

		// 以下联查对应 收支项目的自由项定义
		Vector<?> freeDY = new Vector(); // 收支项目自由项定义
		try {

			freeDY = ((AbstractRuntime)this.m_Parent).getProxy().getIArapBillPrivate().getFreePropertys(
					this.getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString());
		} catch (Exception e) {
			getUITabbedPane1().setEnabledAt(1, false);

			return;
		}

		if (freeDY == null || freeDY.size() < 1)
		// 收支项目没有定义公开要素,自由项不可用
		{
			getUITabbedPane1().setEnabledAt(1, false);
			return;
		}
		Logger.debug("getFreePropertys.size=" + freeDY.size());

		// 以下将公开要素定义保存在全局变量中
		djzbitems[j].freepropertys = new nc.vo.ep.dj.DefdefVO[freeDY.size()];
		for (int i = 0; i < freeDY.size(); i++) {
			djzbitems[j].freepropertys[i] = (nc.vo.ep.dj.DefdefVO) freeDY.elementAt(i);
		}

	}

	/**
	 * connEtoC1:
	 * (UITabbedPane1.change.stateChanged(javax.swing.event.ChangeEvent) -->
	 * ArapDjPanel.changeTab2(I)V)
	 * 
	 * @param arg1
	 *            javax.swing.event.ChangeEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(javax.swing.event.ChangeEvent arg1) {
		try {

			this.changeTab2(getUITabbedPane1().getSelectedIndex());

		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			ExceptionHandler.consume(ivjExc);
		}
	}


	/**
	 * 功能:修改 创建日期：(2001-5-21 17:00:09)
	 * 
	 * @return boolean 作者:阿飞
	 */
	public boolean edit_Pj() {
		getBillCardPanelDj().transferFocusTo(0);

		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

		int m_Syscode = ((nc.vo.ep.dj.DJZBHeaderVO) dataBuffer.getCurrentDJZBVO().getParentVO()).getPzglh().intValue();
		getDjSettingParam().setSyscode(m_Syscode);
		// ArapDjVOCheck djVOCheck = new ArapDjVOCheck(this);
		DjCheckParamVO paramvo = new DjCheckParamVO();
		paramvo.setCurUser(this.getDjSettingParam().getPk_user());

		ResMessage res = m_treater.check_Edit(dataBuffer.getCurrentDJZBVO(), paramvo, false);
		if (!res.isSuccess) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPP2006030102-000375")/* @res "错误" */, res.strMessage);
			return false;
		}
		getBillCardPanelDj().setEnabled(true); // 设置修改状态
		getBillCardPanelDj().getHeadItem("djbh").getComponent().setEnabled(false);
		// 单据号不可修改

		try {
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().rCResetBody(0, true, false);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}

		return true;
	}

	/**
	 * 是否存在业务员未关闭的事项审批单 此处插入方法描述。 创建日期：(2004-2-19 14:31:47)
	 * 
	 * @return boolean
	 * @param pk_corp
	 *            java.lang.String
	 * @param ywybm
	 *            java.lang.String pk_corp公司，ywybm业务员，billcode事项审批单类型
	 */
	public boolean eXist_SS(String pk_corp, String[] ywybm, String billCode, String vouchid) {
		if (getM_DjState() != 3)
			vouchid = null;
		boolean b = false;
		if (ywybm == null || ywybm.length < 1)
			return b;
		try {
			b = nc.impl.arap.proxy.Proxy.getIArapItemPrivate().existSs(pk_corp, ywybm, billCode, vouchid);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000763")/*
																														 * @res
																														 * "前款未清不能费用审请出错"
																														 */);
			b = true;
		}
		return b;
	}

	/**
	 * 功能:删除单据成功时发送信息 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireDelDj(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).deletedDj(e);
			}
		}

	}

	/**
	 * 功能: 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireDjStateChange(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).djStateChange(e);
			}
		}

	}

	/**
	 * 功能:修改单据保存成功后发送信息 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireSaveEditDj(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).saveEditDj(e);
			}
		}

	}

	/**
	 * 功能:新增单据保存成功后发送信息 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireSaveNewDj(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).saveNewDj(e);
			}
		}

	}

	/**
	 * 功能: 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireSelectedBodyRow(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).selectedBodyRow(e);
			}
		}

	}

	/**
	 * 功能: 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireTabIndexChange(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).tabIndexChange(e);
			}
		}

	}

	/**
	 * 功能: 作者：陈飞 创建时间：(2001-8-22 14:58:04) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.ArapDjPanel
	 */
	public void fireZyxStateChange(ArapDjPanel e) {
		Object[] listeners = DjStateChangeListenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DjStateChangeListener.class) {
				((DjStateChangeListener) listeners[i + 1]).zyxStateChange(e);
			}
		}

	}

	/**
	 * 返回 BillCardPanel1 特性值。
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* 警告：此方法将重新生成。 */
	public ArapDjBillCardPanel getBillCardPanelDj() {

		// nc.bs.logging.Log.getInstance(this.getClass()).warn("getBillCardPanelDj1");
		if (ivjBillCardPanelDj == null) {
			try {
				ivjBillCardPanelDj = new ArapDjBillCardPanel(this); // {
				ivjBillCardPanelDj.setTatolRowShow(true);
				nc.vo.pub.bill.BillRendererVO voCell = new nc.vo.pub.bill.BillRendererVO();
				voCell.setShowThMark(true);
				voCell.setShowZeroLikeNull(true);
				ivjBillCardPanelDj.setBodyShowFlags(voCell);
				// ivjBillCardPanelDj.setShowThMark(true);
				// // user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBillCardPanelDj;
	}

	/**
	 * 返回 BillCardPanel2 特性值。
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* 警告：此方法将重新生成。 */
	public DjZYXBillCardPanel getBillCardPanelZyx() {
		if (ivjBillCardPanelZyx == null) {
			try {
				ivjBillCardPanelZyx = new DjZYXBillCardPanel();

				ivjBillCardPanelZyx.setDjDataBuffer(this.getDjDataBuffer());
			} catch (Exception e) {
				nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			}
		}

		return ivjBillCardPanelZyx;
	}

	/**
	 * author:wangqiang create time:2004-09-28 after refactoring start
	 * function:get the bill data buffer
	 */
	public nc.ui.ep.dj.ARAPDjDataBuffer getDjDataBuffer() {
		// for debuging only
		if (m_djDataBuffer == null)
			nc.bs.logging.Log.getInstance(this.getClass()).warn("WARNING:data buffer is null");
		return m_djDataBuffer;
	}

	/**
	 * 返回 djlxRef 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UIRefPane getdjlxRef() {

		if (ivjdjlxRef == null) {
			try {
				ivjdjlxRef = new nc.ui.pub.beans.UIRefPane(this);
				ivjdjlxRef.setName("djlxRef");
				ivjdjlxRef.setLocation(578, 458);
				// user code begin {1}
				ivjdjlxRef.setIsCustomDefined(true);
				ivjdjlxRef.setVisible(false);
				DjlxrefModel refModel = new DjlxrefModel();
				// refModel.setWherePart(" where dwbm='" + getM_Pk_corp() + "'
				// or dwbm='@@@@'");
				String sqlWhere = getDjlxWhere(false);
				refModel.setWherePart(sqlWhere);
				getdjlxRef().setRefModel(refModel);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
			// this.add(getdjlxRef());
		}
		return ivjdjlxRef;
	}
/**
 * @param fcbzflag true --包括封存；false--不包括封存
 * 得到单据类型过滤条件方法
 * update by Songtao 20080721
 * 从getdjlxRef（）方法中分离出来这部分，以便被查询模板使用。
 * */
	public String getDjlxWhere(boolean fcbzflag) {
		//TODO 逻辑需要根据查询使用做一些调整，在查询条件中的需要包含封存单据类型
		
		StringBuilder sqlWhere = new StringBuilder();
		// 期初余额只包括,应收单,应付单,收款单,付款单 这4种单据类型
		if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_EP && !"20040202".equals(getDjSettingParam().getNodeID())) {
			String sql=((AbstractRuntime)m_Parent).getProxy().getDjlxWhere();
			if ("20040305".equals(getDjSettingParam().getNodeID())) {
				sqlWhere.append(sql.replaceAll("and djlxbm<>'DR'", ""));
				 
			}else{
				sqlWhere.append(sql);
			}
			
		} 
		else if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_SS  ) {
			sqlWhere.append(" where ( arap_djlx.dr=0 and dwbm='").append(getDjSettingParam().getPk_corp())
					.append("') and djdl='ss' ");
		} else if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_YuTi)// 预提单
		// rocking
		// 005-8-25
		{
			sqlWhere.append(" where ( arap_djlx.dr=0 and dwbm='").append(getDjSettingParam().getPk_corp())
					.append("') and djdl='yt' ");
			
		} else {
			if (getDjSettingParam().getIsQc()) {

				if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_AR) {
					sqlWhere
							.append(" where ( arap_djlx.dr=0 and dwbm='")
							.append(getDjSettingParam().getPk_corp())
							.append(
									"') and djdl in ('ys','sk','yf','fk') ")
							.append(" and arap_djlx.usesystem in ('0','3')");
				} else if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_AP) {
					sqlWhere
							.append(" where ( arap_djlx.dr=0 and dwbm='")
							.append(getDjSettingParam().getPk_corp())
							.append(
									"') and djdl in ('ys','sk','yf','fk')   ")
							.append(" and arap_djlx.usesystem in ('1','3')");
				} else {
					sqlWhere
							.append(" where ( arap_djlx.dr=0 and dwbm='")
							.append(getDjSettingParam().getPk_corp())
							.append(
									"') and djdl in ('ys','sk','yf','fk')  ")
							.append(" and arap_djlx.usesystem in ('2','3')");
				}
			} else {
				/*
				 * 根据不同系统设置单据类型选择范围 应收系统 只能选择应收、收款、应付、付款（往来对象为客户）； 应付系统
				 * 只能选择应付、付款、应收、收款（往来对象为供应商）； 报账中心
				 * 能选择应付、付款、应收、收款、对外收款结算单、对外付款结算单（往来对象为部门、业务员）；
				 */
				// m_Syscode = 0; //o应收,1应付,2报账中心
				
				if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AR 
						|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_AR) {
					sqlWhere
							.append(" where ( arap_djlx.dr=0 and dwbm='")
							.append(getDjSettingParam().getPk_corp())
							.append("') and djdl in ('ys','sk','yf','fk')")
							.append(" and arap_djlx.usesystem in ('0','3')");
				} else if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_AP
						|| getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_XT_AP) {
					sqlWhere
							.append(" where ( arap_djlx.dr=0 and dwbm='")
							.append(getDjSettingParam().getPk_corp())
							.append("') and djdl in ('ys','sk','yf','fk')  ")
							.append(" and arap_djlx.usesystem in ('1','3')");
				} else if(getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_DjQuery){
					sqlWhere
					.append(" where ( arap_djlx.dr=0 and dwbm='")
					.append(getDjSettingParam().getPk_corp())
					.append(
							"') and djdl in ('ys','sk','yf','fk')   ")
					.append(" and arap_djlx.usesystem in ('0','1','3')");
				}else {
					sqlWhere.append(" where ( arap_djlx.dr=0 and dwbm='").append(
							getDjSettingParam().getPk_corp()).append(
							"') and djdl in ('sj','fj','hj') and djlxbm<>'DR'  ")
							.append(" and arap_djlx.usesystem in ('2','3')");
				}
			}
		}
		if (!fcbzflag) {
			sqlWhere.append(" and fcbz='N' ");
		}
		return sqlWhere.toString();
	}

	/**
	 * author:wangqiang create time: 2004-09-28 function: get the SettingParam
	 * which provide the global vars
	 */
	public nc.ui.ep.dj.ARAPDjSettingParam getDjSettingParam() {
		return m_djSettingParam;
	}

	/**
	 * 功能:根据单据选择的行号取curdjzbvo中对应的序号 简要流程: 创建日期：(2001-6-6 15:07:06)
	 * 
	 * @author：陈飞
	 * @return int -1 失败
	 * @param selectedRow
	 *            int
	 */
	public int getdjzbitemIndex(int SelectedRow) throws Exception {
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000772")/*
																										 * @res
																										 * "选中的行:"
																										 */
				+ SelectedRow);
		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();

		nc.vo.ep.dj.DJZBItemVO[] djzbitems = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000773")/*
																										 * @res
																										 * "全局行数:"
																										 */
				+ djzbitems.length);

		if (SelectedRow < 0 || SelectedRow >= djzbitems.length)
			return -1;

		int selectedDjzbitemsIndex = (new Integer(getBillCardPanelDj().getBodyValueAt(SelectedRow, "djzbitemsIndex")
				.toString())).intValue();
		// DJZBItemVO[]中的唯一标识
		int j = 0;
		for (j = 0; j < djzbitems.length; j++) {
			if (djzbitems[j].getDjzbitemsIndex() != null
					&& djzbitems[j].getDjzbitemsIndex().intValue() == selectedDjzbitemsIndex)
				break;
		}
		if (j == djzbitems.length)
		// 未匹配
		{
			// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000774")/*@res
			// "未匹配(m_djzbitemsIndex)"*/);
			return -1;
		}

		return j;
	}

	/**
	 * 功能:定位 自定义项定义
	 * 
	 * 
	 * 简要流程:
	 * 
	 * 
	 * 创建日期：(2001-5-21 15:07:39) 作者:阿飞 参数:FreeSxh 自由项序号,1:自定义项1,以此类推 return:-1失败
	 */

	/**
	 * 返回 JPanel1 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	// getJPanel1().add(getBillCardPanelZyx(), "Center");
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getJPanel1() {
		if (ivjJPanel1 == null) {
			try {
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.BorderLayout());
//				ivjJPanel1.setToolTipText(text);
				// getJPanel1().add(getBillCardPanelZyx(), "Center");
				// user code begin {1}
				// getJPanel1().add(getBillCardPanelZyx(), "Center");
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
	 * 功能: 简要流程: 创建日期：(2001-6-19 11:14:45)
	 * 
	 * @author：陈飞
	 * @return int
	 */
	public int getM_DjState() {
		return m_DjState;
	}

	/**
	 * 功能:设置ToftPanel 作者：陈飞 创建时间：(2001-7-9 16:45:36) 参数： <|>返回值： 算法：
	 * 
	 * @return nc.ui.pub.ToftPanel
	 */
	public nc.ui.pub.ToftPanel getM_Parent() {
		return m_Parent;
	}

	/**
	 * δ? 作者：陈飞 创建时间：(2001-8-22 17:08:48) 参数： <|>返回值： 算法：
	 * 
	 * @return int
	 */
	public int getM_TabIndex() {
		return m_TabIndex;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2004-3-10 16:23:35)
	 * 
	 * @author：chenf
	 * @return java.lang.String
	 */
	public java.lang.String getM_Title() {
		return m_Title;
	}

	/**
	 * δ埽篭n此处插入方法说明。 创建日期：(2001-8-28 15:38:11)
	 * 
	 * @author：陈飞 算法： 备注：
	 * @return java.lang.String
	 */
	public java.lang.String getM_Zyxmboid() {
		return m_Zyxmboid;
	}

	/**
	 * 功能: 简要流程: 创建日期：(2001-6-19 11:14:45)
	 * 
	 * @author：陈飞
	 * @return int
	 */
	public int getM_ZyxState() {
		return m_ZyxState;
	}

	/**
	 * 返回 UITabbedPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITabbedPane
	 */
	/* 警告：此方法将重新生成。 */
	public nc.ui.pub.beans.UITabbedPane getUITabbedPane1() {
		if (ivjUITabbedPane1 == null) {
			try {
				ivjUITabbedPane1 = new nc.ui.pub.beans.UITabbedPane();
				ivjUITabbedPane1.setName("UITabbedPane1");
				ivjUITabbedPane1.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
						"UPT2006030102-000021")/* @res "单据" */, null, getBillCardPanelDj(), null, 0);
//				ivjUITabbedPane1.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0003327") , null, getJPanel1(), null, 1);
				ivjUITabbedPane1.insertTab("" , null, getJPanel1(), null, 1);
				ivjUITabbedPane1.addChangeListener( ivjEventHandler);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		
		return ivjUITabbedPane1;
		
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
	}

	/*
	 * author:wangqiang create time: 2004-08-04 function : hide the
	 * "serial_number" column in the card panel body only for procceeding
	 * bill,this method is effective remark:only called by nc.ui.ep.dj.DjPanel
	 */
	public void hideGroupingSerialNumber() {
		// before hide the "serial_number" column ,make sure it exist indeed
		BillItem item = getBillCardPanelDj().getBodyItem("serial_number");
		// if the "serial_number" item exist,then hide this item.
		if (item != null) {
			if (getBillCardPanelDj().getBodyItem("serial_number").isShow() == true) {
				getBillCardPanelDj().hideBodyTableCol("serial_number");
			}
		}
	}




	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			setName("ArapDjPanel");
			setLayout(new java.awt.BorderLayout());
			setSize(663, 411);
			add(getUITabbedPane1(), "Center");
			// initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}		
		getUITabbedPane1().setEnabledAt(1, false);
		getBillCardPanelDj().setEnabled(false);
	}


	/**
	 * 功能: 作者：陈飞 创建时间：(2001-8-22 14:53:30) 参数： <|>返回值： 算法：
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.DjStateChangeListener
	 */
	public void removeDjStateChangeListener(DjStateChangeListener listener) {
		DjStateChangeListenerList.remove(DjStateChangeListener.class, listener);
	}

	/**
	 * 此处插入方法描述。 根据单据每一表体行币种的不同重新设置单据每一行的币种小数位数 创建日期：(2003-3-20 9:26:33)
	 */
	private void resetDjByCurrency(DJZBItemVO[] items) throws Exception {
		int rowCount = getBillCardPanelDj().getRowCount();
		String pkcorp = getDjSettingParam().getPk_corp();
		String date = (String)getBillCardPanelDj().getHeadItem("djrq").getValueObject(); // 单据日期
		if (getDjSettingParam().getIsQc())
			// date=getQyrq2().toString(); //启用日期
			date = getDjSettingParam().getQyrq2().toString();
		if (getDjSettingParam().getIsBlnLocalFrac() != null) {
			if (getDjSettingParam().getIsBlnLocalFrac().booleanValue()) {
				UFDouble bbhl = Currency.getRate(pkcorp, getDjSettingParam().getFracCurrPK(), Currency
						.getLocalCurrPK(pkcorp), date);
				if (bbhl != null) {
					getDjSettingParam().setTempRate(getDjSettingParam().getFracCurrPK(), date, bbhl);
				}
			} else {
				getDjSettingParam().setTempRate(getDjSettingParam().getLocalCurrPK(), date, new UFDouble(1.0));
			}
		}
//		DJZBItemVO[] items=(DJZBItemVO[])djzbvo.getChildrenVO();
		for (int i = 0; i < rowCount; i++) {
			String pk_currtype = "";
			pk_currtype = getBillCardPanelDj().getBodyValueAt(i, "bzbm") == null ? null : getBillCardPanelDj()
					.getBodyValueAt(i, "bzbm").toString();
			if (pk_currtype != null) {
				if (getDjSettingParam().getTempRate(pk_currtype, date) == null) {
					UFDouble bbhl = Currency.getRate(pkcorp, pk_currtype, Currency.getLocalCurrPK(pkcorp), date);
					if (bbhl != null) {
						getDjSettingParam().setTempRate(pk_currtype, date, bbhl);
					}
				}
//				adjustRowCurrencyDigit(pk_currtype, i,items[i]);
			}
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().rCResetBody(i, true, false);
		}
		getDjSettingParam().clearTempRateHashtable();
	}


	/**
	 * 此处插入方法说明。 创建日期：(2003-9-8 15:01:26)
	 */
	public nc.vo.ep.dj.DJZBVO getBcDjvo() {
		getBillCardPanelDj().stopEditing();
		try {
			getBillCardPanelDj().dataNotNullValidate();
		} catch (nc.vo.pub.ValidationException e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			m_Parent.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000765")/*
																														 * @res
																														 * "单据必输项未录数据: "
																														 */
					+ e.getMessage());
			return null;
		}

		int itemCount = 0;

		// 取表体行数
		itemCount = getBillCardPanelDj().getBillModel().getRowCount();
		if (itemCount <= 0) {
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000766")/*
																														 * @res
																														 * "表体不能为空,保存失败"
																														 */);
			return null;
		}

		nc.vo.ep.dj.DJZBItemVO[] itemsvo = new nc.vo.ep.dj.DJZBItemVO[itemCount];
		// 表体vo
		nc.vo.ep.dj.DJZBHeaderVO headvo = new nc.vo.ep.dj.DJZBHeaderVO();
		// qdq add for pf
		headvo.setShr(headvo.getLrr());
		headvo.setShrq(headvo.getDjrq());
		//
		// 表头vo
		nc.vo.ep.dj.DJZBVO djzbvo = new nc.vo.ep.dj.DJZBVO();
		// 单据vo

		// 取单据vo
		djzbvo = (nc.vo.ep.dj.DJZBVO) getBillCardPanelDj().getBillValueChangeVO("nc.vo.ep.dj.DJZBVO",
				"nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");

		// 以下准备表头数据

		headvo = (nc.vo.ep.dj.DJZBHeaderVO) djzbvo.getParentVO();

		try {
			// refactoring(2004-11-1),remove this invocation out of
			// befSaveChangeHVo method
			this.getBillCardPanelDj().getM_cardTreater().setHeadJe();

			// headvo = befSaveChangeHVo(headvo);
			// refactoring 2004-11-1
			// ArapDjVOTreat treat = new ArapDjVOTreat(this);
			headvo = new DjVOChanger((AbstractRuntime)this.m_Parent, this).befSaveChangeHVo(djzbvo, this.getM_DjState());

		} catch (Throwable e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000178")/*
																														 * @res
																														 * "操作失败"
																														 */);
			return null;
		}
		// headvo.setZyx20(null);

		djzbvo.setParentVO(headvo);

		// 以上准备表头数据

		// 以下准备表体数据

		try {
			itemsvo = new DjVOChanger((AbstractRuntime)this.m_Parent, this).befSaveChangeBVo(djzbvo);
		} catch (Throwable e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000178")/*
																														 * @res
																														 * "操作失败"
																														 */);
			return null;
		}
		djzbvo.setChildrenVO(itemsvo);
		// 以上准备表体数据
		return djzbvo;

	}

	/**
	 * 功能:当用户在表体中换行时(对自由项的影响) 简要流程: 如果当前单据主表的表体对应收支项目为空,则自由项不可用 否则(自由项可用)
	 * //如果对应收支项目的自由项有数据,则return //否则 如果对应收支项目的自由项定义有数据,则return
	 * 否则,联查对应收支项目的自由项定义 如果对应收支项目的自由项没有定义,则自由项不可用
	 * 
	 * 创建日期：(2001-6-5 12:17:32)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void selectedSzxm(int SelectedRow) throws Exception {
		//

		if (getM_DjState() == 1 || getM_DjState() == 3) {
			// 当单据增加或修改时自由项不可用
			getUITabbedPane1().setEnabledAt(1, false);
			return;
		}
		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

		nc.vo.ep.dj.DJZBItemVO[] djzbitems = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();

		int j = getdjzbitemIndex(SelectedRow); // 根据单据选择的行号取curdjzbvo中对应的序号
		if (j < 0)
			return;
		// m_DjzbIndex = j; //设置curdjzbvo当前位置

		// if( djzbitems[j].getSzxmid()==null ||
		// djzbitems[j].getSzxmid().trim().length()<1)
		if (getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid") == null
				|| getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString().trim().length() < 1)
		// 如果当前单据主表的表体对应收支项目为空,则自由项不可用
		{
			getUITabbedPane1().setEnabledAt(1, false);
		} else {
			getUITabbedPane1().setEnabledAt(1, true);

			if (djzbitems[j].freepropertys == null || djzbitems[j].freepropertys.length < 1)
			// 联查对应 收支项目的自由项定义
			{
				Vector freeDY = new Vector(); // 收支项目自由项定义

			
				try {

					freeDY = ((AbstractRuntime)this.m_Parent).getProxy().getIArapBillPrivate().getFreePropertys(
							getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString());
				} catch (Exception e) {
					
					return;
				}
				Logger.debug("getFreePropertys.size=" + freeDY.size());
				if (freeDY == null || freeDY.size() < 1)
				// 收支项目没有定义公开要素,自由项不可用
				{
					getUITabbedPane1().setEnabledAt(1, false);
					return;
				}
				// 以下将公开要素定义保存在全局变量中
				djzbitems[j].freepropertys = new nc.vo.ep.dj.DefdefVO[freeDY.size()];
				for (int i = 0; i < freeDY.size(); i++) {
					djzbitems[j].freepropertys[i] = (nc.vo.ep.dj.DefdefVO) freeDY.elementAt(i);
				}
			} else
			// 对应 收支项目的自由项定义有数据
			{
				return;
			}


			return;

			// }

		}

	}

	/**
	 * 功能:设置当前要显示的单据 简要流程: 创建日期：(2001-6-19 10:26:34)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	private void setDj_common(nc.vo.ep.dj.DJZBVO newDjzbvo) {
		if (newDjzbvo == null)
			return;
		
		CmpBBHLUFDoubleConverter converter=new CmpBBHLUFDoubleConverter();
		if (getBillCardPanelDj().getBodyItem("bbhl")!=null) {
			getBillCardPanelDj().getBodyItem("bbhl").setConverter(converter);
		}
		// refactoring:get the templet PK from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();
		dataBuffer.setCurrentDJZBVO(newDjzbvo);
		// 以下增加单据表体行唯一标志
		nc.vo.ep.dj.DJZBItemVO[] djzbitemvos = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();
		int i = 0;
		Map<String,String[] >cumap =new HashMap<String,String[] >();
		Map<String,String[] >psnmap =new HashMap<String,String[] >();
		ICustBankQry cuqry=NCLocator.getInstance().lookup(ICustBankQry.class);
		IPsnDocQueryService psnqry=NCLocator.getInstance().lookup(IPsnDocQueryService.class);
		for (i = 0; i < djzbitemvos.length; i++) {
			djzbitemvos[i].setDjzbitemsIndex(new Integer(i));
			djzbitemvos[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
			djzbitemvos[i].m_oldjfbbje = djzbitemvos[i].getJfbbje();
			djzbitemvos[i].setOldybye(djzbitemvos[i].getYbye());
			djzbitemvos[i].setOldfbye(djzbitemvos[i].getFbye());
			djzbitemvos[i].setOldbbye(djzbitemvos[i].getBbye());
			// 以下增加自由项表体行唯一标志
			nc.vo.ep.dj.DJFBItemVO[] items = (nc.vo.ep.dj.DJFBItemVO[]) djzbitemvos[i].getChildrenVO();
			if (items == null || items.length < 1)
				continue;
			for (int j = 0; j < items.length; j++) {
				items[j].setFbitemsIndex(new Integer(j));
				items[j].setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			try {
				if(null!=djzbitemvos[i].getHbbm()&&!cumap.containsKey(djzbitemvos[i].getHbbm())){
						cumap.put(djzbitemvos[i].getHbbm(), cuqry.queryPKsByCuBasPK(djzbitemvos[i].getHbbm()));
				}else if(null!=djzbitemvos[i].getYwybm()&&!psnmap.containsKey(djzbitemvos[i].getYwybm())){
	    			PsnBasManUnionVO basman=psnqry.queryByPsnManDocPK(djzbitemvos[i].getYwybm());
	    			psnmap.put(djzbitemvos[i].getYwybm(),psnqry.queryBankaccbasPksByPsnbasdocPk(basman.getPsnbasvo().getPk_psnbasdoc()));
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
			}
		}
		m_DjzbitemsIndex = (i - 1) >= 0 ? i - 1 : 0;
		// /////////////
		// 初始化单据模板
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dataBuffer.getCurrentDJZBVO().getParentVO();
		if(null==head.getDjlxbm())
			head.setDjlxbm((String)this.getBillCardPanelDj().getHeadItem("djlxbm").getValueObject());
		// m_Vouchid = head.getVouchid();
		String billtypecode = head.getDjlxbm();
		
		if (billtypecode == null || billtypecode.trim().length() < 1) {
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000807")/*
																														 * @res
																														 * "单据类型主键为空"
																														 */);
			return;
		}
		DjLXVO temp_billtypevo = null;
		try {
			temp_billtypevo = FiPubDataCache.getBillType(head.getDjlxbm(),head.getDwbm());
			if (temp_billtypevo == null)
				throw new Exception();
		} catch (Exception ex) {
			Logger.debug("djlxoid=" + billtypecode);
			nc.ui.pub.beans.MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPP2006030102-000375")/* @res "错误" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPP2006030102-000808")/* @res "没有找到相应的单据类型\n可能你没有分配单据类型" */);
			return;
		}
		Integer iSysCode = ArapConstant.INT_ZERO;
		if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm) {
			// 签字确认节点
			iSysCode = new Integer(14);
		} else {
			iSysCode = head.getLybz();
		}

		if (getBillCardPanelDj() instanceof ArapDjBillCardPanel) {
			loadDjTemplet(
					iSysCode,
					head.getDwbm(), temp_billtypevo.getDjdl(), temp_billtypevo.getDjlxbm());
			getBillCardPanelDj().resetTreater(temp_billtypevo.getDjdl());
		}

//		if (head.getIsselectedpay() != null && head.getIsselectedpay().intValue() > 0) {
//			getBillCardPanelDj().getHeadItem("bzbm").setEnabled(false);
//			getBillCardPanelDj().getBodyItem("bzmc").setEnabled(false);
//		} else {
			getBillCardPanelDj().getHeadItem("bzbm").setEnabled(true);
			getBillCardPanelDj().getBodyItem("bzmc").setEnabled(true);
//		}

		getBillCardPanelDj().setShowThMark(true);

		dataBuffer.setCurrentBillTypeInfo(temp_billtypevo);
		// setM_Defcurrency(temp_djlxvo.getDefcurrency()); //设置单据类型默认币种
		dataBuffer.setDefcurrency(temp_billtypevo.getDefcurrency());

		// 刷新单据填充数据
		try {

			head = (nc.vo.ep.dj.DJZBHeaderVO) dataBuffer.getCurrentDJZBVO().getParentVO();
		} catch (Throwable res) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000811")/*
																											 * @res
																											 * "联查单据后重新设置单据VO
																											 * 出错:"
																											 */
					+ res);
		}

		DJZBVO clonedDJZBVO = (DJZBVO) dataBuffer.getCurrentDJZBVO().clone();
		try {

			AdjustVoAfterQuery.getInstance().aftQueryResetDjVO(clonedDJZBVO);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);

		}
		DJZBHeaderVO clonedHead = (DJZBHeaderVO) clonedDJZBVO.getParentVO();
		try {

			String pk_currtype = clonedHead.getBzbm();
			String date = clonedHead.getDjrq().toString();
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBzbm_H(pk_currtype, date, false);

		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000368")/*
																											 * @res
																											 * "设置汇率是否可编辑及汇率小数位数出错:"
																											 */
					+ e);
		}

		try {
			String pk_currtype = clonedHead.getBzbm();
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeDigByCurr(pk_currtype);
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().resetSSDjbh();
			changePk_corp(null, clonedDJZBVO);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
		getBillCardPanelDj().getBodyValueAt(0, "othersysflag");
		getBillCardPanelDj().getBodyItem("othersysflag");

		try {
			getBillCardPanelDj().setHeadItem("hbbm", clonedHead.getHbbm());
			getBillCardPanelDj().setHeadItem("deptid", clonedHead.getDeptid());
			getBillCardPanelDj().setHeadItem("ywybm", clonedHead.getYwybm());
			getBillCardPanelDj().setHeadItem("tradertype", clonedHead.getTradertype());
			ivjBillCardPanelDj.getM_cardTreater().resetSFkyhzh(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage());
		}

		getBillCardPanelDj().setBillValueVO(clonedDJZBVO);
		getBillCardPanelDj().setHeadItem("dwbm", clonedHead.getDwbm());
		BillModel model = getBillCardPanelDj().getBodyPanel().getTableModel();
		if (model.getRowCount() > 0) {
			for (int ii = 0, count = model.getRowCount(); ii < count; ii++){
				BillEditEvent e=new BillEditEvent("","","djrq",ii,1);
//				ivjBillCardPanelDj.getM_cardTreater().changeYHZHEdit(e);
//				ivjBillCardPanelDj.getM_cardTreater().resetYHZH(e,cumap,psnmap);
//				this.getBillCardPanelDj().execBodyFormulas(ii, new String[]{"bankacc_fk->getColValue(bd_bankaccbas,account,pk_bankaccbas,dfyhzh);fkyhmc->getColValue(bd_bankaccbas,accountname,pk_bankaccbas,dfyhzh)"});
				model.setRowState(ii, BillModel.MODIFICATION);
//				try {
//					ivjBillCardPanelDj.getM_cardTreater().rCResetBody(ii,true,false);
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//				}
			}
		}
		// 加载公式
		try {
			getBillCardPanelDj().getBillTable().getSelectionModel().setSelectionInterval(0, 0);// 选择第一行
		} catch (Throwable e) {
			nc.bs.logging.Log.getInstance(this.getClass()).warn("加载公式出错: " + e);
		}
		try {
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().execHeadFormulas_Accountid(
					getBillCardPanelDj());
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().execHeadFormulas_hbbm(getBillCardPanelDj());
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().execHeadFormulas_Account();
			getBillCardPanelDj().execHeadLoadFormulas();
			getBillCardPanelDj().getBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000813")/*
																											 * @res
																											 * "加载公式出错: "
																											 */
					+ e);
		}
		getBillCardPanelDj().getBillTable().getSelectionModel().setSelectionInterval(0, 0);
		// 选择第一行

		try {
			selectedSzxm(0);
			long t = System.currentTimeMillis();
			resetDjByCurrency((DJZBItemVO[])clonedDJZBVO.getChildrenVO());
			nc.bs.logging.Log.getInstance(this.getClass()).debug("设置币种小数位所用时间:  " + (System.currentTimeMillis() - t));
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
		if (temp_billtypevo != null)
			setTitleText(temp_billtypevo.getDjlxmc());

	}

	/**
	 * 功能:多单位录入数据时，当用户改变单位时，动态改变所有参照的单位 算法: 创建日期：(2001-6-26 10:05:52)
	 * 
	 * @author：陈飞
	 * 
	 * 
	 * 
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void changePk_corp(nc.ui.pub.bill.BillEditEvent e, DJZBVO VO) throws Exception {

		DJZBHeaderVO headvo = (DJZBHeaderVO) VO.getParentVO();
		String pk_corp = getDjSettingParam().getPk_corp();
		if (pk_corp == null)
			return;
		if (headvo.getDwbm() != null) {
			if (pk_corp != null && pk_corp.equalsIgnoreCase(headvo.getDwbm()))
				return;

			pk_corp = headvo.getDwbm();
		} else {

			if (e.getKey().equalsIgnoreCase("dwbm") && e.getPos() == 0) {

				if (pk_corp == null || pk_corp.trim().length() < 1)
					return;

			} else
				return;
		}

		BillItem[] headitems = getBillCardPanelDj().getHeadShowItems();
		for (int i = 0; i < headitems.length; i++) {
			if (headitems[i].getDataType() == 5) {
				nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) headitems[i].getComponent();

				ref.getRefModel().setPk_corp(pk_corp);
				// ref.setPk_corp(pk_corp);
				if (ref.getRefModel() instanceof IArapRefModel)
					((IArapRefModel) ref.getRefModel()).refreshWherePart(null, null);

			}
		}
		BillItem[] bodyitems = getBillCardPanelDj().getBodyShowItems();
		for (int i = 0; i < bodyitems.length; i++) {
			if (bodyitems[i].getDataType() == 5) {
				nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) bodyitems[i].getComponent();
				if (null != ref.getRefModel())
					ref.getRefModel().setPk_corp(pk_corp);
			}
		}

		nc.bs.logging.Log.getInstance(this.getClass()).warn("end");
	}

	/**
	 * 功能:设置当前要显示的单据 简要流程: 创建日期：(2001-6-19 10:26:34)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。 // *
	 * by setDj(DJZBVO, boolean).
	 */
	public void setDj(nc.vo.ep.dj.DJZBVO newDjzbvo) {
		this.setDj_common(newDjzbvo);
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815") 
				+ getM_DjState());
		getBillCardPanelDj().setEnabled(false);
	}

	/**
	 * 功能:设置当前要显示的单据 简要流程: 创建日期：(2001-6-19 10:26:34)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void setDj_pay(nc.vo.ep.dj.DJZBVO newDjzbvo) {
		this.setDj_common(newDjzbvo);
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815")/*
																										 * @res
																										 * "刷新单据时重设置单据状态:"
																										 */
				+ getM_DjState());
		m_DjState = 1;
		getBillCardPanelDj().setEnabled(true);
		nc.ui.pub.bill.BillEditEvent e = new nc.ui.pub.bill.BillEditEvent(getBillCardPanelDj().getBodyItem("bzbm"),
				null, "bzbm", 0, 0);
		try {
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBzbm(e);
		} catch (Exception eee) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(eee.getMessage(), eee);
		}
		for (int rowIndex = 0; rowIndex < getBillCardPanelDj().getRowCount(); rowIndex++) {
			nc.ui.pub.bill.BillEditEvent e1 = new nc.ui.pub.bill.BillEditEvent(
					getBillCardPanelDj().getBodyItem("bzmc"), null, "bzmc", rowIndex, 1);
			try {
				((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBItemBzbm(e1, true);
			} catch (Exception eee) {
				nc.bs.logging.Log.getInstance(this.getClass()).error(eee.getMessage(), eee);
			}
			try {
				nc.ui.pub.bill.BillEditEvent e2 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanelDj().getBodyItem(
						"dfybje"), null, "dfybje", rowIndex, 1);
				((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBodyJshj(e2);

			} catch (Exception eee) {
				nc.bs.logging.Log.getInstance(this.getClass()).error(eee.getMessage(), eee);
			}
		}

	}

	/**
	 * 功能:设置当前要显示的单据 简要流程: 创建日期：(2001-6-19 10:26:34)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void setDj_TB(nc.vo.ep.dj.DJZBVO newDjzbvo) {
		nc.bs.logging.Log.getInstance(this.getClass()).warn("in setDj_TB ver1");
		/** *codes below added by wangqiang,2004-08-04,for haire*** */
		BillItem item = getBillCardPanelDj().getBodyItem("serial_number");
		// if the "serial_number" item exist,then show this item.
		if (item != null) {
			getBillCardPanelDj().showBodyTableCol("serial_number");
		}
		this.setDj_common(newDjzbvo);

		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815")/*
																										 * @res
																										 * "刷新单据时重设置单据状态:"
																										 */
				+ getM_DjState());
		m_DjState = 1;
		getBillCardPanelDj().setEnabled(true);

		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) getDjDataBuffer().getCurrentDJZBVO().getParentVO();

		nc.ui.pub.bill.BillEditEvent e2 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanelDj().getHeadItem("bzbm"),
				head.getBzbm(), "bzbm", 0, 0);
		try {
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBzbm(e2);
		} catch (Exception eee) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(eee.getMessage(), eee);
		}

		item = getBillCardPanelDj().getBodyItem("serial_number");
		if (item != null) {
			getBillCardPanelDj().showBodyTableCol("serial_number");
		}

	}

	/**
	 * 功能:设置当前要显示的单据 简要流程: 创建日期：(2001-6-19 10:26:34)
	 * 
	 * @author：陈飞
	 * @exception java.lang.Throwable
	 *                异常说明。
	 */
	public void setDj_TBYT(nc.vo.ep.dj.DJZBVO newDjzbvo) {

		this.setDj_common(newDjzbvo);

		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815")/*
																										 * @res
																										 * "刷新单据时重设置单据状态:"
																										 */
				+ getM_DjState());
		// setM_DjState(1);
		m_DjState = 1;
		getBillCardPanelDj().setEnabled(true);

		// in fact,the newDjzbvo is setted as Current VO in setDj
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) getDjDataBuffer().getCurrentDJZBVO().getParentVO();

		nc.ui.pub.bill.BillEditEvent e2 = new nc.ui.pub.bill.BillEditEvent(getBillCardPanelDj().getHeadItem("bzbm"),
				head.getBzbm(), "bzbm", 0, 0);
		try {
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBzbm(e2);
			getBillCardPanelDj().getHeadItem("bbhl").setValue(head.getBbhl());
		} catch (Exception eee) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(eee.getMessage(), eee);
		}
		for (int rowIndex = 0; rowIndex < getBillCardPanelDj().getRowCount(); rowIndex++) {
			nc.ui.pub.bill.BillEditEvent e1 = new nc.ui.pub.bill.BillEditEvent(
					getBillCardPanelDj().getBodyItem("bzmc"), null, "bzmc", rowIndex, 1);
			try {
				((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().changeBItemBzbm(e1, true);
			} catch (Exception eee) {
				nc.bs.logging.Log.getInstance(this.getClass()).error(eee.getMessage(), eee);
			}
		}

	}

	/**
	 * author:wangqiang create time:2004-09-28 after refactoring start
	 * function:set the bill data buffer
	 */
	public void setDjDataBuffer(nc.ui.ep.dj.ARAPDjDataBuffer newDataBuffer) {
		m_djDataBuffer = newDataBuffer;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-24 16:50:58)
	 * 
	 * @author：陈飞 功能: 算法： 备注：
	 * @param newM_Djlxoid
	 *            java.lang.String
	 */
	public void setDjlxVO(DjLXVO vo) {
		
		Integer iSyscode = ArapConstant.INT_ZERO;
		
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();

		dataBuffer.setCurrentBillTypeInfo(vo);

		loadDjTemplet(iSyscode,
				getDjSettingParam().getPk_corp(), vo.getDjdl(), vo.getDjlxbm()); // 加载单据模板

		try {
			if (!(m_Parent == null)) {
				setM_Title(vo.getDjlxmc());
				m_Parent.setTitleText(vo.getDjlxmc());

			}
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}

	}

	public void loadDjTemplet(Integer iSyscode, String strCorpPK, String strDjdl, String strDjlxbm) {
		DjLXVO vo=FiPubDataCache.getBillType(strDjlxbm, strCorpPK);
		if (vo==null) {
			vo=FiPubDataCache.getBillType(strDjlxbm, null);
		}
		if(m_Parent instanceof DjPanel){
			strDjlxbm=UFBoolean.TRUE.equals(((DjPanel)m_Parent).getIsAloneNode())?null:strDjlxbm;
		}
		if(this.getBillCardPanelDj()!=null&&this.getBillCardPanelDj().getBillModel()!=null){
			String curdjlxbm=getCurdjlxbm();
			if((curdjlxbm==null&&strDjlxbm==null)||
					(curdjlxbm!=null&&curdjlxbm.equals(strDjlxbm)))
					return ;
		}
		
		this.ivjBillCardPanelDj.loadTempletArapDj(iSyscode, strCorpPK, strDjdl, strDjlxbm);
		
		DJZBHeaderVO head=new DJZBHeaderVO ();
		head.setDjdl(strDjdl);
		
		head.setQcbz(this.getDjSettingParam().getIsQc()?UFBoolean.TRUE:UFBoolean.FALSE);
		if(m_Parent instanceof DjPanel&&!((DjPanel)this.m_Parent).isFROMCMP() &&!UFBoolean.FALSE.equals(vo.getIssettleshow())
			&&DjVOTreaterAid.hasSettleInfo(head)){
			try {				
				if(getUITabbedPane1().getTabCount()<3){					
					getUITabbedPane1().insertTab(getSettlePanel().getTabTitle(), null, (ToftPanel)getSettlePanel(), getSettlePanel().getTabTitle(), 2);
				}
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}else{
			if(getUITabbedPane1().getTabCount()>2)
				getUITabbedPane1().remove(2);
		}
		
		
		// resetVOTreater(strDjdl);
	}

	/**
	 * author:wangqiang create time:2004-10-27 comment:this method is copied
	 * from setDjlxVO during debugging
	 */
	public void setTabTitle() {
		// refactoring:get the data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();
		String strDjdl = dataBuffer.getCurrentDjdl();
		try {
			if (!(m_Parent == null)) {
				if ("yt".equals(strDjdl)) {
					String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001038")/*
																													 * @res "
																													 * 预提单 "
																													 */;
					setM_Title(title);
					m_Parent.setTitleText(title);
				} else {
					setM_Title(dataBuffer.getCurrentDjlxmc());
					m_Parent.setTitleText(dataBuffer.getCurrentDjlxmc());
				}
			}
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
	}

	/**
	 * author:wangqiang create time:2004-09-28 after refactoring start
	 * function:set the settingParam
	 */
	public void setDjSettingParam(nc.ui.ep.dj.ARAPDjSettingParam newSettingParam) {
		m_djSettingParam = newSettingParam;
	}

	/**
	 * a功能: 作者：宋涛 创建时间：(2004-10-15 16:42:28) 使用说明：以及别人可能感兴趣的介绍 注意：现存Bug
	 * 
	 * 
	 * @param newDjzbitemsIndex
	 *            int
	 */
	public void setDjzbitemsIndex(int newDjzbitemsIndex) {
		m_DjzbitemsIndex = newDjzbitemsIndex;
	}


	/**
	 * 功能: 简要流程: 创建日期：(2001-6-19 11:14:45)
	 * 
	 * @author：陈飞
	 * @param newM_DjState
	 *            int
	 */
	public void setM_DjState(int newM_DjState) {
		m_DjState = newM_DjState;
		fireDjStateChange(this);

		// //正在删除单据,增加单据,修改单据 页签不可用

		if (m_DjState == ArapBillWorkPageConst.WORKSTAT_NEW  || m_DjState == ArapBillWorkPageConst.WORKSTAT_EDIT || m_DjState==ArapBillWorkPageConst.WORKSTAT_ADJUST) // 增加 或修改,补差
		{
			getBillCardPanelDj().setEnabled(true);
			getUITabbedPane1().setEnabledAt(1, false);

			setBuChaZT(true);

		} else if (m_DjState == ARAPDjSettingParam.is_BuCha) {// 补差单据金额不可编辑
			getBillCardPanelDj().setEnabled(true);
			getUITabbedPane1().setEnabledAt(1, false);

			setBuChaZT(false);

		} else if(m_DjState != 4){
			getBillCardPanelDj().setEnabled(false);
		}
		
		if(m_DjState == ArapBillWorkPageConst.WORKSTAT_NEW  || m_DjState == ArapBillWorkPageConst.WORKSTAT_EDIT){
			getBillCardPanelDj().changePjhInEdit();
		}else if(m_DjState == ArapBillWorkPageConst.WORKSTAT_BROWSE){
			try {
				getBillCardPanelDj().changePjhInQry();
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}

	private Map<String, UFBoolean> _map = new HashMap<String, UFBoolean>();

	private String[] headitem = new String[] { "bzbm", "bzmc", "bbhl", "fbhl" };

	private String[] bodyitem = new String[] { "bzmc","jfybje", "jffbje", "jfbbje",
			"dffbje", "dfbbje", "bbhl", "fbhl", "kslb", "dj", "hsdj",
			"sl", "dfshl", "dfybwsje", "dfybsj", "dfybje", "jfshl", "jfybwsje",
			"jfybsj"};
	
	private void setBuChaZT(boolean status) {
		if (status) {
			if (_map.size() == 0)
				return;
			for(String key :headitem){
				if(null != getBillCardPanelDj().getHeadItem(key) && null !=  _map.get("h_"+key)){
					getBillCardPanelDj().getHeadItem(key).setEnabled(_map.get("h_"+key).booleanValue());
				}
			}
			for(String key : bodyitem){
				if(null != getBillCardPanelDj().getBodyItem(key) && null != _map.get(key)){
					getBillCardPanelDj().getBodyItem(key).setEnabled(_map.get(key).booleanValue());
				}
			}
			_map.clear();
		} else {
			for(String key :headitem){
				if(null != getBillCardPanelDj().getHeadItem(key)){
					_map.put("h_"+key, new UFBoolean(getBillCardPanelDj().getHeadItem(key).isEnabled()));
					getBillCardPanelDj().getHeadItem(key).setEnabled(status);
				}
			}			
			for(String key :bodyitem){
				if(null != getBillCardPanelDj().getBodyItem(key)){
					_map.put(key, new UFBoolean(getBillCardPanelDj().getBodyItem(key).isEnabled()));
					getBillCardPanelDj().getBodyItem(key).setEnabled(status);
				}
			}
		}
	}

	/**
	 * δ? 作者：陈飞 创建时间：(2001-7-9 16:45:36) 参数： <|>返回值： 算法：
	 * 
	 * @param newM_Parent
	 *            nc.ui.pub.ToftPanel
	 */
	public void setM_Parent(nc.ui.pub.ToftPanel newM_Parent) {
		m_Parent = newM_Parent;
	}

	/**
	 * δ? 作者：陈飞 创建时间：(2001-8-22 17:08:48) 参数： <|>返回值： 算法：
	 * 
	 * @param newM_TabIndex
	 *            int
	 */
	public void setM_TabIndex(int newM_TabIndex) {
		m_TabIndex = newM_TabIndex;
		fireTabIndexChange(this);
	}

	/**
	 * 此处插入方法描述。 创建日期：(2004-3-10 16:23:35)
	 * 
	 * @author：chenf
	 * @param newM_Title
	 *            java.lang.String
	 */
	public void setM_Title(java.lang.String newM_Title) {
		m_Title = newM_Title;
	}

	/**
	 * δ埽篭n此处插入方法说明。 创建日期：(2001-8-28 15:38:11)
	 * 
	 * @author：陈飞 算法： 备注：
	 * @param newM_Zyxmboid
	 *            java.lang.String
	 */
	public void setM_Zyxmboid(java.lang.String newM_Zyxmboid) {
		m_Zyxmboid = newM_Zyxmboid;
		getBillCardPanelZyx().loadTemplet(getM_Zyxmboid());
	}

	/**
	 * 功能: 简要流程: 创建日期：(2001-6-19 11:14:45)
	 * 
	 * @author：陈飞
	 * @param newM_ZyxState
	 *            int
	 */
	public void setM_ZyxState(int newM_ZyxState) {
		m_ZyxState = newM_ZyxState;
		fireZyxStateChange(this);
		if (m_ZyxState == 1 || m_ZyxState == 3)// 增加 或修改
		{
			getBillCardPanelZyx().setEnabled(true);

		} else {
			getBillCardPanelZyx().setEnabled(false);
		}
	}

	/**
	 * 功能:根据单据大类不同重置title 作者：陈飞 创建时间：(2001-7-9 15:57:40) 参数： <|>返回值： 算法：
	 */
	public void setTitleText(String djlxmc) {
		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();
		// get current djdl
		String strDjdl = dataBuffer.getCurrentDjdl();
		if (m_Parent == null)
			return;
		if (strDjdl == null)
			return;
		if (djlxmc != null && djlxmc.trim().length() > 0) {
			m_Parent.setTitleText(djlxmc);
			return;
		}
		if (strDjdl.equals("ys")) {

			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000678")/*
																													 * @res
																													 * "应收单"
																													 */);
		} else if (strDjdl.equals("yf")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000679")/*
																													 * @res
																													 * "应付单"
																													 */);
		} else if (strDjdl.equals("sk")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000680")/*
																													 * @res
																													 * "收款单"
																													 */);
		} else if (strDjdl.equals("fk")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000681")/*
																													 * @res
																													 * "付款单"
																													 */);
		} else if (strDjdl.equals("sj")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000682")/*
																													 * @res
																													 * "收款结算单"
																													 */);
		} else if (strDjdl.equals("fj")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000685")/*
																													 * @res
																													 * "付款结算单"
																													 */);
		} else if (strDjdl.equals("hj")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000686")/*
																													 * @res
																													 * "划账结算单"
																													 */);
		} else if (strDjdl.equals("ws")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000683")/*
																													 * @res
																													 * "对外收款结算单"
																													 */);
		} else if (strDjdl.equals("wf")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000684")/*
																													 * @res
																													 * "对外付款结算单"
																													 */);
		} else {
			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_WSZZ)
				m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000816")/*
																														 * @res
																														 * "网上转账"
																														 */);
			else
				m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000413")/*
																														 * @res
																														 * "单据处理"
																														 */);
		}

		if (getDjSettingParam().getSyscode() == 3) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000817")/*
																													 * @res
																													 * "事项审批"
																													 */);
		}

	}

	/**
	 * @return 返回 m_treater。
	 */
	public DjVOTreater getM_treater(DJZBVO djzbvo) {
		String strDjdl = ((DJZBHeaderVO) djzbvo.getParentVO()).getDjdl();
		Log.getInstance(this.getClass()).debug(
				"******set pzglh*****" + ((DJZBHeaderVO) djzbvo.getParentVO()).getPzglh());

		if ("ys".equals(strDjdl) || "yf".equals(strDjdl) || "sk".equals(strDjdl) || "fk".equals(strDjdl)) {
			if (!(m_treater instanceof DjVoTreaterYs))
				m_treater = new DjVoTreaterYs(djzbvo, ((AbstractRuntime)this.m_Parent).getProxy());
		} else if ("sj".equals(strDjdl) || "fj".equals(strDjdl) || "hj".equals(strDjdl) || "wf".equals(strDjdl)
				|| "ws".equals(strDjdl)) {
			if (!(m_treater instanceof DjVOTreaterHj))
				m_treater = new DjVOTreaterHj(djzbvo, ((AbstractRuntime)this.m_Parent).getProxy());
		} else if ("ss".equals(strDjdl)) {
			if (!(m_treater instanceof DjVOTreaterSs))
				m_treater = new DjVOTreaterSs(djzbvo, ((AbstractRuntime)this.m_Parent).getProxy());
		} else if ("yt".equals(strDjdl)) {
			if (!(m_treater instanceof DjVOTreaterYt))
				m_treater = new DjVOTreaterYt(djzbvo, ((AbstractRuntime)this.m_Parent).getProxy());
		}
		if (m_treater == null)
			m_treater = new DjVoTreaterYs(djzbvo, ((AbstractRuntime)this.m_Parent).getProxy());
		return m_treater;
	}

	public boolean isTabChanged() {
		return tabChanged;
	}

	public void setTabChanged(boolean tabChanged) {
		this.tabChanged = tabChanged;
	}

	public void refresh()
	{

		this.getBillCardPanelDj().setBillValueVO(new DJZBVO());
		this.getBillCardPanelDj().removeBillEditListenerHeadTail();
		this.getBillCardPanelDj().removeEditListener();

	}

	public boolean isSelectSettleTab() {
		return SelectSettleTab;
	}

	public void setSelectSettleTab(boolean selectSettleTab) {
		SelectSettleTab = selectSettleTab;
	}

	public ISettlementPanel getSettlePanel() {
		if(settlePanel==null){
			settlePanel=SettlementPanelAgent.getSettlementPanel();
		}
		return settlePanel;
	}

	public void setSettlePanel(ISettlementPanel settlePanel) {
		this.settlePanel = settlePanel;
	}
}