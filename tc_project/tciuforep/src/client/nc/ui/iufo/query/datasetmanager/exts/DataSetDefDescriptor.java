package nc.ui.iufo.query.datasetmanager.exts;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.iufo.query.datasetmanager.DataSetManager;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.dsmanager.DSKindChooserDlg;
import nc.ui.pub.dsmanager.DataSetDesignWizardListPn;
import nc.ui.pub.dsmanager.DesignWizardFactory;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.AbstractWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardTabPn;
import nc.ui.pub.querytoolize.WizardContainerDlg;

import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.datasetmanager.DataSetDirVO;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.pub.dsmanager.DataSetDesignObject;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.iufo.fmtplugin.BDContextKey;
import com.ufsoft.iufo.fmtplugin.ContextFactory;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class DataSetDefDescriptor extends AbstractPlugDes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataSetDefDescriptor(DataSetDefPlugin plugin) {
		super(plugin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
	 */
	protected IExtension[] createExtensions() {
		ArrayList<IExtension> al_extensions = new ArrayList<IExtension>();

		al_extensions.add(new NewDirExt());
		
		al_extensions.add(new NewDSExt());
		
//		// ����������ע���Provider
//		DataSetRegisterItem[] items = DataSetProviderBO_Client.getRegisterItem();
//		if(items != null && items.length > 0){
//			for(DataSetRegisterItem item : items){
//				al_extensions.add(new NewDataSetExt(item.getProviderClass(), StringResource.getStringResource(item.getProviderName())));
//			}
//		}

		al_extensions.add(new EditExt());
		al_extensions.add(new RemoveExt());
		return al_extensions.toArray(new IExtension[0]);
	}

	public class NewDirExt extends DSMActionExt {

		/**
		 * @i18n miufo1000155=����
		 * @i18n miufo01096=˽�����ݼ�Ŀ¼�����½���Ŀ¼��
		 * @i18n miufo1000845=��ʾ
		 * @i18n miufo00572=����ѡ��Ŀ¼��
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			DataSetDirVO parentDirVo = dsManager.getCurrentDir();
			//yza+ 2008-6-16 ˽�����ݼ����ܽ���Ŀ¼
			if(parentDirVo == null){
				MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("usrdef0041"));
				return null;
			}
			if(parentDirVo.isPrivate())
			{
				MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo01096"));
				return null;
			}
			if (parentDirVo != null) {
				InputDirNameDlg dlg = new InputDirNameDlg(getContainer(),
						parentDirVo.getPk_datasetdir(), null);
				if (dlg.showModal() == UIDialog.ID_OK) {
					dsManager.addDir(parentDirVo, dlg.getDirName(), dlg
							.getRemark());
				}
				dsManager.setCurrentDir(null);
			} else {
				dsManager.setCurrentDir(null);
				MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo00572"));
			}
			}catch(Exception ex){
				dsManager.setCurrentDir(null);
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetDefDescriptor.this.getContainer(), ex);
			}
			return null;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		/**
		 * @i18n usrdef0005=Ŀ¼
		 * @i18n uiufofurl530001=�½�
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("usrdef0005"));
			uiDes1.setImageFile("biplugin/same_generation.gif");
			uiDes1.setPaths(new String[] { StringResource.getStringResource("uiufofurl530001") });

			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			return new ActionUIDes[] { uiDes1, uiDes2 };

		}

	}

	//�������ݼ�
	public class NewDSExt extends DSMActionExt {
		/**
		 * @i18n miufo1000845=��ʾ
		 * @i18n miufo01097=����ѡ�����ݼ���Ŀ¼
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			String providerClass="";
			
			String[] kinds={};//Ҫ��ʾ�����ݼ����͡��� ������ʾ����
			DSKindChooserDlg dsKindChooserDlg = new DSKindChooserDlg(getContainer(),kinds );
			
			UIUtilities.centerOnScreen(dsKindChooserDlg);
			int result=dsKindChooserDlg.showModal();
			
			if(result == UIDialog.ID_OK){
				providerClass=dsKindChooserDlg.getProviderClassName();
				
			}else{
				return null;
			}
			
			
			
			try{
			DataSetDirVO dirVo = dsManager.getCurrentDir();
			Context context = createContext();
			ContextFactory.initNCContext(context);
			if (dirVo == null) {
				MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo01097"));
				return null;
			}
			DataSetDefVO vo = new DataSetDefVO();
			vo.setPk_datasetdir(dirVo.getPk_datasetdir());
			//yza+ 2008-6-16 ��˽������ӵ����IDд�����ݼ�����
			if(dirVo.isPrivate())
			{
				vo.setOwner(dsManager.getOwnerID());
			}
			//д���û���Ϣ yza+ 2008-7-25
//			String currentUserId = (String)context.getAttribute(IQEContextKey.LOGIN_USER_ENV);
			String currentUserId = (String)context.getAttribute(BDContextKey.CUR_USER_ID);
			
			vo.setCreator(currentUserId);

			// zjb��
			// DataSetEditor editor = new DataSetEditor(vo, context,
			// providerClass);
			// DataSetWizardDlg dlg = new DataSetWizardDlg(getContainer(),
			// editor);
			// ׼�����ݼ�������ݽṹ
			DataSetDesignObject dsdo = new DataSetDesignObject();
			dsdo.setCurDataSetDef(vo);
			dsdo.setContext(context);
			dsdo.setShowTree(false);
			dsdo.setSaveWhenFinished(true);
			dsdo.setStatus(DataSetDesignObject.STATUS_CREATE);
			String defDsName = DataManageObjectIufo.getIUFODSName(false);
			dsdo.setDefDsName(defDsName);
			// ���б����
			AbstractWizardListPanel listPn = DesignWizardFactory.createDataSetWizard(dsdo, providerClass);
					
			// ����
			Dimension dm = new Dimension(1024,730);
			UIDialog dlg = showWizard(listPn, dm,dsManager,dsdo);
			
			if (dlg != null && dlg.getResult() == UIDialog.ID_OK) {
				DataSetDefVO newVo = dsdo.getCurDataSetDef();
				// DataSetDefVO newVo = dlg.getEditor().getDataset();
				// newVo.setPk_datasetdir(dirVo.getPk_datasetdir());
				
				//modified by biancm 20091104 �־û��Ѿ�������򵼽���ʱִ�й�DataSetDesignWizardListPn.doComplate()
//				try {
//					newVo = DataSetDefBO_Client.createDataSetDef(newVo);
//				} catch (UFOSrvException ex) {
//					AppDebug.debug(ex);
//				}
				dsManager.addDataSetDef(dsManager.getCurrentDir(), newVo);
			}else{
				dsManager.setCurrentDir(null);
			}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetDefDescriptor.this.getContainer(), ex);
			}

			return null;
		}


		@Override
		public UfoCommand getCommand() {
			return null;
		}

		/**
		 * @i18n uiufofurl530001=�½�
		 * @i18n miufo00241=���ݼ�
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo00241"));
			uiDes1.setImageFile("biplugin/same_generation.gif");
			uiDes1.setPaths(new String[] {StringResource.getStringResource("uiufofurl530001")});
			return new ActionUIDes[] { uiDes1 };
		}

	}
	
	@Deprecated
	public class NewDataSetExt extends DSMActionExt {

		private String providerClass;

		public String getProviderClass() {
			return providerClass;
		}

		public void setProviderClass(String providerClass) {
			this.providerClass = providerClass;
		}

		private String providerName;

		public String getProviderName() {
			return providerName;
		}

		public void setProviderName(String providerName) {
			this.providerName = providerName;
		}

		public NewDataSetExt(String providerClass, String providerName) {
			this.providerClass = providerClass;
			this.providerName = providerName;
		}

		// private String
		/**
		 * @i18n miufo1000845=��ʾ
		 * @i18n miufo01097=����ѡ�����ݼ���Ŀ¼
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			DataSetDirVO dirVo = dsManager.getCurrentDir();
			Context context = createContext();
			ContextFactory.initNCContext(context);
			if (dirVo == null) {
				MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo01097"));
				return null;
			}
			DataSetDefVO vo = new DataSetDefVO();
			vo.setPk_datasetdir(dirVo.getPk_datasetdir());
			//yza+ 2008-6-16 ��˽������ӵ����IDд�����ݼ�����
			if(dirVo.isPrivate())
			{
				vo.setOwner(dsManager.getOwnerID());
			}
			//д���û���Ϣ yza+ 2008-7-25
//			String currentUserId = (String)context.getAttribute(IQEContextKey.LOGIN_USER_ENV);
			String currentUserId = (String)context.getAttribute(BDContextKey.CUR_USER_ID);
			
			vo.setCreator(currentUserId);

			// zjb��
			// DataSetEditor editor = new DataSetEditor(vo, context,
			// providerClass);
			// DataSetWizardDlg dlg = new DataSetWizardDlg(getContainer(),
			// editor);
			// ׼�����ݼ�������ݽṹ
			DataSetDesignObject dsdo = new DataSetDesignObject();
			dsdo.setCurDataSetDef(vo);
			dsdo.setContext(context);
			dsdo.setShowTree(false);
			dsdo.setSaveWhenFinished(true);
			dsdo.setStatus(DataSetDesignObject.STATUS_CREATE);
			String defDsName = DataManageObjectIufo.getIUFODSName(false);
			dsdo.setDefDsName(defDsName);
			// ���б����
			AbstractWizardListPanel listPn = DesignWizardFactory.createDataSetWizard(dsdo, providerClass);
					
			// ����
			Dimension dm = new Dimension(1024,730);
			UIDialog dlg = showWizard(listPn, dm,dsManager,dsdo);
			
			if (dlg != null && dlg.getResult() == UIDialog.ID_OK) {
				DataSetDefVO newVo = dsdo.getCurDataSetDef();
				// DataSetDefVO newVo = dlg.getEditor().getDataset();
				// newVo.setPk_datasetdir(dirVo.getPk_datasetdir());
				
				//modified by biancm 20091104 �־û��Ѿ�������򵼽���ʱִ�й�DataSetDesignWizardListPn.doComplate()
//				try {
//					newVo = DataSetDefBO_Client.createDataSetDef(newVo);
//				} catch (UFOSrvException ex) {
//					AppDebug.debug(ex);
//				}
				dsManager.addDataSetDef(dsManager.getCurrentDir(), newVo);
			}else{
				dsManager.setCurrentDir(null);
			}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetDefDescriptor.this.getContainer(), ex);
			}

			return null;
		}


		@Override
		public UfoCommand getCommand() {
			return null;
		}

		/**
		 * @i18n uiufofurl530001=�½�
		 * @i18n miufo00241=���ݼ�
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(providerName);
			uiDes1.setImageFile("biplugin/same_generation.gif");
			uiDes1.setPaths(new String[] {StringResource.getStringResource("uiufofurl530001"), StringResource.getStringResource("miufo00241") });
			return new ActionUIDes[] { uiDes1 };
		}
		
		public Object clone(){
			//�����ı��
			return new NewDataSetExt(this.providerClass,this.providerName);
		}

	}
	

	private Context createContext() {
		Context context=null;
		
		if (getContainer() instanceof UfoReport) {
			// ����UfoReport������
			UfoReport r = (UfoReport) getContainer();
//			liuyy+
			context = ContextFactory.createContext(r); 
		}
		return context;
	}
	
	

	public class EditExt extends DSMActionExt {

		/**
		 * @i18n miufo1000155=����
		 * @i18n miufo01098=˽�����ݼ�Ŀ¼�����޸ġ�
		 * @i18n miufo01099=��Ŀ¼���ܱ��޸ġ�
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{
			DataSetDefVO[] selectedVo = dsManager.getCurrentDataSetDef();
			// �����ǰ���������ݼ������ϣ��޸����ݼ�����
			if (selectedVo.length > 0) {
				//liuyy+
				Context context = createContext();
				ContextFactory.initNCContext(context);
//				if (getContainer() instanceof UfoReport) {
//					context = new UfoReportContext((UfoReport) getContainer());
//				}
				DataSetDefVO cloneVo = (DataSetDefVO) selectedVo[0].clone();
				// zjb��
				// DataSetEditor editor = new DataSetEditor(cloneVo, context);
				// DataSetEditDlg dlg = new DataSetEditDlg(getContainer(),
				// editor);
				// ׼�����ݼ�������ݽṹ
				DataSetDesignObject dsdo = new DataSetDesignObject();
				dsdo.setCurDataSetDef(cloneVo);
				dsdo.setContext(context);
				dsdo.setShowTree(false);
				dsdo.setSaveWhenFinished(true);
				dsdo.setStatus(DataSetDesignObject.STATUS_UPDATE);
				// ���б����
				String providerClass = cloneVo.getDataSetDef().getProvider()
						.getClass().getName();
				AbstractWizardListPanel listPn = DesignWizardFactory
						.createDataSetWizard(dsdo, providerClass);
				//�����޸�ʱ��Context
				if(dsdo.getCurDataSetDef().getDataSetDef()!=null 
						&& dsdo.getCurDataSetDef().getDataSetDef().getProvider()!=null){
					dsdo.getCurDataSetDef().getDataSetDef().getProvider().setContext(context);
				}
				
				// ����
				Dimension dm = new Dimension(1024,730);
				UIDialog dlg = showWizard(listPn, dm,dsManager,dsdo);
				if (dlg != null && dlg.getResult() == UIDialog.ID_OK) {
					DataSetDefVO newVo = dsdo.getCurDataSetDef();
					
					//modified by biancm 20091104 �־û��Ѿ�������򵼽���ʱִ�й�DataSetDesignWizardListPn.doComplate()
//					try {
//						DataSetDefBO_Client.updateDataSetDef(newVo);
//					} catch (UFOSrvException ex) {
//						AppDebug.debug(ex);
//					}
					dsManager.updateDataSetDef(newVo);
				}else{
					dsManager.setCurrentDir(null);
				}

				return null;
			}

			DataSetDirVO dirVo = dsManager.getCurrentDir();
			// ��ǰ������Ŀ¼���ϣ��޸�Ŀ¼
			if (dirVo != null) {
				//yza+ 2008-6-16 ˽�����ݼ����ܽ���Ŀ¼
				if(dirVo.isPrivate())
				{
					MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo01098"));
					return null;
				}
				if(dirVo.getPk_parentdir() == null ||
						dirVo.getPk_parentdir().trim().length() == 0)
				{
					MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo01099"));
					return null;
				}
				InputDirNameDlg dlg = new InputDirNameDlg(getContainer(), dirVo
						.getPk_parentdir(), dirVo);
				dlg.setDirName(dirVo.getName());
//				dlg.setCode(dirVo.getCode());
				dlg.setRemark(dirVo.getRemark());
				if (dlg.showModal() == UIDialog.ID_OK) {
					dirVo.setName(dlg.getDirName());
//					dirVo.setCode(dlg.getCode());
					dirVo.setRemark(dlg.getRemark());
					dsManager.updateDir(dirVo);
				}
			}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetDefDescriptor.this.getContainer(), ex);
			}
			return null;

		}

		@Override
		public UfoCommand getCommand() {

			return null;
		}

		/**
		 * @i18n miufo1001396=�޸�
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("miufo1001396"));
			uiDes1.setImageFile("reportcore/pageset.gif");
			uiDes1.setPaths(new String[] { MultiLang.getString("edit") });

			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			return new ActionUIDes[] { uiDes1, uiDes2 };

		}
	}

	public class RemoveExt extends DSMActionExt {

		/**
		 * @i18n miufo1000775=ȷ��
		 * @i18n miufo01100=��ȷ��Ҫɾ��������ݼ�������
		 * @i18n miufo1000155=����
		 * @i18n miufo01101=˽�����ݼ�Ŀ¼����ɾ����
		 * @i18n miufo1000845=��ʾ
		 * @i18n miufo01102=Ŀ¼û����գ�����ɾ����
		 * @i18n miufo01103=��ȷ��Ҫɾ�����Ŀ¼��
		 * @i18n miufo01104=ɾ��ʧ�ܡ�
		 */
		@Override
		public Object[] getParams(DataSetManager dsManager) {
			try{

			DataSetDefVO[] selectedVo = dsManager.getCurrentDataSetDef();
			// �����ǰ���������ݼ������ϣ�ɾ�����ݼ�����
			if (selectedVo.length > 0) {
				if (MessageDialog.showYesNoDlg(getContainer(), StringResource.getStringResource("miufo1000775"), StringResource.getStringResource("miufo01100")) == MessageDialog.ID_YES) {
					for (DataSetDefVO vo : selectedVo) {
						dsManager.removeDataSetDef(vo);
					}
				}
				return null;
			}

			DataSetDirVO dirVo = dsManager.getCurrentDir();
			// ��ǰ������Ŀ¼���ϣ�ɾ��Ŀ¼
			if (dirVo != null) {
				try {
					//˽�����ݼ�Ŀ¼���ܱ�ɾ�� yza+ 2008-6-17
					if(dirVo.isPrivate())
					{
						MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000155"), StringResource.getStringResource("miufo01101"));
						return null;
					}
					if (DataSetDefBO_Client.hasChild(dirVo)) {
						MessageDialog.showHintDlg(getContainer(), StringResource.getStringResource("miufo1000845"), StringResource.getStringResource("miufo01102"));
					} else if (MessageDialog.showYesNoDlg(getContainer(), StringResource.getStringResource("miufo1000775"), StringResource.getStringResource("miufo01103")) == MessageDialog.ID_YES) {
						if (!dsManager.removeDir(dirVo)) {
							MessageDialog.showErrorDlg(getContainer(), StringResource.getStringResource("miufo1000845"), StringResource
											.getStringResource("miufo01104"));
						}
					}
				} catch (UFOSrvException e1) {

				}
			}
			}catch(Exception ex){
				AppDebug.debug(ex);
				UfoPublic.sendErrorMessage(MultiLang.getString("error"), DataSetDefDescriptor.this.getContainer(), ex);
			}
			return null;
		}

		@Override
		public UfoCommand getCommand() {
			return null;
		}

		/**
		 * @i18n ubichart00006=ɾ��
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(StringResource.getStringResource("ubichart00006"));
			uiDes1.setImageFile("reportcore/delete.gif");
			uiDes1.setPaths(new String[] { MultiLang.getString("edit") });

			ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
			uiDes2.setToolBar(true);
			uiDes2.setPaths(null);
			return new ActionUIDes[] { uiDes1, uiDes2 };

		}

	}

	public Container getContainer() {
		return ((DataSetDefPlugin) getPlugin()).getContainer();
	}
	
	class WindowHandler extends WindowAdapter{
		
		private DataSetDesignObject dsdo = null;
		private DataSetManager dsManager = null;
		private AbstractWizardListPanel listPn = null;
		
		public WindowHandler(DataSetDesignObject dsdo,
				DataSetManager dsManager,AbstractWizardListPanel listPn){
			this.dsdo = dsdo;
			this.dsManager = dsManager;
			this.listPn = listPn;
		}
		
		/**
		 * @i18n miufo1000775=ȷ��
		 * @i18n miufo01105=���ݼ��Ѿ��ı�,�Ƿ񱣴棿
		 */
		public void windowClosing(java.awt.event.WindowEvent e) {
			DataSetDesignWizardListPn dataSetPanel = (DataSetDesignWizardListPn) listPn
					.getContainerPanel().getListPn();
			if (dataSetPanel.isDirty()) {
				// ���ݼ���Ϣ�Ѿ�����
				if (MessageDialog.showYesNoDlg(getContainer(), StringResource.getStringResource("miufo1000775"), StringResource.getStringResource("miufo01105")) == MessageDialog.ID_YES) {
					if(dataSetPanel.doComplate() == false){
						return;
					}
					//listPn.getContainerPanel().getListPn();
//					DataSetDefVO[] vos = dsManager.getCurrentDataSetDef();
//					try {
//						DataSetDefBO_Client.updateDataSetDef(vos[0]);
//					} catch (UFOSrvException ex) {
//						AppDebug.debug(ex);
//					}
					
					//�������ݼ��б�״̬
					DataSetDefVO dsVo=dataSetPanel.getDatasetDef();
					int status = dsdo.getStatus();
					if(status == DataSetDesignObject.STATUS_CREATE){
						dsManager.addDataSetDef(dsManager.getCurrentDir(), dsVo);
					}else{
						dsManager.updateDataSetDef(dsVo);
					}
					
				}
			}
		}
	}

	/**
	 * ��ʾ�򵼶Ի��� �������ڣ�(2002-8-19 11:21:02)
	 */
	public UIDialog showWizard(AbstractWizardListPanel listPn, Dimension dim,
			DataSetManager dsManager,DataSetDesignObject dsdo) {
		final AbstractWizardTabPn tabPn = listPn.getWizardTabPn();
		// ����
		WizardContainerDlg dlg = new WizardContainerDlg(getContainer());
		dlg.addWindowListener(new WindowHandler(dsdo,dsManager,listPn));
		AbstractWizardStepPanel[] panels = listPn.getStepPanels();
		if (panels != null && panels.length >=2 && panels[1] == null) return null;
		if (dim != null) {
			dlg.setSize(dim);
		}
		dlg.setListPn(listPn);
		dlg.setTabPn(tabPn);
		dlg.showModal();
		dlg.destroy();
		return dlg;
	}
}
  