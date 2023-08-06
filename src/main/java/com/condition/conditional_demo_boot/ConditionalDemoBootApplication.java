package com.condition.conditional_demo_boot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;



@SpringBootApplication
public class ConditionalDemoBootApplication {

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConditionalDemoBootApplication.class, args);

        System.out.println("Application started...");
/**
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // setup configuration
        applicationContext.scan("");

        // setup all the dependencies (refresh) and make them run (start)
        applicationContext.refresh();
        applicationContext.start();

        try {
            applicationContext.getBean(Config.class).bean.run();
        } finally {
            applicationContext.close();
        }
        */
    }

    @Configuration
    public static class Config {

        @Autowired
        MyBean bean;
    }

    public static class LocalConfigCondition implements Condition {

        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            return "local".equals(conditionContext.getEnvironment().getProperty("config.condition", "local"));
        }
    }

    public static class RemoteConfigCondition implements Condition {

        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            return "remote".equals(conditionContext.getEnvironment().getProperty("config.condition","remote"));
        }
    }

    public interface MyBean {

    	@PostConstruct
        default void run() {
            System.out.println("My Bean default is running");
        }
    }

    @Component
    @Conditional(LocalConfigCondition.class)
    public static final class LocalBean implements MyBean {

        public void run() {
            System.out.println("Local bean is running");
        }
    }

    @Component
    @Conditional(RemoteConfigCondition.class)
    public static final class RemoteBean implements MyBean {

        public void run() {
            System.out.println("Remote bean is running");
        }
    }



}
