package com.less.test.db;

import com.less.test.bean.Html;

import java.util.List;

/**
 * Created by deeper on 2018/2/3.
 */

public interface HtmlDao {

    void save(Html html);

    List list();

    int count();

}
