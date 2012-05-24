
package nc.ui.eh.h08002;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.h08002.CubasdocVO;
import nc.vo.eh.trade.z00115.CustVO;
import nc.vo.eh.trade.z00115.CustkxlVO;
import nc.vo.eh.trade.z00115.CustyxdbVO;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.HYBillVO;

/**
 * 说明: 期初化客商数据导入
 * @author zqy
 * 2008-8-5 11:02:18
 */ 

public class ClientEventHandler  extends CardEventHandler {

    public static Workbook         w    = null;
    public static WritableWorkbook ww   = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null; // 文本框,用于显示文件路径
    public static CubasdocVO[] ivo = null;
    
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

    private nc.ui.pub.beans.UITextField getTFLocalFile() {
        if (txtfFileUrl == null) {
            try {
                txtfFileUrl = new nc.ui.pub.beans.UITextField();
                txtfFileUrl.setName("txtfFileUrl");
                txtfFileUrl.setBounds(270, 160, 230, 26);
                txtfFileUrl.setMaxLength(2000);
                txtfFileUrl.setEditable(false);

            } catch (java.lang.Throwable e) {
                handleException(e);
            }
        }
        return txtfFileUrl;
    }
    
    private void handleException(java.lang.Throwable exception) {
        System.out.println("--------- 未捕捉到的异常 ---------");
        exception.printStackTrace(System.out);
    }


    /**
     * 功能：
     * <p>读取期初客商数据，只读取营销代表和可销料</p>
     */
    public void onBoStockCope(){   
        try {
            nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            res = fileChooser.showOpenDialog(getBillUI());
            if (res == 0) {
                getTFLocalFile().setText(fileChooser.getSelectedFile().getAbsolutePath());
                txtFile = fileChooser.getSelectedFile();
                String filepath = txtFile.getAbsolutePath();
                WriteToExcel.creatFile(filepath);
                WriteToExcel.readData("", 0, 0, 0);
            } else {
                return;
            }
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        
        ivo = nc.ui.eh.h08002.WriteToExcel.vos;
       
        if(ivo!=null && ivo.length>0){
            HYBillVO billVO = new HYBillVO();
            billVO.setChildrenVO(ivo);
            String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vgh").getEditFormulas();
            getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formual);
            if (getBufferData().isVOBufferEmpty()) {
                getBufferData().addVOToBuffer(billVO);
            } else {
                getBufferData().setCurrentVO(billVO);
            }
            getBufferData().setCurrentRow(0);
            getBillUI().showWarningMessage("读取成功！");
        }
    }         

    /**
     * 功能:
     * <p>
     * 导入数据，向eh_cust,eh_custkxl(可销料),eh_custyxdb(营销代表)
     * </p>
     */
    private void onBoStockChange() throws Exception {
       int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
       for(int i=0;i<row;i++){
           String memo = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "memo")==null?"":
               getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "memo").toString();
           if(memo.length()>0){
               getBillUI().showErrorMessage("第("+(i+1)+")行备注有错误,请到Excel表中检查!请核实!");
               return;
           }
       }

       CubasdocVO[] cvo = nc.ui.eh.h08002.WriteToExcel.vos;
       String pk_corp = _getCorp().getPk_corp();
       PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());    
       if(cvo!=null && cvo.length>0){
           int iRet = getBillUI().showYesNoMessage("是否确定进行删除客商可销料营销代表中旧数据操作?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
        	 //导入前先删除表里的数据 
//             String sql =" delete from  eh_custyxdb  where pk_cubasdoc in (select pk_cumandoc from bd_cumandoc where pk_corp = '"+pk_corp+"')";
//             pubItf.updateSQL(sql);
//             String Sql =" delete from  eh_custkxl  where pk_cubasdoc in (select pk_cumandoc from bd_cumandoc where pk_corp = '"+pk_corp+"')";
//             pubItf.updateSQL(Sql);
//             String SQL = " delete from  eh_cust   where pk_corp = '"+pk_corp+"'";
//             pubItf.updateSQL(SQL);
        	   
        	   String sql = " delete from eh_custyxdb where pk_cust in (select pk_cust from eh_cust where pk_corp = '"+pk_corp+"') ";
        	   pubItf.updateSQL(sql);
        	   String Sql = " delete from eh_custkxl where pk_cust in (select pk_cust from eh_cust where pk_corp = '"+pk_corp+"') ";
        	   pubItf.updateSQL(Sql);
        	   String SQL = " delete from eh_cust where pk_corp = '"+pk_corp+"' ";
        	   pubItf.updateSQL(SQL);
        	   
        	   
             //<修改>删除eh_custaddr表对应的记录。时间：2009-09-04
             String sql_custaddr =" delete from  eh_custaddr  where pk_cubasdoc in (select pk_cumandoc from bd_cumandoc where pk_corp = '"+pk_corp+"')";
             pubItf.updateSQL(sql_custaddr);
           }else{
               return;
           }
       }else{
           getBillUI().showErrorMessage("请先读取要导入数据的Excel表!");
           return;
       }
       /**
        * 生成营销代表、可销料，以及他们的管理表eh_cust，并保存
        */
       pubItf.ReadDateks(cvo);
       getBillUI().showWarningMessage("导入成功！");
    }

   
}
