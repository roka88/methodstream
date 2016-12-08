package com.roka.rokamethodstream;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by roka on 2016. 12. 8..
 */

public class ExtendHandler extends Handler {

    private static RokaMethodStream.Func mFunc;
    private static RokaMethodStream.Procedure mProcedure;
    private static ExtendHandler mExtendHandler;

    private ExtendHandler(){}

    public static ExtendHandler init(@NonNull Object object) {
        if (mExtendHandler == null) {
            mExtendHandler = new ExtendHandler();
        }
        if (object instanceof RokaMethodStream.Func) {
            mFunc = (RokaMethodStream.Func) object;
        } else if (object instanceof RokaMethodStream.Procedure) {
            mProcedure = (RokaMethodStream.Procedure) object;
        }
        return mExtendHandler;
    }



    @Override
    public void handleMessage(Message msg) {
        try {
            if (msg.what == 0) {
                if (mFunc != null) {
                    mFunc.func(msg.obj);
                }
            } else if (msg.what == 1) {
                if (mProcedure != null) {
                    mProcedure.proc();
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) Log.e("ExtendHandler Err", e.toString());
        }

    }
}
