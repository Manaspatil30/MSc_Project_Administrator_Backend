package com.app.msc_project_administrator.programe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programs")
@RequiredArgsConstructor
public class ProgrameController {

    private final ProgrameService programService;

    @GetMapping
    public ResponseEntity<List<Programe>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Programe> getProgramById(@PathVariable Long id) {
        return ResponseEntity.ok(programService.getProgramById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Programe> createProgram(@RequestBody Programe program) {
        return ResponseEntity.ok(programService.createProgram(program));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Programe> updateProgram(@PathVariable Long id, @RequestBody Programe updatedProgram) {
        return ResponseEntity.ok(programService.updateProgram(id, updatedProgram));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
