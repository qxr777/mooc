package edu.whut.cs.jee.mooc.upms.repository;

import edu.whut.cs.jee.mooc.common.persistence.BaseRepository;
import edu.whut.cs.jee.mooc.upms.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

@CacheConfig(cacheNames = "users")
public interface UserRepository extends BaseRepository<User, Long>, QueryByExampleExecutor<User> {

    @Cacheable(key = "#p0")
    List<User> findByName(String name);
}
