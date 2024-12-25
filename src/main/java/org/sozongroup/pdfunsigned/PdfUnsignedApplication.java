package org.sozongroup.pdfunsigned;

import javafx.application.Application;
import org.sozongroup.pdfunsigned.app.PdfUploadApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdfUnsignedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfUnsignedApplication.class, args);
        Application.launch(PdfUploadApp.class, args);
    }

}
