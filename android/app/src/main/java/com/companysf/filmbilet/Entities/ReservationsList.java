package com.companysf.filmbilet.Entities;

import java.util.ArrayList;
import java.util.List;

public class ReservationsList {
    private List<CustomerReservation> list;

    public ReservationsList() {
        list = new ArrayList<>();
    }

    public List<CustomerReservation> getList() {
        return list;
    }

    public void setList(List<CustomerReservation> list) {
        this.list = list;
    }
}
