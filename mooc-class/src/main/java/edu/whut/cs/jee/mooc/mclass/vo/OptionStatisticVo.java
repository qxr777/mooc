package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

@Data
public class OptionStatisticVo {

    /**
     * 选项名
     */
    private String name;
    /**
     * 选项内容
     */
    private String content;
    /**
     * 选择人数
     */
    private Integer count = 0;

    /**
     * 选择百分数
     */
    private Integer percent = 0;

    private boolean correct;
}
