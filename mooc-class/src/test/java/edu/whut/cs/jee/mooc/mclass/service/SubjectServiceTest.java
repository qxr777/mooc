package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.model.Choice;
import edu.whut.cs.jee.mooc.mclass.model.Fill;
import edu.whut.cs.jee.mooc.mclass.model.Judgment;
import edu.whut.cs.jee.mooc.mclass.model.Option;
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
        Fill fill = new Fill();
        fill.setExerciseId(exerciseId);
        fill.setContent("填空题题干_UNIT_TEST");
        fill.setUnique(true);
        fill.setScore(10.0);
        fill.setMatchType(Fill.MATCH_TYPE_EXACT);
        fill.setKeyType(Fill.KEY_TYPE_TEXT);
        fill.setTextKey("填空题答案_UNIT_TEST");
        subjectId = subjectService.saveSubject(fill).getId();
        Assert.isTrue(subjectId > 0);
    }

    @Test
    public void testSaveJudgment() {
        Judgment judgment = new Judgment();
        judgment.setExerciseId(exerciseId);
        judgment.setContent("判断题题干_UNIT_TEST");
        judgment.setScore(10.0);
        judgment.setResult(false);
        subjectId = subjectService.saveSubject(judgment).getId();
        Assert.isTrue(subjectId > 0);
    }

    @Test
    public void testSaveChoice() {
        Choice choice = new Choice();
        choice.setExerciseId(exerciseId);
        choice.setScore(10.0);
        choice.setContent("选择题题干_UNIT_TEST");
        List<Option> options = new ArrayList<Option>();
        Option option1 = Option.builder()
                .name("A")
                .content("选项A_UNIT_TEST")
                .correct(true)
                .count(0)
                .build();
        options.add(option1);
        Option option2 = Option.builder()
                .name("B")
                .content("选项B_UNIT_TEST")
                .correct(true)
                .count(0)
                .build();
        options.add(option2);
        choice.setOptions(options);
        choice = (Choice)subjectService.saveChoice(choice);
        subjectId = choice.getId();
        Assert.isTrue(subjectId > 0);
    }

    @After
    public void clearTestObjects() {
        subjectService.removeSubject(subjectId);
    }
}
