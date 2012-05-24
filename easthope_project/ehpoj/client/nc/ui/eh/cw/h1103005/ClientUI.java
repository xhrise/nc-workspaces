package nc.ui.eh.cw.h1103005;

import java.util.Collection;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z00115.CubasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * ˵�����տ 
 * @author ����Դ
 * ʱ�䣺2008-5-28 14:36:07
 */
public class ClientUI extends AbstractClientUI {
   
    public ClientUI() {
        super();
    }
    
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	
	@Override
	protected void initSelfData() {      
		super.initSelfData();
        
		//��ͷ���տ����������˵�
		getBillCardWrapper().initHeadComboBox("sktype", ICombobox.STR_pk_sfkfs,true);
		getBillListWrapper().initHeadComboBox("sktype", ICombobox.STR_pk_sfkfs,true);
	}

	@Override
	public void setDefaultData() throws Exception {
        //��ʼ���տ�����
        getBillCardPanel().setHeadItem("skrq", _getDate());
		super.setDefaultData();
	}
    
    //����Ա���Ѳ��Ŵ�����
    @SuppressWarnings("unchecked")
	@Override
	public void afterEdit(BillEditEvent e) {
        String strKey = e.getKey();
        
        if(strKey.equals("pk_sfktype")){
            String typename = e.getValue()==null?"":e.getValue().toString();
	            if("��λ����".equalsIgnoreCase(typename)){
	                getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(true);
	                getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
	                getBillCardPanel().getHeadItem("pk_psndoc").setValue(null);
	                getBillCardPanel().getHeadItem("vcode").setValue(null);
	                getBillCardPanel().getHeadItem("vname").setValue(null);
	                getBillCardPanel().getHeadItem("vdept").setValue(null);
	                
	                return;
	            }
	            if("Ա������".equalsIgnoreCase(typename)){
	                getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(true);
	                getBillCardPanel().getHeadItem("shinfo").setEnabled(false);
	                getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
	                getBillCardPanel().getHeadItem("pk_cubasdoc").setValue(null);
	                getBillCardPanel().getHeadItem("shinfo").setValue(null);
	                return;
	            }
	            if("��������".equals(typename)){
	                getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
	                getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
	                getBillCardPanel().getHeadItem("shinfo").setEnabled(false);
	                getBillCardPanel().getHeadItem("pk_cubasdoc").setValue(null);
	                getBillCardPanel().getHeadItem("pk_psndoc").setValue(null);
	                getBillCardPanel().getHeadItem("vcode").setValue(null);
	                getBillCardPanel().getHeadItem("vname").setValue(null);
	                getBillCardPanel().getHeadItem("shinfo").setValue(null);
	                getBillCardPanel().getHeadItem("vdept").setValue(null);
	                
	                return;
	            }
            updateUI();
        }
               
       if(strKey.equals("pk_psndoc")){
           //ѡ��Ա���Ѳ��Ŵ�����
           String pk_psndoc = getBillCardPanel().getHeadItem("pk_psndoc")==null?"":
               getBillCardPanel().getHeadItem("pk_psndoc").getValueObject().toString();      
           IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            StringBuffer sql = new StringBuffer()
            .append(" select pk_deptdoc from bd_psndoc ")
            .append(" where pk_psndoc = '"+pk_psndoc+"' and isnull(dr,0)=0 ");
            
            try {
                Vector al = (Vector) iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
                if(al!=null && al.size()>0){
                    Vector all = (Vector) al.get(0);
                    getBillCardPanel().setHeadItem("pk_deptdoc", all.get(0));
                    String[] formual = getBillCardPanel().getHeadItem("pk_deptdoc").getEditFormulas();
                    getBillCardPanel().execHeadFormulas(formual);  //��ȡ�༭��ʽ
                }
            } catch (BusinessException e1) {
                e1.printStackTrace();
            }
        }
        
        
        if(strKey.equals("pk_cubasdoc")){
            String pk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?null:
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            if(pk_cubasdoc!=null){
	            IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	            try {
	            	PubTools tools = new PubTools();
	                UFDouble overage = tools.getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//���ҿͻ����
	                getBillCardPanel().getHeadItem("canusemoney").setEnabled(false);
                    getBillCardPanel().getHeadItem("canusemoney").setValue(overage);
	                Collection ce =  iUAPQueryBS.retrieveByClause(CubasdocVO.class, "pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"+pk_cubasdoc+"')");
	                CubasdocVO cuvo = (CubasdocVO)ce.toArray(new CubasdocVO[ce.size()])[0];
	                boolean shflag = cuvo.getFreecustflag().booleanValue();
	                if(shflag){
	                    getBillCardPanel().getHeadItem("shinfo").setEnabled(true);
	                }else{
	                    getBillCardPanel().getHeadItem("shinfo").setEnabled(false);
	                    getBillCardPanel().getHeadItem("shinfo").setValue(null);
	                }
	            } catch (BusinessException e1) {
	                e1.printStackTrace();
	            } catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
            }
        }
        
        if(strKey.equals("zje")){
        	UFDouble zje = (UFDouble)this.getBillCardPanel().getHeadItem("zje").getValueObject();
        }
        
        super.afterEdit(e);
    }
     	
}