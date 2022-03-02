package cg.service;

import cg.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ISongService {
    Page<Song> findAll(Pageable pageable);
    Song save(Song song);
    void delete(Long id);
    Optional<Song> findOne(Long id);
    Page<Song> findByName(String name, Pageable pageable);
    Page<Song> findAllByCategory_Id(Long id, Pageable pageable);
}
