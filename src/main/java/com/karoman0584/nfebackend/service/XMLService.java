package com.karoman0584.nfebackend.service;

import com.karoman0584.nfebackend.domain.Nfe;
import com.karoman0584.nfebackend.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class XMLService {
    private final String PREFIX_FILE = "nfe_";
    private final String INPUT_PATH = "/input";
    private final String OUTPUT_PATH = "/output";
    private final String ERROR_PATH = "/error";

    private final Environment env;

    public XMLService(Environment env) {
        this.env = env;
    }

    public void save(MultipartFile file, Integer nfeNumber) {
        String fileName = PREFIX_FILE.concat(nfeNumber.toString()).concat(".xml");
        try {
            Path fileStorageLocation = Paths.get(Objects.requireNonNull(env.getProperty("file.path.dir")).concat(INPUT_PATH))
                    .toAbsolutePath().normalize();
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            log.error("Error saving XML: {}", ex.getMessage());
            throw new BusinessException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean processFile(Integer nfeNumber) throws IOException {
        //simulating the process, 0 -> fails, 1 -> success
        int processResult = new Random().nextInt(2);

        String absolutePath = Objects.requireNonNull(env.getProperty("file.path.dir"));
        String source = absolutePath.concat(INPUT_PATH).concat("/").concat(PREFIX_FILE.concat(nfeNumber.toString())).concat(".xml");
        String target = absolutePath.concat(ERROR_PATH).concat("/").concat(PREFIX_FILE.concat(nfeNumber.toString())).concat(".xml");

        if (processResult == 1) {
            log.info("Processamento do XML da NFe com SUCESSO");
            target = absolutePath.concat(OUTPUT_PATH).concat("/").concat(PREFIX_FILE.concat(nfeNumber.toString())).concat(".xml");
        } else {
            log.error("Processamento do XML da NFe com ERRO");
        }

        Files.move(Paths.get(source), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
        return processResult == 1;
    }
}
