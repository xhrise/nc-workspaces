/*
 * �������� 2005-7-19
 *
 */
package nc.ui.bi.query.designer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.reportquery.demo.ExampleFileFilter;
import nc.us.bi.query.manager.QuerySrv;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryDefToXmlUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.bi.query.manager.ForgeQueryModelVO;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iufo.resmng.dir.DirVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;
import nc.vo.iuforeport.businessquery.QEEnvParamBean;
import nc.vo.pub.querymodel.QueryDefToXmlUtil;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * �����������
 */
class ToolFuncPanel extends UIPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//���뵼���Ի���
	protected UIFileChooser m_fileChooser = null;

	//��������Դ
	private String m_dsNameForDef = null;

	//ѡ�в�ѯID
	private String m_queryId = null;

	//ѡ��Ŀ¼ID
	private String m_folderId = null;

	private UIButton ivjBnImportQE = null;

	private UIButton ivjBnImport = null;

	private UIButton ivjBnExport = null;

	private UIButton ivjBnAlterTable = null;

	/**
	 * ������
	 */
	public ToolFuncPanel() {
		super();
		initUI();
	}

	/**
	 * ��ʼ������
	 */
	private void initUI() {
		//���ò���
		setLayout(new UIButtonLayout());
		//��Ӱ�ť
		add(getBnImport());
		add(getBnExport());
		add(getBnImportQE());
		add(getBnAlterTable());
		//��ʼ������
		initConnections();
	}

	/**
	 * ���� BnImport ����ֵ��
	 * @i18n miufo1000959=����
	 */
	private UIButton getBnImport() {
		if (ivjBnImport == null) {
			ivjBnImport = new UIButton();
			ivjBnImport.setName("BnImport");
			ivjBnImport.setText(StringResource.getStringResource("miufo1000959"));
		}
		return ivjBnImport;
	}

	/**
	 * ���� BnExport ����ֵ��
	 * @i18n miufo1000961=����
	 */
	private UIButton getBnExport() {
		if (ivjBnExport == null) {
			ivjBnExport = new UIButton();
			ivjBnExport.setName("BnExport");
			ivjBnExport.setText(StringResource.getStringResource("miufo1000961"));
		}
		return ivjBnExport;
	}

	/**
	 * ���� BnImportQE ����ֵ��
	 * @i18n miufo00356=����QE��ѯ����
	 */
	private UIButton getBnImportQE() {
		if (ivjBnImportQE == null) {
			ivjBnImportQE = new UIButton();
			ivjBnImportQE.setName("BnImportQE");
			ivjBnImportQE.setText(StringResource.getStringResource("miufo00356"));
		}
		return ivjBnImportQE;
	}

	/**
	 * ���� BnAlterTable ����ֵ��
	 * @i18n miufo00357=�����ṹ
	 */
	private UIButton getBnAlterTable() {
		if (ivjBnAlterTable == null) {
			ivjBnAlterTable = new UIButton();
			ivjBnAlterTable.setName("BnAlterTable");
			ivjBnAlterTable.setText(StringResource.getStringResource("miufo00357"));
		}
		return ivjBnAlterTable;
	}

	/**
	 * ��ʼ������
	 */
	private void initConnections() {
		getBnImportQE().addActionListener(this);
		getBnImport().addActionListener(this);
		getBnExport().addActionListener(this);
		getBnAlterTable().addActionListener(this);
	}

	/**
	 * �¼���Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBnImportQE()) {
			doImportQE(getFolderId());
			return;
		}
		if (e.getSource() == getBnImport()) {
			doImport(getFolderId());
			return;
		}
		if (e.getSource() == getBnExport()) {
			doExport(getQueryId());
			return;
		}
		if (e.getSource() == getBnAlterTable()) {
			doAlter();
			return;
		}
	}

	/**
	 * ����QE��ѯ����
	 * @i18n miufo00358=�����ļ�����
	 * @i18n miufo00359=����QE��ѯ�������
	 * @i18n miufo00360=��Ŀ¼���Ѵ�����ͬ��ʾ���Ľڵ�
	 */
	private void doImportQE(String rootDirId) {
		//ѡ������Դ
		String ds = getDataSource();
		if (ds == null) {
			return;
		}
		//�����Ŀ¼
		DirVO rootDir = new DirVO();
		rootDir.setDirPK(rootDirId);
		rootDir.setName("");
		rootDir.setParentDirPK(null);

		//��õ���·��
		File[] impFiles = getImportPath();
		if (impFiles == null || impFiles.length == 0) {
			return;
		}

		StringBuffer info = new StringBuffer();
		try {
			HashMap<File, DirVO> hmFile2TreeNode = new HashMap<File, DirVO>();
			//
			for (int i = impFiles.length - 1; i >= 0; i--) {
				File impFile = impFiles[i];
				if (impFile.isDirectory()) {
					//��Ŀ¼�Ļ��ͽ�֮
					DirVO parentDir = (DirVO) hmFile2TreeNode.get(impFile
							.getParentFile());
					if (parentDir == null) {
						//��Ӧ��ѡ�ļ��ĸ�Ŀ¼
						hmFile2TreeNode.put(impFile, rootDir);
					} else {
						//����Ŀ¼VO
						DirVO dir = new DirVO();
						dir.setName(impFile.getName());
						dir.setParentDirPK(parentDir.getDirPK());
						ResTreeObject dirVO = new ResTreeObject();
						dirVO.setSrcVO(dir);
						dirVO.setNote(dir.getName());
						dirVO.setParentID(dir.getParentDirPK());
						dirVO.setType(IResTreeObject.OBJECT_TYPE_DIR);
						//ִ�в���
//						DirVO[] dirs = DirectoryBO_Client.createDirs(
//								new DirVO[] { dir }, IDatabaseNames.BI_QUERYDIR);
//						dir.setDirPK(dirs[0].getDirPK());
						IResTreeObject result = QuerySrv.getInstance().createDir(dirVO);
						dir.setDirPK(result.getID());
						//
						hmFile2TreeNode.put(impFile, dir);
					}
				} else {
					DirVO parentDir = (DirVO) hmFile2TreeNode.get(impFile
							.getParentFile());
					if (parentDir == null) {
						continue;
					}
					QueryModelDef qmd = null;
					try {
						//�����ļ���ת��
						qmd = QueryDefToXmlUtil
								.parseQueryDefFromXmlFile(impFile
										.getAbsolutePath());
					} catch (Exception e) {
						AppDebug.debug(e);
						info.append(StringResource.getStringResource("miufo00358")).append(" (").append(
								impFile.getAbsolutePath()).append(")");
						if (e.getMessage() != null) {
							info.append(":").append(e.getMessage()).append(
									"\r\n");
						} else {
							info.append("\r\n");
						}
						continue;
					}

					BIQueryModelDef	biQmd = new BIQueryModelDef();
					//�� QueryModelDef ��������ݵ��� BIQueryModelDef��
					String dsNameForDef = getDsNameForDef();
					//�����ѯ����
					QueryModelVO qm = new QueryModelVO();
					qm.setQuerycode(qmd.getID());
					qm.setQueryname(qmd.getDisplayName());
					qm.setPk_folderID(parentDir.getDirPK());
					qm.setPk_datasource(ds);
					qm.setType(BIQueryConst.TYPENAMES[1]);
					//ת��ΪXML
//					String definition = BIQueryDefToXmlUtil
//							.saveQueryDefToXml(qmd);
					//ִ�в���
					String oid = QueryModelBO_Client.insert(qm, biQmd, dsNameForDef);
					qm.setPrimaryKey(oid);
				}
			}
			//��ʾ
			MessageDialog.showWarningDlg(this, "UFBI", StringResource.getStringResource("miufo00359"));
		} catch (Exception e) {
			int index1 = e.getMessage().indexOf("Against Unique Constraint");
			if (index1 >= 0) {
				MessageDialog.showWarningDlg(this, "UFBI", StringResource.getStringResource("miufo00360"));
				return;
			}
			MessageDialog.showWarningDlg(this, "UFBI", StringResource.getStringResource("miufo00358"));
			AppDebug.debug(e);
		} finally {
			if (info.length() > 0) {
				MessageDialog.showWarningDlg(this, StringResource.getStringResource("miufo00358"), info.toString());
			}
		}
	}

	/**
	 * ��õ���·�� �������ڣ�(2003-10-29 15:45:31)
	 * 
	 * @return java.lang.String
	 */
	protected File[] getImportPath() {
		String hint = "Import";
		//ѡ����·��
		UIFileChooser fileChooser = getFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		ArrayList<File> fileList = new ArrayList<File>();
		if (fileChooser.showDialog(this, hint) == UIFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();
			for (int i = files.length - 1; i >= 0; i--) {
				File file = files[i];
				getFilePathRecursively(file, fileList);
			}
			//�ѵ�ǰĿ¼Ҳ�ӽ�ȥ
			if (files.length > 0) {
				fileList.add(files[0].getParentFile());
			}
		}
		return (File[]) fileList.toArray(new File[fileList.size()]);
	}

	private int getFilePathRecursively(File file, List<File> fileList) {
		String strFilePath = file.getAbsolutePath();
		if (file.isFile() && strFilePath.endsWith(".xml")) {
			fileList.add(file);
			return 1;
		} else if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			int count = 0;
			for (int i = subFiles.length - 1; i >= 0; i--) {
				count += getFilePathRecursively(subFiles[i], fileList);
			}
			if (count > 0) {
				fileList.add(file);
			}
			return count;
		} else {
			return 0;
		}
	}

	/**
	 * ��õ����Ի���ʵ�� �������ڣ�(2003-9-27 12:11:36)
	 * 
	 * @return nc.ui.pub.querymodel.DropTempDlg
	 */
	protected UIFileChooser getFileChooser() {
		if (m_fileChooser == null) {
			m_fileChooser = new UIFileChooser();
			FileFilter newFilter = new ExampleFileFilter("xml");
			if (m_fileChooser.getFileFilter() != null) {
				m_fileChooser.removeChoosableFileFilter(m_fileChooser
						.getFileFilter());
			}
			m_fileChooser.addChoosableFileFilter(newFilter);
			m_fileChooser.setFileFilter(newFilter);
		}
		return m_fileChooser;
	}

	/**
	 * ��ö�������Դ
	 */
	public String getDsNameForDef() {
		return m_dsNameForDef;
	}

	/**
	 * ���ѡ�в�ѯID
	 */
	public String getQueryId() {
		return m_queryId;
	}

	/**
	 * ���ö�������Դ
	 */
	public void setDsNameForDef(String dsNameForDef) {
		m_dsNameForDef = dsNameForDef;
	}

	/**
	 * ����ѡ�в�ѯID
	 */
	public void setQueryId(String queryId) {
		m_queryId = queryId;
		//���ѡ��Ŀ¼ID
		m_folderId = null;
		try {
			QueryModelVO[] qms = QueryModelBO_Client.queryQueryModel(
					"pk_querymodel='" + queryId + "'", m_dsNameForDef);
			if (qms != null && qms.length != 0) {
				m_folderId = qms[0].getPk_folderID();
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * �����ṹ
	 */
	private void doAlter() {
		AlterTableDlg dlg = new AlterTableDlg(this);
		dlg.setDsNameForDef(getDsNameForDef());
		dlg.showModal();
		dlg.destroy();
	}

	/**
	 * ���ѡ��Ŀ¼ID
	 */
	public String getFolderId() {
		return m_folderId;
	}

	/**
	 * ��õ���·�� �������ڣ�(2003-10-29 15:45:31)
	 * 
	 * @return java.lang.String
	 * @param bDirOnly
	 *            boolean
	 */
	protected String getExportPath(boolean bDirOnly) {
		String hint = "Export";
		//ѡ�񵼳�·��
		String strFullName = "";
		UIFileChooser fileChooser = getFileChooser();
		fileChooser
				.setFileSelectionMode(bDirOnly ? JFileChooser.DIRECTORIES_ONLY
						: JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		if (fileChooser.showDialog(this, hint) == UIFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			strFullName = file.getAbsolutePath();
			if (!(bDirOnly || strFullName.endsWith(".xml"))) {
				strFullName += ".xml";
			}
		}
		return strFullName;
	}

	/**
	 * ����
	 * @i18n miufo00361=�������
	 */
	private void doExport(String queryId) {
		//��õ���·��
		String expPath = getExportPath(false);
		if (expPath.equals("")) {
			return;
		}
		try {
			String dsNameForDef = getDsNameForDef();
			//��ò�ѯ��������
			BIQueryModelDef qmd = BIQueryUtil.getQueryModelDef(queryId,
					dsNameForDef);
			//ת��������
			BIQueryDefToXmlUtil.saveQueryDefToXmlFile(qmd, expPath);
			//��ʾ
			MessageDialog.showWarningDlg(this, "UFBI", StringResource.getStringResource("miufo00361"));
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * ����
	 * @i18n miufo00362=�������
	 */
	private void doImport(String folderId) {
		//ѡ������Դ
		String ds = getDataSource();
		if (ds == null) {
			return;
		}
		//��õ���·��
		File[] impFiles = getImportPath();
		if (impFiles == null || impFiles.length == 0) {
			return;
		}
		try {
			//�����ļ���ת��
			BIQueryModelDef qmd = BIQueryDefToXmlUtil
					.parseQueryDefFromXmlFile(impFiles[0].getAbsolutePath());
//			String xml = BIQueryDefToXmlUtil.saveQueryDefToXml(qmd);
			//�����ѯ����VO
			QueryModelVO qm = new QueryModelVO();
			qm.setQuerycode(qmd.getID());
			qm.setQueryname(qmd.getDisplayName());
			qm.setPk_folderID(folderId);
			//
			ForgeQueryModelVO fqm = qmd.getForgeQueryModel();
			qm.setPk_datasource(ds);
			if (fqm == null) {
				qm.setType(BIQueryUtil.judgeQueryType(qmd));
			} else {
				qm.setType(fqm.getType());
			}
			//�������ݿ�
			String oid = QueryModelBO_Client.insert(qm, qmd, getDsNameForDef());
			qm.setPrimaryKey(oid);
			//��ʾ
			MessageDialog.showWarningDlg(this, "UFBI", StringResource.getStringResource("miufo00362"));
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * ���öԻ���ʵ�� �������ڣ�(2005-5-16 19:07:52)
	 *  
	 */
	public void setDlg(ToolFuncDlg newToolDlg) {
	}

	/**
	 * ѡ������Դ
	 */
	private String getDataSource() {
		//���ȫ������Դ
//		DataSourceVO[] dses = null;
//		try {
//			dses = DataSourceSrv.getDataSource("1=1");
//		} catch (Exception e) {
//			AppDebug.debug(e);
//		}
		String[] dses = QEEnvParamBean.reloadInstance().getQueryDsn();
		if (dses != null) {
			//ѡ������Դ
			ChooseDsnDlg dlg = new ChooseDsnDlg(this);
			dlg.initDsn(dses);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//���ѡ������Դ
				String ds = dlg.getSelDs();
				return ds;
			}
		}
		return null;
	}
} 