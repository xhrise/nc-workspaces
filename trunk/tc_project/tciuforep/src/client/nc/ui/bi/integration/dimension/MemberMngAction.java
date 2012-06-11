/**
 * LogAction.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-11
 */
package nc.ui.bi.integration.dimension;

import com.ufida.iufo.pub.tools.AppDebug;

import java.util.ArrayList;
import java.util.TreeMap;

import nc.bs.logging.Log;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.integration.dimension.IDimension;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.WebTableColumn;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.IWebTreeModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.web.MultiFrameAction;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字 ll 2006-01-11
 */
public class MemberMngAction extends MultiFrameAction {
	static final String METHOD_EXECUTE = "execute";

	static final String METHOD_REMOVE = "remove";

	/**
	 * <MethodDescription>ll 2006-01-11
	 */

	public ActionForward execute(ActionForm actionForm) throws WebException {
		ActionForward fwd = DimUIToolKit.checkDimRefer(this);
		if (fwd == null) {
			// 判断权限，
			String strDimPKWithAuthFlag = getTableSelectedID();
			boolean bModify = DimUIToolKit.hasModifyRight(strDimPKWithAuthFlag, getCurUserInfo());
			String dimID = ResMngToolKit.getVOIDByTreeObjectID(strDimPKWithAuthFlag);
			MemberMngForm form = (MemberMngForm) actionForm;
			form.setDimID(dimID);
			form.setBeModified(bModify);
			try {
				DimensionVO dimVO = DimensionSrv.getInstance().getDimByID(dimID);
				if (dimVO == null) {
					throw new DimensionException(DimensionException.ERR_NO_DIMDEF);
				}
				if(dimVO.getDimensionType().intValue() == IDimension.TIME_DIMENSION_TYPE)
					form.setIsCalendar(true);
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
				throw new WebException(e.getMessage());
			}

			fwd = new ActionForward(MemberMngFrameUI.class.getName());
		}
		return fwd;
	}

	/**
	 * @i18n mbidim00044=根节点不能被删除!
	 */
	public ActionForward remove(ActionForm actionForm) throws WebException {
		try {
			String dimID = getRequestParameter(MemberDesignAction.KEY_DIM_ID);

			DimensionVO dimVO = DimensionSrv.getInstance().getDimByID(dimID);
			if (dimVO == null) {
				throw new DimensionException(DimensionException.ERR_NO_DIMDEF);
			}

			DimMemberSrv srv = new DimMemberSrv(dimVO);

			DimMemberVO[] memVOs = srv.getByID(getTableSelectedIDs());
			for (int i = 0; i < memVOs.length; i++) {
				if (memVOs[i] != null) {
					if (memVOs[i].isRoot() == true) {
						return new ErrorForward(StringResource.getStringResource("mbidim00044"));
					}
					srv.remove(memVOs[i].getMemberID());
				}
			}

		} catch (Exception e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
			throw new WebException(e.getMessage());
		}

		ActionForward actionForward = new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
		return actionForward;
	}

	protected String getTreeDimensionID() {
		return ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());
	}

	/**
	 * 树模型加载 ll 2006-01-11
	 */
	@SuppressWarnings("unchecked")
	public IWebTreeModel getTreeModel() {
		String dimID = getTreeDimensionID();
		return getMemberTreeModel(dimID, false);
	}

	public static WebTreeModel getMemberTreeModel(String dimID, boolean bForRef) {
		DimensionVO dimVO = null;
		try {
			dimVO = DimensionSrv.getInstance().getDimByID(dimID);
		} catch (Exception e) {

			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}
		if (dimVO == null) {
			throw new DimensionException(DimensionException.ERR_NO_DIMDEF);
		}

		DimMemberSrv srv = new DimMemberSrv(dimVO);
		DimMemberVO[] memVOS = srv.getAll();
		// ArrayList parentList = new ArrayList();
		TreeMap<String, ResTreeObject> set = new TreeMap<String, ResTreeObject>();
		if (memVOS != null) {
			String icon = WebGlobalValue.IMAGE_PATH + '/' + "CODEITEM.gif";
			String rootIcon = WebGlobalValue.IMAGE_PATH + '/' + "CODE.gif";

			for (int i = 0; i < memVOS.length; i++) {
				ResTreeObject node = new ResTreeObject();
				node.setLabel(memVOS[i].getLabel());
				node.setID(DimUIToolKit.getMemberTreeID(memVOS[i].getMemberID(), dimID));
				node.setParentID(DimUIToolKit.getMemberTreeID(memVOS[i].getParentID(), dimID));
				if (memVOS[i].isRoot()) {
					node.setICON(rootIcon);
				} else {
					node.setICON(icon);
				}
				node.setSrcVO(memVOS[i]);
				set.put(memVOS[i].getMemcode(), node);
			}
		}

		WebTreeModel treeModel = null;
		ResTreeObject[] tos = (ResTreeObject[]) set.values().toArray(new ResTreeObject[0]);
		if (bForRef)
			treeModel = new WebTreeModel(tos, WebTreeModel.TYPE_REFERENCE);
		else
			treeModel = new WebTreeModel(tos);

		return treeModel;
	}

	/**
	 * 表模型加载 ll 2006-01-11
	 */
	@SuppressWarnings("unchecked")
	public IWebTableModel getTableModel() {
		DimensionVO dimVO = null;
		ExPropertyVO[] exPropVOs = null;
		try {
			String dimID = DimUIToolKit.getDimIDByTreeID(getTreeSelectedID());
			dimVO = DimensionSrv.getInstance().getDimByID(dimID);
			exPropVOs = DimensionSrv.getInstance().getAllExPropertyVOs(dimID);
		} catch (Exception e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}
		DimMemberSrv memSrv = new DimMemberSrv(dimVO);
		String selectedMemID = DimUIToolKit.getMemberIDByTreeID(getTreeSelectedID());
		DimMemberVO[] selectedMems = memSrv.getByID(new String[] { selectedMemID });
		DimMemberVO[] subMems = memSrv.getSubMembers(selectedMemID);
		ArrayList list = new ArrayList();
		if (selectedMems != null) {
			for (int i = 0; i < selectedMems.length; i++) {
				if (selectedMems[i] == null)
					continue;
				list.add(selectedMems[i]);
			}
		}
		if (subMems != null) {
			for (int i = 0; i < subMems.length; i++) {
				if (subMems[i] == null)
					continue;
				list.add(subMems[i]);
			}
		}

		WebTableColumn[] cols = new WebTableColumn[exPropVOs.length];
		cols[0] = new WebTableColumn(DimRescource.MEM_FIXED_FLDNAMES[0]);
		cols[0].setSort(true);
		cols[1] = new WebTableColumn(DimRescource.MEM_FIXED_FLDNAMES[1]);
		int index = 2;
		DimMemberSrv[] memSrvArr = new DimMemberSrv[exPropVOs.length];
		for (int i = 0; i < exPropVOs.length; i++) {
			if (DimRescource.MEM_FIXED_FIELDS[0].equalsIgnoreCase(exPropVOs[i].getDBColumnName())
					|| DimRescource.MEM_FIXED_FIELDS[1].equalsIgnoreCase(exPropVOs[i].getDBColumnName())) {
				continue;
			}
			cols[index] = new WebTableColumn(exPropVOs[i].getName());
			index++;

			// 参照类型
			if (exPropVOs[i].getType() == ExPropertyVO.TYPE_REF) {
				DimensionVO dim;
				try {
					dim = DimensionSrv.getInstance().getDimByID(exPropVOs[i].getRefTypePK());
					memSrvArr[i] = new DimMemberSrv(dim);
				} catch (Exception e) {
					Log.getInstance(this.getClass().getName()).error("Error when get referenced dimension member name");
					AppDebug.debug(e);// @devTools AppDebug.debug(e);
				}
			}
		}

		String[][] datas = new String[0][cols.length + 1];
		if (list.isEmpty() == false) {
			subMems = (DimMemberVO[]) list.toArray(new DimMemberVO[0]);
			TreeMap set = new TreeMap();
			for (int i = 0; i < subMems.length; i++)
				set.put(subMems[i].getMemcode(), subMems[i]);
			subMems = (DimMemberVO[]) set.values().toArray(new DimMemberVO[0]);
			datas = new String[subMems.length][cols.length + 1];
			for (int i = 0; i < datas.length; i++) {
				datas[i][0] = subMems[i].getMemberID();
				datas[i][1] = subMems[i].getPropValue(DimRescource.MEM_FIXED_FIELDS[0]);
				datas[i][2] = subMems[i].getPropValue(DimRescource.MEM_FIXED_FIELDS[1]);
				for (int j = 0; j < exPropVOs.length; j++) {
					if (DimRescource.MEM_FIXED_FIELDS[0].equalsIgnoreCase(exPropVOs[j].getDBColumnName())
							|| DimRescource.MEM_FIXED_FIELDS[1].equalsIgnoreCase(exPropVOs[j].getDBColumnName())) {
						continue;
					}

					datas[i][j + 1] = subMems[i].getPropValue(exPropVOs[j].getDBColumnName());
					if (memSrvArr[j] != null) {// 考虑参照字段
						try {
							String strPropValue = subMems[i].getPropValue(exPropVOs[j].getDBColumnName());
							if (strPropValue != null) {
								DimMemberVO[] vos = memSrvArr[j].getByID(new String[] { strPropValue });
								if (vos != null && vos[0] != null) {
									datas[i][j + 1] = vos[0].getMemname();
								}
							}
						} catch (Exception e) {
							AppDebug.debug(e);// @devTools
												// AppDebug.debug(e);
						}
					}

					if (DimRescource.MEM_CALATTR.equals(exPropVOs[j].getDBColumnName())) {
						datas[i][j + 1] = getCalcRuleString(datas[i][j + 1]);
					}

				}
			}
		}

		WebTableModel tableModel = new WebTableModel(datas, cols);
		return tableModel;
	}

	private String getCalcRuleString(String calcType) {
		if (String.valueOf(DimRescource.INT_CACLRULE_ADD).equalsIgnoreCase(calcType))
			return DimRescource.CACLERULE_ADD;

		if (String.valueOf(DimRescource.INT_CACLRULE_SUB).equalsIgnoreCase(calcType))
			return DimRescource.CACLERULE_SUB;

		return StringResource.getStringResource(DimRescource.CACLERULE_NULL_ID);
	}

	/**
	 * 获取Action对应的form名称 ll 2006-01-11
	 */
	public String getFormName() {
		return MemberMngForm.class.getName();
	}
}
