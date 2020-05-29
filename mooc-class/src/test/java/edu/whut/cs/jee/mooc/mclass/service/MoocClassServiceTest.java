package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoocClassServiceTest {

    static Long userId = 1L;
    static Long studentId1 = 2L;
    static Long studentId2 = 3L;
    static Long moocClassId = 1L;
    static Long lessonId = null;

    MoocClassDto moocClass = null;
    LessonDto lesson = null;

    @Resource
    MoocClassService moocClassService;

    @Before
    public void  prepareTestObjects(){
        moocClass = MoocClassDto.builder()
                .name("Java企业版研究生课堂_UNIT_TEST")
                .year("2020")
                .semester("春季")
                .offlineCourse("Java企业版课程_UNIT_TEST")
                .weekday("周三/周五")
                .teacherId(userId)
                .build();

        moocClass = moocClassService.saveMoocClass(moocClass);
        lesson = moocClassService.startLesson(moocClass.getId());
        lessonId = lesson.getId();
    }

    @Test
    public void testGetOwnMoocClasses() {
        Assert.isTrue(moocClassService.getOwnMoocClasses(userId).size() > 0);
    }

    @Test
    public void testEditMoocClss() {
        moocClass.setName(moocClass.getName() + "_UNIT_TEST");
        moocClassService.editMoocClass(moocClass);
    }

    @Test
    public void testJoin() {
        JoinDto joinDto = new JoinDto();
        joinDto.setUserId(studentId1);
        joinDto.setMoocClassCode(moocClass.getCode());
        moocClassService.join(joinDto);
        joinDto.setUserId(studentId2);
        moocClassService.join(joinDto);
        MoocClass joinedClass = moocClassService.getMoocClassByCode(joinDto.getMoocClassCode());
        Assert.isTrue(moocClassService.getUsers(joinedClass.getId()).size() == 2);
    }

    @Test
    public void testStartLesson() {
//        LessonDto lessonDto = moocClassService.startLesson(moocClass.getId());
//        lessonId = lessonDto.getId();
        Assert.isTrue(lesson.getStatus() == Lesson.STATUS_SERVICING);
        Assert.isTrue(moocClassService.isServing(lesson.getMoocClassId()));
    }

    @Test
    public void testEndLesson() {
        moocClassService.endLesson(lessonId);
    }

    @Test
    public void testGetLessons() {
        List<LessonDto> lessonDtos = moocClassService.getLessons(moocClassId);
        for (LessonDto lessonDto : lessonDtos) {
            System.out.println(lessonDto);
        }
    }

    @After
    public void clearTestObjects() {
        moocClassService.removeLesson(lessonId);
        moocClassService.removeCourse(moocClass.getCourseId());
        moocClassService.removeMoocClass(moocClass.getId());
    }


}
