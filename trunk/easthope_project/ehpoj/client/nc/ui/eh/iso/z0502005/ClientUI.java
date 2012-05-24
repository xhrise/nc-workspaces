
package nc.ui.eh.iso.z0502005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.eh.stock.z0502505.ProcheckapplyBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明：检测报告 
 * @author 张起源
 * 时间：2008-4-14
 */
public class ClientUI extends MultiChildBillManageUI {
	public ClientUI() {
	     super();
	 }
   
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
    
    protected BusinessDelegator createBusinessDelegator() {
        return new ClientBaseBD();
    }
	
	protected void initSelfData() {
		//入库标记的初始化
		getBillCardPanel().setHeadItem("rk_flag", "N");
		//表头的抽样单类型下拉菜单
		getBillCardWrapper().initHeadComboBox("resulst", ICombobox.STR_RESULE,true);
		getBillListWrapper().initHeadComboBox("resulst", ICombobox.STR_RESULE,true);
        //表体的检测结果类型下拉菜单
        getBillCardWrapper().initBodyComboBox("result", ICombobox.STR_PASS_FLAG,true);
        getBillListWrapper().initBodyComboBox("result", ICombobox.STR_PASS_FLAG,true);
        //表体的是扣重扣价下拉
		getBillCardWrapper().initBodyComboBox("iskzkj", ICombobox.CW_KZKJ,true);
		getBillListWrapper().initBodyComboBox("iskzkj", ICombobox.CW_KZKJ,true);
		 //表体的分组下拉
		getBillCardWrapper().initBodyComboBox("groupitem", ICombobox.CW_GROUP,true);
		getBillListWrapper().initBodyComboBox("groupitem", ICombobox.CW_GROUP,true);
		 //表体的是否最高下拉
		getBillCardWrapper().initBodyComboBox("ishigh", ICombobox.CW_HIGH,true);
		getBillListWrapper().initBodyComboBox("ishigh", ICombobox.CW_HIGH,true);
	}
	
	public void afterEdit(BillEditEvent e) {
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
         
        //选正指标可修改，表体才可编辑		
		if(e.getKey().equalsIgnoreCase("edit_flag")){
			int row=getBillCardPanel().getBillTable().getRowCount();
            String edit_flag = getBillCardWrapper().getBillCardPanel().getHeadItem("edit_flag").getValueObject()==null?"":
                getBillCardWrapper().getBillCardPanel().getHeadItem("edit_flag").getValueObject().toString();
			if(edit_flag.equals("true")){
	        for (int i = 0; i < row; i++) {
                getBillCardPanel().getBillModel().setCellEditable(i, "fxtype",true);
				getBillCardPanel().getBillModel().setCellEditable(i, "ll_ceil",true);
				getBillCardPanel().getBillModel().setCellEditable(i, "ll_limit", true);
				getBillCardPanel().getBillModel().setCellEditable(i, "rece_ceil", true);
				getBillCardPanel().getBillModel().setCellEditable(i, "rece_limit", true);
				}
			}
			if(edit_flag.equals("false")){
				for(int i=0;i<row;i++){
                    getBillCardPanel().getBillModel().setCellEditable(i, "fxtype",false);
					getBillCardPanel().getBillModel().setCellEditable(i, "ll_ceil",false);
					getBillCardPanel().getBillModel().setCellEditable(i, "ll_limit", false);
					getBillCardPanel().getBillModel().setCellEditable(i, "rece_ceil", false);
					getBillCardPanel().getBillModel().setCellEditable(i, "rece_limit", false);
				}
			}
	        updateUI();
		}     

        //填入检测值后，相应的对是否合格做出判断 add by zqy 2008-6-20 14:1:31 
        if (e.getPos()==BODY && (strKey.equals("checkresult") || strKey.equals("rece_ceil")|| strKey.equals("rece_limit")|| strKey.equals("ll_ceil")||strKey.equals("ll_limit") ) ){
            int row = getBillCardPanel().getBillTable("eh_stock_checkreport").getSelectedRow();
            String[] formual=getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("pk_project").getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(row,formual); 
            UFDouble checkresult =new UFDouble(getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                    .getValueAt(row, "checkresult")==null?"0":getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").
                            getValueAt(row, "checkresult").toString());//检测值   
            
            UFDouble rece_ceil = new UFDouble(getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                    .getValueAt(row, "rece_ceil")==null?"0":getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                            .getValueAt(row, "rece_ceil").toString());//收货指标上限
            
            UFDouble rece_limit = new UFDouble(getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                    .getValueAt(row, "rece_limit")==null?"0":getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                            .getValueAt(row, "rece_limit").toString());//收货指标下限
            
            UFDouble ll_ceil = new UFDouble(getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                    .getValueAt(row, "ll_ceil")==null?"0":getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                            .getValueAt(row, "ll_ceil").toString());//理论指标上限
            
            UFDouble ll_limit = new UFDouble(getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                    .getValueAt(row, "ll_limit")==null?"0":getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport")
                            .getValueAt(row, "ll_limit").toString());//理论指标下限   
            if(getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(row, "checkresult")!=null){
            	String pk_item = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_project")==null?"":
                  getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_project").toString();//带过来的是扣重还是扣价
            	String pk_invbasdoc=getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":
            		getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
            	String sql="select treatype from eh_iso a,eh_iso_b b where  a.pk_iso=b.pk_iso AND  NVL(A.lock_flag,'N')='N' and a.pk_invbasdoc='"+pk_invbasdoc+"' and b.pk_project='"+pk_item+"' and NVL(a.dr,0)=0" +
            			" and NVL(b.dr,0)=0 ";
            	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            	ArrayList al=null;;
				try {
					al = (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	String treatype=null;
            	if(al!=null){
            		for(int i=0;i<al.size();i++){
                		HashMap hm=(HashMap) al.get(i);
                		treatype=hm.get("treatype")==null?"":hm.get("treatype").toString();
                	}
            	}
            	if((checkresult.sub(ll_ceil)).toDouble()<=0 && (checkresult.sub(ll_limit)).toDouble()>=0){
            		//合格
            		getBillCardPanel().setBodyValueAt("0",row, "result");
            	}else if((checkresult.sub(rece_ceil)).toDouble()<=0 && (checkresult.sub(rece_limit)).toDouble()>=0){
            		//扣重扣价
            	   if("1".equals(treatype)){
                       getBillCardPanel().setBodyValueAt("3", row, "result");
                   }else{
                       getBillCardPanel().setBodyValueAt("2", row, "result");
                   }
      
            	}else{
            		//不合格
            		getBillCardPanel().setBodyValueAt("1",row, "result");
            	}
            	
            	
//            if(ll_ceil.compareTo(ll_limit)>0){//理论上限大于理论下限
//                if(ll_ceil.compareTo(checkresult)>=0 && ll_limit.compareTo(checkresult)<=0 ){ //指标在理论上下限中间的就是合格的
//                    getBillCardPanel().setBodyValueAt("0",row, "result");
//                }else if(rece_ceil.compareTo(rece_limit)>=0){//收货指标上大于收货指标下
//                    if(rece_limit.compareTo(checkresult)<=0 && rece_ceil.compareTo(checkresult)>=0 ){
//                        String def_8 = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8").toString();
//                        if("0".equals(def_8)){
//                            getBillCardPanel().setBodyValueAt("2", row, "result");
//                        }else{
//                            getBillCardPanel().setBodyValueAt("3", row, "result");
//                        }       
//                    }
//                    else{
//                        getBillCardPanel().setBodyValueAt("1",row, "result");
//                    }
//                }  
//                else{
//                    if(checkresult.compareTo(rece_ceil)>=0 && checkresult.compareTo(rece_limit)<=0){//在收货指标上下之间的就是扣价
//                        String def_8 = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8").toString();
//                        if("0".equals(def_8)){
//                            getBillCardPanel().setBodyValueAt("2", row, "result");
//                        }else{
//                            getBillCardPanel().setBodyValueAt("3", row, "result");
//                        }       
//                    }
//                    else{
//                        getBillCardPanel().setBodyValueAt("1",row, "result");
//                    }
//                }
//            }
//            
//            if(ll_limit.compareTo(ll_ceil)>0){//理论下限大于理论上限
//                if(checkresult.compareTo(ll_ceil)>=0 && ll_limit.compareTo(checkresult)>=0){ 
//                    getBillCardPanel().setBodyValueAt("0", row, "result");
//                }else if(rece_limit.compareTo(rece_ceil)>=0){
//                    if(rece_limit.compareTo(checkresult)<=0 && rece_ceil.compareTo(checkresult)>=0 ){
//                        String def_8 = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8").toString();
//                        if("0".equals(def_8)){
//                            getBillCardPanel().setBodyValueAt("2", row, "result");
//                        }else{
//                            getBillCardPanel().setBodyValueAt("3", row, "result");
//                        }
//                    }else{
//                        getBillCardPanel().setBodyValueAt("1",row, "result");
//                    }
//                }
//                else{
//                    if(rece_ceil.compareTo(checkresult)>=0 && checkresult.compareTo(rece_limit)<=0){
//                        String def_8 = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8").toString();
//                        if("0".equals(def_8)){
//                            getBillCardPanel().setBodyValueAt("2", row, "result");
//                        }else{
//                            getBillCardPanel().setBodyValueAt("3", row, "result");
//                        }
//                    }
//                    else{
//                        getBillCardPanel().setBodyValueAt("1",row, "result");
//                    }
//                }
//            }
//            
//            if(ll_limit.compareTo(ll_ceil)==0){//理论上限等于理论下限
//                if(checkresult.compareTo(ll_ceil)==0 || ll_limit.compareTo(checkresult)==0){
//                    getBillCardPanel().setBodyValueAt("0", row, "result");
//                }else if(rece_ceil.compareTo(rece_limit)>=0){
//                    if(rece_limit.compareTo(checkresult)<=0 && rece_ceil.compareTo(checkresult)>=0 ){
//                        String def_8 = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8").toString();
//                        if("0".equals(def_8)){
//                            getBillCardPanel().setBodyValueAt("2", row, "result");
//                        }else{
//                            getBillCardPanel().setBodyValueAt("3", row, "result");
//                        }       
//                    }
//                    else{
//                        getBillCardPanel().setBodyValueAt("1",row, "result");
//                    }
//                } else if(rece_limit.compareTo(rece_ceil)>=0){
//                    if(rece_limit.compareTo(checkresult)>=0 && rece_ceil.compareTo(checkresult)<=0 ){
//                        String def_8 = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8")==null?"":
//                            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "def_8").toString();
//                        if("0".equals(def_8)){
//                            getBillCardPanel().setBodyValueAt("2", row, "result");
//                        }else{
//                            getBillCardPanel().setBodyValueAt("3", row, "result");
//                        }
//                    }else{
//                        getBillCardPanel().setBodyValueAt("1",row, "result");
//                    }
//                }    
//            }
            
        }else{
        	getBillCardPanel().setBodyValueAt("4", row, "result");
        }
        
        //检测值修改的是否扣重扣价
        if(e.getPos()==BODY && (strKey.equals("checkresult") || strKey.equals("rece_ceil")|| strKey.equals("rece_limit")|| strKey.equals("ll_ceil")||strKey.equals("ll_limit") ) ){
        //修改值时清空相应的数据
        	getBillCardPanel().setBodyValueAt("0", e.getRow(), "kzkj");
        	getBillCardPanel().setBodyValueAt("0", e.getRow(), "invprice");
        	getBillCardPanel().setBodyValueAt("2", e.getRow(), "iskzkj");
        	getBillCardPanel().setBodyValueAt("10", e.getRow(), "groupitem");
        	getBillCardPanel().setBodyValueAt("2", e.getRow(), "ishigh");
        	try {
        		 String result=getBillCardPanel().getBodyValueAt(e.getRow(), "result")==null?"":getBillCardPanel().getBodyValueAt(e.getRow(), "result").toString();
        		if(result.equals("扣价收货")||result.equals("扣重收货")){
        			Kzkj(e.getRow());
        		}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        
        }
        
		super.afterEdit(e);
	}	

    public void setDefaultData() throws Exception {
	    //表头设置单
        BillCodeObjValueVO objVO = new BillCodeObjValueVO();
        objVO.setAttributeValue(IBillType.eh_z0502005, getUIControl().getBillType());
        String billno = BillcodeRuleBO_Client.getBillCode(IBillType.eh_z0502005, _getCorp().getPrimaryKey(), null,objVO);
        getBillCardPanel().setHeadItem("billno",billno);  
        //在表尾设置制单人
        getBillCardPanel().setTailItem("coperatorid",_getOperator());          
        //在表尾设置制单日期
        getBillCardPanel().setTailItem("dmakedate",getClientEnvironment().getDate());
        //审批状态
        getBillCardPanel().setHeadItem("vbillstatus",Integer.valueOf(String.valueOf(IBillStatus.FREE)));
        //业务类型
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
        //单据类型
        getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());
        // 表头设置公司代码        
        getBillCardPanel().setHeadItem("pk_corp",_getCorp().getPk_corp());        
        //表头设删除标记  
        getBillCardPanel().setHeadItem("dr",new Integer(0));
        getBillCardPanel().setHeadItem("rk_flag", "N");
        getBillCardPanel().setHeadItem("cyperson",_getOperator());
     
        //通过上游单据带过来的数据在初始化的时候在界面上显示(成品检测申请) add by zqy 2008-8-28
        ArrayList brr =new ArrayList();
        ArrayList crr = nc.ui.eh.iso.z0502005.ZA28TOZA30DLG.crr;
        brr = nc.ui.eh.iso.z0502005.ZA28TOZA30DLG.brr;
        HashMap hmcode = GetCode();
        if(brr!=null && brr.size()>0){
            for(int i=0;i<brr.size();i++){
                StockCheckreportCVO cvo = (StockCheckreportCVO)crr.get(i);
            	String pk_procheckapply_b = brr.get(i)==null?"":brr.get(i).toString();
                String jcbillno = hmcode.get(pk_procheckapply_b)==null?"":hmcode.get(pk_procheckapply_b).toString();
                cvo.setJcbillno(jcbillno);
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").addLine();              
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").setBodyRowVO(cvo, i);
                //改变当前行的状态，若为ADD的时候则INSERT
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").setRowState(i,  BillModel.ADD);
                String[] formual=getBillCardPanel().getBillModel("eh_stock_checkreport_c").getItemByKey("pk_invbasdoc").getEditFormulas();
                if (formual!=null && formual.length>0){
                    getBillCardPanel().getBillModel("eh_stock_checkreport_c").execFormulas(i, formual);
                }
                String formual2="jcbillno->getColValue(eh_procheckapply,billno ,pk_procheckapply ,getColValue(eh_procheckapply_b, pk_procheckapply, pk_procheckapply_b,vsourcebillid ) )";
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").execFormulas(new String[]{formual2});
             }
        }     
        
        //原料检测申请单中的物料和表体来源单据ID塞到指定的页签中 add by zqy 2008-9-2 11:34:37
        ArrayList arr = nc.ui.eh.iso.z0502005.ZA23TOZA30DLG.arr;
        HashMap hminv = GetPKinv();
        HashMap hmno = Getbillno();
        if(arr!=null && arr.size()>0){
        	UFDouble amount =new UFDouble(getBillCardWrapper().getBillCardPanel().getHeadItem("dnum").getValueObject()==null?"0":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("dnum").getValueObject().toString());//代表数量  
            for(int i=0;i<arr.size();i++){
                String pk_sample=arr.get(i)==null?"":arr.get(i).toString();
                String pk_invbasdoc = hminv.get(pk_sample)==null?"":hminv.get(pk_sample).toString();
                String jcbillno=hmno.get(pk_sample)==null?"":hmno.get(pk_sample).toString();
                StockCheckreportCVO scvo = new StockCheckreportCVO();
                scvo.setPk_invbasdoc(pk_invbasdoc);
                scvo.setVsourcebillid(pk_sample);
                scvo.setJcbillno(jcbillno);
                scvo.setAmount(amount);
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").clearBodyData();
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").addLine();
                //改变当前行的状态，若为ADD的时候则INSERT add by zqy 
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").setRowState(i,BillModel.ADD);
                getBillCardPanel().getBillModel("eh_stock_checkreport_c").setBodyRowVO(scvo, i);
                String[] formual=getBillCardPanel().getBillModel("eh_stock_checkreport_c").getItemByKey("pk_invbasdoc").getEditFormulas();
                if (formual!=null && formual.length>0){
                    getBillCardPanel().getBillModel("eh_stock_checkreport_c").execFormulas(i, formual);
                }
                String[] formuals=getBillCardPanel().getBillModel("eh_stock_checkreport_c").getItemByKey("vsourcebillid").getEditFormulas();
                if(formuals!=null && formuals.length>0){
                    getBillCardPanel().getBillModel("eh_stock_checkreport_c").execFormulas(i, formuals);
                }
            }
        }       
        
      //add by zqy 因为检测报告表体为多页签，故只能从DLG把VO取出来传到UI中，塞到对应的页签中(成品检测申请) 2008-9-2 11:22:44
      ProcheckapplyBVO[] stockbvo = ZA28TOZA30DLG.stockbvo;  
      if(stockbvo!=null && stockbvo.length>0){
          for(int i=0;i<stockbvo.length;i++){
              getBillCardPanel().getBillModel("eh_stock_checkreport").addLine();
              ProcheckapplyBVO BVO = stockbvo[i];
              String pk_project=BVO.getDef_1();
              String anaimnethod=BVO.getDef_2();
              //<修改>理论指标和收货指标类型修改.日期:2009-8-14.作者:张志远
              UFDouble ll_ceil = new UFDouble(BVO.getDef_3());
              UFDouble ll_limit=new UFDouble(BVO.getDef_4());
              UFDouble rece_ceil=new UFDouble(BVO.getDef_5());
              UFDouble rece_limit =new UFDouble(BVO.getDef_11());
              StockCheckreportBVO sbvo = new StockCheckreportBVO();
              sbvo.setPk_project(pk_project);
              sbvo.setFxtype(anaimnethod);
              sbvo.setLl_ceil(ll_ceil);
              sbvo.setLl_limit(ll_limit);
              sbvo.setRece_ceil(rece_ceil);
              sbvo.setRece_limit(rece_limit);
              
              getBillCardPanel().getBillModel("eh_stock_checkreport").setBodyRowVO(sbvo, i);
              String[] formual=getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("pk_project").getEditFormulas();
              getBillCardPanel().execBodyFormulas(i,formual);
              }   
              updateUI();
          }        
      
      //add by zqy 把DLG中VO传到UI中，塞到对应的页签中(原料检测申请) 2008-9-2 11:33:21
      nc.vo.eh.iso.z0501505.StockCheckreportBVO[] prbvo = (nc.vo.eh.iso.z0501505.StockCheckreportBVO[]) ZA23TOZA30DLG.stbvo;
      if(prbvo!=null && prbvo.length>0){
          for(int i=0;i<prbvo.length;i++){
              getBillCardPanel().getBillModel("eh_stock_checkreport").addLine();
              nc.vo.eh.iso.z0501505.StockCheckreportBVO bvo = prbvo[i];
              String pk_project=bvo.getPk_project();
              String anaimnethod=bvo.getDef_2();
              //<修改>理论指标和收货指标类型修改.日期:2009-8-14.作者:张志远
              UFDouble ll_ceil = bvo.getLl_ceil();
              UFDouble ll_limit=bvo.getLl_limit();
              UFDouble rece_ceil=bvo.getRece_ceil();
              UFDouble rece_limit =bvo.getRece_limit();
              StockCheckreportBVO scvo = new StockCheckreportBVO();
              scvo.setPk_project(pk_project);
              scvo.setFxtype(anaimnethod);
              scvo.setLl_ceil(ll_ceil);
              scvo.setLl_limit(ll_limit);
              scvo.setRece_ceil(rece_ceil);
              scvo.setRece_limit(rece_limit); 
              
              getBillCardPanel().getBillModel("eh_stock_checkreport").setBodyRowVO(scvo, i);
              String[] formual=getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("pk_project").getEditFormulas();
              getBillCardPanel().execBodyFormulas(i, formual);
          }
          updateUI();
      }
      
      //循环指定的页签，执行某个字段的公式 add by zqy 2008-8-31 
      int row = getBillCardWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getRowCount();
      for(int j=0;j<row;j++){
          String[] formual=getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("pk_project").getEditFormulas();
          if (formual!=null && formual.length>0){
              getBillCardPanel().getBillModel("eh_stock_checkreport").execFormulas(j, formual);
          }
      } 
      nc.ui.eh.iso.z0502005.ZA28TOZA30DLG.brr=null;
      nc.ui.eh.iso.z0502005.ZA23TOZA30DLG.arr=null;
      ZA23TOZA30DLG.stbvo =null;
      ZA28TOZA30DLG.stockbvo=null;
	}   

     public static HashMap GetPK(){
         HashMap hmpk = new HashMap();
         ArrayList brr = nc.ui.eh.iso.z0502005.ZA28TOZA30DLG.brr;
         if(brr!=null && brr.size()>0){
             for(int i=0;i<brr.size();i++){
                 String pk_procheckapply_b = brr.get(i)==null?"":brr.get(i).toString();
                 String sql =" select pk_invbasdoc from eh_procheckapply_b " +
                      " where pk_procheckapply_b='"+pk_procheckapply_b+"' and NVL(dr,0)=0 ";
                 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                try {
                    ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                    String pk_invbasdoc=null;
                    if(arr!=null && arr.size()>0){
                        for(int j=0;j<arr.size();j++){
                            HashMap hm = (HashMap)arr.get(j);
                            pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                            hmpk.put(pk_procheckapply_b, pk_invbasdoc);
                        }
                    } 
                } catch (BusinessException e) {
                    e.printStackTrace();
                }                 
             }
         }         
        return hmpk;        
     }
     
     public static HashMap GetCode(){
         HashMap hmcode = new HashMap();
         ArrayList brr = nc.ui.eh.iso.z0502005.ZA28TOZA30DLG.brr;
         if(brr!=null && brr.size()>0){
             for(int i=0;i<brr.size();i++){
                 String pk_procheckapply_b = brr.get(i)==null?"":brr.get(i).toString();
                 String sql =" select a.billno billno from eh_procheckapply a ,eh_procheckapply_b b " +
                      "where a.pk_procheckapply=b.pk_procheckapply and pk_procheckapply_b='"+pk_procheckapply_b+"' and NVL(a.dr,0)=0 ";
                 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                try {
                    ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                    String billno =null;
                    if(arr!=null && arr.size()>0){
                        for(int j=0;j<arr.size();j++){
                            HashMap hm = (HashMap)arr.get(j);
                            billno = hm.get("billno")==null?"":hm.get("billno").toString();
                            hmcode.put(pk_procheckapply_b, billno);
                        }
                    } 
                } catch (BusinessException e) {
                    e.printStackTrace();
                }                 
             }
         }         
        return hmcode;        
     }
     
     public static HashMap GetPKinv(){
         HashMap hmpkinv = new HashMap();
         ArrayList brr = nc.ui.eh.iso.z0502005.ZA23TOZA30DLG.arr;
         if(brr!=null && brr.size()>0){
             for(int i=0;i<brr.size();i++){
                 String pk_sample = brr.get(i)==null?"":brr.get(i).toString();
                 String sql =" select pk_invbasdoc from eh_stock_sample " +
                      " where pk_sample='"+pk_sample+"' and NVL(dr,0)=0 ";
                 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                try {
                    ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                    String pk_invbasdoc=null;
                    if(arr!=null && arr.size()>0){
                        for(int j=0;j<arr.size();j++){
                            HashMap hm = (HashMap)arr.get(j);
                            pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                            hmpkinv.put(pk_sample, pk_invbasdoc);
                        }
                    } 
                } catch (BusinessException e) {
                    e.printStackTrace();
                }                 
             }
         }         
        return hmpkinv;        
     }
    
     public static HashMap Getbillno(){
         HashMap hmbillno = new HashMap();
         ArrayList brr = nc.ui.eh.iso.z0502005.ZA23TOZA30DLG.arr;
         if(brr!=null && brr.size()>0){
             for(int i=0;i<brr.size();i++){
                 String pk_sample = brr.get(i)==null?"":brr.get(i).toString();
                 String sql =" select billno from eh_stock_sample " +
                      " where pk_sample='"+pk_sample+"' and NVL(dr,0)=0 ";
                 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                try {
                    ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                    String billno=null;
                    if(arr!=null && arr.size()>0){
                        for(int j=0;j<arr.size();j++){
                            HashMap hm = (HashMap)arr.get(j);
                            billno = hm.get("billno")==null?"":hm.get("billno").toString();
                            hmbillno.put(pk_sample, billno);
                        }
                    } 
                } catch (BusinessException e) {
                    e.printStackTrace();
                }                 
             }
         }         
        return hmbillno;        
     }
     
     public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
            throws Exception {
    
    }
    
    protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
            int intRow) throws Exception {
    }
    
    protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
            throws Exception {
    }   

    protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
        btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnPrev);
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnNext);
        nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"业务操作","业务操作");
        btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnBus);
        nc.vo.trade.button.ButtonVO btntcai = ButtonFactory.createButtonVO(IEHButton.SpecialCG,"特采","特采");
        btntcai.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btntcai);
    } 
    
    
    //**************************************************************************扣重扣价
    /**
     * 扣重扣价合成公式并计算
     * @throws Exception 
     *
     */
    public void Kzkj(int i) throws Exception{
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    	//物料的PK
    	String pk_invbasdoc=getBillCardPanel().getHeadItem("pk_invbasdoc")==null?"":
    		getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
    		UFDouble retax = new UFDouble(0);                  //系数E
    		UFDouble invprice = new UFDouble(0);               //价格
    		UFDouble  kzkj = new UFDouble(0);                  //扣重扣价
    		Integer iskzkj = new Integer(-1);                  //是扣重还是扣价
    		String  groupitem = null;                          //项目分组
    		String  ishigh = null;                             //项目是否最高项
    		UFDouble  receprice=new UFDouble(0);               //扣重扣价
    		//项目主键
    		String pk_checkitem=getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "pk_project")
    		==null?"":getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "pk_project").toString();
    		
    		//检测值(B)
    		UFDouble checkresult=new UFDouble(getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "checkresult")
    			==null?"":getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "checkresult").toString());
    		//理论指标上线
    		UFDouble ll_ceil=new UFDouble(getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "ll_ceil")
			==null?"":getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "ll_ceil").toString());
    		//理论指标下线
    		UFDouble ll_limit=new UFDouble(getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "ll_limit")
			==null?"":getBillCardPanel().getBillModel("eh_stock_checkreport").getValueAt(i, "ll_limit").toString());
    		
    		String sql="select b.formulae,b.pk_kzkj,b.istopstandard,b.iskzkj,a.groupitem,a.ishigh  from eh_bd_checkitem a," +
    				"eh_kzkj b where a.pk_kzkj=b.pk_kzkj" +
    				" and   a.pk_checkitem='"+pk_checkitem+"' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0  ";
    		
    		ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    		
    		for(int j=0;j<al.size();j++){ //项目每个条件的循环
    			HashMap hm=(HashMap) al.get(j);
    			String formulae=hm.get("formulae")==null?"":hm.get("formulae").toString();//解析并且要计算的公式
    			String pk_kzkj=hm.get("pk_kzkj")==null?"":hm.get("pk_kzkj").toString(); 
    			iskzkj=new Integer(hm.get("iskzkj")==null?"":hm.get("iskzkj").toString());// 是扣重还是扣价的标志
    			Integer istopstandard=new Integer(hm.get("istopstandard")==null?"3":hm.get("istopstandard").toString());//是否是上线指标
    			groupitem=hm.get("groupitem")==null?"10":hm.get("groupitem").toString();//项目分组
    			ishigh=hm.get("ishigh")==null?"10":hm.get("ishigh").toString();//项目分组
    			
    			UFDouble standard=new UFDouble(0);  //理论指标(A)
    			if(istopstandard==0){
    				standard=ll_ceil;  //上线
    			}else if(istopstandard==1){
    				standard=ll_limit; //下线
    			}else{
    				if((checkresult.sub(ll_ceil).abs()).toDouble()>(checkresult.sub(ll_limit).abs()).toDouble()){
    					standard=ll_limit;//下线
    				}else{
    					standard=ll_ceil;  //上线
    				}
    			}
    			
    			HashMap hmxs = getInvRatePrice(pk_kzkj, standard, checkresult,pk_invbasdoc);
    			UFDouble[] xs = (UFDouble[])hmxs.get(pk_invbasdoc);
    			if(xs!=null&&xs.length==2){
    				retax = xs[0];
    				invprice = xs[1];
    			}
    			
//    			//条件选择
//    			String sqlKZKJ="select condition,coefficient,pk_invbasdoc,ismain,price from eh_kzkj_b " +
//    					"where pk_kzkj='"+pk_kzkj+"' and isnull(dr,0)=0 order by pk_invbasdoc  desc";
//    			ArrayList alKZKJ=(ArrayList) iUAPQueryBS.executeQuery(sqlKZKJ, new MapListProcessor());
//    			for(int t=0;t<alKZKJ.size();t++){
//    				HashMap hmKZKJ=(HashMap) alKZKJ.get(t);
//    				String condition=hmKZKJ.get("condition")==null?"":hmKZKJ.get("condition").toString();       //条件
//    				UFDouble coefficient=new UFDouble(hmKZKJ.get("coefficient")==null?"0":hmKZKJ.get("coefficient").toString()); //系数	
//    				String pk_invbasdockzkj=hmKZKJ.get("pk_invbasdoc")==null?"":hmKZKJ.get("pk_invbasdoc").toString(); //物料PK
//    				UFDouble price=new UFDouble(hmKZKJ.get("price")==null?"0":hmKZKJ.get("price").toString()); //物料价格
//    				if(ParseCondition(condition,standard,checkresult)){
//    					if(pk_invbasdockzkj.equals(pk_invbasdoc)&& price.toDouble()!=0 ){//价格和物料同时存在时
//    						retax=coefficient;  //条件下的系数
//    						invprice=price;
//    						break;
//    					}else if(pk_invbasdockzkj.equals("") && price.toDouble()!=0){
//    						retax=coefficient;  //条件下的系数
//    						invprice=price;
//    						break;
//    					}else{
//    						invprice=receprice;
//    						retax=coefficient;  //条件下的系数
//    						break;
//    					}
//    				}
//    			}
    			String parse=ParseFormula(formulae,standard,checkresult,new UFDouble(1),new UFDouble(1),retax);
    			kzkj=accountFormula(parse);//表体一行所要的扣重扣价
    		}
    		getBillCardPanel().getBillModel("eh_stock_checkreport").setValueAt(kzkj, i, "kzkj");
    		getBillCardPanel().getBillModel("eh_stock_checkreport").setValueAt(invprice, i, "invprice");
    		getBillCardPanel().getBillModel("eh_stock_checkreport").setValueAt(iskzkj, i, "iskzkj");
    		getBillCardPanel().getBillModel("eh_stock_checkreport").setValueAt(groupitem, i, "groupitem");
    		getBillCardPanel().getBillModel("eh_stock_checkreport").setValueAt(ishigh, i, "ishigh");
    }
    
    /**
     * 得到产品的扣价扣重系数
     * @param pk_kzkj
     * @param standard
     * @param checkresult
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public HashMap getInvRatePrice(String pk_kzkj,UFDouble standard,UFDouble checkresult,String pk_invbasdoc) throws Exception{
    	HashMap hm = new HashMap();
		String sqlKZKJ="select condition,coefficient,pk_invbasdoc,ismain,price from eh_kzkj_b " +
				"where pk_kzkj='"+pk_kzkj+"' and NVL(dr,0)=0 order by pk_invbasdoc  desc";
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		ArrayList alKZKJ=(ArrayList) iUAPQueryBS.executeQuery(sqlKZKJ, new MapListProcessor());
		for(int t=0;t<alKZKJ.size();t++){
			UFDouble[] sh = new UFDouble[2];
			HashMap hmKZKJ=(HashMap) alKZKJ.get(t);
			String condition = hmKZKJ.get("condition")==null?"":hmKZKJ.get("condition").toString();       		//条件
			String pk_invbasdockz = hmKZKJ.get("pk_invbasdoc")==null?"":hmKZKJ.get("pk_invbasdoc").toString(); 	//物料PK
			UFDouble coefficient = new UFDouble(hmKZKJ.get("coefficient")==null?"0":hmKZKJ.get("coefficient").toString()); //系数	
			UFDouble price = new UFDouble(hmKZKJ.get("price")==null?"0":hmKZKJ.get("price").toString()); //物料价格
			if(ParseCondition(condition,standard,checkresult)){
				sh[0] = coefficient;				//系数
				sh[1] = price;						//价格
				hm.put(pk_invbasdockz, sh);
			}
		}
		if(!hm.containsKey(pk_invbasdoc)){
			UFDouble[] sh2 = (UFDouble[])hm.get("");
			hm.put(pk_invbasdoc, sh2);
		}
		return hm;
    }
    
   /**
    * 解析的结果
    * @param condition 要被解析条件
    * @param A 标准值
    * @param B 检测值
    * @return 是否成功
    * @throws BusinessException
    */
    public boolean ParseCondition(String condition,UFDouble A,UFDouble B) throws BusinessException{
    	//条件的解析
    	String Condition=ParseFormula(condition,A,B,new UFDouble(0),new UFDouble(0),new UFDouble(0));
    	if(stringSplit(Condition,"#")){
    		return true;
    	}else if(stringSplit(Condition,">")){
    		return true;
    	}else if(stringSplit(Condition,"A")){
    		return true;
    	}else if(stringSplit(Condition,"<")){
    		return true;
    	}else if(stringSplit(Condition,"=")){
    		return true;
    	}else if(Condition.equals("")){
    		return true;
    	}
    	return false;
    }
    /**
     * 公式解析成不含有A,B,C,D,E等字母公式
     * @param formulae 导入的公式 或者条件
     * @param A 系数A 标准值
     * @param B 系数B 检测值
     * @param C	系数C 本身的价格
     * @param D	系数D 其他的价格
     * @param E	系数E 系数的维护
     * @return  解析好的公式
     */
    public String ParseFormula(String formulae,UFDouble A,UFDouble B,UFDouble C,UFDouble D,UFDouble E){
    	String Parse=formulae;
//    	UFDouble AC=A.div(100);
//    	UFDouble BC=B.div(100);
    	Parse=Parse.replace("A", A.toString());  	//解析表达式中的字母A
    	Parse=Parse.replace("B", B.toString());	    //解析表达式中的字母B
    	Parse=Parse.replace("C", C.toString());		//解析表达式中的字母C
    	Parse=Parse.replace("D", D.toString());		//解析表达式中的字母D
    	Parse=Parse.replace("E", E.toString());		//解析表达式中的字母E
    	Parse=Parse.replace(">=", "#");				//解析>=
    	Parse=Parse.replace("<=", "A");				//解析<=
    	return Parse;
    }
    /**
     * 	解析计算
     * @param Parse 解析完成公式
     * @return 解析计算后的结果
     * @throws BusinessException
     */
    public UFDouble accountFormula(String Parse) throws BusinessException{
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	UFDouble result=new UFDouble(0);
    	String sql="select  "+Parse+" result from dual ";
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	for(int i=0;i<al.size();i++){
    		HashMap hm=(HashMap) al.get(i);
    		result=new UFDouble(hm.get("result")==null?"0":hm.get("result").toString()).abs();
    	}
    	return result;
    }
    
    /**
     * 分解条件
     * @param string 要分解的SQL
     * @param re  增值表达式
     * @return
     * @throws BusinessException
     */
  public boolean stringSplit(String string,String re) throws BusinessException{
	  //String 的解析
	  String[] Parse=string.split(re);
	  if(re=="#"){//>=
			if(Parse!=null && Parse.length==2){
					UFDouble right=accountFormula(Parse[0]).abs();
			  		UFDouble left=null;
			  		if(ParseCondition2(Parse[1])){
			  			left=ParseCondition3(Parse[1]);
			  			if(right.toDouble()>=left.toDouble()){
				  			return true;
				  		}else{
				  			return false;
				  		}
			  		}else{
			  			left=accountFormula(Parse[1]).abs();
			  			if(right.toDouble()>=left.toDouble()){
				  			return true;
				  		}else{
				  			return false;
				  		}
			  		}
				}else if(Parse.length==3){
					UFDouble right=accountFormula(Parse[0]).abs();
					UFDouble midd=accountFormula(Parse[1]).abs();
					UFDouble left=accountFormula(Parse[2]).abs();
					if(midd.sub(right).toDouble()<=0 && midd.sub(left).toDouble()>=0){
						return true;
					}else{
						return false;
					}
				}
	  }
	  if(re==">"){
			if(Parse!=null && Parse.length==2){
		  		UFDouble right=accountFormula(Parse[0]).abs();
		  		UFDouble left=null;
		  		if(ParseCondition2(Parse[1])){
		  			left=ParseCondition3(Parse[1]);
		  			if(right.toDouble()>left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}else{
		  			left=accountFormula(Parse[1]).abs();
		  			if(right.toDouble()>left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}
			}else if(Parse.length==3){
				UFDouble right=accountFormula(Parse[0]).abs();
				UFDouble midd=accountFormula(Parse[1]).abs();
				UFDouble left=accountFormula(Parse[2]).abs();
				if(midd.sub(right).toDouble()<0 && midd.sub(left).toDouble()>0){
					return true;
				}else{
					return false;
				}
			}
	  }
	  if(re=="A"){//<=
			if(Parse!=null && Parse.length==2){
		  		UFDouble right=accountFormula(Parse[0]).abs();
		  		UFDouble left=null;
		  		if(ParseCondition2(Parse[1])){
		  			left=ParseCondition3(Parse[1]);
		  			if(right.toDouble()<=left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}else{
		  			left=accountFormula(Parse[1]).abs();
		  			if(right.toDouble()<=left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}
			}else if(Parse.length==3){
				UFDouble right=accountFormula(Parse[0]).abs();
				UFDouble midd=accountFormula(Parse[1]).abs();
				UFDouble left=accountFormula(Parse[2]).abs();
				if(midd.sub(right).toDouble()>=0 && midd.sub(left).toDouble()<=0){
					return true;
				}else{
					return false;
				}
			}
	  }
	  if(re=="<"){
			if(Parse!=null && Parse.length==2){
		  		UFDouble right=accountFormula(Parse[0]).abs();
		  		UFDouble left=null;
		  		if(ParseCondition2(Parse[1])){
		  			left=ParseCondition3(Parse[1]);
		  			if(right.toDouble()<left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}else{
		  			left=accountFormula(Parse[1]).abs();
		  			if(right.toDouble()<left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}
			}else if(Parse.length==3){
				UFDouble right=accountFormula(Parse[0]).abs();
				UFDouble midd=accountFormula(Parse[1]).abs();
				UFDouble left=accountFormula(Parse[2]).abs();
				if(midd.sub(right).toDouble()>0 && midd.sub(left).toDouble()<0){
					return true;
				}else{
					return false;
				}
			}
	  }
	  if(re=="="){
			if(Parse!=null && Parse.length==2){
		  		UFDouble right=accountFormula(Parse[0]).abs();
		  		UFDouble left=accountFormula(Parse[1]).abs();
		  		
		  		
		  		if(ParseCondition2(Parse[1])){
		  			left=ParseCondition3(Parse[1]);
		  			if(right.toDouble()==left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}else{
		  			left=accountFormula(Parse[1]).abs();
		  			if(right.toDouble()==left.toDouble()){
			  			return true;
			  		}else{
			  			return false;
			  		}
		  		}
			}
	  }
	  
	  
//	  if(re==">"){
//			if(Parse!=null && Parse.length>=2){
//		  		UFDouble right=accountFormula(Parse[0]).abs();
//		  		UFDouble left=accountFormula(Parse[1]).abs();
//		  		if(right.toDouble()>left.toDouble()){
//		  			return true;
//		  		}else{
//		  			return false;
//		  		}
//			}
//	  }
//	  if(re=="A"){
//			if(Parse!=null && Parse.length>=2){
//		  		UFDouble right=accountFormula(Parse[0]).abs();
//		  		UFDouble left=accountFormula(Parse[1]).abs();
//		  		if(right.toDouble()<=left.toDouble()){
//		  			return true;
//		  		}else{
//		  			return false;
//		  		}
//			}
//	  }
//	  if(re=="<"){
//			if(Parse!=null && Parse.length>=2){
//		  		UFDouble right=accountFormula(Parse[0]).abs();
//		  		UFDouble left=accountFormula(Parse[1]).abs();
//		  		if(right.toDouble()<left.toDouble()){
//		  			return true;
//		  		}else{
//		  			return false;
//		  		}
//			}
//	  }
//	  if(re=="="){
//			if(Parse!=null && Parse.length>=2){
//		  		UFDouble right=accountFormula(Parse[0]).abs();
//		  		UFDouble left=accountFormula(Parse[1]).abs();
//		  		if(right.toDouble()==left.toDouble()){
//		  			return true;
//		  		}else{
//		  			return false;
//		  		}
//			}
//	  }
  	return false;
  	}
  
  
  /**
   * 解析的结果
   * @param condition 要被解析条件
   * @param A 标准值
   * @param B 检测值
   * @return 是否成功
   * @throws BusinessException
   */
   public boolean ParseCondition2(String Condition) throws BusinessException{
   	if(stringSplit2(Condition,"#")){
   		return true;
   	}else if(stringSplit2(Condition,">")){
   		return true;
   	}else if(stringSplit2(Condition,"A")){
   		return true;
   	}else if(stringSplit2(Condition,"<")){
   		return true;
   	}else if(stringSplit2(Condition,"=")){
   		return true;
   	}else if(Condition.equals("")){
   		return false;
   	}
   	return false;
   }
  
  	public boolean stringSplit2(String Condition,String re) throws BusinessException{
  		
  		 String[] Parse=Condition.split(re);
  		 if(Parse.length==2){
  			 if(re=="#"){
   				if(Parse!=null && Parse.length>=2){
   			  		UFDouble right=accountFormula(Parse[0]).abs();
   			  		UFDouble left=accountFormula(Parse[1]).abs();
   			  		if(right.toDouble()>=left.toDouble()){
   			  			return true;
   			  		}else{
   			  			return false;
   			  		}
   				}
   		  }
   		  if(re==">"){
   				if(Parse!=null && Parse.length>=2){
   			  		UFDouble right=accountFormula(Parse[0]).abs();
   			  		UFDouble left=accountFormula(Parse[1]).abs();
   			  		if(right.toDouble()>left.toDouble()){
   			  			return true;
   			  		}else{
   			  			return false;
   			  		}
   				}
   		  }
   		  if(re=="A"){
   				if(Parse!=null && Parse.length>=2){
   			  		UFDouble right=accountFormula(Parse[0]).abs();
   			  		UFDouble left=accountFormula(Parse[1]).abs();
   			  		if(right.toDouble()<=left.toDouble()){
   			  			return true;
   			  		}else{
   			  			return false;
   			  		}
   				}
   		  }
   		  if(re=="<"){
   				if(Parse!=null && Parse.length>=2){
   			  		UFDouble right=accountFormula(Parse[0]).abs();
   			  		UFDouble left=accountFormula(Parse[1]).abs();
   			  		if(right.toDouble()<left.toDouble()){
   			  			return true;
   			  		}else{
   			  			return false;
   			  		}
   				}
   		  }
   		  if(re=="="){
   				if(Parse!=null && Parse.length>=2){
   			  		UFDouble right=accountFormula(Parse[0]).abs();
   			  		UFDouble left=accountFormula(Parse[1]).abs();
   			  		if(right.toDouble()==left.toDouble()){
   			  			return true;
   			  		}else{
   			  			return false;
   			  		}
   				}
   		  } 
  		 }
  	  	return false;
  	}
    
  	public UFDouble ParseCondition3(String Condition) throws BusinessException{
  		UFDouble right=null;
			if(stringSplit3(Condition,"#").toDouble()>0){
				right=stringSplit3(Condition,"#");
			}else if(stringSplit3(Condition,">").toDouble()>0){
				right=stringSplit3(Condition,">");
			}else if( stringSplit3(Condition,"A").toDouble()>0){
				right=stringSplit3(Condition,"A");
			}else if(stringSplit3(Condition,"<").toDouble()>0){
				right=stringSplit3(Condition,"<");
			}else if(stringSplit3(Condition,"=").toDouble()>0){
				right=stringSplit3(Condition,"=");
			}else{
				right=new UFDouble(-10000);
			}
			return right;
  	}
  	
	public UFDouble stringSplit3(String Condition,String re) throws BusinessException{
  		
 		 String[] Parse=Condition.split(re);
 		  if(re=="#"){
 				if(Parse!=null && Parse.length>=2){
 			  		UFDouble right=accountFormula(Parse[0]).abs();
 			  		UFDouble left=accountFormula(Parse[1]).abs();
 			  		if(right.toDouble()>=left.toDouble()){
 			  			return right;
 			  		}else{
 			  			return new UFDouble(-10000);
 			  		}
 				}
 		  }
 		  if(re==">"){
 				if(Parse!=null && Parse.length>=2){
 			  		UFDouble right=accountFormula(Parse[0]).abs();
 			  		UFDouble left=accountFormula(Parse[1]).abs();
 			  		if(right.toDouble()>left.toDouble()){
 			  			return right;
 			  		}else{
 			  			return new UFDouble(-10000);
 			  		}
 				}
 		  }
 		  if(re=="A"){
 				if(Parse!=null && Parse.length>=2){
 			  		UFDouble right=accountFormula(Parse[0]).abs();
 			  		UFDouble left=accountFormula(Parse[1]).abs();
 			  		if(right.toDouble()<=left.toDouble()){
 			  			return right;
 			  		}else{
 			  			return new UFDouble(-10000);
 			  		}
 				}
 		  }
 		  if(re=="<"){
 				if(Parse!=null && Parse.length>=2){
 			  		UFDouble right=accountFormula(Parse[0]).abs();
 			  		UFDouble left=accountFormula(Parse[1]).abs();
 			  		if(right.toDouble()<left.toDouble()){
 			  			return right;
 			  		}else{
 			  			return new UFDouble(-10000);
 			  		}
 				}
 		  }
 		  if(re=="="){
 				if(Parse!=null && Parse.length>=2){
 			  		UFDouble right=accountFormula(Parse[0]).abs();
 			  		UFDouble left=accountFormula(Parse[1]).abs();
 			  		if(right.toDouble()==left.toDouble()){
 			  			return right;
 			  		}else{
 			  			return new UFDouble(-10000);
 			  		}
 				}
 		  }
 		 return new UFDouble(-10000);
 		
 	}
  	
  	
  	
  	
}