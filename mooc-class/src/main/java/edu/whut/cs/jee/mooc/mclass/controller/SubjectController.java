package edu.whut.cs.jee.mooc.mclass.controller;

import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.*;
import edu.whut.cs.jee.mooc.mclass.service.SubjectService;
import edu.whut.cs.jee.mooc.mclass.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api("习题管理")
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("saveJudgment")
    @ApiOperation(value = "新增/保存判断题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "judgmentSaveVo", value = "判断题信息", dataType = "JudgmentSaveVo")
    })
    public Long saveJudgment(@RequestBody @Valid JudgmentSaveVo judgmentSaveVo) {
        return subjectService.saveJudgment(BeanConvertUtils.convertTo(judgmentSaveVo, JudgmentDto::new)).getId();
    }

    @PostMapping("saveFill")
    @ApiOperation(value = "新增/保存填空题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fillSaveVo", value = "填空题信息", dataType = "FillSaveVo")
    })
    public Long saveFill(@RequestBody @Valid FillSaveVo fillSaveVo) {
        FillDto fillDto = BeanConvertUtils.convertTo(fillSaveVo, FillDto::new);
        return subjectService.saveFill(fillDto).getId();
    }

    @PostMapping("saveChoice")
    @ApiOperation(value = "新增/保存选择题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "choiceSaveVo", value = "选择题信息", dataType = "ChoiceSaveVo")
    })
    public Long saveChoice(@RequestBody @Valid ChoiceSaveVo choiceSaveVo) {
        ChoiceDto choiceDto = BeanConvertUtils.convertTo(choiceSaveVo, ChoiceDto::new);
        List<OptionDto> optionDtos = BeanConvertUtils.convertListTo(choiceSaveVo.getOptions(), OptionDto::new);
        choiceDto.setOptions(optionDtos);
        return subjectService.saveChoice(choiceDto).getId();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除习题", notes = "根据ID删除习题")
    @ApiImplicitParam(name = "id", value = "习题ID", required = true, paramType = "path")
    public String delete(@PathVariable Long id) {
        subjectService.removeSubject(id);
        return "success";
    }

    @GetMapping("examination")
    @ApiOperation(value = "获取指定随堂测试的所有习题用于编辑")
    public List<SubjectSaveVo> getSubjectsOfExamination(@RequestParam(value = "examinationId", required = true) Long examinationId) {
        List<SubjectDto> subjectDtos = subjectService.getSubjectsOfExaminzation(examinationId);
        return BeanConvertUtils.convertListTo(subjectDtos, SubjectSaveVo::new, (s, t) -> t.setKey(s.getKey()));
    }

    @GetMapping("exercise")
    @ApiOperation(value = "获取指定练习库的所有习题用于编辑")
    public List<SubjectSaveVo> getSubjectsOfExercise(@RequestParam(value = "exerciseId", required = true) Long exerciseId) {
        List<SubjectDto> subjectDtos = subjectService.getSubjectsOfExercise(exerciseId);
        return BeanConvertUtils.convertListTo(subjectDtos, SubjectSaveVo::new, (s, t) -> t.setKey(s.getKey()));
    }

    @GetMapping("exam")
    @ApiOperation(value = "获取指定随堂测试题")
    public List<SubjectExaminationVo> getSubjectsForExamination(@RequestParam(value = "examinationId", required = true) Long examinationId) {
        List<SubjectDto> subjectDtos = subjectService.getSubjectsOfExaminzation(examinationId);
        List<SubjectExaminationVo> subjectExaminationVos = new ArrayList<>();
        SubjectExaminationVo subjectExaminationVo = null;
        for(SubjectDto subjectDto : subjectDtos) {
            List<OptionDto> optionDtos = subjectDto.getOptions();
            List<OptionExaminationVo> optionExaminationVos = BeanConvertUtils.convertListTo(optionDtos, OptionExaminationVo::new);
            subjectExaminationVo = BeanConvertUtils.convertTo(subjectDto, SubjectExaminationVo::new);
            subjectExaminationVo.setOptions(optionExaminationVos);
            subjectExaminationVos.add(subjectExaminationVo);
        }
        return subjectExaminationVos;
    }

    @GetMapping("statistic")
    @ApiOperation(value = "获取指定随堂测试答题统计")
    public List<SubjectStatisticVo> getSubjectsForStatistic(@RequestParam(value = "examinationId", required = true) Long examinationId) {
        List<SubjectDto> subjectDtos = subjectService.getSubjectsOfExaminzation(examinationId);
        List<SubjectStatisticVo> subjectStatisticVos = new ArrayList<>();
        SubjectStatisticVo subjectStatisticVo = null;
        for(SubjectDto subjectDto : subjectDtos) {
            subjectStatisticVo = new SubjectStatisticVo();
            subjectStatisticVo.convertFor(subjectDto);
            subjectStatisticVos.add(subjectStatisticVo);
        }
        return subjectStatisticVos;
    }

}
