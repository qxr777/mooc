package edu.whut.cs.jee.mooc.upms.controller;

import edu.whut.cs.jee.mooc.common.constant.AppConstants;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.upms.dto.RoleDto;
import edu.whut.cs.jee.mooc.upms.dto.StudentDto;
import edu.whut.cs.jee.mooc.upms.dto.TeacherDto;
import edu.whut.cs.jee.mooc.upms.dto.UserDto;
import edu.whut.cs.jee.mooc.upms.security.JwtAuthenticationRequest;
import edu.whut.cs.jee.mooc.upms.service.AuthService;
import edu.whut.cs.jee.mooc.upms.service.UserService;
import edu.whut.cs.jee.mooc.upms.vo.UserSignUpVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public String createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        // Return the token
        return token;
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public String refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        return refreshedToken;
    }

    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userSignUpVo", value = "用户注册信息", dataType = "UserSignUpVo")
    })
    public Long register(@RequestBody @Valid UserSignUpVo userSignUpVo) throws AuthenticationException {
        UserDto userDto = null;
        RoleDto roleDto = null;
        if (userSignUpVo.getRole() == AppConstants.ROLE_TEACHER_ID) {
            userDto = BeanConvertUtils.convertTo(userSignUpVo, TeacherDto::new);
            roleDto = new RoleDto(AppConstants.ROLE_TEACHER_ID);
        } else if (userSignUpVo.getRole() == AppConstants.ROLE_STUDENT_ID) {
            userDto = BeanConvertUtils.convertTo(userSignUpVo, StudentDto::new);
            roleDto = new RoleDto(AppConstants.ROLE_STUDENT_ID);
        }
        userDto.addRole(roleDto);

        return userService.register(userDto);
    }

}
