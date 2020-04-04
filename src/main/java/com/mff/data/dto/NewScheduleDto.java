package com.mff.data.dto;

import com.mff.data.entities.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewScheduleDto implements ConvertibleDto<Schedule> {

    @NotNull
    private Boolean isPeriodic;

    @NotNull
    private String name;

    private Integer periodLength;

    private Schedule.PERIOD_UNIT periodUnit;

    private Date startDate;

    private Integer baseDayOfMonth;

    @NotNull
    private Boolean isPriorWeekend;

    @Override
    public Schedule toEntity() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(this, Schedule.class);
    }
}
