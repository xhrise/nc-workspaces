package nc.ui.eh.trade.z00115;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
//import nc.vo.eh.trade.z00115.CubinvbasdocVO;
import nc.vo.eh.trade.z00115.CustBillVO;
import nc.vo.eh.trade.z00115.CustVO;
//import nc.vo.eh.trade.z00115.CustaddrVO;
import nc.vo.eh.trade.z00115.CustkxlVO;
import nc.vo.eh.trade.z00115.CustyxdbVO;

/**
 * ���̿�����Ӫ������ά��
 * @author ����
 * �������� 2008-4-1 16:09:43
 */
public class ClientCtrl extends AbstractManageController { 
    public ClientCtrl() {
        super();
    }

    public String[] getCardBodyHideCol() {
        return null;
    }

    public int[] getCardButtonAry() {
        return new int[]{
                IBillButton.Query,
                IBillButton.Add,
                IBillButton.Edit,
                IBillButton.Line,
                IBillButton.Save,
                IBillButton.Delete,
                IBillButton.Cancel,
                IBillButton.Return,
                IBillButton.Refresh,
                IEHButton.Prev,
                IEHButton.Next,
                IEHButton. GENRENDETAIL,//����Ӫ�����������޸�
                IEHButton.DOCMANAGE//����Ƭ�������޸�
            };
    }

    public boolean isShowCardRowNo() {
        return true;
    }

    public boolean isShowCardTotal() {
        return true;
    }

    public String[] getListBodyHideCol() {
        return null;
    }

    public int[] getListButtonAry() {
        return new int[]{
                IBillButton.Query,
                IBillButton.Add,
                IBillButton.Card,
                IBillButton.Refresh,
                IEHButton.Prev,
                IEHButton.Next
                
            };
    }

    public String[] getListHeadHideCol() {
        return null;
    }

    public boolean isShowListRowNo() {
        return true;
    }

    public boolean isShowListTotal() {
        return true;
    }

    public String[] getBillVoName() {
        return new String[]{
            CustBillVO.class.getName(),
            CustVO.class.getName(),
            //CustaddrVO.class.getName(),//�ӱ�ҳǩ��ɾ��
            //CubinvbasdocVO.class.getName(),
            CustkxlVO.class.getName(),
            CustyxdbVO.class.getName()     
        };
    }

    public String getBodyCondition() {
        return null;
    }

    public String getBodyZYXKey() {
        return null;
    }

    public int getBusinessActionType() {
        return IBusinessActionType.BD;
    }

    public String getChildPkField() {
        return null;
    }

    public String getHeadZYXKey() {
        return null;
    }

    public String getPkField() {
        return "pk_cust";
    }

    public Boolean isEditInGoing() throws Exception {
        return null;
    }

    public boolean isExistBillStatus() {
        return false;
    }

    public boolean isLoadCardFormula() {
        return true;
    }

    public String getBillType() {
        return IBillType.eh_z00115;
    }


}