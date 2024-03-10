package com.example.quran.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ayah")
public class Ayah {
    private String arab;
    private Integer asbab;
    private String audio;
    private Integer ayah;
    private Integer hizb;
    @Id
    private Long id;
    private Integer juz;
    private String latin;
    private String notes;
    private Integer page;
    private Integer surah;
    private String text;
    private Integer theme;
}
