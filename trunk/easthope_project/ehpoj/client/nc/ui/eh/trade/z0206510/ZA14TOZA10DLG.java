package nc.ui.eh.trade.z0206510;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.trade.z0207501.InvoiceBVO;
import nc.vo.eh.trade.z0207501.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��:�����ε��ݽ���(���۷�Ʊ�������֪ͨ��)
 * ��־Զ
 * 2009-11-16 ����04:02:28
 */
public class ZA14TOZA10DLG extends AbstractBTOBDLG {
    
	private static final long serialVersionUID = 1L;
	
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	String sDate = ce.getDate().toString();
    
    public ZA14TOZA10DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }

	public String[] getBillVos(){
        return new String[] { 
        		PubBillVO.class.getName(),
        		InvoiceVO.class.getName(),
        		InvoiceBVO.class.getName()
               };
    }
    
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0207501","���۷�Ʊ");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus=1 and isnull(th_flag,'N')='N' and isnull(dr,0)=0 " +
        "and (isnull(lock_flag,'N')='N' or lock_flag='') ");//���ӹر�״̬���
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    public String getBodyCondition() {
    	m_whereStr = null;
    	String sql = addBodyCondtion("pk_invoice_b", "eh_ladingbill_b", "vsourcebillrowid", "vsourcebillid", "zamount","orderamount");
    	return sql;
    }
    
    public String addBodyCondtion(String strsypk_b,String xytablename,String strVsourcebillid,String strVsourcebillrowid,String strxyaount,String strsyamount){
    	StringBuffer sql = null;
    	// ��ȡ������Ӧ�е�����
        try {
        	String pk2 = super.getBodyPk();
        	sql = new StringBuffer()
	    	.append(strsypk_b+" not in ")
	        .append(" (select a."+strsypk_b+" from  ")
	        .append("  (select "+strVsourcebillrowid+" "+strsypk_b+",abs(sum(isnull("+strxyaount+",0))) "+strxyaount+","+strsyamount+"")
	        .append("   from "+xytablename+" ")
	        .append("   where "+strVsourcebillid+" = '"+pk2+"' and isnull(dr,0)=0")
		    .append("   group by "+strVsourcebillrowid+","+strsyamount+") a")
	        .append(" where a."+strxyaount+" >= a."+strsyamount+")");
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        return sql.toString();
    }
    
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author honghai
     * 2007-11-1 ����03:14:05
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
    
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {         
            AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
                    m_billHeadVo, m_billBodyVo);
            retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
            retBillVos = selectedBillVOs;
            
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            String pk_cubasdoc=((InvoiceVO)retBillVos[0].getParentVO()).getPk_cubasdoc();
            // �õ� ���۶��� �ӱ���������õ�������� 
            // �õ� ѡ������۶����������ӱ������ pk ����
            ArrayList arrB = new ArrayList();   // ����ѡ������۶����ӱ� pk
            ArrayList arrC = new ArrayList();   // ����ѡ�������
            String pk_invoice_b = null;
            String pk_invbasdoc = null;
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs = retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {
                	InvoiceBVO bvo = (InvoiceBVO)childs[j];
                    pk_invoice_b = bvo.getPk_invoice_b();
                    pk_invbasdoc = bvo.getPk_invbasdoc();
                    arrB.add(pk_invoice_b);
                    arrC.add(pk_invbasdoc);
                }
            }
            String[] pk_invoice_bs = new String[arrB.size()];
            pk_invoice_bs = (String[])arrB.toArray(pk_invoice_bs);
            String billids = Toolkits.combinArrayToString(pk_invoice_bs);
            HashMap hm = new PubTools().calamount("eh_ladingbill_b", "zamount",billids);                      // ���������
            
            String[] pk_invbasdocs = new String[arrC.size()];
            pk_invbasdocs = (String[])arrC.toArray(pk_invbasdocs);
            pk_invbasdoc = Toolkits.combinArrayToString(pk_invbasdocs);
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                String cubasdoc=((InvoiceVO)retBillVos[i].getParentVO()).getPk_cubasdoc();
                if(!pk_cubasdoc.equals(cubasdoc)){
                    JOptionPane.showMessageDialog(null,"��ѡ����ͬ�Ŀͻ��Ķ���!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;
                }
                
                for (int j = 0; j < childs.length; j++) {
                    InvoiceBVO bvo = (InvoiceBVO)childs[j];
                    pk_invbasdoc = bvo.getPk_invbasdoc();
                    
                   Vector myMeasdoc = getInvRateInfo(pk_invbasdoc);  // ��������PK�õ�����������λPK��ת����
                    String pk_measdoc = myMeasdoc.get(0).toString();
                    UFDouble changerate = new UFDouble(myMeasdoc.get(1).toString());
                    String vsourcebillid = bvo.getPk_invoice_b();
                    //���������������������λ
                    UFDouble ytdef6 = (hm.get(vsourcebillid)==null?new UFDouble(0):new UFDouble(hm.get(vsourcebillid).toString()));
                    //�����������������������λ
                    UFDouble def_6 = (hm.get(vsourcebillid)==null?new UFDouble(0):new UFDouble(hm.get(vsourcebillid).toString())).div(changerate);
                    //�˻�������������λ
                    UFDouble myorderamount = bvo.getAmount();
                  //�˻���������������λ
                    UFDouble orderamount = bvo.getAmount().div(changerate);
                    UFDouble ladingAmount = myorderamount.sub(ytdef6.abs()).div(changerate).multiply(-1);//���������
                    UFDouble zAmount = myorderamount.sub(ytdef6.abs()).multiply(-1);//����
                    bvo.setPk_measdoc(pk_measdoc);//����������λ
                    bvo.setDef_10(ladingAmount);//���������
                    bvo.setDef_9(zAmount);//����
                    bvo.setPk_measdoc(pk_measdoc);
                    UFDouble firdiscut =  ladingAmount.multiply(changerate).div(myorderamount).multiply(new UFDouble(bvo.getFirstdiscount()==null?"0":bvo.getFirstdiscount().toString()));
                    bvo.setDef_8(firdiscut);
                    UFDouble secdiscut =  ladingAmount.multiply(changerate).div(myorderamount).multiply(new UFDouble(bvo.getSeconddiscount()==null?"0":bvo.getSeconddiscount().toString()));
                    bvo.setDef_7(secdiscut);
                    bvo.setDef_6(def_6);
                    UFDouble price = new UFDouble(bvo.getPj()== null?"0":bvo.getPj().toString()).multiply(changerate);
                    bvo.setPrice(price);
                    bvo.setAmount(orderamount);
                    UFDouble bcysje = new UFDouble(0);
                    bcysje = new UFDouble(bvo.getPj()== null?"0":bvo.getPj().toString()).multiply(zAmount).sub(firdiscut).sub(secdiscut);
                    bvo.setTax(bcysje);//����Ӧ�ս��
                    
                    list.add(bvo);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    } 
    
    /**
     * �õ������е��Ƽ�
     */
    public UFDouble getPrice(String pk_invbasdoc){
    	UFDouble price=new UFDouble(0);
    	
    	String sql="select def3 price from bd_invbasdoc where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') and nvl(dr,0)=0";
    	
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hm=(HashMap) al.get(i);
				price=new UFDouble(hm.get("price")==null?"":hm.get("price").toString());
				
				
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return price;
    	
    }
    /**
     * �õ�һ�����ϵ����е�λת����
     * @param pk_invbasdoc
     * @return
     */
    public Vector getInvRateInfo(String pk_invbasdoc){
    	Vector vcc = new Vector();
    	StringBuffer sql = new StringBuffer().append(" select pk_measdoc, mainmeasrate changerate  from bd_convert ") 
        		.append(" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc ")
        		.append(" where pk_invmandoc='"+pk_invbasdoc+"') ");

        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
		    if(vc!=null&&vc.size()>0){
		    	for(int i=0; i<vc.size(); i++){
		    		vcc = (Vector)vc.get(i);
		    	}
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return vcc;
    }   
    
    //ȡ�ÿͻ��Ķ����ۿ��ܶ�
    public UFDouble getAllDiscount(String pk_cubasdoc){
    	UFDouble discount = new UFDouble(0);
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	int date=ce.getDate().getYear()*100+ce.getDate().getMonth();
        StringBuffer sql=new StringBuffer("select pk_cubasdoc keys,sum(isnull(ediscount,0)) value from eh_perioddiscount ")
        .append(" where pk_corp='"+ce.getCorporation().getPk_corp()+"' and (nyear*100+nmonth)='"+date)
        .append("' and pk_cubasdoc='"+pk_cubasdoc+"' and isnull(dr,0)=0 group by pk_cubasdoc");
        try {
	        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
	        if(!arr.isEmpty()){
	             HashMap hm = (HashMap) arr.get(0);
	             discount = new UFDouble(hm.get("value")==null?"0":hm.get("value").toString());
	        }
        } catch (BusinessException e) {
			e.printStackTrace();
		}
        return discount;        
    }
}