package nc.vo.hr.pub.carddef;

import java.lang.reflect.Array;

import nc.vo.hr.tools.pub.StringUtils;

public class CommonUtils {
	
	
	/**
	 * �����Ƿ��Ǽ��ŵķ���
	 * @param pk_corp
	 * @return
	 */
	public static boolean isGroup(String pk_corp){
		if(!StringUtils.hasText(pk_corp)){
			return false;
		}//end if
		
		return pk_corp.equalsIgnoreCase("0001");
	}
	
	/**
	 * �ж�һ���Ӽ��Ƿ���ҵ���Ӽ�
	 * @param setCode
	 * @return
	 */
	public static boolean isPsnBusinessSet(String setCode){
		if(!StringUtils.hasText(setCode)){
			return false;
		}
		
		return isExistInArray(CommonValue.PSN_BUSINESS_SET_CODES,setCode);
	}
	
	/**
	 * ����һ�������Ƿ������Ŀ��������
	 * @param ary
	 * @param o
	 * @return
	 */
	public static boolean isExistInArray(Object[] ary, Object o){
		if(ary==null || ary.length<=0 || o==null){
			return false;
		}//end if
		
		for(int i=0; i<ary.length; i++){
			if(o.equals(ary[i])){
				return true;
			}
		}//end for
		return false;
	}
	
	/**
	 * ��תһ�����飬����һ�������飬ԭ���鲻��
	 * @param ary1
	 * @param ary2
	 * @return
	 */
	public static Object[] reverseArray(Object[] srcAry){
		if(srcAry==null || srcAry.length<=0){
			return srcAry;
		}
		
		Object[] newArray = (Object[])Array.newInstance(srcAry[0].getClass(), srcAry.length);
		
		for(int i=0; i<newArray.length; i++){
			newArray[i] = srcAry[newArray.length-1-i];
		}//end if
		
		return newArray;
	}
	
	/**
	 * �ϲ��������飬ע���������������ͬһ����
	 * @param ary1
	 * @param ary2
	 * @return
	 */
	public static Object[] mergeTwoArray(Object[] ary1, Object[] ary2){
		if(ary1==null || ary1.length<=0){
			return ary2;
		}else if(ary2==null || ary2.length<=0){
			return ary1;
		}
		
		Object[] newArray = (Object[])Array.newInstance(ary1[0].getClass(), ary1.length+ary2.length);
		System.arraycopy(ary1, 0, newArray, 0, ary1.length);
		System.arraycopy(ary2, 0, newArray, ary1.length, ary2.length);
		
		return newArray;
	}
	
	/**
	 * ��������Ȩ�޲���Ȩ�޴���
	 * @param isView
	 * @param isDesign
	 * @param isDelete
	 * @return
	 */
	public static Integer getAuth(boolean isView, boolean isDesign, boolean isDelete){
//   			ɾ��   �޸�   ���
//		   1    0     0      1
//		   2    0     1      0 
//		   3    0     1      1 
//		   4    1     0      0 
//		   5    1     0      1 
//		   6    1     1      0
//		   7    1     1      1 
//		   0    0     0      0
		if(!isView && !isDesign && !isDelete){		//		   0    0     0      0
			return new Integer(0);
		}else if(isView && !isDesign && !isDelete){	//		   1    0     0      1
			return new Integer(1);
		}else if(!isView && isDesign && !isDelete){	//		   2    0     1      0 
			return new Integer(2);
		}else if(isView && isDesign && !isDelete){	//		   3    0     1      1  
			return new Integer(3);
		}else if(!isView && !isDesign && isDelete){	//		   4    1     0      0  
			return new Integer(4);
		}else if(isView && !isDesign && isDelete){	//		   5    1     0      1   
			return new Integer(5);
		}else if(!isView && isDesign && isDelete){	//		   6    1     1      0   
			return new Integer(6);
		}else if(isView && isDesign && isDelete){	//		   7    1     1      1   
			return new Integer(7);
		}else
			return new Integer(0);
	}
}
