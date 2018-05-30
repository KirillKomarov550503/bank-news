package com.netcracker.komarov.news.dao.specification;

import com.netcracker.komarov.news.dao.entity.News;
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
                    criteriaBuilder.equal(newsRoot.get("newsStatus"), params.get("newsStatus")) :
                    criteriaBuilder.exists(subquery.select(newsRoot));
            return predicate;
        };
    }
}
