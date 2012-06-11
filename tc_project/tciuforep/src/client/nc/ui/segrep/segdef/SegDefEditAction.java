/*
 * 创建日期 2006-7-4
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.segrep.segdef.IBSegRepExecService;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.bi.query.manager.QuerySrv;
import nc.us.bi.report.manager.BIReportSrv;
import nc.us.segrep.segdef.SegDefSrv;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.segrep.segdef.SegDefScopeVO;
import nc.vo.segrep.segdef.SegDefVO;
import nc.vo.segrep.segdef.SegRepException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.comp.WebTree2ListModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.comp.tree.WebTreeNode;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

/**
 * @author zyjun
 * 
 * 分部划分编辑Action
 */
public class SegDefEditAction extends DialogAction {
	/**
	 * 新建
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward execute(ActionForm actionForm) {
		if (isCreate()) {
			return create(actionForm);
		} else {
			return update(actionForm);
		}
	}

	public ActionForward create(ActionForm actionForm) {
		// 判断权限
		String strSelectedID = getTreeSelectedID();
		if (ResMngToolKit.isVitualRootDir(strSelectedID)) {
			throw new WebException("miuforesmng0104");// 虚根目录只能创建目录，且必须是系统初始管理员
		}
		AuthUIBizHelper.checkRight(strSelectedID, this);

		SegDefEditForm form = (SegDefEditForm) actionForm;
		form.setDirPK(ResMngToolKit.getVOIDByTreeObjectID(strSelectedID));
		form.setListModel(new WebTree2ListModel());
		return new ActionForward(SegDefEditDlg.class.getName());
	}

	/**
	 * 修改
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward update(ActionForm actionForm) {
		try {
			String strSelectedID = getTableSelectedID();
			AuthUIBizHelper.checkRight(strSelectedID, this);

			SegDefEditForm form = (SegDefEditForm) actionForm;
			// 得到选中的分布划分ID
			String strSegDefPK = ResMngToolKit.getVOIDByTreeObjectID(strSelectedID);
			// 得到SegDefVO
			SegDefVO segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			if (segDefVO != null) {
				setVOToForm(segDefVO, form);
				return new ActionForward(SegDefEditDlg.class.getName());
			}
			return new ErrorForward(StringResource.getStringResource("msrdef0019"));// 分布划分已经删除，不能修改
		} catch (Exception e) {
			return new ErrorForward(e.getMessage());
		}
	}

	/**
	 * 保存
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward save(ActionForm actionForm) {
		try {
			// 判断是新建还是修改
			SegDefEditForm form = (SegDefEditForm) actionForm;
			form.setOrgDimMembers(getOrgMembers());

			if (form.getSegDefPK() == null || form.getSegDefPK().length() == 0) {
				// 新建
				createSegDef(form);
			} else {
				// 修改
				updateSegDef(form);
			}
			return new CloseForward(CloseForward.CLOSE_REFRESH_MAIN);
		} catch (Exception e) {
			return new ErrorForward(e.getMessage());
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufida.web.action.Action#getFormName()
	 */
	public String getFormName() {
		return SegDefEditForm.class.getName();
	}

	/**
	 * 组织维度、对方组织维度、分布依据、对方febu依据不能为空 组织维度、对方组织维度、不能相同 成员不能为空
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void validate(ArrayList errors, ActionForm actionForm) {
		super.validate(errors, actionForm);
		SegDefEditForm form = (SegDefEditForm) actionForm;
		if (form.getSegDefName().getBytes().length > 50) {
			errors.add(StringResource.getStringResource("msrdef0020"));// "分部划分名称长度超过50字节"));
		}
		if (isNull(form.getOrgDimPK())) {
			errors.add(StringResource.getStringResource("msrdef0021"));// 组织维度必须选择
		} else if (form.getOrgDimPK().equals(form.getTradeOrgDimPK())) {
			errors.add(StringResource.getStringResource("msrdef0022"));// 组织维度与对方组织维度不能相同
		}
		if (isNull(form.getTradeOrgDimPK())) {
			errors.add(StringResource.getStringResource("msrdef0024"));// 对方组织维度必须选择
		}

		if (isNull(form.getOrgDimField())) {
			errors.add(StringResource.getStringResource("msrdef0023"));// 分布划分依据必须选择
		} else if (form.getOrgDimField().equals(form.getTradeOrgDimField())) {
			errors.add(StringResource.getStringResource("msrdef0046"));// 增加分布划分依据与对方分部划分依据引用的维度不能相同
		}
		if (isNull(form.getTradeOrgDimField())) {
			errors.add(StringResource.getStringResource("msrdef0025"));// 对方分布划分依据必须选择
		}
		
		String[] strOrgMembers = getOrgMembers();
		if (strOrgMembers == null) {
			errors.add(StringResource.getStringResource("msrdef0026"));// 分布范围必须选择
		}
		if (isNull(form.getSegQueryDirPK())) {
			errors.add(StringResource.getStringResource("msrdef0027"));// 分部对应的查询目录必须选择
		}
		if (isNull(form.getSegReportDirPK())) {
			errors.add(StringResource.getStringResource("msrdef0028"));// 分部报告目录必须选择
		}
		// 判断查询的名称和编码是否重复
		checkQuery(errors, form);
		// 判断报表的名称和编码是否重复
		checkReport(errors, form);
		// 判断分部划分名称是否重复
		String strDirPK = form.getDirPK();
		try {
			SegDefVO oldDefVO = SegDefSrv.getInstance().getSegDefVOByName(strDirPK, form.getSegDefName());
			if (oldDefVO != null && oldDefVO.getSegDefPK().equals(form.getSegDefPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0001"));// "相同名称的分部划分已经存在"
			}
		} catch (SegRepException e) {
			errors.add(e.getMessage());
		}
	}

	/**
	 * 判断查询的名称和编码是否重复
	 * 
	 * @param errors
	 * @param form
	 */
	@SuppressWarnings("unchecked")
	private void checkQuery(ArrayList errors, SegDefEditForm form) {
		try {
			if (form.getSegQueryDirPK().equals(IResMngConsants.VIRTUAL_ROOT_ID)) {
				errors.add(StringResource.getStringResource("msrdef0041"));// 查询目录不能选择虚根目录
			}
			QuerySrv qrySrv = QuerySrv.getInstance();
			QueryModelVO qryVO = qrySrv.getQueryModelVOByCode(form.getSegQueryDirPK(), form.getSegQueryName());
			if (qryVO != null && qryVO.getID().equals(form.getSegReportQueryPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0029"));// "分部划分对应的查询的编码重复"
			}
			qryVO = qrySrv.getQueryModelVOByName(form.getSegQueryDirPK(), form.getSegQueryName());
			if (qryVO != null && qryVO.getID().equals(form.getSegReportQueryPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0030"));// "分部划分对应的查询的名称重复"
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
	}

	/**
	 * 报表名称\编码不能重复
	 * 
	 * @param errors
	 * @param form
	 */
	@SuppressWarnings("unchecked")
	private void checkReport(ArrayList errors, SegDefEditForm form) {
		try {
			if (form.getSegReportDirPK().equals(IResMngConsants.VIRTUAL_ROOT_ID)) {
				errors.add(StringResource.getStringResource("msrdef0042"));// 报表目录不能选择虚根目录
			}
			BIReportSrv repSrv = BIReportSrv.getInstance();
			ReportVO repVO = repSrv.getByCode(form.getSegReportDirPK(), form.getSegReportName());
			if (repVO != null && repVO.getID().equals(form.getSegReportPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0031"));// "分部报表的编码重复");//""
			}
			repVO = repSrv.getByName(form.getSegReportDirPK(), form.getSegReportName());
			if (repVO != null && repVO.getID().equals(form.getSegReportPK()) == false) {
				errors.add(StringResource.getStringResource("msrdef0032"));// ""
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
	}

	/**
	 * 是否为空
	 * 
	 * @param strItem
	 * @return
	 */
	private boolean isNull(String strItem) {
		if (strItem == null || strItem.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询维度修改后，需要重新设置组织维度等信息
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward onQueryChanged(ActionForm actionForm) {
		SegDefEditForm form = (SegDefEditForm) actionForm;
		String strQueryPK = form.getQueryPK();
		try {
			String[][] strDimItems = getDimItems(strQueryPK);
			form.setDimItems(strDimItems);
			// //如果维度不为空,则缺省地设置第一个维度为组织维度和对方组织维度
			// if( strDimItems != null ){
			//				
			// }else{
			form.setOrgDimPK(null);
			form.setTradeOrgDimPK(null);
			form.setSegItemDimPK(null);
			form.setOrgDimField(null);
			form.setTradeOrgDimField(null);
			form.setOrgDimFieldItems(null);
			form.setOrgDimField(null);
			form.setTradeDimFieldItems(null);
			form.setTradeOrgDimField(null);

			// 清除 组织成员选择
			form.setListModel(getTree2ListModel(null, null));

			// 采集查询\分部查询目录\分部报告目录名称必须保持
			setQueryName(form, strQueryPK);
			if (isNull(form.getSegQueryDirPK()) == false) {
				setSegQueryDirName(form, form.getSegQueryDirPK());
			}
			if (isNull(form.getSegReportDirPK()) == false) {
				setSegReportDirName(form, form.getSegReportDirPK());
			}
			return new ActionForward(SegDefEditDlg.class.getName());

		} catch (Exception e) {
			AppDebug.debug(e);
			return new ErrorForward(e.getMessage());
		}
	}

	/**
	 * 组织维度修改后的页面刷新
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward onOrgDimChanged(ActionForm actionForm) {
		SegDefEditForm form = (SegDefEditForm) actionForm;
		// 得到组织维度
		try {
			String strOrgDimPK = form.getOrgDimPK();
			form.setDimItems(getDimItems(form.getQueryPK()));
			// 得到维度字段列表
			form.setOrgDimFieldItems(getDimFieldItems(strOrgDimPK));
			form.setOrgDimField(null);
			form.setListModel(getTree2ListModel(strOrgDimPK, null));

			if (form.getTradeOrgDimPK() != null && form.getTradeOrgDimPK().length() > 0) {
				form.setTradeDimFieldItems(getDimFieldItems(form.getTradeOrgDimPK()));
			}

			setQueryName(form, form.getQueryPK());
			if (isNull(form.getSegQueryDirPK()) == false) {
				setSegQueryDirName(form, form.getSegQueryDirPK());
			}
			if (isNull(form.getSegReportDirPK()) == false) {
				setSegReportDirName(form, form.getSegReportDirPK());
			}

		} catch (Exception e) {
			AppDebug.debug(e);
			return new ErrorForward(e.getMessage());
		}
		return new ActionForward(SegDefEditDlg.class.getName());
	}

	/**
	 * 对方组织维度修改后的页面刷新
	 * 
	 * @param actionForm
	 * @return
	 */
	public ActionForward onTradeDimChanged(ActionForm actionForm) {
		SegDefEditForm form = (SegDefEditForm) actionForm;
		try {
			// 重新设置form中的一些内容
			form.setDimItems(getDimItems(form.getQueryPK()));
			// 得到组织维度
			String strTradeOrgDimPK = form.getTradeOrgDimPK();
			// 设置对方组织维度字段
			form.setTradeDimFieldItems(getDimFieldItems(strTradeOrgDimPK));
			form.setTradeOrgDimField(null);
			form.setOrgDimFieldItems(getDimFieldItems(form.getOrgDimPK()));
			// 需要将提交的
			form.setListModel(getTree2ListModel(form.getOrgDimPK(), getOrgMembers()));

			// 报表、查询的位置名称
			setQueryName(form, form.getQueryPK());
			if (isNull(form.getSegQueryDirPK()) == false) {
				setSegQueryDirName(form, form.getSegQueryDirPK());
			}
			if (isNull(form.getSegReportDirPK()) == false) {
				setSegReportDirName(form, form.getSegReportDirPK());
			}

		} catch (Exception e) {
			AppDebug.debug(e);
			throw new WebException(e.getMessage());
		}
		return new ActionForward(SegDefEditDlg.class.getName());
	}

	/**
	 * 判断操作时新建还是修改
	 * 
	 * @return
	 */
	private boolean isCreate() {
		String strOperType = getRequestParameter("operType");
		if (strOperType.equals("new")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到分部范围选择模型
	 * 
	 * @param strDimPK
	 * @param members
	 * @return
	 * @throws Exception
	 */
	private WebTree2ListModel getTree2ListModel(String strDimPK, String[] members) throws Exception {
		WebTree2ListModel model = new WebTree2ListModel();
		if (strDimPK != null) {
			DimensionVO dimVO = DimensionSrv.getInstance().getDimByID(strDimPK);
			if (dimVO != null) {
				DimMemberSrv memberSrv = new DimMemberSrv(dimVO);
				DimMemberVO[] dimMemberVOs = memberSrv.getAll();
				WebTreeModel treeModel = new WebTreeModel(dimMemberVOs);
				disableNode((WebTreeNode) treeModel.getRoot());
				// model.setSrcTitle();
				model.setTreeModel(treeModel);
				if (members != null) {
					DimMemberVO[] memberVOs = memberSrv.getByID(members);
					String[][] strSelected = new String[memberVOs.length][];
					for (int i = 0; i < memberVOs.length; i++) {
						if (memberVOs[i] != null) {
							strSelected[i] = new String[] { members[i], memberVOs[i].getLabel() };
						}
					}
					model.setDestItems(strSelected);
				}

				// model.setDestTitle(destTitle);
			}
		}
		return model;
	}

	private void disableNode(WebTreeNode node) {
		int nChildren = node.getChildCount();
		if (nChildren > 0) {
			node.setUrl("");
			for (int i = 0; i < nChildren; i++) {
				WebTreeNode child = (WebTreeNode) node.getChildAt(i);
				disableNode(child);
			}
		}
	}

	/**
	 * 维度列表选择内容
	 * 
	 * @param strQueryPK
	 * @return
	 */
	private String[][] getDimItems(String strQueryPK) {
		// 得到查询对应的维度信息
		MetaDataVO[] metaDataVOs = QueryModelSrv.getDimFlds(strQueryPK);
		if (metaDataVOs != null) {
			ArrayList<String[]> aryItems = new ArrayList<String[]>();
			aryItems.add(new String[] { "", "" });
			for (int i = 0; i < metaDataVOs.length; i++) {
				try {
					DimensionVO dimVO = DimensionSrv.getInstance().getDimByID(metaDataVOs[i].getPk_dimdef());
					if (dimVO != null) {
						aryItems.add(new String[] { dimVO.getDimID(), dimVO.getDimname() });
					}
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
			String[][] strDimItems = new String[aryItems.size()][];
			aryItems.toArray(strDimItems);
			return strDimItems;
		}
		return null;
	}

	/**
	 * 得到维度中的引用维度
	 * 
	 * @param strDimPK
	 * @return
	 * @throws Exception
	 */
	private String[][] getDimFieldItems(String strDimPK) throws Exception {
		ExPropertyVO[] propVOs = DimensionSrv.getInstance().getAllExPropertyVOs(strDimPK);
		if (propVOs != null) {
			ArrayList<String[]> aryRefDims = new ArrayList<String[]>();
			for (int i = 0; i < propVOs.length; i++) {
				if (propVOs[i].getType() == ExPropertyVO.TYPE_REF) {
					String strRefDimPK = propVOs[i].getRefTypePK();
					if (strRefDimPK != null) {
						aryRefDims.add(new String[] { strRefDimPK, propVOs[i].getName() });
					}
				}
			}
			if (aryRefDims.size() > 0) {
				String[][] strFieldItems = new String[aryRefDims.size()][];
				aryRefDims.toArray(strFieldItems);
				return strFieldItems;
			}
		}
		return null;
	}

	/**
	 * 设置修改时的初始form
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void setVOToForm(SegDefVO segDefVO, SegDefEditForm form) throws Exception {

		form.setSegDefPK(segDefVO.getSegDefPK());
		form.setSegDefName(segDefVO.getSegDefName());
		form.setDirPK(segDefVO.getDirPK());
		form.setQueryPK(segDefVO.getQueryPK());
		// 查询的名称
		setQueryName(form, segDefVO.getQueryPK());

		String strOrgDimPK = segDefVO.getOrgDimPK();
		form.setDimItems(getDimItems(segDefVO.getQueryPK()));
		form.setOrgDimPK(strOrgDimPK);
		form.setOrgDimFieldItems(getDimFieldItems(strOrgDimPK));

		form.setOrgDimField(segDefVO.getOrgDimField());

		form.setTradeOrgDimPK(segDefVO.getTradeOrgDimPK());
		form.setTradeDimFieldItems(getDimFieldItems(segDefVO.getTradeOrgDimPK()));
		form.setTradeOrgDimField(segDefVO.getTradeOrgDimField());

		// 成员范围
		form.setListModel(getTree2ListModel(strOrgDimPK, segDefVO.getScopeVO().getOrgMemberPKs()));

		// 存储位置
		String strSegQueryPK = segDefVO.getSegReportQueryPK();
		// 得到查询的目录PK和名称
		QueryModelVO qryModelVO = QuerySrv.getInstance().getQueryModelVO(strSegQueryPK);
		if (qryModelVO != null) {
			form.setSegReportQueryPK(strSegQueryPK);
			setSegQueryDirName(form, qryModelVO.getPk_folderID());
			form.setSegQueryDirPK(qryModelVO.getPk_folderID());
			form.setSegQueryName(qryModelVO.getName());
		}

		String strSegRepPK = segDefVO.getSegReportPK();
		// 得到报表的目录和名称
		ReportVO repVO = BIReportSrv.getInstance().getByID(strSegRepPK);
		if (repVO != null) {
			form.setSegReportPK(strSegRepPK);
			setSegReportDirName(form, repVO.getPk_folderID());
			form.setSegReportDirPK(repVO.getPk_folderID());
			form.setSegReportName(repVO.getName());
		}

	}

	/**
	 * 检查分部报告数据是否已经生成
	 * 
	 * @param segDefVO
	 * @return
	 */
	private boolean isSegRepDataExist(SegDefVO segDefVO) throws SegRepException {
		IBSegRepExecService srv = (IBSegRepExecService) NCLocator.getInstance().lookup(
				IBSegRepExecService.class.getName());
		return srv.isSegDataExist(segDefVO);
	}

	/**
	 * 创建查询和查询模型
	 * 
	 * @param form
	 * @return
	 */
	private String createQuery(SegDefEditForm form) throws Exception {

		QueryModelVO queryModelVO = getQueryModelVO(form);
		//校验编码和名称的合法性（同目录下不能重复）
		if(QuerySrv.getInstance().getQueryModelVOByCode(queryModelVO.getPk_folderID(), queryModelVO.getQuerycode()) != null)
			throw new WebException("mbiquery0001");// "查询编码重复
		if(QuerySrv.getInstance().getQueryModelVOByCode(queryModelVO.getPk_folderID(), queryModelVO.getQuerycode()) != null)
			throw new WebException("mbiquery0002");// "查询名称重复

		String strQueryPK = null;
		try {
			strQueryPK = QuerySrv.getInstance().createQueryModelVO(queryModelVO);
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new WebException("msrdef0033");// "无法创建分部报告对应的查询，请检查名称是否重复"
		}
		updateQueryQmd(form, queryModelVO);
		return strQueryPK;

	}

	/**
	 * 创建报表和报表格式
	 * 
	 * @param form
	 * @return
	 */
	private String createReport(SegDefEditForm form) throws Exception {
		ReportVO repVO = getReportVO(form);
		//校验编码和名称的合法性（同目录下不能重复）
		if(BIReportSrv.getInstance().getByCode(repVO.getPk_folderID(), repVO.getReportcode()) != null)
			throw new WebException("mbirep0001");// "报表编码重复";
		if(BIReportSrv.getInstance().getByName(repVO.getPk_folderID(), repVO.getReportname()) != null)
			throw new WebException("mbirep0002");// "报表名称重复";

		repVO.setDefinition(RepModelMaker.getReportModel(form));
		return BIReportSrv.getInstance().createReport(repVO);
	}

	/**
	 * 修改查询的基本信息和查询设计
	 * 
	 * @param form
	 */
	private void updateQuery(SegDefEditForm form, boolean bUpdateModel) throws Exception {
		// 只有在模型或者基本信息都需要修改的时候,才需要修改查询
		// 设置内容
		String strQueryPK = form.getSegReportQueryPK();
		QueryModelVO oldQueryVO = QuerySrv.getInstance().getQueryModelVO(strQueryPK);
		boolean bUpdateBase = false;
		if (oldQueryVO == null) {
			throw new WebException("msrdef0034");// 分部划分对应的查询已经被删除");
		}
		// 比较查询名称，目录是否修改
		if (oldQueryVO.getPk_folderID().equals(form.getSegQueryDirPK()) == false
				|| oldQueryVO.getName().equals(form.getSegQueryName()) == false) {
			bUpdateBase = true;
		}

		if (bUpdateBase || bUpdateModel) {
			QueryModelVO queryVO = getQueryModelVO(form);
			if (bUpdateBase) {
				QuerySrv.getInstance().updateQueryModelVO(queryVO);
			}
			if (bUpdateModel) {
				BIQueryModelDef def = QueryModelMaker.getQueryModelDef(form, queryVO);
				queryVO.setDefinition(def);
				QuerySrv.getInstance().updateQmd(queryVO.getPrimaryKey(), def);
			}

		}
	}

	/**
	 * 修改查询模型
	 * 
	 * @param form
	 * @param modelVO
	 * @throws Exception
	 */
	private void updateQueryQmd(SegDefEditForm form, QueryModelVO modelVO) throws Exception {
		// 设置内容
		BIQueryModelDef def = QueryModelMaker.getQueryModelDef(form, modelVO);
		// 更新查询模型
		QuerySrv.getInstance().updateQmd(modelVO.getID(), def);
	}

	/**
	 * 生成QueryModelVO
	 * 
	 * @param form
	 * @return
	 */
	private QueryModelVO getQueryModelVO(SegDefEditForm form) {
		QueryModelVO queryModelVO = new QueryModelVO();
		queryModelVO.setID(form.getSegReportQueryPK());
		queryModelVO.setPk_folderID(ResMngToolKit.getVOIDByTreeObjectID(form.getSegQueryDirPK()));
		queryModelVO.setName(form.getSegQueryName());
		queryModelVO.setQuerycode(form.getSegQueryName());
		queryModelVO.setOwneruser(getCurUserInfo().getID());

		// 数据源不要设置,类型为采集模型
		queryModelVO.setType(BIQueryConst.TYPE_INPUTMODEL);
		queryModelVO.setNote(StringResource.getStringResource("usrdef0038", new String[] { form.getSegDefName() }));

		return queryModelVO;
	}

	/**
	 * 生成ReportVO
	 * 
	 * @param form
	 * @return
	 */
	private ReportVO getReportVO(SegDefEditForm form) {
		ReportVO repVO = new ReportVO();
		if (form.getSegReportPK() != null && form.getSegReportPK().length() > 0) {
			repVO.setID(form.getSegReportPK());
		}
		repVO.setName(form.getSegReportName());
		repVO.setReportcode(form.getSegReportName());
		repVO.setPk_folderID(ResMngToolKit.getVOIDByTreeObjectID(form.getSegReportDirPK()));
		repVO.setType(new Integer(1));// 多维报表
		repVO.setNote(StringResource.getStringResource("usrdef0039", new String[] { form.getSegDefName() }));
		repVO.setFk_querymodel(form.getSegReportQueryPK());

		return repVO;
	}

	/**
	 * 修改报表
	 * 
	 * @param form
	 */
	private void updateReport(SegDefEditForm form, boolean bUpdateModel) throws Exception {
		// 判断基础信息有没有更改
		String strRepPK = form.getSegReportPK();
		boolean bUpdateBase = false;
		ReportVO oldrepVO = BIReportSrv.getInstance().getByID(strRepPK);
		if (oldrepVO == null) {
			throw new WebException("msrdef0035");// ""
		}
		if (oldrepVO.getName().equals(form.getSegReportName()) == false
				|| oldrepVO.getPk_folderID().equals(form.getSegReportDirPK()) == false) {
			bUpdateBase = true;
		}

		if (bUpdateBase || bUpdateModel) {
			// 设置内容
			ReportVO repVO = getReportVO(form);
			if (bUpdateModel) {
				repVO.setDefinition(RepModelMaker.getReportModel(form));
			} else {
				repVO.setDefinition(oldrepVO.getDefinition());
			}
			BIReportSrv.getInstance().updateReport(repVO);
		}
	}

	/**
	 * 创建分部划分
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void createSegDef(SegDefEditForm form) throws Exception {

		SegDefVO segDefVO = new SegDefVO();
		setFormToVO(segDefVO, form);

		// 新建，需要首先新建分部报告对应的查询和报表
		String strSegQueryPK = null;
		String strSegRepPK = null;
		try {
			strSegQueryPK = createQuery(form);
			form.setSegReportQueryPK(strSegQueryPK);
			strSegRepPK = createReport(form);
			segDefVO.setSegReportPK(strSegRepPK);
			segDefVO.setSegReportQueryPK(strSegQueryPK);

			SegDefSrv.getInstance().createSegDefVO(segDefVO);
		} catch (Exception e) {
			// clean up
			if (strSegQueryPK != null) {
				QuerySrv.getInstance().removeQueryModels(new String[] { strSegQueryPK });
			} else {
				QueryModelVO queryVO = QuerySrv.getInstance().getQueryModelVOByName(
						ResMngToolKit.getVOIDByTreeObjectID(form.getSegQueryDirPK()), form.getSegQueryName());
				if (queryVO != null) {
					QuerySrv.getInstance().removeQueryModels(new String[] { queryVO.getPrimaryKey() });
				}
			}
			if (strSegRepPK != null) {
				BIReportSrv.getInstance().removeReports(new String[] { strSegRepPK });
			}
			throw new WebException("msrdef0036", new String[] { e.getMessage() });// 新建分部划分失败，
		}
	}

	/**
	 * 修改分布划分
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void updateSegDef(SegDefEditForm form) throws Exception {
		SegDefVO segDefVO = new SegDefVO();
		setFormToVO(segDefVO, form);

		SegDefVO oldSegDefVO = SegDefSrv.getInstance().getSegDefVOByPK(form.getSegDefPK());
		if (oldSegDefVO != null) {
			// 需要判断分布对应的查询是否有数据
			if (isSegRepDataExist(oldSegDefVO)) {
				throw new WebException("msrdef0037");// 分布报告已经生成，不能修改
			}

			// 如果分布中的维度信息发生变化，则需要修改查询和报表
			boolean bDimChanged = isDimChanged(oldSegDefVO, form);
			updateQuery(form, bDimChanged);
			if (bDimChanged == false) {
				bDimChanged = isOrgMembersChanged(oldSegDefVO, form);
			}
			updateReport(form, bDimChanged);
			segDefVO.setSegReportPK(form.getSegReportPK());
			segDefVO.setSegReportQueryPK(form.getSegReportQueryPK());
			SegDefSrv.getInstance().updateSegDefVO(segDefVO);
		} else {
			throw new WebException("msrdef0019");// 分部划分已经被删除，无法保存
		}
	}

	/**
	 * 设置VO
	 * 
	 * @param segDefVO
	 * @param form
	 * @throws Exception
	 */
	private void setFormToVO(SegDefVO segDefVO, SegDefEditForm form) throws Exception {
		if (form.getSegDefPK() != null && form.getSegDefPK().length() > 0) {
			segDefVO.setSegDefPK(form.getSegDefPK());
		}
		segDefVO.setQueryPK(form.getQueryPK());
		segDefVO.setDirPK(form.getDirPK());
		segDefVO.setSegDefName(form.getSegDefName());
		segDefVO.setOrgDimPK(form.getOrgDimPK());
		// 得到字段的引用维度PK
		segDefVO.setOrgDimField(form.getOrgDimField());
		segDefVO.setTradeOrgDimPK(form.getTradeOrgDimPK());

		segDefVO.setTradeOrgDimField(form.getTradeOrgDimField());
		SegDefScopeVO scopeVO = new SegDefScopeVO();

		scopeVO.setOrgMemberPKs(getOrgMembers());
		segDefVO.setScopeVO(scopeVO);
	}

	/**
	 * 判断维度等信息是否改变
	 * 
	 * @param segDefVO
	 * @param form
	 * @return
	 */
	private boolean isDimChanged(SegDefVO segDefVO, SegDefEditForm form) {
		if (segDefVO.getQueryPK().equals(form.getQueryPK()) == false
				|| segDefVO.getOrgDimPK().equals(form.getOrgDimPK()) == false
				|| segDefVO.getOrgDimField().equals(form.getOrgDimField()) == false
				|| segDefVO.getTradeOrgDimPK().equals(form.getTradeOrgDimPK()) == false
				|| segDefVO.getTradeOrgDimField().equals(form.getTradeOrgDimField()) == false) {
			return true;
		}
		return false;
	}

	/**
	 * 判断分布范围是否改变
	 * 
	 * @param segDefVO
	 * @param form
	 * @return
	 */
	private boolean isOrgMembersChanged(SegDefVO segDefVO, SegDefEditForm form) {
		SegDefScopeVO scopeVO = segDefVO.getScopeVO();
		if (scopeVO != null) {
			String[] strOldMemberIDs = scopeVO.getOrgMemberPKs();
			String[] strMemberIDs = form.getOrgDimMembers();
			if (strOldMemberIDs == null && strMemberIDs == null) {
				return true;
			}
			if (strOldMemberIDs.length == strMemberIDs.length) {
				Hashtable<String, String> hashIDs = new Hashtable<String, String>();
				for (int i = 0; i < strOldMemberIDs.length; i++) {
					hashIDs.put(strOldMemberIDs[i], strOldMemberIDs[i]);
				}
				for (int i = 0; i < strMemberIDs.length; i++) {
					if (hashIDs.get(strMemberIDs[i]) == null) {
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	/**
	 * 得到分部范围
	 * 
	 * @return
	 */
	private String[] getOrgMembers() {
		String selectIDs = getRequestParameter("strOrgDimMembers");
		if (selectIDs != null && selectIDs.length() > 0) {
			return selectIDs.split(WebGlobalValue.FLAG_SPLIT);
		}
		return null;
	}

	/**
	 * 设置查询名称
	 * 
	 * @param form
	 * @param strQueryPK
	 * @throws Exception
	 */
	private void setQueryName(SegDefEditForm form, String strQueryPK) throws Exception {
		QueryModelVO queryVO = QuerySrv.getInstance().getQueryModelVO(strQueryPK);
		if (queryVO != null) {
			form.setQueryName(queryVO.getQuerycode());
		}
	}

	/**
	 * 设置查询目录名称
	 * 
	 * @param form
	 * @param strDirPK
	 * @throws Exception
	 */
	private void setSegQueryDirName(SegDefEditForm form, String strDirPK) throws Exception {
		IResTreeObject dirVO = QuerySrv.getInstance().getDirByID(strDirPK);
		if (dirVO != null) {
			form.setSegQueryDirName(dirVO.getLabel());
		}
	}

	/**
	 * 设置报表目录名称
	 * 
	 * @param form
	 * @param strDirPK
	 * @throws Exception
	 */
	private void setSegReportDirName(SegDefEditForm form, String strDirPK) throws Exception {
		IResTreeObject dirVO = BIReportSrv.getInstance().getDirByID(strDirPK);
		if (dirVO != null) {
			form.setSegRepDirName(dirVO.getLabel());
		}
	}

}
