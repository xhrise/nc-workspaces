package nc.ui.eh.uibase;

import nc.ui.trade.bill.AbstractManageController;

/**
 * ˵��:����������,�����˲�����ȴ����ʵ�ֵķ���,�����ͽ���Ӧ�̳д���
 * @author LiuYuan
 * 2007-9-27 ����01:44:35
 */
public abstract class AbstractCtrl extends AbstractManageController {

    /**
     * OperatorController ������ע�⡣
     */
    public AbstractCtrl() {
        super();
    }

    /**
     * ��õ������͡�
     * 
     * @return java.lang.String
     */
    public abstract String getBillType();

    /**
     * ��õ���VO��Ϣ 0--billVo 1--billheadVo 2--billitemVo
     * 
     * @return java.lang.String[]
     */
    public abstract java.lang.String[] getBillVoName();

    /**
     * ��ñ����Զ�����ؼ��� �������ڣ�(2003-1-5 11:27:45)
     * 
     * @return java.lang.String
     */
    public String getBodyZYXKey() {
        return null;
    }

    /**
     * ���BusinessAction����(BD\PF)�� �������ڣ�(2004-1-15 13:47:52)
     * 
     * @return int
     */
    public int getBusinessActionType() {
        return nc.ui.trade.businessaction.IBusinessActionType.PLATFORM;
    }

    /**
     * ���ؾ��忨Ƭ���ӱ����� ע��: �����ڸ÷����ڽ���ʵ����,�÷����ڹ����е��ã� ����δʵ���� �������ڣ�(2002-12-25 16:39:45)
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getCardBodyHideCol() {
        return null;
    }

    /**
     * ����¼��Ŀ�Ƭ�Ķ������顣 ����Ĳμ�IBillButton �������ڣ�(2004-1-3 22:14:47)
     * 
     * @return java.lang.int[]
     */
    public abstract int[] getCardButtonAry();

    /**
     * ���ظõ����ӱ������ �������ڣ�(2002-12-27 16:57:01)
     * 
     * @return java.lang.String
     */
    public String getChildPkField() {
        return null;
    }

    /**
     * ��ñ�ͷ�Զ�����ؼ��� �������ڣ�(2003-1-5 11:27:45)
     * 
     * @return java.lang.String
     */
    public String getHeadZYXKey() {
        return null;
    }

    /**
     * ���ؾ����б��ӱ������ �������ڣ�(2002-12-25 16:39:45) ע�⣺ 1.�����ڸ÷����ڽ���ʵ����,�÷����ڹ����е��ã�
     * ����δʵ����
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getListBodyHideCol() {
        return null;
    }

    /**
     * ����¼����б�Ķ������顣 ����Ĳμ�IBillButton �������ڣ�(2004-1-3 22:14:47)
     * 
     * @return java.lang.int[]
     */
    public abstract int[] getListButtonAry();

    /**
     * ���ؾ����б�ı�ͷ���� ע�⣺ 1.�����ڸ÷����ڽ���ʵ����,�÷����ڹ����е��ã� ����δʵ���� �������ڣ�(2002-12-25
     * 16:39:45)
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getListHeadHideCol() {
        return null;
    }

    /**
     * ���ظõ�������������� �������ڣ�(2002-12-27 16:57:01)
     * 
     * @return java.lang.String
     */
    public abstract String getPkField();

    /**
     * �����������Ƿ���޸ġ� ϵͳĬ�ϲ����޸ģ���������޸������ظ÷��� �������ڣ�(2003-7-21 14:32:26)
     * 
     * @return nc.vo.pub.lang.UFBoolean
     * @exception java.lang.Exception �쳣˵����
     */
    public Boolean isEditInGoing() throws Exception {
        return null;
    }

    /**
     * �Ƿ���ڵ���״̬�� �������ڣ�(2004-2-5 13:04:45)
     * 
     * @return boolean
     */
    public boolean isExistBillStatus() {
        return true;
    }

    /** �Ƿ���ؿ�Ƭ��ʽ��ȱʡfalse */
    public boolean isLoadCardFormula() {
        return true;
    }

    /**
     * ��Ƭ�Ƿ���ʾ�к� �������ڣ�(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowCardRowNo() {
        return true;
    }

    /**
     * ��Ƭ�Ƿ���ʾ�ϼ��� �������ڣ�(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowCardTotal() {
        return true;
    }
    /**
     * ���ݵķ�ʽ�Ƿ��ֹ�ѡ��
     */
    public boolean isCombinNeedDef(){
        return true;
    }
    /**
     * �б��Ƿ���ʾ�к� �������ڣ�(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowListRowNo() {
        return true;
    }

    /**
     * �б��Ƿ���ʾ�ϼ��� �������ڣ�(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowListTotal() {
        return true;
    }

    public String getNotSelMsg() {
        return null;
    }

    public String getPageTitleFld() {
        return "";
    }
    
    public String getBodyCondition() {
        return null;
    }
}