package com.kodilla.cheapflightsearch.domain.watcher;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "database_manipulations")
public class DatabaseManipulation {
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private Long databaseManipulationId;
    @NotNull
    @Column(name = "begin_operation_timestamp")
    private LocalDateTime beginTimestamp;
    @NotNull
    @Column(name = "end_operation_timestamp")
    private LocalDateTime endTimestamp;
    @NotNull
    @Column(name = "class_descriptor")
    private String classDescriptor;
    @NotNull
    @Column(name = "method")
    private String method;
}
