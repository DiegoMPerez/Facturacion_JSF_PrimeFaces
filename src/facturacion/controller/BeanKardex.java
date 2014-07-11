package facturacion.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.ManagerKardex;
import facturacion.model.dao.entities.*;

@ManagedBean
@SessionScoped
public class BeanKardex {

	private Integer id;
	private Date fecha;
	private Producto producto;
	private TipoMovimiento tipoMovimiento;
	ManagerDAO managerDAO;
	private List<TipoMovimiento> listaMovimientos;
	private Integer cantidad;
	private Integer codigoProducto;
	ManagerKardex mk;
	private int movimiento;
	private Integer stock;
	private Integer saldo;

	public BeanKardex() {
		// TODO Auto-generated constructor stub
		producto = new Producto();
		managerDAO = new ManagerDAO();
		mk = new ManagerKardex();
		tipoMovimiento = new TipoMovimiento();
		cantidad = 0;
		fecha = new Date(); 
	}

	public List<SelectItem> getListaMovimientos() {
		List<SelectItem> listadoSI = new ArrayList<SelectItem>();
		List<TipoMovimiento> listaMovimientos = managerDAO.findAll(TipoMovimiento.class);

		for (TipoMovimiento tm : listaMovimientos) {
			SelectItem item = new SelectItem(tm.getId(), tm.getMovimiento());
			listadoSI.add(item);
		}
		return listadoSI;
	}

	public void actionNuevoMovimiento() {
		fecha = new Date();
	}

	// Lista Kardex
	public List<Kardex> listaKardex() {
		List<Kardex> lk = mk.findAllKardexByID(producto);
		int i=0;
		for (Kardex kardex : lk) {
			System.out.println(lk.get(i).getTipoMovimientoBean().getMovimiento());
			i++;
		}
		return lk;
	}

	// kardex
	public String cargarKardex(Producto p) {
		producto = p;
		return "kardex";
	}


	// Guardar

	public String actionguardarKardex() {
		Kardex k = new Kardex();
		k.setCantidad(cantidad);
		k.setFecha(fecha);
		k.setProducto(producto);
		k.setStock(producto.getExistencia());
		TipoMovimiento t;
		try {
			t = (TipoMovimiento)managerDAO.findById(TipoMovimiento.class, movimiento);
			k.setTipoMovimientoBean(t);
			mk.insertarKardex(k);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		cantidad = 0;
		fecha = new Date();
		return "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(Integer codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public int getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(int movimiento) {
		this.movimiento = movimiento;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	

}
