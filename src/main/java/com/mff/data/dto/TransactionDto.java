package com.mff.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TransactionDto {
    private int id;

    private String account_id;

    private Date date;

    private boolean credit;

    private double amount;

    private String description;
}
