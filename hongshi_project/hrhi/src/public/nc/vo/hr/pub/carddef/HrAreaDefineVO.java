package nc.vo.hr.pub.carddef;

import nc.vo.hi.pub.CommonValue;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.hr.global.GlobalTool;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * HR��Ƭ����������VO
 * @author wangxing
 *
 */
public class HrAreaDefineVO extends ValueObject {

	/**
	 * ���л�ID
	 */
	private static final long serialVersionUID = 4568567234524762422L;

	public static final String CELLSMODEL_EXT_NAME = "NC_HR_CARD_RPT_AREA_EXT";
	
	private int startCellRow = -1;
	private int startCellCol = -1;
	private int endCellRow = -1;
	private int endCellCol = -1;
	private int areaHeight = 0;
	private int areaWidth = 0;
	// ������λ��WHERE����
	private ItemConditionVO[] subSetLocationConditionVOs = null;
	
	// ��λ��ʽ��Ĭ��Ϊ��Ŷ�λ
	private int subLocation = HrCellDataVO.SUBSET_LOCATION_ORDER;
	// ��Ŷ�λ����ȡֵ��ʽ,Ĭ��Ϊ���
	private int subSetLocationOrder = HrCellDataVO.SUBSET_LOCATION_ORDER_LAST;
	// ��������
	private int subSetLocationOrderCount = 1;
	// ��Ա��Ϣ������,����
	private String psnSetCode = null;
	// �ֶ������飬���ձ�����˳��
	private String[] psnFldCodeArray = null;
	// �ֶ���������
	private int[] psnFldDataTypeArray = null;
	// �ֶβ�������
	private String[] psnFldRefTypeArray = null;
	// �ֶ���Area����Ե�������,��һ������Ϊ0
	private int[] psnFldColIdxArray = null;
	
	/**
	 * ���캯��
	 *
	 */
	public HrAreaDefineVO() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the psnFldDataTypeArray
	 */
	public int[] getPsnFldDataTypeArray() {
		return psnFldDataTypeArray;
	}


	/**
	 * @param psnFldDataTypeArray the psnFldDataTypeArray to set
	 */
	public void setPsnFldDataTypeArray(int[] psnFldDataTypeArray) {
		this.psnFldDataTypeArray = psnFldDataTypeArray;
	}
	
	/**
	 * 
	 */
	public boolean equals(Object o) {
		if(!(o instanceof HrAreaDefineVO)){
			return false;
		}//end if
		HrAreaDefineVO cmpVO = (HrAreaDefineVO)o;
		
		
		return this.startCellRow==cmpVO.getStartCellRow() && 
		this.startCellCol==cmpVO.getStartCellCol() && 
		this.endCellRow==cmpVO.getEndCellRow()&& 
		this.endCellCol==cmpVO.getEndCellCol() && 
		this.areaWidth==cmpVO.getAreaWidth() && 
		this.areaHeight==cmpVO.getAreaHeight();
	}


	/**
	 * ������ʼCell
	 *
	 */
	public void setStartCell(int row, int col){
		startCellRow = row;
		startCellCol = col;
	}
	
	/**
	 *
	 */
	public int getStartCellRow(){
		return startCellRow;
	}
	
	/**
	 *
	 */
	public int getStartCellCol(){
		return startCellCol;
	}
	
	/**
	 * ������ʼCell
	 *
	 */
	public void setEndCell(int row, int col){
		endCellRow = row;
		endCellCol = col;
	}
	
	/**
	 *
	 */
	public int getEndCellRow(){
		return endCellRow;
	}
	
	/**
	 *
	 */
	public int getEndCellCol(){
		return endCellCol;
	}

	/**
	 * 
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return HrAreaDefineVO.class.getName();
	}

	/**
	 * 
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}
	
	/**
	 * �ж�һ��Cell�Ƿ����ڱ�����
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isBelongtoThisArea(int row, int col){
		if(row<0 || col<0){
			return false;
		}
		
		if(row>=startCellRow && row<=endCellRow && 
				col>=startCellCol && col<=endCellCol){
			return true;
		}else
			return false;
	}

	/**
	 * @return the areaHeight
	 */
	public int getAreaHeight() {
		return areaHeight;
	}

	/**
	 * @param areaHeight the areaHeight to set
	 */
	public void setAreaHeight(int areaHeight) {
		this.areaHeight = areaHeight;
	}

	/**
	 * @return the areaWidth
	 */
	public int getAreaWidth() {
		return areaWidth;
	}

	/**
	 * @param areaWidth the areaWidth to set
	 */
	public void setAreaWidth(int areaWidth) {
		this.areaWidth = areaWidth;
	}


	/**
	 * @return the subSetLocationConditionVOs
	 */
	public ItemConditionVO[] getSubSetLocationConditionVOs() {
		return subSetLocationConditionVOs;
	}


	/**
	 * @param subSetLocationConditionVOs the subSetLocationConditionVOs to set
	 */
	public void setSubSetLocationConditionVOs(
			ItemConditionVO[] subSetLocationConditionVOs) {
		this.subSetLocationConditionVOs = subSetLocationConditionVOs;
	}

	/**
	 * @return the subSetLocationOrder
	 */
	public int getSubSetLocationOrder() {
		return subSetLocationOrder;
	}


	/**
	 * @param subSetLocationOrder the subSetLocationOrder to set
	 */
	public void setSubSetLocationOrder(int subSetLocationOrder) {
		this.subSetLocationOrder = subSetLocationOrder;
	}


	/**
	 * @return the subSetLocationOrderCount
	 */
	public int getSubSetLocationOrderCount() {
		return subSetLocationOrderCount;
	}


	/**
	 * @param subSetLocationOrderCount the subSetLocationOrderCount to set
	 */
	public void setSubSetLocationOrderCount(int subSetLocationOrderCount) {
		this.subSetLocationOrderCount = subSetLocationOrderCount;
	}


	/**
	 * @return the subLocation
	 */
	public int getSubLocation() {
		return subLocation;
	}


	/**
	 * @param subLocation the subLocation to set
	 */
	public void setSubLocation(int subLocation) {
		this.subLocation = subLocation;
	}


	/**
	 * @return the psnSetCode
	 */
	public String getPsnSetCode() {
		return psnSetCode;
	}


	/**
	 * @param psnSetCode the psnSetCode to set
	 */
	public void setPsnSetCode(String psnSetCode) {
		this.psnSetCode = psnSetCode;
	}
	
	
	/**
	 * �õ��м�¼��λ�ı��
	 * @return
	 */
	public int toRowLocateFlag(){
		if(subLocation==HrCellDataVO.SUBSET_LOCATION_ORDER){
			switch(subSetLocationOrder){
			// ����ڼ���,��HR_FLDCODE_RECORDNUM=���ֵ��ʼ����ȡ�ڼ�����¼
			case HrCellDataVO.SUBSET_LOCATION_ORDER_FIRSTNO:
			// ����ڼ���, ��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ�ڼ�����¼	
			case HrCellDataVO.SUBSET_LOCATION_ORDER_LASTNO:{
				return this.subSetLocationOrderCount;
			}
			//�������, ��HR_FLDCODE_RECORDNUM=���ֵ ��ʼ����ȡ������¼
			case HrCellDataVO.SUBSET_LOCATION_ORDER_FIRST:
			// �������,��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ������¼
			case HrCellDataVO.SUBSET_LOCATION_ORDER_LAST:{
				return 0;
			}
			default:{
				return 0;
			}//end default
			}//end switch
		}else{
			return 0;
		}
	}
	
	// �Ƿ���Ҫ��ת����
	public boolean isNeedUpsetdown(){
		switch(subSetLocationOrder){
		// ����ڼ���,��HR_FLDCODE_RECORDNUM=���ֵ��ʼ����ȡ�ڼ�����¼
		case HrCellDataVO.SUBSET_LOCATION_ORDER_FIRSTNO:{
			return false;
		}
		//�������, ��HR_FLDCODE_RECORDNUM=���ֵ ��ʼ����ȡ������¼
		case HrCellDataVO.SUBSET_LOCATION_ORDER_FIRST:{
			return false;
		}
		//����ڼ���, ��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ�ڼ�����¼
		case HrCellDataVO.SUBSET_LOCATION_ORDER_LASTNO:{
			return true;
		}
		// �������,��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ������¼
		case HrCellDataVO.SUBSET_LOCATION_ORDER_LAST:{
			return true;
		}
		default:{
			return false;
		}
		}
	}
	
	public int getResultSize(){
		if(subLocation==HrCellDataVO.SUBSET_LOCATION_ORDER){
			return subSetLocationOrderCount>areaHeight?areaHeight:subSetLocationOrderCount;
		// ������λ
		}else{
			return areaHeight;
		// ������λ
		}//end if
	}
	
	/**
	 * ���ɵ���Cell��SQL���
	 * @param pk_psndoc
	 * @param pk_psnbasdoc
	 * @param pk_corp
	 * @param isPartTime
	 * @return
	 */
	public String toSqlCondition(String pk_psndoc, String pk_psnbasdoc,
			String pk_psndeptchg, String pk_corp, boolean isPartTime,
			BusinessFuncParser_sql funcParser) throws Exception {
		// ����SQLͷ
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" select ");
//		if(subLocation==HrCellDataVO.SUBSET_LOCATION_ORDER){
//			sqlBuf.append(" top ");
//			sqlBuf.append(subSetLocationOrderCount>areaHeight?areaHeight:subSetLocationOrderCount);
//			sqlBuf.append(" * from ( select ");
//		// ������λ
//		}else{
//			sqlBuf.append(" top ");
//			sqlBuf.append(areaHeight);
//			sqlBuf.append(" * from ( select ");
//		// ������λ
//		}//end if
		
		// ˳������ֶ�
		for(int i=0; i<this.psnFldCodeArray.length; i++){
			if (this.psnFldCodeArray[i].indexOf("UFAGE[") >= 0) {
				String trueFields = nc.vo.hr.global.DateFormulaParse.proDateFormula(this.psnFldCodeArray[i], this.psnSetCode.equalsIgnoreCase("hi_psndoc_part")?"hi_psndoc_deptchg":this.psnSetCode);
				sqlBuf.append(trueFields);
			}else if (this.psnFldCodeArray[i].indexOf(CommonValue.UFFORMULA_DATA) >= 0) {
				String trueFields = GlobalTool.proDateFormulaSql(this.psnFldCodeArray[i],
						this.psnSetCode, funcParser);
				sqlBuf.append(trueFields);
			}else
				sqlBuf.append(psnFldCodeArray[i]);
			if(i<(this.psnFldCodeArray.length-1))
				sqlBuf.append(", ");
			else
				sqlBuf.append(" ");
		}
		sqlBuf.append(" from ");
		sqlBuf.append(this.psnSetCode.equalsIgnoreCase("hi_psndoc_part")?"hi_psndoc_deptchg":this.psnSetCode);
		sqlBuf.append(" where ");
		// �����û�
		// �����ҵ���Ӽ�
		if(CommonUtils.isPsnBusinessSet(this.psnSetCode)){
			sqlBuf.append(" pk_psndoc='");
			sqlBuf.append(pk_psndoc);
			sqlBuf.append("' ");
			
		// �������ͨ�Ӽ�
		}else{
			sqlBuf.append(" pk_psnbasdoc='");
			sqlBuf.append(pk_psnbasdoc);
			sqlBuf.append("' ");
		}
		// ���⴦��
		if (this.psnSetCode.equalsIgnoreCase("hi_psndoc_part")) {
			sqlBuf.append(" and jobtype<>0 ");
		} else if (this.psnSetCode.equalsIgnoreCase("hi_psndoc_deptchg")) {
			sqlBuf.append(" and jobtype=0 ");
		} else if (this.psnSetCode.equalsIgnoreCase("hi_psndoc_training")) {
			sqlBuf.append(" and approveflag = 2 ");
		} else if (this.psnSetCode.equalsIgnoreCase("hi_psndoc_ctrt")) {
			sqlBuf.append(" and isrefer = 'Y' ");
		}
		
		// ��Ŷ�λ
		if(subLocation==HrCellDataVO.SUBSET_LOCATION_ORDER){
			switch(subSetLocationOrder){
				// ����ڼ���,��HR_FLDCODE_RECORDNUM=���ֵ��ʼ����ȡ�ڼ�����¼
				case HrCellDataVO.SUBSET_LOCATION_ORDER_FIRSTNO:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HrCellDataVO.HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" desc ");
					break;
				}
				//�������, ��HR_FLDCODE_RECORDNUM=���ֵ ��ʼ����ȡ������¼
				case HrCellDataVO.SUBSET_LOCATION_ORDER_FIRST:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HrCellDataVO.HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" desc ");
					break;
				}
				//����ڼ���, ��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ�ڼ�����¼
				case HrCellDataVO.SUBSET_LOCATION_ORDER_LASTNO:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HrCellDataVO.HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" asc ");
					break;
				}
				// �������,��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ������¼
				case HrCellDataVO.SUBSET_LOCATION_ORDER_LAST:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HrCellDataVO.HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" asc ");
					break;
				}
				default:{
					break;
				}
			}
		// ������λ
		}else{
			// �ж������Ƿ�Ϊ��
			if(this.subSetLocationConditionVOs!=null && this.subSetLocationConditionVOs.length>0){
				for(int i=0; i<this.subSetLocationConditionVOs.length; i++){
					sqlBuf.append(" and ");
					sqlBuf.append(subSetLocationConditionVOs[i].toSqlCondition());
				}//end if
			}//end if
			// �����õ�������������SQL�����
			sqlBuf.append(" order by ");
			sqlBuf.append(HrCellDataVO.HR_FLDCODE_RECORDNUM);
			sqlBuf.append(" asc ");
		}//end if
		
//		sqlBuf.append(" ) ");
		
		return sqlBuf.toString();
	}
	
	/**
	 * ��д�Ŀ�¡����
	 */
	public Object clone() {
		HrAreaDefineVO newvo = new HrAreaDefineVO();
		newvo.setAreaHeight(this.getAreaHeight());
		newvo.setAreaWidth(this.getAreaWidth());
		newvo.setEndCell(this.getEndCellRow(), this.getEndCellCol());
		newvo.setPsnSetCode(this.getPsnSetCode());
		newvo.setStartCell(this.getStartCellRow(), this.getStartCellCol());
		newvo.setSubLocation(this.getSubLocation());
		newvo.setSubSetLocationOrder(this.getSubSetLocationOrder());
		ItemConditionVO[] vos = null;
		if(subSetLocationConditionVOs!=null && subSetLocationConditionVOs.length>0){
			vos = new ItemConditionVO[subSetLocationConditionVOs.length];
			for(int i=0; i<vos.length; i++){
				vos[i] = subSetLocationConditionVOs[i]==null?null:(ItemConditionVO)subSetLocationConditionVOs[i].clone();
			}
		}
		newvo.setSubSetLocationConditionVOs(vos);
		newvo.setSubSetLocationOrderCount(this.getSubSetLocationOrderCount());
		newvo.setPsnSetCode(psnSetCode);
		
		// 
		String[] newFlds = new String[psnFldCodeArray.length];
		System.arraycopy(psnFldCodeArray, 0, newFlds, 0, newFlds.length);
		newvo.setPsnFldCodeArray(newFlds);
		
		String[] newFldRefTypes = new String[psnFldRefTypeArray.length];
		System.arraycopy(psnFldRefTypeArray, 0, newFldRefTypes, 0, newFldRefTypes.length);
		newvo.setPsnFldRefTypeArray(newFldRefTypes);
		
		int[] newFldDTs = new int[psnFldDataTypeArray.length];
		System.arraycopy(psnFldDataTypeArray, 0, newFldDTs, 0, newFldDTs.length);
		newvo.setPsnFldDataTypeArray(newFldDTs);
		
		int[] newFldCIndxs = new int[psnFldColIdxArray.length];
		System.arraycopy(psnFldColIdxArray, 0, newFldCIndxs, 0, newFldCIndxs.length);
		newvo.setPsnFldColIdxArray(newFldCIndxs);
		
		return newvo;
	}


	/**
	 * @return the psnFldCodeArray
	 */
	public String[] getPsnFldCodeArray() {
		return psnFldCodeArray;
	}


	/**
	 * @param psnFldCodeArray the psnFldCodeArray to set
	 */
	public void setPsnFldCodeArray(String[] psnFldCodeArray) {
		this.psnFldCodeArray = psnFldCodeArray;
	}


	/**
	 * @return the psnFldColIdxArray
	 */
	public int[] getPsnFldColIdxArray() {
		return psnFldColIdxArray;
	}


	/**
	 * @param psnFldColIdxArray the psnFldColIdxArray to set
	 */
	public void setPsnFldColIdxArray(int[] psnFldColIdxArray) {
		this.psnFldColIdxArray = psnFldColIdxArray;
	}


	/**
	 * @return the psnFldRefTypeArray
	 */
	public String[] getPsnFldRefTypeArray() {
		return psnFldRefTypeArray;
	}


	/**
	 * @param psnFldRefTypeArray the psnFldRefTypeArray to set
	 */
	public void setPsnFldRefTypeArray(String[] psnFldRefTypeArray) {
		this.psnFldRefTypeArray = psnFldRefTypeArray;
	}
}
