package edu.whut.cs.jee.mooc.common.exception;

/**
 * @author qixin
 */
public class OverDueException extends BaseException {

    /**
     * 签到超过截止时间时抛出异常
     */
    private static final long serialVersionUID = 6652098398262607292L;


    public OverDueException(){
        super.message = CHECK_IN_OVER_DUE;
    }

    public static String CHECK_IN_OVER_DUE = "签到已经超过时限！！";

}