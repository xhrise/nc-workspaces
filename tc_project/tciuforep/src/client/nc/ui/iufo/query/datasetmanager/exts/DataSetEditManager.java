package nc.ui.iufo.query.datasetmanager.exts;

import java.awt.Container;
import java.util.Vector;

import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.iufo.query.datasetmanager.DataSetManager;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.datasetmanager.DataSetDirVO;

import com.ufsoft.iufo.inputplugin.inputcore.IDMaker;
import com.ufsoft.iufo.resource.StringResource;

public class DataSetEditManager {

	private Vector<IDataSetEditListener> listeners = new Vector<IDataSetEditListener>();

	private DataSetDefVO[] currentVO;
	
	//待处理的目录
	private DataSetDirVO pasteDir;

	private DataSetManager dsManager;
	

	private boolean isCut = false;

	public DataSetEditManager(DataSetManager dsManager) {
		this.dsManager = dsManager;
	}

	public void addListener(IDataSetEditListener lisener) {
		this.listeners.add(lisener);
	}

	public void removeListener(IDataSetEditListener lisener) {
		this.listeners.remove(lisener);
	}

	public void fireStateChanged() {
		for (IDataSetEditListener listener : listeners) {
			listener.stateChanged(this);
		}
	}

	public void cut() {
		this.isCut = true;
		DataSetDefVO[] defVos = this.dsManager.getCurrentDataSetDef();
		
		if (defVos.length <= 0) {
			//私有数据集不允许被剪切
			if (this.dsManager.getCurrentDir().isPrivate()){
				MessageDialog.showErrorDlg(null, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00577"));
				return;
			}
			//剪切目录
			setPasteDir(this.dsManager.getCurrentDir());
		} else {
			this.setCurrentVO(defVos);
		}
		fireStateChanged();
	}

	/**
	 * @i18n miufo1000845=提示
	 * @i18n miufo00571=不支持目录的复制和粘贴。
	 */
	public void copy() {
		this.isCut = false;
		DataSetDefVO[] defVos = this.dsManager.getCurrentDataSetDef();
		if (defVos.length <= 0) {
			MessageDialog.showHintDlg(null, StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo00571"));
			return;
		} else {
			this.setCurrentVO(defVos);
		}
		fireStateChanged();
	}

	/**
	 * @i18n miufo1000845=提示
	 * @i18n miufo00572=请先选择目录。
	 * @i18n miufo1000155=错误
	 * @i18n miufo00573=不能将公有数据集剪切到私有数据集目录。
	 * @i18n miufo1000775=确认
	 * @i18n miufo00574=是否确认将私有数据集转为公有数据集(该过程不可逆)?
	 * @i18n miufo00575=不能将公有数据集复制到私有数据集目录。
	 * @i18n miufo00576=私有数据集目录不能被复制。
	 * @i18n miufo00577=私有数据集目录不能被剪切。
	 * @i18n miufo00578=不能将目录剪切到下级目录。
	 * @i18n miufo00579=目录已经存在。
	 */
	public void paste(Container container) throws UFOSrvException {
		DataSetDirVO dirVO = this.dsManager.getCurrentDir();
		if (dirVO == null) {
			MessageDialog.showHintDlg(null, StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo00572"));
			return;
		}
		if ((getCurrentVO() != null) && (getCurrentVO().length > 0)) {
			if (isCut) { //剪切
				if (dirVO.getPk_datasetdir().equals(
						getCurrentVO()[0].getPk_datasetdir())) {
					return;
				} else {
					for (DataSetDefVO defVO : this.getCurrentVO()) {
						//yza+ 2008-6-16 公有数据集粘贴到私有数据集目录
						if(defVO.getOwner().trim().length() == 0 && dirVO.isPrivate())
						{
							MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00573"));
							return;
						}
						else if(defVO.getOwner().trim().length() > 0 && !(dirVO.isPrivate()))
						{
							//私有数据集粘贴到公有目录
							if(MessageDialog.showYesNoDlg(container,StringResource.getStringResource("miufo1000775"), StringResource.getStringResource("miufo00574")) 
									== MessageDialog.ID_YES)
							{
								defVO.setOwner("");
							}
							else
							{
								this.dsManager.setCurrentDir(null);
								return;
							}

						}
						defVO.setPk_datasetdir(dirVO.getPk_datasetdir());
						String newName = defVO.getName();
						for (int i = 0; i < 10000; i++) {
							if (i > 0) {
								newName = defVO.getName() + i;
							}
							if (DataSetDefBO_Client.loadDataSetDefVoByName(
									dirVO.getPk_datasetdir(), newName) == null) {
								defVO.setName(newName);
								break;
							}
						}
						DataSetDefBO_Client.updateDataSetDef(defVO);
						dsManager.addDataSetDef(dirVO, defVO);
					}
					//清理
//					setCurrentVO(null);
//					this.dsManager.setCurrentDir(null);
				}
			} else { //复制
				for (DataSetDefVO defVO : this.getCurrentVO()) {
					DataSetDefVO newVO = (DataSetDefVO) defVO.clone();
					newVO.setPk_datasetdir(dirVO.getPk_datasetdir());
					String vopk = IDMaker.makeID(20, IDMaker.TYPE_ALL);
					newVO.setPk_datasetdef(vopk);
					
					//yza+ 2008-6-16 公有数据集粘贴到私有数据集目录,目前没有跨目录选择问题所以这样改
					if(defVO.getOwner().trim().length() == 0 && dirVO.isPrivate())
					{
						MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00575"));
						return;
					}
					else if(defVO.getOwner().trim().length() > 0 && !(dirVO.isPrivate()))
					{
						//私有数据集粘贴到公有目录
						if(MessageDialog.showYesNoDlg(container,StringResource.getStringResource("miufo1000775"), StringResource.getStringResource("miufo00574")) 
								== MessageDialog.ID_YES)
						{
							newVO.setOwner("");
						}
						else
							return;
					}
					
					String newName = defVO.getName();
					for (int i = 0; i < 10000; i++) {
						if (i > 0) {
							newName = newVO.getName() + i;
						}
						if (DataSetDefBO_Client.loadDataSetDefVoByName(
								dirVO.getPk_datasetdir(), newName) == null) {
							newVO.setName(newName);
							break;
						}
					}
					
					for (int i = 1; i < 10000; i++) {
						if (DataSetDefBO_Client.loadDataSetDefVoByCode(newVO
								.getCode()
								+ i) == null) {
							newVO.setCode(newVO.getCode() + i);
							break;
						}
					}

					newVO = DataSetDefBO_Client.createDataSetDef(newVO);
					dsManager.addDataSetDef(dirVO, newVO);
				}
			}
		}
		else //粘贴目录
		{
			if(getPasteDir() == null){
				return;
			}
			String msg = StringResource.getStringResource("miufo00576");
			if(isCut)
			{
				msg = StringResource.getStringResource("miufo00577");
				if(DataSetDefBO_Client.isChildDir(getPasteDir().getPk_datasetdir(), dirVO.getPk_datasetdir()))
				{
					MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00578"));
					return;
				}
			}
			if(getPasteDir().isPrivate())
			{
				MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), msg);
				return;
			}
			DataSetDirVO sameDir = DataSetDefBO_Client.loadDirByName(dirVO.getPk_datasetdir(),getPasteDir().getName());
			if(sameDir != null)
			{
				MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00579"));
				return;
			}
			dealWithDir();
		}
		
		//刷新DataSetTree
		DatasetTree.clearInstance();
		//清理
		setCurrentVO(null);
		this.dsManager.setCurrentDir(null);
		fireStateChanged();
	}

	public boolean isCut() {
		return this.isCut;
	}

	public boolean hasVO() {
		return this.getCurrentVO() != null || this.getPasteDir()!=null;
	}
	
	//处理目录的剪切和复制 yza+ 2008-6-10
	/**
	 * @i18n miufo1000845=提示
	 * @i18n miufo00580=不能将目录粘贴到私有数据集目录。
	 * @i18n miufo00571=不支持目录的复制和粘贴。
	 */
	private void dealWithDir()
	{
		if(getPasteDir() == null)
			return;
		DataSetDirVO currentDir = this.dsManager.getCurrentDir();
		if(isCut)
		{
			if(getPasteDir().equals(currentDir))
				return;
			if(currentDir.isPrivate())
			{
				MessageDialog.showHintDlg(null, StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo00580"));
				return;
			}
			this.getPasteDir().setPk_parentdir(currentDir.getPk_datasetdir());
			this.dsManager.updateDir(this.getPasteDir());
		}
		else
		{
			MessageDialog.showHintDlg(null, StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo00571"));
			return;
			//复制目录
////			DataSetDirVO newDir =dsManager.addDir(currentDir, pasteDir.getName(), pasteDir.getCode());
//			DataSetDirVO newDir =dsManager.addDir(currentDir, pasteDir.getName(), pasteDir.getRemark());
//			DataSetDefVO[] oldDS = null;
//			DataSetDefVO newDS = null;
//			try {
//				oldDS = DataSetDefBO_Client.loadDataSetDefsByDir(this.pasteDir);
//			} catch (UFOSrvException e) {
//				AppDebug.debug(e);
//			}
//			if(oldDS!=null && oldDS.length > 0)
//			{
//				for(DataSetDefVO dsdv : oldDS)
//				{
//					newDS = (DataSetDefVO)dsdv.clone();
//					newDS.setPk_datasetdef(null);
//					newDS.setPk_datasetdir(newDir.getPk_datasetdir());
//					try {
//						DataSetDefBO_Client.createDataSetDef(newDS);
//					} catch (UFOSrvException e) {
//						AppDebug.debug(e);
//					}
//				}
//			}
		}
		dsManager.reloadDirTree();
	}

	public void setCurrentVO(DataSetDefVO[] currentVO) {
		this.currentVO = currentVO;
	}

	public DataSetDefVO[] getCurrentVO() {
		return currentVO;
	}

	public void setPasteDir(DataSetDirVO pasteDir) {
		this.pasteDir = pasteDir;
	}

	public DataSetDirVO getPasteDir() {
		return pasteDir;
	}
}
