/*
 * Classname: 
 * Version information: 
 * Creator: chenjian
 * Create Date: 2007-3-21����06:00:23
 * Copyright notice: ֧�ִ������,ǿ�ұ��ӵ���
 */
package nc.ui.eh.pub;

import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * ����˵��: 
 * @author chenjian
 * 2007-9-6 ����06:12:21
 */
public class WriteToExcel {

    /***/
    public static Workbook         w               = null;
    public static WritableWorkbook ww              = null;
    
    /**
     * ����: �򿪴����ļ�
     * @param sourceFile
     * @param newFile
     * @author chenjian
     * 2007-9-6 ����08:12:24
     */
    public static void creatFile(String sourceFile,String newFile){
        try {
            /** ����ֻ����Excel�������Ķ���*/
            w = Workbook.getWorkbook(new File(sourceFile));
            /** copy�����Excel������,�����µĿ�д���Excel����������*/
            ww = Workbook.createWorkbook(new File(newFile),w);
            
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * ����: �ر��ļ�
     * @author chenjian
     * 2007-9-6 ����08:12:09
     */
    public static void closeFile() {
        try {
            if (ww != null) {
                ww.write();
                ww.close();
            }
            if (w != null) {
                w.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����: 
     * @param obj
     * @param row ����X������
     * @param col ����Y������
     * @param sheetNum ����������,0Ϊ��һ��������
     * @throws Exception
     * @author chenjian
     * 2008-3-6 ����03:56:58
     */
    public static void writeData(Object[][] obj,int row,int col,int sheetNum) throws Exception{
        WritableSheet ws = ww.getSheet(sheetNum);
        
        for (int i = 0; i < obj.length; i++) {
            for (int j = 0; j < obj[0].length; j++) {
                WritableCell wc = null;

                wc = ws.getWritableCell(j + row, i + col);
                wc = ws.getWritableCell(j + row, i + col);
                wc = cloneCellWithValue(j + row, i + col, wc, obj[i][j]);
                if (wc == null) {
                    continue;
                }
                ws.addCell(wc);
            }
        }
    }
    /**
     * ����: 
     * @param o ��һ��object����,���ڱȽ�
     * @param x X������
     * @param y Y������
     * @param sheetNum ����������,0Ϊ��һ��������
     * @param ��
     * @return ����������ȡ��ֵ�봫������ֵ���бȽ�,��ͬreturn 1,����ͬreturn 0;
     * @author chenjian
     * 2008-3-6 ����03:51:53
     */
    public static int readData(Object o,String[] colums,int x,int y,int sheetNum){
        WritableSheet ws = ww.getSheet(sheetNum);
        int rows = ws.getRows();
        while(y<rows){
            Cell[] cells = ws.getRow(y);//ȡExcel��ĳһ��,����,y��ʾY������,����ĳһ��,��0��ʼ
            Cell cell = cells[x];//ȡĳһ�е�ĳһ������,����,x��ʾX������,����ĳһ��,��0��ʼ
            String cellValue = cell.getContents();
            System.out.println(cell.getContents()+"------------------------cjcjcjcjcjcjcjcjjcjcjcjcjcjjcjcjcjjcjc");
            if(cellValue.endsWith(o.toString())){
                return y;
            }
            else
                y = y+13;
        }
        return 0;
    }

    /**
     * ����: 
     * @param col X����ֵ
     * @param row Y����ֵ
     * @param sourceCell ԭExcel��Ԫ��
     * @param value Ҫд������
     * @return WritableCell
     * @author chenjian
     * 2007-9-6 ����06:17:33
     */
    public static WritableCell cloneCellWithValue(int col, int row, Cell sourceCell, Object value) {
        WritableCell wc = null;
        // ���Դ��Ԫ��ĸ�ʽ,���ͣ�������
        CellFormat cFormat = sourceCell.getCellFormat();
        WritableCellFormat wcFormat = null;
        if(cFormat!=null){
            wcFormat = new WritableCellFormat(cFormat);
        }else{
            wcFormat = new WritableCellFormat();
        }
        CellType cType = sourceCell.getType();
        if (value == null) {
            wc = new jxl.write.Blank(col, row, wcFormat);
            return wc;
        }else{
//            value = new Double(value.toString());
            if(value instanceof String)
                value=new String(value.toString());
            else {
                value=new Double(value.toString());
                double values=Double.parseDouble(value.toString());
                if(values==0){
//                            (value.toString().split("\\.")[0]+value.toString().split("\\.")[1].substring(0,1)).equals("0.0")){
                    return null;
                }
            }
        }
        
        // ����Դ��Ԫ��ĸ�ʽ��������ȫһ�µ�Ŀ�굥Ԫ��
        if (cType == CellType.EMPTY) {
            // wc=new
            // jxl.write.Number(col,row,((Double)value).doubleValue(),wcFormat);
            if (value instanceof String) {
                wc = new jxl.write.Label(col, row, value.toString(), wcFormat);
            } else {
                wc = new jxl.write.Number(col, row, ((Double) value).doubleValue(), wcFormat);
            }
        } else if (cType == CellType.LABEL) {
            jxl.LabelCell sl = (jxl.LabelCell) sourceCell;
            wc = new jxl.write.Label(col, row, sl.getContents(), wcFormat);
        } else if (cType == CellType.NUMBER) {
            jxl.NumberCell nc = (jxl.NumberCell) sourceCell;
            wc = new jxl.write.Number(col, row, nc.getValue(), wcFormat);
        } else if (cType == CellType.DATE) {
            jxl.DateCell sd = (jxl.DateCell) sourceCell;
            wc = new jxl.write.DateTime(col, row, sd.getDate(), wcFormat);
        } else if (cType == CellType.BOOLEAN) {
            jxl.BooleanCell sb = (jxl.BooleanCell) sourceCell;
            wc = new jxl.write.Boolean(col, row, sb.getValue(), wcFormat);
        } else if (cType == CellType.ERROR) {
            wc = new jxl.write.Blank(col, row, wcFormat);
        } else {


        }
        return wc;
    }
}
