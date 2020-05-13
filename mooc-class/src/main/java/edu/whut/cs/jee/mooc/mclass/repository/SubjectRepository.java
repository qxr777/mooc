package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.Subject;

import java.util.List;

public interface SubjectRepository extends BaseRepository<Subject, Long> {

    List<Subject> findByExerciseId(Long exerciseId);

    List<Subject> findByExaminationId(Long examinationId);
}
