package nc.ui.ca.tools;


public class OtpErrorTool {

	/* common error code */
	
	
	
	public static String getErrorMsg(int retotp){
		String errMsg = null;
		switch (retotp) {
			case JOTPAgentError.OTPA_OK:								//success 0
				errMsg = "成功";
				break;
			case JOTPAgentError.OTPA_ERR:								// fail 1
				errMsg = "失败";
				break;
			case JOTPAgentError.OTPA_INVALID_PARAM:						// bad parameter 2
				errMsg = "参数错误";
				break;
			case JOTPAgentError.OTPA_ALLOC_MEM_FAILED:					// memory allocation failed 3
				errMsg = "分配内存错误";
				break;
			case JOTPAgentError.OTPA_ACF_INVALID_PARAM:					//nvalid server information in .acf 4
				errMsg = "服务器结构信息无效";
				break;
			case JOTPAgentError.OTPA_INVALID_REQ:						// bad request 5
				errMsg = "请求结构无效";
				break;
			case JOTPAgentError.OTPA_INVALID_PACKET:					// bad packet 6
				errMsg = "无效数据包格式";
				break;
			case JOTPAgentError.OTPA_INIT_SOCKET_FAILED:				// init SOCKET failed 7
				errMsg = "SOCKET初始化失败";
				break;
			case JOTPAgentError.OTPA_SEND_REQ_FAILED:					// send packet failed 8
				errMsg = "请求发送失败";
				break;
			case JOTPAgentError.OTPA_RECV_ACK_FAILED:					// receive packet failed 9
				errMsg = "应答接收失败";
				break;
			case JOTPAgentError.OTPA_INVALID_USER:						// invalid user 10
				errMsg = "无效用户名";
				break;
			case JOTPAgentError.OTPA_TOO_MANY_CONCURRENT_REQ:			// too many concurrent request 11
				errMsg = "当前未完成的请求过多";
				break;
			case JOTPAgentError.OTPA_INVALID_SHAREKEY:					// invalid share key of server and agent 12
				errMsg = "客户端与服务器的共享密钥无效";
				break;
			case JOTPAgentError.OTPA_NEED_SYNC:							// OTP synchronization required 13
				errMsg = "需要同步OTP";
				break;
			case JOTPAgentError.OTPA_INVALID_PIN:					    // invalid PIN 14
				errMsg = "无效PIN码";
				break;
			case JOTPAgentError.OTPA_SET_AUTHNUM_FAILED:				// set the base authentication number failed 15
				errMsg = "服务器设置认证基数失败";
				break;
			case JOTPAgentError.OTPA_INVALID_TOKEN_KEY:					// invalid token key 16
				errMsg = "令牌的共享密钥无效";
				break;
			case JOTPAgentError.OTPA_SET_PIN_FAILED:					// set the new PIN failed on server side 17
				errMsg = "服务器设置新PIN失败";
				break;
			case JOTPAgentError.OTPA_INVALID_ACK:						// invalid ACK 18
				errMsg = "无效应答";
				break;
			case JOTPAgentError.OTPA_INVALID_ACF:						// invalid agent configure file 19
				errMsg = "客户端-服务器信息结构无效";
				break;
			case JOTPAgentError.OTPA_TOKEN_LOST:						// Token has been lost 20
				errMsg = "令牌已挂失";
				break;
			case JOTPAgentError.OTPA_INVALID_PROTECT_TYPE:				// invalid protected type of OTP 21
				errMsg = "动态口令保护类型无效";
				break;
			case JOTPAgentError.OTPA_INVALID_OTP:						// invalid OTP 22
				errMsg = "无效动态口令";
				break;
			case JOTPAgentError.OTPA_TOKEN_LOCKED:						// token has LOGIN-Locked OTP 23
				errMsg = "该令牌已锁定登陆";
				break;
			case JOTPAgentError.OTPA_INVALID_TOKEN_INFO:				// invalid token information 24
				errMsg = "无效令牌信息";
				break;
			case JOTPAgentError.OTPA_TOKEN_BINDED:						// token has binded before 25
				errMsg = "用户令牌已绑定";
				break;
			case JOTPAgentError.OTPA_BIND_TOKEN_FAILED:					// failed to bind user with the token 26
				errMsg = "设置用户令牌失败";
				break;
			case JOTPAgentError.OTPA_USER_NOTEXIST:						// user name doesn't exist 27
				errMsg = "用户不存在";
				break;
			case JOTPAgentError.OTPA_GET_TOKENINFO_FAILED:				// get the token information failed 28
				errMsg = "获取该用户令牌信息失败";
				break;
			case JOTPAgentError.OTPA_INVALID_USER_INFO:					// invalid user information 29
				errMsg = "用户提交的OTP请求信息非法";
				break;
			case JOTPAgentError.OTPA_PIN_ISREQUIRED:					// verify PIN required 30
				errMsg = "用户提交的认证PIN码为空";
				break;
			case JOTPAgentError.OTPA_INVALID_OTP_FORMAT:				// OTP length exceed 6 digits 32
				errMsg = "用户提交的OTP超过最大6位十进制数值";
				break;
			case JOTPAgentError.OTPA_PIN_NOTINIT:					  	// PIN-flag has set, but PIN is empty 33
				errMsg = "用户PIN码未初始化";
				break;
			case JOTPAgentError.OTPA_DBCORE_INTERNAL_ERROR:			 	// DB error
				errMsg = "获取离线认证的次数无效";
				break;
			case JOTPAgentError.OTPA_UNKNOWN:			 				// unknown error
				errMsg = "未知失败原因";
				break;
		default:
			break;
		}
		
		if(errMsg!=null&&errMsg.length()>0){
			errMsg = "动态认证信息:"+errMsg;
		}
		return errMsg;
	}
}
