package com.mff.data.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Builder
public class TransactionDto {
    private int id;

    private String accountId;

    private Date date;

    private boolean credit;

    private double amount;

    private String description;
}
