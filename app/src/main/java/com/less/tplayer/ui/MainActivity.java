package com.less.tplayer.ui;

import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.less.tplayer.AppConfig;
import com.less.tplayer.R;
import com.less.tplayer.base.activity.BaseBackActivity;
import com.less.tplayer.fragment.NavFragment;
import com.less.tplayer.util.SharedPreferenceUtils;
import com.less.tplayer.widget.NavButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends BaseBackActivity {
    private NavFragment mNavBar;
    private long mBackPressedTime;
    private long timeout = 3 * 1000;
    private List<TurnBackListener> mTurnBackListeners = new ArrayList<>();

    public interface TurnBackListener {
        boolean onTurnBack();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        // checkUpdate();
    }

    @Override
    protected void initView() {
        super.initView();
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = ((NavFragment) manager.findFragmentById(R.id.fragment_nav));
        mNavBar.setup(manager, R.id.main_container, new NavFragment.OnNavReselectListener() {
            @Override
            public void onReselect(NavButton navButton) {

            }
        });

        // 首次启动introduce介绍页
        boolean isFirstUsed = SharedPreferenceUtils.getBooleanData(AppConfig.FIRST_BOOT, true);
        if (isFirstUsed) {
            View view = findViewById(R.id.layout_introduce);
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewGroup) v.getParent()).removeView(v);
                    SharedPreferenceUtils.setBooleanData(AppConfig.FIRST_BOOT,false);
                }
            });
        }
    }

    public void addOnTurnBackListener(TurnBackListener turnBackListener) {
        this.mTurnBackListeners.add(turnBackListener);
    }

    @Override
    public void onBackPressed() {
        for (TurnBackListener l : mTurnBackListeners) {
            if (l.onTurnBack()) {
                return;
            }
        }
        boolean isDoubleClick = SharedPreferenceUtils.getBooleanData(AppConfig.KEY_DOUBLE_CLICK_EXIT,true);
        if (isDoubleClick) {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < timeout) {
                finish();
            } else {
                mBackPressedTime = curTime;
                Toast.makeText(this, R.string.tip_double_click_exit, Toast.LENGTH_LONG).show();
            }
        } else {
            finish();
        }
    }
}
