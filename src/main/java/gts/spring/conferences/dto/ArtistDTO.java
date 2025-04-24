package gts.spring.conferences.dto;

import gts.spring.conferences.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Artist", description = "Details about an Artist")
public class ArtistDTO extends BaseEntityDTO {


    @Schema(description = "The name of the artist", example = "Jane Doe")
    @NotBlank
    private String artistName;

    @Schema(description = "Country of the artist", example = "India")
    @NotBlank
    private String country;

}
