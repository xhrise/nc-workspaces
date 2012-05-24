
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
 * ˵��: �ڳ����������ݵ���
 * @author zqy
 * 2008-8-5 11:02:18
 */ 

public class ClientEventHandler  extends CardEventHandler {

    public static Workbook         w    = null;
    public static WritableWorkbook ww   = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null; // �ı���,������ʾ�ļ�·��
    public static CubasdocVO[] ivo = null;
    
    public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
        super(arg0, arg1);
    }

    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn) {
        case IEHButton.CONFIRMBUG: //��ȡ
            onBoStockCope();
            break;
        case IEHButton.CARDAPPROVE://����
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
        System.out.println("--------- δ��׽�����쳣 ---------");
        exception.printStackTrace(System.out);
    }


    /**
     * ���ܣ�
     * <p>��ȡ�ڳ��������ݣ�ֻ��ȡӪ������Ϳ�����</p>
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
            getBillUI().showWarningMessage("��ȡ�ɹ���");
        }
    }         

    /**
     * ����:
     * <p>
     * �������ݣ���eh_cust,eh_custkxl(������),eh_custyxdb(Ӫ������)
     * </p>
     */
    private void onBoStockChange() throws Exception {
       int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
       for(int i=0;i<row;i++){
           String memo = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "memo")==null?"":
               getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "memo").toString();
           if(memo.length()>0){
               getBillUI().showErrorMessage("��("+(i+1)+")�б�ע�д���,�뵽Excel���м��!���ʵ!");
               return;
           }
       }

       CubasdocVO[] cvo = nc.ui.eh.h08002.WriteToExcel.vos;
       String pk_corp = _getCorp().getPk_corp();
       PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());    
       if(cvo!=null && cvo.length>0){
           int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ������ɾ�����̿�����Ӫ�������о����ݲ���?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
        	 //����ǰ��ɾ����������� 
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
        	   
        	   
             //<�޸�>ɾ��eh_custaddr���Ӧ�ļ�¼��ʱ�䣺2009-09-04
             String sql_custaddr =" delete from  eh_custaddr  where pk_cubasdoc in (select pk_cumandoc from bd_cumandoc where pk_corp = '"+pk_corp+"')";
             pubItf.updateSQL(sql_custaddr);
           }else{
               return;
           }
       }else{
           getBillUI().showErrorMessage("���ȶ�ȡҪ�������ݵ�Excel��!");
           return;
       }
       /**
        * ����Ӫ�����������ϣ��Լ����ǵĹ����eh_cust��������
        */
       pubItf.ReadDateks(cvo);
       getBillUI().showWarningMessage("����ɹ���");
    }

   
}
