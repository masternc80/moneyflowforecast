package com.mff.data.dto;

import com.mff.data.entities.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewScheduleDto {

    @NotNull
    @NonNull
    private Boolean isPeriodic;

    @NotNull
    @NonNull
    private String name;

    private Integer periodLength;

    private Schedule.PERIOD_UNIT periodUnit;

    private Date startDate;

    private Integer baseDayOfMonth;

    @NotNull
    @NonNull
    private Boolean isPriorWeekend;
}
