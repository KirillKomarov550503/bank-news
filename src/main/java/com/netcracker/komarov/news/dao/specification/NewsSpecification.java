package com.netcracker.komarov.news.dao.specification;

import com.netcracker.komarov.news.dao.entity.News;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Map;

public class NewsSpecification {
    public static Specification<News> findByStatus(Map<String, String> params) {
        return (Specification<News>) (root, criteriaQuery, criteriaBuilder) -> {
            Subquery<News> subquery = criteriaQuery.subquery(News.class);
            Root<News> newsRoot = subquery.from(News.class);
            Predicate predicate;
            predicate = "true".equals(params.get("filter")) ?
                    criteriaBuilder.equal(root.get("newsStatus"),
                            Enum.valueOf(NewsStatus.class, params.get("status").toUpperCase())) :
                    criteriaBuilder.exists(subquery.select(newsRoot));
            return predicate;
        };
    }
}
