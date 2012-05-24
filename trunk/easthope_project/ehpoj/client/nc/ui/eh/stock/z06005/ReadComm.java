/**
 * @(#)ReadComm.java	V5.01 2008-4-14
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.z06005;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import nc.ui.pub.ClientEnvironment;
import nc.vo.eh.trade.z0600301.WeighbridgeconfigVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
���ܣ���˾������
���ߣ�newyear
���ڣ�2008-4-14 ����02:29:30
 */
public class ReadComm {
    
    private boolean bread=false;
    public  ReadComm(){
    	System.setSecurityManager(null); 
    }
    public boolean isBread() {
        return bread;
    }
    public void setBread(boolean bread) {
        this.bread = bread;
    }   
      
    /**
     * 
     * @param inStr   ��ȡ������Ч����
     * @param spStr     ��־�ַ���
     * @param readFrom  ��ȡλ
     * @param dataLength   ��Ч���ݵĳ���
     * @return String ��ȡ��������    
     */    
    public static String getWeight2(String inStr, String spStr, int begin,int end,int dataLength,UFBoolean ischangesx,Integer isAddZero) 
    {
    	if (inStr.equals("-2"))
			return inStr;
		String rt = "-1";
		String[] a = inStr.split(spStr);
		for (int i=0;i<a.length;i++)
		{				
			String temp =replaceBlank(a[i]).trim();
			System.out.println("ȥ���ո��a["+i+"]="+a[i]+":����Ϊ:"+a[i].length());
			String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp(); 
			//����ͨ�����
			if (temp.length()==dataLength)
			{
				if (ischangesx.booleanValue())
				{
					//����ȡ��
					StringBuffer sb = new StringBuffer();
					for (int j=temp.length()-1;j>=0;j--)
					{
						sb.append(temp.charAt(j));
					}
					temp=sb.toString();
				}	
				rt=temp.substring(begin,end).trim();
				//����
				if (isAddZero!=null && isAddZero.intValue()>0)
				{
					StringBuffer sb2 = new StringBuffer(rt);
					for (int j=0;j<isAddZero.intValue();j++)
					{
						sb2.append(0);
					}
					rt=sb2.toString();
				}
				return rt;
			}
			//�����������
			//�����Ϻ�ϣ��ũҵ���޹�˾

			//modify by houcq 2010-12-06ȡ���Ա�ɽϣ�������⴦��
//			if ("1071".equals(pk_corp)&&temp.length()>0)
//			{
//				rt=a[0].substring(begin,a[0].length()-1).trim();
//				return rt;
//			}
			//�������ж���ϣ������Ӫ�����޹�˾
			//modify by houcq 2010-12-08ȡ���Ծ��ݹ�˾�����⴦��
//			if ("1020".equals(pk_corp)&&temp.length()>0)
//			{
//				rt=a[0].substring(begin,end).trim();
//				return rt;
//			}	
			//������������ϣ������Ӫ��ʳƷ���޹�˾add by houcq 2011-01-10
			if ("1106".equals(pk_corp)&&temp.length()==dataLength+1)
			{
				rt=temp.substring(begin,end).trim();
				return rt;
			}	

		}
		return rt;	
    }
    /**
     *  �����Ť���������¼����򿪴���,����������. 
     *  -1 ����ʧ�� -2 �˿ڱ�ռ�� -3 û��COM������
     *  */
    public static String readWeight(WeighbridgeconfigVO dbvo) {
    	String rt = "";    	
        SerialPort serialPort = null;
        dbvo.setOutms(new UFDouble(1000));
        InputStream inputStream = null;
        OutputStream outStream = null;
        try {
            String portName = dbvo.getIcomtype();//��Ҫ��ȡ��COM��            
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
            try {
                //��COM��
                serialPort = (SerialPort) portId.open("ReadComm", 2000);
            } catch (PortInUseException e) {
            	e.printStackTrace(System.out);
            	return "-2"; 
            }
            /* ���ô���ͨѶ�����������ʡ�����λ��ֹͣλ����żУ�� */
            try {               
                serialPort.setSerialPortParams(dbvo.getIsecnum().intValue(),
                        dbvo.getIdatabit().intValue(), 
                        (dbvo.getIstopbit().intValue()), 
                        dbvo.getIcheckout().intValue());
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace(System.out);
            }

            try {
                //������
                inputStream = serialPort.getInputStream();
                //�����
                outStream = serialPort.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
            try {
                if (inputStream.available() < 0) {
                    rt = "-3";
                    System.out.println("inputStream.available()<0");
                } else {
                    /**-- ��˾��Ϊ����ģʽʱ�������͵ĵ�Э�� ------------------------add by wb at 2008-7-2 17:35:45--*/
                	String outprotocol = dbvo.getOutprotocol();
                	if(outprotocol!=null&&outprotocol.length()>0){
                		outStream.write(outprotocol.getBytes());
	                	Thread.sleep(1000); // ����������ʱ���� 
                	}
                	/**---------end--------------------------*/
                	String str = "";
                    //����λ���ȵ��ֽ�����
                    byte[] readBuffer = new byte[100];
                    /* ����·�϶�ȡ������ */                     
                    while (inputStream.available() > 0) 
                    {
                       	inputStream.read(readBuffer);
                    } // while end
                    str = new String(readBuffer);
                    rt = getWeight2(str.trim(), dbvo.getFc().replaceAll("space", " "), dbvo.getIreadfrom().intValue(),dbvo.getIreadlenth().intValue(), dbvo.getBytelenth(),dbvo.ischangesx,dbvo.getBytelenthw());    
                    System.out.println("˾������Ϊ��"+rt);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        } catch (NoSuchPortException e) {
            e.printStackTrace(System.out);
            return "-3";
        } finally {
        	if (inputStream!=null)
        	{
        		try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if (outStream!=null)
        	{
        		try {
        			outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
            if (serialPort != null) {
                serialPort.close();
            }
        }
        return rt;
    }
    private static String replaceBlank(String str)
	{
	   Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	   Matcher m = p.matcher(str);
	   return  m.replaceAll(""); 
	   
	}
}

