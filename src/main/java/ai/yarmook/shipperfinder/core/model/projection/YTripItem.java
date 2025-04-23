package ai.yarmook.shipperfinder.core.model.projection;

import ai.yarmook.shipperfinder.domain.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

public interface YTripItem {
    Long getId();

    YTripItem id(Long id);

    void setId(Long id);

    Float getItemPrice();

    void setItemPrice(Float itemPrice);

    Long getMaxQty();

    void setMaxQty(Long maxQty);

    Item getItem();

    void setItem(Item item);

    @JsonIgnoreProperties(value = { "tripItems", "cargoRequestItems" }, allowSetters = true)
    Set<Tag> getTags();

    void setTags(Set<Tag> tags);

    @JsonIgnore
    @JsonIgnoreProperties(value = { "items", "fromCountry", "toCountry", "fromState", "toState" }, allowSetters = true)
    Trip getTrip();

    void setTrip(Trip trip);

    Unit getUnit();

    void setUnit(Unit unit);

    TripItem unit(Unit unit);
}
