package com.goofy.tunabank.v1.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Country {

    @Id
    @Column(name = "country_id")
    private int countryId;

    private String countryCode;

    private String countryName;
}
