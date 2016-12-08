package com.roka.rokamethodstream;

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
    private static HashMap<Object, Object> map = new HashMap();
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

    public RokaMethodStream attach(@NonNull Object method, @NonNull Object key) {
        map.put(key, method);
        return this;
    }

    public RokaMethodStream detach(Object key) {
        map.remove(key);
        return this;


    }

    public RokaMethodStream run(final Object data, @NonNull Object key) {
        final Object tempObj = map.get(key);
        if (tempObj != null) {
            mExtendHandler = ExtendHandler.init(tempObj);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    if (tempObj instanceof Func) {
                        message.what = 0;
                    } else if (tempObj instanceof Procedure) {
                        message.what = 1;
                    }
                    message.obj = data;
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


    public interface Func {
        void func(Object data) throws Exception;
    }

    public interface Procedure {
        void proc() throws Exception;
    }



}

