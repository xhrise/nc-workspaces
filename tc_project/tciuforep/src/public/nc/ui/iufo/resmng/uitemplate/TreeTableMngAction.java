/**
 * ResMngAction.java  1.0 
 * 
 * WebDeveloper自动生成.
 * 
 */
package nc.ui.iufo.resmng.uitemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import nc.imp.tc.imp.QueryList;
import nc.itf.tc.imp.IQueryList;
import nc.ui.iufo.exproperty.ExPropDataEditHelper;
import nc.ui.iufo.exproperty.ExPropUIBizHelper;
import nc.ui.iufo.function.FunctionRightHandler;
import nc.ui.iufo.function.IFuncForm;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.ResUIBizHelper;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iufo.resmng.uitemplate.describer.ModuleProductHome;
import nc.ui.iufo.resmng.uitemplate.describer.ResMngHome;
import nc.ui.iufo.resmng.uitemplate.describer.TableHeaderInfo;
import nc.util.iufo.product.ProductBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.iufo.resmng.loader.IModuleLoaderParam;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.vo.iufo.exproperty.ExPropListVO;
import nc.vo.iufo.resmng.uitemplate.ResOperException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.RequestUtils;
import com.ufida.web.action.WebWindow;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.Sort;
import com.ufida.web.comp.table.WebTableColumn;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.IWebTreeModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.html.Element;
import com.ufida.web.html.Script;
import com.ufida.web.html.StringElement;
import com.ufida.web.window.WebStatusBar;
import com.ufsoft.iufo.web.MultiFrameAction;

/**
 * 类作用描述文字 liulp 2005-12-02
 */
public class TreeTableMngAction extends MultiFrameAction {

	/**
	 * <MethodDescription> liulp 2005-12-02
	 */
	public ActionForward execute(ActionForm actionForm) throws WebException {
		ActionForward actionForward = null;

		// USERCODE
		// #Session没有模块的ResWebParam信息时候，先保存resOwner通LoginUserPK生成的ResWebParam到Session中
		if (!hasModuleInfoInSession()) {
			setModuleInfoToSession();
		}

		ResWebParam resWebParam = ResUIBizHelper.getModuleInfoFromSession(this);

		TreeTableMngForm treeTableMngForm = (TreeTableMngForm) actionForm;
		treeTableMngForm.setStrModuleID(resWebParam.getModuleID());
		treeTableMngForm.setStrResOwnerPK(resWebParam.getResOwnerPK());
		treeTableMngForm.setStrOperUserPK(resWebParam.getOperUserPK());
		// by fdh
		// QueryList lit=new QueryList();
		// try {
		// lit.getAllxmlList();
		// } catch (SAXException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// File file=new File("C:/Ufida_IUFO/jieshou/123.xml");
		// //目录 主键 名称 文件
		// lit.SaveXMLRep("白痴", "编码", "名字","说明", file);

		// 设置菜单的权限信息
		IFuncForm funcForm = (IFuncForm) actionForm;
		String strFuncNodeOrder = ResMngHome.getInstance().getFuncNodeOrder(
				resWebParam.getModuleID());
		List<String> listRightedFuncs = FunctionRightHandler.getRightedFuncs(
				getCurUserInfo().getID(), strFuncNodeOrder);
		funcForm.setRightedFuncs(listRightedFuncs);

		String strModuleMngUIName = getModuleMngUIName();
		actionForward = new ActionForward(strModuleMngUIName);

		return actionForward;
	}

	/**
	 * 检查是否已有ModuleInfo在Session中
	 * 
	 * @return
	 */
	private boolean hasModuleInfoInSession() {
		ResWebParam resWebParam = ResWebEnvKit.getModuleInfoFromSession(this);
		if (resWebParam == null) {
			return false;
		}
		return true;
	}

	/**
	 * 需要子类继承 获得模块管理UI的类名称
	 * 
	 * @return
	 */
	private String getModuleMngUIName() {
		return ResMngHome.getInstance().getMngUIName(getModuleID());
	}

	/**
	 * 子类覆盖。
	 * 
	 * @return
	 */
	private String getModuleID() {
		String strModuleID = ResWebEnvKit.getModuleID(this);
		return strModuleID;
	}

	/**
	 * <MethodDescription> liulp 2005-12-02
	 */
	public ActionForward removeAct(ActionForm actionForm) throws WebException {
		ActionForward actionForward = null;

		actionForward = new ActionForward(TreeTableMngAction.class.getName(),
				"");

		return actionForward;
	}

	public IWebTreeModel getTreeModel() {

		// USERCODE
		// #Session没有模块的ResWebParam信息时候，先保存resOwner通LoginUserPK生成的ResWebParam到Session中
		if (!hasModuleInfoInSession()) {
			setModuleInfoToSession();
		}

		IWebTreeModel treeModel = new WebTreeModel();
		// set treeModel's nodes
		// USERCODE begin
		ResWebParam resWebParam = getModuleInfoFromSession();
		IWebTreeLoader treeLoader = TreeTableOperFactory
				.getTreeTableOper(resWebParam);
		try {
			boolean bHaveResMngRight = AuthUIBizHelper.haveResMngRight(this);
			LoaderParam loaderParam = new LoaderParam(resWebParam
					.getResOwnerPK(), bHaveResMngRight, -1, false);
			IModuleLoaderParam moduleLoaderParam = getModuleLoaderParam();
			treeModel = (WebTreeModel) treeLoader.loadTreeModel(false, false,
					loaderParam, moduleLoaderParam);
		} catch (ResOperException e) {
			throw new WebException(e.getMessage());
		}
		// end
		return treeModel;
	}

	/**
	 * @return
	 * @throws UISrvException
	 */
	private ResWebParam getModuleInfoFromSession() {
		return ResUIBizHelper.getModuleInfoFromSession(this);
	}

	private void setModuleInfoToSession() {
		ResUIBizHelper.setModuleInfoToSession(this);
	}

	/**
	 * 得到模块的装载参数 子类可覆盖
	 * 
	 * @return
	 */
	protected IModuleLoaderParam getModuleLoaderParam() {
		return null;
	}

	public IWebTableModel getTableModel() {

		// USERCODE
		// #Session没有模块的ResWebParam信息时候，先保存resOwner通LoginUserPK生成的ResWebParam到Session中
		if (!hasModuleInfoInSession()) {
			setModuleInfoToSession();
		}

		WebTableModel tableModel = new WebTableModel();
		// set tableModel's data
		// USERCODE begin
		// 得到选择的树ID
		String strSelDirTreeObjID = getTreeSelectedID();
		if (strSelDirTreeObjID != null && strSelDirTreeObjID.length() > 0) {
			IWebTableLoader tableLoader = WebTableLoaderFactorty
					.getWebTableLoader(this);
			try {
				tableModel = (WebTableModel) tableLoader
						.loadTableModel(strSelDirTreeObjID);
			} catch (ResOperException e) {
				AppDebug.debug(e);// @devTools e.printStackTrace(System.out);
				throw new WebException(e.getMessage());
			}

			// //设置需要选中的表行
			// String strSelectedTableObjID =
			// getModuleInfoFromSession().getSelTableObjID();
			// boolean bSetSelected = setSelected(tableModel,
			// strSelectedTableObjID);
			// if(!bSetSelected && strSelectedTableObjID!=null){
			// ResUIBizHelper.clearSelTableID2Session(this);
			// }
		}
		// end

		// sort datas by server
		boolean bSortByServer = ResMngHome.getInstance().isTableSortByServer(
				getModuleID());
		if (bSortByServer) {
			tableModel.setSortMode(IWebTableModel.SORT_SERVER);
			Sort sort = getTableSort();
			if (sort != null && sort.getColumnName() != null
					&& sort.getColumnName().length() > 0) {
				tableModel.setSort(sort);
				tableModel.sort();
			}
		}
		// 设置分页选中 yp
		String strSelectedTableObjID = ResUIBizHelper
				.getSelTableIDFromSession(this);
		if (strSelectedTableObjID != null) {
			int iPage = 0;
			for (int i = 0; i < tableModel.getDatas().length; i++) {
				if (strSelectedTableObjID.equals(tableModel.getDatas()[i][0])) {
					int iRowNum = getRowNumPerPage();
					iPage = i / iRowNum + 1;
					break;
				}
			}
			if (iPage != 0) {
				addSessionObject("page", iPage);
			}
		}
		return tableModel;
	}

	/**
	 * 新建分页
	 * 
	 * @author yp
	 */
	@Override
	public int getPage() {

		String strPage = getRequestParameter("page");
		int page = -1;
		try {
			page = Integer.parseInt(strPage);
		} catch (Exception e) {
		}
		if (page == -1) {
			Object pageObj = getSessionObject("page");
			if (pageObj != null) {
				page = Integer.parseInt(pageObj.toString());
				removeSessionObject("page");
			}
		}
		if (page < 1) {
			page = 1;
		}
		return page;

	}

	/**
	 * 设置需要选中的表行
	 * 
	 * @param tableModel
	 * @param strSelectedTableObjID
	 */
	public static boolean setSelected(IWebTableModel tableModel,
			String strSelectedTableObjID) {
		if (strSelectedTableObjID == null
				|| strSelectedTableObjID.length() <= 0 || tableModel == null
				|| tableModel.getSelectMode() == WebTableModel.NO_SELECTION) {
			return false;
		}

		int nSelectedRow = 0;
		Object[][] datas = tableModel.getDatas();
		if (datas != null && datas.length > 0) {
			for (int i = 0, iSize = datas.length; i < iSize; i++) {
				String strTableObjID = (String) datas[i][0];
				if (strTableObjID.equals(strSelectedTableObjID)) {
					nSelectedRow = i + 1;
					break;
				}
			}
		}
		if (nSelectedRow > 0) {
			tableModel.setSelected(nSelectedRow);
			return true;
		}
		return false;
	}

	// 表头栏信息
	public Object[] getTableColumns() {
		// int colNum =2;
		// Object[] columns = new Object[colNum];
		// WebTableColumn column = null;
		// int index = -1;
		// index++;

		// 设置表头名称信息
		boolean bAppedExProp = ModuleProductHome.getInstance().isAppedExprop(
				getModuleID());
		if (!bAppedExProp) {
			return doGetTableCols(getModuleID());
		} else {
			String strAppExPropModuleID = ModuleProductHome.getInstance()
					.getAppExpropModuleID(getModuleID());
			ExPropListVO exPropListVO = ExPropUIBizHelper.getExPropList(
					strAppExPropModuleID, getCurUserInfo().getID());
			if (exPropListVO == null) {
				exPropListVO = ExPropDataEditHelper
						.getDefaultExPropList(strAppExPropModuleID);
			}
			return ExPropDataEditHelper.getColumns(exPropListVO);
		}
	}

	/**
	 * 状态栏重写
	 * 
	 * @param actionForm
	 * @return
	 */
	public WebStatusBar getStatusBar(ActionForm actionForm) {
		if (!ModuleProductHome.getInstance().isAuthShareModule(getModuleID())) {
			return super.getStatusBar(actionForm);
		}

		WebStatusBar bar = new WebStatusBar();
		if (ResMngHome.getInstance().isUnitResOwnerType(getModuleID())) {
			ResWebParam resWebParam = getModuleInfoFromSession();
			String strResOwnerDisName;
			try {
				strResOwnerDisName = ProductBizHelper
						.getUnitDisName(resWebParam.getResOwnerPK());
			} catch (ResOperException e) {
				AppDebug.debug(e);// @devTools e.printStackTrace(System.out);
				throw new WebException(e.getExResourceId());
			}
			bar.appendStatus(TableHeaderInfo.getValue("uiufo50resmng010")
					+ ": " + strResOwnerDisName, "");
		}
		bar.appendStatus(TableHeaderInfo.getValue("uiufo50resmng011") + ": "
				+ getCurUserInfo().getNameWithCode(), "");
		return bar;
	}

	public ActionForward ajaxTable(ActionForm actionForm) {
		// 共享根目录时，右页面为空白页面
		String strSelDirTreeObjID = getTreeSelectedID();
		if (!ResMngToolKit.isSharedRootTreeObj(strSelDirTreeObjID)
				&& !ResMngToolKit.isVitualRootDir(strSelDirTreeObjID)) {
			return super.ajaxTable(actionForm);
		} else {
			// @edit by yp at 2008 十二月 29,12:19:29根节点被选中时，将选中的值置为空
			Script spt = new Script();
			spt.addFuncLine("document.getElementById('TableSelectedID').value = ''");
			WebButton sad = new WebButton();
			sad.setStyle("display:none");
			sad.setOnClick("sdfsd");
			ajax(sad + spt.toString());
			return null;
		}

	}

	public static WebTableColumn[] doGetTableCols(String strModuleID) {
		if (strModuleID == null) {
			return null;
		}
		String[] strColumnNameValues = ResMngHome.getInstance()
				.getTableHeadersName(strModuleID);
		String[] strColumnCodes = ResMngHome.getInstance().getTableHeadersCode(
				strModuleID);
		WebTableColumn[] webTableCols = TableHeaderInfo.createWebTableColumns(
				strColumnNameValues, strColumnCodes);
		return webTableCols;
	}

	/**
	 * ajax获取菜单 liuyy. 2006-07-27
	 */
	public Element getMenu(ActionForm form) {
		try {
			if (!hasModuleInfoInSession()) {
				setModuleInfoToSession();
			}
			// by fdh
			QueryList lit = new QueryList();
			try {
				lit.getAllxmlList();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResWebParam resWebParam = ResUIBizHelper
					.getModuleInfoFromSession(this);

			TreeTableMngForm treeTableMngForm = (TreeTableMngForm) form;
			treeTableMngForm.setStrModuleID(resWebParam.getModuleID());
			treeTableMngForm.setStrResOwnerPK(resWebParam.getResOwnerPK());
			treeTableMngForm.setStrOperUserPK(resWebParam.getOperUserPK());

			String strModuleMngUIName = getModuleMngUIName();
			WebWindow ui = (WebWindow) RequestUtils
					.applicationInstance(strModuleMngUIName);
			ui.processWindowInitial();
			return ui.getMenubar();

		} catch (Throwable e) {
			AppDebug.debug(e);
			return new StringElement(e.getMessage());
		}

	}

	/**
	 * 获取Action对应的form名称 liuyy. 2005-5-24
	 * 
	 * @return
	 */
	public String getFormName() {
		return TreeTableMngForm.class.getName();
	}

}