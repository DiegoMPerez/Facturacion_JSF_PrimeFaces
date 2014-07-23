package facturacion.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.EstadoPedido;
import facturacion.model.dao.entities.FacturaCab;
import facturacion.model.dao.entities.FacturaDet;
import facturacion.model.dao.entities.Kardex;
import facturacion.model.dao.entities.Parametro;
import facturacion.model.dao.entities.PedidoCab;
import facturacion.model.dao.entities.PedidoDet;
import facturacion.model.dao.entities.Producto;

public class ManagerPedidos {
	private ManagerDAO managerDAO;
	private ManagerFacturacion managerFacturacion;

	public ManagerPedidos() {
		managerDAO = new ManagerDAO();
		managerFacturacion = new ManagerFacturacion();
	}

	public void registrarCliente(Cliente cliente, String clave2)
			throws Exception {
		if (cliente.getCedulaCliente() == null
				|| cliente.getCedulaCliente().equals(""))
			throw new Exception("Ingrese la cédula");
		if (cliente.getNombres() == null || cliente.getNombres().equals(""))
			throw new Exception("Ingrese los nombres");
		if (cliente.getApellidos() == null || cliente.getNombres().equals("")
				|| cliente.getApellidos().equals(""))
			throw new Exception("Ingrese los apellidos");
		if (cliente.getDireccion() == null || cliente.getDireccion().equals(""))
			throw new Exception("Ingrese la dirección");
		if (cliente.getClave().equals(clave2) || cliente.getClave().equals("")) {
			managerDAO.insertar(cliente);
		} else {
			throw new Exception("Error de claves");
		}
	}

	// MANEJO DE PEDIDOS:
	/**
	 * Metodo finder para la consulta de pedidos. Hace uso del componente
	 * {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @return Listado de pedidos ordenados por numero de pedido.
	 */
	@SuppressWarnings("unchecked")
	public List<PedidoCab> findAllPedidoCab() {
		return managerDAO.findAll(PedidoCab.class, "o.numeroPedido desc");
	}

	/**
	 * Crea una nueva cabecera de pedido temporal, para que desde el programa
	 * cliente pueda manipularla y llenarle con la informacion respectiva. Esta
	 * informacion solo se mantiene en memoria.
	 * 
	 * @return el nuevo pedido temporal.
	 */
	public PedidoCab crearPedidoTmp() {
		PedidoCab pedidoCabTmp = new PedidoCab();
		pedidoCabTmp.setFechaPedido(new Date());
		pedidoCabTmp.setPedidoDets(new ArrayList<PedidoDet>());
		return pedidoCabTmp;
	}

	/**
	 * Asigna un cliente a un pedido temporal.
	 * 
	 * @param pedidoCabTmp
	 *            Pedido temporal creado en memoria.
	 * @param cedulaCliente
	 *            codigo del cliente.
	 * @throws Exception
	 */
	public void asignarClientePedidoTmp(PedidoCab pedidoCabTmp,
			String cedulaCliente) throws Exception {
		Cliente cliente = null;
		if (cedulaCliente == null || cedulaCliente.length() == 0)
			throw new Exception("Error debe especificar la cedula del cliente.");
		try {
			// invocamos al ManagerFacturacion:
			cliente = managerFacturacion.findClienteById(cedulaCliente);
			if (cliente == null)
				throw new Exception("Error al asignar cliente.");
			// si el pedido no esta creado, se crea automaticamente:
			if (pedidoCabTmp == null)
				pedidoCabTmp = crearPedidoTmp();
			pedidoCabTmp.setCliente(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erroral asignar cliente: " + e.getMessage());
		}
	}

	/**
	 * Realiza los calculos de subtotales, sin impuestos.
	 * 
	 * @param pedidoCabTmp
	 *            Pedido temporal creado en memoria.
	 */
	private void calcularPedidoTmp(PedidoCab pedidoCabTmp) {

		double sumaSubtotales;
		if (pedidoCabTmp == null)
			return;
		// verificamos los campos calculados:
		sumaSubtotales = 0;
		for (PedidoDet det : pedidoCabTmp.getPedidoDets()) {
			sumaSubtotales += det.getCantidad().intValue()
					* det.getPrecioUnitarioVenta().doubleValue();
		}
		pedidoCabTmp.setSubtotal(new BigDecimal(sumaSubtotales));
	}

	public double calcularPedidos(PedidoCab pedidoCab) {
		double sumaSubtotales;
		if (pedidoCab == null)
			return 0.0;
		// verificamos los campos calculados:
		sumaSubtotales = 0;
		for (PedidoDet det : pedidoCab.getPedidoDets()) {
			sumaSubtotales += det.getCantidad().intValue()
					* det.getPrecioUnitarioVenta().doubleValue();
		}
		return sumaSubtotales;
	}

	/**
	 * Adiciona un item detalle a un pedido temporal. Estos valores permanecen
	 * en memoria.
	 * 
	 * @param codigoProducto
	 *            codigo del producto.
	 * @param cantidad
	 *            cantidad del producto.
	 * @throws Exception
	 *             problemas ocurridos al momento de insertar el item detalle.
	 */
	public void agregarDetallePedidoTmp(PedidoCab pedidoCabTmp,
			Integer codigoProducto, Integer cantidad) throws Exception {
		Producto prod;
		PedidoDet pedidoDet;
		// si no esta creado el pedido, lo creamos automaticamente:
		if (pedidoCabTmp == null)
			pedidoCabTmp = crearPedidoTmp();
		if (codigoProducto == null || codigoProducto.intValue() < 0)
			throw new Exception(
					"Error debe especificar el codigo del producto.");
		if (cantidad == null || cantidad.intValue() <= 0)
			throw new Exception(
					"Error debe especificar una cantidad mayor a cero.");
		// buscamos el producto:
		prod = managerFacturacion.findProductoById(codigoProducto);
		// creamos un nuevo detalle y llenamos sus propiedades:
		pedidoDet = new PedidoDet();
		if (existenciaDetalle(prod, pedidoCabTmp) != null) {
			existenciaDetalle(prod, pedidoCabTmp).setCantidad(
					cantidad
							+ existenciaDetalle(prod, pedidoCabTmp)
									.getCantidad());
		} else {
			pedidoDet.setCantidad(cantidad);
			pedidoDet.setPrecioUnitarioVenta(prod.getPrecioUnitario());
			pedidoDet.setProducto(prod);
			// asignacion bidireccional:
			pedidoDet.setPedidoCab(pedidoCabTmp);
			pedidoCabTmp.getPedidoDets().add(pedidoDet);
		}

		// verificamos los campos calculados:
		calcularPedidoTmp(pedidoCabTmp);
	}

	public PedidoDet existenciaDetalle(Producto p, PedidoCab pc) {
		for (PedidoDet fd : pc.getPedidoDets()) {
			if (fd.getProducto() == p) {
				return fd;
			}
		}
		return null;
	}

	/**
	 * Guarda en la base de datos un pedido.
	 * 
	 * @param pedidoCabTmp
	 *            pedido temporal creado en memoria.
	 * @throws Exception
	 *             problemas ocurridos en la insercion.
	 */
	public void guardarPedidoTemporal(PedidoCab pedidoCabTmp) throws Exception {
		if (pedidoCabTmp == null)
			throw new Exception("Debe crear un pedido primero.");
		if (pedidoCabTmp.getPedidoDets() == null
				|| pedidoCabTmp.getPedidoDets().size() == 0)
			throw new Exception("Debe ingresar los productos en el pedido.");
		if (pedidoCabTmp.getCliente() == null)
			throw new Exception("Debe registrar el cliente.");
		pedidoCabTmp.setFechaPedido(new Date());
		// asignamos el estado NUEVO al pedido:
		EstadoPedido estado = findEstadoPedidoById("NV");
		pedidoCabTmp.setEstadoPedido(estado);
		// verificamos los campos calculados:
		calcularPedidoTmp(pedidoCabTmp);
		// guardamos el pedido completo en la bdd. Hay que
		// tomar en cuenta que los codigos de cabecera y
		// detalles se crean automaticamente mediante
		// secuencias de base de datos:
		int contPedidos;
		contPedidos = getContPedidos();
		contPedidos++;

		pedidoCabTmp.setNumeroPedido(contPedidos);

		// asignamos la clave primaria a los detalles:
		int contPedidosDet;
		contPedidosDet = getContPedidosDet();

		for (PedidoDet det : pedidoCabTmp.getPedidoDets()) {
			contPedidosDet++;
			det.setNumeroPedidoDet(new Integer(contPedidosDet));
			// vinculamos el detalle a la cabecera (relacion bidireccional):
			det.setPedidoCab(pedidoCabTmp);
		}
		pedidoCabTmp.setFechaPedido(new Date());
		managerDAO.insertar(pedidoCabTmp);
		actualizarParametro(contPedidos, "cont_Pedidos");
		actualizarParametro(contPedidosDet, "cont_pedidos_det");
		pedidoCabTmp = null;
	}

	private int getContPedidosDet() throws Exception {
		int contPedidosDet = 0;
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"cont_pedidos_det");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Revise el parametro 'cont_pedidos_det': "
					+ e.getMessage());
		}
		contPedidosDet = Integer.parseInt(parametro.getValorParametro());
		return contPedidosDet;
	}

	private int getContPedidos() throws Exception {
		int contPedidos = 0;
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class,
					"cont_Pedidos");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Revise el parametro 'cont_Pedidos': "
					+ e.getMessage());
		}
		contPedidos = Integer.parseInt(parametro.getValorParametro());
		return contPedidos;
	}

	@SuppressWarnings("unused")
	private void actualizarParametro(int nuevoContador, String param)
			throws Exception {
		Parametro parametro = null;
		try {
			parametro = (Parametro) managerDAO.findById(Parametro.class, param);
			parametro.setValorParametro(Integer.toString(nuevoContador));
			managerDAO.actualizar(parametro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al actualizar el parametro " + param
					+ ": " + e.getMessage());
		}
	}

	public EstadoPedido findEstadoPedidoById(String idEstado) throws Exception {
		EstadoPedido estado = (EstadoPedido) managerDAO.findById(
				EstadoPedido.class, idEstado);
		return estado;
	}

	public PedidoCab eliminarProductoPedido(PedidoCab pedidoCab,
			PedidoDet pedidoDetalle, String token) {
		System.out.println(token);
		for (PedidoDet detalle : pedidoCab.getPedidoDets()) {
			if (detalle == pedidoDetalle) {
				if (token.equals("+")) {
					detalle.setCantidad(detalle.getCantidad() + 1);
					return pedidoCab;
				} else {
					if (token.equals("-")) {
						if (pedidoDetalle.getCantidad() > 1) {
							detalle.setCantidad(pedidoDetalle.getCantidad() - 1);
							return pedidoCab;
						}
					} else {
						if (token.equals("e")) {
							pedidoCab.getPedidoDets().remove(detalle);
							return pedidoCab;
						}
					}
				}
			}
		}
		return pedidoCab;
	}

	// calcular totalProductos
	public int calcularTotalProductos(PedidoCab pc) {
		int cantidadTotal = 0;
		for (PedidoDet pd : pc.getPedidoDets()) {
			cantidadTotal += pd.getCantidad();
		}
		return cantidadTotal;
	}

	// anular Pedido

	public void anularPedido(PedidoCab pc) throws Exception {

		if (pc.getCausa().length() == 0)
			throw new Exception("Ingrese un mensaje de causa de anulación");

		if (pc.getEstadoPedido().getIdEstadoPedido().equals("NV")) {
			EstadoPedido temp = (EstadoPedido) managerDAO.findById(
					EstadoPedido.class, "AN");
			pc.setEstadoPedido(temp);
			managerDAO.actualizar(pc);
		} else {
			if (pc.getEstadoPedido().getIdEstadoPedido().equals("AN")) {
				throw new Exception("EL PEDIDO YA FUE ANULADO");
			} else {
				throw new Exception("EL PEDIDO YA FUE DESPACHADO");
			}
		}

	}

	// subtotal

	// ExistenciaProductosCarrito

	public boolean existenciaProductosCarrito(PedidoCab pc) {
		if (pc.getPedidoDets().size() > 0)
			return true;
		return false;
	}

	// LISTA DE PEDIDOSCAB DE UN CLIENTE
	@SuppressWarnings("unchecked")
	public List<PedidoCab> findAllPedidosCAByID(String p) {
		return managerDAO
				.findJPQL("SELECT p FROM Cliente t JOIN t.pedidoCabs p WHERE t.cedulaCliente = "
						+ p);
	}

	// LISTA DE PEDIDOSDET DE UN PEDIDO
	@SuppressWarnings("unchecked")
	public List<PedidoDet> findAllPedidosDetByID(PedidoCab pc) {
		return managerDAO
				.findJPQL("SELECT p FROM PedidoCab t JOIN t.pedidoDets p WHERE t.numeroPedido = "
						+ pc.getNumeroPedido());
	}

	/**
	 * Busca un PedidoCab mediante su numero de pedido.
	 * 
	 * @param numeroPedido
	 *            El numero del pedido a buscar.
	 * @return El PedidoCab encontrado.
	 * @throws Exception
	 */
	public PedidoCab findPedidoCabById(Integer numeroPedido) throws Exception {
		PedidoCab pedidoCab;
		pedidoCab = (PedidoCab) managerDAO.findById(PedidoCab.class,
				numeroPedido);
		return pedidoCab;
	}

	/**
	 * Busca un conjunto de pedidos de compra dentro de un rango de fechas. Para
	 * buscar todos los pedidos sin excepcion, se debe pasar <b>null</b> en los
	 * parametros de fechas.
	 * 
	 * @param fechaInicio
	 *            fecha de inicio de la busqueda.
	 * @param fechaFinal
	 *            fecha final de la busqueda.
	 * @return listado de pedidos resultante.
	 * @throws Exception
	 */
	public List<PedidoCab> findPedidoCabByFechas(Date fechaInicio,
			Date fechaFinal) throws Exception {
		List<PedidoCab> listado = null;
		if (fechaInicio == null || fechaFinal == null)
			return findAllPedidoCab();
		try {
			// Debido a que son insuficientes los metodos genericos de
			// ManagerDAO,
			// creamos un nuevo Query:
			EntityManager em = managerDAO.getEntityManager();
			String sql = "SELECT p FROM PedidoCab p WHERE p.fechaPedido between :fechaInicio and :fechaFinal order by p.numeroPedido asc";
			Query query = em.createQuery(sql);
			// pasamos los parametros a la consulta:
			query.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
			query.setParameter("fechaFinal", fechaFinal, TemporalType.DATE);
			// ejecutamos la consulta:
			listado = (List<PedidoCab>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return listado;
	}

	/**
	 * Genera una factura automaticamente mediante la informacion de un pedido
	 * especifico. Adicionalmente, el pedido que es despachado cambio de estado
	 * a "OK" (despachado).
	 * 
	 * @param numeroPedido
	 * @throws Exception
	 */
	public void despacharPedido(Integer numeroPedido) throws Exception {
		// recuperamos la informacion del pedido:
		PedidoCab pedidoCab = findPedidoCabById(numeroPedido);
		if (pedidoCab.getEstadoPedido().getIdEstadoPedido().equals("OK"))
			throw new Exception("Ya fue despachado el pedido.");
		if (pedidoCab.getEstadoPedido().getIdEstadoPedido().equals("AN"))
			throw new Exception("No puede despachar un pedido anulado.");
		// creamos la factura automaticamente:
		managerFacturacion.crearFacturaConPedido(pedidoCab);
		// si no existen excepciones, actualizamos el estado del pedido:
		EstadoPedido estado = findEstadoPedidoById("OK");
		Date d = new Date();
		pedidoCab.setFechaDespacho(d);
		pedidoCab.setEstadoPedido(estado);
		managerDAO.actualizar(pedidoCab);
	}

}