package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;

import java.util.List;

public interface LessonRepository extends BaseRepository<Lesson, Long> {

    List<Lesson> findByMoocClassId(Long moocClassId);

    List<Lesson> findByMoocClassIdAndStatus(Long moocClassId, Integer status);
}
