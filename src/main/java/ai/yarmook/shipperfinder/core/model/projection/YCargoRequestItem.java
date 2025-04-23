package ai.yarmook.shipperfinder.core.model.projection;

import ai.yarmook.shipperfinder.domain.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

public interface YCargoRequestItem {
    YCargoRequestItem id(Long id);

    Long getId();

    void setId(Long id);

    Long getMaxQty();

    void setMaxQty(Long maxQty);

    String getPhotoUrl();

    void setPhotoUrl(String photoUrl);

    Item getItem();

    void setItem(Item item);

    YCargoRequestItem item(Item item);

    @JsonIgnoreProperties(value = { "tripItems", "cargoRequestItems" }, allowSetters = true)
    Set<Tag> getTags();

    void setTags(Set<Tag> tags);

    @JsonIgnore
    @JsonIgnoreProperties(value = { "items", "fromCountry", "toCountry", "fromState", "toState" }, allowSetters = true)
    CargoRequest getCargoRequest();

    void setCargoRequest(CargoRequest cargoRequest);

    Unit getUnit();

    void setUnit(Unit unit);
    YCargoRequestItem unit(Unit unit);
}
