/*
 * 创建日期 2006-8-9
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.bi.query.manager.QuerySrv;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.integration.dimension.MeasureVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.segrep.segdef.ISegRepConstants;

import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.multidimension.model.SelDimMemberVO;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufida.web.WebException;

public class RepModelMaker {
	/**
	 * 生成多维报表模型，主要是维度选择模型，
	 * 未选维度包括：对方单位和对方分部属性维度
	 * 行维度：指标
	 * 列维度：本方分部属性和交易类型
	 * 页维度：所有其他维度
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public static MultiDimemsionModel getReportModel(SegDefEditForm form) throws Exception{
		if( form == null ){
			return null;
		}
		//定义多维报表格式
		MultiDimemsionModel		model = new MultiDimemsionModel("");//需要写入什么
		
		//纬度选择模型
		String					strQueryPK = form.getSegReportQueryPK();
		SelDimModel				selDimModel = new SelDimModel(model);
		selDimModel.setQueryModel(QuerySrv.getInstance().getQueryModelVO(strQueryPK));

		//根据原始查询的元数据模型设置新的报表的维度选择模型
		MetaDataVO[]			mDataVOs = QueryModelSrv.getSelectFlds(strQueryPK);
		if( mDataVOs != null ){
			//行纬度， 指标
			SelDimensionVO[]	rowDimVOs = new SelDimensionVO[1];
			ArrayList			aryMeasures = new ArrayList();
			//列维度，分部和交易类型
			SelDimensionVO[]	colDimVOs = new SelDimensionVO[2];
			ArrayList			aryPageDims = new ArrayList();
			//对方单位、对方分部依据、本方单位
			SelDimensionVO[]    filterDimVOs = new SelDimensionVO[3];
			String				strFieldDimPK = form.getOrgDimField();
			String				strTradeFieldDimPK = form.getTradeOrgDimField();
			String[]			strOrgMembers = form.getOrgDimMembers();
			
			for( int i=0; i<mDataVOs.length; i++ ){
				String		strDimPK = mDataVOs[i].getPk_dimdef();
				if( mDataVOs[i].getDimflag() == false ){
					SelDimMemberVO	measMemberVO = new SelDimMemberVO(new MeasureVO(mDataVOs[i]));
					aryMeasures.add(measMemberVO);//sel dim members
				}else if( strDimPK != null && strDimPK.length()>0){
					if(strDimPK.equals(strFieldDimPK) && 
						form.getOrgDimPK().equals(mDataVOs[i].getPk_mainDimdef())){
						//分部依据
						String[]	strMemberIDs = getAttrDimMembers(form.getOrgDimPK(), strFieldDimPK, strOrgMembers);
						colDimVOs[0] = getSelDimVO(mDataVOs[i], strMemberIDs);
					}else if( strDimPK.equals(ISegRepConstants.TRADE_TYPE_DIMPK)){//交易类型纬度
						//成员为所有非跟成员
						String[]			strMemberIDs = getTradeTypeMembers(strDimPK);
						colDimVOs[1] = getSelDimVO(mDataVOs[i], strMemberIDs);
					}else if( strDimPK.equals(form.getTradeOrgDimPK()) ){
						filterDimVOs[0] = getSelDimVO(mDataVOs[i], null);
					}else if( strDimPK.equals(strTradeFieldDimPK)&&
							form.getTradeOrgDimPK().equals(mDataVOs[i].getPk_mainDimdef()) ){
						//未选纬度
						filterDimVOs[1] = getSelDimVO(mDataVOs[i], null);
					}else if(strDimPK.equals(form.getOrgDimPK()) ){
						filterDimVOs[2] = getSelDimVO(mDataVOs[i], null);
					}else{
						//否则为根成员
						aryPageDims.add(getSelDimVO(mDataVOs[i], null));
					}
				}
			}
			SelDimensionVO[]  	pageDimVOs = new SelDimensionVO[aryPageDims.size()];
			aryPageDims.toArray(pageDimVOs);
			SelDimMemberVO[]	measMemberVOs = new SelDimMemberVO[aryMeasures.size()];
			aryMeasures.toArray(measMemberVOs);
			rowDimVOs[0] = new SelDimensionVO(null);
			rowDimVOs[0].setSelMembers(measMemberVOs);
			
			selDimModel.setSelDimVOs(IMultiDimConst.POS_ROW, rowDimVOs);
			selDimModel.setSelDimVOs(IMultiDimConst.POS_COLUMN, colDimVOs);
			selDimModel.setSelDimVOs(IMultiDimConst.POS_PAGE, pageDimVOs);
			selDimModel.setSelDimVOs(IMultiDimConst.POS_UNSEL, filterDimVOs);
			model.setSelDimModel(selDimModel);
		}
		return model;
	}
	/**
	 * 根据MetaDataVO,和维度成员PK，构造SelDimensionVO
	 * @param mDataVO
	 * @param strMemberIDs
	 * @return
	 * @throws Exception
	 */
	private static SelDimensionVO  getSelDimVO(MetaDataVO mDataVO, String[] strMemberIDs ) 
		throws Exception
	{	
		DimensionVO			dimVO = DimensionSrv.getInstance().getDimByID(mDataVO.getPk_dimdef());
		if( dimVO == null ){
			throw new WebException("msrdef0018");//"维度不存在"
		}
		DimMemberSrv		memberSrv = new DimMemberSrv(dimVO);
		SelDimMemberVO[]	selDimMemberVOs = null;
		if( strMemberIDs != null ){
			DimMemberVO[]	memberVOs = memberSrv.getByID(strMemberIDs);
			//过滤memberVOs中null的成员
			ArrayList		arySelDimMemberVOs = new ArrayList();
			selDimMemberVOs = new SelDimMemberVO[memberVOs.length];
			for( int j=0; j<memberVOs.length; j++){
				if( memberVOs[j] != null ){
					arySelDimMemberVOs.add( new SelDimMemberVO(memberVOs[j]));
				}
			}
			if( arySelDimMemberVOs.size() >0 ){
				selDimMemberVOs = new SelDimMemberVO[arySelDimMemberVOs.size()];
				arySelDimMemberVOs.toArray(selDimMemberVOs);
			}
		}
		
		//如果没有选择成员，则认为根成员
		if( selDimMemberVOs == null ){
			DimMemberVO		rootVO = memberSrv.getRoot();
			if( rootVO == null ){
				//抛出异常
				throw new WebException("msrdef0018");//"维度不存在"
			}
			selDimMemberVOs = new SelDimMemberVO[1];
			selDimMemberVOs[0] = new SelDimMemberVO(rootVO);
		}
		
		SelDimensionVO		selDimVO = new SelDimensionVO(mDataVO);
		selDimVO.setSelMembers(selDimMemberVOs);
		return selDimVO;
	}
	
	
	private  static String[]  getAttrDimMembers(String strDimPK, String strAttrDimPK, String[] strDimMembers) 
		throws Exception
	{
		DimensionVO		dimVO = DimensionSrv.getInstance().getDimByID(strDimPK);
		if( dimVO != null ){
//			得到属性维度对应的字段
			String			strColName = DimensionSrv.getInstance().getPropDimDBColumn(strDimPK, strAttrDimPK);
			if( strColName != null ){
				DimMemberSrv	srv = new DimMemberSrv(dimVO);
				DimMemberVO[]	memberVOs = srv.getByID(strDimMembers);
				Hashtable       hashIDs = new Hashtable();
				for( int i=0; i<memberVOs.length; i++ ){
					if( memberVOs[i] != null ){
						String	strAttrID = memberVOs[i].getPropValue(strColName);
						if( strAttrID != null ){
							hashIDs.put( strAttrID, strAttrID);
						}
					}
				}
				if( hashIDs.size() >0){
					String[]	strAttrMembers = new String[hashIDs.size()];
					hashIDs.values().toArray(strAttrMembers);
					return strAttrMembers;
				}
			}
		}
		return null;
	}
	
	private static String[]  getTradeTypeMembers(String strDimPK) 
		throws Exception
	{
		String[]	strMemberIDs = null;
		DimensionVO			dimVO = DimensionSrv.getInstance().getDimByID(strDimPK);
		if( dimVO != null ){
			DimMemberSrv	srv = new DimMemberSrv(dimVO);
			DimMemberVO		rootVO = srv.getRoot();
			DimMemberVO[]	memberVOs = srv.getSubMembers(rootVO.getMemberID());
			strMemberIDs = new String[memberVOs.length];
			for( int i=0; i<strMemberIDs.length; i++ ){
				strMemberIDs[i] = memberVOs[i].getMemberID();
			}
		}
		return strMemberIDs;
	}


}
