package example.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liuhaibo on 2018/03/06
 */
public class ReferenceQueueDemo {

    public static void main(String... args) {

        // 四个员工都有强引用
        Employee[] employees = new Employee[]{
            new Employee(0),
            new Employee(1),
            new Employee(2),
            new Employee(3)
        };

        // 两个员工（0、1）放入了set，有了第二个强引用
        Company company = new Company();
        company.addEmployee(employees[0]);
        company.addEmployee(employees[1]);

        // 用来注册被清理的对象的弱引用
        ReferenceQueue<Employee> queue = new ReferenceQueue<>();

        // 把四个员工都加上弱引用，并删了原来的第一个强引用
        List<WeakReference<Employee>> weakEmployees = new ArrayList<>();
        for (int i = 0; i < employees.length; i++) {
            // gc清理时会注册到该queue：
            weakEmployees.add(new WeakReference<>(employees[i], queue));
            employees[i] = null;
        }

        // 还没做gc，显然队列里没有注册任何对象
        System.out.println("Queue's polling returns null? " + (queue.poll() == null));

        // 开始gc，只清理掉了2、3, 0、1因为还有公司的强引用，所以依然活着
        reclaimEmployees(weakEmployees);

        // 公司把0、1也解雇了
        company.layOffAll();

        // 再次gc，0、1肯定也被清了
        reclaimEmployees(weakEmployees);

        System.out.println("=== print register queue ===");
        // 已注册的曾经指向四个员工的弱引用
        Reference<? extends Employee> employeeRef;
        do {
            employeeRef = queue.poll();
            if (employeeRef != null) {
                System.out.println(employeeRef);
            }
        } while (employeeRef != null);
    }

    private static void reclaimEmployees(List<WeakReference<Employee>> weakEmployees) {
        System.out.println("=== Run gc ===");
        System.gc();
        boolean objReclaimed = false;
        // while循环，一直到gc回收了弱引用为止
        while (!objReclaimed) {
            for (int i = 0; i < weakEmployees.size(); i++) {
                if (weakEmployees.get(i).get() == null) {
                    System.out.println(String.format("employee %s is reclaimed with ref %s", i, weakEmployees.get(i)));
                    objReclaimed = true;
                }
            }
        }

        // 删除已清理的员工的弱引用
        weakEmployees.removeIf(ref -> ref.get() == null);
        // 输出还没有被解雇的员工
        System.out.println("Fortunate Employees:");
        weakEmployees.forEach(x -> System.out.println(x.get().number + " is still alive."));
    }

    private static class Employee {
        int number;

        Employee(int i) {
            number = i;
        }
    }

    private static class Company {
        private Set<Employee> employees = new HashSet<>();

        void addEmployee(Employee e) {
            employees.add(e);
        }

        void layOffAll() {
            employees.clear();
        }
    }
}
