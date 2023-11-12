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
