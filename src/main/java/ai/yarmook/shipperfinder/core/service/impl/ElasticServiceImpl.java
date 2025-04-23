package ai.yarmook.shipperfinder.core.service.impl;

import static ai.yarmook.shipperfinder.core.constant.ElasticConstant.CARGO_REQUEST_INDEX;

import ai.yarmook.shipperfinder.core.model.YCargoRequest;
import ai.yarmook.shipperfinder.core.model.YTrip;
import ai.yarmook.shipperfinder.core.service.ElasticService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ElasticServiceImpl implements ElasticService {

    private final ElasticsearchClient client;

    public ElasticServiceImpl(ElasticsearchClient client) {
        this.client = client;
    }

    @Override
    public IndexResponse indexTrip(YTrip trip) throws IOException {
        IndexResponse response = client.index(i -> i.index("yarmook-trip").id(trip.getId().toString()).document(trip));
        return response;
    }

    @Override
    public DeleteResponse deleteTrip(Long tripId) throws IOException {
        DeleteResponse response = client.delete(i -> i.index("yarmook-trip").id(tripId.toString()));
        return response;
    }

    @Override
    public IndexResponse indexCargoRequest(YCargoRequest cargoRequest) throws IOException {
        IndexResponse response = client.index(i ->
            i.index("yarmook-cargo-request").id(cargoRequest.getId().toString()).document(cargoRequest)
        );
        return response;
    }

    @Override
    public DeleteResponse deleteCargoRequest(Long cargoRequestId) throws IOException {
        DeleteResponse response = client.delete(i -> i.index("yarmook-cargo-request").id(cargoRequestId.toString()));
        return response;
    }

    @Override
    public UpdateResponse<Object> updateCargoRequest(String documentId, String filedName, String newValue) throws IOException {
        // Data to update
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put(filedName, newValue);
        // Create the UpdateRequest
        UpdateRequest<Object, Map<String, Object>> updateRequest = UpdateRequest.of(u ->
            u.index(CARGO_REQUEST_INDEX).id(documentId).doc(updatedFields)
        );
        // Execute the update
        UpdateResponse<Object> updateResponse = client.update(updateRequest, Map.class);
        return updateResponse;
    }
}
