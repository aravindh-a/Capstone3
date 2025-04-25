package gts.spring.musicManagement.controller;

import gts.spring.musicManagement.dto.TrackDTO;
import gts.spring.musicManagement.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@Tag(name = "Tracks", description = "Endpoints for managing Presenters")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @Operation(summary = "Get all presenters")
    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks() {
        return ResponseEntity.ok(trackService.findAll());
    }

    @Operation(summary = "Get a presenter by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrack(@PathVariable Long id) {
        var presenter = trackService.findById(id);
        return presenter != null ? ResponseEntity.ok(presenter) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new presenter")
    @PostMapping
    public ResponseEntity<TrackDTO> createTrack(@Valid @RequestBody TrackDTO trackDTO) {
        return ResponseEntity.status(201).body(trackService.create(trackDTO));
    }

    @Operation(summary = "Update an existing presenter by ID (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<TrackDTO> updateTrack(@PathVariable Long id,
                                                    @Valid @RequestBody TrackDTO trackDTO) {
        var presenter = trackService.findById(id);
        return presenter != null ? ResponseEntity.ok(trackService.update(id, trackDTO)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing presenter by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
