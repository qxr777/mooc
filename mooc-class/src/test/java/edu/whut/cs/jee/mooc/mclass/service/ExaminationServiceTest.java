package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.model.Examination;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ExaminationServiceTest {

    Long userId = 1L;
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
    public void testSaveExaminationRecord() {
        Examination examination = examinationService.getExamination(examinationId);

    }
}
