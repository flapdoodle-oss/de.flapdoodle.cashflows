/*
 * Copyright (C) 2022
 *   Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.cashflows.types;

import de.flapdoodle.formula.types.HasHumanReadableLabel;
import de.flapdoodle.reflection.TypeInfo;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
public interface Id<T> extends HasHumanReadableLabel {
    @Value.Default
    default String id() {
        return UUID.randomUUID().toString();
    }

    TypeInfo<T> type();

    @Override
    @Value.Default
    default String asHumanReadable() {
        return "Id("+type()+":"+id()+")";
    }

    static <T> Id<T> of(String id, TypeInfo<T> type) {
        return ImmutableId.<T>builder()
                .id(id)
                .type(type)
                .build();
    }

    static <T> ImmutableId<T> of(TypeInfo<T> type) {
        return ImmutableId.<T>builder()
                .type(type)
                .build();
    }
}
