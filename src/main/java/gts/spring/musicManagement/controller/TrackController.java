package gts.spring.musicManagement.controller;

import gts.spring.musicManagement.dto.TrackDTO;
import gts.spring.musicManagement.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@Tag(name = "Tracks", description = "Endpoints for managing Tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @Operation(summary = "Get all tracks")
    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks() {
        return ResponseEntity.ok(trackService.findAll());
    }

    @Operation(summary = "Get a track by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrack(@PathVariable Long id) {
        var track = trackService.findById(id);
        return track != null ? ResponseEntity.ok(track) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new track")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TrackDTO> createTrack(@Valid @RequestBody TrackDTO trackDTO) {
        return ResponseEntity.status(201).body(trackService.create(trackDTO));
    }

    @Operation(summary = "Update an existing track by ID (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<TrackDTO> updateTrack(@PathVariable Long id,
                                                    @Valid @RequestBody TrackDTO trackDTO) {
        var track = trackService.findById(id);
        return track != null ? ResponseEntity.ok(trackService.update(id, trackDTO)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing track by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
