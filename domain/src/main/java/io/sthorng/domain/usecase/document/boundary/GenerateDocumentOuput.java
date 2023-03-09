package io.sthorng.domain.usecase.document.boundary;

import lombok.Data;
import lombok.NonNull;

@Data
public class GenerateDocumentOuput {
    @NonNull String status;
}
