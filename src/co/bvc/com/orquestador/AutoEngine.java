package co.bvc.com.orquestador;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.CreateReport;
import co.bvc.com.test.Login;
import co.bvc.com.test.Validaciones;
import co.bvc.com.xstreaminet.amp.TradeCancelAmp;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.TrdMatchID;
import quickfix.Message;

public class AutoEngine {

	CreateMessage createMesage = new CreateMessage();

	// metodo que inicia la ejecucion
	public void iniciarEjecucion(int escenarioEjecucion, int escenarioFinal)
			throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		BasicFunctions.createConn();
		int firsIdCaseSec = BasicFunctions.getFirtsIdCaseSeq(escenarioEjecucion);
		BasicFunctions.setEscenarioPrueba(escenarioEjecucion);
		BasicFunctions.setEscenarioFinal(escenarioFinal);
		if (firsIdCaseSec > 0) {
			BasicFunctions.startVariables();
			BasicFunctions.createLogin();
			DataAccess.limpiarCache();
			ejecutarSiguientePaso();
		} else {
			System.out.println("NO HAY DATOS EN LA BASE DE DATOS...");
		}
	}

	public void ejecutarSiguientePaso()
			throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		int caso = BasicFunctions.getEscenarioFinal();
		Thread.sleep(5000);
		System.out.println("ID_CASESEQ: " + BasicFunctions.getIdCaseSeq());
		ResultSet rsDatos = DataAccess.datosMensaje(BasicFunctions.getIdCaseSeq());
		while (rsDatos.next()) {

			BasicFunctions.setIdCase(rsDatos.getInt("ID_CASE"));
			System.out.println("Continua con el siguiente paso.");
			System.out.println("<><><><><><><><>\n " + BasicFunctions.getIdCaseSeq() + "\n<><><><><><><><>");

			if (caso < BasicFunctions.getIdCase()) {
				Thread.sleep(5000);
				System.out.println("********************************************");
				System.out.println("************* FIN DE EJECUCION ************");
				System.out.println("********************************************");
				caso++;
				System.out.println("GENERAR REPORTE....");
				CreateReport.maina();
				BasicFunctions.FinalLogin();
			} else {
				enviarMensaje(rsDatos);
				Thread.sleep(5000);
				BasicFunctions.setIdCaseSeq(BasicFunctions.getIdCaseSeq() + 1);
				System.out.println("************* SECUENCIA *************" + BasicFunctions.getIdCaseSeq());
			}

		}

	}


	public void enviarMensaje(ResultSet resultSet)
			throws SessionNotFound, SQLException, InterruptedException, FieldNotFound {

		String msgType = resultSet.getString("ID_ESCENARIO");
		String idAfiliado = resultSet.getString("ID_AFILIADO");
		String idCase = resultSet.getString("ID_CASE");
		System.out.println(resultSet);
		AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();
		RespuestaConstrucccionMsgFIX respConstruccion = new RespuestaConstrucccionMsgFIX();

		switch (msgType) {
			
		case "FIX_AE":
			
			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_AE **");
			System.out.println("**********************");
			
			respConstruccion = createMesage.createAE(resultSet);
			Session.sendToTarget(respConstruccion.getMessage(), Login.getSessionOfAfiliado(idAfiliado));
			
			for(String session : respConstruccion.getListSessiones()) {
				
				// Construir mensaje a cache.
				datosCache.setReceiverSession(session);
				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				datosCache.setEstado(resultSet.getString("ESTADO"));
				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
				
			}
			
			System.out.println("MENSAJE AE ENVIADO");
			
			break;
			
         case "FIX_AE_R":
			
			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_AE_R **");
			System.out.println("**********************");
			
			BasicFunctions.setAE_R(msgType);
			
			respConstruccion = createMesage.createAE_R(resultSet);
			
			Session.sendToTarget(respConstruccion.getMessage(), Login.getSessionOfAfiliado(idAfiliado));
			
            for(String session : respConstruccion.getListSessiones()) {
				
				// Construir mensaje a cache.
				datosCache.setReceiverSession(session);
				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				datosCache.setEstado(resultSet.getString("ESTADO"));
				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
				
			}
            
            System.out.println("MENSAJE AE_R ENVIADO");
            
			
			break;
			

		default:
			break;
		}

	}

	// Metodo que guarda el registro en base de datos
	public void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException, InterruptedException {
		DataAccess.cargarCache(datosCache);
	}

	// Metodo que elimina el registro en cache (base de datos)
	public void eliminarDatoCache(String session) throws SQLException, InterruptedException {

		String queryDelete = "DELETE FROM bvc_automation_db.aut_fix_tcr_cache WHERE RECEIVER_SESSION = " + "'" + session
				+ "'" + ";";

		DataAccess.setQuery(queryDelete);
	}

	// Metodo que extraer el registro en base de datos
	public AutFixRfqDatosCache obtenerCache(String session) throws SQLException, InterruptedException {

		System.out.println("SESS: " + session);
		return DataAccess.obtenerCache(session);

	}
    
	public void validarAE(SessionID sessionId, Message message) throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {
		
		System.out.println("*************************");
		System.out.println("** INGRESA A VALIDAR AE **");
		System.out.println("*************************");
		
		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		
//		Validaciones validaciones = new Validaciones();
//		validaciones.validarAE(datosCache, (quickfix.fix44.Message) message);
		
		int valueTRType = message.isSetField(856) ? message.getInt(856) : 0;
		
		DataAccess.limpiarCache();
		
		if(valueTRType == 99) {
			String trMatchId = message.getString(880);
			
			TradeCancelAmp tradeCancelAmp = new TradeCancelAmp();
			String userInet = "su1";
			String passInet = "";
			
			boolean approveBVC = tradeCancelAmp.tradeCancelApprove(userInet, passInet, trMatchId);
			
			if(approveBVC) {
				System.out.println("APROBACION EXITOSA...");
			} else {
				System.out.println("APROBACI�N NO EXITOSA...");
			}
			
			System.out.println("Aqu� va aprobaci�n de BVC...");
		} else {		
			if(DataAccess.validarContinuidadEjecucion()) {
				ejecutarSiguientePaso();
				
				System.out.println("*** CONTINUAR ***");
			}else {
				System.out.println("*** ESPERAR ***");
			}
		}
		
	}
	public void validarAR(SessionID sessionId, Message message) throws SQLException, InterruptedException, FieldNotFound, SessionNotFound, IOException {
		
		System.out.println("*************************");
		System.out.println("** INGRESA A VALIDAR AR **");
		System.out.println("*************************");
		
		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		
//		Validaciones validaciones = new Validaciones();
//		validaciones.validarAR(datosCache, (quickfix.fix44.Message) message);
		
		DataAccess.limpiarCache();
		
		if(DataAccess.validarContinuidadEjecucion()) {
			ejecutarSiguientePaso();
			
			System.out.println("*** CONTINUAR ***");
		}else {
			System.out.println("*** ESPERAR ***");
		}		
		
	}

	public static void printMessage(String typeMsg, SessionID sessionId, Message message) throws FieldNotFound {
		System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sessionId
				+ "\nMENSAJE :" + message + "\n----------------------------");

	}

	public void validarAG(SessionID sessionId, Message message)
			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************");
		System.out.println("** INGRESA A validar AG **");
		System.out.println("*************************");

		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		Validaciones validaciones = new Validaciones();
//		validaciones.validarAG(datosCache, (quickfix.fix44.Message) message);

		// Eliminar Registro en Cache.
		DataAccess.limpiarCache();

		ejecutarSiguienteEscenario();
		System.out.println("** CONTINUAR ***");

		System.out.println("*********** SALIENDO DE validarAG ************");
	}

	public void validarER(SessionID sessionId, Message message)
			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************");
		System.out.println("** INGRESA A validar ER **");
		System.out.println("*************************");

		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		Validaciones validaciones = new Validaciones();
		validaciones.validarER(datosCache, (quickfix.fix44.Message) message);

		// Eliminar Registro en Cache.
		DataAccess.limpiarCache();
		
		if(DataAccess.validarContinuidadEjecucion()) {
			ejecutarSiguienteEscenario();
			
			System.out.println("*** CONTINUAR ***");
		}else {
			System.out.println("*** ESPERAR ***");
		}		
		
		System.out.println("*********** SALIENDO DE validarER ************");
	}

	public void ejecutarSiguienteEscenario()
			throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		int sec = BasicFunctions.getIdCase();
		sec = sec + 1;
		System.out.println("********* " + sec);
		String query = "SELECT * FROM bvc_automation_db.aut_fix_tcr_datos" + " WHERE ID_CASE= " + sec
				+ " ORDER BY ID_CASESEQ ASC LIMIT 1;";
		ResultSet resultset = DataAccess.getQuery(query);
		while (resultset.next()) {
			int cas = resultset.getInt("ID_CASESEQ");
			BasicFunctions.setIdCaseSeq(cas);
			ejecutarSiguientePaso();
		}
	}

	public void validar3(SessionID sessionId, Message messageIn)

			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************");
		System.out.println("** INGRESA A VALIDAR 3 **");
		System.out.println("*************************");

		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);

		Validaciones validaciones = new Validaciones();
//		validaciones.validar3(datosCache, (quickfix.fix44.Message) messageIn);

		// Eliminar Registro en Cache.
		eliminarDatoCache(sIdAfiliado);
		DataAccess.limpiarCache();
		ejecutarSiguienteEscenario();
		System.out.println("** CONTINUAR ***");
		System.out.println("*********** SALIENDO DE VALIDAR 3 ************");
		Thread.sleep(5000);

	}

}
