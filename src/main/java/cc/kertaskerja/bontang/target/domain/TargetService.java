package cc.kertaskerja.bontang.target.domain;

import org.springframework.stereotype.Service;

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

    public Target tambahTarget(Target target) {

        return targetRepository.save(target);
    }

    public Target ubahTarget(Long id, Target target) {
        if (!targetRepository.existsById(id)) {
            throw new TargetNotFoundException(id);
        }

        return targetRepository.save(target);
    }

    public void hapusTarget(Long id) {
        if (!targetRepository.existsById(id)) {
            throw new TargetNotFoundException(id);
        }

        targetRepository.deleteById(id);
    }
}