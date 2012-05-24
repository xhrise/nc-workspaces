package nc.ui.eh.sc.h0451505;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpVO;
import nc.vo.eh.sc.h0451505.ScPgdBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��:�ɹ��������ν���(��MRP��������)
 * @author ��־Զ
 * 2009-11-13 11:09:31
 */
public class ZB32TOZA43DLG extends AbstractBTOBDLG {
    
	private static final long serialVersionUID = 1L;
	
	public static  ScPgdBVO[] pgdBVOs = null;
	public ZB32TOZA43DLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);

		BillItem billItem = getbillListPanel().getBillListData().getHeadItem(
				"vbillstatus");
		initComboBox(billItem, IBillStatus.strStateRemark, true);
	}

	public String[] getBillVos() {
		return new String[] {
				PubBillVO.class.getName(),
				ScMrpVO.class.getName(),
				ScMrpBVO.class.getName()  
				};
	}

	protected AbstractBTOBBillQueryDLG getQueryDlg() {
		if (queryCondition == null) {
			queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(),
					getOperator(), getFunNode(), getBusinessType(),
					getCurrentBillType(), getBillType(), null, "H0450705","MRP����");
			queryCondition.hideNormal();
		}
		return queryCondition;
	}

	public String getHeadCondition() {
		StringBuffer strWherePart = new StringBuffer();
		// ����ҵ�����ͣ���˾���룬����״̬��ѯ
		strWherePart.append(" pk_corp = '");
		strWherePart.append(getPkCorp());
		strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS +
				" and (isnull(sc_flag,'N')='N' or sc_flag=''  ) " +
                "and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') ");//���ӹر�״̬���
		String strwp = strWherePart.toString();
		return strwp;
	}
	@Override
	public String getBodyCondition() {
		m_whereStr=null;
		StringBuffer strWherePart = new StringBuffer().append("");
		strWherePart.append(" (isnull(sc_flag,'N')='N' or sc_flag='' ) ");
		String strwp = strWherePart.toString();
		return strwp;
	}

	/**
	 * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
	 * 
	 * @author ���� 2007-11-1 ����03:14:05
	 */
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(
			getbillListPanel().getHeadTable().getSelectedRow()));
			retBillVo = getbillListPanel().getSelectedVO(m_billVo,m_billHeadVo, m_billBodyVo);
			retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo,m_billHeadVo, m_billBodyVo);
			//�ж��Ƿ�ѡ��
			if (retBillVos.length <= 0) {
				JOptionPane.showMessageDialog(null, "������ѡ��һ������!", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			HashMap hm=new HashMap();
			ArrayList mrpArr = new ArrayList();		//���MRP�������������
            ArrayList list = new ArrayList();                                   // �˴��������ε����Ƕ�ҳǩ�������޷�ֱ��ֱ�Ӵ�������,��ͨ�� vo ���� ui �� 
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            String memo = null;
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                ScMrpVO mrp = (ScMrpVO)retBillVos[i].getParentVO();//��������VO
                String pk_mrp = mrp.getPk_mrp();
                mrpArr.add(pk_mrp);
                String mrpsql = "select billno from eh_sc_mrp where pk_mrp = '"+pk_mrp+"'";
                String billno = null;
                try {
                	Object billnoObj = iUAPQueryBS.executeQuery(mrpsql, new ColumnProcessor());
					billno = billnoObj==null?"":billnoObj.toString();
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				//���屸ע
				if(memo == null){
					memo = mrp.getMemo();
				}else{
					memo = memo +", "+ mrp.getMemo();
				}
				
                for (int j = 0; j < childs.length; j++) {
                	ScPgdBVO pgdBVO = new ScPgdBVO();
                    ScMrpBVO mrpBVO = (ScMrpBVO)childs[j];
                    String pk_mrp_b = mrpBVO.getPk_mrp_b()==null?"":mrpBVO.getPk_mrp_b();			//MRP�����ӱ�pk
                    pgdBVO.setVsourcebillrowid("'"+pk_mrp_b+"'");
                    pgdBVO.setVsourcebillid("'"+pk_mrp+"'");
                	pgdBVO.setPk_posm(billno);
                    //�ж������Ƿ���ͬ
                	String pk_invbasdoc= mrpBVO.getPk_invbasdoc()==null?"0":mrpBVO.getPk_invbasdoc().toString();
                	UFDouble newamount = new UFDouble(mrpBVO.getBcamount()==null?"0":mrpBVO.getBcamount().toString());
                	pgdBVO.setPk_invbasdoc(mrpBVO.getPk_invbasdoc());
                	pgdBVO.setVer(mrpBVO.getVer());//BOM�汾
                	pgdBVO.setScmount(mrpBVO.getBcamount());//��������
                	pgdBVO.setYpgamount(mrpBVO.getYzrwamount()==null?new UFDouble(0):mrpBVO.getYzrwamount());	//���ɹ�����
                	pgdBVO.setPgamount(mrpBVO.getBcamount().sub(pgdBVO.getYpgamount()));	//�����ɹ�����
                	pgdBVO.setMemo(mrpBVO.getMemo());
                	//add by houcq 2011-08-08 begin
                	UFDouble kcamount = new PubTools().getInvKcAmount(getPkCorp(),nc.ui.pub.ClientEnvironment.getInstance().getDate(),pk_invbasdoc);
                	//add by houcq 2011-08-08 end
                    if(hm.containsKey(pk_invbasdoc)){
                    	ScPgdBVO oldbvo=(ScPgdBVO) hm.get(pk_invbasdoc);
                    	UFDouble oldamount = new UFDouble(oldbvo.getScmount()==null?"0":oldbvo.getScmount().toString());
                    	UFDouble amount=oldamount.add(newamount);
                    	oldbvo.setScmount(amount);//��������
                    	//�����б�ע
                    	String bmemo = mrpBVO.getMemo() == null?"":mrpBVO.getMemo();
                    	String bodymemo = null;
                    	if(oldbvo.getMemo()==null){
                    		bodymemo = bmemo;
                    	}else{
                    		bodymemo = oldbvo.getMemo()+", "+ bmemo;
                    	}
                    	oldbvo.setMemo(bodymemo);
                    	UFDouble ypgamount = oldbvo.getYpgamount().add(mrpBVO.getYzrwamount()==null?new UFDouble(0):mrpBVO.getYzrwamount());		//���ɹ�����
                    	oldbvo.setYpgamount(ypgamount);									
                    	oldbvo.setPgamount(amount.sub(ypgamount));									//�����ɹ�����
                    	String pk_posm_bs = "'"+pk_mrp_b+"',"+oldbvo.getVsourcebillrowid();
                    	String pk_posms = "'"+pk_mrp+"',"+oldbvo.getVsourcebillid();
                    	String billnos = billno+","+oldbvo.getPk_posm();
                    	oldbvo.setVsourcebillid(pk_posms);
                 		oldbvo.setVsourcebillrowid(pk_posm_bs);
                 		oldbvo.setPk_posm(billnos);
                 		if(mrpBVO.getVer().intValue()>oldbvo.getVer().intValue()){			//������������ͬʱȡ���µ�BOM�汾
                 			oldbvo.setVer(mrpBVO.getVer());
                 		}
                 		oldbvo.setKcamount(kcamount);//add by houcq 2011-08-08
                    	hm.put(pk_invbasdoc, oldbvo);
                    }else{
                    	pgdBVO.setKcamount(kcamount);//add by houcq 2011-08-08
                    	hm.put(pk_invbasdoc, pgdBVO);
                    }
                }
            } 
           
            String[] pk_mrps = (String[])mrpArr.toArray(new String[mrpArr.size()]);
            String pk_mrpss = PubTools.combinArrayToString2(pk_mrps);
            
            if(pk_mrpss!=null&&pk_mrpss.length()>2500){
            	JOptionPane.showMessageDialog(null, "��ѡ�������񵥼�¼̫��,��������100����¼!", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
				return;
            }
            retBillVo.getParentVO().setAttributeValue("pk_mrp", pk_mrpss);
            retBillVo.getParentVO().setAttributeValue("memo", memo);
            
            Object[] keyset=hm.keySet().toArray();
            for(int i=0;i<keyset.length;i++){
            	list.add(hm.get(keyset[i].toString()));
            }
            pgdBVOs=new ScPgdBVO[list.size()] ;
            pgdBVOs = (ScPgdBVO[])list.toArray(pgdBVOs);
            
            SuperVO[] child = new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
			this.getAlignmentX();
			this.closeOK();

		}
	}

}