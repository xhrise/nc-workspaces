
package nc.ui.eh.h08005;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.eh.stock.z0150502.PerioddiscountVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵��: �ڳ����ڼ��ۿ����ݵ���
 * @author zqy
 * 2008-9-1 16:12:10
 */ 
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static PerioddiscountVO[] vos = null;
    public static ArrayList list = new ArrayList();
    
    /**
     * ����: �򿪴����ļ�
     * @param sourceFile
     * @param newFile
     * @author chenjian
     * 2007-9-6 ����08:12:24
     */
    public static void creatFile(String sourceFile){
        try {
            /** ����ֻ����Excel�������Ķ���*/
            w = Workbook.getWorkbook(new File(sourceFile));
            /** copy�����Excel������,�����µĿ�д���Excel����������*/
//            ww = Workbook.createWorkbook(new File(newFile),w);
            
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����: 
     * @param o ��һ��object����,���ڱȽ�
     * @param x X������
     * @param y Y������
     * @param sheetNum ����������,0Ϊ��һ��������
     * @param ��
     * @return ����������ȡ��ֵ�봫������ֵ���бȽ�,��ͬreturn 1,����ͬreturn 0;
     * @author chenjian
     * 2008-3-6 ����03:51:53
     * @return 
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum){
    	list = new ArrayList();
//        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08005.ClientUI.pk_corp; //��˾
        UFDate dmakedate = nc.ui.eh.h08005.ClientUI.dmakedate;//��������
        String coperatrid = nc.ui.eh.h08005.ClientUI.coperatrid;//������
        Sheet ws = w.getSheet(sheetNum);  
        rows = ws.getRows(); // ��        
        HashMap hmcubasdoc = getAllCubasdoc(pk_corp);//������пͻ�
        HashMap hminvbasdoc = getAllInvbasdoc(pk_corp);//�����������
        HashMap hmcode = new HashMap();//�ͻ�����
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            PerioddiscountVO pvo = new PerioddiscountVO();//����ڼ��ۿ�Excel�е�����
                StringBuffer info = new StringBuffer();
                String custcode = cells[0].getContents();//�ͻ�����
                if(custcode!=null&&custcode.equals("END")){
                	break;
                }
                String pk_cubasdoc = null;//�ͻ�PK
                if(custcode!=null){
                    pk_cubasdoc = hmcubasdoc.get(custcode)==null?null:hmcubasdoc.get(custcode).toString();
                    if(pk_cubasdoc==null){
                        info.append("����˾û�иÿͻ�\t");
                    }
                }else{
                    info.append("�ͻ����벻��Ϊ��\t");
                }
                pvo.setPk_cubasdoc(pk_cubasdoc);//��ſͻ�
                
                String invcode = cells[1].getContents();//���ϱ���
                String pk_invbasdoc = null;
                if(invcode!=null){
                    pk_invbasdoc=hminvbasdoc.get(invcode)==null?null:hminvbasdoc.get(invcode).toString();
                    if(pk_invbasdoc==null){
                        info.append("����û��ά��\t");
                    }
                }else{
                    info.append("���ϱ��벻��Ϊ��\t");
                }
                pvo.setPk_invbasdoc(pk_invbasdoc);//�������
                
                if(hmcode.containsKey(pk_cubasdoc+pk_invbasdoc)){
                    info.append("�ͻ���Ӧ���������ظ�\t");
                }else{
                    hmcode.put(pk_cubasdoc+pk_invbasdoc,pk_cubasdoc+pk_invbasdoc);
                }
                
                String invname = cells[2].getContents();//�������� 
                if(invname==null){
                    info.append("�������Ʋ���Ϊ��\t");
                }

                
                
                String qmzk = cells[3].getContents().trim();//��ĩ�ۿ�
                Boolean isflag=CheckStringToNum(qmzk);
                if(isflag==true){
                    info.append("");
                    pvo.setEdiscount(new UFDouble(qmzk));
                }else{
                    info.append("��ĩ�ۿ�Ӧ��Ϊ���ֶ����Ǻ���\t");
                    pvo.setEdiscount(new UFDouble(0.00));
                }
                
                /**------��������ݵ�������¶���EXCEL��Ϊ׼------------**/
                String vyear = cells[4].getContents();  //���
                pvo.setVyear(new Integer(vyear));
                
                String vmonth = cells[5].getContents(); //�¶�
                pvo.setVmonth(new Integer(vmonth));
                
                pvo.setDef_3(info.toString());
                pvo.setDr(0);
                pvo.setPk_corp(pk_corp);
                pvo.setDef_4(dmakedate.toString());
                pvo.setDef_5(coperatrid);
              
                list.add(pvo);              
        }
//        if(list.size()>0){
//            vos = (PerioddiscountVO[])list.toArray(new PerioddiscountVO[0]);
//        }
    }

    
    private static Boolean CheckStringToNum(String str) {
        Pattern pattern = Pattern.compile("[-]?[0.000000-9.000000]*"); 
        Matcher isNum = pattern.matcher(str);   
        boolean isnum = isNum.matches();
        return isnum; 
    }

    /**
     * ���ܣ�
     *<p> �õ����еĿ���PK�ͱ���</p>
     */
    @SuppressWarnings("unchecked")
    public static HashMap getAllCubasdoc(String pk_corp){
        HashMap hmcubasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = " select a.pk_cumandoc,b.custcode from bd_cumandoc a,bd_cubasdoc b "+
		 " where "+
		 " a.pk_cubasdoc=b.pk_cubasdoc  "+
		 " and nvl(a.dr,0)=0  "+
		 " and a.pk_corp = '"+pk_corp+"'  "+
		 " and (a.custflag = '0' OR a.custflag = '1' OR a.custflag = '2') "+
		 " and a.sealflag is null  "+
		 " and (a.frozenflag = 'N' or a.frozenflag is null) ";
        
//        String sql = "select pk_cubasdoc,custcode from bd_cubasdoc where pk_corp= '"+pk_corp+"' and isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null&&arr.size()>0){
                String pk_cubasdoc = null;
                String custcode = null;
                for(int i=0; i<arr.size(); i++){
                    HashMap hmarr = (HashMap)arr.get(i);
                    pk_cubasdoc = hmarr.get("pk_cumandoc")==null?"":hmarr.get("pk_cumandoc").toString();
                    custcode = hmarr.get("custcode")==null?"":hmarr.get("custcode").toString();
                    hmcubasdoc.put(custcode, pk_cubasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmcubasdoc;
    }
    
    /**
     * ���ܣ�
     * <p>�õ���������PK�ͱ���</p>
     */
    @SuppressWarnings("unchecked")
    public static HashMap getAllInvbasdoc(String pk_corp){
        HashMap hminvbasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select b.pk_invmandoc pk_invbasdoc,a.invcode from bd_invbasdoc a,bd_invmandoc b " +
		" where b.pk_corp = '"+pk_corp+"' and isnull(b.dr,0)=0 " +
		" and a.pk_invbasdoc=b.pk_invbasdoc and SUBSTR(a.invcode,1,4) IN ('0101','0102','0103','0104')";
//        String sql ="select pk_invbasdoc,invcode from eh_invbasdoc where pk_corp= '"+pk_corp+"' and  isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_invbasdoc = null;
                String invcode = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                    invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
                    hminvbasdoc.put(invcode, pk_invbasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hminvbasdoc;
    }
    
}
