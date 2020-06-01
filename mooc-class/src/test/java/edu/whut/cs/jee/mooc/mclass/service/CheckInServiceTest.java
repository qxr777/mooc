package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.constant.MoocClassConstatnts;
import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.mclass.dto.AttendanceDto;
import edu.whut.cs.jee.mooc.mclass.dto.CheckInDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import edu.whut.cs.jee.mooc.mclass.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckInServiceTest {

    Long moocClassId = 1L;
    Long lessonId = null;
    Long studentId1 = 2L;
    Long studentId2 = 3L;
    Long checkInId = 1L;
    LocalDate today = LocalDate.now();
    LocalDateTime dateTime = LocalDateTime.now();
    Instant instant = dateTime.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant();
    Date deadline = Date.from(instant);

    CheckInDto checkInDto = CheckInDto.builder()
            .gps(true)
            .longitude(100.0)
            .latitude(100.0)
            .deadline(deadline)
            .lessonId(lessonId)
            .build();

    @Resource
    CheckInService checkInService;

    @Resource
    MoocClassService moocClassService;

    @Resource
    LessonRepository lessonRepository;

    @Before
    public void prepareTestObjects() {
        List<Lesson> lessons = lessonRepository.findByMoocClassIdAndStatus(moocClassId, MoocClassConstatnts.LESSON_STATUS_SERVICING);
        for(Lesson lesson : lessons) {
            moocClassService.endLesson(lesson.getId());
        }
        LessonDto lessonDto = moocClassService.startLesson(moocClassId);
        lessonId = lessonDto.getId();
        checkInDto.setLessonId(lessonId);
        checkInId = checkInService.saveCheckIn(checkInDto);
    }

    @After
    public void clearTestObjects() {
        moocClassService.removeLesson(lessonId);
    }

    @Test
    public void testSaveAttendance() throws APIException {
        AttendanceDto attendanceDto = AttendanceDto.builder()
                .userId(studentId1)
                .checkInId(checkInId)
                .build();
        checkInService.saveAttendance(attendanceDto);
        attendanceDto = AttendanceDto.builder()
                .userId(studentId2)
                .checkInId(checkInId)
                .build();
        checkInService.saveAttendance(attendanceDto);

    }

    @Test
    public void testCloseCheckIn() throws APIException {
        checkInService.closeCheckIn(checkInId);
    }

    @Test
    public void testGetCheckInDto() {
        checkInDto = checkInService.getCheckInDto(checkInId);
        log.info(checkInDto.toString());
    }

}
