package com.example.quran.repository;

import com.example.quran.model.Ayah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AyahRepository extends JpaRepository<Ayah, Long> {
    List<Ayah> findBySurahOrderByAyahAsc(Integer surahNumber);
}
