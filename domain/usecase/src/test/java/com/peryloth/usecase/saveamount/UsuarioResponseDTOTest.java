package com.peryloth.usecase.saveamount;

import com.peryloth.usecase.saveamount.dto.UsuarioResponseDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioResponseDTOTest {

    @Test
    void shouldCreateUsuarioResponseDTOWithCorrectValues() {
        // Arrange
        String email = "test@example.com";
        String nombre = "Juan Pérez";
        Long salarioBase = 5000L;

        // Act
        UsuarioResponseDTO dto = new UsuarioResponseDTO(email, nombre, salarioBase);

        // Assert
        assertThat(dto.email()).isEqualTo(email);
        assertThat(dto.nombre()).isEqualTo(nombre);
        assertThat(dto.salario_base()).isEqualTo(salarioBase);
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        UsuarioResponseDTO dto1 = new UsuarioResponseDTO("a@a.com", "Alice", 1000L);
        UsuarioResponseDTO dto2 = new UsuarioResponseDTO("a@a.com", "Alice", 1000L);
        UsuarioResponseDTO dto3 = new UsuarioResponseDTO("b@b.com", "Bob", 2000L);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldHaveMeaningfulToString() {
        UsuarioResponseDTO dto = new UsuarioResponseDTO("c@c.com", "Carlos", 3000L);

        String toString = dto.toString();

        assertThat(toString).contains("c@c.com");
        assertThat(toString).contains("Carlos");
        assertThat(toString).contains("3000");
    }
}
