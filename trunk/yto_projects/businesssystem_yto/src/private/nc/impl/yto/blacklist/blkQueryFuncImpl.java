package nc.impl.yto.blacklist;

import java.util.Vector;

import nc.bs.pub.SuperDMO;
import nc.vo.pub.lang.UFDate;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;
import nc.vo.yto.business.PsndocVO;

public class blkQueryFuncImpl implements nc.itf.yto.blacklist.IblkQueryFunc {
	
	private String sql = null;
	
	private nc.bs.yto.blacklist.blkFuncDMO blkFunc;
	
	public String getStr(String sql , String datasource) throws Exception {
		if(blkFunc == null)
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();
		
		return blkFunc.getStr(sql, datasource);
	}
	
	public nc.vo.yto.business.PsnbasdocVO getPsnbasdocbyPk(String pk_psnbasdoc , String datasource) throws Exception {
		
		sql = "select * from bd_psnbasdoc where pk_psnbasdoc = '"+pk_psnbasdoc+"'";
		
		if(blkFunc == null) 
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();
		
		return blkFunc.getPsnbasdocbyPk(sql, datasource);
		
	}
	
	public boolean insertPsndocBad_h(Vector ClientData , String pk_billtype , String vbillno , String datasource) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append("insert into hi_psndoc_bad_app_h (pk_corp , pk_psndoc_bad , pk_billtype , ");
		sb.append(" vbillstatus , voperatorid , vbillno , dr , voperatordate )");
		sb.append(" values('"+ClientData.get(0)+"' , generatePK('"+ClientData.get(0)+"') , '"+pk_billtype+"' , '8' , '"+ClientData.get(7)+"' , '"+vbillno+"' , 0 , '"+ClientData.get(9).toString().substring(0 , 10)+"')");

		sql = sb.toString();
		
		if(blkFunc == null) 
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();

		
		return blkFunc.executePsndocBad(sql, datasource);

	}
	
	public boolean insertPsndocBad_b(nc.vo.yto.business.PsnbasdocVO psnbasVO , String pk_psndoc_bad , String psncode , String cuserid , UFDate indate , String pk_corp , String cause , String basgroupdef18 , String cuserName , String unitname , String datasource) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		String sex = psnbasVO.getSex();
		if(sex.equals("ÄÐ"))
			sex = "1";
		else if(sex.equals("Å®"))
			sex = "2";
		
		sb.append("insert into hi_psndoc_bad_app_b (pk_psndoc_bad , pk_psnbasdoc , psncode , ");
		sb.append(" psnname , sex , birthday , id , permanentres , cuserid , indate , pk_corp , ");
		sb.append(" cause , dr , def1 , def2) "); 
		sb.append(" values('"+pk_psndoc_bad+"' , '"+psnbasVO.getPk_psnbasdoc()+"' , ");
		sb.append("'"+ psncode +"' , '"+psnbasVO.getPsnname()+"' , '"+sex+"' , '"+psnbasVO.getBirthdate()+"' ");
		sb.append(" , '"+psnbasVO.getId()+"' , '"+basgroupdef18+"' , '"+cuserid+"' , '"+indate+"' , '"+pk_corp+"' , ");
		sb.append(" '"+cause+"' , 0 , '"+cuserName+"' , '"+unitname+"') ");

		sql = sb.toString();
		
		if(blkFunc == null) 
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();

		
		return blkFunc.executePsndocBad(sql, datasource);

	}
	
	public void modifyPsndocBad_b_photo(String cardid , String datasource) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("update hi_psndoc_bad_app_b set def3 = ");
		sb.append("(select photo from bd_psnbasdoc where id = '"+cardid+"') ");
		sb.append("where id = '"+cardid+"'");
		
		sql = sb.toString();
		
		if(blkFunc == null) 
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();

		
		blkFunc.executePsndocBad(sql, datasource);
	
	}
	
	public boolean insertPsndocBad(HiPsndocBadAppBVO badapp , String datasource) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into hi_psndoc_bad (birthday , cause , comefrom , cuserid , ");
		sb.append(" id , indate , permanentres , pk_corp , pk_psnbasdoc , pk_psndoc_bad , psncode , psnname , sex , dr , delflag)");
		sb.append(" values('"+badapp.getBirthday()+"' , '"+badapp.getCause()+"' , ");
		sb.append(" 1 , '"+badapp.getCuserid()+"' , '"+badapp.getId()+"' , '"+badapp.getIndate()+"' , ");
		sb.append(" '"+badapp.getPermanentres()+"' , '"+badapp.getPk_corp()+"' , '"+badapp.getPk_psnbasdoc()+"' , ");
		sb.append(" generatePK('"+badapp.getPk_corp()+"') , '"+badapp.getPsncode()+"' , '"+badapp.getPsnname()+"' , ");
		sb.append(" '"+badapp.getSex()+"' , 0 , 0) ");
		
		sql = sb.toString();
		
		if(blkFunc == null) 
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();
		
		return blkFunc.executePsndocBad(sql, datasource);
	}
	
	public boolean delPsndocBad(String pk_psnbasdoc , String datasource) throws Exception {
		sql = "delete from hi_psndoc_bad where pk_psnbasdoc = '"+pk_psnbasdoc+"'";
		
		if(blkFunc == null)
			blkFunc = new nc.bs.yto.blacklist.blkFuncDMO();
		
		return blkFunc.executePsndocBad(sql, datasource);
	}
	
	public nc.vo.yto.business.PsnbasdocVO getPsnbasByPK(String pk_psnbasdoc , String datasource) throws Exception {
		return (nc.vo.yto.business.PsnbasdocVO)new SuperDMO(datasource).queryByPrimaryKey(nc.vo.yto.business.PsnbasdocVO.class, pk_psnbasdoc);
	}
	
	public PsndocVO[] getPsnByPK(String pk_psnbasdoc , String datasource) throws Exception {
		return (PsndocVO[]) new SuperDMO(datasource).queryByWhereClause(PsndocVO.class, " pk_psnbasdoc = '"+pk_psnbasdoc+"' and psnclscope = 0") ;
	}
	
	public PsndocVO[] getPsnByPsncode(String psncode , String datasource) throws Exception {
		return (PsndocVO[]) new SuperDMO(datasource).queryByWhereClause(PsndocVO.class, " psncode = '"+psncode+"'") ;
	}
	
//	public boolean modifyPkbybillno() {
//		sql = "update hi_psndoc_bad_app_h set pk_psndoc_bad";
//		
//		return false;
//	}
}
