package nc.ui.eh.stock.h0150220;

/**
 * �ɹ�����
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
 */
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.stock.h0150210.YfTools;
import nc.ui.eh.uibase.AbstractMultiChildClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.h0150220.StockDecisionBVO;
import nc.vo.eh.stock.h0150220.StockDecisionDVO;
import nc.vo.eh.stock.z00140.SwVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


public class ClientUI extends AbstractMultiChildClientUI {
	
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}
    
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}
	
	protected void initSelfData() {
	     getBillCardWrapper().initHeadComboBox("invtype",ICombobox.CG_DECISION, true);
	     getBillListWrapper().initHeadComboBox("invtype",ICombobox.CG_DECISION, true);	
	     getBillCardWrapper().initHeadComboBox("judge",ICombobox.CG_HQ, true);
	     getBillListWrapper().initHeadComboBox("judge",ICombobox.CG_HQ, true);	
	     super.initSelfData();
	}
	
	public void setDefaultData() throws Exception {		
		getBillCardPanel().setHeadItem("invtype", 0);	
        getBillCardPanel().setHeadItem("orderdate", _getDate());
        getBillCardPanel().setHeadItem("jhdate", _getDate());
        getBillCardPanel().setHeadItem("jhadress", this.getAddr());//����PK_CORPȡ��˾Ŀ¼�е�Ӫҵ��ַ
        super.setDefaultData();
	}
	
	public void setDefaultMydata() throws Exception {		
        getBillCardPanel().setHeadItem("orderdate", _getDate());
        getBillCardPanel().setHeadItem("jhdate", _getDate());
        super.setDefaultData();
	}
	

	
	
	
	 public void  afterEdit(BillEditEvent e) {
        String strKey=e.getKey();
        if(e.getPos()==HEAD){
        		//add by houcq 2011-03-10���ݿ��̴���Ӫ������
        	    if (strKey.equals("pk_cubasdoc"))
        	    {
        	    	String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                        getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        	    	 String pk_psndoc;
					try {
						pk_psndoc = new PubTools().getPk_custpsndoc(pk_cubasdoc, _getCorp().getPk_corp());
						getBillCardWrapper().getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
        	    }
        	    // end
                if(strKey.equals("invtype")){
                	String  invtype = getBillCardPanel().getHeadItem("invtype").getValueObject()==null?null:
 						getBillCardPanel().getHeadItem("invtype").getValueObject().toString();
                	BillItem[] headitems = getBillCardPanel().getHeadItems();
	                for(int i=0;i<headitems.length;i++){
	                	String name = headitems[i].getKey();
	                	boolean isEnabled = headitems[i].isEdit();
	                	String tablecode = headitems[i].getTableCode();
	                	if(invtype.equals("0")&&!name.endsWith("invtype")&&isEnabled&&!tablecode.equals("qtjl")){
	                		headitems[i].setEnabled(true);
	                	}
	                	if(invtype.equals("1")&&!name.endsWith("invtype")&&isEnabled&&!tablecode.equals("qtjl")){
	                		headitems[i].setEnabled(false);
	                	}
	                	if(!name.equals("invtype")){
	                		headitems[i].setValue(null);
	                	}
	                }
	                //��պ����³�ʼ��
	                try {
						this.setDefaultMydata();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
                }
                
                if(strKey.equals("pk_invbasdoc")){
	                //��ѡ��ԭ�Ϻ����ѯ�۵��еĲɹ���͹�Ӧ�̵ıȼ�����
	                String  pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?null:
	                	 						getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
	                UFDate date = _getDate();
	                //�����Ųɹ���ҳǩ����ɾ��
					int rowcount = getBillCardPanel().getBillModel("eh_stock_decision_b").getRowCount();
	                int[] rows=new int[rowcount];
	                for(int i=rowcount - 1;i>=0;i--){
	                    rows[i]=i;
	                }
	                getBillCardPanel().getBillModel("eh_stock_decision_b").delLine(rows);
	                //��������Ӧ�̱ȼ�ҳǩ����ɾ��
					int rowcount2 = getBillCardPanel().getBillModel("eh_stock_decision_d").getRowCount();
	                int[] rows2 = new int[rowcount2];
	                for(int i=rowcount2 - 1;i>=0;i--){
	                    rows2[i]=i;
	                }
	                getBillCardPanel().getBillModel("eh_stock_decision_d").delLine(rows2);
	                
	                BillItem[] headitems = getBillCardPanel().getHeadItems();
	                for(int i=0;i<headitems.length;i++){
	                	String name = headitems[i].getKey();
	                	if(name.endsWith("amount")||name.endsWith("day")){
	                		headitems[i].setValue(null);
	                	}
	                }
	                
	                if(pk_invbasdoc!=null){
	                	
	                	setCgValue(pk_invbasdoc, date);
	                	UFDouble[] kc = getKcamount(pk_invbasdoc);
		                getBillCardPanel().setHeadItem("bzkcuseday", kc[0]);		//��׼���ʹ������
		                getBillCardPanel().setHeadItem("bzztuseday", kc[1]);		//��׼��;ʹ������
		                getBillCardPanel().setHeadItem("bamount", kc[2]);			//ǰ20�����
		                getBillCardPanel().setHeadItem("xsamount", kc[3]);			//ǰ30������
		                //modify by houcq 2011-06-20�޸�ȡ��淽��
	        			UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
		                getBillCardPanel().setHeadItem("xzkcamount", kcamount);		//��״�������
		                
		                try {
							setQTjl(pk_invbasdoc);		//����Ǣ̸��¼
						} catch (Exception e1) {
							e1.printStackTrace();
						}			
	                }
                }
               
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
        		String billcode = getBillCardPanel().getCurrentBodyTableCode();		//��ǰҳǩ
        		int row = e.getRow();
        		if(billcode.equals("eh_stock_decision_b")&&(strKey.equals("vsw")||strKey.equals("vareal"))){
        			getBillCardPanel().setBodyValueAt(null, row, "yf");
					getBillCardPanel().setBodyValueAt(null, row, "memo");
					getBillCardPanel().setBodyValueAt(null, row, "dccprice");		 
        			String  pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?null:
	 								getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
        			 String pk_areacl = getBillCardPanel().getBodyValueAt(row, "pk_areal")==null?null:getBillCardPanel().getBodyValueAt(row, "pk_areal").toString();
        			 String pk_sw = getBillCardPanel().getBodyValueAt(row, "pk_sw")==null?null:getBillCardPanel().getBodyValueAt(row, "pk_sw").toString();
        			 String yscode = "";
        				if(pk_invbasdoc!=null&&pk_areacl!=null&&pk_sw!=null){
        					try {
        						IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        						SwVO swvo = (SwVO)iUAPQueryBS.retrieveByPK(SwVO.class, pk_sw);
        						yscode = swvo!=null?swvo.getSwcode():"";					//���䷽ʽ����
        					} catch (BusinessException e1) {
        						e1.printStackTrace();
        					}
        				UFDouble ccprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, "ccprice")==null?"0":
															getBillCardPanel().getBodyValueAt(row, "ccprice").toString());				//̸�м۸�
        				ArrayList arr = new YfTools().getYf(pk_invbasdoc, pk_areacl, pk_sw,_getCorp().getPk_corp(),ccprice);
        				if(arr!=null&&arr.size()==2){
        					UFDouble sumyf = new UFDouble(arr.get(0)==null?"0":arr.get(0).toString());			//�ϼ��˷�
        					String memo = arr.get(1)==null?null:arr.get(1).toString();							//�˷���ϸ
        					UFDouble dcj = ccprice.add(sumyf);				//������
        					if(yscode.equals(YfTools.gjyscode)){			//���ʻ���ʱ ������=�˷�
        						dcj = sumyf;
        						sumyf = new UFDouble(0);
        					}
        					getBillCardPanel().setBodyValueAt(sumyf, row, "yf");
        					getBillCardPanel().setBodyValueAt(memo, row, "memo");
        					getBillCardPanel().setBodyValueAt(dcj, row, "dccprice");					//������ = ̸�м�+�˷�
        				}
        			}
        		}
        		if(billcode.equals("eh_stock_decision_e")&&strKey.equals("vinvcode")){
        			String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:
        										getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
        			getBillCardPanel().setBodyValueAt(null, row, "xzkcamount");
    				getBillCardPanel().setBodyValueAt(null, row, "bzkcamount");
    				getBillCardPanel().setBodyValueAt(null, row, "cjkcamount");
    				getBillCardPanel().setBodyValueAt(null, row, "xzkcuseday");
    				getBillCardPanel().setBodyValueAt(null, row, "bzkcuseday");
    				getBillCardPanel().setBodyValueAt(null, row, "cjkcuseday");
        			if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
        				UFDouble[] kc = getBqamount(pk_invbasdoc);
        				UFDouble bzkcuseday = kc[0];		//���ʹ��������׼
        				UFDouble hyamount = kc[1];			//ǰ10���վ�����
        				UFDouble xzkcamount = kc[2];		//��״�����
        				UFDouble bzkcamount = bzkcuseday.multiply(hyamount); 	//��׼�����
        				UFDouble cjkcamount = xzkcamount.sub(bzkcamount);		//��������
        				UFDouble xzkcuseday = xzkcamount.div(hyamount);			//��״���ʹ������
        				UFDouble cjkcuseday = xzkcuseday.sub(bzkcuseday);		//���ʹ���������
        				getBillCardPanel().setBodyValueAt(hyamount, row, "hyamount");
        				getBillCardPanel().setBodyValueAt(xzkcamount, row, "xzkcamount");
        				getBillCardPanel().setBodyValueAt(bzkcamount, row, "bzkcamount");
        				getBillCardPanel().setBodyValueAt(cjkcamount, row, "cjkcamount");
        				getBillCardPanel().setBodyValueAt(xzkcuseday, row, "xzkcuseday");
        				getBillCardPanel().setBodyValueAt(bzkcuseday, row, "bzkcuseday");
        				getBillCardPanel().setBodyValueAt(cjkcuseday, row, "cjkcuseday");
        			}
        		}
                String[] formual=getBillCardPanel().getBodyItem(strKey)==null?null:getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
     }
		
	 /***
	  * �ӵ����ѯ�۵��д���Ǣ̸��¼��Ӧ��
	  * @param pk_invbasdoc
	  */
	 private void setQTjl(String pk_invbasdoc) throws Exception{
		 StringBuffer sql = new StringBuffer()
		 .append("  SELECT * FROM ( SELECT b.pk_cubasdoc,b.dcj")
		 .append("  FROM eh_stock_queryprice a,eh_stock_queryprice_b b")
		 .append("  WHERE a.pk_queryprice  = b.pk_queryprice and a.method = 1")
		 .append("  AND a.querydate = '"+_getDate()+"'")
		 .append("  AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append("  AND a.vbillstatus = 1 ")
		 .append("  AND B.result = 'Y'")
		 .append("  AND NVL(a.dr,0)= 0 AND NVL(b.dr,0)=0 ")
		 .append("  ORDER BY a.ts DESC )WHERE ROWNUM <='1'");
		 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		 if(arr!=null&&arr.size()>0){
			 HashMap hm = (HashMap)arr.get(0);
			 String pk_cubasdoc = hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
			 UFDouble dcj = new UFDouble(hm.get("dcj")==null?"":hm.get("dcj").toString());
			 getBillCardPanel().getHeadItem("pk_cubasdoc").setValue(pk_cubasdoc);
			 getBillCardPanel().getHeadItem("hsprice").setValue(dcj);
			 String[] editForMulas = getBillCardPanel().getHeadItem("pk_cubasdoc").getEditFormulas();
			 getBillCardPanel().execHeadFormulas(editForMulas);
		 }
	}
	 
	/***
	  * �������䷽ʽ�����˷�
	  * @param row
	  * @param billcode
	  * @param strtpprice
	  * @param stryf
	  * @param strdcj
	  */
	 //<�޸�>�÷���δ�ڳ����е��á�SQLδ���ԡ�
	 public void setYF(int row,String billcode,String strtpprice,String stryf,String strdcj){
			getBillCardPanel().setBodyValueAt(null, row, stryf);
			getBillCardPanel().setBodyValueAt(null, row, "memo");
			UFDouble ysprice = new UFDouble(0);
			UFDouble tprice = new UFDouble(getBillCardPanel().getBodyValueAt(row, strtpprice)==null?"0":
				getBillCardPanel().getBodyValueAt(row, strtpprice).toString());				//̸�м۸�
			String pk_sw = getBillCardPanel().getBodyValueAt(row, "pk_sw")==null?null:getBillCardPanel().getBodyValueAt(row, "pk_sw").toString();
			if(pk_sw!=null){
				StringBuffer sql = new StringBuffer()
				.append(" SELECT typename,SUM(NVL(price,0)) price")
				.append(" FROM eh_stock_rwcarriage ")
				.append(" WHERE pk_sw = '"+pk_sw+"' AND pk_corp = '"+_getCorp().getPk_corp()+"' AND NVL(dr,0)=0")
				.append(" GROUP BY typename ");
				IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
					if(arr!=null&&arr.size()>0){
						String typename = null;
						String[] memos = new String[arr.size()];
						UFDouble price = new UFDouble(0);
						for(int i=0;i<arr.size();i++){
							HashMap hmA = (HashMap)arr.get(i);
							typename = hmA.get("typename").toString();
							price = new UFDouble(hmA.get("price").toString()); 
							ysprice = ysprice.add(price);
							memos[i] = typename+":"+price.toString();
						}
						String memo = PubTools.combinArrayToString3(memos);
						getBillCardPanel().setBodyValueAt(ysprice, row, stryf);
						getBillCardPanel().setBodyValueAt(memo, row, "memo");
					}
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
			}
			getBillCardPanel().setBodyValueAt(tprice.add(ysprice), row, strdcj);			//������ = ̸�м�+�˷�
		}
	 
	 /***
	  * ����������
	  * @param pk_invbasdoc
	  * @param date
	  */
	 public void setCgValue(String pk_invbasdoc,UFDate date){
		 StringBuffer sqla = new StringBuffer()
		 .append(" SELECT b.pk_areacl,c.areaclname,b.tpprice,b.pk_sw,b.carriage,b.dcj,b.memo")
		 .append(" FROM eh_stock_queryprice a,eh_stock_queryprice_b b,bd_areacl c")
		 .append(" WHERE a.pk_queryprice = b.pk_queryprice")
		 .append(" AND b.pk_areacl = c.pk_areacl")
		 .append(" AND a.method = 0")
		 .append(" AND a.yxdate >= '"+date+"'")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND a.vbillstatus = 1")
		 .append(" AND NVL(a.dr,0)=0")
		 .append(" AND NVL(b.dr,0)=0 order by b.dcj ");				//�ɹ���
		 
		 StringBuffer sqlb = new StringBuffer()
		 .append(" SELECT b.pk_cubasdoc,b.qzcust,b.custname,b.pk_areacl,d.phone1,b.tpprice,b.pk_sw,b.carriage,b.dcj,b.memo")
		 .append(" FROM eh_stock_queryprice a join eh_stock_queryprice_b b")
		 .append(" on a.pk_queryprice = b.pk_queryprice ")
		 .append(" left join bd_cumandoc c on b.pk_cubasdoc = c.pk_cumandoc")
		 .append(" left join bd_cubasdoc d on d.pk_cubasdoc = c.pk_cubasdoc")
		 .append(" where a.method = 1")
		 .append(" AND a.yxdate >= '"+date+"'")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND a.vbillstatus = 1")
		 .append(" AND NVL(a.dr,0)=0")
		 .append(" AND NVL(b.dr,0)=0 order by b.dcj");				//��Ӧ��
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
	    		ArrayList  arra = (ArrayList)iUAPQueryBS.executeQuery(sqla.toString(), new MapListProcessor());
	    		int bsize = arra.size();				//�ɹ������ݸ���
	    		if(arra!=null&&arra.size()>0){
	    			String pk_areal = null;
	    			UFDouble tpprice = new UFDouble(0);
	    			String pk_sw = null;
	    			String memo = null;
	    			UFDouble carriage = new UFDouble(0);
	    			UFDouble dcj = new UFDouble(0);
	    			for(int i=0;i<arra.size();i++){
	    				HashMap hmA = (HashMap)arra.get(i);
	    				getBillCardPanel().getBillModel("eh_stock_decision_b").addLine();
	    				StockDecisionBVO bvo = new StockDecisionBVO();
	    				pk_areal = hmA.get("pk_areacl")==null?null:hmA.get("pk_areacl").toString();
	    				tpprice = new UFDouble(hmA.get("tpprice")==null?"0":hmA.get("tpprice").toString());
	    				pk_sw = hmA.get("pk_sw")==null?null:hmA.get("pk_sw").toString();
	    				carriage = new UFDouble(hmA.get("carriage")==null?"0":hmA.get("carriage").toString());
	    				dcj = new UFDouble(hmA.get("dcj")==null?"0":hmA.get("dcj").toString());
	    				memo = hmA.get("memo")==null?null:hmA.get("memo").toString();
	    				
	    				bvo.setPk_areal(pk_areal);
	    				bvo.setPk_sw(pk_sw);
	    				bvo.setYf(carriage);
	    				bvo.setCcprice(tpprice);
	    				bvo.setDccprice(dcj);
	    				bvo.setMemo(memo);
	    				
	    				getBillCardPanel().getBillModel("eh_stock_decision_b").setBodyRowVO(bvo, i);
	    				getBillCardPanel().getBillModel("eh_stock_decision_b").execEditFormulasByKey(i, "pk_areal");
	    			}
	    		}
	    		
	    		//������Ӧ�̱ȼ�
	    		ArrayList  arrb = (ArrayList)iUAPQueryBS.executeQuery(sqlb.toString(), new MapListProcessor());
	    		if(arrb!=null&&arrb.size()>0){
	    			String pk_cubasdoc = null;
	    			String pk_areacl = null;
	    			String qzcust = null; 
	    			String custname = null;
	    			String tel = null;
	    			UFDouble tpprice = new UFDouble(0);
	    			String pk_sw = null;
	    			UFDouble carriage = new UFDouble(0);
	    			UFDouble dcj = new UFDouble(0);
	    			String memo = null;
	    			for(int i=0;i<arrb.size();i++){
	    				HashMap hmA = (HashMap)arrb.get(i);
	    				getBillCardPanel().getBillModel("eh_stock_decision_d").addLine();
	    				StockDecisionDVO bvo = new StockDecisionDVO();
	    				pk_cubasdoc = hmA.get("pk_cubasdoc")==null?null:hmA.get("pk_cubasdoc").toString();
	    				pk_areacl = hmA.get("pk_areacl")==null?null:hmA.get("pk_areacl").toString();
	    				qzcust = hmA.get("qzcust")==null?null:hmA.get("qzcust").toString();
	    				custname = hmA.get("custname")==null?null:hmA.get("custname").toString();
	    				tel = hmA.get("phone1")==null?null:hmA.get("phone1").toString();
	    				tpprice = new UFDouble(hmA.get("tpprice")==null?"0":hmA.get("tpprice").toString());
	    				pk_sw = hmA.get("pk_sw")==null?null:hmA.get("pk_sw").toString();
	    				carriage = new UFDouble(hmA.get("carriage")==null?"0":hmA.get("carriage").toString());
	    				dcj = new UFDouble(hmA.get("dcj")==null?"0":hmA.get("dcj").toString());
	    				memo = hmA.get("memo")==null?null:hmA.get("memo").toString();
	    				
	    				bvo.setPk_cubasdoc(pk_cubasdoc);
	    				bvo.setQzcust(qzcust);
	    				bvo.setCustname(custname);
	    				bvo.setCusttel(tel);
	    				bvo.setPk_sw(pk_sw);
	    				bvo.setYf(carriage);
	    				bvo.setCcprice(tpprice);
	    				bvo.setDccprice(dcj);
	    				bvo.setMemo(memo);
	    				
	    				getBillCardPanel().getBillModel("eh_stock_decision_d").setBodyRowVO(bvo, i);
	    				getBillCardPanel().getBillModel("eh_stock_decision_d").execLoadFormulasByKey("vcust");
	    				
	    				//���ͻ���Ϊ�ɹ�������
	    				getBillCardPanel().getBillModel("eh_stock_decision_b").addLine();
	    				StockDecisionBVO dvo = new StockDecisionBVO();
	    				dvo.setPk_areal(pk_areacl);
	    				dvo.setPk_sw(pk_sw);
	    				dvo.setYf(carriage);
	    				dvo.setCcprice(tpprice);
	    				dvo.setDccprice(dcj);
	    				dvo.setMemo(memo);
	    				getBillCardPanel().getBillModel("eh_stock_decision_b").setBodyRowVO(dvo, bsize+i);
	    				getBillCardPanel().getBillModel("eh_stock_decision_b").execEditFormulasByKey(bsize+i, "pk_areal");
	    			}
	    			
	    		}
			}catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 /***
	  * �õ�ԭ�ϵı�׼���ʹ����������������� 
	  * @param pk_invbasdoc
	  * @return
	  */
	 public UFDouble[] getKcamount(String pk_invbasdoc){
		 UFDouble[] kc = new UFDouble[5];
		 for(int i=0;i<kc.length;i++){
			 kc[i] = new UFDouble(0);
		 }
		 UFDate nowdate = _getDate();
		 UFDate last20date = nowdate.getDateBefore(20);	//ǰ20������
		 UFDate last30date = nowdate.getDateBefore(30);	//ǰ30������
		 String pk_corp = _getCorp().getPk_corp();
		 StringBuffer sql = new StringBuffer()
		 .append(" SELECT sum(NVL(a.kcuseday,0)) kcuseday,sum(NVL(a.ztuseday,0)) ztuseday,sum(NVL(a.ckamount,0)) ckamount,sum(NVL(a.ladingamount,0)) ladingamount ")
		 .append(" FROM ")
//		 .append(" ---��׼")
		 .append(" ((SELECT a.kcuseday,a.ztuseday,0 ckamount,0 ladingamount ")
		 .append(" FROM eh_stock_standard a,eh_stock_standard_b b")
		 .append(" WHERE a.pk_standard = b.pk_standard")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND NVL(a.dr,0)=0 AND NVL(b.dr,0)=0  ")
		 .append(" )  union all ")
//		 .append(" ---ǰ20�����")
		 .append(" (SELECT 0 kcuseday,0 ztuseday,SUM(NVL(b.blmount,0)) ckamount,0 ladingamount")
		 .append(" FROM eh_sc_ckd a ,eh_sc_ckd_b b")
		 .append(" WHERE a.pk_ckd = b.pk_ckd")
		 .append(" AND a.dmakedate BETWEEN '"+last20date+"' AND '"+nowdate+"'")
		 .append(" AND a.vbillstatus = 1")
		 .append(" AND a.pk_corp = '"+pk_corp+"'")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND NVL(a.dr,0)=0")
		 .append(" AND NVL(b.dr,0)=0")
		 .append(" ) union all ")
//		 .append(" ---ǰ30������")
		 .append(" (select 0 kcuseday,0 ztuseday,0 ckamount,sum(a.ladingamount)-sum(a.realbackamount) ladingamount ")
		 .append(" from ")
		 .append(" (select ")
		 .append(" 0 ladingamount,")
		 .append(" sum(NVL(realbackamount,0)) realbackamount ")
		 .append(" from eh_backbill a,eh_backbill_b b ")
		 .append(" where a.pk_backbill=b.pk_backbill and a.vbillstatus=1  ")
		 .append(" and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_corp='"+pk_corp+"'")
		 .append(" and a.dmakedate BETWEEN '"+last30date+"' AND '"+nowdate+"'")
		 .append(" union all ")
		 .append(" select ")
		 .append(" sum(NVL(b.zamount,0))  ladingamount, ")
		 .append(" 0 realbackamount")
		 .append("  from eh_ladingbill a,eh_ladingbill_b b ")
		 .append(" where a.pk_ladingbill=b.pk_ladingbill and a.vbillstatus=1  ")
		 .append(" and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_corp='"+pk_corp+"'")
		 .append(" and a.dmakedate BETWEEN '"+last30date+"' AND '"+nowdate+"'")
		 .append(" ) a")
		 .append(" ) ) a");
		 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 try {
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				kc[0] = new UFDouble(hmA.get("kcuseday")==null?"0":hmA.get("kcuseday").toString());		//��׼���ʹ������
				kc[1] = new UFDouble(hmA.get("ztuseday")==null?"0":hmA.get("ztuseday").toString());		//��;ʹ������
				kc[2] = new UFDouble(hmA.get("ckamount")==null?"0":hmA.get("ckamount").toString());		//ǰ20�����
				kc[3] = new UFDouble(hmA.get("ladingamount")==null?"0":hmA.get("ladingamount").toString());//ǰ30������
			}
			HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, nowdate, "0", pk_corp);
			kc[4] = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString());	//��ǰ���
		 }catch (Exception e) {
			e.printStackTrace();
		}
		 return kc;
	 } 
	 
	 /***
	  * �ڱ�ǩ��װ��ɹ�����ʱ���������
	  * @param pk_invbasdoc
	  * @return
	  */
	 public UFDouble[] getBqamount(String pk_invbasdoc){
		 UFDouble[] kc = new UFDouble[3];
		 for(int i=0;i<kc.length;i++){
			 kc[i] = new UFDouble(0);
		 }
		 UFDate nowdate = _getDate();
		 UFDate last10date = nowdate.getDateBefore(20);	//ǰ10������
		 String pk_corp = _getCorp().getPk_corp();
		 StringBuffer sql = new StringBuffer()
		 .append(" SELECT sum(NVL(a.kcuseday,0)) kcuseday,sum(NVL(a.ckamount,0)) ckamount")
		 .append(" FROM ")
//		 .append(" ---��׼")
		 .append(" (SELECT a.kcuseday,0 ckamount ")
		 .append(" FROM eh_stock_standard a,eh_stock_standard_b b")
		 .append(" WHERE a.pk_standard = b.pk_standard")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND NVL(a.dr,0)=0 AND NVL(b.dr,0)=0 ")
		 .append("  union all")
//		 .append(" ---ǰ10���վ����ú���")
		 .append(" SELECT 0 kcuseday,avg(NVL(b.blmount,0)) ckamount")
		 .append(" FROM eh_sc_ckd a ,eh_sc_ckd_b b")
		 .append(" WHERE a.pk_ckd = b.pk_ckd")
		 .append(" AND a.dmakedate BETWEEN '"+last10date+"' AND '"+nowdate+"'")
		 .append(" AND a.vbillstatus = 1")
		 .append(" AND a.pk_corp = '"+pk_corp+"'")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND NVL(a.dr,0)=0")
		 .append(" AND NVL(b.dr,0)=0")
		 .append(" ) a");
		 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 try {
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				kc[0] = new UFDouble(hmA.get("kcuseday")==null?"0":hmA.get("kcuseday").toString());		//��׼���ʹ������
				kc[1] = new UFDouble(hmA.get("ckamount")==null?"0":hmA.get("ckamount").toString());		//ǰ10���վ�����
			}
			HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, nowdate, "0", pk_corp);
			kc[2] = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString());	//��ǰ���
		 }catch (Exception e) {
			e.printStackTrace();
		}
		 return kc;
	 }
	 
	 //����PK_CORPȡ��˾Ŀ¼�е�Ӫҵ��ַ����Ǣ̸��¼ҳǩ�Ľ����ص�
	 public String getAddr(){
		 String addr = null;
		 String pk_corp = _getCorp().getPrimaryKey();
		 StringBuffer sql = new StringBuffer()
		 .append(" SELECT bdcorp.saleaddr FROM BD_CORP bdcorp WHERE BDCORP.PK_CORP = '"+ pk_corp +"' ");
		 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 try {
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hm = (HashMap)arr.get(0);
				addr = hm.get("saleaddr") == null?"":hm.get("saleaddr").toString();
			}
		 }catch (Exception e) {
			e.printStackTrace();
		}
		 return addr;
	 }
}