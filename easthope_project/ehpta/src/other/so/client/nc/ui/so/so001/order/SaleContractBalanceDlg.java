package nc.ui.so.so001.order;

import java.awt.Container;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.pub.beans.UIDialog;
import nc.vo.pub.lang.UFDouble;

@SuppressWarnings({"rawtypes" , "unchecked" })
public class SaleContractBalanceDlg extends UIDialog{

	private static final long serialVersionUID=1L;

	private JScrollPane jScrollPane=null;

	private JTable jTable=null;

	private JButton jButton=null;

	private String pk_contract=null;
	
	private String pk_custdoc=null;
	
	private String csaleid=null;
	
	private String concode = null;
	
	private String[] sqlString = null;
	
	private Container parent = null;

	public double doublevalue(Object obj){
		if(obj==null||obj.toString().equals("")){
			return 0;
		}else{
			return Double.parseDouble(obj.toString());
		}
	}

	public SaleContractBalanceDlg(Container parent , Object[] objs , String[] sqlString){
		super(parent);
		this.parent = parent;
		if(objs.length==4){
			this.pk_contract=objs[0].toString();
			this.pk_custdoc=objs[1].toString();
			this.csaleid=objs[2].toString();
			this.concode = objs[3].toString();
			this.sqlString = sqlString;
		}
		try{
			initialize();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws Exception
	 */
	private void initialize() throws Exception{
		this.setLayout(null);
		this.setSize(300,380);
		this.setTitle("合同"+concode+"金额信息统计：");
		this.add(getJScrollPane(),null);
		this.add(getJButton(),null);
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 * @throws Exception
	 */
	private JScrollPane getJScrollPane() throws Exception {
		if(jScrollPane==null){
			if(jTable==null){
				String[] titles=new String[]{"类型","金额"};
				
				Vector retVector = (Vector) UAPQueryBS.getInstance().executeQuery(sqlString[0], new VectorProcessor());
				
				Vector addVector = new Vector();
				if(sqlString.length > 1)
					addVector = (Vector) UAPQueryBS.getInstance().executeQuery(sqlString[1], new VectorProcessor());
				
				if(retVector == null || retVector.size() == 0) {
					this.close();
					this.dispose();
					
					if(parent instanceof ExtSaleOrderAdminUI)
						((ExtSaleOrderAdminUI)parent).showErrorMessage("该合同没有相关金额项");
					
				}
				
				List<Object[]> retObject = new ArrayList<Object[]>();
				UFDouble mny = new UFDouble("0" , 2);
				if(retVector != null && retVector.size() > 0) {
					for(int i = 0 , j = retVector.size() ; i < j ; i ++) {
						Object[] arrObj = new Object[]{ ((Vector)retVector.get(i)).get(0) , new UFDouble(((Vector)retVector.get(i)).get(1).toString() , 2) } ;
						UFDouble nowMny = new UFDouble(arrObj[1].toString());
						if(nowMny.doubleValue() == 0 )
							continue;
						retObject.add(arrObj);
						mny = mny.add(nowMny);
					}
				}
				
				if(addVector != null && addVector.size() > 0) {
					for(int i = 0 , j = addVector.size() ; i < j ; i ++) {
						Object[] arrObj = new Object[]{ ((Vector)addVector.get(i)).get(0) , new UFDouble(((Vector)addVector.get(i)).get(1).toString() , 2) } ;
						UFDouble addMny = new UFDouble(arrObj[1].toString());
						if(addMny.doubleValue() == 0 )
							continue;
						retObject.add(arrObj);
						mny = mny.add(addMny);
					}
				}
				
				retObject.add(new Object[]{"合同余额" , new UFDouble(mny.toString() , 2)});
					
				for(Object[] objs : retObject) {
					
					if(objs[0].toString().length() >= 3 && "已使用".equals(objs[0].toString().substring(0,3) )) {
						
						objs[1] = new UFDouble(new UFDouble(objs[1].toString()).multiply(-1).doubleValue() , 2);
					
					} else if("累计开票额".equals(objs[0]) || "已提货金额".equals(objs[0]) || "当前提货金额".equals(objs[0]) || "当前开票金额".equals(objs[0])) {
						objs[1] = new UFDouble(new UFDouble(objs[1].toString()).multiply(-1).doubleValue() , 2);
					}
					
					
					
				}
				
				DefaultTableModel model=new DefaultTableModel(retObject.toArray(new Object[0][]) ,titles);
				jTable=new JTable(model) {
					@Override
					public boolean editCellAt(int i, int j) {
						return false;
					}
				};
				jTable.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer(SwingConstants.LEFT));
				jTable.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer(SwingConstants.RIGHT));
				jTable.getTableHeader().setSize(0,30);
				jTable.setRowHeight(20);
				jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				jTable.getColumn(titles[0]).setPreferredWidth(80);
				jTable.getColumn(titles[1]).setPreferredWidth(120);
				jScrollPane=new JScrollPane(jTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				jScrollPane.setViewportView(jTable);
				jScrollPane.setBounds(new Rectangle(29,39,235,270));
			}
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton(){
		if(jButton==null){
			jButton=new JButton();
			jButton.setBounds(new Rectangle(75,320,145,25));
			jButton.setText("关闭");
			jButton.addMouseListener(new java.awt.event.MouseAdapter(){
				public void mouseClicked(java.awt.event.MouseEvent e){
					dispose(); // mouseClicked()
				}
			});
		}
		return jButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"

