package nc.imp.hskaoqin.all;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.itf.hskaoqin.all.IKaoQin;
import nc.vo.hskaoqin.all.ImpDataVO;

public class KaoQin implements IKaoQin {

	private final String ORACLEDATASOURCE = "nc56true";

	public String impkaoqindata(String starTime, String endTime , String sourceName) {
		String result = "";
		int oknum = 0;//成功条数
		int allnum=0;//所有条数
		boolean isok=false;
		String jieguo="导入停止，请补填NC系统中人员考勤卡号，";
		String jieguo2="";
		ArrayList<String> allpsn=new ArrayList<String>();
		try {
			BasDMO dmo=new BasDMO();
			//IBasDMO dmo = (IBasDMO) NCLocator.getInstance().lookup(IBasDMO.class.getName());
			Vector<ImpDataVO> datavos = new Vector<ImpDataVO>();
			datavos = dmo.getGetDataVOs(starTime, endTime, sourceName);
			allnum=datavos.size();
			if (allnum>0) {
				//在NC考勤中查询当前日期内是否有数据，有就删除后再插入，没有直接插入
				String selectsql="select count(pk_importdata) num from tbm_importdata where calendartime>='20"+starTime+" 00:00:00' and calendartime<='20"+endTime+" 23:59:59' and pk_corp = (select pk_corp from bd_corp where unitcode = '"+sourceName+"') ";
				int numaI=Integer.parseInt(dmo.getStr(selectsql, ORACLEDATASOURCE));
				
				if (numaI>0) {
					String deletesql="delete from tbm_importdata where calendartime>='20"+starTime+" 00:00:00' and calendartime<='20"+endTime+" 23:59:59' and pk_corp = (select pk_corp from bd_corp where unitcode = '"+sourceName+"')";
					if (dmo.executeSql(deletesql, ORACLEDATASOURCE)==true) {
						for (int i = 0; i < allnum; i++) {
							String pk_psndoc = "";
							String pk_corp = "";
							
							String cardid=datavos.get(i).getTimecardid().toString();
							
							String pkpsndocsql="select pk_psndoc from bd_psndoc where timecardid='"+cardid+"'";
							pk_psndoc = dmo.getStr(pkpsndocsql, ORACLEDATASOURCE);
							
							pkpsndocsql="select pk_corp from bd_psndoc where timecardid='"+cardid+"'";
							pk_corp = dmo.getStr(pkpsndocsql, ORACLEDATASOURCE);
							
							String getpsnnamesql="select usName from dbo.UserBasicInfo where usCardNo='"+cardid+"'";
							getpsnnamesql=dmo.getStr(getpsnnamesql, sourceName);
							if ("".equals(pk_psndoc)) {
								if (!allpsn.contains(cardid)) {
									if ("".equals(getpsnnamesql)) {
										getpsnnamesql="            ";
									}
									if (getpsnnamesql.length()==2) {
										getpsnnamesql+="    ";
									}
									jieguo2+=""+getpsnnamesql+"          "+cardid+"\n";
									allpsn.add(cardid);
								}
							}else{
							String sql = "insert into tbm_importdata(pk_importdata,pk_corp,timecardid,calendartime,datastatus,ts,dr,pk_psndoc) values(generatepk('"+pk_corp+"'),'"+pk_corp+"','"
									+ datavos.get(i).getTimecardid()
									+ "','"
									+ datavos.get(i).getCalendartime()
									+ "','"
									+ datavos.get(i).getDatastatus()
									+ "',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),'0','"+pk_psndoc+"')";
							isok=dmo.executeSql(sql, ORACLEDATASOURCE);
							if (isok==true) {
								oknum++;
							}}
						}
					}
				}else {
					for (int i = 0; i < allnum; i++) {
						String pk_psndoc = "";
						String pk_corp = "";
						
						String cardid=datavos.get(i).getTimecardid().toString();
						
						String pkpsndocsql="select pk_psndoc from bd_psndoc where timecardid='"+cardid+"'";
						pk_psndoc=dmo.getStr(pkpsndocsql, ORACLEDATASOURCE);
						
						pkpsndocsql="select pk_corp from bd_psndoc where timecardid='"+cardid+"'";
						pk_corp = dmo.getStr(pkpsndocsql, ORACLEDATASOURCE);
						
						String getpsnnamesql="select usName from dbo.UserBasicInfo where usCardNo='"+cardid+"'";
						getpsnnamesql=dmo.getStr(getpsnnamesql, sourceName);
						if ("".equals(pk_psndoc)) {
							if (!allpsn.contains(cardid)) {
								if ("".equals(getpsnnamesql)) {
									getpsnnamesql="            ";
								}
								if (getpsnnamesql.length()==2) {
									getpsnnamesql+="    ";
								}
								jieguo2+=""+getpsnnamesql+"          "+cardid+"\n";
								allpsn.add(cardid);
							}
						}else {
						String sql = "insert into tbm_importdata(pk_importdata,pk_corp,timecardid,calendartime,datastatus,ts,dr,pk_psndoc) values(generatepk('"+pk_corp+"'),'"+pk_corp+"','"
								+ datavos.get(i).getTimecardid()
								+ "','"
								+ datavos.get(i).getCalendartime()
								+ "','"
								+ datavos.get(i).getDatastatus()
								+ "',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),'0','"+pk_psndoc+"')";
						isok=dmo.executeSql(sql, ORACLEDATASOURCE);
						if (isok==true) {
							oknum++;
						}}
					}
				}
				if (!"".equals(jieguo2)) {
					result="本次导入数据共"+allnum+"条，成功"+oknum+"条，失败"+(allnum-oknum)+"条\n"+jieguo+"共"+allpsn.size()+"人！\n人员              考勤卡号\n"+jieguo2;
				}else {
					result="本次导入数据共"+allnum+"条，成功"+oknum+"条，失败"+(allnum-oknum)+"条";
				}
			}else {
				result="无数据可导入！";
			}
			
		} catch (Exception e) {
			result="异常信息："+e.getMessage();
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getUnits(String dataSource) throws Exception {
		String sql = "select unitcode|| ' ' ||unitname unit from bd_corp order by unitcode";
		BasDMO bas = new BasDMO();
		return bas.getStrs(sql, dataSource);
	}

}
