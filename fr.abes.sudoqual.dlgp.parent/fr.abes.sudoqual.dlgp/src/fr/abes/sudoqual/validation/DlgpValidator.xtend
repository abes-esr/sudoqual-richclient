/*
 * generated by Xtext 2.17.1
 */
package fr.abes.sudoqual.validation

import fr.abes.sudoqual.DlgpUtils
import org.eclipse.xtext.validation.Check
import fr.abes.sudoqual.dlgp.Predicate
import fr.abes.sudoqual.dlgp.DlgpPackage
import fr.abes.sudoqual.dlgp.Atom
import fr.abes.sudoqual.dlgp.DimensionPredicate
import fr.abes.sudoqual.rule_engine.predicate.Filter
import fr.abes.sudoqual.rule_engine.predicate.Criterion
import fr.abes.sudoqual.dlgp.Variable
import fr.abes.sudoqual.dlgp.Constant
import fr.abes.sudoqual.dlgp.Literal
import fr.abes.sudoqual.dlgp.Term
import org.eclipse.emf.common.util.EList
import fr.abes.sudoqual.dlgp.HeadAtom
import org.eclipse.emf.ecore.EStructuralFeature
import fr.abes.sudoqual.rule_engine.DiscretCompType

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class DlgpValidator extends AbstractDlgpValidator {
	
	@Check
	def void checkPredicateName(Predicate predicate) {
		val project = DlgpUtils.getQualinkaProjectFrom(predicate);

		val found = DlgpUtils.search(predicate.name, project.predicatesNames);
		if (!found) {
			error("Predicate " + predicate.name + " is not found. ",
				DlgpPackage.Literals.PREDICATE__NAME)
		}
	}
	
	@Check 
	def void checkHeadAtom(HeadAtom atom) {		
		if (atom.goal !== null) {
			val gpred = atom.goal;
			if(atom.terms.length != 3) {
				error("Exactly 3 terms is expected for a " + gpred.name + ".", DlgpPackage.Literals.HEAD_ATOM__TERMS);
			} else {
				checkIsVariable(atom.terms, 0, DlgpPackage.Literals.HEAD_ATOM__TERMS);
				checkIsVariable(atom.terms, 1, DlgpPackage.Literals.HEAD_ATOM__TERMS);
				
				val threshold = atom.terms.get(atom.terms.length - 1);
				if (!(threshold instanceof Constant || threshold instanceof Literal)) {
					error("The last term of a " + gpred.name + " must be a constant.", DlgpPackage.Literals.HEAD_ATOM__TERMS,
						atom.terms.length - 1);
				} else {
					if (threshold instanceof Literal) {
						if(threshold.value <= 0) {
							error(threshold.value + " is not a valid threshold for " + gpred.name + ", it must be > 0.", DlgpPackage.Literals.HEAD_ATOM__TERMS,
								atom.terms.length - 1);
						}
					} else if(threshold instanceof Constant) {
						if(!(threshold.label.equals(DiscretCompType.ALWAYS_KEY))
						) {
							error("Only " + DiscretCompType.ALWAYS_KEY + " are allowed as constant in a sameAs or a diffFrom.", 
								DlgpPackage.Literals.HEAD_ATOM__TERMS,
								atom.terms.length - 1
							)
						}
					}
				}
			}
		}
	}

	@Check
	def void checkAtom(Atom atom) {
		val project = DlgpUtils.getQualinkaProjectFrom(atom);

		if (atom.predicate !== null) {
			val instance = project.getPredicateInstance(DlgpUtils.removeNotFrom(atom.predicate.name));
			if(instance === null) {
				return;
			}
			if (instance instanceof Filter) {
				if (atom.terms.length != 1) {
					error("An unique term is expected for a filter.", DlgpPackage.Literals.ATOM__TERMS)
				} else {
					checkIsVariable(atom.terms, 0, DlgpPackage.Literals.ATOM__TERMS);
				}
			} else if (instance instanceof Criterion) {
				if(atom.terms.length != 3) {
					error("Exactly 3 terms is expected for a Criterion.", DlgpPackage.Literals.ATOM__TERMS);
				} else {
					checkIsVariable(atom.terms, 0, DlgpPackage.Literals.ATOM__TERMS);
					checkIsVariable(atom.terms, 1, DlgpPackage.Literals.ATOM__TERMS);
					
					val threshold = atom.terms.get(atom.terms.length - 1);
					if (!(threshold instanceof Constant || threshold instanceof Literal)) {
						error("The last term of a criterion must be a constant.", DlgpPackage.Literals.ATOM__TERMS,
							atom.terms.length - 1);
					} else {
						var res = true;
						var Object value = null;
						if (threshold instanceof Constant) {
							value = threshold.label;
							res = instance.comparisonType.check(threshold.label);
						} else if (threshold instanceof Literal) {
							value = threshold.value;
							res = instance.comparisonType.check(threshold.value);
						}
						if (!res) {
							error(value + " is not a valid threshold for this criterion.", DlgpPackage.Literals.ATOM__TERMS,
								atom.terms.length - 1);
						}
					}
				}
			}
		}
	}
	
	private def void checkIsVariable(EList<Term> terms, int index, EStructuralFeature literalType) {
		if(!(terms.get(index) instanceof Variable)) {
			error("This term must be a variable.", literalType, index);
		}
	}

}
