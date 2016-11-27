package com.ckr.otms.common.bo;

import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryRequest;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryResponse;
import com.ckr.otms.common.web.util.RestPaginationTemplate.SortCriteria;
import com.ckr.otms.exception.SystemException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HibernateRestPaginationService {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateRestPaginationService.class);

    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public QueryResponse<Object> query(final String hql,
                                       final Map<String, Object> params,
                                       final Function<Object[], ?> mapper,
                                       final Long maxNoRecordsPerPage) {

        return new BaseRestPaginationServiceTemplate() {


            @Override
            protected QueryResponse<Object> doQuery(QueryResponse<Object> response, QueryRequest request) {
                String queryStr = adjustQueryString(hql, params);

                response = doQueryContent(response, request, queryStr, params, mapper);

                response = doQueryTotalNoRecords(response, request, queryStr, params);

                return response;
            }

        }.query(maxNoRecordsPerPage);

    }

    @SuppressWarnings("rawtypes")
    public QueryResponse query(final String hql, final Map<String, Object> params, Function<Object[], ?> mapper) {

        return query(hql, params, mapper, 500L);

    }


    @SuppressWarnings("unchecked")
    private QueryResponse<Object> doQueryContent(QueryResponse<Object> response,
                                                 QueryRequest request,
                                                 String queryStr,
                                                 Map<String, Object> params,
                                                 Function<Object[], ?> mapper) {

        String queryString = appendSortCriteria(queryStr, request);

        LOG.debug("get data HQL:{}", queryString);

        Query<?> query = sessionFactory.getCurrentSession().createQuery(queryString);

        setQueryParameter(query, params);

        if (request != null && request.getStart() != null) {
            query.setFirstResult(request.getStart().intValue());
        }

        if (request != null && request.getEnd() != null) {
            query.setMaxResults((int) (request.getEnd() - request.getStart()) + 1);
        }

        List<Object> resultList = null;

        if (mapper != null) {
            Stream<Object[]> stream = (Stream<Object[]>) query.stream();

            resultList = stream.map(mapper).collect(Collectors.toList());
        } else {
            resultList = (List<Object>) query.getResultList();
        }

        response.setContent(resultList);

        return response;
    }


    private QueryResponse<Object> doQueryTotalNoRecords(QueryResponse<Object> response,
                                                        QueryRequest request,
                                                        String queryStr,
                                                        Map<String, Object> params) {

        if (request == null || (request.getStart() == 0 && request.getEnd() == null)) {
            response.setTotal((long) response.getContent().size());
            return response;
        }


        String queryString = getHQLForTotalNoRecords(queryStr);

        LOG.debug("get total no of records HQL:{}", queryString);

        Query<?> query = sessionFactory.getCurrentSession().createQuery(queryString);

        setQueryParameter(query, params);

        response.setTotal((Long) query.getSingleResult());

        LOG.debug("total number of records {}", response.getTotal());

        return response;
    }

    private String getHQLForTotalNoRecords(String queryStr) {

        String result = null;

        String upperQueryStr = queryStr.toUpperCase();


        int start = queryStr.indexOf("(");

        int end = queryStr.indexOf(")");

        int fromIndex;

        int i = queryStr.length() - 1;

        do {
            if (i < 0) {
                throw new SystemException("cannot find a top level 'FROM' from query string: '" +
                        queryStr + "' . The i is < 0 already");
            }


            fromIndex = upperQueryStr.lastIndexOf("FROM", i);

            if (fromIndex < 0) {
                throw new SystemException("cannot find a top level 'FROM' from query string: '" +
                        queryStr + "' . Cannot find 'FROM'. The i = " + i);
            }


            if (fromIndex <= end && fromIndex >= start) {
                i = fromIndex - 1;
                continue;
            } else {
                break;
            }

        } while (true);

        result = "SELECT COUNT(*) " + queryStr.substring(fromIndex);

        LOG.debug("HQL to get total number of records {}", result);

        return result;
    }

    private void setQueryParameter(Query<?> query, Map<String, Object> params) {

        if (params == null) {
            return;
        }

        params.entrySet()
                .stream()
                .forEach(e -> query.setParameter(e.getKey(), e.getValue()));

    }

    private String appendSortCriteria(String queryString, QueryRequest request) {

        if (request == null) {
            return queryString;
        }

        List<SortCriteria> sortCriList = request.getSortCriteriaList();

        if (sortCriList == null || sortCriList.isEmpty()) {
            return queryString;
        }


        StringBuffer result = new StringBuffer(queryString);
        result.append(" order by ");

        for (int i = 0; i < sortCriList.size(); i++) {
            SortCriteria criteria = sortCriList.get(i);
            result.append(criteria.getFieldName());

            if (criteria.isAsc()) {
                result.append(" asc ");
            } else {
                result.append(" desc ");
            }

            if (i < sortCriList.size() - 1) {
                result.append(" , ");
            }

        }

        return result.toString();
    }


    protected String adjustQueryString(String hql, Map<String, Object> params) {

        StringBuffer queryStr = new StringBuffer(hql);

        LOG.debug("before adjustment, the query string is {}", queryStr);


        for (int startInd = queryStr.indexOf("/*"); startInd >= 0; startInd = queryStr.indexOf("/*")) {

            int endInd = queryStr.indexOf("*/", startInd);

            if (endInd < 0) {
                break;
            }

            String criteriaStr = queryStr.substring(startInd + 2, endInd);

            LOG.debug("criteria string: {}", criteriaStr);

            StringTokenizer tokenizer = new StringTokenizer(criteriaStr, "|");
            if (tokenizer.countTokens() < 2) {
                LOG.error("invalid criteria string: {}", criteriaStr);
            }

            String criteriaName = tokenizer.nextToken().trim();

            if (params.keySet().contains(criteriaName)) {

                String criteriaContent = tokenizer.nextToken();

                queryStr.replace(startInd, endInd + 2, criteriaContent);

            } else {

                queryStr.replace(startInd, endInd + 2, "");
            }

        }

        LOG.debug("after adjustment, the query string is {}", queryStr);


        return queryStr.toString();

    }


}
