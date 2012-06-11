/*
 * 创建日期 2006-8-9
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import java.sql.Types;

import nc.us.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.AdvancedQueryModelDef;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.ForgeQueryModelVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.segrep.segdef.ISegRepConstants;

import com.ufida.web.WebException;
import com.ufsoft.iufo.resource.StringResource;

public class QueryModelMaker {
	
	public static BIQueryModelDef getQueryModelDef(SegDefEditForm form, QueryModelVO queryVO) 
		throws Exception
	{
		//设置内容
		String					strQueryPK = queryVO.getID();
		BIQueryModelDef			def = new BIQueryModelDef();
		QueryBaseVO				queryBaseVO = new QueryBaseVO();
		//queryBaseVO.setTemptablename("TEMB_"+strQueryPK);//由TEMB_查询PK
		QueryModelDef			baseModelDef = new QueryModelDef();
		baseModelDef.setQueryBaseVO(queryBaseVO);
		
		//元数据模型和物化模型设置
		AdvancedQueryModelDef	advancedModelDef = new AdvancedQueryModelDef();
		ForgeQueryModelVO		forgeVO = new ForgeQueryModelVO();
		forgeVO.setPrimaryKey(strQueryPK);
		forgeVO.setType(queryVO.getType());
		forgeVO.setPk_folderID(queryVO.getPk_folderID());
		
		advancedModelDef.setForgeQueryModel(forgeVO);
		//需要根据采集表对应的查询的原数据模型,建立新的元数据模型，
		MetaDataVO[]			originMDataVOs = QueryModelSrv.getSelectFlds(form.getQueryPK());
		if( originMDataVOs != null ){
			//指标与原来一样，维度增加本方分部属性和对方分部属性，交易类型
			MetaDataVO[]			mDataVOs = new MetaDataVO[originMDataVOs.length+3];
			//本方分部属性字段维度,需要
			mDataVOs[0] = getFieldMetaDataVO(form.getOrgDimPK(), form.getOrgDimField(),true);
			mDataVOs[1] = getFieldMetaDataVO(form.getTradeOrgDimPK(), form.getTradeOrgDimField(),false);

			//交易类型
			mDataVOs[2] = getTradeTypeMetaDataVO();
			System.arraycopy(originMDataVOs, 0, mDataVOs, 3, originMDataVOs.length);
			
			checkMetaDataVOs(mDataVOs);
			advancedModelDef.setMetadatas(mDataVOs);
			
			def.setBaseModel(baseModelDef);
			def.setAdvModel(advancedModelDef);
			
		}else{
			throw new WebException(StringResource.getStringResource("msrdef0043"));
			//采集表引用的查询模型的元数据模型已被删除，无法更新分部报告查询
		}
		return def;
	}
	/**
	 * 根据维度和字段名称，生成MetaDataVO,主要是生成分部划分依据字段对应的MetaDataVO
	 * @param strDimPK
	 * @param strFieldName
	 * @param bOrgField
	 * @return
	 * @throws Exception
	 */
	private  static MetaDataVO	getFieldMetaDataVO(String strDimPK, String strFieldRefType,boolean bOrgField) throws Exception{
		String	strRefDimPK = strFieldRefType;
		if(  strRefDimPK!= null ){
			DimensionVO	 	refDimVO = DimensionSrv.getInstance().getDimByID(strRefDimPK);
			if( refDimVO != null ){
				MetaDataVO		mDataVO = new MetaDataVO();
				mDataVO.setDimflag(true);
				mDataVO.setDimname(refDimVO.getDimname());
				mDataVO.setPk_dimdef(refDimVO.getDimID());
				mDataVO.setPk_mainDimdef(strDimPK);
				mDataVO.setColtype(Types.VARCHAR);//字符
				mDataVO.setPrecision(50);
				mDataVO.setFldalias(bOrgField?ISegRepConstants.ORG_FIELD_NAMEALIAS:ISegRepConstants.COUNTER_FIELD_NAMEALIAS);//别名
				String		strFieldID = bOrgField?"usrdef0021":"usrdef0022";
				mDataVO.setFldname(StringResource.getStringResource(strFieldID));//名称
				return mDataVO;
			}else{
				throw new WebException("msrdef0016");//"引用的维度已经删除"
			}
		}
		return null;
	}
	private static MetaDataVO	getTradeTypeMetaDataVO() throws Exception{
		DimensionVO	 	tradeTypeDimVO = DimensionSrv.getInstance().getDimByID(ISegRepConstants.TRADE_TYPE_DIMPK);
		if( tradeTypeDimVO != null ){
			MetaDataVO		mDataVO = new MetaDataVO();
			mDataVO.setDimflag(true);
			String strName = StringResource.getStringResource(ISegRepConstants.TRADE_TYPE_DIMNAME);
			mDataVO.setDimname(tradeTypeDimVO.getDimname());
			mDataVO.setPk_dimdef(ISegRepConstants.TRADE_TYPE_DIMPK);
			mDataVO.setFldname(strName);
			mDataVO.setFldalias(ISegRepConstants.TRADE_TYPE_NAMEALIAS);
			mDataVO.setColtype(Types.VARCHAR);
			mDataVO.setPrecision(50);
			return mDataVO;
		}else{
			throw  new WebException(StringResource.getStringResource("msrdef0044"));//交易类型维度不存在
		}
	}
	
	/**
	 * 检查元数据模型中维度是否重复
	 * @param mDataVOs
	 * @throws Exception
	 */
	private static void checkMetaDataVOs(MetaDataVO[] mDataVOs) throws Exception{
		
		for(int i=0; i<mDataVOs.length; i++ ){
			if( mDataVOs[i].getDimflag() ){
				String  strDimPK = mDataVOs[i].getPk_dimdef();
				if( strDimPK != null && strDimPK.length() >0 ){
					for( int j=i+1; j<mDataVOs.length; j++ ){
						if( mDataVOs[j].getDimflag() && strDimPK.equals(mDataVOs[j].getPk_dimdef())){
							throw new WebException("msrdef0045");//生成的分部报告对应的查询的元数据模型中有重复的维度，不能继续。
						}
					}
				}
			}
		}
	}
}
