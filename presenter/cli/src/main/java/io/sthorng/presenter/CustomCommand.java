package io.sthorng.presenter;

import io.sthorng.controller.DocumentController;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentInput;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentOuput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

@Component
@Slf4j
@CommandLine.Command(name = "Karasheet", description = "Generate karaoke sheet from a a folder and template source.")
public class CustomCommand implements Callable<Integer> {

    @Autowired
    private DocumentController controller;
    @CommandLine.Option(names = {"-t", "--template"}, description = "template document", required = true)
    private String template;
    @CommandLine.Option(names = {"-i", "--input"}, description = "input/source scan directory", required = true)
    private String input;
    @CommandLine.Option(names = {"-o", "--output"}, description = "output generated file", defaultValue = "output.docx")
    private String output;
    @CommandLine.Option(names = {"-p", "--pattern"}, description = "replace titles pattern")
    private Map<String, Pattern> patterns;

    enum Sort {ALPHABETIC, CREATE_DATE, UPDATE_DATE}

    @CommandLine.Option(names = "--sort", description = "Sorted Mode")
    Sort sort = Sort.ALPHABETIC;

    enum Order {ASC, DESC}

    @CommandLine.Option(names = "--order", description = "Ordered Mode")
    Order order = Order.ASC;


    @Override
    public Integer call() throws Exception {
        GenerateDocumentInput filename = new GenerateDocumentInput(output, template, input);
        controller.generateDocument(filename);

        return -1;

    }
}