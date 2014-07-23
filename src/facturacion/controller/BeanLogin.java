package facturacion.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import facturacion.model.dao.ManagerLogin;
import facturacion.model.dao.entities.Usuario;

@ManagedBean
@SessionScoped
public class BeanLogin {
	private String codigoUsuario;
	private String clave;
	private String tipoUsuario;
	private boolean acceso;
	private ManagerLogin managerLogin;
	private String nombresUsuario;
	private String clave2;
	private List<Usuario> listaUsuarios;

	public BeanLogin() {
		managerLogin = new ManagerLogin();
	}

	/**
	 * Action que permite el acceso al sistema.
	 * 
	 * @return
	 */
	public String accederSistema() {
		acceso = false;
		try {
			// verificamos el acceso del usuario:
			tipoUsuario = managerLogin.accederSistema(codigoUsuario, clave);
			// por seguridad borramos la clave en la sesion:
			clave = "";
			// dependiendo del tipo de usuario, direccionamos:
			if (tipoUsuario.equals("SP")) {
				acceso = true;
				return "supervisor/index";
			}
			if (tipoUsuario.equals("VD")) {
				acceso = true;
				return "Vendedor/facturacion";
			} // caso contrario, ocurrio un error con el tipo de usuario:
			JSFUtil.crearMensajeERROR("Error al acceder (tipo de usuario).");
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}

	public void actionInsertarUsuario() {
		Usuario u = new Usuario();
		u.setClave(clave);
		u.setCodigoUsuario(codigoUsuario);
		u.setNombresUsuario(nombresUsuario);
		u.setTipoUsuario(tipoUsuario);
		managerLogin.insertarUsuario(u, clave2);
		setNull();
	}

	public void setNull() {
		clave = null;
		codigoUsuario = null;
		nombresUsuario = null;
		clave = null;
		clave2 = null;
		tipoUsuario = null;
	}

	public List<Usuario> listaUsuarios() {
		return managerLogin.listaUsuarios();
	}

	public void actionResetPasw(String codUsuario) {
		managerLogin.resetPasswd(codUsuario);
	}

	public void actionEliminarUsuario(Usuario usuario) {
		managerLogin.eliminarUsuario(usuario);
	}

	/**
	 * Finaliza la sesion web del usuario.
	 * 
	 * @return
	 */
	public String salirSistema() {
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();
		return "/index2.xhtml?faces-redirect=true";
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public boolean isAcceso() {
		return acceso;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getNombresUsuario() {
		return nombresUsuario;
	}

	public void setNombresUsuario(String nombresUsuario) {
		this.nombresUsuario = nombresUsuario;
	}

	public String getClave2() {
		return clave2;
	}

	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

}