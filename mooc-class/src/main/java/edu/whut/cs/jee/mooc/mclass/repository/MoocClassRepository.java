package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface MoocClassRepository extends BaseRepository<MoocClass, Long> {

    @Query(value = "select users_id from mclass_class_user where mooc_class_id = ?1", nativeQuery=true)
    List<BigInteger> findUserIds(Long moocClassId);

    @Query(value = "select mooc_class_id from mclass_class_user where users_id = ?1", nativeQuery=true)
    List<BigInteger> findMoocClassIds(Long userId);

    List<MoocClass> findByCode(String code);

    @Query(value = "select m from MoocClass m where m.course.teacher.id = :teacherId")
    List<MoocClass> findByTeacher(Long teacherId);
}
