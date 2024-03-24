package com.example.quran.controller;

import com.example.quran.model.Ayah;
import com.example.quran.model.Doa;
import com.example.quran.response.DoaResponse;
import com.example.quran.response.MessageResponse;
import com.example.quran.services.DoaService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DoaController {
    @Autowired
    DoaService doaService;

    @GetMapping
    public ResponseEntity<DoaResponse> getListDoa(){
        return ResponseEntity.ok(doaService.getListDoa());
    }

    @GetMapping("/doa")
    public ResponseEntity<DoaResponse> getSearchDoa(@RequestParam String title){
        return ResponseEntity.ok(doaService.getDoaByTitle(title));
    }

//    @GetMapping("/doadetail")
//    public ResponseEntity<DoaResponse> getDetailDoa(@RequestParam Long id){
//        return ResponseEntity.ok(doaService.getDoaDetail(id));
//    }
}
