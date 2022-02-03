package com.cuneyt.kazaveoructakibim.entities.concretes;

import com.cuneyt.kazaveoructakibim.entities.abstracts.VakitModelService;

public class YatsiModel implements VakitModelService {

    @Override
    public String modelId(String id) {
        return id;
    }

    @Override
    public String modelDate(String date) {
        return date;
    }

    @Override
    public String modelVakit(String vakit) {
        return vakit;
    }
}
