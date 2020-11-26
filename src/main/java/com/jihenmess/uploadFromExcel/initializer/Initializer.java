package com.jihenmess.uploadFromExcel.initializer;

import com.jihenmess.uploadFromExcel.service.uploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Jihen.Messaabi
 */
@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private uploadService uploadService;

    @Override
    public void run(String... args) throws IOException {
        uploadService.uploadFile();
    }
}
