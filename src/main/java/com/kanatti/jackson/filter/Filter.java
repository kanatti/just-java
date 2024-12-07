package com.kanatti.jackson.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.ToString;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "operator"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EqualsFilter.class, name = "EQ"),
    @JsonSubTypes.Type(value = InFilter.class, name = "IN"),
    @JsonSubTypes.Type(value = NotEqualsFilter.class, name = "NOT_EQ"),
    @JsonSubTypes.Type(value = AndFilter.class, name = "AND"),
    @JsonSubTypes.Type(value = OrFilter.class, name = "OR"),
})
@ToString
public abstract class Filter {
}