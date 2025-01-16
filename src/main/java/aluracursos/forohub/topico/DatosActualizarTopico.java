package aluracursos.forohub.topico;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(
        @NotNull
        String titulo,
        String mensaje,
        String curso
) {
}
