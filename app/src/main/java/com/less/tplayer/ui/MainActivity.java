package com.less.tplayer.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.less.tplayer.AppConfig;
import com.less.tplayer.R;
import com.less.tplayer.base.activity.BaseBackActivity;
import com.less.tplayer.fragment.NavFragment;
import com.less.tplayer.interfaces.OnFragReSelListener;
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
        mNavBar = (NavFragment) manager.findFragmentById(R.id.fragment_nav);
        mNavBar.setup(manager, R.id.main_container, new NavFragment.OnNavReselectListener() {
            @Override
            public void onReselect(NavButton navButton) {
                Fragment fragment = navButton.getFragment();
                if (fragment != null
                        && fragment instanceof OnFragReSelListener) {
                    OnFragReSelListener listener = (OnFragReSelListener) fragment;
                    listener.onFragReSelect();
                }
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

    /**
     * 该监听器在MainActivity的fragment中设置,如: Xfragment.attach(Context context){ activity = (MainActivity) context; activity.addOnTurnBackListener(listener); }
     *
     * @param turnBackListener
     */
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
    
    public void toggleNavFragment(boolean show) {
        final View view = mNavBar.getView();
        if (view == null) {
            return;
        }
        // hide
        view.setVisibility(View.VISIBLE);
        if (!show) {
            view.animate()
                    .translationY(view.getHeight())
                    .setDuration(180)
                    .setInterpolator(new LinearInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                        }
                    });
        } else {
            view.animate()
                    .translationY(0)
                    .setDuration(180)
                    .setInterpolator(new LinearInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // fix:bug > 点击隐藏的同时，快速点击显示
                            view.setVisibility(View.VISIBLE);
                            view.setTranslationY(0);
                        }
                    });
        }
    }
}
