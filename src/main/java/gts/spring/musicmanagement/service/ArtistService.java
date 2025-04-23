package gts.spring.musicmanagement.service;

import gts.spring.musicmanagement.dto.ArtistDTO;
import gts.spring.musicmanagement.entity.Artist;
import gts.spring.musicmanagement.mapper.ArtistMapper;
import gts.spring.musicmanagement.repository.ArtistRepository;

import org.springframework.stereotype.Service;

@Service
public class ArtistService extends CrudService<Artist, ArtistDTO, ArtistRepository, ArtistMapper> {
    public ArtistService(ArtistRepository repository, ArtistMapper mapper) {super(repository, mapper);
    }


}
