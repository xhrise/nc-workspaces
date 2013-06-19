package nc.itf.yto.util;

import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.yto.business.CorpVO;
import nc.vo.yto.business.DeptdocVO;
import nc.vo.yto.business.JobdocVO;
import nc.vo.yto.business.OperationMsg;
import nc.vo.yto.business.PsnbasdocVO;
import nc.vo.yto.business.PsndocVO;

public interface IGener {
	public String generateXml(GeneralVO vo, String roottag, String billtype,
			String proc);

	public String generateXml2(CorpVO vo, String roottag, String billtype,
			String proc);

	public String generateXml3(DeptdocVO vo, String roottag, String billtype,
			String proc);

	public String generateXml4(PsndocVO vo, String roottag, String billtype,
			String proc, OperationMsg opmsg);

	public String generateXml5(JobdocVO vo, String roottag, String billtype,
			String proc);

	public String generateXml6(PsnbasdocVO vo, String roottag, String billtype,
			String proc);

	public void initReadmsg();
}
