package ai.yarmook.shipperfinder.core.dto;

import ai.yarmook.shipperfinder.service.dto.TripDTO;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import java.util.Set;

public class YTripDTO extends TripDTO {

    Set<TripItemDTO> items;

    public Set<TripItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<TripItemDTO> items) {
        this.items = items;
    }
}
