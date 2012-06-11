package com.ufsoft.iuforeport.batchprint;


import java.io.IOException;
import java.util.List;

import javax.print.MultiDoc;

import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumber;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumberRender;
import com.ufsoft.iufo.inputplugin.inputcore.IDNameRender;
import com.ufsoft.iufo.inputplugin.inputcore.StringRender;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.re.IDName;

public class IUFOMultiDoc implements MultiDoc {
	
	private int pkIndex=-1;
	
	/**批量打印时，打印对象数组 */
	private List<TableInputTransObj> vTransObj;	
	private boolean isMulti;
	/** 打印时可调整的文档，默认是第一个文档*/
	private IUFODoc editableDoc;

	public IUFOMultiDoc(List<TableInputTransObj> vTransObj){
		this.vTransObj=vTransObj;
		if(vTransObj!=null&&vTransObj.size()>1){
			setMulti(true);
		}else{
			setMulti(false);
		}
	}
	
	private IUFODoc createDoc(String name,CellsModel showModel) throws IOException{
		IUFODoc doc = new IUFODoc(name,showModel);
		if(doc!=null){
			printBeforeProcessor(doc);
		}
		return doc;
	}
    /**
	 * @i18n uiuforep00124=无法获取报表数据,请重试
	 * @i18n uiuforep00125=IUFO报表
	 */
    public IUFODoc getEditableDoc() throws IOException {
    	if (editableDoc == null) {
			TableInputTransObj inputObj = vTransObj.get(0);
			CellsModel cellsModel = loadCellsModel(inputObj);
			if (cellsModel == null) {
				throw new IOException(MultiLang.getString("uiuforep00124"));
			}
			editableDoc = createDoc(MultiLang.getString("uiuforep00125"),
					cellsModel);
		}
		return editableDoc;
	}
    
	/**
	 * @i18n uiuforep00124=无法获取报表数据,请重试
	 * @i18n uiuforep00125=IUFO报表
	 */
	public IUFODoc getDoc() throws IOException {
		IUFODoc doc = null;
		if (isMulti) {
			TableInputTransObj inputObj = vTransObj.get(pkIndex);
			CellsModel cellsModel = loadCellsModel(inputObj);
			if (cellsModel == null) {
				pkIndex = -1;
				throw new IOException(MultiLang.getString("uiuforep00124"));
			}
			doc =createDoc(MultiLang.getString("uiuforep00125")
					+ (pkIndex + 1), cellsModel);
		} else {
			doc = getEditableDoc();
		}
		
		return doc;
	}
	/**
	 * 打印前的处理器,比如注册绘制器
	 * @create by guogang at Feb 16, 2009,1:40:45 PM
	 *
	 * @param doc
	 * @throws IOException 
	 */
	protected void printBeforeProcessor(IUFODoc doc) throws IOException {
		UFOTable table = doc.getPrintData();
		if (table != null) {
			table.getReanderAndEditor().registRender(RowNumber.class,
					new RowNumberRender());
			table.getReanderAndEditor().registRender(IDName.class,
					new IDNameRender());
			table.getReanderAndEditor().registRender(String.class,
					new StringRender());
		}
	}
    /**
     * 从后台获取报表数据，并添加线程监听数据状态
     */
	public IUFOMultiDoc next() throws IOException {
		if (pkIndex+1<vTransObj.size()){
			pkIndex++;
			return this;
		}else{
			pkIndex=-1;
			return null;
		}
		
	}
	
	protected CellsModel loadCellsModel(TableInputTransObj inputObj){
		Object[] retObjs=(Object[])InputBizOper.doLinkServletTask(ITableInputMenuType.MENU_TYPE_BATCHPRINT,null,inputObj,null);
		if (retObjs.length>2 && retObjs[2]!=null && retObjs[2] instanceof Object[]){
			Object[] otherObjs=(Object[])retObjs[2];
			if (otherObjs.length>0 && otherObjs[0]!=null && otherObjs[0] instanceof Boolean){
				ReportStyle.setShowZero(((Boolean)otherObjs[0]).booleanValue());
			}
		}
		if (retObjs.length>1 && retObjs[1]!=null){
			if(retObjs[1] instanceof Object[]){
				Object[] mainObjs=(Object[])retObjs[1];
				for(int i=0;i<mainObjs.length;i++){
					if(mainObjs[i]!=null&&mainObjs[i] instanceof CellsModel){
						return (CellsModel)mainObjs[i];
					}
				}
			}
			
		}
		return null;
	}

	
	public boolean isMulti() {
		return isMulti;
	}

	public void setMulti(boolean isMulti) {
		this.isMulti = isMulti;
	}
	
}
 