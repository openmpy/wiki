package openmpy.taleswiki.document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "openmpy.taleswiki")
public class DocumentApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DocumentApplication.class, args);
    }
}
