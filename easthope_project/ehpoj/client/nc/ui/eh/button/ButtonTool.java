package nc.ui.eh.button;

import java.util.LinkedList;

import nc.vo.eh.pub.Toolkits;

/**
 *    wqw
 * ˵��:��ť����,���ڰ�ť���׷��,����,ɾ��
 * @author LiuYuan
 * 2007-9-28 ����09:25:42
 */
public class ButtonTool {
    /**
     * ����:���밴ťinsbtn��tarbtns��ť���posǰ 
     * @param insbtn Ҫ����İ�ť
     * @param tarbtns ������İ�ť��
     * @param pos ���ڰ�ť��λ��
     * @return
     * @return:int[] �°�ť��
     * @author LiuYuan
     * 2007-9-28 ����10:44:12
     */
    public static int[] insertButton(int insbtn,int[] tarbtns,int pos){
        return ButtonTool.insertButtons(new int[]{insbtn}, tarbtns, pos);
    }
    
    /**
     * 
     * ����:���밴ť,��insbtns��ť����뵽tarbtns��ť���ָ���±�ǰ,
     * 1.���pos����Ŀ�갴ť��ĳ����򽫲���İ�ť������Ŀ����ĩβ 
     * 2.���posС�ڵ���0�����Ŀ���鿪ͷ
     * @param pos Ŀ�갴ť���ָ���±�
     * @param insbtns Ҫ����İ�ť�� 
     * @param tarbtns Ŀ�갴ť��
     * @return 
     * @return:int[] ����ɹ�����°�ť��
     * @author LiuYuan
     * 2007-9-28 ����09:15:45
     * @throws Exception 
     */
    public static int[] insertButtons(int[] insbtns,int[] tarbtns,int pos){
        int[] newbtns=new int[insbtns.length+tarbtns.length];
        try {
            if(pos<=0){
                copyArray(insbtns,0,newbtns,0,insbtns.length);
                copyArray(tarbtns,0,newbtns,insbtns.length,tarbtns.length);
            }else if(pos>=tarbtns.length){
                copyArray(tarbtns,0,newbtns,0,tarbtns.length);
                copyArray(insbtns,0,newbtns,tarbtns.length,insbtns.length);
            }else{
                int[][] array=splitArray(tarbtns, pos);
                copyArray(array[0],0,newbtns,0,array[0].length);
                copyArray(insbtns,0,newbtns,pos,insbtns.length);
                copyArray(array[1],0,newbtns,array[0].length+insbtns.length,array[1].length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return tarbtns;
        }
        return newbtns;
    }
    /**
     * ����: ������src��spos��ʼ����,������tar��tpos,����Ϊlen
     * @param src  Դ����
     * @param tar Ŀ������
     * @param spos ����src���±�,��Χ������0������tar�ĳ���֮��
     * @param tpos ����tar���±�
     * @param len ���Ƶ���󳤶�
     * @return:void
     * @author LiuYuan
     * 2007-9-28 ����09:31:42
     * @throws Exception 
     */
    public static void copyArray(int[] src,int spos,int[] tar,int tpos,int len) throws Exception{
        if(tar.length<len) 
            throw new Exception("Դ����ĳ��ȳ�����Ŀ������ĳ���!");
        for (int i = 0; i < len; i++) {
            tar[i+tpos]=src[spos+i];
        }
    }
    /**
     * ����: ��������,�����鰴pos�����2��
     * @param tar ���ָ������
     * @param pos �ָ�λ��
     * @return
     * @throws Exception
     * @return:int[][] ������
     * @author LiuYuan
     * 2007-9-28 ����09:54:59
     */
    public static int[][] splitArray(int[] tar,int pos) throws Exception{
        if(pos>tar.length)
            throw new Exception("�����λ�ó�����Ŀ������ĳ���!");
        int[] arr1=new int[pos];
        int[] arr2=new int[tar.length-pos];
        copyArray(tar, 0, arr1, 0, pos);
        copyArray(tar, pos,arr2, 0, tar.length-pos);
        return new int[][]{arr1,arr2};
    }
    /**
     * ����: �Ӱ�ť��btns��ɾ����ťdelbtn,�����delbtn��������
     * @param delbtn ��ɾ���İ�ť
     * @param btns Ŀ�갴ť��
     * @return
     * @return:int[] �°�ť��
     * @author LiuYuan
     * 2007-9-28 ����11:39:09
     */
    public static int[] deleteButton(int delbtn,int[] btns){
        return deleteButtons(new int[]{delbtn}, btns);
    }
    /**
     * ����:�Ӱ�ť��btns��ɾ��һ�鰴ťdelbtns,�����delbtns������������
     * @param btns Ŀ�갴ť��
     * @param delbtns ��ɾ���İ�ť��
     * @return
     * @return:int[] �°�ť��
     * @author LiuYuan
     * 2007-9-28 ����11:40:11
     */
    public static int[] deleteButtons(int[] delbtns,int[] btns){
        LinkedList btnlist=Toolkits.toList(btns);
        for (int i = 0; i < delbtns.length; i++) {
            for (int j = 0; j < btns.length; j++) {
                if(delbtns[i]==btns[j]){
                    btnlist.remove(new Integer(btns[j]));
                }
            }
        }
        return Toolkits.toArray(btnlist);
    }
    public static void main(String[] args) throws Exception {
//        int[] arr1={1,2,3,4,5};
//        int[] arr2={4,5,6,7,8};
//        int[] arr3={0};
//        int[] arr3=insertButtons(arr1, arr2, 4);
//        int[] arr4=deleteButtons(arr1,arr3);
//        System.out.println(arr4);
    }
}
