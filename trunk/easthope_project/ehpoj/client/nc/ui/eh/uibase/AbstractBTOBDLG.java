package nc.ui.eh.uibase;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.eh.pub.Toolkits;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明: 抽象出来上下游单据的自定义参照类
 * @author honghai
 * 2007-10-22 下午02:31:11
 */
public abstract class AbstractBTOBDLG extends BillSourceDLG {
    
    /**列表模板*/
    protected nc.ui.pub.bill.BillListPanel m_arrListPanel     = null;
    /**列表容器*/
    protected java.awt.Container           m_parent           = null;
    /**查询框*/
    protected AbstractBTOBBillQueryDLG     queryCondition     = null;
    /**缓存VO[]*/
    protected AggregatedValueObject[]      m_Vos              = null;
    
   
    public AbstractBTOBDLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, parent);
    }

    public AbstractBTOBDLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);       
    }
    /**
     * 功能: 设置表头隐藏列
     * @return
     * @author 王建超
     * 2007-10-22 下午02:45:53
     */
    @Override
	public String[] getHeadHideCol() {
        return new String[] { "" };
    }
    
    /**
     * 功能: 设置表体隐藏列
     * @return
     * @author 王建超
     * 2007-10-22 下午02:45:53
     */
    @Override
	public String[] getBodyHideCol() {
        return null;
    }
    /**
     * 功能: 表头查询条件 
     * @return
     * @author 王建超
     * 2007-10-22 下午02:46:16
     */
    @Override
	public String getHeadCondition() {
    	
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append("' and pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus =");
        strWherePart.append(IBillStatus.CHECKPASS);
        strWherePart.append(" ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /** 
     * 功能: 设置表体查询条件
     * @return String
     * @author 王建超
     * 2007-10-22 下午02:45:53
     */
    @Override
	public String getBodyCondition() {
    	m_whereStr=null;
    	String strWherePart = null;
        return strWherePart;
    }
    /**
     * 功能:  抽象查询界面方法
     * @return
     * @return:AbstractBTOBBillQueryDLG
     * @author 王建超
     * 2007-10-22 下午04:47:18
     */
    protected abstract AbstractBTOBBillQueryDLG getQueryDlg();
    /**
     * 功能: 查询条件
     * @author 王建超
     * 2007-10-22 下午04:54:49
     */
    @Override
	public void onQuery() {
        getQueryDlg().showModal();
        if (getQueryDlg().isCloseOK()) {
            // 返回查询条件
            m_whereStr = getQueryDlg().getWhereSQL();
            loadHeadData();
        }
    }   
    /**
     * 功能:  获取界面VO名称
     * @return: String[]
     * @author 王建超
     * 2007-10-22 下午04:48:09
     */
    protected abstract String[] getBillVos();
    
    /**
     * 功能: 加载表头数据
     * @param row
     * @author 王建超
     * 2007-10-22 下午04:47:37
     */
    @Override
	public void loadHeadData() {
        try {
            // 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
            String tmpWhere = null;
            String headcondtion = getHeadCondition();
            if (!Toolkits.isEmpty(headcondtion)) {
                if (m_whereStr == null) {
                    tmpWhere = " (" + headcondtion + ")";
                } else {
                    tmpWhere = " (" + m_whereStr + ") and (" + headcondtion + ")";
                }
            } else {
                if (m_whereStr == null) {
                    tmpWhere = " 1=1 ";
                }
            }
            getbillListPanel().setHeaderValueVO(null);
            getbillListPanel().setBodyValueVO(null);
            //回去表头VO
            String BillVos = getBillVos()[1];
            SuperVO[] parentvo = HYPubBO_Client.queryByCondition(Class.forName(BillVos), tmpWhere+" order by billno");
            //把查询出的VO数组放进缓存VO m_Vos中
            if(!Toolkits.isEmpty(parentvo)){
                m_Vos= new AggregatedValueObject[parentvo.length];
                for (int i = 0; i < m_Vos.length; i++) {
                    m_Vos[i]=new HYBillVO();
                    m_Vos[i].setParentVO(parentvo[i]);
                }

                getbillListPanel().setHeaderValueVO(parentvo);
                getbillListPanel().getHeadBillModel().execLoadFormula(); // 验证公式
                getbillListPanel().getHeadTable().clearSelection();
            }
        } catch (Exception e) {
            System.out.println("数据加载失败！");
            e.printStackTrace(System.out);
        } 

    }
    
    public String getBodyPk(){
    	return pk;
    }
    /**
     * 功能: 加载表体数据
     * @param row
     * @author 王建超
     * 2007-10-22 下午04:47:37
     */
    static String pk = null;
    @Override
	public void loadBodyData(int row) {
        try {
            if (m_Vos == null || m_Vos.length < row + 1) {
                System.out.println("程序BUG：表头当前选中行超出表头行数!");
                return;
            }
            // 查询过的数据不再查询
            SuperVO[] tmpBodyVo = null;
            if (m_Vos[row].getChildrenVO() == null || m_Vos[row].getChildrenVO().length == 0) {
                String id = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField())
                        .toString();
                if (id == null) {
                    System.out.println("程序BUG：表头ID或TS不能正确获取!");
                    return;
                }
                tmpBodyVo = (SuperVO[]) m_Vos[row].getChildrenVO();
                m_Vos[row].setChildrenVO(tmpBodyVo);
            }
            //获取表体相应行的主键
            /** 此处取表头PK 不能从原来VO里取,因为当表头在排序后 按照 row 取 pk 会出错
            String pk = m_Vos[row].getParentVO().getPrimaryKey().toString(); 
            edit by wb at 2008-7-16 22:37:48
            **/
            pk = getbillListPanel().getHeadBillModel().getValueAt(row, getpkField()).toString();
            String tmpWhere = null;
            if(pk.length()>0){
            	String bodyCondtion = getBodyCondition();
	            if (bodyCondtion != null && bodyCondtion.length() > 0) {
	            	if (m_whereStr == null) {
	                    tmpWhere = " (" + bodyCondtion + ") and ";
	                } else {
	                    tmpWhere = " (" + m_whereStr + ") and (" + bodyCondtion + ") and ";
	                }
	            } else {
	                if (m_whereStr == null) {
	                    tmpWhere = " 1=1  and "; 
	                }
	            }
              tmpWhere = tmpWhere + getpkField()+" = '"+pk+"'";
            }
            //回去参照单据中表体VO
            String BillVos = getBillVos()[2];
            //返回根据条件查询的表体
            SuperVO[] childvo = HYPubBO_Client.queryByCondition(Class.forName(BillVos), tmpWhere);
            getbillListPanel().setBodyValueVO(childvo);
            getbillListPanel().getBodyBillModel().execLoadFormula();
            loadBodyData2(getpkField()+" = '"+pk+"'");			//在多表体时调用
        } catch (Exception e) {
            e.printStackTrace(System.out);
            if (e instanceof java.rmi.RemoteException)
                nc.ui.pub.beans.MessageDialog.showErrorDlg(this, "加载表体", e.getMessage());
        }
    } 

    /**
     * 在多表体时调用
     * wb
     * 2008-12-1 11:31:33
     */
    public  void loadBodyData2(String tmpWhere) throws Exception {
    	
	}

	/**
     * 功能: 确定按钮，点击时判断是否选中行 
     *      当所选行为多选时，把所有子表VO放在一个数组里返回
     *      返回 retBillVo
     * @author 王建超
     * 2007-10-22 下午03:14:05
     */
    @Override
	public void onOk() {
       
        //当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
 
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel()
                    .setBodyModelDataCopy(
                            getbillListPanel().getHeadBillModel().convertIntoModelRow(
                                    getbillListPanel().getHeadTable().getSelectedRow()));
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            if(retBillVo.getChildrenVO()==null||retBillVo.getChildrenVO().length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {
                    list.add(childs[j]);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
    @Override
	public AggregatedValueObject getRetVo() {
        return retBillVo;
    }
    /**
     * 功能: 调用单选时方法getRetVo()
     * @return AggregatedValueObject[]
     * @author 王建超
     * 2007-10-24 下午03:18:48
     */
    @Override
	public AggregatedValueObject[] getRetVos() {
        
        AggregatedValueObject[] agg = {getRetVo()};
        return agg;
    }

    /**
     * 功能: 重载父类方法，实现不同的业务类型调用不同的查询模板
     * @return
     * @author 王建超
     * 2007-10-22 下午02:32:54
     */
    @Override
	protected nc.ui.pub.bill.BillListPanel getbillListPanel() {
        if (ivjbillListPanel == null) {
            try {
                ivjbillListPanel = new nc.ui.pub.bill.BillListPanel(false);
                ivjbillListPanel.setName("billListPanel");
                // user code begin {1}
                //获的显示位数值
                //装载模板
                nc.vo.pub.bill.BillTempletVO vo = ivjbillListPanel.getDefaultTemplet(getBillType(), 
                        getBusinessType(),getOperator(), getPkCorp(), getNodeKey());

                nc.ui.pub.bill.BillListData billDataVo = new nc.ui.pub.bill.BillListData(vo);

                //更改主表的显示位数
                String[][] tmpAry = getHeadShowNum();
                if (tmpAry != null) {
                    setVoDecimalDigitsHead(billDataVo, tmpAry);
                }
                //更改子表的显示位数
                tmpAry = getBodyShowNum();
                if (tmpAry != null) {
                    setVoDecimalDigitsBody(billDataVo, tmpAry);
                }

                ivjbillListPanel.setListData(billDataVo);

                //进行主子隐藏列的判断
                if (getHeadHideCol() != null) {
                    for (int i = 0; i < getHeadHideCol().length; i++) {
                        ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
                    }
                }
                if (getBodyHideCol() != null) {
                    for (int i = 0; i < getBodyHideCol().length; i++) {
                        ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
                    }
                }

                ivjbillListPanel.setMultiSelect(true);
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                ivjExc.printStackTrace();
                // user code end
            }
        }
        return ivjbillListPanel;
    }
      
    /**
     * 功能:  设置单据状态下拉框
     * @param billItem
     * @param values
     * @param isWhithIndex
     * @return: void
     * @author 王建超
     * 2007-10-22 下午02:44:53
     */
    protected void initComboBox(BillItem billItem,Object[] values,boolean isWhithIndex){

        if(billItem != null && billItem.getDataType() == IBillItem.COMBO){
            billItem.setWithIndex(isWhithIndex);

            nc.ui.pub.beans.UIComboBox cmb = (nc.ui.pub.beans.UIComboBox) billItem.getComponent();

            cmb.removeAllItems();

            for (int i = 0; i < values.length; i++) {
                  cmb.addItem(values[i]);
            }
        }
    }
    
    public boolean setCanMultiSelected(boolean can){
    	if(!can){                                // 表头不可以选择多个数据
    		if(retBillVos.length>1){
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * 在上下游时带出表体,如果表体数量在下游已经完全做过则不显示  注意参数
     * @param strsypk_b  上游子表主键字段名
     * @param xytablename 下游子表名
     * @param strVsourcebillid  带到下游子表的上游主表主键字段
     * @param strVsourcebillrowid 带到下游子表的上游子表主键字段
     * @param strxyaount 下游的数量字段 如提货通知单中就是 提货量
     * @param strsyamount 上游的数量字段
     * @return
     */
    public String addBodyCondtion(String strsypk_b,String xytablename,String strVsourcebillid,String strVsourcebillrowid,String strxyaount,String strsyamount){
    	StringBuffer sql = null;
    	// 获取表体相应行的主键
        try {
        	String pk2 = pk;
        	sql = new StringBuffer()
	    	.append(strsypk_b+" not in ")
	        .append(" (select a."+strsypk_b+" from  ")
	        .append("  (select "+strVsourcebillrowid+" "+strsypk_b+",sum(isnull("+strxyaount+",0)) "+strxyaount+","+strsyamount+"")
	        .append("   from "+xytablename+" ")
	        .append("   where "+strVsourcebillid+" = '"+pk2+"' and isnull(dr,0)=0")
		    .append("   group by "+strVsourcebillrowid+","+strsyamount+") a")
	        .append(" where a."+strxyaount+" >= a."+strsyamount+")");
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        return sql.toString();
    }
    
}
