package com.trade.demo.util;

import java.util.Date;

/**
 * @author honglu
 * @since 2022/1/1
 */
public class DateUtil {
    private DateUtil() {

    }

    public static Date utcToDate(long seconds) {
        return new Date(seconds * 1000);
    }
}
