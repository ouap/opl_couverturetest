package opl.opl_couverturetest;

import java.util.List;

import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

/**
 * Class that create a call graph from a source file executed with Spoon
 *
 * @author sais, badache
 *
 */
public class TestProcessor extends AbstractProcessor<CtClass<?>> {
	static int i = 0;

	public void process(CtClass<?> element) {
		System.out.println("CLASSE " + element.getSimpleName());

		// Every elements which are methods are registered here
		List<CtMethod<?>> elements = element.getElements(new TypeFilter(CtMethod.class));

		for (CtMethod<?> method : elements) {
			System.out.println("annotation :" + method.getAnnotations().toString());
			System.out.println("methode :" + method.getSignature().toString());
			i++;
			// Affiche les annotations des methodes
		}
	}

	public static void main(String[] args) throws Exception {
		// Lancement du processeur
		Launcher spoon = new Launcher();
		spoon.addProcessor(new TestProcessor());
		spoon.run(new String[] { "-i", "sources_test/jfreechart-1.0.19/tests", "-x" });
		System.out.println("-------------------------" + "\n NbMethod test : " + i);

	}

}
