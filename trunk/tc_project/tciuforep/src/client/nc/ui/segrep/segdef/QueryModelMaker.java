/*
 * �������� 2006-8-9
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
		//��������
		String					strQueryPK = queryVO.getID();
		BIQueryModelDef			def = new BIQueryModelDef();
		QueryBaseVO				queryBaseVO = new QueryBaseVO();
		//queryBaseVO.setTemptablename("TEMB_"+strQueryPK);//��TEMB_��ѯPK
		QueryModelDef			baseModelDef = new QueryModelDef();
		baseModelDef.setQueryBaseVO(queryBaseVO);
		
		//Ԫ����ģ�ͺ��ﻯģ������
		AdvancedQueryModelDef	advancedModelDef = new AdvancedQueryModelDef();
		ForgeQueryModelVO		forgeVO = new ForgeQueryModelVO();
		forgeVO.setPrimaryKey(strQueryPK);
		forgeVO.setType(queryVO.getType());
		forgeVO.setPk_folderID(queryVO.getPk_folderID());
		
		advancedModelDef.setForgeQueryModel(forgeVO);
		//��Ҫ���ݲɼ����Ӧ�Ĳ�ѯ��ԭ����ģ��,�����µ�Ԫ����ģ�ͣ�
		MetaDataVO[]			originMDataVOs = QueryModelSrv.getSelectFlds(form.getQueryPK());
		if( originMDataVOs != null ){
			//ָ����ԭ��һ����ά�����ӱ����ֲ����ԺͶԷ��ֲ����ԣ���������
			MetaDataVO[]			mDataVOs = new MetaDataVO[originMDataVOs.length+3];
			//�����ֲ������ֶ�ά��,��Ҫ
			mDataVOs[0] = getFieldMetaDataVO(form.getOrgDimPK(), form.getOrgDimField(),true);
			mDataVOs[1] = getFieldMetaDataVO(form.getTradeOrgDimPK(), form.getTradeOrgDimField(),false);

			//��������
			mDataVOs[2] = getTradeTypeMetaDataVO();
			System.arraycopy(originMDataVOs, 0, mDataVOs, 3, originMDataVOs.length);
			
			checkMetaDataVOs(mDataVOs);
			advancedModelDef.setMetadatas(mDataVOs);
			
			def.setBaseModel(baseModelDef);
			def.setAdvModel(advancedModelDef);
			
		}else{
			throw new WebException(StringResource.getStringResource("msrdef0043"));
			//�ɼ������õĲ�ѯģ�͵�Ԫ����ģ���ѱ�ɾ�����޷����·ֲ������ѯ
		}
		return def;
	}
	/**
	 * ����ά�Ⱥ��ֶ����ƣ�����MetaDataVO,��Ҫ�����ɷֲ����������ֶζ�Ӧ��MetaDataVO
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
				mDataVO.setColtype(Types.VARCHAR);//�ַ�
				mDataVO.setPrecision(50);
				mDataVO.setFldalias(bOrgField?ISegRepConstants.ORG_FIELD_NAMEALIAS:ISegRepConstants.COUNTER_FIELD_NAMEALIAS);//����
				String		strFieldID = bOrgField?"usrdef0021":"usrdef0022";
				mDataVO.setFldname(StringResource.getStringResource(strFieldID));//����
				return mDataVO;
			}else{
				throw new WebException("msrdef0016");//"���õ�ά���Ѿ�ɾ��"
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
			throw  new WebException(StringResource.getStringResource("msrdef0044"));//��������ά�Ȳ�����
		}
	}
	
	/**
	 * ���Ԫ����ģ����ά���Ƿ��ظ�
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
							throw new WebException("msrdef0045");//���ɵķֲ������Ӧ�Ĳ�ѯ��Ԫ����ģ�������ظ���ά�ȣ����ܼ�����
						}
					}
				}
			}
		}
	}
}
