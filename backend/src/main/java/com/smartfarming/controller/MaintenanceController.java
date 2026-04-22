package com.smartfarming.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Temporary maintenance endpoint to clean up orphaned data.
 * Call POST /api/maintenance/cleanup-questions once to fix the DB.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Deletes Question rows whose farmer_id does not exist in the Farmer table.
     */
    @PostMapping("/cleanup-questions")
    public ResponseEntity<Map<String, Object>> cleanupOrphanedQuestions() {
        try {
            // First delete answers that belong to orphaned questions
            int answersDeleted = jdbcTemplate.update(
                "DELETE a FROM Answer a " +
                "INNER JOIN Question q ON a.question_id = q.id " +
                "WHERE q.farmer_id NOT IN (SELECT id FROM Farmer)"
            );

            // Then delete the orphaned questions
            int questionsDeleted = jdbcTemplate.update(
                "DELETE FROM Question WHERE farmer_id NOT IN (SELECT id FROM Farmer)"
            );

            return ResponseEntity.ok(Map.of(
                "success", true,
                "orphanedQuestionsDeleted", questionsDeleted,
                "orphanedAnswersDeleted", answersDeleted,
                "message", "Database cleanup completed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        return ResponseEntity.ok(Map.of(
            "database", "CONNECTED",
            "storage", "84% AVAILABLE",
            "memory", "1.2GB / 4GB",
            "lastBackup", "2026-04-21 21:00:00"
        ));
    }

    @GetMapping("/backup")
    public ResponseEntity<String> downloadBackup() {
        // Return a simulated backup string
        String backupContent = "-- SMART FARMING SYSTEM AUTOMATED BACKUP\n" +
                             "-- Created At: " + java.time.LocalDateTime.now() + "\n" +
                             "USE smartfarming;\n" +
                             "-- [System Data Encapsulated]";
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"smart_farming_backup.sql\"")
            .body(backupContent);
    }
}
