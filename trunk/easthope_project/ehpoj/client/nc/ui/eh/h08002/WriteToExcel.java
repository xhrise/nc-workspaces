
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
 * ����˵��: 
 * <p>
 * �ڳ������̵������ݵ���</br>
 * ֻ����Ӫ������Ϳ�����</br>
 * def13 Ӫ������PK
 * def6 ������PK
 * Custcode ���̻�������Code
 * cubasname ���̻�������name
 * </p>
 * @author chenjian
 * 2007-9-6 ����06:12:21
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static CubasdocVO[] vos = null;
    
    /**
     * ����: �򿪴����ļ�
     * @param sourceFile
     * @param newFile
     * @author chenjia n
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
        ArrayList list = new ArrayList();
        String pk_corp =nc.ui.eh.h08002.ClientUI.pk_corp; //��˾
        UFDate dmakedate = nc.ui.eh.h08002.ClientUI.dmakedate;//��������
        String coperatrid = nc.ui.eh.h08002.ClientUI.coperatrid;//����Ա
        Sheet ws = w.getSheet(sheetNum);
        rows = ws.getRows(); // ��   
        /**�������Ӫ������*/
        HashMap hmpsndoc = getAllPsndoc(pk_corp);
        
        /**�����������*/
        HashMap hminvbasdoc = getAllInvbasdoc(pk_corp);
        
        /**������п���*/
        HashMap hmcuvbasdoc = getAllCubasdoc(pk_corp);
        
                
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            int len = cells.length;      //��ȡEXCEL�ĳ���
            CubasdocVO cvo = new CubasdocVO();//��ſ���Excel���е�����
                StringBuffer info = new StringBuffer();
                
                String cubascode = cells[0].getContents();//���̱���
                if(cubascode!=null&&cubascode.equals("END")){
                	break;
                }
                String pk_cubasdoc = null;
                if(!"".equals(cubascode)){
                	pk_cubasdoc = hmcuvbasdoc.get(cubascode) == null? null : hmcuvbasdoc.get(cubascode).toString();
                	if(pk_cubasdoc == null){
                		info.append("���̱�û��ά��\t");
                	}
                }else{
                	info.append("���̲���Ϊ��\t");
                }
                cvo.setCustcode(cubascode);     //��ſ��̱���
                cvo.setPk_cubasdoc(pk_cubasdoc);//������PK
                
                
                String cubasname = cells[1].getContents();//�������� 
                cvo.setCustname(cubasname);//��ſ�������
                
                String yscode = cells[2].getContents();//Ӫ������
                String pk_psndoc = null;//Ӫ����ԱPK
                
                if(!yscode.equals("")){
                    pk_psndoc = hmpsndoc.get(yscode)==null?null:hmpsndoc.get(yscode).toString();
                    if(pk_psndoc==null){
                        info.append("Ӫ������û��ά��\t");
                    }
                }else{
                    info.append("Ӫ��������Ϊ��\t");
                }
                cvo.setDef13(pk_psndoc);//Ӫ������PK  
                
                if(len-3>0){
                    String code = cells[3].getContents();//�����ϱ���
                    String pk_invbasdoc = null;//�����ϵ�PK
                    pk_invbasdoc = hminvbasdoc.get(code)==null?null:hminvbasdoc.get(code).toString();                    
                    cvo.setDef6(pk_invbasdoc);//������PK
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
     * ���ܣ�
     * <p>����Ӫ������</p>
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
     * ���ܣ�
     * <p>��������</p>
     */
    @SuppressWarnings("unchecked")
    public static HashMap getAllInvbasdoc(String pk_corp){
        HashMap hminvbasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql =" select b.pk_invmandoc pk_invbasdoc,a.invcode from bd_invbasdoc a,bd_invmandoc b " +
        		" where b.pk_corp = '"+pk_corp+"' and isnull(b.dr,0)=0 " +
        		" and a.pk_invbasdoc=b.pk_invbasdoc and SUBSTR(a.invcode,1,4) IN ('0101','0102','0103','0104') ";//�˴�Ϊ������
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
     * ���ܣ�
     * <p>
     * �õ����п���
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