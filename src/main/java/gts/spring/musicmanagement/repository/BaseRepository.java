package gts.spring.musicmanagement.repository;

import gts.spring.musicmanagement.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}
