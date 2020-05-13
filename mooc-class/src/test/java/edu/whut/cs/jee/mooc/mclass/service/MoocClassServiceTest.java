package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoocClassServiceTest {

    Long userId = 2L;
    Long moocClassId = 1L;

    MoocClassDto moocClass = MoocClassDto.builder()
            .name("Java企业版研究生课堂_UNIT_TEST")
            .year("2020")
            .semester("春季")
            .id(moocClassId)
            .offlineCourse("Java企业版课程_UNIT_TEST")
            .build();
    LessonDto lessonDto = LessonDto.builder()
            .moocClassId(moocClassId)
            .serviceDate(new Date())
            .status(Lesson.STATUS_READY)
            .build();

    @Resource
    MoocClassService moocClassService;

    @Test
    public void  testSaveMoocClass(){
//        MoocClassDto moocClass = MoocClassDto.builder()
//                .name("Java企业版研究生课堂")
//                .year("2020")
//                .semester("春季")
//                .offlineCourse("Java企业版课程")
//                .build();
        moocClass = moocClassService.saveMoocClass(moocClass);
    }

    @Test
    public void testSaveLesson() {
        lessonDto = moocClassService.saveLesson(lessonDto);
//        moocClassService.removeLesson(lessonDto);
    }

    @Test
    public void testRemoveLesson() {
//        lessonDto = moocClassService.saveLesson(lessonDto);
        moocClassService.removeLesson(lessonDto);
    }

    @Test
    public void testJoin() {
        JoinDto joinDto = new JoinDto();
        joinDto.setUserId(userId);
        joinDto.setMoocClassId(moocClass.getId());
        moocClassService.join(joinDto);
    }


}
