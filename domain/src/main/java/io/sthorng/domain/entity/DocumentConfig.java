package io.sthorng.domain.entity;

import io.sthorng.domain.enums.Order;
import io.sthorng.domain.enums.Sort;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentConfig {
    @NonNull
    private String scanDirectory;
    @NonNull
    private String template;
    @NonNull
    private Sort sort = Sort.ALPHABETIC;
    @NonNull
    private Order order = Order.ASC;
    @NonNull
    private List<TitleFormat> formats = new ArrayList<>();

}

