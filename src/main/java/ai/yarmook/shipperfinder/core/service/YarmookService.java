package ai.yarmook.shipperfinder.core.service;

import java.util.List;
import java.util.Map;

public interface YarmookService {
    List<Map<String, Object>> itemAutoCompleteList(String query);
}
