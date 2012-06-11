/**  
 * 创建时间2004-8-11  9:07:03
 * @author CaiJie 
 */
package com.ufsoft.report.dialog;

/**
 * 输入数据验证接口
 */
public interface IInputValidator {
    /**
     * 输入信息是否有效
     * @param input 输入
     * @return boolean
     */
    boolean isValid(String input);
}
