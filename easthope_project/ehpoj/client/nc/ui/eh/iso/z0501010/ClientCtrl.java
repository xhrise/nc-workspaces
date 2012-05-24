package nc.ui.eh.iso.z0501010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.vo.eh.iso.z0501005.IsoBVO;
import nc.vo.eh.iso.z0501005.IsoVO;
import nc.vo.eh.pub.PubBillVO;

/** 
 * ˵����ԭ��������׼����������
 * @author ����Դ 
 * ʱ�䣺2008-4-11
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        return IBillType.eh_z0501005;
    }

	 @Override
	public String[] getBillVoName() {
	        return new String[]{
	            PubBillVO.class.getName(),
	            IsoVO.class.getName(),
	            IsoBVO.class.getName()    
	        };
	    }

	
	@Override
	public String getChildPkField() {
		return "pk_iso_b";
	}

	@Override
	public String getPkField() {
		return "pk_iso";
	}

	  @Override
	public int[] getListButtonAry() {
	        return nc.ui.eh.pub.PubTools.getSPLButton();
	    }

	@Override
	public int[] getCardButtonAry() {
		return nc.ui.eh.pub.PubTools.getSPCButton();
	}
	

}
