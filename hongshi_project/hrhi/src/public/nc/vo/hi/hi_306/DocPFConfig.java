package nc.vo.hi.hi_306;

import nc.vo.hr.comp.pf.PFConfig;


public class DocPFConfig extends PFConfig{

	
	public static String BILLTYPE = "6101";
	
	 @Override
	    public String getApproveDateFieldCode()
	    {
	        return DocApplyHVO.APPROVEDATE;
	    }
	    
	    /***********************************************************************************************
	     * Created on 2007 三月 29 15:40:55<br>
	     * @see nc.vo.hr.comp.pf.PFConfig#getApproveNoteFieldCode()
	     **********************************************************************************************/
	    @Override
	    public String getApproveNoteFieldCode()
	    {
	    	return DocApplyHVO.APPROVENOTE;
	    }
	    
	    /***********************************************************************************************
	     * Created on 2007 三月 29 15:40:55<br>
	     * @see nc.vo.hr.comp.pf.PFConfig#getApproverFieldCode()
	     **********************************************************************************************/
	    @Override
	    public String getApproverFieldCode()
	    {
	        return DocApplyHVO.PK_APPROVER;
	    }
	    
	    /***********************************************************************************************
	     * Created on 2007 三月 29 15:40:55<br>
	     * @see nc.vo.hr.comp.pf.PFConfig#getApproveStateFieldCode()
	     **********************************************************************************************/
	    @Override
	    public String getApproveStateFieldCode()
	    {
	        return DocApplyHVO.BILLSTATE;
	    }
	    
	    /***********************************************************************************************
	     * Created on 2007 三月 29 15:40:55<br>
	     * @see nc.vo.hr.comp.pf.PFConfig#getBillSubmitDateFieldCode()
	     **********************************************************************************************/
	    @Override
	    public String getBillSubmitDateFieldCode()
	    {
	        return DocApplyHVO.APPLYDATE;
	    }
	    
	    /***********************************************************************************************
	     * Created on 2007 三月 29 15:40:55<br>
	     * @see nc.vo.hr.comp.pf.PFConfig#getBillSubmitterFieldCode()
	     **********************************************************************************************/
	    @Override
	    public String getBillSubmitterFieldCode()
	    {
	        return DocApplyHVO.PK_PROPOSER;
	    }
	    
	    /***********************************************************************************************
	     * Created on 2007 三月 29 15:40:55<br>
	     * @see nc.vo.hr.comp.pf.PFConfig#getBillWriterFieldCode()
	     **********************************************************************************************/
	    @Override
	    public String getBillWriterFieldCode()
	    {
	        return DocApplyHVO.PK_PROPOSER;
	    }
	    @Override
		public String getBillCodeFieldCode() {
			// TODO Auto-generated method stub
			return DocApplyHVO.VBILLNO;
		}

		@Override
		public String getBillTypeCode() {
			// TODO 自动生成方法存根
			return BILLTYPE;
		}

		@Override
		public String getBusinessTypeFieldCode() {
			// TODO Auto-generated method stub
			return "busitype";
		}
		
		public boolean isDirectQuery()
	    {
	        return true;
	    }
}
