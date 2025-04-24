package gts.spring.conferences.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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

}
