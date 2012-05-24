package nc.ui.eh.trade.z0255001;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.eh.iso.z0502005.StockCheckBillVO;
import nc.vo.eh.iso.z0502005.StockCheckreportBVO;
import nc.vo.eh.iso.z0502005.StockCheckreportCVO;
import nc.vo.eh.iso.z0502005.StockCheckreportVO;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:�����ε��ݽ���(��ⱨ��->���ⵥ)
 * @author wb
 * 2008-12-1 14:15:10
 * ��Բ��ϸ��Ʒ���ػ����⴦��
 */
public class ZA30TOZA11DLG extends AbstractBTOBDLG {
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    static IcoutBVO[] bvos = null;
      
    public ZA30TOZA11DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
		BillItem result = getbillListPanel().getBillListData().getHeadItem("resulst");
		initComboBox(result, ICombobox.STR_RESULE,true);
		BillItem result2 = getbillListPanel().getBillListData().getBodyItem("result");
		initComboBox(result2, ICombobox.STR_PASS_FLAG,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] { 
        		StockCheckBillVO.class.getName(),
 				StockCheckreportVO.class.getName(),
 				StockCheckreportBVO.class.getName(),
                StockCheckreportCVO.class.getName()
               };
    }
    
    
    @Override
	public void loadBodyData2(String tmpWhere) throws Exception{
		//��ȥ���յ����б���VO
        String BillVos2 = getBillVos()[3];			//�������
        //���ظ���������ѯ�ı���
        SuperVO[] childvo = HYPubBO_Client.queryByCondition(Class.forName(BillVos2), tmpWhere);
        getbillListPanel().getBodyBillModel("eh_stock_checkreport_c").setBodyDataVO(childvo);
        getbillListPanel().getBodyBillModel("eh_stock_checkreport_c").execLoadFormula();
	}
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0502005","��ⱨ��");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("'and vbillstatus="+IBillStatus.CHECKPASS+" " +
                            "and (isnull(lock_flag,'N')='N' or lock_flag='') and resulst = 1 and vsourcebilltype = '"+IBillType.eh_z0502505+"' and  isnull(rk_flag,'N')<> 'Y'  and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author honghai
     * 2007-11-1 ����03:14:05
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {

        	//��û�����ε��ݿ��Բ���ʱ ������ʾ
            if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
                super.close();
                JOptionPane.showMessageDialog(null,"û�п��Բ��յĵ���!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
         
            if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
                retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
                retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
                
                if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                    JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;
                }
                
                if(retBillVos.length>1||retBillVos.length==0){
               	 	JOptionPane.showMessageDialog(null,"ֻ����һ�ż�ⱨ�浥!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;          	
               }

                try{
                	StringBuffer sql = new StringBuffer()
                	.append(" select c.pk_invbasdoc, sum(nvl(amount, 0)) amount, d.def3 price ") 
                	.append("   from eh_stock_checkreport   a, ") 
                	.append("        eh_stock_checkreport_c c, ") 
                	.append("        bd_invbasdoc           d, ") 
                	.append("        bd_invmandoc           dd  ") 
                	.append("  where a.pk_checkreport = c.pk_checkreport ") 
                	.append("    and c.pk_invbasdoc = dd.pk_invmandoc  ") 
                	.append("    and d.pk_invbasdoc = dd.pk_invbasdoc  ") 
                	.append("    and a.pk_checkreport = '"+retBillVo.getParentVO().getPrimaryKey()+"' ") 
                	.append("    and nvl(a.dr, 0) = 0 ") 
                	.append("    and nvl(c.dr, 0) = 0  ") 
                	.append("  group by c.pk_invbasdoc, d.def3 ");
	                IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	                ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	                if(arr!=null&&arr.size()>0){
	                	bvos = new IcoutBVO[arr.size()];
	                	String pk_invbasdoc = null;
	                	UFDouble amount = new UFDouble(0);
	                	UFDouble price = new UFDouble(0);
	                	for(int i=0;i<arr.size();i++){
	                		HashMap hm = (HashMap)arr.get(i);
	                		pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
	                		amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
	                		price = new UFDouble(hm.get("price")==null?"0":hm.get("price").toString());
	                		bvos[i] = new IcoutBVO();
	                		bvos[i].setPk_invbasdoc(pk_invbasdoc);
	                		bvos[i].setPrice(price);
	                		bvos[i].setOutamount(amount);
	                	}
	                }
                }catch (Exception e) {
                	e.printStackTrace();
				}
            }
            this.getAlignmentX();
            this.closeOK();
            retBillVo.setChildrenVO(null);

        }   

}