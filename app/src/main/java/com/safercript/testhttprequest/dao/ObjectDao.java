package com.safercript.testhttprequest.dao;

import android.os.Handler;
import android.os.Message;

import java.util.List;

public abstract class ObjectDao {

    private Handler handler;

    public void init(Handler handler){
        this.handler = handler;
    }

   public <K> void  success(int whatForHandler, List<K> listData) {
        if (handler != null) {
            Message msg = handler.obtainMessage(whatForHandler, listData);
            handler.sendMessage(msg);
        }
    }

    void success(int whatForHandler,Object key) {
        Message msg = handler.obtainMessage(whatForHandler, key);
        handler.sendMessage(msg);

    }

    void success(int whatForHandler) {
        handler.sendEmptyMessage(whatForHandler);
    }

    void error(int whatForHandler) {
        handler.sendEmptyMessage(whatForHandler);
    }
}
