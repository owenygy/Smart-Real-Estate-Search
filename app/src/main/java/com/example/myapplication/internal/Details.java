package com.example.myapplication.internal;

public class Details {
    public int id;
    public int rent;
    public int bedroom;
    public String location;
    public int garage;
    public String highlights;

    @Override
    public String toString() {
        return "$" + rent + " | " +
                bedroom + " bedroom" + " | " +
                location + " | " +
                garage + " garage" + " | " +
                highlights;
    }

    public Details(int id, int rent, int bedroom, String location, int garage, String highlights) {
        this.id = id;
        this.rent = rent;
        this.bedroom = bedroom;
        this.location = location;
        this.garage = garage;
        this.highlights = highlights;
    }

    public int getId() {
        return id;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGarage(int garage) {
        this.garage = garage;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public int getRent() {
        return rent;
    }

    public int getBedroom() {
        return bedroom;
    }

    public String getLocation() {
        return location;
    }

    public int getGarage() {
        return garage;
    }

    public String getHighlights() {
        return highlights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Details details = (Details) o;
        return id == details.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
