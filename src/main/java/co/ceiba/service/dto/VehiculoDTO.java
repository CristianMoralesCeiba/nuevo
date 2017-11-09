package co.ceiba.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import co.ceiba.domain.enumeration.TipoVehiculo;

/**
 * A DTO for the Vehiculo entity.
 */
public class VehiculoDTO implements Serializable {

    private Long id;

    @NotNull
    private String placa;

    @NotNull
    private TipoVehiculo tipo;

    private Integer cilindraje;

    @NotNull
    private LocalDate fechaIngreso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public Integer getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(Integer cilindraje) {
        this.cilindraje = cilindraje;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VehiculoDTO vehiculoDTO = (VehiculoDTO) o;
        if(vehiculoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehiculoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehiculoDTO{" +
            "id=" + getId() +
            ", placa='" + getPlaca() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", cilindraje='" + getCilindraje() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            "}";
    }
}
