package nc.ui.eh.iso.z0502005;

import java.util.ArrayList;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵������ⱨ�� 
 * @author ����Դ 
 * ʱ�䣺2008-4-14
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

    public void onBoSave() throws Exception {
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();  
        AggregatedValueObject agg = getBillUI().getVOFromUI();
        StockCheckreportVO report=(StockCheckreportVO) agg.getParentVO();
        String vsourcebilltype  = report.getVsourcebilltype();
        
        /**�ز�ʱ��������OA��������  add by wb at 2008-10-22 14:37:41*/
        boolean tcflag = report.getTc_flag().booleanValue();
        String oaspcode = report.getOaspcode();				//OA��������
        if(tcflag){
        	if(oaspcode==null||oaspcode.length()==0){
        		getBillUI().showErrorMessage("�زɱ�������OA��������!");
        		return;
        	}
        }
        
        //��Ʒ�������ѡ��һ�����Ϻ�ͰѸ����Ͻ��л�д add by zqy 2008-6-6 17:08:23
        else if(vsourcebilltype.equals(IBillType.eh_z0502505)){        	  
//            AggregatedValueObject aggvo = getBillUI().getVOFromUI();
//            StockCheckreportVO svo = (StockCheckreportVO) aggvo.getParentVO();
//            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//            String pk_procheckapplys=svo.getPk_procheckapplys();//��ѡ���ݵ�PK
//            pubitf.Updateiso(pk_procheckapplys);
            
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport_c").getRowCount();
            ArrayList<String> list = new ArrayList<String>();//��ų�Ʒ������뵥�ı���PK
            for(int k=0;k<row;k++){
                String pk_procheckapply_b = getBillCardPanelWrapper().getBillCardPanel().
                    getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid")==null?"":getBillCardPanelWrapper().getBillCardPanel().
                            getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid").toString();
                list.add(pk_procheckapply_b);
//                //��ִ̨�л�д���� add by zqy 2008-9-3 14:57:31
//                pubitf.BackUpdateiso(pk_procheckapply_b);               
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
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("allpk", alsql);
        }
        
        StockCheckreportBVO[] sbvo = (StockCheckreportBVO[]) agg.getChildrenVO();
        int length = sbvo.length;
        ArrayList al1=new ArrayList();  //�ϸ�
        ArrayList al2=new ArrayList();  //���ϸ�
        ArrayList al3=new ArrayList();  //����
        ArrayList al4=new ArrayList();  //�ۼ�
        for(int i=0;i<length;i++){
            int result = new Integer(sbvo[i].getResult()==null?"-1":sbvo[i].getResult().toString()).intValue();
            switch (result){
            case 0:
                al1.add("Y");
                break;
            case 1:
                al2.add("Y");
                break;
            case 2:
                al3.add("Y");
                break;
            case 3:
                al4.add("Y");
                break;
            }            
        }
        if(al1.size()==length){
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("resulst", 0); 
        }else if(al2.size()>0){
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("resulst", 1); 
            getBillUI().showWarningMessage("�˵�Ϊ���ϸ����飡");
        }else if(al2.size()==0 && al3.size()>0 && al4.size()==0){
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("resulst", 2); 
        }else if(al2.size()==0 && al3.size()==0 && al4.size()>0){
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("resulst", 3); 
        }else if(al2.size()==0 && al3.size()>0 && al4.size()>0 ){
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("resulst", 4); 
        }        
        
        //�����屸ע���ݷ����ͷ��ע 
        StockCheckreportBVO[] stbvo = (StockCheckreportBVO[]) agg.getChildrenVO();
        StringBuffer memostr = new StringBuffer();
        for(int i=0;i<stbvo.length;i++){
        	 String memo = stbvo[i].getMemo();// �����ע��Ϊ�� ϵͳ���Ƽ����Ŀ����Ӧ�ı�ע���ݵ���ⵥ�еı�ע�ֶ�
             if(memo!=null&&memo.length()>0){
             	String itemname = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vitemname")==null?"":
             						getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vitemname").toString();
             	memostr.append(itemname+":"+memo+"��");
             }
        }
        if(memostr.length()>0){
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("memo", memostr.toString().substring(0, memostr.length()-1));
        }
        
//        //������Դ���ݷֱ���д����д��� edit by wb at 2008-6-10 9:57:00
//        if(vsourcebilltype.equals(IBillType.eh_z0501505)){ //ԭ�ϼ������
//	        String pk_sample=report.getVsourcebillid()==null?"":report.getVsourcebillid().toString();
//	        if(!(pk_sample==null||pk_sample.equals(""))){
//	        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//	        	pubitf.changeFlag("eh_stock_sample", "def_4", "pk_sample", pk_sample, 0);
//	        }           
//        } 
        super.onBoSave();
        
        /*******�����ε��ݻ�д�ı�ǵĶ����ŵ�һ�������У���������쳣���׳��쳣��
         * ���ѻ�д���ɾ�� add by zqy 2010��11��16��9:58:35  ********/
        UpdateBackflag(vsourcebilltype);
        super.onBoRefresh();
	}

    /**
     * �ڴ˴������ε��ݻ�д��ǽ��д����������쳣������д���
     * @throws Exception
     */
    public void UpdateBackflag(String vsourcebilltype) throws Exception {
		try {
			if(vsourcebilltype.equals(IBillType.eh_z0502505)){
				AggregatedValueObject aggvo = getBillUI().getVOFromUI();
	            StockCheckreportVO svo = (StockCheckreportVO) aggvo.getParentVO();
	            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
	            String pk_procheckapplys=svo.getPk_procheckapplys();//��ѡ���ݵ�PK
	            pubitf.Updateiso(pk_procheckapplys);		//��д��Ʒ��ⵥ
	            
	            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport_c").getRowCount();
	            for(int k=0;k<row;k++){
	                String pk_procheckapply_b = getBillCardPanelWrapper().getBillCardPanel().
	                    getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid")==null?"":getBillCardPanelWrapper().getBillCardPanel().
	                            getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid").toString();
	                pubitf.BackUpdateiso(pk_procheckapply_b);               
	            }
			}else if(vsourcebilltype.equals(IBillType.eh_z0501505)){ //ԭ�ϼ������
				AggregatedValueObject agg = getBillUI().getVOFromUI();
		        StockCheckreportVO report=(StockCheckreportVO) agg.getParentVO();
		        String pk_sample=report.getVsourcebillid()==null?"":report.getVsourcebillid().toString();
		        if(!(pk_sample==null||pk_sample.equals(""))){
		        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		        	pubitf.changeFlag("eh_stock_sample", "def_4", "pk_sample", pk_sample, 0);
		        }           
	        } 
		} catch (Exception e) {
			if(vsourcebilltype.equals(IBillType.eh_z0502505)){
				AggregatedValueObject aggvo = getBillUI().getVOFromUI();
	            StockCheckreportVO svo = (StockCheckreportVO) aggvo.getParentVO();
	            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
	            ArrayList arr = new ArrayList();
	            String pk_procheckapplys=svo.getPk_procheckapplys();			//���ݵ�PK
				String sql = " update eh_procheckapply set yjjc_flag = 'N' where pk_procheckapply in ("+pk_procheckapplys+") ";
				pubitf.updateSQL(sql);
				
				int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport_c").getRowCount();
	            for(int k=0;k<row;k++){
	                String pk_procheckapply_b = getBillCardPanelWrapper().getBillCardPanel().
	                    getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid")==null?"":getBillCardPanelWrapper().getBillCardPanel().
	                            getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid").toString();
	                
	                String sql2 = " update eh_procheckapply_b set def_5='N' where pk_procheckapply_b='"+pk_procheckapply_b+"' ";
	                String sql3=" select pk_procheckapply from eh_procheckapply_b where pk_procheckapply_b='"+pk_procheckapply_b+"' " +
	                		" and isnull(dr,0)=0 ";
	                arr.add(sql2);
	                arr.add(sql3);
	                IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	                String pk_procheckapply = (String) iUAPQueryBS.executeQuery(sql3,new ColumnProcessor());
	                String sql4 = " update eh_procheckapply set yjjc_flag='N' where pk_procheckapply='"+pk_procheckapply+"' ";
	                arr.add(sql4);
	                pubitf.UpdateSQLS(arr);
	                arr.clear();
	            }
				
			}else if(vsourcebilltype.equals(IBillType.eh_z0501505)){
				AggregatedValueObject agg = getBillUI().getVOFromUI();
		        StockCheckreportVO report=(StockCheckreportVO) agg.getParentVO();
		        String pk_sample=report.getVsourcebillid()==null?"":report.getVsourcebillid().toString();
		        if(!(pk_sample==null||pk_sample.equals(""))){
		        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		        	pubitf.changeFlag("eh_stock_sample", "def_4", "pk_sample", pk_sample, 1);
		        }           
			}
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
	}

	public void onButton_N(ButtonObject bo, BillModel model) {      
        super.onButton_N(bo, model);
        String bocode=bo.getCode()==null?"":bo.getCode();
        //���ӳ��������ɼ�ⱨ�浥ʱ�򣬳��������ǲ�����༭��ͬʱ���岻�ܽ����в���
        if(bocode.equals("ԭ�ϼ������")){
          try {
			StockCheckreportVO reportVO = (StockCheckreportVO)getBillUI().getChangedVOFromUI().getParentVO();
			String vbilltype = reportVO.getVsbbilltype()==null?"":reportVO.getVsbbilltype();
			if(vbilltype.equals(IBillType.eh_z06005)){
				    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("rkamount").setEnabled(false);
			    }
    		} catch (Exception e) {
    			e.printStackTrace();
    		}          
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sample").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cyperson").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("spnum").setEnabled(false);
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("rkamount").setEnabled(false);
          getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(true);
          try {
			getBillUI().updateButtonUI();
          } catch (Exception e) {
			e.printStackTrace();
          }
          int row=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getRowCount();
          for(int i=0;i<row;i++){
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"itemno", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"vitemname", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"ll_ceil", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"ll_limit", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"rece_ceil", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"rece_limit", false);
              getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"fxtype", false);
          	}
          
          //��Ʒ������뵥���ɼ�ⱨ�浥ʱ����ͷ���ϲ������޸ģ�ͬʱ�������ݲ������޸ģ�����������в���
        }else if(bocode.equals("��Ʒ�������")){
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getRowCount();
            for(int i=0;i<row;i++){
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "vitemname", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "ll_ceil", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "ll_limit", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "rece_ceil", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "rece_limit", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i,"fxtype", false);
            }
        }
        getBillUI().updateUI();
    }

	protected void onBoDelete() throws Exception {
		int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
    	if(res==0){
    		return;
    	}
    	if(res==1){
	    	StockCheckreportVO  nowReportVO = (StockCheckreportVO)getBillUI().getChangedVOFromUI().getParentVO();
			String vsourcebilltype  = nowReportVO.getVsourcebilltype();
	        if(vsourcebilltype.equals(IBillType.eh_z0501505)){
				String pk_invbasdoc = nowReportVO.getPk_invbasdoc();           // ����
				String def_1 = nowReportVO.getDef_1();                         // �ջ�֪ͨ�� pk
				String rk_flag = nowReportVO.getRk_flag();                     // �����
				if(rk_flag.equalsIgnoreCase("Y")){
					 // �Գ������Ļ�д add by wm at 2008��5��22��09:31:05����д��DEF_4�ϣ�
			        AggregatedValueObject agg = getBillUI().getVOFromUI();
			        StockCheckreportVO report=(StockCheckreportVO) agg.getParentVO();
			        String pk_sample=report.getVsourcebillid()==null?"":report.getVsourcebillid().toString();
			        if(!(pk_sample==null||pk_sample.equals(""))){
			        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			        	pubitf.changeFlag("eh_stock_sample", "def_4", "pk_sample", pk_sample, 1);
			        }
			        
					super.onBoDelete();                     
				}else{        // ɾ��һ��δ��������ʱ��һ����ͬ���ݵ������ ��Ϊδ���
					IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
					IVOPersistence iVOPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
					String sql = "select pk_checkreport ,rk_flag from eh_stock_checkreport where def_1='"+def_1+"' and pk_invbasdoc='"+pk_invbasdoc+"' and NVL(rk_flag,'N')='Y' ";
					Vector ves = (Vector) iUAPQueryBS.executeQuery(sql, new VectorProcessor());
					if(ves!=null&&ves.size()>0){
						Vector ve=(Vector) ves.get(0);
						String pk_checkreportIndata = ve.get(0)==null?"":ve.get(0).toString();
						StockCheckreportVO reportVO = (StockCheckreportVO)iUAPQueryBS.retrieveByPK(StockCheckreportVO.class, pk_checkreportIndata);
						reportVO.setRk_flag("N");
						iVOPersistence.updateVO(reportVO);
					}
                    
					// �Գ������Ļ�д add by wm at 2008��5��22��09:31:05����д��DEF_4�ϣ�
			        AggregatedValueObject agg = getBillUI().getVOFromUI();
			        StockCheckreportVO report=(StockCheckreportVO) agg.getParentVO();
			        String pk_sample=report.getVsourcebillid()==null?"":report.getVsourcebillid().toString();
			        if(!(pk_sample==null||pk_sample.equals(""))){
			        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			        	pubitf.changeFlag("eh_stock_sample", "def_4", "pk_sample", pk_sample, 1);
			        }			 
			   }
	        }
            
	        if(vsourcebilltype.equals(IBillType.eh_z0502505)){
	        	// ɾ��ʱ��д����ǵ���Ʒ������ⵥ�ӱ��� add by zqy 2008-6-6 18:30:26
                AggregatedValueObject aggvo = getBillUI().getVOFromUI();
                StockCheckreportVO svo = (StockCheckreportVO) aggvo.getParentVO();
                String pk_procheckapplys=svo.getPk_procheckapplys();//��ѡ���ݵ�PK
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
               
                String sql4 = "update eh_procheckapply set yjjc_flag='N' where pk_procheckapply in ("+pk_procheckapplys+") " +
                    "and NVL(dr,0)=0 ";
                pubitf.updateSQL(sql4);    
                
                int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport_c").getRowCount();
	            for(int k=0;k<row;k++){
	                String pk_procheckapply_b = getBillCardPanelWrapper().getBillCardPanel().
	                    getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid")==null?"":getBillCardPanelWrapper().getBillCardPanel().
	                            getBillModel("eh_stock_checkreport_c").getValueAt(k, "vsourcebillid").toString();
	                
	                String sql = " update eh_procheckapply_b set def_5='N' where pk_procheckapply_b = '"+pk_procheckapply_b+"' and NVL(dr,0)=0 ";
                    pubitf.updateSQL(sql);
	            }
	       }
        }        
        super.onBoTrueDelete();		
	}
    
    protected void onBoLineAdd() throws Exception {
        getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("vitemname").setEdit(true);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("ll_ceil").setEdit(true);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("ll_limit").setEdit(true);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("rece_ceil").setEdit(true);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("rece_limit").setEdit(true);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").getItemByKey("fxtype").setEdit(true);
        
        super.onBoLineAdd();
    }    
//�÷�����houcqע�͵�   
//    protected void onBoQuery() throws Exception {
//        StringBuffer sbWhere = new StringBuffer();
//        if(askForQueryCondition(sbWhere)==false) 
//            return;
//        String sqlWhere = sbWhere.toString();
//        int pos = sqlWhere.indexOf("���ϸ�", 0);
//        if(pos<=-1){
//            sqlWhere = sqlWhere.replaceFirst("�ϸ�", "0");
//        }else{
//            sqlWhere = sqlWhere.replaceFirst("���ϸ�", "1");
//        }
//        SuperVO[] queryVos = queryHeadVOs(sqlWhere);       
//        getBufferData().clear();
//        // �������ݵ�Buffer
//        addDataToBuffer(queryVos);
//        updateBuffer();
//    }

    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn){
            case IEHButton.LOCKBILL:    //�رյ���
                onBoLockBill();
                break;
            case IEHButton.Prev:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.SpecialCG:	//�ز�
            	onBoSpecialCG();
            	break;
        }   
    }

    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    	String tc_flag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("tc_flag").getValueObject()==null?"":
    					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("tc_flag").getValueObject().toString();
    	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("oaspcode").setEnabled(true);
    	StockCheckreportBVO[] bvos = (StockCheckreportBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
    	if(tc_flag!=null&&tc_flag.equals("true")){
    		for(int i=0;i<bvos.length;i++){
    			getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "ll_ceil", true);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "ll_limit", true);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "rece_ceil", true);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "rece_limit", true);
	    	}
    	}
    }
    /**
     * ˵�����ز�ҵ��
     * 		����زɰ�ťʱ���ز��ֶι���,������ָ�����ջ�ָ�궼�����޸ġ�����������OA��������
     * wb at 2008-10-22 13:59:27
     * @throws Exception 
     */
    private void onBoSpecialCG() throws Exception {
    	String tc_flag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("tc_flag").getValueObject()==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("tc_flag").getValueObject().toString();
    	if(tc_flag!=null&&tc_flag.equals("true")){
    		getBillUI().showErrorMessage("�����ز�ҵ��!");
    		return;
    	}
    	int ress = getBillUI().showYesNoMessage("�Ƿ�����ز�ҵ��?");
    	if(ress==4){
    		if(!isAdding()){
    			getBillUI().setBillOperate(IBillOperate.OP_EDIT);
    		}
    		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tc_flag", "Y");
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("oaspcode").setEnabled(true);
        	StockCheckreportBVO[] bvos = (StockCheckreportBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        	for(int i=0;i<bvos.length;i++){
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "ll_ceil", true);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "ll_limit", true);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "rece_ceil", true);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_checkreport").setCellEditable(i, "rece_limit", true);
        	}
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
        getBillUI().showHintMessage(
                nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000503",null,new String[] { nc.vo.format.Format.indexFormat(getBufferData()
                		.getCurrentRow()+1) }));
          setBoEnabled();
    }
     
     protected void onBoLockBill() throws Exception{
       AggregatedValueObject aggvo = getBillUI().getVOFromUI();
       StockCheckreportVO ivo = (StockCheckreportVO) aggvo.getParentVO();
       String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
       String primaryKey = ivo.getPrimaryKey();
       if(lock_flag.equals("Y")){
           getBillUI().showErrorMessage("�õ����Ѿ��ر�!");
           return;
       }
       else if(!primaryKey.equals("")){
           int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ�����йرղ���?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
               IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
               ivo.setAttributeValue("lock_flag", new UFBoolean(true));
               ivoPersistence.updateVO(ivo);
               getBillUI().showWarningMessage("�Ѿ��رճɹ�");
               onBoRefresh();
           }
           else{
               return;
           }
       }
    }
     
     
     //���ö�ҳǩ�Ĵ�ӡģ�� 
    protected void onBoPrint() throws Exception {
    	//add by houcq begin 2010-09-27 
      	int num=0;
    	String billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject().toString();
    	String old = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_8").getValueObject().toString();
    	if (null==old||"".equals(old))
    	{
    		old="-1";
    	}
    	else 
    	{    		
        	try {
            	num= Integer.valueOf(old).intValue()+1;
        	} catch (Exception e) {
        		getBillUI().showErrorMessage("def_8�ֶε�ֵ����ת��Ϊ����");
        		return;
        	}
		}
    	
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
    	String sql = " update eh_stock_checkreport set def_8='"+num+"' where pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and billno='"+billno+"'";
    	pubitf.updateSQL(sql);
    	onBoRefresh();
    	//add by houcq end 
        nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
                dataSource);
        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
                ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
                .getBusinessType(), getBillUI().getNodeKey());
        print.selectTemplate();
        print.preview();
    }  
    
    protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        if (vbillstatus==null){
        }
        else{   
            switch (vbillstatus.intValue()){
                //free
                case IBillStatus.FREE:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(true);
                    break;
                //commit
                case IBillStatus.COMMIT:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(false);
                    break;
                //CHECKGOING
                case IBillStatus.CHECKGOING:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(false);
                    break;
                //CHECKPASS
                case IBillStatus.CHECKPASS:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(false);
                    break;
                //NOPASS
                case IBillStatus.NOPASS:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(false);
                    break;
            }
        }
        super.setBoEnabled();
    }
}