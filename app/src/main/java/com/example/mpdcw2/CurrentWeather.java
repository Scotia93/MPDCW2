package com.example.mpdcw2;

public class CurrentWeather {
    private String title;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

