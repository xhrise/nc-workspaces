package nc.ui.eh.iso.z0501005;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.iso.z0501005.IsoVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 说明：原料质量标准单 
 * @author 张起源 
 * 时间：2008-4-11
 */
public class ClientEventHandler extends AbstractEventHandler {
    
	Integer ver = -1;                                  // 旧版本
	AggregatedValueObject oldaggVO = null;             // 旧版本VO
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBoSave() throws Exception {
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        Integer ver2 = Integer.parseInt(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").getValueObject().toString());   // 保存时的版本

        int i = ver2-ver;

        //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_project"});
        if(res==1){
            getBillUI().showErrorMessage("检测项目已经存在，不允许操作！");
            return;
        }         
       
       //保存时，检测表头物料是否维护仓库，如无则提示：‘存货管理档案未维护仓库，请确认该物料是否允许使用！’
        String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
        StringBuilder sb = new StringBuilder()
        .append(" select def1 from bd_invmandoc")
        .append(" where pk_corp='"+_getCorp().getPk_corp()+"'")
        .append(" and pk_invmandoc='"+pk_invbasdoc+"'")
        .append(" and nvl(dr,0)=0")
        .append(" and def1 is null");
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sb.toString(),new MapListProcessor());
        if (arr.size()>0)
        {
        	getBillUI().showErrorMessage("存货管理档案未维护仓库，请确认该物料是否允许使用!");
            return;
        }
        
        //最新版本相同和物料相同的时候，不允许保存 add by zqy
        //String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":
        //    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
        String def_1 = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_1").getValueObject()==null?"":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_1").getValueObject().toString();
        String pk_iso = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_iso").getValueObject()==null?"":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_iso").getValueObject().toString();
        String pk_corp = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_corp").getValueObject()==null?"":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_corp").getValueObject().toString();
        PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        int flag = pubItf.BackCheckiso(pk_invbasdoc, def_1,pk_iso,pk_corp);
        if(flag==1&&i!=1){
            getBillUI().showErrorMessage("最新标记和物料有相同，不允许保存，请检查！");
            return;
        }       
        
        super.onBoSave();
        
        if(i==1){  //版本有变更时复制旧版本
            IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            IsoVO isoVO = (IsoVO)oldaggVO.getParentVO();
            isoVO.setDef_1("N");
            isoVO.setLock_flag(new UFBoolean(true));
            iVOPersistence.updateVO(isoVO);   // 表头
        }
  
	  }
	
    @Override
	public String addCondtion() {
    	return " vbilltype = '"+IBillType.eh_z0501005+"'";
    }
	
	  //查询模板中加上查询条件为制单一个月之内的单据
    @Override
	protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
    	String billtype = getUIController().getBillType();           // 单据类型
        String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and NVL(dr,0)=0"; //取节点号
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
			String nodecode = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:
							iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
//            String lastdate = PubTools.getLastDate(_getDate().toString().substring(0, 7))+"-01";
	        dlg.setTempletID(_getCorp().getPk_corp(), nodecode, null, null); 
//	        dlg.setDefaultValue("dmakedate",lastdate,null);
	        dlg.setNormalShow(false);
        }catch (BusinessException e) {
			e.printStackTrace();
		}
        return dlg;
    }
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		switch (intBtn)
        {
            case IEHButton.EditionChange:    //版本变更
            	onEditionChange();
                break;
            case IEHButton.LOCKBILL:    //关闭单据
                onBoLockBill();
                break;
            case IEHButton.Prev:    //上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //上一页 下一页
                onBoBrows(intBtn);
                break;
         }   
	}
   
	@Override
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ver", 1);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_1", "Y");
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("lock_flag", "");
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
        getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);            
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(null);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setValue(null);
	}
	/**
	 * 说明：版本变更
	 * @author 王兵
	 * 2008-4-23 9:46:49
	 */
	private void onEditionChange() {
	 try {
		onBoRefresh();
		IsoVO isoVO = (IsoVO)getBillUI().getVOFromUI().getParentVO();
		String isLastedVersion = isoVO.getDef_1();  // 版本
		int vbillstatus = isoVO.getVbillstatus();
	    // 只有最新版本才能变更
		if(!isLastedVersion.equals("Y")){
			getBillUI().showErrorMessage("不是最新版本数据,无法更新!");
			return;
		}
		//只有审批通过的项目才允许调整
        if(vbillstatus!=1){
            getBillUI().showErrorMessage("只有审批通过的项目才允许调整！");
            return;
        }
        int ok=getBillUI().showYesNoMessage("是否确认进行项目变更?");
        if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
           
			ver = Integer.parseInt(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").getValueObject().toString());   // 当前版本
			oldaggVO = getBillUI().getVOFromUI();
			super.onBoCopy();
			String billNo = BillcodeRuleBO_Client.getBillCode(IBillType.eh_z0501005, _getCorp().getPrimaryKey(),null, null);
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ver", ver+1);
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_1", "Y");
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("billno", billNo);
			getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
            getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);            
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
         
            }
         } catch (Exception e) {
			e.printStackTrace();
		}			
	}

	@Override
	public void onBoCommit() throws Exception {
		super.onBoCommit();
		super.setBoEnabled();
	}
    
    @Override
	protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();        
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        if(vbillstatus==null){
            getButtonManager().getButton(IEHButton.EditionChange).setEnabled(false);            
        }else{
            getButtonManager().getButton(IEHButton.EditionChange).setEnabled(true);
        }
        getBillUI().updateButtonUI();
        super.setBoEnabled();
        
    }
    
    @Override
	protected void onBoLockBill() throws Exception{
//        SuperVO parentvo = (SuperVO)getBillUI().getChangedVOFromUI().getParentVO();
//        String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
//             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        IsoVO ivo = (IsoVO) aggvo.getParentVO();
        String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
        String primaryKey = ivo.getPrimaryKey();
        if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该单据已经关闭!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行关闭操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivo.setDef_1("N");
                ivoPersistence.updateVO(ivo);
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
//    @Override
//    protected void onBoQuery() throws Exception {
//    	// TODO Auto-generated method stub
//    	super.onBoQuery();
//    }
//    
}