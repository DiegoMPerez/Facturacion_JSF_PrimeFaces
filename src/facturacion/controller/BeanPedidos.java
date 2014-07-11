package facturacion.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.FacturaCab;
import facturacion.model.dao.entities.PedidoCab;
import facturacion.model.dao.entities.PedidoDet;
import facturacion.model.dao.entities.Producto;
import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.ManagerFacturacion;
import facturacion.model.dao.ManagerPedidos;
import facturacion.model.dao.entities.Cliente;

@ManagedBean
@SessionScoped
public class BeanPedidos implements Serializable {
	private String cedula;
	private String nombres;
	private String apellidos;
	private String direcion;
	private ManagerFacturacion managerFacturacion;
	private ManagerDAO managerDao;
	private String clave;
	private String clave2;
	private List<Producto> listaProductos;
	private ManagerPedidos managerPedidos;
	private PedidoCab pedidoCabTmp;
	private PedidoCab pedidoCabSeleccionado;
	private String observacion;

	public BeanPedidos() {
		// TODO Auto-generated constructor stub
		managerFacturacion = new ManagerFacturacion();
		managerDao = new ManagerDAO();
		listaProductos = managerFacturacion.findAllProductos();
		managerPedidos = new ManagerPedidos();
	}

	public String actionComprobarCedula() {

		try {
			Cliente c = managerFacturacion.findClienteById(cedula);
			nombres = c.getNombres();
			apellidos = c.getApellidos();
			direcion = c.getDireccion();
			cedula = c.getCedulaCliente();
			clave = c.getClave();
			// creamos el pedido temporal y asignamos el cliente
			// automaticamente:
			pedidoCabTmp = managerPedidos.crearPedidoTmp();
			managerPedidos.asignarClientePedidoTmp(pedidoCabTmp, cedula);
			return "pedidos";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// enviar a la página de registro
			return "registro";
		}
	}

	public void actionInsertarProducto(Producto p) {
		try {
			if (pedidoCabTmp == null)
				pedidoCabTmp = managerPedidos.crearPedidoTmp();
			// agregamos un nuevo producto al carrito de compras:
			managerPedidos.agregarDetallePedidoTmp(pedidoCabTmp,
					p.getCodigoProducto(), 1);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}

	public String registrar() {
		Cliente c = new Cliente();
		c.setApellidos(apellidos);
		c.setCedulaCliente(cedula);
		c.setDireccion(direcion);
		c.setNombres(nombres);
		c.setClave(clave);
		try {
			managerPedidos.registrarCliente(c, clave2);
			return "pedidos";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}

	// Comprobar actualizaciones

	public void actualizarListaObjetos() {
		listaProductos = managerFacturacion.findAllProductos();
	}

	// BeanPedido null

	public String pedidoNull() {
		cedula = null;
		nombres = null;
		apellidos = null;
		direcion = null;
		clave = null;
		clave2 = null;
		return "INICIO";
	}

	// lista
	public List<Producto> listaProductos() {
		actualizarListaObjetos();
		return listaProductos;
	}

	// GUARDAR EL PEDIDO

	public String guardarPedido() {
		try {
			pedidoCabTmp.setObservacion(observacion);
			managerPedidos.guardarPedidoTemporal(pedidoCabTmp);
			JSFUtil.crearMensajeINFO("PEDIDO GUARDADO");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "pedido_imprimir";
	}

	// eliminar productos del pedido
	public void actionEliminarProductosPedido(PedidoDet pd, String token) {
		pedidoCabTmp = managerPedidos.eliminarProductoPedido(pedidoCabTmp, pd,
				token);
	}

	// calcular el total de productos
	public int actionCalcularTotalProductos() {
		return managerPedidos.calcularTotalProductos(pedidoCabTmp);
	}

	// calcular el total de productos
	public int actionCalcularTotalProductosCAB(PedidoCab pc) {
		return managerPedidos.calcularTotalProductos(pc);
	}

	// calcularsubtotal
	public double actionSubtotal(PedidoCab pc) {
		return managerPedidos.calcularPedidos(pc);
	}

	// Verificar si hay productos en carrito
	public String actionExistenciaProductosCarrito() throws Exception {
		if (managerPedidos.existenciaProductosCarrito(pedidoCabTmp)) {
			return "confirmacion";
		} else {
			JSFUtil.crearMensajeWARN("NO SE HAN SELECCIONADO PRODUCTOS");
		}
		return "";
	}

	// lista de pedidos de un cliente

	public List<PedidoCab> actionListapedidosCab() {
		return managerPedidos.findAllPedidosCAByID(cedula);
	}

	// lista de detalles Pedidos

	public List<PedidoDet> actionListapedidosDet() {
		if (pedidoCabSeleccionado == null)
			pedidoCabSeleccionado = new PedidoCab();
		return managerPedidos.findAllPedidosDetByID(pedidoCabSeleccionado);
	}

	// Cliente
	public String cargarCliente() {
		return "pedidoshistorial";
	}

	public String actionCerrarPedido() {
		pedidoCabTmp = null;
		// creamos el pedido temporal y asignamos el cliente automaticamente:
		pedidoCabTmp = managerPedidos.crearPedidoTmp();
		try {
			managerPedidos.asignarClientePedidoTmp(pedidoCabTmp, cedula);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "pedidos";
	}

	public void asignarPedidoseleccionado(PedidoCab pc) {
		if (pedidoCabSeleccionado == null)
			pedidoCabSeleccionado = new PedidoCab();
		System.out.println(pc.getNumeroPedido());
		pedidoCabSeleccionado = pc;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDirecion() {
		return direcion;
	}

	public void setDirecion(String direcion) {
		this.direcion = direcion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClave2() {
		return clave2;
	}

	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}

	public PedidoCab getPedidoCabTmp() {
		return pedidoCabTmp;
	}

	public void setPedidoCabTmp(PedidoCab pedidoCabTmp) {
		this.pedidoCabTmp = pedidoCabTmp;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public PedidoCab getPedidoCabSeleccionado() {
		return pedidoCabSeleccionado;
	}

	public void setPedidoCabSeleccionado(PedidoCab pedidoCabSeleccionado) {
		this.pedidoCabSeleccionado = pedidoCabSeleccionado;
	}

}
