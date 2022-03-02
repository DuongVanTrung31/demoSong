package cg.controller;

import cg.model.Category;
import cg.model.Song;
import cg.service.ICategoryService;
import cg.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping
public class SongController {

    @Value("${file-upload}")
    String fileUpload;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    ISongService songService;

    @ModelAttribute(name = "categories")
    public Iterable<Category> showCategories(){
        return categoryService.findAll();
    }

    @GetMapping
    public ModelAndView showAll(@PageableDefault Pageable pageable, Optional<String> search){
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Song> songs;
        if(search.isPresent()){
            songs = songService.findByName(search.get(), pageable);
            modelAndView.addObject("search", search.get());
        } else {
            songs = songService.findAll(pageable);
        }
        modelAndView.addObject("songs",songs);
        return modelAndView;
    }

    @GetMapping("/desc")
    public ModelAndView sortByDesc(@SortDefault(sort = "name", direction = Sort.Direction.DESC)@PageableDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Song> songs = songService.findAll(pageable);
        modelAndView.addObject("songs", songs);
        return  modelAndView;
    }


    @GetMapping("/asc")
    public ModelAndView sortByAsc(@SortDefault(sort = "name", direction = Sort.Direction.ASC)@PageableDefault Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Song> songs = songService.findAll(pageable);
        modelAndView.addObject("songs", songs);
        return  modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createForm(){
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("song",new Song());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(@ModelAttribute("song") Song song) throws IOException {
        MultipartFile file = song.getFile();
        if(file.getSize() == 0){
            return new ModelAndView("create").addObject("song",song);
        }
        String url = file.getOriginalFilename();
        FileCopyUtils.copy(song.getFile().getBytes(), new File(fileUpload + url));
        song.setUrl(url);
        songService.save(song);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id){
        return new ModelAndView("edit").addObject("song",songService.findOne(id).get());
    }

    @PostMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute("song") Song song, @PathVariable Long id) throws IOException {
        song.setId(id);
        if(song.getFile().getSize() != 0) {
            MultipartFile file = song.getFile();
            String url = file.getOriginalFilename();
            FileCopyUtils.copy(song.getFile().getBytes(), new File(fileUpload + url));
            song.setUrl(url);
        } else {
            song.setUrl(songService.findOne(id).get().getUrl());
        }
        songService.save(song);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
        songService.delete(id);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/category/{id}")
    public ModelAndView showByCategory(@PathVariable Long id, @PageableDefault Pageable pageable) {
        Page<Song> songs = songService.findAllByCategory_Id(id, pageable);
        return new ModelAndView("index").addObject("songs",songs);
    }
}
