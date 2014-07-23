package facturacion.model.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import facturacion.model.dao.entities.PedidoCab;

public class ManagerBusquedasCalendario {

	private ManagerDAO managerDao;

	public ManagerBusquedasCalendario() {
		// TODO Auto-generated constructor stub
		managerDao = new ManagerDAO();
	}

	public List<PedidoCab> busqueda(Date date1, Date date2) {

		try {
			if (date1 != null && date2 != null) {
				return managerDao.findEmployees(date1, date2);
			} else {
				if (date1 == null)
					date1 = date2;
				else
					date2 = date1;
				return managerDao.findEmployees(date1, date2);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return null;
		}
	}
}
