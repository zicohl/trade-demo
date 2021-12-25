package com.trade.demo.service.excel;


import com.trade.demo.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/19
 */
public interface IExcelDataProvider<T> {
    List<T> getBatchData(PageVo pageVo, Map<String,Object> parameters);
}
