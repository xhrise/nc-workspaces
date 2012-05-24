/* =============================================================================
 * JOTPAgentError.java - OTP Agent JNI library's error code defination.
 *  
 * OTP c100 | OTP c200
 * Copyright (C) 2007-2008 Feitian Technologies Co., Ltd
 * http://www.FTsafe.com
 *
 *
 * Created on 2008-10-24, Last version: 2.0
 *
 * Version:2.0
 *  
 */

package nc.ui.ca.tools;							/* Package name */

/* Error Code Jlass*/ 
public class JOTPAgentError
{
	/* common error code */
	public final static int  OTPA_OK							= 		0x0;	;	// success

	public final static int  OTPA_ERR							= 		0x1;	;	// fail

	public final static int  OTPA_INVALID_PARAM					= 		0x2;	;	// bad parameter

	public final static int  OTPA_ALLOC_MEM_FAILED				= 		0x3;	;	// memory allocation failed

	public final static int  OTPA_ACF_INVALID_PARAM				= 		0x4;	;	// invalid server information in .acf

	public final static int  OTPA_INVALID_REQ					= 		0x5;	;	// bad request

	public final static int  OTPA_INVALID_PACKET				= 		0x6;	;	// bad packet

	public final static int  OTPA_INIT_SOCKET_FAILED			= 		0x7;	;	// init SOCKET failed

	public final static int  OTPA_SEND_REQ_FAILED				= 		0x8;	;	// send packet failed

	public final static int  OTPA_RECV_ACK_FAILED				= 		0x9;	;	// receive packet failed

	public final static int  OTPA_INVALID_USER					= 		0xA;	;	// invalid user 

	public final static int  OTPA_TOO_MANY_CONCURRENT_REQ		= 		0xB;	;	// too many concurrent request

	public final static int  OTPA_INVALID_SHAREKEY				= 		0xC;	;	// invalid share key of server and agent 

	public final static int  OTPA_NEED_SYNC						= 		0xD;	;	// OTP synchronization required

	public final static int  OTPA_INVALID_PIN					= 		0xE;	;	// invalid PIN

	public final static int  OTPA_SET_AUTHNUM_FAILED			= 		0xF;	;	// set the base authentication number failed

	public final static int  OTPA_INVALID_TOKEN_KEY				= 		0x10;	// invalid token key

	public final static int  OTPA_SET_PIN_FAILED				= 		0x11;	// set the new PIN failed on server side

	public final static int  OTPA_INVALID_ACK					= 		0x12;	// invalid ACK

	public final static int  OTPA_INVALID_ACF					= 		0x13;	// invalid agent configure file

	public final static int  OTPA_TOKEN_LOST					= 		0x14;	// Token has been lost.

	public final static int  OTPA_INVALID_PROTECT_TYPE			= 		0x15;	// invalid protected type of OTP

	public final static int  OTPA_INVALID_OTP					= 		0x16;	// invalid OTP

	public final static int  OTPA_TOKEN_LOCKED					= 		0x17;	// token has LOGIN-Locked

	public final static int  OTPA_INVALID_TOKEN_INFO			= 		0x18;	// invalid token information

	public final static int  OTPA_TOKEN_BINDED					= 		0x19;	// token has binded before

	public final static int  OTPA_BIND_TOKEN_FAILED				= 		0x1A;	// failed to bind user with the token

	public final static int  OTPA_USER_NOTEXIST					= 		0x1B;	// user name doesn't exist

	public final static int  OTPA_GET_TOKENINFO_FAILED			= 		0x1C;	// get the token information failed

	public final static int  OTPA_INVALID_USER_INFO				= 		0x1D;	// invalid user information

	public final static int  OTPA_PIN_ISREQUIRED				= 		0x1F;	// verify PIN required

	public final static int  OTPA_INVALID_OTP_FORMAT			= 		0x20;	// OTP length exceed 6 digits

	public final static int  OTPA_PIN_NOTINIT					= 		0x21;	// PIN-flag has set, but PIN is empty

	public final static int  OTPA_DBCORE_INTERNAL_ERROR			= 		0x22;	// DB error

	public final static int  OTPA_UNKNOWN						= 		0x3F;	// unknown error
	
}

