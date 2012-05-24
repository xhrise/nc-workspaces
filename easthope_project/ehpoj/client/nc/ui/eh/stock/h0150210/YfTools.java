package nc.ui.eh.stock.h0150210;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.vo.eh.stock.z00140.SwVO;
import nc.vo.pub.lang.UFDouble;
public class YfTools {

	 /***��·����**/
	 String tlyscode =  "001"; 
	
	 public static final String[] tlysmc = {"��վ��","�복��","·�˷�","ȡ����","������"};			//��·����
	 
	 public static final String[] tlysfilds = {"tlszf","tlqcf","tllyf","tlqhf","tlqtf"}; 
	 
	 public static final String tlyszd = PubTools.combinArrayToString3(tlysfilds);
		 
	 /***��������***/
	 String glyscode =  "002"; 
	 public static final String[] glysmc = {"��·�˷�"};
	 
	 public static final String[] glysfilds = {"glyf"};

	 public static final String glyszd = PubTools.combinArrayToString3(glysfilds);
	 
	 /***ˮ·����**/
	 String slyscode1 =  "003";
	 String slyscode2 =  "004";
	 public static final String[] slysmc = {"���˷�","ת�˷�","��;�˷�","������"};		
	 
	 public static final String[] slysfilds = {"slhyf","slzyf","sldtyf","slqtf"}; 
	 
	 public static final String slyszd = PubTools.combinArrayToString3(slysfilds);
	 
	 /***��·��װ������**/
	 String jzxyscode = "005";
	 public static final String[] jzxysmc = {"��վ��","��·����","ȡ����","������"};		
	 
	 public static final String[] jzxysfilds = {"jzxszf","jzxtlf","jzxqhf","jzxqtf"}; 
	 
	 public static final String jzxyszd = PubTools.combinArrayToString3(jzxysfilds);
		
	 
	 /**���ʻ��� add by wb 2009��4��8��16:21:58***/
	 public static String gjyscode = "006";
	 
	 public static final String[] gjysmc = {"���˷�","���շ�","��˰˰��(%)","��ֵ˰˰��(%)","����","���ӷ�","��;�˷�"};
	 
	 public static final String[] gjfilds = {"gjhyf","gjbxf","gjgsrate","gjzzsrate","gjhlrate","gjgzf","gjdtyf"}; 
	 
	 public static final String gjyszd = PubTools.combinArrayToString3(gjfilds);
	 
	 /***
	  * �������ϣ��������˷ѷ�ʽ�õ��ϼ��˷Ѽ���ϸ
	  * wb 2009-3-3 14:34:02
	  * @param pk_invbasdoc
	  * @param pk_areacl
	  * @param yscode
	  * @param pk_corp
	  * @return arr 0 �ϼ��˷� 1 �˷���ϸ
	 * @throws Exception 
	  */
	 @SuppressWarnings("unchecked")
	public ArrayList getYf(String pk_invbasdoc,String pk_areacl,String pk_sw,String pk_corp,UFDouble tpprice)   {
		 ArrayList yf = new ArrayList();
		 
		 String[] ysmc = null;
		 String[] ysfilds = null;
		 String yszd = null;
	 try {
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 SwVO swvo = (SwVO)iUAPQueryBS.retrieveByPK(SwVO.class, pk_sw);
		 String yscode = swvo!=null?swvo.getSwcode():"";
		 //�ж����䷽ʽ
		 if(yscode.equals(tlyscode)){
			 ysmc = tlysmc;
			 ysfilds = tlysfilds;
			 yszd = tlyszd;
		 } 
		 if(yscode.equals(glyscode)){
			 ysmc = glysmc;
			 ysfilds = glysfilds;
			 yszd = glyszd;
		 } 
		 if(yscode.equals(slyscode1)||yscode.equals(slyscode2)){
			 ysmc = slysmc;
			 ysfilds = slysfilds;
			 yszd = slyszd;
		 } 
		 if(yscode.equals(jzxyscode)){
			 ysmc = jzxysmc;
			 ysfilds = jzxysfilds;
			 yszd = jzxyszd;
		 } 
		 if(yscode.equals(gjyscode)){
			 ysmc = gjysmc;
			 ysfilds = gjfilds;
			 yszd = gjyszd;
		 } 
		 
		 StringBuffer sql = new StringBuffer()
		 .append("  SELECT "+yszd+"")
		 .append("  FROM eh_stock_rwcarriage")
		 .append("  WHERE pk_invbasdoc = '"+pk_invbasdoc+"'  ")
		 .append("  AND pk_areacl = '"+pk_areacl+"' AND pk_corp = '"+pk_corp+"'")
		 .append("  AND ISNULL(dr,0)=0");
		 ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				StringBuffer memo = new StringBuffer();		//�˷���ϸ
				UFDouble sumyf = new UFDouble(0);			//�ϼ��˷�
				for(int i=0;i<ysfilds.length;i++){			//�ǹ��ʻ���ʱ���������
					UFDouble yfmx = new UFDouble(hmA.get(ysfilds[i])==null?"0":hmA.get(ysfilds[i]).toString());
					if(yfmx.toDouble()!=0){
						sumyf = sumyf.add(yfmx);
						memo.append(ysmc[i]+":"+yfmx+" ");
					}
				}
				
				if(yscode.equals(gjyscode)){				//���ʻ���ʱ										
					/***���ʻ��� ������=ѯ�۵��е� ��������+���˷�+���շ�
						������˰�۸�=������*��1+��˰˰�ʣ�*��1+��ֵ˰˰�ʣ�
						ѯ�۵��е����ۣ�����ң�=������˰�۸�X����+���ӷ�+��;�˷ѡ�
					****/
					UFDouble gjhyf = new UFDouble(hmA.get("gjhyf")==null?"0":hmA.get("gjhyf").toString());		//���˷�
					UFDouble gjbxf = new UFDouble(hmA.get("gjbxf")==null?"0":hmA.get("gjbxf").toString());		//���շ�
					UFDouble gjgsrate = new UFDouble(hmA.get("gjgsrate")==null?"0":hmA.get("gjgsrate").toString()).div(100);			//��˰˰��/100
					UFDouble gjzzsrate = new UFDouble(hmA.get("gjzzsrate")==null?"0":hmA.get("gjzzsrate").toString()).div(100);			//��ֵ˰˰��/100
					UFDouble gjhlrate = new UFDouble(hmA.get("gjhlrate")==null?"0":hmA.get("gjhlrate").toString());			//����
					UFDouble gjgzf = new UFDouble(hmA.get("gjgzf")==null?"0":hmA.get("gjgzf").toString());					//�����
					UFDouble gjdtyf = new UFDouble(hmA.get("gjdtyf")==null?"0":hmA.get("gjdtyf").toString());				//���˷�
					
					UFDouble daj = tpprice.add(gjhyf).add(gjbxf);				//������=��������+���˷�+���շ�
					UFDouble dawsj = daj.multiply(new UFDouble(1).add(gjgsrate)).multiply(new UFDouble(1).add(gjzzsrate));//������˰�۸�=������X��1+��˰˰�ʣ�X��1+��ֵ˰˰�ʣ� 
					sumyf = dawsj.multiply(gjhlrate).add(gjgzf).add(gjdtyf);			//������ = ������˰�۸�X����+���ӷ�+��;�˷ѡ�
					memo.append("������:"+new UFDouble(daj.toDouble(),2)+" "+"������˰��:"+new UFDouble(dawsj.toDouble(),2)+" ");
				}
			
				yf.add(sumyf);				//������
				yf.add(memo.toString());	//��ϸ����
			}
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		 return yf;
	 }
}
