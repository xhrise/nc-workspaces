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
		int oknum = 0;//�ɹ�����
		int allnum=0;//��������
		boolean isok=false;
		String jieguo="����ֹͣ���벹��NCϵͳ����Ա���ڿ��ţ�";
		String jieguo2="";
		ArrayList<String> allpsn=new ArrayList<String>();
		try {
			BasDMO dmo=new BasDMO();
			//IBasDMO dmo = (IBasDMO) NCLocator.getInstance().lookup(IBasDMO.class.getName());
			Vector<ImpDataVO> datavos = new Vector<ImpDataVO>();
			datavos = dmo.getGetDataVOs(starTime, endTime, sourceName);
			allnum=datavos.size();
			if (allnum>0) {
				//��NC�����в�ѯ��ǰ�������Ƿ������ݣ��о�ɾ�����ٲ��룬û��ֱ�Ӳ���
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
					result="���ε������ݹ�"+allnum+"�����ɹ�"+oknum+"����ʧ��"+(allnum-oknum)+"��\n"+jieguo+"��"+allpsn.size()+"�ˣ�\n��Ա              ���ڿ���\n"+jieguo2;
				}else {
					result="���ε������ݹ�"+allnum+"�����ɹ�"+oknum+"����ʧ��"+(allnum-oknum)+"��";
				}
			}else {
				result="�����ݿɵ��룡";
			}
			
		} catch (Exception e) {
			result="�쳣��Ϣ��"+e.getMessage();
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
