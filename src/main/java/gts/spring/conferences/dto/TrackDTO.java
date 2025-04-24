package gts.spring.conferences.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Presenter", description = "Details about a Presenter")
public class TrackDTO {

    @Schema(description = "The unique identifier of the presenter", example = "1")
    private Long id;

    @Schema(description = "The name of the presenter", example = "John Doe")
    @NotBlank
    private String title;

}
