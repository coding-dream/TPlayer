package com.less.tvplayer;

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
}
