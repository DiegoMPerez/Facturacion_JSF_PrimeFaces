package facturacion.model.dao;

import java.util.List;

import org.eclipse.persistence.sdo.helper.ListWrapper;

import facturacion.model.dao.entities.Usuario;

public class ManagerLogin {
	private ManagerDAO managerDAO;

	public ManagerLogin() {
		managerDAO = new ManagerDAO();
	}

	/**
	 * Metodo que le permite a un usuario acceder al sistema.
	 * 
	 * @param codigoUsuario
	 *            Identificador del usuario.
	 * @param clave
	 *            Clave de acceso.
	 * @return Retorna el tipo de usuario. Puede tener dos valores: SP
	 *         (supervisor) o VD (vendedor).
	 * @throws Exception
	 *             Cuando no coincide la clave proporcionada o si ocurrio un
	 *             error con la consulta a la base de datos.
	 */
	public String accederSistema(String codigoUsuario, String clave)
			throws Exception {
		Usuario usuario = (Usuario) managerDAO.findById(Usuario.class,
				codigoUsuario);
		if (usuario == null)
			throw new Exception("Usuario no existe.");
		if (!usuario.getClave().equals(clave))
			throw new Exception("No coincide la clave.");
		return usuario.getTipoUsuario();
	}

	public List<Usuario> listaUsuarios() {
		return managerDAO.findAll(Usuario.class);
	}

	public void insertarUsuario(Usuario usuario, String clave2) {
		if (usuario.getClave().equals(clave2)) {
			try {
				managerDAO.insertar(usuario);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void resetPasswd(String codigoUsuario) {
		try {
			Usuario ut = (Usuario) managerDAO.findById(Usuario.class,
					codigoUsuario);
			if (ut.getTipoUsuario().equals("VD"))
				ut.setClave("vendedor");
			if (ut.getTipoUsuario().equals("SP"))
				ut.setClave("supervisor");
			managerDAO.actualizar(ut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminarUsuario(Usuario usuario) {
		try {
			managerDAO.eliminar(usuario.getClass(), usuario.getCodigoUsuario());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
