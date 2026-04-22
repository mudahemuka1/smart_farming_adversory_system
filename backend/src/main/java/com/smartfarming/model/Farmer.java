package com.smartfarming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Farmer")
@PrimaryKeyJoinColumn(name = "id")
public class Farmer extends User {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String location;

    @Column(name = "contact_number")
    private String contactNumber;
}
