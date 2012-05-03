package nc.ui.trn.records;

import nc.ui.hi.ref.PostRefModel;
import nc.ui.hr.frame.util.BillPanelUtils;
import nc.ui.hr.utils.Util;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.smtm.pub.CommonValue;
import nc.vo.pub.SuperVO;

import org.apache.commons.lang.StringUtils;

public class ListRefUtils {
	
	public static void setNotLeafSelectedEnabled(BillModel billModel){
		BillItem[] items = billModel.getBodyItems();
		for(BillItem item:items){
			if(item.getDataType() == IBillItem.UFREF){
				((UIRefPane)item.getComponent()).setNotLeafSelectedEnabled(item.isM_bNotLeafSelectedEnabled());
			}
		}
	}
	public static void clearRefValueByKey(String key,int row,BillModel billModel){		
		billModel.setValueAt(null, row, key);
		billModel.setValueAt(null, row, key+BillPanelUtils.REF_SHOW_NAME);
	}
	public static void setRefItemValueByPK(String key,int row,String pk,BillModel billModel){
		UIRefPane refpane = getRefPaneByKey(key, billModel);
		refpane.setPK(pk);
		billModel.setValueAt(refpane.getRefName(), row, key);
		billModel.setValueAt(pk, row, key+BillPanelUtils.REF_SHOW_NAME);
	}
	public static UIRefPane getRefPaneByKey(String key,BillModel billModel){
//		billModel.getItemByKey("pk_corp");
		
		if(key.indexOf(BillPanelUtils.REF_SHOW_NAME)<0){
			key+=BillPanelUtils.REF_SHOW_NAME;
		}
		BillItem item = billModel.getItemByKey(key);
		UIRefPane refpane = null;
		if (item != null && item.getDataType() == BillItem.UFREF){
			refpane =(UIRefPane) item.getComponent();
		}
		return refpane;
	}
	/**
	 * 
	 * @param strTabCode
	 * @param pk_corp
	 * @return
	 */
	public static UIRefPane resetPsnclRef(UIRefPane refpane,String strTabCode,String pk_corp){
		String wherePart = "";
		//去掉人员档案控制
		refpane.getRefModel().setUseDataPower(false);
		if(CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)){
			wherePart = " bd_psncl.psnclscope <> "
			+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_APPLY
			+ " and (bd_psncl.pk_corp='"
			+ pk_corp
			+ "' or bd_psncl.pk_corp='"
			+ nc.vo.hi.pub.CommonValue.GROUPCODE
			+ "')";
		}else if(CommonValue.HI_PSNDOC_PART.equals(strTabCode)){
			wherePart = " bd_psncl.psnclscope = "
				+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK
				+ " and (bd_psncl.pk_corp='"
				+ pk_corp
				+ "' or bd_psncl.pk_corp='"
				+ nc.vo.hi.pub.CommonValue.GROUPCODE
				+ "')";
		}else if(CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)){
			wherePart = " (pk_corp = '"
				+ nc.vo.hi.pub.CommonValue.GROUPCODE
				+ "' or pk_corp = '"
				+ nc.ui.hr.global.Global.getCorpPK()
				+ "')  and (psnclscope <> "
				+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK
				+ " and psnclscope <>"
				+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER
				+ ")";
		}
		if (!StringUtils.isBlank(wherePart)) {
			refpane.setWhereString(wherePart);
		}		
		refpane.getRefModel().reloadData();
		refpane.setButtonFireEvent(true);
		return refpane;
	}
	public static UIRefPane resetDutyRef(UIRefPane refpane,String strTabCode,String pk_corp,String jobseries){
		refpane.getRefModel().setMatchPkWithWherePart(true);
		String wherePart = null;
		if (Util.isDutyDependJobSeries ) {
			wherePart = " (duty.pk_corp='" + pk_corp
					+ "' or duty.pk_corp = '"
					+ nc.vo.hi.pub.CommonValue.GROUPCODE
					+ "') and sery.pk_defdoc=duty.series and sery.pk_defdoclist='HI000000000000000021'";
			if(!StringUtils.isBlank(jobseries)){
				wherePart+=" and sery.pk_defdoc='"+ jobseries + "' ";
			}
		} else {
			wherePart = " (duty.pk_corp='"
					+ pk_corp
					+ "' or duty.pk_corp = '"
					+ nc.vo.hi.pub.CommonValue.GROUPCODE
					+ "') and sery.pk_defdoc=duty.series and duty.series in(select pk_defdoc from bd_defdoc where pk_defdoclist='HI000000000000000020') ";
		}		
		if (!StringUtils.isBlank(wherePart)) {
			refpane.setWhereString(wherePart);
		}
		refpane.setButtonFireEvent(true);
		return refpane;
	}
	public static UIRefPane resetPostRef(UIRefPane refpane,String strTabCode,String pk_corp,String pk_deptdoc){
		if(!(refpane.getRefModel() instanceof PostRefModel)){
			refpane.setRefModel(new PostRefModel());
		}
		String wherePart = null;
		wherePart = " om_job.isabort='N' and (bd_corp.isseal is null or bd_corp.isseal<>'Y') and bd_corp.ishasaccount='Y'";
		if(!StringUtils.isBlank(pk_corp)){
			wherePart += " and bd_corp.pk_corp = '"+ pk_corp + "' ";
		}
		if(!StringUtils.isBlank(pk_deptdoc)){
			wherePart += " and om_job.pk_deptdoc = '"+pk_deptdoc+"' ";
		}
		refpane.setWhereString(wherePart);		
//		refpane.getRefModel().reloadData();
		refpane.setButtonFireEvent(true);
		refpane.updateUI();
		return refpane;
	}
	
	public static UIRefPane resetDeptType(UIRefPane refpane,String pk_corp){
		String wherePart = "(pk_corp = '"
			+ pk_corp + "' or pk_corp = '"
			+ nc.vo.hi.pub.CommonValue.GROUPCODE
			+ "') and pk_defdoclist = 'HI000000000000000020'";
		refpane.setWhereString(wherePart);
		refpane.getRefModel().reloadData();
		refpane.setButtonFireEvent(true);
		return refpane;
	}
	public static UIRefPane resetJobserial(UIRefPane refpane,String pk_corp){
		String wherePart = "(pk_corp = '"
			+ pk_corp
			+ "' or pk_corp = '"
			+ nc.vo.hi.pub.CommonValue.GROUPCODE
			+ "') and pk_defdoclist = 'HI000000000000000021'";
		refpane.setWhereString(wherePart);
		refpane.getRefModel().reloadData();
		refpane.setButtonFireEvent(true);
		refpane.updateUI();
		return refpane;
	}
	public static UIRefPane resetJobrank(UIRefPane refpane,String pk_corp){
		String wherePart = "(pk_corp = '"
			+ pk_corp
			+ "' or pk_corp = '"
			+ nc.vo.hi.pub.CommonValue.GROUPCODE
			+ "') and pk_defdoclist = 'HI000000000000000022'";
		
		if(refpane == null)
			return null;
		
		refpane.setWhereString(wherePart);
		refpane.getRefModel().reloadData();
		refpane.setButtonFireEvent(true);
		return refpane;
	}
	public static UIRefPane resetDeptRef(UIRefPane refpane,String strTabCode,String pk_corp){
		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
				||CommonValue.HI_PSNDOC_DIMISSION.equals(strTabCode)) {
			refpane.getRefModel().setUseDataPower(true);
		}else{
			refpane.getRefModel().setUseDataPower(false);
		}
		String tablename = refpane.getRefModel().getTableName(); 
		String wherePart = " bd_deptdoc.hrcanceled = 'N' ";
		if(tablename.indexOf("bd_corp")>0){
			wherePart += " and ( bd_corp.isseal is null or bd_corp.isseal <> 'Y' ) and bd_corp.ishasaccount = 'Y'"; 
		}		
		if(!StringUtils.isBlank(pk_corp)){
			wherePart += " and bd_deptdoc.pk_corp = '"+ pk_corp + "' ";
		}
		refpane.setWhereString(wherePart);
		refpane.getRefModel().reloadData();
		refpane.setButtonFireEvent(true);
		refpane.getRef().setRefType(1);
		return refpane;
	}
	
	public static void resetCorpRef(String strTabCode,String pk_corp,BillModel billModel){
		//部门
		resetDeptRef(getRefPaneByKey("pk_deptdoc", billModel), strTabCode, pk_corp);
		//人员类别
		resetPsnclRef(getRefPaneByKey("pk_psncl", billModel), strTabCode, pk_corp);
		//职务
		resetDutyRef(getRefPaneByKey("pk_om_duty", billModel), strTabCode, pk_corp, null);
//		职务簇
		resetDeptType(getRefPaneByKey("pk_detytype", billModel), pk_corp);
//		岗位序列
		resetJobserial(getRefPaneByKey("pk_jobserial", billModel), pk_corp);
//		岗位等级
		resetJobrank(getRefPaneByKey("pk_jobrank", billModel), pk_corp);
		//岗位
		resetPostRef(getRefPaneByKey("pk_postdoc", billModel), strTabCode, pk_corp, null);
		
	}
	public static void refreshCorpInfoRef(SuperVO selvalue,String strTabCode,String pk_corp,BillModel billModel){
		String corp = (String)selvalue.getAttributeValue("pk_corp");
		String partPk_corp = (corp != null ? corp : pk_corp);
		String pk_deptdoc =(String) selvalue.getAttributeValue("pk_deptdoc");
		String jobseries = (String)selvalue.getAttributeValue("pk_jobserial");
		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_PART.equals(strTabCode)) {			
			//人员类别
			UIRefPane psnclref = ListRefUtils.getRefPaneByKey("pk_psncl", billModel);
			ListRefUtils.resetPsnclRef(psnclref, strTabCode, partPk_corp);
			//部门
			UIRefPane deptref = ListRefUtils.getRefPaneByKey("pk_deptdoc", billModel);
			ListRefUtils.resetDeptRef(deptref, strTabCode, partPk_corp);
			//岗位
			UIRefPane postrefpane = ListRefUtils.getRefPaneByKey("pk_postdoc", billModel);
			ListRefUtils.resetPostRef(postrefpane, strTabCode, partPk_corp, pk_deptdoc);
			//岗位序列
			UIRefPane jobseriesrefpane = ListRefUtils.getRefPaneByKey("pk_jobserial", billModel);
			ListRefUtils.resetJobserial(jobseriesrefpane, partPk_corp);
			//岗位等级
			UIRefPane jobrankrefpane = ListRefUtils.getRefPaneByKey("pk_jobrank", billModel);
			ListRefUtils.resetJobrank(jobrankrefpane, partPk_corp);
			
			//职务
			UIRefPane dutyrefpane = ListRefUtils.getRefPaneByKey("pk_om_duty", billModel);
			ListRefUtils.resetDutyRef(dutyrefpane, strTabCode, partPk_corp, jobseries);
			//职务簇
			UIRefPane deptTyperefpane = ListRefUtils.getRefPaneByKey("pk_detytype", billModel);
			ListRefUtils.resetDeptType(deptTyperefpane, partPk_corp);				
		}
	}
	public static void setUserDataPower(String key,BillModel billModel,boolean power){
		UIRefPane refpane = getRefPaneByKey(key, billModel);
		if(refpane!=null){
			refpane.getRefModel().setUseDataPower(power);
			refpane.getRefModel().reloadData();
		}
	}
}
