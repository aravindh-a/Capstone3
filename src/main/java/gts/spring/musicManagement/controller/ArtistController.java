package gts.spring.musicManagement.controller;

import gts.spring.musicManagement.dto.ArtistDTO;
import gts.spring.musicManagement.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artists", description = "Endpoints for managing Artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "Get all artists")
    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        return ResponseEntity.ok(artistService.findAll());
    }

    @Operation(summary = "Get an artist by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable Long id) {
        var artist = artistService.findById(id);
        return artist != null ? ResponseEntity.ok(artist) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new artist")
    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@Valid @RequestBody ArtistDTO artistDTO) {
        return ResponseEntity.status(201).body(artistService.create(artistDTO));
    }

    @Operation(summary = "Update an existing artist by ID (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long id,
                                                    @Valid @RequestBody ArtistDTO artistDTO) {
        var artist = artistService.findById(id);
        return artist != null ? ResponseEntity.ok(artistService.update(id, artistDTO)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing artist by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
