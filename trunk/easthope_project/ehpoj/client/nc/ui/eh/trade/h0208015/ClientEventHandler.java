
package nc.ui.eh.trade.h0208015;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.h0208015.TradeBasedataVO;

/**
 * ����:��Ʒӯ����������¼��
 * ZA90
 * @author WB
 * 2008-10-14 16:19:52
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave();
	}

	
	@Override
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		((ClientUI)getBillUI()).setDefaultData();
		getBillUI().updateUI();
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.GENRENDETAIL:    //������ϸ
                onBoGENRENDETAIL();
                break;
            case IEHButton.CALCKCYBB:    //ӯ������
                onBoCALCKCYBB();
                break;
        }  
        super.onBoElse(intBtn);
    }
	 

    //���ɳ�Ʒ��ϸ
	private void onBoGENRENDETAIL() {
		//����ձ���
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("��Ʒ������ϸ��Ҫ����ձ���,�Ƿ�ȷ�����?");
        	if(res==1){
        		int[] rowcount=new int[rows];
                for(int i=rows - 1;i>=0;i--){
                	rowcount[i]=i;
                }
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rowcount);
        	}else{
        		return;
        	}
        }
		StringBuffer sql = new StringBuffer()
		.append("   select a.invcode,a.invname,a.invtype,a.invspec,a.def1 colour,c.brandname,d.pk_invmandoc pk_invbasdoc ")
		.append("  from bd_invmandoc d,bd_invbasdoc a ,eh_stordoc b,eh_brand c ")
		.append("   where d.pk_invbasdoc = a.pk_invbasdoc and d.def1  = b.pk_bdstordoc ")
		.append("   AND a.invpinpai = c.pk_brand")
		.append("   AND b.is_flag = '1'")
		.append(" and ( d.sealflag = 'N' or d.sealflag is null ) ")
		.append("   and d.pk_corp = '"+_getCorp().getPk_corp()+"' ")
		.append("   and nvl(d.dr,0)=0");
		try{
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String pk_invbasdoc = null;			//����
				String invcode = null;
				String invname = null;
				String invspec = null;
				String invtype = null;
				String brand = null;
				String colour = null; 
				for(int i=0;i<arr.size();i++){
					HashMap hm = (HashMap)arr.get(i);
					pk_invbasdoc = hm.get("pk_invbasdoc")==null?null:hm.get("pk_invbasdoc").toString();
					invcode = hm.get("invcode")==null?null:hm.get("invcode").toString();
					invname = hm.get("invname")==null?null:hm.get("invname").toString();
					invspec = hm.get("invspec")==null?null:hm.get("invspec").toString();
					invtype = hm.get("invtype")==null?null:hm.get("invtype").toString();
					brand = hm.get("brandname")==null?null:hm.get("brandname").toString();
					colour = hm.get("colour")==null?null:hm.get("colour").toString();
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invcode, i, "vinvcode");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invname, i, "vinvname");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invspec, i, "invspec");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invtype, i, "invtype");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(colour, i, "vcolour");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(brand, i, "vbrand");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
    
    @Override
    protected void onBoCard() throws Exception {       
        super.onBoCard();
        getButtonManager().getButton(IEHButton.CALCKCYBB).setEnabled(true);
    }
    
    
    //ӯ������ add by zqy 2008��10��157��10:25:37
    @SuppressWarnings("unchecked")
    private void onBoCALCKCYBB() throws Exception {
//       IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//       StringBuffer rqsql = new StringBuffer()
//       .append(" select date from eh_trade_checkdate where pk_corp='"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0 ");
//       ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(rqsql.toString(), new MapListProcessor());
//       ArrayList all = new ArrayList();
//       if(arr!=null && arr.size()>0){
//           for(int i=0;i<arr.size();i++){
//               HashMap hm = (HashMap)arr.get(i);
//               String rq = hm.get("date")==null?"":hm.get("date").toString();   //����ӯ�����˵�����
//               int date_length = (hm.get("date")==null?"":hm.get("date").toString()).length();  //����ӯ�����˵����ڵĳ���
//               String dmakedate = _getDate().toString();
//               String date2 = dmakedate.substring(8, 10);    //ϵͳ��½���ڵ����2λ
//               String last_date = dmakedate.substring(8, 9);
//               if(date_length==1 && last_date.equals("0") ){                  
//                   String rq2 = "0"+rq+"";
//                   if(rq2.equals(date2)){
//                       all.add("Y");
//                   }
//               }
//               if(rq.equals(date2)){
//                   all.add("Y");
//               }  
//           }          
//       }
//       
//       if(all!=null && all.size()>0 ){
//           int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ������ӯ������?");
//           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
//               TradeBasedataVO tvo = new TradeBasedataVO();           
//               tvo.setPk_corp(_getCorp().getPk_corp());
//               tvo.setDmakedate(_getDate());
//               
//               PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//               String sql = " delete from eh_trade_surpluscheck ";
//               pubitf.updateSQL(sql);
//               pubitf.Ykcost(tvo);
//               getBillUI().showWarningMessage("����ɹ���");
//           }
//       }else{
//           getBillUI().showErrorMessage("�����ڲ���ӯ���������涨�ļ�������,���ʵ��");
//           return;
//       }
        int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ������ӯ������?");
        if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            TradeBasedataVO tvo = new TradeBasedataVO();           
            tvo.setPk_corp(_getCorp().getPk_corp());
            tvo.setDmakedate(_getDate());          //����ӯ�����˵ĵ�ǰ����
            
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            String SQL = " delete from eh_trade_surpluscheck where dmakedate='"+_getDate()+"' and isnull(dr,0)=0 ";
            pubitf.updateSQL(SQL);
            
            StringBuffer yksql = new StringBuffer()
            .append(" select distinct a.pk_basedata,a.nyear,a.nmonth,a.pf1,a.fp2,a.managefy,a.sellfy,a.financefy  ")
            .append(" from eh_trade_basedata a , eh_trade_basedata_b b  ")
            .append(" where a.pk_basedata=b.pk_basedata and a.ts =  ")
            .append(" (select max(ts) ts from eh_trade_basedata where nvl(dr,0)=0 AND pk_corp = '"+_getCorp().getPrimaryKey()+"') ")
            .append(" and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and a.pk_corp = '"+_getCorp().getPrimaryKey()+"'");
            
            IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(yksql.toString(), new MapListProcessor());
            if(arr==null||arr.size()==0){
            	this.getBillUI().showErrorMessage("ӯ��������û�����µĸ��ַ��ü۸���ϸ�����ʵ��");
            	return;
            }
            pubitf.Ykcost(tvo);
            
            getBillUI().showWarningMessage("����ɹ���");
        }
    }
  
}

