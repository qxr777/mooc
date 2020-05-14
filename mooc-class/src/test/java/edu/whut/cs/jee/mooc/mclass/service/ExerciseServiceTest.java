package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.model.Exercise;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExerciseServiceTest {

    Long courseId = 1L;
    Long exerciseId = 1L;
    Exercise exercise = Exercise.builder()
            .courseId(courseId)
            .name("课程引论练习")
            .build();

    @Resource
    ExerciseService exerciseService;

    @Test
    public void testSaveExercise() {
        exerciseService.saveExercise(exercise);
    }

    @Test
    public void testRemoveExercise() {
        exerciseService.removeExcercise(exerciseId);
    }

}
