package gts.spring.musicManagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import gts.spring.musicManagement.entity.GENRE;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "track", description = "Details about a track")
public class TrackDTO extends BaseEntityDTO {



    @Schema(description = " Name of the track", example = "Yow yow")
    @NotBlank
    private String title;

    @Schema(description = " Duration of the track", example = "5.24")
    @DecimalMin(value = "0.05")
    @DecimalMax(value = "10.00")
    private double duration;

    @Schema(description = "Track release date",example = "2025-04-01")
    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;

    @Schema(description = " Track Language", example = "English")
    @NotBlank
    private String language;

    @Schema(description = " Track producer Name", example = "AR Studio")
    @NotBlank
    private String producer;

}
