package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 保存填空题VO
 */
@Data
public class FillSaveVo extends SubjectSaveVo {

    /**
     * 数值格式 | 文字格式
     */
    @NotNull
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
    @NotNull
    private Integer matchType;

    /**
     * 是否唯一答案
     */
    @NotNull
    private boolean unique;

}