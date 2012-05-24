
package nc.ui.eh.h08003;

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
import nc.vo.eh.h08003.CustoverageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵��: �ͻ���Ӧ�����������ݵ���
 * @author zqy
 * 2008-9-1 15:52:02
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static CustoverageVO[] vos = null;
    
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
     * @throws BusinessException 
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum) throws BusinessException{
        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08003.ClientUI.pk_corp; //��˾
        Sheet ws = w.getSheet(sheetNum);  
        rows = ws.getRows(); // ��   
        HashMap hmcubasdoc = Getpkcubasdoc();        //��ſͻ�PK
        HashMap hmcustcode = new HashMap();
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            CustoverageVO cvo = new CustoverageVO();        //��Ź�Ӧ�����EXCEl���е�����
            StringBuffer info = new StringBuffer();         //��Ŵ�����Ϣ
            String custcode = cells[0].getContents();       //ȡ�ÿͻ����� 
            if(custcode!=null&&custcode.equals("END")){
            	break;
            }
            
            if(custcode!=null){
                String pk_cubasdoc = hmcubasdoc.get(custcode)==null?"":hmcubasdoc.get(custcode).toString();
                if(pk_cubasdoc!=""){
                    cvo.setPk_cubasdoc(pk_cubasdoc);
                }else{
                    info.append("�ͻ���Ϣû���ڿ��̵�����ά��\t");
                }
            }else{
                info.append("�ͻ����벻��Ϊ��\t");
            }
            if(hmcustcode.containsKey(custcode)){
                info.append("�ͻ��������ظ�\t");
            }else{
                hmcustcode.put(custcode,custcode);
            }
               
            String qmye = cells[2].getContents();           //ȡ����ĩ���
//            cvo.setOverage(new UFDouble(qmye));
            //����ĩ����������ж�
            boolean isnum = CheckStringToNum(qmye);
            if( isnum==true ){
                info.append(""); 
                //cvo.setOverage(new UFDouble(qmye));//��ĩ���
                cvo.setCoumon(new UFDouble(qmye));//��ĩ���
                
            }else{
                info.append("��ĩ���Ӧ��Ϊ���ֶ����Ǻ���\t"); //��ĩ���
                //cvo.setOverage(new UFDouble(0.00));
                cvo.setCoumon(new UFDouble(0.00));
            }
            
            String nyear = cells[3].getContents();//��ʼ����
            String nmonth = cells[4].getContents();//��ʼ����
            
            
            
            //cvo.setPk_corp(pk_corp);
            cvo.setDr(0);
            cvo.setDef_1(info.toString());
            
            cvo.setDef_2(nyear);
            cvo.setDef_3(nmonth);
            
            list.add(cvo);
        }
        if(list!=null && list.size()>0){
            vos = (CustoverageVO[]) list.toArray(new CustoverageVO[list.size()]);
        }
    }

    //�ַ�����ֵ֮���У��(isnumΪtrueʱ,Ϊ��ֵ�ͣ�����Ϊ�ַ���) add by zqy 
    private static Boolean CheckStringToNum(String str) {
        Pattern pattern = Pattern.compile("[-]?[0.000000-9.000000]*"); 
        Matcher isNum = pattern.matcher(str);   
        boolean isnum = isNum.matches();
        return isnum; 
    }
    
    public static HashMap Getpkcubasdoc() throws BusinessException{
        HashMap hmpkcubasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        StringBuffer sql = new StringBuffer()
        //<�޸�>��ӹ�Ӧ�̹�������bd_cumandoc��ʱ�䣺2009-08-17 11:52:02�����ߣ���־Զ��
        //.append(" select custcode ,pk_cubasdoc from bd_cubasdoc where isnull(lock_flag,'N')='N' and isnull(dr,0)=0 ")
        .append(" SELECT cbas.custcode, cbas.pk_cubasdoc FROM bd_cubasdoc cbas,bd_cumandoc cman ")
        .append(" WHERE cman.sealflag is null AND NVL(cman.frozenflag, 'N') = 'N' AND cman.pk_cubasdoc = cbas.pk_cubasdoc AND NVL(cman.dr, 0) = 0 ")
        .append(" AND (cman.custflag = '0' OR cman.custflag = '1' OR cman.custflag = '2') ")
        .append(" AND cman.pk_corp ='"+nc.ui.eh.h08003.ClientUI.pk_corp+"' ");
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        if(arr!=null && arr.size()>0){
            String custcode = null;
            String pk_cubasdoc = null;
            for(int i=0;i<arr.size();i++){
                HashMap hm = (HashMap)arr.get(i);
                custcode = hm.get("custcode")==null?"":hm.get("custcode").toString();
                pk_cubasdoc = hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
                hmpkcubasdoc.put(custcode, pk_cubasdoc);
            }
        }
        return hmpkcubasdoc;
    }
    
   
}
