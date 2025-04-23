package ai.yarmook.shipperfinder.core.model.otp;

import java.util.Objects;

public class GenerateOtp {

    public String originator;
    public String recipient;
    public String content;
    public int expiry;
    public String data_coding;

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public String getData_coding() {
        return data_coding;
    }

    public void setData_coding(String data_coding) {
        this.data_coding = data_coding;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenerateOtp that = (GenerateOtp) o;
        return (
            Objects.equals(originator, that.originator) &&
            Objects.equals(recipient, that.recipient) &&
            Objects.equals(content, that.content) &&
            Objects.equals(expiry, that.expiry) &&
            Objects.equals(data_coding, that.data_coding)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(originator, recipient, content, expiry, data_coding);
    }
}
