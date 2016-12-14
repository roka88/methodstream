package com.roka.rokamethodstream;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by roka on 2016. 12. 1..
 */

public class RokaMethodStream {
    private static HashMap<Object, PerentInterface> map = new HashMap();
    private static RokaMethodStream mHelper;


    private ExtendHandler mExtendHandler;
    private Executor mExecutor = Executors.newFixedThreadPool(2);



    private RokaMethodStream() {
    }

    public static RokaMethodStream init() {
        if (mHelper == null) {
            mHelper = new RokaMethodStream();
        }
        return mHelper;
    }

    public RokaMethodStream attach(@NonNull PerentInterface method, @NonNull Object key) {
        map.put(key, method);
        return this;
    }

    public RokaMethodStream detach(Object key) {
        map.remove(key);
        return this;
    }

    public RokaMethodStream run(final Object data, @NonNull Object key) {
        if (mExtendHandler == null) {
            mExtendHandler = new ExtendHandler();
        }

        final PerentInterface tempObj = map.get(key);
        if (tempObj != null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    if (tempObj instanceof Func) {
                        message.what = 0;
                    } else if (tempObj instanceof Procedure) {
                        message.what = 1;
                    }
                    message.obj = new SendObject(tempObj, data);
                    mExtendHandler.sendMessage(message);
                }
            };
            mExecutor.execute(runnable);
        }
        return this;
    }


    public synchronized RokaMethodStream syncRun(Object data, Object key) {
        try {
            Object tempObj = map.get(key);
            if (tempObj != null) {
                if (tempObj instanceof Func && data != null) {
                    Func func = (Func) tempObj;
                    func.func(data);
                } else if (tempObj instanceof Procedure) {
                    Procedure procedure = (Procedure) tempObj;
                    procedure.proc();
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) Log.e("RokaMethodStream Err", e.toString());
        }
        return this;
    }


    public interface Func extends PerentInterface {
        void func(Object data) throws Exception;
    }

    public interface Procedure extends PerentInterface {
        void proc() throws Exception;
    }

    private class SendObject {

        public Object method;
        public Object data;

        public SendObject(Object method, Object data) {
            this.method = method;
            this.data = data;
        }
    }

    private interface PerentInterface{}

    private class ExtendHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                SendObject tempObject = (SendObject)msg.obj;
                if (msg.what == 0) {
                    RokaMethodStream.Func tempFun = (RokaMethodStream.Func)tempObject.method;
                    tempFun.func(tempObject.data);
                } else if (msg.what == 1) {
                    RokaMethodStream.Procedure tempFun = (RokaMethodStream.Procedure)tempObject.method;
                    tempFun.proc();
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG) Log.e("ExtendHandler Err", e.toString());
            }

        }
    }

}

