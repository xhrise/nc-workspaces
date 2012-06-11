package com.ufsoft.iufo.inputplugin.hbdraft;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.ufsoft.iufo.inputplugin.ufobiz.UfoExcelExpPlugin;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

public class HBDraftVouchViewer extends ReportDesigner{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8465112296344011285L;
	@Override
	protected boolean save() {
		return false;
	}

	@Override
	public String[] createPluginList() {
		return new String[]{UfoExcelExpPlugin.class.getName()};
	}

	@Override
	protected void shutdown() {
		
	}

	public void initCellsModel(HBDraftTraceVO trace) {
		List<Cell[]> lstAll = new ArrayList<Cell[]>();
		//标题单元格
		Cell cellTitle = getTitelCell();
		lstAll.add(new Cell[]{cellTitle});
		String strVouchAloneId = getVouchAloneId(trace);
		Object[][] datas = getVouchDatas(trace.getMeasurePK(), strVouchAloneId, trace.getTaskId());
		
		int iColSize = datas[0].length-1;
		
		
		//关键字单元格
		Cell keywordCell = getKeywordCell(strVouchAloneId);
		lstAll.add(new Cell[]{keywordCell});
        //凭证头cell
		lstAll.add(getHeadCells());
		Cell[][] cells = new Cell[datas.length][iColSize];
		//构建凭证体cell
		for(int i=0;i< datas.length;i++){
			for(int kk = 0;kk < iColSize; kk++){
				Cell cell = new Cell();
				String value = null;
				cell.setFormat(getContentFormat());
				cell.setRow(i + 3);
				cell.setCol(kk);
				if (datas[i][kk + 1] != null) {
					value = datas[i][kk + 1].toString();
					if (datas[i][kk + 1] instanceof WebLabel) {
						value = ((WebLabel) datas[i][kk + 1]).getValue();
						cell.getFormat().setForegroundColor(Color.RED);
					} else if (datas[i][kk + 1] instanceof A) {
						value = ((A) datas[i][kk + 1]).getValue();
					}
					cell.setValue(value);
				}
				cells[i][kk] = cell;
			}
		}
		
		lstAll.addAll(Arrays.asList(cells));

		CellsModel model = CellsModel.getInstance(lstAll.toArray(new Object[0][0]), true);
		//组合标题单元格
		AreaPosition aPos = AreaPosition.getInstance(0, 0, iColSize, 1);
		
		CombinedCell cc = new CombinedCell(aPos, null, "");
		
		CombinedAreaModel combinedModel = CombinedAreaModel.getInstance(model);
		combinedModel.addCombinedCell(cc);
		//组合查询关键字单元格
		AreaPosition bPos = AreaPosition.getInstance(1, 0, iColSize, 1);
		CombinedCell cc2 = new CombinedCell(bPos,null , "");
		combinedModel.addCombinedCell(cc2);

		model.getRowHeaderModel().getHeader(0).setSize(100);
		model.getColumnHeaderModel().getHeader(3).setSize(getColWidth(datas, 4));
		model.getColumnHeaderModel().getHeader(4).setSize(getColWidth(datas, 5));
		model.setDirty(false);
		setCellsModel(model);
	
	}

	/**
	 * 得到关键字cell
	 * @param strVouchAloneId
	 * @return
	 */
	private Cell getKeywordCell(String strVouchAloneId) {
		Cell keywordCell = new Cell();
		keywordCell.setRow(1);
		keywordCell.setCol(0);
		keywordCell.setValue(getKeyword(strVouchAloneId));
		return keywordCell;
	}

	/**
	 * 得到标题cell
	 * @return
	 */
	private Cell getTitelCell() {
		Cell cellTitle = new Cell();
		cellTitle.setRow(0);
		cellTitle.setCol(0);
		cellTitle.setValue(MultiLang.getString("uiuforep00047"));
		cellTitle.setFormat(getTitleFormat());
		return cellTitle;
	}
	/**
	 * @i18n uiuforep00048=制单日期
	 * @i18n uiuforep00049=抵销类型
	 * @i18n uiuforep00050=凭证编号
	 * @i18n uiuforep00051=摘要
	 * @i18n uiuforep00052=项目名称
	 * @i18n uiuforep00053=借方金额
	 * @i18n uiuforep00054=贷方金额
	 */
	private Cell[] getHeadCells(){
		Cell[] cells = new Cell[7];
		
		for(int i=0;i<7;i++){
			Cell cell = new Cell();
			cell.setCol(i);
			cell.setRow(2);
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
	/**
	 * 得到指标列的宽度
	 * @param datas
	 * @return
	 */
	private int getColWidth(Object[][] datas,int iIndex){
		int iMaxLen = 0;
		for(int i=0;i<datas.length;i++){
			if(datas[i][iIndex] == null)
				continue;
			String strMeasureName = null;
			if(datas[i][iIndex] instanceof WebLabel)
				strMeasureName = ((WebLabel)datas[i][iIndex]).getValue();
			else
				strMeasureName = datas[i][iIndex].toString();
			if (iMaxLen<strMeasureName.getBytes().length)
				iMaxLen=strMeasureName.getBytes().length;
		}
		return (int)(iMaxLen*7.5)+20 < 100 ? 100 : (int)(iMaxLen*7.5)+20;
	}
	/**
	 * 得到查询抵销分录的aloneid
	 * @param trace
	 * @return
	 */
	private String  getVouchAloneId(HBDraftTraceVO trace){
		try {
			MeasurePubDataVO pubdata = MeasurePubDataBO_Client.findByAloneID(trace.getAloneId());
			pubdata.setAloneID(null);
			pubdata.setFormulaID(null);
			pubdata.setVer(HBBBSysParaUtil.VER_VOUCHER);
			if(trace.getUnitId()!=null){
				pubdata.setKeywordByPK(KeyVO.CORP_PK, trace.getUnitId());
				pubdata.setUnitPK(trace.getUnitId());
			}
			return MeasurePubDataBO_Client.getAloneID(pubdata);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return null;
		
	}
	
	/**
	 * 得到抵销分录数据
	 * @param measurePK
	 * @param vouchAloneId
	 * @param strTaskId
	 * @return
	 */
	private Object[][] getVouchDatas(String measurePK,String vouchAloneId,String strTaskId){
		String strCon = " select t2.vouch_id, "
			+ " t2.son_order, t2.item_code,'a'," 
			+ " t2.direct, t2.amount, t2.digest, "
			+ " t1.vouch_order, t1.input_date, t1.vouch_type ,t1.dxrelation_id "
			+ " from iufo_vouch_head t1,iufo_vouch_body t2"
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
				.getStringResource("miufopublic308"); // 手工抵销
		String strAutoEntry = StringResource
				.getStringResource("miufopublic309"); // 自动抵销
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
		// 界面上显示去掉了“项目编码”列（第5列），对结果二维数组进行了处理
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
	 * 得到关键字的值
	 * @param aloneId
	 * @return
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
	/**
	 * 得到内容的format
	 * @return
	 */
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
		format.setBackgroundColor(Color.lightGray);
		return format;
	}
	private IufoFormat getTitleFormat(){
		IufoFormat format = new IufoFormat();
		format.setFontSize(30);
		format.setHalign(TableConstant.HOR_CENTER);
		format.setFold(true);
		return format;
	}
}
