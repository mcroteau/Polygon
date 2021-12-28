package shape;

//import org.apache.tomcat.InstanceManager;
//import org.apache.tomcat.SimpleInstanceManager;
//import org.apache.tomcat.util.scan.StandardJarScanFilter;
//import org.apache.tomcat.util.scan.StandardJarScanner;
//import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
//import org.eclipse.jetty.jsp.JettyJspServlet;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.ServerConnector;
//import org.eclipse.jetty.servlet.DefaultServlet;
//import org.eclipse.jetty.servlet.FilterHolder;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.eclipse.jetty.util.component.AbstractLifeCycle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polygon {

	public static final String USER_MAINTENANCE = "users:";

	public static final String SUPER_ROLE  = "SUPER_ROLE";
	public static final String USER_ROLE   = "USER_ROLE";

	public static final String SUPER_USERNAME = "croteau.mike@gmail.com";
	public static final String SUPER_PASSWORD = "password";

	public static final String PROSPECT_STATUS = "Prospect";
	public static final String WORKING_STATUS  = "Working";
	public static final String CUSTOMER_STATUS = "Import";
	public static final String IMPORT_STATUS   = "Customer";
	public static final String IDLE_STATUS     = "Idle";

	public static final String DATE_TIME  = "yyyyMMddHHmm";
	public static final String DATE_FORMAT  = "yyyyMMddHHmm";
	public static final String DATE_PRETTY  = "HH:mmaa dd MMM";

	public static final int    NOTIFICATION_JOB_DURATION = 60;
	public static final String NOTIFICATION_GROUP = "Polygon";
	public static final String NOTIFICATION_JOB = "Notification Job";
	public static final String NOTIFICATION_TRIGGER = "Trigger1";

	public static final String SMS_SERVICE_KEY   = "smsService";
	public static final String PROSPECT_REPO_KEY = "prospectRepo";


	public static String getPhone(String phone){
		if(phone != null)
			return phone
					.replaceAll("[^a-zA-Z0-9]", "")
					.replaceAll(" ", "")
					.replaceAll(" ", "")
					.replaceAll(" ", "");
		return "";
	}

	public static String getSpaces(String email) {
		if(email != null)
			return email.replaceAll(" ", "")
					.replaceAll(" ", "")
					.replaceAll(" ", "");
		return "";
	}

	public static int getNumber(int max){
		Random r = new Random();
		return r.nextInt(max);
	}

	public static boolean containsSpecialCharacters(String str) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.find();
	}


	private static String getExtension(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}

	public static String getString(int n) {
		String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		StringBuilder uuid = new StringBuilder();
		Random rnd = new Random();
		while (uuid.length() < n) {
			int index = (int) (rnd.nextFloat() * CHARS.length());
			uuid.append(CHARS.charAt(index));
		}
		return uuid.toString();
	}

	public static long getDate(){
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Polygon.DATE_FORMAT);
		String date = dtf.format(ldt);
		return Long.parseLong(date);
	}

	public static long getDateTimezone(String timezone){
		LocalDateTime ldt = LocalDateTime.now();
		ZoneId zone = ZoneId.systemDefault();
		ZoneOffset zoneOffset = zone.getRules().getOffset(ldt);
		ZonedDateTime zdt = ldt.atOffset(zoneOffset)
							.atZoneSameInstant(ZoneId.of(timezone));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Polygon.DATE_FORMAT);
		String date = dtf.format(zdt);
		return Long.parseLong(date);
	}

	public static long getDateTimezoneMins(int mins, String timezone){
		LocalDateTime ldt = LocalDateTime.now().plusMinutes(mins);
		ZoneId zone = ZoneId.systemDefault();
		ZoneOffset zoneOffset = zone.getRules().getOffset(ldt);
		ZonedDateTime zdt = ldt.atOffset(zoneOffset)
				.atZoneSameInstant(ZoneId.of(timezone));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Polygon.DATE_FORMAT);
		String date = dtf.format(zdt);
		return Long.parseLong(date);
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String getPretty(Long date){
		String dateString = "";
		try {
			SimpleDateFormat parser = new SimpleDateFormat(Polygon.DATE_FORMAT);
			Date d = parser.parse(Long.toString(date));

			SimpleDateFormat sdf2 = new SimpleDateFormat(Polygon.DATE_PRETTY);
			dateString = sdf2.format(d);
		}catch(Exception ex){}
		return dateString;
	}

	public static String pad(String value, int places, String character){
		if(value.length() < places){
			value = character.concat(value);
			pad(value, places, character);
		}
		return value;
	}

//	Server server;
//	final String WEBROOT_INDEX = "/src/main/webapp/";
//
//	public static void main(String[] args) throws Exception {
//		Polygon bitBeat = new Polygon();
//		bitBeat.start();
//		bitBeat.standby();
//	}
//
//	public void start() throws Exception {
//		server = new Server();
//
//		ServerConnector connector = new ServerConnector(server, -1, -1);
//		connector.setPort(3001);
//		server.addConnector(connector);
//
//		URI baseUri = getWebRootResourceUri();
//
//		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//		servletContextHandler.addEventListener(new EventsListener());
//		servletContextHandler.setContextPath("/");
//		servletContextHandler.setResourceBase(baseUri.toASCIIString());
//
//		enableEmbeddedJspSupport(servletContextHandler);
//
//		addWebappJsps(servletContextHandler);
//
//		ServletHolder mediatorServlet = new ServletHolder("httpMediator", HttpMediator.class);
//		servletContextHandler.addServlet(mediatorServlet, "/*");
//
//		ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
//		holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
//		holderDefault.setInitParameter("dirAllowed", "true");
//		servletContextHandler.addServlet(holderDefault, "/");
//
//		FilterHolder parakeetHolder = new FilterHolder();
//		parakeetHolder.setFilter(new ParakeetFilter());
//		servletContextHandler.addFilter(parakeetHolder, "/*", null);
//
//		server.setHandler(servletContextHandler);
//
//		server.start();
//	}
//
//	protected void addWebappJsps(ServletContextHandler servletContextHandler){
//		Enumeration<JarEntry> entries = Qio.getEntries();
//		do {
//
//			JarEntry jarEntry = entries.nextElement();
//			if(jarEntry.toString().contains("src/main/webapp/") &&
//					jarEntry.toString().endsWith(".jsp")){
//
//				String[] bits = jarEntry.toString().split("src/main/webapp");
//				String path = bits[1];
//
//				ServletHolder holderAltMapping = new ServletHolder();
//				holderAltMapping.setName(path);
//				holderAltMapping.setForcedPath(path);
//
//				servletContextHandler.addServlet(holderAltMapping, path);
//
//			}
//
//		}while(entries.hasMoreElements());
//
//	}
//
//	private void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws IOException {
//
//		File tempDir = new File(System.getProperty("java.io.tmpdir"));
//		File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");
//
//		if (!scratchDir.exists()) {
//			if (!scratchDir.mkdirs()) {
//				throw new IOException("Unable to create scratch directory: " + scratchDir);
//			}
//		}
//
//		servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);
//
//		ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
//		servletContextHandler.setClassLoader(jspClassLoader);
//
//		servletContextHandler.addBean(new Polygon.JettyStarter(servletContextHandler));
//
//		ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
//		holderJsp.setInitOrder(0);
//		holderJsp.setInitParameter("scratchdir", scratchDir.toString());
//		holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
//		holderJsp.setInitParameter("fork", "false");
//		holderJsp.setInitParameter("xpoweredBy", "false");
//		holderJsp.setInitParameter("compilerTargetVM", "1.8");
//		holderJsp.setInitParameter("compilerSourceVM", "1.8");
//		holderJsp.setInitParameter("keepgenerated", "true");
//		servletContextHandler.addServlet(holderJsp, "*.jsp");
//
//		servletContextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
//	}
//
//	private URI getWebRootResourceUri() throws FileNotFoundException, URISyntaxException {
//		URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
//		if (indexUri == null) throw new FileNotFoundException("Unable to find resource " + WEBROOT_INDEX);
//
//		return indexUri.toURI();
//	}
//
//	public void standby() throws InterruptedException {
//		server.join();
//	}



//	public class JettyStarter extends AbstractLifeCycle {
//
//		JettyJasperInitializer sci;
//		ServletContextHandler context;
//
//		public JettyStarter(ServletContextHandler context) {
//			this.sci = new JettyJasperInitializer();
//			this.context = context;
//
//			StandardJarScanner jarScanner = new StandardJarScanner();
//			StandardJarScanFilter jarScanFilter = new StandardJarScanFilter();
//
//			jarScanFilter.setTldScan("taglibs-standard-impl-*");
//			jarScanFilter.setTldSkip("apache-*,ecj-*,jetty-*,asm-*,javax.servlet-*,javax.annotation-*,taglibs-standard-spec-*");
//			jarScanner.setJarScanFilter(jarScanFilter);
//
//			this.context.setAttribute("org.apache.tomcat.JarScanner", jarScanner);
//		}
//
//		@Override
//		protected void doStart() throws Exception {
//			ClassLoader old = Thread.currentThread().getContextClassLoader();
//			Thread.currentThread().setContextClassLoader(context.getClassLoader());
//			try {
//				sci.onStartup(null, context.getServletContext());
//				super.doStart();
//			}finally {
//				Thread.currentThread().setContextClassLoader(old);
//			}
//		}
//	}

}