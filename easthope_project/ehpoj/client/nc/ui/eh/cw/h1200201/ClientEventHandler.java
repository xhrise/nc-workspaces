package nc.ui.eh.cw.h1200201;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.ipub.ISQLChange;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;
/**
 * �޽��������
 * @author wb
 * 2008-8-21 10:41:21 
 */
public class ClientEventHandler extends CardEventHandler {
	
	String pk_icout_b = null;
	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 protected void onBoSave() throws Exception {
         //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         //������ⵥ��Ϊ0���ܱ���
         //�����е��޸�״̬�趨******
        int rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        for(int row=0;row<rows;row++ ){
        	int rowstatus  = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowState(row);
         	if(isAdding()){
         		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(row, BillModel.ADD);
         	}else{
         		if (rowstatus ==BillModel.ADD)
         			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(row, BillModel.ADD);
         		else{
         			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(row, BillModel.MODIFICATION);
         		}
         	}
        }
         super.onBoSave();
         getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
         setBodyData();
         updateHJrk();
	 }
	 
	 
	/**
	 * ���ܣ��ػ��ϼ۸���Զ������ȡ�ɱ����������еĳɱ��۸�
	 * ʱ�䣺2009-11-21
	 * ���ߣ���־Զ
	 */
	protected void onBoEdit() throws Exception {
		
		Class c = Class.forName(getUIController().getBillVoName()[1]);
        SuperVO[] vos = getBusiDelegator().queryByCondition(c, ((ClientUI)getBillUI()).getBodyWherePart());
        
        HashMap hm = this.getPrice();//ȡ����ɱ����ϵ���
        int rows = vos.length;
        for(int row = 0;row<rows;row++){
        	IcoutBVO vo = (IcoutBVO) vos[row];
        	String pk_inv = vo.getPk_invbasdoc();//����PK
        	UFDouble price = new UFDouble(0);
        	if(hm!=null&&hm.size()>0){
        		price = new UFDouble(hm.get(pk_inv)==null?"0":hm.get(pk_inv).toString());//���ϼ۸�
        	}
        	UFDouble amount = new UFDouble(vo.getOutamount()==null?"0":vo.getOutamount().toString());//������������
        	UFDouble jine = price.multiply(amount);//�����ܽ��
        	vo.setPrice(price);
        	vo.setDef_7(jine);
        	
        }
        
        //��Ҫ�����
        getBufferData().clear();
        if (vos != null&&vos.length>0) {
            HYBillVO billVO = new HYBillVO();
            //�������ݵ�����
            billVO.setChildrenVO(vos);
            //�������ݵ�����
            if (getBufferData().isVOBufferEmpty()) {
                getBufferData().addVOToBuffer(billVO);
            } else {
                getBufferData().setCurrentVO(billVO);
            }

            //���õ�ǰ��
            getBufferData().setCurrentRow(0);
            String[] pk_out_bs = new String[vos.length]; 
            for(int i=0;i<vos.length;i++){
            	pk_out_bs[i] = vos[i].getPrimaryKey();
            }
            pk_icout_b = PubTools.combinArrayToString(pk_out_bs);
        } else {
            getBufferData().setCurrentRow(-1);
        }
		
		super.onBoEdit();
	}

	protected void onBoBodyQuery() throws Exception {
    		Class c = Class.forName(getUIController().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, ((ClientUI)getBillUI()).getBodyWherePart());
            //����ѯ�������ݼ۸�,�������Ϊ�� ʱ�䣺2010-01-07���ߣ���־Զ
            for(int i=0;i<vos.length;i++){
            	IcoutBVO ic = (IcoutBVO) vos[i];
            	ic.setPrice(new UFDouble(0));
            	ic.setDef_7(new UFDouble(0));
            }
            
            //��Ҫ�����
            getBufferData().clear();
            if (vos != null&&vos.length>0) {
                HYBillVO billVO = new HYBillVO();
                //�������ݵ�����
                billVO.setChildrenVO(vos);
                //�������ݵ�����
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //���õ�ǰ��
                getBufferData().setCurrentRow(0);
                String[] pk_out_bs = new String[vos.length]; 
                for(int i=0;i<vos.length;i++){
                	pk_out_bs[i] = vos[i].getPrimaryKey();
                }
                pk_icout_b = PubTools.combinArrayToString(pk_out_bs);
            } else {
                getBufferData().setCurrentRow(-1);
            }
        }

     public void setBodyData() throws Exception{
    	 Class c = Class.forName(getUIController().getBillVoName()[1]);
    	 SuperVO[] voss = getBusiDelegator().queryByCondition(c, " pk_icout_b in "+pk_icout_b+" ");
         //��Ҫ�����
         getBufferData().clear();
         if (voss != null&&voss.length>0) {
             HYBillVO billVO = new HYBillVO();
             //�������ݵ�����
             billVO.setChildrenVO(voss);
             //�������ݵ�����
             if (getBufferData().isVOBufferEmpty()) {
                 getBufferData().addVOToBuffer(billVO);
             } else {
                 getBufferData().setCurrentVO(billVO);
             }
             //���õ�ǰ��
             getBufferData().setCurrentRow(0);
         }
         getBillUI().updateUI();
     }
     
     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	onBoBodyQuery();
    }
     
    /**
     * �ڱ���ػ����ⵥ���ݽ������������ػ��ϵ��۲���д���ɹ���ⵥ��
     * wb
     * 2008-12-8 17:49:27
     * @throws Exception
     */
    public void updateHJrk() throws Exception{
    	String startdate = _getDate().toString().substring(0,8)+"01"; 	//indate between '"+startdate+"' and '"+_getDate()+"' and 
    	String enddate = _getDate().toString().substring(0,8)+_getDate().getDaysMonth();
    	String pk_corp = _getCorp().getPk_corp();
    	//ȡ�ػ������ƽ������
    	StringBuffer sql = new StringBuffer()
    	.append(" select cast(round(sum(isnull(b.def_7,0))/sum(isnull(outamount,0)),4) as decimal(18,4))  hjdj")
    	.append(" from eh_icout a,eh_icout_b b")
    	.append(" where a.pk_icout = b.pk_icout")
    	.append(" and a.pk_outtype = '"+ISQLChange.OUTTYPE_HJ+"' and a.pk_corp = '"+pk_corp+"'")
    	.append(" and a.outdate between '"+startdate+"' and '"+enddate+"' AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0");
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	Object hjobj = (Object) iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
    	UFDouble hjdj = hjobj==null?new UFDouble("0"):new UFDouble(hjobj.toString());   //�ػ�����
    	//���µ���ⵥ�лػ���ⵥ��
    	StringBuffer updateSql = new StringBuffer()
    	.append(" update eh_stock_in_b set inprice = "+hjdj+" ,def_6 = inamount*"+hjdj+" where pk_in_b in ")
    	.append(" ( select b.pk_in_b")
    	.append(" from eh_stock_in a,eh_stock_in_b b")
    	.append(" where a.pk_in = b.pk_in")
    	.append(" and a.pk_intype = '"+ISQLChange.INTYPE_HJ+"' and a.pk_corp = '"+pk_corp+"'")
    	.append(" and a.indate between '"+startdate+"' and '"+enddate+"' AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0")
    	.append(" )");
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		pubitf.updateSQL(updateSql.toString());
    }
    
    //ȡ�ɱ������е����ϵ���
    public HashMap getPrice(){
    	HashMap priceHm = new HashMap();
    	String cudate = PubTools.getLastDate(_getDate().toString());
        String year = cudate.substring(0,4);
        String month = cudate.substring(5,7);
        StringBuffer sql = new StringBuffer()
        .append(" SELECT arcob.pk_costobj_b,CASE arcob.def_6 WHEN 0 THEN 0 ELSE arcob.sumfy/arcob.def_6 END price ")
        .append(" FROM eh_arap_cosths arco, eh_arap_cosths_b arcob WHERE arco.pk_cosths = arcob.pk_cosths  ")
        .append(" AND arco.pk_corp = '"+_getCorp().getPk_corp()+"' AND NVL(arco.dr,0)=0 AND arco.Nyear = '"+year+"' AND arco.Nmonth = '"+month+"' ");
        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList ar  = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(ar!=null&&ar.size()>0){
				for(int i=0;i<ar.size();i++){
					HashMap m = (HashMap) ar.get(i);
					String pk_invbasdoc =  m.get("pk_costobj_b")==null?"":m.get("pk_costobj_b").toString();
					UFDouble price = new UFDouble(m.get("price")==null?"0":m.get("price").toString(),2);
					priceHm.put(pk_invbasdoc, price);
				}
			}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}
    	return priceHm;
    }
    
    
}
