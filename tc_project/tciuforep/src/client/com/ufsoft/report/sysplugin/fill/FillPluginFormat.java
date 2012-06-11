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
	/**数值序列时的差值*/
	private double m_dblDiff = 0;
	/**数值序列时的前缀*/
	private String m_strPrefix = "";
	/**数值序列时的后缀*/
	private String m_strSuffix = "";

	/**特定序列
	 * @i18n short_monday=一
	 * @i18n short_tuesday=二
	 * @i18n short_wednesday=三
	 * @i18n short_thursday=四
	 * @i18n short_friday=五
	 * @i18n short_saturday=六
	 * @i18n short_sunday=日
	 * @i18n report00033=星期一
	 * @i18n report00034=星期二
	 * @i18n report00035=星期三
	 * @i18n report00036=星期四
	 * @i18n report00037=星期五
	 * @i18n report00049=星期六
	 * @i18n report00050=星期日
	 * @i18n report00051=一月
	 * @i18n report00052=二月
	 * @i18n report00053=三月
	 * @i18n report00054=四月
	 * @i18n report00055=五月
	 * @i18n report00056=六月
	 * @i18n report00057=七月
	 * @i18n report00058=八月
	 * @i18n report00059=九月
	 * @i18n report00060=十月
	 * @i18n report00061=十一月
	 * @i18n report00062=十二月*/
	private static String[][] strss = {
			{MultiLang.getString("short_monday"), MultiLang.getString("short_tuesday"), MultiLang.getString("short_wednesday"), MultiLang.getString("short_thursday"), MultiLang.getString("short_friday"), MultiLang.getString("short_saturday"), MultiLang.getString("short_sunday")},
			{MultiLang.getString("report00033"), MultiLang.getString("report00034"), MultiLang.getString("report00035"), MultiLang.getString("report00036"), MultiLang.getString("report00037"), MultiLang.getString("report00049"), MultiLang.getString("report00050")},
			{MultiLang.getString("report00051"), MultiLang.getString("report00052"), MultiLang.getString("report00053"), MultiLang.getString("report00054"), MultiLang.getString("report00055"), MultiLang.getString("report00056"), MultiLang.getString("report00057"), MultiLang.getString("report00058"), MultiLang.getString("report00059"), MultiLang.getString("report00060"), MultiLang.getString("report00061"), MultiLang.getString("report00062")},
			{"zero","one","two","three","four","five","six","seven","eight","nine","ten"},
			{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}
	};
	/**特定序列时的步长*/
	private int m_intFoot = 1;
	/**特定序列时的序列*/
	private String[] strs;
	
	/**是否序列填充*/
	private boolean m_bIsSequence = false;
	/**是否数值序列,否则是特定序列*/
	private boolean m_bIsNbrSeq = false;
	/**是否序列填充的用户回答结果，一次操作用户只回答一次*/
	private boolean userAnswer ;
	
	/**按填充方西填充时，当前单元的移动方向*/
	private int dRow;
	private int dCol;
	/**
	 *构造函数，一次操作只使用一个实例，当非一行一列时避免出现多次的序列填充的提示。
	 * @param isSequenceFill 
	 */
	public FillPluginFormat(boolean isSequenceFill){
		userAnswer = isSequenceFill;
	}
	/**
	 * 根据一个序列的前两个元素，初始化format类。
	 * @param first
	 * @param second
	 */
	public void init(String first, String second) {
		m_bIsSequence = isCanSequence(first,second);
	}
	
	/**
	 * 根据format实例，从序列的前一个值得到下一个值。
	 * @param str
	 * @return
	 */
	public String getNextValue(String str) {
		if(m_bIsSequence){//序列方式
			if(m_bIsNbrSeq){//数字序列
				str = splitStr(str)[1];
				double d = Double.parseDouble(str);
				d = d+m_dblDiff;
				if(Math.abs(Math.round(d)-d) < 0.001){//整形量
					return m_strPrefix+Math.round(d)+m_strSuffix;
				}else{//小数量,修改成同精度.
					int precision = str.length()-str.indexOf('.')-1;
					d = UfoPublic1.roundDouble(d,precision);
					return m_strPrefix+d+m_strSuffix;
				}
			}else{//特定序列
				int index = indexOf(strs,str);
				index = index+m_intFoot;
				index = index >= strs.length?index-strs.length:index;
				return strs[index];
			}
		}else{//复制方式.
			return str;
		}
	}
	/**
	 * 判断是否可以按序列化方法填充，同时初始化format的变量。
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
		if (row != -1) {//进入特定序列判断
			if (row == rowIndexOf(strss, second) && userAnswer) {
				m_bIsNbrSeq = false;
				strs = strss[row];
				m_intFoot = indexOf(strs,second) - indexOf(strs,first);
				return true;
			} else {
				return false;
			}
		} else if (isNumber(first)) {//进入数字序列判断
			if (isNumber(second) && userAnswer) {
				m_bIsNbrSeq = true;
				m_dblDiff = Double.parseDouble(second)
						- Double.parseDouble(first);
				return true;
			} else {
				return false;
			}
		} else {//进入普通字符串包含数字序列的判断
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
	 * 把一个含有数字的字符串拆分成三部分，如ab12cd被拆分为ab、12、cd三个字符串。
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
					//结束
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
	 * 得到字符串在字符串数组中的位置。
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
	 * 字符串是否是数字。
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
	 * 一个字符串在一个字符串矩阵中的所在行。
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
	 * @param i 填充时得到下一个单元的行增量。
	 * @param j 填充时得到下一个单元的列增量。
	 */
	public void setPosChange(int dRow, int dCol) {
		this.dRow = dRow;
		this.dCol = dCol;		
	}
	/**
	 * 填充时得到当前单元的下一个单元。
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

 