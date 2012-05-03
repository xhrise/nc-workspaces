package nc.vo.hr.pub.carddef;

import java.util.Vector;

import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * HR卡片报表的数据VO，用来传递一个报表所需要的全部数据
 * @author wangxing
 *
 */
public class HrReportDataVO extends ValueObject {
	
	// 人员类型，默认为全职
	private int psnType = CommonValue.PSNTYPE_NORMAL;
	
	// 人员工作信息主键
	private String psnWorkPk = null;
	
	// 任职表主键
	private String pk_psndoc_sub = null;
	
	// 报表定义主键
	private String rptDefKey = null;

	// 单个Cell的查询和数据存放对象
	private HrCellDataVO[] singleCellsVOArray = null;
	
	// 区域定义数组
	private HrAreaDefineVO[] areaDefVOArray = null;
	
	// 区域数据缓存，该数据数组每一个元素都是GeneralVO[]，代表一个区域的数据
	private Vector<GeneralVO[]> areaDataArray = new Vector<GeneralVO[]>();
	
	/**
	 * 构造函数
	 *
	 */
	public HrReportDataVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return HrReportDataVO.class.getName();
	}

	/**
	 * 
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the areaDataArray
	 */
	public Vector<GeneralVO[]> getAreaDataArrayVector() {
		return areaDataArray;
	}

	/**
	 * @param areaDataArray the areaDataArray to set
	 */
	public void setAreaDataArrayVector(Vector<GeneralVO[]> areaDataArray) {
		this.areaDataArray = areaDataArray;
	}

	/**
	 * @return the areaDefVOArray
	 */
	public HrAreaDefineVO[] getAreaDefVOArray() {
		return areaDefVOArray;
	}

	/**
	 * @param areaDefVOArray the areaDefVOArray to set
	 */
	public void setAreaDefVOArray(HrAreaDefineVO[] areaDefVOArray) {
		this.areaDefVOArray = areaDefVOArray;
	}

	/**
	 * @return the singleCellsVOArray
	 */
	public HrCellDataVO[] getSingleCellsVOArray() {
		return singleCellsVOArray;
	}

	/**
	 * @param singleCellsVOArray the singleCellsVOArray to set
	 */
	public void setSingleCellsVOArray(HrCellDataVO[] singleCellsVOArray) {
		this.singleCellsVOArray = singleCellsVOArray;
	}

	/**
	 * @return the psnWorkPk
	 */
	public String getPsnWorkPk() {
		return psnWorkPk;
	}

	/**
	 * @param psnWorkPk the psnWorkPk to set
	 */
	public void setPsnWorkPk(String psnWorkPk) {
		this.psnWorkPk = psnWorkPk;
	}

	/**
	 * @return the psnType
	 */
	public int getPsnType() {
		return psnType;
	}

	/**
	 * @param psnType the psnType to set
	 */
	public void setPsnType(int psnType) {
		this.psnType = psnType;
	}

	/**
	 * @return the rptDefKey
	 */
	public String getRptDefKey() {
		return rptDefKey;
	}

	/**
	 * @param rptDefKey the rptDefKey to set
	 */
	public void setRptDefKey(String rptDefKey) {
		this.rptDefKey = rptDefKey;
	}

	/**
	 * @return the pk_psndoc_sub
	 */
	public String getPk_psndoc_sub() {
		return pk_psndoc_sub;
	}

	/**
	 * @param pk_psndoc_sub the pk_psndoc_sub to set
	 */
	public void setPk_psndoc_sub(String pk_psndoc_sub) {
		this.pk_psndoc_sub = pk_psndoc_sub;
	}

}
