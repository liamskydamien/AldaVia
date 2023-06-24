package org.hbrs.se2.project.aldavia.test.EntityTest;

import org.hbrs.se2.project.aldavia.entities.Qualifikation;
import org.hbrs.se2.project.aldavia.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QualifikationTest {

    @Test
    public void testMissingCode(){
        Qualifikation qualifikation = Qualifikation.builder()
                .id(1)
                .beschreibung("Test")
                .bezeichnung("Test")
                .beschaftigungsverhaltnis("Test")
                .bis(LocalDate.of(2020, 1, 1))
                .von(LocalDate.of(2020, 1, 1))
                .institution("Test")
                .student(null)
                .build();

        assertEquals(qualifikation.hashCode(), qualifikation.hashCode());
        assertNotEquals(qualifikation.hashCode(), Objects.hash(qualifikation.getId(), qualifikation.getBeschreibung(), qualifikation.getBezeichnung(), qualifikation.getBeschaftigungsverhaltnis(), qualifikation.getBis(), qualifikation.getVon(), qualifikation.getInstitution(), qualifikation.getStudent()));
        assertNotEquals(null, qualifikation);
        assertNotEquals(qualifikation, new Object());
        assertNotEquals(qualifikation, Qualifikation.builder().build());

        qualifikation.removeStudent(new Student());
        assertThrows(NullPointerException.class, () -> qualifikation.getStudent().getId());
    }
}
