package nc.ui.eh.voucher.h10120;

import java.awt.Container;
import java.awt.Frame;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.IinitQueryData;
import nc.ui.pub.pf.IinitQueryData2;
import nc.ui.pub.query.QueryConditionClient;

/**
 * ˵��: ����������ݲ��յĲ�ѯID��
 * 
 * @author honghai 2007-10-22 ����02:40:33
 */

public class VoucherBaseBillQueryDLG extends QueryConditionClient implements IinitQueryData,
        IinitQueryData2 {
    /**���ݵĹ��ܽ��*/
    protected String funNode = null;
    /**����*/
    protected String title = null;
    /** ��ǰ�������� */
    protected String sCurBillType   = null;
    /** ҵ������ */
    protected String m_busitype     = null;
    /** ��Դ�������� */
    protected String m_destbilltype = null;
    /** ����Ա */
    protected String m_operator     = null;
    /** ��˾���� */
    protected String m_pkCorp       = null;
    public VoucherBaseBillQueryDLG() {
        super();
    }

    public VoucherBaseBillQueryDLG(Container arg0) {
        super(arg0);
    }

    public VoucherBaseBillQueryDLG(Container arg0, String arg1) {
        super(arg0, arg1);
    }

    public VoucherBaseBillQueryDLG(Frame arg0) {
        super(arg0);
    }

    public VoucherBaseBillQueryDLG(Frame arg0, String arg1) {
        super(arg0, arg1);
    }

    public VoucherBaseBillQueryDLG(boolean arg0) {
        super(arg0);
    }

    protected void setBillType(java.lang.String newBizType) {
        sCurBillType = newBizType;
    }

    protected void initiNormal() throws Exception {
        hideNormal();
    }

    public VoucherBaseBillQueryDLG(
            java.awt.Container parent,
            String pk_corp,
            String operator,
            String nodecode,
            String strBizType,
            String strCurrBillType,
            String strBillType,
            Object obUser,String funNode,String title) {

            super(parent);
            setBillType(strBillType);
            m_operator = operator;
            m_pkCorp = pk_corp;
            m_destbilltype = strCurrBillType;
            m_busitype = strBizType;
            this.funNode = funNode;
            this.title = title;
            try {             
                initData(
                    pk_corp,
                    operator,
                    funNode,
                    strBizType,
                    strCurrBillType,
                    strBillType,
                    obUser);

            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }


    public void initData
    (String pkCorp,String operator,String funNode,String businessType,String currentBillType,String sourceBilltype,Object userObj) 
            throws Exception {
        
        m_operator = operator;
        m_pkCorp = pkCorp;
        m_destbilltype = currentBillType;
        m_busitype = businessType;
        this.funNode = funNode;
        
        initiNormal();
        initiDefine();
        initiTitle();
        //add by houcq 2010-10-30
        setDefaultValue("dmakedate",ClientEnvironment.getInstance().getDate().toString(),null);
    }

    protected void initiTitle() {
        setTitle(title);
    }

    protected void initiDefine() throws Exception {
        // װ��ģ��
        setTempletID(m_pkCorp, funNode, m_operator, m_busitype);
    }

    public void initData(String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, String nodeKey, Object userObj) throws Exception {
    }


}
