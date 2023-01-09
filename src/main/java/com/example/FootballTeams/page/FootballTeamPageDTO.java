package com.example.FootballTeams.page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FootballTeamPageDTO<T> {
    private T data;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
}
