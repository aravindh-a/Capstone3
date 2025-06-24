package gts.spring.musicManagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Artist extends BaseEntity {

    private String firstName;
    private String lastName;
    private String country;
    private String bio;
    private LocalDate birthDate;

    @ManyToMany(mappedBy = "artists", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<AlbumCollection> albumCollections = new HashSet<>();
}
