package com.trade.demo.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author zicohl
 * Approval
 */

@Data
@ToString
@NoArgsConstructor
public class ApprovalBo {
    private String id;
    private boolean status;
}
