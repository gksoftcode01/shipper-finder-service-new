package ai.yarmook.shipperfinder.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ai.yarmook.shipperfinder.domain.OtpLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OtpLogDTO implements Serializable {

    private Long id;

    @NotNull
    private String mobileNumber;

    private String otpValue;

    private Instant createdDate;

    private Instant sendDate;

    private Integer delivered;

    private Integer verified;

    private Integer triesCount;

    private String response;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getSendDate() {
        return sendDate;
    }

    public void setSendDate(Instant sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getDelivered() {
        return delivered;
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getTriesCount() {
        return triesCount;
    }

    public void setTriesCount(Integer triesCount) {
        this.triesCount = triesCount;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OtpLogDTO)) {
            return false;
        }

        OtpLogDTO otpLogDTO = (OtpLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, otpLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OtpLogDTO{" +
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
