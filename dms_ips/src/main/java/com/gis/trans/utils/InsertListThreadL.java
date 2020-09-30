package com.gis.trans.utils;

import com.gis.trans.model.ShapePoint;

import java.util.List;

public abstract class InsertListThreadL extends Thread {

    private List<ShapePoint> list;
    private int start;
    private int end;
    private Double strain;


    public InsertListThreadL(List<ShapePoint> list) {
        this.list = list;
    }

    public  InsertListThreadL() {

    }

    public Double getStrain() {
        return strain;
    }

    public void setStrain(Double strain) {
        this.strain = strain;
    }

    @Override
    public void run() {
        try {
           // plotInfSevice.insertList(plotInfs);
           method(list);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public abstract void method(List<ShapePoint> plotInfs);

}
