
package nc.ui.eh.trade.h0205615;

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
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.eh.trade.h0205605.TradePlanBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明:从Excel中读取销售计划
 * @author wb
 * 2008-10-14 18:14:33
 */
public class ExcelImport {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static int nyear;
    public static int nmonth;
    public static String pk_period;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    String pk_corp = ce.getCorporation().getPk_corp();
    
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
     * 判断Excel中有没有数据
     * @param sheetNum
     * @param filepath
     * @return
     */
    public static boolean hasData(int sheetNum,String filepath){
    	boolean hasdata = false;
    	creatFile(filepath);
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // 行
        if(rows>1){
        	hasdata = true;
        }
        return hasdata;
    }
    
    /**
     * 功能: 读取Excel中数据
     * @author:zqy
     * date:2008-9-3 15:55:45
     */
    
    @SuppressWarnings("unchecked")
	public static TradePlanBVO[] readData(Object o,int x,int y,int sheetNum,String filepath){
    	TradePlanBVO[] vos  = null;
    	ArrayList list = new ArrayList();
        creatFile(filepath);
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // 行   
        nyear = Integer.parseInt(ws.getRow(2)[0].getContents());		//年度 Excel中第2行第1列
        nmonth = Integer.parseInt(ws.getRow(2)[1].getContents());		//月份 Excel中第2行第2列
        pk_period = getPeriod(nyear, nmonth);						//期间PK
        
        HashMap hmA = getCustPsn();									//客户PK,营销代表PK
        HashMap hmB = getAllInvbasdoc();							//物料PK
        String pk_cubasdoc = null;
        String pk_psndoc = null;
        String invcode = null;
        String invname = null;
        String pk_invbasdoc = null;
        for(int i=2;i<rows;i++){
        	StringBuffer errorinfo = new StringBuffer();
            Cell[] cells = ws.getRow(i); 
            TradePlanBVO bvo = new TradePlanBVO();   
            String nyearstr = cells[0].getContents();		//年度 Excel中第2行第1列
            if(nyearstr!=null&&nyearstr.length()>0){
	            String psnname = cells[2].getContents();		//营销代表
	            String custcode = cells[3].getContents();		//客户编码
	            String custname = cells[4].getContents();		//客户名称
	            
	            String[] pks = (String[])hmA.get(custcode);
	            if(pks==null||pks.length<3){
	            	errorinfo.append(custname+"编码不存在  ");
	            	pk_cubasdoc = null;
	            	custname = null;
	            }else{
	            	pk_cubasdoc = pks[0];
	            	pk_psndoc = pks[1];
	            	String truepsname = pks[2];							//系统中的营销代表
	            	if(!psnname.equals(truepsname)){
	            		errorinfo.append(custname+"的营销代表不正确  ");
	            	}
	            }
	            invcode = cells[5].getContents();				//物料编码
	            invname = cells[6].getContents();				//物料名称
	            UFDouble amount = new UFDouble(cells[7].getContents()==null?"0":cells[7].getContents().toString());//计划销量
	            
	            pk_invbasdoc = hmB.get(invcode)==null?null:hmB.get(invcode).toString();
	            if(pk_invbasdoc==null){
	            	errorinfo.append(invcode+"物料不存在");
	            	invcode = null;
	            	invname = null;
	            }
	            
	            bvo.setPk_cubasdoc(pk_cubasdoc);
	            bvo.setVcust(custname);
	            bvo.setPk_psndoc(pk_psndoc);
	            bvo.setVpsndoc(psnname);
	            
	            bvo.setPk_invbasdoc(pk_invbasdoc);
	            bvo.setVinvcode(invcode);
	            bvo.setVinvname(invname);
	            bvo.setAmount(amount);
	            
	            bvo.setErrorinfo(errorinfo.toString());
	            bvo.setDr(0);
	                
	            list.add(bvo);    
            }
        }
        if(list.size()>0){
        	vos = new TradePlanBVO[list.size()];
            vos = (TradePlanBVO[])list.toArray(new TradePlanBVO[0]);
        }   
        return vos;
    }
    
    
    /**
     * 根据客户编码找到客户的pk，及营销代表PK
     * @return
     */
    @SuppressWarnings("unchecked")
	public static HashMap getCustPsn(){
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	HashMap hm = new HashMap();
    	StringBuffer sql = new StringBuffer()
    	.append(" select a.custcode,a.pk_cubasdoc,b.pk_psndoc,c.psnname")
    	.append(" from bd_cubasdoc a,eh_custyxdb b,bd_psndoc c")
    	.append(" where a.pk_cubasdoc = b.pk_cubasdoc and b.pk_psndoc = c.pk_psndoc")
    	.append(" and b.ismain = 'Y'")
    	.append(" and a.custprop in (0,2)")
    	.append(" and a.pk_corp = '"+ce.getCorporation().getPk_corp()+"'")
    	.append(" and isnull(a.dr,0)=0")
    	.append(" and isnull(b.dr,0)=0");
    	try{
    		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
    			String custcode = null;							//客户编码
    			String pk_cubasdoc =null;
    			String pk_psndoc = null;						//营销代表
    			String psnname = null;
    			for(int i=0;i<arr.size();i++){
    				HashMap hmA = (HashMap)arr.get(i);
    				String[] pks = new String[3];
    				custcode = hmA.get("custcode")==null?"":hmA.get("custcode").toString();
    				pk_cubasdoc = hmA.get("pk_cubasdoc")==null?"":hmA.get("pk_cubasdoc").toString();
    				pk_psndoc = hmA.get("pk_psndoc")==null?"":hmA.get("pk_psndoc").toString();
    				psnname = hmA.get("psnname")==null?"":hmA.get("psnname").toString();
    				pks[0] = pk_cubasdoc;
    				pks[1] = pk_psndoc;
    				pks[2] = psnname;
    				hm.put(custcode, pks);
    			}
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return hm;
    }
    
    @SuppressWarnings("unchecked")
	public static HashMap getAllInvbasdoc(){
        HashMap hminvbasdoc = new HashMap();
        ClientEnvironment ce = ClientEnvironment.getInstance();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select pk_invbasdoc,invcode from eh_invbasdoc where isnull(dr,0)=0 and lock_flag <> 'Y' and pk_corp = '"+ce.getCorporation().getPk_corp()+"'";
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
     * 根据年月找到期间PK
     * @param nyear
     * @param nmonth
     * @return
     */
    public static String getPeriod(int nyear,int nmonth){
    	String pk_period = null;
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	String sql = "select pk_period from eh_period where nyear = "+nyear+" and nmonth = "+nmonth+"  and pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
    	try {
    		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            Object obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
            if(obj!=null){
            	pk_period = obj.toString();
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return pk_period;
    }
  
}
