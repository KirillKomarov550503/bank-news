package com.netcracker.komarov.news.dao.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "news")
@Proxy(lazy = false)
public class News extends BaseEntity {

    @Column(name = "date", length = 20)
    private String date;

    @Column(name = "title")
    private String title;

    @Column(name = "text", columnDefinition = "Text")
    private String text;

    @Column(name = "admin_id")
    private long adminId;

    @Column(name = "news_status")
    @Enumerated(EnumType.STRING)
    private NewsStatus newsStatus;

    public News() {
    }

    public NewsStatus getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(NewsStatus newsStatus) {
        this.newsStatus = newsStatus;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        News news = (News) o;
        return adminId == news.adminId &&
                Objects.equals(date, news.date) &&
                Objects.equals(title, news.title) &&
                Objects.equals(text, news.text) &&
                newsStatus == news.newsStatus;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), date, title, text, adminId, newsStatus);
    }

    @Override
    public String toString() {
        return "News{" +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", adminId=" + adminId +
                ", newsStatus=" + newsStatus +
                ", id=" + id +
                '}';
    }
}

