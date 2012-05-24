/**
 * 
 */
package nc.ui.eh.h09901;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.h09901.BugVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.HYBillVO;

/**
 * 说明: BUG文档
 * @author 王建超
 * 2007-11-26 下午06:44:05
 */
public class ClientEventHandler extends CardEventHandler {


    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }
    @Override
	protected void onBoQuery() throws Exception {
        onBoBodyQuery();
    }
    
    @Override
	protected void onBoBodyQuery() throws Exception {
        StringBuffer strWhere = new StringBuffer();
        strWhere.append(" 1=1 ");
        strWhere.append(" and vbilltype='");
        strWhere.append(getUIController().getBillType());
        strWhere.append("' ");
        SuperVO[] queryVos = queryHeadVOs(strWhere.toString());     
        if(getBufferData().getCurrentVO()!=null){
         getBufferData().getCurrentVO().setChildrenVO(queryVos);
         updateBuffer();
        }else {return;}
    }
    
    @Override
	protected void onBoSave() throws Exception {
            //非空的有效性判断
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {
            //设置公司编码
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(this.getUIController().getBillType(), i, "vbilltype");
        }
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        super.onBoSave();
        onBoQuery();
    }
    
    //  点修改时增加数据
    @Override
	protected void onBoEdit() throws Exception {
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        if(row==0){
           getBillUI().setBillOperate(IBillOperate.OP_ADD);
           onBoLineAdd();
        }else{
            super.onBoEdit();
        }
    }
    
    @Override
	protected void onBoLineAdd() throws Exception {
        super.onBoLineAdd();
        int countrow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), countrow, "repdate");
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), countrow, "repman");
        getBillCardPanelWrapper().getBillCardPanel().execBodyFormula(countrow, "Vbbr");
    }
    
    @Override
	public void onBoElse(int btn)throws Exception{
        if(btn==IEHButton.CONFIRMBUG){
          int countrow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();

          AggregatedValueObject AggVO = getBillCardPanelWrapper().getBillVOFromUI();
          if(AggVO!=null && countrow>=0){
              BugVO VO = (BugVO)AggVO.getChildrenVO()[countrow];
              VO.setBconfirm(new UFBoolean("Y"));
              VO.setConfirmdate(_getDate());
              VO.setConfirmman(_getOperator());
            IVOPersistence  iUAPQueryBS=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            iUAPQueryBS.updateVO(VO);
            getBillUI().showWarningMessage("确认成功!"); 
          }
          
          getBillCardPanelWrapper().getBillCardPanel().getBillModel().execEditFormulas(countrow);
          onBoRefresh();
      }
        //_-----------------------------数据的交换        
//        if(btn==IEHButton.CARDAPPROVE){
//        	//需要更改是数据
//        	String sql="select pk_invoice,totalprice from eh_invoice ";
//        	HashMap hmarr=new HashMap();
//        	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
//        	for(int i=0;i<al.size();i++){
//        		HashMap hm=(HashMap) al.get(i);
//        		String pk_invoice=hm.get("pk_invoice")==null?"":hm.get("pk_invoice").toString();
//        		UFDouble totalprice=new UFDouble(hm.get("totalprice")==null?"0":hm.get("totalprice").toString());
//        		hmarr.put(pk_invoice, totalprice);
//        	}
//        	//跟新数据
//        	String [] pk=(String[]) hmarr.keySet().toArray(new String[hmarr.size()]);
//        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//        	for(int i=0;i<pk.length;i++){
//        		String updateSQL="update eh_invoice set hxje="+hmarr.get(pk[i])+" where pk_invoice='"+pk[i]+"'";
//        		pubitf.updateSQL(updateSQL);
//        	}
//        }
        //---------------------------------------------
    }
    
    @Override
    protected void onBoRefresh() throws Exception {
    	 Class c = Class.forName(getUIController().getBillVoName()[1]);
         SuperVO[] vos = getBusiDelegator().queryByCondition(c, " vbilltype= '"+getUIController().getBillType()+"'");
         //需要先清空
         getBufferData().clear();

         if (vos != null) {
             HYBillVO billVO = new HYBillVO();
             //加载数据到单据
             billVO.setChildrenVO(vos);
             //加载数据到缓冲
             if (getBufferData().isVOBufferEmpty()) {
                 getBufferData().addVOToBuffer(billVO);
             } else {
                 getBufferData().setCurrentVO(billVO);
             }

             //设置当前行
             getBufferData().setCurrentRow(0);
         } else {
             getBufferData().setCurrentRow(-1);
         }
    }
    
    public void WriteError(String error) throws Exception{
 	   File file=new File("C:/NCPZERROR.txt"); 
 	   StringBuffer brerror=new StringBuffer();
 	  String errors=null;
 	   if(file.exists()){//文件存在的情况
 		   BufferedReader  br=new BufferedReader(new FileReader(file)); 
 		   String line=null;
 		   while((line=br.readLine())!=null){
 			  brerror.append(line+"\t\n");
 		   }
 		   file.delete();  //文件已经删除
 		   br.close();
 	   }
 	   if(brerror.toString()==null){
 		  errors=error;
 	   }else{
 		  errors=error+brerror.toString();//新的错误信息
 	   }
 	   
 	   FileOutputStream out = new FileOutputStream(new File("C:/NCPZERROR.txt"));
 	  BufferedOutputStream Buff=new BufferedOutputStream(out);
 	   byte[] a=errors.getBytes();
 	   Buff.write(a);
 	   Buff.close();
 	   out.close();
    }
}
