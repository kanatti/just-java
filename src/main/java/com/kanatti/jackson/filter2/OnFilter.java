package com.kanatti.jackson.filter2;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kanatti.jackson.filter2.operations.EqualsOp;
import com.kanatti.jackson.filter2.operations.InOp;
import com.kanatti.jackson.filter2.operations.NotEqualsOp;

import lombok.ToString;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "operator"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EqualsOp.class, name = "EQ"),
    @JsonSubTypes.Type(value = InOp.class, name = "IN"),
    @JsonSubTypes.Type(value = NotEqualsOp.class, name = "NOT_EQ"),
})
@ToString
public abstract class OnFilter extends Filter {
    
}
