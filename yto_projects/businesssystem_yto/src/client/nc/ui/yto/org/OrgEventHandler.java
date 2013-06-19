package nc.ui.yto.org;

import java.lang.reflect.Array;

import nc.bs.framework.common.NCLocator;
import nc.bs.util.Uriread;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.bd.pub.ISortVOByFields;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.yto.org.OrganizeVO;

public class OrgEventHandler extends DefaultBDBillCardEventHandle{

	StringBuffer strWhere = null;
	
	public OrgEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoQuery() throws Exception {
		
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		strWhere = new StringBuffer();
        if(!askForQueryCondition(strWhere)) {
            return;
            
        } else {
        	
        	if(getBillUI().showOkCancelMessage("是否包含历史记录！") == 1)
        		strWhere = new StringBuffer(strWhere.toString().substring(0 , strWhere.length() - 20));
        	
        	SuperVO[] queryVos = (SuperVO[]) iUAPQueryBS.retrieveByClause(OrganizeVO.class, (strWhere == null ? " 1 = 1" : strWhere.toString()) , new String[]{
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
             AggregatedValueObject vo = (AggregatedValueObject)Class.forName(getUIController().getBillVoName()[0]).newInstance();
             if(getUIController() instanceof ISortVOByFields)
             {
                 String fields[] = ((ISortVOByFields)getUIController()).getSortFields();
                 if(fields != null && queryVos != null)
                     VOUtil.ascSort(queryVos, fields);
             }
             vo.setChildrenVO(queryVos);
             getBufferData().addVOToBuffer(vo);
             updateBuffer();
            
            return;
        }
	}
	
	@Override
	public void onButton(ButtonObject bo) {
		try {
			
			if("查询".equals(bo.getName()))
				onBoQuery();
			else if("刷新".equals(bo.getName()))
				onBoRefresh(strWhere == null ? null : strWhere.toString());
			else if("删除".equals(bo.getName()))
				onBoDelete();
			else
				super.onButton(bo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
        CircularlyAccessibleValueObject[] selectedVOs = getBillCardPanelWrapper().getSelectedBodyVOs();
        if(selectedVOs != null && selectedVOs.length > 0) {
        	
        	if(getBillUI().showOkCancelMessage("确定要删除该记录吗？") == 2)
        		return;
        	
        	String pk_organizational = String.valueOf(selectedVOs[0].getAttributeValue("pk_organizational"));
        	try {
        		iUAPQueryBS.executeQuery("update bd_organizational set dr = 1 where pk_organizational = '"+pk_organizational+"'", null);
        	} catch (Exception e) { }
        	
        	SuperVO[] queryVos = (SuperVO[]) iUAPQueryBS.retrieveByClause(OrganizeVO.class, " 1 = 1 and nvl(dr , 0 ) = 0 and " + (strWhere == null ? " 1 = 1" : strWhere.toString()) , new String[]{
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
             AggregatedValueObject vo = (AggregatedValueObject)Class.forName(getUIController().getBillVoName()[0]).newInstance();
             if(getUIController() instanceof ISortVOByFields)
             {
                 String fields[] = ((ISortVOByFields)getUIController()).getSortFields();
                 if(fields != null && queryVos != null)
                     VOUtil.ascSort(queryVos, fields);
             }
             vo.setChildrenVO(queryVos);
             getBufferData().addVOToBuffer(vo);
             updateBuffer();
             
        } else 
        	getBillUI().showWarningMessage("请选择一条记录！");
	}
	
	protected void onBoRefresh(String where) throws Exception {
		
		int check = getBillUI().showYesNoMessage("耗时操作，确定执行吗？");
		if(check == 8 || check == 2) {
			return ;
		}
		
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		if(where == null || "".equals(where))
			where = getUIController().getBodyCondition();
		
		if(where == null || "".equals(where))
			where = " nvl(dr , 0 ) = 0";
		
		SuperVO[] queryVos = (SuperVO[]) iUAPQueryBS.retrieveByClause(OrganizeVO.class, where , new String[]{
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
        AggregatedValueObject vo = (AggregatedValueObject)Class.forName(getUIController().getBillVoName()[0]).newInstance();
        if(getUIController() instanceof ISortVOByFields)
        {
            String fields[] = ((ISortVOByFields)getUIController()).getSortFields();
            if(fields != null && queryVos != null)
                VOUtil.ascSort(queryVos, fields);
        }
        vo.setChildrenVO(queryVos);
        getBufferData().addVOToBuffer(vo);
        updateBuffer();
        
	}
}
