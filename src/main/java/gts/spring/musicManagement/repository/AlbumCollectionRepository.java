package gts.spring.musicManagement.repository;

import gts.spring.musicManagement.entity.AlbumCollection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumCollectionRepository extends BaseRepository<AlbumCollection> {

    List<AlbumCollection> findAllByOrderByIdAsc();
}
