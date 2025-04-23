package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.OtpLog;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OtpLog} and its DTO {@link OtpLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface OtpLogMapper extends EntityMapper<OtpLogDTO, OtpLog> {}
