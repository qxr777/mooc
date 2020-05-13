package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.exception.OverDueException;
import edu.whut.cs.jee.mooc.mclass.dto.AttendanceDto;
import edu.whut.cs.jee.mooc.mclass.dto.CheckInDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckInServiceTest {

    LocalDate today = LocalDate.now();
    LocalDateTime dateTime = LocalDateTime.now();
    Instant instant = dateTime.plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant();
    Date deadline = Date.from(instant);

    CheckInDto checkInDto = CheckInDto.builder()
            .gps(true)
            .longitude(100.0)
            .latitude(100.0)
            .deadline(deadline)
            .lessonId(1L)
            .build();

    @Resource
    CheckInService checkInService;

    @Test
    public void testSaveCheckIn() throws ParseException {
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd ");
//        String date = time.format(new Date());
//        date += "15:20";
//        Date deadline = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);

        checkInDto = checkInService.saveCheckIn(checkInDto);
    }

    @Test
    public void testSaveAttendance() throws ParseException, OverDueException {
        testSaveCheckIn();

        AttendanceDto attendanceDto = AttendanceDto.builder()
                .userId(1L)
                .checkInId(checkInDto.getId())
                .build();
        checkInService.saveAttendance(attendanceDto);

    }

    @Test
    public void testCloseCheckIn() throws ParseException, OverDueException {
        testSaveAttendance();

        checkInService.closeCheckIn(checkInDto);
    }

    @Test
    public void testGetCheckInDto() {
        checkInDto = checkInService.getCheckInDto(1L);
    }

    public void dummy() {
    }
}
