package com.ufsoft.iufo.fmtplugin.datapreview;

import java.awt.Component;
import java.util.Arrays;

import nc.pub.iufo.accperiod.IUFODefaultNCAccSchemeUtil;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.util.WebGlobalValue;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsData;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsDlg;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsExt;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.repdatainput.LoginEnvVO;
import com.ufsoft.iuforeport.repdatainput.RepDataOperResultVO;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableDataInputAuth;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.UfoPublic;

public class ChangeKeyWordPlugin2 extends AbstractPlugIn{

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			@Override
			protected IExtension[] createExtensions() {
				return new ICommandExt[] { new ChangeKeywordsExt(getReport()) {
					public Object[] getParams(UfoReport container) {
						// 得到切换关键字数据对象
						KeyVO[] keyVOs = KeywordModel.getInstance(
								getReport().getCellsModel()).getMainKeyVOs();
						if (keyVOs==null || keyVOs.length<=0)
							return new Object[] { new String[0],
									keyVOs, container};
						Arrays.sort(keyVOs);
						String strOrgPK = (String) container.getContextVo()
								.getAttribute(IUfoContextKey.ORG_PK);
						
						Context context = (Context) ((UfoReport) getReport()).getContextVo();
						MeasurePubDataVO pubData=(MeasurePubDataVO)context.getAttribute(IUfoContextKey.MEASURE_PUB_DATA_VO);

						ChangeKeywordsData[] changeKeywordsDatas = (ChangeKeywordsData[]) ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "getDataExploreChangeKeywordsData",
								new Object[]{keyVOs, pubData==null?null:pubData.getKeywords(), false,strOrgPK});
						
						IRepDataParam repDataParam =(IRepDataParam)context
								.getAttribute(IUfoContextKey.REP_DATA_PARAM);
						repDataParam.setOperType(WebGlobalValue.MenuType_RELEASE_PREVIEW);
						// WebGlobalValue.MenuType_RELEASE_PREVIEW

						// 打开选择报表的窗口
						ChangeKeywordsDlg dlg = new ChangeKeywordsDlg(
								container, changeKeywordsDatas);
						dlg.setVisible(true);
						if (dlg.getResult() == UfoDialog.ID_OK) {
							return new Object[] { dlg.getInputKeyValues(),
									keyVOs, container };
						}
						return null;
					}

					public UfoCommand getCommand() {
						return new UfoCommand() {
							/**
							 * @i18n miufohbbb00081=切换关键字失败
							 */
							public void execute(Object[] params) {
								if (params==null)
									return;
								
								String[] keyValues = (String[]) params[0];
								KeyVO[] keyVOs = (KeyVO[]) params[1];
								UfoReport ufoReport = (UfoReport) params[2];

								Context context = (Context) ((UfoReport) getReport()).getContextVo();
								IRepDataParam param=(IRepDataParam)context.getAttribute(IUfoContextKey.REP_DATA_PARAM);
								LoginEnvVO loginEnv=(LoginEnvVO)context.getAttribute(IUfoContextKey.LOGIN_ENV);
								
								try{
									MeasurePubDataVO pubData=(MeasurePubDataVO)ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "submitKeywordData",
										new Object[]{param,loginEnv,keyVOs,keyValues});
									Viewer view=getMainboard().getCurrentView();
									pubData.setAccSchemePK(IUFODefaultNCAccSchemeUtil.getInstance().getIUFODefaultNCAccScheme());
									view.getContext().setAttribute(IUfoContextKey.MEASURE_PUB_DATA_VO, pubData);
									context.setAttribute(IUfoContextKey.MEASURE_PUB_DATA_VO, pubData);
									
									param.setAloneID(pubData==null?null:pubData.getAloneID());
									param.setPubData(pubData);
									RepDataOperResultVO result=(RepDataOperResultVO)ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "openRepData",
											new Object[]{param,loginEnv});
									
									if (result.getCellsModel()!=null){
										ufoReport.getTable().setCurCellsModel(result.getCellsModel());
										ReportDesigner editor=null;
										Component comp=ufoReport.getTable();
										while (comp!=null){
											if (comp instanceof ReportDesigner){
												editor=(ReportDesigner)comp;
												break;
											}
											comp=comp.getParent();
										}
										if (editor!=null)
											result.getCellsModel().setCellsAuth(new TableDataInputAuth(editor));
									}
									MeasureFmt.setCanInput(true);
								}catch(Throwable e){
									AppDebug.debug(e);
									UfoPublic.sendErrorMessage(StringResource
											.getStringResource("miufohbbb00081"), ufoReport,e);
								}
							}
						};
					}

					public boolean isEnabled(Component focusComp) {
						return true;
					}
				} };

			}
		};
	}
}
 