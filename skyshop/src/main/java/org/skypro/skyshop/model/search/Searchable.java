package org.skypro.skyshop.model.search;

import java.util.UUID;

public interface Searchable {
    UUID getId();
    String getSearchTerm();
    String getContentType();
    String getSearchableName();

    default String getStringRepresentation() {
        return getSearchableName();
    }
}
