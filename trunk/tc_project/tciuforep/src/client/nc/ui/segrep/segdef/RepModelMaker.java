/*
 * �������� 2006-8-9
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
	 * ���ɶ�ά����ģ�ͣ���Ҫ��ά��ѡ��ģ�ͣ�
	 * δѡά�Ȱ������Է���λ�ͶԷ��ֲ�����ά��
	 * ��ά�ȣ�ָ��
	 * ��ά�ȣ������ֲ����Ժͽ�������
	 * ҳά�ȣ���������ά��
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public static MultiDimemsionModel getReportModel(SegDefEditForm form) throws Exception{
		if( form == null ){
			return null;
		}
		//�����ά�����ʽ
		MultiDimemsionModel		model = new MultiDimemsionModel("");//��Ҫд��ʲô
		
		//γ��ѡ��ģ��
		String					strQueryPK = form.getSegReportQueryPK();
		SelDimModel				selDimModel = new SelDimModel(model);
		selDimModel.setQueryModel(QuerySrv.getInstance().getQueryModelVO(strQueryPK));

		//����ԭʼ��ѯ��Ԫ����ģ�������µı����ά��ѡ��ģ��
		MetaDataVO[]			mDataVOs = QueryModelSrv.getSelectFlds(strQueryPK);
		if( mDataVOs != null ){
			//��γ�ȣ� ָ��
			SelDimensionVO[]	rowDimVOs = new SelDimensionVO[1];
			ArrayList			aryMeasures = new ArrayList();
			//��ά�ȣ��ֲ��ͽ�������
			SelDimensionVO[]	colDimVOs = new SelDimensionVO[2];
			ArrayList			aryPageDims = new ArrayList();
			//�Է���λ���Է��ֲ����ݡ�������λ
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
						//�ֲ�����
						String[]	strMemberIDs = getAttrDimMembers(form.getOrgDimPK(), strFieldDimPK, strOrgMembers);
						colDimVOs[0] = getSelDimVO(mDataVOs[i], strMemberIDs);
					}else if( strDimPK.equals(ISegRepConstants.TRADE_TYPE_DIMPK)){//��������γ��
						//��ԱΪ���зǸ���Ա
						String[]			strMemberIDs = getTradeTypeMembers(strDimPK);
						colDimVOs[1] = getSelDimVO(mDataVOs[i], strMemberIDs);
					}else if( strDimPK.equals(form.getTradeOrgDimPK()) ){
						filterDimVOs[0] = getSelDimVO(mDataVOs[i], null);
					}else if( strDimPK.equals(strTradeFieldDimPK)&&
							form.getTradeOrgDimPK().equals(mDataVOs[i].getPk_mainDimdef()) ){
						//δѡγ��
						filterDimVOs[1] = getSelDimVO(mDataVOs[i], null);
					}else if(strDimPK.equals(form.getOrgDimPK()) ){
						filterDimVOs[2] = getSelDimVO(mDataVOs[i], null);
					}else{
						//����Ϊ����Ա
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
	 * ����MetaDataVO,��ά�ȳ�ԱPK������SelDimensionVO
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
			throw new WebException("msrdef0018");//"ά�Ȳ�����"
		}
		DimMemberSrv		memberSrv = new DimMemberSrv(dimVO);
		SelDimMemberVO[]	selDimMemberVOs = null;
		if( strMemberIDs != null ){
			DimMemberVO[]	memberVOs = memberSrv.getByID(strMemberIDs);
			//����memberVOs��null�ĳ�Ա
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
		
		//���û��ѡ���Ա������Ϊ����Ա
		if( selDimMemberVOs == null ){
			DimMemberVO		rootVO = memberSrv.getRoot();
			if( rootVO == null ){
				//�׳��쳣
				throw new WebException("msrdef0018");//"ά�Ȳ�����"
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
//			�õ�����ά�ȶ�Ӧ���ֶ�
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
