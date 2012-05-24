
package nc.ui.eh.voucher.h10115;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import nc.ui.pub.ClientEnvironment;
import nc.vo.eh.voucher.h10115.PfxxVoucherVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明: 凭证导入
 * @author wb
 * 2009-9-27 13:49:49
 */
public class WriteToExcel {

    public static Workbook         w   = null;
    public static WritableWorkbook ww  = null;
    public static int rows ;
    public static PfxxVoucherVO[] vos = null;
    static ClientEnvironment ce = ClientEnvironment.getInstance();
    
    /**
     * 功能: 打开创建文件
     * @param sourceFile
     * @param newFile
     * @author chenjian
     * 2007-9-6 下午08:12:24
     */
    public static void creatFile(String sourceFile){
        try {
            /** 创建只读的Excel工作薄的对象*/
            w = Workbook.getWorkbook(new File(sourceFile));
            /** copy上面的Excel工作薄,创建新的可写入的Excel工作薄对象*/
//            ww = Workbook.createWorkbook(new File(newFile),w);
            
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能: 读取Excel中数据
     * @author:wb
     * 2009-9-27 14:10:07
     */

    @SuppressWarnings("unchecked")
    public static void readData(Object o,int x,int y,int sheetNum){
        ArrayList arr = new ArrayList();
        String pk_corp = ce.getCorporation().getPk_corp(); //公司
        Sheet ws = w.getSheet(sheetNum); 
        rows = ws.getRows(); // 行   
        
        String first = null;
        Integer nmonth;							//期间	
        String voucher_type;					//凭证类别
        String businesscode; 					// 业务号
        Integer voucher_id;						//凭证号
        String prepareddate;					//凭证日期
        String attachment_number;				//附件数
        String enter;							//制单人
        String memo;
        
        String abstractinfo;					//摘要
        String account_code;					//科目编码
        String account_name;					//科目名称
        UFDouble natural_debit_currency; 		// 本币借方金额
    	UFDouble secondary_debit_amount;		// 辅币借方金额
    	UFDouble natural_credit_currency;		// 本币贷方金额
    	UFDouble secondary_credit_amount;		// 辅币贷方金额
    	UFDouble exchange_rate1;				// 汇率
        UFDouble debit_quantity;				//借方数量
        UFDouble credit_quantity;				//贷方数量
        
        String settlement;						//结算方式
        String document_id;						//票据号
        String document_date;					//票据日期
        String currency;						//币种
        UFDouble unit_price;					//单价
        
        String itemname1;
        String itemname2;
        String itemname3;
        String itemname4;
        String itemname5;
        String itemname6;
        String itemname7;
        String itemname8;
        String itemname9;
        String itemname10;
        String itemname11;
        String itemname12;
        String itemname13;
        String itemname14;
        String itemname15;
        String itemname16;
        String itemname17;
        String itemname18;
        String itemname19;
        String itemname20;
        
        String itemcode1;
        String itemcode2;
        String itemcode3;
        String itemcode4;
        String itemcode5;
        String itemcode6;
        String itemcode7;
        String itemcode8;
        String itemcode9;
        String itemcode10;
        String itemcode11;
        String itemcode12;
        String itemcode13;
        String itemcode14;
        String itemcode15;
        String itemcode16;
        String itemcode17;	
        String itemcode18;
        String itemcode19;
        String itemcode20;
        
        UFDouble money1;
        UFDouble money2;
        UFDouble money3;
        String pk_cashflow1;
        String pk_cashflow2;
        String pk_cashflow3;
        for(int i=1;i<rows;i++){
            Cell[] cells = ws.getRow(i); 
            first = cells[0].getContents();
            if(first!=null&&first.equals("END")){
            	break;
            }
            nmonth = Integer.parseInt(cells[0].getContents()==null?null:cells[0].getContents().trim());		//期间	
            voucher_type = cells[1].getContents();														//凭证类别
            businesscode = cells[2].getContents()==null?null:cells[2].getContents().trim();	//凭证号
            prepareddate = cells[3].getContents();														//凭证日期
            attachment_number = cells[4].getContents();				//附件数
            enter = cells[5].getContents();							//制单人
            memo = cells[6].getContents();							//备注
            
            abstractinfo = cells[7].getContents();					//摘要
            account_code = cells[8].getContents();					//科目编码
            account_name = cells[9].getContents();					//科目名称
            natural_debit_currency = new UFDouble(cells[10].getContents()==null?"0":cells[10].getContents().trim());		//借方金额
            secondary_debit_amount = new UFDouble(cells[11].getContents()==null?"0":cells[11].getContents().trim());		//借方辅币金额
            natural_credit_currency = new UFDouble(cells[12].getContents()==null?"0":cells[12].getContents().trim());		//贷方金额
            secondary_credit_amount = new UFDouble(cells[13].getContents()==null?"0":cells[13].getContents().trim());		//贷方辅币金额
            exchange_rate1 = new UFDouble(cells[14].getContents()==null?"0":cells[14].getContents().trim());				//汇率
            debit_quantity = new UFDouble(cells[15].getContents()==null?"0":cells[15].getContents().trim());				//借方数量
            credit_quantity = new UFDouble(cells[16].getContents()==null?"0":cells[16].getContents().trim());				//贷方数量
            
            settlement = cells[17].getContents();						//结算方式
            document_id = cells[18].getContents();						//票据号
            document_date = cells[19].getContents();					//票据日期
            currency = cells[20].getContents();							//币种
            
            itemcode1 = cells[21].getContents();
            itemname1 = cells[22].getContents();
            itemcode2 = cells[23].getContents();
            itemname2 = cells[24].getContents();
            itemcode3 = cells[25].getContents();
            itemname3 = cells[26].getContents();
            itemcode4 = cells[27].getContents();
            itemname4 = cells[28].getContents();
            itemcode5 = cells[29].getContents();
            itemname5 = cells[30].getContents();
            itemcode6 = cells[31].getContents();
            itemname6 = cells[32].getContents();
            itemcode7 = cells[33].getContents();
            itemname7 = cells[34].getContents();
            itemcode8 = cells[35].getContents();
            itemname8 = cells[36].getContents();
            itemcode9 = cells[37].getContents();
            itemname9 = cells[38].getContents();
            
            itemcode10 = cells[39].getContents();
            itemname10 = cells[40].getContents();
            itemcode11 = cells[41].getContents();
            itemname11 = cells[42].getContents();
            itemcode12 = cells[43].getContents();
            itemname12 = cells[44].getContents();
            itemcode13 = cells[45].getContents();
            itemname13 = cells[46].getContents();
            itemcode14 = cells[47].getContents();
            itemname14 = cells[48].getContents();
            itemcode15 = cells[49].getContents();
            itemname15 = cells[50].getContents();
            itemcode16 = cells[51].getContents();
            itemname16 = cells[52].getContents();
            itemcode17 = cells[53].getContents();
            itemname17 = cells[54].getContents();
            itemcode18 = cells[55].getContents();
            itemname18 = cells[56].getContents();
            itemcode19 = cells[57].getContents();
            itemname19 = cells[58].getContents();
            itemcode20 = cells[59].getContents();
            itemname20 = cells[60].getContents();
            
            pk_cashflow1 = cells[61].getContents();
            money1 = new UFDouble(cells[62].getContents()==null?"0":cells[62].getContents().trim());
            pk_cashflow2 = cells[63].getContents();
            money2 = new UFDouble(cells[64].getContents()==null?"0":cells[64].getContents().trim());
            pk_cashflow3 = cells[65].getContents();
            money3 = new UFDouble(cells[66].getContents()==null?"0":cells[66].getContents().trim());
            
            unit_price = new UFDouble(cells[67].getContents()==null?"0":cells[67].getContents().trim());
            
            PfxxVoucherVO pvo = new PfxxVoucherVO();//存放EXCEL中的数据
            pvo.setNmonth(nmonth);
            pvo.setVoucher_type(voucher_type);
            pvo.setBusinesscode(businesscode);
            pvo.setPrepareddate(prepareddate);
            pvo.setAttachment_number(attachment_number);
            pvo.setEnter(enter);
            pvo.setMemo(memo);
            
            pvo.setAbstractinfo(abstractinfo);
            pvo.setAccount_code(account_code);
            pvo.setAccount_name(account_name);
            pvo.setNatural_debit_currency(natural_debit_currency);
            pvo.setSecondary_debit_amount(secondary_debit_amount);
            pvo.setNatural_credit_currency(natural_credit_currency);
            pvo.setSecondary_credit_amount(secondary_credit_amount);
            pvo.setExchange_rate1(exchange_rate1);
            pvo.setDebit_quantity(debit_quantity);
            pvo.setCredit_quantity(credit_quantity);
            
            pvo.setSettlement(settlement);
            pvo.setDocument_id(document_id);
            pvo.setDocument_date(document_date);
            pvo.setCurrency(currency);
            
            pvo.setItemcode1(itemcode1);
            pvo.setItemname1(itemname1);
            pvo.setItemcode2(itemcode2);
            pvo.setItemname2(itemname2);
            pvo.setItemcode3(itemcode3);
            pvo.setItemname3(itemname3);
            pvo.setItemcode4(itemcode4);
            pvo.setItemname4(itemname4);
            pvo.setItemcode5(itemcode5);
            pvo.setItemname5(itemname5);
            pvo.setItemcode6(itemcode6);
            pvo.setItemname6(itemname6);
            pvo.setItemcode7(itemcode7);
            pvo.setItemname7(itemname7);
            pvo.setItemcode8(itemcode8);
            pvo.setItemname8(itemname8);
            pvo.setItemcode9(itemcode9);
            pvo.setItemname9(itemname9);
            pvo.setItemcode10(itemcode10);
            pvo.setItemname10(itemname10);
            
            pvo.setItemcode11(itemcode11);
            pvo.setItemname11(itemname11);
            pvo.setItemcode12(itemcode12);
            pvo.setItemname12(itemname12);
            pvo.setItemcode13(itemcode13);
            pvo.setItemname13(itemname13);
            pvo.setItemcode14(itemcode14);
            pvo.setItemname14(itemname14);
            pvo.setItemcode15(itemcode15);
            pvo.setItemname15(itemname15);
            pvo.setItemcode16(itemcode16);
            pvo.setItemname16(itemname16);
            pvo.setItemcode17(itemcode17);
            pvo.setItemname17(itemname17);
            pvo.setItemcode18(itemcode18);
            pvo.setItemname18(itemname18);
            pvo.setItemcode19(itemcode19);
            pvo.setItemname19(itemname19);
            pvo.setItemcode20(itemcode20);
            pvo.setItemname20(itemname20);
            
            pvo.setPk_cashflow1(pk_cashflow1);
            pvo.setMoney1(money1);
            pvo.setPk_cashflow2(pk_cashflow2);
            pvo.setMoney2(money2);
            pvo.setPk_cashflow3(pk_cashflow3);
            pvo.setMoney3(money3);
            
            pvo.setUnit_price(unit_price);
            pvo.setPk_corp(pk_corp);
            
            arr.add(pvo);              
        }
        if(arr.size()>0){
            vos = (PfxxVoucherVO[])arr.toArray(new PfxxVoucherVO[arr.size()]);
        }     
    }
    
}
