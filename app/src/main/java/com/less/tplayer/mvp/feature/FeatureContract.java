package com.less.tplayer.mvp.feature;

import com.less.tplayer.mvp.base.BasePresenter;
import com.less.tplayer.mvp.base.BaseView;

/**
 * Created by deeper on 2017/11/24.
 */

public interface FeatureContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
