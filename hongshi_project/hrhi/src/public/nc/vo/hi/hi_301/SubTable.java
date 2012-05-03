package nc.vo.hi.hi_301;

/**
 * �ӱ��װ��
 * �������ڣ�(2004-5-13 10:38:37)
 * @author��Administrator
 */
import java.util.*;
import nc.vo.pub.*;

public class SubTable implements java.io.Serializable,Cloneable {
	//�ӱ����
	private java.lang.String tableCode;
	//�ӱ��¼
	private Vector records=new Vector();

/**
 * SubTable ������ע�⡣
 */
public SubTable() {
	super();
}
	public Object clone(){
		SubTable subTable;
		try{
			subTable=(SubTable)super.clone();
		}catch(Exception e){
			subTable=new SubTable();
		}
		subTable.records=new Vector();
		for(int i=0;i<records.size();i++){
			GeneralVO gvo=(GeneralVO)records.elementAt(i);
			subTable.records.addElement(gvo.clone());
		}
		return subTable;
	}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-13 20:49:08)
 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public Vector getRecords() {
	return records;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-13 10:40:10)
 * @return java.lang.String
 */
public java.lang.String getTableCode() {
	return tableCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-21 12:01:44)
 * @param data nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setRecordArray(CircularlyAccessibleValueObject[] data) {
	records.removeAllElements();
	for(int i=0;i<data.length;i++)
		records.addElement(data[i]);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-13 20:49:08)
 * @param newRecords nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setRecords(Vector newRecords) {
	records=newRecords;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-13 10:40:10)
 * @param newTableCode java.lang.String
 */
public void setTableCode(java.lang.String newTableCode) {
	tableCode = newTableCode;
}
}
