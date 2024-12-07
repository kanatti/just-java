package com.kanatti.jackson.filter;

import lombok.ToString;

@ToString
public class NotEqualsFilter extends Filter {
    public String column;
    public Object value;
}
