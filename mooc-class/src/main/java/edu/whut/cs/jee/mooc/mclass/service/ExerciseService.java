package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.ExerciseDto;
import edu.whut.cs.jee.mooc.mclass.model.Exercise;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import edu.whut.cs.jee.mooc.mclass.repository.ExerciseRepository;
import edu.whut.cs.jee.mooc.mclass.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    /**
     * 创建练习
     * @param exerciseDto
     * @return
     */
    public ExerciseDto saveExercise(ExerciseDto exerciseDto) {
        Exercise exercise = BeanConvertUtils.convertTo(exerciseDto, Exercise::new);
        exercise = exerciseRepository.save(exercise);
        exerciseDto.setId(exercise.getId());
        return exerciseDto;
    }

    public List<ExerciseDto> getExercises(Long courseId) {
        List<Exercise> exercises = exerciseRepository.findByCourseId(courseId);
        return BeanConvertUtils.convertListTo(exercises, ExerciseDto::new);
    }

    /**
     * 删除练习库中的练习
     * @param exerciseId
     * @return
     */
    public void removeExcercise(Long exerciseId) {

        List<Subject> subjects = subjectRepository.findByExerciseId(exerciseId);
        subjectRepository.deleteAll(subjects);
        exerciseRepository.deleteById(exerciseId);
    }
}
