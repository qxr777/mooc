package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

@Data
public class MoocClassDetailVo {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 学年
     */
    private String year;

    /**
     * 学期
     */
    private String semester;

    /**
     * 周几上课
     */
    private String weekday;

    private String courseName;

    private String teacherName;

}
