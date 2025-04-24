package gts.spring.conferences.repository;

import gts.spring.conferences.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findAllByOrderByIdAsc();
}
