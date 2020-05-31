package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.Data;

import java.util.List;

/**
 * 习题
 */
@Data
public class SubjectDto {

    private Long id;

    /**
     * 练习库中练习
     */
    private Long exerciseId;

    /**
     * 随堂练习
     */
    private Long examinationId;

    /**
     * 题干内容
     */
    private String content;

    /**
     * 分数
     */
    private Double score;

    /**
     * 客观题类型
     */
    private String type;

    /**
     * 正确人数
     */
    private Integer rightCount = 0;

    /**
     * 正确百分比
     */
    private Integer rightPercent;

    /**
     * 错误人数
     */
    private Integer errorCount = 0;

    /**
     * 错误百分比
     */
    private Integer errorPercent;

    /**
     * 选择题选项
     */
    public List<OptionDto> getOptions() {
        return null;
    }

    public String getKey() {
        return null;
    }

}