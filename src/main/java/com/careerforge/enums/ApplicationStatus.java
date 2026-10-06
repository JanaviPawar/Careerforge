package com.careerforge.enums;

public enum ApplicationStatus {
    APPLIED,            // Just submitted the application
    OA_PENDING,         // Online Assessment not yet given
    OA_CLEARED,         // Cleared the Online Assessment
    INTERVIEW_SCHEDULED,// Interview date is fixed
    INTERVIEW_DONE,     // Interview completed, waiting for result
    SELECTED,           // Got the offer 🎉
    REJECTED,           // Did not get through
    ON_HOLD
}
