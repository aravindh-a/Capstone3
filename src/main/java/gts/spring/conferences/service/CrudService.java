package gts.spring.conferences.service;

import gts.spring.conferences.dto.BaseEntityDTO;
import gts.spring.conferences.entity.BaseEntity;
import gts.spring.conferences.mapper.BaseMapper;
import gts.spring.conferences.repository.BaseRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class CrudService<T extends BaseEntity,
        U extends BaseEntityDTO, V extends BaseRepository<T>,
        W extends BaseMapper<T, U>> {

    private final V repository;
    private final W mapper;

    public List<U> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public U findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public U create(U dto) {
        T entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public U update(Long id, U dto) {
        T existing = repository.findById(id).orElse(null);
        if (existing == null) return null;
        mapper.updateEntityFromDTO(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

