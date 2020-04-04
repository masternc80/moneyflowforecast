package com.mff.data.dto;

import com.mff.data.entities.Schedule;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto implements ConvertibleDto<Schedule> {

    @NotNull
    private int id;

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

    @Override
    public Schedule toEntity() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(this, Schedule.class);
    }
}
