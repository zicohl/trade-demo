package com.trade.demo.vo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author honglu
 * @since 2021/10/24
 */

@Data
@ToString
@NoArgsConstructor
public class Pager<T> {
    private PageVo pageVo;
    private List<T> results;
}
