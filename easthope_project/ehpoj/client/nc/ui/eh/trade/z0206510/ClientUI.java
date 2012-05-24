/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0206510;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.businessref.YsContractRefModel;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ct.OperationState;
import nc.vo.trade.pub.IBillStatus;

/**
 * ���� ���֪ͨ��
 * @author �麣
 * 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {

	public static String nowdate = null;          // ����
    public String pk_measdoc = null; // ��λ����
    public static String pk_cubasdoc = null;          // ����
    UIRefPane ref=null;
	
    public ClientUI() {
        super();
        nowdate = _getDate().toString();
        ref=(UIRefPane) getBillCardPanel().getHeadItem("pk_yscontracts").getComponent();
		ref.setMultiSelectedEnabled(true);
		ref.setProcessFocusLost(false);
		ref.setButtonFireEvent(true);
		ref.setTreeGridNodeMultiSelected(true);
        // TODO �Զ����ɹ��캯�����
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
        // TODO �Զ����ɹ��캯�����
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
            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
            	if(pk_cubasdoc==null||pk_cubasdoc.length()<=0){
            		showErrorMessage("�ͻ�����Ϊ��,����ѡ��ͻ�!");
            	}
            }
    	   if(strKey.equals("dw")){
			int row=getBillCardPanel().getBillTable().getSelectedRow();
			pk_measdoc = getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                        getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //��λ
		  }
    	}
    	return super.beforeEdit(e);
    }
    @SuppressWarnings("unchecked")
	@Override
    public void afterEdit(BillEditEvent e) {
        // TODO Auto-generated method stub
        //super.afterEdit(arg0);
        String strKey=e.getKey();
        //2008��9��27��13:27:42 by wm ѡ�����ϵ�ʱ�����ԭ�е�����
        if(e.getPos()==BODY){
        	if(strKey.equals("vinvbascode")){
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "ladingamount"); //��������� �����
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "zamount"); //���� �����
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "dw"); //������λ �����
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "pk_measdoc"); //������λ �����
        	}
        	
        }
        // end 2008��9��27��13:29:09
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        if("pk_yscontracts".equalsIgnoreCase(strKey)){				//ѡ�������ͬ��������� add by wb at 2008-10-14 10:21:00
        	String[] billnos = ref.getRefNames();
        	String billno = PubTools.combinArrayToString3(billnos);
          	getBillCardPanel().setHeadItem("ysbillnos", billno);
          }
        
        //ѡ��ͻ�ʱ���ۿ۵Ĵ���
        if(strKey.equals("pk_cubasdoc")){
            String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            ArrayList arr=new nc.ui.eh.trade.z0206005.ClientUI().getDiscount(pk_cubasdoc,null);
            @SuppressWarnings("unused")
			HashMap hm=(HashMap)arr.get(0);
            @SuppressWarnings("unused")
			HashMap hm2=(HashMap)arr.get(1);       
            HashMap hm3=(HashMap)arr.get(2);    
            int rows=getBillCardPanel().getBillTable().getRowCount();
            YsContractRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������   add by wb at 2008-10-14 10:18:52
//            UFDouble ze=new UFDouble(0);
//            UFDouble seccountze=new UFDouble(0);          //�����ۿ��ܶ�
//            for(int i=0;i<rows;i++){
//                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
//                String pk_invbasdoc=getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
//                                        getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();
//                UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"ladingamount")==null?"0":
//                   getBillCardPanel().getBodyValueAt(i,"ladingamount").toString());
//                UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString());
//                getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), i, "firstdiscount");
//                UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(i,"price")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"price").toString());                                      //�õ��Ƽ�
////                UFDouble secondcount=price.multiply(amount).multiply(IPubInterface.DISCOUNTRATE).sub(amount.multiply(firstdiscount));    
//                UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString());
//                //������������ۿ�С���ۿ۱��ϵĿ����ۿ������ۿ۱��ϵĶ����ۿ�Ϊ�ۿ۶�
////                if(seccount.compareTo(secondcount)<0){
////                    secondcount=seccount;
////                }//����õ������ۿ�
//               
//                getBillCardPanel().setBodyValueAt(seccount, i, "seconddiscount");   
//                String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();//��ȡ�༭��ʽ
//                getBillCardPanel().execBodyFormulas(i,formual);
//               
//                
//                
//                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
//                ze=ze.add(bcysje);
//                UFDouble scount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString());
//                seccountze=seccountze.add(scount);
////                hm2.put(pk_invbasdoc+pk_cubasdoc, seccount.sub(secondcount));
//                hm2.put(pk_invbasdoc+pk_cubasdoc, seccount);
//            }
            //��ѡ��ͻ�ʱ���ͻ����ö����ۿ����д���ͷ�����ۿ۽��
            UFDouble secondamount=new UFDouble(hm3.get(pk_cubasdoc)==null?"0":hm3.get(pk_cubasdoc).toString());
            getBillCardPanel().setHeadItem("def_7",secondamount);
            getBillCardPanel().setHeadItem("def_9",secondamount);
            getBillCardPanel().setHeadItem("def_8",null);
            getBillCardPanel().setHeadItem("def_6",null);
            getBillCardPanel().setHeadItem("dkze",null);
            getBillCardPanel().setHeadItem("tempdiscount",null);
            getBillCardPanel().setHeadItem("bcyfje",null);
            getBillCardPanel().setHeadItem("seconddiscount",null);
            //����Ӫ������ ���
//            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//            StringBuffer sql = new StringBuffer()
//            .append(" select pk_psndoc from eh_custyxdb ")
//            .append(" where pk_cubasdoc ='"+pk_cubasdoc+"' and ismain ='Y' and isnull(dr,0)=0 ");
//            StringBuffer yesql = new StringBuffer()         
//            .append(" select overage from eh_custoverage ")
//            .append(" where pk_cubasdoc ='"+pk_cubasdoc+"' and isnull(dr,0)=0 ");
            try {
            	PubTools tools = new PubTools();
                UFDouble overage = tools.getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//���ҿͻ����
                
                String pk_psndoc = tools.getPk_custpsndoc(pk_cubasdoc, _getCorp().getPk_corp());	//���̴���  edit by wb 2009-11-4 16:19:49
//            	Object pk_psnobj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
//            	Object overageobj = iUAPQueryBS.executeQuery(yesql.toString(), new ColumnProcessor());
//            	UFDouble overage = new UFDouble(overageobj==null?"0":overageobj.toString());
//            	String pk_psndoc = pk_psnobj==null?"":pk_psnobj.toString();
            	getBillCardWrapper().getBillCardPanel().setHeadItem("sxje",overage);
            	getBillCardWrapper().getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
            	//add by houcq 2011-05-04 begin ����Ӫ�������������
            	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            	if (pk_psndoc!=null)
            	{
            		String sql ="select pk_deptdoc from bd_psndoc where pk_psndoc='"+pk_psndoc+"'";
                	Object obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
                	getBillCardWrapper().getBillCardPanel().setHeadItem("def_2",obj);
            	}
            	//add by houcq 2011-05-04 end
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//��ѡ��ͻ�ʱ��ձ���  add by wb at 2008-5-19 15:38:59
            int[] rowcount=new int[rows];
            for(int i=rows - 1;i>=0;i--){
            	rowcount[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowcount);
            this.updateUI();
        }
        
        if(strKey.equals("vinvbascode")){
            int row=getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
            					getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();
            //ȡ�����ϵ����п��
			try {
				//HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "1", _getCorp().getPk_corp());
				//UFDouble amountkc = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
				//modify by houcq 2011-06-20�޸�ȡ��淽��
				UFDouble amountkc = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
	            getBillCardPanel().setBodyValueAt(amountkc, row, "storeamount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "firstdiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "seconddiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "bcysje");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "lsdiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "lsyydiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "lssydiscount");
	            
	            getBillCardPanel().setBodyValueAt(0, row, "firstcount");
	            getBillCardPanel().setBodyValueAt(0, row, "secondcount");
	            getBillCardPanel().setBodyValueAt(0, row, "bcysje");
	            
	            //modfiy by houcq 2011-04-18
	            //UFDouble price = new PubTools().getInvPrice(pk_invbasdoc);
	            UFDouble price = new PubTools().getInvPrice(pk_invbasdoc,_getDate());
	            int rows=e.getRow();
	        	String pk_measdoc=getBillCardPanel().getBodyValueAt(rows, "pk_measdoc")==null?"":
	        		getBillCardPanel().getBodyValueAt(rows, "pk_measdoc").toString();
	        	String pk_invbasdoc2 = getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc")==null?"":
	        		getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc").toString();
				setUA2(pk_measdoc,pk_invbasdoc2,price,rows);
            
				getBillCardPanel().setBodyValueAt(price, row, "zprice");
				
				/**�������ϵ��ѿ������δ������,�������ɹ�δ�������  add by wb at 2008-10-22 18:51:11*/
				UFDouble[] unckrk = getUnckUnrk(pk_invbasdoc,_getDate());
				if(unckrk!=null&&unckrk.length==2){
					UFDouble ytwcamount = unckrk[0];		//�ѿ������δ������
					UFDouble ypgwrkamount = unckrk[1];		//�������ɹ�δ�������
					UFDouble maxthamount = amountkc.sub(ytwcamount).add(ypgwrkamount);		//��������
					getBillCardPanel().setBodyValueAt(ytwcamount, row, "ytwcamount");
					getBillCardPanel().setBodyValueAt(ypgwrkamount, row, "ypgwrkamount");
					getBillCardPanel().setBodyValueAt(maxthamount, row, "maxthamount");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//add by houcq 2011-02-25 begin,ѡ������ʱ��ձ�������ۿۺͱ�ͷ��̯�ۿ�
			getBillCardPanel().setHeadItem("def_6",null);
			int rows1 = getBillCardPanel().getBillTable().getRowCount();
			for (int i=0;i<rows1;i++)
			{
				getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");
			}
			//add by houcq 2011-02-25 end
		}
        //ѡ��λʱ�Ĵ���  add by wb at 2008-5-6 14:11:50
//        if(strKey.equals("dw")){
//        	super.changeDW(pk_measdoc,"pk_measdoc","ladingamount", "price", "vje");
//        }
       
        //��̯�ۿ۵Ĵ���  add by wb at 2008-6-5 15:31:36
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
                 LadingbillVO ladVO = (LadingbillVO)getChangedVOFromUI().getParentVO();
            	 UFDouble dkze = ladVO.getDkze()==null?null:ladVO.getDkze();                    // �ܶ�
            	 if (dkze==null) {
                     showErrorMessage("�������δ���벻�ܷ�̯,������������!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
            	    UFDouble zkye = ladVO.getDef_8()==null?new UFDouble(0):ladVO.getDef_8();                    // �����ۿ��ܶ�
					UFDouble seconddiscount = ladVO.getDef_7()==null?new UFDouble(0):ladVO.getDef_7();          // �����ۿ۽��(�ͻ������ۿ����)
					UFDouble ftdiscount = ladVO.getDef_6()==null?new UFDouble(0):ladVO.getDef_6();             // ��̯�����ۿ�
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
		                		UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
		                            getBillCardPanel().getBodyValueAt(i,"vje").toString());
		                 		UFDouble bl = vje.div(vjeze);  // ��ռ����
		                		UFDouble ftdisco = new UFDouble(df.format(ftdiscount.multiply(bl).toDouble()));
		                		getBillCardPanel().setBodyValueAt(ftdisco, i,"seconddiscount");
		                        String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();//��ȡ�༭��ʽ
//		                        UFDouble a=new UFDouble(getBillCardPanel().getBodyValueAt(i, "vje").toString());
//		                        UFDouble b=new UFDouble(getBillCardPanel().getBodyValueAt(i, "seconddiscount").toString());
		                        getBillCardPanel().execBodyFormulas(i,formual);
		                        ze = ze.add(ftdisco);
		                	}
		                	//����ķ�̯���ϼ�������̯�ܶ�Ƚ�,���ڱ�������������,�轫������ۿ۷�̯�����һ�б�����
		                	UFDouble chae = ftdiscount.sub(ze);
//		                	if(chae.toDouble()<0){
		                		UFDouble lastdis = new UFDouble(getBillCardPanel().getBodyValueAt(row-1,"seconddiscount")==null?"0":
		                            		getBillCardPanel().getBodyValueAt(row-1,"seconddiscount").toString());
		                		getBillCardPanel().setBodyValueAt(lastdis.add(chae), row-1, "seconddiscount");
		                		
		                		UFDouble bcysje2 = new UFDouble(getBillCardPanel().getBodyValueAt(row-1,"bcysje")==null?"0":
                            		getBillCardPanel().getBodyValueAt(row-1,"bcysje").toString());
		                		getBillCardPanel().setBodyValueAt(bcysje2.sub(chae), row-1, "bcysje");
//		                	}
		                    updateUI();
//		                }
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                 
                	 
             }
        }
        // ��д�������������ʵʱ�����������(�����������������) add by wb at 2008-5-15 11:47:22
        int flag = ClientEventHandler.flag;          // �������ӱ��
        int invoiceflag = ClientEventHandler.invoiceflag;          // �������ӱ��
        if(e.getKey().equals("ladingamount")&&flag==1){  // �����۶�������ʱ�����������
	       	 int row = getBillCardPanel().getBillTable().getSelectedRow();
	       	 String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
	       		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
	       	 String pk_ladingbill_b = getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b")==null?null:
	                                  getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b").toString();  // �ӱ�����
	            //ʵʱ�����ջ���(�����������ջ���)
	       	 UFDouble amount = PubTools.calTotalamount("eh_ladingbill_b", "ladingamount", vsourcebillid, "pk_ladingbill_b", pk_ladingbill_b);
	       	 getBillCardPanel().setBodyValueAt(amount, row, "ytamount");
	       	 int rowcount=getBillCardPanel().getBillTable().getRowCount();
	       	 for(int i=0;i<rowcount;i++){
	            	getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");    // ���������ʱ�������ۿ���Ϊ��
	         }
	       	getBillCardPanel().setHeadItem("def_6",null);
	       	ClientEventHandler.flag = 0;//��������־��ʼ��
        }
        //---------------------------------------------------------------------------------
        if(e.getKey().equals("ladingamount")&&invoiceflag==2){  // �����۷�Ʊ����ʱ�����������
	       	 int row = getBillCardPanel().getBillTable().getSelectedRow();
	       	 @SuppressWarnings("unused")
			String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
	       		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
	       	 @SuppressWarnings("unused")
			String pk_ladingbill_b = getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b")==null?null:
	                                  getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b").toString();  // �ӱ�����
	            //ʵʱ�����ջ���(�����������ջ���)
//	       	 UFDouble amount = PubTools.calTotalamount("eh_ladingbill_b", "ladingamount", vsourcebillid, "pk_ladingbill_b", pk_ladingbill_b);
//	       	 getBillCardPanel().setBodyValueAt(amount, row, "ytamount");
//	       	 int rowcount=getBillCardPanel().getBillTable().getRowCount();
//	       	 for(int i=0;i<rowcount;i++){
//	            	getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");    // ���������ʱ�������ۿ���Ϊ��
//	         }
//	       	getBillCardPanel().setHeadItem("def_6",null);
	       	 
	       	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //��Ʒ
	       	
	       	String pk_unit = getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //��λ
            
            UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"ladingamount")==null?"0":
               getBillCardPanel().getBodyValueAt(row,"ladingamount").toString());                                      //����
           
            //���ݵ�λת��һ���ۿ۶�  add by wb at 2008-5-31 15:19:15
            UFDouble rate = new PubTools().getInvRate(pk_invbasdoc, pk_unit);
            
            UFDouble zamount = amount.multiply(rate);//����
            
            UFDouble orderamount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"orderamount")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"orderamount").toString());    
            
            UFDouble fircut=new UFDouble(getBillCardPanel().getBodyValueAt(row,"def_10")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"def_10").toString());    //һ���ۿ��ܶ�
            
            UFDouble seccut=new UFDouble(getBillCardPanel().getBodyValueAt(row,"def_9")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"def_9").toString());    //�����ۿ��ܶ�
            
            UFDouble firdiscut = amount.div(orderamount).multiply(fircut);//����һ���ۿ�
            UFDouble secdiscut = amount.div(orderamount).multiply(seccut);//���ζ����ۿ�
            
            this.getBillCardPanel().setBodyValueAt(zamount, row, "zamount");//����
            
            this.getBillCardPanel().setBodyValueAt(firdiscut, row, "firstdiscount");
            this.getBillCardPanel().setBodyValueAt(secdiscut, row, "seconddiscount");
            UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(row,"price")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"price").toString());    //�����ۿ�
            
            UFDouble bcysje = amount.multiply(price).sub(firdiscut).sub(secdiscut);
            this.getBillCardPanel().setBodyValueAt(bcysje, row, "bcysje");
            
            String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            
            
            //ClientEventHandler.invoiceflag = 0;//�����۷�Ʊ��־��ʼ��
       }
        //---------------------------------------------------------------------------------
        
        //ѡ���Ʒʱ�����ۿ�,�޸ı����еĲ�Ʒ�������������еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
        if((strKey.equals("vinvbascode")||strKey.equals("ladingamount")||strKey.equals("dw")||strKey.equals("def_6"))&&invoiceflag != 2){
            String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();   //�ͻ�
            int row=getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //��Ʒ
            @SuppressWarnings("unused")
			UFDouble seconddisount  = new UFDouble(getBillCardPanel().getBodyValueAt(row,"def_7")==null?"0":
                								getBillCardPanel().getBodyValueAt(row,"def_7").toString());            
            ArrayList arr=new nc.ui.eh.trade.z0206005.ClientUI().getDiscount(pk_cubasdoc,pk_invbasdoc);
            HashMap hm=(HashMap)arr.get(0);                                                                       //һ���ۿ�
            @SuppressWarnings("unused")
			HashMap hm2=(HashMap)arr.get(1);
            
            @SuppressWarnings("unused")
			int rowcount=getBillCardPanel().getBillTable().getRowCount();
          
            String pk_unit = getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //��λ
            
            UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"ladingamount")==null?"0":
               getBillCardPanel().getBodyValueAt(row,"ladingamount").toString());                                      //����
           
            //���ݵ�λת��һ���ۿ۶�  add by wb at 2008-5-31 15:19:15
            UFDouble rate = new PubTools().getInvRate(pk_invbasdoc, pk_unit);
            UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString()).multiply(rate);
            getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), row, "firstdiscount");                //���ݿͻ��Ͳ�Ʒ����һ���ۿ�
           
//            UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString()).div(rate);//���ö����ۿ�
//           
//            for(int i=0;i<rowcount;i++){
//                String pk_inv=getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
//                    getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();     
//                if(pk_inv.equals(pk_invbasdoc)){
//                    UFDouble amount1=new UFDouble(getBillCardPanel().getBodyValueAt(i,"ladingamount")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"ladingamount").toString());     
//                    UFDouble firstdiscount1=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());  
//                    UFDouble price1=new UFDouble(getBillCardPanel().getBodyValueAt(i,"price")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"price").toString());
//                    
//                    UFDouble secondcount=price1.multiply(amount1).multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscount1); //�������õĶ����ۿ�
//                    
//                    UFDouble lsdiscount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"lsdiscount")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"lsdiscount").toString());  
//                    
//                    
//                        //������������ۿ�С���ۿ۱��ϵĿ����ۿ������ۿ۱��ϵĶ����ۿ�Ϊ�ۿ۶�
//                        if(seccount.compareTo(secondcount)<0){
//                            secondcount=seccount;
//                        }
//                        if(secondcount.doubleValue()<0){
//                            secondcount=new UFDouble(0);
//                        }
//                        
//                        
//                        UFDouble templediscount =(price1.multiply(amount1).multiply(IPubInterface.DISCOUNTRATE)).sub(firstdiscount1).sub(secondcount);
//                        
//                        //����һ���ۿۺͶ����ۿ�֮���Ƿ������ۿ��ܶ�,���������������粻��������ʱ�ۿ�
//                        if(templediscount.doubleValue()>0){
//                            UFDouble addlidiscount= lsdiscount.sub(templediscount);                     //ʣ����ʱ�ۿ۶�
//                            //����ʱ�ۿ۶�������Ҫ���ۿ۶�����϶���ۿ۶����������ʱ�ۿ�ȫ������
//                            if(addlidiscount.doubleValue()>0){
//                                getBillCardPanel().setBodyValueAt(secondcount.add(templediscount), i, "seconddiscount");  
//                                getBillCardPanel().setBodyValueAt(templediscount, i, "lsyydiscount");  
//                                getBillCardPanel().setBodyValueAt(addlidiscount, i, "lssydiscount");  
//                            }else{
//                                getBillCardPanel().setBodyValueAt(secondcount.add(lsdiscount), i, "seconddiscount");  
//                                getBillCardPanel().setBodyValueAt(lsdiscount, i, "lsyydiscount");  
//                                getBillCardPanel().setBodyValueAt(0, i, "lssydiscount");  
//                            }
//                        }else{
//                            getBillCardPanel().setBodyValueAt(secondcount, i, "seconddiscount");  
//                            getBillCardPanel().setBodyValueAt(0, i, "lsyydiscount");  
//                            getBillCardPanel().setBodyValueAt(lsdiscount, i, "lssydiscount");  
//                        }
//                        
//                        seccount=seccount.sub(secondcount);
//                        getBillCardPanel().setBodyValueAt(secondcount, i, "def_9");
//                }
//               
//            }
        }
            if((strKey.equals("vinvbascode")||strKey.equals("ladingamount")||strKey.equals("dw")||strKey.equals("def_6"))&&invoiceflag == 2){
            	
            	UFDouble ze=new UFDouble(0);                 //����Ӧ�ս��
            	//�����еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
                int rows=getBillCardPanel().getBillTable().getRowCount();
                for(int i=0;i<rows;i++){
                    UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
                        getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
                    ze=ze.add(bcysje);
                }
                try {
    	            getBillCardPanel().setHeadItem("bcyfje",ze);
    	           } catch (Exception e1) {
    				e1.printStackTrace();
    			}
            }
            
            if(strKey.equals("ladingamount")){
            	int rows=e.getRow();
            	String pk_measdoc=getBillCardPanel().getBodyValueAt(rows, "pk_measdoc")==null?"":
            		getBillCardPanel().getBodyValueAt(rows, "pk_measdoc").toString();
            	String pk_invbasdoc2 = getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc")==null?"":
            		getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc").toString();
            	UFDouble amounts=new UFDouble(getBillCardPanel().getBodyValueAt(rows, "ladingamount")==null?"-1000":
            		getBillCardPanel().getBodyValueAt(rows, "ladingamount").toString());
            	try {
					setUA(pk_measdoc,pk_invbasdoc2,amounts,rows);
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//add by houcq 2011-02-25 begin,���������ʱ����ձ�������ۿۺͱ�ͷ��̯�ۿ�
				getBillCardPanel().setHeadItem("def_6",null);
				int rows1 = getBillCardPanel().getBillTable().getRowCount();
				for (int i=0;i<rows1;i++)
				{
					getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");
					
					UFDouble bcysje=new UFDouble(0);
	                 
	                UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
	                    getBillCardPanel().getBodyValueAt(i,"vje").toString());
	              
	                UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
	                    getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
	                bcysje = vje.sub(firstdis);
	                getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
				}
				//add by houcq 2011-02-25 end
            }
            
            
            
            String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();                        //��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            
            
            //�����еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);                 //����Ӧ�ս��
            UFDouble vjeze=new UFDouble(0);                //�����ܶ�
            UFDouble firstdiscze=new UFDouble(0);          //һ���ۿ��ܶ�
            UFDouble seccountze=new UFDouble(0);          //�����ۿ��ܶ�
            for(int i=0;i<rows;i++){
                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
                ze=ze.add(bcysje);
                UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"vje").toString());
                vjeze=vjeze.add(vje);
                UFDouble scount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString());
                seccountze=seccountze.add(scount);
                UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
                firstdiscze=firstdiscze.add(firstdis);
            }
            try {
				LadingbillVO ladVO = (LadingbillVO)getChangedVOFromUI().getParentVO();
			
	            UFDouble lastzk = new UFDouble(ladVO.getDef_7()==null?"0":ladVO.getDef_7().toString());
	            getBillCardPanel().setHeadItem("bcyfje",ze);
	            getBillCardPanel().setHeadItem("seconddiscount",lastzk.sub(seccountze));                         // �����ۿ���� = �����ۿ۽��(�����ۿ�)-�������ö����ۿ�
	            getBillCardPanel().setHeadItem("dkze",vjeze);
//	            UFDouble def_8 = vjeze.multiply(IPubInterface.DISCOUNTRATE); 
	            UFDouble sczk = new UFDouble(0);
	            UFDouble zk = vjeze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze);// ���۶����ۿ� (���*40%-һ���ۿ��ܶ�)
	            if(zk.compareTo(new UFDouble(0))>0){//�����۶����ۿ�Ϊ����ʱ��0�滻
	            	sczk = zk;
	            }
	            getBillCardPanel().setHeadItem("def_8",sczk);  // ���۶����ۿ� (���*40%-һ���ۿ��ܶ�) edit by wb at 2008-7-10 12:25:07
            } catch (Exception e1) {
				e1.printStackTrace();
			}
       // }
        

        
        updateUI();
    }
    
    
    
    
    public void setDefaultData() throws Exception {
        String pk_corp = _getCorp().getPrimaryKey();
        
        getBillCardPanel().setHeadItem("ladingdate", _getDate());
        //add by houcq modify 2010-11-22 ���˻�������Ϊ�Զ���+30��
        getBillCardPanel().setHeadItem("enddate", _getDate().getDateAfter(30));
        //add by houcq 2010-12-09Ĭ��������Ǳ���
        getBillCardPanel().setHeadItem("self_flag",new UFBoolean(true));
        BillItem oper = getBillCardPanel().getTailItem("coperatorid");
        if (oper != null)
            oper.setValue(_getOperator());
        else
            getBillCardPanel().getHeadItem("coperatorid").setValue(_getOperator());
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null)
            date.setValue(_getDate());
        else
            getBillCardPanel().getHeadItem("dmakedate").setValue(_getDate());
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
       
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);

        getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());
        BillItem vbillstatus = getBillCardPanel().getHeadItem("vbillstatus");
        if (vbillstatus!= null)
            getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
    
    }
    
    /*
     * ע���Զ��尴ť
     * 2008-04-02
     */
    protected void initPrivateButton() {    	
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
        btn.setOperateStatus(new int[]{OperationState.EDIT,OperationState.ADD});
        addPrivateButton(btn);
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.TEMPLETDISCOUNT,"��ʱ�ۿ�","��ʱ�ۿ�");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
        addPrivateButton(btn1);
        super.initPrivateButton();
    }
    
    @Override
    protected void initSelfData() {
//    	getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
    	super.initSelfData();
    }

    @SuppressWarnings("unchecked")
	public HashMap getInvRate2(String pk_invbasdoc){
    	HashMap hm = new HashMap();
//    	StringBuffer sql = new StringBuffer()
//        .append(" select c.pk_measdoc,a.price,b.changerate from eh_invbasdoc a,eh_invbasdoc_b b,bd_measdoc c")
//        .append(" where a.pk_invbasdoc = b.pk_invbasdoc")
//        .append(" and b.pk_measdoc = c.pk_measdoc")
//        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ")
//        .append(" union all ")
//        .append(" select c.pk_measdoc,a.price,1 changerate")
//        .append(" from  eh_invbasdoc a,bd_measdoc c")
//        .append(" where a.pk_measdoc = c.pk_measdoc")
//        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(c.dr,0)=0");
    	//����SQL���
    	String sql = " select c.pk_measdoc,a.def3 price,b.mainmeasrate changerate "+
    				 " from bd_invbasdoc a,bd_convert b,bd_measdoc c "+
    				 " where a.pk_invbasdoc = b.pk_invbasdoc "+
    				 " and b.pk_measdoc = c.pk_measdoc "+
    				 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') "+
    				 " and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 "+
    				 " union all "+
    				 " select c.pk_measdoc,a.def3 price,1 changerate "+
    				 " from  bd_invbasdoc a,bd_measdoc c "+
    				 " where a.pk_measdoc = c.pk_measdoc "+
    				 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') "+
    				 " and nvl(a.dr,0)=0 and nvl(c.dr,0)=0 ";

        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
		    if(vc!=null&&vc.size()>0){
		    	for(int i=0; i<vc.size(); i++){
		    		Vector vcc = (Vector)vc.get(i);
		    		String pk_measdoc = vcc.get(0)==null?"":vcc.get(0).toString();
		    		UFDouble price = new UFDouble(vcc.get(1)==null?"0":vcc.get(1).toString());
		    		UFDouble changerate = new UFDouble(vcc.get(2)==null?"0":vcc.get(2).toString());
		    		hm.put(pk_measdoc, new UFDouble[]{price,changerate});
		    	}
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return hm;
    }
    
    /**
     * ��������λ�͸���������λ֮���װ��ϵ���������ı任
     * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
	public void setUA(String pk_measdoc,String pk_invbasdoc,UFDouble amount,int row) throws BusinessException{
    	String sql="select mainmeasrate changerate from bd_convert " +
    			" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
    			" where pk_invmandoc='"+pk_invbasdoc+"') " +
    			" and pk_measdoc='"+pk_measdoc+"' and nvl(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	UFDouble changerate=new UFDouble(-1000000);
    	for(int i=0;i<al.size();i++){
    		HashMap hm=(HashMap) al.get(i);
    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
    	}
//    	UFDouble fzamount=amount.div(changerate);
    	/**ԭ��ϵ���� 1�� = 25����1��=1/25�� 
    	 * ������ϵ����ά��Ϊ 1�� = 0.04��  �� ���� = ��������*ϵ�� edit by wb 2009��11��4��11:23:25***/
    	UFDouble fzamount=amount.multiply(changerate);
    	getBillCardPanel().setBodyValueAt(fzamount, row, "zamount");
    	
    }
    
    
    @SuppressWarnings("unchecked")
	public void setUA2(String pk_measdoc,String pk_invbasdoc,UFDouble zprice,int row) throws BusinessException{
    	String sql="select mainmeasrate changerate from bd_convert " +
				" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
				" where pk_invmandoc='"+pk_invbasdoc+"') " +
				" and pk_measdoc='"+pk_measdoc+"' and nvl(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	UFDouble changerate=new UFDouble(-1000000);
    	for(int i=0;i<al.size();i++){
    		HashMap hm=(HashMap) al.get(i);
    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
    	}
//    	UFDouble fzamount=zprice.div(changerate);
    	/**ԭ��ϵ���� 1�� = 25����1��=1/25�� 
    	 * ������ϵ����ά��Ϊ 1�� = 0.04��  �� ���� = ��������*ϵ�� edit by wb 2009��11��4��11:23:25***/
    	UFDouble fzamount=zprice.multiply(changerate);
    	getBillCardPanel().setBodyValueAt(fzamount, row, "price");
    }
    
    /***
	 * �õ���¼����ǰ2��(������)�����ϵ��������ɹ�δ��������������ѿ������δ������
	 * @param pk_invbasdoc ���Ϲ�����PK
	 * @return
	 * wb 2008-10-22 18:25:34
	 */
	@SuppressWarnings("unchecked")
	public UFDouble[] getUnckUnrk(String pk_invbasdoc,UFDate date){
		UFDouble[] unrksc = new UFDouble[2];
		String pk_corp = _getCorp().getPk_corp();
		UFDate beforeDate = date.getDateBefore(1);
		StringBuffer sql = new StringBuffer()
		.append(" select sum(isnull(a.pgamount,0)) pgamount,sum(isnull(a.scamount,0)) scamount")
		.append(" from ")
//		.append(" ---��¼����ǰ11��(������)�������ɹ�δ�������")
		.append(" (select b.pk_invbasdoc,sum(isnull(b.pgamount,0)) pgamount,0 scamount")
		.append(" from eh_sc_pgd a,eh_sc_pgd_b b")
		.append(" where a.pk_pgd = b.pk_pgd")
		.append(" and isnull(a.lock_flag,'N') <> 'Y'")
		.append(" and isnull(a.rk_flag,'N')<>'Y'")
		.append(" and isnull(a.xdflag,'N')='Y'")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"' ")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" and a.dmakedate between '"+beforeDate+"' and '"+date+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all ")
		//modify by houcq 2011-08-16
		//�ѿ������δ������
//		.append(" select b.pk_invbasdoc,0 pgamount,sum(isnull(b.zamount,0)) scamount")
//		.append(" from eh_ladingbill a,eh_ladingbill_b b")
//		.append(" where a.pk_ladingbill = b.pk_ladingbill")
//		.append(" and isnull(a.ck_flag,'N') <> 'Y'")
//		.append(" and isnull(a.lock_flag,'N') <> 'Y'")//add by houcq 2011-08-05
//		.append(" and isnull(b.isfull,'N') <> 'Y'")
//		.append(" and a.vbillstatus = 1")
//		.append(" and a.pk_corp = '"+pk_corp+"'")
//		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'  ")
//		.append(" and isnull(a.dr,0) = 0")
//		.append(" and isnull(b.dr,0) = 0")
//		.append(" group by b.pk_invbasdoc")
		.append(" select pk_invbasdoc, 0 pgamount,sum(nvl(zamount,0))-sum(nvl(outamount,0)) scamount from (")
		.append(" select a.pk_ladingbill_b,a.pk_invbasdoc,a.zamount,b.outamount from")
		.append(" (select b.pk_invbasdoc,b.pk_ladingbill_b, sum(nvl(b.zamount,0)) zamount")
		.append(" from eh_ladingbill a, eh_ladingbill_b b")
		.append(" where a.pk_ladingbill = b.pk_ladingbill and a.dmakedate <= '"+date+"'")
		//.append(" and nvl(a.dr,0) = 0 and a.vbillstatus = 1 and nvl(b.dr,0) = 0")
		.append(" and nvl(a.dr,0) = 0 and nvl(b.dr,0) = 0")//���������۳�����̬���ݵ������ ��
		.append(" and nvl(a.lock_flag,'N') = 'N' and nvl(a.ck_flag,'N') = 'N' and nvl(b.isfull,'N') = 'N'")
		.append(" and a.pk_corp = '"+pk_corp+"' and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		.append(" group by b.pk_ladingbill_b,b.pk_invbasdoc) a,")
		.append(" (select b.vsourcebillid, sum(nvl(b.outamount,0)) outamount from eh_icout a, eh_icout_b b")
		.append(" where a.pk_icout = b.pk_icout and nvl(a.dr,0) = 0 and a.vbillstatus = 1 and nvl(b.dr,0) = 0")
		.append(" and a.pk_corp='"+pk_corp+"' and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.vsourcebillid) b")
		.append(" where a.pk_ladingbill_b = b.vsourcebillid(+))")
		.append(" group by pk_invbasdoc")
		.append(" ) a");
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
    		ArrayList  arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				unrksc[0] = new UFDouble(hmA.get("scamount")==null?"0":hmA.get("scamount").toString());			//�ѿ������δ������
				unrksc[1] = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());			//�������ɹ�δ�������
    		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return unrksc;
	}
}

   
    

