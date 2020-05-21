package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.Exercise;

import java.util.List;

public interface ExerciseRepository extends BaseRepository<Exercise, Long> {

    List<Exercise> findByCourseId(Long courseId);
}
