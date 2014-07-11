package facturacion.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the kardex database table.
 * 
 */
@Entity
@Table(name="kardex")
@NamedQuery(name="Kardex.findAll", query="SELECT k FROM Kardex k")
public class Kardex implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	private Integer cantidad;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Integer saldo;

	private Integer stock;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="codigo_producto")
	private Producto producto;

	//bi-directional many-to-one association to TipoMovimiento
	@ManyToOne
	@JoinColumn(name="tipo_movimiento")
	private TipoMovimiento tipoMovimientoBean;

	public Kardex() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getSaldo() {
		return this.saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoMovimiento getTipoMovimientoBean() {
		return this.tipoMovimientoBean;
	}

	public void setTipoMovimientoBean(TipoMovimiento tipoMovimientoBean) {
		this.tipoMovimientoBean = tipoMovimientoBean;
	}

}