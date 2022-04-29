package fr.abes.qualinka.eclipse.propertyTester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;

import fr.abes.qualinka.eclipse.QualinkaUtils;
import fr.abes.sudoqual.rule_engine.feature.Feature;
import fr.abes.sudoqual.rule_engine.predicate.Criterion;
import fr.abes.sudoqual.rule_engine.predicate.Filter;

public class QualinkaPropertyTester extends PropertyTester {

	private static final String PROPERTY_IS_FEATURE = "isFeature"; //$NON-NLS-1$
	private static final String PROPERTY_IS_FILTER = "isFilter"; //$NON-NLS-1$
	private static final String PROPERTY_IS_CRITERION = "isCriterion"; //$NON-NLS-1$

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		IJavaElement javaElement = null;

		if (receiver instanceof IAdaptable) {
			javaElement = QualinkaUtils.iAdaptableToIJavaElement((IAdaptable) receiver);
		}
		if (javaElement != null) {
			if (!javaElement.exists()) {
				return false;
			}
			if (PROPERTY_IS_FEATURE.equals(property)) {
				return isInstanceOf(javaElement, Feature.class.getName());
			} else if (PROPERTY_IS_FILTER.equals(property)) {
				return isInstanceOf(javaElement, Filter.class.getName());
			} else if (PROPERTY_IS_CRITERION.equals(property)) {
				return isInstanceOf(javaElement, Criterion.class.getName());
			}
		}
		return false;
	}

	private boolean isInstanceOf(IJavaElement element, String className) {
		try {
			IType type = getType(element);
			if (type != null) {
				IType[] allSuperTypes = JavaModelUtil.getAllSuperTypes(type, new NullProgressMonitor());
				for (int i = 0; i < allSuperTypes.length; i++) {
					IType superType = allSuperTypes[i];
					if (superType.getFullyQualifiedName().equals(className)) { // $NON-NLS-1$
						return true;
					}
				}
			}
		} catch (JavaModelException e) {
		}
		return false;
	}

	private IType getType(IJavaElement element) throws JavaModelException {
		IType type = null;
		if (element instanceof ICompilationUnit) {
			ICompilationUnit cu = (ICompilationUnit) element;
			type = cu.getType(Signature.getQualifier(cu.getElementName()));
		} else if (element instanceof IClassFile) {
			type = ((IClassFile) element).getType();
		} else if (element instanceof IType) {
			type = (IType) element;
		} else if (element instanceof IMember) {
			type = ((IMember) element).getDeclaringType();
		}
		return type;
	}

}
