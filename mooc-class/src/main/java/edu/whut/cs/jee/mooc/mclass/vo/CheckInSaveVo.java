package edu.whut.cs.jee.mooc.mclass.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CheckInSaveVo {

    @NotNull
    private Long lessonId;

    @NotNull
    private Date deadline;

    private boolean gps;

    /**
     * 签到处 经度
     */
    private Double longitude;

    /**
     * 签到处 纬度
     */
    private Double latitude;

}
