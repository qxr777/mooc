package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.constant.AppConstants;
import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.common.util.LocationUtils;
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
import org.springframework.transaction.annotation.Transactional;

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
        Lesson lesson = lessonRepository.findById(checkInDto.getLessonId()).get();
        if (lesson.getCheckIn() != null && lesson.getCheckIn().getStatus() == CheckIn.STATUS_OPEN) {
            throw new APIException(AppCode.HAS_OPENING_CHECKIN, AppCode.HAS_OPENING_CHECKIN.getMsg() + lesson.getCheckIn().getId());
        }

        CheckIn checkIn = checkInDto.convertTo();
        checkIn.setStatus(CheckIn.STATUS_OPEN);
        checkIn = checkInRepository.save(checkIn);

        // 将所有学生标记未签到
        if (lesson.getCheckIn() != null && lesson.getCheckIn().getStatus() == CheckIn.STATUS_CLOSED) {
            List<Attendance> attendances = attendanceRepository.findByCheckInId(lesson.getCheckIn().getId());
            attendanceRepository.deleteAll(attendances);
            checkInRepository.delete(lesson.getCheckIn());
        }
        lesson.setCheckIn(checkIn);
        MoocClass moocClass = moocClassRepository.findById(lesson.getMoocClassId()).get();
        List<User> users = new ArrayList<User>(moocClass.getUsers());
        for(User user : users) {
            Attendance attendance = Attendance.builder()
                    .checkInId(checkIn.getId())
                    .status(Attendance.STATUS_ABSENCE)
                    .user(user)
                    .build();
            attendanceRepository.save(attendance);
        }
        return checkInDto.convertFor(checkIn);
    }

    /**
     * 学生签到
     * @param attendanceDto
     * @return
     * @throws APIException
     */
    public AttendanceDto saveAttendance(AttendanceDto attendanceDto) throws APIException {
        Attendance attendance = attendanceRepository.findByCheckInIdAndUserId(attendanceDto.getCheckInId(), attendanceDto.getUserId()).get(0);
        CheckIn checkIn = checkInRepository.findById(attendance.getCheckInId()).get();
        //签到地点判断
        Double latitude = attendanceDto.getLatitude();
        Double longitude = attendanceDto.getLongitude();
        Double centerLatitude = checkIn.getLatitude();
        Double centerLongitude = checkIn.getLongitude();
        if (checkIn.isGps() && latitude != null && longitude != null) {
            double distance = LocationUtils.getDistance(latitude, longitude, centerLatitude, centerLongitude);
            if (distance > AppConstants.MAX_DISTANCE_RANGE) {
                throw new APIException(AppCode.OVER_RANGE_ERROR, AppCode.OVER_RANGE_ERROR.getMsg() + AppConstants.MAX_DISTANCE_RANGE + AppConstants.DISTANCE_UNIT);
            }
        }
        // 签到时限判断
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime deadline = LocalDateTime.ofInstant(checkIn.getDeadline().toInstant(), ZoneId.systemDefault());
        if(nowTime.isAfter(deadline)) {
            attendance.setStatus(Attendance.STATUS_LATE);
        } else {
            attendance.setStatus(Attendance.STATUS_CHECKED);
        }
        Lesson lesson = lessonRepository.findById(checkIn.getLessonId()).get();
        if(lesson.getStatus() == Lesson.STATUS_END || checkIn.getStatus() == CheckIn.STATUS_CLOSED) {
            throw new APIException(AppCode.OVER_DUE_ERROR, AppCode.OVER_DUE_ERROR.getMsg());
        }

        attendance = attendanceRepository.save(attendance);
        return attendanceDto.convertFor(attendance);
    }

    /**
     * 关闭签到
     * @param checkInId
     * @return
     */
    public void closeCheckIn(Long checkInId) {
        CheckIn checkIn = checkInRepository.findById(checkInId).get();
        checkIn.setStatus(CheckIn.STATUS_CLOSED);
        checkIn = this.updateCount(checkIn);
        checkInRepository.save(checkIn);
    }

    /**
     * 更新签到活动统计数据
     * @param checkIn
     * @return
     */
    private CheckIn updateCount(CheckIn checkIn) {
        // 正常签到
        List<Attendance> checkedAttendances = attendanceRepository.findByCheckInIdAndStatus(checkIn.getId(), Attendance.STATUS_CHECKED);
        int checkedCount = checkedAttendances.size();
        checkIn.setCheckedCount(checkedCount);

        // 迟到
        List<Attendance> lateAttendances = attendanceRepository.findByCheckInIdAndStatus(checkIn.getId(), Attendance.STATUS_LATE);
        int lateCount = lateAttendances.size();
        checkIn.setLateCount(lateCount);

        // 缺课记录
        List<Attendance> absenceAttendances = attendanceRepository.findByCheckInIdAndStatus(checkIn.getId(), Attendance.STATUS_ABSENCE);
        int absenceCount = absenceAttendances.size();
        checkIn.setAbsenceCount(absenceCount);

        return checkIn;
    }

    /**
     * 获取签到详情
     * @param checkInId
     * @return
     */
    @Transactional(readOnly = true)
    public CheckInDto getCheckInDto(Long checkInId) {
        CheckInDto checkInDto = new CheckInDto();
        CheckIn checkIn = checkInRepository.findById(checkInId).get();
        checkIn = this.updateCount(checkIn);
        checkInDto.convertFor(checkIn);
        return checkInDto;
    }

    public void removeCheckIn(Long checkInId) {
        checkInRepository.deleteById(checkInId);
    }
}
