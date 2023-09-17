package show.me.demo;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import show.me.demo.components.ApplicationReportListener;

@AutoConfiguration
public class DemoReporterAutoconfiguration {

    @Bean
    static ApplicationListener<ApplicationEvent> reportListener(ConditionEvaluationReport autoConfigurationReport) {
        return new ApplicationReportListener(autoConfigurationReport);
    }
}

