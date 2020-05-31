package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.service.MoocClassService;
import edu.whut.cs.jee.mooc.mclass.vo.*;
import edu.whut.cs.jee.mooc.upms.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api("慕课堂管理")
@RequestMapping("/mclass")
public class MoocClassController {

    @Autowired
    private MoocClassService moocClassServicee;

    @PostMapping("")
    @ApiOperation(value = "新增慕课堂")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moocClassNewVo", value = "慕课堂信息", dataType = "MoocClassNewVo")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public Long save(@RequestBody @Valid MoocClassNewVo moocClassNewVo) {
        MoocClassDto moocClassDto = BeanConvertUtils.convertTo(moocClassNewVo, MoocClassDto::new);
        return moocClassServicee.saveMoocClass(moocClassDto);
    }

    @PostMapping("add")
    @ApiOperation(value = "向现有课程中添加慕课堂")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moocClassAddVo", value = "慕课堂信息", dataType = "MoocClassAddVo")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public Long add(@RequestBody @Valid MoocClassAddVo moocClassAddVo) {
        MoocClassDto moocClassDto = BeanConvertUtils.convertTo(moocClassAddVo, MoocClassDto::new);
        return moocClassServicee.addMoocClass(moocClassDto);
    }

    @PostMapping("join")
    @ApiOperation(value = "加入慕课堂")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "joinDto", value = "加入信息", dataType = "JoinDto")
    })
    public String join(@RequestBody @Valid JoinDto joinDto) {
        moocClassServicee.join(joinDto);
        return "success";
    }

    @PostMapping("prepare")
    @ApiOperation(value = "添加备课")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonReadyVo", value = "备课信息", dataType = "LessonReadyVo")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public Long prepare(@RequestBody @Valid LessonReadyVo lessonReadyVo) {
        return moocClassServicee.saveLesson(BeanConvertUtils.convertTo(lessonReadyVo, LessonDto::new));
    }

    @PostMapping("{moocClassId}/start")
    @ApiOperation(value = "开始上课", notes = "路径参数ID")
    @PreAuthorize("hasRole('TEACHER')")
    public LessonDto start(@PathVariable Long moocClassId) {
        return moocClassServicee.startLesson(moocClassId);
    }

    @PutMapping("/lesson/{lessonId}/end")
    @ApiOperation(value = "结束上课", notes = "路径参数ID")
    @PreAuthorize("hasRole('TEACHER')")
    public String endLesson(@PathVariable Long lessonId) {
        moocClassServicee.endLesson(lessonId);
        return "success";
    }

    @ApiOperation(value = "获取上课记录详细信息", notes = "路径参数ID")
    @GetMapping(value = "/lesson/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public LessonDto detailLesson(@PathVariable Long lessonId) {
        return moocClassServicee.getLesson(lessonId);
    }

    @PutMapping("")
    @ApiOperation(value = "编辑慕课堂基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moocClassEditVo", value = "慕课堂信息", dataType = "MoocClassEditVo")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public Long edit(@RequestBody @Valid MoocClassEditVo moocClassEditVo) {
        return moocClassServicee.editMoocClass(BeanConvertUtils.convertTo(moocClassEditVo, MoocClassDto::new));
    }

//    @ApiOperation("获取所有慕课堂列表")
//    @GetMapping(value = "")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<MoocClassDto> list() {
//        return moocClassServicee.getAllMoocClasses();
//    }

    @ApiOperation("获取教师的慕课堂列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teacherId", value = "教师ID", dataType = "Long")
    })
    @GetMapping(value = "own")
    @PreAuthorize("hasRole('TEACHER')")
    public List<MoocClassDto> list(@RequestParam(value = "teacherId", required = true) Long teacherId) {
        return moocClassServicee.getOwnMoocClasses(teacherId);
    }

    @ApiOperation(value = "获取慕课堂详细信息", notes = "路径参数ID")
    @GetMapping(value = "{id}")
    public MoocClassDetailVo detail(@PathVariable Long id) {
        MoocClassDto moocClassDto = moocClassServicee.getMoocClass(id);
        return BeanConvertUtils.convertTo(moocClassDto, MoocClassDetailVo::new);
    }

    @ApiOperation(value = "获取慕课堂的学生", notes = "路径参数ID")
    @GetMapping(value = "{id}/users")
    @PreAuthorize("hasRole('TEACHER')")
    public List<UserDto> userList(@PathVariable Long id) {
        return moocClassServicee.getUserDtos(id);
    }

    @ApiOperation(value = "获取慕课堂的上课记录", notes = "路径参数ID")
    @GetMapping(value = "{id}/lessons")
    public List<LessonDto> lessonList(@PathVariable Long id) {
        return moocClassServicee.getLessons(id);
    }
}
