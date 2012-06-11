package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.sysplugin.keyassist.KeyAssistPlugin;

/**
 * �����߿�ݼ����
 * @author xulm
 *
 */
public class InputKeyAssistPlugin extends KeyAssistPlugin{

	/**
	 * ����ݼ���������
	 * @i18n report00001=����ȫ��
	 * @i18n report00002=����
	 * @i18n report00003=�ָ�
	 * @i18n miufo1001659=��ӡԤ��
	 * @i18n miufo1001332=��ӡ
	 * @i18n miufo1000654=����
	 * @i18n miufo1000653=����
	 * @i18n miufo1000655=ճ��
	 * @i18n miufo1000103=���
	 * @i18n miufoweb0018=ȫ��
	 * @i18n miufo1004056=��ʽˢ
	 * @i18n report00004=����һ��(֮��)
	 * @i18n report00005=����һ��(֮��)
	 * @i18n report00006=�������(֮��)
	 * @i18n uiufotask00079=ɾ��ѡ����
	 * @i18n miufo1001134=����
	 * @i18n mbidim00066=�滻
	 * @i18n miufo1001079=��λ
	 * @i18n miufo1000033=����
	 * @i18n uiufofurl0141=�������
	 * @i18n miufo1000959=����
	 * @i18n miufo1000961=����
	 * @i18n miufoiufoddc012=��λ��Ϣ
	 * @i18n report00007=���񱨱���Ϣ
	 * @i18n report00008=����������Ϣ
	 * @i18n report00009=��������
	 * @i18n report00010=ˢ�µ�ǰ����
	 * @i18n report00011=�л��ؼ�����������¼����
	 * @i18n report00012=����ҳǩ��һҳ
	 * @i18n report00013=����ҳǩ��һҳ
	 */
	@Override
	public String[][] getKeyAssistContent() {
		return  new String[][] {
			{ StringResource.getStringResource("mbiadhoc00006")+"(S)", "Ctrl+S"},
			{ StringResource.getStringResource("report00001")+"(L)", "Ctrl+Shift+S" }, 
			{ StringResource.getStringResource("report00002"), "Ctrl+Z" },
			{ StringResource.getStringResource("report00003"), "Ctrl+Y" }, 
			{ StringResource.getStringResource("miufo1001659")+"(V)", "Ctrl+W" },
			{ StringResource.getStringResource("miufo1001332")+"(P)...", "Ctrl+P" }, 
			{ StringResource.getStringResource("miufo1000654")+"(T)", "Ctrl+X" },
			{ StringResource.getStringResource("miufo1000653")+"(C)", "Ctrl+C" },
			{ StringResource.getStringResource("miufo1000655")+"(P)", "Ctrl+V" },
			{ StringResource.getStringResource("miufo1000103")+"(A)->"+StringResource.getStringResource("miufoweb0018") + "(A)", "Delete" }, 
			{ StringResource.getStringResource("miufo1004056"), "Ctrl+Shift+C" },
			{ StringResource.getStringResource("report00004"), "Ctrl+Enter" }, 
			{ StringResource.getStringResource("report00005")+"(C)" , "Ctrl+Shift+Enter" },
			{ StringResource.getStringResource("report00006")+"(M)...", "Ctrl+Ins" }, 
			{ StringResource.getStringResource("uiufotask00079"), "Ctrl+D" },
			{ StringResource.getStringResource("miufo1001134")+"(F)...", "Ctrl+F" },
			{ StringResource.getStringResource("mbidim00066")+"(E)...", "Ctrl+H" }, 
			{ StringResource.getStringResource("miufo1001079")+"(G)...", "Ctrl+G" },
			{ StringResource.getStringResource("miufo1000033")+"(C)", "Shift+F9" }, 
			{ StringResource.getStringResource("uiufofurl0141")+"(U)", "Ctrl+U" },
			{ StringResource.getStringResource("miufo1000959")+"(I)->Excel(E)...", "Ctrl+I" },
			{ StringResource.getStringResource("miufo1000961")+"(O)->Excel(E)...", "Ctrl+E" },
			//��Ϊ��ʱʵ�ֲ������ӵĿ�ݼ��������ڰ�����������ȥ��
			//{ StringResource.getStringResource("miufoiufoddc012")+"(U)...", "Ctrl+Shift+U" }, 
			//{ StringResource.getStringResource("report00007")+"(T)...", "Ctrl+Shift+T" },
			//{ StringResource.getStringResource("report00008")+"(D)...", "Ctrl+Shift+D" }
			{StringResource.getStringResource("report00009"), "F2" },
			{StringResource.getStringResource("report00010"), "F5" },
			{StringResource.getStringResource("report00011"), "F6" },
			{StringResource.getStringResource("report00012"), "Ctrl+PgUp" },
			{StringResource.getStringResource("report00013"), "Ctrl+PgDn" }
	    };
	}
	
	

}
  