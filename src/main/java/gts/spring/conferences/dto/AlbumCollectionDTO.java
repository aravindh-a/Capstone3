package gts.spring.conferences.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(name = "AlbumCollection", description = "Details about a Conference Session")
public class AlbumCollectionDTO {

    @Schema(description = "The unique identifier of the session", example = "1")
    private Long id;

    @Schema(description = "The title of the session", example = "Spring Boot Deep Dive")
    @NotBlank
    private String albumName;

    @Schema(description = "List of associated presenters")
    private List<TrackDTO> tracks;

    @Schema(description = "List of attendees")
    private List<ArtistDTO> artists;
}
