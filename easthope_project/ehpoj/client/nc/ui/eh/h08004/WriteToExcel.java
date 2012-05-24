
package nc.ui.eh.h08004;

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
import nc.bs.eh.h08004.CalcKcybbBDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.eh.h08004.CalcKcybbBVO;
import nc.vo.eh.h08004.InvmanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵��: �ڳ�������±������ݵ���
 * @author zqy
 * 2007-9-6 ����06:12:21
 */

public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static CalcKcybbBVO[] vos = null;
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
    public static CalcKcybbBVO[] readData(Object o,int x,int y,int sheetNum){
        String pk_corp = nc.ui.eh.h08004.ClientUI.pk_corp;
        UFDate dmakedate = nc.ui.eh.h08004.ClientUI.dmakedate;
        String coperiod = nc.ui.eh.h08004.ClientUI.coperatrid;        
        ArrayList list = new ArrayList();
        Sheet ws = w.getSheet(sheetNum);  
        rows = ws.getRows(); // ��   
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        
        HashMap hmcode = new HashMap();                             //���ϱ���
        
        InvmanVO[] invos = new InvmanVO[rows];
        for(int i=2;i<rows;i++){
        	InvmanVO invo = new InvmanVO();//��ʱ������Ϣ��VO
        	Cell[] cells = ws.getRow(i);//������
        	String invcode = cells[0].getContents();            //���ϱ���
        	if(invcode!=null&&invcode.equals("END")){
        		break;
            }else{
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
            //}
       
//        CalcKcybbBDAO cb = new CalcKcybbBDAO();//����ʱ���ϴ������
//        cb.insertLsVo(ar);
        HashMap hminvcode = getAllInvcode(pk_corp);                        //ȡ�ö�ȡ���ϱ��������PK
        HashMap hmstordoc = getAllStordoc(pk_corp);                        //������вֿ�
        	
        for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            CalcKcybbBVO ivo = new CalcKcybbBVO();                  //��ſ���±���Excel������
                StringBuffer info = new StringBuffer();             //��ע
                String invcode = cells[0].getContents();            //���ϱ���
                if(invcode!=null&&invcode.equals("END")){
                	break;
                }
                String pk_invbasdoc = null;
                if(invcode!=null){
                    pk_invbasdoc = hminvcode.get(invcode)==null?"":hminvcode.get(invcode).toString();
                    if(pk_invbasdoc==""){
                      info.append("����û��ά��\t");
                    }
                }else{
                    info.append("���ϱ��벻��Ϊ��\t");
                }
                if(hmcode.containsKey(invcode)){
                    info.append("���ϱ������ظ�\t");
                }else{
                    hmcode.put(invcode, invcode);
                }                
                ivo.setPk_invbasdoc(pk_invbasdoc);//�������               
                
                String dw = cells[1].getContents();//��λ
                String pk_measdoc = null;
                if(dw!=null){
                    String sql = " select pk_measdoc from bd_measdoc where measname='"+dw+"' and NVL(dr,0)=0 ";
                    try {
                        ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                        if(all!=null && all.size()>0){
                            for(int m=0;m<all.size();m++){
                                HashMap hm = (HashMap) all.get(m);
                                pk_measdoc = hm.get("pk_measdoc")==null?null:hm.get("pk_measdoc").toString();
                                if(pk_measdoc==null){
                                    info.append("��λû��ά��\t");
                                }
                            }
                        }
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                }else{
                    info.append("��λ����Ϊ��\t");
                }
                ivo.setDef_2(pk_measdoc);//��λ
                
                String storname = cells[2].getContents();//�ֿ�
                String pk_stordoc = null;
                if(storname!=null){
                    pk_stordoc = hmstordoc.get(storname)==null?null:hmstordoc.get(storname).toString();
                    if(pk_stordoc==null){
                        info.append("�ֿ�û��ά��");
                    }
                }else{
                    info.append("�ֿⲻ��Ϊ��");
                }
                
                ivo.setDef_3(pk_stordoc);//�ֿ�
                
                String qmje = cells[3].getContents();//��ĩ���
                boolean isnum = CheckStringToNum(qmje);
                if( isnum==true ){
                    info.append(""); 
                    ivo.setQmje(new UFDouble(qmje));//��ĩ���
                }else{
                    info.append("��ĩ���Ӧ��Ϊ���ֶ����Ǻ���\t"); 
                    ivo.setQmje(new UFDouble(0.00));//��ĩ���
                }                
                
                String qmsl = cells[4].getContents();//��ĩ���� 
                boolean isnum1 = CheckStringToNum(qmsl);
                if( isnum1==true ){
                    info.append(""); 
                    ivo.setQmsl(new UFDouble(qmsl));//��ĩ����
                }else{
                    info.append("��ĩ����Ӧ��Ϊ���ֶ����Ǻ���\t");
                    ivo.setQmsl(new UFDouble(0.00));//��ĩ����
                }
                
                /**---------�¼ӵ��������¶�-----------------------**/
                String nyear = cells[5].getContents();  //���
                ivo.setDef_1(nyear);
                
                String nmonth = cells[6].getContents(); //�¶�
                ivo.setDef_2(nmonth);
                
                ivo.setDef_4(pk_corp);
                ivo.setDef_5(dmakedate.toString());
                ivo.setDef_6(coperiod);
                ivo.setMemo(info.toString());
                
                list.add(ivo);              
        }
        if(list.size()>0){
            vos = (CalcKcybbBVO[])list.toArray(new CalcKcybbBVO[0]);
//          ����ʱ���ж�Ӧ��˾������ɾ������  
            StringBuffer sql = new StringBuffer()
            .append(" DELETE FROM ls_invman WHERE pk_corp = '"+pk_corp+"' ");
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            try {
				pubitf.updateSQL(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return vos;
    }
    
    //�ַ�����ֵ֮���У��(isnumΪtrueʱ,Ϊ��ֵ�ͣ�����Ϊ�ַ���) add by zqy 
    private static Boolean CheckStringToNum(String str) {
        Pattern pattern = Pattern.compile("[-]?[0.000000-9.000000]*"); 
        Matcher isNum = pattern.matcher(str);   
        boolean isnum = isNum.matches();
        return isnum; 
    }
    
    //���вֿ�
    @SuppressWarnings("unchecked")
    public static HashMap getAllStordoc(String pk_corp){
        HashMap hmstordoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        //ϵͳ�ֿ��
        String sql = "select pk_stordoc,storname from bd_stordoc where pk_corp = '"+pk_corp+"' and NVL(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null&&arr.size()>0){
                String pk_stordoc = null;
                String storname = null;
                for(int i=0; i<arr.size(); i++){
                    HashMap hmarr = (HashMap)arr.get(i);
                    pk_stordoc = hmarr.get("pk_stordoc")==null?"":hmarr.get("pk_stordoc").toString();
                    storname = hmarr.get("storname")==null?"":hmarr.get("storname").toString();
                    hmstordoc.put(storname, pk_stordoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmstordoc;
    }
    
    
    //�������ϱ���
    @SuppressWarnings("unchecked")
    public static HashMap getAllInvcode(String pk_corp){
        HashMap hminvcode = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        //String sql ="select pk_invbasdoc,invcode from eh_invbasdoc where pk_corp = '"+pk_corp+"' and isnull(dr,0)=0 ";
        StringBuffer sql =new StringBuffer()
        .append(" SELECT iman.pk_invmandoc pk_invbasdoc , ibas.invcode FROM bd_invbasdoc ibas,bd_invmandoc iman,LS_INVMAN ls ")
        .append(" WHERE iman.pk_invbasdoc = ibas.pk_invbasdoc AND ibas.invcode = ls.ls_invcode AND iman.pk_corp = '"+pk_corp+"' AND NVL(iman.dr, 0) = 0 ");
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_invbasdoc = null;
                String invcode = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                    invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
                    hminvcode.put(invcode, pk_invbasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hminvcode;
    }
    
}
