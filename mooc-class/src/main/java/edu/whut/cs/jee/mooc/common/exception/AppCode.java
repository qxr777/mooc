package edu.whut.cs.jee.mooc.common.exception;

import edu.whut.cs.jee.mooc.common.constant.StatusCode;
import lombok.Getter;

@Getter
public enum  AppCode implements StatusCode {

    APP_ERROR(2000, "业务异常"),
    PRICE_ERROR(2001, "价格异常"),
    OVER_DUE_ERROR(2002, "签到已经超过时限"),
    USERNAME_DUPLICATE_ERROR(2004, "用户名已经存在"),
    USER_HAS_JOINED_ERROR(2005, "用户已经加入此慕课堂"),
    NO_SUBJECT_ERROR(2006, "习题不存在"),
    NO_USER_ERROR(2003, "用户不存在");

    private int code;
    private String msg;

    AppCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}