package com.ti.test.runner;

import lombok.extern.slf4j.Slf4j;
import com.ti.test.presentation.MainPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainRunner implements CommandLineRunner {

    @Autowired
    private MainPresentation mainPresentation;

    @Override
    public void run(String... args) throws Exception {
        log.info("Application runner start");
        mainPresentation.present();
    }
}
