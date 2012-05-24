
package nc.ui.eh.cw.h11060;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.cw.h11060.CwHxVO;
import nc.vo.eh.pub.PubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
/**
 * 说明: 成品核销
 * @author 王明
 * 2008年8月26日14:38:55
 */
public class ClientEventHandler extends CardEventHandler {
	ArrayList alA=new ArrayList();//销售发票
	ArrayList alB=new ArrayList();//收款单
    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }
    @Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
        case IEHButton.Prev:    //查询
        	QueData();
            break;
        case IEHButton.Next:    //核销
        	HxData();
            break;
		}
	}
    //查数据
	private void QueData(){
		alA.clear();
		alB.clear();
		CalcDialog calcDialog=new CalcDialog();
		calcDialog.showModal();
		String pk_cubasdoc = CalcDialog.pk_cubasdoc;//客商PK
		//清空表体数据
          int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
          int[] rows=new int[rowcount];
          for(int i=rowcount - 1;i>=0;i--){
              rows[i]=i;
          }
          getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
          getBillUI().updateUI();
		//加载数据
		String sql=" select pk_invoice pk,billno billno,totalprice moeny,hxje hxje,'A' flag,ts from eh_invoice " +
				" where isnull(dr,0)=0 and isnull(hx_flag,'N')='N'  and pk_cubasdoc='"+pk_cubasdoc+"' " +
				" union all " +
				" select pk_sk pk,billno billno,zje moeny,hxje hxje,'B' flag,ts from eh_arap_sk where " +
				" isnull(dr,0)=0 and isnull(hx_flag,'N')='N' and pk_cubasdoc='"+pk_cubasdoc+"' " +
				"  order by ts ";
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList alC = new ArrayList();//对应客商发票的PK
		try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hm=(HashMap) al.get(i);
				String pk=hm.get("pk")==null?"":hm.get("pk").toString();
				String billno=hm.get("billno")==null?"":hm.get("billno").toString();
				UFDouble moeny=new UFDouble(hm.get("moeny")==null?"":hm.get("moeny").toString());
				UFDouble hxje=new UFDouble(hm.get("hxje")==null?"0":hm.get("hxje").toString());
				String flag=hm.get("flag")==null?"":hm.get("flag").toString();
				if(flag.equals("A")){
					CwHxVO vo=new CwHxVO();
					vo.setBillno(billno);
					vo.setJe(moeny);
					vo.setWhxje(moeny);
					vo.setXsdj("销售发票");
					vo.setPk(pk);
					vo.setPk_cubasdoc(pk_cubasdoc);
					alA.add(vo);
					alC.add(vo);
					
				}else if(flag.equals("B")){
					CwHxVO vo=new CwHxVO();
					vo.setBillno(billno);
					vo.setJe(moeny);
					vo.setXsdj("收款单");
					vo.setWhxje(moeny);
					vo.setPk_cubasdoc(pk_cubasdoc);
					vo.setPk(pk);
					alB.add(vo);
					alC.add(vo);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		for(int i=0;i<alC.size();i++){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().addLine();
			CwHxVO vo=(CwHxVO) alC.get(i);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo, i);
			 String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_cubasdoc").getEditFormulas();//获取编辑公式
			 getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
		}
	}
	//核销数据
	private void HxData() throws Exception{
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		ArrayList alhxA=new ArrayList();//eh_invoice 的回写的PK
		ArrayList alhxB=new ArrayList();//eh_arap_sk 的回写的PK
		if(alA==null || alA.size()==0){
			getBillUI().showErrorMessage("没有合适的销售发票，无法核销！");
			return;
		}
		if(alB==null || alB.size()==0){
			getBillUI().showErrorMessage("没有合适的收款单，无法核销！");
			return;
		}
		for(int i=0;i<alA.size();i++){
			CwHxVO voA=(CwHxVO) alA.get(i);
			UFDouble je=voA.getWhxje();
			
			if(je.toDouble()>0){
				for(int j=0;j<alB.size();j++){
					CwHxVO voB=(CwHxVO) alB.get(j);
					UFDouble jeb=voB.getWhxje();
					if(jeb.toDouble()>0 && je.toDouble()>0){
						je=je.sub(jeb);
						
						String skbillnoB=voB.getSkbillno()==null?"":voB.getSkbillno().toString();
						voB.setSkbillno(voA.getBillno()+","+skbillnoB);
						
						String skbillnoA=voA.getSkbillno()==null?"":voA.getSkbillno().toString();
						voA.setSkbillno(voB.getBillno()+","+skbillnoA);
						
						if(je.toDouble()>0){    //销售发票大
							alhxB.add(voB.getPk());
							voB.setWhxje(new UFDouble(0));
							voB.setIshx(new UFBoolean(true));
							
							
							if(je.toDouble()<0){
								je=new UFDouble(0).sub(je);
							}
							voA.setWhxje(je);
						}
						if(je.toDouble()==0){  //销售发票和收款单一样大
							alhxA.add(voA.getPk());
							alhxB.add(voB.getPk());
							voA.setIshx(new UFBoolean(true));
							voB.setIshx(new UFBoolean(true));
							voA.setWhxje(new UFDouble(0));
							voB.setWhxje(new UFDouble(0));
						}
						if(je.toDouble()<0){   //收款单大
							alhxA.add(voA.getPk());
							voA.setWhxje(new UFDouble(0));
							voA.setIshx(new UFBoolean(true));
							if(je.toDouble()<0){
								je=new UFDouble(0).sub(je);
							}
							voB.setWhxje(je);
						}
					}
			}	
			}
			//huixie 到那两张表中去
			if(alhxA!=null &&  alhxA.size()>0){
				String [] pk_invoice=(String[]) alhxA.toArray(new String[alhxA.size()]);
				String pk_invoices=PubTool.combinArrayToString(pk_invoice);
				String updateSql="update eh_invoice set hx_flag='Y' where pk_invoice in"+pk_invoices;
				pubitf.updateSQL(updateSql);
			}
			if(alhxB!=null &&  alhxB.size()>0){
				String [] pk_sk=(String[]) alhxB.toArray(new String[alhxB.size()]);
				String pk_sks=PubTool.combinArrayToString(pk_sk);
				String updateSql2="update eh_arap_sk set hx_flag='Y' where pk_sk in"+pk_sks;
				pubitf.updateSQL(updateSql2);
			}
		}
		//清空数据
		 int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
         int[] rows=new int[rowcount];
         for(int i=rowcount - 1;i>=0;i--){
             rows[i]=i;
         }
         getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
         getBillUI().updateUI();
		//最后在保存数据
		for(int i=0;i<alA.size();i++){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().addLine();
			CwHxVO vo=(CwHxVO) alA.get(i);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo, i);
			String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_cubasdoc").getEditFormulas();//获取编辑公式
			getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i,formual);
		}
		for(int i=0;i<alB.size();i++){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().addLine();
			CwHxVO vo=(CwHxVO) alB.get(i);
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(vo, alA.size()+i);
			String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_cubasdoc").getEditFormulas();//获取编辑公式
			 getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(alA.size()+i,formual);
		}
		CwHxVO[] vos=(CwHxVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		for(int i=0;i<vos.length;i++){
			UFDouble whxje = vos[i].getWhxje();
			if(whxje.toDouble()>0){
				String pk=vos[i].getPk();
				String name=vos[i].getXsdj();
				if(name.equals("销售发票")){
					String sql="update eh_invoice  set hxje="+whxje+" where pk_invoice='"+pk+"'";
					pubitf.updateSQL(sql);
				}else if(name.equals("收款单")){
					String sql="update eh_arap_sk  set hxje="+whxje+" where pk_sk='"+pk+"'";
					pubitf.updateSQL(sql);
				}	
			}
		}
		IVOPersistence IVOPersistence =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
		IVOPersistence.insertVOArray(vos);
	}
	
    
  
    
    

}
