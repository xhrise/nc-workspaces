package nc.ui.bi.query.freequery.measquery;

import java.util.Date;
import java.util.Hashtable;

import nc.ui.bi.query.manager.RptProvider;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.ICustomRef;
import com.ufida.dataset.ICustomRefModel;
import com.ufida.dataset.Provider;
import com.ufida.dataset.RefK;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.inputcore.AccTimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.CodeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.inputcore.TimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.UnitRefTextField;
import com.ufsoft.iufo.resource.StringResource;

public class IUFO2NCRef implements ICustomRef {
	private RptProvider rptProvider=null;
	private Hashtable<RefK,ICustomRefModel> refs=new Hashtable<RefK,ICustomRefModel>();
	
	static{
		RefData.setProxy(new IufoRefData());
	}

	public ICustomRefModel getRefModel(RefK refModelName) {
		return refs.get(refModelName);
	}

	/**
	 * @i18n miufo1000751=ֵ
	 */
	public RefK[] getRefModelList() {
		if(refs.size()==0){
			if(rptProvider!=null){
				if(rptProvider.getMeasQeuryModelDef()!=null){
				    KeyGroupVO keygroup = rptProvider.getMeasQeuryModelDef().getKeyGroupVO();
				    if (keygroup != null) {
				    	ICustomRefModel keyRef=null;
				    	KeyVO key=null;
						KeyVO[] keyVOs = keygroup.getKeys();
						RefK ref=null;
						for (int i = 0; i < keyVOs.length; i++) {
							key=keyVOs[i];
							keyRef=getKeyRef(key);
							
							if (keyRef != null) {
								if (key.isAccPeriodKey()) {
									ref = new RefK(
											StringResource
													.getStringResource("miufo5508000001"),
											key.getKeywordPK());
									refs.put(ref, keyRef);
								} else if (key.isTimeKeyVO()) {
									ref = new RefK(StringResource
											.getStringResource("miufo1003135"),
											key.getKeywordPK());
									refs.put(ref, keyRef);
								} else if (key.getType() == KeyVO.TYPE_REF) {
									ref = new RefK(
											key.getName()
													+ StringResource
															.getStringResource("miufo1000751"),
											key.getKeywordPK());
									refs.put(ref, keyRef);
								} else {
									ref = new RefK(key.getName(), key
											.getKeywordPK());
									refs.put(ref, keyRef);
								}
							}
						}
					}
				}
			}
		}
		return refs.keySet().toArray(new RefK[0]);
	}

	public void setProvider(Provider provider) {
		if(provider instanceof RptProvider){
			this.rptProvider=(RptProvider)provider;
		}
	}
	
	private ICustomRefModel getKeyRef(KeyVO key){
		ICustomRefModel keyRef=null;
		if(key.isTimeKeyVO()){
			keyRef=new IUFO2NCRefModel(new TimeRefTextField(key.getKeywordPK(),new Date()));
		}else if(key.isAccPeriodKey()){
			String strAccPreiodPk = null;
			if(this.rptProvider!=null){
				Context context=rptProvider.getContext() ;
				if(context!=null){
					strAccPreiodPk=InputUtil.getAccSechemePK((String)context.getAttribute(IUfoContextKey.TASK_PK));
				}
				
			}
			String strKeyWordPk=key.getKeywordPK();
			if(strAccPreiodPk!=null){
			keyRef=new IUFO2NCRefModel(new AccTimeRefTextField(null,strAccPreiodPk,strKeyWordPk));
			}
		}else if(KeyVO.isUnitKeyVO(key)||KeyVO.isDicUnitKeyVO(key)){
			String strLoginUnitPK=null;
			if(this.rptProvider!=null){
				Context context=rptProvider.getContext() ;
				if(context!=null){
					strLoginUnitPK=(String)context.getAttribute(IUfoContextKey.LOGIN_UNIT_ID);
					if(strLoginUnitPK==null){
						strLoginUnitPK=(String)context.getAttribute(IUfoContextKey.CUR_UNIT_ID);
					}
				}
			}
			if(strLoginUnitPK!=null){
				UnitInfoVO unitInfo = GeneralQueryUtil.findUnitInfoByPK(strLoginUnitPK);
				keyRef=new IUFO2NCRefModel(new UnitRefTextField(unitInfo.getCode(), "level_code"));
			}
		}else if(key.getRef()!=null){
			keyRef=new IUFO2NCRefModel(new CodeRefTextField(key.getRef()));
		}else{
		   keyRef=new IUFO2NCRefModel(new UITextField());
		}
		
		return keyRef;
	}

}
 