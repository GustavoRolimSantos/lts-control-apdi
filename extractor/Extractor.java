package extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

import constants.Constants;
import data.Product;
import ltscontrol.LTSControl;

public class Extractor {

	private LTSControl pdfExtractor;
	private PrintWriter printWriter;

	public Extractor(LTSControl pdfExtractor) {
		this.pdfExtractor = pdfExtractor;
	}

	private String marca = "";

	public void execute(File file) {
		File outputFile = buildFiles(file);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));

			String line = reader.readLine();

			while (line != null) {

				line = deAccent(line);

				if (line.contains(",,,,")) {
					marca = line.replace(",", "").trim();
				}

				if (line.contains(",")) {
					try {

						if (line.split(",").length == 5 && line.contains("\"")) {
							int start = line.indexOf('"'), end = line.lastIndexOf('"');
							// System.out.println(start + " " + end);

							String sub = line.substring(start, end);
							String sub2 = sub.replaceAll(",", ".");

							line = line.replace(sub, sub2);
						}

						String[] data = line.split(",");

						if (line.contains(" UN")) {

							String quantity = "", id = "", name = "";

							for (String d : data) {
								if (d.trim().isEmpty())
									continue;

								if (d.contains(" UN")) {
									quantity = d.trim().replace(".", "").replace("UN", "").trim();
								} else if (d.length() == 5) {
									id = d;
								} else if (d.length() >= 10) {
									name = d;
								}
							}

							pdfExtractor.getProductManager().addProduct(new Product(name, id, quantity, marca));

						}
					} catch (Exception e) {

					}
				}

				line = reader.readLine();
			}

			reader.close();

			for (Product product : pdfExtractor.getProductManager().getProducts()) {
				printWriter.println(product.getId() + ";" + product.getName() + ";" + product.getBrand() + ";" + product.getQuantity());
			}
			printWriter.close();
			Files.copy(outputFile.toPath(), new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/" + outputFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String removeAccents(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	private File buildFiles(File file) {
		Calendar calendar = Calendar.getInstance();

		String outputPath = Constants.MAIN_DIRECTORY + "output/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);

		if (!new File(outputPath).exists()) {
			new File(outputPath).mkdirs();
		}

		File outputFile = new File(outputPath + "/" + file.getName());

		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			this.printWriter = new PrintWriter(Files.newBufferedWriter(outputFile.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return outputFile;
	}

}
