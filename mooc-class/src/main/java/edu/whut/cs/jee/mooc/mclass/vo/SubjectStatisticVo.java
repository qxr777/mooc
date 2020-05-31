package edu.whut.cs.jee.mooc.mclass.vo;

import edu.whut.cs.jee.mooc.common.constant.SubjectConstants;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectStatisticVo {

    private Long id;
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
     * 正确答案
     */
    private String key;

    private List<OptionStatisticVo> optionStatisticVos = new ArrayList<>();

    public SubjectStatisticVo convertFor(SubjectDto subject){
        BeanUtils.copyProperties(subject,this);
        this.key = subject.getKey();
        if (subject instanceof JudgmentDto) {
            JudgmentDto judgment = (JudgmentDto) subject;
            int totalCount = judgment.getTrueCount() + judgment.getFalseCount();
            // true 选项
            OptionStatisticVo trueOptionStatisticVo = new OptionStatisticVo();
            trueOptionStatisticVo.setName(SubjectConstants.TRUE_NAME);
            trueOptionStatisticVo.setContent(SubjectConstants.TRUE_CONTENT);
            trueOptionStatisticVo.setCount(judgment.getTrueCount());
            trueOptionStatisticVo.setPercent(judgment.getTrueCount() * 100 / totalCount);
            trueOptionStatisticVo.setCorrect(judgment.isResult());
            optionStatisticVos.add(trueOptionStatisticVo);
            // true 选项
            OptionStatisticVo falseOptionStatisticVo = new OptionStatisticVo();
            falseOptionStatisticVo.setName(SubjectConstants.FALSE_NAME);
            falseOptionStatisticVo.setContent(SubjectConstants.FALSE_CONTENT);
            falseOptionStatisticVo.setCount(judgment.getFalseCount());
            falseOptionStatisticVo.setPercent(judgment.getFalseCount() * 100 / totalCount);
            falseOptionStatisticVo.setCorrect(!judgment.isResult());
            optionStatisticVos.add(falseOptionStatisticVo);
        } else if (subject instanceof ChoiceDto) {
            ChoiceDto choice = (ChoiceDto)subject;
            List<OptionDto> options = choice.getOptions();
            int totalCount = options.stream().mapToInt(OptionDto::getCount).sum();
            OptionStatisticVo optionStatisticVo = null;
            for (OptionDto option : options) {
                optionStatisticVo = BeanConvertUtils.convertTo(option, OptionStatisticVo::new);
                optionStatisticVo.setPercent(optionStatisticVo.getCount() * 100 / totalCount);
                optionStatisticVos.add(optionStatisticVo);
            }
        } else if (subject instanceof FillDto) {
            FillDto fill = (FillDto)subject;
            // 填对
            OptionStatisticVo rightOptionStatisticVo = new OptionStatisticVo();
            rightOptionStatisticVo.setName(SubjectConstants.RIGHT_NAME);
            rightOptionStatisticVo.setContent(SubjectConstants.RIGHT_CONTENT);
            rightOptionStatisticVo.setCount(fill.getRightCount());
            rightOptionStatisticVo.setPercent(fill.getRightPercent());
            rightOptionStatisticVo.setCorrect(true);
            optionStatisticVos.add(rightOptionStatisticVo);
            // 填错
            OptionStatisticVo errorOptionStatisticVo = new OptionStatisticVo();
            errorOptionStatisticVo.setName(SubjectConstants.ERROR_NAME);
            errorOptionStatisticVo.setContent(SubjectConstants.ERROR_CONTENT);
            errorOptionStatisticVo.setCount(fill.getErrorCount());
            errorOptionStatisticVo.setPercent(fill.getErrorPercent());
            optionStatisticVos.add(errorOptionStatisticVo);
        }
        return this;
    }
}
