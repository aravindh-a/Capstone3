package gts.spring.musicManagement.controller;

import gts.spring.musicManagement.dto.AlbumCollectionDTO;
import gts.spring.musicManagement.service.AlbumCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@Tag(name = "Albums", description = "Endpoints for managing album")
@RequiredArgsConstructor
public class AlbumCollectionController {

    private final AlbumCollectionService albumCollectionService;

    @Operation(summary = "Get all album")
    @GetMapping
    public ResponseEntity<List<AlbumCollectionDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumCollectionService.findAll());
    }

    @Operation(summary = "Get a album by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumCollectionDTO> getAlbumById(@PathVariable Long id) {
        var album = albumCollectionService.findById(id);
        return album != null ? ResponseEntity.ok(album) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a album")
    @PostMapping
    public ResponseEntity<AlbumCollectionDTO> createAlbum(@Valid @RequestBody AlbumCollectionDTO albumDTO) {
        return ResponseEntity.status(201).body(albumCollectionService.create(albumDTO));
    }

    @Operation(summary = "Update an existing album by ID (PUT)")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumCollectionDTO> updateAlbum(@PathVariable Long id,
                                                            @Valid @RequestBody AlbumCollectionDTO albumDTO) {
        var album = albumCollectionService.findById(id);
        return album != null ? ResponseEntity.ok(albumCollectionService.update(id, albumDTO)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing album by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumCollectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register a new artist in an existing album")
    @PostMapping("/{albumId}/artists/{artistId}")
    public ResponseEntity<AlbumCollectionDTO> registerArtist(@PathVariable Long albumId, @PathVariable Long artistId) {
        var album = albumCollectionService.registerArtist(albumId, artistId);
        return album != null ? ResponseEntity.ok(album)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Assign a new track to an existing album")
    @PostMapping("/{albumId}/tracks/{trackId}")
    public ResponseEntity<AlbumCollectionDTO> registerTrack(@PathVariable Long albumId, @PathVariable Long trackId) {
        var album = albumCollectionService.registerTrack(albumId, trackId);
        return album != null ? ResponseEntity.ok(album)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get all album in which the given artist is registered")
    @GetMapping("/artists/{artistId}")
    public ResponseEntity<List<AlbumCollectionDTO>> getAlbumByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(albumCollectionService.findByArtistId(artistId));
    }

    @Operation(summary = "Get all album to which the given track is Registered")
    @GetMapping("/tracks/{trackId}")
    public ResponseEntity<List<AlbumCollectionDTO>> getAlbumByTrack(@PathVariable Long trackId) {
        return ResponseEntity.ok(albumCollectionService.findByTrackId(trackId));
    }
}
