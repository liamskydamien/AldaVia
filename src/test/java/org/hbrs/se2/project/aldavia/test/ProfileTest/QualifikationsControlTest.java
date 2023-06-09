package org.hbrs.se2.project.aldavia.test.ProfileTest;

import org.hbrs.se2.project.aldavia.service.QualifikationenService;
import org.hbrs.se2.project.aldavia.control.exception.PersistenceException;
import org.hbrs.se2.project.aldavia.dtos.QualifikationsDTO;
import org.hbrs.se2.project.aldavia.entities.Qualifikation;
import org.hbrs.se2.project.aldavia.entities.Student;
import org.hbrs.se2.project.aldavia.entities.User;
import org.hbrs.se2.project.aldavia.repository.QualifikationRepository;
import org.hbrs.se2.project.aldavia.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class QualifikationsControlTest {

    public static final String MESSAGE = "Wrong Exception thrown";
    public static final String MESSAGE2 = "Wrong Exception-Message thrown";

    @Autowired
    private QualifikationenService qualifikationControl;

    @Autowired
    private QualifikationRepository qualifikationsRepository;


    @Autowired
    private StudentRepository studentRepository;

    private Student student;
    private QualifikationsDTO qualifikationDTO;
    private Qualifikation qualifikationTest;

    private final Logger logger = LoggerFactory.getLogger(QualifikationsControlTest.class);

    @BeforeEach
    public void setUp(){
        User user = User.builder()
                .userid("Ke21252312341234112343121432412343232414sUserK")
                .password("TestPas1241231412112432141321214313244312132443123423414223512351swor´321d")
                .email("Test@LK1342314212123412411235235523531324231441234125123512354321MisTe123st.de")
                .build();

        student = Student.builder()
                .vorname("Test_L_VornaÖ413435231412124me")
                .nachname("Test_L_Nac21512412321321412412412142421435:23124")
                .matrikelNummer("Kenntn3512312351253215413241241234321sTes124tMatrNrL")
                .build();

        student.setUser(user);

        student = studentRepository.save(student);

        qualifikationDTO = QualifikationsDTO.builder()
                .bezeichnung("TestQualifikation")
                .bereich("TestBereich")
                .von(LocalDate.of(2019, 1, 1))
                .bis(LocalDate.of(2020, 1, 1))
                .institution("TestInstitution")
                .beschaeftigungsart("TestBeschaeftigungsart")
                .beschreibung("TestBeschreibung")
                .id(-1)
                .build();
    }

    @AfterEach
    public void tearDown(){
        try {
            qualifikationDTO.setId(qualifikationTest.getId());
            qualifikationControl.removeQualifikation(qualifikationDTO);
        }
        catch (Exception e){
            logger.error("Fehler beim Löschen der Testdaten");
        }
        studentRepository.deleteById(student.getId());
        qualifikationTest = null;
        qualifikationDTO = null;
    }

    @Test
    public void test_AddQualificationWithPresentQualifikation() throws PersistenceException {
        qualifikationTest = Qualifikation.builder()
                .bezeichnung(qualifikationDTO.getBezeichnung())
                .bereich(qualifikationDTO.getBereich())
                .von(qualifikationDTO.getVon())
                .bis(qualifikationDTO.getBis())
                .institution(qualifikationDTO.getInstitution())
                .beschaftigungsverhaltnis(qualifikationDTO.getBeschaeftigungsart())
                .beschreibung(qualifikationDTO.getBeschreibung())
                .build();
        qualifikationTest.setStudent(student);

        qualifikationTest = qualifikationsRepository.save(qualifikationTest);

        qualifikationDTO.setId(qualifikationTest.getId());

        qualifikationControl.addUpdateQualifikation(qualifikationDTO, student);

        qualifikationTest = qualifikationsRepository.findById(qualifikationTest.getId()).orElseThrow();
        assertEquals(qualifikationDTO.getBezeichnung(), qualifikationTest.getBezeichnung());
        assertEquals(qualifikationTest.getStudent().getId(), student.getId());
    }

    @Test
    public void test_AddQualificationWithNewQualifikation() throws PersistenceException {
        qualifikationTest = qualifikationControl.addUpdateQualifikation(qualifikationDTO, student);

        qualifikationTest = qualifikationsRepository.findById(qualifikationTest.getId()).orElseThrow();
        assertEquals(qualifikationDTO.getBezeichnung(), qualifikationTest.getBezeichnung());
        assertEquals(qualifikationTest.getStudent().getId(), student.getId());
    }

    @Test
    public void test_RemoveQualificationWithNewQualifikation(){
        PersistenceException qualificationNotFound = assertThrows(PersistenceException.class, () -> qualifikationControl.removeQualifikation(qualifikationDTO));

        assertEquals(qualificationNotFound.getPersistenceExceptionType(), PersistenceException.PersistenceExceptionType.QUALIFIKATION_NOT_FOUND, MESSAGE);
        assertEquals("Qualifikation not found", qualificationNotFound.getReason(), MESSAGE2);
    }
}
