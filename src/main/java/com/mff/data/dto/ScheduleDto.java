package com.mff.data.dto;

import com.mff.data.entities.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private Boolean isPeriodic;

    private Integer periodLength;

    private Schedule.PERIOD_UNIT periodUnit;

    private Date startDate;

    private Integer baseDayOfMonth;

    @NotNull
    private Boolean isPriorWeekend;

    private Date nextDate;

    private Boolean canDelete;
}
