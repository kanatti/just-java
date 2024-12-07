package com.kanatti.jackson.filter;

import java.util.List;

import lombok.ToString;

@ToString
public class AndFilter extends Filter {
    public List<Filter> filters;
}
