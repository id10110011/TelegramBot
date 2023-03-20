package org.example;

public class Location {
    private String city;
    private String country;
    private String latlon;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLatlon() {
        return latlon;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", latlon='" + latlon + '\'' +
                '}';
    }
}
