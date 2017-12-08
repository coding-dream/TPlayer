package com.less.tvplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deeper
 * @date 2017/12/8
 */

public class ListLiveActivity extends BaseLiveActivity {
    private ObjectAnimator objectAnimator;
    LinearLayout layout_ex_choose;
    private ListView listView;
    private LiveAdapter<LiveInfo> liveAdapter;
    private boolean drawerOpen = false;

    @Override
    public void loadData() {
        // String id = getIntent().getExtras().getString("Room_id");
        // boolean movie = getIntent().getExtras().getBoolean("isMovie");
        markMovie(false);
        API.getDataById("1", new API.LoadCallback() {
            @Override
            public void onDataLoaded(LiveInfo liveInfo) {
                loadVideo(liveInfo.getRoom_name(),liveInfo.getLive_url());
            }

            @Override
            public void onDataNotAvailable() {
                // svProgressHUD.showErrorWithStatus("主播还在赶来的路上~~");
            }
        });
    }

    @Override
    public void handleLayout(int... ids) {
        final LinearLayout layout_top_menu = findViewById(ids[0]);
        LinearLayout layout_bottom = findViewById(ids[1]);
        final LinearLayout layout_ex = findViewById(ids[2]);

        LayoutInflater.from(this).inflate(R.layout.layout_top_menu, layout_top_menu, true);
        LayoutInflater.from(this).inflate(R.layout.layout_ex_list, layout_ex, true);

        ImageView iv_live_download = layout_top_menu.findViewById(R.id.iv_live_download);
        layout_ex_choose = layout_ex.findViewById(R.id.layout_ex_choose);
        listView = layout_ex.findViewById(R.id.listView);

        liveAdapter = new LiveAdapter<LiveInfo>(ListLiveActivity.this) {
            @Override
            protected int getLayoutId() {
                return R.layout.item_list_ex;
            }

            @Override
            protected void bindData(ViewHolder viewHolder, LiveInfo item, int position) {
                viewHolder.tv_num.setText(position + 1 + "");
                viewHolder.tv_name.setText(item.getRoom_name());
            }
        };
        listView.setAdapter(liveAdapter);
        iv_live_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
                startAnimation();
                layout_ex.setVisibility(View.VISIBLE);
            }
        });
    }

    private void startAnimation() {
        if (objectAnimator != null && objectAnimator.isRunning() || drawerOpen) {
            return;
        }
        // 动画显示ListView
        objectAnimator = ObjectAnimator.ofFloat(layout_ex_choose, "translationX",- dip2px(200),0);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                drawerOpen = true;
            }
        });
        objectAnimator.start();

    }

    private void fetchData() {
        List<LiveInfo> list = new ArrayList<>();
        for(int i = 0;i < 40;i++) {
            LiveInfo liveInfo = new LiveInfo();
            liveInfo.setRoom_name("Java " + i);
            liveInfo.setLive_url("http://www.baidu.com");
            list.add(liveInfo);
        }
        liveAdapter.addItems(list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (objectAnimator != null && objectAnimator.isRunning()) {
            objectAnimator.cancel();
            objectAnimator = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (objectAnimator != null && objectAnimator.isRunning()) {
            return;
        }
        if (drawerOpen) {
            objectAnimator = ObjectAnimator.ofFloat(layout_ex_choose, "translationX", 0, -dip2px(200));
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.setDuration(500);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    drawerOpen = false;
                }
            });
            objectAnimator.start();
        } else {
            super.onBackPressed();
        }
    }

    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
