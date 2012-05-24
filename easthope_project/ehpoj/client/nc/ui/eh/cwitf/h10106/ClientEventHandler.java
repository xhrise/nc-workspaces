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
 * ���ܣ�ƾ֤ģ�嶨��
 * @author ����Դ
 * ���ڣ�2008-3-25
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
			getBillUI().showErrorMessage("ģ�嶨����岻��Ϊ��!");
			return;
		}
		for (int i = 0; i < row; i++) {
        	//���ù�˾����
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
        
        
        //��ϵͳ�����ҵ�����PK�����ֶ�
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
        //�ڸ�����Ŀ���ͻ�����Ӧ�̵��ж�ģ�����ȷ��
        for(int i=0;i<bmvo.length;i++){
        	ItfBillmodelBVO bvo=bmvo[i];
        	String Invfild=bvo.getInvfile()==null?"":bvo.getInvfile().toString();        //��Ŀ�����ϣ�
        	String Custfield=bvo.getCustfield()==null?"":bvo.getCustfield().toString();	 //�ͻ�
        	String Vendorfield=bvo.getVendorfield()==null?"":bvo.getVendorfield().toString();//��Ӧ��
        	String Jefield=bvo.getJefield()==null?"":bvo.getJefield().toString();//���
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
				getBillUI().showErrorMessage("��Ķ����ģ�������⣬���ѯȷ�Ϻ��ڱ��棡");
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
            TColumRefModel.pk_billtype = billtypecode;      // ���������ʹ��������
        }
		super.onBoEdit();
		ItfBillmodelVO mvo = (ItfBillmodelVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String tablename_h = mvo.getTablename_h();
        String tablename_b = mvo.getTablename_b();
       
        TColumRefModel.tablename_h = tablename_h;      // ���������������
        TColumRefModel.tablename_b = tablename_b;
	}
	/**
	 * ��ѡ��ʱִ�еĲ��� �����˻���ķ���,��Ϊ�����ʵ�ִ���Ч������ û�з��ӻ��������
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
	 * �˴����뷽��˵���� �������ڣ�(2008-3-25)
	 * 
	 * @param node
	 * nc.ui.trade.pub.VOTreeNode
	 */
	public void onQueryHeadData(nc.ui.trade.pub.VOTreeNode selectnode)
			throws Exception {
		//��ջ�������
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
            TColumRefModel.pk_billtype = billtypecode;      // ���������ʹ��������
            //���ҵ������Ͷ�Ӧ�����ӱ�
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
                    TColumRefModel.tablename_h = tablename_h;      // ���������������
                    TColumRefModel.tablename_b = tablename_b;
        		}
        	} catch (Exception e) {
    			e.printStackTrace();
    		}
            getBillUI().updateUI();
        }else{
        	getBillUI().showErrorMessage("��ѡ�񵥾����ͽ�������!");
        }    
		
	}
    
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.Prev:    				            //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:   							 	//��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.CREATEVOUCHER:    					//����ƾ֤
                onBoCreateVoucher(intBtn);
                break;
            case IEHButton.INSERTCDOE:    					    //��Ŀ����
                onBoInsertcode(intBtn);
                break;
        }   
    }
    //ƾ֤����
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
    			 if(res.endsWith("�Ѿ�����!")){
    				 if(getBillUI().showYesNoMessage("����"+billtypename+"ƾ֤�Ѿ�����,�Ƿ�ɾ�����µ���?") == UIDialog.ID_YES){
    					 PubTools.WriteError("����"+billtypename+"ƾ֤�Ѿ�����,���µ���\r\n", _getDate().toString()+billtypename);
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
     * ƾ֤��Ŀ��U8���뵽NC����
     * @param intBtn
     * @throws Exception
     */
    private void onBoInsertcode(int intBtn)  throws Exception{
    	if(getBillUI().showYesNoMessage("�Ƿ��������ɿ�Ŀ?") == UIDialog.ID_YES){
        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        	IVOPersistence  iVOPersistence =    (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
        	//��ѯU8code���е������ڲ���NC����
        	StringBuffer br=new StringBuffer();
        	br.append("select ");
        	br.append("ccode,");  					//��Ŀ����              
        	br.append("ccode_name,");  		 		//��Ŀ����                  
        	br.append("ccode_engl,");  		 		//��ĿӢ������              
        	br.append("igrade,");  				    //���뼶��                  
        	br.append("bproperty,");		  	    //��Ŀ����                  
        	br.append("cbook_type,"); 	   			//��ҳ��ʽ                  
        	br.append("cbook_type_engl,"); 			//��ҳ��ʽӢ������               
        	br.append("chelp,");				    //��Ŀ������            
        	br.append("cexch_name,");			    //��������                    
        	br.append("cmeasure,");		            //������λ                  
        	br.append("bperson,");                  //�Ƿ������������       
        	br.append("bcus ,");                    //�Ƿ�ͻ���������    
        	br.append("bsup ,");                    //�Ƿ�Ӧ���������� 
        	br.append("bdept,");                    //�Ƿ��ź���     
        	br.append("bitem,");                    //�Ƿ���Ŀ����      
        	br.append("cass_item ,");               //��Ŀ�������          
        	br.append("br ");                		//�Ƿ��ռ���                      
        	br.append(" from code");
        	ClientEnvironment ce = ClientEnvironment.getInstance();
        	CodeVO[] codes=pubItf.U8Select(br.toString(),ce.getCorporation().getPk_corp());
        	if(codes!=null && codes.length>0){
        		iVOPersistence.insertVOArray(codes);
        	}
        	
        	getBillUI().showWarningMessage("��Ŀ������������");
            getBillUI().showHintMessage("��Ŀ�Ѿ���������");
    	}
	}
    
    
	

    private void onBoBrows(int intBtn) throws java.lang.Exception {
        // ����ִ��ǰ����
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
        // ����ִ�к���
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
        //���йرհ�ťʱ�Թرհ�ť�Ŀ��� 
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

