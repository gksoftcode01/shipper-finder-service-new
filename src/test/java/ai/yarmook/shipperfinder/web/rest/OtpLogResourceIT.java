package ai.yarmook.shipperfinder.web.rest;

import static ai.yarmook.shipperfinder.domain.OtpLogAsserts.*;
import static ai.yarmook.shipperfinder.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.yarmook.shipperfinder.IntegrationTest;
import ai.yarmook.shipperfinder.domain.OtpLog;
import ai.yarmook.shipperfinder.repository.OtpLogRepository;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import ai.yarmook.shipperfinder.service.mapper.OtpLogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OtpLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtpLogResourceIT {

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OTP_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OTP_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SEND_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEND_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DELIVERED = 1;
    private static final Integer UPDATED_DELIVERED = 2;

    private static final Integer DEFAULT_VERIFIED = 1;
    private static final Integer UPDATED_VERIFIED = 2;

    private static final Integer DEFAULT_TRIES_COUNT = 1;
    private static final Integer UPDATED_TRIES_COUNT = 2;

    private static final String DEFAULT_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/otp-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OtpLogRepository otpLogRepository;

    @Autowired
    private OtpLogMapper otpLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtpLogMockMvc;

    private OtpLog otpLog;

    private OtpLog insertedOtpLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtpLog createEntity() {
        return new OtpLog()
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .otpValue(DEFAULT_OTP_VALUE)
            .createdDate(DEFAULT_CREATED_DATE)
            .sendDate(DEFAULT_SEND_DATE)
            .delivered(DEFAULT_DELIVERED)
            .verified(DEFAULT_VERIFIED)
            .triesCount(DEFAULT_TRIES_COUNT)
            .response(DEFAULT_RESPONSE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtpLog createUpdatedEntity() {
        return new OtpLog()
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .otpValue(UPDATED_OTP_VALUE)
            .createdDate(UPDATED_CREATED_DATE)
            .sendDate(UPDATED_SEND_DATE)
            .delivered(UPDATED_DELIVERED)
            .verified(UPDATED_VERIFIED)
            .triesCount(UPDATED_TRIES_COUNT)
            .response(UPDATED_RESPONSE);
    }

    @BeforeEach
    public void initTest() {
        otpLog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOtpLog != null) {
            otpLogRepository.delete(insertedOtpLog);
            insertedOtpLog = null;
        }
    }

    @Test
    @Transactional
    void createOtpLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);
        var returnedOtpLogDTO = om.readValue(
            restOtpLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OtpLogDTO.class
        );

        // Validate the OtpLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOtpLog = otpLogMapper.toEntity(returnedOtpLogDTO);
        assertOtpLogUpdatableFieldsEquals(returnedOtpLog, getPersistedOtpLog(returnedOtpLog));

        insertedOtpLog = returnedOtpLog;
    }

    @Test
    @Transactional
    void createOtpLogWithExistingId() throws Exception {
        // Create the OtpLog with an existing ID
        otpLog.setId(1L);
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtpLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMobileNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        otpLog.setMobileNumber(null);

        // Create the OtpLog, which fails.
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        restOtpLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOtpLogs() throws Exception {
        // Initialize the database
        insertedOtpLog = otpLogRepository.saveAndFlush(otpLog);

        // Get all the otpLogList
        restOtpLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otpLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].otpValue").value(hasItem(DEFAULT_OTP_VALUE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sendDate").value(hasItem(DEFAULT_SEND_DATE.toString())))
            .andExpect(jsonPath("$.[*].delivered").value(hasItem(DEFAULT_DELIVERED)))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED)))
            .andExpect(jsonPath("$.[*].triesCount").value(hasItem(DEFAULT_TRIES_COUNT)))
            .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE)));
    }

    @Test
    @Transactional
    void getOtpLog() throws Exception {
        // Initialize the database
        insertedOtpLog = otpLogRepository.saveAndFlush(otpLog);

        // Get the otpLog
        restOtpLogMockMvc
            .perform(get(ENTITY_API_URL_ID, otpLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otpLog.getId().intValue()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.otpValue").value(DEFAULT_OTP_VALUE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.sendDate").value(DEFAULT_SEND_DATE.toString()))
            .andExpect(jsonPath("$.delivered").value(DEFAULT_DELIVERED))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED))
            .andExpect(jsonPath("$.triesCount").value(DEFAULT_TRIES_COUNT))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE));
    }

    @Test
    @Transactional
    void getNonExistingOtpLog() throws Exception {
        // Get the otpLog
        restOtpLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOtpLog() throws Exception {
        // Initialize the database
        insertedOtpLog = otpLogRepository.saveAndFlush(otpLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otpLog
        OtpLog updatedOtpLog = otpLogRepository.findById(otpLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOtpLog are not directly saved in db
        em.detach(updatedOtpLog);
        updatedOtpLog
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .otpValue(UPDATED_OTP_VALUE)
            .createdDate(UPDATED_CREATED_DATE)
            .sendDate(UPDATED_SEND_DATE)
            .delivered(UPDATED_DELIVERED)
            .verified(UPDATED_VERIFIED)
            .triesCount(UPDATED_TRIES_COUNT)
            .response(UPDATED_RESPONSE);
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(updatedOtpLog);

        restOtpLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otpLogDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOtpLogToMatchAllProperties(updatedOtpLog);
    }

    @Test
    @Transactional
    void putNonExistingOtpLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpLog.setId(longCount.incrementAndGet());

        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otpLogDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtpLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpLog.setId(longCount.incrementAndGet());

        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otpLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtpLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpLog.setId(longCount.incrementAndGet());

        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otpLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtpLogWithPatch() throws Exception {
        // Initialize the database
        insertedOtpLog = otpLogRepository.saveAndFlush(otpLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otpLog using partial update
        OtpLog partialUpdatedOtpLog = new OtpLog();
        partialUpdatedOtpLog.setId(otpLog.getId());

        partialUpdatedOtpLog
            .createdDate(UPDATED_CREATED_DATE)
            .sendDate(UPDATED_SEND_DATE)
            .delivered(UPDATED_DELIVERED)
            .triesCount(UPDATED_TRIES_COUNT)
            .response(UPDATED_RESPONSE);

        restOtpLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtpLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtpLog))
            )
            .andExpect(status().isOk());

        // Validate the OtpLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtpLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOtpLog, otpLog), getPersistedOtpLog(otpLog));
    }

    @Test
    @Transactional
    void fullUpdateOtpLogWithPatch() throws Exception {
        // Initialize the database
        insertedOtpLog = otpLogRepository.saveAndFlush(otpLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otpLog using partial update
        OtpLog partialUpdatedOtpLog = new OtpLog();
        partialUpdatedOtpLog.setId(otpLog.getId());

        partialUpdatedOtpLog
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .otpValue(UPDATED_OTP_VALUE)
            .createdDate(UPDATED_CREATED_DATE)
            .sendDate(UPDATED_SEND_DATE)
            .delivered(UPDATED_DELIVERED)
            .verified(UPDATED_VERIFIED)
            .triesCount(UPDATED_TRIES_COUNT)
            .response(UPDATED_RESPONSE);

        restOtpLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtpLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtpLog))
            )
            .andExpect(status().isOk());

        // Validate the OtpLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtpLogUpdatableFieldsEquals(partialUpdatedOtpLog, getPersistedOtpLog(partialUpdatedOtpLog));
    }

    @Test
    @Transactional
    void patchNonExistingOtpLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpLog.setId(longCount.incrementAndGet());

        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtpLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otpLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otpLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtpLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpLog.setId(longCount.incrementAndGet());

        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otpLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtpLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otpLog.setId(longCount.incrementAndGet());

        // Create the OtpLog
        OtpLogDTO otpLogDTO = otpLogMapper.toDto(otpLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtpLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(otpLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtpLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtpLog() throws Exception {
        // Initialize the database
        insertedOtpLog = otpLogRepository.saveAndFlush(otpLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the otpLog
        restOtpLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, otpLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return otpLogRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OtpLog getPersistedOtpLog(OtpLog otpLog) {
        return otpLogRepository.findById(otpLog.getId()).orElseThrow();
    }

    protected void assertPersistedOtpLogToMatchAllProperties(OtpLog expectedOtpLog) {
        assertOtpLogAllPropertiesEquals(expectedOtpLog, getPersistedOtpLog(expectedOtpLog));
    }

    protected void assertPersistedOtpLogToMatchUpdatableProperties(OtpLog expectedOtpLog) {
        assertOtpLogAllUpdatablePropertiesEquals(expectedOtpLog, getPersistedOtpLog(expectedOtpLog));
    }
}
