package com.netcracker.komarov.news.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "client_news")
public class ClientNews extends BaseEntity {
    @Column(name = "client_id")
    private long clientId;

    @Column(name = "news_id")
    private long newsId;

    public ClientNews() {
    }

    public ClientNews(long clientId, long newsId) {
        this.clientId = clientId;
        this.newsId = newsId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientNews that = (ClientNews) o;
        return clientId == that.clientId &&
                newsId == that.newsId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), clientId, newsId);
    }

    @Override
    public String toString() {
        return "ClientNews{" +
                "clientId=" + clientId +
                ", newsId=" + newsId +
                ", id=" + id +
                '}';
    }
}
