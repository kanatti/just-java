package com.kanatti.jackson.filter2.operations;

import com.kanatti.jackson.filter2.OnFilter;

import lombok.ToString;

@ToString
public class EqualsOp extends OnFilter {
    public String column;
    public Object value;
}
