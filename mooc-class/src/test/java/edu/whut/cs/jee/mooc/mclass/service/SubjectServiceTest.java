package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.constant.SubjectConstants;
import edu.whut.cs.jee.mooc.mclass.dto.ChoiceDto;
import edu.whut.cs.jee.mooc.mclass.dto.FillDto;
import edu.whut.cs.jee.mooc.mclass.dto.JudgmentDto;
import edu.whut.cs.jee.mooc.mclass.dto.OptionDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubjectServiceTest {

    Long exerciseId = 1L;
    Long courseId = 1L;
    Long subjectId = null;

    @Resource
    SubjectService subjectService;

    @Resource
    ExerciseService exerciseService;

    @Before
    public void  prepareTestObjects(){
    }

    @Test
    public void testSaveFill() {
        FillDto fill = new FillDto();
        fill.setExerciseId(exerciseId);
        fill.setContent("填空题题干_UNIT_TEST");
        fill.setUnique(true);
        fill.setScore(10.0);
        fill.setMatchType(SubjectConstants.FILL_MATCH_TYPE_EXACT);
        fill.setKeyType(SubjectConstants.FILL_KEY_TYPE_TEXT);
        fill.setTextKey("填空题答案_UNIT_TEST");
        subjectId = subjectService.saveFill(fill).getId();
        Assert.isTrue(subjectId > 0);
    }

    @Test
    public void testSaveJudgment() {
        JudgmentDto judgment = new JudgmentDto();
        judgment.setExerciseId(exerciseId);
        judgment.setContent("判断题题干_UNIT_TEST");
        judgment.setScore(10.0);
        judgment.setResult(false);
        subjectId = subjectService.saveJudgment(judgment).getId();
        Assert.isTrue(subjectId > 0);
    }

    @Test
    public void testSaveChoice() {
        ChoiceDto choice = new ChoiceDto();
        choice.setExerciseId(exerciseId);
        choice.setScore(10.0);
        choice.setContent("选择题题干_UNIT_TEST");
        List<OptionDto> options = new ArrayList<>();
        OptionDto option1 = OptionDto.builder()
                .name("A")
                .content("选项A_UNIT_TEST")
                .correct(true)
                .count(0)
                .build();
        options.add(option1);
        OptionDto option2 = OptionDto.builder()
                .name("B")
                .content("选项B_UNIT_TEST")
                .correct(true)
                .count(0)
                .build();
        options.add(option2);
        choice.setOptions(options);
        choice = subjectService.saveChoice(choice);
        subjectId = choice.getId();
        Assert.isTrue(subjectId > 0);
    }

    @After
    public void clearTestObjects() {
        subjectService.removeSubject(subjectId);
    }
}
