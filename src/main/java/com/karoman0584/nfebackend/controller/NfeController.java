package com.karoman0584.nfebackend.controller;

import com.karoman0584.nfebackend.domain.Nfe;
import com.karoman0584.nfebackend.service.NfeService;
import com.karoman0584.nfebackend.service.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class NfeController {

    private final NfeService nfeService;

    public NfeController(NfeService nfeService) {
        this.nfeService = nfeService;
    }

    @GetMapping(value = "/files")
    public ResponseEntity<List<Nfe>> list() {
        log.info("Listing NFes");
        List<Nfe> list = this.nfeService.getAll();
        log.info("List of all NFes got it");
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> upload(@RequestParam("file") MultipartFile file) {
        log.info("Uploading NFe");
        ResponseDTO response = this.nfeService.upload(file);
        log.info("NFe uploaded and saved successful");
        return ResponseEntity.ok(response);
    }
}
