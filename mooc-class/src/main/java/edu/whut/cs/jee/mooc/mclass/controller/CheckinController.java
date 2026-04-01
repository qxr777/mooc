package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.AttendanceDto;
import edu.whut.cs.jee.mooc.mclass.dto.CheckInDto;
import edu.whut.cs.jee.mooc.mclass.service.CheckInService;
import edu.whut.cs.jee.mooc.mclass.vo.AttendanceSaveVo;
import edu.whut.cs.jee.mooc.mclass.vo.CheckInSaveVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Api("签到管理")
@RequestMapping("/checkin")
public class CheckinController {

    @Autowired
    private CheckInService checkInService;

    @PostMapping("")
    @ApiOperation(value = "新增签到活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "checkInSaveVo", value = "签到基本信息", dataTypeClass = CheckInSaveVo.class)
    })
    @PreAuthorize("hasRole('TEACHER')")
    public Long save(@RequestBody @Valid CheckInSaveVo checkInSaveVo) {
        return checkInService.saveCheckIn(toDto(checkInSaveVo));
    }

    @PostMapping("attend")
    @ApiOperation(value = "学生签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attendanceSaveVo", value = "签到DTO", dataTypeClass = AttendanceSaveVo.class)
    })
    public Long attend(@RequestBody @Valid AttendanceSaveVo attendanceSaveVo) {
        return checkInService.saveAttendance(toDto(attendanceSaveVo));
    }

    @ApiOperation(value = "关闭签到活动", notes = "路径参数ID")
    @PostMapping(value = "{id}/close")
    @PreAuthorize("hasRole('TEACHER')")
    public void close(@PathVariable Long id) {
        checkInService.closeCheckIn(id);
    }

    @ApiOperation(value = "获取签到详细信息", notes = "路径参数ID")
    @GetMapping(value = "{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public CheckInDto detail(@PathVariable Long id) {
        return checkInService.getCheckInDto(id);
    }

    // DTO 转换辅助方法
    private CheckInDto toDto(CheckInSaveVo vo) {
        return BeanConvertUtils.convertTo(vo, CheckInDto::new);
    }

    private AttendanceDto toDto(AttendanceSaveVo vo) {
        return BeanConvertUtils.convertTo(vo, AttendanceDto::new);
    }
}
