/* =============================================================================
 * JOTPServer.java - OTP server library.
 *  
 * OTP c100 | OTP c200
 * Copyright (C) 2007-2009 Feitian Technologies Co., Ltd
 * http://www.FTsafe.com
 *
 * This is a JNI interface 
 *
 * Created on 2008-09-23, Last version: 2.0
 *
 * Version:2.0
 *  
 */

 
package nc.ui.wb.tools;			/* package name */

public   class   JOTPServer
{  
	public JOTPServer()
	{
	}
	
	static
	{
	  try
	  {
	    System.loadLibrary("jotpserver");     /* dynamic load */
	  }
	  catch(UnsatisfiedLinkError e)
	  {
	    System.err.println( "Cannot load jotpserver library:\n " + e.toString());
	  }
	}
	
	/*
	 * Database type 
	 */
	public final static int OTPS_DB_TYPE_ACCESS			= 0;		/* Access */
	public final static int OTPS_DB_TYPE_MSDE 			= 1;		/* MSDE */
	public final static int OTPS_DB_TYPE_SQLSERVER		= 2;		/* SQL Server */
	public final static int OTPS_DB_TYPE_MYSQL			= 3;		/* MySQL */
	public final static int OTPS_DB_TYPE_ORACLE			= 4;		/* Oracle */
	public final static int OTPS_DB_TYPE_POSTGRESQL 	= 5;		/* PostgreSQL */
	
	
	/* 
	 * Database connection type 
	 */
	public final static int OTPS_DB_CONN_TYPE_ODBC		= 0;
	
	
	/*
	 * Authentication Host priority
	 */
	public final static int OTPS_HOST_PRIORITY_LOW		= 0;
	public final static int OTPS_HOST_PRIORITY_NORMAL	= 1;
	public final static int OTPS_HOST_PRIORITY_HIGH		= 2;
	
	
	/*
	 * User source type
	 */
	public final static int USER_FROM_DEFAULT_DB		= 0;		/* from the default database */
	public final static int USER_FROM_OTHER_DB			= 1;		/* from other table */
	public final static int USER_FROM_LDAP				= 2;		/* from LDAP server */
	
	
	/*
	 * LDAP Type
	 */	
	public final static int LDAP_TYPE_MSAD				= 0;		/* Microsoft Windows Active Directory */
	public final static int LDAP_TYPE_OPENLDAP			= 1;		/* OpenLDAP */
	public final static int LDAP_TYPE_OTHER				= 2;		/* types supported in the future */
	
	
	/* =============================================================================
	 * Function   : otp_server_init 
	 * Description: OTP server library initialization, should be called 
	 *				at the beginning of a program.
	 * Parameter  : 
	 *				strLicFile			the license file
	 *				strDbIniFile		the OTP server database configuration file
	 *				iReserve			reserved,must be 0
	 * return     : 0 - success, others for error.
	 */
	public static native int otp_server_init (String strLicFile, String strDbIniFile, 
											int iReserve);

	/* =============================================================================
	 * Function   : otp_server_term 
	 * Description: OTP server library uninitialization, should be called 
	 *				at the end of a program.
	 * Parameter  : n/a
	 * return     : 0 - success, others for error.
	 */
	public static native int otp_server_term ();
	
	/* =============================================================================
	 * Function   : otp_server_get_license 
	 * Description: OTP server library initialization, should be called 
	 *				at the beginning of a program.
	 * Parameter  : 
	 *				strLicFile		the license file, if set it is NULL, 
	 *								use the default license file of initialize.
	 *				jIssuer			issuer of this license
	 *				jOwner			owner of this license
	 *				jiStartSec		start time of this license,
	 *								the time is second relative to 1970-01-01 00:00:00
	 *				jiExpireSec		expire time of this license
	 *				jiMaxToken		the maximal token count of can used under this license.
	 *				jDescp			the description of this license
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_license (String strLicFile,
								JOTPString jIssuer,	JOTPString jOwner,
								JOTPint jiStartSec, JOTPint jiExpireSec, 
								JOTPint jiMaxToken,
								JOTPString jDescp);
	
	/* =============================================================================
	 * Function   : otp_server_get_version 
	 * Description: Get the OTP server API version information.
	 * Parameter  : 
	 *				jVersion		the version information of OTP server API
	 * return     : 0 - success, others for error.
	 */
	public static native int otp_server_get_version (JOTPString jVersion);
	
	/* =============================================================================
	 * Function   : otp_server_set_config 
	 * Description: Configure the database connect information of OTP server
	 * Parameter  : 
	 *				iDbType				the type of database
	 *				iDbConnType			the connection type
	 *				strDbServer			the database server's name or IP
	 *				iDbPort				the database server's port of listening
	 *				strDbName			the database name
	 *				strDbPrefix			the database table prefix
	 *				strDbUser			the user name to connect database
	 *				strDbPasswd			the password of db_user
	 *				strDbDriver			the ODBC driver name
	 *				strDbConnStr		the connection string,default is NULL
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_config (int iDbType, int iDbConnType,
							   String strDbServer, int iDbPort,
							   String strDbName, String strDbPrefix,
							   String strDbUser, String strDbPasswd,
							   String strDbDriver,String strDbConnStr);
	
	/* =============================================================================
	 * Function   : otp_server_reload_config 
	 * Description: Reload the OTP server's configuration
	 * Parameter  : n/a
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_reload_config ();
	
	/* =============================================================================
	 * Function   : otp_server_connect_test 
	 * Description: Test OTP server's connection of database
	 * Parameter  : 
	 *				jErrorMsg		the message of error if connect failure
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_connect_test (JOTPString jErrorMsg);
	
	/* =============================================================================
	 * Function   : otp_server_import_tokens 
	 * Description: Import token from token file to the database
	 * Parameter  : 
	 *				strTnkFile		the token file that saved the token
	 *				strLicFilel		the license file,used to decrypt the token file,
	 *								if it set to NULL, will use the default license.
 	 *              i_success		the count of succeed importing.
 	 *              i_failed		the count of failure importing.	 
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_import_tokens (String strTnkFile, String strLicFilel,
												JOTPint jiSuccess, JOTPint jiFailed);
	
	/* =============================================================================
	 * Function   : otp_server_auth 
	 * Description: OTP server authentication interface
	 * Parameter  : 
	 *				strUser			user name
	 *				strOtp				the one-time password
	 *				strPin				PIN of this user
	 *				jiRetry			retry count remained
	 *				iMaxRetry		maximum retry count allowed	 
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_auth (String strUser, String strOtp, String strPin,
										JOTPint jiRetry, int iMaxRetry);
	
	/* =============================================================================
	 * Function   : otp_server_auth_ex 
	 * Description: extend authentication interface
	 * Parameter  : 
	 *				strUser			user name
	 *				strToken		token's bar code
	 *				strOtp			the one-time password
	 *				strPin			PIN of this user
	 *				jiRetry			retry count remained
	 *				iMaxRetry		maximum retry count allowed	 
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_auth_ex (String strUser, String strToken, 
							String strOtp, String strPin,
							JOTPint jiRetry, int iMaxRetry);
	
	/* =============================================================================
	 * Function   : otp_server_sync 
	 * Description: OTP server synchronization interface
	 * Parameter  : 
	 *				strUser			user name
	 *				strOtp1			the first OTP
	 *				strOtp2			the next OTP
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_sync (String strUser, String strOtp1, String strOtp2);
	

	/* =============================================================================
	 * Function   : otp_server_sync_ex 
	 * Description: extend synchronization interface
	 * Parameter  : 
	 *				strUser			user name
	 *				strToken		token's bar code
	 *				strOtp1			the first OTP
	 *				strOtp2			the next OTP
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_sync_ex (String strUser, String strToken, 
							String strOtp1, String strOtp2);
	

	/* =============================================================================
	 * Function   : otp_server_add_user 
	 * Description: Add a user to database
	 * Parameter  : 
	 *				strUser			user name
	 *				iNeedpin		the user is need pin or not
	 *				strPin			user's PIN if the needpin is set to need
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_add_user (String strUser, int iNeedpin, String strPin);
	

	/* =============================================================================
	 * Function   : otp_server_del_user 
	 * Description: Delete a user from database
	 * Parameter  : 
	 *				strUser			the user name want to be deleted
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_del_user (String strUser);
	

	/* =============================================================================
	 * Function   : OTP_Server_User_Exist 
	 * Description: Look up a user exists or not
	 * Parameter  : 
	 *				strUser			the user name want to look up
	 *				jiExsit			if success, 0-not exsit, 1-exsit
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_user_exist (String strUser, JOTPint jiExsit);
	

	/* =============================================================================
	 * Function   : otp_server_get_tokens 
	 * Description: Get tokens of a user assigned
	 * Parameter  : 
	 *				strUser			user name
	 *				jiTokenCount	the token count that this user has assigned
	 *				jTokens			the buffer used to save the assigned tokens,
	 *								separated by ':'.
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_tokens (String strUser, JOTPint jiTokenCount, 
											JOTPString jTokens);
		
	
	/* =============================================================================
	 * Function   : otp_server_get_needpin 
	 * Description: check for the login method of PIN requirement of a user
	 * Parameter  : 
	 *				strUser			user name
	 *				jiNeedpin		the status to get
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_needpin (String strUser, JOTPint jiNeedpin);
	
	
	/* =============================================================================
	 * Function   : otp_server_set_needpin 
	 * Description: set the login method to 'PIN required' or not.
	 * Parameter  : 
	 *				strUser			user name
	 *				iNeedpin		the status to set
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_needpin (String strUser, int iNeedpin);
	
	/* =============================================================================
	 * Function   : otp_server_set_pin 
	 * Description: 
	 * Parameter  : 
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_pin (String strUser, String strPin);
	

	/* =============================================================================
	 * Function   : otp_server_del_token 
	 * Description: delete a token from the OTP system
	 * Parameter  : 
	 *				strToken		the token ID
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_del_token (String strToken);
	

	/* =============================================================================
	 * Function   : otp_server_token_exsit 
	 * Description: check the existence of a token
	 * Parameter  : 
	 *				strToken		the token ID
	 *				jiExsit			the status of existence
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_token_exsit (String strToken, JOTPint jiExsit);
	

	/* =============================================================================
	 * Function   : otp_server_get_token_lost 
	 * Description: check the locking status of a token
	 * Parameter  : 
	 *				strToken			the token ID
	 *				jiLocked			the status
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_token_lost (String strToken, JOTPint jiLocked);
	

	/* =============================================================================
	 * Function   : otp_server_set_token_lost 
	 * Description: block / unblock a token
	 * Parameter  : 
	 *				strToken		the token ID
	 *				iLocked			the status to set
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_token_lost (String strToken, int iLocked);
	

	/* =============================================================================
	 * Function   : otp_server_get_token_loginlock 
	 * Description: check the status about a token which can be used to login or not.
	 * Parameter  : 
	 *				strToken		the token ID
	 *				jiLogin			the status
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_token_loginlock (String strToken, JOTPint jiLogin);
	
	
	/* =============================================================================
	 * Function   : otp_server_set_token_loginlock 
	 * Description: set the status about a token which can bed used to login or not.
	 * Parameter  : 
	 *				strToken		the token ID
	 *				iLogin			the status to set
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_token_loginlock (String strToken, int iLogin);
	
	
	/* =============================================================================
	 * Function   : otp_server_bind 
	 * Description: bind a user with a token ID
	 * Parameter  : 
	 *				strUser			the user name
	 *				strToken		the token ID to be binded
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_bind (String strUser, String strToken);


	/* =============================================================================
	 * Function   : otp_server_unbind 
	 * Description: unbind the token of a user
	 * Parameter  : 
	 *				strUser			the user name
	 *				strToken		the token ID to be unbinded.
	 *				
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_unbind (String strUser, String strToken);
	
	
	/* ===========================================================================
	 * Function   : otp_server_set_usersource 
	 * Description: set user source.
	 * Parameter  : 
	 *				type		the user source type, listed bellow:
	 *
	 *                   USER_FROM_DEFAULT_DB  - use our default table.
	 *                   USER_FROM_OTHER_DB    - use other table.
	 *                   USER_FROM_LDAP        - from the LDAP.
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_usersource (int type);


	/* ===========================================================================
	 * Function   : otp_server_get_usersource 
	 * Description: get user source.
	 * Parameter  : 
	 *				type		the user source type, listed bellow:
	 *
	 *                   USER_FROM_DEFAULT_DB  - use our default table.
	 *                   USER_FROM_OTHER_DB    - use other table.
	 *                   USER_FROM_LDAP        - from the LDAP.
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_usersource (JOTPint type);


	/* ===========================================================================
	 * Function   : otp_server_set_user_table 
	 * Description: set user source from other table.
	 * Parameter  : 
	 *
	 *    s_source_table		user source table.
	 *   s_source_column		user source column.
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_user_table (String s_source_table, 
												String s_source_column);


	/* ===========================================================================
	 * Function   : otp_server_get_user_table
	 * Description: get user source from other table.
	 * Parameter  : 
	 *
	 *    s_source_table		user source table.
	 *   s_source_column		user source column.
	 *
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_user_table (JOTPString s_source_table, 
												JOTPString s_source_column);


	/* ===========================================================================
	 * Function   : otp_server_set_ldap_connection
	 * Description: set the LDAP connection parameters.
	 * Parameter  : 
	 *
	 *         ldap_user		user name
	 *         ldap_pass		password
	 *       ldap_server		ldap server IP address
	 *         ldap_port		ldap server port
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_ldap_connection ( String ldap_user, 
												String ldap_pass, 
								   				String ldap_server, int ldap_port );


	/* ===========================================================================
	 * Function   : otp_server_get_ldap_connection
	 * Description: get the LDAP connection parameters.
	 * Parameter  : 
	 *
	 *         ldap_user		user name
	 *         ldap_pass		password
	 *       ldap_server		ldap server IP address
	 *         ldap_port		ldap server port
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_ldap_connection ( JOTPString ldap_user, 
													JOTPString ldap_pass, 
								   					JOTPString ldap_server, 
								   					JOTPint ldap_port );


	/* ===========================================================================
	 * Function   : otp_server_set_ldap_ssl
	 * Description: set the LDAP connection parameters.
	 * Parameter  : 
	 *
	 *        enable_ssl		enable SSL connection to the LDAP or not:
	 *                          0 - disable, other value for enable.
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_set_ldap_ssl ( int enable_ssl );


	/* ===========================================================================
	 * Function   : otp_server_get_ldap_ssl
	 * Description: set the LDAP connection parameters.
	 * Parameter  : 
	 *
	 *        enable_ssl		enable SSL connection to the LDAP or not:
	 *                          0 - disable, other value for enable.
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_get_ldap_ssl ( JOTPint enable_ssl );


	/* ===========================================================================
	 * Function   : otp_server_set_ldap_base_dn
	 * Description: set the LDAP's BaseDN ( this is optional in generally ).
	 * Parameter  : 
	 *
	 *            basedn		the base DN of LDAP.
	 *
	 * return     : 0 - success, others for error.
	 */ 
	public native int otp_server_set_ldap_base_dn ( String basedn );


	/* ===========================================================================
	 * Function   : otp_server_get_ldap_base_dn
	 * Description: get the LDAP's BaseDN.
	 * Parameter  : 
	 *
	 *            basedn		the base DN of LDAP.
	 *
	 * return     : 0 - success, others for error.
	 */ 
	public native int otp_server_get_ldap_base_dn ( JOTPString basedn );


	/* ===========================================================================
	 * Function   : otp_server_set_ldap_addition_info
	 * Description: set LDAP`s filter and type information.
	 * Parameter  : 
	 *
   *            filter		filter string 
   *                          (e.g. "(&(objectCategory=person)(objectClass=user))")
   *
   *            attrib		attribute string
   *                          (e.g. "sAMAccountName")
   *
   *              type		LDAP server type:
   *
   *							LDAP_TYPE_MSAD
   *							LDAP_TYPE_OPENLDAP
   *							LDAP_TYPE_OTHER
	 *
	 * return     : 0 - success, others for error.
	 */  
	public native int otp_server_set_ldap_addition_info ( String filter, String attrib, 
															int type );


	/* ===========================================================================
	 * Function   : otp_server_get_ldap_addition_info
	 * Description: set LDAP`s filter and type information.
	 * Parameter  : 
	 *
   *            filter		filter string 
   *                          (e.g. "(&(objectCategory=person)(objectClass=user))")
   *
   *            attrib		attribute string
   *                          (e.g. "sAMAccountName")
   *
   *              type		LDAP server type:
   *
   *							LDAP_TYPE_MSAD
   *							LDAP_TYPE_OPENLDAP
   *							LDAP_TYPE_OTHER
	 *
	 * return     : 0 - success, others for error.
	 */  
	public native int otp_server_get_ldap_addition_info ( JOTPString filter, 
														JOTPString attrib, JOTPint type );

	
	/* =============================================================================
	 * Function   : otp_server_add_log 
	 * Description: add log to otp system.
	 * Parameter  : 
	 *				iLevel		log level: 0-99 OTP system used, >= 100, user defined
	 *				strUser		user name
	 *				strToken	token id
	 *				strContent	log detail
	 *
	 * return     : 0 - success, others for error.
	 */
	public native int otp_server_add_log (int iLevel, 
							String strUser, 
							String strToken, 
							String strContent);
							
}

