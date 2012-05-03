package nc.impl.mbSyn;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.mbSyn.IQueryList;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;

import com.seeyon.client.PersonServiceStub.PersonInfoParam_All;

public class QueryList implements IQueryList{
	private BasDMO dmo = null;
	private StringBuffer sqlbuf = null;
	private String sql = "";
	public List<PersonInfoParam_All> getPerson(String datasource , String pk_psndoc) throws Exception{
		sqlbuf = new StringBuffer();
		sqlbuf.append(" select psn.psnname ,psn.psncode , (case when psndoc.sex = '男' then '1' when psndoc.sex = '女' then '2' else '0' end) as sex , ");
		sqlbuf.append(" psndoc.birthdate , psndoc.mobile , psndoc.email ,psn.def1 , " );
		sqlbuf.append(" job.jobname , dept.deptname , dept.deptcode , dept.deptlevel , chg.poststat");
		sqlbuf.append(" , corp.unitname , corp.unitshortname from hi_psndoc_deptchg chg ");
		sqlbuf.append(" left join bd_deptdoc dept on chg.pk_deptdoc = dept.pk_deptdoc");
		sqlbuf.append(" left join bd_psndoc psn on psn.pk_psndoc = chg.pk_psndoc ");
		sqlbuf.append(" left join om_job job on chg.pk_postdoc = job.pk_om_job");
		sqlbuf.append(" left join bd_psnbasdoc psndoc on psndoc.pk_psnbasdoc = psn.pk_psnbasdoc");
		sqlbuf.append(" left join bd_corp corp on corp.pk_corp = chg.pk_corp ");
		sqlbuf.append(" where chg.enddate is null and psn.poststat <> 'N' ");
		sqlbuf.append(" and psn.psnname is not null ");
		sqlbuf.append(" and psn.def1 is not null and psn.pk_psndoc = '"+pk_psndoc+"' " );
		
		sql = sqlbuf.toString();
		
		dmo = new BasDMO();
		
		List<PersonInfoParam_All> personList = dmo.getPerson(sql, datasource);
		
//		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
//		for(PersonInfoParam_All psn : personList) {
//			Object unitname = iUAPQueryBS.executeQuery("select unitname from bd_corp where pk_corp = (select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')", new ColumnProcessor());
//			Object deptname = iUAPQueryBS.executeQuery("select deptname from bd_deptdoc where pk_deptdoc = (select pk_deptdoc from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')" , new ColumnProcessor());
//			Object pk_corp = iUAPQueryBS.executeQuery("select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'", new ColumnProcessor());
//			
//			String deptnames = deptname.toString();
//			
//			String sql = "";
//			boolean check = true;
//			while(check) {
//				sql = "select deptname from bd_deptdoc where pk_deptdoc = (select pk_fathedept from bd_deptdoc where pk_corp = '"+pk_corp+"' and deptname = '"+deptname+"')";
//				Object dept = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
//				if(dept != null) {
//					deptname = dept.toString();
//					deptnames = dept + "/" + deptnames;
//				} else 
//					check = false;
//			}
//			
//			deptnames = unitname + "/" + deptnames;
//			
//			psn.setDepartmentName(new String[]{deptnames , deptnames});
//			
//		}
		
//		List<PersonInfoParam_All> personList1 = new ArrayList<PersonInfoParam_All>(); ;
//		try{
//			personList1 = this.getPerson_AddSup(personList,datasource);
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return personList1;
//		}
		
		personList = this.getPerson_SecOcup(personList, datasource);
		
		return personList;
	}
	
	public List<PersonInfoParam_All> getPerson1(String arg0 , String pk_psndoc) throws Exception{
		sqlbuf = new StringBuffer();
		sqlbuf.append(" select psn.psnname ,psn.psncode , (case when psndoc.sex = '男' then '1' when psndoc.sex = '女' then '2' else '0' end) as sex , ");
		sqlbuf.append(" psndoc.birthdate , psndoc.mobile , psndoc.email ,psn.def1 , " );
		sqlbuf.append(" job.jobname , dept.deptname , dept.deptcode , dept.deptlevel , chg.poststat");
		sqlbuf.append(" , corp.unitname , corp.unitshortname from hi_psndoc_deptchg chg ");
		sqlbuf.append(" left join bd_deptdoc dept on chg.pk_deptdoc = dept.pk_deptdoc");
		sqlbuf.append(" left join bd_psndoc psn on psn.pk_psndoc = chg.pk_psndoc ");
		sqlbuf.append(" left join om_job job on chg.pk_postdoc = job.pk_om_job");
		sqlbuf.append(" left join bd_psnbasdoc psndoc on psndoc.pk_psnbasdoc = psn.pk_psnbasdoc");
		sqlbuf.append(" left join bd_corp corp on corp.pk_corp = chg.pk_corp ");
		sqlbuf.append(" where "); // chg.enddate is null and chg.poststat <> 'N' and
		sqlbuf.append(" psn.psnname is not null ");
		sqlbuf.append(" and psn.def1 is not null ");
		sqlbuf.append(" and psn.pk_psndoc = '"+pk_psndoc+"' " );
		
		sql = sqlbuf.toString();
		
		dmo = new BasDMO();
		
		List<PersonInfoParam_All> personList = dmo.getPerson(sql, arg0);
		
//		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
//		for(PersonInfoParam_All psn : personList) {
//			Object unitname = iUAPQueryBS.executeQuery("select unitname from bd_corp where pk_corp = (select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')", new ColumnProcessor());
//			Object deptname = iUAPQueryBS.executeQuery("select deptname from bd_deptdoc where pk_deptdoc = (select pk_deptdoc from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')" , new ColumnProcessor());
//			Object pk_corp = iUAPQueryBS.executeQuery("select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'", new ColumnProcessor());
//			
//			String deptnames = deptname.toString();
//			
//			String sql = "";
//			boolean check = true;
//			while(check) {
//				sql = "select deptname from bd_deptdoc where pk_deptdoc = (select pk_fathedept from bd_deptdoc where pk_corp = '"+pk_corp+"' and deptname = '"+deptname+"')";
//				Object dept = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
//				if(dept != null) {
//					deptname = dept.toString();
//					deptnames = dept + "/" + deptnames;
//				} else 
//					check = false;
//			}
//			
//			deptnames = unitname + "/" + deptnames;
//			
//			psn.setDepartmentName(new String[]{deptnames , deptnames});
//			
//		}
		
		List<PersonInfoParam_All> personList1 = new ArrayList<PersonInfoParam_All>(); ;
		try{
			personList1 = this.getPerson_AddSup(personList,arg0);
		}catch(Exception ex){
			ex.printStackTrace();
			return personList1;
		}
		return personList;
	}
	
	public List<PersonInfoParam_All> getPerson2(String arg0 , String pk_psndoc) throws Exception{
		sqlbuf = new StringBuffer();
		sqlbuf.append(" select psn.psnname ,psn.psncode , (case when psndoc.sex = '男' then '1' when psndoc.sex = '女' then '2' else '0' end) as sex , ");
		sqlbuf.append(" psndoc.birthdate , psndoc.mobile , psndoc.email ,psn.def1 , " );
		sqlbuf.append(" job.jobname , dept.deptname , dept.deptcode , dept.deptlevel ");
		sqlbuf.append(" , corp.unitname , corp.unitshortname from hi_stapplyb_h chg ");
		sqlbuf.append(" left join bd_deptdoc dept on chg.pk_aimdeptdoc = dept.pk_deptdoc ");
		sqlbuf.append(" left join bd_psndoc psn on psn.pk_psndoc = chg.pk_psndoc ");
		sqlbuf.append(" left join om_job job on chg.pk_aimjob = job.pk_om_job ");
		sqlbuf.append(" left join bd_psnbasdoc psndoc on psndoc.pk_psnbasdoc = psn.pk_psnbasdoc ");
		sqlbuf.append(" left join bd_corp corp on corp.pk_corp = chg.pk_aimcorp ");
		sqlbuf.append(" where psn.psnname is not null and psn.def1 is not null and  psn.pk_psndoc = '"+pk_psndoc+"' ");
		
		sql = sqlbuf.toString();
		
		dmo = new BasDMO();
		
		List<PersonInfoParam_All> personList = dmo.getPerson(sql, arg0);
		
//		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
//		for(PersonInfoParam_All psn : personList) {
//			Object unitname = iUAPQueryBS.executeQuery("select unitname from bd_corp where pk_corp = (select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')", new ColumnProcessor());
//			Object deptname = iUAPQueryBS.executeQuery("select deptname from bd_deptdoc where pk_deptdoc = (select pk_deptdoc from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')" , new ColumnProcessor());
//			Object pk_corp = iUAPQueryBS.executeQuery("select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'", new ColumnProcessor());
//			
//			String deptnames = deptname.toString();
//			
//			String sql = "";
//			boolean check = true;
//			while(check) {
//				sql = "select deptname from bd_deptdoc where pk_deptdoc = (select pk_fathedept from bd_deptdoc where pk_corp = '"+pk_corp+"' and deptname = '"+deptname+"')";
//				Object dept = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
//				if(dept != null) {
//					deptname = dept.toString();
//					deptnames = dept + "/" + deptnames;
//				} else 
//					check = false;
//			}
//			
//			deptnames = unitname + "/" + deptnames;
//			
//			psn.setDepartmentName(new String[]{deptnames , deptnames});
//			
//		}
		
		List<PersonInfoParam_All> personList1 = new ArrayList<PersonInfoParam_All>(); ;
		try{
			personList1 = this.getPerson_AddSup(personList,arg0);
		}catch(Exception ex){
			ex.printStackTrace();
			return personList1;
		}
		return personList;
	}
	
	public List<PersonInfoParam_All> getPerson3(String arg0 , String pk_psndoc) throws Exception{
		sqlbuf = new StringBuffer();
		sqlbuf.append("select psn.psnname ,psn.psncode , (case when psndoc.sex = '男' then '1' when psndoc.sex = '女' then '2' else '0' end) as sex , ");
		sqlbuf.append(" psndoc.birthdate , psndoc.mobile , psndoc.email ,psn.def1 , " );
		sqlbuf.append("job.jobname , dept.deptname , dept.deptcode , dept.deptlevel , chg.poststat");
		sqlbuf.append(" , corp.unitname , corp.unitshortname from hi_psndoc_deptchg chg ");
		sqlbuf.append(" left join bd_deptdoc dept on chg.pk_deptdoc = dept.pk_deptdoc");
		sqlbuf.append(" left join bd_psndoc psn on psn.pk_psndoc = chg.pk_psndoc ");
		sqlbuf.append(" left join om_job job on chg.pk_postdoc = job.pk_om_job");
		sqlbuf.append(" left join bd_psnbasdoc psndoc on psndoc.pk_psnbasdoc = psn.pk_psnbasdoc");
		sqlbuf.append(" left join bd_corp corp on corp.pk_corp = chg.pk_corp ");
		sqlbuf.append(" where psn.poststat <> 'N' and "); // chg.enddate is null and 
		sqlbuf.append(" psn.pk_psndoc = '"+pk_psndoc+"' " );
		
		sql = sqlbuf.toString();
		
		dmo = new BasDMO();
		
		List<PersonInfoParam_All> personList = dmo.getPerson(sql, arg0);
		
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
		for(PersonInfoParam_All psn : personList) {
			Object unitname = iUAPQueryBS.executeQuery("select unitname from bd_corp where pk_corp = (select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')", new ColumnProcessor());
			Object deptname = iUAPQueryBS.executeQuery("select deptname from bd_deptdoc where pk_deptdoc = (select pk_deptdoc from bd_psndoc where pk_psndoc = '"+pk_psndoc+"')" , new ColumnProcessor());
			Object pk_corp = iUAPQueryBS.executeQuery("select pk_corp from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'", new ColumnProcessor());
			
			String deptnames = deptname.toString();
			
			String sql = "";
			boolean check = true;
			while(check) {
				sql = "select deptname from bd_deptdoc where pk_deptdoc = (select pk_fathedept from bd_deptdoc where pk_corp = '"+pk_corp+"' and deptname = '"+deptname+"')";
				Object dept = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				if(dept != null) {
					deptname = dept.toString();
					deptnames = dept + "/" + deptnames;
				} else 
					check = false;
			}
			
			deptnames = unitname + "/" + deptnames;
			
			psn.setDepartmentName(new String[]{deptnames , deptnames});
			
		}
		
//		List<PersonInfoParam_All> personList1 = new ArrayList<PersonInfoParam_All>(); ;
//		try{
//			personList1 = this.getPerson_AddSup(personList,arg0);
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return personList1;
//		}
		return personList;
	}
	
	public String getPersonPKbyOldPK(String pk_psndoc , String pk_aimcorp , String datasource) throws Exception {
		sql = "select distinct pk_psndoc from bd_psndoc where psnname = '"+this.getPersonNamebyOldPK(pk_psndoc, datasource)+"' and poststat = 'Y' and psnclscope = 0 and pk_corp = '"+pk_aimcorp+"'";
		if(dmo == null)
			dmo = new BasDMO();
		
		return dmo.getPersonName(sql, datasource);
		
	}
	
	public String getPersonNamebyOldPK(String pk_psndoc , String datasource) throws Exception {
		sql = "select distinct psnname from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'";
		if(dmo == null)
			dmo = new BasDMO();
		
		return dmo.getPersonName(sql, datasource);
	}
	
	public String[] getPersonDefVal(String pk_psndoc , String datasource) throws Exception{
		
		sql = "select def1 , def2 , def3 from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'";
		
		dmo = new BasDMO();

		try{
			return dmo.getPersonDefVal(sql, datasource);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public List<PersonInfoParam_All> getPerson_AddSup(List<PersonInfoParam_All> personList,String arg0) throws Exception{
		for(PersonInfoParam_All person : personList){
			if(person.getDeptcode().length() > 2){
				
				sql = "select distinct deptname ,deptcode from bd_deptdoc where deptcode = " +
						"substr('"+person.getDeptcode()+"' , 0 , " +
						"(select distinct (length(deptcode) - 2) " +
						"from bd_deptdoc where deptcode = '"+person.getDeptcode()+"')) and rownum = 1";
				
				dmo = new BasDMO();
				String deptname = dmo.getDeptName(sql, arg0);
				if(deptname != null){
					person.setDepartmentName(new String[]{deptname , person.getDepartmentName()[1]});
				} else {
					person.setDepartmentName(new String[]{person.getDepartmentName()[1] , person.getDepartmentName()[1]});
				}
			}
		}
		
		return personList;
	}
	
	public List<PersonInfoParam_All> getPerson_SecOcup(List<PersonInfoParam_All> personList,String datasource) throws Exception {
		for(PersonInfoParam_All person : personList) {
			StringBuffer sb = new StringBuffer();
			sb.append(" select chg.jobtype , psn.pk_corp , psn.psncode , ");
			sb.append(" psn.psnname , job.jobname , dept.deptname from ");
			sb.append(" hi_psndoc_deptchg chg left join bd_psndoc psn ");
			sb.append(" on psn.pk_psndoc = chg.pk_psndoc left join om_job ");
			sb.append(" job on job.pk_om_job = chg.pk_postdoc left join ");
			sb.append(" bd_deptdoc dept on dept.pk_deptdoc = chg.pk_deptdoc ");
			sb.append(" where jobtype <> 0 and psncode = '"+person.getStaffNumber()+"' ");
			sb.append(" and chg.enddate is null and chg.bendflag = 'N' ");
			sql = sb.toString();
			
			if(dmo == null)
				dmo = new BasDMO();
			
			List<String> strList = dmo.getSecondOcupationName(sql, datasource);
			String[] strArr = strList.toArray(new String[0]);
			
			person.setSecondOcupationName(strArr);
		}
		
		return personList;
	}
	
	public int updatePerson(String oaname , String psnname , String oaId,String arg0) throws Exception{
		sql = "update bd_psndoc set def3 = '"+oaId+"' , def4 = to_char(sysdate , 'yyyy-MM-dd') , def5 = '' where def1 = '"+oaname+"' and psnname = '"+psnname+"'";
		dmo = new BasDMO();
		if(dmo.updatePerson(sql, arg0))
			return 1;
		return 0;
	}
	
	public List<Long> getDeletePerson(String pk_psndoc,String arg0) throws Exception {
		
		
		sql = "select def3 from bd_psndoc where pk_psndoc = '"+pk_psndoc+"'";
		dmo = new BasDMO();
		return dmo.getDeletePerson(sql, arg0);
	}
	
	public List<Long> getPersonbypkpsn(String arg0 , String arg1) throws Exception {
//		sql = "select def3 from bd_psndoc where def1 is not null and pk_psndoc = '" + arg1 + "'";
		sql = " select distinct def3 from (select def3 from bd_psndoc where pk_psndoc = '" + arg1 + "' " +
				"union select def3 from bd_psndoc_turnover where pk_psndoc = '" + arg1 + "')";
		dmo = new BasDMO();
		return dmo.getDeletePerson(sql, arg0);
	}
	
	public int updateDelPerson(String oaId , String arg0) throws Exception{
		sql = "update bd_psndoc set def3 = '' , def4 = '' , def5 = to_char(sysdate , 'yyyy-MM-dd') where def3 = '"+oaId+"'";
		dmo = new BasDMO();
		if(dmo.updatePerson(sql, arg0))
			return 1;
		return 0;
	}
	
	public int updateDelPerson2(String oaId , String arg0) throws Exception{
		sql = "update bd_psndoc set def1 = '' , def2 = 'N' , def3 = '' where def1 =  '"+oaId+"'";
		dmo = new BasDMO();
		if(dmo.updatePerson(sql, arg0))
			return 1;
		return 0;
	}
	
	
	public int updateNewPerson(String oaId , String[] strs , String arg0) throws Exception{
		sql = "update bd_psndoc set def1 = '"+strs[0]+"' , def2 = '"+strs[1]+"' , def3 = '"+strs[2]+"' where pk_psnbasdoc = (select pk_psnbasdoc from bd_psndoc where pk_psndoc = '"+oaId+"') and psnclscope = 0";
		dmo = new BasDMO();
		if(dmo.updatePerson(sql, arg0))
			return 1;
		return 0;
	}
	
	
	
	
	public List<String[]> getDepts(String pk_corp , String arg0) throws Exception{
		sql = "select deptname , deptcode , pk_corp from bd_deptdoc where def20 is null and pk_corp = '" + pk_corp + "'";
		dmo = new BasDMO();
		List<String[]> deptNames = dmo.getDepts(sql, arg0);
		List<String[]> deptList = this.getDepts2(deptNames,arg0);
		return deptList;
	}
	
	public List<String[]> getDepts2(List<String[]> deptNames , String arg0) throws Exception{
		List<String[]> deptNames2 = new ArrayList<String[]>();
//		DepartmentVO department = new DepartmentVO();
		if(deptNames.size() > 0){
			for(String[] dept : deptNames){
				if(dept[2].length() > 2){
					sql = "select deptname ,deptcode from bd_deptdoc where deptcode = " +
							"substr('"+dept[2]+"' , 0 , " +
							"(select distinct (length(deptcode) - 2) " +
							"from bd_deptdoc where deptcode = '"+dept[2]+"')) and def20 is null and pk_corp = '" + dept[3] + "' and rownum = 1";
					
					dmo = new BasDMO();
					String deptname = dmo.getDeptName(sql, arg0);
					//if(deptname != null){
					deptNames2.add(new String[]{deptname , dept[1]});
					//}
				}else
					deptNames2.add(new String[]{dept[1] , dept[1]});
//				deptNames2.add(department);
			}
			
			//deptNames2 = this.setUnitName(deptNames2);
		}
		return deptNames2;
	}
	
//	public List<DepartmentVO> setUnitName(List<DepartmentVO> deptList) throws Exception{
//		sql = "select distinct pk_corp , unitname from bd_corp";
//		dmo = new BasDMO();
//		List<String[]> corps = dmo.getCorps(sql, arg0);
//		if(corps.size() != 0){
//			for(int i = 0 ; i < corps.size() ; i++){
//				deptList.get(i).setUnits(corps.get(i));
//			}
//		}
//		
//		return deptList;
//	}
	
	public List<String[]> getUnits(String arg0) throws Exception{
		sql = "select pk_corp , unitname from bd_corp";
		dmo = new BasDMO();
		List<String[]> corps = dmo.getCorps(sql, arg0);
		return corps;
	}
	
	public List<String[]> getUnitsbyPk(String arg0 , String pk_corp) throws Exception{
		sql = "select pk_corp , unitname from bd_corp where pk_corp = '"+pk_corp+"'";
		dmo = new BasDMO();
		List<String[]> corps = dmo.getCorps(sql, arg0);
		return corps;
	}
	
	public int updateDepartment(String pk_corp , String deptname , String arg0) throws Exception{
		sql = "update bd_deptdoc set def20 = 'Y' where deptname = '"+deptname+"' and pk_corp = '" + pk_corp + "'";
		dmo = new BasDMO();
		if(dmo.updatePerson(sql, arg0))
			return 1;
		return 0;
	}
	
	public List<String[]> getOcup(String pk_corp , String arg0) throws Exception{
		sql = "select jobcode , jobname , pk_corp from om_job where groupdef10 is null and pk_corp = '" + pk_corp + "'";
		dmo = new BasDMO();
		List<String[]> ocupNames = dmo.getOcups(sql, arg0);
		return ocupNames;
	}
	
	public int updateOcupation(String pk_corp , String jobname , String jobcode , String arg0) throws Exception{
		sql = "update om_job set groupdef10 = 'Y' where jobname = '"+jobname+"' and jobcode = '" + jobcode + "' and pk_corp = '" + pk_corp + "'";
		dmo = new BasDMO();
		if(dmo.updatePerson(sql, arg0))
			return 1;
		return 0;
	}
	
	public boolean addPsnTurnOver(String pk_psndoc , String datasource) throws Exception {
		sql = "insert into BD_PSNDOC_TURNOVER value select * from BD_PSNDOC where pk_psndoc = '"+pk_psndoc+"'";
		dmo = new BasDMO();
		return dmo.insertPerson(sql, datasource);
	}
	
//	public Map<String , Object> getV3xMemberById_x(long id , String datasource) throws Exception {
//		sql = "select id, name, code, org_department_id, org_level_id, org_account_id, org_post_id from v3x_org_member where id =  '"+id+"'";
//		
//		dmo = new BasDMO();
//		
//		return dmo.getV3xMemberById(sql, datasource);
//	}
//	
//	public long getOrgDeptId_x(long accId , String name , String datasource) throws Exception{
//		sql = "select id from v3x_org_department where name = '"+name+"' and org_account_id = '"+accId+"'";
//		dmo = new BasDMO();
//		return dmo.getLongId(sql, datasource);
//	}
//	
//	public long getOrgPostId_x(long accId , String name , String datasource) throws Exception{
//		sql = "select id from v3x_org_department where name = '"+name+"' and org_account_id = '"+accId+"'";
//		dmo = new BasDMO();
//		return dmo.getLongId(sql, datasource);
//	}
//	
//	public void modifyMemberById_x(long id , long deptId, long accId , long postId, String datasource) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		sb.append("update v3x_org_member set org_department_id = '"+deptId+"' ");
//		sb.append(" , org_account_id = '"+accId+"' , org_post_id = '"+postId+"' ");
//		sb.append(" where id = '"+id+"' ");
//		sql = sb.toString();
//		
//		dmo = new BasDMO();
//		
//		dmo.modifyMemberById_x(sql, datasource);
//	}
	
//	public String getDatasourse() {
//		String ncFileName = "Ufida/source";
//		String xmlfile = "Ufida/source/source.properties";
//
//		File myFilePath = new File(ncFileName);
//		if (!(myFilePath.exists())) {
//			myFilePath.mkdirs();
//		}
//		File xmlList = new File(xmlfile);
//
//		// 如果xmlList.xml不存在则创建
//		Boolean bl = xmlList.exists();
//		
//		try {
//			
//
//			if (!bl) {
//				String path = QueryList.class.getClass().getResource("/").getPath();
//				xmlList = new File("Ufida/source/source.properties");
//				InputStream is = new FileInputStream(xmlfile);
//				Properties prop = new Properties();
//				prop.load(is);
//				is.close();
//				if (prop.keySet().size() == 0) {
//					OutputStream fos = new FileOutputStream(xmlfile);
//					prop.setProperty("sources", "hssn");
//					prop.store(fos, " ");
//					fos.close();
//				}
//
//				return prop.getProperty("sources");
//			}else {
//				InputStream is = new FileInputStream(xmlfile);
//
//				Properties prop = new Properties();
//				prop.load(is);
//				is.close();
//				return prop.getProperty("sources");
//			}
//	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
	
//	public String getDatasourse() {
//		String ncFileName = "Ufida/datasource";
//		String xmlfile = "Ufida/datasource/datasource.xml";
//		org.w3c.dom.NodeList ip = null;
//		org.w3c.dom.Document document = null;
//
//		File myFilePath = new File(ncFileName);
//		if (!(myFilePath.exists())) {
//			myFilePath.mkdirs();
//		}
//		java.io.File xmlList = new java.io.File(xmlfile);
//
//		// 如果xmlList.xml不存在则创建
//		Boolean bl = xmlList.exists();
//		if (!bl) {
//
//			try {
//				document = XMLUtil.newDocument();
//			} catch (ParserConfigurationException e) {
//				e.printStackTrace();
//			}
//			org.w3c.dom.Element element_ip = document.createElement("source");
//			element_ip.setTextContent("hssn");
//			document.appendChild(element_ip);
//			this.toSave(document, xmlList.getPath());
//		}
//
//		try {
//			document = XMLUtil.getDocumentBuilder().parse(xmlList);
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		ip = document.getElementsByTagName("source");
//
//		return ip.item(0).getTextContent();
//	}
//	
//	public void toSave(org.w3c.dom.Document document, String filename) {
//		PrintWriter pw = null;
//		try {
//			TransformerFactory tf = TransformerFactory.newInstance();
//			Transformer transformer = tf.newTransformer();
//			DOMSource source = new DOMSource(document);
//			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			pw = new PrintWriter(new FileOutputStream(filename));
//			StreamResult result = new StreamResult(pw);
//			transformer.transform(source, result);
//		} catch (TransformerException mye) {
//			mye.printStackTrace();
//		} catch (IOException exp) {
//			exp.printStackTrace();
//		} finally {
//			pw.close();
//		}
//	}
}
