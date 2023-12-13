package com.example.yourtrainercareer;

public class Qualification {
    private String qualificationName;
    private String trainerId;

    public Qualification() {
    }

    public Qualification(String qualificationName, String trainerId) {
        this.qualificationName = qualificationName;
        this.trainerId = trainerId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }
}
