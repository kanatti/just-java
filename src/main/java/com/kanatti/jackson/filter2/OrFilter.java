package com.kanatti.jackson.filter2;

import java.util.List;

import lombok.ToString;

@ToString
public class OrFilter extends Filter {
    public List<Filter> filters;

    public OrFilter(List<Filter> filters) {
        this.filters = filters;
    }
}
