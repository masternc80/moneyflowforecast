package com.mff.data.dto;

public interface ConvertibleDto<T> {
    T toEntity();
}
