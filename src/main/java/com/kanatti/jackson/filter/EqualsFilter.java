package com.kanatti.jackson.filter;

import lombok.ToString;

@ToString
public class EqualsFilter extends Filter {
    public String column;
    public Object value;
}
