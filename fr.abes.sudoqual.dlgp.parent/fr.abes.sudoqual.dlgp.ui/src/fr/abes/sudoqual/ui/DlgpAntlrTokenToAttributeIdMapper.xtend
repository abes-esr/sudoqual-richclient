package fr.abes.sudoqual.ui

import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper
import javax.inject.Singleton
import org.eclipse.xtext.ide.editor.syntaxcoloring.HighlightingStyles

@Singleton
class DlgpAntlrTokenToAttributeIdMapper extends DefaultAntlrTokenToAttributeIdMapper {
	static val char quote = '\'';
	
	override protected String calculateId(String tokenName, int tokenType) {
		if(tokenName.charAt(0) === quote) {
			return HighlightingStyles.KEYWORD_ID;
		}
		if("RULE_DIFF_FROM".equals(tokenName)) {
			return DlgpHighlightingConfiguration.DIFF_FROM;
		}
		if("RULE_SAME_AS".equals(tokenName)) {
			return DlgpHighlightingConfiguration.SAME_AS;
		}
		if("RULE_DIM_IDENT".equals(tokenName)) {
			return DlgpHighlightingConfiguration.DIM_IDENT;
		}
		if("RULE_INTEGER".equals(tokenName)) {
			return HighlightingStyles.NUMBER_ID;
		}
		return super.calculateId(tokenName, tokenType);
	}
}