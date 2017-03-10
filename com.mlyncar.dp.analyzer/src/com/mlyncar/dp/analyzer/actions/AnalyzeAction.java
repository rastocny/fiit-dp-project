/*
 * Copyright 2017 Andrej Mlyncar <a.mlyncar@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mlyncar.dp.analyzer.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

import com.mlyncar.dp.analyzer.code.impl.KdmAnalyzer;
import com.mlyncar.dp.analyzer.exception.SourceCodeAnalyzerException;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 *
 * @see IWorkbenchWindowActionDelegate
 */
public class AnalyzeAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /**
     * The constructor.
     */
    public AnalyzeAction() {
    }

    /**
     * The action has been activated. The argument of the method represents the
     * 'real' action sitting in the workbench UI.
     *
     * @see IWorkbenchWindowActionDelegate#run
     */
    public void run(IAction action) {
        KdmAnalyzer kdmAnalyer = new KdmAnalyzer();
        try {
            kdmAnalyer.extractSequenceDiagramFromMain();
            MessageDialog.openInformation(
                    window.getShell(),
                    "Synchronization Tool",
                    "KDM successfully generated");
        } catch (SourceCodeAnalyzerException ex) {
            MessageDialog.openInformation(
                    window.getShell(),
                    "Analysis Failed",
                    ex.getMessage());
        }

    }

    /**
     * Selection in the workbench has been changed. We can change the state of
     * the 'real' action here if we want, but this can only happen after the
     * delegate has been created.
     *
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * We can use this method to dispose of any system resources we previously
     * allocated.
     *
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    public void dispose() {
    }

    /**
     * We will cache window object in order to be able to provide parent shell
     * for the message dialog.
     *
     * @see IWorkbenchWindowActionDelegate#init
     */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}
