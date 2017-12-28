package com.less.tplayer.mvp.movie.data;

import java.io.Serializable;

/**
 *
 * @author deeper
 * @date 2017/12/27
 */

public class Movie implements Serializable {

    private static final long serialVersionUID = 5468335797443850679L;
    private String name;
    private String playUrl;
    private String detailUrl;
    private String detail;
    private String score;
    private boolean recommend;
    private String date;
    private String image;
    private String actors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", detail='" + detail + '\'' +
                ", score='" + score + '\'' +
                ", recommend=" + recommend +
                ", date='" + date + '\'' +
                ", image='" + image + '\'' +
                ", actors='" + actors + '\'' +
                '}';
    }
}
