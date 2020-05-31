package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.*;
import edu.whut.cs.jee.mooc.mclass.model.*;
import edu.whut.cs.jee.mooc.mclass.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public ChoiceDto saveChoice(ChoiceDto choiceDto) {
        Choice choice = BeanConvertUtils.convertTo(choiceDto, Choice::new);
        List<OptionDto> optionDtos = choiceDto.getOptions();
        List<Option> options = BeanConvertUtils.convertListTo(optionDtos, Option::new);
        choice.setOptions(options);
        choice = subjectRepository.save(choice);
        return BeanConvertUtils.convertTo(choice, ChoiceDto::new);
    }

    public JudgmentDto saveJudgment(JudgmentDto judgmentDto) {
        Judgment judgment = BeanConvertUtils.convertTo(judgmentDto, Judgment::new);
        judgment = subjectRepository.save(judgment);
        return BeanConvertUtils.convertTo(judgment, JudgmentDto::new);
    }

    public FillDto saveFill(FillDto fillDto) {
        Fill fill = BeanConvertUtils.convertTo(fillDto, Fill::new);
        fill = subjectRepository.save(fill);
        return BeanConvertUtils.convertTo(fill, FillDto::new);
    }

    /**
     * 获得随堂测试的习题
     * @param examinationId
     * @return
     */
    public List<SubjectDto> getSubjectsOfExaminzation(Long examinationId) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Subject> subjects = subjectRepository.findByExaminationId(examinationId, sort);
//        List<SubjectDto> subjectDtos = new ArrayList<>();
//        for(Subject subject : subjects) {
//            if (subject instanceof Fill) {
//                subjectDtos.add(BeanConvertUtils.convertTo(subject, FillDto::new, (s, t) -> {
//                    t.setType(s.getClass().getSimpleName());
//                }));
//            } else if (subject instanceof Judgment) {
//                subjectDtos.add(BeanConvertUtils.convertTo(subject, JudgmentDto::new, (s, t) -> { t.setType(s.getClass().getSimpleName());}));
//            } else if (subject instanceof Choice) {
//                List<Option> options = ((Choice) subject).getOptions();
//                List<OptionDto> optionDtos = BeanConvertUtils.convertListTo(options, OptionDto::new);
//                ChoiceDto choiceDto = BeanConvertUtils.convertTo(subject, ChoiceDto::new, (s, t) -> { t.setType(s.getClass().getSimpleName());});
//                choiceDto.setOptions(optionDtos);
//                subjectDtos.add(choiceDto);
//            }
//        }
//        return subjectDtos;
        return convertToDtos(subjects);
    }

    private List<SubjectDto> convertToDtos(List<Subject> subjects) {
        List<SubjectDto> subjectDtos = new ArrayList<>();
        for(Subject subject : subjects) {
            if (subject instanceof Fill) {
                subjectDtos.add(BeanConvertUtils.convertTo(subject, FillDto::new, (s, t) -> {
                    t.setType(s.getClass().getSimpleName());
                }));
            } else if (subject instanceof Judgment) {
                subjectDtos.add(BeanConvertUtils.convertTo(subject, JudgmentDto::new, (s, t) -> { t.setType(s.getClass().getSimpleName());}));
            } else if (subject instanceof Choice) {
                List<Option> options = ((Choice) subject).getOptions();
                List<OptionDto> optionDtos = BeanConvertUtils.convertListTo(options, OptionDto::new);
                ChoiceDto choiceDto = BeanConvertUtils.convertTo(subject, ChoiceDto::new, (s, t) -> { t.setType(s.getClass().getSimpleName());});
                choiceDto.setOptions(optionDtos);
                subjectDtos.add(choiceDto);
            }
        }
        return subjectDtos;
    }

    /**
     * 获得练习库中的习题
     * @param exerciseId
     * @return
     */
    public List<SubjectDto> getSubjectsOfExercise(Long exerciseId) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Subject> subjects = subjectRepository.findByExerciseId(exerciseId, sort);
//        return BeanConvertUtils.convertListTo(subjects, SubjectDto::new, (s, t) -> { t.setType(s.getClass().getSimpleName());});
        return convertToDtos(subjects);
    }

    public void removeSubject(Long subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new APIException(AppCode.NO_SUBJECT_ERROR, AppCode.NO_SUBJECT_ERROR.getMsg() + subjectId);
        }
        subjectRepository.deleteById(subjectId);
    }
}
