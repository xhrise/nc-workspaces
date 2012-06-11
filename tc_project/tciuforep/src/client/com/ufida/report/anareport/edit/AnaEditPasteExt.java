package com.ufida.report.anareport.edit;

import javax.swing.JComboBox;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.edit.AbsChoosePaste;
import com.ufsoft.report.sysplugin.edit.EditPasteAll;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class AnaEditPasteExt extends EditPasteExt{

	public AnaEditPasteExt(UfoReport report) {
		super(report);

	}

	@Override
	public Object[] getParams(UfoReport container) {
		Object[] aryParams = new Object[2];// ��������
		AbsChoosePaste choosePaste = null;
		JComboBox comboBox = getItem();// ���ѡ����ճ���ؼ�������
		if (comboBox == null) {// ճ����ݼ��Ĵ���Ĭ����ճ��ȫ��
			comboBox = new JComboBox();
		}

		Object selectValueObj = comboBox.getSelectedItem();// ���ѡ����ճ����ֵ
		if (selectValueObj == null) {// �����ֵΪNULL�������ÿ�ݼ�ճ��
			choosePaste = new EditPasteAll(getReport(), false);	
		}
		if (selectValueObj != null) {// ����������˵�ѡ�񷵻ص�״̬����
			choosePaste = getSelectPaste(selectValueObj.toString());
		}
		if(choosePaste != null){
			choosePaste.setDataModel(getAanRepModel().getFormatModel());
			choosePaste.setAnchorCell(getStartCell());
		}
		comboBox.getModel().setSelectedItem(null);
		aryParams[0] = container;
		aryParams[1] = choosePaste;// ѡ��ʱ��״̬����ʵ��
		return aryParams;
	}
	
	
	@Override
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {
				if(!isEnabled(null))
					return;
				if (params == null || params.length < 2) {
					return;
				}
				AbsChoosePaste choosePaste = (AbsChoosePaste) params[1];// ���״̬����

				if (choosePaste == null) {
					return;
				}
				choosePaste.choosePaste();// ִ��ճ��
				if(!getAanRepModel().isFormatState())
					getAanPlugin().refreshDataModel(true);	
			}
			
		};
	}

	private CellPosition getStartCell(){
		AnaReportModel anaRepModel = getAanRepModel();
		CellsModel dataModel = getReport().getCellsModel();
		AreaPosition[] selectedAreas = anaRepModel.getFormatModel().getSelectModel().getSelectedAreas();
		if(!anaRepModel.isFormatState())//����̬ʱ����Ҫ������̬��λ��ת��Ϊ��ʽ̬�Ķ�Ӧ�ֶ�λ����
			selectedAreas =  anaRepModel.getFormatAreas(dataModel, dataModel.getSelectModel().getSelectedAreas());
		if(selectedAreas != null && selectedAreas.length >0)
			return selectedAreas[selectedAreas.length -1].getStart();
		return null;
	}
	
	private AnaReportModel getAanRepModel(){
		return getAanPlugin().getModel();
	}
	
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}

}
