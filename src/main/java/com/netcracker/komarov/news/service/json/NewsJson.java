package com.netcracker.komarov.news.service.json;

import com.netcracker.komarov.news.service.dto.entity.NewsDTO;

import java.io.Serializable;
import java.util.Objects;

public class NewsJson implements Serializable {
    private long id;
    private String date;
    private String title;
    private String text;
    private String status;
//    private String exception;

    public NewsJson() {
    }

    public NewsJson(NewsDTO dto) {
        id = dto.getId();
        date = dto.getDate();
        title = dto.getTitle();
        text = dto.getText();
        status = dto.getStatus();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsJson newsJson = (NewsJson) o;
        return id == newsJson.id &&
                Objects.equals(date, newsJson.date) &&
                Objects.equals(title, newsJson.title) &&
                Objects.equals(text, newsJson.text) &&
                Objects.equals(status, newsJson.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, date, title, text, status);
    }
}
