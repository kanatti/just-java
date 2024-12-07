package com.kanatti.jackson.filter2;

import java.util.List;

import lombok.ToString;

@ToString
public class AndFilter extends Filter {
    public List<Filter> filters;

    public AndFilter(List<Filter> filters) {
        this.filters = filters;
    }
}
