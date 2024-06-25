package com.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerApplicationTests {

	@Test
	void applicationStarts() {
		CustomerApplication.main(new String[] {});
		Assertions.assertDoesNotThrow(() -> {
		});

	}

}
