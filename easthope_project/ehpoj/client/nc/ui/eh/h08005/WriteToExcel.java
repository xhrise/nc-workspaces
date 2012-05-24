
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
 * 功能说明: 期初化期间折扣数据导入
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
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum){
    	list = new ArrayList();
//        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08005.ClientUI.pk_corp; //公司
        UFDate dmakedate = nc.ui.eh.h08005.ClientUI.dmakedate;//导入日期
        String coperatrid = nc.ui.eh.h08005.ClientUI.coperatrid;//导入人
        Sheet ws = w.getSheet(sheetNum);  
        rows = ws.getRows(); // 行        
        HashMap hmcubasdoc = getAllCubasdoc(pk_corp);//存放所有客户
        HashMap hminvbasdoc = getAllInvbasdoc(pk_corp);//存放所有物料
        HashMap hmcode = new HashMap();//客户名称
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            PerioddiscountVO pvo = new PerioddiscountVO();//存放期间折扣Excel中的数据
                StringBuffer info = new StringBuffer();
                String custcode = cells[0].getContents();//客户编码
                if(custcode!=null&&custcode.equals("END")){
                	break;
                }
                String pk_cubasdoc = null;//客户PK
                if(custcode!=null){
                    pk_cubasdoc = hmcubasdoc.get(custcode)==null?null:hmcubasdoc.get(custcode).toString();
                    if(pk_cubasdoc==null){
                        info.append("本公司没有该客户\t");
                    }
                }else{
                    info.append("客户编码不能为空\t");
                }
                pvo.setPk_cubasdoc(pk_cubasdoc);//存放客户
                
                String invcode = cells[1].getContents();//物料编码
                String pk_invbasdoc = null;
                if(invcode!=null){
                    pk_invbasdoc=hminvbasdoc.get(invcode)==null?null:hminvbasdoc.get(invcode).toString();
                    if(pk_invbasdoc==null){
                        info.append("物料没有维护\t");
                    }
                }else{
                    info.append("物料编码不能为空\t");
                }
                pvo.setPk_invbasdoc(pk_invbasdoc);//存放物料
                
                if(hmcode.containsKey(pk_cubasdoc+pk_invbasdoc)){
                    info.append("客户对应的物料有重复\t");
                }else{
                    hmcode.put(pk_cubasdoc+pk_invbasdoc,pk_cubasdoc+pk_invbasdoc);
                }
                
                String invname = cells[2].getContents();//物料名称 
                if(invname==null){
                    info.append("物料名称不能为空\t");
                }

                
                
                String qmzk = cells[3].getContents().trim();//期末折扣
                Boolean isflag=CheckStringToNum(qmzk);
                if(isflag==true){
                    info.append("");
                    pvo.setEdiscount(new UFDouble(qmzk));
                }else{
                    info.append("期末折扣应该为数字而不是汉字\t");
                    pvo.setEdiscount(new UFDouble(0.00));
                }
                
                /**------导入的数据的年度与月度以EXCEL中为准------------**/
                String vyear = cells[4].getContents();  //年度
                pvo.setVyear(new Integer(vyear));
                
                String vmonth = cells[5].getContents(); //月度
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
     * 功能：
     *<p> 得到所有的客商PK和编码</p>
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
     * 功能：
     * <p>得到所有物料PK和编码</p>
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
