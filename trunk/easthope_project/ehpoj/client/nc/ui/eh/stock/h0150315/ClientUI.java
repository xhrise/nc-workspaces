package nc.ui.eh.stock.h0150315;

/**
 * 五金采购决策
 * ZB23
 * @author wangbing
 * 2009-1-7 17:38:34
 */

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractMultiChildClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionDVO;
import nc.vo.eh.trade.z00115.CubasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


public class ClientUI extends AbstractMultiChildClientUI {
	
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBaseBD();
	}
	
	
	@Override
	public void setDefaultData() throws Exception {		
		getBillCardPanel().setHeadItem("invtype", 0);	
		super.setDefaultData();
    	if(ZB22TOZB23DLG.wjbvos!=null&&ZB22TOZB23DLG.wjbvos.length>0){
        	getBillCardPanel().setHeadItem("vsourcebilltype", IBillType.eh_h0150305);
        	getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());//add by houcq 2011-04-01
    		setBodyData(ZB22TOZB23DLG.wjbvos,ZB22TOZB23DLG.wjcvos);       // 塞表体数据  
    		ZB22TOZB23DLG.wjbvos = null;
    		ZB22TOZB23DLG.wjcvos = null;
        }
    	else{
            IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = "select * from bd_psndoc where pk_psnbasdoc = (select pk_psndoc FROM sm_userandclerk su WHERE su.userid = '"+_getOperator()+"')";
        	Object obj = iUAPQueryBS.executeQuery(sql, new BeanProcessor(PsndocVO.class));
        	PsndocVO psnvo = obj==null?new PsndocVO():(PsndocVO)obj;  
        	getBillCardPanel().setHeadItem("pk_deptdoc",psnvo.getPk_deptdoc());		//申请部门
        	getBillCardPanel().setHeadItem("pk_psndoc",psnvo.getPk_psndoc());		//申请人
        }
        
	}
	
	/**
	 * 说明：上游传过的表体数组(单据是多页签，数据无法直接直接通过上游带到下游)
	 * @param pgdBVOs
	 */
	public void setBodyData(StockWjdecisionBVO[] bvos,StockWjdecisionCVO[] cvos){
		for(int i=0; i<bvos.length; i++){
            getBillCardPanel().getBillModel("eh_stock_wjdecision_b").addLine();
			getBillCardPanel().getBillModel("eh_stock_wjdecision_b").setBodyRowVO(bvos[i], i);
        	getBillCardPanel().getBillModel("eh_stock_wjdecision_b").execEditFormulasByKey(i, "invcode");
		}
		for(int i=0; i<cvos.length; i++){
            getBillCardPanel().getBillModel("eh_stock_wjdecision_c").addLine();
			getBillCardPanel().getBillModel("eh_stock_wjdecision_c").setBodyRowVO(cvos[i], i);
			getBillCardPanel().getBillModel("eh_stock_wjdecision_c").execEditFormulasByKey(i, "invcode");
			
			//比价页签
			String pk_invbasdoc = cvos[i].getPk_invbasdoc();
			StockWjdecisionDVO dvo = new StockWjdecisionDVO();
			dvo.setPk_invbasdoc(pk_invbasdoc);
			getBillCardPanel().getBillModel("eh_stock_wjdecision_d").addLine();
			getBillCardPanel().getBillModel("eh_stock_wjdecision_d").setBodyRowVO(dvo, i);
			getBillCardPanel().getBillModel("eh_stock_wjdecision_d").execEditFormulasByKey(i, "invcode");
		}
		 updateUI();
	 }
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}
	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	
	/*
	 * (non-Javadoc) @功能说明：自定义按钮
	 */
	@Override
	protected void initPrivateButton() {
        super.initPrivateButton();
	}
	
	
	 @Override
	public void  afterEdit(BillEditEvent e) {
        String strKey=e.getKey();
        if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
	        	if(strKey.equals("invcode")){
	        		String pk_invbasdoc = getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc")==null?null:
	    				getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc").toString();
	        		try {
	        			UFDouble[] kc = getWJamount(pk_invbasdoc);
	        			getBillCardPanel().setBodyValueAt(kc[0], e.getRow(),"bzkcuseday");		//标准库存使用天数
	        			 //modify by houcq 2011-06-20修改取库存方法
	        			UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
	        			getBillCardPanel().setBodyValueAt(kcamount, e.getRow(),"kcamount");		//库存
	    			} catch (Exception e1) {
	    				e1.printStackTrace();
	    			}
	        	}
                String[] formual=getBillCardPanel().getBodyItem(strKey)==null?null:getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
              //add by houcq 2011-03-29
            	if(strKey.equals("vcust")){
            		String pk_cubasdoc = getBillCardPanel().getBodyItem("vcust").getValueObject()==null?"":
            										getBillCardPanel().getBodyItem("vcust").getValueObject().toString();
            		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            		try {
            			StringBuffer str = new StringBuffer()
            			.append("SELECT * FROM bd_cubasdoc cubas WHERE cubas.pk_cubasdoc IN(SELECT cuman.pk_cubasdoc FROM bd_cumandoc cuman WHERE cuman.pk_cumandoc = '"+pk_cubasdoc+"')");    			
        				CubasdocVO cuvo = (CubasdocVO)iUAPQueryBS.executeQuery(str.toString(), new BeanProcessor(CubasdocVO.class));
        				boolean sf_flag = cuvo.getFreecustflag()==null?false:cuvo.getFreecustflag().booleanValue();   //散户标记
        				getBillCardPanel().getBodyItem("psninfo").setEnabled(sf_flag);
            		} catch (BusinessException e1) {
        				e1.printStackTrace();
        			}
            	}
            	//end 
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
     }
		

	 
	 /***
	  * 在标签包装物采购决策时的相关数据
	  * @param pk_invbasdoc
	  * @return
	  */
	 public UFDouble[] getWJamount(String pk_invbasdoc){
		 UFDouble[] kc = new UFDouble[3];
		 for(int i=0;i<kc.length;i++){
			 kc[i] = new UFDouble(0);
		 }
		 UFDate nowdate = _getDate();
		 UFDate last10date = nowdate.getDateBefore(20);	//前10天日期
		 String pk_corp = _getCorp().getPk_corp();
		 StringBuffer sql = new StringBuffer()
		 .append(" SELECT sum(NVL(a.kcuseday,0)) kcuseday,sum(NVL(a.ckamount,0)) ckamount")
		 .append(" FROM ")
//		 .append(" ---标准")
		 .append(" (SELECT a.kcuseday,0 ckamount ")
		 .append(" FROM eh_stock_standard a,eh_stock_standard_b b")
		 .append(" WHERE a.pk_standard = b.pk_standard")
		 .append(" AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append(" AND NVL(a.dr,0)=0 AND NVL(b.dr,0)=0 ")
		 .append("  union all")
//		 .append(" ---前10天日均耗用耗用")
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
				kc[0] = new UFDouble(hmA.get("kcuseday")==null?"0":hmA.get("kcuseday").toString());		//标准库存使用天数
				kc[1] = new UFDouble(hmA.get("ckamount")==null?"0":hmA.get("ckamount").toString());		//前10天日均耗用
			}
			HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, nowdate, "0", pk_corp);
			kc[2] = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString());	//当前库存
		 }catch (Exception e) {
			e.printStackTrace();
		}
		 return kc;
	 }
}