package io.sthorng.domain.port;

import io.sthorng.domain.entity.Document;

public interface DocumentPort extends IPort {

    Document generate(Document document);
}
