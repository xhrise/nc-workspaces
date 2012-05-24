/* =============================================================================
 * erro.java - OTP server JNI library's error code defination.
 *  
 * OTP c100 | OTP c200
 * Copyright (C) 2007-2009 Feitian Technologies Co., Ltd
 * http://www.FTsafe.com
 *
 * Created on 2008-10-6, Last version: 2.0
 *
 * Version:2.0
 *  
 */

package nc.ui.wb.tools;			/* package name */

/* Error Code Jlass*/ 
public class JOTPError
{

	/* common error code */
	
	public final static int OTPS_SUCCESS					= 0x00000000;	/* Status OK */
	
	public final static int OTPS_INVALID_PARAMETER			= 0x00000103;	/* invalid parameter(s) */
	
	public final static int OTPS_INVALID_TOKEN				= 0x00000104;	/* Token is invalid, or token has not bind yet */
	
	public final static int OTPS_INVALID_TNK_FILE			= 0x00000105;	/* .tnk file is invalid */
	
	public final static int OTPS_INVALID_LIC_FILE			= 0x00000106;	/* .lic file is invalid */
	
	public final static int OTPS_PIN_TOO_LONG				= 0x00000107;	/* PIN is too long */
	
	public final static int OTPS_USER_HAS_ATOKEN_BANDED		= 0x00000108;	/* user has a token banded */

	public final static int OTPS_TOKEN_HASNOT_BINDED		= 0x00000109;	/* token has't binded with this user */

	public final static int OTPS_UNBIND_FAILED				= 0x0000010A;	/* failed to unbind user-token. */

		
	public final static int OTPS_UNKNOWN					= 0x0000FFFF;	/* Unknown error */
	
	public final static int OTPS_COMM_FAIL					= 0x80001001;	/* operation failed */
	
	public final static int OTPS_LICENSE_INVALID			= 0x80001003;	/* invalid license file */
	
	public final static int OTPS_LICENSE_EXPIRED			= 0x80001004;	/* license file has expired */
	
	public final static int OTPS_USER_EMPTY					= 0x80002001;	/* user name is empty */
	
	public final static int OTPS_OTP_EMPTY					= 0x80002002;	/* OTP is empty */
	
	public final static int OTPS_PIN_EMPTY					= 0x80002003;	/* user PIN  is empty */
	
	public final static int OTPS_TSN_EMPTY					= 0x80002004;	/* token id is empty */
	
	
	public final static int OTPS_INVALID_OTP				= 0x80002006;	/* invalid OTP */
	
	public final static int OTPS_INVALID_TOKENKEY			= 0x80002007;	/* invalid token security key */
	
	public final static int OTPS_INVALID_AUTHNUM			= 0x80002008;	/* invalid HOTP token`s authentication number */
	
	public final static int OTPS_NEED_SYNC					= 0x80002009;	/* synchronization required */
	
	public final static int OTPS_INVALID_TIMEDRIFT			= 0x8000200A;	/* invalid TOTP token`s time drift */
	
	public final static int OTPS_INVALID_PIN				= 0x8000200B;	/* invalid user PIN */
	
	public final static int OTPS_INVALID_DBPIN				= 0x8000200C;	/* PIN data in DB is invalid */
	
	public final static int OTPS_GET_USERINFO_FAILED		= 0x8000200D;	/* failed to get user information from DB */
	
	public final static int OTPS_GET_TOKENINFO_FAILED		= 0x8000200E;	/* failed to get token information from DB */
	
	public final static int OTPS_TOKEN_LOST					= 0x8000200F;	/* token has been set to 'lost' status */
	
	public final static int OTPS_LOGIN_LOCKED				= 0x80002010;	/* token has been set to 'login locked' status */
	
	
	public final static int OTPS_INVALID_TOKENTYPE			= 0x80002012;	/* invalid token type */ 
	
	public final static int OTPS_INVALID_TOKENSN			= 0x80002013;	/* invalid token ID number */
	
	public final static int OTPS_GET_TBIND_FAILED			= 0x80002014;	/* failed to get token binding status */
	
	public final static int OTPS_GET_UBIND_FAILED			= 0x80002015;	/* failed to get user's binded-token amount */
	
	public final static int OTPS_TOKEN_HAS_BINDED			= 0x80002016;	/* token has binded with other user */
	
	public final static int OTPS_BIND_EXCEED				= 0x80002017;	/* token cannot be binded with more users */
		
	
	/*
	 * data base or server configuration stuff
	 */
	
	public final static int OTPS_INVALID_CONFIGFILE			= 0x8000300F;	/* invalid configuration file */
	
	public final static int OTPS_INVALID_DBTYPE				= 0x80003010;	/* invalid DB type */
	
	public final static int OTPS_DB_CONNECT_FAILED			= 0x80003011;	/* database connecting failed */
	
	public final static int OTPS_ODBC_DRIVER_NOTFOUND		= 0x80003012;	/* ODBC driver not found */
	
	public final static int OTPS_DB_NOTFOUND				= 0x80003013;	/* DB not found */
	
	public final static int OTPS_DB_SERVER_NOTFOUND			= 0x80003014;	/* server not found or access denied */
	
	public final static int OTPS_INVALID_DB_USER_PASS		= 0x80003015;	/* connect to DB failed with user or password error */
	
	public final static int OTPS_CONNECT_TIMEOUT			= 0x80003016;	/* DB connecting timeout */
	
	public final static int OTPS_TOOMANY_CONNECT			= 0x80003017;	/* too many connections */
	
	public final static int OTPS_ADD_USER_FAILED			= 0x80003018;	/* add user failed */
	
	public final static int OTPS_USER_EXISTS				= 0x80003019;	/* user has existed */
	
	public final static int OTPS_USER_NOTEXISTS				= 0x8000301A;	/* user doesn't exist */
	
	public final static int OTPS_RECORD_NOTEXISTS			= 0x8000301B;	/* record doesn't exist */ 
	
	public final static int OTPS_TOKEN_EXISTS				= 0x8000301C;	/* token has existed */
	
	public final static int OTPS_TOKEN_NOTEXISTS			= 0x8000301D;	/* token doesn't exist */
	
	public final static int OTPS_AGENT_EXISTS				= 0x8000301E;	/* agent has existed */
	
	public final static int OTPS_AGENT_NOTEXISTS			= 0x8000301F;	/* agent doesn't exist */
	
	public final static int OTPS_SERVER_EXISTS				= 0x80003020;	/* auth. server has existed */
	
	public final static int OTPS_SERVER_NOTEXISTS			= 0x80003021;	/* auth. server doesn't exist */
	
	public final static int OTPS_INVALID_USERSOURCE_TYPE	= 0x80003022;	/* invalid user source type */
	
	public final static int OTPS_NOTENOUGH_BUFFER			= 0x80003023;	/* buffer size if too short */
	
	public final static int OTPS_EXEC_SQL_QUERY_FAILED		= 0x80003024;	/* internal error: failed to exec the SQL query. */
	
}

