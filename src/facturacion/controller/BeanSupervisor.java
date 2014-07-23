package facturacion.controller;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import facturacion.model.dao.ManagerPedidos;
import facturacion.model.dao.entities.PedidoCab;

@ManagedBean
@SessionScoped
public class BeanSupervisor {
	private Date fechaInicio;
	private Date fechaFinal;
	private ManagerPedidos managerPedidos;
	private PedidoCab pedidoCabTmp;
	private String causa;

	public BeanSupervisor() {
		managerPedidos = new ManagerPedidos();
	}

	public String actionCargarPedido(PedidoCab pedidoCab) {
		try {
			// capturamos el valor enviado desde el DataTable:
			pedidoCabTmp = pedidoCab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void actionAnularPedido(PedidoCab pc) {
		try {
			managerPedidos.anularPedido(pc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JSFUtil.crearMensajeWARN(e.getMessage());
		}
	}

	public String actionBuscar() {
		// unicamente se invoca esta accion para actualizar
		// los parametros de fechas.
		return "";
	}

	public List<PedidoCab> getListaPedidoCab() {
		try {
			return managerPedidos
					.findPedidoCabByFechas(fechaInicio, fechaFinal);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String actionDespacharPedido(PedidoCab pedidoCab) {
		try {
			// invocamos a ManagerFacturacion para crear una nueva factura:
			managerPedidos.despacharPedido(pedidoCab.getNumeroPedido());
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public PedidoCab getPedidoCabTmp() {
		return pedidoCabTmp;
	}

	public void setPedidoCabTmp(PedidoCab pedidoCabTmp) {
		this.pedidoCabTmp = pedidoCabTmp;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

}