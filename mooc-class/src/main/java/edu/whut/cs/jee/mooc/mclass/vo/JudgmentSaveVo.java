package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 保存填空题VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JudgmentSaveVo extends SubjectSaveVo {

    /**
     * 正确判断结果
     */
    private boolean result;
}