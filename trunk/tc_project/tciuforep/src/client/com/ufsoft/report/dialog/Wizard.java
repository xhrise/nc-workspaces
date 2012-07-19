/* Generated by Together */

package com.ufsoft.report.dialog;

import java.util.ArrayList;

import javax.swing.JDialog;

import com.ufsoft.report.util.MultiLang;

/**
 * ��������
 * ʵ����IWizard�ӿ�.
 * �򵼶Ի����е����ɰ�ťʱ����performFinish()������Ӧ�����ء�
 * �򵼶Ի����е��ȡ����ťʱ����performCancel()������Ӧ�����ء�
 * ����Ϣ�㹻ʱ��Ӧͨ��setWizardFinish(boolean)��֪ͨ�򵼶Ի���
 * @author CaiJie
 */
public class  Wizard  implements IWizard {
  /**
   *���캯��
   */
  public Wizard() {
  }

  /**
   * ���ó�ʼ�Ļҳ������
   */
  private final int INITIAL_MAX_PAGE_NUMBER = 20;

  /**
   * �򵼻ҳ������
   * ���ݳ�ʼ�Ļҳ��������ʼ������
   */
  private ArrayList activePageList = new ArrayList(INITIAL_MAX_PAGE_NUMBER);

  /**
   * �������ĶԻ���
   */
  private JDialog m_container = null;

  /**
   * �򵼳�ʼҳ��
   */
  private IWizardPage m_startingPage = null;
  
  /**
   * ��־�����
   */
  private boolean m_bWizardFinish = false;
  /**
   * �Ƿ���Ҫ��һ�����û������Ϳ�������򵼡�
 * @return boolean
   */
   public boolean canFinish(){
       return m_bWizardFinish;
   }
   /**
    * ���������
    * ����Ϣ�㹻ʱ��Ӧͨ���˷�����֪ͨ�򵼶Ի���
 * @param WizardFinish
    */
   public void setWizardFinish(boolean WizardFinish) {
     m_bWizardFinish = WizardFinish;
     //�����򵼶Ի������ɰ�ť
     if (WizardFinish){
         if((this.getContainer() != null)&&(this.getContainer() instanceof WizardDialog )){
             WizardDialog wizardDialog = (WizardDialog)this.getContainer();
             if (wizardDialog.getOKButton() != null)
                 wizardDialog.getOKButton().setEnabled(true);
         }        
     } else{
         if((this.getContainer() != null)&&(this.getContainer() instanceof WizardDialog )){
             WizardDialog wizardDialog = (WizardDialog)this.getContainer();
             if (wizardDialog.getOKButton() != null)
                 wizardDialog.getOKButton().setEnabled(false);
         }   
     }
   }
  /**
   *���ܣ�����һ���µ���ҳ��
   *˵�������ӵ���ǰʹ�õ���ҳ��������ĩ�ˡ�
   * ���ҳ����������ҳ��ʱ,�Զ�ָ����һ�����ҳ��Ϊ��ʼҳ��
 * @param newWizardPage
   */
  public void addPage(IWizardPage newWizardPage) {
      if (newWizardPage == null) {
          String strError = MultiLang.getString("uiuforep0000700");//"�����������:�µ���ҳ�治��Ϊ��";
          throw new IllegalArgumentException(strError);
      }
    //���ҳ����������ҳ��ʱ,�Զ�ָ����һ�����ҳ��Ϊ��ʼҳ��
    if ((getPageCount() == 0) && (m_startingPage == null)) {
      m_startingPage = newWizardPage;
    }

    //���ҳ��������ҳ������������ʼָ����ҳ����initTotalPageNumʱ��������
    if (activePageList.size() >= INITIAL_MAX_PAGE_NUMBER) {
      activePageList.ensureCapacity(activePageList.size() + 1);
      activePageList.add(newWizardPage);
    }
    else {
      activePageList.add(newWizardPage);
    }

    //ָ���¼�ҳ����������
    newWizardPage.setWizard(this); 
    }
  
  /**
   *��ȡָ��ҳ�ĺ��ҳ
   * @param page IWizardPage��ָ��ҳ
   * @return IWizardPage
   */
  public IWizardPage getNextPage(IWizardPage page) {
    if (page == null) {
        String strError = MultiLang.getString("uiuforep0000701");//"�����������:ҳ�治��Ϊ��";
        throw new IllegalArgumentException(strError);
      }
    return page.getNextPage();
  }

  /**
   * ��ȡָ�����Ƶ���ҳ
   * @param pageName String����ҳ������
   * @return IWizardPage
   */
  public IWizardPage getPage(String pageName) {
    if(pageName == null){
        String strError = MultiLang.getString("uiuforep0000702");//"�����������:ҳ�����Ʋ���Ϊ��";
        throw new IllegalArgumentException(strError);
    }
    IWizardPage result = null;
    int pageNumber = activePageList.size();
    for(int i = 0; i < pageNumber; i++){
      if (((IWizardPage)(activePageList.get(i))).getName().equals(pageName)){
        result = (IWizardPage)(activePageList.get(i));
        break;
      }
    }
    return result;
  }

  /**
   *����ҳ�������
   * @return int
   */
  public int getPageCount() {
    return activePageList.size();
  }

  /**
   * ��������ʽ��ȡ�����򵼵�����ҳ��
   * @return IWizardPage[]
   */
  public IWizardPage[] getPages() {
    IWizardPage[] arrPages = new IWizardPage[activePageList.size()];
    for(int i = 0; i < arrPages.length; i++){
      arrPages[i] = (IWizardPage)(activePageList.get(i));
    }
    return arrPages;
  }

  /**
   * ��ȡָ��ҳ��ǰһҳ
   * @param page IWizardPage��ָ��ҳ
   * @return IWizardPage
   */
  public IWizardPage getPreviousPage(IWizardPage page) {
    if(page == null)
        return null;
    return page.getPreviousPage();
  }


  /**
   * ��ȡ�򵼵ĳ�ʼҳ
   * @return IWizardPage
   */
  public IWizardPage getStartingPage() {
    return m_startingPage;
  }

  /**
   * ��Ӧ��ȡ��
   * �����Ӧ�ø��Ǵ˺���
   * @return boolean
   */
  public boolean performCancel() {
    return true;
  }

  /**
   * ��Ӧ�����
   * �����Ӧ�ø��Ǵ˺���
   * @return boolean
   */
  public boolean performFinish(){
      return true;
  }

  /**
   *ָ�����������ĶԻ���
   * @param wizardDialog WizardDialog
   */
  public void setContainer(JDialog wizardDialog) {
    m_container = wizardDialog;
  }

  /**
   * �����������ĶԻ���
   * @return WizardDialog
   */
  public JDialog getContainer() {
    return m_container;
  }
  /**
   * ������ʼҳ�棬Ĭ�ϵ���ʼҳ���ǵ�һ�������򵼵�ҳ��
   * ����ʱ��2004-9-14  9:27:48
   * @param startPage - ��ʼҳ��
   */
  public void setStartPage(IWizardPage startPage){
      if (startPage == null) {
          String strError = MultiLang.getString("uiuforep0000703");//"�����������:��ʼҳ�治��Ϊ��";
          throw new IllegalArgumentException(strError);
      }
      m_startingPage = startPage;
  }
  /**
   * ������ʼҳ��
   * ����ʱ��2004-9-14  9:27:22
   * @return - ��ʼҳ��
   */
  public IWizardPage getStartPage(){
      return m_startingPage;
  }
}