package cg.service.impl;

import cg.model.Song;
import cg.repository.ISongRepository;
import cg.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SongServiceImpl implements ISongService {

    @Autowired
    private ISongRepository songRepository;

    @Override
    public Page<Song> findAll(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    @Override
    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public void delete(Long id) {
        songRepository.deleteById(id);
    }

    @Override
    public Optional<Song> findOne(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public Page<Song> findByName(String name, Pageable pageable) {
        return songRepository.findAllByNameContaining(name,pageable);
    }

    @Override
    public Page<Song> findAllByCategory_Id(Long id, Pageable pageable) {
        return songRepository.findAllByCategory_Id(id, pageable);
    }
}
