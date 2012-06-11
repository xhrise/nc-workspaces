package com.ufsoft.iufo.calcmonitor.ui;
import com.ufida.iufo.pub.tools.AppDebug;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.bs.framework.ServiceConfig;
import nc.bs.framework.common.RuntimeEnv;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIEditorPane;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.task.ConfigCalcItemVO;
import nc.vo.iufo.task.ConfigCalcVO;
import nc.vo.logging.Debug;

import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropBO_Client;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.iufo.resource.StringResource;


/**
 * 
 * @author zzl 2005-5-17
 */
public class ConfigRepCalcQueue extends nc.ui.pub.beans.UIFrame {
    
    /**
	 * @i18n miufohbbb00067=IP地址
	 * @i18n miufohbbb00070=计算队列总长度
	 * @i18n miufohbbb00071=单节点计算队列长度
	 */
    public final static String[] COLUMN_NAMES = { StringResource.getStringResource("miufohbbb00067"), StringResource.getStringResource("miufohbbb00070"), StringResource.getStringResource("miufohbbb00071")};





    private Font m_font = new Font("dialog",0,13);
    
    private JTextField m_iSizePerNode;
    private JTextField m_dblSizePerCPU; 
    private JTextField m_dblNodeSizeRate;
    
    private JRadioButton m_radioByNode;
    private JRadioButton m_radioByCPU;
    
    private JTable m_table;
    
    private ButtonGroup buttonGroup = new ButtonGroup();




    /**
	 * @i18n miufohbbb00072=请输入端口号
	 * @i18n miufohbbb00073=报表计算队列配置
	 */
    public static void main(String args[]) {    	
        String port = JOptionPane.showInputDialog(null,StringResource.getStringResource("miufohbbb00072"));
        
    	initProxy(port);
    	
        ConfigRepCalcQueue frame = new ConfigRepCalcQueue();
        frame.setTitle(StringResource.getStringResource("miufohbbb00073"));
        frame.setBounds(100, 100, 500, 375);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private static void initProxy(String strPort){
    	RuntimeEnv.getInstance().setRunningInServer(false);
    	
    	ServiceConfig.setBaseHttpURL("http://127.0.0.1:"+strPort);
    	
    	// 得到接入的服务器IP
    	Properties properties = new Properties();
    	try {
    		properties.put("codesyncServletURL","/CodeSynServlet");
    		properties.put("SERVICELOOKUP_URL","/ServiceLookuperServlet");
    		properties.put("SERVICEDISPATCH_URL","/ServiceDispatcherServlet");
    		properties.put("CLIENT_COMMUNICATOR","nc.bs.framework.comn.cli.JavaURLCommunicator");
    		
    		RuntimeEnv.getInstance().setProperty("CLIENT_COMMUNICATOR",
    				properties.getProperty("CLIENT_COMMUNICATOR"));
    		String serviceLookuperURL = ServiceConfig.getBaseHttpURL()
    				+ properties.getProperty("SERVICELOOKUP_URL");
    		String serviceDispatcherURL = ServiceConfig.getBaseHttpURL()
    				+ properties.getProperty("SERVICEDISPATCH_URL");

    		Debug.debug("The ServiceLookuper URL is " + serviceDispatcherURL);
    		RuntimeEnv.getInstance().setProperty("SERVICELOOKUP_URL",
    				serviceLookuperURL);
    		RuntimeEnv.getInstance().setProperty("SERVICEDISPATCH_URL",
    				serviceDispatcherURL);
    		
    	} catch (Exception e) {
    		AppDebug.debug(e);//@devTools e.printStackTrace();
    	}    	
    }



















    /**
	 * @i18n miufohbbb00074=默认配置
	 * @i18n miufohbbb00075=各节点计算队列最大长度相同，最大长度为：
	 * @i18n miufohbbb00077=按服务器CPU数配置服务器各节点计算队列总长度的最大数，最大数为：
	 * @i18n miufohbbb00079=服务器上各节点计算队列的最大长度：
	 * @i18n miufohbbb00083=分服务器配置
	 * @i18n miufohbbb00084=服务器计算队列配置列表
	 * @i18n miufo1000950=添加
	 * @i18n ubichart00006=删除
	 * @i18n miufo1000758=确定
	 * @i18n miufo1000757=取消
	 * @i18n miufo1001113=帮助
     * @i18n miufohbbb00176=（1-20，整数）
     * @i18n miufohbbb00178=(1-20,五位小数)
     * @i18n miufohbbb00179=(0.1-1,五位小数)
     * @i18n miufohbbb00180=总数×
     * @i18n miufohbbb00181=CPU数×
	 */
    public ConfigRepCalcQueue() {
        super();
        initUI();
        final nc.ui.pub.beans.UITabbedPane tabbedPane = new nc.ui.pub.beans.UITabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        final JPanel panel = new UIPanel();
        panel.setBounds(7, 24, 487, 321);
        panel.setLayout(null);
        tabbedPane.addTab(StringResource.getStringResource("miufohbbb00074"), null, panel, null);

        m_radioByNode = new UIRadioButton();
        m_radioByNode.setBounds(26, 29, 24, 27);
        panel.add(m_radioByNode);
        buttonGroup.add(m_radioByNode);

        m_radioByCPU = new UIRadioButton();
        m_radioByCPU.setBounds(26, 110, 24, 27);
        panel.add(m_radioByCPU);
        buttonGroup.add(m_radioByCPU);

        final JLabel label = new nc.ui.pub.beans.UILabel();
        label.setFont(m_font);
        label.setBounds(53, 27, 409, 30);
        panel.add(label);
        label.setText(StringResource.getStringResource("miufohbbb00075"));

        m_iSizePerNode = new UITextField();
        m_iSizePerNode.setBounds(57, 58, 50, 24);
        panel.add(m_iSizePerNode);

        final JLabel label_1 = new nc.ui.pub.beans.UILabel();
        label_1.setFont(m_font);
        label_1.setBounds(134, 58, 120, 30);
        panel.add(label_1);
        label_1.setText(StringResource.getStringResource("miufohbbb00176"));

        final JLabel label_2 = new nc.ui.pub.beans.UILabel();
        label_2.setFont(m_font);
        label_2.setBounds(50, 110, 437, 30);
        panel.add(label_2);
        label_2.setText(StringResource.getStringResource("miufohbbb00077"));

        m_dblSizePerCPU = new UITextField();
        m_dblSizePerCPU.setBounds(103, 140, 60, 24);
        panel.add(m_dblSizePerCPU);

        final JLabel label_3 = new nc.ui.pub.beans.UILabel();
        label_3.setFont(m_font);
        label_3.setBounds(227, 136, 120, 30);
        panel.add(label_3);
        label_3.setText(StringResource.getStringResource("miufohbbb00178"));

        final JLabel label_4 = new nc.ui.pub.beans.UILabel();
        label_4.setFont(m_font);
        label_4.setBounds(54, 177, 399, 30);
        panel.add(label_4);
        label_4.setText(StringResource.getStringResource("miufohbbb00079"));

        m_dblNodeSizeRate = new UITextField();
        m_dblNodeSizeRate.setBounds(90, 210, 60, 24);
        panel.add(m_dblNodeSizeRate);

        final JLabel label_5 = new nc.ui.pub.beans.UILabel();
        label_5.setFont(m_font);
        label_5.setBounds(222, 206, 120, 30);
        panel.add(label_5);
        label_5.setText(StringResource.getStringResource("miufohbbb00179"));

        final JLabel label_6 = new nc.ui.pub.beans.UILabel();
        label_6.setFont(m_font);
        label_6.setBounds(50, 209, 43, 30);
        panel.add(label_6);
        label_6.setText(StringResource.getStringResource("miufohbbb00180"));

        final JLabel label_7 = new nc.ui.pub.beans.UILabel();
        label_7.setFont(m_font);
        label_7.setBounds(50, 140, 56, 30);
        panel.add(label_7);
        label_7.setText(StringResource.getStringResource("miufohbbb00181"));

        final JPanel panel_1 = new UIPanel();
        panel_1.setLayout(null);
        tabbedPane.addTab(StringResource.getStringResource("miufohbbb00083"), null, panel_1, null);

        final JLabel label_8 = new nc.ui.pub.beans.UILabel();
        label_8.setFont(m_font);
        label_8.setBounds(10, 9, 194, 28);
        panel_1.add(label_8);
        label_8.setText(StringResource.getStringResource("miufohbbb00084"));
        
        m_table = new nc.ui.pub.beans.UITable(0,3);
        JScrollPane scrollPane = new UIScrollPane(m_table);
        scrollPane.setBounds(27, 46, 422, 128);
        panel_1.add(scrollPane);

        final JButton btnAdd = new UIButton();
        btnAdd.setBounds(90, 206, 120, 30);
        panel_1.add(btnAdd);
        btnAdd.setText(StringResource.getStringResource("miufo1000950"));
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                addTableRow(null,null,null);                
            }
            
        });

        final JButton btnDel = new nc.ui.pub.beans.UIButton();
        btnDel.setBounds(270, 205, 75, 22);
        panel_1.add(btnDel);
        btnDel.setText(StringResource.getStringResource("ubichart00006"));
        btnDel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ((DefaultTableModel)m_table.getModel()).removeRow(m_table.getSelectedRow());             
            }
            
        });

        final JPanel btnPanel = new UIPanel();
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        final JButton btnOK = new nc.ui.pub.beans.UIButton();
        btnPanel.add(btnOK);
        btnOK.setText(StringResource.getStringResource("miufo1000758"));
        btnOK.addActionListener(new ActionListener(){








            /**
			 * @i18n miufohbbb00085=表格中不能有空值
			 * @i18n miufohbbb00086=表格中后两列要求输入整数
			 * @i18n miufohbbb00087=单节点计算队列长度只能在1到20之间
			 * @i18n miufohbbb00088=服务器计算队列总长度只能在1到100之间
			 * @i18n miufohbbb00089=IP地址有重复
             * @i18n miufohbbb00174=不是合法的IP地址
			 */
            public void actionPerformed(ActionEvent e) {
                //判断不能有null值。
                for(int row=0;row<m_table.getRowCount();row++){
                    for(int col=0;col<m_table.getColumnCount();col++){
                        if(m_table.getValueAt(row,col)==null || m_table.getValueAt(row,col).equals("")){
                            JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource.getStringResource("miufohbbb00085"));
                            return;
                        }
                        if(col == 1 || col == 2){
                        	if (!isInteger(m_table.getValueAt(row,col).toString())){
                            	JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource
										.getStringResource("miufohbbb00086"));
                            	return;
                        	}
                        	int iNum=Integer.parseInt(m_table.getValueAt(row,col).toString());
                        	if (col==2 && (iNum<1 || iNum>20)){
                        		JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource
										.getStringResource("miufohbbb00087"));
                        		return;
                        	}
                        	
                        	if (col==1 && (iNum<1 || iNum>100)){
                        		JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource
										.getStringResource("miufohbbb00088"));
                        		return;
                        	}
                        }
                        if (col==0){
                        	String strIP=m_table.getValueAt(row,col).toString();
                        	try{
                        		InetAddress.getByName(strIP);
                        	}
                        	catch(Exception ue){
                        		JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,"["+strIP+"]" + StringResource
										.getStringResource("miufohbbb00174"));
                        		return;
                        	}
                        }
                    }
                }
                Vector ipVec = new Vector();
                for(int row=0;row<m_table.getRowCount();row++){
                    if(ipVec.contains(m_table.getValueAt(row,0))){
                        JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource.getStringResource("miufohbbb00089"));
                        return;
                    }else{
                        ipVec.add(m_table.getValueAt(row,0));
                    }
                }
                if (save())
                	System.exit(0);
            }

            private boolean isInteger(String str) {
                try{
                    Integer.parseInt(str);
                    return true;
                }catch(Exception e){
                    return false;   
                }
            }       
        });
        final JButton btnCancal = new nc.ui.pub.beans.UIButton();
        btnPanel.add(btnCancal);
        btnCancal.setText(StringResource.getStringResource("miufo1000757"));
        btnCancal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }            
        });
        final JButton btnHelp = new nc.ui.pub.beans.UIButton();
        btnPanel.add(btnHelp);
        btnHelp.setText(StringResource.getStringResource("miufo1001113"));
        btnHelp.addActionListener(new ActionListener(){



            /**
			 * @i18n miufo1001113=帮助
			 */
            public void actionPerformed(ActionEvent e) {
                try{
                final String url = "http://127.0.0.1:"+System.getProperty("SERVER_PORT")+"/nc/IUFOConfigCalc_readme.txt";
                    JEditorPane pane = new UIEditorPane(url);
                    JDialog dialog = new UIDialog(ConfigRepCalcQueue.this);
                    dialog.setTitle(StringResource.getStringResource("miufo1001113"));
                    dialog.getContentPane().add(new UIScrollPane(pane));
                    dialog.setLocation(200,200);
                    dialog.setSize(500,500);
                    dialog.setVisible(true);
                } catch (MalformedURLException ee) {
AppDebug.debug(ee);//@devTools                     ee.printStackTrace();
                } catch (IOException ee) {
AppDebug.debug(ee);//@devTools                     ee.printStackTrace();
                }
            }            
        });
        //
        
        init();
    }
    
    
    /**
     *  初始化界面字体显示

     */
    private void initUI() {
        Font font = new Font("宋体", Font.PLAIN, 12);
        FontUIResource fontRes = new FontUIResource(font);
  
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
			    UIManager.put(key, fontRes);
		}
		
    }

    /**
     *  初始化界面
     * @return void
     */
    private void init() {
        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(m_radioByNode.isSelected()){
                    m_iSizePerNode.setEditable(true);
                    m_dblSizePerCPU.setEditable(false);
                    m_dblNodeSizeRate.setEditable(false);
                }else{
                    m_iSizePerNode.setEditable(false);
                    m_dblSizePerCPU.setEditable(true);
                    m_dblNodeSizeRate.setEditable(true);
                }
            }
            
        };
        
        m_radioByNode.addActionListener(listener);
        m_radioByCPU.addActionListener(listener);
        
        if(getConfigCalcVO().getDefaultType() == ConfigCalcVO.QUEUE_TYPE_BYCPU){
            m_radioByCPU.setSelected(true);
        }else{
            m_radioByNode.setSelected(true);
        }
        listener.actionPerformed(null);
                
        m_iSizePerNode.setDocument(new PlainDocument(){
        	public void insertString(int offs, String str, AttributeSet atts)
					throws BadLocationException {
				if (str == null || str.length() < 1
						|| !Character.isDigit(str.charAt(0))) {
					return;
				}
				super.insertString(offs, str, atts);
			}
        });
        
        UfoCellEditDocument document=new UfoCellEditDocument();
        document.setCellBit(5);
        m_dblSizePerCPU.setDocument(document);
        
        document=new UfoCellEditDocument();
        document.setCellBit(5);        
        m_dblNodeSizeRate.setDocument(document);
        
        m_iSizePerNode.setText(""+getConfigCalcVO().getSizePerNode());
        
        if (getConfigCalcVO().getSizePerCPU()==((int)getConfigCalcVO().getSizePerCPU()))
        	m_dblSizePerCPU.setText(""+((int)getConfigCalcVO().getSizePerCPU()));
        else
        	m_dblSizePerCPU.setText(""+getConfigCalcVO().getSizePerCPU());
        
        m_dblNodeSizeRate.setText(""+getConfigCalcVO().getNodeSizeRate());

        for(int i=0;i<COLUMN_NAMES.length;i++){
            m_table.getColumnModel().getColumn(i).setHeaderValue(COLUMN_NAMES[i]);
        }
        
        Vector items = getConfigCalcVO().getConfigItem();
        for(int i=0;items!=null&&i<items.size();i++){
            ConfigCalcItemVO itemVO = (ConfigCalcItemVO) items.get(i);
            addTableRow(itemVO.getHostAddress(),""+itemVO.getSizeByTotal(),""+itemVO.getSizeByNode());
        }
        
    }
    





    /**
	 * @i18n miufo00561=服务器各节点计算队列总长度是CPU的倍数只能在1-20之间
	 * @i18n miufo00562=各节点计算队列是服务器各节点计算队列总长度的倍数只能在0.1到1之间
	 * @i18n miufo00563=节点计算队列最大长度必须在1至20之间
	 */
    private boolean save() {
        ConfigCalcVO configCalcVO = new ConfigCalcVO();
        
        configCalcVO.setSizePerNode(Integer.parseInt(m_iSizePerNode.getText()));
        configCalcVO.setSizePerCPU(Double.parseDouble(m_dblSizePerCPU.getText()));
        configCalcVO.setNodeSizeRate(Double.parseDouble(m_dblNodeSizeRate.getText()));        
        
        if(m_radioByCPU.isSelected()){
            configCalcVO.setDefaultType(ConfigCalcVO.QUEUE_TYPE_BYCPU);
            
        	double dbSizePerCPU=configCalcVO.getSizePerCPU();
        	double dbNodeSizePerRate=configCalcVO.getNodeSizeRate();
        	
        	if (dbSizePerCPU<1 || dbSizePerCPU>20){
        		JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource.getStringResource("miufo00561"));
        		return false;
        	}
        	
        	if (dbNodeSizePerRate<0.1 || dbNodeSizePerRate>1){
        		JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource.getStringResource("miufo00562"));
        		return false;
        	}      	                      
        }else{
            configCalcVO.setDefaultType(ConfigCalcVO.QUEUE_TYPE_BYNODE); 
            
        	if (configCalcVO.getSizePerNode()<1 || configCalcVO.getSizePerNode()>20){
        		JOptionPane.showMessageDialog(ConfigRepCalcQueue.this,StringResource.getStringResource("miufo00563"));
        		return false;
        	}              
        }
        
        Vector items = new Vector();
        for(int i=0;i<m_table.getRowCount();i++){
            items.add(new ConfigCalcItemVO(
                    m_table.getValueAt(i,0).toString(),
                    Integer.parseInt(m_table.getValueAt(i,1).toString()),
                    Integer.parseInt(m_table.getValueAt(i,2).toString())
            ));
        }
        configCalcVO.setConfigItem(items);

        SysPropVO sysProp;
        try {
            sysProp = SysPropBO_Client.getSysPropsByNames(new String[]{ISysProp.CONFIG_CALC_QUEUE})[0];
            sysProp.setValue(configCalcVO.toString());
            SysPropBO_Client.updateSysProp(sysProp);
        } catch (Exception e) {
AppDebug.debug(e);//@devTools             e.printStackTrace();
        }
        return true;
    }
    
    private void addTableRow(Object o1, Object o2, Object o3){
        ((DefaultTableModel)m_table.getModel()).addRow(new Object[]{o1,o2,o3}); 
    }
    
    private ConfigCalcVO m_configCalcVO;
    private ConfigCalcVO getConfigCalcVO(){
        if(m_configCalcVO==null){
            try {
                m_configCalcVO = new ConfigCalcVO(SysPropBO_Client.getSysPropsByNames(new String[]{ISysProp.CONFIG_CALC_QUEUE})[0].getValue());
            } catch (Exception e) {
AppDebug.debug(e);//@devTools                 e.printStackTrace();
            }
            
        }
        return m_configCalcVO;
    }
}

class UfoCellEditDocument extends javax.swing.text.PlainDocument {
	private static final long serialVersionUID = 867644516998570678L;
	// 接受编辑单元为数值类型的位数
	int m_nCellBit = DefaultSetting.DEFAULT_DECIMALDIGITS;
	public UfoCellEditDocument() {
		super();
	}
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		if (str == null)
			return;
		String strOld = getText(0, getLength());
		String strNew = strOld.substring(0, offs) + str
				+ strOld.substring(offs);
		if (strNew.length() == 0) {
			super.insertString(offs, str, a);
			return;
		} else if ((strNew.length() == 1 && strNew.charAt(0) == '-')
				|| (strNew.length() == 1 && strNew.charAt(0) == '+')) {
			super.insertString(offs, str, a);
			return;
		}
		try {
			Double.parseDouble(strNew);
			int nLen, nFind;
			nLen = strNew.length();
			nFind = strNew.indexOf(".");
			if (nFind > 0) {
				if (nLen - nFind - 1 > m_nCellBit) {
					return;
				}
			}
			if (nLen > 0) {
				char ch = strNew.charAt(nLen - 1);
				if (ch == 'd' || ch == 'D' || ch == 'f' || ch == 'F') {
					return;
				}
			}
			super.insertString(offs, str, a);

		} catch (NumberFormatException e) {
			// Toolkit.getDefaultToolkit().beep();
		}

	}
	public void setCellBit(int nBit) {
		m_nCellBit = nBit;
	}

}

    