
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
 * ���̿�����Ӫ������ά��
 * @author ����
 * �������� 2008-4-1 16:09:43
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
	            case IEHButton.Prev:    //��һҳ ��һҳ
	                onBoBrows(intBtn);
	                break;
	            case IEHButton.Next:    //��һҳ ��һҳ
	                onBoBrows(intBtn);
	                break;
                case IEHButton.LOCKBILL:    //ͣ�ñ��
                    onBoLockBill();
                    break;
                case IEHButton.THAW:    	//����
                    onBoThawBill();
                    break;
                case IEHButton.GENRENDETAIL:    	//Ӫ�����������޸�
                	onBoConGenal();
                    break;
                case IEHButton.DOCMANAGE:    	//Ƭ�������޸�
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
					getBillUI().showWarningMessage("��Ӫ���������Ӫ������Ϊͬһ��");
					return;
				}
				else
				{
					PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
			           try {
						   String pkss = PubTools.combinArrayToString(pks);
						   String sql = " update eh_custyxdb set pk_psndoc = '"+new_pk_psndoc+"' where pk_cubasdoc in "+pkss+"";
						   pubItf.updateSQL(sql);
						   getBillUI().showWarningMessage("�����޸�Ӫ������ɹ�");
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
						   getBillUI().showWarningMessage("�����޸�Ƭ���ɹ�");
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
	            getBillUI().showErrorMessage("�ÿͻ�δ��ͣ��!");
	            return;
	        }
	        else if(!primaryKey.equals("")){
	            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ���������ò���?");
	            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
	                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
	                cvo.setAttributeValue("lock_flag", new UFBoolean(false));
	                ivoPersistence.updateVO(cvo);
	                getBillUI().showWarningMessage("���óɹ�!");
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
            getBillUI().showErrorMessage("�ÿͻ��Ѿ�ͣ��!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ������ͣ�ò���?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                cvo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(cvo);
                getBillUI().showWarningMessage("ͣ�óɹ�!");
                onBoRefresh();
            }
            else{
                return;
            }
        }
        setBoEnabled();
    }
     
    private void onBoBrows(int intBtn) throws java.lang.Exception {
			// ����ִ��ǰ����
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
			// ����ִ�к���
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
																		 * "ת����:" +
																		 * getBufferData().getCurrentRow() +
																		 * "ҳ���)"
																		 */
							);
		}
	@SuppressWarnings("unchecked")
    @Override
	protected void onBoSave() throws Exception {
		//�յ��ж�
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel("kxl");
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("���������������ظ��������������");
            return;
        } 
        //��Ա���ж�
        BillModel yxdb=getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb");
        int count=new PubTools().uniqueCheck(yxdb, new String[]{"pk_psndoc"});
        if(count==1){
            getBillUI().showErrorMessage("�б���Ա�ظ��������������");
            return;
        }
//        BillModel cubinvbasdoc=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_cubinvbasdoc");
//        int count1=new PubTools().uniqueCheck(cubinvbasdoc, new String[]{"pk_invbasdoc"});
//        if(count1==1){
//            getBillUI().showErrorMessage("���ϵ����е����ϱ������ظ��������������");
//            return;
//        }
        //�ж�ֻ��һ����Ҫ����
        int row2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb").getRowCount();
        HashMap hm2=new HashMap();
        for(int i=0;i<row2;i++){
        	String ismain=getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb").getValueAt(i, "ismain")==null?"":
        		getBillCardPanelWrapper().getBillCardPanel().getBillModel("yxdb").getValueAt(i, "ismain").toString();
        	if(ismain.equals("true")){
        		if(hm2.containsKey("Y")){
        			getBillUI().showErrorMessage("�����ж����Ҫ�������ʵ");
        			return;
        		}else{
        			hm2.put("Y", "Ok");
        		}
        	}
        }
        if(!hm2.containsKey("Y")){
    		getBillUI().showErrorMessage("��ѡ����Ҫ����");
    		return;
    	}
        
        PubItf pubitf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        CustVO hvo = (CustVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String primary = hvo.getPrimaryKey()==null?" ":hvo.getPrimaryKey(); 
        if(primary==null||primary.length()!=20){
        	String sql=" pk_cubasdoc='"+hvo.getPk_cubasdoc()+"' and pk_cust!='"+primary+"'";
            int i=pubitf.BackCheck(hvo.getTableName(),sql);
            if(i==1){
                this.getBillUI().showErrorMessage("�����Ѿ����ڣ��������ظ���");
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
        int pos = sqlWhere.indexOf("��Ӧ��", 0);
        if(pos<=-1){
            sqlWhere = sqlWhere.replaceFirst("�ͻ�", "0");
        }else{
            sqlWhere = sqlWhere.replaceFirst("��Ӧ��", "1");
        }if(pos<0){
            sqlWhere = sqlWhere.replaceFirst("����", "2");
        }
        SuperVO[] queryVos = queryHeadVOs(sqlWhere);
        
        getBufferData().clear();
        // �������ݵ�Buffer
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
        //����ͣ�ð�ťʱ��ͣ�ð�ť�Ŀ��� 
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
    		getBillUI().showErrorMessage("����ѡ�����!");
    		return;
    	}else{
    		super.onBoLineAdd();
    		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
    		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_cubasdoc, row, "pk_cubasdoc");
    	}
    }
    
    /**
     * ��������������֪ͨ�������ú����ڸýڵ㲻�������ɾ��
     * add by zqy 2010��11��17��9:53:09
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
    		getBillUI().showErrorMessage("���ͻ��ѷ���ҵ��,���������ɾ��!");
    		return;
    	}else{
    		super.onBoDelete();
    	}
    }
    
    
    
	
}
