package com.foodfinder.diagnostic.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "diagnostic")
public class Diagnostic implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "message", length = 1000)
    private String message;

    @Column(name = "exception", length = 1000)
    private String exception;

    @Column(name = "comments", length = 1000)
    private String comments;

    @Column(name = "date")
    private Date date;
}