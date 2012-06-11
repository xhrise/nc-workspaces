package com.ufsoft.iufo.fmtplugin.formula;


import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
/**
 * ���Demo����ʽ�ڵ������
 * @author wupeng
 * 2004-8-30
 */
public class FormulaDescriptor extends AbstractPlugDes {

	public FormulaDescriptor(FormulaDefPlugin pi){
		super(pi);
	}

	public String getName() {
		return "formula";
	}

	public String getNote() {
		return "formula";
	}

	public IExtension[] createExtensions() {		
		ICommandExt cellDefExt = new CalculateFmlExt();//��Ԫ��ʽ
		ICommandExt fmlTransExt = new FormulaTransExt();//��ʽ����
		ICommandExt checkFmlExt = new CheckFmlExt(this);//��˹�ʽ
		ICommandExt batchFmlExt = new BatchFmlExt(this);//������ʽ
		FormulaRendererExt fmlRendererExt = new FormulaRendererExt(this);//��ʽ��չ�����Ƿ���Ⱦ

		ICommandExt[] exts = {cellDefExt, checkFmlExt, batchFmlExt, fmlTransExt, 
				fmlRendererExt};
		return exts;
	  
	}
}
