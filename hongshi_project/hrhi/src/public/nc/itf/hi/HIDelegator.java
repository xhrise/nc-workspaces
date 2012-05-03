package nc.itf.hi;

import nc.bs.framework.common.NCLocator;
import nc.itf.hr.u9.psn.IPersonADDSV;
import nc.itf.uap.bd.corp.ICorpQry;
import nc.itf.uap.bd.dept.IDeptdocQry;
import nc.itf.uap.bd.psndoc.IPsnDocQueryService;
import nc.itf.uap.pf.IPFMessage;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.uap.sf.IUserAndClerkService;
import nc.itf.uap.sfapp.IBillcodeRuleService;

public class HIDelegator {	
	private static IBillcodeRuleService billcodeRuleService = null;

	public static IBillcodeRuleService getBillcodeRule() {
		billcodeRuleService = (IBillcodeRuleService) NCLocator.getInstance().lookup(
				IBillcodeRuleService.class.getName());
		return billcodeRuleService;
	}
	private static ICorpQry corpQry = null;
	public static ICorpQry getCorpQry() {
		if (corpQry == null) {
			corpQry = (ICorpQry) NCLocator.getInstance().lookup(
					ICorpQry.class.getName());
	
		}
		return corpQry;
	}
	private static ICreateCorpQueryService createCorpQueryService = null;
	
	public static ICreateCorpQueryService getCreateCorpQueryService(){
		if(createCorpQueryService == null){
			createCorpQueryService = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
		}
		return createCorpQueryService;
	}
	private static IDeptdocQry deptdocQry = null;
	public static IDeptdocQry getDeptdocQry(){
		if(deptdocQry==null){
			deptdocQry = (IDeptdocQry)NCLocator.getInstance().lookup(IDeptdocQry.class.getName());
		}
		return deptdocQry;
	}
	private static nc.itf.hi.IHIFileTrans fileTrans = null;
	public static nc.itf.hi.IHIFileTrans getFileTrans(){
		if(fileTrans==null){
			fileTrans = (nc.itf.hi.IHIFileTrans)NCLocator.getInstance().lookup(nc.itf.hi.IHIFileTrans.class.getName());
		}
		return fileTrans;
	}
	private static IPFMessage pfmessage = null;
	public static IPFMessage getPFMessage(){
		if(pfmessage == null){
			pfmessage = (IPFMessage)NCLocator.getInstance().lookup(IPFMessage.class.getName());
		}
		return pfmessage;
	}
	private static IPsnCardBrowser psnCardBrowser = null;
	public static IPsnCardBrowser getPsnCardBrowser(){
		if(psnCardBrowser== null){
			psnCardBrowser = (IPsnCardBrowser)NCLocator.getInstance().lookup(IPsnCardBrowser.class.getName());
		}
		return psnCardBrowser;
	}
	

	private static IPsndocBad psndocBad= null;
	public static IPsndocBad getPsndocBad(){
		if(psndocBad ==null){
			psndocBad =(IPsndocBad)NCLocator.getInstance().lookup(IPsndocBad.class.getName());
		}
		return psndocBad;
	}
	private static IPsnDocQueryService psnDocQueryService = null;
	public static IPsnDocQueryService getPsnDocQueryService(){
		if(psnDocQueryService== null){
			psnDocQueryService = (IPsnDocQueryService)NCLocator.getInstance().lookup(IPsnDocQueryService.class.getName());
		}
		return psnDocQueryService;
	}
	private static IInfoSet iInfoSet = null;
	public static IInfoSet getIInfoSet(){
		if(iInfoSet==null){
			iInfoSet = (IInfoSet)NCLocator.getInstance().lookup(IInfoSet.class.getName());
		}
		return iInfoSet;
	}
	private static IPsnInf psnInf= null;
	public static IPsnInf getPsnInf(){
		if(psnInf==null){
			psnInf = (IPsnInf)NCLocator.getInstance().lookup(IPsnInf.class.getName());
		}
		return psnInf;
	}
	private static IPsnInfReport psnInfReport = null;
	public static IPsnInfReport getPsnInfReport(){
		if(psnInfReport == null){
			psnInfReport = (IPsnInfReport)NCLocator.getInstance().lookup(IPsnInfReport.class.getName());
		}
		return psnInfReport;
	}

	private static ISglstat sglstat= null;
	public static ISglstat getSglstatH() {
		if(sglstat ==null){
			sglstat =(ISglstat)NCLocator.getInstance().lookup(ISglstat.class.getName());
		}
		return sglstat;
	}
	
	private static IUserAndClerkService userAndClerkService = null;
	public static IUserAndClerkService getUserAndClerkService(){
		if(userAndClerkService == null){
			userAndClerkService = (IUserAndClerkService)NCLocator.getInstance().lookup(IUserAndClerkService.class.getName());
		}
		return userAndClerkService;
	}
	private static IUserManage usermanage = null;
	public static IUserManage getUserManage(){
		if(usermanage == null){
			usermanage = (IUserManage)NCLocator.getInstance().lookup(IUserManage.class.getName());
		}
		return usermanage;
	}
	private static IHRhiBS iHRhiBS = null;
	public static IHRhiBS getIHRhiBS(){
		if(iHRhiBS == null){
			iHRhiBS = (IHRhiBS)NCLocator.getInstance().lookup(IHRhiBS.class.getName());
		}
		return iHRhiBS;
	}
	private static IHRhiQBS iHRhiQBS = null;
	public static IHRhiQBS getIHRhiQBS(){
		if(iHRhiQBS == null){
			iHRhiQBS = (IHRhiQBS)NCLocator.getInstance().lookup(IHRhiQBS.class.getName());
		}
		return iHRhiQBS;
	}
	
	private static IHrRpt iHRRpt = null;
	public static IHrRpt getIHrReport(){
		if(iHRRpt == null){
			iHRRpt = (IHrRpt)NCLocator.getInstance().lookup(IHrRpt.class.getName());
		}
		return iHRRpt;
	}
	private static IKeyPerson iKeyPerson = null;
	public static IKeyPerson getIKeyPerson(){
		if(iKeyPerson == null){
			iKeyPerson = (IKeyPerson)NCLocator.getInstance().lookup(IKeyPerson.class.getName());
		}
		return iKeyPerson;
	}
	private static IPersonADDSV iPersonADDSV = null;
	public static IPersonADDSV getIPersonADDSV(){
		if(iPersonADDSV == null){
			iPersonADDSV = (IPersonADDSV)NCLocator.getInstance().lookup(IPersonADDSV.class.getName());
		}
		return iPersonADDSV;
	}
}
