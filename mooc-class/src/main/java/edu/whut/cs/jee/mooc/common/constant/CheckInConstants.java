package edu.whut.cs.jee.mooc.common.constant;

public class CheckInConstants {

    public static final int ATTENDANCE_STATUS_CHECKED = 1; // 已签到
    public static final int ATTENDANCE_STATUS_LATE = 2;  // 迟到
    public static final int ATTENDANCE_STATUS_ABSENCE = 3; // 缺课

    public static final String[] ATTENDANCE_STATUS_STRING_CH={"未签到", "已签到", "迟到", "缺课"};

    public static final int CHECK_IN_STATUS_OPEN = 1;
    public static final int CHECK_IN_STATUS_CLOSED = 2;

    public static final String[] CHECK_IN_STATUS_STRING_CH={"未知", "开放", "关闭"};
}
