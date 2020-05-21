package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.mclass.model.Judgment;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import edu.whut.cs.jee.mooc.mclass.service.SubjectService;
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
@Api("习题管理")
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("")
    @ApiOperation(value = "新增判断题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "judgment", value = "判断题信息", dataType = "Judgment")
    })
    public Subject saveJudgment(@RequestBody @Valid Judgment judgment) {
        return subjectService.saveSubject(judgment);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除习题", notes = "根据ID删除习题")
    @ApiImplicitParam(name = "id", value = "习题ID", required = true, paramType = "path")
    public String delete(@PathVariable Long id) {
        subjectService.removeSubject(id);
        return "success";
    }

    @GetMapping("")
    @ApiOperation(value = "获取指定随堂练习的所有习题")
    public List<Subject> getSubjectsOfExamination(@RequestParam(value = "examinationId", required = true) Long examinationId) {
        return  subjectService.getSubjectsOfExaminzation(examinationId);
    }

}
