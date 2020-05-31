package edu.whut.cs.jee.mooc.upms.controller;

import edu.whut.cs.jee.mooc.common.constant.AppConstants;
import edu.whut.cs.jee.mooc.common.constant.PageConsts;
import edu.whut.cs.jee.mooc.upms.dto.RoleDto;
import edu.whut.cs.jee.mooc.upms.dto.StudentDto;
import edu.whut.cs.jee.mooc.upms.dto.TeacherDto;
import edu.whut.cs.jee.mooc.upms.dto.UserDto;
import edu.whut.cs.jee.mooc.upms.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api("用户管理")
@RequestMapping("/user")
@PostAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("所有用户列表")
    @GetMapping(value = "")
    public List<UserDto> list() {
        log.info("UserController: info");
        log.error("User Controller : error");
        return userService.getAllUsers();
    }

    @ApiOperation(value = "获取用户详细信息", notes = "路径参数ID")
    @GetMapping(value = "{id}")
    @PostAuthorize("returnObject.name == principal.username or hasRole('ADMIN')")
    public UserDto detail(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/saveStudent")
    @ApiOperation(value = "新增或更新学生用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentDto", value = "学生信息", dataType = "StudentDto")
    })
    public Long saveStudent(@RequestBody @Valid StudentDto studentDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = studentDto.getPassword();
        studentDto.setPassword(encoder.encode(rawPassword));
        RoleDto roleDto = new RoleDto(AppConstants.ROLE_STUDENT_ID);
        studentDto.addRole(roleDto);
        return userService.saveUser(studentDto);
    }

    @PostMapping("/saveTeacher")
    @ApiOperation(value = "新增或更新教师用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teacherDto", value = "教师信息", dataType = "TeacherDto")
    })
    public Long saveTeacher(@RequestBody @Valid TeacherDto teacherDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = teacherDto.getPassword();
        teacherDto.setPassword(encoder.encode(rawPassword));
        if (teacherDto.getRoles().size() == 0) {
            RoleDto roleDto = new RoleDto(AppConstants.ROLE_TEACHER_ID);
            teacherDto.addRole(roleDto);
        }
        return userService.saveUser(teacherDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    public String delete(@PathVariable Long id) {
        userService.removeUser(id);
        return "success";
    }

    @GetMapping("page")
    @ApiOperation(value = "分页获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = PageConsts.PAGE_NUM, value = "分页页码", defaultValue = PageConsts.PAGE_NUM_DEFAULT, dataType = "Integer"),
            @ApiImplicitParam(name = PageConsts.PAGE_SIZE, value = "分页大小", defaultValue = PageConsts.PAGE_SIZE_DEFAULT, dataType = "Integer"),
            @ApiImplicitParam(name = PageConsts.SORT, value = "排序字段", defaultValue = PageConsts.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = PageConsts.ORDER, value = "排序方向", defaultValue = PageConsts.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userDto", value = "用户信息", dataType = "UserDto")
    })
    public Page<UserDto> userList(@RequestParam(value = PageConsts.PAGE_NUM, required = false, defaultValue = PageConsts.PAGE_NUM_DEFAULT) Integer pageNum,
                               @RequestParam(value = PageConsts.PAGE_SIZE, required = false, defaultValue = PageConsts.PAGE_SIZE_DEFAULT) Integer pageSize,
                               @RequestParam(value = PageConsts.SORT, required = false, defaultValue = PageConsts.PAGE_SORT_DEFAULT) String sort,
                               @RequestParam(value = PageConsts.ORDER, required = false, defaultValue = PageConsts.PAGE_ORDER_DEFAULT) String order,
                               UserDto userDto) {

        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sort).descending());

        return userService.getUsersByPage(userDto, pageable);

    }


}
