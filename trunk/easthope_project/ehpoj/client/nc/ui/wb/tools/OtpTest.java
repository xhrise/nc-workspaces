package nc.ui.wb.tools;

import java.util.Scanner;

public class OtpTest {
	int  iRe				= 0;
	nc.ui.wb.tools.JOTPString jstrVersion	= new nc.ui.wb.tools.JOTPString();
	String strLicFile 		= new String("OTPauth.lic");
	String strDbIniFile 	= new String("otpcore.ini");
	String strTnkFile 		= new String("customer.tnk");
	int iDbType				= JOTPServer.OTPS_DB_TYPE_SQLSERVER;
	int iDbConnType 		= JOTPServer.OTPS_DB_CONN_TYPE_ODBC;
	String strDbServer 		= new String("127.0.0.1");
	int iDbPort				= 1433;
	String strDbName		= new String("otpserver_db");
	String strDbPrefix		= new String("ftotp_");
	String strDbUser 		= new String("sa");
	String strDbPasswd		= new String("1");
	String strDbDriver		= "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	String strDbConnStr		= null;
	
	nc.ui.wb.tools.JOTPString jstrErrorMsg	= new nc.ui.wb.tools.JOTPString();
	
	String strUser			= new String("demo");
	String strToken			= new String("1811304559328");
	String strPin			= new String("123456");
	
	JOTPint jiRetry			= new JOTPint();
	int iMaxRetry			= 4;
			
	JOTPint	jiLoginLock		= new JOTPint();
	
	/* importing tokens counter */
	JOTPint jiSuccess		= new JOTPint();
	JOTPint jiFailed		= new JOTPint();
		
	
	/* Get an instance of JOTPServer */
	nc.ui.wb.tools.JOTPServer instance = new nc.ui.wb.tools.JOTPServer();	
	
	public static void main(String[] args) {
//		OtpTest otp = new OtpTest();
//		otp.init();
		String a = "H0001";
		System.out.println(a.substring(0,1));
	}
	
	//初始化
	@SuppressWarnings("static-access")
	public void init(){
		iRe = instance.otp_server_init(strLicFile, strDbIniFile, 0);
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			System.out.format("Initialization failure! And the error code is: 0x%X.\n", iRe);
			return;
		}
		else
		{
			System.out.println("Initialization OK!");
		}
	}
	
	//获得版本
	@SuppressWarnings("static-access")
	public void getVersion(){
		iRe = instance.otp_server_get_version(jstrVersion);
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			System.out.format("Get version failure, and the error code is: 0x%X.\n", iRe);

			instance.otp_server_term();
			return;
		}
		else
		{
			System.out.format("Get version successful, and the version is: %s\n", jstrVersion.str);
		}
	}
	
	//设置数据库连接
	@SuppressWarnings("static-access")
	public void setConfig(){
		iRe = instance.otp_server_set_config(iDbType, iDbConnType, 
											strDbServer, iDbPort, 
											strDbName, strDbPrefix, 
											strDbUser, strDbPasswd, 
											strDbDriver, strDbConnStr);
		
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			System.out.format("Config database failed! And the error code is: 0x%X.\n", iRe);
			
			instance.otp_server_term();
			return;				
		}
		else
		{
			System.out.println("Config database successfully!");
		}
	}
	
	//测试数据库连接
	@SuppressWarnings("static-access")
	public void testContion(){
		iRe = instance.otp_server_connect_test(jstrErrorMsg);
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			System.out.format("Connect test failure! And the error code is: 0x%X\n\t error message is: %s.\n",
							 iRe, jstrErrorMsg.str);

			instance.otp_server_term();
			return;
		}
		else
		{
			System.out.println("Connect test OK!");
		}	
	}
	
	//将令牌信息导入数据库
	@SuppressWarnings("static-access")
	public void importtokens(){
		iRe = instance.otp_server_import_tokens(strTnkFile, strLicFile, jiSuccess, jiFailed);
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			System.out.format("Import tokens failed! And the error code is: 0x%X.\n", iRe);
			instance.otp_server_term();
			return;				
		}
		else
		{
			/* note, if your DB have these tokens, bellow codes will show you 
			 * 'Importing 0 tokens!'
			 * that's OK.
			 */			
			System.out.format("Import %s tokens.\n", jiSuccess.i);
		}		
	}
	
	//添加用户
	@SuppressWarnings("static-access")
	public void addUser(){
		iRe = instance.otp_server_add_user(strUser, 1, strPin);
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			if (iRe == JOTPError.OTPS_USER_EXISTS )
				System.out.println("Adding user failed for it existing, ignoring this error.\n");
			else			
			{
				System.out.format("Add user failed! And the error code is: 0x%X.\n", iRe);
				
				instance.otp_server_term();
				return;				
			}
		}
		else
		{
			System.out.println("Add user OK!");
		}	
	}
	
	//为指定的用户绑定令牌
	@SuppressWarnings("static-access")
	public void bindServer() {
		iRe = instance.otp_server_bind(strUser, strToken);
		if (iRe != JOTPError.OTPS_SUCCESS)
		{
			if (iRe == JOTPError.OTPS_TOKEN_HAS_BINDED)
				System.out.format("This token has binded to this user. Skipping this error.\n");
			else
			{
				System.out.format("Bind user and token failed! And the error code is: 0x%X.\n", iRe);
			
				instance.otp_server_term();
				return;				
			}
		}
		else
		{
			System.out.println("Bind user and token successful!");
		}		
	}
	
	public void otpTest(){
		while(true){
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please input your 6 digits One-Time Password:");
			String strOtp  = scanner.nextLine();
	
			//用于对指定的用户、动态口令和PIN进行身份认证
			iRe = instance.otp_server_auth(strUser, strOtp, strPin, jiRetry, iMaxRetry);
			if (iRe == JOTPError.OTPS_NEED_SYNC)
			{// Need synchronization
				System.out.println("Authentication failed and the OTP synchronization required!");
				System.out.println("Please input your first 6 digits One-Time Password:");
				String strOtp1 = scanner.nextLine();
				
				System.out.println("Please input your next 6 digits One-Time Password:");
				String strOtp2 = scanner.nextLine();		
	
				//对指定的用户进行令牌同步
				iRe = instance.otp_server_sync(strUser, strOtp1, strOtp2);
				if (iRe != JOTPError.OTPS_SUCCESS)
				{
					System.out.format("Synchronization failure! And the error code is: 0x%X.\n", iRe);
					break;
				}
				else
				{
					System.out.println("Synchronization successful!");
					continue;
				}
			}
			else if (iRe !=JOTPError.OTPS_SUCCESS)
			{
				System.out.format("Authentication failure! And the error code is: 0x%X.\n", iRe);
				//用于获取指定的令牌的锁定状态，处于锁定状态或者解锁状态
				iRe = instance.otp_server_get_token_loginlock(strToken, jiLoginLock);
				if (iRe == JOTPError.OTPS_SUCCESS)
				{
					if (jiLoginLock.i == 0)
					{
						System.out.println("Get login locked status successful, and the token status is OK!");
					}
					else
					{
						System.out.println("Get login locked status successful, and the token has been locked!");
					}
				}
				else
				{
					System.out.format("Get login locked status failure,and the error code is: 0x%X.\n", iRe);
				}
				break;
			}
			else
			{
				System.out.println("Authentication successful!");
				break;
			}
		}
	}
	
	//结束认证服务器SDK的反初始化
	@SuppressWarnings("static-access")
	public void treamOtp(){
		instance.otp_server_term();
	}
}
