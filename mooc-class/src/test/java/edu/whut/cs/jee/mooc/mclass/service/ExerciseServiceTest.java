package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.dto.ExerciseDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExerciseServiceTest {

    Long courseId = 1L;
    Long exerciseId = 1L;
    ExerciseDto exerciseDto = null;

    @Resource
    ExerciseService exerciseService;

    @Before
    public void  prepareTestObjects(){
        exerciseDto = ExerciseDto.builder()
                .courseId(courseId)
                .name("课程引论练习_UNIT_TEST")
                .build();

        exerciseDto = exerciseService.saveExercise(exerciseDto);
    }

    @Test
    public void testSaveExercise() {
        Assert.isTrue(exerciseDto.getId() > 0);
    }

    @After
    public void clearTestObjects() {
        exerciseService.removeExcercise(exerciseDto.getId());
    }

}
