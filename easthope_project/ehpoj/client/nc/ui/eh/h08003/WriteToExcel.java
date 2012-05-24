
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
 * 功能说明: 客户供应商余额基础数据导入
 * @author zqy
 * 2008-9-1 15:52:02
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static CustoverageVO[] vos = null;
    
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
     * 功能: 
     * @param o 传一个object对象,用于比较
     * @param x X轴坐标
     * @param y Y轴坐标
     * @param sheetNum 工作表索引,0为第一个工作表
     * @param 列
     * @return 根据坐标所取的值与传过来的值进行比较,相同return 1,不相同return 0;
     * @author chenjian
     * 2008-3-6 下午03:51:53
     * @return 
     * @throws BusinessException 
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum) throws BusinessException{
        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08003.ClientUI.pk_corp; //公司
        Sheet ws = w.getSheet(sheetNum);  
        rows = ws.getRows(); // 行   
        HashMap hmcubasdoc = Getpkcubasdoc();        //存放客户PK
        HashMap hmcustcode = new HashMap();
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            CustoverageVO cvo = new CustoverageVO();        //存放供应商余额EXCEl表中的数据
            StringBuffer info = new StringBuffer();         //存放错误信息
            String custcode = cells[0].getContents();       //取得客户编码 
            if(custcode!=null&&custcode.equals("END")){
            	break;
            }
            
            if(custcode!=null){
                String pk_cubasdoc = hmcubasdoc.get(custcode)==null?"":hmcubasdoc.get(custcode).toString();
                if(pk_cubasdoc!=""){
                    cvo.setPk_cubasdoc(pk_cubasdoc);
                }else{
                    info.append("客户信息没有在客商档案中维护\t");
                }
            }else{
                info.append("客户编码不能为空\t");
            }
            if(hmcustcode.containsKey(custcode)){
                info.append("客户编码有重复\t");
            }else{
                hmcustcode.put(custcode,custcode);
            }
               
            String qmye = cells[2].getContents();           //取得期末余额
//            cvo.setOverage(new UFDouble(qmye));
            //对期末余额做数字判断
            boolean isnum = CheckStringToNum(qmye);
            if( isnum==true ){
                info.append(""); 
                //cvo.setOverage(new UFDouble(qmye));//期末余额
                cvo.setCoumon(new UFDouble(qmye));//期末余额
                
            }else{
                info.append("期末余额应该为数字而不是汉字\t"); //期末余额
                //cvo.setOverage(new UFDouble(0.00));
                cvo.setCoumon(new UFDouble(0.00));
            }
            
            String nyear = cells[3].getContents();//初始化年
            String nmonth = cells[4].getContents();//初始化月
            
            
            
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

    //字符与数值之间的校验(isnum为true时,为数值型，否则为字符型) add by zqy 
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
        //<修改>添加供应商管理档案表bd_cumandoc。时间：2009-08-17 11:52:02。作者：张志远。
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
