package nc.ui.yto.org;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.sun.java.browser.net.ProxyService;

import nc.bs.framework.common.NCLocator;
import nc.bs.util.Uriread;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.ui.bd.pub.ISortVOByFields;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.body.AbstractClientUI;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.yto.org.OrganizeVO;

public class ClientUI extends AbstractClientUI {
	
	private int maxRows = 100000;
	
	@Override
	protected CardEventHandler createEventHandler() {
		return new OrgEventHandler(this, createController());
	}

	@Override
	protected ICardController createController() {
		return new OrgController();
	}

	@Override
	protected void initSelfData() {
		
		try {
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
			
			SuperVO[] queryVos = (SuperVO[]) iUAPQueryBS.retrieveByClause(OrganizeVO.class, " nvl(dr , 0) = 0", new String[]{
				"pk_organizational",
				"(case when proc = 'add' then '新增' when proc = 'update' then '升级' when proc = 'changerelation' then '关系变更'  when proc = 'delete' then '注销'  when proc = 'merger' then '合并'  when proc = 'merged' then '被合并' when proc = 'modify' then '修改' else proc end) proc",
				"unitcode",
				"unitname",
				"parentcode",
				"type",
				"dr",
				"ts",
				"description",
				"regularchain",
				"used",
				"status orgstatus"
			}).toArray((SuperVO[]) Array.newInstance(OrganizeVO.class, 0));

//			List<OrganizeVO> orgList = new ArrayList<OrganizeVO>();
			IGener iGener = (IGener)NCLocator.getInstance().lookup(IGener.class);
			IFilePost iFilePost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class);
			
			for(OrganizeVO org : (OrganizeVO[])queryVos) {

				if(("修改".equals(org.getProc()) || "modify".equals(org.getProc())) && !"虚拟组织".equals(org.getType()) && org.getDr() == 0) {
					String code = org.getUnitcode();
					nc.vo.yto.business.CorpVO[] corpVOs = (nc.vo.yto.business.CorpVO[])HYPubBO_Client.queryByCondition(nc.vo.yto.business.CorpVO.class, " unitcode = '"+code+"' ");
					
					if(corpVOs.length > 0) {
						if(!String.valueOf(org.getRegularchain()).equals(String.valueOf(corpVOs[0].getIsworkingunit())))
							continue;
						
						if(String.valueOf(org.getUsed()).equals(String.valueOf(corpVOs[0].getIsseal())))
							continue;
						
						if("N".equals(String.valueOf(org.getUsed())) || org.getUsed() == null)
							continue;
						
						try {
							iUAPQueryBS.executeQuery("update bd_corp set unitshortname = '"+org.getUnitname()+"' where unitcode = '"+org.getUnitcode()+"'", null);
						} catch(Exception e) {}
						
						
						
						corpVOs = (nc.vo.yto.business.CorpVO[])HYPubBO_Client.queryByCondition(nc.vo.yto.business.CorpVO.class, " unitcode = '"+code+"' ");
						
						String xmlStr = iGener.generateXml2(corpVOs[0], "RequestCorp", "corp", "modify");
						
						String retStr = iFilePost.postFile(Uriread.uriPath(), xmlStr);
						
						String[] strs = retStr.split("<success>");
						String retMsg = "";
						if (strs.length > 1)
							retMsg = strs[1].substring(0, strs[1].indexOf("<"));

						if (retMsg.equals("false") || strs.length <= 1) {
							try {
								iUAPQueryBS.executeQuery("update bd_organizational set dr = 0 where pk_organizational = '"+org.getPk_organizational()+"'", null);
							}catch(Exception e) {}
						}
						
					} else {
						nc.vo.yto.business.DeptdocVO[] deptVOs = (nc.vo.yto.business.DeptdocVO[])HYPubBO_Client.queryByCondition(nc.vo.yto.business.DeptdocVO.class, " deptcode = '"+code+"' ");
						if(deptVOs.length > 0) {
							
							if(String.valueOf(org.getUsed()).equals(String.valueOf(deptVOs[0].getHrcanceled())))
								continue;
							
							if(String.valueOf(org.getUsed()).equals(String.valueOf(deptVOs[0].getCanceled())))
								continue;
							
							if("N".equals(String.valueOf(org.getUsed())) || org.getUsed() == null)
								continue;
							
							try {
								iUAPQueryBS.executeQuery("update bd_deptdoc set deptname = '"+org.getUnitname()+"' where deptcode = '"+org.getUnitcode()+"'", null);
							} catch(Exception e) {}
							
							
							deptVOs = (nc.vo.yto.business.DeptdocVO[])HYPubBO_Client.queryByCondition(nc.vo.yto.business.DeptdocVO.class, " deptcode = '"+code+"' ");
							
							String xmlStr = iGener.generateXml3(deptVOs[0], "RequestDeptdoc", "dept", "modify");
						
							String retStr = iFilePost.postFile(Uriread.uriPath(), xmlStr);
							
							String[] strs = retStr.split("<success>");
							String retMsg = "";
							if (strs.length > 1)
								retMsg = strs[1].substring(0, strs[1].indexOf("<"));

							if (retMsg.equals("false") || strs.length <= 1) {
								try {
									iUAPQueryBS.executeQuery("update bd_organizational set dr = 0 where pk_organizational = '"+org.getPk_organizational()+"'", null);
								}catch(Exception e) {}
							}
							
						} 
					
					}
					
				} 
			}
			
			queryVos = (SuperVO[]) iUAPQueryBS.retrieveByClause(OrganizeVO.class, " nvl(dr , 0) = 0", new String[]{
				"pk_organizational",
				"(case when proc = 'add' then '新增' when proc = 'update' then '升级' when proc = 'changerelation' then '关系变更'  when proc = 'delete' then '注销'  when proc = 'merger' then '合并'  when proc = 'merged' then '被合并' when proc = 'modify' then '修改' else proc end) proc",
				"unitcode",
				"unitname",
				"parentcode",
				"type",
				"dr",
				"ts",
				"description",
				"regularchain",
				"used",
				"status orgstatus"
			}).toArray((SuperVO[]) Array.newInstance(OrganizeVO.class, 0));
			
			getBufferData().clear();
	        AggregatedValueObject vo = (AggregatedValueObject)Class.forName(createController().getBillVoName()[0]).newInstance();
	        if(createController() instanceof ISortVOByFields)
	        {
	            String fields[] = ((ISortVOByFields)createController()).getSortFields();
	            if(fields != null && queryVos != null)
	                VOUtil.ascSort(queryVos, fields);
	        }
	        
	        vo.setChildrenVO(queryVos);
	        getBufferData().addVOToBuffer(vo);

	        
	        if(getBufferData().getVOBufferSize() != 0)
	        {
	            setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
	            setBillOperate(2);
	            getBufferData().setCurrentRow(0);
	        } else
	        {
	            setListHeadData(null);
	            setBillOperate(4);
	            getBufferData().setCurrentRow(-1);
	            showHintMessage(NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000066"));
	        }
	    } catch (Exception e) {
	        	e.printStackTrace();
	    }
		
        ((BillScrollPane)getBillCardPanel().getBodySelectedScrollPane()).addTableSortListener();
        
	}

	

}
