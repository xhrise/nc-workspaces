package nc.ui.iufo.unit;

public interface ITreeDataLoader {
	public ITreeNodeData getRootNodeData();
	public ITreeNodeData[] getSubNodeDatas(ITreeNodeData parentData);
}
