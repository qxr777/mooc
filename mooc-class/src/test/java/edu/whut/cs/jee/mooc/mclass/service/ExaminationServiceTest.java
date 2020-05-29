package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.dto.AnswerDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationRecordDto;
import edu.whut.cs.jee.mooc.mclass.model.Examination;
import edu.whut.cs.jee.mooc.mclass.vo.SubjectVo;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExaminationServiceTest {

    Long studentId1 = 2L;
    Long studentId2 = 3L;
    Long lessonId = 1L;
    Long exerciseId = 1L;
    Long examinationId = null;

    List<Long> examinationRecordIds = new ArrayList<>();

    @Resource
    ExaminationService examinationService;

    @Before
    public void prepareTestObjects() {
        Examination examination = examinationService.importFromExercise(lessonId, exerciseId);
        examinationId = examination.getId();
        examinationService.publishExamination(examinationId, lessonId);

    }

    @After
    public void clearTestObjects() {
        for(Long examinationRecordId : examinationRecordIds) {
            examinationService.removeExaminationRecord(examinationRecordId);
        }
        examinationService.removeExamination(examinationId);
    }

    @Test
    public void testPublishExamination() {
        examinationService.publishExamination(examinationId, lessonId);
        Assert.isTrue(examinationService.getExamination(examinationId).getStatus() == Examination.STATUS_OPEN);
    }

    @Test
    public void testGetExminationDto() {
        ExaminationDto examinationDto = examinationService.getExaminationDto(examinationId);
        log.info(examinationDto.toString());
        Assert.isTrue(examinationDto.getSubjectVos().size() > 0);
    }

    @Test
    public void testSaveExaminationRecord() {
        ExaminationDto examinationDto = examinationService.getExaminationDto(examinationId);
        List<SubjectVo> subjects = examinationDto.getSubjectVos();

        ExaminationRecordDto examinationRecordDto = new ExaminationRecordDto();
        examinationRecordDto.setExaminationId(examinationId);
        examinationRecordDto.setUserId(studentId1);
        AnswerDto answerDto0 = new AnswerDto();
        answerDto0.setSubjectId(subjects.get(0).getId());
        answerDto0.setAnswer("填空题答案_UNIT_TEST");
        examinationRecordDto.addAnswer(answerDto0);
        AnswerDto answerDto1 = new AnswerDto();
        answerDto1.setSubjectId(subjects.get(1).getId());
        answerDto1.setAnswer("B");
        examinationRecordDto.addAnswer(answerDto1);
        AnswerDto answerDto2 = new AnswerDto();
        answerDto2.setSubjectId(subjects.get(2).getId());
        answerDto2.setAnswer("false");
        examinationRecordDto.addAnswer(answerDto2);
        examinationRecordDto = examinationService.saveExaminationRecord(examinationRecordDto);
        examinationRecordIds.add(examinationRecordDto.getId());

        ExaminationRecordDto examinationRecordDto1 = new ExaminationRecordDto();
        examinationRecordDto1.setExaminationId(examinationId);
        examinationRecordDto1.setUserId(studentId2);
        answerDto0 = new AnswerDto();
        answerDto0.setSubjectId(subjects.get(0).getId());
        answerDto0.setAnswer("填空题答案_UNIT_TEST");
        examinationRecordDto1.addAnswer(answerDto0);
        answerDto1 = new AnswerDto();
        answerDto1.setSubjectId(subjects.get(1).getId());
        answerDto1.setAnswer("A,B");
        examinationRecordDto1.addAnswer(answerDto1);
        answerDto2 = new AnswerDto();
        answerDto2.setSubjectId(subjects.get(2).getId());
        answerDto2.setAnswer("false");
        examinationRecordDto1.addAnswer(answerDto2);
        examinationRecordDto1 = examinationService.saveExaminationRecord(examinationRecordDto1);
        examinationRecordIds.add(examinationRecordDto1.getId());
    }

}
