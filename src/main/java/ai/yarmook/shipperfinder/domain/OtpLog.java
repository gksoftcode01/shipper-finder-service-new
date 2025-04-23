package ai.yarmook.shipperfinder.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OtpLog.
 */
@Entity
@Table(name = "otp_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OtpLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "otp_value")
    private String otpValue;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "send_date")
    private Instant sendDate;

    @Column(name = "delivered")
    private Integer delivered;

    @Column(name = "verified")
    private Integer verified;

    @Column(name = "tries_count")
    private Integer triesCount;

    @Column(name = "response")
    private String response;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OtpLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public OtpLog mobileNumber(String mobileNumber) {
        this.setMobileNumber(mobileNumber);
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtpValue() {
        return this.otpValue;
    }

    public OtpLog otpValue(String otpValue) {
        this.setOtpValue(otpValue);
        return this;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public OtpLog createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getSendDate() {
        return this.sendDate;
    }

    public OtpLog sendDate(Instant sendDate) {
        this.setSendDate(sendDate);
        return this;
    }

    public void setSendDate(Instant sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getDelivered() {
        return this.delivered;
    }

    public OtpLog delivered(Integer delivered) {
        this.setDelivered(delivered);
        return this;
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    public Integer getVerified() {
        return this.verified;
    }

    public OtpLog verified(Integer verified) {
        this.setVerified(verified);
        return this;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getTriesCount() {
        return this.triesCount;
    }

    public OtpLog triesCount(Integer triesCount) {
        this.setTriesCount(triesCount);
        return this;
    }

    public void setTriesCount(Integer triesCount) {
        this.triesCount = triesCount;
    }

    public String getResponse() {
        return this.response;
    }

    public OtpLog response(String response) {
        this.setResponse(response);
        return this;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OtpLog)) {
            return false;
        }
        return getId() != null && getId().equals(((OtpLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OtpLog{" +
            "id=" + getId() +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", otpValue='" + getOtpValue() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", sendDate='" + getSendDate() + "'" +
            ", delivered=" + getDelivered() +
            ", verified=" + getVerified() +
            ", triesCount=" + getTriesCount() +
            ", response='" + getResponse() + "'" +
            "}";
    }
}
