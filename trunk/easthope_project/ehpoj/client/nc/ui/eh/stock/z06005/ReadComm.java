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
功能：读司磅数据
作者：newyear
日期：2008-4-14 下午02:29:30
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
     * @param inStr   读取到的有效数据
     * @param spStr     标志字符串
     * @param readFrom  读取位
     * @param dataLength   有效数据的长度
     * @return String 读取到的重量    
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
			System.out.println("去除空格后a["+i+"]="+a[i]+":长度为:"+a[i].length());
			String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp(); 
			//处理通用情况
			if (temp.length()==dataLength)
			{
				if (ischangesx.booleanValue())
				{
					//倒着取数
					StringBuffer sb = new StringBuffer();
					for (int j=temp.length()-1;j>=0;j--)
					{
						sb.append(temp.charAt(j));
					}
					temp=sb.toString();
				}	
				rt=temp.substring(begin,end).trim();
				//补零
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
			//处理特殊情况
			//处理上海希望农业有限公司

			//modify by houcq 2010-12-06取消对宝山希望的特殊处理
//			if ("1071".equals(pk_corp)&&temp.length()>0)
//			{
//				rt=a[0].substring(begin,a[0].length()-1).trim();
//				return rt;
//			}
			//处理荆州市东方希望动物营养有限公司
			//modify by houcq 2010-12-08取消对荆州公司的特殊处理
//			if ("1020".equals(pk_corp)&&temp.length()>0)
//			{
//				rt=a[0].substring(begin,end).trim();
//				return rt;
//			}	
			//处理信阳东方希望动物营养食品有限公司add by houcq 2011-01-10
			if ("1106".equals(pk_corp)&&temp.length()==dataLength+1)
			{
				rt=temp.substring(begin,end).trim();
				return rt;
			}	

		}
		return rt;	
    }
    /**
     *  点击按扭所触发的事件：打开串口,并监听串口. 
     *  -1 读数失败 -2 端口被占用 -3 没有COM口连接
     *  */
    public static String readWeight(WeighbridgeconfigVO dbvo) {
    	String rt = "";    	
        SerialPort serialPort = null;
        dbvo.setOutms(new UFDouble(1000));
        InputStream inputStream = null;
        OutputStream outStream = null;
        try {
            String portName = dbvo.getIcomtype();//需要读取的COM口            
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
            try {
                //打开COM口
                serialPort = (SerialPort) portId.open("ReadComm", 2000);
            } catch (PortInUseException e) {
            	e.printStackTrace(System.out);
            	return "-2"; 
            }
            /* 设置串口通讯参数：波特率、数据位、停止位、奇偶校验 */
            try {               
                serialPort.setSerialPortParams(dbvo.getIsecnum().intValue(),
                        dbvo.getIdatabit().intValue(), 
                        (dbvo.getIstopbit().intValue()), 
                        dbvo.getIcheckout().intValue());
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace(System.out);
            }

            try {
                //输入流
                inputStream = serialPort.getInputStream();
                //输出流
                outStream = serialPort.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
            try {
                if (inputStream.available() < 0) {
                    rt = "-3";
                    System.out.println("inputStream.available()<0");
                } else {
                    /**-- 在司磅为发送模式时读到发送的的协议 ------------------------add by wb at 2008-7-2 17:35:45--*/
                	String outprotocol = dbvo.getOutprotocol();
                	if(outprotocol!=null&&outprotocol.length()>0){
                		outStream.write(outprotocol.getBytes());
	                	Thread.sleep(1000); // 输出和输入的时间差处理 
                	}
                	/**---------end--------------------------*/
                	String str = "";
                    //数据位长度的字节数组
                    byte[] readBuffer = new byte[100];
                    /* 从线路上读取数据流 */                     
                    while (inputStream.available() > 0) 
                    {
                       	inputStream.read(readBuffer);
                    } // while end
                    str = new String(readBuffer);
                    rt = getWeight2(str.trim(), dbvo.getFc().replaceAll("space", " "), dbvo.getIreadfrom().intValue(),dbvo.getIreadlenth().intValue(), dbvo.getBytelenth(),dbvo.ischangesx,dbvo.getBytelenthw());    
                    System.out.println("司磅读数为："+rt);
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

