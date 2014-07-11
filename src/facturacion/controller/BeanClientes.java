package facturacion.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import facturacion.model.dao.ManagerDAO;
import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.Producto;

@ManagedBean
@SessionScoped
public class BeanClientes {

	private String cedula;
	private String nombres;
	private String apellidos;
	private String direccion;
	private List<Cliente> clientes;
	private ManagerDAO managerDAO;

	public BeanClientes() {
		// TODO Auto-generated constructor stub
		managerDAO = new ManagerDAO();
	}

	public String actionInsertarCliente() {
		Cliente c = new Cliente();
		c.setApellidos(apellidos);
		c.setCedulaCliente(cedula);
		c.setDireccion(direccion);
		c.setNombres(nombres);
		try {
			managerDAO.insertar(c);
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

	public String elimiarCliente(Cliente c) {
		try {
			managerDAO.eliminar(Cliente.class, c.getCedulaCliente());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String cargarCliente(Cliente c) {
		cedula = c.getCedulaCliente();
		nombres = c.getNombres();
		apellidos = c.getApellidos();
		direccion = c.getDireccion();
		return "edicionCliente";
	}

	public String actualizarCliente() {
		Cliente c = new Cliente();
		c.setCedulaCliente(cedula);
		c.setApellidos(apellidos);
		c.setDireccion(direccion);
		c.setNombres(nombres);
		try {
			managerDAO.actualizar(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Null();
		return "clientes";
	}

	public void Null() {
		cedula = null;
		nombres = null;
		apellidos = null;
		direccion = null;
	}

	public List<Cliente> getListaClientes() {
		clientes = managerDAO.findAll(Cliente.class,"o.cedulaCliente");
		return clientes;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
}
