package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.model.otp.GenerateOtp;
import ai.yarmook.shipperfinder.core.service.OTPService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public String generateOtp(String mobileNo) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            GenerateOtp generateOtp = new GenerateOtp();
            generateOtp.setOriginator("SignOTP");
            generateOtp.setRecipient(mobileNo.startsWith("+") ? mobileNo : "+".concat(mobileNo));
            generateOtp.setContent("Greetings from D7 API, your mobile verification code is: {}");
            generateOtp.setExpiry(600);
            generateOtp.setData_coding("text");
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            okhttp3.RequestBody body = RequestBody.create(ow.writeValueAsString(generateOtp), JSON);
            Request request = new Request.Builder()
                .url("https://api.d7networks.com/verify/v1/otp/send-otp")
                .method("POST", body)
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhdXRoLWJhY2tlbmQ6YXBwIiwic3ViIjoiMzU3NmQ4MTEtNDAzZi00YTcyLWJhMmEtYmVkZWFiNGQ1OTgwIn0.QLyjUGYaZ-NrZnh7RznYAcRnjIjlr9KmCspBvM2DDf8"
                )
                .addHeader("Content-Type", "application/json")
                .build();
            Response response = client.newCall(request).execute();
            response.body().close();
            return response.body().string();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String resendOtp(String otp_id) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create("{\"otp_id\":\"" + "otp_id" + "\"}", JSON);
        Request request = new Request.Builder()
            .url("https://api.d7networks.com/verify/v1/otp/resend-otp")
            .method("POST", body)
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhdXRoLWJhY2tlbmQ6YXBwIiwic3ViIjoiMzU3NmQ4MTEtNDAzZi00YTcyLWJhMmEtYmVkZWFiNGQ1OTgwIn0.QLyjUGYaZ-NrZnh7RznYAcRnjIjlr9KmCspBvM2DDf8"
            )
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
        response.body().close();
        return response.body().string();
    }

    @Override
    public String verifyOtp(String otp_id, String otp_code) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create("{\"otp_id\":\"" + otp_id + "\",\"otp_code\":" + otp_code + "}", JSON);
        Request request = new Request.Builder()
            .url("https://api.d7networks.com/verify/v1/otp/verify-otp")
            .method("POST", body)
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhdXRoLWJhY2tlbmQ6YXBwIiwic3ViIjoiMzU3NmQ4MTEtNDAzZi00YTcyLWJhMmEtYmVkZWFiNGQ1OTgwIn0.QLyjUGYaZ-NrZnh7RznYAcRnjIjlr9KmCspBvM2DDf8"
            )
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
        response.body().close();
        return response.body().string();
    }
}
