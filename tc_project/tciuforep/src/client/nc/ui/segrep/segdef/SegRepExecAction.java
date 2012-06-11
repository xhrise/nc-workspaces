/*
 * 创建日期 2006-7-17
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.segrep.segdef.IBSegRepExecService;
//import nc.ui.bi.report.multidimension.WebMultiDimRepAction;
import nc.ui.bi.report.manager.RepViewDispatchAction;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.segrep.segdef.SegDefSrv;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.segrep.segdef.ISegRepConstants;
import nc.vo.segrep.segdef.SegDefVO;
import nc.vo.segrep.segdef.SegRepException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.ConfirmForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.action.MessageForward;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.DialogAction;

public class SegRepExecAction extends DialogAction {
	/**
	 * 根据选中的分部PK, 生成分部报告的维度成员录入界面
	 * 
	 */
	public ActionForward  execute(ActionForm actionForm){
		try{
			//设置form
			String			strSegDefPK = ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());
			SegExecForm		form = (SegExecForm)actionForm;
			form.setSegDefPK(strSegDefPK);
			
			//得到分布定义中的采集表中对应的查询,得到查询中的维度信息
			SegDefVO		segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			if( segDefVO != null ){
				MetaDataVO[]	mDataVOs = QueryModelSrv.getDimFlds(segDefVO.getQueryPK());
				if( mDataVOs != null ){
					//得到除组织维度＼对方组织维度外的其他维度信息,
					ArrayList		aryList = new ArrayList();
					for( int i=0; i<mDataVOs.length; i++ ){
						if( mDataVOs[i].getDimflag() ){
							String		strDimPK = mDataVOs[i].getPk_dimdef();
							if( strDimPK.equals(segDefVO.getOrgDimPK()) || 
								strDimPK.equals(segDefVO.getTradeOrgDimPK()) ||
								strDimPK.equals(segDefVO.getOrgDimField()) ||
								strDimPK.equals(segDefVO.getTradeOrgDimField()) ){				
								continue;
							}else{
								aryList.add(strDimPK);
							}
						}
					}
					//维度信息设置到form中
					if( aryList.size() >0 ){
						String[]	strDimPKs = new String[aryList.size()];
						aryList.toArray(strDimPKs);
						DimensionVO[]	dimVOs = DimensionSrv.getInstance().getDimByIDs(strDimPKs);
						form.setDimVOs(dimVOs);
						return new ActionForward(SegRepExecDlg.class.getName());
					}
				}

				return new ErrorForward(StringResource.getStringResource("msrdef0038"));//"查询已经被修改，无法生成分布数据"
			}else{
				return new ErrorForward(StringResource.getStringResource("msrdef0019"));//
			}
		}catch(Exception e){
			return new ErrorForward(e.getMessage());
		}
	}
	/**
	 * 生成分布报告数据,并返回结果显示
	 * @param actionForm
	 * @return
	 */
	public ActionForward  generateData(ActionForm actionForm){
		try{
			SegExecForm		form = (SegExecForm)actionForm;
			String			strSegDefPK = form.getSegDefPK();
			int				nDimNumber = form.getDimNumbers();
			String[]		strDimPKs = new String[nDimNumber];
			String[]		strDimMemberCodes = new String[nDimNumber];
			
			for( int i=0; i<nDimNumber; i++ ){
				strDimPKs[i] = getRequestParameter(ISegRepConstants.PARAM_DIMPK+i);
				String  strMemberPK = getRequestParameter(ISegRepConstants.PARAM_DIMMEMBER+i);
				DimensionVO		dimVO = DimensionSrv.getInstance().getDimByID(strDimPKs[i]);
				if( dimVO != null ){
					DimMemberVO	memberVO = (new DimMemberSrv(dimVO)).getByID(new String[]{strMemberPK})[0];
					if( memberVO != null ){
						strDimMemberCodes[i] = memberVO.getMemcode();
					}else{
						return new ErrorForward(StringResource.getStringResource("msrdef0039"));//
					}
				}				
			}
			
			IBSegRepExecService  service = (IBSegRepExecService)NCLocator.getInstance().lookup(IBSegRepExecService.class.getName());
			service.generateSegData(strSegDefPK, strDimPKs, strDimMemberCodes);
			
			//需要调用多维报表的Web显示Action的方法
			SegDefVO		segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			ActionForward   okfwd = new ActionForward(RepViewDispatchAction.class.getName(), RepViewDispatchAction.METHOD_REPVIEW);
			okfwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID,segDefVO.getSegReportPK());
			return new ConfirmForward(StringResource.getStringResource("msrdef0040"), okfwd);//"分部报告数据已经生成,是否查看?");
			
		}catch(Exception e){
			return new ErrorForward(e.getMessage());
		}
	}
	/**
	 * 分部数据显示
	 * @param actionForm
	 * @return
	 */
	public ActionForward   showData(ActionForm  actionForm){
		try{
			//得到分部定义的报表ＩＤ
			String			strSegDefPK = ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());
			SegDefVO		segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			if( segDefVO != null ){
				ActionForward  fwd = new ActionForward(RepViewDispatchAction.class.getName(), RepViewDispatchAction.METHOD_REPVIEW);
				fwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID,segDefVO.getSegReportPK());
				return fwd;
			}else{
				return new ErrorForward(StringResource.getStringResource("msrdef0019"));//"分部划分已经删除");
			}
		}catch(SegRepException e){
			return new ErrorForward(e.getMessage());
		}
	}
	public String getFormName() {
		return SegExecForm.class.getName();
	}


	
	
	
}
