/*
 * Copyright (c) 2019. This is property of the Patrone Network and it's corresponding entities. This code may not be re-distributed in any way shape or form.
 */

package me.wolflie.simplehome.config.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSection {
    String value();

}
