package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.dto.AnswerDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationRecordDto;
import edu.whut.cs.jee.mooc.mclass.model.Examination;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExaminationServiceTest {

    Long userId = 1L;
    Long studentId1 = 2L;
    Long studentId2 = 3L;
    Long lessonId = 1L;
    Long exerciseId = 1L;
    Long examinationId = 1L;
    Examination examination = Examination.builder()
            .lessonId(lessonId)
            .name("课程引论随堂练习_UNIT_TEST")
            .build();

    @Resource
    ExaminationService examinationService;

    @Test
    public void testSaveExamination() {
        examinationService.saveExamination(examination);
    }

    @Test
    public void testImportFromExercise() {
        examinationService.importFromExercise(lessonId, exerciseId);
    }

    @Test
    public void testRemoveExamination() {
        examinationService.removeExamination(examinationId);
    }

    @Test
    public void testPublishExamination() {
        examinationService.publishExamination(examinationId);
    }

    @Test
    public void testGetExminationDto() {
        ExaminationDto examinationDto = examinationService.getExaminationDto(examinationId);
        log.info(examinationDto.toString());
    }

    @Test
    public void testSaveExaminationRecord() {
        ExaminationDto examinationDto = examinationService.getExaminationDto(examinationId);
        List<Subject> subjects = examinationDto.getSubjects();

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
        examinationService.saveExaminationRecord(examinationRecordDto);

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

        examinationService.saveExaminationRecord(examinationRecordDto1);
    }
}
