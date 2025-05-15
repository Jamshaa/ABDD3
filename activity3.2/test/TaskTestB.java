// TaskTestB.java
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TaskTestB {

    @Test
    void simpleTaskB_notifies() {
        SimpleTaskB t = new SimpleTaskB(new BigDecimal("10.00"));
        AtomicReference<BigDecimal> oldV = new AtomicReference<>();
        AtomicReference<BigDecimal> newV = new AtomicReference<>();
        t.addPropertyChangeListener(evt -> {
            oldV.set((BigDecimal) evt.getOldValue());
            newV.set((BigDecimal) evt.getNewValue());
        });
        t.changeCost(new BigDecimal("12.00"));
        assertEquals(new BigDecimal("10.00"), oldV.get());
        assertEquals(new BigDecimal("12.00"), newV.get());
    }

    @Test
    void compositeTaskB_recalculates() {
        SimpleTaskB t1 = new SimpleTaskB(new BigDecimal("5.00"));
        SimpleTaskB t2 = new SimpleTaskB(new BigDecimal("7.50"));
        CompositeTaskB c = new CompositeTaskB(List.of(t1, t2));
        assertEquals(new BigDecimal("12.50"), c.costInEuros());
        t2.changeCost(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("15.00"), c.costInEuros());
    }

    @Test
    void nestedCompositeTaskB_propagates() {
        SimpleTaskB leaf = new SimpleTaskB(new BigDecimal("3.00"));
        CompositeTaskB inner = new CompositeTaskB(List.of(leaf));
        CompositeTaskB outer = new CompositeTaskB(List.of(inner));
        AtomicReference<BigDecimal> cap = new AtomicReference<>();
        outer.addPropertyChangeListener(evt -> cap.set((BigDecimal) evt.getNewValue()));
        leaf.changeCost(new BigDecimal("4.00"));
        assertEquals(new BigDecimal("4.00"), outer.costInEuros());
        assertEquals(new BigDecimal("4.00"), cap.get());
    }

    @Test
    void compositeTaskB_cannotChangeDirectly() {
        CompositeTaskB c = new CompositeTaskB(List.of(new SimpleTaskB(new BigDecimal("2.00"))));
        assertThrows(UnsupportedOperationException.class,
                () -> c.changeCost(new BigDecimal("5.00")));
    }
}
