
package nc.ui.eh.kc.h0251010;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.KcVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵�����������ϳ��ⵥ 
 * @author ����Դ
 * ʱ�䣺2008-5-08
 */
public class ClientUI extends AbstractClientUI {
    
    public static String vpk_unit = null;
    public static String unitname = null;
    
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
	    //������
         getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
         getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
		//��ó�������
		getBillCardPanel().setHeadItem("ckdate", _getDate());        
        super.setDefaultData();
	}

    @Override
	public boolean beforeEdit(BillEditEvent arg0) {
        //����޸�ǰ�ĵ�λ
        int row = arg0.getRow();
        vpk_unit = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit").toString(); //�޸�ǰ�ĵ�λ
        
        unitname = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "vunit")==null?"":
            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "vunit").toString();
                
        return super.beforeEdit(arg0);
    }
    
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		
		
		//�����н����ڳ�������*����
		if(strKey.equals("blmount")||strKey.equals("price")){
			int row = getBillCardPanel().getBillTable().getRowCount();
			for(int i=0;i<row;i++){
				UFDouble blmount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "blmount")==null?"":
					getBillCardPanel().getBodyValueAt(i, "blmount").toString());
				UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(i, "price")==null?"":
					getBillCardPanel().getBodyValueAt(i, "price").toString());
				UFDouble summoney = blmount.multiply(price);
				getBillCardPanel().setBodyValueAt(summoney, i, "summoney");
				
			}			
		}
		 //�������ϡ��ֿ������Ӧ���(û�в�λʱ),����  add by wb at 2008-6-14 10:17:41
        if(strKey.equals("vstore")||strKey.equals("vinvcode")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
            KcVO kcVO = new KcVO();
            kcVO.setPk_invbasdoc(pk_invbasdoc);
            kcVO.setPk_store(pk_store);
            kcVO.setPk_pos(null);
            kcVO.setPk_corp(_getCorp().getPk_corp());
            UFDouble kcamount = new PubTools().getKCamount_Back(kcVO,_getDate()); //���
            
            int nmonth = _getDate().getMonth();
            int nyear = _getDate().getYear();
            
            CalcKcybbVO kcbbVO = new CalcKcybbVO();
            kcbbVO.setPk_corp(_getCorp().getPk_corp());
            kcbbVO.setPk_kcybb(pk_invbasdoc);
            kcbbVO.setPk_store(pk_store);
            kcbbVO.setPk_period(_getDate().toString());
            kcbbVO.setNyear(nyear);
            kcbbVO.setNmonth(nmonth);
            UFDouble price = new PubTools().getCKPrice(kcbbVO,2);    // ���ϳ��ⵥ�� (�����ڳ����+���³�����)/(�����ڳ�����+���³�������)
            UFDouble blmount = new UFDouble(getBillCardPanel().getBodyValueAt(row, "blmount")==null?"":
				getBillCardPanel().getBodyValueAt(row, "blmount").toString());
			UFDouble summoney = blmount.multiply(price);
			getBillCardPanel().setBodyValueAt(price, row, "price");
			getBillCardPanel().setBodyValueAt(summoney, row, "summoney");
            getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
            
        }
        //�������ϡ��ֿ⡢��λ������Ӧ���  add by wb at 2008-6-14 10:17:41
        if(strKey.equals("vpk_pos")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
            String pk_pos = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_pos")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_pos").toString();
            KcVO kcVO = new KcVO();
            kcVO.setPk_invbasdoc(pk_invbasdoc);
            kcVO.setPk_store(pk_store);
            kcVO.setPk_pos(pk_pos);
            kcVO.setPk_corp(_getCorp().getPk_corp());
            UFDouble kcamount = new PubTools().getKCamount_Back(kcVO,_getDate());
            getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
        } 
        
        //��λѡ�����ж� add by zhangqiyuan 2008-5-23 11:05:47
       if(strKey.equals("vunit")){
           int row = e.getRow();
        String pk_unit = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();  //�����еĵ�λ(�ֶ�)
           UFDouble blmount = new UFDouble(getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "blmount")==null?"0":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "blmount").toString());  //�����е�����
           UFDouble price = new UFDouble(getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "price")==null?"0":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "price").toString());  //�����еĵ���
           UFDouble summoney = new UFDouble(getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "summoney")==null?"0":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "summoney").toString());  //�����еĽ����
//           super.changeDW(vpk_unit, "pk_unit", "blmount", "price", "summoney");          
       }
        
		super.afterEdit(e);
	}
    
     @Override
	protected void initPrivateButton() {
         nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"��һҳ","��һҳ");
         btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnPrev);
         nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"��һҳ","��һҳ");
         btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnNext);
    }
}