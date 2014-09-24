import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NotesRecolator {

	private final static Logger logger = LogManager.getLogger(NotesRecolator.class.getName());

	public static void main(String[] args) {
		String pathAGuardar = "//home//pruebahadoop//Documentos//DescargasPeriodicos//Procesado//LaNacion//Economia//";

		// Obtener la carpeta donde se encuentran todos los archivos
		File carpeta = new File("//home//pruebahadoop//Documentos//DescargasPeriodicos//Original//LaNacion//Economia//");
		if (carpeta.isDirectory()) {
			ExecutorService executor = Executors.newFixedThreadPool(32);
			logger.info("PUBLICACION; NOTA; TIEMPO PROCESAMIENTO(ms); TIEMPO DESCARGA(ms); TIEMPO GUARDAR EN DISCO(ms)");
			// Recorrer cada archivo de la carpeta
			for (String archivo : carpeta.list()) {
				File file = new File(carpeta.getAbsolutePath() + "//" + archivo);
				if (file.isFile()) {
					// Obtener los links asociados a las notas de cada archivo
					try {
						Element notasABuscar = Jsoup.parse(file, "utf-8").getElementById("archivo-notas-272");
						if(notasABuscar == null)
							continue;
						Elements nota = notasABuscar.getElementsByTag("a").select("[href]");

						for (Element E : nota) {
							NoteProcessor np = new NoteProcessor(archivo, E, pathAGuardar);
							executor.execute(np);
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Se estaba procesando el archivo " + archivo);
						continue;
					}
				}
			}
		}

	}

}