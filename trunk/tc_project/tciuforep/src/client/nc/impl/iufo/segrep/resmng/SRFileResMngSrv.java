/*
 * 创建日期 2006-7-4
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SRFileResMngSrv implements ISrptFileResMngSrv {
	
	/* （非 Javadoc）
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getFileTreeObj(nc.vo.pub.ValueObject, java.lang.String, java.lang.String)
	 */
	public IResTreeObject getFileTreeObj(ValueObject valueObject,
			String strModuleID, String strShareFlag) {
		if( valueObject == null || strModuleID == null ){
			return null;
		}
		
		//应该只有一个模块使用目录管理
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

	/* （非 Javadoc）
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getModuleDescriptor(java.lang.String)
	 */
	public ModuleDescriptor getModuleDescriptor(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return  getSegDefDescriptor();
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getTreeLoader(java.lang.String)
	 */
	public ITreeLoader getTreeLoader(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return ResTreeLoader.getResTreeLoader(strModuleID);
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getIFiled(java.lang.String)
	 */
	public IFiled getIFiled(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return SegDefSrv.getInstance();
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see nc.itf.iufo.resmng.IFileResMngSrv#getIDirectoried(java.lang.String)
	 */
	public IDirectoried getIDirectoried(String strModuleID) {
		if( strModuleID.equals(ISRResMngConstants.MODULE_SEGREP_DEF)){
			return SegDefSrv.getInstance();
		}
		return null;
	}

	/* （非 Javadoc）
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
		//模块管理UI类名
		String strModuleMngUI = SegDefMngUI.class.getName();
		moduleDescriptor.setModuleMngUI(strModuleMngUI);
		//是否有“权限”菜单组
		moduleDescriptor.setIsHaveAuthorize(true);
		//模块显示名称
		moduleDescriptor.setModuleDisNameCode(ISRResMngConstants.RESOURCE_ID_OF_MODULE_SEGREP);//分部报告
		//资源拥有者类型
		moduleDescriptor.setResOwnerType(ModuleDescriptor.SYS_RES_OWNER);
		//目录名称
		moduleDescriptor.setDirNameCode(ISRResMngConstants.RESOURCE_ID_OF_MENUITEM_SEGREP_DIR);
		//文件名称
		moduleDescriptor.setFileNameCode(ISRResMngConstants.RESOURCE_ID_OF_MENUITEM_SEGREP_DEF);
		//模块功能节点标识
		moduleDescriptor.setFuncNodeName(ModuleDescriptor.FUNCNODENAME_SR_SEGDEF);//分部划分管理
		moduleDescriptor.setFuncNodeOrder(IFuncOrderFlag.HBBB_SEGREP_MNG);//"分部划分管理"功能节点的funcOrder
		//表头名称
		String[] strHearderNames =  new String[] {
                TableHeaderInfo.RESOURCE_ID_OF_SEGNAME,
                TableHeaderInfo.RESOURCE_ID_OF_SEGINPUTREP,
                TableHeaderInfo.RESOURCE_ID_OF_SEGREP};
			
		moduleDescriptor.setTableHeader(strHearderNames);
		//目录中文件是否可同名
		moduleDescriptor.setIsSameFileNameInDir(false);

		//操作菜单 
	    ResMenuInfo[] editDirMenuInfos = ResMenuToolkit.getMenusEdit(
	                strModuleID, ResEditDirAction.class.getName(), false, true);
	    moduleDescriptor.setEditDirMenuInfos(editDirMenuInfos);
	    // 编辑文件
	    ResMenuInfo[] editFileMenuInfos = ResMenuToolkit.getMenusEdit(
	                strModuleID, SegDefEditAction.class.getName(), true, true);
	    moduleDescriptor.setEditFileMenuInfos(editFileMenuInfos);

		//编辑菜单
	    // 复制移动目录
        ResMenuInfo[] mcDirMenuInfos = ResMenuToolkit.getMenusMoveCopy(
                strModuleID, false, false, null);
        moduleDescriptor.setMCDirMenuInfos(mcDirMenuInfos);
        // 复制移动文件
        ResMenuInfo[] mcFileMenuInfos = ResMenuToolkit.getMenusMoveCopy(
                strModuleID, false, true, ResMoveCopyAction.class.getName());//TODO 
        moduleDescriptor.setMCFileMenuInfos(mcFileMenuInfos);
        moduleDescriptor.setIsSeperateMenu(true);


		//目录的数据存储表名
		moduleDescriptor.setDBTableNames(new String[] {
				IDatabaseNames.SEGREP_SEGDEF_DIR, 
				"",
				"",
				IDatabaseNames.SEGREP_SEGDEF_AUTH,});//有问题
		//模块URL???
		moduleDescriptor.setModuleURL(ResWebEnvKit.getModuleURL4Dir(strModuleID));
		
		return moduleDescriptor;

	}

}
