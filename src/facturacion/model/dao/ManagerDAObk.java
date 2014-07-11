package facturacion.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.Parametro;
import facturacion.model.dao.entities.Producto;

public class ManagerDAObk {

	private static EntityManagerFactory factory;
	private static EntityManager em;

	public ManagerDAObk() {
		// TODO Auto-generated constructor stub
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("FACTURACION2");
		}
		if (em == null) {
			em = factory.createEntityManager();
		}
	}

	public List<Producto> finallProductos() {
		Query q;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		q = em.createQuery("SELECT p FROM Producto p order by p.nombre");
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		return q.getResultList();
	}

	public void insertarProducto(Producto p)throws Exception{
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.persist(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	public Producto findProducto(Integer codigo){
		Producto p = null;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			p = em.find(Producto.class, codigo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		return p;
	}
	
	public void eliminarProducto(Integer codigo){
		Producto p = findProducto(codigo);
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.remove(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	public void actualizarProducto(Integer codigo, String nombre, String descripcion){
		Producto p = findProducto(codigo);
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		p.setNombre(nombre);
		p.setDescripcion(descripcion);
		try {
			em.merge(p);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	
	// ****************************   CLIENTE   *******************************
	
	public List<Cliente> finallClientes() {
		Query q;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		q = em.createQuery("SELECT p FROM Cliente p");
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		return q.getResultList();
	}

	public void insertarCliente(Cliente c)throws Exception{
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.persist(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	public Cliente findCliente(String cedula){
		Cliente c = null;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			c = em.find(Cliente.class, cedula);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		return c;
	}
	
	public void eliminarCliente(String cedula){
		Cliente c = findCliente(cedula);
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.remove(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	public void actualizarCliente(String cedula, String nombres, String apellidos, String direccion){
		Cliente c = findCliente(cedula);
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		c.setNombres(nombres);
		c.setApellidos(apellidos);
		c.setDireccion(direccion);
		try {
			em.merge(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	
	//***************   PARÁMETROS  *************************
	
	public List<Parametro> finallParametros() {
		Query q;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		q = em.createQuery("SELECT p FROM Parametro p");
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		return q.getResultList();
	}

	public void insertarParametro(Parametro c)throws Exception{
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.persist(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	public Parametro findParametro(String nombre_parametro){
		Parametro c = null;
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			c = em.find(Parametro.class, nombre_parametro);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
		return c;
	}
	
	public void eliminarParametro(String nombre_parametro){
		Parametro c = findParametro(nombre_parametro);
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		try {
			em.remove(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
	
	public void actualizarParametro(String nombre_parametro, String valor){
		Parametro c = findParametro(nombre_parametro);
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		c.setValorParametro(valor);
		try {
			em.merge(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}
}
