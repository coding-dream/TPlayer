package com.less.tvplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author deeper
 * @date 2017/12/8
 */

public class LiveActivity extends BaseLiveActivity {

    @Override
    public void loadData() {
        // String id = getIntent().getExtras().getString("Room_id");
        // boolean movie = getIntent().getExtras().getBoolean("isMovie");
        // markMovie(false);
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

    /**
     * 这里其实不传ids也是可以直接 findViewById的,但我们希望仅处理允许的布局,这里可以对Base里面的布局进行替换和修改,主功能
     * @param ids
     */
    @Override
    public void handleLayout(int... ids) {
        LinearLayout layout_top_menu = findViewById(ids[0]);
        LinearLayout layout_bottom = findViewById(ids[1]);
        LinearLayout layout_ex = findViewById(ids[2]);

        LayoutInflater.from(this).inflate(R.layout.layout_top_menu, layout_top_menu, true);
        ImageView iv_live_download = layout_top_menu.findViewById(R.id.iv_live_download);
        iv_live_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveActivity.this, "download", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
