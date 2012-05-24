package nc.ui.eh.uibase;

import java.awt.Container;
import java.awt.Frame;
import nc.ui.pub.pf.IinitQueryData;
import nc.ui.pub.pf.IinitQueryData2;
import nc.ui.pub.query.QueryConditionClient;

/**
 * 说明: 抽象出来单据参照的查询ID类
 * 
 * @author honghai 2007-10-22 下午02:40:33
 */

public class AbstractBTOBBillQueryDLG extends QueryConditionClient implements IinitQueryData,
        IinitQueryData2 {
    /**单据的功能结点*/
    protected String funNode = null;
    /**标题*/
    protected String title = null;
    /** 当前单据类型 */
    protected String sCurBillType   = null;
    /** 业务类型 */
    protected String m_busitype     = null;
    /** 来源单据类型 */
    protected String m_destbilltype = null;
    /** 操作员 */
    protected String m_operator     = null;
    /** 公司主键 */
    protected String m_pkCorp       = null;
    public AbstractBTOBBillQueryDLG() {
        super();
    }

    public AbstractBTOBBillQueryDLG(Container arg0) {
        super(arg0);
    }

    public AbstractBTOBBillQueryDLG(Container arg0, String arg1) {
        super(arg0, arg1);
    }

    public AbstractBTOBBillQueryDLG(Frame arg0) {
        super(arg0);
    }

    public AbstractBTOBBillQueryDLG(Frame arg0, String arg1) {
        super(arg0, arg1);
    }

    public AbstractBTOBBillQueryDLG(boolean arg0) {
        super(arg0);
    }

    protected void setBillType(java.lang.String newBizType) {
        sCurBillType = newBizType;
    }

    protected void initiNormal() throws Exception {
        hideNormal();
    }

    public AbstractBTOBBillQueryDLG(
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
    }

    protected void initiTitle() {
        setTitle(title);
    }

    protected void initiDefine() throws Exception {
        // 装载模板
        setTempletID(m_pkCorp, funNode, m_operator, m_busitype);
    }

    public void initData(String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, String nodeKey, Object userObj) throws Exception {
    }


}
