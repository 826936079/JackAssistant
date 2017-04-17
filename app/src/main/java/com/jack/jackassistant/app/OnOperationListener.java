package com.jack.jackassistant.app;

import com.jack.jackassistant.bean.Function;

/**
 * Created by xiaofeng on 2017/3/26.
 */

public interface OnOperationListener {

    public void sendMessages(String content);
    public void selectedFace(String resId);
    public void selectedFunction(Function function);
}
