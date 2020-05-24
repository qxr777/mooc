package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.Examination;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExaminationRepository extends BaseRepository<Examination, Long> {

    @Query("select e from Examination e where e.lesson.moocClassId = :moocClassId and e.status = :status")
    List<Examination> findByMoocClassIdAndStatus(Long moocClassId, Integer status);

}
