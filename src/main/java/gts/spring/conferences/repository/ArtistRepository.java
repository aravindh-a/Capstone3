package gts.spring.conferences.repository;

import gts.spring.conferences.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends BaseRepository<Artist> {

    List<Artist> findAllByOrderByIdAsc();
}
