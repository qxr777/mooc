package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.common.constant.AppConstants;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationRecordDto;
import edu.whut.cs.jee.mooc.mclass.service.ExaminationService;
import edu.whut.cs.jee.mooc.mclass.vo.ExaminationRecordVo;
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
@Api("随堂测试管理")
@RequestMapping("/examination")
public class ExaminationController {

    @Autowired
    private ExaminationService examinationService;


    @PostMapping("/importFromExercise")
    @ApiOperation(value = "从练习库导入随堂测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonId", value = "上课ID", dataType = "Long"),
            @ApiImplicitParam(name = "exerciseId", value = "练习ID", dataType = "Long")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public Long importFromExercise(@RequestParam(value = "lessonId", required = true) Long lessonId,
                                          @RequestParam(value = "exerciseId", required = true) Long exerciseId) {
        return examinationService.importFromExercise(lessonId, exerciseId);
    }

    @PostMapping("/publish")
    @ApiOperation(value = "发布未公开的随堂测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonId", value = "上课ID", dataType = "Long"),
            @ApiImplicitParam(name = "examinationId", value = "随堂测试ID", dataType = "Long")
    })
    @PreAuthorize("hasRole('TEACHER')")
    public String publish(@RequestParam(value = "lessonId", required = true) Long lessonId,
                                          @RequestParam(value = "examinationId", required = true) Long examinationId) {
        examinationService.publishExamination(examinationId, lessonId);
        return AppConstants.SUCCESS;
    }

    @PostMapping("submit")
    @ApiOperation(value = "学生提交随堂测试答题卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examinationRecordDto", value = "随堂测试答题卡", dataType = "ExaminationRecordDto")
    })
    public ExaminationRecordDto submit(@RequestBody @Valid ExaminationRecordDto examinationRecordDto) {
        return examinationService.saveExaminationRecord(examinationRecordDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除随堂测试", notes = "根据ID删除随堂测试")
    @ApiImplicitParam(name = "id", value = "随堂练习ID", required = true, paramType = "path")
    @PreAuthorize("hasRole('TEACHER')")
    public String delete(@PathVariable Long id) {
        examinationService.removeExamination(id);
        return "success";
    }

    @ApiOperation(value = "获取参加随堂测试学生的答题结果", notes = "路径参数ID")
    @GetMapping(value = "{id}/record")
    @PreAuthorize("hasRole('TEACHER')")
    public List<ExaminationRecordVo> getRecords(@PathVariable Long id) {
        List<ExaminationRecordDto> examinationRecordDtos = examinationService.getExaminationRecords(id);
        return BeanConvertUtils.convertListTo(examinationRecordDtos, ExaminationRecordVo::new);
    }

    @GetMapping("/privates")
    @ApiOperation(value = "获取慕课堂未公开的随堂测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moocClassId", value = "慕课堂ID", dataType = "Long")
    })
    public List<ExaminationDto> privates(@RequestParam(value = "moocClassId", required = true) Long moocClassId) {
        return examinationService.getPrivateExaminations(moocClassId);
    }

}
