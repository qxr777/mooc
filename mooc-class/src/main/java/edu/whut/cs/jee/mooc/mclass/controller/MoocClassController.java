package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.service.MoocClassService;
import edu.whut.cs.jee.mooc.upms.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
            @ApiImplicitParam(name = "moocClassDto", value = "慕课堂信息", dataType = "MoocClassDto")
    })
    public MoocClassDto save(@RequestBody @Valid MoocClassDto moocClassDto) {
        return moocClassServicee.saveMoocClass(moocClassDto);
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
            @ApiImplicitParam(name = "lessonDto", value = "备课信息", dataType = "LessonDto")
    })
    public LessonDto prepare(@RequestBody @Valid LessonDto lessonDto) {
        return moocClassServicee.saveLesson(lessonDto);
    }

    @PutMapping("")
    @ApiOperation(value = "编辑慕课堂基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moocClassDto", value = "慕课堂信息", dataType = "MoocClassDto")
    })
    public MoocClassDto edit(@RequestBody @Valid MoocClassDto moocClassDto) {
        return moocClassServicee.editMoocClass(moocClassDto);
    }

    @ApiOperation("获取所有慕课堂列表")
    @GetMapping(value = "")
    public List<MoocClassDto> list() {
        return moocClassServicee.getAllMoocClasses();
    }

    @ApiOperation(value = "获取慕课堂详细信息", notes = "路径参数ID")
    @GetMapping(value = "{id}")
    public MoocClassDto detail(@PathVariable Long id) {
        return moocClassServicee.getMoocClass(id);
    }

    @ApiOperation(value = "获取慕课堂的学生", notes = "路径参数ID")
    @GetMapping(value = "{id}/users")
    public List<User> userList(@PathVariable Long id) {
        return moocClassServicee.getUsers(id);
    }

    @ApiOperation(value = "获取慕课堂的上课记录", notes = "路径参数ID")
    @GetMapping(value = "{id}/lessons")
    public List<LessonDto> lessonList(@PathVariable Long id) {
        return moocClassServicee.getLessons(id);
    }
}
