package com.less.tplayer.mvp.movie.data;

import android.os.Handler;
import android.os.Looper;

import com.less.tplayer.TpApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2017/11/25.
 */

public class MovieRepository implements MovieDataSource {

    private static MovieRepository INSTANCE = null;

    private final MovieDataSource mRetemoDataSource;

    private final MovieDataSource mLocalDataSource;

    private Handler handler = new Handler(Looper.getMainLooper());

    public MovieRepository() {
        this.mLocalDataSource = new MovieLocalDataSource();
        this.mRetemoDataSource = new MovieRemoteDataSource();
    }

    @Override
    public void getDatasByPage(final String category, final int page, final LoadCallback callback) {
        TpApplication.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://v.361keji.com/movie.php?m=http://www.360kan.com/dianying/list.php?cat=" + category + "%26pageno=" + page;
                    Document document = Jsoup.parse(new URL(url), 6000);
                    Elements elements = document.select("div[class=s-tab-main]").select("ul[class=list g-clear]").select("li[class=item]");
                    final List<Movie> list = new ArrayList<Movie>();
                    Movie movie = null;
                    for (Element e : elements) {
                        movie = new Movie();
                        String name = e.select("a[class=js-tongjic]").attr("title");
                        String image = e.select("img").attr("src");
                        String detailUrl = e.select("a[class=js-tongjic]").attr("abs:href");
                        String score = e.select("span[class=s2]").text();
                        String actors = e.select("p[class=star]").text();
                        String date = e.select("span[class=hint]").text();

                        movie.setName(name);
                        movie.setImage(image);
                        movie.setDetailUrl(detailUrl);
                        movie.setScore(score);
                        movie.setActors(actors);
                        movie.setDate(date);
                        list.add(movie);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataLoaded(list);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataNotAvailable();
                        }
                    });
                }
            }
        });
    }
}
