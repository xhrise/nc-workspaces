package nc.ui.eh.tools;

public class TypeUntil {

	
	/**
	 * ˵������ģ�������������ת��Ϊ������Ŀ�������
	 * @param templtetype
	 * @return
	 *  ��ģ������������      �ַ� 0 ������ 5��������6 ,���ı� 9������ 1 ��С�� 2 ���� 3 ���߼� 4
	 *  �ڵ�����Ŀ����������� �ַ� 0 �������� 1�� ���� 2 ������ 3 ��ts 4�� ������ 5
	 */
	public static int changeDatatype(int templtetype){
		int itemtype = 0;
		switch (templtetype) {
		case 1:
			itemtype = 3;
			break;
		case 2:
			itemtype = 1;
			break;
		case 3:
			itemtype = 2;
			break;
		case 4:
			itemtype = 5;
			break;
		}
		return itemtype;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(changeDatatype(4));
	}

	
}
