package nc.ui.arap.pubdj;

/**
 * �˴���������˵��������ģ�����鵥���� �������ڣ�(2001-6-19 9:45:58) setm_parent(tofpanel) ��Ȼ
 *
 * @author���·� ��ע:�����ʱ���¼��������������� setM_parent(toftpanel); setM_djoid(vouchid)
 *              ���鵥�ݱ������� setM_DjState(0); changeTab2(0);//��ʾ���ݽ���
 *              setM_pk_corp(pk_corp);//��λ setM_kjnd(kjnd);//������
 *              setM_kjqj(kjqj);//����ڼ� setM_Lrr(lrr);//¼����
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
 
	// ��ʱ����״̬����ʼ 0 ,��ʱ���� 3 ,���� 4
	public int m_iVitStat = ArapBillWorkPageConst.VITINIT;

	public ArapDjBillCardPanel ivjBillCardPanelDj = null;

	private DjZYXBillCardPanel ivjBillCardPanelZyx = null;

	private nc.ui.pub.beans.UITabbedPane ivjUITabbedPane1 = null;
	
	private ISettlementPanel settlePanel=null;

	protected javax.swing.event.EventListenerList DjStateChangeListenerList = new javax.swing.event.EventListenerList();
	//maji add 
//	private DJZBVO tempdjzbvo=null;

	public int m_DjState = 0;

	// ������״̬,0 ����,1ɾ��,2�޸�
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
	//��������ʱ�洢������Ϣ�����ںͽ����л��á� NND ��DefaultCurrTypeDecimalAdapter���У������ֱ��Ĺ���һ�Σ�����TMD���ң�
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
	 * ArapDjPanel ������ע�⡣
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
	 * ArapDjPanel ������ע�⡣
	 * 
	 * @param p0
	 *            java.awt.LayoutManager
	 */
	public ArapDjPanel(java.awt.LayoutManager p0) {
		super(p0);
	}

	/**
	 * ArapDjPanel ������ע�⡣
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
	 * ArapDjPanel ������ע�⡣
	 * 
	 * @param p0
	 *            boolean
	 */
	public ArapDjPanel(boolean p0) {
		super(p0);
	}

	/**
	 * ����: ���ߣ��·� ����ʱ�䣺(2001-8-22 14:53:30) ������ <|>����ֵ�� �㷨��
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
	 * ����:�ı�ҳǩ(���ݺ�������֮��)
	 * 
	 * ���ı䵽������ʱ: A.��̬�ı��������ģ��: ��̬�ı䵥��ģ�� 1.���ڵ���ģ�����ʱ����BillTempletVO(�ı�ʱʹ��) ????
	 * 2.�ı�BillTempletVO���� 3.����new BillData(BillTempletVO)
	 * 4.����BillCardPanel.setBillData(BillData)
	 * B.�������������:1.�����ݸ���������vo������Ӧ��������ʱ��ʾ��ǰ����,2���򵱵��ݴ��ڷ�����״̬ʱ���������� B
	 * �ı�Ϊ===>>>>>>>> ���ɣ��κ��������ѯ������,������������״̬Ϊ��ʼ״̬
	 * C.����������buttons:ֻ��ʾ�����������,�޸�,ɾ��,����,ɾ��,����,ȡ�� D.�����������ͷ:���ݱ�����Ӧ�е�ĳЩ������Ϊ�������ͷ
	 * 
	 * ���ı䵽����ʱ: ���õ��ݵ�buttons:ֻ��ʾ������Ӧ��button
	 * 
	 * �������ڣ�(2001-5-21 15:07:39) ����:���� ����:tabIndex ҳǩ��
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
			if (tabIndex == 1)// �л���������
			{
				if (getJPanel1().getComponentCount() < 1) {
					getJPanel1().add(getBillCardPanelZyx(), "Center");
				}
			}
			// cf test end
			if (m_Parent != null)
				// m_Parent.showHintMessage("����ʼ�����ݣ����Ժ�...");
				m_Parent.showHintMessage(" ");
			// refactoring:useless member var
			setM_TabIndex(tabIndex);
			// m_TabIndex=tabIndex;
	
			// 0Ϊ����,1Ϊ������
	
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
																													 * "�������������:"
																													 */
							+ e);
					this.m_Parent.showErrorMessage(e.getMessage());
				}
	
				// ���ϴ���B
	
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
		Logger.error("��ʼ���ý���ISettlementPanel�ӿ�");
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
	 * ����:���û��ı���Ŀ����ʱ��̬������Ŀ�׶β���(���Ӧ��,��Ӧ����) �㷨: �������ڣ�(2001-6-26 10:05:52)
	 * 
	 * @author���·�
	 * 
	 * 
	 * 
	 * @exception java.lang.Throwable
	 *                �쳣˵����
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
																											 * "������Ŀ�׶β���"
																											 */);
			try {
			
				nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanelDj().getBodyItem("xmjdmc")
						.getComponent();
				// String
				// pk_jobmngfil=getBillCardPanelDj().getBodyValueAt(e.getRow(),"pk_jobmngfil").toString();//��Ŀ����������
				String pk_jobmngfil = getBillCardPanelDj().getBodyValueAt(e.getRow(), "jobid").toString();
				// ��Ŀ����������
				nc.ui.bd.b39.PhaseRefModel model = (nc.ui.bd.b39.PhaseRefModel) ref.getRefModel();
				String dwbm=(String)this.getBillCardPanelDj().getHeadItem("dwbm").getValueObject();
				JobmngfilVO vo=(JobmngfilVO) NCLocator.getInstance().lookup(IUAPQueryBS.class).retrieveByClause(JobmngfilVO.class, " pk_jobbasfil='"+pk_jobmngfil+"' and pk_corp='"+dwbm+"'");

				model.setJobID(vo.getPk_jobmngfil());

				getBillCardPanelDj().setBodyValueAt(null, e.getRow(), "xmjdmc");// ��Ŀ�׶�����
				getBillCardPanelDj().setBodyValueAt(null, e.getRow(), "pk_jobobjpha");
				getBillCardPanelDj().setBodyValueAt(null, e.getRow(), "jobphaseid");

			} catch (Throwable ee) {
				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000759")/*
																												 * @res
																												 * "������Ŀ�׶β���,�������Ҳ���������:"
																												 */
						+ ee);
			}

		}

	}

	/**
	 * ����:���û��ı���֧��Ŀʱɾ����Ӧ����Ҫ��ͬʱ��ѯ��֧��Ŀ���� ��Ҫ����: �������ڣ�(2001-6-7 9:40:44)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵����
	 */
	public void changeSzxm(int SelectedRow) throws java.lang.Exception {

		// refactoring
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

		nc.vo.ep.dj.DJZBItemVO[] djzbitems = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();

		int j = getdjzbitemIndex(SelectedRow); // ���ݵ���ѡ����к�ȡcurdjzbvo�ж�Ӧ�����
		if (j < 0)
			return;
		// m_DjzbIndex = j; //����curdjzbvo��ǰλ��
		djzbitems[j].setChildrenVO(null); // ɾ������������
		djzbitems[j].freepropertys = null; // ɾ���������

		if (getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid") == null
				|| getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString().trim().length() < 1)
		// �����ǰ��������ı����Ӧ��֧��ĿΪ��,�����������
		{
			getUITabbedPane1().setEnabledAt(1, false);
			return;
		}

		getUITabbedPane1().setEnabledAt(1, true);

		// ���������Ӧ ��֧��Ŀ���������
		Vector<?> freeDY = new Vector(); // ��֧��Ŀ�������
		try {

			freeDY = ((AbstractRuntime)this.m_Parent).getProxy().getIArapBillPrivate().getFreePropertys(
					this.getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString());
		} catch (Exception e) {
			getUITabbedPane1().setEnabledAt(1, false);

			return;
		}

		if (freeDY == null || freeDY.size() < 1)
		// ��֧��Ŀû�ж��幫��Ҫ��,���������
		{
			getUITabbedPane1().setEnabledAt(1, false);
			return;
		}
		Logger.debug("getFreePropertys.size=" + freeDY.size());

		// ���½�����Ҫ�ض��屣����ȫ�ֱ�����
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
	/* ���棺�˷������������ɡ� */
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
	 * ����:�޸� �������ڣ�(2001-5-21 17:00:09)
	 * 
	 * @return boolean ����:����
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
					"UPP2006030102-000375")/* @res "����" */, res.strMessage);
			return false;
		}
		getBillCardPanelDj().setEnabled(true); // �����޸�״̬
		getBillCardPanelDj().getHeadItem("djbh").getComponent().setEnabled(false);
		// ���ݺŲ����޸�

		try {
			((ArapDjBillCardPanel) getBillCardPanelDj()).getM_cardTreater().rCResetBody(0, true, false);
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}

		return true;
	}

	/**
	 * �Ƿ����ҵ��Աδ�رյ����������� �˴����뷽�������� �������ڣ�(2004-2-19 14:31:47)
	 * 
	 * @return boolean
	 * @param pk_corp
	 *            java.lang.String
	 * @param ywybm
	 *            java.lang.String pk_corp��˾��ywybmҵ��Ա��billcode��������������
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
																														 * "ǰ��δ�岻�ܷ����������"
																														 */);
			b = true;
		}
		return b;
	}

	/**
	 * ����:ɾ�����ݳɹ�ʱ������Ϣ ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ����: ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ����:�޸ĵ��ݱ���ɹ�������Ϣ ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ����:�������ݱ���ɹ�������Ϣ ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ����: ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ����: ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ����: ���ߣ��·� ����ʱ�䣺(2001-8-22 14:58:04) ������ <|>����ֵ�� �㷨��
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
	 * ���� BillCardPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� BillCardPanel2 ����ֵ��
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� djlxRef ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
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
 * @param fcbzflag true --������棻false--���������
 * �õ��������͹�����������
 * update by Songtao 20080721
 * ��getdjlxRef���������з�������ⲿ�֣��Ա㱻��ѯģ��ʹ�á�
 * */
	public String getDjlxWhere(boolean fcbzflag) {
		//TODO �߼���Ҫ���ݲ�ѯʹ����һЩ�������ڲ�ѯ�����е���Ҫ������浥������
		
		StringBuilder sqlWhere = new StringBuilder();
		// �ڳ����ֻ����,Ӧ�յ�,Ӧ����,�տ,��� ��4�ֵ�������
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
		} else if (getDjSettingParam().getSyscode() == ResMessage.$SysCode_YuTi)// Ԥ�ᵥ
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
				 * ���ݲ�ͬϵͳ���õ�������ѡ��Χ Ӧ��ϵͳ ֻ��ѡ��Ӧ�ա��տӦ���������������Ϊ�ͻ����� Ӧ��ϵͳ
				 * ֻ��ѡ��Ӧ�������Ӧ�ա��տ��������Ϊ��Ӧ�̣��� ��������
				 * ��ѡ��Ӧ�������Ӧ�ա��տ�����տ���㵥�����⸶����㵥����������Ϊ���š�ҵ��Ա����
				 */
				// m_Syscode = 0; //oӦ��,1Ӧ��,2��������
				
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
	 * ����:���ݵ���ѡ����к�ȡcurdjzbvo�ж�Ӧ����� ��Ҫ����: �������ڣ�(2001-6-6 15:07:06)
	 * 
	 * @author���·�
	 * @return int -1 ʧ��
	 * @param selectedRow
	 *            int
	 */
	public int getdjzbitemIndex(int SelectedRow) throws Exception {
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000772")/*
																										 * @res
																										 * "ѡ�е���:"
																										 */
				+ SelectedRow);
		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();

		nc.vo.ep.dj.DJZBItemVO[] djzbitems = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000773")/*
																										 * @res
																										 * "ȫ������:"
																										 */
				+ djzbitems.length);

		if (SelectedRow < 0 || SelectedRow >= djzbitems.length)
			return -1;

		int selectedDjzbitemsIndex = (new Integer(getBillCardPanelDj().getBodyValueAt(SelectedRow, "djzbitemsIndex")
				.toString())).intValue();
		// DJZBItemVO[]�е�Ψһ��ʶ
		int j = 0;
		for (j = 0; j < djzbitems.length; j++) {
			if (djzbitems[j].getDjzbitemsIndex() != null
					&& djzbitems[j].getDjzbitemsIndex().intValue() == selectedDjzbitemsIndex)
				break;
		}
		if (j == djzbitems.length)
		// δƥ��
		{
			// Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102","UPP2006030102-000774")/*@res
			// "δƥ��(m_djzbitemsIndex)"*/);
			return -1;
		}

		return j;
	}

	/**
	 * ����:��λ �Զ������
	 * 
	 * 
	 * ��Ҫ����:
	 * 
	 * 
	 * �������ڣ�(2001-5-21 15:07:39) ����:���� ����:FreeSxh ���������,1:�Զ�����1,�Դ����� return:-1ʧ��
	 */

	/**
	 * ���� JPanel1 ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	// getJPanel1().add(getBillCardPanelZyx(), "Center");
	/* ���棺�˷������������ɡ� */
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
	 * ����: ��Ҫ����: �������ڣ�(2001-6-19 11:14:45)
	 * 
	 * @author���·�
	 * @return int
	 */
	public int getM_DjState() {
		return m_DjState;
	}

	/**
	 * ����:����ToftPanel ���ߣ��·� ����ʱ�䣺(2001-7-9 16:45:36) ������ <|>����ֵ�� �㷨��
	 * 
	 * @return nc.ui.pub.ToftPanel
	 */
	public nc.ui.pub.ToftPanel getM_Parent() {
		return m_Parent;
	}

	/**
	 * ��? ���ߣ��·� ����ʱ�䣺(2001-8-22 17:08:48) ������ <|>����ֵ�� �㷨��
	 * 
	 * @return int
	 */
	public int getM_TabIndex() {
		return m_TabIndex;
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-3-10 16:23:35)
	 * 
	 * @author��chenf
	 * @return java.lang.String
	 */
	public java.lang.String getM_Title() {
		return m_Title;
	}

	/**
	 * ��ܣ�\n�˴����뷽��˵���� �������ڣ�(2001-8-28 15:38:11)
	 * 
	 * @author���·� �㷨�� ��ע��
	 * @return java.lang.String
	 */
	public java.lang.String getM_Zyxmboid() {
		return m_Zyxmboid;
	}

	/**
	 * ����: ��Ҫ����: �������ڣ�(2001-6-19 11:14:45)
	 * 
	 * @author���·�
	 * @return int
	 */
	public int getM_ZyxState() {
		return m_ZyxState;
	}

	/**
	 * ���� UITabbedPane1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITabbedPane
	 */
	/* ���棺�˷������������ɡ� */
	public nc.ui.pub.beans.UITabbedPane getUITabbedPane1() {
		if (ivjUITabbedPane1 == null) {
			try {
				ivjUITabbedPane1 = new nc.ui.pub.beans.UITabbedPane();
				ivjUITabbedPane1.setName("UITabbedPane1");
				ivjUITabbedPane1.insertTab(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
						"UPT2006030102-000021")/* @res "����" */, null, getBillCardPanelDj(), null, 0);
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
	 * ÿ�������׳��쳣ʱ������
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
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ����: ���ߣ��·� ����ʱ�䣺(2001-8-22 14:53:30) ������ <|>����ֵ�� �㷨��
	 * 
	 * @param param
	 *            nc.ui.arap.pubdj.DjStateChangeListener
	 */
	public void removeDjStateChangeListener(DjStateChangeListener listener) {
		DjStateChangeListenerList.remove(DjStateChangeListener.class, listener);
	}

	/**
	 * �˴����뷽�������� ���ݵ���ÿһ�����б��ֵĲ�ͬ�������õ���ÿһ�еı���С��λ�� �������ڣ�(2003-3-20 9:26:33)
	 */
	private void resetDjByCurrency(DJZBItemVO[] items) throws Exception {
		int rowCount = getBillCardPanelDj().getRowCount();
		String pkcorp = getDjSettingParam().getPk_corp();
		String date = (String)getBillCardPanelDj().getHeadItem("djrq").getValueObject(); // ��������
		if (getDjSettingParam().getIsQc())
			// date=getQyrq2().toString(); //��������
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
	 * �˴����뷽��˵���� �������ڣ�(2003-9-8 15:01:26)
	 */
	public nc.vo.ep.dj.DJZBVO getBcDjvo() {
		getBillCardPanelDj().stopEditing();
		try {
			getBillCardPanelDj().dataNotNullValidate();
		} catch (nc.vo.pub.ValidationException e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			m_Parent.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000765")/*
																														 * @res
																														 * "���ݱ�����δ¼����: "
																														 */
					+ e.getMessage());
			return null;
		}

		int itemCount = 0;

		// ȡ��������
		itemCount = getBillCardPanelDj().getBillModel().getRowCount();
		if (itemCount <= 0) {
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000766")/*
																														 * @res
																														 * "���岻��Ϊ��,����ʧ��"
																														 */);
			return null;
		}

		nc.vo.ep.dj.DJZBItemVO[] itemsvo = new nc.vo.ep.dj.DJZBItemVO[itemCount];
		// ����vo
		nc.vo.ep.dj.DJZBHeaderVO headvo = new nc.vo.ep.dj.DJZBHeaderVO();
		// qdq add for pf
		headvo.setShr(headvo.getLrr());
		headvo.setShrq(headvo.getDjrq());
		//
		// ��ͷvo
		nc.vo.ep.dj.DJZBVO djzbvo = new nc.vo.ep.dj.DJZBVO();
		// ����vo

		// ȡ����vo
		djzbvo = (nc.vo.ep.dj.DJZBVO) getBillCardPanelDj().getBillValueChangeVO("nc.vo.ep.dj.DJZBVO",
				"nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");

		// ����׼����ͷ����

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
																														 * "����ʧ��"
																														 */);
			return null;
		}
		// headvo.setZyx20(null);

		djzbvo.setParentVO(headvo);

		// ����׼����ͷ����

		// ����׼����������

		try {
			itemsvo = new DjVOChanger((AbstractRuntime)this.m_Parent, this).befSaveChangeBVo(djzbvo);
		} catch (Throwable e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000178")/*
																														 * @res
																														 * "����ʧ��"
																														 */);
			return null;
		}
		djzbvo.setChildrenVO(itemsvo);
		// ����׼����������
		return djzbvo;

	}

	/**
	 * ����:���û��ڱ����л���ʱ(���������Ӱ��) ��Ҫ����: �����ǰ��������ı����Ӧ��֧��ĿΪ��,����������� ����(���������)
	 * //�����Ӧ��֧��Ŀ��������������,��return //���� �����Ӧ��֧��Ŀ���������������,��return
	 * ����,�����Ӧ��֧��Ŀ��������� �����Ӧ��֧��Ŀ��������û�ж���,�����������
	 * 
	 * �������ڣ�(2001-6-5 12:17:32)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵����
	 */
	public void selectedSzxm(int SelectedRow) throws Exception {
		//

		if (getM_DjState() == 1 || getM_DjState() == 3) {
			// ���������ӻ��޸�ʱ���������
			getUITabbedPane1().setEnabledAt(1, false);
			return;
		}
		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

		nc.vo.ep.dj.DJZBItemVO[] djzbitems = (nc.vo.ep.dj.DJZBItemVO[]) dataBuffer.getCurrentDJZBVO().getChildrenVO();

		int j = getdjzbitemIndex(SelectedRow); // ���ݵ���ѡ����к�ȡcurdjzbvo�ж�Ӧ�����
		if (j < 0)
			return;
		// m_DjzbIndex = j; //����curdjzbvo��ǰλ��

		// if( djzbitems[j].getSzxmid()==null ||
		// djzbitems[j].getSzxmid().trim().length()<1)
		if (getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid") == null
				|| getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString().trim().length() < 1)
		// �����ǰ��������ı����Ӧ��֧��ĿΪ��,�����������
		{
			getUITabbedPane1().setEnabledAt(1, false);
		} else {
			getUITabbedPane1().setEnabledAt(1, true);

			if (djzbitems[j].freepropertys == null || djzbitems[j].freepropertys.length < 1)
			// �����Ӧ ��֧��Ŀ���������
			{
				Vector freeDY = new Vector(); // ��֧��Ŀ�������

			
				try {

					freeDY = ((AbstractRuntime)this.m_Parent).getProxy().getIArapBillPrivate().getFreePropertys(
							getBillCardPanelDj().getBodyValueAt(SelectedRow, "szxmid").toString());
				} catch (Exception e) {
					
					return;
				}
				Logger.debug("getFreePropertys.size=" + freeDY.size());
				if (freeDY == null || freeDY.size() < 1)
				// ��֧��Ŀû�ж��幫��Ҫ��,���������
				{
					getUITabbedPane1().setEnabledAt(1, false);
					return;
				}
				// ���½�����Ҫ�ض��屣����ȫ�ֱ�����
				djzbitems[j].freepropertys = new nc.vo.ep.dj.DefdefVO[freeDY.size()];
				for (int i = 0; i < freeDY.size(); i++) {
					djzbitems[j].freepropertys[i] = (nc.vo.ep.dj.DefdefVO) freeDY.elementAt(i);
				}
			} else
			// ��Ӧ ��֧��Ŀ���������������
			{
				return;
			}


			return;

			// }

		}

	}

	/**
	 * ����:���õ�ǰҪ��ʾ�ĵ��� ��Ҫ����: �������ڣ�(2001-6-19 10:26:34)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵����
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
		// �������ӵ��ݱ�����Ψһ��־
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
			// �������������������Ψһ��־
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
		// ��ʼ������ģ��
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dataBuffer.getCurrentDJZBVO().getParentVO();
		if(null==head.getDjlxbm())
			head.setDjlxbm((String)this.getBillCardPanelDj().getHeadItem("djlxbm").getValueObject());
		// m_Vouchid = head.getVouchid();
		String billtypecode = head.getDjlxbm();
		
		if (billtypecode == null || billtypecode.trim().length() < 1) {
			m_Parent.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000807")/*
																														 * @res
																														 * "������������Ϊ��"
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
					"UPP2006030102-000375")/* @res "����" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
					"UPP2006030102-000808")/* @res "û���ҵ���Ӧ�ĵ�������\n������û�з��䵥������" */);
			return;
		}
		Integer iSysCode = ArapConstant.INT_ZERO;
		if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_EC_SignatureConfirm) {
			// ǩ��ȷ�Ͻڵ�
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
		// setM_Defcurrency(temp_djlxvo.getDefcurrency()); //���õ�������Ĭ�ϱ���
		dataBuffer.setDefcurrency(temp_billtypevo.getDefcurrency());

		// ˢ�µ����������
		try {

			head = (nc.vo.ep.dj.DJZBHeaderVO) dataBuffer.getCurrentDJZBVO().getParentVO();
		} catch (Throwable res) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000811")/*
																											 * @res
																											 * "���鵥�ݺ��������õ���VO
																											 * ����:"
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
																											 * "���û����Ƿ�ɱ༭������С��λ������:"
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
		// ���ع�ʽ
		try {
			getBillCardPanelDj().getBillTable().getSelectionModel().setSelectionInterval(0, 0);// ѡ���һ��
		} catch (Throwable e) {
			nc.bs.logging.Log.getInstance(this.getClass()).warn("���ع�ʽ����: " + e);
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
																											 * "���ع�ʽ����: "
																											 */
					+ e);
		}
		getBillCardPanelDj().getBillTable().getSelectionModel().setSelectionInterval(0, 0);
		// ѡ���һ��

		try {
			selectedSzxm(0);
			long t = System.currentTimeMillis();
			resetDjByCurrency((DJZBItemVO[])clonedDJZBVO.getChildrenVO());
			nc.bs.logging.Log.getInstance(this.getClass()).debug("���ñ���С��λ����ʱ��:  " + (System.currentTimeMillis() - t));
		} catch (Exception e) {
			nc.bs.logging.Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
		if (temp_billtypevo != null)
			setTitleText(temp_billtypevo.getDjlxmc());

	}

	/**
	 * ����:�൥λ¼������ʱ�����û��ı䵥λʱ����̬�ı����в��յĵ�λ �㷨: �������ڣ�(2001-6-26 10:05:52)
	 * 
	 * @author���·�
	 * 
	 * 
	 * 
	 * @exception java.lang.Throwable
	 *                �쳣˵����
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
	 * ����:���õ�ǰҪ��ʾ�ĵ��� ��Ҫ����: �������ڣ�(2001-6-19 10:26:34)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵���� // *
	 * by setDj(DJZBVO, boolean).
	 */
	public void setDj(nc.vo.ep.dj.DJZBVO newDjzbvo) {
		this.setDj_common(newDjzbvo);
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815") 
				+ getM_DjState());
		getBillCardPanelDj().setEnabled(false);
	}

	/**
	 * ����:���õ�ǰҪ��ʾ�ĵ��� ��Ҫ����: �������ڣ�(2001-6-19 10:26:34)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵����
	 */
	public void setDj_pay(nc.vo.ep.dj.DJZBVO newDjzbvo) {
		this.setDj_common(newDjzbvo);
		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815")/*
																										 * @res
																										 * "ˢ�µ���ʱ�����õ���״̬:"
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
	 * ����:���õ�ǰҪ��ʾ�ĵ��� ��Ҫ����: �������ڣ�(2001-6-19 10:26:34)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵����
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
																										 * "ˢ�µ���ʱ�����õ���״̬:"
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
	 * ����:���õ�ǰҪ��ʾ�ĵ��� ��Ҫ����: �������ڣ�(2001-6-19 10:26:34)
	 * 
	 * @author���·�
	 * @exception java.lang.Throwable
	 *                �쳣˵����
	 */
	public void setDj_TBYT(nc.vo.ep.dj.DJZBVO newDjzbvo) {

		this.setDj_common(newDjzbvo);

		Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000815")/*
																										 * @res
																										 * "ˢ�µ���ʱ�����õ���״̬:"
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-24 16:50:58)
	 * 
	 * @author���·� ����: �㷨�� ��ע��
	 * @param newM_Djlxoid
	 *            java.lang.String
	 */
	public void setDjlxVO(DjLXVO vo) {
		
		Integer iSyscode = ArapConstant.INT_ZERO;
		
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = this.getDjDataBuffer();

		dataBuffer.setCurrentBillTypeInfo(vo);

		loadDjTemplet(iSyscode,
				getDjSettingParam().getPk_corp(), vo.getDjdl(), vo.getDjlxbm()); // ���ص���ģ��

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
																													 * Ԥ�ᵥ "
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
	 * a����: ���ߣ����� ����ʱ�䣺(2004-10-15 16:42:28) ʹ��˵�����Լ����˿��ܸ���Ȥ�Ľ��� ע�⣺�ִ�Bug
	 * 
	 * 
	 * @param newDjzbitemsIndex
	 *            int
	 */
	public void setDjzbitemsIndex(int newDjzbitemsIndex) {
		m_DjzbitemsIndex = newDjzbitemsIndex;
	}


	/**
	 * ����: ��Ҫ����: �������ڣ�(2001-6-19 11:14:45)
	 * 
	 * @author���·�
	 * @param newM_DjState
	 *            int
	 */
	public void setM_DjState(int newM_DjState) {
		m_DjState = newM_DjState;
		fireDjStateChange(this);

		// //����ɾ������,���ӵ���,�޸ĵ��� ҳǩ������

		if (m_DjState == ArapBillWorkPageConst.WORKSTAT_NEW  || m_DjState == ArapBillWorkPageConst.WORKSTAT_EDIT || m_DjState==ArapBillWorkPageConst.WORKSTAT_ADJUST) // ���� ���޸�,����
		{
			getBillCardPanelDj().setEnabled(true);
			getUITabbedPane1().setEnabledAt(1, false);

			setBuChaZT(true);

		} else if (m_DjState == ARAPDjSettingParam.is_BuCha) {// ����ݽ��ɱ༭
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
	 * ��? ���ߣ��·� ����ʱ�䣺(2001-7-9 16:45:36) ������ <|>����ֵ�� �㷨��
	 * 
	 * @param newM_Parent
	 *            nc.ui.pub.ToftPanel
	 */
	public void setM_Parent(nc.ui.pub.ToftPanel newM_Parent) {
		m_Parent = newM_Parent;
	}

	/**
	 * ��? ���ߣ��·� ����ʱ�䣺(2001-8-22 17:08:48) ������ <|>����ֵ�� �㷨��
	 * 
	 * @param newM_TabIndex
	 *            int
	 */
	public void setM_TabIndex(int newM_TabIndex) {
		m_TabIndex = newM_TabIndex;
		fireTabIndexChange(this);
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-3-10 16:23:35)
	 * 
	 * @author��chenf
	 * @param newM_Title
	 *            java.lang.String
	 */
	public void setM_Title(java.lang.String newM_Title) {
		m_Title = newM_Title;
	}

	/**
	 * ��ܣ�\n�˴����뷽��˵���� �������ڣ�(2001-8-28 15:38:11)
	 * 
	 * @author���·� �㷨�� ��ע��
	 * @param newM_Zyxmboid
	 *            java.lang.String
	 */
	public void setM_Zyxmboid(java.lang.String newM_Zyxmboid) {
		m_Zyxmboid = newM_Zyxmboid;
		getBillCardPanelZyx().loadTemplet(getM_Zyxmboid());
	}

	/**
	 * ����: ��Ҫ����: �������ڣ�(2001-6-19 11:14:45)
	 * 
	 * @author���·�
	 * @param newM_ZyxState
	 *            int
	 */
	public void setM_ZyxState(int newM_ZyxState) {
		m_ZyxState = newM_ZyxState;
		fireZyxStateChange(this);
		if (m_ZyxState == 1 || m_ZyxState == 3)// ���� ���޸�
		{
			getBillCardPanelZyx().setEnabled(true);

		} else {
			getBillCardPanelZyx().setEnabled(false);
		}
	}

	/**
	 * ����:���ݵ��ݴ��಻ͬ����title ���ߣ��·� ����ʱ�䣺(2001-7-9 15:57:40) ������ <|>����ֵ�� �㷨��
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
																													 * "Ӧ�յ�"
																													 */);
		} else if (strDjdl.equals("yf")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000679")/*
																													 * @res
																													 * "Ӧ����"
																													 */);
		} else if (strDjdl.equals("sk")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000680")/*
																													 * @res
																													 * "�տ"
																													 */);
		} else if (strDjdl.equals("fk")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000681")/*
																													 * @res
																													 * "���"
																													 */);
		} else if (strDjdl.equals("sj")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000682")/*
																													 * @res
																													 * "�տ���㵥"
																													 */);
		} else if (strDjdl.equals("fj")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000685")/*
																													 * @res
																													 * "������㵥"
																													 */);
		} else if (strDjdl.equals("hj")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000686")/*
																													 * @res
																													 * "���˽��㵥"
																													 */);
		} else if (strDjdl.equals("ws")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000683")/*
																													 * @res
																													 * "�����տ���㵥"
																													 */);
		} else if (strDjdl.equals("wf")) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000684")/*
																													 * @res
																													 * "���⸶����㵥"
																													 */);
		} else {
			if (getDjSettingParam().getSyscode() == ArapBillWorkPageConst.SysCode_WSZZ)
				m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000816")/*
																														 * @res
																														 * "����ת��"
																														 */);
			else
				m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000413")/*
																														 * @res
																														 * "���ݴ���"
																														 */);
		}

		if (getDjSettingParam().getSyscode() == 3) {
			m_Parent.setTitleText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000817")/*
																													 * @res
																													 * "��������"
																													 */);
		}

	}

	/**
	 * @return ���� m_treater��
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