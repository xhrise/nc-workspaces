package com.ufsoft.report.sysplugin.fill;

import javax.swing.JOptionPane;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic1;
import com.ufsoft.table.CellPosition;

/**
 * <pre>
 * </pre>
 * @author zzl
 * @version 5.0
 * Create on 2004-10-28
 */
public class FillPluginFormat {
	/**��ֵ����ʱ�Ĳ�ֵ*/
	private double m_dblDiff = 0;
	/**��ֵ����ʱ��ǰ׺*/
	private String m_strPrefix = "";
	/**��ֵ����ʱ�ĺ�׺*/
	private String m_strSuffix = "";

	/**�ض�����
	 * @i18n short_monday=һ
	 * @i18n short_tuesday=��
	 * @i18n short_wednesday=��
	 * @i18n short_thursday=��
	 * @i18n short_friday=��
	 * @i18n short_saturday=��
	 * @i18n short_sunday=��
	 * @i18n report00033=����һ
	 * @i18n report00034=���ڶ�
	 * @i18n report00035=������
	 * @i18n report00036=������
	 * @i18n report00037=������
	 * @i18n report00049=������
	 * @i18n report00050=������
	 * @i18n report00051=һ��
	 * @i18n report00052=����
	 * @i18n report00053=����
	 * @i18n report00054=����
	 * @i18n report00055=����
	 * @i18n report00056=����
	 * @i18n report00057=����
	 * @i18n report00058=����
	 * @i18n report00059=����
	 * @i18n report00060=ʮ��
	 * @i18n report00061=ʮһ��
	 * @i18n report00062=ʮ����*/
	private static String[][] strss = {
			{MultiLang.getString("short_monday"), MultiLang.getString("short_tuesday"), MultiLang.getString("short_wednesday"), MultiLang.getString("short_thursday"), MultiLang.getString("short_friday"), MultiLang.getString("short_saturday"), MultiLang.getString("short_sunday")},
			{MultiLang.getString("report00033"), MultiLang.getString("report00034"), MultiLang.getString("report00035"), MultiLang.getString("report00036"), MultiLang.getString("report00037"), MultiLang.getString("report00049"), MultiLang.getString("report00050")},
			{MultiLang.getString("report00051"), MultiLang.getString("report00052"), MultiLang.getString("report00053"), MultiLang.getString("report00054"), MultiLang.getString("report00055"), MultiLang.getString("report00056"), MultiLang.getString("report00057"), MultiLang.getString("report00058"), MultiLang.getString("report00059"), MultiLang.getString("report00060"), MultiLang.getString("report00061"), MultiLang.getString("report00062")},
			{"zero","one","two","three","four","five","six","seven","eight","nine","ten"},
			{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}
	};
	/**�ض�����ʱ�Ĳ���*/
	private int m_intFoot = 1;
	/**�ض�����ʱ������*/
	private String[] strs;
	
	/**�Ƿ��������*/
	private boolean m_bIsSequence = false;
	/**�Ƿ���ֵ����,�������ض�����*/
	private boolean m_bIsNbrSeq = false;
	/**�Ƿ����������û��ش�����һ�β����û�ֻ�ش�һ��*/
	private boolean userAnswer ;
	
	/**����䷽�����ʱ����ǰ��Ԫ���ƶ�����*/
	private int dRow;
	private int dCol;
	/**
	 *���캯����һ�β���ֻʹ��һ��ʵ��������һ��һ��ʱ������ֶ�ε�����������ʾ��
	 * @param isSequenceFill 
	 */
	public FillPluginFormat(boolean isSequenceFill){
		userAnswer = isSequenceFill;
	}
	/**
	 * ����һ�����е�ǰ����Ԫ�أ���ʼ��format�ࡣ
	 * @param first
	 * @param second
	 */
	public void init(String first, String second) {
		m_bIsSequence = isCanSequence(first,second);
	}
	
	/**
	 * ����formatʵ���������е�ǰһ��ֵ�õ���һ��ֵ��
	 * @param str
	 * @return
	 */
	public String getNextValue(String str) {
		if(m_bIsSequence){//���з�ʽ
			if(m_bIsNbrSeq){//��������
				str = splitStr(str)[1];
				double d = Double.parseDouble(str);
				d = d+m_dblDiff;
				if(Math.abs(Math.round(d)-d) < 0.001){//������
					return m_strPrefix+Math.round(d)+m_strSuffix;
				}else{//С����,�޸ĳ�ͬ����.
					int precision = str.length()-str.indexOf('.')-1;
					d = UfoPublic1.roundDouble(d,precision);
					return m_strPrefix+d+m_strSuffix;
				}
			}else{//�ض�����
				int index = indexOf(strs,str);
				index = index+m_intFoot;
				index = index >= strs.length?index-strs.length:index;
				return strs[index];
			}
		}else{//���Ʒ�ʽ.
			return str;
		}
	}
	/**
	 * �ж��Ƿ���԰����л�������䣬ͬʱ��ʼ��format�ı�����
	 * @param first
	 * @param second
	 * @return
	 */
	private boolean isCanSequence(String first, String second) {
		if(first == null || first.equals("") || 
		   second == null || second.equals("") || 
		   first.equals(second)){
			return false;
		}
		int row = rowIndexOf(strss, first);
		if (row != -1) {//�����ض������ж�
			if (row == rowIndexOf(strss, second) && userAnswer) {
				m_bIsNbrSeq = false;
				strs = strss[row];
				m_intFoot = indexOf(strs,second) - indexOf(strs,first);
				return true;
			} else {
				return false;
			}
		} else if (isNumber(first)) {//�������������ж�
			if (isNumber(second) && userAnswer) {
				m_bIsNbrSeq = true;
				m_dblDiff = Double.parseDouble(second)
						- Double.parseDouble(first);
				return true;
			} else {
				return false;
			}
		} else {//������ͨ�ַ��������������е��ж�
			String[] str1 = splitStr(first);
			String[] str2 = splitStr(second);
			if(str1[0].equals(str2[0]) && str1[2].equals(str2[2]) && !str1[1].equals("") && userAnswer){
				m_bIsNbrSeq = true;
				m_strPrefix = str1[0];
				m_strSuffix = str1[2];
				m_dblDiff = Double.parseDouble(str2[1]) - Double.parseDouble(str1[1]);
				return true;
			}else{
				return false;
			}
		}
	}



	/**
	 * ��һ���������ֵ��ַ�����ֳ������֣���ab12cd�����Ϊab��12��cd�����ַ�����
	 * @param first
	 * @return
	 */
	private String[] splitStr(String str) {
		boolean hasComma = false;
		boolean hasFind = false;
		StringBuffer s0 = new StringBuffer();
		StringBuffer s1 = new StringBuffer();
		StringBuffer s2 = new StringBuffer();
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			if(hasFind){
				if(c >= '0' && c <= '9' || (!hasComma && c == '.')){
					if(c == '.') {
						hasComma = true;
					}
					s1.append(c);
				}else{
					//����
					s2.append(str.substring(i));
					break;
				}
			}else{
				if(c >= '0' && c <= '9'){
					hasFind = true;
					if(i>0 && str.charAt(i-1) == '-'){
						s1.append(str.charAt(i-1));
						s0.deleteCharAt(s0.length()-1);
					}
					s1.append(c);
				}else{
					s0.append(c);
				}
				
			}
		}
		return new String[]{s0.toString(),s1.toString(),s2.toString()};
	}

	/**
	 * �õ��ַ������ַ��������е�λ�á�
	 * @param strs
	 * @param str
	 * @return
	 */
	private int indexOf(String[] strs, String str) {
		for(int i=0;i<strs.length;i++){
			if(strs[i].equals(str)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * �ַ����Ƿ������֡�
	 * @param str
	 * @return
	 */
	private static boolean isNumber(String str){
		try{
			Double.parseDouble(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * һ���ַ�����һ���ַ��������е������С�
	 * @param strss
	 * @param str
	 * @return
	 */
	private int rowIndexOf(String[][] strss, String str) {
		for(int i=0;i<strss.length;i++){
			if(indexOf(strss[i],str) != -1){
				return i;
			}
		}
		return -1;
	}
	/**
	 * @param i ���ʱ�õ���һ����Ԫ����������
	 * @param j ���ʱ�õ���һ����Ԫ����������
	 */
	public void setPosChange(int dRow, int dCol) {
		this.dRow = dRow;
		this.dCol = dCol;		
	}
	/**
	 * ���ʱ�õ���ǰ��Ԫ����һ����Ԫ��
	 * @return
	 */
	public CellPosition getNextCellPos(CellPosition pos) {
		//liuyy. 2007-04-04
		if (pos.getRow() + dRow < 0 || pos.getColumn() + dCol < 0) {
			return null;
		}
		return CellPosition.getInstance(pos.getRow() + dRow, pos.getColumn()
				+ dCol);
	}
}

 