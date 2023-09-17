package show.me.demo.components;

import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

public class ApplicationReportListener implements ApplicationListener<ApplicationEvent>, Ordered {

    private final ConditionEvaluationReport report;

    public ApplicationReportListener(ConditionEvaluationReport report) {
        this.report = report;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (!(event instanceof ContextRefreshedEvent refreshedEvent)) {
            return;
        }
        int[] count = new int[1];
        System.out.println("""
                ***************************************************************************************
                                           Applied AutoConfigurations
                ***************************************************************************************
                """);
        report.getConditionAndOutcomesBySource().forEach((k,v) -> {
            if (v.isFullMatch()) {
                System.out.print("\t");
                System.out.print(k);
                System.out.println();
                count[0]++;
            }
        });
        System.out.println();
        System.out.println("TOTAL: " + report.getConditionAndOutcomesBySource().size());
        System.out.println("APPLIED: " + count[0]);
        System.out.println("BEAN DEFINITIONS: " + refreshedEvent.getApplicationContext().getBeanDefinitionCount());
        System.out.println("BEANS: " + refreshedEvent.getApplicationContext().getBeansOfType(Object.class).size());
        System.out.println();


        if (refreshedEvent.getApplicationContext().getEnvironment().getProperty("report.beans", boolean.class, false)) {
            refreshedEvent.getApplicationContext().getBeansOfType(Object.class)
                    .forEach((n,b) -> System.out.println("\t" + n + "\t" + b.getClass()));
        }

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
