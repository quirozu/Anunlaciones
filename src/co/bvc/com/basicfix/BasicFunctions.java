package co.bvc.com.basicfix;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import co.bvc.com.test.AdapterIO;
import co.bvc.com.test.Login;

public class BasicFunctions {

	private static Connection conn;
	private static Login login;
	private static Map<String, String> quoteReqId = new HashMap<String, String>();
	private static String quoteIdGenered;
	private static String quoteId;
	private static long idEjecution;
	private static int idCaseSeqTcr;
	private static int idCaseSeq;
	private static AdapterIO adapterIO;
	private static int idCase;
	private static int escenarioPrueba;
	private static String iniciator;
	private static String receptor;
	private static int escenarioFinal;
	private static boolean allMarket = false;
	
	
	
	// Mensaje AE
	
//	private static String trdMatchId;
//	private static Double lastPx;
//	private static Double lastQPy;
//	private static LocalDateTime transactTime;
//	private static String symbol;
//	private static String securitySybType;
//	private static String securityId;
//	private static String securityIdSource;
//	private static char sides;
//	private static String AE_R;
	
	
	
//	public static String getAE_R() {
//		return AE_R;
//	}
//
//	public static void setAE_R(String aE_R) {
//		AE_R = aE_R;
//	}

	public static int getIdCaseSeqTcr() {
		return idCaseSeqTcr;
	}

	public static void setIdCaseSeqTcr(int idCaseSeqTcr) {
		BasicFunctions.idCaseSeqTcr = idCaseSeqTcr;
	}

//	public static char getSides() {
//		return sides;
//	}
//
//	public static void setSides(char sides) {
//		BasicFunctions.sides = sides;
//	}
//
//	public static String getTrdMatchId() {
//		return trdMatchId;
//	}
//
//	public static void setTrdMatchId(String trdMatchId) {
//		BasicFunctions.trdMatchId = trdMatchId;
//	}
//
//	public static Double getLastQPy() {
//		return lastQPy;
//	}
//
//	public static void setLastQPy(Double lastQPy) {
//		BasicFunctions.lastQPy = lastQPy;
//	}
//
//	public static LocalDateTime getTransactTime() {
//		return transactTime;
//	}
//
//	public static void setTransactTime(LocalDateTime transactTime) {
//		BasicFunctions.transactTime = transactTime;
//	}
//
//	public static Double getLastPx() {
//		return lastPx;
//	}
//
//	public static void setLastPx(Double lastPx) {
//		BasicFunctions.lastPx = lastPx;
//	}
//
//	public static String getSymbol() {
//		return symbol;
//	}
//
//	public static void setSymbol(String symbol) {
//		BasicFunctions.symbol = symbol;
//	}
//
//	public static String getSecuritySybType() {
//		return securitySybType;
//	}
//
//	public static void setSecuritySybType(String securitySybType) {
//		BasicFunctions.securitySybType = securitySybType;
//	}
//
//	public static String getSecurityId() {
//		return securityId;
//	}
//
//	public static void setSecurityId(String securityId) {
//		BasicFunctions.securityId = securityId;
//	}
//
//	public static String getSecurityIdSource() {
//		return securityIdSource;
//	}
//
//	public static void setSecurityIdSource(String securityIdSource) {
//		BasicFunctions.securityIdSource = securityIdSource;
//	}
	
	// RFQ

	public static int getEscenarioFinal() {
		return escenarioFinal;
	}

	public static void setEscenarioFinal(int escenarioFinal) {
		BasicFunctions.escenarioFinal = escenarioFinal;
	}

	public static String getReceptor() {
		return receptor;
	}

	public static void setReceptor(String receptor) {
		BasicFunctions.receptor = receptor;
	}

	public static boolean isAllMarket() {
		return allMarket;
	}

	public static void setAllMarket(boolean allMarket) {
		BasicFunctions.allMarket = allMarket;
	}

	public static String getIniciator() {
		return iniciator;
	}

	public static void setIniciator(String iniciator) {
		BasicFunctions.iniciator = iniciator;
	}

	public static int getIdCase() {
		return idCase;
	}

	public static void setIdCase(int d) {
		BasicFunctions.idCase = d;
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		BasicFunctions.conn = conn;
	}

	public static Login getLogin() {
		return login;
	}

	public static void setLogin(Login login) {
		BasicFunctions.login = login;
	}

	// Metodo de QuoteReqId
	public static void addQuoteReqId(String k, String v) {
		BasicFunctions.quoteReqId.put(k, v);
	}

	public static String getQuoteReqIdOfAfiliado(String afiliado) {
		return BasicFunctions.quoteReqId.get(afiliado);
	}

	public static void setQuoteReqId(Map<String, String> quoteReqId) {
		BasicFunctions.quoteReqId = quoteReqId;
	}

	public static Map<String, String> getQuoteReqId() {
		return quoteReqId;
	}

	public static String getQuoteIdGenered() {
		return quoteIdGenered;
	}

	public static void setQuoteIdGenered(String quoteIdGenered) {
		BasicFunctions.quoteIdGenered = quoteIdGenered;
	}

	public static String getQuoteId() {
		return quoteId;
	}

	public static void setQuoteId(String quoteId) {
		BasicFunctions.quoteId = quoteId;
	}

	public static long getIdEjecution() {
		return idEjecution;
	}

	public static void setIdEjecution(long idEjecution) {
		BasicFunctions.idEjecution = idEjecution;
	}

	public static int getIdCaseSeq() {
		return idCaseSeq;
	}

	public static void setIdCaseSeq(int idCaseSeq) {
		BasicFunctions.idCaseSeq = idCaseSeq;
	}

	public static AdapterIO getAdapterIO() {
		return adapterIO;
	}

	public static void setAdapterIO(AdapterIO adapterIO) {
		BasicFunctions.adapterIO = adapterIO;
	}

	public static int getEscenarioPrueba() {
		return escenarioPrueba;
	}

	public static void setEscenarioPrueba(int escenarioPrueba) {
		BasicFunctions.escenarioPrueba = escenarioPrueba;
	}

	/**
	 * Crea la conexi�n a la db y se la asigna a la variable conn de
	 * BasicFunctions
	 * 
	 * @return
	 */
	public static boolean createConn() {
		boolean retorno = false;

		//
		BasicFunctions.conn = DataAccess.getConnection();
		if (BasicFunctions.conn != null) {
			retorno = true;
		}

		return retorno;
	}

	/**
	 * Se crea el adaptador y las sessiones y el login con el motor de INET
	 * 
	 * @return
	 */
	public static void createLogin() {
		if (BasicFunctions.adapterIO == null) {
			BasicFunctions.adapterIO = new AdapterIO();

		}

		if (BasicFunctions.login == null) {
			BasicFunctions.login = new Login();
			BasicFunctions.login.initiation();
		}
	}
	public static void FinalLogin() {
		
			BasicFunctions.login = new Login();
			BasicFunctions.login.Final();
		
	}

	public static void startVariables() {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyMMddHmmss");
		long id_ejecution = Long.parseLong(SDF.format(new Date()));
		System.out.println("ID_EJECUCION GENERADO : " + id_ejecution);

		BasicFunctions.setIdEjecution(id_ejecution);

	}

	public static int getFirtsIdCaseSeq(int escenarioEjecucion) throws SQLException {
		int firstIdDB = DataAccess.getFirstIdCaseSeq(escenarioEjecucion);
		return firstIdDB;
	}

	public static void imprimir(String vari) {
		System.out.println(
				"\n#####################\nCLASE: " + vari.getClass() + "VARIABLE: " + vari + "\n#####################");
	}

	public static void imprimir(int vari) {
		System.out.println("\n#####################\nVARIABLE ENTERA: " + vari + "\n#####################");
	}

	public static void imprimir(boolean vari) {
		System.out.println("\n#####################\nVARIABLE BOOLEAN: " + vari + "\n#####################");
	}

}
