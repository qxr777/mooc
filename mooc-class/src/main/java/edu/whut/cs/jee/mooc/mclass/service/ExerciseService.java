package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.model.*;
import edu.whut.cs.jee.mooc.mclass.repository.*;
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
     * @param exercise
     * @return
     */
    public Exercise saveExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
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
