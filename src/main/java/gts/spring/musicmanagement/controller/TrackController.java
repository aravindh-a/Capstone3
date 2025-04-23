package gts.spring.musicmanagement.controller;

import gts.spring.musicmanagement.dto.TrackDTO;
import gts.spring.musicmanagement.repository.TrackRepository;
import gts.spring.musicmanagement.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;

    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks() {
        return ResponseEntity.ok(trackService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrack(@PathVariable long id) {
        var track = trackService.findById(id);
        return track != null ? ResponseEntity.ok(trackService.findById(id)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TrackDTO> createTrack(@Valid @RequestBody TrackDTO trackDTO) {
        return ResponseEntity.status(201).body(trackService.create(trackDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackDTO> updateTrack(@PathVariable long id, @Valid @RequestBody TrackDTO trackDTO) {
        var track = trackService.findById(id);
        return track != null ? ResponseEntity.ok(trackService.update(id, trackDTO)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TrackDTO> deleteTrack(@PathVariable long id) {
        trackService.delete(id);
        return ResponseEntity.ok().build();
    }

}
