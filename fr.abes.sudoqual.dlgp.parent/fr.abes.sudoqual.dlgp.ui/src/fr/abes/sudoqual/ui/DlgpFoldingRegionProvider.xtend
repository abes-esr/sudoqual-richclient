package fr.abes.sudoqual.ui

import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionProvider
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionAcceptor
import org.eclipse.xtext.util.ITextRegion
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionAcceptorExtension


/**
 * This class is a hack to implement folding of single-line comment blocks. <br/>
 * <br/>
 * Associated with this ML_COMMENT terminal definition: <br/>
 * terminal ML_COMMENT: <br/>
 *    ('%' !('\n'|'\r')* ('\r'? '\n'))* <br/>
 *    '%' !('\n'|'\r')* ('\r'? '\n')? <br/>
 * ;<br/>
 * But, the original folding of this ML_COMMENT definition take one extra line. So, we decorate
 * the original IFoldingRegionAcceptor to remove this extra line from the foldable chunk.
 */
class DlgpFoldingRegionProvider extends DefaultFoldingRegionProvider {
	
	protected override void computeCommentFolding(IXtextDocument xtextDocument, IFoldingRegionAcceptor<ITextRegion> foldingRegionAcceptor) {
		val foldingRegionAcceptorDecorator = new FoldingRegionAcceptorDecorator(foldingRegionAcceptor); 
		super.computeCommentFolding(xtextDocument, foldingRegionAcceptorDecorator);
	}
	
	private static class FoldingRegionAcceptorDecorator implements IFoldingRegionAcceptorExtension<ITextRegion> {
		
		val IFoldingRegionAcceptor<ITextRegion> component;
		
		new(IFoldingRegionAcceptor<ITextRegion>  component) {
			this.component = component;
		}
		
		override accept(int offset, int length) {
			val newLength = length -1;
			if(newLength > 1) {
				this.component.accept(offset, newLength);
			}
		}
		
		override accept(int offset, int length, ITextRegion param) {
			val newLength = length -1;
			if(newLength > 1) {
				this.component.accept(offset, newLength, param);
			}
		}
		
		override accept(int offset, int length, boolean bool) {
			if(this.component instanceof IFoldingRegionAcceptorExtension) {
				val newLength = length -1;
				if(newLength > 1) {
					this.component.accept(offset, newLength, bool);
				}
			}
		}
		
		override accept(int offset, int length, boolean bool, ITextRegion param) {
			if(this.component instanceof IFoldingRegionAcceptorExtension) {
				val newLength = length -1;
				if(newLength > 1) {
					this.component.accept(offset, newLength, bool, param);
				}
			}
		}
		
	}
}