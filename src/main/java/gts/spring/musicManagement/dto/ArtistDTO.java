package gts.spring.musicManagement.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "Artist", description = "Details about an Artist")
public class ArtistDTO extends BaseEntityDTO {


    @Schema(description = "The name of the artist", example = "Jane Doe")
    @NotBlank
    private String firstName;

    @Schema(description = "The name of the artist", example = "Jane Doe")
    @NotBlank
    private String lastName;




    @Schema(description = "Country of the artist", example = "India")
    @NotBlank
    private String country;

}
