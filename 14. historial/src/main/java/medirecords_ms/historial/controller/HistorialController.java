package medirecords_ms.historial.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import medirecords_ms.historial.dto.HistorialRequestDTO;
import medirecords_ms.historial.dto.HistorialResponseDTO;
import medirecords_ms.historial.service.HistorialService;

@RestController
@RequestMapping("/api/historiales")
public class HistorialController {
    @Autowired
    private HistorialService historialService;

    @GetMapping
    public ResponseEntity<List<HistorialResponseDTO>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(historialService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(historialService.buscarDetalle(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<HistorialResponseDTO>> buscarVariosId(@RequestParam("ids") String ids) {
        String[] partes = ids.split(",");
        List<Long> listaIds = new ArrayList<>();
        for (String parte : partes) {
            listaIds.add(Long.parseLong(parte));
        }
        return ResponseEntity.status(HttpStatus.OK).body(historialService.buscarVariosId(listaIds));
    }

    @PostMapping
    public ResponseEntity<HistorialResponseDTO> crearHistorial(@RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historialService.crearHistorial(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialResponseDTO> actualizarHistorial(@PathVariable Long id, @RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(historialService.actualizarHistorial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarHistorial(@PathVariable Long id) {
        historialService.borrarHistorial(id);
        return ResponseEntity.status(HttpStatus.OK).body("Borrado: " + id);
    }
}
