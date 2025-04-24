package gts.spring.conferences.repository;

import gts.spring.conferences.entity.AlbumCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumCollectionRepository extends BaseRepository<AlbumCollection> {

    List<AlbumCollection> findAllByOrderByIdAsc();
}
