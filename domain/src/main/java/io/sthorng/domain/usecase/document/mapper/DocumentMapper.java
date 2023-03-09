package io.sthorng.domain.usecase.document.mapper;

import io.sthorng.domain.entity.Document;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentInput;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentOuput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "config.scanDirectory", source = "scanDirectory")
    @Mapping(target = "config.template", source = "template")
    Document mapFrom(GenerateDocumentInput input);

    GenerateDocumentOuput mapFrom(Document entity);

}
