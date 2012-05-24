
package nc.ui.eh.h08002;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.eh.h08002.CubasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

/**
 * 功能说明: 
 * <p>
 * 期初化客商档案数据导入</br>
 * 只导入营销代表和可销料</br>
 * def13 营销代表PK
 * def6 可销料PK
 * Custcode 客商基本档案Code
 * cubasname 客商基本档案name
 * </p>
 * @author chenjian
 * 2007-9-6 下午06:12:21
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static CubasdocVO[] vos = null;
    
    /**
     * 功能: 打开创建文件
     * @param sourceFile
     * @param newFile
     * @author chenjia n
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
        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08002.ClientUI.pk_corp; //公司
        UFDate dmakedate = nc.ui.eh.h08002.ClientUI.dmakedate;//导入日期
        String coperatrid = nc.ui.eh.h08002.ClientUI.coperatrid;//操作员
        Sheet ws = w.getSheet(sheetNum);
        rows = ws.getRows(); // 行   
        /**存放所有营销代表*/
        HashMap hmpsndoc = getAllPsndoc(pk_corp);
        
        /**存放所有物料*/
        HashMap hminvbasdoc = getAllInvbasdoc(pk_corp);
        
        /**存放所有客商*/
        HashMap hmcuvbasdoc = getAllCubasdoc(pk_corp);
        
                
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            int len = cells.length;      //读取EXCEL的长度
            CubasdocVO cvo = new CubasdocVO();//存放客商Excel表中的数据
                StringBuffer info = new StringBuffer();
                
                String cubascode = cells[0].getContents();//客商编码
                if(cubascode!=null&&cubascode.equals("END")){
                	break;
                }
                String pk_cubasdoc = null;
                if(!"".equals(cubascode)){
                	pk_cubasdoc = hmcuvbasdoc.get(cubascode) == null? null : hmcuvbasdoc.get(cubascode).toString();
                	if(pk_cubasdoc == null){
                		info.append("客商表没有维护\t");
                	}
                }else{
                	info.append("客商不能为空\t");
                }
                cvo.setCustcode(cubascode);     //存放客商编码
                cvo.setPk_cubasdoc(pk_cubasdoc);//管理档案PK
                
                
                String cubasname = cells[1].getContents();//客商名称 
                cvo.setCustname(cubasname);//存放客商名称
                
                String yscode = cells[2].getContents();//营销工号
                String pk_psndoc = null;//营销人员PK
                
                if(!yscode.equals("")){
                    pk_psndoc = hmpsndoc.get(yscode)==null?null:hmpsndoc.get(yscode).toString();
                    if(pk_psndoc==null){
                        info.append("营销代表没有维护\t");
                    }
                }else{
                    info.append("营销代表不能为空\t");
                }
                cvo.setDef13(pk_psndoc);//营销代表PK  
                
                if(len-3>0){
                    String code = cells[3].getContents();//可销料编码
                    String pk_invbasdoc = null;//可销料的PK
                    pk_invbasdoc = hminvbasdoc.get(code)==null?null:hminvbasdoc.get(code).toString();                    
                    cvo.setDef6(pk_invbasdoc);//可销料PK
                }else{
                    String pkinvbasdoc = null;
                    cvo.setDef6(pkinvbasdoc);
                }
                cvo.setMemo(info.toString());
                cvo.setPk_corp(pk_corp);
                cvo.setDef19(coperatrid);
                cvo.setSealflag(dmakedate);
                list.add(cvo);              
        }
        if(list.size()>0){
            vos = (CubasdocVO[])list.toArray(new CubasdocVO[0]);
        }
    }
    
    /**
     * 功能：
     * <p>所有营销代表</p>
     */
    @SuppressWarnings("unchecked")
    public static HashMap getAllPsndoc(String pk_corp){
        HashMap hmpsndoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select pk_psndoc,psncode from bd_psndoc where pk_corp = '"+pk_corp+"' and isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_psndoc = null;
                String psncode = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_psndoc = hm.get("pk_psndoc")==null?"":hm.get("pk_psndoc").toString();
                    psncode = hm.get("psncode")==null?"":hm.get("psncode").toString();
                    hmpsndoc.put(psncode, pk_psndoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmpsndoc;
    }   
    /**
     * 功能：
     * <p>所有物料</p>
     */
    @SuppressWarnings("unchecked")
    public static HashMap getAllInvbasdoc(String pk_corp){
        HashMap hminvbasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select b.pk_invmandoc pk_invbasdoc,a.invcode from bd_invbasdoc a,bd_invmandoc b " +
        		" where b.pk_corp = '"+pk_corp+"' and isnull(b.dr,0)=0 " +
        		" and a.pk_invbasdoc=b.pk_invbasdoc and SUBSTR(a.invcode,1,4) IN ('0101','0102','0103','0104') ";//此处为可销料
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
    
    /**
     * 功能：
     * <p>
     * 得到所有客商
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static  HashMap getAllCubasdoc(String pk_corp){
        HashMap hmpkcubasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = " select a.pk_cumandoc,b.custcode from bd_cumandoc a,bd_cubasdoc b "+
        			 " where "+
        			 " a.pk_cubasdoc=b.pk_cubasdoc  "+
        			 " and nvl(a.dr,0)=0  "+
        			 " and a.pk_corp = '"+pk_corp+"'  "+
        			 " and (a.custflag = '0' OR a.custflag = '1' OR a.custflag = '2') "+
        			 " and a.sealflag is null  "+
        			 " and (a.frozenflag = 'N' or a.frozenflag is null) ";
        
        ArrayList arr;
		try {
			arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
			if(arr!=null && arr.size()>0){
	            String custcode = null;
	            String pk_cubasdoc = null;
	            for(int i=0;i<arr.size();i++){
	                HashMap hm = (HashMap)arr.get(i);
	                custcode = hm.get("custcode")==null?"":hm.get("custcode").toString();
	                pk_cubasdoc = hm.get("pk_cumandoc")==null?"":hm.get("pk_cumandoc").toString();
	                hmpkcubasdoc.put(custcode,pk_cubasdoc);
	            }
	        }
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return hmpkcubasdoc;
    }
  
    
}