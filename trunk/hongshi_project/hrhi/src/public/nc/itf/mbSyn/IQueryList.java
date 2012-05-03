package nc.itf.mbSyn;

import java.util.List;
import java.util.Map;

import nc.vo.mbSyn.DepartmentVO;

import com.seeyon.client.PersonServiceStub.PersonInfoParam_All;

public interface IQueryList {
	public List<PersonInfoParam_All> getPerson(String arg0,String arg1) throws Exception;
	public int updatePerson(String oaname , String psnname , String oaId,String arg0) throws Exception;
	public List<String[]> getDepts(String pk_corp,String arg0) throws Exception;
	public List<String[]> getDepts2(List<String[]> deptNames,String arg0) throws Exception;
	// public List<DepartmentVO> setUnitName(List<DepartmentVO> deptList) throws
	// Exception;
	public List<String[]> getUnits(String arg0) throws Exception;
	public int updateDepartment(String pk_corp , String deptname,String arg0) throws Exception;
	public List<String[]> getOcup(String pk_corp,String arg0) throws Exception;
	public int updateOcupation(String pk_corp , String jobname , String jobcode,String arg0) throws Exception;
	public List<Long> getDeletePerson(String time,String arg0) throws Exception;
	public int updateDelPerson(String oaId,String arg0) throws Exception;
	public List<Long> getPersonbypkpsn(String arg0 , String arg1) throws Exception;
	public List<String[]> getUnitsbyPk(String arg0 , String pk_corp) throws Exception;
	public List<PersonInfoParam_All> getPerson1(String arg0 , String arg1) throws Exception;
	public boolean addPsnTurnOver(String pk_psndoc , String datasource) throws Exception;
	public List<PersonInfoParam_All> getPerson2(String arg0 , String arg1) throws Exception;
	public int updateDelPerson2(String oaId , String arg0) throws Exception;
	public String[] getPersonDefVal(String pk_psndoc , String datasource) throws Exception;
	public int updateNewPerson(String oaId , String[] strs , String arg0) throws Exception;
	public String getPersonPKbyOldPK(String pk_psndoc, String pk_aimcorp , String datasource) throws Exception;
	public String getPersonNamebyOldPK(String pk_psndoc , String datasource) throws Exception;
	public List<PersonInfoParam_All> getPerson3(String arg0 , String arg1) throws Exception;
//	public Map<String , Object> getV3xMemberById_x(long id , String datasource) throws Exception;
	
}
