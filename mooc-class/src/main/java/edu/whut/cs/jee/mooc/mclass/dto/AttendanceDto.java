package edu.whut.cs.jee.mooc.mclass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Builder.Default
    private Double longitude = 0.0;

    /**
     * 签到处 纬度
     */
    @Builder.Default
    private Double latitude = 0.0;

    private String userName;

    private String statusCh;

    private Date createTime;

}
