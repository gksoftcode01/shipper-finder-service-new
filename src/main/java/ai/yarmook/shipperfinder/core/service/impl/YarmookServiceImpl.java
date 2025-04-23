package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.dto.ItemAutoComplete;
import ai.yarmook.shipperfinder.core.service.YarmookService;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class YarmookServiceImpl implements YarmookService {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public YarmookServiceImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> itemAutoCompleteList(String query) {
        String sql =
            """
            select * from (select id, name,NAME_EN as nameEn ,NAME_AR as nameAr ,NAME_URDU as nameUrdu , 1 type  from ITEM_TYPE
             union
            select id, name,NAME_EN as nameEn,NAME_AR as nameAr,NAME_URDU as nameUrdu, 2 type  from ITEM)
            where
               name like '%'||:query||'%' or nameEn like '%'||:query||'%' or nameAr  like '%'||:query||'%' or nameUrdu  like '%'||:query||'%'
            order by name, nameEn, nameAr, nameUrdu
            """;
        SqlParameterSource parameterSource = new MapSqlParameterSource("query", query);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, parameterSource);
        return list;
    }
}
