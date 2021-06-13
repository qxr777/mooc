package edu.whut.cs.jee.mooc.mclass.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

@CacheConfig(cacheNames = "mclasses")
public interface MoocClassRepository extends BaseRepository<MoocClass, Long> {

    @Query(value = "select users_id from mclass_class_user where mooc_class_id = ?1", nativeQuery=true)
    List<BigInteger> findUserIds(Long moocClassId);

    List<MoocClass> findByCode(String code);

    @Cacheable
    @Query(value = "select m from MoocClass m where m.course.teacher.id = :teacherId")
    List<MoocClass> findByTeacher(Long teacherId);
}
