package com.home.automation.homeboard.repo;

import com.home.automation.homeboard.domain.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository<T extends BaseModel> extends JpaRepository<T, Long> {
}
