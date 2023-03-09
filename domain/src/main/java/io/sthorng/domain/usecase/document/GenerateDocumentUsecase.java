package io.sthorng.domain.usecase.document;

import io.sthorng.domain.entity.Document;
import io.sthorng.domain.exception.ValidationException;
import io.sthorng.domain.port.DocumentPort;
import io.sthorng.domain.usecase.IUsecase;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentInput;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentOuput;
import io.sthorng.domain.usecase.document.mapper.DocumentMapper;
import io.sthorng.domain.validator.DocumentValidator;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateDocumentUsecase implements IUsecase<GenerateDocumentInput, GenerateDocumentOuput> {

    private DocumentMapper mapper;
    private DocumentPort documentPort;

    @Autowired
    public GenerateDocumentUsecase(
            DocumentMapper mapper,
            DocumentPort documentPort) {
        this.mapper = mapper;
        this.documentPort = documentPort;
    }

    @NonNull
    @Override
    public GenerateDocumentOuput process(GenerateDocumentInput input) throws ValidationException {
        Document document = mapper.mapFrom(input);
        DocumentValidator.validate(document.getConfig());
        documentPort.generate(document);

        return new GenerateDocumentOuput("OK");
    }
}
