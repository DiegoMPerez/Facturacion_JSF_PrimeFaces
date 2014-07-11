package facturacion.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;

import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.Parametro;
import facturacion.model.dao.entities.Producto;

@ManagedBean
@SessionScoped
public class BeanParametros {

	private String nombreParametro;
	private String valorParametro;	
	ManagerDAO managerDAO;
	private List<Parametro> parametros;
	
	public BeanParametros() {
		// TODO Auto-generated constructor stub
		managerDAO = new ManagerDAO();
	}
	
	public String actionInsertarParametro(){
		Parametro p = new Parametro();
		p.setNombreParametro(nombreParametro);
		p.setValorParametro(valorParametro);
		try {
			managerDAO.insertar(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,e.getMessage(),null));
			e.printStackTrace();
		}
		return "";
	}
	
	public String elimiarParametro(Parametro p){
		try {
			managerDAO.eliminar(p.getClass(),p.getNombreParametro());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String cargarParametro(Parametro p){
		valorParametro = p.getValorParametro();
		nombreParametro = p.getNombreParametro();
		return "edicionParametro";
	}
	
	public String actualizarParametro(){
		Parametro p = new Parametro();
		p.setNombreParametro(nombreParametro);
		p.setValorParametro(valorParametro);
		try {
			managerDAO.actualizar(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "parametros";
	}
	
	public List<Parametro> getListaParametros() {
		parametros = managerDAO.findAll(Parametro.class);
		return parametros;
	}

	public String getNombreParametro() {
		return nombreParametro;
	}

	public void setNombreParametro(String nombreParametro) {
		this.nombreParametro = nombreParametro;
	}

	public String getValorParametro() {
		return valorParametro;
	}

	public void setValorParametro(String valorParametro) {
		this.valorParametro = valorParametro;
	}


	
	
}
