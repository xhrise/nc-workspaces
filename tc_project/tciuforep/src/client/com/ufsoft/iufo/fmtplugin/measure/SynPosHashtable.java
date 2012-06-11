///*
// * Created by caijie  on  2005-12-13
// *   
// */
//package com.ufsoft.iufo.fmtplugin.measure;
//
//import java.util.Enumeration;
//import java.util.EventObject;
//import java.util.Hashtable;
//
//import javax.swing.event.ChangeEvent;
//
//
//import com.ufsoft.table.CellPosition;
//import com.ufsoft.table.CellsModel;
//import com.ufsoft.table.ForbidedOprException;
//import com.ufsoft.table.header.HeaderEvent;
//import com.ufsoft.table.header.HeaderModelListener;
//
///**
// * 位置同步hashTable
// * 由于CellsModel中的单元位置随行列以及单元的增删改而变动，
// * 故通过该类实现位置key与CellsModel中单元位置的同步变化
// * 要求key-value结构中必须为key: CellsPosition ; value: Object
// * 当key不为CellsPosition时,该key-value对与CellsModel的同步将忽略
// * @author caijie 
// */
//public class SynPosHashtable extends Hashtable implements HeaderModelListener{
//
//    //需要同步的CellsModel    
//    private CellsModel cellsModel = null;
//    
//    public SynPosHashtable(){
//        super();
//    }
//    
//    /**
//     * @return Returns the cellsModel.
//     */
//    public CellsModel getCellsModel() {
//        return cellsModel;
//    }
//    /**
//     * 注册到CellsModel,监听CellsModel中行列单元变化事件
//     */
//    public void setCellsModel(CellsModel cellsModel) {
//        if(cellsModel == null) return;
//        
//        CellsModel old = this.getCellsModel();
//        
//        this.cellsModel = cellsModel;        
//        this.getCellsModel().getRowHeaderModel().addHeaderModelListener(this);
//        
//        try {
//            this.getCellsModel().getRowHeaderModel().addHeaderModelListener(this);
//        } catch (Exception e) {            
//        }
//        
//    }
//    /***
//     * 
//     * 将CellsModel中的指标标记以该hasttable为准重新整理
//     * 如果清理中出现错误，则返回ｆａｌｓｅ，否则返回ｔｒｕｅ
//     */
//    public boolean synchronizeCellsModel(){
//        Hashtable ori = this.getCellsModel().getBsFormats(MeasureDefinePlugIn.EXT_FMT_MEASURE);
//        CellPosition pos = null;
//        for(Enumeration enumer = ori.keys();enumer.hasMoreElements();){
//            pos = (CellPosition) enumer.nextElement();                
//            if(this.get(pos) == null){
//                this.getCellsModel().setBsFormat(pos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.FALSE);
//            }
//        }  
//        return true;
//    }
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.header.HeaderModelListener#addHeader(com.ufsoft.table.header.HeaderEvent)
//     */
//    public void addHeader(HeaderEvent e) {
//        if(e == null) return;
//       
//        int start = e.getStartIndex();
//        int count = e.getCount();
//        
//        //思路：将修改的和不修改的都倒入一个临时Hashtable，然后重新装载。
//        Hashtable tmp = new Hashtable(this);  
//        this.clear();
//        CellPosition oldPos = null;
//        CellPosition newPos = null;  
//        if(e.getType() == 0){        
//            for(Enumeration enumer = tmp.keys();enumer.hasMoreElements();){
//                oldPos = (CellPosition) enumer.nextElement();               
//                if(oldPos.getRow() >= start){
//                    newPos = CellPosition.getInstance(oldPos.getRow() + count, oldPos.getColumn());
//                    this.put(newPos, tmp.get(oldPos));
//                    this.getCellsModel().setBsFormat(oldPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.FALSE);
//                    this.getCellsModel().setBsFormat(newPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.TRUE);
//                }else{
//                    this.put(oldPos, tmp.get(oldPos));
//                }
//            }  
//        }else{         
//            for(Enumeration enumer = tmp.keys();enumer.hasMoreElements();){
//                oldPos = (CellPosition) enumer.nextElement();                
//                if(oldPos.getColumn()>= start){
//                    newPos = CellPosition.getInstance(oldPos.getRow(), oldPos.getColumn() + count);
//                    this.put(newPos, tmp.get(oldPos));
//                    this.getCellsModel().setBsFormat(oldPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.FALSE);
//                    this.getCellsModel().setBsFormat(newPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.TRUE);
//                }else{
//                    this.put(oldPos, tmp.get(oldPos));
//                }
//            }  
//        }       
//        synchronizeCellsModel();
//    }
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.header.HeaderModelListener#removeHeader(com.ufsoft.table.header.HeaderEvent)
//     */
//    public void removeHeader(HeaderEvent e) {
//
//        if(e == null) return;
//       
//        int count = e.getCount();
//        int start = e.getStartIndex();
//        int end = start + count;
//        
//        //思路：将修改的和不修改的都倒入一个临时Hashtable，然后重新装载。
//        Hashtable tmp = new Hashtable(this);  
//        this.clear();
//        CellPosition oldPos = null;
//        CellPosition newPos = null;  
//        if(e.getType() == 0){        
//            for(Enumeration enumer = tmp.keys();enumer.hasMoreElements();){
//                oldPos = (CellPosition) enumer.nextElement();               
//                if(oldPos.getRow() >= end){
//                    newPos = CellPosition.getInstance(oldPos.getRow() - count, oldPos.getColumn());                    
//                    this.put(newPos, tmp.get(oldPos));
//                    this.getCellsModel().setBsFormat(oldPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.FALSE);
//                    this.getCellsModel().setBsFormat(newPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.TRUE);
//                }else if (oldPos.getRow()< start){
//                    this.put(oldPos, tmp.get(oldPos));
//                }
//            }  
//        }else{         
//            for(Enumeration enumer = tmp.keys();enumer.hasMoreElements();){
//                oldPos = (CellPosition) enumer.nextElement();                
//                if(oldPos.getColumn()>= end){
//                    newPos = CellPosition.getInstance(oldPos.getRow(), oldPos.getColumn() + count);
//                    this.put(newPos, tmp.get(oldPos));
//                    this.getCellsModel().setBsFormat(oldPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.FALSE);
//                    this.getCellsModel().setBsFormat(newPos, MeasureDefinePlugIn.EXT_FMT_MEASURE, Boolean.TRUE);
//                }else if (oldPos.getColumn()< start){
//                    this.put(oldPos, tmp.get(oldPos));
//                }
//            }  
//        }  
//        synchronizeCellsModel();
//    }
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.header.HeaderModelListener#moveHeader(com.ufsoft.table.header.HeaderEvent)
//     */
//    public void moveHeader(HeaderEvent e) {
//        throw new IllegalArgumentException("目前还不支持行列移动");
//        
//    }   
//    
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.header.HeaderModelListener#headerMarginChanged(javax.swing.event.ChangeEvent)
//     */
//    public void headerMarginChanged(ChangeEvent e) {        
//    }
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.header.HeaderModelListener#formatChanged(com.ufsoft.table.header.HeaderEvent)
//     */
//    public void formatChanged(HeaderEvent e) {        
//    }
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.header.HeaderModelListener#contentChanged(com.ufsoft.table.header.HeaderEvent)
//     */
//    public void contentChanged(HeaderEvent e) {        
//    }
//    /* (non-Javadoc)
//     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
//     */
//    public String isSupport(int source, EventObject e) throws ForbidedOprException {
//        // TODO Auto-generated method stub
//        return null;
//    }
//}
