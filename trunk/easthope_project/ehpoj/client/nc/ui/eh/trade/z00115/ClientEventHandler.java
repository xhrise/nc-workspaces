
package nc.ui.eh.trade.z00115;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z00115.CubasdocVO;
import nc.vo.eh.trade.z00115.CustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 客商可销料营销代表维护
 * @author 王明
 * 创建日期 2008-4-1 16:09:43
 */

public class ClientEventHandler extends ManageEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	} 
	 @Override
	protected void onBoElse(int intBtn) throws Exception {
	        switch (intBtn)
	        {
	            case IEHButton.Prev:    //上一页 下一页
	                onBoBrows(intBtn);
	                break;
	            case IEHButton.Next:    //上一页 下一页
	                onBoBrows(intBtn);
	                break;
                case IEHButton.LOCKBILL:    //停用标记
                    onBoLockBill();
                    break;
                case IEHButton.THAW:    	//启用
                    onBoThawBill();
                    break;
                case IEHButton.GENRENDETAIL:    	//营销代表批量修改
                	onBoConGenal();
                    break;
                case IEHButton.DOCMANAGE:    	//片区批量修改
                	onBoAreaModify();
                    break;
	        }   
	    }
	 private void onBoConGenal() {
			CustPsnDlg dlg = new CustPsnDlg(getBillUI());
			if(dlg.showModal()== UIDialog.ID_OK){ 
				BillCardPanel dlgCardPanel = dlg.getBillCardPanel();
				String old_pk_psndoc= dlgCardPanel.getHeadItem("oldpsnpk").getValueObject().toString();
				String[] pks= dlg.getPks();
				String new_pk_psndoc= dlgCardPanel.getHeadItem("newpsnpk").getValueObject().toString();
				if (old_pk_psndoc.equals(new_pk_psndoc))
				{
					getBillUI().showWarningMessage("老营销代表和新营销代表为同一人");
					return;
				}
				else
				{
					PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
			           try {
						   String pkss = PubTools.combinArrayToString(pks);
						   String sql = " update eh_custyxdb set pk_psndoc = '"+new_pk_psndoc+"' where pk_cubasdoc in "+pkss+"";
						   pubItf.updateSQL(sql);
						   getBillUI().showWarningMessage("批量修改营销代表成功");
						   return;
			           }catch (Exception e1) {
						  e1.printStackTrace();
					    }
				}
			}
		}

		private void onBoAreaModify() {
			CustAreaDlg dlg = new CustAreaDlg(getBillUI());
			if(dlg.showModal()== UIDialog.ID_OK){ 
				BillCardPanel dlgCardPanel = dlg.getBillCardPanel();
				
				String[] pks= dlg.getPks();
				String newpk_areacl= dlgCardPanel.getHeadItem("newpk_areacl").getValueObject().toString();
				
				{
					PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
			           try {
						   String pkss = PubTools.combinArrayToString(pks);
						   String sql = " update bd_cumandoc set def5 = '"+newpk_areacl+"' where pk_cumandoc in "+pkss+"";
						   pubItf.updateSQL(sql);
						   getBillUI().showWarningMessage("批量修改片区成功");
						   return;
			           }catch (Exception e1) {
						  e1.printStackTrace();
					    }
				}
			}
		}
	 private void onBoThawBill() throws Exception {
	        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
	        CubasdocVO cvo = (CubasdocVO) aggvo.getParentVO();
	        String lock_flag = cvo.getLock_flag()==null?"N":cvo.getLock_flag().toString();
	        String primaryKey = cvo.getPrimaryKey();
	        if(lock_flag.equals("N")){
	            getBillUI().showErrorMessage("该客户未被停用!");
	            return;
	        }
	        else if(!primaryKey.equals("")){
	            int iRet = getBillUI().showYesNoMessage("是否确定进行启用操作?");
	            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
	                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
	                cvo.setAttributeValue("lock_flag", new UFBoolean(false));
	                ivoPersistence.updateVO(cvo);
	                getBillUI().showWarningMessage("启用成功!");
	                onBoRefresh();
	            }
	            else{
	                return;
	            }
	        }
	        setBoEnabled();
	    }
	 
	 private void onBoLockBill() throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        CubasdocVO cvo = (CubasdocVO) aggvo.getParentVO();
        String lock_flag = cvo.getLock_flag()==null?"N":cvo.getLock_flag().toString();
        String primaryKey = cvo.getPrimaryKey();
        if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该客户已经停用!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行停用操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                cvo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(cvo);
                getBillUI().showWarningMessage("停用成功!");
                onBoRefresh();
            }
            else{
                return;
            }
        }
        setBoEnabled();
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
		}
	@SuppressWarnings("unchecked")
    @Override
	protected void onBoSave() throws Exception {
		//空的判断
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel("kxl");
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("可销料中物料有重复，不允许操作！");
            return;
        } 
        //人员的判断
        BillModel yxdb=getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb");
        int count=new PubTools().uniqueCheck(yxdb, new String[]{"pk_psndoc"});
        if(count==1){
            getBillUI().showErrorMessage("列表人员重复，不允许操作！");
            return;
        }
//        BillModel cubinvbasdoc=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_cubinvbasdoc");
//        int count1=new PubTools().uniqueCheck(cubinvbasdoc, new String[]{"pk_invbasdoc"});
//        if(count1==1){
//            getBillUI().showErrorMessage("物料档案中的物料编码有重复，不允许操作！");
//            return;
//        }
        //判断只有一个主要代表
        int row2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb").getRowCount();
        HashMap hm2=new HashMap();
        for(int i=0;i<row2;i++){
        	String ismain=getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb").getValueAt(i, "ismain")==null?"":
        		getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb").getValueAt(i, "ismain").toString();
        	if(ismain.equals("true")){
        		if(hm2.containsKey("Y")){
        			getBillUI().showErrorMessage("不能有多个主要代表，请核实");
        			return;
        		}else{
        			hm2.put("Y", "Ok");
        		}
        	}
        }
        if(!hm2.containsKey("Y")){
    		getBillUI().showErrorMessage("请选择主要代表！");
    		return;
    	}
        
        PubItf pubitf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        CustVO hvo = (CustVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String primary = hvo.getPrimaryKey()==null?" ":hvo.getPrimaryKey(); 
        if(primary==null||primary.length()!=20){
        	String sql=" pk_cubasdoc='"+hvo.getPk_cubasdoc()+"' and pk_cust!='"+primary+"'";
            int i=pubitf.BackCheck(hvo.getTableName(),sql);
            if(i==1){
                this.getBillUI().showErrorMessage("客商已经存在，不允许重复！");
                return;
            }
        }
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", _getOperator());
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
        
		
		super.onBoSave();
		onBoRefresh();
     }
    
    @Override
	protected void onBoQuery() throws Exception {
        StringBuffer sbWhere = new StringBuffer();
        if(askForQueryCondition(sbWhere)==false) 
            return;

        String sqlWhere = sbWhere.toString();
        int pos = sqlWhere.indexOf("供应商", 0);
        if(pos<=-1){
            sqlWhere = sqlWhere.replaceFirst("客户", "0");
        }else{
            sqlWhere = sqlWhere.replaceFirst("供应商", "1");
        }if(pos<0){
            sqlWhere = sqlWhere.replaceFirst("客商", "2");
        }
        SuperVO[] queryVos = queryHeadVOs(sqlWhere);
        
        getBufferData().clear();
        // 增加数据到Buffer
        addDataToBuffer(queryVos);

        updateBuffer();
    }
    
    private void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        if(getButtonManager().getButton(IEHButton.Prev)!=null){
            if(!getBufferData().hasPrev()){
                getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
            }
            else{
                getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
            }
            if(!getBufferData().hasNext()){
                getButtonManager().getButton(IEHButton.Next).setEnabled(false);
            }
            else{
                getButtonManager().getButton(IEHButton.Next).setEnabled(true);
            }
        }
        //在有停用按钮时对停用按钮的控制 
        String[] keys = aggvo.getParentVO().getAttributeNames();
        if(keys!=null && keys.length>0){
            for(int i=0;i<keys.length;i++){
                if(keys[i].endsWith("lock_flag")){ 
                    String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"false":
                        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                    if(lock_flag.equals("false")){
                        getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                        getButtonManager().getButton(IEHButton.THAW).setEnabled(false);
                    }else{
                        getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
                        getButtonManager().getButton(IEHButton.THAW).setEnabled(true);
                    }
                    break;
                }
                   
            }
        }
        getBillUI().updateButtonUI();
    }

    @Override
    protected void onBoCard() throws Exception {
    	super.onBoCard();
    	setBoEnabled();
    }
    
    @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	setBoEnabled();
    }
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
    }
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	CustVO hvo = (CustVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_cubasdoc = hvo.getPk_cubasdoc()==null?null:hvo.getPk_cubasdoc();
    	if(pk_cubasdoc==null||pk_cubasdoc.length()==0){
    		getBillUI().showErrorMessage("请先选择客商!");
    		return;
    	}else{
    		super.onBoLineAdd();
    		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_cubasdoc, row, "pk_cubasdoc");
    	}
    }
    
    /**
     * 如果可销料在提货通知单被引用后，则在该节点不允许进行删除
     * add by zqy 2010年11月17日9:53:09
     */
    @SuppressWarnings("unchecked")
	protected void onBoDelete() throws Exception {
    	String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
    	String sql = " select * from eh_ladingbill where pk_cubasdoc = '"+pk_cubasdoc+"' " +
    			" and pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(dr,0)=0 ";
    	
    	IUAPQueryBS iUAPQUeryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList arr = (ArrayList)iUAPQUeryBS.executeQuery(sql,new MapListProcessor());
    	if(arr!=null && arr.size()>0){
    		getBillUI().showErrorMessage("本客户已发生业务,不允许进行删除!");
    		return;
    	}else{
    		super.onBoDelete();
    	}
    }
    
    
    
	
}
