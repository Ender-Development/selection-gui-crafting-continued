package io.enderdev.selectionguicrafting.registry.util;

import java.util.ArrayList;
import java.util.List;

public class Validation {
    private boolean valid;
    private final List<String> msg;

    public Validation() {
        this.valid = true;
        this.msg = new ArrayList<>();
    }

    public void error(String msg) {
        this.valid = false;
        this.msg.add(msg);
    }

    public boolean valid() {
        return this.valid;
    }

    public String msg() {
        return msg.stream().reduce("", (a, b) -> a + "\n" + b);
    }

    public List<String> listMsg() {
        return msg;
    }
}
