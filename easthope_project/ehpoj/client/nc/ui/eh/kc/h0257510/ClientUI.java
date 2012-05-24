/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.kc.h0257510;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;


/**
 * ����:ԭ���̵㵥����
 * ZB01
 * @author WB
 * 2008-10-15 16:30:09
 *
 */
public class ClientUI extends AbstractClientUI{
	
	public ClientUI() {
        super();
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO �Զ����ɷ������
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
  
   
   
 
    /*
     * ע���Զ��尴ť
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
    	super.initPrivateButton();
    }
 
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	if(strKey.equals("vinvcode")){
    		String pk_period = getBillCardPanel().getHeadItem("pk_period").getValueObject()==null?null:
    			 					getBillCardPanel().getHeadItem("pk_period").getValueObject().toString();			//�ڼ�
    		String pk_store = getBillCardPanel().getHeadItem("pk_store").getValueObject()==null?null:
					getBillCardPanel().getHeadItem("pk_store").getValueObject().toString();								//�ֿ�
    		if(pk_period==null||pk_store==null){
    			showErrorMessage("����ѡ���ڼ���̵�ֿ�!");
    		}
    	}
    	return super.beforeEdit(e);
    }
    @Override
    public void afterEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	//�̵�ʱ��������ȡ�� ���µĿ��
    	if(strKey.equals("vinvcode")){
    		int row=e.getRow();
    		String pk_period = getBillCardPanel().getHeadItem("pk_period").getValueObject()==null?"":
    			 					getBillCardPanel().getHeadItem("pk_period").getValueObject().toString();			//�ڼ�
    		String pk_store = getBillCardPanel().getHeadItem("pk_store").getValueObject()==null?"":
					getBillCardPanel().getHeadItem("pk_store").getValueObject().toString();								//�ֿ�
    		String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
        							getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();					//����
    		HashMap hm = getInvKc(pk_store, pk_period, pk_invbasdoc);
    		UFDouble kcamount = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());			//�������
    		getBillCardPanel().setBodyValueAt(kcamount, row, "totalnum");
    		
    	}
    	super.afterEdit(e);
    }
    
    /**
     * �õ��������
     * @param pk_store
     * @param pk_period
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap getInvKc(String pk_store,String pk_period,String pk_invbasdoc){
    	HashMap hm = new HashMap();
    	StringBuffer sql = new StringBuffer()
    	.append(" select b.pk_invbasdoc,sum(isnull(b.qmsl,0)) qmsl")
    	.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")
    	.append(" where a.pk_kcybb = b.pk_kcybb")
    	.append(" and a.pk_period = '"+pk_period+"'")
    	.append(" and a.pk_store = '"+pk_store+"'")
    	.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
    	.append(" and isnull(a.dr,0)=0")
    	.append(" and isnull(a.dr,0)=0")
    	.append(" group by b.pk_invbasdoc");
    	try{
	    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	    	ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	    	if(arr!=null&&arr.size()>0){
	    		UFDouble kcamount = new UFDouble(0);				//�������
	    		for(int i=0;i<arr.size();i++){
	    			HashMap hmA = (HashMap)arr.get(i);
	    			kcamount = new UFDouble(hmA.get("qmsl")==null?"0":hmA.get("qmsl").toString());
	    			hm.put(pk_invbasdoc, kcamount);
	    		}
	    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return hm;
    }
}

   
    


