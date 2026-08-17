package br.com.accenture.dealership_management;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class DealershipManagementApplicationMainTest {

    @Test
    void shouldInvokeSpringApplicationRunInMain() {
        String[] args = new String[]{"--spring.main.web-application-type=none"};

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            DealershipManagementApplication.main(args);
            mocked.verify(() -> SpringApplication.run(DealershipManagementApplication.class, args));
        }
    }
}

