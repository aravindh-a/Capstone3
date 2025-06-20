package gts.spring.musicManagement.entity;

import gts.spring.musicManagement.dto.ArtistDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "track")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Track extends BaseEntity {

    private String title;
    private double duration;
    private LocalDate releaseDate;
    private String language;
    private String producer;


    @ManyToMany(mappedBy = "tracks", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<AlbumCollection> albumCollections = new HashSet<>();
}
