package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.constant.ExaminationConstants;
import edu.whut.cs.jee.mooc.common.constant.MoocClassConstatnts;
import edu.whut.cs.jee.mooc.mclass.dto.AnswerDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationRecordDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.SubjectDto;
import edu.whut.cs.jee.mooc.mclass.model.Course;
import edu.whut.cs.jee.mooc.mclass.model.Exercise;
import edu.whut.cs.jee.mooc.mclass.model.Fill;
import edu.whut.cs.jee.mooc.mclass.repository.CourseRepository;
import edu.whut.cs.jee.mooc.mclass.repository.ExerciseRepository;
import edu.whut.cs.jee.mooc.mclass.repository.LessonRepository;
import edu.whut.cs.jee.mooc.mclass.repository.SubjectRepository;
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
    Long lessonId = null;
    Long exerciseId = null;
    Long examinationId = null;
    Long courseId = 1L;
    Long moocClassId = 1L;

    List<Long> examinationRecordIds = new ArrayList<>();

    @Resource
    ExaminationService examinationService;

    @Resource
    SubjectService subjectService;

    @Resource
    MoocClassService moocClassService;

    @Resource
    ExerciseRepository exerciseRepository;

    @Resource
    CourseRepository courseRepository;

    @Resource
    SubjectRepository subjectRepository;

    @Resource
    LessonRepository lessonRepository;

    @Before
    public void prepareTestObjects() {
        // Cleanup existing lessons first
        List<edu.whut.cs.jee.mooc.mclass.model.Lesson> lessons = lessonRepository.findByMoocClassIdAndStatus(moocClassId, MoocClassConstatnts.LESSON_STATUS_SERVICING);
        for(edu.whut.cs.jee.mooc.mclass.model.Lesson lesson : lessons) {
            moocClassService.endLesson(lesson.getId());
        }

        // Create Lesson
        LessonDto lessonDto = moocClassService.startLesson(moocClassId);
        lessonId = lessonDto.getId();

        // Create Exercise
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            // Should not happen if SQL loaded correctly, but safeguard
            log.error("Course 1 not found");
        }
        Exercise exercise = new Exercise();
        exercise.setName("Test Exercise");
        if (course != null) {
            exercise.setCourseId(course.getId());
        }
        exercise = exerciseRepository.save(exercise);
        exerciseId = exercise.getId();

        // Add some subjects to the exercise?
        // importFromExercise needs subjects?
        // SubjectServiceTest creates subjects.
        // Let's create a dummy subject.
        Fill fill = new Fill();
        fill.setContent("Test Subject");
        fill.setScore(10.0);
        fill.setExerciseId(exercise.getId());
        fill.setTextKey("Key");
        fill.setMatchType(1); // SubjectConstants.FILL_MATCH_TYPE_EXACT
        fill.setKeyType(2);   // SubjectConstants.FILL_KEY_TYPE_TEXT
        subjectRepository.save(fill);

        examinationId = examinationService.importFromExercise(lessonId, exerciseId);
        examinationService.publishExamination(examinationId, lessonId);
    }

    @After
    public void clearTestObjects() {
        if (examinationId != null) {
            for (Long examinationRecordId : examinationRecordIds) {
                try {
                    examinationService.removeExaminationRecord(examinationRecordId);
                } catch (Exception e) {}
            }
            try {
                examinationService.removeExamination(examinationId);
            } catch (Exception e) {}
        }
        if (lessonId != null) {
            moocClassService.removeLesson(lessonId);
        }
        if (exerciseId != null) {
            subjectRepository.deleteAll(); // Cascade?
            exerciseRepository.deleteById(exerciseId);
        }
    }

    @Test
    public void testPublishExamination() {
        examinationService.publishExamination(examinationId, lessonId);
        Assert.isTrue(examinationService.getExamination(examinationId).getStatus() == ExaminationConstants.EXAMINATION_STATUS_OPEN);
    }

    @Test
    public void testSaveExaminationRecord() {
        List<SubjectDto> subjects = subjectService.getSubjectsOfExaminzation(examinationId);
        if (subjects.isEmpty()) {
            return; // Should fail?
        }

        ExaminationRecordDto examinationRecordDto = new ExaminationRecordDto();
        examinationRecordDto.setExaminationId(examinationId);
        examinationRecordDto.setUserId(studentId1);
        AnswerDto answerDto0 = new AnswerDto();
        answerDto0.setSubjectId(subjects.get(0).getId());
        answerDto0.setAnswer("填空题答案_UNIT_TEST");
        examinationRecordDto.addAnswer(answerDto0);

        // Remove other answers as we only created one subject
        /*
        AnswerDto answerDto1 = new AnswerDto();
        answerDto1.setSubjectId(subjects.get(1).getId());
        answerDto1.setAnswer("B");
        examinationRecordDto.addAnswer(answerDto1);
        AnswerDto answerDto2 = new AnswerDto();
        answerDto2.setSubjectId(subjects.get(2).getId());
        answerDto2.setAnswer("false");
        examinationRecordDto.addAnswer(answerDto2);
        */

        examinationRecordDto = examinationService.saveExaminationRecord(examinationRecordDto);
        examinationRecordIds.add(examinationRecordDto.getId());

        /*
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
        */
    }

}
