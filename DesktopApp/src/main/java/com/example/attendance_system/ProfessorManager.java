package com.example.attendance_system;

public class ProfessorManager {
    private static ProfessorManager instance;
    private int professorId;

    // Private constructor to prevent instantiation outside this class
    private ProfessorManager() {
        // Initialize professorId to a default value, like 0 or -1
        professorId = -1;
    }

    // Method to get the singleton instance of ProfessorManager
    public static synchronized ProfessorManager getInstance() {
        if (instance == null) {
            instance = new ProfessorManager();
        }
        return instance;
    }

    // Method to set the professorId
    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    // Method to get the professorId
    public int getProfessorId() {
        return professorId;
    }
}

