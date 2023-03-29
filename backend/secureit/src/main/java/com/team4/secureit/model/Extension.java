package com.team4.secureit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Data
@AllArgsConstructor
public class Extension {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;
}
