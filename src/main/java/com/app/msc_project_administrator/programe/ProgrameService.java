package com.app.msc_project_administrator.programe;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgrameService {

    private final ProgrameRepository programRepository;

    public List<Programe> getAllPrograms() {
        return programRepository.findAll();
    }

    public Programe getProgramById(Long id) {
        return programRepository.findById(id).orElseThrow(() -> new RuntimeException("Program not found"));
    }

    public Programe createProgram(Programe programe) {
        return programRepository.save(programe);
    }

    public Programe updateProgram(Long id, Programe updatedProgram) {
        Programe program = getProgramById(id);
        program.setTitle(updatedProgram.getTitle());
        program.setActive(updatedProgram.isActive());
        return programRepository.save(program);
    }

    public void deleteProgram(Long id) {
        programRepository.deleteById(id);
    }
}
