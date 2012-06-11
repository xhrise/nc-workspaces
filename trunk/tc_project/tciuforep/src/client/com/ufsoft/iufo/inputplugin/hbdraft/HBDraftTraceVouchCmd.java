package com.ufsoft.iufo.inputplugin.hbdraft;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.hbbb.pub.HBBBUIUtils;
import nc.ui.hbbb.vouch.ComputeSubtotal;
import nc.ui.hbbb.vouch.VouchBO_Client;
import nc.ui.hbbb.vouch.VouchParameters;
import nc.ui.hbbb.vouch.VouchResultsUtil;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.vo.hbbb.vouch.VouchHeadVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.html.A;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.window.WindowMngPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigationPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNextPlugin;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.report.util.MultiLang;

public class HBDraftTraceVouchCmd extends UfoCommand implements IUfoContextKey{
	
	private UfoReport _report;

    /**
     * @param _report
     */
	public HBDraftTraceVouchCmd(UfoReport report) {
    	_report = report;
    }

	/**
	 * @i18n uiuforep00046=������¼������
	 * @i18n uiuforep00047=������¼��ѯ
	 */
	@Override
	public void execute(Object[] params) {
		// TODO Auto-generated method stub
		Map map =  HBDraftTraceExt.getSelectedCellParaMap(_report);
		if(map == null)
			return ;
		String strUnitPK = (String)map.get("unitPK");
		String measurePK = (String)map.get("measurePK");
		TableInputContextVO context = (TableInputContextVO)_report.getContextVo();
		
		Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj obj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
//		TableInputTransObj obj = context.getInputTransObj();
		IRepDataParam hbParam = obj.getRepDataParam();
		
		String aloneId = hbParam.getAloneID();
		String strTaskId = hbParam.getTaskPK();
		String vouchAloneId = HBDraftTraceExt.getAloneIdByUnitId(aloneId, strUnitPK, HBBBSysParaUtil.VER_VOUCHER);
		Object[][] datas = getVouchDatas(measurePK, vouchAloneId, strTaskId);
		CellsModel model = getVouchCellsModel(datas,vouchAloneId,strUnitPK);
		if (model == null) {
			javax.swing.JOptionPane.showMessageDialog(_report,
					MultiLang.getString("uiuforep00046"));
		}else
			QueryNavigation
		.showReport(new UfoReport(UfoReport.OPERATION_INPUT, _report.getContextVo(), model,new PluginRegister(){
			public void register() {
				getReport().addPlugIn(PrintPlugin.class.getName());        //��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
//				getReport().addPlugIn(HBDraftTracePlugin.class.getName()); 
				getReport().addPlugIn(WindowMngPlugin.class.getName());
				getReport().addPlugIn(QueryNavigationPlugin.class.getName());
				getReport().addPlugIn(QueryNextPlugin.class.getName());
			}}),
				MultiLang.getString("uiuforep00047"), true,false);
		
	}
	/**
	 * @i18n uiuforep00047=������¼��ѯ
	 */
	private CellsModel getVouchCellsModel(Object[][] datas,String vouchAloneId,String strUnitPK){
		Cell cellTitle = new Cell();
		cellTitle.setRow(0);
		cellTitle.setCol(0);
		cellTitle.setValue(MultiLang.getString("uiuforep00047"));
		
		IufoFormat format = new IufoFormat();
		format.setFontSize(30);
		format.setHalign(TableConstant.HOR_CENTER);
		format.setFold(true);
		cellTitle.setFormat(format);
		
	    int titleSize = 3;
		int headSize = 1;
		int keywordSize = 2;
		
		int rowSize = datas.length+titleSize + headSize+keywordSize;
		int colSize = datas[0].length-1;
		
		Cell[][] cells = new Cell[rowSize][colSize];
		
		cells[0][0] = cellTitle;
		
		Cell keywordCell = new Cell();
		keywordCell.setRow(titleSize);
		keywordCell.setCol(0);
		keywordCell.setValue(getKeyword(vouchAloneId));
		keywordCell.setFormat(getContentFormat());
		cells[titleSize][0] = keywordCell;
		
		keywordCell = new Cell();
		keywordCell.setRow(titleSize+1);
		keywordCell.setCol(0);
		keywordCell.setValue(getKeyword(vouchAloneId));
		keywordCell.setFormat(getContentFormat());
		cells[titleSize+1][0] = keywordCell;
		
		cells[titleSize + headSize+keywordSize-1] = getHeadCells(titleSize + headSize+keywordSize);
		
		for(int i=0;i<rowSize-titleSize -headSize-keywordSize;i++){
			for(int j=0;j<colSize;j++){
				Cell cell = new Cell();
				String value = null;
				cell.setFormat(getContentFormat());
				cell.setRow(i+titleSize + headSize+keywordSize);
				cell.setCol(j);
				cells[i+titleSize + headSize+keywordSize][j]= cell;
				if (datas[i][j + 1] != null) {
					value = datas[i][j + 1].toString();
					if (datas[i][j + 1] instanceof WebLabel) {
						value = ((WebLabel) datas[i][j + 1]).getValue();
						cell.getFormat().setForegroundColor(Color.RED);
					} else if (datas[i][j + 1] instanceof A) {
						value = ((A) datas[i][j + 1]).getValue();
					}
					cell.setValue(value);
				}
				
			}
		}
		
		AreaPosition aPos = AreaPosition.getInstance(0, 0, colSize, 3);
		
		CombinedCell cc = new CombinedCell(aPos, null, "");
		CellsModel model = CellsModel.getInstance(cells, true);
		
		CombinedAreaModel combinedModel = CombinedAreaModel.getInstance(model);
		combinedModel.addCombinedCell(cc);
		
		AreaPosition bPos = AreaPosition.getInstance(titleSize+1, 0, colSize, 0);
		CombinedCell cc2 = new CombinedCell(bPos,null , "");
		combinedModel.addCombinedCell(cc2);
		
		for(int i=0;i<rowSize-titleSize -headSize-keywordSize;i++){
			String strMeaRelaId = null;
			if(datas[i][0]!=null)
				strMeaRelaId = (String)datas[i][0];
			//�ϲ���Χ�ĵ�λ��
			for(int j=colSize-2;j<colSize;j++){
					CellPosition pos =CellPosition.getInstance(titleSize +headSize+keywordSize+i, j);
					
					
					if(strMeaRelaId!=null){
						Map map = new HashMap();
						map.put("colType", HBDraftTraceExt.DRAFT_MEETDATACOL);
						map.put("aloneId", vouchAloneId);
						String strMeasurePK = strMeaRelaId.split(VouchParameters.VOUCH_TABLEID_SPLIT)[0];
						String strRelaId = strMeaRelaId.split(VouchParameters.VOUCH_TABLEID_SPLIT)[1];
						map.put("measurePK", strMeasurePK);
						map.put("relaId", strRelaId);
						map.put("unitPK", strUnitPK);
						model.setBsFormat(pos, HBDraftTraceExt.HBDRAFT_TRACE_FMT, map);
					}
						
//					map.put("measurePK", measurePK);
					
			}
		}
		return model;
	}
	/**
	 * @i18n uiuforep00048=�Ƶ�����
	 * @i18n uiuforep00049=��������
	 * @i18n uiuforep00050=ƾ֤���
	 * @i18n uiuforep00051=ժҪ
	 * @i18n uiuforep00052=��Ŀ����
	 * @i18n uiuforep00053=�跽���
	 * @i18n uiuforep00054=�������
	 */
	private Cell[] getHeadCells(int row){
		Cell[] cells = new Cell[7];
		
		for(int i=0;i<7;i++){
			Cell cell = new Cell();
			cell.setCol(i);
			cell.setRow(row+1);
			cell.setFormat(getHeadFormat());
			cells[i] = cell;
		}
		cells[0].setValue(MultiLang.getString("uiuforep00048"));
		cells[1].setValue(MultiLang.getString("uiuforep00049"));
		cells[2].setValue(MultiLang.getString("uiuforep00050"));
		cells[3].setValue(MultiLang.getString("uiuforep00051"));
		cells[4].setValue(MultiLang.getString("uiuforep00052"));
		cells[5].setValue(MultiLang.getString("uiuforep00053"));
		cells[6].setValue(MultiLang.getString("uiuforep00054"));
		return cells;
	}
	private IufoFormat getContentFormat(){
		int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
		IufoFormat format = new IufoFormat();
		for (int i = 0; i < linePos.length; i++) {
	        format.setLineType(linePos[i],TableConstant.L_SOLID1);
	        format.setLineColor(linePos[i],Color.black);
	      }
		format.setHalign(TableConstant.HOR_RIGHT);
		return format;
	}
	private IufoFormat getHeadFormat(){
		int [] linePos = {Format.TOPLINE,Format.BOTTOMLINE,Format.LEFTLINE,Format.RIGHTLINE};
		IufoFormat format = new IufoFormat();
		for (int i = 0; i < linePos.length; i++) {
	        format.setLineType(linePos[i],TableConstant.L_SOLID1);
	        format.setLineColor(linePos[i],Color.black);
	      }
		format.setHalign(TableConstant.HOR_CENTER);
		format.setBackgroundColor(Color.GRAY);
		return format;
	}
	
	private Object[][] getVouchDatas(String measurePK,String vouchAloneId,String strTaskId){
		String strCon = " select t2.vouch_id, "
			+ " t2.son_order, t2.item_code,'a'," // t3.name, "
			// + " t2.son_order,'a'," //t3.name, "
			+ " t2.direct, t2.amount, t2.digest, "
			+ " t1.vouch_order, t1.input_date, t1.vouch_type ,t1.dxrelation_id "
			+ " from iufo_vouch_head t1,iufo_vouch_body t2"
			// + " ,iufo_measure t3 where t2.item_code = t3.code and "
			+ " where " + " t2.vouch_id = t1.id "
			+ " and t1.vouch_type like '%" + VouchParameters.VOUCHTYPED + "%' "
			+ " and t1.alone_id = '" + vouchAloneId + "' "
			+ " and t1.bcancel<>'1' " + " and t1.task_id = '" + strTaskId
			+ "'  and t1.id in( select t1.id from iufo_vouch_head t1 inner join iufo_vouch_body t2 on t2.vouch_id= t1.id where ( t2.item_code in ('"+measurePK+"')  and t1.alone_id = '" + vouchAloneId + "' "
			+ " and t1.task_id = '" + strTaskId+ "'  )) "+ " order by t1.vouch_order,t2.vouch_id,t2.son_order asc";

	Object[][] vo = null;
	Object[][] retVO = null;

	int[] i = { 6, 7 };
	Vector vObject = null;
	try {
		vObject = VouchBO_Client.loadVouchBodyObjVouchNumber(new String[]{strCon});
	} catch (Exception e) {
		AppDebug.debug(e);
	}
	if (vObject != null) {
		ComputeSubtotal.fillMeasureName(vObject, 4, 5);
		String strManuEntry = StringResource
				.getStringResource("miufopublic308"); // �ֹ�����
		String strAutoEntry = StringResource
				.getStringResource("miufopublic309"); // �Զ�����
		for (int m = 0; m < vObject.size(); m++) {
			Object[] arySingleRec = (Object[]) vObject.elementAt(m);
			String strVouchType = (String) arySingleRec[1];
			if (VouchHeadVO.TYPE_MANUAL_ENTRY.equals(strVouchType)) {
				arySingleRec[1] = strManuEntry;
			} else if (VouchHeadVO.TYPE_AUTO_ENTRY.equals(strVouchType)) {
				arySingleRec[1] = strAutoEntry;
			}
		}
		VouchResultsUtil.SortDispObjByDXVouchNO(vObject);
		vo = ComputeSubtotal.getSubtotal(vObject, 2, i, 5, 10,0,2);
		// ��������ʾȥ���ˡ���Ŀ���롱�У���5�У����Խ����ά��������˴���
		retVO = new Object[vo.length][];
		for (int kk = 0; kk < vo.length; kk++) {
			Object[] valueObj = vo[kk];
			Object[] tempObj = new Object[valueObj.length-2];
			if(valueObj[1]!=null && valueObj[1].equals(strAutoEntry))
			tempObj[0] = valueObj[4] + VouchParameters.VOUCH_TABLEID_SPLIT
					+ valueObj[valueObj.length -1];
			System.arraycopy(valueObj, 0, tempObj, 1, 4);
			System.arraycopy(valueObj, 5, tempObj, 5, valueObj.length - 7);
			
			retVO[kk] = tempObj;
		}
		vo = retVO;
	}
	if (vo == null)
		return null;
	return vo;
	}
	
	/**
	 * @i18n uiuforep00055=����ڼ�
	 */
	private String getKeyword(String aloneId){
		String keyword = "";
		MeasurePubDataVO pubdata= null;
		try {
			pubdata = MeasurePubDataBO_Client.findByAloneID(aloneId);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		KeyVO[] keys = pubdata.getKeyGroup().getKeys();
		String name = null;
		String keywordPK = null;
		String value = null;
		for(int i=0;i<keys.length;i++){
			name = keys[i].getName();
			keywordPK = keys[i].getKeywordPK();
			value = pubdata.getKeywordByPK(keywordPK);
			if(keys[i].getType()==KeyVO.TYPE_TIME)
				name = MultiLang.getString("uiuforep00055");
			if(keywordPK.equals(KeyVO.CORP_PK))
				value = HBBBUIUtils.getUnitCodeNameByPK(value);
			keyword = keyword+ name+": "+value+"    ";
		}
		return keyword;
	}
	

	

}
 