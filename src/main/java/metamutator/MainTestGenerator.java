package metamutator;
import java.io.File;

import spoon.Launcher;


public class MainTestGenerator {

	public static void main(String[] args) {

		generateTests("sources_test/commons-lang/src/test/java", 5);
		//generateTests("sources_test/jfreechart/tests", 30);


		//spoon.run(new String[] { "-i", "sources_test/jfreechart-1.0.19/tests", "-x" });

		//spoon.run(new String[] { "-i", "sources_test/joda-time/src/test", "-x" });

		//spoon.run(new String[] { "-i", "sources_test/junit/src/test/java", "-x" });

		//spoon.run(new String[] { "-i", "sources_test/poi-3.13/src", "-x" });
	}

	private static void generateTests(String path, int nb) {
		File old;
		File newName;
		// Lancement du processeur

		for (int i = 0; i < 100; i++) {
			Launcher spoon = new Launcher();

			switch (nb) {
			case 100:
				spoon.addProcessor(new UniqueTestGenerator100());
				break;
			case 10:
				spoon.addProcessor(new UniqueTestGenerator10());
				break;
			case 5:
				spoon.addProcessor(new UniqueTestGenerator5());
				break;
			case 30:
				spoon.addProcessor(new UniqueTestGenerator30());
				break;
			case 1000:
				spoon.addProcessor(new UniqueTestGenerator1000());
				break;
			}


			spoon.run(new String[] { "-i", path, "-x" });
			old = new File("spooned/test/TestSuite.java");
			newName = new File("spooned/test/TestSuite_"+ path.split("/")[1] + nb + "-"+(i+1)+".java");

			if(old.renameTo(newName)) {
				System.out.println("renamed");
				old.delete();
			} else {
				System.out.println("Error");
			}

		}
	}

}
