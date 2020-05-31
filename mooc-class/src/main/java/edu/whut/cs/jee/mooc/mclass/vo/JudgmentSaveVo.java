package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

/**
 * 保存填空题VO
 */
@Data
public class JudgmentSaveVo extends SubjectSaveVo {

    /**
     * 正确判断结果
     */
    private boolean result;
}