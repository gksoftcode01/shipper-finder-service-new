package ai.yarmook.shipperfinder.core.model;

import ai.yarmook.shipperfinder.core.model.projection.YCargoRequestItem;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class YCargoRequest {

    @Id
    private Long id;

    private Instant createDate;

    private Instant validUntil;

    private CargoRequestStatus status;

    private Boolean isNegotiable;

    private Float budget;

    private UUID createdByEncId;

    private UUID takenByEncId;

    @JsonIgnoreProperties(value = { "cargoRequest" }, allowSetters = true)
    private Set<YCargoRequestItem> items = new HashSet<>();

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

    public YCargoRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public YCargoRequest createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getValidUntil() {
        return this.validUntil;
    }

    public YCargoRequest validUntil(Instant validUntil) {
        this.setValidUntil(validUntil);
        return this;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public CargoRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CargoRequestStatus status) {
        this.status = status;
    }

    public Boolean getIsNegotiable() {
        return this.isNegotiable;
    }

    public YCargoRequest isNegotiable(Boolean isNegotiable) {
        this.setIsNegotiable(isNegotiable);
        return this;
    }

    public void setIsNegotiable(Boolean isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public Float getBudget() {
        return this.budget;
    }

    public YCargoRequest budget(Float budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public UUID getCreatedByEncId() {
        return this.createdByEncId;
    }

    public YCargoRequest createdByEncId(UUID createdByEncId) {
        this.setCreatedByEncId(createdByEncId);
        return this;
    }

    public void setCreatedByEncId(UUID createdByEncId) {
        this.createdByEncId = createdByEncId;
    }

    public UUID getTakenByEncId() {
        return this.takenByEncId;
    }

    public YCargoRequest takenByEncId(UUID takenByEncId) {
        this.setTakenByEncId(takenByEncId);
        return this;
    }

    public void setTakenByEncId(UUID takenByEncId) {
        this.takenByEncId = takenByEncId;
    }

    public Set<YCargoRequestItem> getItems() {
        return this.items;
    }

    public void setItems(Set<YCargoRequestItem> cargoRequestItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCargoRequest(null));
        }
        if (cargoRequestItems != null) {
            //cargoRequestItems.forEach(i -> i.setCargoRequest(this));
        }
        this.items = cargoRequestItems;
    }

    public YCargoRequest items(Set<YCargoRequestItem> cargoRequestItems) {
        this.setItems(cargoRequestItems);
        return this;
    }

    public YCargoRequest addItems(YCargoRequestItem cargoRequestItem) {
        this.items.add(cargoRequestItem);
        //cargoRequestItem.setCargoRequest(this);
        return this;
    }

    public YCargoRequest removeItems(YCargoRequestItem cargoRequestItem) {
        this.items.remove(cargoRequestItem);
        cargoRequestItem.setCargoRequest(null);
        return this;
    }

    public Country getFromCountry() {
        return this.fromCountry;
    }

    public void setFromCountry(Country country) {
        this.fromCountry = country;
    }

    public YCargoRequest fromCountry(Country country) {
        this.setFromCountry(country);
        return this;
    }

    public Country getToCountry() {
        return this.toCountry;
    }

    public void setToCountry(Country country) {
        this.toCountry = country;
    }

    public YCargoRequest toCountry(Country country) {
        this.setToCountry(country);
        return this;
    }

    public StateProvince getFromState() {
        return this.fromState;
    }

    public void setFromState(StateProvince stateProvince) {
        this.fromState = stateProvince;
    }

    public YCargoRequest fromState(StateProvince stateProvince) {
        this.setFromState(stateProvince);
        return this;
    }

    public StateProvince getToState() {
        return this.toState;
    }

    public void setToState(StateProvince stateProvince) {
        this.toState = stateProvince;
    }

    public YCargoRequest toState(StateProvince stateProvince) {
        this.setToState(stateProvince);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((CargoRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequest{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", status='" + getStatus() + "'" +
            ", isNegotiable='" + getIsNegotiable() + "'" +
            ", budget=" + getBudget() +
            ", createdByEncId='" + getCreatedByEncId() + "'" +
            ", takenByEncId='" + getTakenByEncId() + "'" +
            "}";
    }
}
