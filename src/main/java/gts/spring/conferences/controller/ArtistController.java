package gts.spring.conferences.controller;

import gts.spring.conferences.dto.ArtistDTO;
import gts.spring.conferences.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artists", description = "Endpoints for managing Attendees")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "Get all attendees")
    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @Operation(summary = "Get an attendee by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable Long id) {
        var attendee = artistService.findById(id);
        return attendee != null ? ResponseEntity.ok(attendee) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new attendee")
    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@Valid @RequestBody ArtistDTO artistDTO) {
        return ResponseEntity.status(201).body(artistService.create(artistDTO));
    }

    @Operation(summary = "Update an existing attendee by ID (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long id,
                                                    @Valid @RequestBody ArtistDTO artistDTO) {
        var attendee = artistService.findById(id);
        return attendee != null ? ResponseEntity.ok(artistService.update(id, artistDTO)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing attendee by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
