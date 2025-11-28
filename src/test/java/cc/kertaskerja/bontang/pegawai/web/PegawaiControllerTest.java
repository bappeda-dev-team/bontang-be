package cc.kertaskerja.bontang.pegawai.web;

import cc.kertaskerja.bontang.pegawai.domain.Pegawai;
import cc.kertaskerja.bontang.pegawai.domain.PegawaiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PegawaiControllerTest {

    @Mock
    private PegawaiService pegawaiService;

    private PegawaiController pegawaiController;

    @BeforeEach
    void setUp() {
        pegawaiController = new PegawaiController(pegawaiService);
    }

    @Test
    void findAll_returnsAllPegawaiFromService() {
        Iterable<Pegawai> pegawaiList = List.of(
                new Pegawai(1L, 10L, "John Doe", "12345", "john@example.com", "Staff", "Tim A", Instant.now(), Instant.now()),
                new Pegawai(2L, 20L, "Jane Doe", "67890", "jane@example.com", "Manager", "Tim B", Instant.now(), Instant.now())
        );

        when(pegawaiService.findAll()).thenReturn(pegawaiList);

        Iterable<Pegawai> result = pegawaiController.findAll();

        assertEquals(pegawaiList, result);
        verify(pegawaiService).findAll();
    }

    @Test
    void getByNip_returnsPegawaiFromService() {
        String nip = "12345";
        Pegawai pegawai = new Pegawai(1L, 10L, "John Doe", nip, "john@example.com", "Staff", "Tim A", Instant.now(), Instant.now());

        when(pegawaiService.detailPegawaiByNip(nip)).thenReturn(pegawai);

        Pegawai result = pegawaiController.getByNip(nip);

        assertEquals(pegawai, result);
        verify(pegawaiService).detailPegawaiByNip(nip);
    }

    @Test
    void put_updatesPegawaiUsingService() {
        String nip = "12345";
        Pegawai existingPegawai = new Pegawai(1L, 10L, "John Doe", nip, "john@example.com", "Staff", "Tim A", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        PegawaiRequest request = new PegawaiRequest(null, "Jane Doe", "67890", "jane@example.com", "Manager", "Tim B", "OPD-001");
        Pegawai updatedPegawai = new Pegawai(1L, 30L, request.namaPegawai(), request.nip(), request.email(), request.jabatanDinas(), request.jabatanTim(), existingPegawai.createdDate(), Instant.parse("2024-01-03T00:00:00Z"));

        when(pegawaiService.detailPegawaiByNip(nip)).thenReturn(existingPegawai);
        when(pegawaiService.ubahPegawai(eq(nip), any(Pegawai.class), eq(request.kodeOpd()))).thenReturn(updatedPegawai);

        Pegawai result = pegawaiController.put(nip, request);

        ArgumentCaptor<Pegawai> pegawaiCaptor = ArgumentCaptor.forClass(Pegawai.class);
        verify(pegawaiService).detailPegawaiByNip(nip);
        verify(pegawaiService).ubahPegawai(eq(nip), pegawaiCaptor.capture(), eq(request.kodeOpd()));

        Pegawai pegawaiPassed = pegawaiCaptor.getValue();
        assertEquals(existingPegawai.id(), pegawaiPassed.id());
        assertEquals(request.namaPegawai(), pegawaiPassed.namaPegawai());
        assertEquals(request.nip(), pegawaiPassed.nip());
        assertEquals(request.email(), pegawaiPassed.email());
        assertEquals(request.jabatanDinas(), pegawaiPassed.jabatanDinas());
        assertEquals(request.jabatanTim(), pegawaiPassed.jabatanTim());
        assertEquals(existingPegawai.createdDate(), pegawaiPassed.createdDate());
        assertNull(pegawaiPassed.lastModifiedDate());
        assertNull(pegawaiPassed.opdId());

        assertEquals(updatedPegawai, result);
    }

    @Test
    void post_createsPegawaiAndReturnsCreatedResponse() {
        PegawaiRequest request = new PegawaiRequest(null, "John Doe", "12345", "john@example.com", "Staff", "Tim A", "OPD-001");
        Pegawai savedPegawai = new Pegawai(1L, 20L, request.namaPegawai(), request.nip(), request.email(), request.jabatanDinas(), request.jabatanTim(), Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(pegawaiService.tambahPegawai(any(Pegawai.class), eq(request.kodeOpd()))).thenReturn(savedPegawai);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/pegawai");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<Pegawai> response = pegawaiController.post(request);

        ArgumentCaptor<Pegawai> pegawaiCaptor = ArgumentCaptor.forClass(Pegawai.class);
        verify(pegawaiService).tambahPegawai(pegawaiCaptor.capture(), eq(request.kodeOpd()));

        Pegawai pegawaiPassed = pegawaiCaptor.getValue();
        assertNull(pegawaiPassed.id());
        assertNull(pegawaiPassed.opdId());
        assertEquals(request.namaPegawai(), pegawaiPassed.namaPegawai());
        assertEquals(request.nip(), pegawaiPassed.nip());
        assertEquals(request.email(), pegawaiPassed.email());
        assertEquals(request.jabatanDinas(), pegawaiPassed.jabatanDinas());
        assertEquals(request.jabatanTim(), pegawaiPassed.jabatanTim());
        assertNull(pegawaiPassed.createdDate());
        assertNull(pegawaiPassed.lastModifiedDate());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedPegawai, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/pegawai/" + savedPegawai.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesPegawaiUsingService() {
        String nip = "12345";

        pegawaiController.delete(nip);

        verify(pegawaiService).hapusPegawai(nip);
    }
}
