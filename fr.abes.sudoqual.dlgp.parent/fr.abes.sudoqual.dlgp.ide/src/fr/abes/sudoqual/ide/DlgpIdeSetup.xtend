/*
 * generated by Xtext 2.17.1
 */
package fr.abes.sudoqual.ide

import com.google.inject.Guice
import fr.abes.sudoqual.DlgpRuntimeModule
import fr.abes.sudoqual.DlgpStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class DlgpIdeSetup extends DlgpStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new DlgpRuntimeModule, new DlgpIdeModule))
	}
	
}
