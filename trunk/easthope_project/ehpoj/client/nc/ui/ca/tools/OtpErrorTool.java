package nc.ui.ca.tools;


public class OtpErrorTool {

	/* common error code */
	
	
	
	public static String getErrorMsg(int retotp){
		String errMsg = null;
		switch (retotp) {
			case JOTPAgentError.OTPA_OK:								//success 0
				errMsg = "�ɹ�";
				break;
			case JOTPAgentError.OTPA_ERR:								// fail 1
				errMsg = "ʧ��";
				break;
			case JOTPAgentError.OTPA_INVALID_PARAM:						// bad parameter 2
				errMsg = "��������";
				break;
			case JOTPAgentError.OTPA_ALLOC_MEM_FAILED:					// memory allocation failed 3
				errMsg = "�����ڴ����";
				break;
			case JOTPAgentError.OTPA_ACF_INVALID_PARAM:					//nvalid server information in .acf 4
				errMsg = "�������ṹ��Ϣ��Ч";
				break;
			case JOTPAgentError.OTPA_INVALID_REQ:						// bad request 5
				errMsg = "����ṹ��Ч";
				break;
			case JOTPAgentError.OTPA_INVALID_PACKET:					// bad packet 6
				errMsg = "��Ч���ݰ���ʽ";
				break;
			case JOTPAgentError.OTPA_INIT_SOCKET_FAILED:				// init SOCKET failed 7
				errMsg = "SOCKET��ʼ��ʧ��";
				break;
			case JOTPAgentError.OTPA_SEND_REQ_FAILED:					// send packet failed 8
				errMsg = "������ʧ��";
				break;
			case JOTPAgentError.OTPA_RECV_ACK_FAILED:					// receive packet failed 9
				errMsg = "Ӧ�����ʧ��";
				break;
			case JOTPAgentError.OTPA_INVALID_USER:						// invalid user 10
				errMsg = "��Ч�û���";
				break;
			case JOTPAgentError.OTPA_TOO_MANY_CONCURRENT_REQ:			// too many concurrent request 11
				errMsg = "��ǰδ��ɵ��������";
				break;
			case JOTPAgentError.OTPA_INVALID_SHAREKEY:					// invalid share key of server and agent 12
				errMsg = "�ͻ�����������Ĺ�����Կ��Ч";
				break;
			case JOTPAgentError.OTPA_NEED_SYNC:							// OTP synchronization required 13
				errMsg = "��Ҫͬ��OTP";
				break;
			case JOTPAgentError.OTPA_INVALID_PIN:					    // invalid PIN 14
				errMsg = "��ЧPIN��";
				break;
			case JOTPAgentError.OTPA_SET_AUTHNUM_FAILED:				// set the base authentication number failed 15
				errMsg = "������������֤����ʧ��";
				break;
			case JOTPAgentError.OTPA_INVALID_TOKEN_KEY:					// invalid token key 16
				errMsg = "���ƵĹ�����Կ��Ч";
				break;
			case JOTPAgentError.OTPA_SET_PIN_FAILED:					// set the new PIN failed on server side 17
				errMsg = "������������PINʧ��";
				break;
			case JOTPAgentError.OTPA_INVALID_ACK:						// invalid ACK 18
				errMsg = "��ЧӦ��";
				break;
			case JOTPAgentError.OTPA_INVALID_ACF:						// invalid agent configure file 19
				errMsg = "�ͻ���-��������Ϣ�ṹ��Ч";
				break;
			case JOTPAgentError.OTPA_TOKEN_LOST:						// Token has been lost 20
				errMsg = "�����ѹ�ʧ";
				break;
			case JOTPAgentError.OTPA_INVALID_PROTECT_TYPE:				// invalid protected type of OTP 21
				errMsg = "��̬�����������Ч";
				break;
			case JOTPAgentError.OTPA_INVALID_OTP:						// invalid OTP 22
				errMsg = "��Ч��̬����";
				break;
			case JOTPAgentError.OTPA_TOKEN_LOCKED:						// token has LOGIN-Locked OTP 23
				errMsg = "��������������½";
				break;
			case JOTPAgentError.OTPA_INVALID_TOKEN_INFO:				// invalid token information 24
				errMsg = "��Ч������Ϣ";
				break;
			case JOTPAgentError.OTPA_TOKEN_BINDED:						// token has binded before 25
				errMsg = "�û������Ѱ�";
				break;
			case JOTPAgentError.OTPA_BIND_TOKEN_FAILED:					// failed to bind user with the token 26
				errMsg = "�����û�����ʧ��";
				break;
			case JOTPAgentError.OTPA_USER_NOTEXIST:						// user name doesn't exist 27
				errMsg = "�û�������";
				break;
			case JOTPAgentError.OTPA_GET_TOKENINFO_FAILED:				// get the token information failed 28
				errMsg = "��ȡ���û�������Ϣʧ��";
				break;
			case JOTPAgentError.OTPA_INVALID_USER_INFO:					// invalid user information 29
				errMsg = "�û��ύ��OTP������Ϣ�Ƿ�";
				break;
			case JOTPAgentError.OTPA_PIN_ISREQUIRED:					// verify PIN required 30
				errMsg = "�û��ύ����֤PIN��Ϊ��";
				break;
			case JOTPAgentError.OTPA_INVALID_OTP_FORMAT:				// OTP length exceed 6 digits 32
				errMsg = "�û��ύ��OTP�������6λʮ������ֵ";
				break;
			case JOTPAgentError.OTPA_PIN_NOTINIT:					  	// PIN-flag has set, but PIN is empty 33
				errMsg = "�û�PIN��δ��ʼ��";
				break;
			case JOTPAgentError.OTPA_DBCORE_INTERNAL_ERROR:			 	// DB error
				errMsg = "��ȡ������֤�Ĵ�����Ч";
				break;
			case JOTPAgentError.OTPA_UNKNOWN:			 				// unknown error
				errMsg = "δ֪ʧ��ԭ��";
				break;
		default:
			break;
		}
		
		if(errMsg!=null&&errMsg.length()>0){
			errMsg = "��̬��֤��Ϣ:"+errMsg;
		}
		return errMsg;
	}
}
