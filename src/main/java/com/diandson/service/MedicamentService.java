package com.diandson.service;

import com.diandson.domain.*;
import com.diandson.repository.*;
import com.diandson.security.SecurityUtils;
import com.diandson.service.dto.MedicamentDTO;
import com.diandson.service.mapper.MedicamentMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.diandson.service.mapper.VenteMapper;
import com.diandson.service.mapper.VenteMedicamentMapper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Medicament}.
 */
@Service
@Transactional
public class MedicamentService {

    private final Logger log = LoggerFactory.getLogger(MedicamentService.class);

    private final MedicamentRepository medicamentRepository;

    private final MedicamentMapper medicamentMapper;
    @Autowired
    private StructureRepository structureRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private VenteMedicamentRepository venteMedicamentRepository;
    @Autowired
    private VenteMedicamentMapper venteMedicamentMapper;
    @Autowired
    private VenteRepository venteRepository;
    @Autowired
    private VenteMapper venteMapper;

    public MedicamentService(MedicamentRepository medicamentRepository, MedicamentMapper medicamentMapper) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
    }

    /**
     * Save a medicament.
     *
     * @param medicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicamentDTO save(MedicamentDTO medicamentDTO) {
        log.debug("Request to save Medicament : {}", medicamentDTO);
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        medicament = medicamentRepository.save(medicament);
        return medicamentMapper.toDto(medicament);
    }

    public MedicamentDTO saveUpload(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        List<Medicament> medicamentList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Personne personne = personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get());

        for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
            if (index > 0) {
                Medicament medicament = new Medicament();
                XSSFRow row = worksheet.getRow(index);

                medicament.setDenomination(row.getCell(0).getStringCellValue());
                medicament.setDci(row.getCell(0).getStringCellValue());
                medicament.setForme(row.getCell(1).getStringCellValue());
                medicament.setDosage(row.getCell(2).toString());
                medicament.setClasse(row.getCell(3).getStringCellValue());
                medicament.setCodeBare(row.getCell(4).getStringCellValue());
                medicament.setPrixAchat((long) row.getCell(5).getNumericCellValue());
                medicament.setPrixPublic((long) row.getCell(6).getNumericCellValue());
                medicament.setStockAlerte((long) row.getCell(7).getNumericCellValue());
                medicament.setStockSecurite((long) row.getCell(8).getNumericCellValue());
                medicament.setStockTheorique((long) row.getCell(9).getNumericCellValue());
//                medicament.setDateFabrication(LocalDate.parse(row.getCell(10).toString(), formatter));
//                medicament.setDateExpiration(LocalDate.parse(row.getCell(11).toString(), formatter));
                medicament.setStructure(personne.getStructure());

                medicamentList.add(medicament);
            }
        }

        medicamentList = medicamentRepository.saveAll(medicamentList);

        return medicamentMapper.toDto(medicamentList.get(0));
    }

    /**
     * Partially update a medicament.
     *
     * @param medicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicamentDTO> partialUpdate(MedicamentDTO medicamentDTO) {
        log.debug("Request to partially update Medicament : {}", medicamentDTO);

        return medicamentRepository
            .findById(medicamentDTO.getId())
            .map(existingMedicament -> {
                medicamentMapper.partialUpdate(existingMedicament, medicamentDTO);

                return existingMedicament;
            })
            .map(medicamentRepository::save)
            .map(medicamentMapper::toDto);
    }

    /**
     * Get all the medicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicaments");
        return medicamentRepository.findAll().stream()
            .map(medicamentMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one medicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicamentDTO> findOne(Long id) {
        log.debug("Request to get Medicament : {}", id);
        return medicamentRepository.findById(id).map(medicamentMapper::toDto);
    }

    /**
     * Delete the medicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medicament : {}", id);
        medicamentRepository.deleteById(id);
    }

    public List<MedicamentDTO> findAllGenerer() {
        List<MedicamentDTO> medicamentDTOList = medicamentRepository.findAll().stream()
            .map(medicamentMapper::toDto).collect(Collectors.toList());
        for (MedicamentDTO medicamentDTO : medicamentDTOList){
            medicamentDTO.setStockTheorique(0L);
            ZonedDateTime startMonth = ZonedDateTime.now().minusMonths(1)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime endMonth = ZonedDateTime.now();

            for (Vente vente : venteRepository.findAllByDateVenteBetween(startMonth, endMonth)){

                for (VenteMedicament venteMedicament : vente.getVenteMedicaments()){
                    if (medicamentDTO.getId().equals(venteMedicament.getMedicament().getId())) {
                        medicamentDTO.setStockTheorique(medicamentDTO.getStockTheorique() + venteMedicament.getQuantite());
                    }
                }
            }
            medicamentDTO.setStockTheorique(medicamentDTO.getStockTheorique() / 3);
        }
        return medicamentDTOList;
    }

}
