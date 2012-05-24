package nc.ui.trade.businessaction;

import java.util.*;
/**
 * �˴���������˵����
 * �������ڣ�(2004-5-14 18:37:37)
 * @author�����ھ�1
 */
import nc.vo.pub.*;
public abstract class DefaultBusinessAction implements IBusinessController {
/**
 * DefaultBusinessAction ������ע�⡣
 */
public DefaultBusinessAction() {
	super();
}
/**
 * ����Ŀǰ�������ݡ�
 * �������ڣ�(2004-5-14 18:42:39)
 * @param totalVo nc.vo.pub.CircularlyAccessibleValueObject
 * @param retChgVo nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception �쳣˵����
 */
protected void fillUITotalVO(
	CircularlyAccessibleValueObject[] totalVo,
	CircularlyAccessibleValueObject[] retChgVo)
	throws java.lang.Exception {
	if (totalVo == null || retChgVo == null)
		return;
	ArrayList newAl = new ArrayList();
	for (int j = 0; j < retChgVo.length; j++)
	{
		if (retChgVo[j].getStatus() != VOStatus.DELETED)
			newAl.add(retChgVo[j]);
	}
	if (totalVo != null)
	{
		int intChanged = 0;
		for (int j = 0; j < totalVo.length; j++)
		{
			switch (totalVo[j].getStatus())
			{
				case VOStatus.DELETED :
					{
						break;
					}
				case VOStatus.UPDATED :
				case VOStatus.NEW :
					{
						totalVo[j] = (SuperVO) newAl.get(intChanged++);
						break;
					}
			}
		}
	}
}
}
