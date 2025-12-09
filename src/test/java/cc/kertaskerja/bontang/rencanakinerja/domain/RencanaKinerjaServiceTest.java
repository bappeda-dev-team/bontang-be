package cc.kertaskerja.bontang.rencanakinerja.domain;

import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RencanaKinerjaServiceTest {

    @Mock
    private RencanaKinerjaRepository rencanaKinerjaRepository;

    @InjectMocks
    private RencanaKinerjaService rencanaKinerjaService;

    @Test
    void findAll_returnsAllRencanaKinerja() {
        List<RencanaKinerja> data = List.of(sampleRencanaKinerja(1L), sampleRencanaKinerja(2L));
        when(rencanaKinerjaRepository.findAll()).thenReturn(data);

        Iterable<RencanaKinerja> result = rencanaKinerjaService.findAll();

        assertThat(result).containsExactlyElementsOf(data);
        verify(rencanaKinerjaRepository).findAll();
    }

    @Test
    void detailRencanaKinerjaById_returnsRencana_whenFound() {
        Long id = 5L;
        RencanaKinerja expected = sampleRencanaKinerja(id);
        when(rencanaKinerjaRepository.findById(id)).thenReturn(Optional.of(expected));

        RencanaKinerja result = rencanaKinerjaService.detailRencanaKinerjaById(id);

        assertThat(result).isEqualTo(expected);
        verify(rencanaKinerjaRepository).findById(id);
    }

    @Test
    void detailRencanaKinerjaById_throws_whenNotFound() {
        Long id = 7L;
        when(rencanaKinerjaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rencanaKinerjaService.detailRencanaKinerjaById(id))
                .isInstanceOf(RencanaKinerjaNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(rencanaKinerjaRepository).findById(id);
    }

    @Test
    void tambahRencanaKinerja_savesEntity() {
        RencanaKinerja newData = sampleRencanaKinerja(null);
        RencanaKinerja persisted = sampleRencanaKinerja(3L);
        when(rencanaKinerjaRepository.save(newData)).thenReturn(persisted);

        RencanaKinerja result = rencanaKinerjaService.tambahRencanaKinerja(newData);

        assertThat(result).isEqualTo(persisted);
        verify(rencanaKinerjaRepository).save(newData);
    }

    @Test
    void ubahRencanaKinerja_saves_whenExists() {
        Long id = 9L;
        RencanaKinerja updated = sampleRencanaKinerja(id);
        when(rencanaKinerjaRepository.existsById(id)).thenReturn(true);
        when(rencanaKinerjaRepository.save(updated)).thenReturn(updated);

        RencanaKinerja result = rencanaKinerjaService.ubahRencanaKinerja(id, updated);

        assertThat(result).isEqualTo(updated);
        verify(rencanaKinerjaRepository).existsById(id);
        verify(rencanaKinerjaRepository).save(updated);
    }

    @Test
    void ubahRencanaKinerja_throws_whenMissing() {
        Long id = 10L;
        RencanaKinerja updated = sampleRencanaKinerja(id);
        when(rencanaKinerjaRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> rencanaKinerjaService.ubahRencanaKinerja(id, updated))
                .isInstanceOf(RencanaKinerjaNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(rencanaKinerjaRepository).existsById(id);
        verify(rencanaKinerjaRepository, never()).save(any());
    }

    @Test
    void hapusRencanaKinerja_deletes_whenExists() {
        Long id = 11L;
        when(rencanaKinerjaRepository.existsById(id)).thenReturn(true);

        rencanaKinerjaService.hapusRencanaKinerja(id);

        verify(rencanaKinerjaRepository).existsById(id);
        verify(rencanaKinerjaRepository).deleteById(id);
    }

    @Test
    void hapusRencanaKinerja_throws_whenMissing() {
        Long id = 12L;
        when(rencanaKinerjaRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> rencanaKinerjaService.hapusRencanaKinerja(id))
                .isInstanceOf(RencanaKinerjaNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(rencanaKinerjaRepository).existsById(id);
        verify(rencanaKinerjaRepository, never()).deleteById(anyLong());
    }

    private RencanaKinerja sampleRencanaKinerja(Long id) {
        Instant fixed = Instant.parse("2024-02-01T00:00:00Z");
        return new RencanaKinerja(
                id,
                "Rencana " + (id == null ? "baru" : id),
                "Indikator " + (id == null ? "baru" : id),
                "Target " + (id == null ? "baru" : id),
                "APBD",
                "Keterangan",
                fixed,
                fixed
        );
    }
}
