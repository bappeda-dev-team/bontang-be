package cc.kertaskerja.bontang.target.domain;

import cc.kertaskerja.bontang.target.web.TargetRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TargetService {
	private TargetRepository targetRepository;

    public TargetService(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    public Iterable<Target> findAllTargets() {
        return targetRepository.findAll();
    }

    public Target detailTargetById(Long id) {
        return targetRepository.findById(id)
                .orElseThrow(() -> new TargetNotFoundException(id));
    }

    public Target tambahTarget(String target, String satuan, Long indikatorId) {
        Target newTarget = Target.of(
                target,
                satuan,
                indikatorId
        );
        return targetRepository.save(newTarget);
    }

    public Target tambahTarget(TargetRequest request) {
        Target target = Target.of(
                request.target(),
                request.satuan(),
                request.indikatorId()
        );
        return targetRepository.save(target);
    }

    public Target ubahTarget(Long id, TargetRequest request) {
        Target existingTarget = detailTargetById(id);

        if (!Objects.equals(existingTarget.id(), id)) {
            throw new TargetNotFoundException(id);
        }
        Target target = new Target(
                existingTarget.id(),
                request.target(),
                request.satuan(),
                existingTarget.indikatorId(),
                existingTarget.createdDate(),
                null
        );

        return targetRepository.save(target);
    }

    public void hapusTarget(Long id) {
        if (!targetRepository.existsById(id)) {
            throw new TargetNotFoundException(id);
        }

        targetRepository.deleteById(id);
    }

    public Optional<Target> findFirstByTarget(String target) {
        return targetRepository.findFirstByTarget(target);
    }

    public List<Target> findByIndikatorId(Long indikatorId) {
        return targetRepository.findByIndikatorId(indikatorId);
    }
}
