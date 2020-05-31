package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.mclass.dto.ExerciseDto;
import edu.whut.cs.jee.mooc.mclass.service.ExerciseService;
import edu.whut.cs.jee.mooc.mclass.service.SubjectService;
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
@Api("练习库管理")
@RequestMapping("/exercise")
@PreAuthorize("hasRole('TEACHER')")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SubjectService subjectService;

    @PostMapping("")
    @ApiOperation(value = "新增练习")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exerciseDto", value = "练习基本信息", dataType = "ExerciseDto")
    })
    public ExerciseDto save(@RequestBody @Valid ExerciseDto exerciseDto) {
        return exerciseService.saveExercise(exerciseDto);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除练习", notes = "根据ID删除练习")
    @ApiImplicitParam(name = "id", value = "练习ID", required = true, paramType = "path")
    public String delete(@PathVariable Long id) {
        exerciseService.removeExcercise(id);
        return "success";
    }

    @GetMapping("")
    @ApiOperation(value = "获取课程的所有练习")
    public List<ExerciseDto> getExercises(@RequestParam(value = "courseId", required = true) Long courseId) {
        return  exerciseService.getExercises(courseId);
    }

}
