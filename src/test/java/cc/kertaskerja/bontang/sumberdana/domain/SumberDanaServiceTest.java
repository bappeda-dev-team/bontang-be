package cc.kertaskerja.bontang.sumberdana.domain;

import cc.kertaskerja.bontang.sumberdana.domain.exception.SumberDanaNotFoundException;
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
class SumberDanaServiceTest {

    @Mock
    private SumberDanaRepository sumberDanaRepository;

    @InjectMocks
    private SumberDanaService sumberDanaService;

    @Test
    void findAll_returnsAllSumberDana() {
        List<SumberDana> sumberDanas = List.of(sampleSumberDana(1L));
        when(sumberDanaRepository.findAll()).thenReturn(sumberDanas);

        Iterable<SumberDana> result = sumberDanaService.findAll();

        assertThat(result).containsExactlyElementsOf(sumberDanas);
        verify(sumberDanaRepository).findAll();
    }

    @Test
    void detailSumberDanaById_returnsSumberDanaWhenFound() {
        Long id = 5L;
        SumberDana expected = sampleSumberDana(id);
        when(sumberDanaRepository.findById(id)).thenReturn(Optional.of(expected));

        SumberDana result = sumberDanaService.detailSumberDanaById(id);

        assertThat(result).isEqualTo(expected);
        verify(sumberDanaRepository).findById(id);
    }

    @Test
    void detailSumberDanaById_throwsWhenMissing() {
        Long id = 10L;
        when(sumberDanaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sumberDanaService.detailSumberDanaById(id))
                .isInstanceOf(SumberDanaNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(sumberDanaRepository).findById(id);
    }

    @Test
    void tambahSumberDana_savesAndReturnsEntity() {
        SumberDana sumberDana = sampleSumberDana(null);
        SumberDana persisted = sampleSumberDana(3L);
        when(sumberDanaRepository.save(sumberDana)).thenReturn(persisted);

        SumberDana result = sumberDanaService.tambahSumberDana(sumberDana);

        assertThat(result).isEqualTo(persisted);
        verify(sumberDanaRepository).save(sumberDana);
    }

    @Test
    void ubahSumberDana_savesWhenExists() {
        Long id = 7L;
        SumberDana sumberDana = sampleSumberDana(id);
        when(sumberDanaRepository.existsById(id)).thenReturn(true);
        when(sumberDanaRepository.save(sumberDana)).thenReturn(sumberDana);

        SumberDana result = sumberDanaService.ubahSumberDana(id, sumberDana);

        assertThat(result).isEqualTo(sumberDana);
        verify(sumberDanaRepository).existsById(id);
        verify(sumberDanaRepository).save(sumberDana);
    }

    @Test
    void ubahSumberDana_throwsWhenNotFound() {
        Long id = 8L;
        SumberDana sumberDana = sampleSumberDana(id);
        when(sumberDanaRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> sumberDanaService.ubahSumberDana(id, sumberDana))
                .isInstanceOf(SumberDanaNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(sumberDanaRepository).existsById(id);
        verify(sumberDanaRepository, never()).save(any());
    }

    @Test
    void hapusSumberDana_deletesWhenExists() {
        Long id = 9L;
        when(sumberDanaRepository.existsById(id)).thenReturn(true);

        sumberDanaService.hapusSumberDana(id);

        verify(sumberDanaRepository).existsById(id);
        verify(sumberDanaRepository).deleteById(id);
    }

    @Test
    void hapusSumberDana_throwsWhenNotFound() {
        Long id = 11L;
        when(sumberDanaRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> sumberDanaService.hapusSumberDana(id))
                .isInstanceOf(SumberDanaNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(sumberDanaRepository).existsById(id);
        verify(sumberDanaRepository, never()).deleteById(anyLong());
    }

    private SumberDana sampleSumberDana(Long id) {
        Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
        return new SumberDana(
                id,
                "KD-01",
                "Sumber Dana",
                "KD-02",
                SetInput.Ya,
                fixedInstant,
                fixedInstant
        );
    }
}
