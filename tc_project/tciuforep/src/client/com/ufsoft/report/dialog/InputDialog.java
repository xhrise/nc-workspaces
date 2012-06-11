/**  
 * 创建时间2004-8-1015:33:01
 * @author CaiJie 
 */
package com.ufsoft.report.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import com.ufsoft.report.util.MultiLang;

/**
 * 输入文本对话框,并可以对输入的字符串进行验证.
 */
public class InputDialog extends BaseDialog{
    /**
     * 完整输入项数组
     */
    private ArrayList m_arrInputItems = new ArrayList();  
    
    private GridBagLayout m_gridBagLayout = null;
    
    private GridBagConstraints  m_gridBagConstraints = null;
    /**
     * 已经添加的输入项计数器
     */
    private int m_iItemNumber = 0;
    
    /**
     * 构造函数
     */
    public InputDialog(Component parentComponent, String title, boolean modal){
        super(parentComponent, title, modal);
        initialization();  
    }
    
    /**
     *添加一个包含输入标签、验证子的完整输入项
     * 创建时间2004-8-17  14:27:17
     * @param item InputItem - 输入项
     */
    public void addInputItem(InputItem item){        
        if(item == null) 
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000724"));//错误的输入项
        
        m_arrInputItems.add(item);                
        //设置布局条件
        m_gridBagConstraints.anchor =  GridBagConstraints.WEST;
        m_gridBagConstraints.insets = new Insets(0,0,0,0);
        //添加标签
        m_gridBagConstraints.gridwidth = 1;
        super.getDialogArea().add(item.getLabel());
        //添加输入域
        m_gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        super.getDialogArea().add(item.getInputField());

        m_iItemNumber++;
    }
    
    /**
     * 获取指定输入项的项值
     * 创建时间2004-8-17  14:33:33
     * @param name - 输入项名称
     * @return - 输入标签的项值，无该标签则返回null
     */
    public String getText(String name) {
        if (name == null)
            return null;
       
        for(int i = 0; i < m_arrInputItems.size(); i++){
            InputItem item = (InputItem)(m_arrInputItems.get(i));
            if ((item.getName()).equals(name)){
                return item.getText();
            }
        }
         return null;
    }
    
    /**
     * 响应ok按钮的OK消息，用检测子检测每个输入项,
     * 如果所有的输入都满足检测子,则关闭对话框.
     */
    protected void finishDialog() {      
      String inputString = null;
      InputItem item = null; 
      IInputValidator validator = null;
      for(int i = 0; i < m_arrInputItems.size(); i++){
          item = (InputItem) m_arrInputItems.get(i); 
          if (item != null){
              validator = item.getValidator();
              if (validator == null) continue;//没有检测子
              
              inputString = item.getText();              
              if (!validator.isValid(inputString)){
                  return ; //无法通过检测子，返回
              }
          }
          
      }
      //设置用户的选择
      setSelectOption(OK_OPTION);
      
      //关闭对话框
      dispose();
    }
    
    /**
     * 初始化对话框
     * 在对话框内容区域添加一个文本输入域
     */
    private void initialization(){
        //网袋布局
        m_gridBagLayout  = new GridBagLayout();
        m_gridBagConstraints = new GridBagConstraints();        
        super.getDialogArea().setLayout(m_gridBagLayout);
        
        this.setTitle(MultiLang.getString("uiuforep0000725"));//输入
        this.setModal(true);        
        this.setResizable(false);
    }   
    

    
    
}
