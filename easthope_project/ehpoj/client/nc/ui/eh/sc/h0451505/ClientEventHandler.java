
package nc.ui.eh.sc.h0451505;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0451505.ScPgdVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * ����˵�����ɹ���
 * @author ����
 * 2008-5-7 11:36:45
 */

public class ClientEventHandler extends AbstractEventHandler {
    
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
    
     @Override
	protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn)
            {
                case IEHButton.SENDFA:    //�´�
                    onBoSendFa();
                    break;
            }   
            super.onBoElse(intBtn);
        }
     
        
        
		private void onBoSendFa() throws Exception {
			String xdflag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xdflag").getValueObject()==null?"N":
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("xdflag").getValueObject().toString();
			String lockflag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lockflag").getValueObject()==null?"N":
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lockflag").getValueObject().toString();
			try {
				ScPgdVO pgdVO = (ScPgdVO)getBillUI().getChangedVOFromUI().getParentVO();
				if(pgdVO==null){
					getBillUI().showErrorMessage("��ѡ��һ�ŵ���!");
			       return;
				}
				if(lockflag.equals("true")){
				       getBillUI().showErrorMessage("�õ����Ѿ��ر�,�����´�!");
				       return;
				}
				if(xdflag.equals("true")){
			       getBillUI().showErrorMessage("�õ����Ѿ��´�!");
			       return;
			    }
			   else if(pgdVO!=null){
			       int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ���´�˼ƻ���?");
			       if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
			    	    pgdVO.setXdflag(new UFBoolean(true));
				       	IVOPersistence ivopersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				       	ivopersistence.updateVO(pgdVO);
				       	//���´��ʱ��ر������������� add by wb at 2008-9-3 11:20:44
				       	String vsourcebillid = pgdVO.getVsourcebillid();
				       	if(vsourcebillid!=null&&vsourcebillid.length()>0){
					       	String pk_pgd = pgdVO.getPk_pgd();
					       	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
					       	pubItf.closePosmBill(pk_pgd);
				       	}
				       	getBillUI().showWarningMessage("�Ѿ��´�ɹ�");
				        onBoRefresh();
			       }
			   }
			} catch (Exception e) {
				e.printStackTrace();
			}
			setBoEnabled();
	   }
        
        @Override
		public void onBoSave() throws Exception {
    		//�ǿ��ж�
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
            //�������Ψһ��У��
            BillModel bm1=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b");
            int res1=new PubTools().uniqueCheck(bm1, new String[]{"pk_invbasdoc"});
            String vsourcebillid = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?null:
            		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
            if(vsourcebillid==null&&res1==1){    // ����ʱ�ж��Ƿ���ͬһ����
                getBillUI().showErrorMessage("�ӱ�������ͬ���ϣ������������");
                return;
            }        
            BillModel bm2=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_psn");
            int res2=new PubTools().uniqueCheck(bm2, new String[]{"pk_psndoc"});
            if(res2==1){
                getBillUI().showErrorMessage("������Ա�ӱ�������ͬ��Ա�������������");
                return;
            } 
            ScPgdVO hvo = (ScPgdVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
            boolean cd_flag = hvo.getCd_flag()==null?false:hvo.getCd_flag().booleanValue();
            if(!cd_flag){
            	if (getBillUI().showYesNoMessage("�üƻ������ǲ嵥,�Ƿ��������õ���?") != UIDialog.ID_YES) {
                    return;
                }
            }
            int ret= getBillUI().getBillOperate();
            //�༭����ʱ������
			if (ret==3)
			{
				if(vsourcebillid!=null&&vsourcebillid.length()>0){		
		   			//�����κ�ʹ�ô������,�����sc_flag���,ʹ�ô���+1
		    			String updateSql = " update eh_sc_mrp set ysycs = nvl(ysycs,0)+1 where pk_mrp in ("+vsourcebillid+")";
		    			String updateSql2 = " update eh_sc_mrp set sc_flag = 'Y' where pk_mrp in ("+vsourcebillid+") and nvl(bc,1)=nvl(ysycs,0)" ;
		    			PubItf pubItf2 = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		    			pubItf2.updateSQL(updateSql);
		    			pubItf2.updateSQL(updateSql2);
		    		}
		            setBoEnabled();
			}
			super.onBoSave();
    	}
        

        @Override
        protected void onBoDelete() throws Exception {
        	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
        	if(res==0){
        		return;
        	}
        	ScPgdVO hvo = (ScPgdVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
    		String vsourcebillid = hvo.getVsourcebillid()==null?null:hvo.getVsourcebillid().toString();
    		if(vsourcebillid!=null&&vsourcebillid.length()>0){		//�ر�������������
//    			String updateSql = " update eh_sc_posm set pg_flag = 'N' where pk_posm in ("+vsourcebillid+")";
//    			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//    			pubItf.updateSQL(updateSql);
//    			
    			String updateSql2 = " update eh_sc_mrp set sc_flag = 'N' where pk_mrp in ("+vsourcebillid+")";
    			PubItf pubItf2 = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
    			pubItf2.updateSQL(updateSql2);
    		}
        	super.onBoTrueDelete();
        	
    	}
        
        @Override
		protected void onBoLineAdd() throws Exception {
        	super.onBoLineAdd();
        	String currentcode = getBillCardPanelWrapper().getBillCardPanel().getCurrentBodyTableCode();
        	if(currentcode!=null&&("eh_sc_pgd_b".equals(currentcode))){
	        	int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
	        	String starttime = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("starttime").getValueObject()
	        					==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("starttime").getValueObject().toString();
	        	String endtime = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("endtime").getValueObject()
	        					==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("endtime").getValueObject().toString();
	        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(starttime, row, "starttime");
	        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(endtime, row, "endtime");
	        	
	        	/***����Ĭ�ϻ���  wb 2009��4��1��10:59:023**/
	        	String[] devices = ((ClientUI)getBillUI()).getDefaultdevice();
	        	if(devices!=null&&devices.length==2){
	        		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(devices[0], row, "vdevicename");
	        		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(devices[1], row, "def_1");
	        	}
	        	/*************** end *******************/
        	}
        }
    	
    	@Override
		public void onButton_N(ButtonObject bo, BillModel model) {
            
            super.onButton_N(bo, model);
            String bocode=bo.getCode()==null?"":bo.getCode();
//            String pk_corp = _getCorp().getPk_corp();
            //���ӳ��������ɼ�ⱨ�浥ʱ�򣬳��������ǲ�����༭��ͬʱ���岻�ܽ����в���
            if(bocode.equals("��������")){
              int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
              for(int i=0;i<row;i++){
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vinvbascode", false);
                  getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vhjl", false);
//                  String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
//                	  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
//                  UFDouble kcamount = new PubTools().getKCamountByinv(pk_invbasdoc, pk_corp);
//                  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kcamount, i, "kcamount");
//                  UFDouble pgamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pgamount")==null?"":
//  					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pgamount").toString());		//�����ɹ����� = ������������-���ɹ����� 
//                  /**��������δ�������������δ�������� add by wb at 2008-10-21 17:36:39*/
//                  UFDouble[] unrksc = ((ClientUI)getBillUI()).getUnrkUnsc(pk_invbasdoc);		//�õ�����δ�������������δ��������
//	          	  if(unrksc!=null&&unrksc.length==2){
//	          			UFDouble scunrk  = unrksc[0];				//����δ�������
//	          			UFDouble cjunsc  = unrksc[1];				//����δ��������
//	          			UFDouble needpg = pgamount.sub(scunrk).add(cjunsc);			//�ɹ�����=�����ɹ�����-����δ�������+����δ��������
//	          			//����ɹ�����<�㣬�ɹ�����ȡ�㲻�������������޸ģ�����ɹ��������㣬�ɹ�����ȡ�����������������޸�
//	          			if(needpg.toDouble()<0){
//	          				pgamount = new UFDouble(0);
//	          				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("�����ϲ���Ҫ���ɹ�", i, "memo");
//	          				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "def_1");
//	                   		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "pgamount", false);
//	          			}
//	          			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scunrk, i, "scunrk"); 
//	          			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(cjunsc, i, "cjunsc");
//	          			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pgamount, i, "pgamount");
//	          	  }
                  String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").getEditFormulas();//��ȡ�༭��ʽ
                  getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
              	}
              getBillUI().updateUI();
            }
            
            if(bocode.equals("MRP����")){
                int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
                for(int i=0;i<row;i++){
                    getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vinvbascode", false);
                    //getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_sc_pgd_b").setCellEditable(i,"vhjl", false);
                    String[] formual= getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").getEditFormulas();//��ȡ�༭��ʽ
                    getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
                	}
                getBillUI().updateUI();
              }
            
            
        }
        
    	
    	 //���ð�ť�Ŀ���״̬
        @SuppressWarnings("null")
		@Override
		protected void setBoEnabled() throws Exception {
            ScPgdVO pgVO = (ScPgdVO)getBillUI().getVOFromUI().getParentVO();
//            if (pgVO==null||pgVO.getPrimaryKey().length()==0){
//            	
//            }
//            else{  
            if(pgVO != null || pgVO.getPrimaryKey().length()!=0 ){
            	super.setBoEnabled();
            	boolean xdflag = pgVO.getXdflag()==null?null:pgVO.getXdflag().booleanValue();// �´���
                if(xdflag){
                	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    getButtonManager().getButton(IEHButton.SENDFA).setEnabled(false);
                }
                if(!xdflag){
                    getButtonManager().getButton(IEHButton.SENDFA).setEnabled(true);
                }
                getBillUI().updateButtonUI();
            }
            
        }
        
        
        @Override
		protected void onBoCard() throws Exception {
        	super.onBoCard();
        	setBoEnabled();
        }
       
        
      
        
}
