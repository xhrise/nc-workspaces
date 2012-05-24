
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
 * 功能说明: 期初物料数据导入
 * @author zqy
 * 2008-9-1 14:30:36 
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static InvbasdocVO[] vos = null;
    
    /**
     * 功能: 打开创建文件
     * @param sourceFile
     * @param newFile
     * @author chenjian
     * 2007-9-6 下午08:12:24
     */
    public static void creatFile(String sourceFile){
        try {
            /** 创建只读的Excel工作薄的对象*/
            w = Workbook.getWorkbook(new File(sourceFile));
            /** copy上面的Excel工作薄,创建新的可写入的Excel工作薄对象*/
//            ww = Workbook.createWorkbook(new File(newFile),w);
            
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能: 读取Excel中数据
     * @author:zqy
     * date:2008-9-3 15:55:45
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum){
        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08001.ClientUI.pk_corp; //公司
        UFDate dmakedate = nc.ui.eh.h08001.ClientUI.dmakedate;//导入日期
        String coperatrid = nc.ui.eh.h08001.ClientUI.coperatrid;//操作员
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // 行   
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        
        HashMap hmstore = getAllStore(pk_corp); //存放所有仓库
        HashMap hmbrand = getAllBrand(pk_corp); //存放所有品牌
        HashMap hmmeasdoc = getAllMeasdoc();//存放所有单位
        HashMap hminvcl = getAllinvcl();//存放所以物料大类
        HashMap hmcode = new HashMap();
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i); 
            int len = cells.length;
            InvbasdocVO ivo = new InvbasdocVO();//存放EXCEL中的数据
                @SuppressWarnings("unused")
                String invclassname = cells[0].getContents();//物料分类
                StringBuffer info = new StringBuffer();               
                String invcode = cells[1].getContents();//物料编码
                String subcode = null;
                subcode = invcode.substring(0, invcode.length()-4);
                String pkinvcl = hminvcl.get(subcode)==null?"":hminvcl.get(subcode).toString();
                ivo.setPk_invcl(pkinvcl);//存放物料大类
                
                if(hmcode.containsKey(invcode)){
                    info.append("物料编码有重复\t");
                }else{
                    hmcode.put(invcode, invcode);
                }
                ivo.setInvcode(invcode);//存放物料编码
                
                String invname = cells[2].getContents();//物料名称
                if(invname.equals("")){
                    info.append("物料名称不能为空\t");
                }
                ivo.setInvname(invname);//存放物料名称
                
                String invmname = cells[3].getContents();//助记码
                ivo.setInvmnecode(invmname);//存放助记码
                String invspec = cells[4].getContents();//规格
                ivo.setInvspec(invspec);
                String invtype = cells[5].getContents();//型号
                ivo.setInvtype(invtype);
                String colour = cells[6].getContents();//颜色
                ivo.setColour(colour);
                String brand = cells[7].getContents();//品牌
                String pk_brand = null;
                pk_brand = hmbrand.get(brand)==null?null:hmbrand.get(brand).toString();
                ivo.setBrand(pk_brand);
                ivo.setDef_2(brand);
                String stoctime = cells[8].getContents();//保质期
                boolean isnum2 = CheckStringToNum(stoctime);
                if( isnum2==true){
                    info.append(""); 
                    ivo.setStoretime(new UFDouble(stoctime));
                }else{
                    info.append("保质期应该为数字而不是字符\t"); 
                    ivo.setStoretime(new UFDouble(0.00));
                }
                
                String producttime = cells[9].getContents();//生产周期
                boolean isnum3 = CheckStringToNum(producttime);
                if( isnum3==true){
                    info.append(""); 
                    ivo.setProduceperiod(new UFDouble(producttime));
                }else{
                    info.append("生产周期应该为数字而不是字符\t"); 
                    ivo.setProduceperiod(new UFDouble(0.00));
                }
                
                String stocdoc = cells[10].getContents();//仓库
                String pk_stordoc = null;
                if(stocdoc!=null){
                     pk_stordoc = hmstore.get(stocdoc)==null?null:hmstore.get(stocdoc).toString();
                     if(pk_stordoc==null){
                         info.append("仓库没有维护\t");
                     }
                }else{
                    info.append("仓库不能为空\t");
                }
                ivo.setWarehouseid(pk_stordoc);
                
                String price = cells[11].getContents();//牌价
                ivo.setPrice(new UFDouble(price));
                
                String measdoc = cells[12].getContents();//主计量单位
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
                                    info.append("主计量单位没有维护\t");
                                }
                            }
                        }
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                }else{
                    info.append("主计量单位不能为空\t");
                }
                ivo.setPk_measdoc(pk_measdoc);
                
                if(len-13>0){
                    String fuzdw = cells[13].getContents();//辅计量单位 
                    String pk_fuzdw = hmmeasdoc.get(fuzdw)==null?null:hmmeasdoc.get(fuzdw).toString();
                    ivo.setDef_5(pk_fuzdw);
                }else{
                    String pkfuzdw = null;
                    ivo.setDef_5(pkfuzdw);
                }
                
                if(len-14>0){
                    String changerate = cells[14].getContents();//主辅换算率
                    if(changerate!=null){
                        boolean isnum = CheckStringToNum(changerate);
                        if( isnum==true){
                            info.append(""); 
                            ivo.setDef_6(new UFDouble(changerate));
                        }else{
                            info.append("换算率应该为数字而不是字符\t"); 
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
    
    //字符与数值之间的校验(isnum为true时,为数值型，否则为字符型) add by zqy 
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
