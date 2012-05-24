package nc.ui.eh.trade.h0208020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.h0208020.TradeCheckBVO;
import nc.vo.eh.trade.h0208020.TradeCheckVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 
���ܣ�ӯ�������쳣��
���ߣ�zqy
���ڣ�2008-10-26 ����10:56:36
 */
public class ClientEventHandler extends AbstractEventHandler {

    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

    @Override
	public void onBoSave() throws Exception {
        super.onBoSave();
    }

	@Override
	protected void onBoCard() throws Exception {
	    super.onBoCard();
	}
    
    @Override
	protected void onBoRefresh() throws Exception {
        super.onBoRefresh2();
    }
    
    @Override
	protected void onBoReturn() throws Exception {
        super.onBoReturn2();
    }

    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn) {
        case IEHButton.CONFIRMBUG: //��ѯ
            onBoStockCope();
            break;
        }       
    }

    /**
     * 
    ���ܣ���ѯ��ӯ�������б߼ʹ��׶�С��0�����ݣ����浽ӯ�������쳣���в���ʾ�ڽ�����
    ���ߣ�zqy
    ���ڣ�2008-10-29 ����11:04:16
    @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void onBoStockCope() throws Exception {
        IVOPersistence iUAPQueryBS1 =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        ClientEnvironment ce = ClientEnvironment.getInstance(); 
        
        String billnosql = "select billno from eh_trade_check where isnull(dr,0)=0 and pk_corp = '"+ce.getCorporation().getPk_corp()+"'";
        Vector vearr = (Vector)iUAPQueryBS.executeQuery(billnosql.toString(),new VectorProcessor());
        String billno = null;               //���ݺ�
        if(vearr!=null && vearr.size()>0){
            String sql = " select max(substring(billno,13,14))+1 billno from eh_trade_check where isnull(dr,0)=0 and pk_corp = '"+ce.getCorporation().getPk_corp()+"' ";
            Vector arr = (Vector)iUAPQueryBS.executeQuery(sql.toString(),new VectorProcessor());
            if(arr!=null && arr.size()>0){
                Vector ve = (Vector)arr.get(0);
                String code = ve.get(0)==null?"":ve.get(0).toString();
                billno = "YKYC"+_getDate().getYear()+""+_getDate().getMonth()+""+_getDate().getDay()+""+code+"";
            }
        }else{
            billno = "YKYC"+_getDate().getYear()+""+_getDate().getMonth()+""+_getDate().getDay()+""+1001+"";
        }
        
        TradeCheckVO[] trvo = null;
        ArrayList list = new ArrayList();
        TradeCheckVO vo = new TradeCheckVO();
        vo.setBillno(billno);
        vo.setDr(0);
        vo.setPk_corp(ce.getCorporation().getPk_corp());
        vo.setCheckdate(_getDate());
        vo.setDmakedate(_getDate());
        vo.setVbillstatus(8);
        vo.setCoperatorid(_getOperator());
        list.add(vo);
        
        if(list!=null && list.size()>0){
            trvo = (TradeCheckVO[])list.toArray(new TradeCheckVO[list.size()]);
        }
        if(trvo!=null && trvo.length>0){
            String upsql = " delete from eh_trade_check where pk_corp = '"+ce.getCorporation().getPk_corp()+"'";
            pubItf.updateSQL(upsql);
            iUAPQueryBS1.insertVOArray(trvo);
        }
        
        String pksql = " select pk_check from eh_trade_check where pk_corp='"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
        Vector vect = (Vector)iUAPQueryBS.executeQuery(pksql.toString(),new VectorProcessor());
        String pk_check = null;
        if(vect!=null && vect.size()>0){
            Vector ve = (Vector)vect.get(0);
            pk_check = ve.get(0)==null?"":ve.get(0).toString();
        }
        
        /**------<��ѯ���߼ʹ��׶�С��0������(�����ӯ��������ϸ��)>-------**/
        StringBuffer sql = new StringBuffer()
        .append(" select pk_cubasdoc ,custname ,pk_invbasdoc ,invname ,dmakedate , ")
        .append(" price ,firstdiscount ,avgnmonth ,avgjd ,avgnyear , ")
        .append(" (price-firstdiscount-avgnmonth-avgjd-avgnyear) avgprice, ")
        .append(" avgprofit ,requireB,(avgprofit-requireB) deffy ")
        .append(" from eh_trade_surpluscheck where mininprofit<0 and pk_corp='"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ");
        
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        ArrayList all = new ArrayList();
        TradeCheckBVO[] tvo = null;
        if(arr!=null && arr.size()>0){
            String pk_cubasdoc = null;        //�ͻ�PK
            String pk_invbasdoc = null;       //����PK
            String custname = null;           //�ͻ�
            String invname = null;            //��������
//            UFDate makedate = null;           //ͳ��ʱ��
            UFDouble price = null;            //�Ƽ�
            UFDouble firstdiscount = null;    //һ���ۿ�
            UFDouble seconddiscount = null;   //�����ۿ�
            UFDouble avgjd = null;            //����
            UFDouble avgnyear = null;         //����
            UFDouble avgprice = null;         //����
            UFDouble bjgx = null;             //�߼ʹ��׶�X
            UFDouble requireB = null;         //�߼ʹ���Ҫ��
            UFDouble deffy = null;            //��Ҫ����Y
            for(int i=0;i<arr.size();i++){
                HashMap hm = (HashMap)arr.get(i);
                pk_cubasdoc = hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
                pk_invbasdoc =hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                custname =hm.get("custname")==null?"":hm.get("custname").toString();
                invname =hm.get("invname")==null?"":hm.get("invname").toString();
//                makedate = new UFDate(hm.get("dmakedate")==null?null:hm.get("dmakedate").toString());
                price = new UFDouble(hm.get("price")==null?"0":hm.get("price").toString());
                firstdiscount = new UFDouble(hm.get("firstdiscount")==null?"0":hm.get("firstdiscount").toString());
                seconddiscount = new UFDouble(hm.get("avgnmonth")==null?"0":hm.get("avgnmonth").toString());
                avgjd = new UFDouble(hm.get("avgjd")==null?"0":hm.get("avgjd").toString());
                avgnyear = new UFDouble(hm.get("avgnyear")==null?"0":hm.get("avgnyear").toString());
                avgprice = new UFDouble(hm.get("avgprice")==null?"0":hm.get("avgprice").toString());
                bjgx = new UFDouble(hm.get("avgprofit")==null?"0":hm.get("avgprofit").toString());
                requireB = new UFDouble(hm.get("requireb")==null?"0":hm.get("requireb").toString());
                deffy = new UFDouble(hm.get("deffy")==null?"0":hm.get("deffy").toString());
                
                TradeCheckBVO bvo = new TradeCheckBVO();
                bvo.setPk_cubasdoc(pk_cubasdoc);
                bvo.setPk_invbasdoc(pk_invbasdoc);
                bvo.setInvname(invname);
                bvo.setCustname(custname);
                bvo.setDr(0);
                bvo.setPk_corp(ce.getCorporation().getPk_corp());
                bvo.setData(_getDate());
                bvo.setPrice(price);
                bvo.setFirstdisdiscount(firstdiscount);
                bvo.setSeconddisdiscount(seconddiscount);
                bvo.setAvgjd(avgjd);
                bvo.setAvgnyear(avgnyear);
                bvo.setAvgprice(avgprice);
                bvo.setPorfit(requireB);
                bvo.setPorfitx(bjgx);
                bvo.setDifferencey(deffy);
                bvo.setPk_check(pk_check);
                all.add(bvo);
            }
        }else{
            String delesql = " delete from eh_trade_check where pk_corp='"+ce.getCorporation().getPk_corp()+"'";
            pubItf.updateSQL(delesql);
            getBillUI().showErrorMessage("û�в鵽�߼ʹ��׶�<0������!");
            return;
        }
        if(all.size()>0){
            tvo = (TradeCheckBVO[])all.toArray(new TradeCheckBVO[all.size()]);
        }
        if(tvo!=null && tvo.length>0){
            String desql = " delete from eh_trade_check_b where pk_corp='"+ce.getCorporation().getPk_corp()+"' ";
            pubItf.updateSQL(desql);
            iUAPQueryBS1.insertVOArray(tvo);
        }
        
        String SQL = " pk_corp='"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0 ";
        SuperVO[] queryVos = queryHeadVOs(SQL);
        getBufferData().clear();
        // �������ݵ�Buffer
        addDataToBuffer(queryVos);
        updateBuffer(); 
        getBillUI().showWarningMessage("��ѯ�ɹ�!");
    }
   
//    /*
//     * ���ܣ���ѯ�Ի�����ʾ
//     */
//     private QueryConditionClient dlg = null;
//     protected QueryConditionClient getQueryDLG(){        
//     if(dlg == null){
//        dlg = createQueryDLG();
//      }
//        return dlg;
//     }
//    
//     protected QueryConditionClient createQueryDLG() {
//       QueryConditionClient dlg = new QueryConditionClient();
//       dlg.setTempletID(_getCorp().getPk_corp(), "h0208020", null, null); 
//       dlg.setNormalShow(false);
//       return dlg;
//      }
//
//     protected void onBoQuery() throws Exception {
//        ClientEnvironment ce = ClientEnvironment.getInstance();
//        QueryConditionClient uidialog = getQueryDLG();
//        String date = ce.getDate().toString();
//        uidialog.setDefaultValue("checkdate", date, "");
//        int type = getQueryDLG().showModal();
//        if(type==1){
//            ConditionVO[] checkdate  = getQueryDLG().getConditionVOsByFieldCode("checkdate");
//            UFDate check_date = null;
//            if(checkdate.length>0){
//                check_date = new UFDate(checkdate[0].getValue()==null?"":checkdate[0].getValue().toString());
//            }
//            String pk_corp = ce.getCorporation().getPk_corp();
//            String SQL = " pk_corp='"+pk_corp+"' and isnull(dr,0)=0 ";
//            SuperVO[] queryVos = queryHeadVOs(SQL);
//            getBufferData().clear();
//            // �������ݵ�Buffer
//            addDataToBuffer(queryVos);
//            updateBuffer(); 
//         }
//     }
     
}