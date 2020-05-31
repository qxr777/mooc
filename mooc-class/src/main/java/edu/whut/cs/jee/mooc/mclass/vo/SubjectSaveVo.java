package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import java.util.List;

/**
 * 保存习题VO
 */
@Data
public class SubjectSaveVo {

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
     * 正确判断结果
     */
    private boolean result;

    /**
     * 数值格式 | 文字格式
     */
    private Integer keyType;

    /**
     * 数值格式答案
     */
    private String decimalKey;

    /**
     * 文字格式答案
     */
    private String textKey;

    /**
     * 匹配类型：精确 | 模糊
     */
    private Integer matchType;

    /**
     * 是否唯一答案
     */
    private boolean unique;
    /**
     * 选择题选项
     */
    private List<OptionSaveVo> options;

    /**
     * 正确答案
     */
    private String key;
}