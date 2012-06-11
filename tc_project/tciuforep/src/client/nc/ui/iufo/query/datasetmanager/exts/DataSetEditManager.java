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
	
	//�������Ŀ¼
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
			//˽�����ݼ�����������
			if (this.dsManager.getCurrentDir().isPrivate()){
				MessageDialog.showErrorDlg(null, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00577"));
				return;
			}
			//����Ŀ¼
			setPasteDir(this.dsManager.getCurrentDir());
		} else {
			this.setCurrentVO(defVos);
		}
		fireStateChanged();
	}

	/**
	 * @i18n miufo1000845=��ʾ
	 * @i18n miufo00571=��֧��Ŀ¼�ĸ��ƺ�ճ����
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
	 * @i18n miufo1000845=��ʾ
	 * @i18n miufo00572=����ѡ��Ŀ¼��
	 * @i18n miufo1000155=����
	 * @i18n miufo00573=���ܽ��������ݼ����е�˽�����ݼ�Ŀ¼��
	 * @i18n miufo1000775=ȷ��
	 * @i18n miufo00574=�Ƿ�ȷ�Ͻ�˽�����ݼ�תΪ�������ݼ�(�ù��̲�����)?
	 * @i18n miufo00575=���ܽ��������ݼ����Ƶ�˽�����ݼ�Ŀ¼��
	 * @i18n miufo00576=˽�����ݼ�Ŀ¼���ܱ����ơ�
	 * @i18n miufo00577=˽�����ݼ�Ŀ¼���ܱ����С�
	 * @i18n miufo00578=���ܽ�Ŀ¼���е��¼�Ŀ¼��
	 * @i18n miufo00579=Ŀ¼�Ѿ����ڡ�
	 */
	public void paste(Container container) throws UFOSrvException {
		DataSetDirVO dirVO = this.dsManager.getCurrentDir();
		if (dirVO == null) {
			MessageDialog.showHintDlg(null, StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo00572"));
			return;
		}
		if ((getCurrentVO() != null) && (getCurrentVO().length > 0)) {
			if (isCut) { //����
				if (dirVO.getPk_datasetdir().equals(
						getCurrentVO()[0].getPk_datasetdir())) {
					return;
				} else {
					for (DataSetDefVO defVO : this.getCurrentVO()) {
						//yza+ 2008-6-16 �������ݼ�ճ����˽�����ݼ�Ŀ¼
						if(defVO.getOwner().trim().length() == 0 && dirVO.isPrivate())
						{
							MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00573"));
							return;
						}
						else if(defVO.getOwner().trim().length() > 0 && !(dirVO.isPrivate()))
						{
							//˽�����ݼ�ճ��������Ŀ¼
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
					//����
//					setCurrentVO(null);
//					this.dsManager.setCurrentDir(null);
				}
			} else { //����
				for (DataSetDefVO defVO : this.getCurrentVO()) {
					DataSetDefVO newVO = (DataSetDefVO) defVO.clone();
					newVO.setPk_datasetdir(dirVO.getPk_datasetdir());
					String vopk = IDMaker.makeID(20, IDMaker.TYPE_ALL);
					newVO.setPk_datasetdef(vopk);
					
					//yza+ 2008-6-16 �������ݼ�ճ����˽�����ݼ�Ŀ¼,Ŀǰû�п�Ŀ¼ѡ����������������
					if(defVO.getOwner().trim().length() == 0 && dirVO.isPrivate())
					{
						MessageDialog.showErrorDlg(container, StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00575"));
						return;
					}
					else if(defVO.getOwner().trim().length() > 0 && !(dirVO.isPrivate()))
					{
						//˽�����ݼ�ճ��������Ŀ¼
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
		else //ճ��Ŀ¼
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
		
		//ˢ��DataSetTree
		DatasetTree.clearInstance();
		//����
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
	
	//����Ŀ¼�ļ��к͸��� yza+ 2008-6-10
	/**
	 * @i18n miufo1000845=��ʾ
	 * @i18n miufo00580=���ܽ�Ŀ¼ճ����˽�����ݼ�Ŀ¼��
	 * @i18n miufo00571=��֧��Ŀ¼�ĸ��ƺ�ճ����
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
			//����Ŀ¼
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
