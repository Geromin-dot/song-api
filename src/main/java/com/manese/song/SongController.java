package com.manese.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manese/songs")
public class SongController {

    @Autowired
    private SongRepository songRepo;

    // GET all songs
    @GetMapping
    public List<Song> listSongs() {
        return songRepo.findAll();
    }

    // GET song by id
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        return songRepo.findById(id)
                .map(foundSong -> ResponseEntity.ok(foundSong))
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE a song
    @PostMapping
    public ResponseEntity<Song> addSong(@RequestBody Song song) {
        Song saved = songRepo.save(song);
        return ResponseEntity.ok(saved);
    }

    // SEARCH songs
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Song>> searchSongs(@PathVariable String keyword) {

        List<Song> songs = songRepo
                .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrGenreContainingIgnoreCase(
                        keyword, keyword, keyword, keyword
                );

        return ResponseEntity.ok(songs);
    }

    // UPDATE song
    @PutMapping("/{id}")
    public ResponseEntity<Song> editSong(@PathVariable Long id, @RequestBody Song songInfo) {

        return songRepo.findById(id)
                .map(song -> {
                    song.setTitle(songInfo.getTitle());
                    song.setArtist(songInfo.getArtist());
                    song.setAlbum(songInfo.getAlbum());
                    song.setGenre(songInfo.getGenre());
                    song.setUrl(songInfo.getUrl());

                    Song updated = songRepo.save(song);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE song
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeSong(@PathVariable Long id) {

        if (!songRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        songRepo.deleteById(id);

        return ResponseEntity.ok("Song with ID " + id + " deleted.");
    }
}