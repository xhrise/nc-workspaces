/*
 * �������� 2006-7-4
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.impl.iufo.segrep.resmng;


import nc.itf.iufo.resmng.ISrptFileResMngSrv;
import nc.ui.iufo.function.IFuncOrderFlag;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iufo.resmng.uitemplate.ResEditDirAction;
import nc.ui.iufo.resmng.uitemplate.ResMoveCopyAction;
import nc.ui.iufo.resmng.uitemplate.describer.ModuleDescriptor;
import nc.ui.iufo.resmng.uitemplate.describer.ModuleProductInfo;
import nc.ui.iufo.resmng.uitemplate.describer.ResMenuInfo;
import nc.ui.iufo.resmng.uitemplate.describer.ResMenuToolkit;
import nc.ui.iufo.resmng.uitemplate.describer.TableHeaderInfo;
import nc.ui.segrep.segdef.SegDefEditAction;
import nc.ui.segrep.segdef.SegDefMngUI;
import nc.us.segrep.segdef.SegDefSrv;
import nc.util.iufo.resmng.IDirectoried;
import nc.util.iufo.resmng.IFiled;
import nc.util.iufo.resmng.loader.ITreeLoader;
import nc.util.iufo.resmng.loader.ResTreeLoader;
import nc.util.segrep.resmng.ISRResMngConstants;
import nc.vo.iufo.pub.IDatabaseNames;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zyjun
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class SRFileResMngSrv implements ISrptFileResMngSrv {
	
	/* ���� Javadoc��
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getFileTreeObj(nc.vo.pub.ValueObject, java.lang.String, java.lang.String)
	 */
	public IResTreeObject getFileTreeObj(ValueObject valueObject,
			String strModuleID, String strShareFlag) {
		if( valueObject == null || strModuleID == null ){
			return null;
		}
		
		//Ӧ��ֻ��һ��ģ��ʹ��Ŀ¼����
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			IFiled				filedImpl = SegDefSrv.getInstance();
			if( filedImpl != null ){
				try{
					ValueObject[]		valueObjs = new ValueObject[]{valueObject};
					IResTreeObject[]	objs = filedImpl.changetoFileTreeObjs(valueObjs, strShareFlag);
					return objs[0];
				}catch(UISrvException e){
					AppDebug.debug(e);
				}
			}
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getModuleDescriptor(java.lang.String)
	 */
	public ModuleDescriptor getModuleDescriptor(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return  getSegDefDescriptor();
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getTreeLoader(java.lang.String)
	 */
	public ITreeLoader getTreeLoader(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return ResTreeLoader.getResTreeLoader(strModuleID);
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getIFiled(java.lang.String)
	 */
	public IFiled getIFiled(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return SegDefSrv.getInstance();
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getIDirectoried(java.lang.String)
	 */
	public IDirectoried getIDirectoried(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return SegDefSrv.getInstance();
		}
		return null;
	}

	/* ���� Javadoc��
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getRootDirDisName(java.lang.String, java.lang.String, boolean)
	 */
	public String getRootDirDisName(String strModuleID, String strResOwnerPK,
			boolean selfRoot) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			try{
				return SegDefSrv.getInstance().getRootDirDisName(strResOwnerPK, selfRoot);
			}catch(UISrvException e){
				AppDebug.debug(e);
			}
		}
		return null;
	}
	
	private ModuleDescriptor  getSegDefDescriptor(){
		String strModuleID = ISRResMngConstants.MODULE_SEGREP_DEF;
		ModuleDescriptor moduleDescriptor = new ModuleDescriptor(strModuleID);
		moduleDescriptor.setComponentFlag(ModuleProductInfo.COMPONENT_SRPT);
		//ģ�����UI����
		String strModuleMngUI = SegDefMngUI.class.getName();
		moduleDescriptor.setModuleMngUI(strModuleMngUI);
		//�Ƿ��С�Ȩ�ޡ��˵���
		moduleDescriptor.setIsHaveAuthorize(true);
		//ģ����ʾ����
		moduleDescriptor.setModuleDisNameCode(ISRResMngConstants.RESOURCE_ID_OF_MODULE_SEGREP);//�ֲ�����
		//��Դӵ��������
		moduleDescriptor.setResOwnerType(ModuleDescriptor.SYS_RES_OWNER);
		//Ŀ¼����
		moduleDescriptor.setDirNameCode(ISRResMngConstants.RESOURCE_ID_OF_MENUITEM_SEGREP_DIR);
		//�ļ�����
		moduleDescriptor.setFileNameCode(ISRResMngConstants.RESOURCE_ID_OF_MENUITEM_SEGREP_DEF);
		//ģ�鹦�ܽڵ��ʶ
		moduleDescriptor.setFuncNodeName(ModuleDescriptor.FUNCNODENAME_SR_SEGDEF);//�ֲ����ֹ���
		moduleDescriptor.setFuncNodeOrder(IFuncOrderFlag.HBBB_SEGREP_MNG);//"�ֲ����ֹ���"���ܽڵ��funcOrder
		//��ͷ����
		String[] strHearderNames =  new String[] {
                TableHeaderInfo.RESOURCE_ID_OF_SEGNAME,
                TableHeaderInfo.RESOURCE_ID_OF_SEGINPUTREP,
                TableHeaderInfo.RESOURCE_ID_OF_SEGREP};
			
		moduleDescriptor.setTableHeader(strHearderNames);
		//Ŀ¼���ļ��Ƿ��ͬ��
		moduleDescriptor.setIsSameFileNameInDir(false);

		//�����˵� 
	    ResMenuInfo[] editDirMenuInfos = ResMenuToolkit.getMenusEdit(
	                strModuleID, ResEditDirAction.class.getName(), false, true);
	    moduleDescriptor.setEditDirMenuInfos(editDirMenuInfos);
	    // �༭�ļ�
	    ResMenuInfo[] editFileMenuInfos = ResMenuToolkit.getMenusEdit(
	                strModuleID, SegDefEditAction.class.getName(), true, true);
	    moduleDescriptor.setEditFileMenuInfos(editFileMenuInfos);

		//�༭�˵�
	    // �����ƶ�Ŀ¼
        ResMenuInfo[] mcDirMenuInfos = ResMenuToolkit.getMenusMoveCopy(
                strModuleID, false, false, null);
        moduleDescriptor.setMCDirMenuInfos(mcDirMenuInfos);
        // �����ƶ��ļ�
        ResMenuInfo[] mcFileMenuInfos = ResMenuToolkit.getMenusMoveCopy(
                strModuleID, false, true, ResMoveCopyAction.class.getName());//TODO 
        moduleDescriptor.setMCFileMenuInfos(mcFileMenuInfos);
        moduleDescriptor.setIsSeperateMenu(true);


		//Ŀ¼�����ݴ洢����
		moduleDescriptor.setDBTableNames(new String[] {
				IDatabaseNames.SEGREP_SEGDEF_DIR, 
				"",
				"",
				IDatabaseNames.SEGREP_SEGDEF_AUTH,});//������
		//ģ��URL???
		moduleDescriptor.setModuleURL(ResWebEnvKit.getModuleURL4Dir(strModuleID));
		
		return moduleDescriptor;

	}

}
