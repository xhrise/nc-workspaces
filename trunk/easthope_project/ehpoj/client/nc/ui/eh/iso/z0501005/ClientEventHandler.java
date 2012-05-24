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
 * ˵����ԭ��������׼�� 
 * @author ����Դ 
 * ʱ�䣺2008-4-11
 */
public class ClientEventHandler extends AbstractEventHandler {
    
	Integer ver = -1;                                  // �ɰ汾
	AggregatedValueObject oldaggVO = null;             // �ɰ汾VO
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBoSave() throws Exception {
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        Integer ver2 = Integer.parseInt(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").getValueObject().toString());   // ����ʱ�İ汾

        int i = ver2-ver;

        //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_project"});
        if(res==1){
            getBillUI().showErrorMessage("�����Ŀ�Ѿ����ڣ������������");
            return;
        }         
       
       //����ʱ������ͷ�����Ƿ�ά���ֿ⣬��������ʾ�������������δά���ֿ⣬��ȷ�ϸ������Ƿ�����ʹ�ã���
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
        	getBillUI().showErrorMessage("���������δά���ֿ⣬��ȷ�ϸ������Ƿ�����ʹ��!");
            return;
        }
        
        //���°汾��ͬ��������ͬ��ʱ�򣬲������� add by zqy
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
            getBillUI().showErrorMessage("���±�Ǻ���������ͬ���������棬���飡");
            return;
        }       
        
        super.onBoSave();
        
        if(i==1){  //�汾�б��ʱ���ƾɰ汾
            IVOPersistence  iVOPersistence =   (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            IsoVO isoVO = (IsoVO)oldaggVO.getParentVO();
            isoVO.setDef_1("N");
            isoVO.setLock_flag(new UFBoolean(true));
            iVOPersistence.updateVO(isoVO);   // ��ͷ
        }
  
	  }
	
    @Override
	public String addCondtion() {
    	return " vbilltype = '"+IBillType.eh_z0501005+"'";
    }
	
	  //��ѯģ���м��ϲ�ѯ����Ϊ�Ƶ�һ����֮�ڵĵ���
    @Override
	protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
    	String billtype = getUIController().getBillType();           // ��������
        String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and NVL(dr,0)=0"; //ȡ�ڵ��
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
            case IEHButton.EditionChange:    //�汾���
            	onEditionChange();
                break;
            case IEHButton.LOCKBILL:    //�رյ���
                onBoLockBill();
                break;
            case IEHButton.Prev:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //��һҳ ��һҳ
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
	 * ˵�����汾���
	 * @author ����
	 * 2008-4-23 9:46:49
	 */
	private void onEditionChange() {
	 try {
		onBoRefresh();
		IsoVO isoVO = (IsoVO)getBillUI().getVOFromUI().getParentVO();
		String isLastedVersion = isoVO.getDef_1();  // �汾
		int vbillstatus = isoVO.getVbillstatus();
	    // ֻ�����°汾���ܱ��
		if(!isLastedVersion.equals("Y")){
			getBillUI().showErrorMessage("�������°汾����,�޷�����!");
			return;
		}
		//ֻ������ͨ������Ŀ���������
        if(vbillstatus!=1){
            getBillUI().showErrorMessage("ֻ������ͨ������Ŀ�����������");
            return;
        }
        int ok=getBillUI().showYesNoMessage("�Ƿ�ȷ�Ͻ�����Ŀ���?");
        if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
           
			ver = Integer.parseInt(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").getValueObject().toString());   // ��ǰ�汾
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
            getBillUI().showErrorMessage("�õ����Ѿ��ر�!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�����йرղ���?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivo.setDef_1("N");
                ivoPersistence.updateVO(ivo);
                getBillUI().showWarningMessage("�Ѿ��رճɹ�");
                onBoRefresh();
            }
            else{
                return;
            }
        }
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
          setBoEnabled();
    }
//    @Override
//    protected void onBoQuery() throws Exception {
//    	// TODO Auto-generated method stub
//    	super.onBoQuery();
//    }
//    
}