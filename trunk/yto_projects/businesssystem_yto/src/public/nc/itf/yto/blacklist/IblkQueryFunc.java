package nc.itf.yto.blacklist;

import java.util.Vector;

import nc.vo.pub.lang.UFDate;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;
import nc.vo.yto.business.PsndocVO;

public interface IblkQueryFunc {
	public String getStr(String pk_psndoc , String datasource) throws Exception;
	public nc.vo.yto.business.PsnbasdocVO getPsnbasdocbyPk(String pk_psnbasdoc , String datasource) throws Exception;
	public boolean insertPsndocBad_h(Vector ClientData , String pk_billtype , String vbillno , String datasource) throws Exception;
	public boolean insertPsndocBad_b(nc.vo.yto.business.PsnbasdocVO psnbasVO , String pk_psndoc_bad , String psncode , String cuserid , UFDate indate , String pk_corp , String cause , String basgroupdef18 , String cuserName , String unitname , String datasource) throws Exception ;
	public boolean insertPsndocBad(HiPsndocBadAppBVO badapp , String datasource) throws Exception;
	public boolean delPsndocBad(String pk_psnbasdoc , String datasource) throws Exception;
	public nc.vo.yto.business.PsnbasdocVO getPsnbasByPK(String pk_psnbasdoc , String datasource) throws Exception;
	public PsndocVO[] getPsnByPK(String psncode , String datasource) throws Exception;
	public PsndocVO[] getPsnByPsncode(String psncode , String datasource) throws Exception;
	public void modifyPsndocBad_b_photo(String cardid , String datasource) throws Exception;
}
