package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Container;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * 
 * @author wangyga
 * @created at 2009-3-12,ÉÏÎç09:17:16
 *
 */
public class DynAreaKeyMngEditorAction extends AbsEditorAction{

	DynAreaKeyMngEditorAction(Container contain){
		super(contain);
	}

	@Override
	public void execute(Object[] params) {
    	if(params == null) return;
      DynAreaVO dynAreaVO = (DynAreaVO) params[0];
      CellPosition cellPos = (CellPosition) params[1];
      KeyVO keyVO = (KeyVO) params[2];
      new DynAreaKeyMngDlg(getParent(), getCellsModel(),getContextVo(),dynAreaVO,cellPos, keyVO).setVisible(true);
		
	}

	@Override
	public Object[] getParams() {
	    CellsModel cellsModel = getCellsModel();
        CellPosition anchorPos = cellsModel.getSelectModel().getAnchorCell();
        DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);        
        DynAreaVO dynAreaVO = dynAreaModel.getDynAreaCellByPos(anchorPos).getDynAreaVO();
        KeyVO keyVO = dynAreaModel.getKeywordModel().getKeyVOByPos(anchorPos);
        if(MeasureModel.getInstance(cellsModel).getMeasureVOByPos(anchorPos)!=null){
        	UfoPublic.sendWarningMessage(StringResource.getStringResource("uiiufofmt00054"), getParent());
        	return null;
        }
        if(cellsModel!=null && anchorPos!=null){
        	Cell cellTemp=cellsModel.getCell(anchorPos);
        	if(cellTemp!=null && cellTemp.getValue()!=null && "".equals(cellTemp.getValue())==false){
        		UfoPublic.sendWarningMessage(StringResource.getStringResource("uiiufofmt00055"), getParent());
        		return null;
        	}
        }       
        return new Object[]{dynAreaVO,anchorPos,keyVO};
	}
}
