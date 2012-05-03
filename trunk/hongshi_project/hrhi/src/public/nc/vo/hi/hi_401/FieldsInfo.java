package nc.vo.hi.hi_401;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.IBillItem;
import java.util.*;

/**
 * �˴���������˵����
 * �������ڣ�(2002-3-20 10:12:44)
 * @author���ﺣ��
 */
public  class FieldsInfo {

//	 ��������
//    final static int STRING = 0; // �ַ�
//
//    final static int INTEGER = 1; // ����
//
//    final static int DECIMAL = 2; // С��
//
//    final static int DATE = 3; // ����
//
//    final static int BOOLEAN = 4; // �߼�
//
//    final static int UFREF = 5; // ����
//
//    final static int COMBO = 6; // ����
//
//    final static int USERDEF = 7; // �Զ������
//
//    final static int TIME = 8; // ʱ��
//
//    final static int TEXTAREA = 9; // ���ı�
//
//    final static int IMAGE = 10; // ͼƬ
//
//    final static int OBJECT = 11; // ����
//
//    final static int BLANK = 12; // ռλ��
//
//    final static int EMAILADDRESS = 14;// report used only 2004-03-30 ==13
//
//    final static int PASSWORDFIELD = 13;// �������ʽ,ΪȨ������ 2005-10-14, ��ΪΪ����ʾ��Ҫ
//
//    final static int MINTYPE = STRING; // �ַ�
//
//    final static int MAXTYPE = IMAGE; // ͼƬ

/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-3-20 10:30:09)
 */
public static String[][] getFieldFomula(SetdictItemVO[] vos) {

	/***** ���� *********/
	Logger.error("��ʼ���Թ�ʽ����");

	//	
	String[][] fomula = null;
	if (vos.length > 0) {
		fomula = new String[vos.length][];
	} else {
		return null;
	}
	Hashtable fomulahash = new Hashtable();

	nc.bs.pub.formulaparse.FormulaParse parse =
		new nc.bs.pub.formulaparse.FormulaParse();
	nc.vo.pub.formulaset.VarryVO varVO = null;
	for (int i = 0; i < vos.length; i++) {

		if (vos[i].getFldformula() != null && vos[i].getFldformula().length() != 0) {
			parse.setExpress(vos[i].getFldformula());
			varVO = parse.getVarry();
			if (varVO != null) {
				String[] vars = varVO.getVarry();
				if (vars != null) {
					for (int j = 0; j < vars.length; j++) {
						String tempFomula = "";
						switch (vos[i].getDatatype().intValue()) {
							case 2 :
								{
									tempFomula = vos[i].getFldcode() + "->" + "int(" + vos[i].getFldformula() + ")";
									break;

								}
							default :
								{
									tempFomula = vos[i].getFldcode() + "->" + vos[i].getFldformula();
									break;
								}

						}
						if (fomulahash.containsKey(vars[j])) {

							Vector tempfumulaVec = (Vector) fomulahash.get(vars[j]);

							tempfumulaVec.addElement(tempFomula);
							fomulahash.put(vars[j], tempfumulaVec);

						} else {

							//String fstFomula = vos[i].getFldcode() + "->" + vos[i].getFldformula();
							Vector vecFomula = new Vector();
							vecFomula.addElement(tempFomula);
							fomulahash.put(vars[j], vecFomula);

						}

					}
				} else {
					continue;
				}

			}
		}
	}

	for (int i = 0; i < vos.length; i++) {
		if (fomulahash.containsKey(vos[i].getFldcode())) {
			Vector vec = (Vector) fomulahash.get(vos[i].getFldcode());
			if (vec.size() > 0) {
				fomula[i] = new String[vec.size()];
				vec.copyInto(fomula[i]);
				//

			}

			//

		} else
			fomula[i] = null;

	}

	return fomula;

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-3-20 10:30:09)
 */
public static String[] getFieldKey(nc.vo.pub.ValueObject[] fieldvos)
{

	if (fieldvos != null && fieldvos.length != 0)
	{
		String[] keys = new String[fieldvos.length];
		for (int i = 0; i < fieldvos.length; i++)
		{
			if (((SetdictItemVO)fieldvos[i]).getFldcode() != null && ((SetdictItemVO)fieldvos[i]).getFldcode().length() != 0)
			{   keys[i]=((SetdictItemVO)fieldvos[i]).getFldcode();
			
	            }else keys[i]="";
			

		}
	return keys;    

	}else return null;

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-3-20 10:30:09)
 */
public static String[] getFieldName(nc.vo.pub.ValueObject[] fieldvos)
{
	if (fieldvos != null && fieldvos.length != 0)
	{
		String[] names = new String[fieldvos.length];
		for (int i = 0; i < fieldvos.length; i++)
		{
			if (((SetdictItemVO)fieldvos[i]).getFldname() != null && ((SetdictItemVO)fieldvos[i]).getFldname().length() != 0)
			{
				names[i] =((SetdictItemVO) fieldvos[i]).getFldname();

			}
			else
				names[i] = "";

		}
		return names;

	}
	else
		return null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-3-20 10:30:09)
 */
public static int[] getFieldType(nc.vo.pub.ValueObject[] vos) {
	int[] types = null;
	if (vos != null && vos.length != 0) {
		types = new int[vos.length];
		for (int i = 0; i < types.length; i++) {
			switch (((SetdictItemVO) vos[i]).getDatatype().intValue()) {
				case 1 :
					{
						types[i] = IBillItem.STRING;
						break;
					}
				case 2 :
					{
						types[i] = IBillItem.INTEGER;
						break;
					}
				case 3 :
					{
						types[i] = IBillItem.DECIMAL;
						break;
					}
				case 4 :
					{
						types[i] = IBillItem.DATE;
						break;
					}
				case 5 :
					{
						types[i] = IBillItem.BOOLEAN;
						break;
					}
				case 6 :
					{
						types[i] = IBillItem.UFREF;
						break;
					}
				case 7 :
					{
						types[i] = IBillItem.COMBO;
						break;
					}
				case 8 :
					{
						types[i] = IBillItem.TIME;
						break;
					}
				case 9 :
					{
						types[i] = IBillItem.STRING;
						break;
					}

				default :
					{
						types[i] = IBillItem.STRING;
						break;
					}
			}
		}
	}
	return types;

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-3-20 10:30:09)
 */
public static int getFieldType(int iType) {
	switch (iType) {
		case 1 :

			return IBillItem.STRING;

		case 2 :
			return IBillItem.INTEGER;
		case 3 :
			return IBillItem.DECIMAL;
		case 4 :
			return IBillItem.DATE;
		case 5 :
			return IBillItem.BOOLEAN;
		case 6 :
			return IBillItem.UFREF;
		case 7 :
			return IBillItem.COMBO;
		case 8 :
			return IBillItem.TIME;

		case 9 :
			return IBillItem.STRING;

		default :
			return IBillItem.STRING;
	}

}
}
