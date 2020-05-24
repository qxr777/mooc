package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.mclass.model.Choice;
import edu.whut.cs.jee.mooc.mclass.model.Option;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import edu.whut.cs.jee.mooc.mclass.repository.OptionRepository;
import edu.whut.cs.jee.mooc.mclass.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Choice saveChoice(Choice choice) {
        List<Option> options = choice.getOptions();
        for (Option option : options) {
            optionRepository.save(option);
        }
        return subjectRepository.save(choice);
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public List<Subject> getSubjectsOfExaminzation(Long examinationId) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Subject> subjects = subjectRepository.findByExaminationId(examinationId, sort);
        return subjects;
    }

    public List<Subject> getSubjectsOfExercise(Long exerciseId) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Subject> subjects = subjectRepository.findByExerciseId(exerciseId, sort);
        return subjects;
    }

    public void removeSubject(Long subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new APIException(AppCode.NO_SUBJECT_ERROR, AppCode.NO_SUBJECT_ERROR.getMsg() + subjectId);
        }
        subjectRepository.deleteById(subjectId);
    }
}
