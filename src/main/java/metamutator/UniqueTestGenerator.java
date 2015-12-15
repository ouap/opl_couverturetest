package metamutator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;

import junit.framework.TestCase;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

/**
 * Creates a unique test case inside a unique test class.
 *
 * The created class is runnable as main or as a test case itself.
 *
 */
@SuppressWarnings("rawtypes")
public class UniqueTestGenerator extends AbstractProcessor<CtClass> {

	// contais the block to be created
	CtBlock bWithBlock;

	@Override
	public void processingDone() {
		createClass("TestSuite");
		System.out.println("done");
	}

	private void createClass(String klassName) {
		final CtClass c = getFactory().Core().createClass();
		c.addModifier(ModifierKind.PUBLIC);
		c.setSimpleName(klassName.replace('-', '_').replace('.', '_'));


		//		// main
		{
			final CtMethod m = getFactory().Core().createMethod();
			m.setSimpleName("main");
			m.addModifier(ModifierKind.STATIC);
			m.addModifier(ModifierKind.PUBLIC);
			m.setBody(bWithBlock);
			m.setType(getFactory().Type().VOID_PRIMITIVE);
			final List<CtParameter> l = new ArrayList<>();
			final CtParameter par = getFactory().Core().createParameter();
			par.setType((CtTypeReference) getFactory().Core().createTypeReference().setSimpleName("String[]"));
			par.setSimpleName("args");
			l.add(par);
			m.setParameters(l);
			c.addMethod(m);
			m.addThrownType((CtTypeReference) getFactory().Core().createTypeReference().setSimpleName("Exception"));

			final CtCodeSnippetStatement e = getFactory().Core().createCodeSnippetStatement ();
			e.setValue("new "+c.getSimpleName()+"().test()");
			final CtBlock bod= getFactory().Core().createBlock();
			bod.addStatement(e);
			m.setBody(bod);
		}

		{
			final CtMethod m = getFactory().Core().createMethod();
			m.setSimpleName("test");
			m.addModifier(ModifierKind.PUBLIC);
			getFactory().Annotation().annotate(m, Test.class);
			m.setBody(bWithBlock);
			m.addThrownType((CtTypeReference) getFactory().Core().createTypeReference().setSimpleName("Exception"));
			m.setType(getFactory().Type().VOID_PRIMITIVE);
			c.addMethod(m);
		}

		// adding the class
		final CtPackage p = getFactory().Package().getOrCreate("test");
		p.addType(c);
	}

	@Override
	public boolean isToBeProcessed(CtClass element) {
		return element.getElements(new TypeFilter(TestCase.class)).size()>0
				&& !element.getModifiers().contains(ModifierKind.ABSTRACT)
				&& element.isTopLevel();
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public void process(CtClass element) {
		try {
			// run normal
			if (bWithBlock == null) {
				bWithBlock = getFactory().Core().createBlock();
			}

			CtCodeSnippetStatement e = createTestSnippet(element, BlockJUnit4ClassRunner.class);
			bWithBlock.addStatement(e);

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private CtCodeSnippetStatement createTestSnippet(CtClass element, Class<? extends Runner> runner) {
		final CtCodeSnippetStatement e = getFactory().Core()
				.createCodeSnippetStatement();
		final String val = "	new "+runner.getCanonicalName()+"("
				+ element.getQualifiedName()
				+ ".class).run(new org.junit.runner.notification.RunNotifier() {\n"
				+ "		@Override\n"
				+ "		public void fireTestFailure(org.junit.runner.notification.Failure failure) {\n"
				+ "			if (failure.getException() instanceof RuntimeException) throw (RuntimeException)failure.getException(); \n"
				+ "			if (failure.getException() instanceof Error) throw (Error)failure.getException(); \n"
				+ "         throw new RuntimeException(failure.getException());\n"
				+ "		}\n" + "	})";
		e.setValue(val);
		return e;
	}
}