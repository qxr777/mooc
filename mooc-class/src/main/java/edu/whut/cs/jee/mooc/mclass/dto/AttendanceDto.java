package edu.whut.cs.jee.mooc.mclass.dto;

import edu.whut.cs.jee.mooc.mclass.model.Attendance;
import edu.whut.cs.jee.mooc.upms.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {

    private Long checkInId;

    private Long userId;

    /**
     * 签到处 经度
     */
    private Double longitude = 0.0;

    /**
     * 签到处 纬度
     */
    private Double latitude = 0.0;

    private String userName;

    private String statusCh;

    private Date createTime;

    public Attendance convertTo(){
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(this, attendance);
        User user = new User();
        user.setId(this.userId);
        attendance.setUser(user);
        return attendance;
    }

    public AttendanceDto convertFor(Attendance attendance){
        BeanUtils.copyProperties(attendance,this);
        this.setUserId(attendance.getUser().getId());
        this.setUserName(attendance.getUser().getName());
        this.setStatusCh(Attendance.STATUS_STRING_CH[attendance.getStatus()]);
        return this;
    }
}
