package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSaveVo {

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

}
