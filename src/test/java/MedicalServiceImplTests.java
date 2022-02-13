import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;


public class MedicalServiceImplTests {
    @BeforeEach
    public void init() {
    }

    @BeforeAll
    public static void started() {
        System.out.println("Tests started.");
    }

    @AfterEach
    public void finished() {
        System.out.println("test completed.");
    }

    @AfterAll
    static void finishedAll() {
        System.out.println("Tests completed.");
    }

    String id = "User1";
    PatientInfo patientInfo = new PatientInfo(id, id, LocalDate.of(1001, 1, 1),
            new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80)));
    String warningMessage = "Warning, patient with id: null, need help";

    @Test
    public void testCheckBloodPressure() {
        BloodPressure bloodPressure = new BloodPressure(100, 60);
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id, bloodPressure);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(warningMessage, argumentCaptor.getValue());
    }

    @Test
    public void testCheckTemperature() {
        BigDecimal temperature = new BigDecimal("34.6");
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(id, temperature);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(warningMessage, argumentCaptor.getValue());
    }

    @Test
    public void testNormalHealthParameters() {
        BigDecimal temperature = new BigDecimal("36.6");
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(id, temperature);
        medicalService.checkBloodPressure(id, bloodPressure);
        Mockito.verify((sendAlertService), Mockito.times(0)).send(Mockito.any());
    }
}