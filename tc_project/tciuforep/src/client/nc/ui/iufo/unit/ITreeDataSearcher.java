package nc.ui.iufo.unit;

public interface ITreeDataSearcher {
	/**
	 * ��̬������ʱ������ֵ��������
	 * @param strText��������ֵ
	 * @param strOldPK����һ�β��ҵ��Ľڵ��PKֵ
	 * @param bDown���Ƿ�����������
	 * @return���ѵ��Ľڵ�ĸ��ڵ��PKֵ����
	 */
	public String[] searchNodePaths(String strText,String strOldPK,boolean bDown);
}
