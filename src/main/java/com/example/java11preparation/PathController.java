package com.example.java11preparation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class PathController {
    Logger log = LoggerFactory.getLogger(PathController.class);

    @Value("${root.dir}")
    String rootDir;

    @GetMapping("list")
    public List<Path> list(@RequestParam("p") String[] path) {
        log.info("got paths {}", Arrays.toString(path));
        Path p = Path.of(rootDir, path);

        List<Path> list = new ArrayList<>();

        try {
            Files.list(p).map(Path::getFileName).iterator().forEachRemaining(list::add);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @PutMapping("create-dir")
    public ResponseEntity<Path> createDirectory(@RequestParam("name") String name, @RequestParam("p") String[] path) {
        Path p = Path.of(rootDir, path);
        Path created;

        if (Files.exists(p.resolve(name))) {
            return ResponseEntity.badRequest().body(p.resolve(name));
        }

        try {
            created = Files.createDirectory(p.resolve(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(created);
    }
}
