
package nc.ui.eh.h08001;

import java.io.File;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.h08001.InvbasdocVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 说明: 期初化物料数据导入
 * @author zqy
 * date:2008-8-5 11:02:18
 */ 

public class ClientEventHandler  extends CardEventHandler {

    public static Workbook         w    = null;
    public static WritableWorkbook ww   = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null; // 文本框,用于显示文件路径
    public static InvbasdocVO[] ivo = null;
    
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

    //读取物料Excel中的数据
    public void onBoStockCope(){   
        try {
            nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            res = fileChooser.showOpenDialog(getBillUI());
            if (res == 0) {
                getTFLocalFile().setText(fileChooser.getSelectedFile().getAbsolutePath());
                txtFile = fileChooser.getSelectedFile();
                String filepath = txtFile.getAbsolutePath();
                WriteToExcel.creatFile(filepath);//查找文件的路径
                WriteToExcel.readData("", 0, 0, 0);//读取文件的内容
            } else {
                return;
            }
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        
        ivo = nc.ui.eh.h08001.WriteToExcel.vos;       
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

    //确保界面上无误时，导入物料Excel中数据
    private void onBoStockChange() throws Exception {
       int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
       for(int i=0;i<row;i++){
           String memo = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_4")==null?"":
               getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_4").toString();
           if(memo.length()>0){
               getBillUI().showErrorMessage("第("+(i+1)+")行备注有错误,请到Excel表中检查!请核实!");
               return;
           }
       }
      
      InvbasdocVO[] invo = nc.ui.eh.h08001.WriteToExcel.vos;
      if(invo!=null && invo.length>0){
          int iRet = getBillUI().showYesNoMessage("是否确定进行删除物料档案旧数据操作?");
          if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
        	String pk_corp = _getCorp().getPk_corp();
            //在导入数据之前先删除表中数据
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            String sql = " delete from eh_invbasdoc_b where pk_invbasdoc in (select pk_invbasdoc from eh_invbasdoc  where pk_corp = '"+pk_corp+"')";
            pubitf.updateSQL(sql);
            String SQL1 = " delete from eh_invbasdoc  where pk_corp = '"+pk_corp+"'";
            pubitf.updateSQL(SQL1);
          
          }else{
              return;
          }
      }else{
          getBillUI().showErrorMessage("请先读取要导入数据的Excel表!");
          return;
      }
      
      PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName()); 
      pubItf.ReadDatewl(invo);
      
      getBillUI().showWarningMessage("导入成功！");
    }


}
