package nc.impl.yto.util;

import nc.bs.pub.SuperDMO;
import nc.itf.yto.util.IReadmsg;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.pub.SuperVO;

public class Readmsg implements IReadmsg{

	public boolean checkExist(Class cls , String SqlWhere) throws Exception{
		Object[] objs = new SuperDMO().queryByWhereClause(cls, SqlWhere);
		if(objs.length == 0) 
			return true;
		else 
			return false;
	}
	
	public SuperVO[] getGeneralVOs(Class cls , String SqlWhere) throws Exception {
		SuperVO[] vos = new SuperDMO().queryByWhereClause(cls, SqlWhere);
		return vos;
	}
	
	
}
