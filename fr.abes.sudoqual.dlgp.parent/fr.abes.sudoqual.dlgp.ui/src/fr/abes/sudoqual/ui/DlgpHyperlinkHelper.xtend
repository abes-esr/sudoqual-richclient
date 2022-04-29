package fr.abes.sudoqual.ui

import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor
import javax.inject.Inject
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import fr.abes.sudoqual.DlgpUtils
import fr.abes.sudoqual.dlgp.Predicate
import org.eclipse.jface.text.Region
import org.eclipse.xtext.nodemodel.util.NodeModelUtils

class DlgpHyperlinkHelper extends HyperlinkHelper {
	
	@Inject
	EObjectAtOffsetHelper eObjectAtOffsetHelper;
	
	override void createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
		val object = eObjectAtOffsetHelper.resolveElementAt(resource, offset);
		if (object === null)
			return;
					
		if(object instanceof Predicate) {
			// get region
			val node = NodeModelUtils.findLeafNodeAtOffset(resource.parseResult.rootNode, offset);
			if (node !== null) {
				val region = new Region(node.offset, node.length);
				val project = DlgpUtils.getQualinkaProjectFrom(object);
				val predicateName = DlgpUtils.removeNotFrom(object.name);
				acceptor.accept(new JavaElementHyperlink(region, project.getPredicateType(predicateName)));
			}	
	    } else {
	    	super.createHyperlinksByOffset( resource,  offset,  acceptor);
	    }

	}
}