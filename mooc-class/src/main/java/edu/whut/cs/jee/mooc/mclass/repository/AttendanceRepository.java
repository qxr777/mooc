package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.Attendance;

import java.util.List;

public interface AttendanceRepository extends BaseRepository<Attendance, Long> {

    List<Attendance> findByCheckInId(Long chekInId);

    List<Attendance> findByCheckInIdAndStatus(Long checkInId, Integer status);
}
