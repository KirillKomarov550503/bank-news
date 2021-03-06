package com.netcracker.komarov.news.service.dto.entity;

import java.io.Serializable;
import java.util.Objects;

public class NewsDTO implements Serializable {
    private long id;

    private String date;

    private String title;

    private String text;

    private String status;

    public NewsDTO() {
    }

    public NewsDTO(long id, String date, String title, String text, String status) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
        this.status = status;
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
        NewsDTO newsDTO = (NewsDTO) o;
        return id == newsDTO.id &&
                Objects.equals(date, newsDTO.date) &&
                Objects.equals(title, newsDTO.title) &&
                Objects.equals(text, newsDTO.text) &&
                Objects.equals(status, newsDTO.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, date, title, text, status);
    }

    @Override
    public String toString() {
        return "NewsDTO{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
