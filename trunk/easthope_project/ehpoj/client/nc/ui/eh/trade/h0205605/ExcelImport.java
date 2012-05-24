
package nc.ui.eh.trade.h0205605;

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
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.eh.h08004.InvmanVO;
import nc.vo.eh.trade.h0205605.TradePlanBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵��:��Excel�ж�ȡ���ۼƻ�
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
    static ClientEnvironment ce = ClientEnvironment.getInstance();
    static String  pk_corp = ce.getCorporation().getPk_corp();
    
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
     * �ж�Excel����û������
     * @param sheetNum
     * @param filepath
     * @return
     */
    public static boolean hasData(int sheetNum,String filepath){
    	boolean hasdata = false;
    	creatFile(filepath);
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // ��
        if(rows>1){
        	hasdata = true;
        }
        return hasdata;
    }
    
    /**
     * ����: ��ȡExcel������
     * @author:zqy
     * date:2008-9-3 15:55:45
     */
    
    @SuppressWarnings("unchecked")
	public static TradePlanBVO[] readData(Object o,int x,int y,int sheetNum,String filepath){
    	TradePlanBVO[] vos  = null;
    	ArrayList list = new ArrayList();
        creatFile(filepath);
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // ��   
        nyear = Integer.parseInt(ws.getRow(2)[0].getContents());		//��� Excel�е�2�е�1��
        nmonth = Integer.parseInt(ws.getRow(2)[1].getContents());		//�·� Excel�е�2�е�2��
        pk_period = getPeriod(nyear, nmonth);						//�ڼ�PK
        
        HashMap hmA = getCustPsn();									//�ͻ�PK,Ӫ������PK
        
        //ֻ��ȡ�ⲿ�����������Ϣ2010-01-05
        InvmanVO[] invos = new InvmanVO[rows];
        for(int i=2;i<rows;i++){
        	InvmanVO invo = new InvmanVO();//��ʱ������Ϣ��VO
        	Cell[] cells = ws.getRow(i);//������

        	String nyearstr = cells[0].getContents();		//��� Excel�е�2�е�1��
            if(nyearstr!=null&&nyearstr.equals("END")){
            	break;
            }else{
            	String invcode = cells[5].getContents();            //���ϱ���
            	invo.setLs_invcode(invcode);//����ȡ������CODE����VO
            	invo.setPk_corp(pk_corp);
            	invo.setDr(0);
            	invos[i-2]=invo;
            }
        }
        IVOPersistence ip = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            try {
				//ip.insertVO(invo);
            	ip.insertVOArray(invos);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
        
        HashMap hmB = getAllInvbasdoc();							//����PK
        String pk_cubasdoc = null;
        String pk_psndoc = null;
        String invcode = null;
        String invname = null;
        String pk_invbasdoc = null;
        for(int i=2;i<rows;i++){
        	StringBuffer errorinfo = new StringBuffer();
            Cell[] cells = ws.getRow(i); 
            TradePlanBVO bvo = new TradePlanBVO();   
            String nyearstr = cells[0].getContents();		//��� Excel�е�2�е�1��
            if(nyearstr!=null&&nyearstr.equals("END")){
            	break;
            }
            if(nyearstr!=null&&nyearstr.length()>0){
	            String psnname = cells[2].getContents();		//Ӫ������
	            String custcode = cells[3].getContents();		//�ͻ�����
	            String custname = cells[4].getContents();		//�ͻ�����
	            
	            String[] pks = (String[])hmA.get(custcode);
	            if(pks==null||pks.length<3){
	            	errorinfo.append(custname+"���벻����  ");
	            	pk_cubasdoc = null;
	            	custname = null;
	            }else{
	            	pk_cubasdoc = pks[0];
	            	pk_psndoc = pks[1];
	            	String truepsname = pks[2];							//ϵͳ�е�Ӫ������
	            	if(!psnname.equals(truepsname)){
	            		errorinfo.append(custname+"��Ӫ��������ȷ  ");
	            	}
	            }
	            invcode = cells[5].getContents();				//���ϱ���
	            invname = cells[6].getContents();				//��������
	            UFDouble amount = new UFDouble(cells[7].getContents()==null?"0":cells[7].getContents().toString());//�ƻ�����
	            
	            pk_invbasdoc = hmB.get(invcode)==null?null:hmB.get(invcode).toString();
	            if(pk_invbasdoc==null){
	            	errorinfo.append(invcode+"���ϲ�����");
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
     * ���ݿͻ������ҵ��ͻ���pk����Ӫ������PK
     * @return
     */
    @SuppressWarnings("unchecked")
	public static HashMap getCustPsn(){
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	HashMap hm = new HashMap();
//    	StringBuffer sql = new StringBuffer()
//    	.append(" select a.custcode,a.pk_cubasdoc,b.pk_psndoc,c.psnname")
//    	.append(" from bd_cubasdoc a,eh_custyxdb b,bd_psndoc c")
//    	.append(" where a.pk_cubasdoc = b.pk_cubasdoc and b.pk_psndoc = c.pk_psndoc")
//    	.append(" and b.ismain = 'Y'")
//    	.append(" and a.custprop in (0,2)")
//    	.append(" and a.pk_corp = '"+ce.getCorporation().getPk_corp()+"'")
//    	.append(" and isnull(a.dr,0)=0")
//    	.append(" and isnull(b.dr,0)=0");
    	String sql = " select a.custcode,aa.pk_cumandoc pk_cubasdoc, "+
    				 " b.pk_psndoc,c.psnname "+
    				 " from bd_cubasdoc a,bd_cumandoc aa, "+
    				 " eh_custyxdb b,bd_psndoc c "+
    				 " where a.pk_cubasdoc=aa.pk_cubasdoc "+
    				 " and aa.pk_cumandoc = b.pk_cubasdoc  "+
    				 " and b.pk_psndoc = c.pk_psndoc "+
    				 " and b.ismain = 'Y' "+
    				 " and ( aa.custflag = '0' or aa.custflag = '2' ) "+
    				 " and aa.pk_corp = '"+ce.getCorporation().getPk_corp()+"' "+
    				 " and nvl(aa.dr,0)=0 "+
    				 " and nvl(b.dr,0)=0 ";
    	try{
    		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
    			String custcode = null;							//�ͻ�����
    			String pk_cubasdoc =null;
    			String pk_psndoc = null;						//Ӫ������
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
    
    /**
     *���ܣ�
     *<p>�õ���������PK�ͱ���</p> 
     */
    @SuppressWarnings("unchecked")
	public static HashMap getAllInvbasdoc(){
        HashMap hminvbasdoc = new HashMap();
        ClientEnvironment ce = ClientEnvironment.getInstance();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select b.pk_invmandoc pk_invbasdoc,a.invcode from bd_invbasdoc a,bd_invmandoc b,LS_INVMAN ls " +
		" where b.pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(b.dr,0)=0 " +
		" and a.pk_invbasdoc=b.pk_invbasdoc AND a.invcode = ls.ls_invcode ";
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
     * ���������ҵ��ڼ�PK
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
