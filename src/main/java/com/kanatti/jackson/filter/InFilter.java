package com.kanatti.jackson.filter;

import java.util.List;

import lombok.ToString;

@ToString
public class InFilter extends Filter {
    public String column;
    public List<Object> values;
}
