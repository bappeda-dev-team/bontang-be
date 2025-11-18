package cc.kertaskerja.bontang;

import cc.kertaskerja.bontang.config.BontangDataProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final BontangDataProperties bontangDataProperties;

    public HomeController(BontangDataProperties bontangDataProperties) {
        this.bontangDataProperties = bontangDataProperties;
    }

    @GetMapping("/")
    public String getStatus() {
        return bontangDataProperties.getStatus();
    }
}
