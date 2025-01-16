package aluracursos.forohub.controller;

import aluracursos.forohub.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public  ResponseEntity<DatosRespuestaTopico> registrarTopicos(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico, UriComponentsBuilder uriComponentsBuilder) {
       Topico topico = topicoRepository.save(new Topico(datosRegistroTopico));
       DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(),
               topico.getMensaje(), topico.getFechaCreacion(),
               topico.getStatus(), topico.getAutor(), topico.getCurso());
       URI url = uriComponentsBuilder.path("/topicos/{id]").buildAndExpand(topico.getId()).toUri();
       return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping
    public ResponseEntity <Page<DatosListadoTopico>> listadoTopicos(@PageableDefault(size = 10) Pageable paginacion){
        Pageable paginacionOrdenada = PageRequest.of(
                paginacion.getPageNumber(),
                paginacion.getPageSize(),
                Sort.by("fechaCreacion").ascending()
        );
        return ResponseEntity.ok(topicoRepository.findAll(paginacionOrdenada).map(DatosListadoTopico::new));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        // Verificar si el tópico existe en la base de datos
        Optional<Topico> optionalTopico = topicoRepository.findById(id);

        if (optionalTopico.isPresent()) {
            // Si el tópico existe, se procede con la actualización
            Topico topico = optionalTopico.get();
            topico.actualizarDatos(datosActualizarTopico);

            // Retornar la respuesta con los datos actualizados
            var datosTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(),
                    topico.getStatus(), topico.getAutor(), topico.getCurso());
            return ResponseEntity.ok(datosTopico);
        } else {
            // Si el tópico no existe, retornar un error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tópico con el ID especificado no existe.");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topicoRepository.delete(topico);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornaDatosTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(),
                topico.getStatus(), topico.getAutor(), topico.getCurso());
        return ResponseEntity.ok(datosTopico);
    }
}
