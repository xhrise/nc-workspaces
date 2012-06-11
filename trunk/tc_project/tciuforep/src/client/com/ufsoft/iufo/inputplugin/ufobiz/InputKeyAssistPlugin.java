package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.sysplugin.keyassist.KeyAssistPlugin;

/**
 * 报表工具快捷键插件
 * @author xulm
 *
 */
public class InputKeyAssistPlugin extends KeyAssistPlugin{

	/**
	 * 填充快捷键帮助内容
	 * @i18n report00001=保存全部
	 * @i18n report00002=撤销
	 * @i18n report00003=恢复
	 * @i18n miufo1001659=打印预览
	 * @i18n miufo1001332=打印
	 * @i18n miufo1000654=剪切
	 * @i18n miufo1000653=复制
	 * @i18n miufo1000655=粘贴
	 * @i18n miufo1000103=清除
	 * @i18n miufoweb0018=全部
	 * @i18n miufo1004056=格式刷
	 * @i18n report00004=插入一组(之后)
	 * @i18n report00005=复制一组(之后)
	 * @i18n report00006=插入多组(之后)
	 * @i18n uiufotask00079=删除选中组
	 * @i18n miufo1001134=查找
	 * @i18n mbidim00066=替换
	 * @i18n miufo1001079=定位
	 * @i18n miufo1000033=计算
	 * @i18n uiufofurl0141=表内审核
	 * @i18n miufo1000959=导入
	 * @i18n miufo1000961=导出
	 * @i18n miufoiufoddc012=单位信息
	 * @i18n report00007=任务报表信息
	 * @i18n report00008=报表数据信息
	 * @i18n report00009=弹出参照
	 * @i18n report00010=刷新当前界面
	 * @i18n report00011=切换关键字面板和数据录入区
	 * @i18n report00012=数据页签上一页
	 * @i18n report00013=数据页签下一页
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
			//因为暂时实现不了增加的快捷键，所以在帮助内容中先去掉
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
  