
package nc.ui.eh.h08008;

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
import nc.vo.eh.trade.z00120.InvbasdocVO;
import nc.vo.pub.BusinessException;

/**
 * 
功能：从NC中把物料档案数据导入到U8物料档案中
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
        getBillUI().showOkCancelMessage("物料数据导入之前请确认'物料分类'与'计量单位组'已经在U8系统中维护完毕!");
        ArrayList list = new ArrayList();
        InvbasdocVO[] ivo = null;
        String pk_corp = _getCorp().getPk_corp();
        StringBuffer sql = new StringBuffer()
        .append(" select invcode,invname,invmnecode,pk_invcl,invspec,invtype,colour,brand ")
        .append(" from eh_invbasdoc where isnull(lock_flag,'N')='N' and pk_corp='"+pk_corp+"' and isnull(dr,0)=0 ");
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        if(arr!=null && arr.size()>0){
            String invcode = null;
            String invname = null;
            String invmnecode = null;
            String pk_invcl = null;
            String invspec = null;
            String invtype = null;
            String colour = null;
            String brand = null;
            for(int i=0;i<arr.size();i++){
                HashMap hm = (HashMap)arr.get(i);
                invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
                invname = hm.get("invname")==null?"":hm.get("invname").toString();
                invmnecode = hm.get("invmnecode")==null?"":hm.get("invmnecode").toString();
                pk_invcl = hm.get("pk_invcl")==null?"":hm.get("pk_invcl").toString();
                invspec = hm.get("invspec")==null?"":hm.get("invspec").toString();
                invtype = hm.get("invtype")==null?"":hm.get("invtype").toString();
                colour = hm.get("colour")==null?"":hm.get("colour").toString();
                brand = hm.get("brand")==null?"":hm.get("brand").toString();
                
                InvbasdocVO vo = new InvbasdocVO();
                vo.setInvcode(invcode);
                vo.setInvname(invname);
                vo.setInvmnecode(invmnecode);
                vo.setPk_invcl(pk_invcl);
                vo.setInvspec(invspec);
                vo.setInvtype(invtype);
                vo.setColour(colour);
                vo.setBrand(brand);
                
                list.add(vo);
            }
        }
        if(list!=null && list.size()>0){
            ivo = (InvbasdocVO[]) list.toArray(new InvbasdocVO[list.size()]);
        }
        
        if(ivo!=null && ivo.length>0){
            for(int j=0;j<ivo.length;j++){
                String invcode = ivo[j].getInvcode()==null?"":ivo[j].getInvcode().toString();
                String invname = ivo[j].getInvname()==null?"":ivo[j].getInvname().toString();
                String invmnecode = ivo[j].getInvmnecode()==null?"":ivo[j].getInvmnecode().toString();
                String pk_invcl = ivo[j].getPk_invcl()==null?"":ivo[j].getPk_invcl().toString();
                String invspec = ivo[j].getInvspec()==null?"":ivo[j].getInvspec().toString();
                String invtype = ivo[j].getInvtype()==null?"":ivo[j].getInvtype().toString();
                String colour = ivo[j].getColour()==null?"":ivo[j].getColour().toString();
                String brand = ivo[j].getBrand()==null?"":ivo[j].getBrand().toString();
                
                InvbasdocVO VO = new InvbasdocVO();
                VO.setInvcode(invcode);
                VO.setInvname(invname);
                VO.setInvmnecode(invmnecode);
                VO.setPk_invcl(pk_invcl);
                VO.setInvspec(invspec);
                VO.setInvtype(invtype);
                VO.setColour(colour);
                VO.setBrand(brand);
                
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(VO,j);
            }
        }
        
    }

    private void onBoStockChange() throws BusinessException {  //把显示在界面上的数据给塞到U8客商表中,如果有重复的则UPDATE
        String pk_corp = _getCorp().getPk_corp();
        nc.vo.eh.h08008.InvbasdocVO vo = new nc.vo.eh.h08008.InvbasdocVO();
        vo.setPk_corp(pk_corp);
        vo.setDef_1(_getCorp().getUnitcode());
        PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        StringBuffer SQL = pubItf.Datepassinv(vo);
        if(SQL!=null && SQL.length()>0){
            getBillUI().showWarningMessage(SQL.toString());
        }
    }



}
