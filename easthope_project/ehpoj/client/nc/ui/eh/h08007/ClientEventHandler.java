
package nc.ui.eh.h08007;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.h08007.CubasdocVO;
import nc.vo.pub.BusinessException;

/**
 * 
功能：从NC中把供应商数据导入到U8客商表中
作者：zqy
日期：2008-11-6 上午09:10:14
 */

public class ClientEventHandler  extends CardEventHandler {

    
    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn) {
        case IEHButton.CONFIRMBUG: //读取
            onBoStockCope();
            break;
        case IEHButton.CARDAPPROVE://导入
            onBoStockChange();
            break;
        }       
    }

    @SuppressWarnings("unchecked")
    private void onBoStockCope() throws Exception {  //读取NC供应商的数据,并在界面上显示出来
        ArrayList list = new ArrayList();
        CubasdocVO[] cvo = null;
        String pk_corp = _getCorp().getPk_corp();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        StringBuffer sql = new StringBuffer()
        .append(" select custcode,custname,custshortname,mnecode,custprop,pk_areacl,pk_marketarea ")
        .append(" from bd_cubasdoc ")
        .append(" where pk_corp='"+pk_corp+"' and isnull(lock_flag,'N')='N' and custprop in (1,2) and isnull(dr,0)=0 ");
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        if(arr!=null && arr.size()>0){
            String custcode = null;
            String custname = null;
            String custshortname = null;
            String mnecode = null;
            Integer custprop = null;
            String pk_areacl = null;
            String pk_marketarea = null;
            for(int i=0;i<arr.size();i++){
                HashMap hm = (HashMap)arr.get(i);
                custcode = hm.get("custcode")==null?"":hm.get("custcode").toString();
                custname = hm.get("custname")==null?"":hm.get("custname").toString();
                custshortname = hm.get("custshortname")==null?"":hm.get("custshortname").toString();
                mnecode = hm.get("mnecode")==null?"":hm.get("mnecode").toString();
                custprop = new Integer(hm.get("custprop")==null?"":hm.get("custprop").toString());
                pk_areacl = hm.get("pk_areacl")==null?"":hm.get("pk_areacl").toString();
                pk_marketarea = hm.get("pk_marketarea")==null?"":hm.get("pk_marketarea").toString();
                
                CubasdocVO vo = new CubasdocVO();
                vo.setCustcode(custcode);
                vo.setCustname(custname);
                vo.setCustshortname(custshortname);
                vo.setMnecode(mnecode);
                vo.setCustprop(custprop);
                vo.setPk_areacl(pk_areacl);
                vo.setPk_marketarea(pk_marketarea);
                
                list.add(vo);
            }
        }
        if(list!=null && list.size()>0){
            cvo = (CubasdocVO[]) list.toArray(new CubasdocVO[list.size()]);
        }
        if(cvo!=null && cvo.length>0){
            for(int j=0;j<cvo.length;j++){
                String custcode = cvo[j].getCustcode()==null?"":cvo[j].getCustcode().toString();
                String custname = cvo[j].getCustname()==null?"":cvo[j].getCustname().toString();
                String custshortname = cvo[j].getCustshortname()==null?"":cvo[j].getCustshortname().toString();
                String mnecode = cvo[j].getMnecode()==null?"":cvo[j].getMnecode().toString();
                Integer custprop = new Integer(cvo[j].getCustprop()==null?"":cvo[j].getCustprop().toString());
                String pk_areacl = cvo[j].getPk_areacl()==null?"":cvo[j].getPk_areacl().toString();
                String pk_marketarea = cvo[j].getPk_marketarea()==null?"":cvo[j].getPk_marketarea().toString();
                
                CubasdocVO VO = new CubasdocVO();
                VO.setCustcode(custcode);
                VO.setCustname(custname);
                VO.setCustshortname(custshortname);
                VO.setMnecode(mnecode);
                VO.setCustprop(custprop);
                VO.setPk_areacl(pk_areacl);
                VO.setPk_marketarea(pk_marketarea);
                
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(VO,j);
            }
        }
     
    }

    private void onBoStockChange() throws BusinessException {  //把显示在界面上的数据给塞到U8客商表中,如果有重复的则UPDATE
        String pk_corp = _getCorp().getPk_corp();
        nc.vo.eh.h08007.CubasdocVO vo = new nc.vo.eh.h08007.CubasdocVO();
        vo.setPk_corp(pk_corp);
        vo.setDef1(_getCorp().getUnitcode());
        PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        StringBuffer SQL = pubItf.Datapassgys(vo);
        if(SQL!=null && SQL.length()>0){
            getBillUI().showWarningMessage(SQL.toString());
        }
    }



}
