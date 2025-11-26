package cc.kertaskerja.bontang.programprioritas.domain;

import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramPrioritasServiceTest {

    @Mock
    private ProgramPrioritasRepository programPrioritasRepository;

    private ProgramPrioritasService programPrioritasService;

    @BeforeEach
    void setUp() {
        programPrioritasService = new ProgramPrioritasService(programPrioritasRepository);
    }

    @Test
    void findAll_returnsAllProgramPrioritas() {
        ProgramPrioritas programPrioritas1 = sampleProgramPrioritas(1L, "Program Prioritas 1");
        ProgramPrioritas programPrioritas2 = sampleProgramPrioritas(2L, "Program Prioritas 2");
        List<ProgramPrioritas> programPrioritasList = Arrays.asList(programPrioritas1, programPrioritas2);

        when(programPrioritasRepository.findAll()).thenReturn(programPrioritasList);

        Iterable<ProgramPrioritas> result = programPrioritasService.findAll();

        assertEquals(programPrioritasList, result);
        verify(programPrioritasRepository).findAll();
    }

    @Test
    void detailProgramPrioritasById_returnsProgramPrioritas_whenFound() {
        Long id = 1L;
        ProgramPrioritas programPrioritas = sampleProgramPrioritas(id, "Program Prioritas 1");
        when(programPrioritasRepository.findById(id)).thenReturn(Optional.of(programPrioritas));

        ProgramPrioritas result = programPrioritasService.detailProgramPrioritasById(id);

        assertEquals(programPrioritas, result);
        verify(programPrioritasRepository).findById(id);
    }

    @Test
    void detailProgramPrioritasById_throwsException_whenNotFound() {
        Long id = 404L;
        when(programPrioritasRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProgramPrioritasNotFoundException.class, () -> programPrioritasService.detailProgramPrioritasById(id));
        verify(programPrioritasRepository).findById(id);
    }

    @Test
    void tambahProgramPrioritas_savesProgramPrioritas() {
        ProgramPrioritas programPrioritas = ProgramPrioritas.of("Program Prioritas Baru");
        ProgramPrioritas savedProgramPrioritas = sampleProgramPrioritas(1L, "Program Prioritas Baru");

        when(programPrioritasRepository.save(programPrioritas)).thenReturn(savedProgramPrioritas);

        ProgramPrioritas result = programPrioritasService.tambahProgramPrioritas(programPrioritas);

        assertEquals(savedProgramPrioritas, result);
        verify(programPrioritasRepository).save(programPrioritas);
    }

    @Test
    void ubahProgramPrioritas_savesProgramPrioritas_whenIdExists() {
        Long id = 1L;
        ProgramPrioritas programPrioritas = sampleProgramPrioritas(id, "Program Prioritas 1");
        ProgramPrioritas updatedProgramPrioritas = sampleProgramPrioritas(id, "Program Prioritas Updated");

        when(programPrioritasRepository.existsById(id)).thenReturn(true);
        when(programPrioritasRepository.save(programPrioritas)).thenReturn(updatedProgramPrioritas);

        ProgramPrioritas result = programPrioritasService.ubahProgramPrioritas(id, programPrioritas);

        assertEquals(updatedProgramPrioritas, result);
        verify(programPrioritasRepository).existsById(id);
        verify(programPrioritasRepository).save(programPrioritas);
    }

    @Test
    void ubahProgramPrioritas_throwsException_whenIdNotExists() {
        Long id = 404L;
        ProgramPrioritas programPrioritas = sampleProgramPrioritas(id, "Program Prioritas 1");

        when(programPrioritasRepository.existsById(id)).thenReturn(false);

        assertThrows(ProgramPrioritasNotFoundException.class, () -> programPrioritasService.ubahProgramPrioritas(id, programPrioritas));
        verify(programPrioritasRepository).existsById(id);
        verify(programPrioritasRepository, never()).save(any());
    }

    @Test
    void hapusProgramPrioritas_deletesProgramPrioritas_whenIdExists() {
        Long id = 1L;

        when(programPrioritasRepository.existsById(id)).thenReturn(true);

        programPrioritasService.hapusProgramPrioritas(id);

        verify(programPrioritasRepository).existsById(id);
        verify(programPrioritasRepository).deleteById(id);
    }

    @Test
    void hapusProgramPrioritas_throwsException_whenIdNotExists() {
        Long id = 404L;

        when(programPrioritasRepository.existsById(id)).thenReturn(false);

        assertThrows(ProgramPrioritasNotFoundException.class, () -> programPrioritasService.hapusProgramPrioritas(id));
        verify(programPrioritasRepository).existsById(id);
        verify(programPrioritasRepository, never()).deleteById(any());
    }

    private ProgramPrioritas sampleProgramPrioritas(Long id, String programPrioritasName) {
        Instant now = Instant.parse("2024-01-01T00:00:00Z");
        return new ProgramPrioritas(
                id,
                programPrioritasName,
                now,
                now
        );
    }
}
