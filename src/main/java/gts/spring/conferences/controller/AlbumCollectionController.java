package gts.spring.conferences.controller;

import gts.spring.conferences.dto.AlbumCollectionDTO;
import gts.spring.conferences.service.AlbumCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@Tag(name = "Albums", description = "Endpoints for managing Conference Sessions")
@RequiredArgsConstructor
public class AlbumCollectionController {

    private final AlbumCollectionService albumCollectionService;

    @Operation(summary = "Get all conference sessions")
    @GetMapping
    public ResponseEntity<List<AlbumCollectionDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumCollectionService.findAll());
    }

    @Operation(summary = "Get a conference session by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumCollectionDTO> getAlbumById(@PathVariable Long id) {
        var session = albumCollectionService.findById(id);
        return session != null ? ResponseEntity.ok(session) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new conference session")
    @PostMapping
    public ResponseEntity<AlbumCollectionDTO> createAlbum(@Valid @RequestBody AlbumCollectionDTO sessionDTO) {
        return ResponseEntity.status(201).body(albumCollectionService.create(sessionDTO));
    }

    @Operation(summary = "Update an existing conference session by ID (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumCollectionDTO> updateAlbum(@PathVariable Long id,
                                                            @Valid @RequestBody AlbumCollectionDTO sessionDTO) {
        var session = albumCollectionService.findById(id);
        return session != null ? ResponseEntity.ok(albumCollectionService.update(id, sessionDTO)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing conference session by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumCollectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register a new attendee in an existing conference session")
    @PostMapping("/{sessionId}/attendees/{attendeeId}")
    public ResponseEntity<AlbumCollectionDTO> registerArtist(@PathVariable Long sessionId, @PathVariable Long attendeeId) {
        var session = albumCollectionService.registerAttendee(sessionId, attendeeId);
        return session != null ? ResponseEntity.ok(session)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Assign a new presenter to an existing conference session")
    @PostMapping("/{sessionId}/presenters/{presenterId}")
    public ResponseEntity<AlbumCollectionDTO> assignTrack(@PathVariable Long sessionId, @PathVariable Long presenterId) {
        var session = albumCollectionService.assignPresenter(sessionId, presenterId);
        return session != null ? ResponseEntity.ok(session)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all conference sessions in which the given attendee is registered")
    @GetMapping("/attendee/{attendeeId}")
    public ResponseEntity<List<AlbumCollectionDTO>> getAlbumByArtist(@PathVariable Long attendeeId) {
        return ResponseEntity.ok(albumCollectionService.findByAttendeeId(attendeeId));
    }

    @Operation(summary = "Get all conference sessions to which the given presenter is assigned")
    @GetMapping("/presenter/{presenterId}")
    public ResponseEntity<List<AlbumCollectionDTO>> getAlbumByTrack(@PathVariable Long presenterId) {
        return ResponseEntity.ok(albumCollectionService.findByPresenterId(presenterId));
    }
}
