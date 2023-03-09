package io.sthorng.domain.usecase.document.boundary;

import io.sthorng.domain.entity.TitleFormat;
import io.sthorng.domain.enums.Order;
import io.sthorng.domain.enums.Sort;
import lombok.Data;
import lombok.NonNull;

import java.util.List;


@Data
public class GenerateDocumentInput {
    @NonNull
    private String filename;
    @NonNull
    private String template;
    @NonNull
    private String scanDirectory;
    private Sort sort;
    private Order order;
    private List<TitleFormat> formatTitle;

}
