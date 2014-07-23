package facturacion.model.dao.entities;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the pedido_cab database table.
 * 
 */
@Entity
@Table(name="pedido_cab")
@NamedQuery(name="PedidoCab.findAll", query="SELECT p FROM PedidoCab p")
public class PedidoCab implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator="seq_pedido_cab")
	@SequenceGenerator(name="seq_pedido_cab",sequenceName="seq_pedido_cab", allocationSize=1)
	@Id
	@Column(name="numero_pedido", unique=true, nullable=false)
	private Integer numeroPedido;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_pedido", nullable=false)
	private Date fechaPedido;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_despacho", nullable=false)
	private Date fechaDespacho;

	@Column(length=100)
	private String observacion;

	@Column(nullable=false, precision=12, scale=2)
	private BigDecimal subtotal;
	
	@Column(length=100)
	private String causa;

	//bi-directional many-to-one association to EstadoPedido
	@ManyToOne
	@JoinColumn(name="id_estado_pedido", nullable=false)
	private EstadoPedido estadoPedido;

	//bi-directional many-to-one association to PedidoDet
	@OneToMany(mappedBy="pedidoCab", cascade=CascadeType.ALL)
	private List<PedidoDet> pedidoDets;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="cedula_cliente", nullable=false)
	private Cliente cliente;

	public PedidoCab() {
	}

	public Integer getNumeroPedido() {
		return this.numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Date getFechaPedido() {
		return this.fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public EstadoPedido getEstadoPedido() {
		return this.estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public List<PedidoDet> getPedidoDets() {
		return this.pedidoDets;
	}

	public void setPedidoDets(List<PedidoDet> pedidoDets) {
		this.pedidoDets = pedidoDets;
	}

	public PedidoDet addPedidoDet(PedidoDet pedidoDet) {
		getPedidoDets().add(pedidoDet);
		pedidoDet.setPedidoCab(this);

		return pedidoDet;
	}

	public PedidoDet removePedidoDet(PedidoDet pedidoDet) {
		getPedidoDets().remove(pedidoDet);
		pedidoDet.setPedidoCab(null);

		return pedidoDet;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getFechaDespacho() {
		return fechaDespacho;
	}

	public void setFechaDespacho(Date fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}
	
	

}