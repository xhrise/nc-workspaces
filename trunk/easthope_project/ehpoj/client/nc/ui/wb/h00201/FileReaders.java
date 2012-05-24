package nc.ui.wb.h00201;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.vo.eh.kc.h0257005.CalcKcybbBVO;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.KcVO;
import nc.vo.eh.stock.z0150502.PerioddiscountHVO;
import nc.vo.eh.stock.z0150502.PerioddiscountVO;
import nc.vo.eh.trade.z00115.CustyxdbVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class FileReaders {
    
    /**
     * ����: ���ļ��������� ���뵽���ݿ�
     * @param filepath �ļ�·��
     * @return:void
     * @author ʩ��
     * 2008-01-02 ����06:05:51
     */
    public void fileRow(String filepath){
        String [] ss;
        ArrayList arr1 = new ArrayList();
        int no_count=0;//δ��������
        int has_count=0;//�Ѹ�������
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String s;
            String pk_materiel=null;
            while((s=br.readLine())!=null){
                ss = s.split("\t") ;
                //���ݱ��뵥�Ÿ��±�׼����
                String custname = ss[0].trim();       //��������
                String usercode = ss[2].trim();       //����
                
                
                StringBuffer querySql1= new StringBuffer("")
                .append(" select pk_cubasdoc  from bd_cubasdoc")
                .append(" where custname = '"+custname+"' and isnull(dr,0)=0");
                StringBuffer querySql2 = new StringBuffer("")
                .append(" select pk_psndoc  from bd_psndoc")
                .append(" where psncode = '"+usercode+"' and isnull(dr,0)=0");
                IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
                Object pk_cubasdocobj = iUAPQueryBS.executeQuery(querySql1.toString(), new ColumnProcessor());
                Object pk_psndocobj = iUAPQueryBS.executeQuery(querySql2.toString(), new ColumnProcessor());
                if(pk_cubasdocobj!=null&&pk_psndocobj!=null){
                	String pk_cubasdoc = pk_cubasdocobj.toString();
                    String pk_psndoc = pk_psndocobj.toString();
                   String sql = "delete from eh_custyxdb where pk_cubasdoc = '"+pk_cubasdoc+"'";
                   PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                   pubItf.updateSQL(sql);
                   CustyxdbVO kcybVO = new CustyxdbVO();
                   kcybVO.setPk_cubasdoc(pk_cubasdoc);
                   kcybVO.setPk_psndoc(pk_psndoc);
                   kcybVO.setIsmain(new UFBoolean(true));

                   ivoPersistence.insertVO(kcybVO);

                   has_count++;
                }
                else{
                    no_count++;
                    arr1.add("+���Ե�����:"+no_count+"  "+" ����:"+custname+" ����:"+usercode+"");
                    continue;
                }
             }
             arr1.add("�Ѹ������ݣ�"+has_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<arr1.size();i++){
            System.out.println(arr1.get(i));
        }         
    }
    
    
    public static void main(String[] args) {
//    	new FileReaders().fileRow("D:/ksdb.txt");   
    	String res = "ABCDE+/-=*";
    	if(res.matches("[A-E+-/=]+/**/")){
    		System.out.println("����Ҫ��!");
    	}else{
    		System.out.println("������Ҫ��!");
    	}
    		
    }
    
    /**
     * ���������Ƽ�
     * 
     * **/
    public void insertInvPrice(String filepath){
        String [] ss;
        ArrayList arr1 = new ArrayList();
        int no_count=0;//δ��������
        int has_count=0;//�Ѹ�������
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String s;
            String pk_materiel=null;
            while((s=br.readLine())!=null){
                ss = s.split("\t") ;
                String invcode =  ss[0].trim();       //���ϱ���
                String ivnname =  ss[1].trim();       //��������
                UFDouble price = new UFDouble(ss[6].trim()); //�Ƽ�
                
                StringBuffer querySql1= new StringBuffer("")
                .append(" select pk_invbasdoc  from eh_invbasdoc")
                .append(" where invcode = '"+invcode+"' and isnull(dr,0)=0");
               
                IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                
                Object pk_invbasdocobj = iUAPQueryBS.executeQuery(querySql1.toString(), new ColumnProcessor());
                if(pk_invbasdocobj!=null&&pk_invbasdocobj!=null){
                	String pk_invbasdoc = pk_invbasdocobj.toString();
                    String sql = "update eh_invbasdoc set price = "+price+" where pk_invbasdoc = '"+pk_invbasdoc+"'";
                   PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                   pubItf.updateSQL(sql);
                  has_count++;
                }
                else{
                    no_count++;
                    arr1.add("+���Ե�����:"+no_count+"  "+"���룺"+invcode+" ����:"+ivnname);
                    continue;
                }
             }
             arr1.add("�Ѹ������ݣ�"+has_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<arr1.size();i++){
            System.out.println(arr1.get(i));
        }         
    }
    
    public void impInvKC1(String filepath){
        String [] ss;
        ArrayList arr1 = new ArrayList();
        int no_count=0;//δ��������
        int has_count=0;//�Ѹ�������
        try {
        	 IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
             
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String s;
            ArrayList insertArr=new ArrayList();                                       //������µ�����
            while((s=br.readLine())!=null){
                ss = s.split("\t") ;
                String invcode =  ss[1].trim();     //���ϱ���
                String invname = ss[2].trim();
                UFDouble kcamount = new UFDouble(ss[8].trim()==null||ss[8].trim()==""?"0":ss[8].trim()); //�����
                String sql = "select warehouseid,pk_measdoc,pk_invbasdoc from eh_invbasdoc where invcode = '"+invcode+"' and isnull(dr,0)=0";
                PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                Vector vc1 = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
                KcVO kcvo = new KcVO();
                if(vc1!=null&&vc1.size()>0){
                	Vector vcc = (Vector)vc1.get(0);
                	String pk_stordoc = vcc.get(0).toString();
                	String pk_measdoc = vcc.get(1).toString();
                	String pk_invbasdoc = vcc.get(2).toString();
                	kcvo.setPk_invbasdoc(pk_invbasdoc);
                	kcvo.setPk_store(pk_stordoc);
                	kcvo.setPk_unit(pk_measdoc);
                	kcvo.setPk_corp("1001");
                	kcvo.setAmount(kcamount);
                	insertArr.add(kcvo);
                	
                   has_count++;
                }
                else{
                    no_count++;
                    arr1.add("+���Ե�����:"+no_count+"  "+"���룺"+invcode+" ����:"+invname);
                    continue;
                }
             }
            KcVO[] insertSupervo=(KcVO[])insertArr.toArray(new KcVO[insertArr.size()]);     //����������
            ivoPersistence.insertVOArray(insertSupervo);
             arr1.add("�Ѹ������ݣ�"+has_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<arr1.size();i++){
            System.out.println(arr1.get(i));
        }         
    }
    
    public void impInvKC(String filepath){
        String [] ss;
        ArrayList arr1 = new ArrayList();
        int no_count=0;//δ��������
        int has_count=0;//�Ѹ�������
        try {
        	 IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
             
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String s;
            String pk_materiel=null;
            ArrayList insertArr=new ArrayList();                                       //������µ�����
            while((s=br.readLine())!=null){
                ss = s.split("\t") ;
                String invcode =  ss[0].trim();       //���ϱ���
                String ivnname =  ss[1].trim();       //��������
                UFDouble price = new UFDouble(ss[6].trim()==null||ss[6].trim()==""?"0":ss[6].trim()); //�����
                
                StringBuffer querySql1= new StringBuffer("")
                .append(" select pk_invbasdoc,pk_measdoc,warehouseid  from eh_invbasdoc")
                .append(" where invcode = '"+invcode+"' and isnull(dr,0)=0");
                
                PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                Vector vc1 = (Vector)iUAPQueryBS.executeQuery(querySql1.toString(), new VectorProcessor());
                
                if(vc1!=null&&vc1.size()>0){
                	Vector vcc = (Vector)vc1.get(0);
                	String pk_invbasdoc = vcc.get(0).toString();
                	String updateSQL = " update eh_invbasdoc set price = "+price+" where pk_invbasdoc = '"+pk_invbasdoc+"'";
                	pubItf.updateSQL(updateSQL);
                   has_count++;
                }
                else{
                    no_count++;
                    arr1.add("+���Ե�����:"+no_count+"  "+"���룺"+invcode+" ����:"+ivnname);
                    continue;
                }
             }
            KcVO[] insertSupervo=(KcVO[])insertArr.toArray(new KcVO[insertArr.size()]);     //����������
            ivoPersistence.insertVOArray(insertSupervo);
             arr1.add("�Ѹ������ݣ�"+has_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<arr1.size();i++){
            System.out.println(arr1.get(i));
        }         
    }
    
    /**
     * �������·ݿ���±��� ��Ʒ C ԭ�� Y �ֲֿ⵼��,���ҳ��ֿ� pk
     * 
     * **/
    public void impInvKCYBB(String filepath,String invtype){
        String [] ss;
        ArrayList arr1 = new ArrayList();
        int no_count=0;//δ��������
        int has_count=0;//�Ѹ�������
        try {
        	 IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
        	 PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
             String pk_kcybb =  pubItf.getPri("1001");
             CalcKcybbVO hvo = new CalcKcybbVO();
             hvo.setPrimaryKey(pk_kcybb);
             hvo.setPk_period("1001ZZ10000000003PUW");        //����ڼ�
             hvo.setNyear(new Integer(2008));   //���
             hvo.setNmonth(new Integer(07)); //�·�
             hvo.setPk_store("1001ZZ100000000000VJ");          //�ֿ� ?????PK
             hvo.setCoperatorid("0001ZZ10000000000KX4");    //����Ա
             hvo.setCalcdate(new UFDate(new Date()));
             hvo.setPk_corp("1001");
             hvo.setInvtype(invtype);
             hvo.setDr(new Integer(0));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String s;
            ArrayList insertArr=new ArrayList();                                       //������µ�����
            while((s=br.readLine())!=null){
                ss = s.split("\t") ;
                String invcode =  ss[0].trim();       //���ϱ���
                String ivnname =  ss[1].trim();       //��������
                String storname = ss[8].trim();       //�ֿ�����
                UFDouble kcamount = new UFDouble(ss[7].trim()); //�����
                UFDouble qmprice = new UFDouble(ss[8].trim()); //��ĩ���
                StringBuffer querySql1= new StringBuffer("")
                .append(" select pk_invbasdoc,pk_measdoc  from eh_invbasdoc")
                .append(" where invcode = '"+invcode+"' and isnull(dr,0)=0");
                
               
                IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                Vector vc1 = (Vector)iUAPQueryBS.executeQuery(querySql1.toString(), new VectorProcessor());
                
                
                if(vc1!=null&&vc1.size()>0){
                	Vector vcc = (Vector)vc1.get(0);
                	String pk_invbasdoc = vcc.get(0).toString();
                    String pk_measdoc = vcc.get(1).toString();
                    
                    CalcKcybbBVO vo = getNewKcybbBVO();
                    vo.setPk_kcybb(pk_kcybb);      //��������
                    vo.setPk_invbasdoc(pk_invbasdoc);
                    vo.setQmsl(kcamount);      
                    vo.setQmje(qmprice);
                    insertArr.add(vo);
                   
                   has_count++;
                }
                else{
                    no_count++;
                    arr1.add("+���Ե�����:"+no_count+"  "+"���룺"+invcode+" ����:"+ivnname);
                    continue;
                }
             }
            CalcKcybbBVO[] bVos=(CalcKcybbBVO[])insertArr.toArray(new CalcKcybbBVO[insertArr.size()]);     //����������
            if(hvo!=null && bVos!=null && bVos.length>0){
            	ivoPersistence.insertVO(hvo);
            	ivoPersistence.insertVOArray(bVos);
            }   
             arr1.add("�Ѹ������ݣ�"+has_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<arr1.size();i++){
            System.out.println(arr1.get(i));
        }         
    }
    
    /**
    ���ܣ���ʼ��VO,�������е�UFDouble����Ϊ0
    ���ߣ�newyear
    ���ڣ�2007-12-6 ����08:34:39
    @return
    @throws RemoteException
     */
    protected CalcKcybbBVO getNewKcybbBVO() throws RemoteException{
    	CalcKcybbBVO vo = new CalcKcybbBVO();
        try{
            String[] keys = vo.getAttributeNames();
            if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].endsWith("je") || keys[i].endsWith("sl"))
                        vo.setAttributeValue(keys[i], new UFDouble(0));
                }
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new java.rmi.RemoteException(e.getMessage());
        }
        return vo;
    }
    
    /**
     * ����ͻ��ڼ��ۿ� 
     * */
    public void impCusDis(String filepath){
        String [] ss;
        ArrayList<String> arr1 = new ArrayList();
        int no_count=0;//δ��������
        int has_count=0;//�Ѹ�������
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
            String s;
            IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
       	 	PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            String pk_corp = "1001";
       	 	String pk_discount_h =  pubItf.getPri(pk_corp);
            PerioddiscountHVO hVO = new PerioddiscountHVO();
            hVO.setPk_corp(pk_corp);
            hVO.setNyear(2008);
            hVO.setNmonth(8);
            hVO.setCoperatorid("0001ZZ10000000000KX4");    //����Ա
            hVO.setCalcdate(new UFDate(new Date()));
            hVO.setDr(new Integer(0));
    		hVO.setPk_perioddiscount_h(pk_discount_h);
    		ArrayList arr = new ArrayList();
            while((s=br.readLine())!=null){
                ss = s.split("\t") ;
                //���ݱ��뵥�Ÿ��±�׼����
                String custcode = ss[0].trim();       //���̱���
                String custname = ss[1].trim();       //��������
                String invcode = ss[2].trim();        //���ϱ���
                UFDouble discount = new UFDouble(ss[8].trim()==null||ss[8].trim()==""?"0":ss[8].trim());
                
                
                StringBuffer querySql1= new StringBuffer("")
                .append(" select pk_cubasdoc  from bd_cubasdoc")
                .append(" where custcode = '"+custcode+"' and isnull(dr,0)=0");
                StringBuffer querySql2 = new StringBuffer("")
                .append(" select pk_invbasdoc  from eh_invbasdoc")
                .append(" where invcode = '"+invcode+"' and isnull(dr,0)=0");
                Object pk_cubasdocobj = iUAPQueryBS.executeQuery(querySql1.toString(), new ColumnProcessor());
                Object pk_invdocobj = iUAPQueryBS.executeQuery(querySql2.toString(), new ColumnProcessor());
                if(pk_cubasdocobj!=null&&pk_invdocobj!=null){
                	String pk_cubasdoc = pk_cubasdocobj.toString();
                    String pk_invbasdoc = pk_invdocobj.toString();
                    
                    PerioddiscountVO periodVO = getNewdisVO();
	    	    	periodVO.setPk_perioddiscount_h(pk_discount_h);
	    	    	periodVO.setPk_cubasdoc(pk_cubasdoc);
	    	    	periodVO.setPk_invbasdoc(pk_invbasdoc);
		    	    periodVO.setNyear(2008);
		    	    periodVO.setNmonth(8);
		    	    periodVO.setEdiscount(discount);
		    	    periodVO.setPk_corp(pk_corp);
		    	    periodVO.setDr(new Integer(0));

		    	    arr.add(periodVO);

                   has_count++;
                }
                else{
                    no_count++;
                    arr1.add("+���Ե�����:"+no_count+"  "+" �ͻ�����:"+custname+" ���ϱ���:"+invcode+"");
                    continue;
                }
             }
            PerioddiscountVO[]   bVos=(PerioddiscountVO[])  arr.toArray(new PerioddiscountVO[arr.size()]);     //����������
            if(hVO!=null && bVos!=null && bVos.length>0){
            	ivoPersistence.insertVO(hVO);
            	ivoPersistence.insertVOArray(bVos);
            }  
             arr1.add("�Ѹ������ݣ�"+has_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<arr1.size();i++){
            System.out.println(arr1.get(i));
        }         
    }
    
    
    protected PerioddiscountVO getNewdisVO() throws RemoteException{
    	PerioddiscountVO vo = new PerioddiscountVO();
        try{
            String[] keys = vo.getAttributeNames();
            if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].endsWith("discount"))
                        vo.setAttributeValue(keys[i], new UFDouble(0));
                }
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new java.rmi.RemoteException(e.getMessage());
        }
        return vo;
    }
}
   
    

