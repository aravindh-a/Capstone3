package gts.spring.musicmanagement.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Schema(name = "track", description = "Details about a track")

public class TrackDTO  extends BaseDTO {

    @Schema(description = "TrackName", example = "Track1")
    @NotBlank
    private String trackName;
    @Schema(description = "Track Genre", example = "ROCK,POP,JAZZ,BLUES")
    @NotBlank
    private String genre;
    @Schema(description = "Track Language", example = "English")
    private String language;

}
