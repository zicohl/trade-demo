package com.trade.demo.exception;

public enum ResponseCode {
    INTERNAL_ERROR("errorcode.internalError","Internal error"),
    TRADE_EXIST_ERROR("errorcode.tradeExistError",""),
    TRADE_VERSION_ERROR("errorcode.tradeVersionError",""),
    TRADE_STATUS_ERROR("errorcode.tradeStatusError",""),
    REPORT_COMPILE_ERROR("errorcode.compileReportTemplateError",""),
    EXCEL_EXPORT_ERROR("errorcode.excelExportError","");

    private String code;
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public  String getMessage() {
        return message;
    }
}
