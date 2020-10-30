package edu.mermet.tp8.fenetres;

import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author brunomermet
 */
public abstract class AbstractFenetreInterne extends JInternalFrame {
    private Action action;
    public AbstractFenetreInterne(Action monAction, String nom) {
        super(nom, true,true,true,true);
        action = monAction;
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.addInternalFrameListener(new EcouteurFenetre());

    }
    
    private class EcouteurFenetre extends InternalFrameAdapter {
        @Override
        public void internalFrameClosing(InternalFrameEvent ife) {
            action.setEnabled(true);
        }
    }
}
