package fr.abes.sudoqual.ui

import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor
import org.eclipse.swt.SWT
import org.eclipse.xtext.ui.editor.utils.TextStyle
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration
import org.eclipse.swt.graphics.RGB

class DlgpHighlightingConfiguration implements IHighlightingConfiguration {
	
	public static val DIM_IDENT = "dimIndent";
    public static val DIFF_FROM = "diffFrom";
	public static val SAME_AS = "sameAs";
	
	public static val KEYWORD_ID = "keyword"
	public static val COMMENT_ID = "comment"
	public static val STRING_ID = "string"
	public static val NUMBER_ID = "number"
	public static val DEFAULT_ID = "default"
	
	override void configure(IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword", keywordTextStyle());
		acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment", commentTextStyle());
		acceptor.acceptDefaultHighlighting(STRING_ID, "String", stringTextStyle());
		acceptor.acceptDefaultHighlighting(NUMBER_ID, "Number", numberTextStyle());
		acceptor.acceptDefaultHighlighting(DEFAULT_ID, "Default", defaultTextStyle());
		
		acceptor.acceptDefaultHighlighting(DIM_IDENT, "Dimension identifier", dimIdentTextStyle());
		acceptor.acceptDefaultHighlighting(DIFF_FROM, "diffFrom", diffFromTextStyle());
		acceptor.acceptDefaultHighlighting(SAME_AS, "sameAs", sameAsTextStyle());
		
	}

	def TextStyle defaultTextStyle() {
		return new TextStyle();
	}
	
	def TextStyle dimIdentTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}
	
	def TextStyle diffFromTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(160, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}
	
	def TextStyle sameAsTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 112, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}
	
	def TextStyle numberTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(125, 125, 125));
		return textStyle;
	}

	def TextStyle stringTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(42, 0, 255));
		return textStyle;
	}

	def TextStyle commentTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(63, 127, 95));
		return textStyle;
	}

	def TextStyle keywordTextStyle() {
		val textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}


	
}