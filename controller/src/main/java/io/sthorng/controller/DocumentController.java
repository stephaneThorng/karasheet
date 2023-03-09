package io.sthorng.controller;

import io.sthorng.domain.exception.ValidationException;
import io.sthorng.domain.usecase.document.GenerateDocumentUsecase;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentInput;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentOuput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DocumentController {
    private GenerateDocumentUsecase generateDocumentUsecase;

    @Autowired
    public DocumentController(GenerateDocumentUsecase generateDocumentUsecase) {
        this.generateDocumentUsecase = generateDocumentUsecase;
    }

    public GenerateDocumentOuput generateDocument(GenerateDocumentInput input) throws ValidationException {
        return generateDocumentUsecase.process(input);
    }
}
