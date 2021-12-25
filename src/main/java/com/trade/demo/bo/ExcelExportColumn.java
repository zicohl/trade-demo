package com.trade.demo.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Locale;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/22
 */
@Data
@ToString
@NoArgsConstructor
public class ExcelExportColumn {
    private Map<Locale, String> displayName;
    private String fieldName;
    private String format;
}
