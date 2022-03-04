package com.karoman0584.nfebackend.service;

import com.google.common.io.Files;
import com.karoman0584.nfebackend.domain.Nfe;
import com.karoman0584.nfebackend.domain.ProcessStatus;
import com.karoman0584.nfebackend.exception.BusinessException;
import com.karoman0584.nfebackend.repository.NfeRepository;
import com.karoman0584.nfebackend.service.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NfeService {
    private final NfeRepository nfeRepository;
    private final XMLService xmlService;

    public NfeService(NfeRepository nfeRepository, XMLService xmlService) {
        this.nfeRepository = nfeRepository;
        this.xmlService = xmlService;
    }

    @Transactional
    public ResponseDTO upload(MultipartFile file) {
        //Validating extension
        String extension = Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if (!extension.equals("xml")) {
            throw new BusinessException("O arquivo tem que estar num formato valido (.xml)", HttpStatus.BAD_REQUEST);
        }
        try {
            //Parsing XML to Object
            String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            JAXBContext jaxbContext = JAXBContext.newInstance(Nfe.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Nfe nfe = (Nfe) jaxbUnmarshaller.unmarshal(new StringReader(xmlContent));

            nfe.setStatus(ProcessStatus.WAITING_PROCESS);
            nfe.getDuplicatas().forEach(d -> d.setNfe(nfe));
            nfeRepository.save(nfe);

            xmlService.save(file, nfe.getNumber());

        } catch (JAXBException | IOException ex) {
            log.error(ex.getMessage());
            throw new BusinessException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseDTO("Seu arquivo foi recepcionado com sucesso e será processado em breve", HttpStatus.OK.value());
    }


    @Scheduled(fixedRate = 120000) //time in milliseconds
    public void process() {
        log.info("Executing NFe Service...");
        List<Nfe> nfes = nfeRepository.findAllByStatus(ProcessStatus.WAITING_PROCESS);
        nfes.forEach(n -> {
            n.setStatus(ProcessStatus.ON_PROCESS);
            nfeRepository.save(n);

            try {
                boolean success = xmlService.processFile(n.getNumber());
                n.setStatus(ProcessStatus.PROCESSED_WITH_ERROR);
                if (success) {
                    n.setStatus(ProcessStatus.PROCESSED_WITH_SUCCESS);
                }
            } catch (IOException ex) {
                log.error("ERROR AO MOVER ARQUIVO: {}", ex.getMessage());
                n.setStatus(ProcessStatus.WAITING_PROCESS);
            }

            nfeRepository.save(n);
        });
    }

    public List<Nfe> getAll() {
        return (List<Nfe>) nfeRepository.findAll();
    }
}
