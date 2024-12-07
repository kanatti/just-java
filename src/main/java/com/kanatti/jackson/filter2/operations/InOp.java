package com.kanatti.jackson.filter2.operations;

import java.util.List;

import com.kanatti.jackson.filter2.OnFilter;

import lombok.ToString;

@ToString
public class InOp extends OnFilter {
    public String column;
    public List<Object> values;
}
