package com.ssafyhome.user.dao;

import com.ssafyhome.user.entity.EmailSecretEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmailSecretRepository extends CrudRepository<EmailSecretEntity, String> {
}
