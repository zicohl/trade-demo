package com.trade.demo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@MappedTypes({Long.class})
@MappedJdbcTypes({JdbcType.TIMESTAMP})
public class TimestampLongHandler extends BaseTypeHandler<Long> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setTimestamp(i, new Timestamp(parameter * 1000));
        }
    }

    @Override
    public Long getNullableResult(ResultSet rs, String column) throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        if (ts != null) {
            return ts.getTime() / 1000;
        } else {
            return null;
        }
    }

    @Override
    public Long getNullableResult(ResultSet rs, int column) throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        if (ts != null) {
            return ts.getTime() / 1000;
        } else {
            return null;
        }
    }

    @Override
    public Long getNullableResult(CallableStatement cs, int column) throws SQLException {
        Timestamp ts = cs.getTimestamp(column);
        if (ts != null) {
            return ts.getTime() / 1000;
        } else {
            return null;
        }
    }
}
