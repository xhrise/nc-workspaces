package nc.ui.eh.sc.h0450705;

/**
 * MRP����
 * ZB32
 * @author wangbing
 * 2008-12-20 16:18:55
 */

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.eh.sc.h0450705.ScMrpVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.MessageinfoVO;

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		ScMrpVO hvo = (ScMrpVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		//�Ƿ񳬹������ж�
		ScMrpBVO[] bvos = (ScMrpBVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").getBodyValueVOs(getUIController().getBillVoName()[2]);
		//add by houcq 2011-11-11 begin
		for (int i=0;i<bvos.length;i++)
		{
			if(bvos[i].getBcamount().toDouble()==0)
			{
				getBillUI().showErrorMessage("��������������Ϊ��!");
				return;
			}
		}
		//add by hucq 2011-11-11 end
		UFDouble maxamount = isMaxCN(hvo,bvos);
		if(maxamount.toDouble()>0){
			getBillUI().showErrorMessage("���հ��������������ղ���,����"+maxamount+"��,���ܱ���! ��Ա������������е���!");
			return;
		}
		//ԭ���Ƿ�����
		ScMrpCVO[] cvos = (ScMrpCVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_c").getBodyValueVOs(getUIController().getBillVoName()[3]);
		String strMessage = isAllamountYL(cvos);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("notenough_flag").setValue("N");
		if(strMessage!=null&&strMessage.length()>0){
			if (getBillUI().showYesNoMessage("������ǰ��Ʒ����ԭ�Ͽ�治��,��鿴<ԭ����ϸ>ҳǩ.\r\n�Ƿ��������?") != UIDialog.ID_YES) {
	            return;
	        }
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("notenough_flag").setValue("Y");
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("notenoughmsg").setValue(strMessage);
		}
     	super.onBoSave_withBillno();
     	String pk_mrp = hvo.getPk_mrp();
     	StringBuilder sb = new StringBuilder()
     	.append(" select sum(sla)-sum(slb) xl from (")
     	.append(" select count(*) slA,0  slB  from eh_sc_mrp_b where pk_mrp='"+pk_mrp+"' and sc_flag='Y'")
     	.append(" union all")
     	.append(" select 0  slA,count(*) slB  from eh_sc_mrp_b where pk_mrp='"+pk_mrp+"' and nvl(sc_flag,'N')='N')")
     	.append(" having sum(sla)-sum(slb)=0");
     	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object obj = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor());
		if (obj!=null)
		{
			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String usql="update eh_sc_mrp set sc_flag='Y' where pk_mrp='"+pk_mrp+"'";
			pubitf.updateSQL(usql);
		}
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void onBoCommit() throws Exception {
		super.onBoCommit();
		ScMrpVO hvo = (ScMrpVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		boolean notenough_flag = hvo.getNotenough_flag()==null?false:hvo.getNotenough_flag().booleanValue();
		if(notenough_flag){			//���ύʱ��ԭ�ϲ�����ϸ��Ϣ���͸� ��������Ʒ�ܾ����ܾ���
			String pk_corp = _getCorp().getPk_corp();
			String strMessage = hvo.getNotenoughmsg();
			StringBuffer sql = new StringBuffer()
			.append("  SELECT a.cuserid")
			.append("  FROM sm_user_role a,sm_role b")
			.append("  WHERE a.pk_role = b.pk_role")
			.append("  AND b.role_name IN ('��������','Ʒ�ܾ���','�ܾ���')")
			.append("  AND a.pk_corp = '"+pk_corp+"'")
			.append("  AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0");
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				ClientEnvironment ce = ClientEnvironment.getInstance();
				MessageinfoVO[] infos = new MessageinfoVO[arr.size()];
				for(int i=0;i<arr.size();i++){
					MessageinfoVO info = new MessageinfoVO();
					HashMap hm = (HashMap)arr.get(i);
					String cuserid = hm.get("cuserid")==null?null:hm.get("cuserid").toString();
					info.setSenddate(ce.getServerTime());
					info.setSenderman(_getOperator());
					info.setCheckman(cuserid);
					info.setBillno(hvo.getBillno());
					info.setTitle("MRP�����е���:"+hvo.getBillno()+"��ԭ�ϲ�������!");
					info.setContent(strMessage);
					info.setPk_corp(pk_corp);
					info.setPriority(1);
					info.setType(1);
					info.setDr(0);
					info.setState(0);
					
					infos[i] = info;
				}
				IVOPersistence iVO =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				iVO.insertVOArray(infos);
			}
		}
	}
	/***
	 * �Ƿ񳬹������ж�
	 * wb 2008-12-22 10:16:13
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public UFDouble isMaxCN(ScMrpVO hvo,ScMrpBVO[] bvos) throws Exception{
		UFDouble maxammount = new UFDouble(0);
		//�����Ѱ���������
		StringBuffer sql =  new StringBuffer()
		.append(" SELECT SUM(ISNULL(b.bcamount,0)) amount")
		.append(" FROM eh_sc_mrp a,eh_sc_mrp_b b")
		.append(" WHERE a.pk_mrp = b.pk_mrp")
		.append(" AND a.calcdate = '"+_getDate()+"' and a.pk_mrp <> '"+hvo.getPk_mrp()+"'")
		//.append(" --AND a.vbillstatus = 1")
		.append(" AND a.pk_corp = '"+_getCorp().getPk_corp()+"'")
		.append(" AND ISNULL(a.dr,0)=0")
		.append(" AND ISNULL(b.dr,0)=0");
		//�ղ���
		String cnsql = "SELECT maxamount FROM eh_sc_cn WHERE ISNULL(dr,0)=0 AND pk_corp = '"+_getCorp().getPk_corp()+"'";
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object objamount = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		Object objMaxamount = iUAPQueryBS.executeQuery(cnsql, new ColumnProcessor());
		UFDouble amountIndata = new UFDouble(0);				//�����Ѱ���������
		UFDouble currentAmount = new UFDouble(0);				//��ǰ������������
		UFDouble cnamount = new UFDouble(0);					//��������
		amountIndata = new UFDouble(objamount==null?"0":objamount.toString());
		cnamount = new UFDouble(objMaxamount==null?"0":objMaxamount.toString());
		if(bvos!=null&&bvos.length>0){
			for(int i=0;i<bvos.length;i++){
				ScMrpBVO bvo = bvos[i];
				currentAmount = currentAmount.add(bvo.getBcamount());
			}
		}
		maxammount = amountIndata.add(currentAmount).sub(cnamount);  //�Ѱ���������+��ǰ����������-��������
		return new UFDouble(maxammount.toString(),2);
	}
    
	/***
	 * ԭ�������ж�
	 * @param cvos
	 * @return
	 */
	public String isAllamountYL(ScMrpCVO[] cvos){
		StringBuffer str = new StringBuffer();
		if(cvos!=null&&cvos.length>0){
		 try {
			for(int i=0;i<cvos.length;i++){
				ScMrpCVO cvo = cvos[i];
				UFDouble cy = cvo.getCy();				//���� ���-����
				if(cy.toDouble()<0){
					str.append(" ��"+(i+1)+"�в�ࣺ"+cy+"\r\n");
					break;
				}
			}
		  } catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str.toString();
	}
	
//    @Override
//    protected void onBoPrint() throws Exception {
//        nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
//                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
//        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
//                dataSource);
//        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
//                ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
//                .getBusinessType(), getBillUI().getNodeKey());
//        print.selectTemplate();
//        print.preview();
//    }
   
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
//    	//���޸�ʱ�Բ��谲�������������� ������������ �ֶβ����Ա༭
//    	ScMrpBVO[] bvos = (ScMrpBVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").getBodyValueVOs(getUIController().getBillVoName()[2]);
//    	if(bvos!=null&&bvos.length>0){
//			for(int i=0;i<bvos.length;i++){
//				ScMrpBVO bvo = bvos[i];
//				boolean sc_flag = bvo.getSc_flag()==null?false:bvo.getSc_flag().booleanValue();
//				if(sc_flag){
//					getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setCellEditable(i, "bcamount", false);
//				}
//			}
//    	}
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void onButton_N(ButtonObject bo, BillModel model) {
    	super.onButton_N(bo, model);
    	 String bocode=bo.getCode();
         if(bocode.equals("���۶���")){
           int row=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").getRowCount();
           HashMap hm = new PubTools().getInvSafeKC(null);		//��ȫ����� 
           for(int i=0;i<row;i++){
               getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setCellEditable(i,"vinvcode", false);
               String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
               							getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
               UFDouble safekc =  new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
               UFDouble scamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "bcamount")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "bcamount").toString());		//������������ = ��������-��������
               UFDouble truekc = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount").toString());			//ʵ�ʿ��
               UFDouble scrwamount = scamount.sub(truekc).add(safekc);									//������������=������������+��ȫ�����-ʵ�ʿ����
               //���������������<�㣬������������ȡ�㲻�������������޸ģ�������������������㣬������������ȡ�����������������޸�
               if(scrwamount.toDouble()<0){
            	   	scamount = new UFDouble(0);
               		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("����Ҫ��������", i, "memo");
//               		getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt("����Ҫ��������", i, "memo");
               		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "sc_flag");
//               		getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_mrp_b").setCellEditable(i, "bcamount", false);
               }else{
            	   scamount = scrwamount;
               }
               getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(safekc, i, "safekc");
               getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scamount, i, "bcamount");
           }
//           getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
//           getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
//           getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
           getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
//           getBillUI().updateUI();
         }
    }
    
    
    
    @Override
    protected void onBoLineDel() throws Exception {
    	//��ԭ������ҳǩʱ������ɾ��
    	String currentModel = getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode();
    	if(currentModel.equals("eh_sc_mrp_c")){
    		getBillUI().showErrorMessage("ԭ����ϸ������ɾ�в���!");
    		return;
    	}
    	super.onBoLineDel();
    }
}
