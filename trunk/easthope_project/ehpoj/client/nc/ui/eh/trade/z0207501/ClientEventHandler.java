package nc.ui.eh.trade.z0207501;


import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0207501.InvoiceBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**���۷�Ʊ
 * @author ����
 * 2008-03-24 ����04:03:18
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoSave() throws Exception {					
		
		//��ʾ�Ǵ��Ǹ��ط����ĵ���(1�ǳ��ⵥ 2 �����֪ͨ��)
		String vsourcebilltype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject()==null?"":
	                             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject().toString();
		
		String vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());	
        boolean add=isAdding();
        if(vsourcebilltype.equals(IBillType.eh_z0206510)){
			//��д��eh_ladingbill�У���ʾ�Ѿ����ڷ�Ʊ��
			
			String sql = "update eh_ladingbill set used_flag='Y'  where pk_ladingbill in "+vsourcebillid+"";
			pubitf.updateSQL(sql);
		}
		else if(vsourcebilltype.equals(IBillType.eh_z0255001))
		{
		    //��д��eh_icout�У���ʾ�Ѿ����ڷ�Ʊ��
			String sql="update eh_icout set used_flag='Y'  where pk_icout in "+vsourcebillid+"";
			pubitf.updateSQL(sql);
	
		}	
		else if(vsourcebilltype.equals(IBillType.eh_z0207010)){//�����˻ص�
			String sql="update eh_backbill set ykfp_flag='Y'  where pk_backbill in "+vsourcebillid+"";
			pubitf.updateSQL(sql);
		}
        InvoiceBVO[] bvos=(InvoiceBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		UFDouble notaxmoney = new UFDouble(0);			//��˰���
		UFDouble taxmoney = new UFDouble(0);			//˰��
		for(int i=0;i<bvos.length;i++){
			UFDouble notaxmony = bvos[i].getDef_8();
			notaxmoney=notaxmoney.add(notaxmony);
			UFDouble taxmony = bvos[i].getTax();
			taxmoney=taxmoney.add(taxmony);
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("notaxmoney",notaxmoney);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("taxmoney",taxmoney);
		
		//�������
		UFDouble hxje=new UFDouble(0);
		for(int i=0;i<bvos.length;i++){
			UFDouble hsprice=new UFDouble(bvos[i].getHsprice()==null?"0":bvos[i].getHsprice().toString());
			hxje=hxje.add(hsprice);
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("hxje",hxje);
		
		
		
		
	    super.onBoSave();          
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
		  String vsourcetyp=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject()==null?"":
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject().toString();
    	    PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            String vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject()==null?"":
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebillid").getValueObject().toString();
            if(vsourcetyp.equals(IBillType.eh_z0206510)){
    			//��д��eh_ladingbill�У���ʾ�Ѿ����ڷ�Ʊ��
    			
    			String sql = "update eh_ladingbill set used_flag='N'  where pk_ladingbill in "+vsourcebillid+"";
    			pubitf.updateSQL(sql);
    		}
    		else if(vsourcetyp.equals(IBillType.eh_z0255001))
    		{
    		    //��д��eh_icout�У���ʾ�Ѿ����ڷ�Ʊ��
    			String sql="update eh_icout set used_flag='N'  where pk_icout in "+vsourcebillid+"";
    			pubitf.updateSQL(sql);
    	    }
    		else if(vsourcetyp.equals(IBillType.eh_z0207010)){//�����˻ص�
    			String sql="update eh_backbill set ykfp_flag='N'  where pk_backbill in "+vsourcebillid+"";
    			pubitf.updateSQL(sql);
    		}
    	    super.onBoTrueDelete();
			
		
	}


	
      @Override
      public void onButton_N(ButtonObject bo, BillModel model) {
          // TODO Auto-generated method stub
          super.onButton_N(bo, model);
          String bocode=bo.getCode();
          //�������۶������������ʱ���ͻ��Ͳ�Ʒ������༭��ͬʱ���岻�ܽ����в���
          if(bocode.equals("���֪ͨ��")||bocode.equals("���۳��ⵥ")||bocode.equals("�����˻�")){
              //��Ʊ�ܶ�ļ���
              int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
              UFDouble ze=new UFDouble(0);
              UFDouble rate =  new PubTools().getXSCgRate(0);
              for(int i=0;i<rows;i++){
            	  String[] formual = getBillCardPanelWrapper().getBillCardPanel().getBodyItem("amount").getEditFormulas();//��ȡ�༭��ʽ
            	  getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
            	  UFDouble amout = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"amount")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"amount").toString());                   //����
            	  UFDouble jgf = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"hsprice")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"hsprice").toString());                 //Ӧ�ս��
            	  ze=ze.add(jgf);
                  UFDouble prcie = (jgf.div(amout)).div(new UFDouble(1).add(rate));                    //����˰����(Ӧ�ս��/����)/1.13
                  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(prcie,i,"price");                        //����˰����
                  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amout.multiply(prcie),i,"def_8");        //����˰���
                  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(rate,i,"taxrate");
                  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amout.multiply(prcie).multiply(rate),i,"tax"); // ˰��  = ����˰���*˰��
                  //add by houcq begin 2010-11-25
                  String pk_invbasdoc=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
                		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();
                  StringBuffer sql = new StringBuffer()
                  .append(" select b.mainmeasrate changerate from bd_invmandoc d,bd_invbasdoc a,bd_convert b,bd_measdoc c")
                  .append(" where d.pk_invbasdoc = a.pk_invbasdoc and a.pk_invbasdoc = b.pk_invbasdoc")
                  .append(" and b.pk_measdoc = c.pk_measdoc")
                  .append(" and d.pk_invmandoc = '"+pk_invbasdoc+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 ");
                  IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                String sql1=sql.toString();  
				try {
					ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql1, new MapListProcessor());
					UFDouble changerate=new UFDouble(1);
	              	if (al.size()==1)
	              	{
	              		HashMap hm=(HashMap) al.get(0);
	              		changerate=new UFDouble(hm.get("changerate")==null?"1":hm.get("changerate").toString());
	              		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amout.div(changerate),i,"def_6"); 
	              	}
	              	else
	              	{
	              		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amout,i,"def_6");                  
	              		
	              	}
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}              	
              }
              getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_currency","00010000000000000001");
              getBillCardPanelWrapper().getBillCardPanel().setHeadItem("totalprice",ze);
          }
          if(bocode.equals("�����˻�")){
        	   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbilltype", IBillType.eh_z0207501);
        	   getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vsourcebilltype", IBillType.eh_z0207010);
        	   int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
               UFDouble ze=new UFDouble(0);
               for(int i=0;i<rows;i++){
            	  UFDouble amout = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"amount")==null?"0":
                       getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"amount").toString()));                   //����
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amout,i,"amount");                        
            	  UFDouble  first = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString()));                   //һ���ۿ�
            	  UFDouble second = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString()));                   //�����ۿ�
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(first,i,"firstdiscount");  
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(second,i,"seconddiscount"); 
            	  UFDouble price = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"def_8")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"def_8").toString()));                   //����˰���
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(price,i,"def_8");  
            	  UFDouble taxrate = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"taxrate")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"taxrate").toString()));                 //˰��
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(taxrate,i,"taxrate");  
            	  UFDouble tax = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"tax")==null?"0":
                      getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"tax").toString()));                   // ˰�� 
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(tax,i,"tax");  
            	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(first.add(second),i,"totaldiscount");  // �ۿ��ܶ�
            	  
            	  UFDouble jgf = new UFDouble(0).sub(new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"hsprice")==null?"0":
                       getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"hsprice").toString()));                 //Ӧ�ս��
             	  ze=ze.add(jgf);
             	  getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(jgf,i,"hsprice");  // Ӧ�ս��
               }
               getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_currency","00010000000000000001");
               getBillCardPanelWrapper().getBillCardPanel().setHeadItem("totalprice",ze);
          }
      }
    
}

