package com.companysf.filmbilet.Entities;

import com.companysf.filmbilet.Entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private List<Movie> list = new ArrayList<>();

    public List<Movie> getList() {
        return list;
    }

    public void setList(List<Movie> list) {
        this.list = list;
    }
}
