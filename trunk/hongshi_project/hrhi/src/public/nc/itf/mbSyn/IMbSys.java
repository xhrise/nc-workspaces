package nc.itf.mbSyn;

import nc.vo.hrsm.hrsm_301.StapplybHHeaderVO;

public interface IMbSys {
	public String create(String arg0, String arg1 , String arg2) throws Exception;

	public String createDept(String arg0) throws Exception;

	public String createOcuption(String arg0) throws Exception;

	public String delete(String time, String arg0) throws Exception;

	public void update(String pk_psndoc, String pk_aimcorp, Long id,
			String source, boolean arg4 , boolean resetPwd) throws Exception;

	public void update1(String def1, String pk_psndoc, String pk_aimcorp,
			Long id, String source, boolean arg4);
	
	public void update2(String pk_psndoc, String pk_aimcorp, Long id,
			String source, boolean arg4);
	
	public void enable(long id, boolean enabled);
}
