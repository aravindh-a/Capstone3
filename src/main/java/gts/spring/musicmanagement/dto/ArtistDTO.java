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
@Schema(name = "artist", description = "Artists information")
public class ArtistDTO extends BaseDTO {

    @Schema(description = "The name of the artist", example = "john")
    @NotBlank
    private String artistName;

    @Schema(description = "Genre Name", example = "POP,ROCK,JAZZ,BLUES")
    @NotBlank
    private String genre;

    @Schema(description = "Artist country name", example = "India")
    private String country;
}
