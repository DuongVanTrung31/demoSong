package cg.repository;

import cg.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISongRepository extends JpaRepository<Song,Long> {
    Page<Song> findAllByNameContaining(String name , Pageable pageable);
    Page<Song> findAllByCategory_Id(Long id, Pageable pageable);
}
