package com.less.tplayer.mvp.movie;

import com.less.tplayer.mvp.base.BasePresenter;
import com.less.tplayer.mvp.base.BaseView;
import com.less.tplayer.mvp.feature.data.Feature;

import java.util.List;

/**
 * Created by deeper on 2017/12/27.
 */

public interface MovieContract {

    interface View extends BaseView<Presenter> {
        /**
         * 刷新成功
         * @param datas
         */
        void showRefreshSuccess(List<Feature> datas);

        /**
         * 加载成功
         * @param datas
         */
        void showLoadMoreSuccess(List<Feature> datas);

        /**
         * 没有更多数据
         */
        void showNoMore();

        /**
         * 加载完成
         */
        void showComplete();

        /**
         * 网络异常
         * @param strId
         */
        void showNetworkError(int strId);
    }

    interface Presenter extends BasePresenter {

        void doRefresh();

        void doLoadMore();
    }
}
