package ai.yarmook.shipperfinder.core.dto;

import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import java.util.Set;

public class YCargoRequestDTO extends CargoRequestDTO {

    Set<CargoRequestItemDTO> items;

    public Set<CargoRequestItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<CargoRequestItemDTO> items) {
        this.items = items;
    }
}
