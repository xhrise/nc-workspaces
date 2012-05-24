/**
 * 
 */
package nc.ui.eh.h08003;

import java.io.File;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.h08003.CustoverageVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 说明: 客户供应商余额基础数据导入
 * @author zqy
 * 2008-8-5 11:02:18
 */

public class ClientEventHandler  extends CardEventHandler {

    public static Workbook         w    = null;
    public static WritableWorkbook ww   = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null; // 文本框,用于显示文件路径
    public static CustoverageVO[] cvo = null;
    
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


    //读取
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

        cvo =nc.ui.eh.h08003.WriteToExcel.vos;
        
        if(cvo!=null && cvo.length>0){
            HYBillVO billVO = new HYBillVO();
            billVO.setChildrenVO(cvo);
            if (getBufferData().isVOBufferEmpty()) {
                getBufferData().addVOToBuffer(billVO);
            } else {
                getBufferData().setCurrentVO(billVO);
            }
            getBufferData().setCurrentRow(0);
            getBillUI().showWarningMessage("读取成功！");
        }
    }         

    //导入
    private void onBoStockChange() throws Exception {
       int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
       for(int i=0;i<row;i++){
           String memo = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_1")==null?"":
               getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_1").toString();
           if(memo.length()>0){
               getBillUI().showErrorMessage("第("+(i+1)+")行备注有错误,请到Excel中检查!请核实!");
               return;
           }
       }
       
       CustoverageVO[] kcvo = nc.ui.eh.h08003.WriteToExcel.vos;
       String[] contents = new String[4];
       if(kcvo!=null && kcvo.length>0){
           int iRet = getBillUI().showYesNoMessage("是否确定进行删除库存旧数据操作?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
             //在导入数据之前先删除表中数据
        	 String pk_corp = _getCorp().getPk_corp();
        	 String pk_period = null;
//             PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
//             String SQL = " delete from eh_custoverage where pk_corp = '"+pk_corp+"'";
//             pubItf.updateSQL(SQL);
        	 CustoverageVO cvo = kcvo[0];
        	 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
             String sql = "select pk_period FROM eh_period WHERE nyear = "+cvo.getDef_2()+" and nmonth = "+cvo.getDef_3()+" and pk_corp = '"+pk_corp+"'";
             try {
             	pk_period = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
             	contents[0] = pk_period;
             	contents[1] = _getOperator();
             	contents[2] = _getDate().toString();
             	contents[3] = pk_corp;
             
             }catch(Exception e){
             	e.printStackTrace();
             }
           }else{
        	   return;
           }
       }else{
           getBillUI().showErrorMessage("请先读取要导入数据的Excel表!");
           return;
       }
     
       PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
       pubItf.ReadDatekc(kcvo,contents);

       getBillUI().showWarningMessage("导入成功！");
    }


}
