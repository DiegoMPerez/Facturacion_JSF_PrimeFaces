package facturacion.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Fetch;

import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.FacturaCab;
import facturacion.model.dao.entities.FacturaDet;
import facturacion.model.dao.entities.Kardex;
import facturacion.model.dao.entities.Parametro;
import facturacion.model.dao.entities.Producto;
import facturacion.model.dao.entities.TipoMovimiento;

/**
 * Clase que implementa las reglas de negocio relacionadas al sistema de
 * facturacion.
 * 
 * @author mrea
 * 
 */
public class ManagerKardex {
	private ManagerDAO managerDAO;

	public ManagerKardex() {
		managerDAO = new ManagerDAO();
	}

	// MANEJO DE PRODUCTOS:
	/**
	 * Metodo finder para consulta de productos. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @return listado de Productos ordenados por descripcion.
	 */
	@SuppressWarnings("unchecked")
	public List<Kardex> findAllKardexByID(Producto p) {
		return managerDAO
				.findJPQL("SELECT p FROM Producto t JOIN t.kardexs p WHERE t.codigoProducto = "
						+ p.getCodigoProducto());
	}

	/**
	 * Metodo finder para consulta de productos. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @param codigoProducto
	 *            codigo del producto que se desea buscar.
	 * @return el producto encontrado.
	 * @throws Exception
	 */
	public Producto findProductoById(Integer codigoProducto) throws Exception {
		return (Producto) managerDAO.findById(Producto.class, codigoProducto);
	}

	/**
	 * Guarda un nuevo producto en la base de datos. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @param p
	 *            El nuevo producto.
	 * @throws Exception
	 */
	public void insertarKardex(Kardex kardex) throws Exception {
		int id = managerDAO.findAll(Kardex.class).size();
		kardex.setId(id + 1);
		saldo(kardex);
		movimientoProductos(kardex.getProducto(), kardex
				.getTipoMovimientoBean().getId(), kardex.getCantidad());
		managerDAO.insertar(kardex);
	}

	public void movimientoProductos(Producto p, int codigomovimiento,
			int catidad) {
		// INGRESO
		if (codigomovimiento == 1) {
			p.setExistencia(p.getExistencia() + catidad);
		}
		// EGRESO
		else if (codigomovimiento == 2 || codigomovimiento == 3) {
			if (p.getExistencia() >= catidad)
				p.setExistencia(p.getExistencia() - catidad);
		} else if (codigomovimiento == 4) {
			p.setExistencia(p.getExistencia() + catidad);
		}
	}

	public void saldo(Kardex kardex) {
		// INGRESO
		if (kardex.getTipoMovimientoBean().getId() == 1) {
			kardex.setSaldo(kardex.getStock() + kardex.getCantidad());
		}
		// EGRESO
		else if (kardex.getTipoMovimientoBean().getId() == 2
				|| kardex.getTipoMovimientoBean().getId() == 3) {
			if (kardex.getProducto().getExistencia() >= kardex.getCantidad())
				kardex.setSaldo(kardex.getProducto().getExistencia()
						- kardex.getCantidad());
		}else if (kardex.getTipoMovimientoBean().getId() == 4) {
			kardex.setSaldo(kardex.getProducto().getExistencia()
					+ kardex.getCantidad());
		}
	}

	/**
	 * Borra de la base de datos un producto especifico. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @param codigoProducto
	 *            el codigo del producto que se desea eliminar.
	 * @throws Exception
	 */
	public void eliminarProducto(Integer codigoProducto) throws Exception {
		managerDAO.eliminar(Producto.class, codigoProducto);
	}

	/**
	 * Actualiza la informacion de un producto en la base de datos. Hace uso del
	 * componente {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @param producto
	 *            Los datos del producto que se desea actualizar.
	 * @throws Exception
	 */
	public void actualizarProducto(Producto producto) throws Exception {
		Producto p = null;
		try {
			// buscamos el producto a modificar desde la bdd:
			p = findProductoById(producto.getCodigoProducto());
			// actualizamos las propiedades:
			p.setDescripcion(producto.getDescripcion());
			p.setExistencia(producto.getExistencia());
			p.setNombre(producto.getNombre());
			p.setPrecioUnitario(producto.getPrecioUnitario());
			p.setTieneImpuesto(producto.getTieneImpuesto());
			// actualizamos:
			managerDAO.actualizar(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	// MANEJO DE PARAMETROS:

	/**
	 * Metodo finder para la consulta de parametros. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @return listado de parametros.
	 */
	@SuppressWarnings("unchecked")
	public List<Parametro> findAllParametros() {
		return managerDAO.findAll(Parametro.class);
	}

	/**
	 * Obtiene el valor actual para el porcentaje de impuesto IVA.
	 * 
	 * @return valor del IVA
	 */
	public double getPorcentajeIVA() {
		Parametro parametro;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"valor_iva");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return Double.parseDouble(parametro.getValorParametro());

	}

	/**
	 * Retorna el valor actual del contador de facturas. Este contador es un
	 * parametro del sistema.
	 * 
	 * @return ultimo valor del contador de facturas
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private int getContFacturas() throws Exception {
		int contFacturas = 0;
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"cont_facturas");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Revise el parametro 'cont_facturas': "
					+ e.getMessage());
		}
		contFacturas = Integer.parseInt(parametro.getValorParametro());
		return contFacturas;
	}

	/**
	 * Retorna el valor actual del contador de los detalles de facturas. Este
	 * contador es un parametro del sistema.
	 * 
	 * @return ultimo valor del contador del detalle de facturas
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private int getContFacturasDet() throws Exception {
		int contFacturasDet = 0;
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"cont_facturas_det");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Revise el parametro 'cont_facturas_det': "
					+ e.getMessage());
		}
		contFacturasDet = Integer.parseInt(parametro.getValorParametro());
		return contFacturasDet;
	}

	/**
	 * Actualiza el valor del contador de facturas.
	 * 
	 * @param nuevoContadorFacturas
	 *            nuevo valor del contador.
	 * @throws Exception
	 */
	private void actualizarContFacturas(int nuevoContadorFacturas)
			throws Exception {
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"cont_facturas");
			parametro
					.setValorParametro(Integer.toString(nuevoContadorFacturas));
			managerDAO.actualizar(parametro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error al actualizar el parametro 'cont_facturas': "
							+ e.getMessage());
		}
	}

	/**
	 * Actualiza el contador del detalle de facturas.
	 * 
	 * @param nuevoContadorFacturasDet
	 *            nuevo valor del contador.
	 * @throws Exception
	 */
	private void actualizarContFacturasDet(int nuevoContadorFacturasDet)
			throws Exception {
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"cont_facturas_det");
			parametro.setValorParametro(Integer
					.toString(nuevoContadorFacturasDet));
			managerDAO.actualizar(parametro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error al actualizar el parametro 'cont_facturas_det': "
							+ e.getMessage());
		}
	}

	// MANEJO DE CLIENTES:

	/**
	 * Metodo finder para la consulta de clientes. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @return listado de clientes ordenados por apellidos.
	 */
	@SuppressWarnings("unchecked")
	public List<Cliente> findAllClientes() {
		return managerDAO.findAll(Cliente.class, "o.apellidos");
	}

	/**
	 * Metodo finder para la consulta de un cliente especifico.
	 * 
	 * @param cedula
	 *            cedula del cliente que se desea buscar.
	 * @return datos del cliente.
	 * @throws Exception
	 */
	public Cliente findClienteById(String cedula) throws Exception {
		Cliente cliente = null;
		try {
			cliente = (Cliente) managerDAO.findById(Cliente.class, cedula);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar cliente: " + e.getMessage());
		}
		return cliente;
	}

	// MANEJO DE FACTURAS:

	/**
	 * Metodo finder para la consulta de facturas. Hace uso del componente
	 * {@link model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @return Listado de facturas ordenadas por fecha de emision y numero de
	 *         factura.
	 */
	@SuppressWarnings("unchecked")
	public List<FacturaCab> findAllFacturaCab() {
		return managerDAO.findAll(FacturaCab.class,
				"o.fechaEmision desc,o.numeroFactura desc");
	}

	/**
	 * Crea una nueva cabecera de factura temporal, para que desde el programa
	 * cliente pueda manipularla y llenarle con la informacion respectiva. Esta
	 * informacion solo se mantiene en memoria.
	 * 
	 * @return la nueva factura temporal.
	 */

	/**
	 * Asigna un cliente a una factura temporal.
	 * 
	 * @param cedulaCliente
	 *            codigo del cliente.
	 * @throws Exception
	 */
	public void asignarClienteFacturaTmp(FacturaCab facturaCabTmp,
			String cedulaCliente) throws Exception {

		Cliente cliente = null;
		if (cedulaCliente == null || cedulaCliente.length() == 0)
			throw new Exception("Error debe especificar la cedula del cliente.");
		try {
			cliente = findClienteById(cedulaCliente);
			if (cliente == null)
				throw new Exception("Error al asignar cliente.");
			facturaCabTmp.setCliente(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al asignar cliente: " + e.getMessage());
		}
	}

	public void agregarDetalleFacturaTmp(FacturaCab facturaCabTmp,
			Integer codigoProducto, Integer cantidad) throws Exception {
		Producto p;
		FacturaDet fd;

		if (facturaCabTmp == null)
			throw new Exception("Error primero debe crear una nueva factura.");
		if (codigoProducto == null || codigoProducto.intValue() < 0)
			throw new Exception(
					"Error debe especificar el codigo del producto.");
		if (cantidad == null || cantidad.intValue() <= 0)
			throw new Exception(
					"Error debe especificar la cantidad del producto.");

		// buscamos el producto:
		p = findProductoById(codigoProducto);

		if (p.getExistencia() - cantidad < 0)
			throw new Exception("La cantidad sobrepasa el stock del producto");
		if (p.getExistencia() == 0)
			throw new Exception();
		// creamos un nuevo detalle y llenamos sus propiedades:
		fd = new FacturaDet();
		fd.setCantidad(cantidad);
		fd.setPrecioUnitarioVenta(p.getPrecioUnitario());
		fd.setProducto(p);
		facturaCabTmp.getFacturaDets().add(fd);

		// verificamos los campos calculados:
		calcularFacturaTmp(facturaCabTmp);
	}

	private void calcularFacturaTmp(FacturaCab facturaCabTmp) {
		double sumaSubtotales;
		double porcentajeIVA, valorIVA, totalFactura;
		// verificamos los campos calculados:
		sumaSubtotales = 0;
		for (FacturaDet det : facturaCabTmp.getFacturaDets()) {
			sumaSubtotales += det.getCantidad().intValue()
					* det.getPrecioUnitarioVenta().doubleValue();
		}

		porcentajeIVA = getPorcentajeIVA();
		valorIVA = sumaSubtotales * porcentajeIVA / 100;
		totalFactura = sumaSubtotales + valorIVA;

		facturaCabTmp.setSubtotal(new BigDecimal(sumaSubtotales));
		facturaCabTmp.setValorIva(new BigDecimal(valorIVA));
		facturaCabTmp.setBaseCero(new BigDecimal(0));// no calculamos la base
														// cero en este ejemplo.
		facturaCabTmp.setTotal(new BigDecimal(totalFactura));
	}

	// CERO STOCK
	public boolean existenciaProducto(int codigo) {
		try {
			Producto p = (Producto) managerDAO.findById(Producto.class,
					(Object) codigo);
			if (p.getExistencia() != 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

}
