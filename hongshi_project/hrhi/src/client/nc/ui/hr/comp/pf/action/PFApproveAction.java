package nc.ui.hr.comp.pf.action;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.hr.utils.PubEnv;
import nc.hr.utils.ResHelper;
import nc.itf.hr.comp.IParValue;
import nc.itf.hr.pub.PubDelegator;
import nc.itf.oa.IBasDMO;
import nc.ui.hr.comp.pf.DirectApproveDialog;
import nc.ui.hr.comp.pf.PFDataModel;
import nc.ui.hr.comp.pf.PFMainPanel;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.frame.action.AbstractAction;
import nc.ui.hr.frame.impl.MainBillMgrPanel;
import nc.ui.hr.global.Global;
import nc.ui.hrtm.hrtm_302.TurnoverApproveUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.PfUtilClient;
import nc.vo.hr.comp.pf.PFConfig;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.hr.tools.pub.HRConstEnum;
import nc.vo.hrsm.hrsm_301.StapplybBVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.sm.cmenu.GlorgbookExtVO;
import nc.vo.trade.pub.IBillStatus;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/*************************************************************************************************************
 * ��������<br>
 * Created on 2006-11-25 11:14:01<br>
 * @author Rocex Wang
 ************************************************************************************************************/
public class PFApproveAction extends AbstractAction
{
    /** ֱ���Ƿ�Ҫ��ҵ������Ĭ����Ҫ��ҵ���� */
    private boolean blDirectApproveFlow = true;
    
    protected boolean blIsDirectApprove = false;// �ж��Ƿ���ֱ����������Ӧ����validate()�и�ֵ
    
    protected SuperVO bodyVOs[];
    
    protected PFDataModel dataModel = (PFDataModel) getDataModel();
    protected HashMap<String, String> hashPara = new HashMap<String, String>();// �ύʱ��Ĳ���
    
    protected SuperVO headVO;
    
    protected HRAggVO hrAggVO;
    
    private int iDirectApproveResult = -1;
    
    protected PFMainPanel mainPanel = (PFMainPanel) getMainPanel();
    
    protected PFConfig pfConfig = dataModel.getPFConfig();
    
    /*********************************************************************************************************
     * Created on 2006-11-25 11:13:57<br>
     * @author Rocex Wang
     * @param frameUI
     ********************************************************************************************************/
    public PFApproveAction(FrameUI frameUI)
    {
        super(frameUI);
    }
    
    /*********************************************************************************************************
     * Created on 2006-11-25 11:13:55<br>
     * @author Rocex Wang
     * @param frameUI
     * @param checkClassMethod
     ********************************************************************************************************/
    public PFApproveAction(FrameUI frameUI, String checkClassMethod)
    {
        super(frameUI, checkClassMethod);
    }
    
    /***************************************************************************
     * <br>
     * Created on 2009-6-24 9:42:32<br>
     * @author Rocex Wang
     * @param strBillType
     * @param strBusiType
     * @param strBillPk_corp
     * @return HashMap<String, String>
     * @throws BusinessException
     ***************************************************************************/
    public static HashMap<String, String> checkApproveType(String strBillPk_corp, String strBillType, String strBusiType)
            throws BusinessException
    {
        HashMap<String, String> hashPara = new HashMap<String, String>();
        
        if (strBillPk_corp == null)
        {
            strBillPk_corp = PubEnv.getPk_corp();
        }
        
        Integer intApproveType = NCLocator.getInstance().lookup(IParValue.class).getParaInt(strBillPk_corp, strBillType);
        
        int iApproveType = intApproveType == null ? 0 : intApproveType;
        
        if (iApproveType == HRConstEnum.APPROVE_TYPE_FORCE_DIRECT)
        {
            hashPara.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);
        }
        
        return hashPara;
    }
    
    /***************************************************************************
     * ��ѯ������ʽ֮ǰ���ã��������false��������������������ʽ<br>
     * Created on 2009-7-3 14:13:59<br>
     * @author Rocex Wang
     * @return boolean
     * @throws BusinessException
     ***************************************************************************/
    protected boolean beforeCheckApproveType() throws BusinessException
    {
        return true;
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-11-25 11:07:18<br>
     * @author Rocex Wang
     ********************************************************************************************************/
    protected void displayData()
    {
        try
        {
            hrAggVO = dataModel.queryBillByHeadPk(headVO.getPrimaryKey());
        }
        catch (BusinessException ex)
        {
            Logger.error(ex.getMessage(), ex);
        }
        
        MainBillMgrPanel mainMgrPanel = (MainBillMgrPanel) getMainPanel();
        
        mainMgrPanel.getMainBillListPanel().setSelectedData(hrAggVO);
        mainMgrPanel.getMainBillCardPanel().setData(hrAggVO);
        
        mainMgrPanel.afterSetData();
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2006-11-25 11:13:31<br>
     * @author Rocex Wang
     * @return boolean
     * @throws BusinessException
     ********************************************************************************************************/
    public boolean doDirectApprove() throws BusinessException
    {
    	try{
    		// add by river 
    	
	    	 IBasDMO basDMO = (IBasDMO) NCLocator.getInstance().lookup(
					IBasDMO.class.getName());
			String date = String.valueOf(ClientEnvironment.getServerTime()
					.getYear());
			GlorgbookExtVO extVO = (GlorgbookExtVO) ClientEnvironment
					.getInstance().getValue("pk_glorgbook");
			String pk_glorgbook = String.valueOf(extVO.m_pk_glorgbook);

			double benj = getDebitamountsum(basDMO, Global.getCorpPK(),
					((StapplybBVO) hrAggVO.getChildrenVO()[0]).getPk_psndoc(),
					pk_glorgbook, date, "122103");

			int yorn = 0;
			if (benj != 0)
				yorn = javax.swing.JOptionPane.showConfirmDialog(null,
						"����Ա����δ��������Ϊ��" + benj + " ��ȷ������ͨ����", "������ʾ",
						javax.swing.JOptionPane.YES_NO_OPTION);

			if (yorn == 1)
				return false;
    	} catch(Exception ex){
    		
    	}
    	
        DirectApproveDialog approveDlg = new DirectApproveDialog(getFrameUI());
        approveDlg.showModal();
        
        iDirectApproveResult = approveDlg.getResult();
        
        // û�е���������ťʱ��������
        if (DirectApproveDialog.PF_APPROVE_APPROVED != iDirectApproveResult
            && DirectApproveDialog.PF_APPROVE_REJECTED != iDirectApproveResult
            && DirectApproveDialog.PF_APPROVE_RETURN != iDirectApproveResult)
        {
            return false;
        }
        
        headVO.setAttributeValue(pfConfig.getApproveStateFieldCode(), approveDlg.getResult());
        headVO.setAttributeValue(pfConfig.getApproveNoteFieldCode(), approveDlg.getApproveNote());
        headVO.setAttributeValue(pfConfig.getApproverFieldCode(), PubEnv.getPk_user());
        headVO.setAttributeValue(pfConfig.getApproveDateFieldCode(), PubEnv.getServerDate());
        
        return doDirectApprove(hrAggVO, approveDlg.getApproveNote(), approveDlg.getResult());
    }
    
    /*********************************************************************************************************
     * <br>
     * Created on 2007 ���� 20 15:07:40<br>
     * @author Rocex Wang
     * @param hrAggVO2
     * @param strApproveNote
     * @param iApproveResult DirectApproveDialog.PF_APPROVE_APPROVED, DirectApproveDialog.PF_APPROVE_REJECTED,
     *            DirectApproveDialog.PF_APPROVE_RETURN
     * @return boolean
     * @throws BusinessException
     ********************************************************************************************************/
    public boolean doDirectApprove(HRAggVO hrAggVO2, String strApproveNote, int iApproveResult) throws BusinessException
    {
        WorkflownoteVO worknoteVO = new WorkflownoteVO();
        
        worknoteVO.setBillid(headVO.getPrimaryKey());// ����ID
        worknoteVO.setChecknote(strApproveNote);// �������
        worknoteVO.setDealdate(PubEnv.getServerTime());// ��������
        
        // ��˾
        Object objPk_corp = headVO.getAttributeValue(pfConfig.getCorpFieldCode());
        worknoteVO.setPk_corp(objPk_corp == null ? PubEnv.getPk_corp() : objPk_corp.toString());
        
        // ���ݱ��
        worknoteVO.setBillno(ObjectUtils.toString(headVO.getAttributeValue(pfConfig.getBillCodeFieldCode())));
        
        // ������
        Object objSender = headVO.getAttributeValue(pfConfig.getBillSubmitterFieldCode());
        worknoteVO.setSenderman(objSender == null ? PubEnv.getPk_user() : objSender.toString());
        
        // ��������
        Object objSubmitDate = headVO.getAttributeValue(pfConfig.getBillSubmitDateFieldCode());
        worknoteVO.setSenddate(objSubmitDate == null ? PubEnv.getServerTime() : new UFDateTime(objSubmitDate.toString()));
        
        // Y,����ͨ����N��������ͨ��
        worknoteVO.setApproveresult(DirectApproveDialog.PF_APPROVE_APPROVED == iApproveResult ? "Y" : "N");
        worknoteVO.setApprovestatus(1);// ֱ����״̬
        worknoteVO.setIscheck(DirectApproveDialog.PF_APPROVE_APPROVED == iApproveResult ? "Y" : "N");
        worknoteVO.setActiontype("APPROVE");
        worknoteVO.setMoney(new UFDouble(0, 2));
        worknoteVO.setLocalMoney(new UFDouble(0, 2));
        worknoteVO.setCheckman(PubEnv.getPk_user());
        
        // ��������
        worknoteVO.setPk_billtype(dataModel.getBillTypeCode());
        
        if (StringUtils.isNotBlank(pfConfig.getBusinessTypeFieldCode()))
        {
            worknoteVO.setPk_businesstype(ObjectUtils.toString(headVO.getAttributeValue(pfConfig.getBusinessTypeFieldCode())));
        }
        
        PubDelegator.getIPersistenceUpdate().insertVO(null, worknoteVO, null);
        
        return true;
    }
    
    public double getDebitamountsum(IBasDMO basDMO, String pk_corp,
			String pk_psn, String pk_glorgbook, String date, String subjcode) {
		double debitamountsum = 0;
		String sql = "select sum(sum(gl_detail.debitamount)) debitamountsum from gl_detail gl_detail,gl_fixtmpfreevalue gl_ftpfreevalue , gl_freevalue free where  gl_detail.pk_accsubj=(select pk_accsubj from bd_accsubj where subjcode='"
				+ subjcode
				+ "' and pk_glorgbook='"
				+ pk_glorgbook
				+ "')  and gl_detail.yearv='"
				+ date
				+ "'  and gl_detail.discardflagv = 'N' and gl_detail.dr = 0 and gl_detail.voucherkindv <> 255 and gl_detail.pk_managerv = 'N/A' and gl_detail.assid = gl_ftpfreevalue.assid "
				+ " and gl_ftpfreevalue.assid = free.freevalueid and gl_ftpfreevalue.def4 = '"+pk_psn+"' and free.valuecode = gl_ftpfreevalue.code4 "
				+ " group by  gl_detail.pk_accsubj,gl_detail.pk_glorgbook,gl_detail.yearv,gl_detail.assid order by  gl_detail.pk_accsubj,gl_detail.pk_glorgbook,gl_detail.yearv,gl_detail.assid ";
		try {
			String num = basDMO.getStr(sql, "nc56true");
			debitamountsum = Double.parseDouble(num == null ? "0" : num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return debitamountsum;
	}
    
    /*********************************************************************************************************
     * Created on 2006-11-22 13:00:00<br>
     * @author Rocex Wang
     * @see nc.ui.hr.frame.action.ICommand#execute()
     ********************************************************************************************************/
    public void execute() throws Exception
    {
        try
        {
            lockData(hrAggVO);
            
            boolean blSuccess = true;
            
            if (blIsDirectApprove)
            {
                blSuccess = doDirectApprove();
                
                // ���������ͨ��������Ҫ��ҵ�����ģ�����ҵ����
                if (DirectApproveDialog.PF_APPROVE_APPROVED == iDirectApproveResult && blDirectApproveFlow && blSuccess)
                {
                    PfUtilClient.runAction(getFrameUI(), IPFActionName.APPROVE + PubEnv.getPk_user(), dataModel.getBillTypeCode(), PubEnv
                        .getServerDate().toString(), dataModel.createPFAggVO(hrAggVO), null, null, null, hashPara);
                    
                    blSuccess = PfUtilClient.isSuccess();
                }
            }
            else
            {
                PfUtilClient.runAction(getFrameUI(), IPFActionName.APPROVE + PubEnv.getPk_user(), dataModel.getBillTypeCode(), PubEnv
                    .getServerDate().toString(), dataModel.createPFAggVO(hrAggVO), null, null, null, hashPara);
                
                blSuccess = PfUtilClient.isSuccess();
            }
            
            if (!blSuccess)
            {
                return;
            }
            
            displayData();
        }
        finally
        {
            unlockData(hrAggVO);
        }
        
        getFrameUI().showHintMessage(ResHelper.getString("nc_hr_pf", "UPPnc_hr_pf_000002"));// ���������������
    }
    
    /*********************************************************************************************************
     * Created on 2007-6-12 10:51:29<br>
     * @author Rocex Wang
     * @param directApproveFlow ֱ���Ƿ�Ҫ��ҵ������Ĭ����Ҫ��ҵ����
     ********************************************************************************************************/
    public void setDirectApproveFlow(boolean directApproveFlow)
    {
        blDirectApproveFlow = directApproveFlow;
    }
    
    /*********************************************************************************************************
     * Created on 2006-11-25 11:14:11<br>
     * @author Rocex Wang
     * @see nc.ui.hr.frame.action.AbstractAction#validate()
     ********************************************************************************************************/
    @Override
    public boolean validate() throws ValidationException
    {
        hrAggVO = getSelection(mainPanel);
        
        if (hrAggVO == null || (headVO = (SuperVO) hrAggVO.getParentVO()) == null)
        {
            throw new ValidationException(getResource("nc_hr_pf", "UPPnc_hr_pf_000000"));// ����ѡ�����ݣ�
        }
        
        bodyVOs = (SuperVO[]) hrAggVO.getChildrenVO();
        
        try
        {
            headVO = PubDelegator.getIPersistenceRetrieve().retrieveByPk(null, headVO.getClass(), headVO.getPrimaryKey());
        }
        catch (BusinessException ex)
        {
            Logger.error(ex.getMessage(), ex);
        }
        
        if (headVO == null)
        {
            throw new ValidationException(getResource("nc_hr_pf", "UPPnc_hr_pf_000042"));// �����Ѿ����޸ģ�����ˢ���ٲ�����
        }
        
        Integer intApproveState = (Integer) headVO.getAttributeValue(pfConfig.getApproveStateFieldCode());
        
        // ���������ͨ��������δͨ����״̬���Ͳ�������
        if (intApproveState != null && IBillStatus.COMMIT != intApproveState && IBillStatus.CHECKGOING != intApproveState)
        {
            throw new ValidationException(getResource("nc_hr_pf", "UPPnc_hr_pf_000003"));// ��������δ��ʼ�����Ѿ����������ܽ���������
        }
        
        blIsDirectApprove = dataModel.isDirectApprove(headVO.getPrimaryKey());
        
        if (!blIsDirectApprove && !dataModel.isCheckman(headVO.getPrimaryKey(), dataModel.getBillTempletCode()))
        {
            throw new ValidationException(getResource("nc_hr_pf", "UPPnc_hr_pf_000029")); // �����ǵ��ݵĵ�ǰ�����ˣ�
        }
        
        hrAggVO.setParentVO(headVO);
        
        String strBusiType = null;
        String strBillPk_corp = null;
        
        if (pfConfig.getBusinessTypeFieldCode() != null)
        {
            strBusiType = (String) headVO.getAttributeValue(pfConfig.getBusinessTypeFieldCode());
            strBillPk_corp = (String) headVO.getAttributeValue(pfConfig.getCorpFieldCode());
        }
        
        try
        {
            if (beforeCheckApproveType())
            {
                hashPara = checkApproveType(strBillPk_corp, dataModel.getBillTypeCode(), strBusiType);
            }
        }
        catch (BusinessException ex)
        {
            throw new ValidationException(ex.getMessage());
        }
        
        return true;
    }
}
