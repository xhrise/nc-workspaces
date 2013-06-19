package nc.itf.yto.util;

import nc.vo.pub.SuperVO;

public interface IReadmsg {
	public boolean checkExist(Class cls , String SqlWhere) throws Exception;
	public SuperVO[] getGeneralVOs(Class cls , String SqlWhere) throws Exception ;
}
