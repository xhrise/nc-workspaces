package nc.ui.eh.sc.h0450515;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0450515.BomBVO;
import nc.vo.eh.sc.h0450515.BomVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
/**
 * 说明：BOM档案 
 * @author 张起源 
 * 时间：2008-5-06
 */
public class ClientEventHandler extends AbstractEventHandler {
	private BomVO bvo=null;
    int flag = 0 ;				//当已经有此物料BOM时复制另外一物料的BOM,在保存时需将原料的物料BOM最新标记去掉，flag = 1保存时不做校验
	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    
	@SuppressWarnings("deprecation")
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			bo.removeChildButton(bo.getChildButtonGroup()[0]);	//去掉终止单据按钮
			
			ButtonObject boUncheck = new ButtonObject("取消当前生产");
			boUncheck.setTag(String.valueOf(IEHButton.UnCheck));
			boUncheck.setCode("取消当前生产");
			bo.addChildButton(boUncheck);
			
			ButtonObject boPrevedition = new ButtonObject("上一页");
			boPrevedition.setTag(String.valueOf(IEHButton.Prev));
			boPrevedition.setCode("上一页");
			bo.addChildButton(boPrevedition);
			
			ButtonObject boNextedition = new ButtonObject("下一页");
			boNextedition.setTag(String.valueOf(IEHButton.Next));
			boNextedition.setCode("下一页");
			bo.addChildButton(boNextedition);
		}
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.EditionChange: 			//版本变更
			setEditionChange();
			break;
        case IEHButton.LOCKBILL:    			//关闭单据
            onBoLockBill();
            break;
        case IEHButton.Prev:    				//上一页 下一页
            onBoBrows(intBtn);
            break;
        case IEHButton.Next:   					 //上一页 下一页
            onBoBrows(intBtn);
            break;
        case IEHButton.CONFIRMBUG:    			 //复制
            onBoElseCopy(intBtn);
            break;
        case IEHButton.prevedition:               //上一版本
            onBoprevedition(intBtn);
            break;
        case IEHButton.nextedition:               //下一版本
            onBonextedition(intBtn);
            break;    
        case IEHButton.ConfirmSC:               //确定生产
            onBoConfirmSC(intBtn);
            break; 
        case IEHButton.UnCheck:               	//取消生产
            onBoCancelSC(intBtn);
            break;
		}
	}
	
	/***
	 * 取消生产 
	 * 2009-2-23 16:10:34
	 * @throws Exception 
	 */
	private void onBoCancelSC(int intBtn) throws Exception{
		String invname = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject()==null?"":
 			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject().toString();
		int iRet = getBillUI().showYesNoMessage("是否取消按此配方生产 "+invname+" 产品?");
		if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
			 BomVO hvo = (BomVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
			 hvo.setSc_flag(new UFBoolean(false));
			 IVOPersistence ivopersistence=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			 ivopersistence.updateVO(hvo);
			 onBoRefresh();
		}
	}

	/***
	 * 确定生产 
	 * wb 2009-2-11 18:53:32
	 * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	private void onBoConfirmSC(int intBtn) throws Exception {
    	BomVO hvo = (BomVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_invmandoc = hvo.getPk_invbasdoc();
    	StringBuilder sql2 = new StringBuilder()
    	.append(" select b.invcode,b.invname  from bd_invmandoc a,bd_invbasdoc b")
    	.append(" where a.pk_invbasdoc=b.pk_invbasdoc")
    	.append(" and a. pk_corp = '"+_getCorp().getPk_corp()+"'")
    	.append(" and a.pk_invmandoc = '"+pk_invmandoc+"'")
    	.append(" and nvl(b.def8,'N')='Y'");
    	ArrayList ar = (ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor()); 
    	if (ar.size()>0)
    	{
    		getBillUI().showErrorMessage("此物料已封存,不允许使用,请核对!");
            return;
    	}
    	BomBVO[] bvos = (BomBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
    	StringBuilder sb = new StringBuilder("('");
    	for (int i=0;i<bvos.length;i++)
    	{
    		BomBVO bvo = bvos[i];
    		
    		if (i==bvos.length-1)
    		{
    			sb.append(bvo.getPk_invbasdoc()+"')");  
    		}else
    		{
    			sb.append(bvo.getPk_invbasdoc()+"','");  
    		}
    	}
    	StringBuilder tips = new StringBuilder("");
    	StringBuilder sqlnew = new StringBuilder()
    	.append(" select b.invcode,b.invname  from bd_invmandoc a,bd_invbasdoc b")
    	.append(" where a.pk_invbasdoc=b.pk_invbasdoc")
    	.append(" and a. pk_corp = '"+hvo.getPk_corp()+"'")
    	.append(" and a.pk_invmandoc in "+sb.toString())
    	.append(" and (nvl(b.def8,'N')='Y'")
    	.append(" or a.def1 is  null)");
    	ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sqlnew.toString(), new MapListProcessor());    	
    	for (int i=0;i<arr.size();i++)
    	{
    		HashMap hm =(HashMap) arr.get(i);
    		tips.append(hm.get("invcode")+":"+hm.get("invname")+"\r\n");
    	}    	
    	if (!"".equals(tips.toString()))
    	{
    		getBillUI().showWarningMessage(tips.toString()+"以上物料已封存或没有维护仓库,请核对!");
    		return;
    	}
    	//add by houcq 2011-09-28 begin
    	StringBuilder tips2 = new StringBuilder("");
    	for (int i=0;i<bvos.length;i++)
    	{
    		if (bvos[i].getPk_altinvbasdoc()==null||"".equals(bvos[i].getPk_altinvbasdoc()))
    		{
    			tips2.append((i+1)+",");
    		}
    	}
    	if (!"".equals(tips2.toString()))
    	{
    		getBillUI().showWarningMessage("第"+tips2.toString().substring(0,tips2.toString().length()-1)+"行仓库不能为空!");
    		return;
    	}
    	//add by houcq 2011-09-28 end
    	 String invname = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject()==null?"":
    		 			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject().toString();
    	 int iRet = getBillUI().showYesNoMessage("是否确定按此配方生产 "+invname+" 产品?");
         if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
        	 //BomVO hvo = (BomVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	 String sql = "update eh_bom set sc_flag = NULL where pk_invbasdoc = '"+hvo.getPk_invbasdoc()+"'";
        	 PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
             pubItf.updateSQL(sql);
             
        	 hvo.setSc_flag(new UFBoolean(true));
        	 IVOPersistence ivopersistence=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
        	 ivopersistence.updateVO(hvo);
        	 onBoRefresh();
         }
        
	}

	//下一版本 add by zqy 2008-10-6 15:09:56
	@SuppressWarnings("unchecked")
	private void onBonextedition(int intBtn) throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        BomVO bvo = (BomVO) aggvo.getParentVO();
        String pk_invbasdoc = bvo.getPk_invbasdoc()==null?"":bvo.getPk_invbasdoc().toString();//物料主键
        Integer ver = new Integer(bvo.getVer()==null?"":bvo.getVer().toString());//版本号
        Integer ver2 = ver+1;
        String sql = " select * from eh_bom where pk_invbasdoc='"+pk_invbasdoc+"' and ver='"+ver2+"' and NVL(dr,0)=0 ";
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        if(arr.size()==0){
            getBillUI().showErrorMessage("此物料没有下一版本,此版本已为最高版本!");
            return;
        }else{
            //将数据显示到界面
            String whereSql = " pk_invbasdoc = '"+pk_invbasdoc+"' and ver='"+ver2+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
            nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
            SuperVO[] supervo = business.queryByCondition(BomVO.class, whereSql);
            getBufferData().clear();
           // 增加数据到Buffer
           addDataToBuffer(supervo);
           updateBuffer();
        }
    
    }

    //上一版本 add by zqy 2008-10-6 15:08:15
    private void onBoprevedition(int intBtn) throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        BomVO bvo = (BomVO) aggvo.getParentVO();
        String pk_invbasdoc = bvo.getPk_invbasdoc()==null?"":bvo.getPk_invbasdoc().toString();//物料主键
        Integer ver = new Integer(bvo.getVer()==null?"":bvo.getVer().toString());//版本号
        if(ver.compareTo(1)==0){
            getBillUI().showErrorMessage("此物料没有上一版本,此版本已为最低版本!");
            return;
        }else{
            Integer ver2 = ver-1;
            //将数据显示到界面
            String whereSql = " pk_invbasdoc = '"+pk_invbasdoc+"' and ver='"+ver2+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
            nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
            SuperVO[] supervo = business.queryByCondition(BomVO.class, whereSql);
            getBufferData().clear();
           // 增加数据到Buffer
           addDataToBuffer(supervo);
           updateBuffer();
        }
        
    }

    //当已经有此物料BOM时复制另外一物料的BOM,在保存时需将原料的物料BOM最新标记去掉，保存时不做校验 add by wb at 2008-8-19 13:54:52 
	private void onBoElseCopy(int intBtn) throws Exception{
		onBoRefresh();
		onBoCopy();
		flag = 1;
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sc_flag",null);
	}

	//版本变更
	private void setEditionChange() throws Exception{
		onBoRefresh();
		AggregatedValueObject aggVO = getBillUI().getChangedVOFromUI();
		if(aggVO==null){
			getBillUI().showErrorMessage("取源单据发生异常,请查询后重新尝试!");
            return;
		}
//add by houcq 2011-10-24取消只有最新版本才能做版本变更
//		Object new_flag= aggVO.getParentVO().getAttributeValue("new_flag");
//		if(new_flag!=null && new_flag.toString().equals("N")){
//			getBillUI().showErrorMessage("你更新的不是最新版本，不可以更改");
//			return;
//		}
	
	    bvo = (BomVO) getBillUI().getVOFromUI().getParentVO();
		onBoCopy();
		//int ver = bvo.getVer();
		String sql="select max(ver) ver from eh_bom  where pk_corp='"+_getCorp().getPk_corp()+"' and pk_invbasdoc='"+bvo.getPk_invbasdoc()+"'  and NVL(dr,0)=0 ";
		try {
			Object verobj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
	        int ver = Integer.parseInt(verobj==null?"0":verobj.toString())+1;
	  		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ver",ver);
	  		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sc_flag",null);
			getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
	        getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
	        //getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);//modify by houcq 2011-10-14
	        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_unit").setEnabled(false);
	        getButtonManager().getButton(IEHButton.EditionChange).setEnabled(false);
		}catch(Exception ex){
        	ex.printStackTrace();
        }
      }
	
	
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
		boolean lb_add = true;
        AggregatedValueObject aggVO = getBillUI().getChangedVOFromUI();
		String pk_invmandoc = aggVO.getParentVO().getAttributeValue("pk_invbasdoc").toString();
    	StringBuilder sql2 = new StringBuilder()
    	.append(" select b.invcode,b.invname  from bd_invmandoc a,bd_invbasdoc b")
    	.append(" where  a.pk_invbasdoc=b.pk_invbasdoc")
    	.append(" and a. pk_corp = '"+_getCorp().getPk_corp()+"'")
    	.append(" and a.pk_invmandoc = '"+pk_invmandoc+"'")
    	.append(" and nvl(b.def8,'N')='Y'");
    	ArrayList ar = (ArrayList)iUAPQueryBS.executeQuery(sql2.toString(), new MapListProcessor()); 
    	if (ar.size()>0)
    	{
    		getBillUI().showErrorMessage("此物料是封存物料,不能保存,请核对!");
            return;
    	}
		lb_add = isAdding();
        if(aggVO==null){
            getBillUI().showErrorMessage("取源单据发生异常,请查询后重新尝试!");
            return;
        }
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//前台校验
		BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("物料有重复！");
            return;
        }
        
        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        if (model != null) {
            int rowCount = model.getRowCount();
            if (rowCount < 1) {
                NCOptionPane.showMessageDialog(getBillUI(), "表体行不能为空!");
                return;
            }
        }
        
        
        //判断该物料是否已经录入过，如果系统中已经存在着BOM情况，则不允许保存
        //add by newyear at 2008-5-23 
        if (isAdding()&&flag!=1&&bvo==null){
            String pk_invbasdoc = aggVO.getParentVO().getAttributeValue("pk_invbasdoc").toString();
            String sql="select * from eh_bom  where pk_invbasdoc='"+pk_invbasdoc+"' and NVL(dr,0)=0";
            BomVO bomvo = (BomVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BomVO.class));
            if(bomvo!=null){
            	if (getBillUI().showYesNoMessage("该物料已有BOM，是否确认保存?")!=4)
            	{
            		onBoCancel();
            		return;
            	}            	
            }
        }   
        // 当已经有此物料BOM时复制另外一物料的BOM,在保存时需将原料的物料BOM最新标记去掉，保存时不做校验 add by wb at 2008-8-19 13:54:52 
        if(flag==1){
        	String pk_invbasdoc = aggVO.getParentVO().getAttributeValue("pk_invbasdoc").toString();
        	String sql="select max(ver) ver from eh_bom  where pk_invbasdoc='"+pk_invbasdoc+"'  and NVL(dr,0)=0 ";
			try {
				Object verobj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				if (getBillUI().getBillOperate()!=0)
				{
					int ver = Integer.parseInt(verobj==null?"0":verobj.toString())+1;
	            	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ver", ver);
				}
            }catch(Exception ex){
            	ex.printStackTrace();
            }
            flag = 0;
        }
        
        if(ClientUI.flag==1){
        	String sql = "delete from eh_bom_b where pk_bom_b in "+ClientUI.pk_bom_bs;
            PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubItf.updateSQL(sql);
        }
        ClientUI.pk_bom_bs = "('')";
        
        BomBVO[] bvos = (BomBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        UFDouble pprate = ((BomVO)aggVO.getParentVO()).getPprate()==null?new UFDouble("0"): ((BomVO)aggVO.getParentVO()).getPprate();
        for(int i=0;i<bvos.length;i++){
        	UFDouble zxamount = bvos[i].getAmount();
        	UFDouble zamount = bvos[i].getZamount();
        	if (zxamount==null || zxamount.toDouble()==0 ||zamount==null || zamount.toDouble()==0)
        	{
        		getBillUI().showWarningMessage("第"+(i+1)+"行子项用量或zamount字段不能为0或空");
        		return;
        	}
        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pprate.multiply(zxamount), i, "ppamount");
        	
        	int rowstatus  = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowState(i);
        	if(lb_add )
        		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
        	else{
        		if (rowstatus ==BillModel.ADD)
        			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.ADD);
        		else
        			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
        	}
        }
        StringBuilder sb = new StringBuilder("('");
    	for (int i=0;i<bvos.length;i++)
    	{
    		BomBVO bvo = bvos[i];
    		
    		if (i==bvos.length-1)
    		{
    			sb.append(bvo.getPk_invbasdoc()+"')");  
    		}else
    		{
    			sb.append(bvo.getPk_invbasdoc()+"','");  
    		}
    	}
    	//add by houcq 2011-09-28 begin
    	StringBuilder tips2 = new StringBuilder("");
    	StringBuilder sqlnew = new StringBuilder()
    	.append(" select b.invcode,b.invname  from bd_invmandoc a,bd_invbasdoc b")
    	.append(" where a.pk_invbasdoc=b.pk_invbasdoc")
    	.append(" and a. pk_corp = '"+_getCorp().getPk_corp()+"'")
    	.append(" and a.pk_invmandoc in "+sb.toString())
    	.append(" and (nvl(b.def8,'N')='Y'")
    	.append(" or a.def1 is  null)");
    	ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sqlnew.toString(), new MapListProcessor());    	
    	for (int i=0;i<arr.size();i++)
    	{
    		HashMap hm =(HashMap) arr.get(i);
    		tips2.append(hm.get("invcode")+":"+hm.get("invname")+"\r\n");
    	}    	
    	if (!"".equals(tips2.toString()))
    	{
    		getBillUI().showWarningMessage(tips2.toString()+"以上物料是封存物料或没有维护仓库,请核对!");
    		return;
    	}
    	//add by houcq 2011-09-28 end
      //add by houcq 2011-08-08 begin
        String pk_invbasdoc = aggVO.getParentVO().getAttributeValue("pk_invbasdoc").toString();
        String isbcp="select def3 from bd_invmandoc where def3='Y' and pk_invmandoc='"+pk_invbasdoc+"' and pk_corp='"+ _getCorp().getPk_corp()+"'";
        Object isbcpObj = iUAPQueryBS.executeQuery(isbcp, new ColumnProcessor());
		if(isbcpObj!=null){
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vsourcebillid", 'Y');
        }
        //add by houcq 2011-08-08 end
        super.onBoSave();  
	   }
	
    /*
     * 功能：查询对话框显示
     */
	 private QueryConditionClient dlg = null;
     protected QueryConditionClient getQueryDLG()
     {        
         if(dlg == null){
             dlg = createQueryDLG();
         }
         return dlg;
     }
	
    //查询模板中选择为最新标记
     protected QueryConditionClient createQueryDLG() {
            QueryConditionClient dlg = new QueryConditionClient();
            dlg.setTempletID(_getCorp().getPk_corp(), "H0450515", null, null); 
            //dlg.setDefaultValue("new_flag","Y",null);
            dlg.setNormalShow(false);
            return dlg;
        }
     
     @SuppressWarnings("unchecked")
	@Override
     protected void onBoQuery() throws Exception {
 		ClientEnvironment ce = ClientEnvironment.getInstance();
         int type = getQueryDLG().showModal();
         if(type==1){
             
             //在BOM查询模板中加入物料类型与物料名称这二个查询条件 add by zqy 2008-9-27 13:29:36
             ConditionVO[] invtype  = getQueryDLG().getConditionVOsByFieldCode("invtype");
             ConditionVO[] name  = getQueryDLG().getConditionVOsByFieldCode("invname");
             ConditionVO[] wlpk_invbasdc  = getQueryDLG().getConditionVOsByFieldCode("pk_invbasdoc");
            // ConditionVO[] new_flag  = getQueryDLG().getConditionVOsByFieldCode("new_flag");//最新标记
             ConditionVO[] sc_flag  = getQueryDLG().getConditionVOsByFieldCode("sc_flag");//生产标记
             ConditionVO[] ver  = getQueryDLG().getConditionVOsByFieldCode("ver");//版本号
             ConditionVO[] coperatorid  = getQueryDLG().getConditionVOsByFieldCode("coperatorid");//制单人
             ConditionVO[] dmakedate  = getQueryDLG().getConditionVOsByFieldCode("dmakedate");//制单日期            
             ConditionVO[] zbno  = getQueryDLG().getConditionVOsByFieldCode("zbno");//总部编号
             String invclasscode = null;//物料分类编码
             String invname = null;//物料名称
             String pk_invbasdoc=null;//物料PK 
             @SuppressWarnings("unused")
			String invcode=null;
             @SuppressWarnings("unused")
			HashMap hm = getAllcode();
             StringBuffer wljbsql = new StringBuffer();			//物料基本档案SQL
             StringBuffer wlglsql = new StringBuffer();			//物料管理档案SQL
             StringBuffer bomsql = new StringBuffer();			//BOM SQL
             if(invtype.length>0){
                 invclasscode = invtype[0].getValue()==null?"":invtype[0].getValue().toString();
                 String SQL =" select invclasscode from bd_invcl where pk_invcl='"+invclasscode+"' and NVL(dr,0)=0 ";
                 String code = null;
                 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                 ArrayList arr =(ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
                 if(arr!=null && arr.size()>0){
                     for(int i=0;i<arr.size();i++){
                         HashMap hmm = (HashMap)arr.get(i);
                         code = hmm.get("invclasscode")==null?"":hmm.get("invclasscode").toString();
                     }
                 }
                 wljbsql.append(" and  invcode like '%"+code+"%' ");
             }
             if(name.length>0){
                 invname = name[0].getValue()==null?"":name[0].getValue().toString();
                 wljbsql.append("and  (invname like '%"+invname+"%' or invmnecode like '%"+invname+"%')");
             }
             if(wlpk_invbasdc.length>0){
                 pk_invbasdoc=wlpk_invbasdc[0].getValue()==null?"":wlpk_invbasdc[0].getValue().toString();
                 wlglsql.append(" and  pk_invmandoc ='"+pk_invbasdoc+"'");
             }
//             if(new_flag.length>0&&!new_flag[0].getValue().equals("N")){
//                 String newflag = new_flag[0].getValue()==null?"":new_flag[0].getValue().toString();
//                 bomsql.append(" and new_flag='"+newflag+"' ");
//             }
             if(sc_flag.length>0&&!sc_flag[0].getValue().equals("N")){
                 String scflag = sc_flag[0].getValue()==null?"":sc_flag[0].getValue().toString();
                 bomsql.append(" and NVL(sc_flag,'N') = '"+scflag+"' ");
             }
             if(ver.length>0){
                 String vver = ver[0].getValue()==null?"":ver[0].getValue().toString();
                 bomsql.append(" and ver='"+vver+"' ");
             }
             if(coperatorid.length>0){
                 String coper = coperatorid[0].getValue()==null?"":coperatorid[0].getValue().toString();
                 bomsql.append(" and coperatorid='"+coper+"' ");
             }
             if(dmakedate.length>0){
                 String date = dmakedate[0].getValue()==null?"":dmakedate[0].getValue().toString();
                 bomsql.append(" and dmakedate='"+date+"' ");
             }
             if(zbno.length>0){
                 String zno = zbno[0].getValue()==null?"":zbno[0].getValue().toString();
                 bomsql.append(" and zbno like '%"+zno+"%' ");
             }
             String pk_corp = ce.getCorporation().getPk_corp();
             
             String SQL = " pk_invbasdoc in (select pk_invmandoc from bd_invmandoc  where 1=1 "+wlglsql.toString()+" and pk_invbasdoc in (select pk_invbasdoc from bd_invbasdoc where 1=1  "+wljbsql.toString()+" )" +
                     " and pk_corp ='"+pk_corp+"' ) and 1=1 "+bomsql.toString()+" and pk_corp='"+pk_corp+"' and NVL(dr,0)=0 ";
             
             SuperVO[] queryVos = queryHeadVOs(SQL);
             getBufferData().clear();
             // 增加数据到Buffer
             addDataToBuffer(queryVos);
             updateBuffer(); 
         }
     }
      
      @SuppressWarnings("unchecked")
	public static HashMap getAllcode(){
          HashMap hminvcode = new HashMap();
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          String sql = " select invclasscode,invclassname from bd_invcl where NVL(dr,0)=0 ";
          try {
              ArrayList arr =(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
              if(arr!=null && arr.size()>0){
                  String invclasscode = null;
                  String invclassname = null;
                  for(int i=0;i<arr.size();i++){
                      HashMap hm = (HashMap)arr.get(i);
                      invclasscode = hm.get("invclasscode")==null?"":hm.get("invclasscode").toString();
                      invclassname = hm.get("invclassname")==null?"":hm.get("invclassname").toString();
                      hminvcode.put(invclassname, invclasscode);
                  }
              }
          } catch (BusinessException e) {
              e.printStackTrace();
          }       
          return hminvcode;        
      }
     
     protected void onBoLockBill() throws Exception{
         AggregatedValueObject aggvo = getBillUI().getVOFromUI();
         BomVO bvo = (BomVO)aggvo.getParentVO();
        String primaryKey = bvo.getPrimaryKey();
        String lock_flag = bvo.getLock_flag()==null?"N":bvo.getLock_flag().toString();
        if(lock_flag.equals("Y")){
             getBillUI().showErrorMessage("该单据已经关闭!");
             return;
         }
         else if(!primaryKey.equals("")){
             int iRet = getBillUI().showYesNoMessage("是否确定进行关闭操作?");
             if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	 getBillCardPanelWrapper().getBillCardPanel().setHeadItem("new_flag", new UFBoolean(false));
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                bvo.setAttributeValue("lock_flag", new UFBoolean(true));
                bvo.setNew_flag(new UFBoolean(false));
                ivoPersistence.updateVO(bvo);
                getBillUI().showWarningMessage("已经关闭成功");
                 onBoRefresh();
             }
             else{
                return;
             }
         }
     }
     
     private void onBoBrows(int intBtn) throws java.lang.Exception {
        // 动作执行前处理
        buttonActionBefore(getBillUI(), intBtn);
        switch (intBtn) {
        case IEHButton.Prev: {
            getBufferData().prev();
            break;
        }
        case IEHButton.Next: {
            getBufferData().next();
            break;
        }
        }
        // 动作执行后处理
        buttonActionAfter(getBillUI(), intBtn);
        getBillUI().showHintMessage(
                nc.ui.ml.NCLangRes.getInstance()
                        .getStrByID(
                                "uifactory",
                                "UPPuifactory-000503",
                                null,
                                new String[] { nc.vo.format.Format
                                        .indexFormat(getBufferData()
                                                .getCurrentRow()+1) })/*
                                                                     * @res
                                                                     * "转换第:" +
                                                                     * getBufferData().getCurrentRow() +
                                                                     * "页完成)"
                                                                     */
                        );
        setBoEnabled();
     }
     
     @Override
    protected void onBoCopy() throws Exception {
         super.onBoCopy();
         getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ver",1);
         getBillCardPanelWrapper().getBillCardPanel().setHeadItem("new_flag","Y");
         getBillCardPanelWrapper().getBillCardPanel().setHeadItem("lock_flag","N");
         getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
         getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
         getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sc_flag",null);
         setBoEnabled();
    }

    
     
//   设置按钮的可用状态
     protected void setBoEnabled() throws Exception {
         AggregatedValueObject aggvo=getBillUI().getVOFromUI();
         String pk_bom = aggvo.getParentVO().getPrimaryKey();
         if (pk_bom==null){
         }
         else{   
             //上一页 下一页的按钮状态  add by wb at 2008-6-20 14:30:23
             if(getButtonManager().getButton(IEHButton.Prev)!=null){
 	            if(!getBufferData().hasPrev()){
 	    			getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[1].setEnabled(false);
 	    		}
 	            else{
 	            	getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[1].setEnabled(true);
 	            }
 	    		if(!getBufferData().hasNext()){
 	    			getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[2].setEnabled(false);
 	    		}
 	    		else{
 	    			getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[2].setEnabled(true);
 	            }
             }
             String sc_flag = aggvo.getParentVO().getAttributeValue("sc_flag").toString();
             if(sc_flag!=null&&sc_flag.equals("Y")){
            	 getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(true);
             }else{
            	 getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(false);
             }
         }
         getBillUI().updateButtonUI();
     }
     
     @Override
    protected void onBoCard() throws Exception {
    	 super.onBoCard();
    	 setBoEnabled();
    	 onBoRefresh();
     }

     
    @SuppressWarnings("unchecked")
	protected void onBoEdit() throws Exception {
        onBoRefresh();
        BomVO bom = (BomVO)getBillUI().getVOFromUI().getParentVO();
//        UFBoolean new_flag = new UFBoolean(bom.getNew_flag()==null?"":bom.getNew_flag().toString());
//        UFBoolean lock_flag = new UFBoolean(bom.getLock_flag()==null?"":bom.getLock_flag().toString());      
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	String sql="select pk_invbasdoc from eh_sc_cprkd_b   where isnull(dr,0)=0 and pk_invbasdoc='"+bom.getPk_invbasdoc()+"' and ver="+bom.getVer();
		ArrayList isBomUsed = (ArrayList) iUAPQueryBS.executeQuery(sql, new ArrayListProcessor());
		if (isBomUsed.size()>0)
		{
			getBillUI().showErrorMessage("该BOM档案已被使用，不允许修改!");
			return;
		}
//        if((new_flag.toString()).equals("N") || (lock_flag.toString()).equals("Y")){
//            getBillUI().showErrorMessage("该单据不是最新的BOM版本，不可以修改!");
//            return;
//        }
        bvo = null;
        flag = 0;
        super.onBoEdit();
    }
    
    @SuppressWarnings("unchecked")
	protected void onBoDelete() throws Exception {
//        BomVO bom = (BomVO)getBillUI().getVOFromUI().getParentVO();
//        UFBoolean new_flag = new UFBoolean(bom.getNew_flag()==null?"":bom.getNew_flag().toString());
//        UFBoolean lock_flag = new UFBoolean(bom.getLock_flag()==null?"":bom.getLock_flag().toString());
//        
//        if((new_flag.toString()).equals("N") || (lock_flag.toString()).equals("Y")){
//            getBillUI().showErrorMessage("该单据不是最新的BOM版本，不可以删除!");
//            return;
//        }
//        super.onBoDelete();
    	//add by houcq  begin 20101013
    	/*BOM档案删除目前是只能删除最新标记，不是件最新标记的删除，但是这样的话，
    	 * 就容易把生产使用过的BOM删除掉，导致月末无法进行成本计算。
		 *在删除时，检查该配方有没有被没有删除标记的入库单引用过，如果引用过，无论是否有最新标记，
    	 * 均不允许删除，删除时提示：该BOM档案已被使用，不允许删除！。
         */
    	BomVO bom = (BomVO)getBillUI().getVOFromUI().getParentVO();
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	String sql="select pk_invbasdoc from eh_sc_cprkd_b   where isnull(dr,0)=0 and pk_invbasdoc='"+bom.getPk_invbasdoc()+"' and ver="+bom.getVer();
		ArrayList isBomUsed = (ArrayList) iUAPQueryBS.executeQuery(sql, new ArrayListProcessor());
		if (isBomUsed.size()>0)
		{
			getBillUI().showErrorMessage("该BOM档案已被使用，不允许删除!");
			return;
		}
		super.onBoDelete();
		//add by houcq end
		/*删除时，增加检查程序：
		1、判断当前删除的BOM单中的终止标记是否勾选，
		2、如勾选，则直接执行删除操作。
		3、如当前BOM单中的终止标记为空，即没有勾选，则记住当前的版本，在删除当前BOM单后，
		查找到当前版本上一个有效的版本的配方（如A物料有10个版本，当前要删除的版本为10，
		其中：版本789前期已经删除，则此时的有效的配方为版本6），报消该BOM单据的终止标记，使其成为最新的。
		*/
		UFBoolean lock_flag = new UFBoolean(bom.getLock_flag()==null?"":bom.getLock_flag().toString());
		if(lock_flag.toString().equals("N")){
			StringBuilder sb = new StringBuilder()
			.append(" update eh_bom set new_flag='Y',lock_flag='N'")
			.append(" where pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr, 0) = 0")
			.append(" and nvl(lock_flag, 'N') = 'Y' and pk_invbasdoc = '"+bom.getPk_invbasdoc()+"'")
			.append("  and ver=(select max(ver) from eh_bom")
			.append(" where pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr, 0) = 0")
			.append(" and nvl(lock_flag, 'N') = 'Y' and pk_invbasdoc = '"+bom.getPk_invbasdoc()+"'")
			.append(" and ver < "+bom.getVer()+")");
			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			pubItf.updateSQL(sb.toString());
		}
    	
    }
    
    @Override
    protected void onBoPrint() throws Exception {
    	nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()._getModuleCode(), 
        		            getBillUI()._getOperator(), getBillUI().getBusinessType(), getBillUI().getNodeKey());
        print.selectTemplate();
        ClientCardPanelPRTS.templetecode = print.getTemplate().getTemplateCode();			//选择的模板
        print.preview();
    }
     
}