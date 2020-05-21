package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.mclass.model.Examination;
import edu.whut.cs.jee.mooc.mclass.service.ExaminationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api("随堂练习管理")
@RequestMapping("/examination")
public class ExaminationController {

    @Autowired
    private ExaminationService examinationService;

    @PostMapping("/importFromExercise")
    @ApiOperation(value = "从练习库导入随堂练习")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonId", value = "上课ID", dataType = "Long"),
            @ApiImplicitParam(name = "exerciseId", value = "练习ID", dataType = "Long")
    })
    public Examination importFromExercise(@RequestParam(value = "lessonId", required = true) Long lessonId,
                                  @RequestParam(value = "exerciseId", required = true) Long exerciseId) {
        return examinationService.importFromExercise(lessonId, exerciseId);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除随堂练习", notes = "根据ID删除随堂练习")
    @ApiImplicitParam(name = "id", value = "随堂练习ID", required = true, paramType = "path")
    public String delete(@PathVariable Long id) {
        examinationService.removeExamination(id);
        return "success";
    }

}
