package toolkit;

public sealed interface Hardware permits
        Display,
        Keyboard,
        Mouse {}
