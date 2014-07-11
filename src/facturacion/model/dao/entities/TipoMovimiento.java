package facturacion.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_movimiento database table.
 * 
 */
@Entity
@Table(name="tipo_movimiento")
@NamedQuery(name="TipoMovimiento.findAll", query="SELECT t FROM TipoMovimiento t")
public class TipoMovimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=7)
	private String movimiento;

	//bi-directional many-to-one association to Kardex
	@OneToMany(mappedBy="tipoMovimientoBean")
	private List<Kardex> kardexs;

	public TipoMovimiento() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMovimiento() {
		return this.movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	public List<Kardex> getKardexs() {
		return this.kardexs;
	}

	public void setKardexs(List<Kardex> kardexs) {
		this.kardexs = kardexs;
	}

	public Kardex addKardex(Kardex kardex) {
		getKardexs().add(kardex);
		kardex.setTipoMovimientoBean(this);

		return kardex;
	}

	public Kardex removeKardex(Kardex kardex) {
		getKardexs().remove(kardex);
		kardex.setTipoMovimientoBean(null);

		return kardex;
	}

}