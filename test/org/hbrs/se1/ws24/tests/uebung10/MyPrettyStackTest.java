package org.hbrs.se1.ws24.tests.uebung10;

import org.hbrs.se1.ws24.exercises.uebung10.MyPrettyStack;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MyPrettyStackTest {

    private MyPrettyStack<Integer> stack;
    private int MAX_SIZE = 4;

    @BeforeEach
    void setUp() {
        assertTrue(MAX_SIZE > 0);
        stack = new MyPrettyStack<>(MAX_SIZE);
    }

    @Test
    void testEmpty() {
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPopOnEmptyStack() {
        assertTrue(stack.isEmpty());
        assertThrows(IllegalStateException.class, () -> stack.pop());
    }

    @Test
    void testPartiallyFilledState() {
        stack.push(12);
        assertEquals(1, stack.size());
        assertEquals(12, stack.top());

        stack.push(32);
        assertEquals(2, stack.size());
        assertEquals(32, stack.top());
    }

    @Test
    void testIsFull() {
        assertFalse(stack.isFull());
        for(int i = 0; i < MAX_SIZE; i++) {
            stack.push(i);
        }
        assertTrue(stack.isFull());
    }

    @Test
    void testFullStack() {
        stack.push(12);
        stack.push(32);
        stack.push(112);
        stack.push(1211);

        assertEquals(MAX_SIZE, stack.size());

        assertThrows(IllegalStateException.class, () -> stack.push(934));
    }

    @Test
    void testPop() {
        stack.push(12);
        stack.push(32);

        assertEquals(2, stack.size());
        assertEquals(32, stack.top());
        stack.pop();
        assertEquals(1, stack.size());
        assertEquals(12, stack.top());
    }

    @Test
    void testNull() {
        assertThrows(NullPointerException.class, () -> stack.push(null));
    }

    @Test
    void testStateTransitions() {
        assertTrue(stack.isEmpty());
        assertThrows(IllegalStateException.class, () -> stack.top());
        assertThrows(IllegalStateException.class, () -> stack.pop());

        stack.push(12);
        assertEquals(1, stack.size());

        stack.push(32);
        assertEquals(2, stack.size());

        stack.push(1);
        assertEquals(3, stack.size());

        stack.push(47);
        assertEquals(4, stack.size());

        assertTrue(stack.isFull());
        assertThrows(IllegalStateException.class, () -> stack.push(911));
        assertEquals(4, stack.size());

        stack.pop();
        assertEquals(3, stack.size());

        stack.pop();
        assertEquals(2, stack.size());

        stack.pop();
        assertEquals(1, stack.size());

        stack.pop();
        assertEquals(0, stack.size());

        assertTrue(stack.isEmpty());
        assertThrows(IllegalStateException.class, () -> stack.pop());
    }
}
