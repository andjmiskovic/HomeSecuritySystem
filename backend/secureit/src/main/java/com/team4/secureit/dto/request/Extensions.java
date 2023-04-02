package com.team4.secureit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class Extensions {

    @NotNull
    private List<String> keyUsage;

    private List<String> subjectAlternativeName;

}
