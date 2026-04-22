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
@Table(name = "AgroDealer")
@PrimaryKeyJoinColumn(name = "id")
public class AgroDealer extends User {

    @Column(name = "company_name", nullable = false)
    private String companyName;

    private String location;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "full_texts", columnDefinition = "TEXT")
    private String fullTexts;
}
