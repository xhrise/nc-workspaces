package nc.ui.eh.cwitf.h10106;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.itfrefpub.TColumRefModel;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.eh.cwitf.h10106.ItfBillmodelBVO;
import nc.vo.eh.cwitf.h10106.ItfBillmodelVO;
import nc.vo.eh.cwitf.h10106.ItfVoucherVO;
import nc.vo.eh.cwitf.h10107.CodeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 功能：凭证模板定义
 * @author 张起源
 * 日期：2008-3-25
 */
public class ClientEventHandler extends TreeManageEventHandler {
	
	nc.ui.trade.pub.VOTreeNode snode=null;
	public ClientEventHandler(BillTreeManageUI billUI, ICardController control) {
		super(billUI, control);
	}
	public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}

	protected void onBoSave() throws Exception {
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		ItfBillmodelVO mvo = (ItfBillmodelVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		ItfBillmodelBVO[] bmvo=(ItfBillmodelBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		if(row==0||row<0){
			getBillUI().showErrorMessage("模板定义表体不能为空!");
			return;
		}
		for (int i = 0; i < row; i++) {
        	//设置公司编码
        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");
        }
		
		
		String tablename_h = mvo.getTablename_h();
        String tablename_b = mvo.getTablename_b();
        
//        String a=tablename_h.substring(tablename_h.length()-2, tablename_h.length());
        if(tablename_h.substring(tablename_h.length()-2, tablename_h.length()).equals("_b")){
        	String change=new String();
        	change=tablename_b;
        	tablename_b=tablename_h;
        	tablename_h=change;
        }
        
        
        //在系统表中找到主表PK主键字段
        StringBuffer br=new StringBuffer();
        br.append(" select COLUMN_NAME = convert(sysname,c.name)              ");
        br.append(" from                                                      ");
        br.append(" sysindexes i, syscolumns c, sysobjects o                  ");
        br.append(" where o.id = object_id('"+tablename_h+"')                 ");
        br.append(" and o.id = c.id                                           ");
        br.append(" and o.id = i.id                                           ");
        br.append(" and (i.status & 0x800) = 0x800                            ");
        br.append(" and (c.name = index_col ('"+tablename_h+"', i.indid,  1) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  2) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  3) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  4) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  5) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  6) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  7) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  8) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid,  9) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 10) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 11) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 12) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 13) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 14) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 15) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 16) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 17) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 18) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 19) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 20) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 21) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 22) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 23) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 24) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 25) or    ");
        br.append("      c.name = index_col ('"+tablename_h+"', i.indid, 26)) ");
        ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(br.toString(), new MapListProcessor());
        String PrimaryKey=new String();
        if(al!=null&&al.size()>0){
        	HashMap hm=(HashMap) al.get(0);
        	PrimaryKey=hm.get("column_name")==null?"":hm.get("column_name").toString();
        }
        //在根据项目，客户，供应商到判断模板的正确性
        for(int i=0;i<bmvo.length;i++){
        	ItfBillmodelBVO bvo=bmvo[i];
        	String Invfild=bvo.getInvfile()==null?"":bvo.getInvfile().toString();        //项目（物料）
        	String Custfield=bvo.getCustfield()==null?"":bvo.getCustfield().toString();	 //客户
        	String Vendorfield=bvo.getVendorfield()==null?"":bvo.getVendorfield().toString();//供应商
        	String Jefield=bvo.getJefield()==null?"":bvo.getJefield().toString();//金额
        	StringBuffer sqlbr=new StringBuffer();
        	
        	sqlbr.append("select sum(isnull("+Jefield+",0)) ");
        	if(!Invfild.equals("")){
        		sqlbr.append(","+Invfild+" ");	
        	}
        	if(!Custfield.equals("")){
        		sqlbr.append(","+Custfield+" ");	
        	}
        	if(!Vendorfield.equals("")){
        		sqlbr.append(","+Vendorfield+" ");	
        	}
        	sqlbr.append(" from "+tablename_h+" ,"+tablename_b+"  where "+tablename_h+"."+PrimaryKey+"="+tablename_b+"."+PrimaryKey+" ");
        	if(!Invfild.equals("") || !Custfield.equals("") || !Vendorfield.equals("")){
        		sqlbr.append("  group by ");	
        	}
        	if(!Invfild.equals("")){
        		sqlbr.append(" "+Invfild+",");	
        	}
        	if(!Custfield.equals("")){
        		sqlbr.append(" "+Custfield+",");	
        	}
        	if(!Vendorfield.equals("")){
        		sqlbr.append(" "+Vendorfield+",");	
        	}
        	String sqlfile=null;
        	if(!Invfild.equals("")&&!Custfield.equals("")&&!Vendorfield.equals("")){
        		sqlfile=sqlbr.toString();
        	}else{
        		sqlfile=(String) sqlbr.toString().subSequence(0, sqlbr.toString().length()-1);
        	}
        		
        	try {
        		iUAPQueryBS.executeQuery(sqlfile, new MapListProcessor());
			} catch (Exception e) {
				getBillUI().showErrorMessage("你的定义的模板有问题，请查询确认后在保存！");
        		return;
			}
        }
        super.onBoSave();
	}

	protected void onBoEdit() throws Exception {
		VOTreeNode node=((ClientUI)getBillUI()).getBillTreeSelectNode();
        if (node != null) {
            nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) node.getData();
            String billtypecode = vo.getAttributeValue("billtypecode").toString();
            TColumRefModel.pk_billtype = billtypecode;      // 将单据类型传入参照中
        }
		super.onBoEdit();
		ItfBillmodelVO mvo = (ItfBillmodelVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String tablename_h = mvo.getTablename_h();
        String tablename_b = mvo.getTablename_b();
       
        TColumRefModel.tablename_h = tablename_h;      // 将表名传入参照中
        TColumRefModel.tablename_b = tablename_b;
	}
	/**
	 * 树选中时执行的操作 重载了基类的方法,因为基类的实现存在效率问题 没有发挥缓存的作用
	 */
	public void onTreeSelected(nc.ui.trade.pub.VOTreeNode node) {

		if (node.isRoot())
			return;
		nc.ui.trade.buffer.BillUIBuffer buffer = (nc.ui.trade.buffer.BillUIBuffer) 
												((nc.ui.trade.treemanage.BillTreeManageUI) getBillUI())
												.getTreeToBuffer().get(node.getNodeID());
		if (buffer == null || buffer.isVOBufferEmpty()) {
			try {
				onQueryHeadData(node);
				snode=node;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				((nc.ui.trade.treemanage.BillTreeManageUI) getBillUI())
						.setListHeadData(getBufferData()
								.getAllHeadVOsFromBuffer());
				((nc.ui.trade.treemanage.BillTreeManageUI) getBillUI())
						.setBillOperate(IBillOperate.OP_NOTEDIT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2008-3-25)
	 * 
	 * @param node
	 * nc.ui.trade.pub.VOTreeNode
	 */
	public void onQueryHeadData(nc.ui.trade.pub.VOTreeNode selectnode)
			throws Exception {
		//清空缓冲数据
		getBufferData().clear();

		nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectnode.getData();
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" pk_billtype = '").append(vo.getAttributeValue("pk_billtype")).append("' and pk_corp = '"+_getCorp().getPk_corp()+"' order by ts ");
		SuperVO[] queryVos = queryHeadVOs(sqlWhere.toString());

		if (queryVos != null && queryVos.length != 0) {
			for (int i = 0; i < queryVos.length; i++) {
				AggregatedValueObject aVo = (AggregatedValueObject) Class
						.forName(getUIController().getBillVoName()[0])
						.newInstance();
				aVo.setParentVO(queryVos[i]);
				getBufferData().addVOToBuffer(aVo);
			}
			getBillUI().setListHeadData(queryVos);
			getBufferData().setCurrentRow(0);
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		} else {
			getBillUI().setListHeadData(queryVos);
			getBufferData().setCurrentRow(-1);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		}
	}

	public void onBoAdd(ButtonObject arg0) throws Exception {
        VOTreeNode node=((ClientUI)getBillUI()).getBillTreeSelectNode();
        if (node != null) {
            nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) node.getData();
            super.onBoAdd(arg0);
            String pk_billtype = vo.getAttributeValue("pk_billtype").toString();
            String billtypecode = vo.getAttributeValue("billtypecode").toString();
            String billtypename = vo.getAttributeValue("billtypename").toString();
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_billtype",pk_billtype); 
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("billtypename",billtypecode);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_2",billtypename);
            TColumRefModel.pk_billtype = billtypecode;      // 将单据类型传入参照中
            //查找单据类型对应的主子表
            String sql = "select distinct table_code from pub_billtemplet_b where pk_billtemplet in(select pk_billtemplet from pub_billtemplet where pk_billtypecode = '"+billtypecode+"')"+ 
            			 "and ( totalflag = 0 or totalflag = 1) and table_code like 'eh%'";
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        	try {
        		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
        		if(arr!=null&&arr.size()>0){
        			HashMap hmA = (HashMap)arr.get(0);
        			String tablename_h = hmA.get("table_code")==null?"":hmA.get("table_code").toString();
        			String tablename_b = null;
        			if(arr.size()==1){
        				tablename_b = tablename_h;
        			}else{
        				HashMap hmB = (HashMap)arr.get(1);
        				tablename_b = hmB.get("table_code")==null?"":hmB.get("table_code").toString();
        			}
        			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tablename_h",tablename_h); 
                    getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tablename_b",tablename_b);
//                    String a=tablename_h.substring(tablename_h.length()-2, tablename_h.length());
                    if(tablename_h.substring(tablename_h.length()-2, tablename_h.length()).equals("_b")){
                    	String change=new String();
                    	change=tablename_b;
                    	tablename_b=tablename_h;
                    	tablename_h=change;
                    }
                    TColumRefModel.tablename_h = tablename_h;      // 将表名传入参照中
                    TColumRefModel.tablename_b = tablename_b;
        		}
        	} catch (Exception e) {
    			e.printStackTrace();
    		}
            getBillUI().updateUI();
        }else{
        	getBillUI().showErrorMessage("请选择单据类型进行增加!");
        }    
		
	}
    
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.Prev:    				            //上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:   							 	//上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.CREATEVOUCHER:    					//生成凭证
                onBoCreateVoucher(intBtn);
                break;
            case IEHButton.INSERTCDOE:    					    //科目生成
                onBoInsertcode(intBtn);
                break;
        }   
    }
    //凭证生成
    private void onBoCreateVoucher(int intBtn)  throws Exception{
    	
    	 PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
    	 String res = null;
    	 VOTreeNode node=((ClientUI)getBillUI()).getBillTreeSelectNode();
    	 if (node != null) {
    		 nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) node.getData();
    		 String billtypecode=vo.getAttributeValue("billtypecode")==null?"":vo.getAttributeValue("billtypecode").toString();
    		 String billtypename=vo.getAttributeValue("billtypename")==null?"":vo.getAttributeValue("billtypename").toString();
    		 ItfVoucherVO msgvo = pubItf.SDmodel(_getDate(),billtypecode,_getCorp().getPk_corp());
    		 res = msgvo.getHintmsg().trim();
    		 String writeMsg = billtypename+":\r\n"+msgvo.getWritemsg();
    		 if(res!=null&&res.length()>0){
    			 if(res.endsWith("已经存在!")){
    				 if(getBillUI().showYesNoMessage("本月"+billtypename+"凭证已经存在,是否删除重新导入?") == UIDialog.ID_YES){
    					 PubTools.WriteError("本月"+billtypename+"凭证已经存在,重新导入\r\n", _getDate().toString()+billtypename);
    					 pubItf.deleteU8data(_getDate(), _getCorp().getPk_corp(), billtypecode);
    					 msgvo = pubItf.SDmodel(_getDate(),billtypecode,_getCorp().getPk_corp());
    					 res = msgvo.getHintmsg();
    					 writeMsg = billtypename+":\r\n"+msgvo.getWritemsg();
    				 }
    			 }
				 res = res.replaceAll("<br>", "");
    			 writeMsg = writeMsg.replaceAll("<br>", "");
    			 PubTools.WriteError(writeMsg, _getDate().toString()+billtypename);
    			 getBillUI().showWarningMessage(res);
    	         getBillUI().showHintMessage(res);
    		 }
    	 }
         
	}
    
    /**
     * 凭证科目从U8导入到NC表中
     * @param intBtn
     * @throws Exception
     */
    private void onBoInsertcode(int intBtn)  throws Exception{
    	if(getBillUI().showYesNoMessage("是否重新生成科目?") == UIDialog.ID_YES){
        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        	IVOPersistence  iVOPersistence =    (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
        	//查询U8code表中的数据在插入NC表中
        	StringBuffer br=new StringBuffer();
        	br.append("select ");
        	br.append("ccode,");  					//科目编码              
        	br.append("ccode_name,");  		 		//科目名称                  
        	br.append("ccode_engl,");  		 		//科目英文名称              
        	br.append("igrade,");  				    //编码级次                  
        	br.append("bproperty,");		  	    //科目性质                  
        	br.append("cbook_type,"); 	   			//账页格式                  
        	br.append("cbook_type_engl,"); 			//账页格式英文名称               
        	br.append("chelp,");				    //科目助记码            
        	br.append("cexch_name,");			    //币种名称                    
        	br.append("cmeasure,");		            //计量单位                  
        	br.append("bperson,");                  //是否个人往来核算       
        	br.append("bcus ,");                    //是否客户往来核算    
        	br.append("bsup ,");                    //是否供应商往来核算 
        	br.append("bdept,");                    //是否部门核算     
        	br.append("bitem,");                    //是否项目核算      
        	br.append("cass_item ,");               //项目大类编码          
        	br.append("br ");                		//是否日记账                      
        	br.append(" from code");
        	ClientEnvironment ce = ClientEnvironment.getInstance();
        	CodeVO[] codes=pubItf.U8Select(br.toString(),ce.getCorporation().getPk_corp());
        	if(codes!=null && codes.length>0){
        		iVOPersistence.insertVOArray(codes);
        	}
        	
        	getBillUI().showWarningMessage("科目生成重新生成");
            getBillUI().showHintMessage("科目已经重新生成");
    	}
	}
    
    
	

    private void onBoBrows(int intBtn) throws java.lang.Exception {
        // 动作执行前处理
        buttonActionBefore(getBillUI(), intBtn);
        switch (intBtn) {
	        case IEHButton.Prev: {
	            getBufferData().prev();
	            break;
	        }
	        case IEHButton.Next: {
	            getBufferData().next();
	            break;
	        }
        }
        // 动作执行后处理
        buttonActionAfter(getBillUI(), intBtn);
        getBillUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().
        		getStrByID("uifactory","UPPuifactory-000503",null,new String[] { nc.vo.format.Format.
        				indexFormat(getBufferData().getCurrentRow()+1) }));
         setBoEnabled();
    }

    private void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        if(getButtonManager().getButton(IEHButton.Prev)!=null){
            if(!getBufferData().hasPrev()){
                getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
            }
            else{
                getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
            }
            if(!getBufferData().hasNext()){
                getButtonManager().getButton(IEHButton.Next).setEnabled(false);
            }
            else{
                getButtonManager().getButton(IEHButton.Next).setEnabled(true);
            }
        }
        //在有关闭按钮时对关闭按钮的控制 
        String[] keys = aggvo.getParentVO().getAttributeNames();
        if(keys!=null && keys.length>0){
            for(int i=0;i<keys.length;i++){
                if(keys[i].endsWith("lock_flag")){ 
                    String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
                        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                    if(lock_flag.equals("false")){
                        getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                    }else{
                        getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
                    }
                    break;
                }  
            }
        }
        getBillUI().updateButtonUI();
    }
   
}

