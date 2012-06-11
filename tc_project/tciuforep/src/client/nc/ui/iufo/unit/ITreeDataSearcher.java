package nc.ui.iufo.unit;

public interface ITreeDataSearcher {
	/**
	 * 动态树搜索时，根据值进行搜索
	 * @param strText，搜索的值
	 * @param strOldPK，上一次查找到的节点的PK值
	 * @param bDown，是否是向下搜索
	 * @return，搜到的节点的父节点的PK值数组
	 */
	public String[] searchNodePaths(String strText,String strOldPK,boolean bDown);
}
