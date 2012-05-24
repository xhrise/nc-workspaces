package nc.ui.eh.voucher.h10120;

import java.awt.Container;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;


/**
 *��Ӧ������ƾ֤
 *wb 2009-11-5 13:10:27
 */

@SuppressWarnings("serial")
public class VoucherDLG extends VoucherBaseDLG {
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    String pk_billtype = null;  	//��Դ��������
    String[] vos = null;
    PubTools tools = null;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
    public VoucherDLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        tools = new PubTools();
        pk_billtype = getBillType();
        try {
        	vos= PfUtilBaseTools.getStrBillVo(getBillType());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
        getbillListPanel().getBodyUIPanel().setVisible(false); // ���ر���
    }

    @Override
	public String[] getBillVos(){
        return vos;
    }
    
    @Override
	protected VoucherBaseBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new VoucherBaseBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,getFunNode(),"���֪ͨ��");
            queryCondition.setDefaultValue("dmakedate",ce.getDate().toString(),null);
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
        String fieldname=this.getPkField(pk_billtype);        
        strWherePart.append(" nvl(dr,0) = 0 and vbillstatus = 1 and pk_corp = '");
        strWherePart.append(getPkCorp()).append("'");
//        strWherePart.append("' and nvl(pz_flag,'N')<>'Y'");
        // strWherePart.append(" and billno not in (select billcode from dap_finindex where pk_corp = '"+getPkCorp()+"' and nvl(dr,0)=0 )");
        //add by houcq modify 
        //modify by houcq 2011-11-11
        //strWherePart.append(" and billno is not null and billno not in (select billcode from dap_finindex where pk_corp = '"+getPkCorp()+"' and nvl(dr,0)=0 and dmakedate=dap_finindex.busidate)");
        strWherePart.append(" and "+fieldname+" not in (select procmsg from dap_finindex where pk_corp = '"+getPkCorp()+"' and nvl(dr,0)=0)");
		String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
    	m_whereStr = null;
        return " 1=1 ";
    }
    
    /**
     * ����:����ƾ֤
     * @author wb
     * 2009-11-5 14:45:34
     */
    @Override
	public void onBoGenVoucher() {
    		
    		if(!tools.hasVoucherTemplet(pk_billtype, getPkCorp())){
    			 JOptionPane.showMessageDialog(null,"�˵���û����ƾ֤ģ��,������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                 return;
    		}
        	//��û�����ε��ݿ��Բ���ʱ ������ʾ
            if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
                super.close();
                JOptionPane.showMessageDialog(null,"û��ѡ�񵥾�,�޷�����ƾ֤!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
         
            if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
                retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
                retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
                
                if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                    JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;
                }
                
                int impres = 0;
                for(int i=0;i<retBillVos.length;i++){
                	AggregatedValueObject aggVO = retBillVos[i];
                    try {
						boolean res = tools.sendMessageByAggVO(aggVO, getPkCorp(), pk_billtype);
						if(res){
							impres = impres+1;
						}
                    } catch (Exception e) {
						e.printStackTrace();
					}
                }
            	JOptionPane.showMessageDialog(null,"ѡ��"+(retBillVos.length)+"�ŵ���,�ɹ�����ƾ֤"+impres+"��!","��ʾ",JOptionPane.INFORMATION_MESSAGE);            	
            	close();
            	return;
            }
    }
    private String getPkField(String pk_billtype)
    {
    	HashMap<String,String> hm = new HashMap<String,String>();
    	hm.put("ZA10", "pk_ladingbill");
    	hm.put("ZA14", "pk_invoice");
    	hm.put("ZA45", "pk_dbd");
    	hm.put("ZB42", "pk_hjlhs");
    	hm.put("ZA57", "pk_fk");
    	hm.put("ZA59", "pk_stockinvoice");
    	hm.put("ZA61", "pk_sk");
    	hm.put("ZB25", "pk_ckd");
    	hm.put("ZA22", "pk_in");
    	
    	return hm.get(pk_billtype);
    }
   
}