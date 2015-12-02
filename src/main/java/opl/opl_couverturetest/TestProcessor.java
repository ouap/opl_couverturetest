package opl.opl_couverturetest;

import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

/**
 * Class that create a call graph from a source file executed with Spoon
 *
 * @author sais, badache
 *
 */
public class TestProcessor extends AbstractProcessor<CtClass<?>> {

	public void process(CtClass<?> element) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
		// Lancement du processeur
		Launcher spoon = new Launcher();
		spoon.addProcessor(new TestProcessor());
		spoon.run(new String[] { "-i", "sources_test/opl/java/src", "-x" });
	}

}
