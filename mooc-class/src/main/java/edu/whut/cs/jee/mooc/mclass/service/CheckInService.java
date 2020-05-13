package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.exception.OverDueException;
import edu.whut.cs.jee.mooc.mclass.dto.AttendanceDto;
import edu.whut.cs.jee.mooc.mclass.dto.CheckInDto;
import edu.whut.cs.jee.mooc.mclass.model.Attendance;
import edu.whut.cs.jee.mooc.mclass.model.CheckIn;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import edu.whut.cs.jee.mooc.mclass.repository.AttendanceRepository;
import edu.whut.cs.jee.mooc.mclass.repository.CheckInRepository;
import edu.whut.cs.jee.mooc.mclass.repository.LessonRepository;
import edu.whut.cs.jee.mooc.mclass.repository.MoocClassRepository;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CheckInService {

    @Autowired
    private MoocClassRepository moocClassRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    /**
     * 添加、启动签到
     * @param checkInDto
     * @return
     */
    public CheckInDto saveCheckIn(CheckInDto checkInDto) {
        CheckIn checkIn = checkInDto.convertTo();
        checkIn.setStatus(CheckIn.STATUS_OPEN);
        checkIn = checkInRepository.save(checkIn);
        return checkInDto.convertFor(checkIn);
    }

    /**
     * 学生签到
     * @param attendanceDto
     * @return
     * @throws OverDueException
     */
    public AttendanceDto saveAttendance(AttendanceDto attendanceDto) throws OverDueException {
        Attendance attendance = attendanceDto.convertTo();
        CheckIn checkIn = checkInRepository.findById(attendance.getCheckInId()).get();
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime deadline = LocalDateTime.ofInstant(checkIn.getDeadline().toInstant(), ZoneId.systemDefault());
        if(nowTime.isAfter(deadline)) {
            attendance.setStatus(Attendance.STATUS_LATE);
        } else {
            attendance.setStatus(Attendance.STATUS_CHECKED);
        }
        Lesson lesson = lessonRepository.findById(checkIn.getLessonId()).get();
        if(lesson.getStatus() == Lesson.STATUS_END) {
            throw new OverDueException();
        }

        attendance = attendanceRepository.save(attendance);
        return attendanceDto.convertFor(attendance);
    }

    /**
     * 关闭签到
     * @param checkInDto
     * @return
     */
    public CheckInDto  closeCheckIn(CheckInDto checkInDto) {
        CheckIn checkIn = checkInDto.convertTo();
        checkIn.setStatus(CheckIn.STATUS_CLOSED);

        Lesson lesson = lessonRepository.findById(checkIn.getLessonId()).get();
        MoocClass moocClass = moocClassRepository.findById(lesson.getMoocClassId()).get();
        List<User> users = new ArrayList<User>(moocClass.getUsers());

        // 正常签到
        List<Attendance> checkedAttendances = attendanceRepository.findByCheckInIdAndStatus(checkInDto.getId(), Attendance.STATUS_CHECKED);
        int checkedCount = checkedAttendances.size();
        checkIn.setCheckedCount(checkedCount);
        for(Attendance attendance : checkedAttendances) {
            User user = attendance.getUser();
            users.remove(user);
        }

        // 迟到
        List<Attendance> lateAttendances = attendanceRepository.findByCheckInIdAndStatus(checkInDto.getId(), Attendance.STATUS_LATE);
        int lateCount = lateAttendances.size();
        checkIn.setLateCount(lateCount);
        for(Attendance attendance : lateAttendances) {
            User user = attendance.getUser();
            users.remove(user);
        }

        // 补充缺课记录
        int absenceCount = users.size();
        checkIn.setAbsenceCount(absenceCount);
        Attendance attendance = null;
        for(User user : users) {
            attendance = Attendance.builder()
                    .checkInId(checkIn.getId())
                    .user(user)
                    .status(Attendance.STATUS_ABSENCE)
                    .build();
            attendanceRepository.save(attendance);
        }

        checkIn = checkInRepository.save(checkIn);
        return checkInDto.convertFor(checkIn);
    }

    /**
     * 获取签到详情
     * @param checkInId
     * @return
     */
    public CheckInDto getCheckInDto(Long checkInId) {
        CheckInDto checkInDto = new CheckInDto();
        CheckIn checkIn = checkInRepository.findById(checkInId).get();
        checkInDto.convertFor(checkIn);
        return checkInDto;
    }
}
