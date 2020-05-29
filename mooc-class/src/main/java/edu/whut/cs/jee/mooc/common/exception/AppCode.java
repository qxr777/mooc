package edu.whut.cs.jee.mooc.common.exception;

import edu.whut.cs.jee.mooc.common.constant.StatusCode;
import lombok.Getter;

@Getter
public enum  AppCode implements StatusCode {

    APP_ERROR(2000, "业务异常"),
    PRICE_ERROR(2001, "价格异常"),
    OVER_DUE_ERROR(2002, "签到已经关闭"),
    USERNAME_DUPLICATE_ERROR(2004, "用户名已经存在"),
    USER_HAS_JOINED_ERROR(2005, "用户已经加入此慕课堂"),
    NO_SUBJECT_ERROR(2006, "习题不存在"),
    HAS_SERVING_LESSON(2007, "正在上课中"),
    HAS_OPENING_CHECKIN(2008, "正在签到中"),
    OVER_RANGE_ERROR(2009, "超过GPS签到中心偏差距离"),
    EXAMINATION_STATUS_ERROR(2010, "此随堂测试状态异常"),
    NO_MCLASS_ERROR(2011, "慕课堂不存在"),
    NO_COURSE_ERROR(2012, "课程不存在"),
    NO_USER_ERROR(2003, "用户不存在");

    private int code;
    private String msg;

    AppCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}