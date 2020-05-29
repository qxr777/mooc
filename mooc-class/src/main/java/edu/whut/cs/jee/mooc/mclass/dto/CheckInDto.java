package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.Attendance;
import edu.whut.cs.jee.mooc.mclass.model.CheckIn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDto {

    private Long id;

    @NotNull
    private Long lessonId;

    @NotNull
    private Date deadline;

    private boolean gps;

    private String statusCh;

    /**
     * 签到处 经度
     */
    private Double longitude;

    /**
     * 签到处 纬度
     */
    private Double latitude;

    /**
     * 正常签到数
     */
    private Integer checkedCount;

    /**
     * 旷课数
     */
    private Integer absenceCount;

    /**
     * 迟到数
     */
    private Integer lateCount;

    /**
     * 学生到课记录
     */
    private List<AttendanceDto> attendanceDtos = new ArrayList<>();

    public CheckIn convertTo(){
        CheckIn checkIn = new CheckIn();
        BeanUtils.copyProperties(this, checkIn);
        return checkIn;
    }

    public CheckInDto convertFor(CheckIn checkIn){
        BeanUtils.copyProperties(checkIn,this);
        if(checkIn.getAttendances() != null) {
            for (Attendance attendance : checkIn.getAttendances()) {
                AttendanceDto attendanceDto = new AttendanceDto();
                attendanceDtos.add(attendanceDto.convertFor(attendance));
            }
        }
        this.setStatusCh(CheckIn.STATUS_STRING_CH[checkIn.getStatus()]);
        return this;
    }

}
