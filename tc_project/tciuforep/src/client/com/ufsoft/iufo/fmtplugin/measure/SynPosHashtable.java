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
// * λ��ͬ��hashTable
// * ����CellsModel�еĵ�Ԫλ���������Լ���Ԫ����ɾ�Ķ��䶯��
// * ��ͨ������ʵ��λ��key��CellsModel�е�Ԫλ�õ�ͬ���仯
// * Ҫ��key-value�ṹ�б���Ϊkey: CellsPosition ; value: Object
// * ��key��ΪCellsPositionʱ,��key-value����CellsModel��ͬ��������
// * @author caijie 
// */
//public class SynPosHashtable extends Hashtable implements HeaderModelListener{
//
//    //��Ҫͬ����CellsModel    
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
//     * ע�ᵽCellsModel,����CellsModel�����е�Ԫ�仯�¼�
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
//     * ��CellsModel�е�ָ�����Ը�hasttableΪ׼��������
//     * ��������г��ִ����򷵻أ����壬���򷵻أ������
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
//        //˼·�����޸ĵĺͲ��޸ĵĶ�����һ����ʱHashtable��Ȼ������װ�ء�
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
//        //˼·�����޸ĵĺͲ��޸ĵĶ�����һ����ʱHashtable��Ȼ������װ�ء�
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
//        throw new IllegalArgumentException("Ŀǰ����֧�������ƶ�");
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
