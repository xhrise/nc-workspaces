package nc.ui.eh.kc.h0251005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0452005.ScCprkdBVO;
import nc.vo.eh.sc.h0452005.ScCprkdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * ���ܣ������ε��ݲ��գ���Ʒ��ⵥ�����ϳ��ⵥ�� 
 * Edit: ����Դ 
 * Date:2008-04-14
 */

public class ZA47TOZA46DLG extends AbstractBTOBDLG {
    
	static ArrayList al2 = null;  //ѡ���ͷ��ʱ�򣬴�Ŷ��ŵ��ݵ���PK
	static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    ClientEnvironment ce = ClientEnvironment.getInstance();
    String addsql = null;
    
    public ZA47TOZA46DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        try {
			addsql = getAddCondtion();
		} catch (Exception e) {
			e.printStackTrace();
		}
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] { PubBillVO.class.getName(),
        		ScCprkdVO.class.getName(),
        		ScCprkdBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0452005","��Ʒ��ⵥ");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
    	StringBuffer strWherePart = new StringBuffer();
        try {
	        //����ҵ�����ͣ���˾���룬����״̬��ѯ
	        strWherePart.append(" pk_corp = '");
	        strWherePart.append(getPkCorp());
	        strWherePart.append("'and vbillstatus="+IBillStatus.CHECKPASS+"  and vbilltype = '"+IBillType.eh_h0452005+"' and (isnull(lock_flag,'N')='N' or lock_flag='') and isnull(dr,0)=0 and isnull(is_fenj,'N')='N' ");
	        if(addsql!=null){
	        	strWherePart.append(" and pk_rkd in "+addsql);
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
        return strWherePart.toString();
    }
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author ����Դ
     * 2008-4-9
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
	       	 	JOptionPane.showMessageDialog(null,"��ͷֻ��ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
	            return;          	
            }
            //�жϱ����Ƿ�ȫѡ
            int rows = 0;  //rowΪ�����Ѿ�ѡ�е����ݸ���
            HashMap hm = new HashMap();
            for(int i=0;i<retBillVos.length;i++){
                ScCprkdBVO[] cpbvo = (ScCprkdBVO[]) retBillVos[i].getChildrenVO();
                rows = rows + cpbvo.length;
            }
            String pk_rkd = retBillVo.getParentVO().getAttributeValue("pk_rkd").toString();
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            StringBuffer sql = new StringBuffer()
            .append(" select count(*) mount from eh_sc_cprkd_b where pk_rkd='"+pk_rkd+"' and isnull(dr,0)=0 ");
            int mount = -1; //mountΪ���ݿ��б������ݵĸ���
            try {
                ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                if(arr!=null && arr.size()>0){
                    for(int i=0;i<arr.size();i++){
                        HashMap hm2 = (HashMap) arr.get(i);
                        mount = new Integer(hm2.get("mount")==null?"0":hm2.get("mount").toString());
                    }
                }                
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(mount-rows!=0){
                JOptionPane.showMessageDialog(null,"��ⵥ�������ݱ���ȫѡ,�����Ե�ѡ�����ʵ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            ScCprkdBVO[] bvo = getBomYl(pk_rkd);
            if(bvo==null){
            	JOptionPane.showMessageDialog(null,"û�������ڲֿ�����!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            retBillVo.setChildrenVO(bvo);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
    
    /**
     * ������ⵥ��������BOM�ֽ����¼��Ա��زֿ��ԭ��
     * @author wb
     * 2008-6-12 15:28:50
     * @param pk_rkd
     * @return
     */
    @SuppressWarnings("unchecked")
	public ScCprkdBVO[] getBomYl(String pk_rkd){
    	ScCprkdBVO[] rkVOs = null;
    	String cuserid = ce.getUser().getPrimaryKey();               // ����Ա
    	StringBuffer sql = new StringBuffer()
    	.append(" select distinct d.pk_invbasdoc,d.pk_measdoc,d.ckamount,d.pk_fjbom")
    	.append(" from eh_stordoc a, eh_stordoc_b b,bd_invmandoc c,eh_fjbom d")
        .append(" where a.pk_stordoc = b.pk_stordoc")
        .append(" and c.def1 = a.pk_bdstordoc")
        .append(" and c.pk_invmandoc = d.pk_invbasdoc")
        .append(" and (b.cuserid = '"+cuserid+"' or a.pk_psndoc='"+cuserid+"') and d.pk_rkd = '"+pk_rkd+"' and isnull(d.ck_flag,'N')='N'")
        .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 and isnull(d.dr,0)=0");
    	
    	String rkdSQL = "select pk_invbasdoc from eh_sc_cprkd_b where pk_rkd = '"+pk_rkd+"' and isnull(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	
    	String pk_invbasdoc = null;
    	String pk_measdoc = null;
    	UFDouble ckamount = null;
    	String pk_fjbom = null;
    	String pk_cpinvbasdoc = null;
    	try {
    		ArrayList<HashMap> arrRkB = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(rkdSQL,new MapListProcessor());
    		if(arrRkB!=null&&arrRkB.size()>0){
    			String[] pk_cpinvbasdocs = new String[arrRkB.size()];
    			for(int i=0; i<arrRkB.size(); i++){
    				HashMap hm = arrRkB.get(i);
    				pk_cpinvbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
    				pk_cpinvbasdocs[i] = pk_cpinvbasdoc;
    			}
    			new PubTools();
				pk_cpinvbasdoc = PubTools.combinArrayToString(pk_cpinvbasdocs);
    		}
    		ArrayList<HashMap> arr = (ArrayList<HashMap>)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
			if(arr!=null&&arr.size()>0){
			   rkVOs = new ScCprkdBVO[arr.size()];
			   for(int i=0; i<arr.size(); i++){
				  ScCprkdBVO rkVO = new ScCprkdBVO();
				  HashMap hm = arr.get(i);
			      pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
			      pk_measdoc = hm.get("pk_measdoc")==null?"":hm.get("pk_measdoc").toString();
			      ckamount = new UFDouble(hm.get("ckamount")==null?"0":hm.get("ckamount").toString());
			      pk_fjbom = hm.get("pk_fjbom")==null?"":hm.get("pk_fjbom").toString();
	              rkVO.setPk_invbasdoc(pk_invbasdoc);
	              rkVO.setPk_unit(pk_measdoc);
	              rkVO.setVsourcebillid(pk_fjbom);        // �ֽ�bom�� eh_fjbom ������
	              rkVO.setRkmount(ckamount);
	              rkVO.setMemo(pk_cpinvbasdoc);            // ��Ʒ��������
	              rkVOs[i] = rkVO;
			   }
			}
    	} catch (BusinessException e) {
			e.printStackTrace();
		}
		return rkVOs;
   }
    
    /***
     * �õ�δ����ĳ�Ʒ��ⵥ����
     * @return
     * @throws Exception
     * wb 2009-10-9 10:58:11
     */
    @SuppressWarnings("unchecked")
	public String getAddCondtion() throws Exception{
    	String pk_rkdadd = "''";
    	StringBuffer sql = new StringBuffer()
//    	.append("  select DISTINCT d.pk_rkd")
//    	.append(" from eh_fjbom d,eh_stordoc a, eh_stordoc_b b,bd_invmandoc c")
//		.append(" where a.pk_stordoc = b.pk_stordoc")
//		.append(" and c.def1 = a.pk_bdstordoc")
//		.append(" and c.pk_invmandoc = d.pk_invbasdoc")
//		.append(" and isnull(d.ck_flag,'N')='N' and c.pk_corp = '"+getPkCorp()+"'  and SUBSTRING(d.pk_rkd,1,4) = '"+getPkCorp()+"'")
//		.append(" and (b.cuserid = '"+ce.getUser().getPrimaryKey()+"' or a.pk_psndoc='"+ce.getUser().getPrimaryKey()+"') ")
//		.append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 and isnull(d.dr,0)=0 ");
    	.append("  select DISTINCT d.pk_rkd")
    	.append(" from eh_fjbom d")
		.append(" where  isnull(d.ck_flag,'N')='N'")
		.append(" and isnull(d.ck_flag,'N')='N' and isnull(d.dr,0)=0 and SUBSTRING(d.pk_rkd,1,4) = '"+getPkCorp()+"'");
    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    	if(arr!=null&&arr.size()>0){
    		String[] pk_rkds = new String[arr.size()];
    		for(int i=0;i<arr.size();i++){
    			HashMap hm = (HashMap)arr.get(i);
    			pk_rkds[i] = hm.get("pk_rkd")==null?"":hm.get("pk_rkd").toString();
    		}
    		pk_rkdadd = PubTools.combinArrayToString(pk_rkds);
    	}
    	return pk_rkdadd;
    }
}