package com.home.releasedemo;

import org.assertj.core.api.Assertions;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.security.web.SecurityFilterChain;
import show.me.demo.DemoSecurityAutoconfiguration;

class AutoconfigurationTests {


	void haveBeans() {
		new WebApplicationContextRunner()
				.withConfiguration(AutoConfigurations.of(DemoSecurityAutoconfiguration.class))
				.run((context) -> {
					Assertions.assertThat(context).hasSingleBean(SecurityFilterChain.class);
				});

	}

}
