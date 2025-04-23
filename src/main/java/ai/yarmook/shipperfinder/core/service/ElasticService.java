package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.model.YCargoRequest;
import ai.yarmook.shipperfinder.core.model.YTrip;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import java.io.IOException;

public interface ElasticService {
    IndexResponse indexTrip(YTrip trip) throws IOException;

    DeleteResponse deleteTrip(Long tripId) throws IOException;

    IndexResponse indexCargoRequest(YCargoRequest cargoRequest) throws IOException;

    DeleteResponse deleteCargoRequest(Long cargoRequestId) throws IOException;

    UpdateResponse<Object> updateCargoRequest(String documentId, String filedName, String newValue) throws IOException;
}
