package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 向现有课程中添加课堂
 */
@Data
public class MoocClassAddVo {

    /**
     * 名称
     */
    @NotNull(message = "慕课堂名称不允许为空")
    private String name;

    /**
     * 学年
     */
    @NotNull(message = "慕课堂所属学年不允许为空")
    private String year;

    /**
     * 学期
     */
    @NotNull(message = "慕课堂所属学期不允许为空")
    private String semester;

    /**
     * 周几上课
     */
    private String weekday;

    /**
     * 加入的课程
     */
    @NotNull(message = "慕课堂所属课程不允许为空")
    private Long courseId;

}
