/*
 * �������� 2006-7-17
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
	 * ����ѡ�еķֲ�PK, ���ɷֲ������ά�ȳ�Ա¼�����
	 * 
	 */
	public ActionForward  execute(ActionForm actionForm){
		try{
			//����form
			String			strSegDefPK = ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());
			SegExecForm		form = (SegExecForm)actionForm;
			form.setSegDefPK(strSegDefPK);
			
			//�õ��ֲ������еĲɼ����ж�Ӧ�Ĳ�ѯ,�õ���ѯ�е�ά����Ϣ
			SegDefVO		segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			if( segDefVO != null ){
				MetaDataVO[]	mDataVOs = QueryModelSrv.getDimFlds(segDefVO.getQueryPK());
				if( mDataVOs != null ){
					//�õ�����֯ά�ȣܶԷ���֯ά���������ά����Ϣ,
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
					//ά����Ϣ���õ�form��
					if( aryList.size() >0 ){
						String[]	strDimPKs = new String[aryList.size()];
						aryList.toArray(strDimPKs);
						DimensionVO[]	dimVOs = DimensionSrv.getInstance().getDimByIDs(strDimPKs);
						form.setDimVOs(dimVOs);
						return new ActionForward(SegRepExecDlg.class.getName());
					}
				}

				return new ErrorForward(StringResource.getStringResource("msrdef0038"));//"��ѯ�Ѿ����޸ģ��޷����ɷֲ�����"
			}else{
				return new ErrorForward(StringResource.getStringResource("msrdef0019"));//
			}
		}catch(Exception e){
			return new ErrorForward(e.getMessage());
		}
	}
	/**
	 * ���ɷֲ���������,�����ؽ����ʾ
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
			
			//��Ҫ���ö�ά�����Web��ʾAction�ķ���
			SegDefVO		segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			ActionForward   okfwd = new ActionForward(RepViewDispatchAction.class.getName(), RepViewDispatchAction.METHOD_REPVIEW);
			okfwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID,segDefVO.getSegReportPK());
			return new ConfirmForward(StringResource.getStringResource("msrdef0040"), okfwd);//"�ֲ����������Ѿ�����,�Ƿ�鿴?");
			
		}catch(Exception e){
			return new ErrorForward(e.getMessage());
		}
	}
	/**
	 * �ֲ�������ʾ
	 * @param actionForm
	 * @return
	 */
	public ActionForward   showData(ActionForm  actionForm){
		try{
			//�õ��ֲ�����ı���ɣ�
			String			strSegDefPK = ResMngToolKit.getVOIDByTreeObjectID(getTableSelectedID());
			SegDefVO		segDefVO = SegDefSrv.getInstance().getSegDefVOByPK(strSegDefPK);
			if( segDefVO != null ){
				ActionForward  fwd = new ActionForward(RepViewDispatchAction.class.getName(), RepViewDispatchAction.METHOD_REPVIEW);
				fwd.addParameter(WebGlobalValue.TABLE_SELECTED_ID,segDefVO.getSegReportPK());
				return fwd;
			}else{
				return new ErrorForward(StringResource.getStringResource("msrdef0019"));//"�ֲ������Ѿ�ɾ��");
			}
		}catch(SegRepException e){
			return new ErrorForward(e.getMessage());
		}
	}
	public String getFormName() {
		return SegExecForm.class.getName();
	}


	
	
	
}
