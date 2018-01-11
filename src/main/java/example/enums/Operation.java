package example.enums;

import lombok.Getter;

/**
 * @author liuhaibo on 2017/12/27
 */
public enum Operation {

    PLUS("I am plus"),
    MINUS("I am minus"),
    TIMES("I am times"),
    DIVIDE("I am divide"),
    UNKNOWN("I am trouble-maker");

    @Getter
    private String name;

    Operation(String name) {
        this.name = name;
    }

    public double calculate(double x, double y) {
        // 'this' in Enum is an instantiated object,
        // in this case such as: PLUS/MINUS/TIMES/DIVEDE/UNKNOWN
        switch (this) {
            case PLUS:
                return x + y;
            case MINUS:
                return x - y;
            case TIMES:
                return x * y;
            case DIVIDE:
                return x / y;
            default:
                throw new AssertionError("Unknown operation: " + this + " , name is: " + this.name);
        }
    }

    public static void main(String [] args) {
        // use for normal method invoke
        System.out.println(Operation.PLUS.calculate(4, 5));

        // values()
        for (Operation op : Operation.values()) {
            System.out.println(op);
        }

        // valueOf(String): get a Enum object by string
        // 必须和枚举类型字面量一样，也就是得大写
        Operation op = Operation.valueOf("times".toUpperCase());
        System.out.println(op + " <=> " + op.getName());

//        System.out.println(Operation.UNKNOWN.calculate(4, 5));
    }
}
