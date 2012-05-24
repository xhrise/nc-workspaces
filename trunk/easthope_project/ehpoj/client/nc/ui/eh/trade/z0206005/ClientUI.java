
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
 * 功能 销售订单
 * @author 洪海
 * 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {
   
	public static String pk_cubasdoc = null;          // 客商
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
            		showErrorMessage("客户不能为空,请先选择客户!");
            	}
            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //将客商传到参照中
            }
            if(strKey.equals("dw")||strKey.equals("amount")){
    			int row=getBillCardPanel().getBillTable().getSelectedRow();
    			pk_unit= getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                            getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //单位
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
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        
        if(strKey.equals("amount")){
        	//如果出现负数很大的情况的话 就是在单位中没有辅助计量单位
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
                                 getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //产品
            getBillCardPanel().setBodyValueAt(null, row, "amount"); 
//            getBillCardPanel().setBodyValueAt(0, row, "totalprice");
            getBillCardPanel().setBodyValueAt(0, row, "firstcount");
            getBillCardPanel().setBodyValueAt(0, row, "secondcount");
            getBillCardPanel().setBodyValueAt(0, row, "bcysje");
            //modfiy by houcq 2011-04-18
            UFDouble price = new PubTools().getInvPrice(pk_invbasdoc,_getDate());
			getBillCardPanel().setBodyValueAt(price, row, "oneprice");
			
        }
        
//      分摊折扣的处理  add by wb at 2008-6-5 15:31:36
        if(strKey.equals("def_6")){
        	 BillModel model = getBillCardPanel().getBillModel();
             if (model != null) {
            	 int rowCount = model.getRowCount();
                 if (rowCount < 1) {
                     showErrorMessage("表体为空,不能分摊折扣!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
               try {
                 OrderVO orVO = (OrderVO)getChangedVOFromUI().getParentVO();
                 UFDouble dkze = orVO.getDef_9()==null?null:orVO.getDef_9();                    // 总额
            	 if (dkze==null) {
                     showErrorMessage("订单数量未输入不能分摊,请先输入数量!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
            	    UFDouble zkye = orVO.getZkye()==null?new UFDouble(0):orVO.getZkye();                        // 理论折扣
					UFDouble seconddiscount = orVO.getSecondamount()==null?new UFDouble(0):orVO.getSecondamount();          // 二次折扣金额(客户二次折扣余额)
					UFDouble ftdiscount = orVO.getDef_6();             // 分摊的总折扣
					//add by houcq 2011-02-25 begin
					if (ftdiscount==null)
					{						   
							int row=getBillCardPanel().getBillTable().getRowCount();
	                		for(int i=0;i<row;i++)
	                		{
	                			getBillCardPanel().setBodyValueAt("", i, "secondcount");
	                			UFDouble bcysje=new UFDouble(0);                              //本次应收总额
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
						showErrorMessage("分摊折扣不能大于理论折扣总额!");
						getBillCardPanel().setHeadItem("def_6", null);
						return;
					}
					else if(ftdiscount.compareTo(seconddiscount)>0){
						showErrorMessage("分摊折扣不能大于二次折扣金额!");
						getBillCardPanel().setHeadItem("def_6", null);
						return;
					}
					else{
//						int iRet = showYesNoMessage("是否确定分摊此折扣?");
//		                if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
		                	int row=getBillCardPanel().getBillTable().getRowCount();
		                    UFDouble vjeze = dkze;         // 总金额 根据表体数据金额与总金额的比例来分摊折扣
		                    UFDouble ze = new UFDouble(0); // 表体分摊的金额合计
		                    DecimalFormat df = new DecimalFormat("#.00"); 
		                	for(int i=0;i<row;i++){
		                		UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"totalprice")==null?"0":
		                            getBillCardPanel().getBodyValueAt(i,"totalprice").toString());
		                		UFDouble bl = vje.div(vjeze);  // 所占比例
		                		UFDouble ftdisco = new UFDouble(df.format(ftdiscount.multiply(bl).toDouble()));
		                		getBillCardPanel().setBodyValueAt(ftdisco, i,"secondcount");
		                        String[] formual=getBillCardPanel().getBodyItem("amount").getEditFormulas();//获取编辑公式
		                        getBillCardPanel().execBodyFormulas(i,formual);
		                        ze = ze.add(ftdisco);
		                	}
		                	// 表体的分摊金额合计与表体分摊总额比较,由于比例的四舍五入,需将多余的折扣分摊到最后一行表体中
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
        
//        /*-----------------------------------------------------------------有问题
        //  选择单位时的处理  add by wb at 2008-5-5 15:48:30
//        if(strKey.equals("dw")){
//           super.changeDW(pk_unit, "pk_measdoc", "amount", "oneprice", "strtotalprice");           
//        }
        // 输入提货量时将二次折扣设为空
        if(e.getKey().equals("amount")){  
	       	 int rowcount=getBillCardPanel().getBillTable().getRowCount();
	       	 for(int i=0;i<rowcount;i++){
	           getBillCardPanel().setBodyValueAt(null, i, "secondcount");
            	UFDouble bcysje=new UFDouble(0);                              //本次应收总额
                UFDouble totalprice=new UFDouble(getBillCardPanel().getBodyValueAt(i,"totalprice")==null?"0":
                getBillCardPanel().getBodyValueAt(i,"totalprice").toString());
                UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstcount")==null?"0":
                getBillCardPanel().getBodyValueAt(i,"firstcount").toString());
                bcysje=totalprice.sub(firstdis);
                getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
	         }
	       	getBillCardPanel().setHeadItem("def_6",null);
	       	
       }
        //选择产品时计算折扣,修改表体中的产品数量，将表体中的本次应收金额合计后写入表头中的应收款总额
        if(strKey.equals("amount")||strKey.equals("dw")||strKey.equals("def_6")){
        	int row=getBillCardPanel().getBillTable().getSelectedRow();
        	String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();   //客户
        	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //产品
        	String pk_measdoc= getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //单位
            ArrayList arr=getDiscount(pk_cubasdoc,pk_invbasdoc);
            HashMap hm=(HashMap)arr.get(0);                                                                       //一次折扣
            HashMap hm2=(HashMap)arr.get(1);    
            //二次折扣
            
            UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"amount")==null?"0":
               getBillCardPanel().getBodyValueAt(row,"amount").toString());                                      //数量
            //根据单位转换一次折扣额  add by wb at 2008-5-31 15:19:15
            UFDouble rate = new PubTools().getInvRate(pk_invbasdoc, pk_measdoc);
//            UFDouble rate = new UFDouble(getInvRate(pk_invbasdoc).get(pk_measdoc)==null?"1":getInvRate(pk_invbasdoc).get(pk_measdoc).toString());  //单位的转换率
            UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString()).multiply(rate);
            getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), row, "firstcount");                //根据客户和产品计算一次折扣

            UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(row,"price")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"price").toString());                                      //得到牌价
//          //计算得到二次折扣 牌价*数量*折扣比率-(一次折扣*数量)
//            UFDouble secondcount=price.multiply(amount).multiply(IPubInterface.DISCOUNTRATE).sub(amount.multiply(firstdiscount));  //计算得到二次折扣
//            UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString());
//            //如果计算所得折扣小于折扣表上的余额则以计算所得折扣为折扣额 比较时取最小的 edit by wb at 2008-7-4 15:59:54
//            if(secondcount.compareTo(seccount)<0){
//            	seccount=secondcount;
//            }
//            getBillCardPanel().setBodyValueAt(seccount, row, "secondcount");                    
            String[] formual=getBillCardPanel().getBodyItem("amount").getEditFormulas();                        //获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            //表体中的本次应收金额合计后写入表头中的应收款总额
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);                              //应收总额
            UFDouble jgze=new UFDouble(0);                            //货款总额
            UFDouble firstdiscze=new UFDouble(0);          		//一次折扣总额
            UFDouble seccountze=new UFDouble(0);          //二次折扣总额
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
				// 二次折扣余额 = 二次折扣金额(上月折扣)-本次所用二次折扣
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
				UFDouble zd = jgze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze);// 理论二次折扣 (金额*40%-一次折扣总额)
				if(zd.compareTo(new UFDouble(0))>0){//当理论二次折扣为负数时用0替换
					ddzkye = zd;
				}
				getBillCardPanel().setHeadItem("zkye",ddzkye);  // 理论二次折扣 (金额*40%-一次折扣总额) edit by wb at 2008-7-10 12:25:07);
            } catch (Exception e1) {
				e1.printStackTrace();
			}
        }
//        ----------------------------------------------------------------------------------------*/
        //选择客户时对折扣的处理
        if(strKey.equals("pk_cubasdoc")){
            String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            ArrayList arr=getDiscount(pk_cubasdoc,null);
            HashMap hm=(HashMap)arr.get(0);
            HashMap hm2=(HashMap)arr.get(1);       
            HashMap hm3=(HashMap)arr.get(2);       
            //根据客户把客商档案表体中营销代表带出来 2008-05-06 add by zqy
            try {
            	PubTools tools = new PubTools();
                UFDouble overage = tools.getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//查找客户余额
                String pk_psndoc = tools.getPk_custpsndoc(pk_cubasdoc, _getCorp().getPk_corp());	//客商代表  edit by wb 2009-11-4 16:19:49
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
//                    getBillCardPanel().getBodyValueAt(i,"price").toString());                                      //得到牌价
//                UFDouble secondcount=price.multiply(amount).multiply(IPubInterface.DISCOUNTRATE).sub(amount.multiply(firstdiscount));    
//                UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString());
//                //如果计算所得折扣小于折扣表上的可用折扣则以折扣表上的二次折扣为折扣额
//                if(seccount.compareTo(secondcount)<0){
//                    secondcount=seccount;
//                }//计算得到二次折扣
//                getBillCardPanel().setBodyValueAt(secondcount, i, "secondcount");   
//                String[] formual=getBillCardPanel().getBodyItem("amount").getEditFormulas();//获取编辑公式
//                getBillCardPanel().execBodyFormulas(i,formual);
//                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
//                ze=ze.add(bcysje);
//            }
            getBillCardPanel().setHeadItem("zkye",null);   //选择客商时将可用折扣设为空
            getBillCardPanel().setHeadItem("yfze",null);   //应付总额
            //当选择客户时将客户可用二次折扣余额写入表头二次折扣总额
            UFDouble secondamount=new UFDouble(hm3.get(pk_cubasdoc)==null?"0":hm3.get(pk_cubasdoc).toString());
            getBillCardPanel().setHeadItem("secondamount",secondamount);
            // 再选择客户时清空表体  add by wb at 2008-5-19 15:38:59
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
            //取得该客户该物料的一次折扣金额
            StringBuffer sql=new StringBuffer("select b.pk_invbasdoc||b.pk_cubasdoc keys,isnull(b.newdiscount,0) value from eh_firstdiscount a ")
            .append(" ,eh_firstdiscount_b b where a.pk_firstdiscount=b.pk_firstdiscount and '"+_getDate()+"' between b.zxdate ") // 加上最新标记
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
            //取得客户该物料的二次折扣金额
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

	        //取得客户的二次折扣总额
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
     * 注册自定义按钮 
     * 2008-04-02
     */
    protected void initPrivateButton() {
        super.initPrivateButton();
    }
    /**
     * 主计量单位和辅助计量单位之间的装换系数和数量的变换
     * @param String pk_invbasdoc 物料管理档案PK；
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

   
    

