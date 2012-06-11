/**  
 * ����ʱ��2004-8-1015:33:01
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
 * �����ı��Ի���,�����Զ�������ַ���������֤.
 */
public class InputDialog extends BaseDialog{
    /**
     * ��������������
     */
    private ArrayList m_arrInputItems = new ArrayList();  
    
    private GridBagLayout m_gridBagLayout = null;
    
    private GridBagConstraints  m_gridBagConstraints = null;
    /**
     * �Ѿ���ӵ������������
     */
    private int m_iItemNumber = 0;
    
    /**
     * ���캯��
     */
    public InputDialog(Component parentComponent, String title, boolean modal){
        super(parentComponent, title, modal);
        initialization();  
    }
    
    /**
     *���һ�����������ǩ����֤�ӵ�����������
     * ����ʱ��2004-8-17  14:27:17
     * @param item InputItem - ������
     */
    public void addInputItem(InputItem item){        
        if(item == null) 
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000724"));//�����������
        
        m_arrInputItems.add(item);                
        //���ò�������
        m_gridBagConstraints.anchor =  GridBagConstraints.WEST;
        m_gridBagConstraints.insets = new Insets(0,0,0,0);
        //��ӱ�ǩ
        m_gridBagConstraints.gridwidth = 1;
        super.getDialogArea().add(item.getLabel());
        //���������
        m_gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        super.getDialogArea().add(item.getInputField());

        m_iItemNumber++;
    }
    
    /**
     * ��ȡָ�����������ֵ
     * ����ʱ��2004-8-17  14:33:33
     * @param name - ����������
     * @return - �����ǩ����ֵ���޸ñ�ǩ�򷵻�null
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
     * ��Ӧok��ť��OK��Ϣ���ü���Ӽ��ÿ��������,
     * ������е����붼��������,��رնԻ���.
     */
    protected void finishDialog() {      
      String inputString = null;
      InputItem item = null; 
      IInputValidator validator = null;
      for(int i = 0; i < m_arrInputItems.size(); i++){
          item = (InputItem) m_arrInputItems.get(i); 
          if (item != null){
              validator = item.getValidator();
              if (validator == null) continue;//û�м����
              
              inputString = item.getText();              
              if (!validator.isValid(inputString)){
                  return ; //�޷�ͨ������ӣ�����
              }
          }
          
      }
      //�����û���ѡ��
      setSelectOption(OK_OPTION);
      
      //�رնԻ���
      dispose();
    }
    
    /**
     * ��ʼ���Ի���
     * �ڶԻ��������������һ���ı�������
     */
    private void initialization(){
        //��������
        m_gridBagLayout  = new GridBagLayout();
        m_gridBagConstraints = new GridBagConstraints();        
        super.getDialogArea().setLayout(m_gridBagLayout);
        
        this.setTitle(MultiLang.getString("uiuforep0000725"));//����
        this.setModal(true);        
        this.setResizable(false);
    }   
    

    
    
}
