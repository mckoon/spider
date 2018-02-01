package net.mckoon.spider.console;

import javax.annotation.Nullable;

/**
 * Service that runs the entire console application.
 */
public interface ConsoleApplicationService {

    /**
     * Runs the application, reading in the input data.
     *
     * @param args the arguments passed to the console application.
     */
    void execute(
            @Nullable String... args
    );

}
