package ai.yarmook.shipperfinder.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.yarmook.shipperfinder.domain.CargoRequestItem} entity. This class is used
 * in {@link ai.yarmook.shipperfinder.web.rest.CargoRequestItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cargo-request-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequestItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter maxQty;

    private StringFilter photoUrl;

    private LongFilter itemId;

    private LongFilter unitId;

    private LongFilter tagId;

    private LongFilter cargoRequestId;

    private Boolean distinct;

    public CargoRequestItemCriteria() {}

    public CargoRequestItemCriteria(CargoRequestItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.maxQty = other.optionalMaxQty().map(LongFilter::copy).orElse(null);
        this.photoUrl = other.optionalPhotoUrl().map(StringFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(LongFilter::copy).orElse(null);
        this.unitId = other.optionalUnitId().map(LongFilter::copy).orElse(null);
        this.tagId = other.optionalTagId().map(LongFilter::copy).orElse(null);
        this.cargoRequestId = other.optionalCargoRequestId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CargoRequestItemCriteria copy() {
        return new CargoRequestItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getMaxQty() {
        return maxQty;
    }

    public Optional<LongFilter> optionalMaxQty() {
        return Optional.ofNullable(maxQty);
    }

    public LongFilter maxQty() {
        if (maxQty == null) {
            setMaxQty(new LongFilter());
        }
        return maxQty;
    }

    public void setMaxQty(LongFilter maxQty) {
        this.maxQty = maxQty;
    }

    public StringFilter getPhotoUrl() {
        return photoUrl;
    }

    public Optional<StringFilter> optionalPhotoUrl() {
        return Optional.ofNullable(photoUrl);
    }

    public StringFilter photoUrl() {
        if (photoUrl == null) {
            setPhotoUrl(new StringFilter());
        }
        return photoUrl;
    }

    public void setPhotoUrl(StringFilter photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public Optional<LongFilter> optionalItemId() {
        return Optional.ofNullable(itemId);
    }

    public LongFilter itemId() {
        if (itemId == null) {
            setItemId(new LongFilter());
        }
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getUnitId() {
        return unitId;
    }

    public Optional<LongFilter> optionalUnitId() {
        return Optional.ofNullable(unitId);
    }

    public LongFilter unitId() {
        if (unitId == null) {
            setUnitId(new LongFilter());
        }
        return unitId;
    }

    public void setUnitId(LongFilter unitId) {
        this.unitId = unitId;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public Optional<LongFilter> optionalTagId() {
        return Optional.ofNullable(tagId);
    }

    public LongFilter tagId() {
        if (tagId == null) {
            setTagId(new LongFilter());
        }
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    public LongFilter getCargoRequestId() {
        return cargoRequestId;
    }

    public Optional<LongFilter> optionalCargoRequestId() {
        return Optional.ofNullable(cargoRequestId);
    }

    public LongFilter cargoRequestId() {
        if (cargoRequestId == null) {
            setCargoRequestId(new LongFilter());
        }
        return cargoRequestId;
    }

    public void setCargoRequestId(LongFilter cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CargoRequestItemCriteria that = (CargoRequestItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(maxQty, that.maxQty) &&
            Objects.equals(photoUrl, that.photoUrl) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(cargoRequestId, that.cargoRequestId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maxQty, photoUrl, itemId, unitId, tagId, cargoRequestId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequestItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMaxQty().map(f -> "maxQty=" + f + ", ").orElse("") +
            optionalPhotoUrl().map(f -> "photoUrl=" + f + ", ").orElse("") +
            optionalItemId().map(f -> "itemId=" + f + ", ").orElse("") +
            optionalUnitId().map(f -> "unitId=" + f + ", ").orElse("") +
            optionalTagId().map(f -> "tagId=" + f + ", ").orElse("") +
            optionalCargoRequestId().map(f -> "cargoRequestId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
