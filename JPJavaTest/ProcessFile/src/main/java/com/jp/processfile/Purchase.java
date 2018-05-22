/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jp.processfile;

/**
 *
 * @author douglas rossi
 */
public class Purchase {

    private String fileType;
    private String productType;
    private Integer value;
    private Integer occurrenses;

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }
    
    /**
     * @return the occurrenses
     */
    public Integer getOccurrenses() {
        return occurrenses;
    }

    /**
     * @param occurrenses the occurrenses to set
     */
    public void setOccurrenses(Integer occurrenses) {
        this.occurrenses = occurrenses;
    }
    
    @Override
    public String toString() {
        return "Purchase:: ProductType=" + this.productType + " Value=" + this.value;
    }
}
