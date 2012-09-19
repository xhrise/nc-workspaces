package nc.bs.ic.pub.pfconv;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;

/**
 * ������
 * ���ܣ�������ⵥ���������ⵥ��BS�˺��������ࡣ
   Ŀǰ��ʱ���á�
 * ���ڣ�(2004-8-20 14:52:11)
 */
public class ChgAft4Y24E implements nc.vo.pf.change.IchangeVO {
	
	protected final IUAPQueryBS iUAPQueryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	/**
	 * ChgAft4Y24E ������ע�⡣
	 */
	public ChgAft4Y24E() {
		super();
	}
	/*
	 	���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ�����㣬
	*/
	public nc.vo.pub.AggregatedValueObject retChangeBusiVO(nc.vo.pub.AggregatedValueObject preVo, nc.vo.pub.AggregatedValueObject nowVo) throws BusinessException {
		nc.vo.scm.pub.SCMEnv.out(" enter ChgAft4Y24E");
		nc.vo.pub.AggregatedValueObject[] voRet = null;
	
		voRet =
			retChangeBusiVOs(
				new nc.vo.ic.pub.bill.GeneralBillVO[] { (nc.vo.ic.pub.bill.GeneralBillVO)preVo },
				new nc.vo.ic.pub.bill.GeneralBillVO[] {
					(nc.vo.ic.pub.bill.GeneralBillVO) nowVo });
	
		if (voRet != null && voRet.length > 0) {
			
			CircularlyAccessibleValueObject[]  retBodyVOs = voRet[0].getChildrenVO();
			for(CircularlyAccessibleValueObject bodyVO : retBodyVOs) {
				Object cprojectid = bodyVO.getAttributeValue("cprojectid");
				if(cprojectid != null) {
					Object jobname = iUAPQueryBS.executeQuery("select jobname from bd_jobbasfil where pk_jobbasfil in (select pk_jobbasfil from bd_jobmngfil where pk_jobmngfil = '"+cprojectid+"')", new ColumnProcessor());
					if(jobname != null) {
						
						UFDate billdate = (UFDate) voRet[0].getParentVO().getAttributeValue("dbilldate");
						jobname = jobname + " - " + change(billdate.toString().split("-") , false);
						
						// ��ȡ��ˮ��
						Object vbatchcode = iUAPQueryBS.executeQuery("select max(vbatchcode) from scm_batchcode where vbatchcode like '"+jobname+"%'", new ColumnProcessor());
						if(vbatchcode == null)
							jobname = jobname + " - 01";
						else {
							String[] codeSplit = vbatchcode.toString().split(" - ");
							if(codeSplit.length < 3)
								jobname = jobname + " - 01";
							else {
								Integer no = Integer.valueOf(codeSplit[2]);
								jobname = jobname + " - " + (no + 1 < 10 ? "0" + (no + 1) : (no + 1));
							}
						}
							
						bodyVO.setAttributeValue("vbatchcode", jobname);
						
					}
				}
				
				bodyVO.setStatus(VOStatus.NEW);
			}
			
			voRet[0].getParentVO().setStatus(VOStatus.NEW);
			
			return voRet[0];
		} else {
			return nowVo;
			
		}
	}
	/*
	 	���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ���������ת��
	 	����ת��
	*/
	public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(nc.vo.pub.AggregatedValueObject[] preVo, nc.vo.pub.AggregatedValueObject[] nowVo) throws BusinessException {
		if(nowVo==null||nowVo.length==0)
			return nowVo;
	
		try{
	
	//		�޸��ˣ������� �޸����ڣ�2007-9-11����07:34:07 �޸�ԭ���ڵ�����������������VO�����󣬰����ε�������Ϣ���룬�Ա�֤���ε������ᱻ�޸ģ������ݿ������ݱ���һ��
			for (int i=0;i<nowVo.length;i++)
				nc.bs.ic.pub.ICCommonBusiImpl.execFormulaForBatchCode(preVo[i].getChildrenVO(),nowVo[i].getChildrenVO());
	
			nc.bs.ic.ic218.GeneralHBO bo = new nc.bs.ic.ic218.GeneralHBO();
	
			nowVo = bo.getInFromOut((nc.vo.ic.pub.bill.GeneralBillVO[])preVo,(nc.vo.ic.pub.bill.GeneralBillVO[])nowVo);
	
		
			
		}catch(Exception e){
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("4008other","UPP4008other-000085")/*@res "���ݽ���û����ɣ�"*/+e.getMessage());
		}
		return nowVo;
	}
	
	public final String change(String[] strArray , Boolean type)  {
		
		String newStr = "";
		for(String str : strArray) {
			if(type)
				newStr += str + ",";
			else 
				newStr += str;
		}
		
		if(type)
			if(newStr.length() > 0) 
				newStr = newStr.substring(0 , newStr.length() - 1);
		
		return newStr;
	}
}