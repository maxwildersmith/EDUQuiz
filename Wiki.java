package com.example.quiz.eduquiz;

/**
 * Created by csaper6 on 3/13/17.
 */
public class Wiki implements Comparable<Wiki>{
    String name,url;

    public Wiki(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Wiki wiki) {
        return name.compareTo(wiki.getName());
    }
}
