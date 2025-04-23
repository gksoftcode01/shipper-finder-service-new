package ai.yarmook.shipperfinder.core.model;

import ai.yarmook.shipperfinder.core.model.projection.YTripItem;
import ai.yarmook.shipperfinder.domain.*;
import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class YTrip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Instant tripDate;

    private Instant arriveDate;

    private Long maxWeight;

    private String notes;

    private Instant createDate;

    private Boolean isNegotiate;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    private UUID createdByEncId;

    @JsonIgnoreProperties(value = { "trip" }, allowSetters = true)
    private Set<YTripItem> items = new HashSet<>();

    @JsonIgnoreProperties(value = { "stateProvinces" }, allowSetters = true)
    private Country fromCountry;

    @JsonIgnoreProperties(value = { "stateProvinces" }, allowSetters = true)
    private Country toCountry;

    @JsonIgnoreProperties(value = { "cities", "country" }, allowSetters = true)
    private StateProvince fromState;

    @JsonIgnoreProperties(value = { "cities", "country" }, allowSetters = true)
    private StateProvince toState;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public YTrip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTripDate() {
        return this.tripDate;
    }

    public YTrip tripDate(Instant tripDate) {
        this.setTripDate(tripDate);
        return this;
    }

    public void setTripDate(Instant tripDate) {
        this.tripDate = tripDate;
    }

    public Instant getArriveDate() {
        return this.arriveDate;
    }

    public YTrip arriveDate(Instant arriveDate) {
        this.setArriveDate(arriveDate);
        return this;
    }

    public void setArriveDate(Instant arriveDate) {
        this.arriveDate = arriveDate;
    }

    public Long getMaxWeight() {
        return this.maxWeight;
    }

    public YTrip maxWeight(Long maxWeight) {
        this.setMaxWeight(maxWeight);
        return this;
    }

    public void setMaxWeight(Long maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getNotes() {
        return this.notes;
    }

    public YTrip notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public YTrip createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsNegotiate() {
        return this.isNegotiate;
    }

    public YTrip isNegotiate(Boolean isNegotiate) {
        this.setIsNegotiate(isNegotiate);
        return this;
    }

    public void setIsNegotiate(Boolean isNegotiate) {
        this.isNegotiate = isNegotiate;
    }

    public TripStatus getStatus() {
        return this.status;
    }

    public YTrip status(TripStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public UUID getCreatedByEncId() {
        return this.createdByEncId;
    }

    public YTrip createdByEncId(UUID createdByEncId) {
        this.setCreatedByEncId(createdByEncId);
        return this;
    }

    public void setCreatedByEncId(UUID createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public Set<YTripItem> getItems() {
        return this.items;
    }

    public void setItems(Set<YTripItem> tripItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setTrip(null));
        }
        if (tripItems != null) {
            //tripItems.forEach(i -> i.setTrip(this));
        }
        this.items = tripItems;
    }

    public YTrip items(Set<YTripItem> tripItems) {
        this.setItems(tripItems);
        return this;
    }

    public YTrip addItems(YTripItem tripItem) {
        this.items.add(tripItem);
        //tripItem.setTrip(this);
        return this;
    }

    public YTrip removeItems(YTripItem tripItem) {
        this.items.remove(tripItem);
        tripItem.setTrip(null);
        return this;
    }

    public Country getFromCountry() {
        return this.fromCountry;
    }

    public void setFromCountry(Country country) {
        this.fromCountry = country;
    }

    public YTrip fromCountry(Country country) {
        this.setFromCountry(country);
        return this;
    }

    public Country getToCountry() {
        return this.toCountry;
    }

    public void setToCountry(Country country) {
        this.toCountry = country;
    }

    public YTrip toCountry(Country country) {
        this.setToCountry(country);
        return this;
    }

    public StateProvince getFromState() {
        return this.fromState;
    }

    public void setFromState(StateProvince stateProvince) {
        this.fromState = stateProvince;
    }

    public YTrip fromState(StateProvince stateProvince) {
        this.setFromState(stateProvince);
        return this;
    }

    public StateProvince getToState() {
        return this.toState;
    }

    public void setToState(StateProvince stateProvince) {
        this.toState = stateProvince;
    }

    public YTrip toState(StateProvince stateProvince) {
        this.setToState(stateProvince);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return getId() != null && getId().equals(((Trip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
        @Override
        public String toString() {
            return "Trip{" +
                "id=" + getId() +
                ", tripDate='" + getTripDate() + "'" +
                ", arriveDate='" + getArriveDate() + "'" +
                ", maxWeight=" + getMaxWeight() +
                ", notes='" + getNotes() + "'" +
                ", createDate='" + getCreateDate() + "'" +
                ", isNegotiate='" + getIsNegotiate() + "'" +
                ", status='" + getStatus() + "'" +
                ", createdByEncId='" + getCreatedByEncId() + "'" +
                "}";
        }
}
