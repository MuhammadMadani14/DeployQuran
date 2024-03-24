package com.example.quran.services;

import com.example.quran.data.DoaData;
import com.example.quran.model.Doa;
import com.example.quran.repository.DoaRepository;
import com.example.quran.response.DoaResponse;
import com.example.quran.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DoaService {
    @Autowired
    DoaRepository doaRepository;

    public DoaResponse getListDoa(){
        DoaResponse doaResponse = new DoaResponse();

        MessageResponse messageResponse = new MessageResponse(false, "Success");
        doaResponse.setMessageResponse(messageResponse);

        List<Doa> doaList = doaRepository.findAll();

        List<DoaData> dataList = new ArrayList<>();
        for (Doa doa : doaList) {
            DoaData doaData = new DoaData();
            doaData.setId(doa.getId());
            doaData.setDoaName(doa.getTypeDoa());
            dataList.add(doaData);
        }
        doaResponse.setData(dataList);

        return doaResponse;
    }

    public DoaResponse getDoaByTitle(String title){
        DoaResponse doaResponse = new DoaResponse();
        MessageResponse messageResponse = new MessageResponse(false, "Success");
        doaResponse.setMessageResponse(messageResponse);
        List<Doa> doaList = doaRepository.findByTypeDoaContainingIgnoreCase(title);
        List<DoaData> dataList = new ArrayList<>();
        for(Doa doa : doaList){
            DoaData doaData = new DoaData();
            doaData.setId(doa.getId());
            doaData.setDoaName(doa.getTypeDoa());
            dataList.add(doaData);
        }
        doaResponse.setData(dataList);
        return doaResponse;
    }

//    public DoaResponse getDoaDetail(Long title){
//        DoaResponse doaResponses = new DoaResponse();
//        MessageResponse messageResponse = new MessageResponse(false, "Success");
//        doaResponses.setMessageResponse(messageResponse);
//        List<DoaData> dataList = new ArrayList<>();
//        for(Doa doa : doaList){
//            DoaData doaData = new DoaData();
//            doaData.setId(doa.getId());
//            doaData.setDoaName(doa.getTypeDoa());
//            doaData.setArabDoa(doa.getArabDoa());
//            doaData.setTranslateDoa(doa.getTranslateDoa());
//            dataList.add(doaData);
//
//        doaResponses.setData(dataList);
//        return doaResponses;
//    }
}
