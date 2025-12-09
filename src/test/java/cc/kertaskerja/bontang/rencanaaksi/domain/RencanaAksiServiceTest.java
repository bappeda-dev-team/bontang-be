package cc.kertaskerja.bontang.rencanaaksi.domain;

import cc.kertaskerja.bontang.rencanaaksi.domain.exception.RencanaAksiNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RencanaAksiServiceTest {

    @Mock
    private RencanaAksiRepository rencanaAksiRepository;

    @InjectMocks
    private RencanaAksiService rencanaAksiService;

    @Test
    void findAll_returnsAllRencanaAksi() {
        List<RencanaAksi> data = List.of(sampleRencanaAksi(1L), sampleRencanaAksi(2L));
        when(rencanaAksiRepository.findAll()).thenReturn(data);

        Iterable<RencanaAksi> result = rencanaAksiService.findAll();

        assertThat(result).containsExactlyElementsOf(data);
        verify(rencanaAksiRepository).findAll();
    }

    @Test
    void detailRencanaAksiById_returnsEntity_whenFound() {
        Long id = 5L;
        RencanaAksi expected = sampleRencanaAksi(id);
        when(rencanaAksiRepository.findById(id)).thenReturn(Optional.of(expected));

        RencanaAksi result = rencanaAksiService.detailRencanaAksiById(id);

        assertThat(result).isEqualTo(expected);
        verify(rencanaAksiRepository).findById(id);
    }

    @Test
    void detailRencanaAksiById_throws_whenNotFound() {
        Long id = 6L;
        when(rencanaAksiRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rencanaAksiService.detailRencanaAksiById(id))
                .isInstanceOf(RencanaAksiNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(rencanaAksiRepository).findById(id);
    }

    @Test
    void tambahRencanaAksi_savesEntity() {
        RencanaAksi newData = sampleRencanaAksi(null);
        RencanaAksi persisted = sampleRencanaAksi(3L);
        when(rencanaAksiRepository.save(newData)).thenReturn(persisted);

        RencanaAksi result = rencanaAksiService.tambahRencanaAksi(newData);

        assertThat(result).isEqualTo(persisted);
        verify(rencanaAksiRepository).save(newData);
    }

    @Test
    void ubahRencanaAksi_saves_whenExists() {
        Long id = 7L;
        RencanaAksi updated = sampleRencanaAksi(id);
        when(rencanaAksiRepository.existsById(id)).thenReturn(true);
        when(rencanaAksiRepository.save(updated)).thenReturn(updated);

        RencanaAksi result = rencanaAksiService.ubahRencanaAksi(id, updated);

        assertThat(result).isEqualTo(updated);
        verify(rencanaAksiRepository).existsById(id);
        verify(rencanaAksiRepository).save(updated);
    }

    @Test
    void ubahRencanaAksi_throws_whenMissing() {
        Long id = 8L;
        RencanaAksi updated = sampleRencanaAksi(id);
        when(rencanaAksiRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> rencanaAksiService.ubahRencanaAksi(id, updated))
                .isInstanceOf(RencanaAksiNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(rencanaAksiRepository).existsById(id);
        verify(rencanaAksiRepository, never()).save(any());
    }

    @Test
    void hapusRencanaAksi_deletes_whenExists() {
        Long id = 9L;
        when(rencanaAksiRepository.existsById(id)).thenReturn(true);

        rencanaAksiService.hapusRencanaAksi(id);

        verify(rencanaAksiRepository).existsById(id);
        verify(rencanaAksiRepository).deleteById(id);
    }

    @Test
    void hapusRencanaAksi_throws_whenMissing() {
        Long id = 10L;
        when(rencanaAksiRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> rencanaAksiService.hapusRencanaAksi(id))
                .isInstanceOf(RencanaAksiNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(rencanaAksiRepository).existsById(id);
        verify(rencanaAksiRepository, never()).deleteById(anyLong());
    }

    private RencanaAksi sampleRencanaAksi(Long id) {
        Instant fixed = Instant.parse("2024-02-01T00:00:00Z");
        return new RencanaAksi(
                id,
                "Rencana " + (id == null ? "baru" : id),
                id == null ? 1 : id.intValue(),
                fixed,
                fixed
        );
    }
}
