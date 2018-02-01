package net.mckoon.spider;

import java.lang.invoke.MethodHandles;

import javax.annotation.Nullable;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.mckoon.spider.console.ConsoleApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main executable class for the console application.
 */
public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Main() {
        /* Do not instantiate. */
    }

    /**
     * The entry point to the command-line application.
     * Bootstraps our dependency injection framework and executes the {@link ConsoleApplicationService}.
     *
     * @param args to the application.
     */
    public static void main(
            @Nullable String... args
    ) {
        LOGGER.info("Main started.");

        executeConsoleApplication(args);

        LOGGER.info("Main completed.");
    }

    private static void executeConsoleApplication(
            @Nullable String... args
    ) {
        Injector injector = createInjector();
        ConsoleApplicationService consoleApplicationService = injector.getInstance(ConsoleApplicationService.class);

        try {
            consoleApplicationService.execute(args);
        } catch (Exception exception) {
            LOGGER.error("Exception bubbled out to main.", exception);
            throw exception;
        }
    }

    private static Injector createInjector() {
        LOGGER.info("Creating Guice Injector.");

        Injector injector = Guice.createInjector(
                new SpiderGuiceModule()
        );

        LOGGER.info("Guice Injector created.");

        return injector;
    }

}
