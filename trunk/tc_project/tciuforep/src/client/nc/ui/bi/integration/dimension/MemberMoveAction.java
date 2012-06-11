/**
 * Action1.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.MessageForward;
import com.ufsoft.iufo.web.DialogAction;

/**
 * �������������� ll 2006-01-17
 */
public class MemberMoveAction extends DialogAction {

	static final String KEY_DIM_ID = "key_of_dimension";

	public static final String METHOD_MOVE_MEMBER = "execute";

	public static final String METHOD_MOVE_MEMBER_SUBMIT = "moveMember_submit";

	public ActionForward execute(ActionForm actionForm) throws WebException {
		MemberMoveForm form = (MemberMoveForm) actionForm;

		String dimID = getRequestParameter(KEY_DIM_ID);
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		String memID = getTableSelectedID();
		DimMemberVO memVO = (new DimMemberSrv(dimVO)).getByID(new String[] { memID })[0];
		String msg = null;
		if (memVO == null) {
			msg = "mbidim00100";// ѡ�е�ά�ȳ�Ա������
		}
		if (memVO.getDepth().intValue() == 0) {
			msg = "mbidim00102";// ����Ա���ܽ����ƶ�
		}
		if (msg != null)
			return new MessageForward(msg);

		form.setSelectedTreeID(dimID);

		ActionForward actionForward = new ActionForward(MemberMoveDlg.class.getName());
		return actionForward;
	}

	public ActionForward moveMember_submit(ActionForm actionForm) throws WebException {
		MemberMoveForm form = (MemberMoveForm) actionForm;
		//��ȡ����
		String strDestID = DimUIToolKit.getMemberIDByTreeID(form.getDestMemID());
		String strSrcID = getTableSelectedID();

		String dimID = getRequestParameter(KEY_DIM_ID);
		DimensionVO dimVO = (DimensionVO) DimensionSrv.getByID(dimID);
		DimMemberSrv memSrv = new DimMemberSrv(dimVO);

		// ִ�г�Ա�ƶ�
		moveMember(memSrv, strSrcID, strDestID);
		return new CloseForward(CloseForward.CLOSE_REFRESH_PARENT);
	}

	private void moveMember(DimMemberSrv memSrv, String strSrcID, String strDestID) throws WebException {
		if (strSrcID == null || strDestID == null) {
			return;
		}
		DimMemberVO srcMemVO = memSrv.getByID(new String[] { strSrcID })[0];
		DimMemberVO destMemVO = memSrv.getByID(new String[] { strDestID })[0];

		// �ƶ�������
		DimMemberVO[] submems = isCanMoveTo(memSrv, srcMemVO, destMemVO);

		// #ִ��ʵ�ʵ��ƶ�
		// ͬʱά����ģ�͹�ϵ����
		moveTo(memSrv, submems, destMemVO);
	}

	/**
	 * ����Ա�ƶ��ĺϷ��ԣ������ƶ����ӳ�Ա����,�ƶ����ܳ�����󼶴�
	 */
	private DimMemberVO[] isCanMoveTo(DimMemberSrv memSrv, DimMemberVO srcMemVO, DimMemberVO destMemVO)
			throws WebException {
		if (srcMemVO == null || destMemVO == null) {
			throw new WebException("mbidim00100");// ѡ�е�ά�ȳ�Ա������
		}
		int srcDepth = srcMemVO.getDepth().intValue();
		String strDestID = destMemVO.getMemberID();
		boolean bSamePos = strDestID.equals(srcMemVO.getLevels()[srcDepth - 1]);
		boolean bChild = false;
		if (!bSamePos) {
			String[] levelIDs = destMemVO.getLevels();
			for (int i = 0; i < levelIDs.length; i++) {
				if (levelIDs[i] != null && levelIDs[i].equals(srcMemVO.getMemberID())) {
					bChild = true;
					break;
				}
			}
		}
		if (bSamePos || bChild) {
			throw new WebException("mbidim00101"); // "Ŀ��λ�ò�����Դ��Ա�����ӳ�Ա"+"!"
		}
		// ����ƶ���ļ��β�����DimRescource.INT_MAX_FLDPRE_NUMBER
		DimMemberVO[] subMems = memSrv.getAllSubMembers(srcMemVO.getMemberID());
		int subCount = ((subMems == null) || (subMems[0] == null)) ? 0 : subMems.length;
		DimMemberVO[] mems = new DimMemberVO[subCount + 1];
		mems[0] = (DimMemberVO)srcMemVO.clone();
		if (subCount > 0){
			for (int i = 0; i < subMems.length; i++) {
				mems[i+1] = (DimMemberVO)subMems[i].clone();
			}
		}

		boolean tooMuchlvl = false;
		int updatelvl = destMemVO.getDepth() - srcMemVO.getDepth() + 1;
		for (int i = 0; i < mems.length; i++) {
			if (mems[i].getDepth() + updatelvl >= DimRescource.INT_MAX_FLDPRE_NUMBER) {
				tooMuchlvl = true;
				break;
			}
		}
		if (tooMuchlvl) {
			throw new WebException("mbidim00035");// ������󼶴���
		}
		return mems;

	}

	private void moveTo(DimMemberSrv memSrv, DimMemberVO[] subMemVOs, DimMemberVO destMemVO) throws WebException {
		// �޸������ƶ���Ա�ļ��ι�ϵ�ͼ������Ե�
		DimMemberSrv.processMemProps(subMemVOs, destMemVO);
		// ִ�и���
		memSrv.update(subMemVOs);

	}


	/**
	 * ����Form
	 * 
	 */
	public String getFormName() {
		return MemberMoveForm.class.getName();
	}
}
