package com.companysf.filmbilet.entities;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private List<Movie> list;

    public MovieList() {
        list = new ArrayList<>();
    }

    public List<Movie> getList() {
        return list;
    }

    public void setList(List<Movie> list) {
        this.list = list;
    }
}
