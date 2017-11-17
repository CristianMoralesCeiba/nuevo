package co.ceiba.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import co.ceiba.domain.enumeration.TipoVehiculo;

/**
 * A DTO for the Vehiculo entity.
 */
public class VehiculoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1500057442285367359L;

	public VehiculoDTO() {
		super();
	}
	
    public VehiculoDTO(Long id, String placa, TipoVehiculo tipo, Integer cilindraje, Instant fechaIngreso) {
		super();
		this.id = id;
		this.placa = placa;
		this.tipo = tipo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = fechaIngreso;
	}

	private Long id;

    @NotNull
    private String placa;

    @NotNull
    private TipoVehiculo tipo;

    private Integer cilindraje;

    @NotNull
    private Instant fechaIngreso;

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

    public Instant getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Instant fechaIngreso) {
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
