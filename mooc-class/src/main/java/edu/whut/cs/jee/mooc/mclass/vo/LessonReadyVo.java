package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import java.util.Date;

/**
 * 添加备课VO
 */
@Data
public class LessonReadyVo {
    private Long moocClassId;
    private Date serviceDate;
}
