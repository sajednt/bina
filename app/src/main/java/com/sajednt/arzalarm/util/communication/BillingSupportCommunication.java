package com.sajednt.arzalarm.util.communication;


import com.sajednt.arzalarm.util.IabResult;

public interface BillingSupportCommunication {
    void onBillingSupportResult(int response);
    void remoteExceptionHappened(IabResult result);
}
