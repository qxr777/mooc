package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.service.MoocClassService;
import edu.whut.cs.jee.mooc.upms.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api("慕课堂管理")
@RequestMapping("/mclass")
public class MoocClassController {

    @Autowired
    private MoocClassService moocClassServicee;

    @ApiOperation("所有慕课堂列表")
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
