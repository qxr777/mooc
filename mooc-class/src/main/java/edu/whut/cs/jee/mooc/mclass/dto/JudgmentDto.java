package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.common.constant.SubjectConstants;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断题
 */
@Data
public class JudgmentDto extends SubjectDto {

    /**
     * 正确判断结果
     */
    private boolean result;

    /**
     * 选T的人数
     */
    private Integer trueCount = 0;

    /**
     * 选F的人数
     */
    private Integer falseCount = 0;

    @Override
    public List<OptionDto> getOptions() {
        List<OptionDto> optionDtos = new ArrayList<>();
        OptionDto trueOptionDto = OptionDto.builder()
                .name(SubjectConstants.TRUE_NAME)
                .content("")
                .build();
        OptionDto falseOptionDto = OptionDto.builder()
                .name(SubjectConstants.FALSE_NAME)
                .content("")
                .build();
        optionDtos.add(trueOptionDto);
        optionDtos.add(falseOptionDto);
        return optionDtos;
    }

    public String getKey() {
        return this.result + "";
    }
}