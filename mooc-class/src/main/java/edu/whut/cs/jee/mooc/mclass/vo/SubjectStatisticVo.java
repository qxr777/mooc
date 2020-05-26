package edu.whut.cs.jee.mooc.mclass.vo;

import edu.whut.cs.jee.mooc.common.constant.SubjectConstants;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.model.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectStatisticVo {

    private Long id;
    private String content;
    private Double score;
    private String type;

    private List<OptionStatisticVo> optionStatisticVos = new ArrayList<>();

    public SubjectStatisticVo convertFor(Subject subject){
        BeanUtils.copyProperties(subject,this);
        this.type = subject.getClass().getSimpleName();
        if (subject instanceof Judgment) {
            Judgment judgment = (Judgment) subject;
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
        } else if (subject instanceof Choice) {
            Choice choice = (Choice)subject;
            List<Option> options = choice.getOptions();
            int totalCount = options.stream().mapToInt(Option::getCount).sum();

            OptionStatisticVo optionStatisticVo = null;
            for (Option option : options) {
                optionStatisticVo = BeanConvertUtils.convertTo(option, OptionStatisticVo::new);
                optionStatisticVo.setPercent(optionStatisticVo.getCount() * 100 / totalCount);
                optionStatisticVos.add(optionStatisticVo);
            }
        } else if (subject instanceof Fill) {
            Fill fill = (Fill)subject;
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
