package io.sthorng.domain.validator;

import io.sthorng.domain.entity.DocumentConfig;
import io.sthorng.domain.exception.ValidationException;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

public class DocumentValidator {

    public static void validate(DocumentConfig documentConfig) throws ValidationException {

        validateSourceDirectory(documentConfig);
        validateTemplate(documentConfig);

    }

    private static void validateSourceDirectory(DocumentConfig config) throws ValidationException {
        var scanDirectory = new File(config.getScanDirectory());
        if (!scanDirectory.exists()) {
            throw new ValidationException("Scan Directory " + scanDirectory + " doesn't exist.");
        }

        if (scanDirectory.isFile()) {
            throw new ValidationException("Scan Directory " + scanDirectory + " isn't a directory.");
        }

        if (ArrayUtils.isEmpty(scanDirectory.listFiles())) {
            throw new ValidationException("Scan Directory " + scanDirectory + " is empty.");
        }

    }

    private static void validateTemplate(DocumentConfig config) throws ValidationException {
        val template = new File(config.getTemplate());
        if (!template.exists()) {
            throw new ValidationException("Template " + template + " doesn't exist.");
        }

        if (!template.isFile()) {
            throw new ValidationException("Template " + template + " isn't a file.");
        }

        if (!template.canRead()) {
            throw new ValidationException("You doesn't have authorization you read the file " + template);
        }
    }
}
