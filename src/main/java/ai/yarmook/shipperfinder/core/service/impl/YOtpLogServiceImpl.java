package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.repository.YOtpLogRepository;
import ai.yarmook.shipperfinder.core.service.YOtpLogService;
import ai.yarmook.shipperfinder.core.service.errors.*;
import ai.yarmook.shipperfinder.domain.OtpLog;
import ai.yarmook.shipperfinder.service.OtpLogService;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class YOtpLogServiceImpl implements YOtpLogService {

    private final YOtpLogRepository yOtpLogRepository;
    private final OtpLogService service;

    @Value("${ai.yarmook.otp.max_tries}")
    int otpMaxTries;

    @Value("${ai.yarmook.otp.exp_duration}")
    int otpExpDuration;

    @Value("${ai.yarmook.otp.max_per_day}")
    int otpMaxPerDay;

    @Value("${ai.yarmook.otp.digit_size}")
    int otpDigitSize;

    public YOtpLogServiceImpl(YOtpLogRepository yOtpLogRepository, OtpLogService service) {
        this.yOtpLogRepository = yOtpLogRepository;
        this.service = service;
    }

    public List<OtpLog> getOtpLogsByMobileNumber(String mobileNumber, Instant from, Instant to) {
        return yOtpLogRepository.findByMobileNumberAndCreatedDateBetweenOrderByCreatedDateDesc(mobileNumber, from, to);
    }

    public List<OtpLog> getOtpLogsLastMobileNumber(String mobileNumber) {
        PageRequest request = PageRequest.of(0, 1);
        return yOtpLogRepository.findByMobileNumberAndOtpValueAndVerifiedOrderByCreatedDateDesc(mobileNumber, 0, request);
    }

    public int getAvailableLastDay(String mobileNumber) {
        List<OtpLog> list = getOtpLogsByMobileNumber(mobileNumber, Instant.now(), Instant.now().minus(1, ChronoUnit.DAYS));
        return otpMaxPerDay - list.size();
    }

    @Override
    public OtpLogDTO generateOtp(String mobileNumber) throws OtpExceedingException {
        mobileNumber = mobileNumber.replace("+", "");
        if (getAvailableLastDay(mobileNumber) >= 0) {
            return save(mobileNumber);
        } else {
            throw new OtpExceedingException("");
        }
    }

    @Override
    public boolean validateOtp(String mobileNumber, String otp)
        throws IncorrectOtpException, ExpiredOtpException, NotFoundOtpException, ManyTriesOtpException {
        List<OtpLog> list = getOtpLogsLastMobileNumber(mobileNumber);
        if (list.size() == 1) {
            OtpLog otpLog = list.get(0);
            otpLog.setTriesCount(otpLog.getTriesCount() + 1);
            otpLog = yOtpLogRepository.save(otpLog);
            if (otpLog.getTriesCount() > otpMaxTries) {
                throw new ManyTriesOtpException("");
            } else if (otpLog.getOtpValue().equals(otp)) {
                if (otpLog.getCreatedDate().plus(otpExpDuration, ChronoUnit.MINUTES).isBefore(Instant.now())) {
                    throw new ExpiredOtpException("");
                } else {
                    otpLog.setVerified(1);
                    yOtpLogRepository.save(otpLog);
                    return true;
                }
            } else {
                throw new IncorrectOtpException("");
            }
        } else {
            throw new NotFoundOtpException();
        }
    }

    public OtpLogDTO save(String mobileNumber) {
        OtpLogDTO otpLogDTO = new OtpLogDTO();
        otpLogDTO.setMobileNumber(mobileNumber);
        otpLogDTO.setDelivered(0);
        otpLogDTO.setCreatedDate(Instant.now());
        otpLogDTO.setVerified(0);
        otpLogDTO.setOtpValue(String.valueOf(generateOTP(otpDigitSize)));
        otpLogDTO.setTriesCount(0);
        return service.save(otpLogDTO);
    }

    public int generateOTP(int length) {
        if (length < 1 || length > 9) throw new IllegalArgumentException("OTP length must be between 1 and 9");
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
