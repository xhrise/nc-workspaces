/*
 * Created on 2004-3-19
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.ufsoft.iufo.jiuqi.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.ufsoft.iufo.jiuqi.pub.GetTaskRequest;
import com.ufsoft.iufo.jiuqi.pub.LoginInfo;
import com.ufsoft.iufo.jiuqi.pub.LoginRequest;
import com.ufsoft.iufo.jiuqi.pub.LoginResponse;
import com.ufsoft.iufo.jiuqi.pub.StatusCodeConstants;
import com.ufsoft.report.util.MultiLang;

/**
 * <p>Title: 久其接口客户端用户登录界面类</p>
 * <p>Description: 完成服务器地址指定与登录信息录入功能</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */
public class LoginDlg extends JFrame implements ActionListener {

    /**
	 * @i18n uiuforep00149=单位编码:
	 */
    private final static String LBL_UNIT = MultiLang.getString("uiuforep00149");
    /**
	 * @i18n uiuforep00150=用户编码:
	 */
    private final static String LBL_USER = MultiLang.getString("uiuforep00150");
    /**
	 * @i18n uiuforep00151=用户口令:
	 */
    private final static String LBL_PASSWORD = MultiLang.getString("uiuforep00151");
    /**
	 * @i18n ok=确定
	 */
    private final static String BTN_OK = MultiLang.getString("ok");
    /**
	 * @i18n cancel=取消
	 */
    private final static String BTN_CANCEL = MultiLang.getString("cancel");
    private static final String strFilename = "ServerSet.config";
    private static final String configFileName = "ServerContext.config";
    /**
	 * @i18n uiuforep00152=服务器地址:
	 */
    private final static String LBL_SERVER_ADDRESS = MultiLang.getString("uiuforep00152");
    /**
	 * @i18n uiuforep00153=端口:
	 */
    private final static String LBL_SERVER_PORT = MultiLang.getString("uiuforep00153");
    private final static String CONFIG_CONTEXT = "server_context";
    private final static String CONFIG_SERVLET = "servlet";
    private File workingFilePath = null;
    private String m_strParaFileName = null;//程序被调用时传递的参数文件名
    private ParaFileInfo m_paraFile = null;
    private String strServerAddress;
    private String strServerPort;

    private JLabel lblServerAddress = null;
    private JLabel lblServerPort = null;
    private JTextField txtServerAddress = null;
    private JTextField txtServerPort = null;
    //录入组件的面板
    private JPanel jPInputPanel = null;
    //按钮面板
    private JPanel jPButtonPanel = null;
    private JLabel lblUnit = null;
    private JLabel lblUser = null;
    private JLabel lblPassword = null;
    private JTextField txtUnit = null;
    private JTextField txtUser = null;
    private JPasswordField txtPassword = null;
    private JButton btnOK = null;
    private JButton btnCancel = null;
    private JPanel jInputContentPane = new JPanel();
    
    private ServerConfig serverConfig = new ServerConfig();
    
//    private Font titleFont = new Font("宋体",0,15);
//    private Font lblFont = new Font("宋体",0,13);
//    private Font btnFont = new Font("宋体",0,13);
//    private Font tblFont = new Font("宋体",0,13);


    public LoginDlg(String strParaFileName) throws Exception{
        super();
        m_strParaFileName = strParaFileName;
        Properties props = System.getProperties();
        String java_home = props.getProperty("java.home");
        File javaHomePath = new File(java_home);
        workingFilePath = javaHomePath.getParentFile();
        parseParaFile();
        init();
    }
    /**
	 * @i18n uiuforep00154=程序调用错误，请增加文件名参数
	 * @i18n uiuforep00155=程序调用错误，文件名参数不能为空
	 * @i18n uiuforep00156=程序开始执行，久其系统传入参数文件：
	 */
    public static void main(String[] args) throws Exception{
    	
        Properties props = System.getProperties();
        String java_home = props.getProperty("java.home");
        File javaHomePath = new File(java_home);
        File workingFilePath = javaHomePath.getParentFile();
        
        Log.getInstance().setWorkingFilePath(workingFilePath);
        
        if(args.length ==0){
        	Log.getInstance().log(MultiLang.getString("uiuforep00154"));
            throw new Exception(MultiLang.getString("uiuforep00154"));            
        }
        String strParaFileName = args[0];
        if(strParaFileName == null || strParaFileName.trim().equals("")){
        	Log.getInstance().log(MultiLang.getString("uiuforep00155"));
            throw new Exception(MultiLang.getString("uiuforep00155"));
        }

        Log.getInstance().log(MultiLang.getString("uiuforep00156") +strParaFileName);
        
        LoginDlg frame = new LoginDlg(strParaFileName);
    }

    /**
     * 解析参数文件的内容
     * @throws Exception
     */
    private void parseParaFile() throws Exception{
        ParaFileParser parser = new ParaFileParser(m_strParaFileName);
        m_paraFile = parser.parse();
    }

    /**
	 * @i18n uiuforep00157=登录IUFO
	 */
    private void init()
    {
        try {
              UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

//              UIManager.put("TabbedPane.font", titleFont);
//              UIManager.put("TitledBorder.font", titleFont);
//              UIManager.put("Component.font", titleFont);
////              UIManager.put("Button.font", btnFont);
////              UIManager.put("Label.font", lblFont);
//              UIManager.put("ComboBox.font", titleFont);
////              UIManager.put("Table.font", tblFont);
//              UIManager.put("List.font", titleFont);
//              UIManager.put("ToolTip.font", titleFont);
//              UIManager.put("TableHeader.font", titleFont);
//              UIManager.put("Frame.font", titleFont);
//              UIManager.put("Panel.font", titleFont);
//              UIManager.put("ScrollPane.font", titleFont);
//              UIManager.put("OptionPane.font", titleFont);
////              UIManager.put("Lable.font", lblFont);
//              UIManager.put("ContentModel.font", titleFont);
//              UIManager.put("FileChooser.font", titleFont);
//              UIManager.put("TextField.font", tblFont);
            }
            catch (Exception exc) {
              Log.getInstance().log("Error loading L&F: " + exc);
            }
        setTitle(MultiLang.getString("uiuforep00157"));


        try{
            readSet();
        }catch(Exception e){
        }

        //设置字体，防止出现乱码
//        setFont(titleFont);
        lblUnit = new JLabel(LBL_UNIT);
//        lblUnit.setFont(lblFont);
        lblUser = new JLabel(LBL_USER);
//        lblUser.setFont(lblFont);
        lblPassword = new JLabel(LBL_PASSWORD);
//        lblPassword.setFont(lblFont);
        lblServerAddress = new JLabel(LBL_SERVER_ADDRESS);
//        lblServerAddress.setFont(lblFont);
        lblServerPort = new JLabel(LBL_SERVER_PORT);
//        lblServerPort.setFont(lblFont);
        String address = "http://";
        if(strServerAddress != null)
            address = strServerAddress;
        txtServerAddress = new JTextField(address);
        txtServerPort = new JTextField("");
        if(strServerPort != null)
        {
            txtServerPort.setText(strServerPort);
        }

        txtUnit = new JTextField("");
        txtUser = new JTextField("");
        txtPassword = new JPasswordField("");
        btnOK = new JButton(BTN_OK);
//        btnOK.setFont(btnFont);
        btnCancel = new JButton(BTN_CANCEL);
//        btnCancel.setFont(btnFont);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        getContentPane().add(getInputPanel(), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        getContentPane().add(new JLabel(""),gbc);
        gbc.weightx = 0;
        getContentPane().add(Box.createVerticalStrut(13),gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        getContentPane().add(getJButtonPanel(), gbc);
        int nWidth = 400;
        int nHeight = 300;
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenwidth = screenSize.width;
        int screenheight = screenSize.height;
        setSize(nWidth,nHeight);
        setLocation((screenwidth-nWidth)/2,(screenheight-nHeight)/2);
        setVisible(true);

    }
    private JPanel getJButtonPanel()
    {
        if(jPButtonPanel == null)
        {
            jPButtonPanel = new JPanel();
            jPButtonPanel.setLayout(new GridBagLayout()); //(6,4));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            jPButtonPanel.add(btnOK,gbc);
            gbc.weightx = 1.0;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.EAST;
            jPButtonPanel.add(btnCancel,gbc);
            btnOK.addActionListener(this);
            btnCancel.addActionListener(this);
        }
        return jPButtonPanel;
    }
    /**
	 * @i18n uiuforep00158=登录参数设置
	 */
    private JPanel getInputPanel()
    {
        if(jPInputPanel == null)
        {
            jPInputPanel = new JPanel();
//            Font font = new Font("宋体",0,15);
//            jPInputPanel.setFont(font);
            jPInputPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.NORTHWEST;

            jPInputPanel.add(lblServerAddress,gbc);
            jPInputPanel.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            txtServerAddress.setPreferredSize(new Dimension(180,25));
            jPInputPanel.add(txtServerAddress,gbc);
            gbc.weightx = 0;
            jPInputPanel.add(Box.createVerticalStrut(3),gbc);

            gbc.gridwidth = 1;
            jPInputPanel.add(lblServerPort,gbc);
            jPInputPanel.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 2.0;
            txtServerPort.setPreferredSize(new Dimension(180,25));
            jPInputPanel.add(txtServerPort,gbc);
            gbc.weightx = 0;
            jPInputPanel.add(Box.createVerticalStrut(13),gbc);

            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            jPInputPanel.add(lblUnit,gbc);
            jPInputPanel.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            txtUnit.setPreferredSize(new Dimension(180,25));
            jPInputPanel.add(txtUnit,gbc);
            gbc.weightx = 0;
            jPInputPanel.add(Box.createVerticalStrut(3),gbc);

            gbc.gridwidth = 1;
            jPInputPanel.add(lblUser,gbc);
            jPInputPanel.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            txtUser.setPreferredSize(new Dimension(180,25));
            jPInputPanel.add(txtUser,gbc);
            gbc.weightx = 0;
            jPInputPanel.add(Box.createVerticalStrut(3),gbc);

            gbc.gridwidth = 1;
            jPInputPanel.add(lblPassword,gbc);
            jPInputPanel.add(Box.createHorizontalStrut(10),gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            txtPassword.setPreferredSize(new Dimension(180,25));
            jPInputPanel.add(txtPassword,gbc);
            gbc.weightx = 0;
            jPInputPanel.add(Box.createVerticalStrut(3),gbc);
            Border etched = BorderFactory.createEtchedBorder();
            Border title = BorderFactory.createTitledBorder(etched,MultiLang.getString("uiuforep00158"));
            jPInputPanel.setBorder(title);

            jInputContentPane.setLayout(new BorderLayout());
            jInputContentPane.add(jPInputPanel,BorderLayout.CENTER);

        }
        return jInputContentPane;
    }
    /**
	 * @i18n uiuforep00079=任务关键字设置
	 */
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equalsIgnoreCase(BTN_CANCEL)){
            this.dispose();
        } else if(e.getActionCommand().equalsIgnoreCase(BTN_OK)){
            try
            {
                //验证输入合法性，包括登陆到服务器！
                if(checkInput()){
                    //保存端口设置
                    writeSet();
                    //转到下一个界面--任务关键字设置界面
                    GetTaskRequest request =
                        new GetTaskRequest(new LoginInfo(txtUnit.getText(), txtUser.getText(), txtPassword.getText()));
                    try{
                        TaskInfoInputDlg taskInfoInputDlg = new TaskInfoInputDlg(this, MultiLang.getString("uiuforep00079"), true, request);
                        this.setVisible(false);
                        taskInfoInputDlg.setLocationRelativeTo(this);
                        taskInfoInputDlg.setVisible(true);
                        taskInfoInputDlg.pack();
                        this.setVisible(true);
                    }catch(Exception ex){
                        throw ex;
                    }
                }
            }catch(Exception ex){
            	Log.getInstance().log(ex.getMessage());
                showErrDialog(ex);
            }
        }
    }

    public ParaFileInfo getParaFileInfo(){
        return m_paraFile;
    }

    /**
	 * @i18n uiuforep00093=错误信息
	 */
    private void showErrDialog(Exception ex){
        JOptionPane.showMessageDialog(
            this,
            ex.getMessage(),
            MultiLang.getString("uiuforep00093"), JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * 读取服务器地址设置
     * @author syang
     *
     * To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Generation - Code and Comments
     * @i18n uiuforep00159=读取配置文件:
     */
    private void readSet() throws Exception{
        File file = null;
        FileReader in = null;
        try{
            file = new File(workingFilePath, strFilename);
            if(file.exists()){
                in = new FileReader(file);
                BufferedReader reader = new BufferedReader(in);
                String strLine1 = reader.readLine();
                String strLine2 = reader.readLine();
                strServerAddress = strLine1;
                strServerPort = strLine2;
            }
        }finally{
            if(in!=null){
                try{
                    in.close();
                }catch(IOException e){
                }
            }
        }
        
        try{
        	file = new File(workingFilePath, configFileName);
        	if(file.exists()){
        		in = new FileReader(file);
        		Log.getInstance().log(MultiLang.getString("uiuforep00159") + configFileName);
                BufferedReader reader = new BufferedReader(in);
                String strLine1 = reader.readLine();
                String strLine2 = reader.readLine();
                String[] configValue = parseConfigParam(strLine1);
                if(configValue != null && configValue.length>0){
                	String param = configValue[0];
                	String value = configValue[1];
                	if(param.equals(CONFIG_CONTEXT) && !"".equals(value)){
                		serverConfig.setServerContext(value);
                	}
                }
                configValue = parseConfigParam(strLine2);
                if(configValue != null && configValue.length>0){
                	String param = configValue[0];
                	String value = configValue[1];
                	if(param.equals(CONFIG_SERVLET) && !"".equals(value)){
                		serverConfig.setServletName(value);
                	}
                }
        	}
        }catch(Exception ignore){        	
        }finally{
        	if(in != null){
        		try{
        			in.close();
        		}catch(IOException e){        			
        		}
        	}
        }
    }
	/**
	 * parseConfigParam
	 * LoginDlg
	 * void
	 * @param strLine
	 */
	private String[] parseConfigParam(String strLine) {
		if(strLine != null){
			int pos = strLine.indexOf('=');
			if(pos >=0){
				String param = strLine.substring(0, pos);
				String value = strLine.substring(pos+1);
				if(param != null){
					param = param.trim();
				}
				if(value != null){
					value = value.trim();
				}else{
					value = "";
				}
				return new String[]{param, value};
			}
			return null;
		}
		return null;
	}

    /**
     * 保存服务器地址设置到文件中
     * @author syang
     * To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Generation - Code and Comments
     */
    private void writeSet() throws Exception{
        File file = null;
        FileWriter out = null;
        try{
            file = new File(workingFilePath, strFilename);
            out = new FileWriter(file);
            PrintWriter writer = new PrintWriter(out);
            writer.println(strServerAddress);
            writer.println(strServerPort);
        }finally{
            if(out!=null){
                try{
                    out.close();
                }catch(IOException ex){

                }
            }
        }
    }

    /**
     * 验证输入合法性，包括登录到服务器！
     * @throws Exception
     * @return boolean
     */
    private boolean checkInput() throws Exception{
        //得到登陆界面的录入值
        String serverAddress = txtServerAddress.getText();
        String serverPort = txtServerPort.getText();
        String strLoginUnit = txtUnit.getText();
        String strLoginUser = txtUser.getText();
        String strLoginPassword = txtPassword.getText();
        try
        {
            //校验输入框输入制的正确性
            checkInputText(serverAddress,serverPort,strLoginUnit,strLoginUser);
            //构造服务器地址
            StringBuffer sbAddress = new StringBuffer();
            sbAddress.append(strServerAddress);
            if(strServerPort != null
               && !strServerPort.trim().equalsIgnoreCase("")){
                sbAddress.append(":");
                sbAddress.append(strServerPort);
            }
            sbAddress.append(serverConfig.toString());
            //校验登录设置
            checkLogin(strLoginUnit, strLoginUser, strLoginPassword,sbAddress.toString());
            return true;
        }catch(Exception e)
        {
            throw e;
        }
    }
    /**
     * 校验输入框输入制的正确性
     * @param serverAddress String
     * @param serverPort String
     * @param strLoginUnit String
     * @param strLoginUser String
     * @throws Exception
     * @i18n uiuforep00160=服务器地址不能为空
     * @i18n uiuforep00161=端口输入不合法
     * @i18n uiuforep00162=登录单位不能为空
     * @i18n uiuforep00163=登录用户不能为空
     * @i18n uiuforep00081=服务器地址未设置
     */
    private void checkInputText(String serverAddress,
                                String serverPort,
                                String strLoginUnit,
                                String strLoginUser) throws Exception
    {
        //对服务器地址和端口进行校验
        if(serverAddress == null || serverAddress.trim().equalsIgnoreCase("")){
            throw new Exception(MultiLang.getString("uiuforep00160"));
        }
        serverAddress = serverAddress.trim();
        if(serverPort!=null){
            serverPort = serverPort.trim();
            if(!serverPort.equalsIgnoreCase("")){
                try{
                    Integer.parseInt(serverPort);
                }catch(Exception e){
                    throw new Exception(MultiLang.getString("uiuforep00161"));
                }
            }
        }
        strServerAddress = serverAddress;
        strServerPort = serverPort;
        if(strLoginUnit == null || strLoginUnit.trim().equalsIgnoreCase("")){
            throw new Exception(MultiLang.getString("uiuforep00162"));
        }
        if(strLoginUser == null || strLoginUser.trim().equalsIgnoreCase("")){
            throw new Exception(MultiLang.getString("uiuforep00163"));
        }
        if(strServerAddress == null
           || strServerAddress.equalsIgnoreCase("")){
            throw new Exception(MultiLang.getString("uiuforep00081"));
        }

    }
    /**
     * 检查登陆，主要校验登陆的单位、用户和密码，已经登陆的服务器地址等是否正确
     * @param strLoginUnit String
     * @param strLoginUser String
     * @param strLoginPassword String
     * @param strAddress String
     * @throws Exception
     * @i18n uiuforep00164=登录失败
     */
    private void checkLogin(String strLoginUnit, String strLoginUser, String strLoginPassword,String strAddress) throws Exception
    {
        LoginRequest request =
            new LoginRequest(new LoginInfo(strLoginUnit, strLoginUser, strLoginPassword));
        HttpRequestOperator operator = new HttpRequestOperator(strAddress.toString());
        LoginResponse response = (LoginResponse)operator.sendRequest(request);
        if(response != null){
            if(response
               .getStatusCode()
               .equalsIgnoreCase(StatusCodeConstants.LOGIN_SUCCESS)){
            } else if(
                response.getStatusCode().equalsIgnoreCase(
                StatusCodeConstants.LOGIN_FAILURE)){
                throw new Exception(MultiLang.getString("uiuforep00164"));
            }
        }else
        {
            throw new Exception(MultiLang.getString("uiuforep00164"));
        }
    }
    /**
     * @return Returns the strServerAddress.
     */
    public String getServerAddress() {
        return strServerAddress;
    }
    /**
     * @return Returns the strServerPort.
     */
    public String getServerPort() {
        return strServerPort;
    }
    public void close()
    {
        this.dispose();
        Log.getInstance().close();
        System.exit(0);
    }
	public ServerConfig getServerConfig() {
		return serverConfig;
	}
}
 