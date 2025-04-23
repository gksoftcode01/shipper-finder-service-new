package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.model.YPreferredRequest;
import ai.yarmook.shipperfinder.core.model.YSearchRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<ObjectNode> preferred(YPreferredRequest request) throws IOException;

    List<ObjectNode> search(YSearchRequest request) throws IOException;
}
