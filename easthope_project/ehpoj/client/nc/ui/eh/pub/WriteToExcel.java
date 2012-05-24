/*
 * Classname: 
 * Version information: 
 * Creator: chenjian
 * Create Date: 2007-3-21下午06:00:23
 * Copyright notice: 支持打击盗版,强烈鄙视盗版
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
 * 功能说明: 
 * @author chenjian
 * 2007-9-6 下午06:12:21
 */
public class WriteToExcel {

    /***/
    public static Workbook         w               = null;
    public static WritableWorkbook ww              = null;
    
    /**
     * 功能: 打开创建文件
     * @param sourceFile
     * @param newFile
     * @author chenjian
     * 2007-9-6 下午08:12:24
     */
    public static void creatFile(String sourceFile,String newFile){
        try {
            /** 创建只读的Excel工作薄的对象*/
            w = Workbook.getWorkbook(new File(sourceFile));
            /** copy上面的Excel工作薄,创建新的可写入的Excel工作薄对象*/
            ww = Workbook.createWorkbook(new File(newFile),w);
            
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 功能: 关闭文件
     * @author chenjian
     * 2007-9-6 下午08:12:09
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
     * 功能: 
     * @param obj
     * @param row 代表X轴坐标
     * @param col 代表Y轴坐标
     * @param sheetNum 工作表索引,0为第一个工作表
     * @throws Exception
     * @author chenjian
     * 2008-3-6 下午03:56:58
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
     * 功能: 
     * @param o 传一个object对象,用于比较
     * @param x X轴坐标
     * @param y Y轴坐标
     * @param sheetNum 工作表索引,0为第一个工作表
     * @param 列
     * @return 根据坐标所取的值与传过来的值进行比较,相同return 1,不相同return 0;
     * @author chenjian
     * 2008-3-6 下午03:51:53
     */
    public static int readData(Object o,String[] colums,int x,int y,int sheetNum){
        WritableSheet ws = ww.getSheet(sheetNum);
        int rows = ws.getRows();
        while(y<rows){
            Cell[] cells = ws.getRow(y);//取Excel的某一行,其中,y表示Y轴坐标,代表某一行,从0开始
            Cell cell = cells[x];//取某一行的某一列数据,其中,x表示X轴坐标,代表某一列,从0开始
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
     * 功能: 
     * @param col X轴数值
     * @param row Y轴数值
     * @param sourceCell 原Excel单元格
     * @param value 要写入数据
     * @return WritableCell
     * @author chenjian
     * 2007-9-6 下午06:17:33
     */
    public static WritableCell cloneCellWithValue(int col, int row, Cell sourceCell, Object value) {
        WritableCell wc = null;
        // 获得源单元格的格式,类型，及内容
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
        
        // 根据源单元格的格式，生成完全一致的目标单元格
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
