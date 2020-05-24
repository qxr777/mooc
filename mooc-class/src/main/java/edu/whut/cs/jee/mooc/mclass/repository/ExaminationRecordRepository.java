package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.ExaminationRecord;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExaminationRecordRepository extends BaseRepository<ExaminationRecord, Long> {

    List<ExaminationRecord> findByExaminationId(Long examinationId, Sort sort);
}
