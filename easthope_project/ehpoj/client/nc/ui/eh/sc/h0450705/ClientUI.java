package nc.ui.eh.sc.h0450705;

/**
 * MRP运算
 * ZB32
 * @author wangbing
 * 2008-12-20 16:18:55
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.eh.sc.h0450705.ScMrpTempVO;
import nc.vo.eh.trade.z00120.InvbasdocVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


@SuppressWarnings("serial")
public class ClientUI extends MultiChildBillManageUI {
	
	String pk_ylinvbasdoc = null;			//原料pks
	public ClientUI() {
		super();
	}
	
	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
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
		 //审批流
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);	
	     getBillCardWrapper().initHeadComboBox("invtype",ICombobox.CG_DECISION, true);
	     getBillListWrapper().initHeadComboBox("invtype",ICombobox.CG_DECISION, true);	
	     getBillCardWrapper().initHeadComboBox("judge",ICombobox.CG_HQ, true);
	     getBillListWrapper().initHeadComboBox("judge",ICombobox.CG_HQ, true);	
	}
	
	public void setDefaultData() throws Exception {		
	     //审批流
	     getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));     
		// 表尾的初始话
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());	
		getBillCardPanel().setHeadItem("calcdate", _getDate());	
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setTailItem("coperatorid",_getOperator());
        getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
        if(ZA09TOZB32DLG.bvos!=null&&ZA09TOZB32DLG.bvos.length>0){
        	ScMrpBVO[] bvos = ZA09TOZB32DLG.bvos;
        	setBodyDatabvos(bvos);       	// 塞表体数据 订单明细
        	bvos = changeBvos(bvos);
        	setMrpCvos(bvos);       		// 塞表体数据 原料明细
        }
        ZA09TOZB32DLG.bvos = null;
	}
	
	@SuppressWarnings("unchecked")
	public ScMrpBVO[] changeBvos(ScMrpBVO[] bvos)  throws Exception{
		HashMap hm = new PubTools().getInvSafeKC(null);		//安全库存量 
        for(int i=0;i<bvos.length;i++){
        	ScMrpBVO bvo = bvos[i];
            String pk_invbasdoc = bvo.getPk_invbasdoc();
            UFDouble safekc =  new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
            UFDouble scamount = bvo.getBcamount();			//本次生产数量 	
            UFDouble truekc = bvo.getKcamount();			//实际库存
            UFDouble scrwamount = scamount.sub(truekc).add(safekc);									//生产任务数量=本次生产数量+安全库存量-实际库存量
            //如果生产任务数量<零，生产任务数量取零不安排生产并可修改；如果生产任务数量〉零，生产任务数量取正数安排生产并可修改
            if(scrwamount.toDouble()<0){
         	   	scamount = new UFDouble(0);
         	   	bvo.setSc_flag(new UFBoolean(true));
            }else{
         	   scamount = scrwamount;
            }
            bvo.setSafekc(safekc);
            bvo.setBcamount(scamount);
            bvos[i] = bvo;
        }
        return bvos;
	}
	/***
	 * 将所选订单产品信息保存到临时表中，并得出所需的原料明细
	 * wb 2009-4-29 11:05:22
	 * @param bvos
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void setMrpCvos(ScMrpBVO[] bvos) throws Exception {
		if(bvos!=null&&bvos.length>0){
			//先删除原有数据
			int rowcount = getBillCardPanel().getBillModel("eh_sc_mrp_c").getRowCount();
	        if(rowcount>0){
	        	int[] rows=new int[rowcount];
		        for(int i=rowcount - 1;i>=0;i--){
		            rows[i]=i;
		        }
		        getBillCardPanel().getBillModel("eh_sc_mrp_c").delLine(rows);
		        updateUI();
	        }
			dealTemp(bvos);
			StringBuffer sql = new StringBuffer()
			.append("  SELECT a.ylpk,b.invcode,b.invname,b.invtype,b.invspec,b.def1 colour,e.brandname,d.measname,a.ylamount")
			.append("  FROM ")
			.append("  (select c.pk_invbasdoc ylpk,SUM(ISNULL(a.scamount,0)*c.zamount) ylamount")
			.append("  from ")
			.append("  (select a.pk_invbasdoc,a.ver,sum(isnull(a.scamount,0)) scamount")
			.append("  from eh_sc_mrp_temp a")
			.append("  where a.dmakedate = '"+_getDate()+"'")
			.append("  and a.pk_corp = '"+_getCorp().getPk_corp()+"'")
			.append("  and isnull(a.dr,0)=0")
			.append("  group by a.pk_invbasdoc,a.ver")
			.append("  ) a,eh_bom b,eh_bom_b c")
			.append("  where a.pk_invbasdoc = b.pk_invbasdoc")
			.append("  and b.pk_bom = c.pk_bom")
			.append("  and a.ver = b.ver")
			.append("  and b.pk_corp = '"+_getCorp().getPk_corp()+"'")
			.append("  and isnull(b.dr,0)=0")
			.append("  and isnull(c.dr,0)=0")
			.append("  GROUP BY c.pk_invbasdoc")
			.append("  ) a JOIN bd_invmandoc c ON a.ylpk = c.pk_invmandoc")
			.append("  join bd_invbasdoc b on c.pk_invbasdoc = b.pk_invbasdoc LEFT JOIN eh_brand e ON b.invpinpai = e.pk_brand")
			.append("  JOIN bd_measdoc d ON b.pk_measdoc = d.pk_measdoc")
			.append("  where a.ylamount<>0 ORDER BY b.invcode");
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			 ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			 if(arr!=null&&arr.size()>0){
				 HashMap hmkc = new PubTools().getDateinvKC(null, null, _getDate(), "0", _getCorp().getPk_corp());
				 String pk_invbasdoc = null;
				 String invcode = null;
				 String invname = null;
				 String invspec = null;
				 String invtype = null;
				 String colour = null;
				 String brandname = null;
				 String measname = null;
				 UFDouble ylamount = new UFDouble(0);
				 UFDouble kcamount = new UFDouble(0);
				 for(int i=0;i<arr.size();i++){
					 HashMap hmA = (HashMap)arr.get(i);
					 pk_invbasdoc = hmA.get("ylpk")==null?null:hmA.get("ylpk").toString();
					 invcode = hmA.get("invcode")==null?null:hmA.get("invcode").toString();
					 invname = hmA.get("invname")==null?null:hmA.get("invname").toString();
					 invspec = hmA.get("invspec")==null?null:hmA.get("invspec").toString();
					 invtype = hmA.get("invtype")==null?null:hmA.get("invtype").toString();
					 colour = hmA.get("colour")==null?null:hmA.get("colour").toString();
					 brandname = hmA.get("brandname")==null?null:hmA.get("brandname").toString();
					 measname = hmA.get("measname")==null?null:hmA.get("measname").toString();
					 ylamount = new UFDouble(hmA.get("ylamount")==null?"0":hmA.get("ylamount").toString());
					 ScMrpCVO cvo = new ScMrpCVO();
					 cvo.setPk_invbasdoc(pk_invbasdoc);
					 kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString());	//当前库存
					 cvo.setKcamount(kcamount);
					 cvo.setNeedamount(ylamount);
					 cvo.setCy(cvo.getKcamount().sub(cvo.getNeedamount()));  		//差异
					
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").addLine();
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setBodyRowVO(cvo, i);
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(invcode, i, "vinvcode");
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(invname, i, "vinvname");
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(invtype, i, "vinvtype");
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(invspec, i, "vinvspec");
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(colour, i, "vcolour");
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(brandname, i, "vbrand");
					 getBillCardPanel().getBillModel("eh_sc_mrp_c").setValueAt(measname, i, "dw");
				 }
			 }
		}
	}

	public void dealTemp(ScMrpBVO[] bvos) throws Exception{
		String deleteSQL = "delete from eh_sc_mrp_temp where dmakedate = '"+_getDate()+"' and pk_corp = '"+_getCorp().getPk_corp()+"'";
		PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		pubItf.updateSQL(deleteSQL);			//先将临时表中数据删除
		ScMrpTempVO[] tvos = new ScMrpTempVO[bvos.length];
		for(int i=0;i<bvos.length;i++){
			tvos[i] = new ScMrpTempVO();
			tvos[i].setPk_invbasdoc(bvos[i].getPk_invbasdoc());
			tvos[i].setScamount(bvos[i].getBcamount());
			tvos[i].setVer(bvos[i].getVer());
			tvos[i].setDmakedate(_getDate());
			tvos[i].setPk_corp(_getCorp().getPk_corp());
		}
		IVOPersistence iVO =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
		iVO.insertVOArray(tvos);
	}
	/**
	 * 说明：上游传过的表体数组(单据是多页签，数据无法直接直接通过上游带到下游)
	 * @param bvos
	 * 订单明细
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void setBodyDatabvos(ScMrpBVO[] bvos) throws Exception{
		HashMap hminv = getInvInfo();
		for(int i=0; i<bvos.length; i++){
            getBillCardPanel().getBillModel("eh_sc_mrp_b").addLine();
            ScMrpBVO bvo = bvos[i];
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setBodyRowVO(bvo, i);
			InvbasdocVO vo = (InvbasdocVO)hminv.get(bvo.getPk_invbasdoc());
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getInvcode(), i, "vinvcode");
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getInvname(), i, "vinvname");
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getInvtype(), i, "vinvtype");
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getInvspec(), i, "vinvspec");
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getColour(), i, "vcolour");
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getBrand(), i, "vbrand");
			getBillCardPanel().getBillModel("eh_sc_mrp_b").setValueAt(vo.getPk_measdoc(), i, "dw");
//			getBillCardPanel().getBillModel("eh_sc_mrp_b").execEditFormulaByKey(i, "vinvcode");
       }
		 updateUI();
	 }
	
	
	
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	
	/*
	 * (non-Javadoc) @功能说明：自定义按钮
	 */
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
    	btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnPrev);
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnNext);
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
		nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"业务操作","业务操作");
        btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnBus);
        super.initPrivateButton();
	}
	
	
	 @SuppressWarnings("unchecked")
	public void  afterEdit(BillEditEvent e) {
        String strKey=e.getKey();
        if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
        		try {
        			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        			String currentModel = getBillCardPanel().getCurrentBodyTableCode();
        			if(currentModel.equals("eh_sc_mrp_b")&&strKey.equals("vinvcode")){
        				int row = e.getRow();
        				getBillCardPanel().setBodyValueAt(null, row, "bcamount");
        				getBillCardPanel().setBodyValueAt(null, row, "ver");
        				String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:
        											getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
        				if(pk_invbasdoc!=null){
        					String sql="select ver from eh_bom where isnull(dr,0)=0 and sc_flag ='Y' and pk_corp='"+_getCorp().getPk_corp()+"' and pk_invbasdoc='"+pk_invbasdoc+"'";
        					Object verObj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
            				if(verObj!=null){		 //BOM版本
        				    	Integer ver = Integer.parseInt(verObj.toString());
        				    	getBillCardPanel().setBodyValueAt(ver, e.getRow(), "ver");
            				}
            				//库存
            				//HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "1", _getCorp().getPk_corp());
            				//HashMap hmsc = getScamount(_getCorp().getPk_corp(), pk_invbasdoc);
            				//UFDouble kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
            				//UFDouble scamount = new UFDouble(hmsc.get(pk_invbasdoc)==null?"":hmsc.get(pk_invbasdoc).toString());			//在生产量
            				//modify by houcq 2011-06-20修改取库存方法
                			UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
            				getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount"); 
            				//安全库存
            				HashMap hmsafekc = new PubTools().getInvSafeKC(pk_invbasdoc); 
            				UFDouble safekc = new UFDouble(hmsafekc.get(pk_invbasdoc)==null?"":hmsafekc.get(pk_invbasdoc).toString());
            				getBillCardPanel().setBodyValueAt(safekc, row, "safekc"); 
        				}
        			}
        			//更新原料明细
        			if(currentModel.equals("eh_sc_mrp_b")&&strKey.equals("bcamount")){
        				ScMrpBVO[] bvos = (ScMrpBVO[])getBillCardPanel().getBillModel("eh_sc_mrp_b").getBodyValueVOs(getUIControl().getBillVoName()[2]);
        				setMrpCvos(bvos);
        			}
	                String[] formual=getBillCardPanel().getBodyItem(strKey)==null?null:getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
	                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        		}catch (Exception ex) {
                	ex.printStackTrace();
				}
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
     }
	
	 @SuppressWarnings("unchecked")
	public ScMrpCVO[] getMrpCvos2(ScMrpBVO[] bvos) throws Exception{
		 ScMrpCVO[] cvos = null;
		 if(bvos!=null&&bvos.length>0){
			 HashMap hmhz = new HashMap();
			 for(int i=0;i<bvos.length;i++){
				 String pk_invbasdoc = bvos[i].getPk_invbasdoc();				//物料
				 int ver = bvos[i].getVer()==null?0:bvos[i].getVer();									//BOM版本
				 UFDouble bcscamount = bvos[i].getBcamount();					//本次生产数量
				 hmhz = getBOM2(hmhz, pk_invbasdoc, ver, bcscamount);
			 }
			 
			 cvos = new ScMrpCVO[hmhz.size()];
			 String key = null;
             Iterator iter = hmhz.keySet().iterator();
             int i = 0;
             ArrayList pk_invarr = new ArrayList();			//物料pks
             while(iter.hasNext()){
                Object o = iter.next();
                key =o.toString();
                cvos[i] = (ScMrpCVO)hmhz.get(key);
                i++;
                pk_invarr.add(key);
             }
            String[] pk_invs = (String[])pk_invarr.toArray(new String[pk_invarr.size()]);
            pk_ylinvbasdoc = PubTools.combinArrayToString2(pk_invs);
		 }
		 return cvos;
	 }
	 
	 
	 /***
	  * 根据成品找到所需原料
	  * @param hmhz
	  * @param pk_invbasdoc
	  * @param ver
	  * @param bcscamount
	  * @return
	  * @throws Exception
	  */
	 @SuppressWarnings("unchecked")
	public HashMap getBOM2(HashMap hmhz,String pk_invbasdoc,int ver,UFDouble bcscamount) throws Exception{
		 StringBuffer sql = new StringBuffer()
		 .append(" select a.pk_invbasdoc,a.zamount*"+bcscamount+" amount " )
         .append(" from eh_bom_b a,eh_bom b ")
         .append(" where a.pk_bom=b.pk_bom ")
         .append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"' and  b.ver = "+ver+" ")
         .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ");
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		 if(arr!=null&&arr.size()>0){
			 HashMap hmkc = new PubTools().getDateinvKC(null, null, _getDate(), "0", _getCorp().getPk_corp());
			 String ylpk_invbasdoc = null;
			 UFDouble amount = new UFDouble(0);
			 UFDouble kcamount = new UFDouble(0);			//库存数量
			 for(int i=0;i<arr.size();i++){
				 HashMap hmA = (HashMap)arr.get(i);
				 ylpk_invbasdoc = hmA.get("pk_invbasdoc")==null?null:hmA.get("pk_invbasdoc").toString();
				 amount = new UFDouble(hmA.get("amount")==null?"0":hmA.get("amount").toString());
				 if(hmhz.containsKey(ylpk_invbasdoc)){
					 ((ScMrpCVO)hmhz.get(ylpk_invbasdoc)).setNeedamount(((ScMrpCVO)hmhz.get(ylpk_invbasdoc)).getNeedamount().add(amount));
				 }else{
					 ScMrpCVO cvo = new ScMrpCVO();
					 cvo.setPk_invbasdoc(ylpk_invbasdoc);
					 kcamount = new UFDouble(hmkc.get(ylpk_invbasdoc)==null?"0":hmkc.get(ylpk_invbasdoc).toString());	//当前库存
					 cvo.setKcamount(kcamount);
					 cvo.setNeedamount(amount);
					 hmhz.put(ylpk_invbasdoc, cvo);
				 }
			 }
		 }
		 return hmhz;
	 }
	 
	 /***
	  * 查出物料相关信息
	  * 避免执行模板中公式
	  * @return
	  * @throws Exception
	  */
	 @SuppressWarnings("unchecked")
	public HashMap getInvInfo() throws Exception{
		 String[] invs = ZA09TOZB32DLG.invs;
		 String pk_invs = PubTools.combinArrayToString(invs);
		 HashMap hm = new HashMap();
		 StringBuffer sql = new StringBuffer()
		 .append(" SELECT b.pk_invmandoc pk_invbasdoc,a.invcode,a.invname,a.invspec,a.invtype,a.def1,c.brandname,d.measname")
		 .append(" FROM bd_invbasdoc a join bd_invmandoc b on a.pk_invbasdoc = b.pk_invbasdoc LEFT JOIN eh_brand c")
		 .append(" ON a.invpinpai = c.pk_brand")
		 .append(" LEFT JOIN bd_measdoc d ON a.pk_measdoc = d.pk_measdoc")
		 .append(" WHERE b.pk_invmandoc in  "+pk_invs+" and b.pk_corp = '"+_getCorp().getPk_corp()+"'")
		 .append(" AND nvl(a.dr,0)=0 and nvl(b.dr,0)=0");
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		 if(arr!=null&&arr.size()>0){
			 String pk_invbasdoc = null;
			 String invcode = null;
			 String invname = null;
			 String invspec = null;
			 String invtype = null;
			 String colour = null;
			 String brandname = null;
			 String measname = null;
			 for(int i=0;i<arr.size();i++){
				 HashMap hmA = (HashMap)arr.get(i);
				 pk_invbasdoc = hmA.get("pk_invbasdoc")==null?null:hmA.get("pk_invbasdoc").toString();
				 invcode = hmA.get("invcode")==null?null:hmA.get("invcode").toString();
				 invname = hmA.get("invname")==null?null:hmA.get("invname").toString();
				 invspec = hmA.get("invspec")==null?null:hmA.get("invspec").toString();
				 invtype = hmA.get("invtype")==null?null:hmA.get("invtype").toString();
				 //colour = hmA.get("colour")==null?null:hmA.get("colour").toString();
				 colour = hmA.get("def1")==null?null:hmA.get("def1").toString();//modify by houcq 2011-11-11
				 brandname = hmA.get("brandname")==null?null:hmA.get("brandname").toString();
				 measname = hmA.get("measname")==null?null:hmA.get("measname").toString();
				 
				 InvbasdocVO vo = new InvbasdocVO();
				 vo.setPk_invbasdoc(pk_invbasdoc);
				 vo.setInvcode(invcode);
				 vo.setInvname(invname);
				 vo.setInvtype(invtype);
				 vo.setInvspec(invspec);
				 vo.setColour(colour);
				 vo.setBrand(brandname);
				 vo.setPk_measdoc(measname);
				 hm.put(pk_invbasdoc, vo);
			 }
		 }
		 return hm;
	 }
	 
	 /***
		 * 已派工未入库数量
		 * @param pk_orders
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		public HashMap getScamount(String pk_corp,String pk_invbasdoc) throws Exception{
			HashMap hm = new HashMap();
			StringBuffer sql = new StringBuffer()
			.append(" select b.pk_invbasdoc,sum(isnull(b.pgamount,0)) pgamount")
			.append(" from eh_sc_pgd a,eh_sc_pgd_b b")
			.append(" where a.pk_pgd = b.pk_pgd")
			.append(" and isnull(a.lock_flag,'N') <> 'Y'")
			.append(" and isnull(a.rk_flag,'N')<>'Y'")
			.append(" and isnull(a.xdflag,'N')='Y'")
			.append(" and a.pk_corp = '"+pk_corp+"' and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
			.append(" and isnull(a.dr,0) = 0")
			.append(" and isnull(b.dr,0) = 0")
			.append(" group by b.pk_invbasdoc");
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	        if(arr!=null&&arr.size()>0){
	        	UFDouble pgamount = null;
	        	for(int i=0;i<arr.size();i++){
	      		  HashMap hmA = (HashMap)arr.get(i);
	      		  pgamount = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());
	      		  hm.put(pk_invbasdoc, pgamount);
	        	}
	       }
	        return hm;
		}
	 
}