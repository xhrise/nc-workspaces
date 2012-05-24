/**
 * 
 */
package nc.ui.eh.h08005;

import java.io.File;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.stock.z0150502.PerioddiscountVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 说明: 期间折扣数据导入
 * @author zqy
 * 2008-8-5 11:02:18
 */

public class ClientEventHandler  extends CardEventHandler {

    public static Workbook         w    = null;
    public static WritableWorkbook ww   = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null; // 文本框,用于显示文件路径
    public static PerioddiscountVO[] ivo = null;
    
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
     * <p>读取数据</p>
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
    
        //ivo = nc.ui.eh.h08005.WriteToExcel.vos;
        ArrayList list = nc.ui.eh.h08005.WriteToExcel.list;
        
      if(list!=null&&list.size()>0){
      ivo = (PerioddiscountVO[])list.toArray(new PerioddiscountVO[0]);
  }
       
        if(ivo!=null && ivo.length>0){
            HYBillVO billVO = new HYBillVO();
            billVO.setChildrenVO(ivo);
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
     * 功能：
     * <p>导入数据</p>
     */
    @SuppressWarnings("unchecked")
	private void onBoStockChange() throws Exception {      
       int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
       for(int i=0;i<row;i++){
           String memo = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_3")==null?"":
               getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_3").toString();
           if(memo.length()>0){
               getBillUI().showErrorMessage("第("+(i+1)+")行备注有错误,请到Excel表中检查!请核实!");
               return;
           }
       }

       //PerioddiscountVO[] pervo = nc.ui.eh.h08005.WriteToExcel.vos;
       ArrayList<PerioddiscountVO> pervo = nc.ui.eh.h08005.WriteToExcel.list;
       if(pervo!=null && pervo.size()>0){
           int iRet = getBillUI().showYesNoMessage("是否确定进行删除期间折扣表中旧数据操作?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
               //在导入数据之前先删除表中数据
        	   String pk_corp = _getCorp().getPk_corp();
               PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
               String sql = " DELETE FROM eh_perioddiscount a WHERE a.pk_perioddiscount_h IN ( SELECT b.pk_perioddiscount_h FROM eh_perioddiscount_h b WHERE b.pk_corp = '"+pk_corp+"' ) ";
               pubitf.updateSQL(sql);
               String SQL = " delete from eh_perioddiscount_h where pk_corp = '"+pk_corp+"'";
               pubitf.updateSQL(SQL);
               
           }else{
        	   return;
           }
       }else{
           getBillUI().showErrorMessage("请先读取要导入数据的Excel表!");
           return;
       }
       
       PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
       //pubitf.ReadDateqj(pervo);
       pubitf.importZK(pervo);
       getBillUI().showWarningMessage("导入成功！");
    }

}
