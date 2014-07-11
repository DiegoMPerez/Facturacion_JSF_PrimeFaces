package facturacion.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.ManagerFacturacion;
import facturacion.model.dao.entities.*;

@ManagedBean
@SessionScoped
public class BeanProductos {

	private List<Producto> listaPrductos;
	private ManagerDAO managerDAO;

	private Integer codigoProducto;
	private String descripcion;
	private Integer existencia;
	private String nombre;
	private BigDecimal precioUnitario;
	private String tieneImpuesto;
	private List<FacturaDet> facturaDets;
	private List<Kardex> kardexs;
	private ManagerFacturacion managerFacturacion;

	public BeanProductos() {
		// TODO Auto-generated constructor stub
		managerDAO = new ManagerDAO();
		managerFacturacion = new ManagerFacturacion();
	}

	public String actionInsertarProducto() {
		Producto p = new Producto();
		p.setCodigoProducto(codigoProducto);
		p.setNombre(nombre);
		p.setDescripcion(descripcion);
		p.setExistencia(existencia);
		p.setPrecioUnitario(precioUnitario);
		p.setTieneImpuesto(tieneImpuesto);
		try {
			managerDAO.insertar(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e
							.getMessage(), null));
			e.printStackTrace();
		}
		return "";
	}

	public String elimiarProducto(Producto p) {
		try {
			managerDAO.eliminar(Producto.class, p.getCodigoProducto());
		} catch (Exception e) {
			// TODO Auto-generated catch block hacer un faces messaje
			e.printStackTrace();
		}
		nullProducto();
		return "";
	}

	public void cargarProducto(Producto p) {
		codigoProducto = p.getCodigoProducto();
		nombre = p.getNombre();
		descripcion = p.getDescripcion();
		existencia = p.getExistencia();
		precioUnitario = p.getPrecioUnitario();
		tieneImpuesto = p.getTieneImpuesto();
	}

	public String edicionProducto(Producto p) {
		cargarProducto(p);
		return "edicionProducto";
	}

	public String actualizarProducto() {
		Producto p = new Producto();
		p.setCodigoProducto(codigoProducto);
		p.setNombre(nombre);
		p.setDescripcion(descripcion);
		try {
			managerDAO.actualizar(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nullProducto();

		return "productos";
	}

	public void nullProducto() {
		codigoProducto = null;
		nombre = null;
		descripcion = null;
		precioUnitario = null;
		existencia = null;
		tieneImpuesto = null;
	}

	public List<Producto> getListaProductos() {
		listaPrductos = managerDAO.findAll(Producto.class);
		return listaPrductos;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Producto> getListaPrductos() {
		return listaPrductos;
	}

	public void setListaPrductos(List<Producto> listaPrductos) {
		this.listaPrductos = listaPrductos;
	}

	public ManagerDAO getManagerDAO() {
		return managerDAO;
	}

	public void setManagerDAO(ManagerDAO managerDAO) {
		this.managerDAO = managerDAO;
	}

	public Integer getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(Integer codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public Integer getExistencia() {
		return existencia;
	}

	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getTieneImpuesto() {
		return tieneImpuesto;
	}

	public void setTieneImpuesto(String tieneImpuesto) {
		this.tieneImpuesto = tieneImpuesto;
	}

	public List<Kardex> getKardexs() {
		return kardexs;
	}

	public void setKardexs(List<Kardex> kardexs) {
		this.kardexs = kardexs;
	}

}
