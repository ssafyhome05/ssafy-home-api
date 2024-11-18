package com.ssafyhome.house.dao.repository;

import com.ssafyhome.house.entity.TopTenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TopTenRepository extends CrudRepository<TopTenEntity, String> {

  Optional<TopTenEntity> findLastByOrderByRankTimeDesc();
}
