package edu.whut.cs.jee.mooc.common.exception;

import edu.whut.cs.jee.mooc.common.constant.StatusCode;
import lombok.Getter;

@Getter
public enum  AppCode implements StatusCode {

    APP_ERROR(2000, "业务异常"),
    PRICE_ERROR(2001, "价格异常"),
    OVER_DUE_ERROR(2002, "签到已经超过时限"),
    NO_USER_ERROR(2003, "用户不存在");

    private int code;
    private String msg;

    AppCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}