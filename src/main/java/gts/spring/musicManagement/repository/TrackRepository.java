package gts.spring.musicManagement.repository;

import gts.spring.musicManagement.entity.Track;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends BaseRepository<Track> {

    List<Track> findAllByOrderByIdAsc();
}
