
package nc.ui.eh.h08004;

import java.io.File;
import java.util.Vector;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.h08004.CalcKcybbBVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * ˵��: ����±������ݵ���
 * @author zqy
 * 2008-8-5 11:02:18
 */

public class ClientEventHandler  extends CardEventHandler {

    public static Workbook         w    = null;
    public static WritableWorkbook ww   = null;
    private int res;
    private File txtFile = null;
    private nc.ui.pub.beans.UITextField txtfFileUrl = null; // �ı���,������ʾ�ļ�·��
    public static nc.vo.eh.h08004.CalcKcybbBVO[] ivo = null;
    
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

    //��ȡ
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
   
        ivo = nc.ui.eh.h08004.WriteToExcel.vos;             
        if(ivo!=null && ivo.length>0){
            HYBillVO billVO = new HYBillVO();
            billVO.setChildrenVO(ivo);
            if (getBufferData().isVOBufferEmpty()) {
                getBufferData().addVOToBuffer(billVO);
            } else {
                getBufferData().setCurrentVO(billVO);
            }
            getBufferData().setCurrentRow(0);
            
            getBillUI().showWarningMessage("��ȡ�ɹ���");
        }
    }         

    //����
    private void onBoStockChange() throws Exception {
       int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
       for(int i=0;i<row;i++){
           String memo = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "memo")==null?"":
               getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "memo").toString();
           if(memo.length()>0){
               getBillUI().showErrorMessage("��("+(i+1)+")�б�ע�д���,�뵽Excel�м��!���ʵ!");
               return;
           }
       }
 
       CalcKcybbBVO[] cbvo = nc.ui.eh.h08004.WriteToExcel.vos;
       
       //<�޸�>�ڳ�������±����¶��е�ά��У�顣ʱ�䣺2009-09-07
       String nyear = null;
       String nmonth = null;
       String pk_corp_i = null;
       if(cbvo!=null&&cbvo.length>0){
           nyear = cbvo[0].getDef_1();
           nmonth = cbvo[0].getDef_2();
           pk_corp_i = cbvo[0].getDef_4()==null?"":cbvo[0].getDef_4().toString();
       }
       IUAPQueryBS iUAPQueryBS1 =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
       StringBuffer rqsql = new StringBuffer()
       .append(" select pk_period from eh_period where nyear='"+nyear+"' and nmonth='"+nmonth+"' and pk_corp = '"+pk_corp_i+"' and NVL(dr,0)=0 ");
       Vector ve = (Vector)iUAPQueryBS1.executeQuery(rqsql.toString(),new VectorProcessor());
       if(ve == null || ve.size() <= 0){
    	   this.getBillUI().showErrorMessage(""+nmonth+"�¿���±���û��ά���������µ��룡");
    	   return;
       }
       //<�޸�>�޸Ľ���
       if(cbvo!=null && cbvo.length>0){
           int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ������ɾ������±�����о����ݲ���?");
           if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
               //�ڵ�������֮ǰ��ɾ����������
        	   String pk_corp = _getCorp().getPk_corp();
        	   PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	   String SQL2 = " delete from eh_calc_kcybb_b where pk_kcybb in (select pk_kcybb from eh_calc_kcybb where pk_corp = '"+pk_corp+"')";
               pubitf.updateSQL(SQL2);    
        	   String SQL = " delete from eh_calc_kcybb where pk_corp = '"+pk_corp+"'";
               pubitf.updateSQL(SQL);
               String sql =" delete from eh_kc where pk_corp = '"+pk_corp+"'";
               pubitf.updateSQL(sql);
           }else{
        	   return;
           }
       }else{
           getBillUI().showErrorMessage("���ȶ�ȡҪ�������ݵ�Excel��!");
           return;
       }

       PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
       pubitf.ReadDateyb(cbvo);
       getBillUI().showWarningMessage("����ɹ���");
    }

   
}
