package com.trade.demo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zicohl
 * Article
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBo {
    private String id;
    private String author;
    private String url;
}
