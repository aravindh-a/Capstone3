package gts.spring.musicmanagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Artist extends BaseEntity {

    private String artistName;
    private String genre;
    private String country;

    @ManyToMany ( mappedBy = "artists1", cascade = {CascadeType.PERSIST, CascadeType.MERGE})

//    @JoinTable(
//            name = "artist",
//            joinColumns = @JoinColumn(name = "artist_id"),
//            inverseJoinColumns = @JoinColumn(name = "track_id")
//    )
    private Set<Track> tracks = new HashSet<>();

}
