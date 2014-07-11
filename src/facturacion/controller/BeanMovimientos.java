package facturacion.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.Kardex;
import facturacion.model.dao.entities.Parametro;
import facturacion.model.dao.entities.Producto;
import facturacion.model.dao.entities.TipoMovimiento;

@ManagedBean
@SessionScoped
public class BeanMovimientos {

	ManagerDAO managerDAO;
	private Integer id;
	private String movimiento;
	private List<TipoMovimiento> listaMovimientos;
	
	public BeanMovimientos() {
		// TODO Auto-generated constructor stub
		managerDAO = new ManagerDAO();
	}
	
	public String actionInsertarMovimiento(){
		TipoMovimiento tm = new TipoMovimiento();
		tm.setId(id);
		tm.setMovimiento(movimiento);
		try {
			managerDAO.insertar(tm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,e.getMessage(),null));
			e.printStackTrace();
		}
		return "";
	}
	
	public String elimiarTipoMovimiento(TipoMovimiento tm){
		try {
			managerDAO.eliminar(tm.getClass(),tm.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String cargarMovimiento(TipoMovimiento tm){
		id = tm.getId();
		movimiento = tm.getMovimiento();
		return "edicionMovimiento";
	}
	
	public String actualizarMovimiento(){
		TipoMovimiento tm = new TipoMovimiento();
		tm.setId(id);
		tm.setMovimiento(movimiento);
		try {
			managerDAO.actualizar(tm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "tipoMovimientos";
	}
	
	public List<TipoMovimiento> getListatipoMovimientos() {
		listaMovimientos = managerDAO.findAll(TipoMovimiento.class);
		return listaMovimientos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}


	
	
	
}
