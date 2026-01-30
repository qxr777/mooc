package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 保存填空题VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChoiceSaveVo extends SubjectSaveVo {

    private List<OptionSaveVo> options;

}