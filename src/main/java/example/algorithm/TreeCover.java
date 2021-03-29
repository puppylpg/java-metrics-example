package example.algorithm;


/**
 * @author liuhaibo on 2020/11/24
 */
public class TreeCover {

    public static class Node {
        int value;
        Node left, right;

        public Node(int value) {
            this.value = value;
        }
    }

    public static void main(String [] args) throws InterruptedException {
        //            8
        //    8               2
        //9       7
        Node a = new Node(8);
        a.left = new Node(8);
        a.right = new Node(2);
        a.left.left = new Node(9);
        a.left.right = new Node(7);

        //            8
        //    9               2
        Node b = new Node(8);
        b.left = new Node(9);
        b.right = new Node(2);

        Node c = new Node(8);
        c.left = new Node(9);
        c.right = new Node(2);
        c.left.left = new Node(0);

        System.out.println("a vs. b:");
        System.out.println(equal1(a, b));
        System.out.println(equal(a, b, false));
        System.out.println(equal3(a, b));
        System.out.println(equal4(a, b));

        System.out.println("c vs. b:");
        System.out.println(equal1(c, b));
        System.out.println(equal(c, b, false));
        System.out.println(equal3(c, b));
        System.out.println(equal4(c, b));
    }

    // 不科学
    public static boolean equal1(Node a, Node b) {
        if (b == null) {
            return true;
        }

        if (a == null) {
            return false;
        }

        if (a.value == b.value) {
            if (equal1(a.left, b.left) && equal1(a.right, b.right)) {
                return true;
            } else {
                return (equal1(a.left, b)) || equal1(a.right, b);
            }
        } else {
            return (equal1(a.left, b)) || equal1(a.right, b);
        }
    }

    // 太复杂
    private static boolean equal(Node a, Node b, boolean being) {
        if (b == null) {
            return true;
        }

        if (a == null) {
            return false;
        }

        // 如果是在进行校验对照，节点还不同，那可以结束了
        if (being && a.value != b.value) {
            return false;
        }

        if (a.value == b.value) {
            if (equal(a.left, b.left, true) && equal(a.right, b.right, true)) {
                return true;
            }
        }
        return (equal(a.left, b, false)) || equal(a.right, b, false);
    }

    // easy & simple
    public static boolean equal3(Node a, Node b) {
        if (a.value == b.value) {
            return dfs(a, b);
        } else {
            return (equal3(a.left, b) || equal3(a.right, b));
        }
    }

    public static boolean dfs(Node r1, Node r2) {
        if (r2 == null) {
            return true;
        }
        if (r1 == null) {
            return false;
        }
        return r1.value==r2.value && dfs(r1.left, r2.left) && dfs(r1.right, r2.right);
    }

    // simplest
    public static boolean equal4(Node a, Node b) {
        return dfs(a, b)
                || ((a.left != null && equal4(a.left, b))
                || (a.right != null && equal4(a.right, b)));
    }
}
