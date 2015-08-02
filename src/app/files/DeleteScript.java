package app.files;

import java.io.File;

import app.main.MainClass;

public class DeleteScript {

	public static void main(String[] args) {

		DeleteScript.deleteEvaluationResults(MainClass.ROOT_PATH_PREF);
		System.out.println("Done deleting");

	}

	public static void deleteEvaluationResults(String folderPath) {

		File fileToExtract = new File(folderPath);

		File[] insideFolder = fileToExtract.listFiles();

		for (int i = 0; i < insideFolder.length; i++) {

			if (insideFolder[i].isDirectory()) {
				File[] wegf234 = insideFolder[i].listFiles();

				for (int j = 0; j < wegf234.length; j++) {
					if (wegf234[j].getName().contains("generated_profiled_")
							&& (wegf234[j].getName().contains(".arff") || wegf234[j]
									.getName().contains(".xls"))) {
						wegf234[j].delete();
					}
					if (wegf234[j].getName().contains("realAndPredicted")
							&& wegf234[j].getName().contains(".csv")) {
						wegf234[j].delete();
					}
				}
			} else {
				insideFolder[i].delete();
			}
		}
	}
}
