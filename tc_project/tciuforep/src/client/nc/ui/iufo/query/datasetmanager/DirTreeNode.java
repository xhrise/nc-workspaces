package nc.ui.iufo.query.datasetmanager;

import nc.ui.pub.beans.tree.ExTreeNode;
import nc.vo.iufo.datasetmanager.DataSetDirVO;

/**
 * 数据集目录节点
 * @author yangjiana
 * 2008-4-23
 */
@SuppressWarnings("serial")
public class DirTreeNode extends ExTreeNode {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataSetDirVO dirVo;
	
	public DirTreeNode(DataSetDirVO dirVo)
	{
		this.dirVo = dirVo;
	}
	
	public String toString()
	{
		if (dirVo == null)
		{
			return "null";
		}
		return dirVo.getName();
	}

	public DataSetDirVO getDirVo() {
		return dirVo;
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DirTreeNode)) {
			return false;
		}
		DirTreeNode node = (DirTreeNode)obj;
		if(node.getDirVo().getPk_datasetdir() == null)
			return false;
		return this.dirVo.getPk_datasetdir().equals(node.getDirVo().getPk_datasetdir());
	}
	
	
}
