/*
 * Created on 23/08/2006
 */

package br.org.archimedes.redo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.org.archimedes.controller.commands.PutOrRemoveElementCommand;
import br.org.archimedes.exceptions.IllegalActionException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.interfaces.Command;
import br.org.archimedes.interfaces.UndoableCommand;
import br.org.archimedes.model.Drawing;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Point;
import br.org.archimedes.undo.UndoCommand;

/**
 * Belongs to package com.tarantulus.archimedes.model.commands.
 * 
 * @author night
 */
public class RedoTest {

    private Drawing drawing;

    private UndoCommand undo;

    private List<Command> commands;

    private RedoCommand redo;


    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp () throws Exception {

        drawing = new Drawing("Drawing");
        commands = new ArrayList<Command>();
        Point p1 = new Point(0, 10);
        Point p2 = new Point(60, 48);
        Element element = EasyMock.createMock(Element.class);
        Collection<Point> points = new ArrayList<Point>();
        points.add(p1);
        points.add(p2);
        Map<Element, Collection<Point>> pointsToMove = new HashMap<Element, Collection<Point>>();
        pointsToMove.put(element, points);
        commands.add(new PutOrRemoveElementCommand(element, false));
        commands.add(EasyMock.createMock(UndoableCommand.class));
        commands.add(EasyMock.createMock(Command.class));
        commands.add(new PutOrRemoveElementCommand(element, true));

        undo = new UndoCommand();
        redo = new RedoCommand();
    }

    /*
     * @see TestCase#tearDown()
     */
    @After
    public void tearDown () throws Exception {

        drawing = null;
        commands = null;
        undo = null;
        redo = null;
    }

    /*
     * Test method for
     * 'com.tarantulus.archimedes.model.commands.RedoCommand.doIt(Drawing)'
     */
    @Test
    public void testDoIt () {

        try {
            redo.doIt(null);
            Assert.fail("Should throw a NullArgumentException");
        }
        catch (IllegalActionException e) {
            Assert.fail("Should throw a NullArgumentException");
        }
        catch (NullArgumentException e) {}

        try {
            redo.doIt(drawing);
            Assert.fail("Should throw an IllegalActionException");
        }
        catch (IllegalActionException e) {}
        catch (NullArgumentException e) {
            Assert.fail("Should throw an IllegalActionException");
        }

        Stack<UndoableCommand> undoHistory = drawing.getUndoHistory();
        Stack<UndoableCommand> redoHistory = drawing.getRedoHistory();
        try {
            drawing.execute(commands);
        }
        catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
        Assert.assertEquals("The undo history size is wrong.", 3, undoHistory
                .size());
        Assert.assertEquals("The first element in the undo history is wrong",
                commands.get(commands.size() - 1), undoHistory.peek());
        Assert.assertEquals("The redo history size is wrong.", 0, redoHistory
                .size());

        try {
            redo.doIt(drawing);
            Assert.fail("Should throw an IllegalActionException");
        }
        catch (IllegalActionException e) {}
        catch (NullArgumentException e) {
            Assert.fail("Should throw an IllegalActionException");
        }
        Assert.assertEquals("The undo history size is wrong.", 3, undoHistory
                .size());
        Assert.assertEquals("The first element in the undo history is wrong",
                commands.get(commands.size() - 1), undoHistory.peek());
        Assert.assertEquals("The redo history size is wrong.", 0, redoHistory
                .size());

        try {
            undo.doIt(drawing);
            undo.doIt(drawing);
            undo.doIt(drawing);
            redo.doIt(drawing);
        }
        catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
        Assert.assertEquals("The undo history size is wrong.", 1, undoHistory
                .size());
        Assert.assertEquals("The first element in the undo history is wrong",
                commands.get(commands.size() - 4), undoHistory.peek());
        Assert.assertEquals("The redo history size is wrong.", 2, redoHistory
                .size());
        Assert.assertEquals("The first element in the redo history is wrong",
                commands.get(commands.size() - 3), redoHistory.peek());

        try {
            redo.doIt(drawing);
        }
        catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
        Assert.assertEquals("The undo history size is wrong.", 2, undoHistory
                .size());
        Assert.assertEquals("The first element in the undo history is wrong",
                commands.get(commands.size() - 3), undoHistory.peek());
        Assert.assertEquals("The redo history size is wrong.", 1, redoHistory
                .size());
        Assert.assertEquals("The first element in the redo history is wrong",
                commands.get(commands.size() - 1), redoHistory.peek());

        try {
            redo.doIt(drawing);
        }
        catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
        Assert.assertEquals("The undo history size is wrong.", 3, undoHistory
                .size());
        Assert.assertEquals("The first element in the undo history is wrong",
                commands.get(commands.size() - 1), undoHistory.peek());
        Assert.assertEquals("The redo history size is wrong.", 0, redoHistory
                .size());

        try {
            redo.doIt(drawing);
            Assert.fail("Should throw an IllegalActionException");
        }
        catch (IllegalActionException e) {}
        catch (NullArgumentException e) {
            Assert.fail("Should throw an IllegalActionException");
        }
        Assert.assertEquals("The undo history size is wrong.", 3, undoHistory
                .size());
        Assert.assertEquals("The first element in the undo history is wrong",
                commands.get(commands.size() - 1), undoHistory.peek());
        Assert.assertEquals("The redo history size is wrong.", 0, redoHistory
                .size());
    }

}
