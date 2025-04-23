package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.constant.ElasticConstant;
import ai.yarmook.shipperfinder.core.model.YPreferredRequest;
import ai.yarmook.shipperfinder.core.model.YSearchRequest;
import ai.yarmook.shipperfinder.core.service.SearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final ElasticsearchClient esClient;

    public SearchServiceImpl(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public List<ObjectNode> preferred(YPreferredRequest request) throws IOException {
        SearchResponse<ObjectNode> response = esClient.search(
            buildPreferredSearchRequest(
                request,
                request.getSearchType().equals(ElasticConstant.TRIP_INDEX_SEARCH_TYPE)
                    ? ElasticConstant.TRIP_INDEX
                    : ElasticConstant.CARGO_REQUEST_INDEX
            ),
            ObjectNode.class
        );
        List<ObjectNode> list = response.hits().hits().stream().map(item -> item.source()).toList();
        return list;
    }

    @Override
    public List<ObjectNode> search(YSearchRequest request) throws IOException {
        SearchResponse<ObjectNode> response = esClient.search(
            buildSearchRequest(
                request,
                request.getSearchType().equals(ElasticConstant.TRIP_INDEX_SEARCH_TYPE)
                    ? ElasticConstant.TRIP_INDEX
                    : ElasticConstant.CARGO_REQUEST_INDEX
            ),
            ObjectNode.class
        );
        List<ObjectNode> list = response.hits().hits().stream().map(item -> item.source()).toList();
        return list;
    }

    private SearchRequest buildSearchRequest(YSearchRequest request, String index) {
        BoolQuery boolQuery = BoolQuery.of(q -> {
            if (!request.getMustFromCountry().isEmpty()) {
                q.must(MatchQuery.of(tr -> tr.field("fromCountry.iso2").query(request.getMustFromCountry()))._toQuery());
            }
            if (!request.getMustToCountry().isEmpty()) {
                q.must(MatchQuery.of(tr -> tr.field("toCountry.iso2").query(request.getMustToCountry()))._toQuery());
            }
            if (!request.getMustFromState().isEmpty()) {
                q.must(MatchQuery.of(tr -> tr.field("fromState.isoCode").query(request.getMustFromState()))._toQuery());
            }
            if (!request.getMustToState().isEmpty()) {
                q.must(MatchQuery.of(tr -> tr.field("toState.isoCode").query(request.getMustToState()))._toQuery());
            }
            if (request.getMustCategory() != null && !request.getMustCategory().isEmpty()) {
                BoolQuery bq = BoolQuery.of(qq -> {
                    for (String category : request.getMustCategory()) {
                        qq.should(MatchQuery.of(tr -> tr.field("items.item.itemType.id").query(category).boost(0f))._toQuery());
                    }
                    return qq;
                });
                q.must(bq._toQuery());
            }
            if (request.getMustItem() != null && !request.getMustItem().isEmpty()) {
                BoolQuery bq = BoolQuery.of(qq -> {
                    for (String item : request.getMustItem()) {
                        qq.should(MatchQuery.of(tr -> tr.field("items.item.id").query(item).boost(0f))._toQuery());
                    }
                    return qq;
                });
                q.must(bq._toQuery());
            }
            q.should(qq -> qq.bool(buildPreferredQuery(request, index, false)));
            return q;
        });
        SearchRequest req = SearchRequest.of(sr ->
            sr.index(index).from(request.getPage()).size(request.getPageSize()).query(q -> q.bool(boolQuery))
        );
        log.info(req.query().toString());
        return req;
    }

    private BoolQuery buildPreferredQuery(YPreferredRequest request, String index, boolean dateRestriction) {
        LocalDateTime _now = LocalDate.now().atTime(0, 0);
        LocalDateTime _week = LocalDate.now().minusWeeks(1).atTime(0, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        JsonData jsonData_now = JsonData.of(calendar.getTime());
        BoolQuery boolQuery = BoolQuery.of(q -> {
            q
                .should(qr -> qr.matchAll(MatchAllQuery.of(m -> m)))
                .should(qr -> qr.match(mp -> mp.field("fromCountry.iso2").query(request.getFromCountry()).boost(2f)))
                .should(qr -> qr.match(mp -> mp.field("fromState.isoCode").query(request.getFromState()).boost(5f)))
                .should(qr -> qr.match(mp -> mp.field("toCountry.iso2").query(request.getToCountry()).boost(2f)))
                .should(qr -> qr.match(mp -> mp.field("toState.isoCode").query(request.getToState()).boost(5f)))
                .must(qr -> qr.matchPhrase(mp -> mp.field("status").query("PUBLISHED")));

            //q.should(qr -> qr.range(rg -> rg.field("tripDate").from(_week.toString()).to(_now.toString()).boost(5f)))
            if (dateRestriction) {
                q.must(qr -> {
                    qr.range(rg ->
                        rg.date(builder -> {
                            builder
                                .field(
                                    index.equals(ElasticConstant.TRIP_INDEX)
                                        ? ElasticConstant.TRIP_VALID_UNTIL_DATE
                                        : ElasticConstant.CARGO_REQUEST_VALID_UNTIL_DATE
                                )
                                .gt(jsonData_now.toString());
                            return builder;
                        })
                    );
                    return qr;
                });
            }
            return q;
        });
        return boolQuery;
    }

    private SearchRequest buildPreferredSearchRequest(YPreferredRequest request, String index) throws IOException {
        SearchRequest req = SearchRequest.of(sr ->
            sr
                .index(index)
                .query(q -> q.bool(buildPreferredQuery(request, index, true)))
                .size(request.getPageSize())
                .from(request.getPage())
        );
        log.info(req.query().toString());
        return req;
    }
}
