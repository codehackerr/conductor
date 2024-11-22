/*
 * Copyright 2024 Conductor Authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.common.constraints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class NameValidator implements ConstraintValidator<ValidNameConstraint, String> {

    private static final String NAME_PATTERN = "^[A-Za-z0-9_<>{}#\\s-]+$";
    public static final String INVALID_NAME_MESSAGE =
            "Allowed characters are alphanumeric, underscores, spaces, hyphens, and special characters like <, >, {, }, #";

    @Value("${conductor.app.workflow.name-validation.enabled}")
    private boolean nameValidationEnabled;

    @Override
    public void initialize(ValidNameConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        boolean valid = name == null || !nameValidationEnabled || name.matches(NAME_PATTERN);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Invalid name '" + name + "'. " + INVALID_NAME_MESSAGE)
                    .addConstraintViolation();
        }
        return valid;
    }
}
