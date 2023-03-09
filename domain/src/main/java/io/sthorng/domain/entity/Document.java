package io.sthorng.domain.entity;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class Document {
    @NonNull
    private String filename;
    private String path;
    private String size;
    private Date createdAt;
    private Date updatedAt;
    @NonNull
    private DocumentConfig config;
}
