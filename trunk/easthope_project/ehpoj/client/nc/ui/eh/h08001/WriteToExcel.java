
package nc.ui.eh.h08001;

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
import nc.vo.eh.h08001.InvbasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵��: �ڳ��������ݵ���
 * @author zqy
 * 2008-9-1 14:30:36 
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static InvbasdocVO[] vos = null;
    
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
     * ����: ��ȡExcel������
     * @author:zqy
     * date:2008-9-3 15:55:45
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum){
        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08001.ClientUI.pk_corp; //��˾
        UFDate dmakedate = nc.ui.eh.h08001.ClientUI.dmakedate;//��������
        String coperatrid = nc.ui.eh.h08001.ClientUI.coperatrid;//����Ա
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // ��   
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        
        HashMap hmstore = getAllStore(pk_corp); //������вֿ�
        HashMap hmbrand = getAllBrand(pk_corp); //�������Ʒ��
        HashMap hmmeasdoc = getAllMeasdoc();//������е�λ
        HashMap hminvcl = getAllinvcl();//����������ϴ���
        HashMap hmcode = new HashMap();
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i); 
            int len = cells.length;
            InvbasdocVO ivo = new InvbasdocVO();//���EXCEL�е�����
                @SuppressWarnings("unused")
                String invclassname = cells[0].getContents();//���Ϸ���
                StringBuffer info = new StringBuffer();               
                String invcode = cells[1].getContents();//���ϱ���
                String subcode = null;
                subcode = invcode.substring(0, invcode.length()-4);
                String pkinvcl = hminvcl.get(subcode)==null?"":hminvcl.get(subcode).toString();
                ivo.setPk_invcl(pkinvcl);//������ϴ���
                
                if(hmcode.containsKey(invcode)){
                    info.append("���ϱ������ظ�\t");
                }else{
                    hmcode.put(invcode, invcode);
                }
                ivo.setInvcode(invcode);//������ϱ���
                
                String invname = cells[2].getContents();//��������
                if(invname.equals("")){
                    info.append("�������Ʋ���Ϊ��\t");
                }
                ivo.setInvname(invname);//�����������
                
                String invmname = cells[3].getContents();//������
                ivo.setInvmnecode(invmname);//���������
                String invspec = cells[4].getContents();//���
                ivo.setInvspec(invspec);
                String invtype = cells[5].getContents();//�ͺ�
                ivo.setInvtype(invtype);
                String colour = cells[6].getContents();//��ɫ
                ivo.setColour(colour);
                String brand = cells[7].getContents();//Ʒ��
                String pk_brand = null;
                pk_brand = hmbrand.get(brand)==null?null:hmbrand.get(brand).toString();
                ivo.setBrand(pk_brand);
                ivo.setDef_2(brand);
                String stoctime = cells[8].getContents();//������
                boolean isnum2 = CheckStringToNum(stoctime);
                if( isnum2==true){
                    info.append(""); 
                    ivo.setStoretime(new UFDouble(stoctime));
                }else{
                    info.append("������Ӧ��Ϊ���ֶ������ַ�\t"); 
                    ivo.setStoretime(new UFDouble(0.00));
                }
                
                String producttime = cells[9].getContents();//��������
                boolean isnum3 = CheckStringToNum(producttime);
                if( isnum3==true){
                    info.append(""); 
                    ivo.setProduceperiod(new UFDouble(producttime));
                }else{
                    info.append("��������Ӧ��Ϊ���ֶ������ַ�\t"); 
                    ivo.setProduceperiod(new UFDouble(0.00));
                }
                
                String stocdoc = cells[10].getContents();//�ֿ�
                String pk_stordoc = null;
                if(stocdoc!=null){
                     pk_stordoc = hmstore.get(stocdoc)==null?null:hmstore.get(stocdoc).toString();
                     if(pk_stordoc==null){
                         info.append("�ֿ�û��ά��\t");
                     }
                }else{
                    info.append("�ֿⲻ��Ϊ��\t");
                }
                ivo.setWarehouseid(pk_stordoc);
                
                String price = cells[11].getContents();//�Ƽ�
                ivo.setPrice(new UFDouble(price));
                
                String measdoc = cells[12].getContents();//��������λ
                String pk_measdoc = null;
                if(measdoc!=null){
                    String sql = " select pk_measdoc from bd_measdoc where measname='"+measdoc+"' and isnull(dr,0)=0 ";
                    try {
                        ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                        if(all!=null && all.size()>0){
                            for(int m=0;m<all.size();m++){
                                HashMap hm = (HashMap) all.get(m);
                                pk_measdoc = hm.get("pk_measdoc")==null?null:hm.get("pk_measdoc").toString();
                                if(pk_measdoc==null){
                                    info.append("��������λû��ά��\t");
                                }
                            }
                        }
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                }else{
                    info.append("��������λ����Ϊ��\t");
                }
                ivo.setPk_measdoc(pk_measdoc);
                
                if(len-13>0){
                    String fuzdw = cells[13].getContents();//��������λ 
                    String pk_fuzdw = hmmeasdoc.get(fuzdw)==null?null:hmmeasdoc.get(fuzdw).toString();
                    ivo.setDef_5(pk_fuzdw);
                }else{
                    String pkfuzdw = null;
                    ivo.setDef_5(pkfuzdw);
                }
                
                if(len-14>0){
                    String changerate = cells[14].getContents();//����������
                    if(changerate!=null){
                        boolean isnum = CheckStringToNum(changerate);
                        if( isnum==true){
                            info.append(""); 
                            ivo.setDef_6(new UFDouble(changerate));
                        }else{
                            info.append("������Ӧ��Ϊ���ֶ������ַ�\t"); 
                            ivo.setDef_6(new UFDouble(0.00));
                        }
                    }
                }else{
                    ivo.setDef_6(new UFDouble(0.00));
                }
               
                
                ivo.setDef_4(info.toString());
                ivo.setDr(0);
                ivo.setPk_corp(pk_corp);
                ivo.setDmakedate(dmakedate);
                ivo.setCoperatorid(coperatrid);
                
                list.add(ivo);              
        }
        if(list.size()>0){
            vos = (InvbasdocVO[])list.toArray(new InvbasdocVO[0]);
        }     
    }
    
    //�ַ�����ֵ֮���У��(isnumΪtrueʱ,Ϊ��ֵ�ͣ�����Ϊ�ַ���) add by zqy 
    private static Boolean CheckStringToNum(String str) {
        Pattern pattern = Pattern.compile("[-]?[0.000000-9.000000]*"); 
        Matcher isNum = pattern.matcher(str);   
        boolean isnum = isNum.matches();
        return isnum; 
    }
    
    
    @SuppressWarnings("unchecked")
    public static HashMap getAllStore(String pk_corp){
        HashMap hm = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = "select pk_stordoc,storname from eh_stordoc where pk_corp = '"+pk_corp+"' and isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null&&arr.size()>0){
                String pk_stordoc = null;
                String storname = null;
                for(int i=0; i<arr.size(); i++){
                    HashMap hmarr = (HashMap)arr.get(i);
                    pk_stordoc = hmarr.get("pk_stordoc")==null?"":hmarr.get("pk_stordoc").toString();
                    storname = hmarr.get("storname")==null?"":hmarr.get("storname").toString();
                    hm.put(storname, pk_stordoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hm;
    }
    
    @SuppressWarnings("unchecked")
    public static HashMap getAllBrand(String pk_corp){
        HashMap hmbrand = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select pk_brand,brandname from eh_brand where pk_corp = '"+pk_corp+"' and isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_brand = null;
                String brandname = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_brand = hm.get("pk_brand")==null?"":hm.get("pk_brand").toString();
                    brandname = hm.get("brandname")==null?"":hm.get("brandname").toString();
                    hmbrand.put(brandname, pk_brand);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmbrand;
    }
    
    @SuppressWarnings("unchecked")
    public static HashMap getAllMeasdoc(){
        HashMap hmmeasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql ="select pk_measdoc,measname from bd_measdoc where isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_measdoc = null;
                String measname = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_measdoc = hm.get("pk_measdoc")==null?"":hm.get("pk_measdoc").toString();
                    measname = hm.get("measname")==null?"":hm.get("measname").toString();
                    hmmeasdoc.put(measname, pk_measdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmmeasdoc;
    }
    
    @SuppressWarnings("unchecked")
    public static HashMap getAllinvcl(){
        HashMap hminvcl = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql ="select pk_invcl,substring(invclasscode,1,9) invclasscode from bd_invcl where isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_invcl = null;
                String invclasscode = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_invcl = hm.get("pk_invcl")==null?"":hm.get("pk_invcl").toString();
                    invclasscode = hm.get("invclasscode")==null?"":hm.get("invclasscode").toString();
                    hminvcl.put(invclasscode, pk_invcl);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hminvcl;
    }
    
}
