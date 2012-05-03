
/**
 * AffairServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.seeyon.client;

    /**
     *  AffairServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class AffairServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public AffairServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public AffairServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for exportAgentPendingList method
            * override this method for handling normal response from exportAgentPendingList operation
            */
           public void receiveResultexportAgentPendingList(
                    com.seeyon.client.AffairServiceStub.ExportAgentPendingListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportAgentPendingList operation
           */
            public void receiveErrorexportAgentPendingList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportTrackList method
            * override this method for handling normal response from exportTrackList operation
            */
           public void receiveResultexportTrackList(
                    com.seeyon.client.AffairServiceStub.ExportTrackListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportTrackList operation
           */
            public void receiveErrorexportTrackList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportPendingList method
            * override this method for handling normal response from exportPendingList operation
            */
           public void receiveResultexportPendingList(
                    com.seeyon.client.AffairServiceStub.ExportPendingListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportPendingList operation
           */
            public void receiveErrorexportPendingList(java.lang.Exception e) {
            }
                


    }
    