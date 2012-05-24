
package nc.ui.eh.trade.z0206005;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z0206005.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ���� ���۶���
 * @author �麣
 * 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {
   
	public static String pk_cubasdoc = null;          // ����
	public static String pk_unit = null;
	public ClientUI() {
        super();
        pk_cubasdoc = null;
    }

    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    protected AbstractManageController createController() {
        return new ClientCtrl();
    }

    public ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }
   
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String strKey=e.getKey();
        if (e.getPos()==BODY){
            if("vinvbascode".equalsIgnoreCase(strKey)){
            	pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                    getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            	if(pk_cubasdoc==null||pk_cubasdoc.length()<=0){
            		showErrorMessage("�ͻ�����Ϊ��,����ѡ��ͻ�!");
            	}
            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
            }
            if(strKey.equals("dw")||strKey.equals("amount")){
    			int row=getBillCardPanel().getBillTable().getSelectedRow();
    			pk_unit= getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                            getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //��λ
    		}
        }
    	return super.beforeEdit(e);
    }
    @SuppressWarnings("unchecked")
	public void afterEdit(BillEditEvent e) {
        // TODO Auto-generated method stub
        //super.afterEdit(arg0);
        String strKey=e.getKey();
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        
        if(strKey.equals("amount")){
        	//������ָ����ܴ������Ļ� �����ڵ�λ��û�и���������λ
        	int row=e.getRow();
        	String fzunit=getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
        		getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();
        	UFDouble fzamount =new UFDouble(getBillCardPanel().getBodyValueAt(row,"amount")==null?"-100000":
        		getBillCardPanel().getBodyValueAt(row,"amount").toString());
        	String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
        		getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
        	UFDouble oneprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, "oneprice")==null?"-10000":
        		getBillCardPanel().getBodyValueAt(row, "oneprice").toString());
        	try {
				setUA(fzunit,pk_invbasdoc,fzamount,oneprice,row);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        }
        
        
        if(strKey.equals("vinvbascode")){
            int row=getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                                 getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //��Ʒ
            getBillCardPanel().setBodyValueAt(null, row, "amount"); 
//            getBillCardPanel().setBodyValueAt(0, row, "totalprice");
            getBillCardPanel().setBodyValueAt(0, row, "firstcount");
            getBillCardPanel().setBodyValueAt(0, row, "secondcount");
            getBillCardPanel().setBodyValueAt(0, row, "bcysje");
            //modfiy by houcq 2011-04-18
            UFDouble price = new PubTools().getInvPrice(pk_invbasdoc,_getDate());
			getBillCardPanel().setBodyValueAt(price, row, "oneprice");
			
        }
        
//      ��̯�ۿ۵Ĵ���  add by wb at 2008-6-5 15:31:36
        if(strKey.equals("def_6")){
        	 BillModel model = getBillCardPanel().getBillModel();
             if (model != null) {
            	 int rowCount = model.getRowCount();
                 if (rowCount < 1) {
                     showErrorMessage("����Ϊ��,���ܷ�̯�ۿ�!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
               try {
                 OrderVO orVO = (OrderVO)getChangedVOFromUI().getParentVO();
                 UFDouble dkze = orVO.getDef_9()==null?null:orVO.getDef_9();                    // �ܶ�
            	 if (dkze==null) {
                     showErrorMessage("��������δ���벻�ܷ�̯,������������!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
            	    UFDouble zkye = orVO.getZkye()==null?new UFDouble(0):orVO.getZkye();                        // �����ۿ�
					UFDouble seconddiscount = orVO.getSecondamount()==null?new UFDouble(0):orVO.getSecondamount();          // �����ۿ۽��(�ͻ������ۿ����)
					UFDouble ftdiscount = orVO.getDef_6();             // ��̯�����ۿ�
					//add by houcq 2011-02-25 begin
					if (ftdiscount==null)
					{						   
							int row=getBillCardPanel().getBillTable().getRowCount();
	                		for(int i=0;i<row;i++)
	                		{
	                			getBillCardPanel().setBodyValueAt("", i, "secondcount");
	                			UFDouble bcysje=new UFDouble(0);                              //����Ӧ���ܶ�
	                            UFDouble totalprice=new UFDouble(getBillCardPanel().getBodyValueAt(i,"totalprice")==null?"0":
	                                getBillCardPanel().getBodyValueAt(i,"totalprice").toString());
	                            UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstcount")==null?"0":
	                                getBillCardPanel().getBodyValueAt(i,"firstcount").toString());
	                            bcysje=totalprice.sub(firstdis);
	                            getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
	                		}	                		
	                		
					}
					//add by houcq 2011-02-25 end
					if(ftdiscount.compareTo(zkye)>0){
						showErrorMessage("��̯�ۿ۲��ܴ��������ۿ��ܶ�!");
						getBillCardPanel().setHeadItem("def_6", null);
						return;
					}
					else if(ftdiscount.compareTo(seconddiscount)>0){
						showErrorMessage("��̯�ۿ۲��ܴ��ڶ����ۿ۽��!");
						getBillCardPanel().setHeadItem("def_6", null);
						return;
					}
					else{
//						int iRet = showYesNoMessage("�Ƿ�ȷ����̯���ۿ�?");
//		                if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
		                	int row=getBillCardPanel().getBillTable().getRowCount();
		                    UFDouble vjeze = dkze;         // �ܽ�� ���ݱ������ݽ�����ܽ��ı�������̯�ۿ�
		                    UFDouble ze = new UFDouble(0); // �����̯�Ľ��ϼ�
		                    DecimalFormat df = new DecimalFormat("#.00"); 
		                	for(int i=0;i<row;i++){
		                		UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"totalprice")==null?"0":
		                            getBillCardPanel().getBodyValueAt(i,"totalprice").toString());
		                		UFDouble bl = vje.div(vjeze);  // ��ռ����
		                		UFDouble ftdisco = new UFDouble(df.format(ftdiscount.multiply(bl).toDouble()));
		                		getBillCardPanel().setBodyValueAt(ftdisco, i,"secondcount");
		                        String[] formual=getBillCardPanel().getBodyItem("amount").getEditFormulas();//��ȡ�༭��ʽ
		                        getBillCardPanel().execBodyFormulas(i,formual);
		                        ze = ze.add(ftdisco);
		                	}
		                	// ����ķ�̯���ϼ�������̯�ܶ�Ƚ�,���ڱ�������������,�轫������ۿ۷�̯�����һ�б�����
		                	UFDouble chae = ftdiscount.sub(ze);
		                	if(chae.toDouble()>0){
		                		UFDouble lastdis = new UFDouble(getBillCardPanel().getBodyValueAt(row-1,"secondcount")==null?"0":
		                            		getBillCardPanel().getBodyValueAt(row-1,"secondcount").toString());
		                		getBillCardPanel().setBodyValueAt(lastdis.add(chae), row-1, "secondcount");
		                	}		                	
		                    updateUI();
//		                }
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                 
                	 
             }
        }
        
//        /*-----------------------------------------------------------------������
        //  ѡ��λʱ�Ĵ���  add by wb at 2008-5-5 15:48:30
//        if(strKey.equals("dw")){
//           super.changeDW(pk_unit, "pk_measdoc", "amount", "oneprice", "strtotalprice");           
//        }
        // ���������ʱ�������ۿ���Ϊ��
        if(e.getKey().equals("amount")){  
	       	 int rowcount=getBillCardPanel().getBillTable().getRowCount();
	       	 for(int i=0;i<rowcount;i++){
	           getBillCardPanel().setBodyValueAt(null, i, "secondcount");
            	UFDouble bcysje=new UFDouble(0);                              //����Ӧ���ܶ�
                UFDouble totalprice=new UFDouble(getBillCardPanel().getBodyValueAt(i,"totalprice")==null?"0":
                getBillCardPanel().getBodyValueAt(i,"totalprice").toString());
                UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstcount")==null?"0":
                getBillCardPanel().getBodyValueAt(i,"firstcount").toString());
                bcysje=totalprice.sub(firstdis);
                getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
	         }
	       	getBillCardPanel().setHeadItem("def_6",null);
	       	
       }
        //ѡ���Ʒʱ�����ۿ�,�޸ı����еĲ�Ʒ�������������еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
        if(strKey.equals("amount")||strKey.equals("dw")||strKey.equals("def_6")){
        	int row=getBillCardPanel().getBillTable().getSelectedRow();
        	String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();   //�ͻ�
        	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //��Ʒ
        	String pk_measdoc= getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //��λ
            ArrayList arr=getDiscount(pk_cubasdoc,pk_invbasdoc);
            HashMap hm=(HashMap)arr.get(0);                                                                       //һ���ۿ�
            HashMap hm2=(HashMap)arr.get(1);    
            //�����ۿ�
            
            UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"amount")==null?"0":
               getBillCardPanel().getBodyValueAt(row,"amount").toString());                                      //����
            //���ݵ�λת��һ���ۿ۶�  add by wb at 2008-5-31 15:19:15
            UFDouble rate = new PubTools().getInvRate(pk_invbasdoc, pk_measdoc);
//            UFDouble rate = new UFDouble(getInvRate(pk_invbasdoc).get(pk_measdoc)==null?"1":getInvRate(pk_invbasdoc).get(pk_measdoc).toString());  //��λ��ת����
            UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString()).multiply(rate);
            getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), row, "firstcount");                //���ݿͻ��Ͳ�Ʒ����һ���ۿ�

            UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(row,"price")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"price").toString());                                      //�õ��Ƽ�
//          //����õ������ۿ� �Ƽ�*����*�ۿ۱���-(һ���ۿ�*����)
//            UFDouble secondcount=price.multiply(amount).multiply(IPubInterface.DISCOUNTRATE).sub(amount.multiply(firstdiscount));  //����õ������ۿ�
//            UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString());
//            //������������ۿ�С���ۿ۱��ϵ�������Լ��������ۿ�Ϊ�ۿ۶� �Ƚ�ʱȡ��С�� edit by wb at 2008-7-4 15:59:54
//            if(secondcount.compareTo(seccount)<0){
//            	seccount=secondcount;
//            }
//            getBillCardPanel().setBodyValueAt(seccount, row, "secondcount");                    
            String[] formual=getBillCardPanel().getBodyItem("amount").getEditFormulas();                        //��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            //�����еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);                              //Ӧ���ܶ�
            UFDouble jgze=new UFDouble(0);                            //�����ܶ�
            UFDouble firstdiscze=new UFDouble(0);          		//һ���ۿ��ܶ�
            UFDouble seccountze=new UFDouble(0);          //�����ۿ��ܶ�
            for(int i=0;i<rows;i++){
                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
                UFDouble totalprice=new UFDouble(getBillCardPanel().getBodyValueAt(i,"totalprice")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"totalprice").toString());
               UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstcount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"firstcount").toString());
               UFDouble scount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"secondcount")==null?"0":
                   getBillCardPanel().getBodyValueAt(i,"secondcount").toString());
               seccountze=seccountze.add(scount);
                ze=ze.add(bcysje);
                jgze=jgze.add(totalprice);
                firstdiscze=firstdiscze.add(firstdis);
            }
            try {
				OrderVO orVO = (OrderVO)getChangedVOFromUI().getParentVO();
				UFDouble lastzk = new UFDouble(orVO.getSecondamount()==null?"0":orVO.getSecondamount().toString());
				getBillCardPanel().setHeadItem("def_7",lastzk.sub(seccountze)); 
				// �����ۿ���� = �����ۿ۽��(�����ۿ�)-�������ö����ۿ�
				//add by houcq 2011-02-25 begin
				
				if (orVO.getDef_6()==null)
				{					
					getBillCardPanel().setHeadItem("yfze",jgze.sub(firstdiscze));
				}
				else
				{
					getBillCardPanel().setHeadItem("yfze",ze);
				}
				
				//add by houcq 2011-02-25 end
				
				getBillCardPanel().setHeadItem("def_9",jgze);
  //            getBillCardPanel().setHeadItem("zkye",jgze.multiply(IPubInterface.DISCOUNTRATE));     //edit by wb at 2008-7-4 16:16:16
				
				UFDouble ddzkye = new UFDouble(0);
				UFDouble zd = jgze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze);// ���۶����ۿ� (���*40%-һ���ۿ��ܶ�)
				if(zd.compareTo(new UFDouble(0))>0){//�����۶����ۿ�Ϊ����ʱ��0�滻
					ddzkye = zd;
				}
				getBillCardPanel().setHeadItem("zkye",ddzkye);  // ���۶����ۿ� (���*40%-һ���ۿ��ܶ�) edit by wb at 2008-7-10 12:25:07);
            } catch (Exception e1) {
				e1.printStackTrace();
			}
        }
//        ----------------------------------------------------------------------------------------*/
        //ѡ��ͻ�ʱ���ۿ۵Ĵ���
        if(strKey.equals("pk_cubasdoc")){
            String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            ArrayList arr=getDiscount(pk_cubasdoc,null);
            HashMap hm=(HashMap)arr.get(0);
            HashMap hm2=(HashMap)arr.get(1);       
            HashMap hm3=(HashMap)arr.get(2);       
            //���ݿͻ��ѿ��̵���������Ӫ����������� 2008-05-06 add by zqy
            try {
            	PubTools tools = new PubTools();
                UFDouble overage = tools.getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//���ҿͻ����
                String pk_psndoc = tools.getPk_custpsndoc(pk_cubasdoc, _getCorp().getPk_corp());	//���̴���  edit by wb 2009-11-4 16:19:49
                getBillCardWrapper().getBillCardPanel().setHeadItem("yfye",overage);
				getBillCardWrapper().getBillCardPanel().setHeadItem("pk_psndoc",pk_psndoc);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            
            int rows=getBillCardPanel().getBillTable().getRowCount();
//            UFDouble ze=new UFDouble(0);
//            for(int i=0;i<rows;i++){
////                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
//                String pk_invbasdoc=getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
//                                        getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();
//                UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"amount")==null?"0":
//                   getBillCardPanel().getBodyValueAt(i,"amount").toString());
//                UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString());
//                getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), i, "firstcount");
//                UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(i,"price")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"price").toString());                                      //�õ��Ƽ�
//                UFDouble secondcount=price.multiply(amount).multiply(IPubInterface.DISCOUNTRATE).sub(amount.multiply(firstdiscount));    
//                UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString());
//                //������������ۿ�С���ۿ۱��ϵĿ����ۿ������ۿ۱��ϵĶ����ۿ�Ϊ�ۿ۶�
//                if(seccount.compareTo(secondcount)<0){
//                    secondcount=seccount;
//                }//����õ������ۿ�
//                getBillCardPanel().setBodyValueAt(secondcount, i, "secondcount");   
//                String[] formual=getBillCardPanel().getBodyItem("amount").getEditFormulas();//��ȡ�༭��ʽ
//                getBillCardPanel().execBodyFormulas(i,formual);
//                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
//                ze=ze.add(bcysje);
//            }
            getBillCardPanel().setHeadItem("zkye",null);   //ѡ�����ʱ�������ۿ���Ϊ��
            getBillCardPanel().setHeadItem("yfze",null);   //Ӧ���ܶ�
            //��ѡ��ͻ�ʱ���ͻ����ö����ۿ����д���ͷ�����ۿ��ܶ�
            UFDouble secondamount=new UFDouble(hm3.get(pk_cubasdoc)==null?"0":hm3.get(pk_cubasdoc).toString());
            getBillCardPanel().setHeadItem("secondamount",secondamount);
            // ��ѡ��ͻ�ʱ��ձ���  add by wb at 2008-5-19 15:38:59
            int[] rowcount=new int[rows];
            for(int i=rows - 1;i>=0;i--){
            	rowcount[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowcount);
            this.updateUI();
        }
                
        updateUI();
    }

    @SuppressWarnings("unchecked")
    public ArrayList getDiscount(String pk_cubasdoc,String pk_invbasdoc){
        ArrayList array=new ArrayList();
        try {
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            //ȡ�øÿͻ������ϵ�һ���ۿ۽��
            StringBuffer sql=new StringBuffer("select b.pk_invbasdoc||b.pk_cubasdoc keys,isnull(b.newdiscount,0) value from eh_firstdiscount a ")
            .append(" ,eh_firstdiscount_b b where a.pk_firstdiscount=b.pk_firstdiscount and '"+_getDate()+"' between b.zxdate ") // �������±��
            .append(" and b.yxdate and a.pk_corp='"+_getCorp().getPk_corp()+"' and b.pk_cubasdoc='"+pk_cubasdoc+"' and isnull(a.def_1,'N')='Y'   and isnull(a.lock_flag,'N')='N'  and isnull(b.lock_flag,'N')='N' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0  and vbillstatus="+IBillStatus.CHECKPASS+" order by a.ts desc");
            ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
            HashMap hm=new HashMap();
            if(!arr.isEmpty()){
                for(int i=0;i<arr.size();i++){
                    HashMap al=(HashMap) arr.get(i);
                    hm.put(al.get("keys").toString(),al.get("value").toString());
                }
            }
            array.add(hm);
            //ȡ�ÿͻ������ϵĶ����ۿ۽��
            int date=_getDate().getYear()*100+_getDate().getMonth();
            StringBuffer sql2=new StringBuffer("select pk_invbasdoc||pk_cubasdoc keys,sum(isnull(ediscount,0)) value from eh_perioddiscount ")
            .append(" where pk_corp='"+_getCorp().getPk_corp()+"' and (nyear*100+nmonth)='"+date)
            .append("' and pk_cubasdoc='"+pk_cubasdoc+"'  and pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(dr,0)=0 group by pk_invbasdoc||pk_cubasdoc");
            ArrayList arr2=(ArrayList)iUAPQueryBS.executeQuery(sql2.toString(),new MapListProcessor());
            HashMap hm2=new HashMap();
            if(!arr2.isEmpty()){
                for(int i=0;i<arr2.size();i++){
                    HashMap al=(HashMap) arr2.get(i);
                    hm2.put(al.get("keys").toString(),al.get("value").toString());
                }
            }
	        array.add(hm2);

	        //ȡ�ÿͻ��Ķ����ۿ��ܶ�
            StringBuffer sql3=new StringBuffer("select pk_cubasdoc keys,sum(isnull(ediscount,0)) value from eh_perioddiscount ")
            .append(" where pk_corp='"+_getCorp().getPk_corp()+"' and (nyear*100+nmonth)='"+date)
            .append("' and pk_cubasdoc='"+pk_cubasdoc+"' and isnull(dr,0)=0 group by pk_cubasdoc");
            ArrayList arr3=(ArrayList)iUAPQueryBS.executeQuery(sql3.toString(),new MapListProcessor());
            HashMap hm3=new HashMap();
            if(!arr3.isEmpty()){
                for(int i=0;i<arr3.size();i++){
                    HashMap al=(HashMap) arr3.get(i);
                    hm3.put(al.get("keys").toString(),al.get("value").toString());
                }
            }
            array.add(hm3);
            
        } catch (ComponentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
    public void setDefaultData() throws Exception {
        getBillCardPanel().setHeadItem("getdate", _getDate());
        getBillCardPanel().setHeadItem("enddate", _getDate());
        super.setDefaultData();
        BillItem vbillstatus = getBillCardPanel().getHeadItem("vbillstatus");
        if (vbillstatus!= null)
        	getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.CHECKPASS));
    }  
    
    /*
     * ע���Զ��尴ť 
     * 2008-04-02
     */
    protected void initPrivateButton() {
        super.initPrivateButton();
    }
    /**
     * ��������λ�͸���������λ֮���װ��ϵ���������ı任
     * @param String pk_invbasdoc ���Ϲ�����PK��
     * @throws BusinessException 
     */
    public void setUA(String fzunit,String pk_invbasdoc,UFDouble amount,UFDouble price,int row) throws BusinessException{
    	String sql="select mainmeasrate changerate from bd_convert where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') and nvl(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	UFDouble changerate=new UFDouble(-1000000);
    	for(int i=0;i<al.size();i++){
    		HashMap hm=(HashMap) al.get(i);
    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
    	}
    	UFDouble fzamount=amount.multiply(changerate);
    	UFDouble oneprice=price.multiply(changerate);
    	getBillCardPanel().setBodyValueAt(oneprice, row, "price");
    	getBillCardPanel().setBodyValueAt(fzamount, row, "fzamount");
    }
    
    
}

   
    

