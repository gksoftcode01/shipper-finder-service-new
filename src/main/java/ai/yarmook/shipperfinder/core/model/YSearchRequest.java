package ai.yarmook.shipperfinder.core.model;

import java.time.Instant;
import java.util.Set;

public class YSearchRequest extends YPreferredRequest {

    String mustToState;
    String mustToCountry;
    String mustFromState;
    String mustFromCountry;
    Set<String> mustItem;
    Set<String> mustCategory;
    Instant fromDate;
    Instant toDate;

    public String getMustToState() {
        return mustToState;
    }

    public void setMustToState(String mustToState) {
        this.mustToState = mustToState;
    }

    public String getMustToCountry() {
        return mustToCountry;
    }

    public void setMustToCountry(String mustToCountry) {
        this.mustToCountry = mustToCountry;
    }

    public String getMustFromState() {
        return mustFromState;
    }

    public void setMustFromState(String mustFromState) {
        this.mustFromState = mustFromState;
    }

    public String getMustFromCountry() {
        return mustFromCountry;
    }

    public void setMustFromCountry(String mustFromCountry) {
        this.mustFromCountry = mustFromCountry;
    }

    public Set<String> getMustItem() {
        return mustItem;
    }

    public void setMustItem(Set<String> mustItem) {
        this.mustItem = mustItem;
    }

    public Set<String> getMustCategory() {
        return mustCategory;
    }

    public void setMustCategory(Set<String> mustCategory) {
        this.mustCategory = mustCategory;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }
}
