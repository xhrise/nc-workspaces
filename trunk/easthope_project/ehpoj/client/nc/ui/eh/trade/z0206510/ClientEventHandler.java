package nc.ui.eh.trade.z0206510;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.eh.trade.pub.Tools;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.businessref.YsContractRefModel;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.pub.PubTool;
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * ���� ���֪ͨ��
 * @author �麣
 * 2008-04-08
 */

public class ClientEventHandler extends AbstractEventHandler {
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    public static int flag = 0;            // ���ӵ��ݱ��(���� 0,�����۶��� 1)
    public static int invoiceflag = 0; // ���ӵ��ݱ��(2 ���۷�Ʊ)
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }
    //add by houcq 2010-11-23��������ύ�󣬲�ѯ����������޸�
    protected void onBoQuery() throws Exception {
    	   super.onBoQuery();
           setBoEnabled();
    	}
    @Override
    protected void onBoEdit() throws Exception {
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("�������޸��������룡");
            return;
        }
        super.onBoEdit();
        int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        for(int i=0;i<row;i++){
            Object vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid");
            if(vsourcebillid!=null){
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvbascode", false);
            }
        }
        String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        YsContractRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
        getBillUI().updateUI();
        
    }
    
    /* ���� Javadoc��
     * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.TEMPLETDISCOUNT:    //��ʱ�ۿ�
                onBoTEMPLETDISCOUNT();
                break;
        }
        super.onBoElse(intBtn);
    }
    @SuppressWarnings("unchecked")
	@Override
    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("������ɾ���������룡");
            return;
        }
       //��д�����۶���
        LadingbillVO vo=(LadingbillVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        //add by houcq 2011-11-18 begin ��ǰҪɾ�������֪ͨ������ж����ۿ�,��ɾ���ۿ��ڼ��еĶ����ۿ�,�����ѿۼ������ָ�
        String pk_ladingbill = vo.getPk_ladingbill();
        if (pk_ladingbill!=null)
        {
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String sql ="delete from eh_perioddiscount where def_1='"+pk_ladingbill+"'";
        	pubitf.updateSQL(sql);
        	String sqlnew = "select pk_cubasdoc pk_cubasdoc,bcyfje kuye ,pk_corp pk_corp  from eh_ladingbill where pk_ladingbill = '"+pk_ladingbill+"'";
        	new Tools().changeCusOverage(sqlnew,"+");
        }
       //add by houcq 2011-11-18 end
        LadingbillBVO [] bvo=(LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        ArrayList alpk_order=new ArrayList();
        //��������
        String billtype = vo.getVsourcebilltype()==null?"":vo.getVsourcebilltype();
        if(billtype.equals("ZA09")){
        for(int i=0;i<bvo.length;i++){
        	String pk_main=bvo[i].getVsourcebillrowid()==null?"":bvo[i].getVsourcebillrowid().toString();
        	if(!pk_main.equals("")){
        		alpk_order.add(pk_main);
        	}
        }
        if(alpk_order!=null&&alpk_order.size()>0){
        	String [] pk_order=(String[]) alpk_order.toArray(new String[alpk_order.size()]);
        	String pk_orde=PubTool.combinArrayToString(pk_order);
	        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String sqlupdate="update eh_order set th_flag='N' where pk_order in"+pk_orde;
			pubitf.updateSQL(sqlupdate);
        	
        }
        //end 
        }else if(billtype.equals("ZA14")){
        //-------------------------��д�����۷�Ʊ-----------------
        for(int i=0;i<bvo.length;i++){
        	String pk_main=bvo[i].getVsourcebillrowid()==null?"":bvo[i].getVsourcebillrowid().toString();
        	if(!pk_main.equals("")){
        		alpk_order.add(pk_main);
        	}
        }
        if(alpk_order!=null&&alpk_order.size()>0){
        	String [] pk_order=(String[]) alpk_order.toArray(new String[alpk_order.size()]);
        	String pk_orde=PubTool.combinArrayToString(pk_order);
	        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String sqlupdate="update eh_invoice set th_flag='N' where pk_invoice in"+pk_orde;
			pubitf.updateSQL(sqlupdate);
        }
        //------------------------------------------
        }
        super.onBoTrueDelete();
    }
    
        //�ύ
        public void onBoCommit() throws Exception {
            // TODO �Զ����ɷ������
            String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
            if(!coperatorid.equals(_getOperator())){
                getBillUI().showErrorMessage("�������������������ύ��");
                return;
            }
           
            LadingbillVO  ladVO = (LadingbillVO)getBillUI().getChangedVOFromUI().getParentVO();            
            //add by houcq 2011-15 begin
            String billId=ladVO.getPk_ladingbill();
            if (billId!=null)
            {
            	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                pubitf.kjye(billId,_getDate());
            }
            else
            {
            	 getBillUI().showErrorMessage("����Ϊ��,�������ύ!");
                 return;
            }
          //add by houcq 2011-15 end
            super.onBoCommit();
        }       
        @SuppressWarnings("unchecked")
		public void onButton_N(ButtonObject bo, BillModel model) {
        	
        	flag = 0;            // ���ӵ��ݱ��(���� 0,�����۶��� 1)
        	invoiceflag = 0; 	// ���ӵ��ݱ��(2 ���۷�Ʊ)
        	String bocode=bo.getCode()==null?"":bo.getCode();        	
        	if(bocode.equals("���Ƶ���")||bocode.equals("���۶���")||bocode.equals("���۷�Ʊ")){

       		 //���ۿ��ڼ�����������������ݡ����ù��ܣ��κ�һ�����ܱ�Ǻ󣬾Ͳ��������ڸ��¶���¼���ۿ۵�������add by houcq 2011-07-07
        		StringBuffer str = new StringBuffer()
        		.append(" SELECT * FROM eh_perioddiscount_h ")
        		.append(" WHERE nyear = "+_getDate().getYear())
        		.append(" AND nmonth = "+_getDate().getMonth())
        		.append(" and pk_corp = '"+_getCorp().getPrimaryKey()+"'")
        		.append(" AND NVL(dr,0)=0 ")
        		.append(" and (scxy_flag='Y' or qy_flag='Y')");
        		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        		try {
        			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
        			if(arr!=null && arr.size()>0){
        				getBillUI().showErrorMessage("�����ۿ��ڼ������ѽ�ת����ֹ�µ��Ƶ�����!");
        				return;
        	        }
        		} catch (BusinessException e) {
        			e.printStackTrace();
        		}
                 /**��������糬24Сʱû�м���ӯ������ϵͳ���������ٿ������ add by zqy 2008��10��22��18:20:10*/
                 String befordate = _getDate().getDateBefore(2).toString();     //ϵͳ��½ǰ2��
                 String bjdate = null;    //�Ƚ�����
                 if(befordate.substring(8, 9).equals("0")){
                     bjdate = befordate.substring(9, 10);
                 }else{
                     bjdate = befordate.substring(8, 10);
                 }
                 
                 ArrayList list = new ArrayList();
                 StringBuffer sql = new StringBuffer()
                 .append(" select vdate from eh_trade_checkdate where pk_corp='"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0 ");
                 try {
                     ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                     if(arr!=null && arr.size()>0){
                         String date = null;  //��������
                         for(int i=0;i<arr.size();i++){
                             HashMap hm = (HashMap)arr.get(i);
                             date = hm.get("date")==null?"":hm.get("date").toString();
                             if(date.equals(bjdate)){
                                 list.add("Y");
                             }
                         }                   
                     }
                 } catch (BusinessException e1) {
                     e1.printStackTrace();
                 }
                 String SQL = " select * from eh_trade_surpluscheck where dmakedate='"+befordate+"' and isnull(dr,0)=0 and pk_corp='"+_getCorp().getPk_corp()+"' ";
                 ArrayList all = null;
                 try {
                     all = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
                 } catch (BusinessException e1) {
                     e1.printStackTrace();
                 }
                 if(list.size()>0 && list!=null && (all==null||all.size()==0) ){
                     getBillUI().showErrorMessage("Ŀǰ�ѳ�������ӯ������24Сʱ,ϵͳ�������������!");
                     return;
                 }
                 
                 //�Թ�˾�ڳ����˽��п���ʱ�䣺2009-12-15���ߣ���־Զ
                 if(!super.getEveDiscount()){
                	 this.getBillUI().showErrorMessage("�����ڼ��ۿ�δ���ʣ������ۿ��ڼ����ڵ㣬���б����ۿ��ڳ�����");
                	 return;
                 }else{
                	//���ܣ��Ա����ڳ��ۿ۵Ŀ��� ʱ�䣺2009-12-15���ߣ���־Զ
                	 if(!super.getDiscount()){
                    	 this.getBillUI().showErrorMessage("�����ڼ��ۿ�δ���㣬�����ۿ��ڼ����ڵ㣬�����������ݣ�");
                    	 return;
                     }
                 }
                //ȡ����ʱ�ۿ�BUTTON
             	//getButtonManager().getButton(IEHButton.TEMPLETDISCOUNT).setEnabled(true);
                 try {
                     getBillUI().updateButtonUI();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
        	 
        	
        	super.onButton_N(bo, model);
        	//add by houcq 2011-05-06 begin ����Ӫ�������������
            Object pk_psndoc =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getValueObject();
         	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
         	if (pk_psndoc!=null)
         	{
         		String sql ="select pk_deptdoc from bd_psndoc where pk_psndoc='"+pk_psndoc+"'";
             	Object obj;
 				try {
 					obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
 					getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_2",obj);
 				} catch (BusinessException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}            	
         	}
         	//add by houcq 2011-05-06 end
            //�������۶������������ʱ���ͻ��Ͳ�Ʒ������༭��ͬʱ���岻�ܽ����в���
            if(bocode.equals("���۶���")){
              flag = 1;
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);           
              int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            try {
              AggregatedValueObject vo = getBillCardPanelWrapper().getBillVOFromUI();
              for(int i=0;i<row;i++){
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvbascode", false);
                 /**�������ϵ��ѿ������δ������,�������ɹ�δ�������  add by wb at 2008-10-22 18:51:11*/
                 String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
               	  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
     			UFDouble[] unckrk = ((ClientUI)getBillUI()).getUnckUnrk(pk_invbasdoc,_getDate());
//     			UFDouble kcamount = new PubTools().getKCamountByinv_Back(pk_invbasdoc, _getCorp().getPk_corp(),_getDate());
			//HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "1", _getCorp().getPk_corp());
			//UFDouble kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
			//modify by houcq 2011-06-20�޸�ȡ��淽��
			UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);;
			
     			if(unckrk!=null&&unckrk.length==2){
     				UFDouble ytwcamount = unckrk[0];		//�ѿ������δ������
     				UFDouble ypgwrkamount = unckrk[1];		//�������ɹ�δ�������
     				UFDouble maxthamount = kcamount.sub(ytwcamount).add(ypgwrkamount);		//��������
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kcamount, i, "storeamount"); //ʵ�ʿ����
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(ytwcamount, i, "ytwcamount");
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(ypgwrkamount, i, "ypgwrkamount");
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(maxthamount, i, "maxthamount");
     			}
     			//getButtonManager().getButton(IEHButton.TEMPLETDISCOUNT).setEnabled(true);
                //����һ����houcqע��2010-12-09����ٶ�����
     			//getBillUI().updateButtonUI();
     			/********************************** end ********************************************/
     			 
     			/***�Ӷ��������ʱ���������Ĭ�� add by wb 2008-12-23 9:58:48****************************************/
     			
     			setBcthamount(vo);
     			
     			/************* end **********************************/
				}
              }catch (Exception e) {
					e.printStackTrace();
			  }
     			
              }
            
            //�������۶������������ʱ���ͻ��Ͳ�Ʒ������༭��ͬʱ���岻�ܽ����в���
            if(bocode.equals("���۷�Ʊ")){
              invoiceflag = 2;
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
              int i = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
              for(int j=0;j<i;j++){
            	  this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").setEnabled(false);
              }
     			
            }
            
            getBillUI().updateUI();
            
        }
       
        /***
         * ��ͨ�������������֪ͨ��ʱ�����������������Ĭ�ϣ��Լ���Ӧ���ۿ�
         * add by wb 2008-12-22 18:33:16 ��������
         * @param i �к�
         * @throws Exception 
         */
        @SuppressWarnings("unchecked")
		public void setBcthamount(AggregatedValueObject vo) throws Exception{
        	LadingbillVO hvo = (LadingbillVO)vo.getParentVO();
        	LadingbillBVO[] bvos = (LadingbillBVO[])vo.getChildrenVO();
        	String pk_cubasdoc = hvo.getPk_cubasdoc();
        	UFDouble ze=new UFDouble(0);                	 	//����Ӧ�ս��
            UFDouble vjeze=new UFDouble(0);               	 	//�����ܶ�
            UFDouble firstdiscze=new UFDouble(0);         		//һ���ۿ��ܶ�
            UFDouble seccountze=new UFDouble(0);          		//�����ۿ��ܶ�
        	HashMap hm = getCustZK(pk_cubasdoc);				//�ͻ�һ���ۿ�
            for(int i=0;i<bvos.length;i++){
        		LadingbillBVO bvo = bvos[i];
        		String pk_invbasdoc = bvo.getPk_invbasdoc();
        		UFDouble orderamount = bvo.getOrderamount()==null?new UFDouble(0):bvo.getOrderamount();		//��������
            	UFDouble ytamount = bvo.getYtamount()==null?new UFDouble(0):bvo.getYtamount();				//��������
            	UFDouble bcthamount = orderamount.sub(ytamount);											//�����������(��)
             	String pk_measdoc = bvo.getPk_measdoc();				//������λ
            	UFDouble amount = getMainUnitamount(pk_invbasdoc, pk_measdoc, bcthamount);					//�����������(��)
            	UFDouble fisdiscount = new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString()).multiply(amount);
            	UFDouble price = bvo.getZprice();
            	UFDouble bcysje = amount.multiply(price).sub(fisdiscount);
            	UFDouble zje = amount.multiply(price);
            	ze = ze.add(bcysje);							//����Ӧ�ս��
            	vjeze = vjeze.add(zje);							//������
            	firstdiscze = firstdiscze.add(fisdiscount);		//һ���ۿ��ܶ�
            	//add by houcq 2011-05-25 begin
            	UFDouble seconddiscount = new UFDouble(bvo.getSeconddiscount()==null?"0":bvo.getSeconddiscount().toString()).multiply(amount);
            	UFDouble bjj =amount.multiply(price).sub(fisdiscount).sub(seconddiscount).div(bcthamount);
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bjj, i, "def_7");		//������
            	//end
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcthamount, i, "ladingamount");		//����	
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amount, i, "zamount");				//����
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(fisdiscount, i, "firstdiscount");	//һ���ۿ�
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(zje, i, "vje");						//�ܽ��
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");				//����Ӧ�ս��
        	}
            //���ܽ������Ϣ������ͷ
            UFDouble lastzk = new UFDouble(hvo.getDef_7()==null?"0":hvo.getDef_7().toString());
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bcyfje",ze);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("seconddiscount",lastzk.sub(seccountze));                         // �����ۿ���� = �����ۿ۽��(�����ۿ�)-�������ö����ۿ�
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("dkze",vjeze);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_8",vjeze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze));  // ���۶����ۿ� (���*40%-һ���ۿ��ܶ�) 
            
            //�˻���� 
            UFDouble overage = new PubTools().getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//���ҿͻ����
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sxje",overage);
        }
        
        /***
         * �ͻ���һ���ۿ�
         * @param pk_cubasdoc
         * @return
         */
        @SuppressWarnings("unchecked")
		public HashMap getCustZK(String pk_cubasdoc){
        	HashMap hm = new HashMap();
        	try {
                IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                //ȡ�øÿͻ������ϵ�һ���ۿ۽��
                StringBuffer sql=new StringBuffer("select b.pk_invbasdoc||b.pk_cubasdoc keys,isnull(b.newdiscount,0) value from eh_firstdiscount a ")
                .append(" ,eh_firstdiscount_b b where a.pk_firstdiscount=b.pk_firstdiscount and '"+_getDate()+"' between b.zxdate ") // �������±��
                .append(" and b.yxdate and a.pk_corp='"+_getCorp().getPk_corp()+"' and b.pk_cubasdoc='"+pk_cubasdoc+"' and isnull(a.def_1,'N')='Y'   and isnull(a.lock_flag,'N')='N'  and isnull(b.lock_flag,'N')='N' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0  and vbillstatus="+IBillStatus.CHECKPASS+"");
                ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                if(!arr.isEmpty()){
                    for(int i=0;i<arr.size();i++){
                        HashMap hmA = (HashMap) arr.get(i);
                        hm.put(hmA.get("keys").toString(),hmA.get("value").toString());
                    }
                }
        	}catch (Exception ex) {
        		ex.printStackTrace();
			}
			return hm;
        }
        
        /***
         * ���ݸ�����λ�����������������λ���� 
         * @param pk_invbasdoc
         * @param pk_measdoc
         * @param fzamount
         * @return
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
		public UFDouble getMainUnitamount(String pk_invbasdoc,String pk_measdoc,UFDouble fzamount) throws Exception{
        	String sql="select mainmeasrate changerate " +
        			" from bd_convert where " +
        			" pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') " +
        			" and pk_measdoc='"+pk_measdoc+"' and nvl(dr,0)=0";
        	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
        	UFDouble changerate=new UFDouble(-1000000);
        	for(int i=0;i<al.size();i++){
        		HashMap hm=(HashMap) al.get(i);
        		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
        	}
        	UFDouble amount=fzamount.multiply(changerate);
        	return amount;
        }
        
        @SuppressWarnings({ "unchecked" })
		@Override
        public void onBoSave() throws Exception {
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            //Ψһ��У��
            BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
            
            int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","vsourcebillid"});
            if(res==1){
                getBillUI().showErrorMessage("�������ظ���");
                return;
            }
            /**������������ܴ����������� edit by wb at 2008-10-23 18:21:34*/
            LadingbillVO  ladVO = (LadingbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
            LadingbillBVO[] bvos = (LadingbillBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			for(int i=0; i<bvos.length; i++){
				LadingbillBVO bvo = bvos[i];
				UFDouble ladingamount = bvo.getZamount()==null?new UFDouble(0): bvo.getZamount(); 								// �����������
				UFDouble maxthamount = bvo.getMaxthamount()==null?new UFDouble(0):bvo.getMaxthamount();                     	// ����������
				double subamount = ladingamount.sub(maxthamount).doubleValue();   	// �����������-����������
			    if(subamount>0){
			    	getBillUI().showErrorMessage("��"+(i+1)+"�����������������������,��ȷ�ϱ����������!");
                    return;			    	
			    }
			   //add by houcq 2011-03-14 begin
			    if (bvo.getBcysje()==null || bvo.getBcysje().toDouble()==0)
			    {
			    	getBillUI().showErrorMessage("��"+(i+1)+"�б���Ӧ�ս���Ϊ�պ���!");
                    return;		
			    }
			  //add by houcq 2011-03-14 end
			  //add by houcq 2011-04-14 begin �����ж�����ܽ��-һ���ۿ�-�����ۿ۽��<>����Ӧ�ս����ʾ
			  UFDouble vje=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
			    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje").toString());
			  UFDouble firstdiscount=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
		    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
			  UFDouble seconddiscount=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
		    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString());
			  UFDouble bcysje=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
		    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
			  UFDouble temp =bcysje.sub(vje.sub(firstdiscount).sub(seconddiscount));
			  if (temp.toDouble()!=0)
			  {
				  getBillUI().showErrorMessage("��"+(i+1)+"���ܽ��-һ���ۿ�-�����ۿ۲����ڱ���Ӧ��/�˽��,����!");
                  return;	
			  }
			//add by houcq 2011-04-14 end
			}
			/**********************  end  *******************************/
			
			String vsourceBillType = ladVO.getVsourcebilltype() == null?"":ladVO.getVsourcebilltype();
			
    		AggregatedValueObject aggVO = getBillCardPanelWrapper().getBillVOFromUI();
    		LadingbillBVO[] bvs = (LadingbillBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
    		if(isEditing() && aggVO!=null && aggVO.getChildrenVO()!=null && aggVO.getParentVO().getPrimaryKey().length()>0){
            	LadingbillBVO[] bvoss = (LadingbillBVO[])aggVO.getChildrenVO();
            	IVOPersistence iVOPersistence =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            	iVOPersistence.updateVOArray(bvoss);
            	getBillUI().updateUI();
            }
    		
    		UFDouble zje = new UFDouble(0);			//�ܽ��
    		for(int i=0;i<bvs.length;i++){
    			UFDouble bcysje = bvs[i].getBcysje();
    			zje = zje.add(bcysje);	
    		}
    		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bcyfje", zje);//���ܽ��
    		super.onBoSave();
   		    //add by houcq 2011-06-01 begin modify by 2011-06-09
    		LadingbillVO   vo =(LadingbillVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
            new PubTools().recordTime(vo.getVbilltype(), vo.getPk_ladingbill(), vo.getPk_corp(), vo.getCoperatorid());
            //add by houcq end
    		/**�����ݱ����ز��Ҳ��Ǳ�������ڱ���ʱ�����Ƿ���Ҫ�޸������ͬ add by wb at 2008-11-18 11:21:55**/
    		String pk_ladingbill = ladVO.getPk_ladingbill();
    		UFBoolean self_flag = ladVO.getSelf_flag();
    		if(!self_flag.booleanValue()&&pk_ladingbill!=null&&pk_ladingbill.length()>0){
    			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    			StringBuffer sql = new StringBuffer()
    			.append(" select * from pub_workflownote where pk_wf_task in ")
    			.append(" ( select pk_wf_task from pub_wf_task where pk_wf_instance in ")
    			.append(" ( select pk_wf_instance from pub_wf_instance where billid = '"+pk_ladingbill+"' ) and tasktype != 1 ")
    			.append(" )")
    			.append(" and ischeck = 'X' and approvestatus = 4 ");
    			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
    			if(al!=null&&al.size()>0){
    				getBillUI().showWarningMessage("���ݱ����أ���ȷ�������ͬ�Ƿ���Ҫ�޸�?");
    			}
    		}
            /************************** end *******************************/
    		
    		//�����֪ͨ�������ʱ���д��ǵĹ���д��һ��������,��Ϊ2�����(�쳣����쳣) add by zqy 2010-11-20 14:18:19
    		UpdateFlag(vsourceBillType);
    
    		super.onBoRefresh();
        }
        
        /**
         * �����֪ͨ�������ʱ���д��ǵĹ���д��һ��������,��Ϊ2�����(�쳣����쳣) add by zqy 2010-11-20 14:18:19
         * @param vsourceBillType ��������
         */
        @SuppressWarnings({ "unchecked", "null" })
		private void UpdateFlag(String vsourceBillType) throws Exception {
			try {
				if(vsourceBillType.equals("ZA09")){
	    			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		            LadingbillBVO[] reBVO=(LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		    		String [] pk_order_b=new String [reBVO.length];//��������ӱ��PK
		    		String [] pk_ladingbill_b =new String [reBVO.length];//����Լ��ӱ��PK
		    		@SuppressWarnings("unused")
					String info="";
		    		for(int i=0;i<reBVO.length;i++){
		    			pk_order_b[i] = reBVO[i].getVsourcebillid()==null?"":reBVO[i].getVsourcebillid().toString(); //��Դ�����ӱ�����
		    			pk_ladingbill_b[i]=reBVO[i].getPk_ladingbill_b()==null?"":reBVO[i].getPk_ladingbill_b().toString();  //�ӱ�����
		    		}
		    		String pksql2=PubTool.combinArrayToString(pk_ladingbill_b); 		//ת����('','')��ʽ    
	                 
		    		 //�Լ����е���������������PK��ȫ��������
		    		HashMap hmde=new HashMap();//����������PK�Ͷ�Ӧ���������������ͽ�Ҫ���������ĺ�
		    		for(int i=0;i<reBVO.length;i++){
		    			String pk_in=reBVO[i].getVsourcebillrowid()==null?"":reBVO[i].getVsourcebillrowid().toString();//���ε�������PK�ļ���
		    			UFDouble scmount=new UFDouble(reBVO[i].getLadingamount()==null?"0":reBVO[i].getLadingamount().toString());//�����ϵ���������
		    			if(!pk_in.equals("")){
		    				if(hmde.containsKey(pk_in)){
		    					UFDouble old=new UFDouble(hmde.get(pk_in)==null?"":hmde.get(pk_in).toString());
		    					UFDouble newmount=scmount.add(old);
		    					hmde.put(pk_in, newmount);
		    				}else{
		    					hmde.put(pk_in, scmount);
		    				}
		    			}
		    		}
		    		//�ҵ���Ӧ������ pk�ĵ�������ֵ
		    		Object [] keyset=(Object [])hmde.keySet().toArray();
		    		if(keyset!=null||keyset.length<=0){
		    			String [] pk_orde=new String [keyset.length];
		    			for(int i=0;i<keyset.length;i++){
		    				pk_orde[i]=(String) keyset[i];
		    			}
		    			String pk_or=PubTool.combinArrayToString(pk_orde);
		    			StringBuffer sql2 = new StringBuffer("")
		    			.append(" select pk_order pk,sum(isnull(amount,0)) amount,'A' flag  from eh_order_b")
		    			.append(" where pk_order in "+pk_or+" and  isnull(dr,0)=0 group by  pk_order ")
		    			.append(" union all ")
		    			.append("select vsourcebillrowid pk, sum(isnull(ladingamount,0)) amount ,")
		    			.append("'B' flag from eh_ladingbill_b where vsourcebillrowid in "+pk_or+" and pk_ladingbill_b not in "+pksql2+" ")
		    			.append(" and isnull(dr,0)=0 group by vsourcebillrowid ");
		    			
		    			
		    			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql2.toString(),new MapListProcessor());
		    			HashMap hmA=new HashMap();//������������������ ��Ӧ�����ε������µ���������
		    			HashMap hmB=new HashMap();
		    			for(int i=0;i<al.size();i++){
		    				HashMap hm=(HashMap) al.get(i);
		    				String pk_in=hm.get("pk")==null?"":hm.get("pk").toString();
		    				UFDouble amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
		    				String flag=hm.get("flag")==null?"":hm.get("flag").toString();
		    				if(flag.equals("A")){
		    					hmA.put(pk_in, amount);	
		    				}
		    				if(flag.equals("B")){
		    					hmB.put(pk_in, amount);
		    				}
		    						
		    			}
		    			ArrayList alac=new ArrayList();
		    			for(int i=0;i<keyset.length;i++){
		    				String pk_order=keyset[i]==null?"":keyset[i].toString();
		    				//�����ϵ�ֵ
		    				UFDouble panel=new UFDouble(hmde.get(pk_order)==null?"0":hmde.get(pk_order).toString());
		    				//�Լ����е�ֵ
		    				UFDouble Myamount=new UFDouble(hmB.get(pk_order)==null?"0":hmB.get(pk_order).toString());
		    				//���ο��е�ֵ
		    				UFDouble foramount=new UFDouble(hmA.get(pk_order)==null?"0":hmA.get(pk_order).toString());		//��������
		    				
		    				UFDouble add=Myamount.add(panel);			//�������
		    				UFDouble a=add.sub(foramount);
		    				/**������������ڻ���ڶ�������ʱ�ر����ζ��� edit by wb at 2008-10-23 18:02:06*/
		    				if(a.toDouble()>=0){
		    					alac.add(pk_order);
		    					
		    				}			
		    			}
		    			String [] ac=(String[]) alac.toArray(new String [alac.size()]);
		    			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		    			if(keyset!=null||keyset.length<=0){
		    				String sqlupdate="update eh_order set th_flag='N' where pk_order in"+pk_or;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    			if(ac!=null){
		    				String df=PubTool.combinArrayToString(ac);
		    				String sqlupdate="update eh_order set th_flag='Y' where pk_order in"+df;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    		}
		    		
		    		//�����۶����е����֪ͨ���Ĺرձ�Ǻ����۶������������񵥵Ĺرձ�Ƕ��߶��ر�ʱ���������۶���������ر�(lock_flag) add by zqy 2008-10-6 9:27:16
		            LadingbillBVO[] lbvo = (LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		            int length = lbvo.length;
		            ArrayList list = new ArrayList();//������۶����ӱ�Vsourcebillrowid��Ϊ�����PK
		            for(int i=0;i<length;i++){
		                String vsourcebillrowid = lbvo[i].getVsourcebillrowid()==null?"":lbvo[i].getVsourcebillrowid().toString();//���۶���PK
		                list.add(vsourcebillrowid);
		            }
		            StringBuffer alsql = new StringBuffer();
		            for (int i = 0; i < list.size(); i++) {
		                alsql.append("'");
		                alsql.append(list.get(i));
		                alsql.append("'");
		                if ((i + 1) < list.size()) {
		                    alsql.append(",");
		                } else {
		                    alsql.append("");
		                }
		            }          
		            //������Դ���ݵ�PK������Ӧ�����ݣ����scrw_flag��th_flag�����ϱ��ʱ��������۶����Ĺرհ�ť��ر�
		            StringBuffer sql = new StringBuffer()
		            .append(" select scrw_flag,th_flag,pk_order from eh_order where pk_order in ("+alsql+") and isnull(dr,0)=0 ");
		            ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		            if(arr!=null && arr.size()>0){
		                for(int j=0;j<arr.size();j++){
		                    HashMap hm = (HashMap) arr.get(j);
		                    String scrw_flag = hm.get("scrw_flag")==null?"":hm.get("scrw_flag").toString();//�������񵥵Ĺرձ��
		                    String th_flag = hm.get("th_flag")==null?"":hm.get("th_flag").toString();//���֪ͨ���Ĺرձ��
		                    String pk_order = hm.get("pk_order")==null?"":hm.get("pk_order").toString();//���۶�����PK
		                    if(("Y".equals(scrw_flag)) && ("Y".equals(th_flag))){
		                        String updatesql = " update eh_order set lock_flag='Y' where pk_order='"+pk_order+"' ";
		                        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		                        pubitf.updateSQL(updatesql);
		                    }
		                }
		            }
	    	}else if(vsourceBillType.equals("ZA14")){
	    		int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
	    		UFDouble za = new UFDouble(0);
	    		for(int i = 0;i<row;i++){
	    			
	    			//��������ֵ���˻�����
	    			UFDouble orderamount=  new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "orderamount")==null?"0":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "orderamount").toString());
	    			//��������ֵ����������
	    			UFDouble ytamount=  new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ytamount")==null?"0":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ytamount").toString());
	    			//��������ֵ�������������
	    			UFDouble ladingamount=  new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?"0":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount").toString());
	    			
	    			if(ladingamount.abs().compareTo(orderamount.sub(ytamount.abs()))>0){
	    				this.getBillUI().showErrorMessage("��"+(i+1)+"���������������������,����������!");
	    				return;
	    			}
	    		}
	    	
	    		PubItf  pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
	            AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
	            LadingbillBVO[] ibvos=(LadingbillBVO[])aggvo.getChildrenVO();
	    	       String pk_invoce  = null;
	    	        for(int i=0;i<ibvos.length;i++){
	    	        	LadingbillBVO ibvo=(ibvos[i]);
	    	        	pk_invoce = ibvo.getVsourcebillrowid();
	    	        	za = za.add(ibvo.getZamount());
	    	        }
	    	        //����Ʊ�ϵ��������˻����ϵ������ó����Ƚ�
	    	        StringBuffer sql1=new StringBuffer("")
	    	        .append(" select pk_invoice pk,sum(nvl(amount,0)) amount,'A' flag from eh_invoice_b where ")
	    	        .append("  pk_invoice = '"+pk_invoce+"' and nvl(dr,0)=0 group by pk_invoice ")
	    	        .append("  union select vsourcebillrowid pk,abs(sum(nvl(zamount,0))) amount,'B' flag from eh_ladingbill_b where ")
	    	        .append("  vsourcebillrowid = '"+pk_invoce+"' and nvl(dr,0)=0 group by vsourcebillrowid ");
	    	        
	    	        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    	        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql1.toString(),new MapListProcessor());
	    	        if(arr!=null&&arr.size()>0){
	    				HashMap hma = new HashMap();
	    				for(int i=0; i<arr.size(); i++){
	    					HashMap hm=(HashMap)arr.get(i);
	    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
	    					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
	    					hma.put(flag, amount);
	    				}
	    				String updateSQL = null;
	    				//��Ʊ������
	    				UFDouble voiceA = new UFDouble(hma.get("A")==null?"0":hma.get("A").toString());
	    				//�ᵥ������
	    				UFDouble ladingB = new UFDouble(hma.get("B")==null?"0":hma.get("B").toString());
	    				//ȫ���˻�ʱ�����۷�Ʊ�ر�
	    				//voiceA.compareTo(ladingB.add(za.abs())��ΪvoiceA.compareTo(ladingB) modify by houcq 2010-12-10
	    				if(voiceA.compareTo(ladingB) ==0){
	    					updateSQL = "update eh_invoice set th_flag = 'Y' where pk_invoice = '"+pk_invoce+"'";
	    				}
	    				else{
	    					updateSQL = "update eh_invoice set th_flag = 'N' where pk_invoice = '"+pk_invoce+"'";
	    				}
	    				pubitf.updateSQL(updateSQL);
	    			}
	    	}
				
			} catch (Exception e) {
				if(vsourceBillType.equals("ZA09")){
	    			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		            LadingbillBVO[] reBVO=(LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		    		String [] pk_order_b=new String [reBVO.length];//��������ӱ��PK
		    		String [] pk_ladingbill_b =new String [reBVO.length];//����Լ��ӱ��PK
		    		for(int i=0;i<reBVO.length;i++){
		    			pk_order_b[i] = reBVO[i].getVsourcebillid()==null?"":reBVO[i].getVsourcebillid().toString(); //��Դ�����ӱ�����
		    			pk_ladingbill_b[i]=reBVO[i].getPk_ladingbill_b()==null?"":reBVO[i].getPk_ladingbill_b().toString();  //�ӱ�����
		    		}
		    		String pksql2=PubTool.combinArrayToString(pk_ladingbill_b); 		//ת����('','')��ʽ    
	                 
		    		 //�Լ����е���������������PK��ȫ��������
		    		HashMap hmde=new HashMap();//����������PK�Ͷ�Ӧ���������������ͽ�Ҫ���������ĺ�
		    		for(int i=0;i<reBVO.length;i++){
		    			String pk_in=reBVO[i].getVsourcebillrowid()==null?"":reBVO[i].getVsourcebillrowid().toString();//���ε�������PK�ļ���
		    			UFDouble scmount=new UFDouble(reBVO[i].getLadingamount()==null?"0":reBVO[i].getLadingamount().toString());//�����ϵ���������
		    			if(!pk_in.equals("")){
		    				if(hmde.containsKey(pk_in)){
		    					UFDouble old=new UFDouble(hmde.get(pk_in)==null?"":hmde.get(pk_in).toString());
		    					UFDouble newmount=scmount.add(old);
		    					hmde.put(pk_in, newmount);
		    				}else{
		    					hmde.put(pk_in, scmount);
		    				}
		    			}
		    		}
		    		//�ҵ���Ӧ������ pk�ĵ�������ֵ
		    		Object [] keyset=(Object [])hmde.keySet().toArray();
		    		if(keyset!=null||keyset.length<=0){
		    			String [] pk_orde=new String [keyset.length];
		    			for(int i=0;i<keyset.length;i++){
		    				pk_orde[i]=(String) keyset[i];
		    			}
		    			String pk_or=PubTool.combinArrayToString(pk_orde);
		    			StringBuffer sql2 = new StringBuffer("")
		    			.append(" select pk_order pk,sum(isnull(amount,0)) amount,'A' flag  from eh_order_b")
		    			.append(" where pk_order in "+pk_or+" and  isnull(dr,0)=0 group by  pk_order ")
		    			.append(" union all ")
		    			.append("select vsourcebillrowid pk, sum(isnull(ladingamount,0)) amount ,")
		    			.append("'B' flag from eh_ladingbill_b where vsourcebillrowid in "+pk_or+" and pk_ladingbill_b not in "+pksql2+" ")
		    			.append(" and isnull(dr,0)=0 group by vsourcebillrowid ");
		    			
		    			
		    			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql2.toString(),new MapListProcessor());
		    			HashMap hmA=new HashMap();//������������������ ��Ӧ�����ε������µ���������
		    			HashMap hmB=new HashMap();
		    			for(int i=0;i<al.size();i++){
		    				HashMap hm=(HashMap) al.get(i);
		    				String pk_in=hm.get("pk")==null?"":hm.get("pk").toString();
		    				UFDouble amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
		    				String flag=hm.get("flag")==null?"":hm.get("flag").toString();
		    				if(flag.equals("A")){
		    					hmA.put(pk_in, amount);	
		    				}
		    				if(flag.equals("B")){
		    					hmB.put(pk_in, amount);
		    				}
		    						
		    			}
		    			ArrayList alac=new ArrayList();
		    			for(int i=0;i<keyset.length;i++){
		    				String pk_order=keyset[i]==null?"":keyset[i].toString();
		    				//�����ϵ�ֵ
		    				UFDouble panel=new UFDouble(hmde.get(pk_order)==null?"0":hmde.get(pk_order).toString());
		    				//�Լ����е�ֵ
		    				UFDouble Myamount=new UFDouble(hmB.get(pk_order)==null?"0":hmB.get(pk_order).toString());
		    				//���ο��е�ֵ
		    				UFDouble foramount=new UFDouble(hmA.get(pk_order)==null?"0":hmA.get(pk_order).toString());		//��������
		    				
		    				UFDouble add=Myamount.add(panel);			//�������
		    				UFDouble a=add.sub(foramount);
		    				/**������������ڻ���ڶ�������ʱ�ر����ζ��� edit by wb at 2008-10-23 18:02:06*/
		    				if(a.toDouble()>=0){
		    					alac.add(pk_order);
		    					
		    				}			
		    			}
		    			String [] ac=(String[]) alac.toArray(new String [alac.size()]);
		    			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		    			if(keyset!=null||keyset.length<=0){
		    				String sqlupdate="update eh_order set th_flag='N' where pk_order in"+pk_or;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    			if(ac!=null){
		    				String df=PubTool.combinArrayToString(ac);
		    				String sqlupdate="update eh_order set th_flag='N' where pk_order in"+df;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    		}
		    		
		            LadingbillBVO[] lbvo = (LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		            int length = lbvo.length;
		            ArrayList list = new ArrayList();//������۶����ӱ�Vsourcebillrowid��Ϊ�����PK
		            for(int i=0;i<length;i++){
		                String vsourcebillrowid = lbvo[i].getVsourcebillrowid()==null?"":lbvo[i].getVsourcebillrowid().toString();//���۶���PK
		                list.add(vsourcebillrowid);
		            }
		            StringBuffer alsql = new StringBuffer();
		            for (int i = 0; i < list.size(); i++) {
		                alsql.append("'");
		                alsql.append(list.get(i));
		                alsql.append("'");
		                if ((i + 1) < list.size()) {
		                    alsql.append(",");
		                } else {
		                    alsql.append("");
		                }
		            }          
		            //������Դ���ݵ�PK������Ӧ�����ݣ����scrw_flag��th_flag�����ϱ��ʱ��������۶����Ĺرհ�ť��ر�
		            StringBuffer sql = new StringBuffer()
		            .append(" select scrw_flag,th_flag,pk_order from eh_order where pk_order in ("+alsql+") and isnull(dr,0)=0 ");
		            ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		            if(arr!=null && arr.size()>0){
		                for(int j=0;j<arr.size();j++){
		                    HashMap hm = (HashMap) arr.get(j);
		                    String pk_order = hm.get("pk_order")==null?"":hm.get("pk_order").toString();//���۶�����PK
		                    String updatesql = " update eh_order set lock_flag='N' where pk_order='"+pk_order+"' ";
		                    PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		                    pubitf.updateSQL(updatesql);
		                }
		            }
	    	}else if(vsourceBillType.equals("ZA14")){
	    		PubItf pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
	            AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
	            LadingbillBVO[] ibvos=(LadingbillBVO[])aggvo.getChildrenVO();
	    	       String pk_invoce  = null;
	    	        for(int i=0;i<ibvos.length;i++){
	    	        	LadingbillBVO ibvo=(ibvos[i]);
	    	        	pk_invoce = ibvo.getVsourcebillrowid();
	    	        	String updateSQL = "update eh_invoice set th_flag = 'N' where pk_invoice = '"+pk_invoce+"'";
	    	        	pubitf.updateSQL(updateSQL);
	    	        }
	    	}
				
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
			
		}

		protected void onBoTEMPLETDISCOUNT() throws Exception{
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for(int i=0;i<row;i++){
                double ladingamount=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?0:
                   Double.parseDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount").toString());
                if(ladingamount==0){
                    getBillUI().showErrorMessage("����("+(i+1)+")�������������");
                    return;
                }
            }
            TempletDialog dlg=new TempletDialog("TEST");
            AggregatedValueObject vos=getBillCardPanelWrapper().getBillVOFromUI();
            dlg.setVOs(vos);                                                      //�������ϵ�vo����dialog��ȥ
            dlg.showModal();
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6",null);
            AggregatedValueObject vo=dlg.getVOs();                                //ȡ�ü�����ʱ�ۿۺ��vo
            //��������ʱ�ۿ۵�vo���м����������
            LadingbillBVO[] bodyvos=(LadingbillBVO[])vo.getChildrenVO();
            BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
            UFDouble lsdiscountze = new UFDouble(0);        //��ʱ�ۿ۶�
            for(int i=0;i<bodyvos.length;i++){
            	if(bm.getRowState(i)==BillModel.ADD)
                    bm.setRowState(i,BillModel.ADD);
                else
                    bm.setRowState(i,BillModel.MODIFICATION);
            	LadingbillBVO bodyvo =bodyvos[i];
                double lsdiscount=bodyvo.getLsdiscount()==null?0:bodyvo.getLsdiscount().doubleValue();
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(0, i, "lsyydiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lssydiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lsdiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lssydiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lsyydiscount");
                lsdiscountze=lsdiscountze.add(lsdiscount);
            }
            UFDouble cusdiscount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_9").getValueObject()==null?"0":
            		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_9").getValueObject().toString());  //�����ۿ۽��
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_7",cusdiscount.add(lsdiscountze));                    // ���ӿͻ��Ķ����ۿ۽��
            UFDouble secye = cusdiscount.add(lsdiscountze);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("seconddiscount",secye);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tempdiscount",lsdiscountze);
            getBillUI().updateUI();
            
        }
        @Override
        protected void onBoLineAdd() throws Exception {
            // TODO Auto-generated method stub
            super.onBoLineAdd();
          //add by houcq 2011-02-25 begin
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6", null);
            int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for (int i=0;i<rows;i++)
            {
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");
            }
          //add by houcq 2011-02-25 end
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvbascode", true);
//            getBillCardPanelWrapper().getBillCardPanel().getBillModel().removeRowStateChangeEventListener()
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), row, "pk_corp");
            getBillCardPanelWrapper().getBillCardPanel().setFocusable(true);
            getBillCardPanelWrapper().getBillCardPanel().setFocusTraversalKeysEnabled(true);
//            getBillCardPanelWrapper().getBillCardPanel().get
//            getBillCardPanelWrapper().getBillCardPanel().getComponent(0).mouseExit(evt, x, y)
            
        }
        @Override
        protected void onBoLineIns() throws Exception {
            // TODO Auto-generated method stub
            super.onBoLineIns();
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvbascode", true);
        }
        @Override
        protected void onBoLinePaste() throws Exception {
            // TODO Auto-generated method stub
            super.onBoLinePaste();
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvbascode", true);
        }
        
        @Override
        protected void onBoLineDel() throws Exception {
        	super.onBoLineDel();
        	//�����еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
            int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);                 //����Ӧ�ս��
            UFDouble vjeze=new UFDouble(0);                //�����ܶ�
            UFDouble firstdiscze=new UFDouble(0);          //һ���ۿ��ܶ�
            UFDouble seccountze=new UFDouble(0);           //�����ۿ��ܶ�
            UFDouble lsdisze=new UFDouble(0);              //��ʱ�ۿ��ܶ�
            for(int i=0;i<rows;i++){
            	UFDouble scount= new UFDouble(0);
                seccountze=seccountze.add(scount);
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scount,i,"seconddiscount");
            	getBillUI().updateUI();
                
                UFDouble firstdis=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
                	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
                firstdiscze = firstdiscze.add(firstdis);
                UFDouble lsdis=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"lsdiscount")==null?"0":
                	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"lsdiscount").toString());
                lsdisze=lsdisze.add(lsdis);
                UFDouble vje=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
                	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje").toString());						//�����ܶ�
                vjeze=vjeze.add(vje);
                //����һ�д�����houcq�޸�2010-11-03��ԭ����ΪUFDouble bcysje= vje.sub(firstdiscze).sub(scount);
                UFDouble bcysje= vje.sub(firstdis).sub(scount);															//����Ӧ�ս��
                ze=ze.add(bcysje);
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
            }
            try {
				LadingbillVO ladVO = (LadingbillVO)getBillCardPanelWrapper().getChangedVOFromUI().getParentVO();
			
	            UFDouble lastzk = new UFDouble(ladVO.getDef_7()==null?"0":ladVO.getDef_7().toString());
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bcyfje",ze);
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("seconddiscount",lastzk.sub(seccountze));                         // �����ۿ���� = �����ۿ۽��(�����ۿ�)-�������ö����ۿ�
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("dkze",vjeze);
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tempdiscount",lsdisze);
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6",null);
	            //	            UFDouble def_8 = vjeze.multiply(IPubInterface.DISCOUNTRATE);                         
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_8",vjeze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze));  // ���۶����ۿ� (���*40%-һ���ۿ��ܶ�) edit by wb at 2008-7-10 12:25:07
            } catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        
        /***
         * �жϵ�ǰ������Ƿ񳬹�����տ�Ʊ����׼
         * wb 2008-12-23 11:09:00
         * @return
         * @throws Exception 
         */
        @SuppressWarnings("unchecked")
		public UFDouble  isMaxthamount() throws Exception{
        	LadingbillVO hvo = (LadingbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	@SuppressWarnings("unused")
			String pk_ladingbill = hvo.getPk_ladingbill();		//����
        	UFDate ladingdate = hvo.getLadingdate();			//�������
        	@SuppressWarnings("unused")
			String isMax = null;
        	HashMap hmbz = new HashMap();					//����տ�Ʊ����׼
			StringBuffer gzdj = new StringBuffer()
			.append(" select beginamount,endamount,maxamount")
			.append(" from eh_trade_salestand")
			.append(" where isnull(dr,0)=0")
			.append(" order by beginamount");
			UFDouble beginamount = new UFDouble(0);
			UFDouble endamount = new UFDouble(0);
			UFDouble bzamount = new UFDouble(0);
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList gzdjarr = (ArrayList) iUAPQueryBS.executeQuery(gzdj.toString(), new MapListProcessor());
			if(gzdjarr!=null&&gzdjarr.size()>0){
				for(int i=0;i<gzdjarr.size();i++){
					HashMap hm = (HashMap)gzdjarr.get(i);
					beginamount = new UFDouble(hm.get("beginamount")==null?"0":hm.get("beginamount").toString());			//��ʼֵ
					endamount = new UFDouble(hm.get("endamount")==null?"0":hm.get("endamount").toString());					//����ֵ
					bzamount = new UFDouble(hm.get("maxamount")==null?"0":hm.get("maxamount").toString());					//��Ʊ��
					hmbz.put(beginamount+"-"+endamount, bzamount);
				}
			}
			
			//���տ�Ʊ������
			StringBuffer ladSQL = new StringBuffer()
			.append(" select sum(isnull(b.zamount,0)) amount")
			.append(" from eh_ladingbill a,eh_ladingbill_b b")
			.append(" where a.pk_ladingbill = b.pk_ladingbill")
			.append(" and ( a.ladingdate = '"+ladingdate+"' )")
			.append(" and a.pk_corp = '"+_getCorp().getPk_corp()+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0");
			Object amountobj = iUAPQueryBS.executeQuery(ladSQL.toString(), new ColumnProcessor());
        	UFDouble ladingamount = new UFDouble(amountobj==null?"0":amountobj.toString());
        	
        	//������������������ 
        	StringBuffer planSQL = new StringBuffer()
        	.append(" select sum(isnull(b.amount,0)) planamount")
        	.append(" from eh_trade_plan a,eh_trade_plan_b b")
        	.append(" where a.pk_plan = b.pk_plan")
        	.append(" and a.nyear = "+ladingdate.getYear()+" and a.nmonth = "+ladingdate.getMonth()+"")
        	.append(" and a.pk_corp = '"+_getCorp().getPk_corp()+"' and a.vbillstatus = 1 ")
        	.append(" and isnull(a.dr,0)=0 ")
        	.append(" and isnull(b.dr,0)=0");
        	Object plamamountobj = iUAPQueryBS.executeQuery(planSQL.toString(), new ColumnProcessor());
        	UFDouble planamount = new UFDouble(plamamountobj==null?"0":plamamountobj.toString());
        	
        	
        	//�õ�����������������Ӧ����߿�Ʊ����׼
        	UFDouble maxamount = new UFDouble(0);		//����տ�Ʊ��
        	String key = null;
            Iterator iter = hmbz.keySet().iterator();
            while(iter.hasNext()){ 					
                Object o = iter.next();
                key =o.toString();
                String[] amounts = key.split("-");
                beginamount = new UFDouble(amounts[0]);
                endamount = new UFDouble(amounts[1]);
                bzamount = new UFDouble(hmbz.get(key).toString());
                if(planamount.sub(beginamount).toDouble()>0&&endamount.sub(planamount).toDouble()>=0){		
                	maxamount = bzamount;
                	break;
                }
            }
            UFDouble subamount = ladingamount.sub(maxamount);
			return subamount;
        }
        
      //add by houcq 2010-09-27f���Ƿ����� ���´�ӡ����
    	@Override
    	protected void onBoPrint() throws Exception {
     	   	int num=0;
        	String billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject().toString();
        	String old = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_3").getValueObject().toString();
        	if (null==old||"".equals(old))
        	{
        		old="-1";
        	}
        	else 
        	{    		
            	try {
                	num= Integer.valueOf(old).intValue()+1;
            	} catch (Exception e) {
            		getBillUI().showErrorMessage("def_3�ֶε�ֵ����ת��Ϊ����");
            		return;
            	}
    		}
        	
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
        	String sql = " update eh_ladingbill set def_3='"+num+"' where pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and billno='"+billno+"'";
        	pubitf.updateSQL(sql);
        	onBoRefresh();
        	super.onBoPrint();
    	}    
    	//ȡ���֪ͨ���е��ۿۣ������ۿۡ��������ͷ�Ʊ�ۿۡ���������  
    	public String getFlow(String billId){
    		
    		StringBuffer strBuf = new StringBuffer()
    		.append(" SELECT a.billno, a.pk_cubasdoc, b.pk_invbasdoc, SUM(NVL(b.seconddiscount,0)) discount ")
    		.append(" FROM eh_ladingbill a,eh_ladingbill_b b ")
    		.append(" WHERE a.pk_ladingbill = b.pk_ladingbill AND a.pk_ladingbill = '"+billId+"' AND NVL(b.dr,0)=0  ")
    		.append(" AND NVL(b.seconddiscount,0)<>0 ")
    		.append(" GROUP BY a.billno,a.pk_cubasdoc,b.pk_invbasdoc ");
    		return strBuf.toString();
    		
    	}
}

