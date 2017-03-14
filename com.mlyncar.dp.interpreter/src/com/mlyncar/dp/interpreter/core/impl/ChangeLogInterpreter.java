package com.mlyncar.dp.interpreter.core.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import com.mlyncar.dp.comparison.entity.Change;
import com.mlyncar.dp.interpreter.core.ChangeInterpreter;
import com.mlyncar.dp.interpreter.exception.InterpreterException;

public class ChangeLogInterpreter implements ChangeInterpreter {

    private final PrintWriter fileWriter;

    public ChangeLogInterpreter(String changeLogFileName) throws InterpreterException {
        FileWriter fw;
        try {
            fw = new FileWriter(changeLogFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            fileWriter = new PrintWriter(bw);
        } catch (IOException ex) {
            throw new InterpreterException("Unable to initialize changelog file", ex);
        }

    }

    @Override
    public void interpretChange(Change change) throws InterpreterException {
        Date date = new Date();
        String outputLine = date.toString() + ": " + change.getChangeType().getCode() + " = " + change.getNewValue();
        fileWriter.println(outputLine);
        fileWriter.close();
    }

    @Override
    public void interpretChanges(List<Change> changes)
            throws InterpreterException {
        // TODO Auto-generated method stub

    }

}