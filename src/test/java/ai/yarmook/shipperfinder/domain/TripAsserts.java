package ai.yarmook.shipperfinder.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TripAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTripAllPropertiesEquals(Trip expected, Trip actual) {
        assertTripAutoGeneratedPropertiesEquals(expected, actual);
        assertTripAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTripAllUpdatablePropertiesEquals(Trip expected, Trip actual) {
        assertTripUpdatableFieldsEquals(expected, actual);
        assertTripUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTripAutoGeneratedPropertiesEquals(Trip expected, Trip actual) {
        assertThat(actual)
            .as("Verify Trip auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTripUpdatableFieldsEquals(Trip expected, Trip actual) {
        assertThat(actual)
            .as("Verify Trip relevant properties")
            .satisfies(a -> assertThat(a.getTripDate()).as("check tripDate").isEqualTo(expected.getTripDate()))
            .satisfies(a -> assertThat(a.getArriveDate()).as("check arriveDate").isEqualTo(expected.getArriveDate()))
            .satisfies(a -> assertThat(a.getMaxWeight()).as("check maxWeight").isEqualTo(expected.getMaxWeight()))
            .satisfies(a -> assertThat(a.getNotes()).as("check notes").isEqualTo(expected.getNotes()))
            .satisfies(a -> assertThat(a.getCreateDate()).as("check createDate").isEqualTo(expected.getCreateDate()))
            .satisfies(a -> assertThat(a.getIsNegotiate()).as("check isNegotiate").isEqualTo(expected.getIsNegotiate()))
            .satisfies(a -> assertThat(a.getStatus()).as("check status").isEqualTo(expected.getStatus()))
            .satisfies(a -> assertThat(a.getCreatedByEncId()).as("check createdByEncId").isEqualTo(expected.getCreatedByEncId()))
            .satisfies(a -> assertThat(a.getEncId()).as("check encId").isEqualTo(expected.getEncId()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTripUpdatableRelationshipsEquals(Trip expected, Trip actual) {
        assertThat(actual)
            .as("Verify Trip relationships")
            .satisfies(a -> assertThat(a.getFromCountry()).as("check fromCountry").isEqualTo(expected.getFromCountry()))
            .satisfies(a -> assertThat(a.getToCountry()).as("check toCountry").isEqualTo(expected.getToCountry()))
            .satisfies(a -> assertThat(a.getFromState()).as("check fromState").isEqualTo(expected.getFromState()))
            .satisfies(a -> assertThat(a.getToState()).as("check toState").isEqualTo(expected.getToState()));
    }
}
