package edu.whut.cs.jee.mooc.common.persistence;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    List<T> findTop3ByOrderByUpdateTimeDescIdAsc();
}