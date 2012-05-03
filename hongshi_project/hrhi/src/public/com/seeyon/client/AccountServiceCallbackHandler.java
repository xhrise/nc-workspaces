
/**
 * AccountServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  AccountServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class AccountServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public AccountServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public AccountServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for deleteAccountByCode method
            * override this method for handling normal response from deleteAccountByCode operation
            */
           public void receiveResultdeleteAccountByCode(
                    com.seeyon.client.AccountServiceStub.DeleteAccountByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteAccountByCode operation
           */
            public void receiveErrordeleteAccountByCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAccountByCode method
            * override this method for handling normal response from getAccountByCode operation
            */
           public void receiveResultgetAccountByCode(
                    com.seeyon.client.AccountServiceStub.GetAccountByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAccountByCode operation
           */
            public void receiveErrorgetAccountByCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAccountId method
            * override this method for handling normal response from getAccountId operation
            */
           public void receiveResultgetAccountId(
                    com.seeyon.client.AccountServiceStub.GetAccountIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAccountId operation
           */
            public void receiveErrorgetAccountId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateAccountByCode method
            * override this method for handling normal response from updateAccountByCode operation
            */
           public void receiveResultupdateAccountByCode(
                    com.seeyon.client.AccountServiceStub.UpdateAccountByCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateAccountByCode operation
           */
            public void receiveErrorupdateAccountByCode(java.lang.Exception e) {
            }
                


    }
    