package gts.spring.musicManagement.dto;

import gts.spring.musicManagement.entity.GENRE;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(name = "AlbumCollection", description = "Details about a album")
public class AlbumCollectionDTO extends BaseEntityDTO {


    @Schema(description = "Title of the album", example = "Spring Boot Deep Dive")
    @NotBlank
    private String albumName;

    @Schema(description = "Album genre", example = "POP,ROCK,JAZZ,BLUES,HIPHOP")
    @NotNull
    private GENRE genre;

    @Schema(description = "List of associated presenters")
    private List<TrackDTO> tracks;

    @Schema(description = "List of attendees")
    private List<ArtistDTO> artists;
}
