package io.sthorng.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "Karasheet", description = "Generate karaoke sheet from a a folder and template source.")
@ComponentScan(basePackages = {"io.sthorng.*"})
@EnableAutoConfiguration
@SpringBootApplication
public class Launcher implements CommandLineRunner, ExitCodeGenerator {
    private CommandLine.IFactory factory;
    private CustomCommand command;
    private int exitCode;

    @Autowired
    public Launcher(CommandLine.IFactory factory, CustomCommand command) {
        this.factory = factory;
        this.command = command;
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(command, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(Launcher.class, args)));
    }
}
