package com.ufsoft.iufo.fmtplugin.key;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JOptionPane;

import nc.ui.pub.beans.UIPanel;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
	 * params[0]  �¹ؼ��ֵ�Vector
	 * ���������¹ؼ��ֲ�����Ϊnull�������κιؼ���,��ɾ�����еĹؼ���
	 * @author caijie 
	 * @since 3.1
	 */
	public class KeywordSetCmd extends UfoCommand implements IUfoContextKey{	  
		private UfoReport _report;

        /**
         * @param _report
         */
        KeywordSetCmd(UfoReport report) {
        	_report = report;
        }

        public void execute(Object[] params) {
        	if(params == null){
        		return;
        	}
            HashMap newKeyVOPos = (HashMap)params[0];
            if(newKeyVOPos == null){
            	newKeyVOPos = new HashMap();
            }
            //�޸�ǰԭ���Ĺؼ�����ϡ�
            KeyGroupVO oldKeygroup = new KeyGroupVO();
            KeywordModel keywordModel = KeywordModel.getInstance(getCellsModel());
            HashMap allKeyVO = keywordModel.getMainKeyVOPos();
            if(allKeyVO != null){
                KeyVO[] keys =  (KeyVO[]) allKeyVO.keySet().toArray(new KeyVO[allKeyVO.size()]);                
                oldKeygroup.addKeyToGroup(keys);
            }
            
            boolean bHaveChangeKeys=false;//����Ƿ�����˹ؼ��ּ��ϣ�����������λ��
            
            //����У��ʱ��ؼ��ַ����仯�����
            int oldTimeKeyVOIndex = oldKeygroup.getTimeKey() == null ? -1 : oldKeygroup.getTimeKey().getTimeKeyIndex();
            //�û�ѡ��ɾ��
            boolean allowDelete = false;                
            int newTimeKeyIndex = -1;
            for(Iterator iter = newKeyVOPos.keySet().iterator();iter.hasNext();){
                KeyVO keyVO = (KeyVO) iter.next();                
                if(keyVO.getTimeKeyIndex() != -1 && oldTimeKeyVOIndex != -1 &&
                        keyVO.getTimeKeyIndex() != oldTimeKeyVOIndex){
                	
                	bHaveChangeKeys=true;
                	
                    String sHint = StringResource.getStringResource("miufo1001722");  //"ʱ��ؼ����޸Ļᵼ�±��������ݱ�ɾ����ͬʱ���������ָ�����ã�"
                    int j = JOptionPane.showConfirmDialog(new UIPanel(), sHint, StringResource.getStringResource("miufo1000718"), JOptionPane.YES_NO_OPTION,  //"���Ѽ���"
                        JOptionPane.WARNING_MESSAGE);
                    if(j==JOptionPane.YES_OPTION){ 
                        allowDelete = true;
                        newTimeKeyIndex = keyVO.getTimeKeyIndex();
                    }else{
                        return;
                    }
                }
            }
            DynAreaCell[] dynCells = DynAreaModel.getInstance(getCellsModel()).getDynAreaCells();
            KeywordModel keyModel = KeywordModel.getInstance(getCellsModel());
            for(int m = 0; m < dynCells.length; m++){
                Hashtable dynKeys = keyModel.getKeyVOByArea(dynCells[m].getArea());
                if(dynKeys.size() + newKeyVOPos.size() > KeyGroupVO.MaxCount){
                    String errorMsg = StringResource.getStringResource("miufo1001723",new String[]{dynCells[m].getArea().toString()});
                    UfoPublic.sendWarningMessage(errorMsg,null);
                    return;
                }
                if(newTimeKeyIndex != -1){
                    for(Iterator iter = dynKeys.values().iterator(); iter.hasNext();){
                        KeyVO dynKey = (KeyVO) iter.next();
                        //��������޸�ʱ��ؼ��ֺ󲻰����ӱ��ʱ��ؼ��֣�����ʾ�û������޸�
                        if(dynKey.getTimeKeyIndex() != -1 && dynKey.getTimeKeyIndex() < newTimeKeyIndex){
                            String errorMsg = StringResource.getStringResource("miufo1001724",new String[]{dynCells[m].getArea().toString()});
                            UfoPublic.sendWarningMessage(errorMsg,null);
                            return;
                        }
                    }
                }
            }
            int oriLen = oldKeygroup.getSize();
            KeyVO[] newKeyVOs = (KeyVO[]) newKeyVOPos.keySet().toArray(new KeyVO[0]);
            oldKeygroup.addKeyToGroup(newKeyVOs);


            if(oldKeygroup.getSize() != oriLen || newKeyVOs.length != oriLen  )
            	bHaveChangeKeys=true;
            
            if( (oldKeygroup.getSize() != oriLen && oriLen != 0) || (newKeyVOs.length != oriLen && oriLen != 0)){
                //����û��Ѿ���ʱ��ؼ��ַ����仯��ѡ��ɾ��ָ�����ã������ٴ���ʾ�û�
                if(!allowDelete){
                    String sHint = StringResource.getStringResource("miufo1001728");  //"�ؼ����޸Ļᵼ�±��������ݱ�ɾ����ͬʱ���������ָ�����ã�"
                    int j = JOptionPane.showConfirmDialog(new UIPanel(), sHint, StringResource.getStringResource("miufo1000718"), JOptionPane.YES_NO_OPTION,  //"���Ѽ���"
                        JOptionPane.WARNING_MESSAGE);
                    if(j == JOptionPane.YES_OPTION){
                    	allowDelete=true;
                    } else{ 
                    	return;
                    }
                }
            }
            if(!isHasMeasure(newKeyVOPos.values())){
            	if(allowDelete){
//            		���ָ������,�������ʱ���ٽ��б����������
            		 clearRefMeasures();
            	}
            		
            	getKeyModel().resetMainKeyVOPos(newKeyVOPos);
            	KeyGroupVO keyGroupVO = new KeyGroupVO();
            	keyGroupVO.addKeyToGroup(newKeyVOs);
            	//TODO ��ҪУ�鵱keyGroupTemp==nullʱ�Ƿ�Ӱ���Ժ����̡�
            	KeyGroupVO keyGroupTemp=CacheProxy.getSingleton().getKeyGroupCache().getPkByKeyGroup(keyGroupVO);
            	if(keyGroupTemp!=null){
                	getKeyModel().setMainKeyCombPK(keyGroupTemp.getKeyGroupPK());
            	}
          
            	  //TODO ��Ҫȷ��bHaveChangeKeys�Ƿ�������ȷ
                if(bHaveChangeKeys==true){
                	FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());

//    	            FormulaDefPlugin formPlugIn=(FormulaDefPlugin) _report.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//    	            if(formPlugIn!=null)
    	            	formulaModel.getUfoFmlExecutor().resetMainKeyVos();
                }
            }        
            
          
        }
        /**
		 * @i18n uiiufofmt00051=�ؼ���λ����ָ��λ���ظ�
		 */
        private boolean isHasMeasure(Collection poss) {
			for (Iterator iter = poss.iterator(); iter.hasNext();) {
				CellPosition pos = (CellPosition) iter.next();
				if(pos==null)
					continue;
				if(getMeasureModel().getMeasureVOByPos(pos) != null){
					com.ufsoft.report.util.UfoPublic.sendMessage(StringResource.getStringResource("uiiufofmt00051"), _report);
					return true;
				}
			}
			return false;
		}

		private KeywordModel getKeyModel() {
			return KeywordModel.getInstance(getCellsModel());
		}
		private MeasureModel getMeasureModel() {
			return MeasureModel.getInstance(getCellsModel());
		}
		private CellsModel getCellsModel(){
        	return _report.getCellsModel();
        }
        private void clearRefMeasures() {
        	Hashtable posVOs = getMeasureModel().getMeasureVOPosByAll();
        	for(Enumeration enumeration = posVOs.keys();enumeration.hasMoreElements();){
        		CellPosition cellPos = (CellPosition)enumeration.nextElement();
        		MeasureVO measureVO = (MeasureVO) posVOs.get(cellPos);
        		String strRepId = _report.getContextVo().getAttribute(REPORT_PK) == null ? null : (String)_report.getContextVo().getAttribute(REPORT_PK);
        		
        		if(!measureVO.getReportPK().equals(strRepId)){
        			getMeasureModel().removeMeasureVOByPos(cellPos);
        		}
        	}
        }
	} 