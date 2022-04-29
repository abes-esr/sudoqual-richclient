package fr.abes.sudoqual.ui;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.PartInitException;

public class JavaElementHyperlink implements IHyperlink {

    private final IRegion fUrlRegion;
    private final IType type;

    public JavaElementHyperlink(IRegion urlRegion, IType type) {
        fUrlRegion = urlRegion;
        this.type = type;
    }

    @Override
    public IRegion getHyperlinkRegion() {
        return fUrlRegion;
    }

    @Override
    public String getTypeLabel() {
        return null;
    }

    @Override
    public String getHyperlinkText() {
        return null;
    }

    @Override
    public void open() {
    	ICompilationUnit cu = type.getCompilationUnit();
        try {
			JavaUI.openInEditor(cu);
        } catch (PartInitException | JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
}