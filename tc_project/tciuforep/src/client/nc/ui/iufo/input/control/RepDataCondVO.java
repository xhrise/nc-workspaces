package nc.ui.iufo.input.control;

import nc.ui.iufo.input.edit.IPostRepDataEditorActive;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * 报表数据页对应的条件VO
 * @author weixl
 *
 */
public class RepDataCondVO extends ValueObject {
	private static final long serialVersionUID = 82372406162491204L;
	
	private String m_strAloneID=null;
	private MeasurePubDataVO m_pubData=null;
	private String m_strRepPK=null;
	private String m_strTaskPK=null;
	private boolean m_bKeyValid=true;
	private IPostRepDataEditorActive postActive=null;
	
	public RepDataCondVO(String strAloneID,MeasurePubDataVO pubData,String strRepPK,String strTaskPK,boolean bKeyValid,IPostRepDataEditorActive postActive){
		this.m_pubData=pubData;
		this.m_strAloneID=strAloneID;
		this.m_strRepPK=strRepPK;
		this.m_strTaskPK=strTaskPK;
		this.m_bKeyValid=bKeyValid;
		this.postActive=postActive;
	}
	
	public String getAloneID() {
		return m_strAloneID;
	}

	public String getRepPK() {
		return m_strRepPK;
	}

	public MeasurePubDataVO getPubData() {
		return m_pubData;
	}
	
	public String getTaskPK() {
		return m_strTaskPK;
	}

	public String getEntityName() {
		return null;
	}
	
	public IPostRepDataEditorActive getPostRepDataActive(){
		return postActive;
	}

	public void validate() throws ValidationException {
	}

	public boolean isKeyValid() {
		return m_bKeyValid;
	}
}
