package com.less.tvplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 *
 * @author deeper
 * @date 2017/12/6
 * 描述: 该类主要用于抽取手势的各种监听事件,简化主要代码的逻辑
 */

public class GestureFocusLayout extends LinearLayout {

    public interface onGestureCallback {

    }

    public GestureFocusLayout(Context context) {
        super(context);
        init(context);
    }

    public GestureFocusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

    }
}
