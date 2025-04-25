package gts.spring.musicManagement.repository;

import gts.spring.musicManagement.entity.Artist;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends BaseRepository<Artist> {

    List<Artist> findAllByOrderByIdAsc();
}
