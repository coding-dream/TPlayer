package com.less.tplayer.interfaces;

/**
 * Created by deeper on 2017/11/21.
 */

public interface FragmentReSelected{
    /**
     * 当底部navBar中某个tab被重复点击时触发,该接口用于某个fragment实例主动调用的公用方法,非回调实现,也是一种通信方式.
     */
    void callReSelect();
}
