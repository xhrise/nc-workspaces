package nc.ui.ep.dj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.cmp.pub.cache.FiPubDataCache;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.arap.actions.AddAction;
import nc.ui.arap.actions.CopyDjAction;
import nc.ui.arap.actions.SearchAction;
import nc.ui.arap.actions.SearchActionNor;
import nc.ui.arap.buttonstat.DjCopyBtnStatLisener;
import nc.ui.arap.buttonstat.StatListenerHelper;
import nc.ui.arap.global.DjTempletHelper;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.businessaction.IBusinessController;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapDjCalculator;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.global.IRuntimeConstans;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.ep.dj.ArapBillMapVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

@SuppressWarnings({ "restriction", "serial" , "rawtypes"})
public abstract class FiFlowPanel extends ArapBaseEntry{

//	public ButtonObject m_boBusiType = new ButtonObject(  "业务类型" ,   "业务类型" , 5,"业务类型");
	DjCopyBtnStatLisener cpyListener=null;
	CopyDjAction cpy=new CopyDjAction();
	ButtonObject[] btns;
	
	// 设置余额调整单的业务相关信息
	// add by river for 2012-07-25
	// start ..
	private nc.ui.ehpta.hq010403.ClientUI clientUI = null;
	private nc.ui.ehpta.hq010403.EventHandler handler = null;
	private IBusinessController businessAction = null;

	// .. end
	
	public FiFlowPanel(){
		super();
		cpyListener=new DjCopyBtnStatLisener();
		cpyListener.setActionRunntimeV0(this);
		cpy.setActionRunntimeV0(this);
	}
	public FiFlowPanel(int syscode){
		super(syscode);
		cpyListener=new DjCopyBtnStatLisener();
		cpyListener.setActionRunntimeV0(this);
		cpy.setActionRunntimeV0(this);
	}
	public ButtonObject[] getDjButtons() {
		ButtonObject[] ret=null;
		if(this.getLastWorkPage()==this.getCurrWorkPage()&&btns!=null){
			return btns;
		}
		else{
			if(this.getArapDjPanel1().isSelectSettleTab()){
				btns=this.getArapDjPanel1().getSettlePanel().getButtons();
			}else{
				try {
				ret=getExtBtnProxy().getButtons()==null?new ButtonObject[]{}:getExtBtnProxy().getButtons();
				if(ArapBillWorkPageConst.CARDPAGE==this.getCurrWorkPage()){
					ButtonObject[] localArray=null;
					if(getPanelProp()== 0){
						localArray= new ButtonObject[]{m_boAdd,m_boCpy};
					}
					btns=new ButtonObject[(ret==null?0:ret.length)+(localArray==null?0:localArray.length)];
					if(null!=localArray&&localArray.length>0)
						 System.arraycopy(localArray, 0,btns , 0, localArray.length);
					if(null!=ret&&ret.length>0)
						 System.arraycopy(ret,0,btns ,  localArray==null?0:localArray.length, ret.length);
				}else{
					btns=ret;
				}
//				 this.updateButtons();
			} catch (Exception e) {

				Log.getInstance(this.getClass()).error(e);
				this.showErrorMessage(e.getMessage());

				this.showHintMessage(e.getMessage());
			}
			}
		}
		return btns;
	}

	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		
		try {
			beforeOnButtonClicked(bo);
		} catch (BusinessException e2) {
			showErrorMessage(e2.getMessage());
			return ;
		}
		
		this.beginPressBtn(bo);
		this.showProgressBar(true);
		String butitype=null;
		if(bo.getParent()==m_boAdd){
			if(!checkID())
				return ;
			try {
				butitype=selectBusitype(bo);
			} catch (BusinessException e1) {
//				ExceptionHandler.consume(e1);
				this.showProgressBar(false);
				return ;
			}
			PfUtilClient.childButtonClicked(bo,this.getCurrentCorp(),this.getNodeCode(),this.getDjSettingParam().getPk_user(),
					getDjDataBuffer().getCurrentDjlxbm(),this);
		    if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
		      //自制单据处理
		    	AddAction add=new AddAction();
		    	add.setActionRunntimeV0(this);
		    	try {
					add.add(bo.getTag().substring(1+bo.getTag().indexOf(":")));//流程名PK
				} catch (Exception e) {
					this.showErrorMessage(e.getMessage());
					this.showHintMessage(e.getMessage());
				}
		    }else if (PfUtilClient.isCloseOK()){
		      //单据来源参照返回单据聚合VO或数组
		      AggregatedValueObject[] vos = PfUtilClient.getRetVos();

		      for(AggregatedValueObject vo:vos){
			    	DJZBVO djvo = (DJZBVO) vo;
					DJZBItemVO[] items = djvo.items;
					DJZBHeaderVO head = djvo.header;
						for(DJZBItemVO item:items){
							//重新计算单据的各项金额
							try {
								ArapDjCalculator.getInstance().changeBodyByYbje(item,djvo.header);
								item=ArapDjCalculator.getInstance().calculateVO(item, "bzbm",getDjSettingParam().getLoginDate().toString(), head.getDjdl(), ArapDjCalculator.getInstance().getProior(head));

							} catch (NullPointerException e) {
								ExceptionHandler.consume(e);
							}catch (Exception e) {
								ExceptionHandler.consume(e);
								this.showErrorMessage(e.getMessage());
							}
						}
					DjVOTreaterAid.sumBtoH(djvo);
			 }


		      //重新设置billmap的信息
		      resetBillMap(vos);

//		      DJZBHeaderVO[] heads=new DJZBHeaderVO[vos.length];
		      try {
		    	  showbills(vos,butitype);
//			      san.changeTab();
//				 san.updateListPanel(heads);
			} catch (Exception e) {
				this.showErrorMessage(e.getMessage());
			}
		    }
		}else if(bo.getParent()==m_boCpy){
			if(!checkID())
				return ;
			try {
				butitype=bo.getTag().substring(bo.getTag().indexOf(":")+1);
//				butitype=selectBusitype(bo);
				cpy.copyDj(butitype);
			} catch (Exception e1) {
//				ExceptionHandler.consume(e1);
				this.showErrorMessage(e1.getMessage());
				this.showProgressBar(false);
				return ;
			}
		}else{
			super.onButtonClicked(bo);
		}
//		if(bo instanceof nc.ui.arap.engine.ExtButtonObject&&("单据类型".equalsIgnoreCase((( nc.ui.arap.engine.ExtButtonObject)bo).getBtninfo().getBtncode())	||"列表".equalsIgnoreCase((( nc.ui.arap.engine.ExtButtonObject)bo).getBtninfo().getBtncode()))){
//			try {
//				updateLocalButtons();
//			} catch (Exception e) {
//				showErrorMessage(e.getMessage());
//			}
//
//		}

		if(isSuccess()){
			this.endPressBtn(bo);
		}
		cpyAndAddBoStatListener();
		this.showProgressBar(false);
		this.updateButtonStatus();
		
		afterOnButtonClicked(bo, isSuccess());

	}
	
	/**
	 *  功能　：重写beforeOnButtonClicked 
	 *  
	 *  Authur : river
	 *  
	 *  Create Date : 2012-07-25
	 *  
	 */
	@Override
	protected void beforeOnButtonClicked(ButtonObject bo)
			throws BusinessException {
		
		if("反审核".equals(bo.getName())) {
			beforeOnBoCancleAudit();
		} else
			super.beforeOnButtonClicked(bo);
		
	}
	
	private final void beforeOnBoCancleAudit() throws BusinessException {
		Vector<String> djpks = getAllSelectedDJPK();
		if(djpks != null && djpks.size() == 0)
			if(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid") != null)
				djpks.add(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid").getValueObject().toString());
		
		if(djpks != null && djpks.size() > 0) {
			try {
				Object userObj = new nc.ui.ehpta.hq010403.ClientUICheckRuleGetter();
				
				String whereSql = "";
				int i = 0 ;
				for(String djpk : djpks) {
					if(i == djpks.size() - 1)
						whereSql += "'" + djpk + "'";
					else 
						whereSql += "'" + djpk + "',";
					
					i++;
				}
				
				Vector retVector = null;
				if(!"".equals(whereSql)) {
					try {
						retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery("select djzt , spzt , zyx6 , zyx7 , vouchid , shr , shrq , ybje from arap_djzb where vouchid in ("+whereSql+") and dwbm = '"+getCorpPrimaryKey()+"' and zyx6 is not null and nvl(dr,0)=0 ", new VectorProcessor());
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
				
				
				if(retVector != null && retVector.size() > 0) {
					for(Object vct : retVector) {
						
						Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(count(1),0) from so_sale where pk_contract = '"+((Vector)vct).get(2)+"' and nvl(dr,0)=0", new ColumnProcessor());
						if(count > 0) {
							Logger.error("收款已被使用，不能进行弃审操作！");
							throw new BusinessException("收款已被使用，不能进行弃审操作！" );
						}
					}
					
				}
			} catch(Exception e) {
				throw new BusinessException(e);
			}
		} else
			return ;
	}
	
	/**
	 *  功能　：重写afterOnButtonClicked 
	 *  
	 *  Authur : river
	 *  
	 *  Create Date : 2012-07-25
	 *  
	 */
	@Override
	protected void afterOnButtonClicked(ButtonObject bo, boolean success) {
		
		if(success) {
			if("审核".equals(bo.getName())) {
				afterOnBoAudit();
			} else if("反审核".equals(bo.getName())) {
				afterOnBoCancleAudit();
			}
		}
	}
	
	/**
	 *  功能 ： 审核通过时推式生成余额调整单记录
	 *  
	 *  Authur : river 
	 *  
	 *  Create Date : 2012-07-25
	 *  
	 */
	private final void afterOnBoAudit() {
		
		Object userObj = new nc.ui.ehpta.hq010403.ClientUICheckRuleGetter();
		Vector<String> djpks = getAllSelectedDJPK();
		if(djpks != null && djpks.size() == 0)
			if(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid") != null)
				djpks.add(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid").getValueObject().toString());
		
		String whereSql = "";
		if(djpks != null && djpks.size() > 0) {
			int i = 0 ;
			for(String djpk : djpks) {
				if(i == djpks.size() - 1)
					whereSql += "'" + djpk + "'";
				else 
					whereSql += "'" + djpk + "',";
				
				i++;
			}
		} else 
			return ;
		
		Vector retVector = null;
		if(!"".equals(whereSql)) {
			try {
				retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery("select djzt , spzt , zyx6 , zyx7 , vouchid , shr , shrq , ybje from arap_djzb where vouchid in ("+whereSql+") and dwbm = '"+getCorpPrimaryKey()+"' and zyx6 is not null and nvl(dr,0) = 0 ", new VectorProcessor());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		
		List<HYBillVO> adjustList = new ArrayList<HYBillVO>();
		
		if(retVector != null && retVector.size() > 0) {
			for(Object vct : retVector) {
				try {
					Object djzt = ((Vector)vct).get(0);
					Object spzt = ((Vector)vct).get(1);
					Object pk_contract = ((Vector)vct).get(2);
					Object iscredit = ((Vector)vct).get(3);
					Object vouchid = ((Vector)vct).get(4);
					Object shr = ((Vector)vct).get(5);
					Object shrq = ((Vector)vct).get(6);
					Object ybje = ((Vector)vct).get(7);
					
					if(spzt != null || !"".equals(spzt)) {
						switch(Integer.valueOf(spzt.toString())) {
						
							case 1 :
								// 审核通过
								if(Integer.valueOf(djzt.toString()) == 3 ) {
									Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select count(1) from ehpta_adjust where def1 = '"+vouchid+"' and nvl(dr,0)=0 ", new ColumnProcessor());
									if(count == 0) {
										
										AdjustVO adjust = new AdjustVO();
										adjust.setAttributeValue("type", "1");
										adjust.setAttributeValue("reason", "收款录入");
										adjust.setAttributeValue("mny", new UFDouble(ybje.toString()));
										adjust.setAttributeValue("pk_contract", pk_contract);
										adjust.setAttributeValue("pk_cubasdoc", UAPQueryBS.iUAPQueryBS.executeQuery("select pk_cumandoc from bd_cumandoc where pk_cubasdoc in (select distinct hbbm from arap_djfb where vouchid = '"+vouchid+"')", new ColumnProcessor()));
										adjust.setAttributeValue("memo", "收款录入推式生成");
										adjust.setAttributeValue("vbillno", GeneraterBillNO.getInstanse().build("HQ07", getCorpPrimaryKey()));
										adjust.setAttributeValue("adjustdate", new UFDate(shrq.toString()));
										adjust.setAttributeValue("managerid", shr);
										adjust.setAttributeValue("vbillstatus", 8);
										adjust.setAttributeValue("pk_corp", getCorpPrimaryKey());
										adjust.setAttributeValue("pk_billtype", "HQ07");
										adjust.setAttributeValue("voperatorid", shr);
										adjust.setAttributeValue("dmakedate", new UFDate(shrq.toString()));
										adjust.setAttributeValue("def1", vouchid);
										adjust.setAttributeValue("def2", "Y");
										adjust.setAttributeValue("def3", iscredit);
										
										HYBillVO billVO = new HYBillVO();
										billVO.setParentVO(adjust);
										adjustList.add(billVO);
										
									}
								}
								
								
								break;
								
							default : 
								
								break;
						}
					}
				} catch (Exception e) {
					Logger.error(e);
					continue;
				}
			
			}
		
		}
		
		try {
			if (adjustList != null && adjustList.size() > 0) {

				AggregatedValueObject[] adjustAggVOs = HYPubBO_Client.saveBDs( adjustList.toArray(new HYBillVO[0]), userObj);
				
				
				
				for(AggregatedValueObject billVO : adjustAggVOs) {
					// 审核前不需要提交
					// try { businessAction.commit(billVO, "HQ07", billVO.getParentVO().getAttributeValue("dmakedate").toString() ,  userObj ); } catch(Exception e) { Logger.error(e); }
					
					try { 
						SuperVO adjust = HYPubBO_Client.queryByPrimaryKey(AdjustVO.class, billVO.getParentVO().getPrimaryKey());
						HYBillVO newBillVO = new HYBillVO();
						newBillVO.setParentVO(adjust);
						
						getBusiController().approve(newBillVO, "HQ07", billVO.getParentVO().getAttributeValue("dmakedate").toString() , userObj ); 
					
					} catch(Exception e) { 
						Logger.error(e); 
					}
					
				}
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}
	
	/**
	 *  功能 ： 弃审时推式生成余额调整单记录
	 *  
	 *  Authur : river 
	 *  
	 *  Create Date : 2012-07-25
	 *  
	 */
	private final void afterOnBoCancleAudit() {
		
		Vector<String> djpks = getAllSelectedDJPK();
		if(djpks != null && djpks.size() == 0)
			if(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid") != null)
				djpks.add(getArapDjPanel1().getBillCardPanelDj().getBillData().getHeadItem("vouchid").getValueObject().toString());
		
		if(djpks != null && djpks.size() > 0) {
			try {
				Object userObj = new nc.ui.ehpta.hq010403.ClientUICheckRuleGetter();
				
				String whereSql = "";
				int i = 0 ;
				for(String djpk : djpks) {
					if(i == djpks.size() - 1)
						whereSql += "'" + djpk + "'";
					else 
						whereSql += "'" + djpk + "',";
					
					i++;
				}
				
				Vector retVector = null;
				if(!"".equals(whereSql)) {
					try {
						retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery("select djzt , spzt , zyx6 , zyx7 , vouchid , shr , shrq , ybje from arap_djzb where vouchid in ("+whereSql+") and dwbm = '"+getCorpPrimaryKey()+"' and zyx6 is not null and nvl(dr,0)=0 ", new VectorProcessor());
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
				
				SuperVO[] superVos = HYPubBO_Client.queryByCondition(AdjustVO.class, " def1 in ("+whereSql+") and nvl(dr,0) = 0 " );
				List<HYBillVO> billVOs = new ArrayList<HYBillVO>();
				for(SuperVO superVO : superVos) {
					HYBillVO billVO = new HYBillVO();
					billVO.setParentVO(superVO);
					billVOs.add(billVO);
				}
				
				AggregatedValueObject[] adjustAggVOs = billVOs.toArray(new HYBillVO[0]);
				
				if(adjustAggVOs != null && adjustAggVOs.length > 0) {
					for(AggregatedValueObject billVO : adjustAggVOs) {
						
						Object vouchid = billVO.getParentVO().getAttributeValue("def1");
						if(vouchid != null) {
							for(Object vct : retVector) {
								Object arap_vouchid = ((Vector)vct).get(4);
								if(arap_vouchid.equals(vouchid)) {
									if(((Vector)vct).get(1) != null) {
										Logger.info("单据未弃审至制单状态，不能弃审余额调整单中的记录");
										Logger.error("单据未弃审至制单状态，不能弃审余额调整单中的记录");
										
										showErrorMessage("余额调整单 - " + billVO.getParentVO().getAttributeValue("vbillno") + " : 单据未弃审至制单状态，不能弃审余额调整单中的记录" );
										
										continue;
									}
								}
							}
						}
						
						try { getBusiController().unapprove(billVO, "HQ07", billVO.getParentVO().getAttributeValue("dapprovedate").toString() , userObj); } catch(Exception e) { Logger.error(e); }
						try { 
							SuperVO adjust = HYPubBO_Client.queryByPrimaryKey(AdjustVO.class, billVO.getParentVO().getPrimaryKey());
							HYBillVO newBillVO = new HYBillVO();
							newBillVO.setParentVO(adjust);
							
							getBusiController().delete(newBillVO, "HQ07", billVO.getParentVO().getAttributeValue("dapprovedate").toString() , userObj); 
						} catch(Exception e) { 
							
							Logger.error(e); 
						}
					
					}
					
				}
			} catch(Exception e) {
				Logger.error(e);
			}
		} else
			return ;
		
	}
	
	/** 
	 *  
	 *  功能 :　获取到余额调整单中的业务接口，以便后续的审核、弃审等操作使用
	 *  
	 *  Authur : river 
	 *  
	 *  Create Date : 2012-07-25
	 *  
	 * @return businessAction
	 */
	private final IBusinessController getBusiController() {
		
		if(clientUI == null) 
			clientUI = new nc.ui.ehpta.hq010403.ClientUI();
		
		if(handler == null)
			handler = new nc.ui.ehpta.hq010403.EventHandler(clientUI , new nc.ui.ehpta.hq010403.ClientUICtrl());
		
		if(businessAction == null)
			businessAction = handler.getBusiAction();
		
		return businessAction;
	}
	


	public void cpyAndAddBoStatListener() {
		if("200602".equals(getDjSettingParam().getNodeID()) || "200802".equals(getDjSettingParam().getNodeID())){
			if(StatListenerHelper.getQcIsClose(this) || StatListenerHelper.checkQcdj_jzxx(this)){
				m_boCpy.setEnabled(false);
				m_boAdd.setEnabled(false);
			}else{
				if(null != this.getDjDataBuffer().getCurrentDJZBVO()){
					m_boCpy.setEnabled(true);
				}else{
					m_boCpy.setEnabled(false);
				}
				m_boAdd.setEnabled(true);
			}
		}else if(DjTempletHelper.isCardInEdit(this)){
			m_boCpy.setEnabled(false);
			m_boAdd.setEnabled(false);
		}else{
			if(null != this.getDjDataBuffer().getCurrentDJZBVO()){
				m_boCpy.setEnabled(true);
			}else{
				m_boCpy.setEnabled(false);
			}
			m_boAdd.setEnabled(true);
		}
	}
	/**
	 * @param vos
	 *
	 * 重新设置billmap的信息
	 */
	private void resetBillMap(AggregatedValueObject[] vos) {
		Map<String,List<ArapBillMapVO>> map=new HashMap<String, List<ArapBillMapVO>>();
		  for(AggregatedValueObject agvo:vos){
			DJZBVO vo=(DJZBVO) agvo;
			DJZBItemVO[] items = vo.items;
			for(DJZBItemVO item:items){
				if(item.getBillmap()!=null && item.getBillmap().size()!=0){
					map.put(item.getFb_oid(), item.getBillmap());
				}
			}
		  }
		  if(map.size()!=0)
			  this.setAttribute(IRuntimeConstans.FlowMap, map);
	}


	public void updateLocalButtons() throws Exception{
		if(null!=getDjDataBuffer().getCurrentDjdl()){
//		String parentBilltype=PfDataCache.getBillType(this.getDjDataBuffer().getCurrentDjlxbm()).getParentbilltype();
			PfUtilClient.retAddBtn2(m_boAdd, this.getCurrentCorp(),getBilltype(getDjDataBuffer().getCurrentDjdl()),getDjDataBuffer().getCurrentDjlxbm());
			//cpyListener.updateButtonStatus(m_boCpy, this);
			PfUtilClient.retCopyBtn(m_boCpy, this.getCurrentCorp(), getBilltype(getDjDataBuffer().getCurrentDjdl()),getDjDataBuffer().getCurrentDjlxbm());
//			m_boAdd.setPowerContrl(false);
			List<ButtonObject> btns=new ArrayList<ButtonObject>();
			for(int i=0,size=m_boAdd.getChildCount();i<size;i++){
				if(this.getDjSettingParam().getIsQc()){
					if( m_boAdd.getChildButtonGroup()[i].getTag().startsWith("makeflag:")){
						btns.add(m_boAdd.getChildButtonGroup()[i]);
					}
				}
				else
					btns.add(m_boAdd.getChildButtonGroup()[i]);
				m_boAdd.getChildButtonGroup()[i].setPowerContrl(false);
			}
			m_boAdd.setChildButtonGroup(btns.toArray(new ButtonObject[]{}));
			btns.clear();
//			m_boCpy.setPowerContrl(false);
			for(int i=0,size=m_boCpy.getChildCount();i<size;i++){
				if(this.getDjSettingParam().getIsQc()){
					if( m_boCpy.getChildButtonGroup()[i].getTag().startsWith("makeflag:")){
						btns.add(m_boCpy.getChildButtonGroup()[i]);
					}
				}
				else
					btns.add(m_boCpy.getChildButtonGroup()[i]);
				m_boCpy.getChildButtonGroup()[i].setPowerContrl(false);
			}
			m_boCpy.setChildButtonGroup(btns.toArray(new ButtonObject[]{}));
			setButtons(this.getDjButtons());
		}
	}

	protected void showbills(AggregatedValueObject[] vos,String busitype) throws Exception{
		 this.getDjDataBuffer().getListSelectedVOs().clear();
			int pzglh=this.getDjSettingParam().getSyscode();
			this.settlevo(vos, pzglh,busitype);
	      SearchAction sa=new SearchActionNor();
	      sa.setActionRunntimeV0(this);
	      SearchActionNor san=new SearchActionNor();
	      san.setActionRunntimeV0(this);
	      san.changeTab();
	      sa.updateListPanel((DJZBVO[])vos);
//	      this.getArapDjPanel1().setDj((DJZBVO)vos[0]);
	      this.getDjDataBuffer().setCurrentDJZBVO((DJZBVO)vos[0]);

//	      if(this.getDjDataBuffer().getCurrentDJZBVO()!=null){
//				DJZBHeaderVO headvo=(DJZBHeaderVO)this.getDjDataBuffer().getCurrentDJZBVO().getParentVO();
//				if(headvo.getDjzt()==DJZBVOConsts.m_intDJStatus_UNSaved){
//					EditAction ea=new EditAction();
//					ea.setActionRunntimeV0(this);
//					try {
//						ea.edit();
//						this.getArapDjPanel1().setM_DjState(ArapBillWorkPageConst.WORKSTAT_NEW);
//					} catch (BusinessException e) {
//						ExceptionHandler.consume(e);
//					}
//				}
//			}
	}
	protected void settlevo(AggregatedValueObject[] vos,int pzglh,String busitype) throws Exception{
//		Map<String,List<ArapBillMapVO>> billmap=new HashMap<String,List<ArapBillMapVO>>();
	  int pos=0;
      for(int i=0,size=vos.length;i<size;i++){
    	  DJZBHeaderVO head=(DJZBHeaderVO)vos[i].getParentVO();
    	  head.setVouchid(DJZBVOConsts.ARAP_TEMP_VOUCHID+(pos++));
    	  head.setDjzt(DJZBVOConsts.m_intDJStatus_UNSaved);
    	  head.setTransientFlag(DJZBVOConsts.ACT_FLOW_BILL);
    	  if(null!=busitype){
    		  head.setXslxbm(busitype);
    	  }
    	  if(pzglh!=head.getPzglh()){//处理应收系统到应付或反之情况
//			for(DJZBItemVO item:items){
//				if(DJZBVOConsts.WLDX_KH.equals(item.getWldx())){
//					item.setWldx(DJZBVOConsts.WLDX_GYS);
//				}else if(DJZBVOConsts.WLDX_GYS.equals(item.getWldx())){
//					item.setWldx(DJZBVOConsts.WLDX_KH);
//				}
//			}
			head.setPzglh(pzglh);
		}
    	if(  this.getDjSettingParam().getIsQc()){
    		head.setQcbz(UFBoolean.TRUE);
    		try {
				nc.vo.pub.lang.UFDate qyrq = this.getDjSettingParam().getQyrq2();// getQyrq2();
				// //启用日期
				// 期初单据日期在启用日期前一天
				head.setDjrq(qyrq.getDateBefore(1));
				head.setEffectdate(qyrq.getDateBefore(1));
			} catch (Throwable e) {
			}
			head.setShrq( this.getDjSettingParam().getLoginDate()); // 审核日期
			head.setDjzt(2); // 单据状态(期初单据
    	}
      }
	}
	protected String getBilltype(String djdl){
		String billtype=null;
		if("ys".equals(djdl))
			billtype="F0";
		else if("yf".equals(djdl))
			billtype="F1";
		else if("sk".equals(djdl))
			billtype="F2";
		else if("fk".equals(djdl))
			billtype="F3";
		else if("sj".equals(djdl))
			billtype="F4";
		else if("fj".equals(djdl))
			billtype="F5";
		else if("hj".equals(djdl))
			billtype="F6";
		return billtype;
	}
	protected String selectBusitype(ButtonObject bt)throws BusinessException{
		if(null==bt||bt.getTag()==null)return null;

		String billtype=bt.getTag().substring(0, bt.getTag().indexOf(":"));
		if(!isOutFlowBillType(billtype))
			return null;

		List<String> busi=new ArrayList<String>();
		StringTokenizer st=new StringTokenizer(bt.getTag().substring(bt.getTag().indexOf(":")+1),",");
		while(st.hasMoreElements()){
			busi.add((String)st.nextElement());
		}
		if(busi.size()==1)return busi.get(0);
		ArapBusiConfirmDLg dlg=new ArapBusiConfirmDLg(this);
		dlg.showModel(busi.toArray(new String[]{}));
		if(dlg.isOK())
			return dlg.getSelectData();
		else
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000087")/*@res "用户取消操作!"*/);
	}
	protected boolean isOutFlowBillType(String billtype){
		 if(outflow.contains(billtype))
			 return true;
		 DjLXVO djlxvo= FiPubDataCache.getBillType(billtype, this.getCorpPrimaryKey());
		 if(null!=djlxvo&&"ss".equals(djlxvo.getDjdl()))
			 return true;
		 return false;
	}

		protected   List<String>  outflow=Arrays.asList(new String[]{
				"FZ",
				"45",
				"Z4",
				"Z5",
				"50",
				"21",
				"33",
				"25",
				"4501",
				"Z2",
				"4453",
				"5HZG",
				"4814",
				"4816",
				"4T",
				"5F",
				"5GZG",
				"4812",
				"30"});

}