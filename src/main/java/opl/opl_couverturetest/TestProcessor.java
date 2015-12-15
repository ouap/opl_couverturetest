
package opl.opl_couverturetest;
import java.util.Set;

import org.junit.Test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtTypeAccessImpl;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

/**
 * Class that create random tests suite from existing ones.
 *
 * @author sais, badache
 *
 */
public class TestProcessor extends AbstractProcessor<CtClass<?>> {
	static int i = 0;
	TestSuite suite = new TestSuite();

	public void process(CtClass<?> element) {
		System.out.println("CLASSE " + element.getSimpleName());

		// Every elements which are methods are registered here
		Set<CtMethod<?>> elements = element.getMethodsAnnotatedWith(getFactory().Annotation().createReference(Test.class));

		for (CtMethod<?> method : elements) {

			
			System.out.println(element.getActualClass());
			

			System.out.println(element.getSimpleName());
			System.out.println("=======================");
			System.out.println("ANNOTATION :" + method.getAnnotations().toString());
			System.out.println("METHODE :" + method.getSimpleName());
			System.out.println("CONTENU : " + method );
			System.out.println("=======================");

			i++;
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
