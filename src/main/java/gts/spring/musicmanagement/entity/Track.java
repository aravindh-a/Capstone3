package gts.spring.musicmanagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "track")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Track extends BaseEntity {

    private String trackName;
    private String genre;
    private String language;

    @ManyToMany(mappedBy = "tracks1", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Artist> artists = new HashSet<>();
}
