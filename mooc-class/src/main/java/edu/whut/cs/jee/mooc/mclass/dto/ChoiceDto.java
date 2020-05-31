package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.Data;

import java.util.List;

/**
 * 选择题
 */
@Data
public class ChoiceDto extends SubjectDto {

    /**
     * 候选项
     */
    private List<OptionDto> options;

    public String getKey() {
        String key = "";
        for (OptionDto optionDto : options) {
            if (optionDto.isCorrect()) {
                key += optionDto.getName() + ",";
            }
        }
        return key.length() > 0 ? key.substring(0, key.length() - 1) : "";
    }

}