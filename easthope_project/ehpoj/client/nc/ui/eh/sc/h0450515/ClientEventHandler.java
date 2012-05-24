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
 * ˵����BOM���� 
 * @author ����Դ 
 * ʱ�䣺2008-5-06
 */
public class ClientEventHandler extends AbstractEventHandler {
	private BomVO bvo=null;
    int flag = 0 ;				//���Ѿ��д�����BOMʱ��������һ���ϵ�BOM,�ڱ���ʱ�轫ԭ�ϵ�����BOM���±��ȥ����flag = 1����ʱ����У��
	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    
	@SuppressWarnings("deprecation")
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			bo.removeChildButton(bo.getChildButtonGroup()[0]);	//ȥ����ֹ���ݰ�ť
			
			ButtonObject boUncheck = new ButtonObject("ȡ����ǰ����");
			boUncheck.setTag(String.valueOf(IEHButton.UnCheck));
			boUncheck.setCode("ȡ����ǰ����");
			bo.addChildButton(boUncheck);
			
			ButtonObject boPrevedition = new ButtonObject("��һҳ");
			boPrevedition.setTag(String.valueOf(IEHButton.Prev));
			boPrevedition.setCode("��һҳ");
			bo.addChildButton(boPrevedition);
			
			ButtonObject boNextedition = new ButtonObject("��һҳ");
			boNextedition.setTag(String.valueOf(IEHButton.Next));
			boNextedition.setCode("��һҳ");
			bo.addChildButton(boNextedition);
		}
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.EditionChange: 			//�汾���
			setEditionChange();
			break;
        case IEHButton.LOCKBILL:    			//�رյ���
            onBoLockBill();
            break;
        case IEHButton.Prev:    				//��һҳ ��һҳ
            onBoBrows(intBtn);
            break;
        case IEHButton.Next:   					 //��һҳ ��һҳ
            onBoBrows(intBtn);
            break;
        case IEHButton.CONFIRMBUG:    			 //����
            onBoElseCopy(intBtn);
            break;
        case IEHButton.prevedition:               //��һ�汾
            onBoprevedition(intBtn);
            break;
        case IEHButton.nextedition:               //��һ�汾
            onBonextedition(intBtn);
            break;    
        case IEHButton.ConfirmSC:               //ȷ������
            onBoConfirmSC(intBtn);
            break; 
        case IEHButton.UnCheck:               	//ȡ������
            onBoCancelSC(intBtn);
            break;
		}
	}
	
	/***
	 * ȡ������ 
	 * 2009-2-23 16:10:34
	 * @throws Exception 
	 */
	private void onBoCancelSC(int intBtn) throws Exception{
		String invname = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject()==null?"":
 			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject().toString();
		int iRet = getBillUI().showYesNoMessage("�Ƿ�ȡ�������䷽���� "+invname+" ��Ʒ?");
		if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
			 BomVO hvo = (BomVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
			 hvo.setSc_flag(new UFBoolean(false));
			 IVOPersistence ivopersistence=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			 ivopersistence.updateVO(hvo);
			 onBoRefresh();
		}
	}

	/***
	 * ȷ������ 
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
    		getBillUI().showErrorMessage("�������ѷ��,������ʹ��,��˶�!");
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
    		getBillUI().showWarningMessage(tips.toString()+"���������ѷ���û��ά���ֿ�,��˶�!");
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
    		getBillUI().showWarningMessage("��"+tips2.toString().substring(0,tips2.toString().length()-1)+"�вֿⲻ��Ϊ��!");
    		return;
    	}
    	//add by houcq 2011-09-28 end
    	 String invname = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject()==null?"":
    		 			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vinvname").getValueObject().toString();
    	 int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�������䷽���� "+invname+" ��Ʒ?");
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

	//��һ�汾 add by zqy 2008-10-6 15:09:56
	@SuppressWarnings("unchecked")
	private void onBonextedition(int intBtn) throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        BomVO bvo = (BomVO) aggvo.getParentVO();
        String pk_invbasdoc = bvo.getPk_invbasdoc()==null?"":bvo.getPk_invbasdoc().toString();//��������
        Integer ver = new Integer(bvo.getVer()==null?"":bvo.getVer().toString());//�汾��
        Integer ver2 = ver+1;
        String sql = " select * from eh_bom where pk_invbasdoc='"+pk_invbasdoc+"' and ver='"+ver2+"' and NVL(dr,0)=0 ";
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        if(arr.size()==0){
            getBillUI().showErrorMessage("������û����һ�汾,�˰汾��Ϊ��߰汾!");
            return;
        }else{
            //��������ʾ������
            String whereSql = " pk_invbasdoc = '"+pk_invbasdoc+"' and ver='"+ver2+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
            nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
            SuperVO[] supervo = business.queryByCondition(BomVO.class, whereSql);
            getBufferData().clear();
           // �������ݵ�Buffer
           addDataToBuffer(supervo);
           updateBuffer();
        }
    
    }

    //��һ�汾 add by zqy 2008-10-6 15:08:15
    private void onBoprevedition(int intBtn) throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        BomVO bvo = (BomVO) aggvo.getParentVO();
        String pk_invbasdoc = bvo.getPk_invbasdoc()==null?"":bvo.getPk_invbasdoc().toString();//��������
        Integer ver = new Integer(bvo.getVer()==null?"":bvo.getVer().toString());//�汾��
        if(ver.compareTo(1)==0){
            getBillUI().showErrorMessage("������û����һ�汾,�˰汾��Ϊ��Ͱ汾!");
            return;
        }else{
            Integer ver2 = ver-1;
            //��������ʾ������
            String whereSql = " pk_invbasdoc = '"+pk_invbasdoc+"' and ver='"+ver2+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
            nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
            SuperVO[] supervo = business.queryByCondition(BomVO.class, whereSql);
            getBufferData().clear();
           // �������ݵ�Buffer
           addDataToBuffer(supervo);
           updateBuffer();
        }
        
    }

    //���Ѿ��д�����BOMʱ��������һ���ϵ�BOM,�ڱ���ʱ�轫ԭ�ϵ�����BOM���±��ȥ��������ʱ����У�� add by wb at 2008-8-19 13:54:52 
	private void onBoElseCopy(int intBtn) throws Exception{
		onBoRefresh();
		onBoCopy();
		flag = 1;
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sc_flag",null);
	}

	//�汾���
	private void setEditionChange() throws Exception{
		onBoRefresh();
		AggregatedValueObject aggVO = getBillUI().getChangedVOFromUI();
		if(aggVO==null){
			getBillUI().showErrorMessage("ȡԴ���ݷ����쳣,���ѯ�����³���!");
            return;
		}
//add by houcq 2011-10-24ȡ��ֻ�����°汾�������汾���
//		Object new_flag= aggVO.getParentVO().getAttributeValue("new_flag");
//		if(new_flag!=null && new_flag.toString().equals("N")){
//			getBillUI().showErrorMessage("����µĲ������°汾�������Ը���");
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
    		getBillUI().showErrorMessage("�������Ƿ������,���ܱ���,��˶�!");
            return;
    	}
		lb_add = isAdding();
        if(aggVO==null){
            getBillUI().showErrorMessage("ȡԴ���ݷ����쳣,���ѯ�����³���!");
            return;
        }
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//ǰ̨У��
		BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("�������ظ���");
            return;
        }
        
        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        if (model != null) {
            int rowCount = model.getRowCount();
            if (rowCount < 1) {
                NCOptionPane.showMessageDialog(getBillUI(), "�����в���Ϊ��!");
                return;
            }
        }
        
        
        //�жϸ������Ƿ��Ѿ�¼��������ϵͳ���Ѿ�������BOM�������������
        //add by newyear at 2008-5-23 
        if (isAdding()&&flag!=1&&bvo==null){
            String pk_invbasdoc = aggVO.getParentVO().getAttributeValue("pk_invbasdoc").toString();
            String sql="select * from eh_bom  where pk_invbasdoc='"+pk_invbasdoc+"' and NVL(dr,0)=0";
            BomVO bomvo = (BomVO) iUAPQueryBS.executeQuery(sql, new BeanProcessor(BomVO.class));
            if(bomvo!=null){
            	if (getBillUI().showYesNoMessage("����������BOM���Ƿ�ȷ�ϱ���?")!=4)
            	{
            		onBoCancel();
            		return;
            	}            	
            }
        }   
        // ���Ѿ��д�����BOMʱ��������һ���ϵ�BOM,�ڱ���ʱ�轫ԭ�ϵ�����BOM���±��ȥ��������ʱ����У�� add by wb at 2008-8-19 13:54:52 
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
        		getBillUI().showWarningMessage("��"+(i+1)+"������������zamount�ֶβ���Ϊ0���");
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
    		getBillUI().showWarningMessage(tips2.toString()+"���������Ƿ�����ϻ�û��ά���ֿ�,��˶�!");
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
     * ���ܣ���ѯ�Ի�����ʾ
     */
	 private QueryConditionClient dlg = null;
     protected QueryConditionClient getQueryDLG()
     {        
         if(dlg == null){
             dlg = createQueryDLG();
         }
         return dlg;
     }
	
    //��ѯģ����ѡ��Ϊ���±��
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
             
             //��BOM��ѯģ���м����������������������������ѯ���� add by zqy 2008-9-27 13:29:36
             ConditionVO[] invtype  = getQueryDLG().getConditionVOsByFieldCode("invtype");
             ConditionVO[] name  = getQueryDLG().getConditionVOsByFieldCode("invname");
             ConditionVO[] wlpk_invbasdc  = getQueryDLG().getConditionVOsByFieldCode("pk_invbasdoc");
            // ConditionVO[] new_flag  = getQueryDLG().getConditionVOsByFieldCode("new_flag");//���±��
             ConditionVO[] sc_flag  = getQueryDLG().getConditionVOsByFieldCode("sc_flag");//�������
             ConditionVO[] ver  = getQueryDLG().getConditionVOsByFieldCode("ver");//�汾��
             ConditionVO[] coperatorid  = getQueryDLG().getConditionVOsByFieldCode("coperatorid");//�Ƶ���
             ConditionVO[] dmakedate  = getQueryDLG().getConditionVOsByFieldCode("dmakedate");//�Ƶ�����            
             ConditionVO[] zbno  = getQueryDLG().getConditionVOsByFieldCode("zbno");//�ܲ����
             String invclasscode = null;//���Ϸ������
             String invname = null;//��������
             String pk_invbasdoc=null;//����PK 
             @SuppressWarnings("unused")
			String invcode=null;
             @SuppressWarnings("unused")
			HashMap hm = getAllcode();
             StringBuffer wljbsql = new StringBuffer();			//���ϻ�������SQL
             StringBuffer wlglsql = new StringBuffer();			//���Ϲ�����SQL
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
             // �������ݵ�Buffer
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
             getBillUI().showErrorMessage("�õ����Ѿ��ر�!");
             return;
         }
         else if(!primaryKey.equals("")){
             int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�����йرղ���?");
             if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	 getBillCardPanelWrapper().getBillCardPanel().setHeadItem("new_flag", new UFBoolean(false));
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                bvo.setAttributeValue("lock_flag", new UFBoolean(true));
                bvo.setNew_flag(new UFBoolean(false));
                ivoPersistence.updateVO(bvo);
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

    
     
//   ���ð�ť�Ŀ���״̬
     protected void setBoEnabled() throws Exception {
         AggregatedValueObject aggvo=getBillUI().getVOFromUI();
         String pk_bom = aggvo.getParentVO().getPrimaryKey();
         if (pk_bom==null){
         }
         else{   
             //��һҳ ��һҳ�İ�ť״̬  add by wb at 2008-6-20 14:30:23
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
			getBillUI().showErrorMessage("��BOM�����ѱ�ʹ�ã��������޸�!");
			return;
		}
//        if((new_flag.toString()).equals("N") || (lock_flag.toString()).equals("Y")){
//            getBillUI().showErrorMessage("�õ��ݲ������µ�BOM�汾���������޸�!");
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
//            getBillUI().showErrorMessage("�õ��ݲ������µ�BOM�汾��������ɾ��!");
//            return;
//        }
//        super.onBoDelete();
    	//add by houcq  begin 20101013
    	/*BOM����ɾ��Ŀǰ��ֻ��ɾ�����±�ǣ����Ǽ����±�ǵ�ɾ�������������Ļ���
    	 * �����װ�����ʹ�ù���BOMɾ������������ĩ�޷����гɱ����㡣
		 *��ɾ��ʱ�������䷽��û�б�û��ɾ����ǵ���ⵥ���ù���������ù��������Ƿ������±�ǣ�
    	 * ��������ɾ����ɾ��ʱ��ʾ����BOM�����ѱ�ʹ�ã�������ɾ������
         */
    	BomVO bom = (BomVO)getBillUI().getVOFromUI().getParentVO();
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	String sql="select pk_invbasdoc from eh_sc_cprkd_b   where isnull(dr,0)=0 and pk_invbasdoc='"+bom.getPk_invbasdoc()+"' and ver="+bom.getVer();
		ArrayList isBomUsed = (ArrayList) iUAPQueryBS.executeQuery(sql, new ArrayListProcessor());
		if (isBomUsed.size()>0)
		{
			getBillUI().showErrorMessage("��BOM�����ѱ�ʹ�ã�������ɾ��!");
			return;
		}
		super.onBoDelete();
		//add by houcq end
		/*ɾ��ʱ�����Ӽ�����
		1���жϵ�ǰɾ����BOM���е���ֹ����Ƿ�ѡ��
		2���繴ѡ����ֱ��ִ��ɾ��������
		3���統ǰBOM���е���ֹ���Ϊ�գ���û�й�ѡ�����ס��ǰ�İ汾����ɾ����ǰBOM����
		���ҵ���ǰ�汾��һ����Ч�İ汾���䷽����A������10���汾����ǰҪɾ���İ汾Ϊ10��
		���У��汾789ǰ���Ѿ�ɾ�������ʱ����Ч���䷽Ϊ�汾6����������BOM���ݵ���ֹ��ǣ�ʹ���Ϊ���µġ�
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
        ClientCardPanelPRTS.templetecode = print.getTemplate().getTemplateCode();			//ѡ���ģ��
        print.preview();
    }
     
}