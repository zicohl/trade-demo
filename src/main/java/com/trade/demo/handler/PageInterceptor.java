package com.trade.demo.handler;

import com.trade.demo.vo.PageVo;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author honglu
 * @since 2021/10/24
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class PageInterceptor implements Interceptor {
    private static final String SUFFIX = "Count";
    private static final int DEFAULT_PAGESIZE = 10;
    private static final int DEFAULT_PAGENUMBER = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) getUnProxyObject(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");

        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        MapperMethod.ParamMap parameterObject = (MapperMethod.ParamMap) boundSql.getParameterObject();

        if (!checkSelect(sql)) {
            return invocation.proceed();
        }

        PageVo pageVo = getPageParam(parameterObject);
        if (pageVo == null) {
            return invocation.proceed();
        }

        if (pageVo.getPageSize() == null) {
            pageVo.setPageSize(DEFAULT_PAGESIZE);
        }
        if (pageVo.getPageNumber() == null) {
            pageVo.setPageNumber(DEFAULT_PAGENUMBER);
        }
        pageVo.setStartIndex((pageVo.getPageNumber() - 1) * pageVo.getPageSize());

        if (pageVo.getTotalRow() != null && pageVo.getTotalRow() > 0) {
            return invocation.proceed();
        }

        int total = getTotal(invocation, metaStatementHandler);
        pageVo.setTotalRow(total);
        pageVo.setTotalPage(total % pageVo.getPageSize() == 0 ? total / pageVo.getPageSize() : total / pageVo.getPageSize() + 1);
        pageVo.setStartIndex(pageVo.getStartIndex() + 1);
        pageVo.setEndIndex(pageVo.getStartIndex() + pageVo.getPageSize() - 1 > total ? total : pageVo.getStartIndex() + pageVo.getPageSize() - 1);

        return invocation.proceed();
    }

    private int getTotal(Invocation invocation, MetaObject metaStatementHandler) throws SQLException {
        MappedStatement currentMappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        Configuration configuration = currentMappedStatement.getConfiguration();

        String countId = metaStatementHandler.getValue("delegate.mappedStatement.id") + SUFFIX;
        MappedStatement countMappedStatement = configuration.getMappedStatement(countId);
        BoundSql countSql = countMappedStatement.getBoundSql(((BoundSql) metaStatementHandler.getValue("delegate.boundSql")).getParameterObject());

        Connection connection = (Connection) invocation.getArgs()[0];
        PreparedStatement countPreparedStatement = null;
        int total = 0;
        try {
            countPreparedStatement = connection.prepareStatement(countSql.getSql());

            ParameterHandler parameterHandler = new DefaultParameterHandler(countMappedStatement, countSql.getParameterObject(), countSql);
            // 设置总数sql参数
            parameterHandler.setParameters(countPreparedStatement);
            ResultSet resultSet = countPreparedStatement.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } finally {
            if (countPreparedStatement != null) {
                countPreparedStatement.close();
            }
        }
        return total;
    }

    private PageVo getPageParam(MapperMethod.ParamMap parameterObject) {
        if (parameterObject == null) {
            return null;
        }
        for (Object param : parameterObject.values()) {
            if (param instanceof PageVo) {
                return (PageVo) param;

            }
        }
        return null;
    }

    private boolean checkSelect(String sql) {
        String trimSql = sql.trim();
        return trimSql.toLowerCase().startsWith("select");
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    private Object getUnProxyObject(Object target) {
        MetaObject metaStatementHandler = SystemMetaObject.forObject(target);

        // 分离代理对象链, 由于目标类可能被多个拦截器拦截, 从而形成多次代理, 通过循环可以分离出最原始的目标类
        Object object = null;
        while (metaStatementHandler.hasGetter("h")) {
            object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }
        if (object == null) {
            return target;
        }
        return object;
    }

}
